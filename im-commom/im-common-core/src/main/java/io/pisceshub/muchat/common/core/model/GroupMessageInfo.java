package io.pisceshub.muchat.common.core.model;

import lombok.Data;

import java.util.List;

@Data
public class GroupMessageInfo extends CommonMessageInfo {

  /*
   * 群聊id
   */
  private Long groupId;

  /*
   * 接收者id
   */
  private List<Long> recvIds;

}
