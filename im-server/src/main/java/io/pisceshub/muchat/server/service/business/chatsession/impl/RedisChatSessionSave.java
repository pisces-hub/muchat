package io.xiaochangbai.muchat.server.service.business.chatsession.impl;

import io.xiaochangbai.muchat.server.service.business.chatsession.ChatSessionSave;
import io.xiaochangbai.muchat.server.session.SessionContext;
import io.xiaochangbai.muchat.server.vo.ChatSessionAddVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author xiaochangbai
 * @date 2023-06-15 21:49
 */
@Component
public class RedisChatSessionSave implements ChatSessionSave {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean add(ChatSessionAddVo vo) {

        Long aLong = redisTemplate.opsForSet().add(String.valueOf(SessionContext.getUserId()), vo);
        return aLong>0;
    }
}
