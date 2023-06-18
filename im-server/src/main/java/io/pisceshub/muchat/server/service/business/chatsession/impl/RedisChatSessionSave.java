package io.pisceshub.muchat.server.service.business.chatsession.impl;

import cn.hutool.core.collection.CollUtil;
import io.pisceshub.muchat.server.common.dto.ChatSessionInfoDto;
import io.pisceshub.muchat.server.common.vo.user.ChatSessionInfoResp;
import io.pisceshub.muchat.server.service.business.chatsession.ChatSessionSave;
import io.pisceshub.muchat.server.util.BeanUtils;
import io.pisceshub.muchat.server.util.SessionContext;
import io.pisceshub.muchat.server.common.vo.user.ChatSessionAddReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiaochangbai
 * @date 2023-06-15 21:49
 */
@Component
public class RedisChatSessionSave implements ChatSessionSave {

    private final static Long MAX_SIZE = 100L;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean add(ChatSessionInfoDto dto) {
        String key = buildKey();
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add(key,dto,System.currentTimeMillis());
        Long size = zSetOperations.size(key);
        if(size>MAX_SIZE){
            zSetOperations.removeRange(key,0,size-MAX_SIZE);
        }
        return true;
    }

    private String buildKey(){
        return String.valueOf(SessionContext.getUserId());
    }

    @Override
    public Set<ChatSessionInfoDto> list() {
        Set<ChatSessionInfoDto> sets = redisTemplate.opsForZSet()
                .rangeByScore(buildKey(), 0, System.currentTimeMillis());
        if(CollUtil.isEmpty(sets)){
            return Collections.emptySet();
        }
        return sets;
    }

    @Override
    public boolean del(ChatSessionInfoDto dto) {
        redisTemplate.opsForZSet().remove(buildKey(),dto);
        return true;
    }
}