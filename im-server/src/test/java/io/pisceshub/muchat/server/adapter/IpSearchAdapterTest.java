package io.pisceshub.muchat.server.adapter;

import static org.junit.jupiter.api.Assertions.*;

import io.pisceshub.muchat.server.BaseSpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class IpSearchAdapterTest extends BaseSpringBootTest {


  @Autowired
  private IpSearchAdapter ipSearchAdapter;


  @Test
  public void testSearch(){
    assertEquals("雷德蒙德", ipSearchAdapter.search("20.24.37.65"));
    assertEquals("上海市", ipSearchAdapter.search("222.64.95.141"));
    assertEquals("蚌埠市", ipSearchAdapter.search("112.132.84.220"));
    assertEquals("深圳市", ipSearchAdapter.search("183.14.30.22"));
    assertEquals("伊朗", ipSearchAdapter.search("37.128.246.26"));
  }

}