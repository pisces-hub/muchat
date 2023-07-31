package io.pisceshub.muchat.server.common.dto;

import io.pisceshub.muchat.common.core.enums.ChatType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author xiaochangbai
 * @date 2023-06-17 16:34
 */
@Data
public class ChatSessionInfoDto implements Serializable {

  @NotNull(message = "对方id不能为空")
  private Long targetId;

  @NotNull(message = "聊天类型不能为空")
  private ChatType chatType;

  private Long createTime;

}
