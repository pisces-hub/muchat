package io.pisceshub.muchat.common.log.entity;

import lombok.Data;

/**
 * @author xiaochangbai
 * @date 2023-07-01 10:48
 */
@Data
public class ApiLogEntity {

  private String apiModule;

  private String apiName;

  private String beginTime;

  private Object[] inputParams;

  private Object outputParams;

}
