package io.pisceshub.muchat.server.service.business.chatsession.impl;

import cn.hutool.core.collection.CollUtil;
import io.pisceshub.muchat.server.common.dto.ChatSessionInfoDto;
import io.pisceshub.muchat.server.service.business.chatsession.ChatSessionSave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Collections;
import java.util.Set;

/**
 * @author xiaochangbai
 * @date 2023-06-15 21:49
 */
// @Component
public class RedisChatSessionSave implements ChatSessionSave {

    private final static Long MAX_SIZE = 100L;

    @Autowired
    private RedisTemplate     redisTemplate;

    @Override
    public boolean add(Long userId, ChatSessionInfoDto dto) {
        dto.setCreateTime(System.currentTimeMillis());
        String key = String.valueOf(userId);
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add(key, dto, System.currentTimeMillis());
        Long size = zSetOperations.size(key);
        if (size > MAX_SIZE) {
            zSetOperations.removeRange(key, 0, size - MAX_SIZE);
        }
        return true;
    }

    @Override
    public Set<ChatSessionInfoDto> list(Long userId) {
        Set<ChatSessionInfoDto> sets = redisTemplate.opsForZSet()
            .rangeByScore(String.valueOf(userId), 0, System.currentTimeMillis());
        if (CollUtil.isEmpty(sets)) {
            return Collections.emptySet();
        }
        return sets;
    }

    @Override
    public boolean del(Long userId, ChatSessionInfoDto dto) {
        redisTemplate.opsForZSet().remove(String.valueOf(userId), dto);
        return true;
    }
}
