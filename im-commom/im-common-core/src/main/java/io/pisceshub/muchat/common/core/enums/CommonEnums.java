package io.pisceshub.muchat.common.core.enums;

import lombok.Getter;

/**
 * @description:
 * @author: xiaochangbai
 * @date: 2023/8/7 17:31
 */
@Getter
public enum CommonEnums {


  Yes(1,"yes"),
  No(0,"no")
    ;


  Integer code;

  String desc;

  CommonEnums(Integer code,String desc){
    this.code = code;
    this.desc = desc;
  }

}
