package io.pisceshub.muchat.connector.netty.processor;


import io.netty.channel.ChannelHandlerContext;

public interface MessageProcessor {

    public void process(ChannelHandlerContext ctx,Object data);

}
