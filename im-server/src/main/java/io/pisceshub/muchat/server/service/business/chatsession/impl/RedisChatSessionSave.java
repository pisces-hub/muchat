package io.pisceshub.muchat.server.service.business.chatsession.impl;

import io.pisceshub.muchat.server.common.dto.ChatSessionInfoDto;
import io.pisceshub.muchat.server.common.vo.user.ChatSessionInfoResp;
import io.pisceshub.muchat.server.service.business.chatsession.ChatSessionSave;
import io.pisceshub.muchat.server.util.BeanUtils;
import io.pisceshub.muchat.server.util.SessionContext;
import io.pisceshub.muchat.server.common.vo.user.ChatSessionAddReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @author xiaochangbai
 * @date 2023-06-15 21:49
 */
@Component
public class RedisChatSessionSave implements ChatSessionSave {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean add(ChatSessionInfoDto dto) {
        redisTemplate.opsForSet().add(String.valueOf(SessionContext.getUserId()), dto);
        return true;
    }

    @Override
    public Set<ChatSessionInfoDto> list() {
        return redisTemplate.opsForSet().members(String.valueOf(SessionContext.getUserId()));
    }

    @Override
    public boolean del(ChatSessionInfoDto dto) {
        redisTemplate.opsForSet().remove(String.valueOf(SessionContext.getUserId()),dto);
        return true;
    }
}
