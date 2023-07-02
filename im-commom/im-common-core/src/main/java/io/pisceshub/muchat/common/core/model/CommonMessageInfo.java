package io.pisceshub.muchat.common.core.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.pisceshub.muchat.common.core.serializer.DateToLongSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xiaochangbai
 * @date 2023-07-01 13:38
 */
@Data
public abstract class CommonMessageInfo implements Serializable {


    /*
     * 发送者id
     */
    protected Long sendId;


    /*
     * 消息id
     */
    protected Long id;

    /*
     * 发送内容
     */
    protected String content;

    /*
     * 消息内容类型 具体枚举值由应用层定义
     */
    protected Integer type;


    /**
     * 发送时间
     */
    @JsonSerialize(using = DateToLongSerializer.class)
    protected Date sendTime;
}
