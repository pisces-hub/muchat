package io.pisceshub.muchat.connector.listener;

import cn.hutool.core.util.StrUtil;
import io.pisceshub.muchat.common.core.enums.NetProtocolEnum;
import io.pisceshub.muchat.common.core.utils.MixUtils;
import io.pisceshub.muchat.connector.config.AppConfigProperties;
import io.pisceshub.muchat.connector.listener.event.NodeRegisterEvent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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

    @Autowired
    private CuratorFramework client;

    @SneakyThrows
    @EventListener(classes = NodeRegisterEvent.class)
    public void onApplicationEvent(NodeRegisterEvent event) {

        NetProtocolEnum protocolEnum = event.getNetProtocolEnum();
        Integer port = event.getPort();

        String protocolPath = appConfigProperties.getZk().getPath()+"/"+protocolEnum;
        checkZkPath(protocolPath);


        String nodePath = protocolPath+"/"+buildNodeInfo(port);
        delPath(nodePath);

        client.create().withMode(CreateMode.EPHEMERAL).forPath(nodePath,MixUtils.LongToBytes(event.getRegisterTime()));
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

    }

    /**
     * 删除path
     * @param nodePath
     * @throws Exception
     */
    private void delPath(String nodePath) throws Exception {
        if(client.checkExists().forPath(nodePath)!=null){
            client.delete().forPath(nodePath);
        }
    }

    /**
     * 检查路径是否存在，不存在就创建
     * @param zkPath
     * @throws Exception
     */
    private void checkZkPath(String zkPath) throws Exception {
        String[] paths = zkPath.split("/");
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
    }


    private String buildNodeInfo(Integer port){
        String ip = appConfigProperties.getIp();
        if(StrUtil.isBlank(ip)){
            ip = MixUtils.getInet4Address();
        }
        return ip+":"+port;
    }
}
