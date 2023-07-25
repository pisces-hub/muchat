package io.pisceshub.muchat.server.tcp.listener.zk;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.pisceshub.muchat.common.core.enums.NetProtocolEnum;
import io.pisceshub.muchat.common.core.utils.MixUtils;
import io.pisceshub.muchat.server.config.properties.AppConfigInfo;
import io.pisceshub.muchat.server.core.NodeContainer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @description:
 * @author: xiaochangbai
 * @date: 2023/6/12 17:13
 */
@Slf4j
@Component
public class ConnectorNodeListener implements ApplicationListener<ApplicationStartedEvent> {


    @Autowired
    private AppConfigInfo appConfigInfo;

    private CuratorFramework client;

    @Autowired
    private List<INodeUpdateNodeEventListener> nodeUpdateNodeEventListeners;

    @Autowired
    private NodeContainer nodeContainer;

    @SneakyThrows
    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        client = CuratorFrameworkFactory.newClient(appConfigInfo.getZk().getAddress(),
                new RetryNTimes(10, 5000));
        client.start();

        for(NetProtocolEnum protocolEnum:NetProtocolEnum.values()){
            String zkProtocolPath = buildZkPath(protocolEnum);
            //监听变更
            childrenCacheListener(zkProtocolPath,protocolEnum);
            //主动拉取一遍最新数据
//            List<String> paths = client.getChildren().forPath(zkProtocolPath);
//            if(CollUtil.isEmpty(paths)){
//                continue;
//            }
//            List<NodeContainer.WNode> nodeList = paths.stream().map(e->{return convertZkNodeToWNote(e,protocolEnum);})
//                    .filter(Objects::nonNull).collect(Collectors.toList());
//            this.addNode(protocolEnum,nodeList);
        }
    }


    private NodeContainer.WNode convertZkNodeToWNote(ChildData childData,String parentPath,NetProtocolEnum protocolEnum){
        String zkPath = childData.getPath().substring(parentPath.length()+1);
        if(StrUtil.isEmpty(zkPath)){
            return null;
        }
        String[] split = zkPath.split(":");
        if(split.length<2){
            return null;
        }
        NodeContainer.WNode wNode = NodeContainer.WNode.builder()
                .protocolEnum(protocolEnum)
                .ip(split[0])
                .port(Integer.valueOf(split[1]))
                .registerTime(MixUtils.BytesToLong(childData.getData()))
                .build();
        return wNode;
    }

    private String buildZkPath(NetProtocolEnum netProtocolEnum) throws Exception {
        String zkPath = appConfigInfo.getZk().getPath()+"/"+netProtocolEnum;
        String path = "";
        for(String p:zkPath.split("/")){
            if(StrUtil.isBlank(p)){
                continue;
            }
            path +="/"+p;
            if(client.checkExists().forPath(path)==null){
                client.create().forPath(path);
            }
        }
        return zkPath;
    }

    private void childrenCacheListener(String listenerPath,NetProtocolEnum netProtocolEnum) throws Exception {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, listenerPath,true);
        PathChildrenCacheListener pathChildrenCacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                ChildData childData = event.getData();
                if (childData != null) {
                    if(PathChildrenCacheEvent.Type.CHILD_ADDED==event.getType()){
                        NodeContainer.WNode wNode = convertZkNodeToWNote(childData,listenerPath,netProtocolEnum);
                        if(wNode!=null){
                            addNode(netProtocolEnum, Collections.singletonList(wNode));
                        }
                    }else if(PathChildrenCacheEvent.Type.CHILD_REMOVED==event.getType()){
                        NodeContainer.WNode wNode = convertZkNodeToWNote(childData,listenerPath,netProtocolEnum);
                        removeNode(netProtocolEnum,wNode);

                    }
                }
            }
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        pathChildrenCache.start();
    }

    public void removeNode(NetProtocolEnum netProtocolEnum,NodeContainer.WNode node){
        if(node==null){
            return;
        }
        nodeContainer.remove(netProtocolEnum,node);
        log.info("机器下线成功：{}", node);

        //发布事件
        if(CollUtil.isNotEmpty(nodeUpdateNodeEventListeners)){
            nodeUpdateNodeEventListeners.forEach(e->{
                e.delete(netProtocolEnum,node);
                e.list(netProtocolEnum,nodeContainer.list(netProtocolEnum));
            });
        }
    }

    public void addNode(NetProtocolEnum netProtocolEnum,List<NodeContainer.WNode> nodes){
        if(CollUtil.isEmpty(nodes)){
            return;
        }
        nodeContainer.add(netProtocolEnum,nodes);
        log.info("新的机器上线成功：{}",nodes);

        //发布事件
        if(CollUtil.isNotEmpty(nodeUpdateNodeEventListeners)){
            nodeUpdateNodeEventListeners.forEach(e->{
                e.add(netProtocolEnum,nodes);
                e.list(netProtocolEnum,nodeContainer.list(netProtocolEnum));
            });
        }
    }
}