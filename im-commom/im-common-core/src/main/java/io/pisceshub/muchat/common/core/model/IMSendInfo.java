package io.pisceshub.muchat.common.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IMSendInfo<T> {

    /*
     * 命令
     */
    private Integer cmd;

    /*
     * 推送消息体
     */
    private T data;

}
