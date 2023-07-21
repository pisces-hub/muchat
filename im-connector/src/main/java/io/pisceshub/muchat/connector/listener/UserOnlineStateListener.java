package io.pisceshub.muchat.connector.listener;

import cn.hutool.core.util.StrUtil;
import io.pisceshub.muchat.common.core.contant.AppConst;
import io.pisceshub.muchat.common.core.contant.RedisKey;
import io.pisceshub.muchat.common.core.enums.IMCmdType;
import io.pisceshub.muchat.common.core.enums.NetProtocolEnum;
import io.pisceshub.muchat.common.core.model.IMSendInfo;
import io.pisceshub.muchat.common.core.utils.MixUtils;
import io.pisceshub.muchat.common.core.utils.SpringContextHolder;
import io.pisceshub.muchat.connector.config.AppConfigProperties;
import io.pisceshub.muchat.connector.listener.event.NodeRegisterEvent;
import io.pisceshub.muchat.connector.listener.event.UserOnlineStateEvent;
import io.pisceshub.muchat.connector.netty.IMServerGroup;
import io.pisceshub.muchat.connector.utils.SendMessageUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author XDD
 * @project muchat
 * @date 2021/7/23
 * @description Good Good Study,Day Day Up.
 */
@Component
@Slf4j
public class UserOnlineStateListener {

    @Autowired
    RedisTemplate<String,Object> redisTemplate;


    @SneakyThrows
    @EventListener(classes = UserOnlineStateEvent.class)
    public void onApplicationEvent(UserOnlineStateEvent event) {

        if(UserOnlineStateEvent.Event.ONLINE.equals(event.getEvent())){
            redisTemplate.opsForValue().set(RedisKey.IM_USER_SERVER_ID+event.getUserId(),
                    IMServerGroup.serverId, AppConst.ONLINE_TIMEOUT_SECOND, TimeUnit.SECONDS);
            // 响应ws
            SendMessageUtils.send(event.getCtx(), IMSendInfo.create(IMCmdType.LOGIN,"登录成功"));
        }else if(UserOnlineStateEvent.Event.OFFLINE.equals(event.getEvent())){
            // 用户下线
            String key = RedisKey.IM_USER_SERVER_ID + event.getUserId();
            redisTemplate.delete(key);
        }

    }


}
