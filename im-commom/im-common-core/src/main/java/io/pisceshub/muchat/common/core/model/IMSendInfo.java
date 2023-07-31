package io.pisceshub.muchat.common.core.model;

import io.pisceshub.muchat.common.core.enums.IMCmdType;
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

  public static IMSendInfo create(IMCmdType cmdType) {
    IMSendInfo sendInfo = new IMSendInfo();
    sendInfo.setCmd(cmdType.code());
    return sendInfo;
  }

  public static IMSendInfo create(IMCmdType cmdType, String msg) {
    IMSendInfo sendInfo = new IMSendInfo();
    sendInfo.setCmd(cmdType.code());
    sendInfo.setData(msg);
    return sendInfo;
  }

}
