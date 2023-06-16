package io.pisceshub.muchat.common.core.algorithm.consistenthash;

import cn.hutool.core.util.StrUtil;
import io.pisceshub.muchat.common.core.algorithm.RouteHandle;
import org.junit.jupiter.api.Test;

import java.util.*;


class ConsistentHashHandleTest {

    List<String> list =
            Arrays.asList("localhost:11111",
                    "localhost:11112",
                    "localhost:11113",
                    "localhost:11114",
                    "localhost:11115"
            );;

    @Test
    void test1() {
        RouteHandle routeHandle = new ConsistentHashHandle();
        String before = null;
        for(int i=0;i<1000;i++){
            if(before==null){
                before = routeHandle.routeServer(list, "1");
            }else{
                String current = routeHandle.routeServer(list, "1");
                if(StrUtil.isBlank(before) || !before.equals(current)){
                    throw new RuntimeException("结果不一致,current="+current+",before="+before+",i="+i);
                }
                before = current;
            }

        }
    }


    @Test
    void test2() {
        RouteHandle routeHandle = new ConsistentHashHandle();
        Map<String,Integer> map = new HashMap<>();
        for(int i=0;i<3000;i++){
            Random random = new Random();
            int nextInt = random.nextInt(50);
            String node = routeHandle.routeServer(list, String.valueOf(nextInt));
            map.put(node,map.getOrDefault(node,0)+1);
        }
        System.out.println(map);
        /**
         * {localhost:11111=835, localhost:11112=363, localhost:11113=682, localhost:11114=604, localhost:11115=516}
         */
    }


}