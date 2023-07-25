package io.pisceshub.muchat.connector.remote.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.timeout.IdleStateHandler;
import io.pisceshub.muchat.common.core.contant.AppConst;
import io.pisceshub.muchat.common.core.utils.MixUtils;
import io.pisceshub.muchat.connector.config.AppConfigProperties;
import io.pisceshub.muchat.connector.remote.IMServer;
import io.pisceshub.muchat.connector.remote.netty.factory.NettyEventLoopFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: xiaochangbai
 * @date: 2023/7/25 16:50
 */
@Slf4j
public abstract class AbstractRemoteServer implements IMServer {

    protected ServerBootstrap bootstrap;
    protected EventLoopGroup bossGroup;
    protected EventLoopGroup workGroup;
    protected volatile boolean ready = false;

    @Autowired
    protected AppConfigProperties appConfigProperties;

    public AbstractRemoteServer(){
        bootstrap = new ServerBootstrap();
        bossGroup = NettyEventLoopFactory.eventLoopGroup(1);
        workGroup = NettyEventLoopFactory.eventLoopGroup(Math.min(Runtime.getRuntime().availableProcessors() + 1, 32));
    }


    @Override
    public boolean enable() {
        AppConfigProperties.TcpNode tcpNode = nodeInfo();
        return tcpNode != null && tcpNode.getEnable();
    }

    protected abstract AppConfigProperties.TcpNode nodeInfo();


    protected void addPipeline(ChannelPipeline pipeline){
        pipeline.addLast(new IdleStateHandler(0, 0, AppConst.ONLINE_TIMEOUT_SECOND, TimeUnit.SECONDS));
        pipeline.addLast(new IMChannelHandler());
    }

    protected Integer port(){
        AppConfigProperties.TcpNode tcpNode = nodeInfo();
        Integer port = tcpNode.getPort();
        if (port == null || port < 1) {
            port = MixUtils.findAvailablePort();
            tcpNode.setPort(port);
        }
        return port;
    }


    @Override
    public void stop() {
        if (bossGroup != null && !bossGroup.isShuttingDown() && !bossGroup.isShutdown()) {
            bossGroup.shutdownGracefully();
        }
        if (workGroup != null && !workGroup.isShuttingDown() && !workGroup.isShutdown()) {
            workGroup.shutdownGracefully();
        }
        this.ready = false;
        log.error("tcp server 停止");
    }

}
