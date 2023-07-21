package io.pisceshub.muchat.connector.netty.tcp;

import io.pisceshub.muchat.common.core.contant.AppConst;
import io.pisceshub.muchat.common.core.enums.NetProtocolEnum;
import io.pisceshub.muchat.common.core.utils.MixUtils;
import io.pisceshub.muchat.common.core.utils.SpringContextHolder;
import io.pisceshub.muchat.connector.config.AppConfigProperties;
import io.pisceshub.muchat.connector.listener.event.NodeRegisterEvent;
import io.pisceshub.muchat.connector.netty.IMChannelHandler;
import io.pisceshub.muchat.connector.netty.IMServer;
import io.pisceshub.muchat.connector.netty.factory.NettyEventLoopFactory;
import io.pisceshub.muchat.connector.netty.tcp.endecode.MessageProtocolDecoder;
import io.pisceshub.muchat.connector.netty.tcp.endecode.MessageProtocolEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 *  TCP服务器,用于连接非网页的客户端,协议格式： 4字节内容的长度+IMSendInfo的JSON序列化
 *
 * @author Blue
 * @date 2022-11-20
 */
@Slf4j
@Component
public class TcpSocketServer implements IMServer {


    private volatile boolean ready = false;

    @Autowired
    private AppConfigProperties appConfigProperties;

    private  ServerBootstrap bootstrap;
    private  EventLoopGroup bossGroup;
    private  EventLoopGroup workGroup;

    @Override
    public boolean enable(){
        return appConfigProperties.getTcp().getEnable();
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    @Override
    public void start() {
        bootstrap = new ServerBootstrap();
        bossGroup = NettyEventLoopFactory.eventLoopGroup(1);
        workGroup = NettyEventLoopFactory.eventLoopGroup(Math.min(Runtime.getRuntime().availableProcessors() + 1,32));
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
                        pipeline.addLast(new IdleStateHandler(0, 0, AppConst.ONLINE_TIMEOUT_SECOND, TimeUnit.SECONDS));
                        pipeline.addLast("encode",new MessageProtocolEncoder());
                        pipeline.addLast("decode",new MessageProtocolDecoder());
                        pipeline.addLast("handler", new IMChannelHandler());
                    }
                });

        try {
            Integer port = appConfigProperties.getTcp().getPort();
            if(port==null || port<1){
                port = MixUtils.findAvailablePort();
                appConfigProperties.getTcp().setPort(port);
            }
            // 绑定端口，启动select线程，轮询监听channel事件，监听到事件之后就会交给从线程池处理
            Channel channel = bootstrap.bind(port).sync().channel();
            // 就绪标志
            this.ready = true;
            SpringContextHolder.sendEvent(
                    NodeRegisterEvent.builder()
                            .netProtocolEnum(NetProtocolEnum.TCP)
                            .port(port)
                            .registerTime(System.currentTimeMillis())
                            .build());
            log.info("tcp server 初始化完成,端口：{}",port);
        } catch (InterruptedException e) {
            log.error("tcp server 初始化异常",e);
        }
    }

    @Override
    public void stop(){
        if(bossGroup != null && !bossGroup.isShuttingDown() && !bossGroup.isShutdown() ) {
            bossGroup.shutdownGracefully();
        }
        if(workGroup != null && !workGroup.isShuttingDown() && !workGroup.isShutdown() ) {
            workGroup.shutdownGracefully();
        }
        this.ready = false;
        log.error("tcp server 停止");
    }


}
