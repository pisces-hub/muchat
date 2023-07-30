package io.pisceshub.muchat.connector.remote.netty.tcp;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;
import io.pisceshub.muchat.common.core.contant.AppConst;
import io.pisceshub.muchat.common.core.enums.NetProtocolEnum;
import io.pisceshub.muchat.common.core.utils.SpringContextHolder;
import io.pisceshub.muchat.connector.config.AppConfigProperties;
import io.pisceshub.muchat.connector.listener.event.NodeRegisterEvent;
import io.pisceshub.muchat.connector.remote.netty.AbstractRemoteServer;
import io.pisceshub.muchat.connector.remote.netty.IMChannelHandler;
import io.pisceshub.muchat.connector.remote.netty.factory.NettyEventLoopFactory;
import io.pisceshub.muchat.connector.remote.netty.tcp.endecode.MessageProtocolDecoder;
import io.pisceshub.muchat.connector.remote.netty.tcp.endecode.MessageProtocolEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * TCP服务器
 */
@Slf4j
@Component
public class TcpSocketServer extends AbstractRemoteServer {

    @Autowired
    private AppConfigProperties appConfigProperties;

    @Override
    protected AppConfigProperties.TcpNode nodeInfo() {
        return appConfigProperties.getTcp();
    }

    @Override
    public void start() {
        // 设置为主从线程模型
        bootstrap.group(bossGroup, workGroup)
            // 设置服务端NIO通信类型
            .channel(NettyEventLoopFactory.serverSocketChannelClass())
            .option(ChannelOption.SO_BACKLOG, 1024)
            .option(ChannelOption.SO_REUSEADDR, true)
            .childOption(ChannelOption.SO_KEEPALIVE, false)
            // 表示连接保活，相当于心跳机制，默认为7200s
            .childOption(ChannelOption.TCP_NODELAY, true)
            // 设置ChannelPipeline，也就是业务职责链，由处理的Handler串联而成，由从线程池处理
            .childHandler(new ChannelInitializer<Channel>() {

                // 添加处理的Handler，通常包括消息编解码、业务处理，也可以是日志、权限、过滤等
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    // 获取职责链
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast("encode", new MessageProtocolEncoder());
                    pipeline.addLast("decode", new MessageProtocolDecoder());
                    pipeline.addLast("handler", new IMChannelHandler());
                    addPipeline(pipeline);
                }
            });

        try {
            Integer port = super.port();
            // 绑定端口，启动select线程，轮询监听channel事件，监听到事件之后就会交给从线程池处理
            Channel channel = bootstrap.bind(port).sync().channel();
            // 就绪标志
            ready = true;
            SpringContextHolder.sendEvent(NodeRegisterEvent.builder()
                .netProtocolEnum(NetProtocolEnum.TCP)
                .port(port)
                .registerTime(System.currentTimeMillis())
                .build());
            log.info("tcp server 初始化完成,端口：{}", port);
        } catch (InterruptedException e) {
            log.error("tcp server 初始化异常", e);
        }
    }

}
