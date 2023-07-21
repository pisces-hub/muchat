package io.pisceshub.muchat.connector.utils;

import io.netty.channel.ChannelHandlerContext;
import io.pisceshub.muchat.common.core.enums.IMCmdType;
import io.pisceshub.muchat.common.core.model.IMSendInfo;

/**
 * @description:
 * @author: xiaochangbai
 * @date: 2023/7/21 15:51
 */
public final class SendMessageUtils {



    public static boolean sendError(ChannelHandlerContext ctx,String errorInfo){
        return send(ctx,IMSendInfo.builder().cmd(IMCmdType.ERROR.code()).data(errorInfo).build());
    }

    public static boolean send(ChannelHandlerContext ctx, IMSendInfo msg){
        if(ctx==null || msg==null || !ctx.channel().isOpen()){
            return false;
        }
        ctx.channel().writeAndFlush(msg);
        return true;
    }

    public static void close(ChannelHandlerContext ctx) {
        ctx.close();
    }

}
