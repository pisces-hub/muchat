package io.pisceshub.muchat.server.listener.zk;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.pisceshub.muchat.common.core.contant.AppConst;
import io.pisceshub.muchat.common.core.enums.NetProtocolEnum;
import io.pisceshub.muchat.server.config.properties.AppConfigInfo;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
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

    @SneakyThrows
    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        client = CuratorFrameworkFactory.newClient(appConfigInfo.getZk().getAddress(),
                new RetryNTimes(10, 5000));
        client.start();

        //监听子节点
        childrenCacheListener(client,NetProtocolEnum.TCP);
        childrenCacheListener(client,NetProtocolEnum.WS);
        this.addNode(NetProtocolEnum.TCP,client.getChildren().forPath(buildZkPath(NetProtocolEnum.TCP)));
        this.addNode(NetProtocolEnum.WS,client.getChildren().forPath(buildZkPath(NetProtocolEnum.WS)));
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

    private void childrenCacheListener(CuratorFramework client,NetProtocolEnum netProtocolEnum) throws Exception {
        String listenerPath = buildZkPath(netProtocolEnum);
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, listenerPath,true);
        PathChildrenCacheListener pathChildrenCacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                ChildData childData = event.getData();
                if (childData != null) {
                    String path = childData.getPath().substring(listenerPath.length()+1);
                    if(PathChildrenCacheEvent.Type.CHILD_ADDED==event.getType()){
                        addNode(netProtocolEnum,Arrays.asList(path));
                        log.info("新的机器上线成功：ws:{},tcp:{}", AppConst.WS_NODES, AppConst.TCP_NODES);
                    }else if(PathChildrenCacheEvent.Type.CHILD_REMOVED==event.getType()){
                        removeNode(netProtocolEnum,path);
                        log.info("机器下线成功：ws:{},tcp:{}", AppConst.WS_NODES, AppConst.TCP_NODES);
                    }
                }
            }
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        pathChildrenCache.start();
    }

    public void removeNode(NetProtocolEnum netProtocolEnum,String node){
        if(StrUtil.isBlank(node)){
            return;
        }
        if(NetProtocolEnum.TCP.equals(netProtocolEnum)){
            AppConst.TCP_NODES.remove(node);
        }
        if(NetProtocolEnum.WS.equals(netProtocolEnum)){
            AppConst.WS_NODES.remove(node);
        }
    }

    public void addNode(NetProtocolEnum netProtocolEnum,List<String> nodes){
        if(CollUtil.isEmpty(nodes)){
            return;
        }
        if(NetProtocolEnum.TCP.equals(netProtocolEnum)){
            AppConst.TCP_NODES.addAll(nodes);
        }
        if(NetProtocolEnum.WS.equals(netProtocolEnum)){
            AppConst.WS_NODES.addAll(nodes);
        }
    }
}