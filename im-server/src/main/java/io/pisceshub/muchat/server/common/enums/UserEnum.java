package io.pisceshub.muchat.server.common.enums;

import lombok.Getter;

public interface UserEnum {

    @Getter
    enum AccountType {

                      Plain(0, "正常"), Anonymous(1, "匿名"),;

        private Integer code;

        private String  msg;

        AccountType(Integer code, String msg){
            this.code = code;
            this.msg = msg;
        }

    }

    @Getter
    enum RegisterRromEnum {

                           SYS(0, "SYS"), GITEE(1, "GITEE"), GITHUB(2, "GITHUB"),;

        private Integer code;

        private String  msg;

        RegisterRromEnum(Integer code, String msg){
            this.code = code;
            this.msg = msg;
        }

        public static RegisterRromEnum findByMsg(String msg) {
            for (RegisterRromEnum e : values()) {
                if (e.getMsg().equals(msg)) {
                    return e;
                }
            }
            return null;
        }
    }
}
