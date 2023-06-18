package io.pisceshub.muchat.server.common.enums;

import io.swagger.models.auth.In;
import lombok.Getter;


@Getter
public enum RegisterRromEnum {

    SYS(0,"SYS"),
    GITEE(1,"GITEE"),
    GITHUB(2,"GITHUB"),
    ;

    private Integer code;

    private String msg;

    RegisterRromEnum(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public static RegisterRromEnum findByMsg(String msg) {
        for(RegisterRromEnum e:values()){
            if(e.getMsg().equals(msg)){
                return e;
            }
        }
        return null;
    }
}
