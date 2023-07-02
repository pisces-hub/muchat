package io.pisceshub.muchat.server.adapter;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author xiaochangbai
 * @date 2023-07-02 11:30
 */
@Slf4j
@Component
public class IpSearchAdapter {

    @Autowired
    private Searcher searcher;


    public String search(String ip){
        if(StrUtil.isEmpty(ip)){
            return "未知";
        }
        try {
            String strs =  searcher.search(ip);
            String[] strings = strs.split("\\|");
            return strings[3];
        } catch (Exception e) {
            log.error("ip归属地查询异常",e);
            return null;
        }
    }

}
