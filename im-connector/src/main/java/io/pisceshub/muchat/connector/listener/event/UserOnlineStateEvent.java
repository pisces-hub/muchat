package io.pisceshub.muchat.connector.listener.event;

import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: xiaochangbai
 * @date: 2023/7/21 17:31
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserOnlineStateEvent {

    private ChannelHandlerContext ctx;

    private Long userId;

    private Event event;


    public static enum Event{

        /**
         * 上线
         */
        ONLINE,

        /**
         * 下线
         */
        OFFLINE
        ;

    }
}
