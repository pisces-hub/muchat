package io.pisceshub.muchat.server.util;

import lombok.Data;

@Data
public class IP {

  private String country;
  private String province;
  private String city;
  private String isp;
  private String region;

  public static IP Local() {
    IP ip = new IP();
    ip.setCountry("本地");
    ip.setProvince("本地");
    ip.setCity("本地");
    ip.setRegion("本地");
    ip.setRegion("0");
    return ip;
  }
}