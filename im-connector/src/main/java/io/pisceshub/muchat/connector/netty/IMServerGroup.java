package io.pisceshub.muchat.connector.netty;

import io.pisceshub.muchat.common.core.contant.RedisKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
public class IMServerGroup implements CommandLineRunner  {

    public static volatile long serverId = 0;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private List<IMServer> imServers;

    /***
     * 判断服务器是否就绪
     *
     * @return
     **/
    public boolean isReady(){
        for(IMServer imServer:imServers){
            if(imServer.enable() && !imServer.isReady()){
                return false;
            }
        }
        return true;
    }

    @Override
    public void run(String... args) throws Exception {
        // 初始化SERVER_ID
        String key = RedisKey.IM_MAX_SERVER_ID;
        serverId =  redisTemplate.opsForValue().increment(key,1);
        Iterator<IMServer> iterator = imServers.iterator();
        while (iterator.hasNext()){
            IMServer imServer = iterator.next();
            if(imServer.enable()){
                imServer.start();
            }
        }
    }

    @PreDestroy
    public void destroy(){
        // 停止服务
        for(IMServer imServer:imServers){
            imServer.stop();
        }
    }
}
