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
public class UserEvent {

    private ChannelHandlerContext ctx;

    private Long userId;

    private Event event;



    public static UserEvent buildOnlineEvent(Long userId, ChannelHandlerContext ctx){
        return UserEvent.builder().ctx(ctx).userId(userId).event(Event.ONLINE).build();
    }

    public static UserEvent buildOfflineEvent(Long userId, ChannelHandlerContext ctx){
        return UserEvent.builder().ctx(ctx).userId(userId).event(Event.OFFLINE).build();
    }

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
