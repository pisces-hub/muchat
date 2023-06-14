package io.xiaochangbai.muchat.connector.listener;

import cn.hutool.core.util.StrUtil;
import io.xiaochangbai.muchat.common.core.enums.NetProtocolEnum;
import io.xiaochangbai.muchat.common.core.utils.MixUtils;
import io.xiaochangbai.muchat.connector.config.AppConfigProperties;
import io.xiaochangbai.muchat.connector.listener.event.NodeRegisterEvent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author XDD
 * @project muchat
 * @date 2021/7/23
 * @description Good Good Study,Day Day Up.
 */
@Component
@Slf4j
public class NodeRegisterListener {

    @Autowired
    private AppConfigProperties appConfigProperties;

    private CuratorFramework client;

    @SneakyThrows
    @EventListener(classes = NodeRegisterEvent.class)
    public void onApplicationEvent(NodeRegisterEvent event) {
        if(event==null){
            return;
        }
        if(client==null || !client.isStarted()){
            synchronized (this){
                if(client==null || !client.isStarted()){
                    client = CuratorFrameworkFactory.newClient(appConfigProperties.getZk().getAddress(),
                            new RetryNTimes(10, 5000));
                    client.start();
                }
            }
        }

        this.registerEphemeralNode(event.getNetProtocolEnum(),event.getPort());

    }

    private boolean registerEphemeralNode(NetProtocolEnum protocolEnum, Integer port) throws Exception {

        String path = appConfigProperties.getZk().getPath()+"/"+protocolEnum;

        String[] paths = path.split("/");
        String parentPath = "";
        for (String p:paths){
            if(StrUtil.isBlank(p)){
                continue;
            }
            parentPath+="/"+p;
            if(client.checkExists().forPath(parentPath)==null){
                client.create().forPath(parentPath);
            }
        }


        String info = buildNodeInfo(port);
        String nodePath = path+"/"+info;
        if(client.checkExists().forPath(nodePath)!=null){
            client.delete().forPath(nodePath);
        }
        client.create().withMode(CreateMode.EPHEMERAL).forPath(nodePath);
        log.info("zk注册完毕,nodePath:{}",nodePath);

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            try {
                client.delete().forPath(nodePath);
                log.info("删除节点成功,{}",nodePath);
            } catch (Exception e) {
                log.info("删除节点移除,",e);
                throw new RuntimeException(e);
            }
        }));

        return true;
    }

    private String buildNodeInfo(Integer port) throws UnknownHostException {
        String ip = appConfigProperties.getIp();
        if(StrUtil.isBlank(ip)){
            ip = MixUtils.getInet4Address();
        }
        return ip+":"+port;
    }
}
