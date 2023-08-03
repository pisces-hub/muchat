package io.pisceshub.muchat.server.adapter;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xiaochangbai
 * @date 2023-07-02 11:30
 */
@Slf4j
@Component
public class IpSearchAdapter {

  private final static String NOT = "未知";


  @Autowired
  private Searcher searcher;

  /**
   * 根据经纬度查找城市名称
   * @param ip
   * @return
   */
  public String search(String ip) {
    if (StrUtil.isEmpty(ip)) {
      return NOT;
    }
    try {
      String strs = searcher.search(ip);
      if(StrUtil.isBlank(strs)){
        return NOT;
      }
      strs = strs.replaceAll("\\|0","");
      String[] strings = strs.split("\\|");
      if(strings.length>1){
        return strings[strings.length-2];
      }
      return strings[strings.length-1];
    } catch (Exception e) {
      log.error("ip归属地查询异常", e);
      return null;
    }
  }

}
