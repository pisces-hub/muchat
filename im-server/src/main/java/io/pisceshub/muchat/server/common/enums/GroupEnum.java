package io.pisceshub.muchat.server.common.enums;

import lombok.Getter;

public interface GroupEnum {

  @Getter
  enum GroupType {

    Plain(0, "正常"), Anonymous(1, "匿名"),
    ;

    private Integer code;

    private String msg;

    GroupType(Integer code, String msg) {
      this.code = code;
      this.msg = msg;
    }

  }

}
