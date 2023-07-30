package io.pisceshub.muchat.server.common.vo.user;

import io.pisceshub.muchat.common.core.enums.ChatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author xiaochangbai
 * @date 2023-06-15 21:44
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatSessionAddReq implements Serializable {

    @NotNull(message = "对方id不能为空")
    private Long     targetId;

    @NotNull(message = "聊天类型不能为空")
    private ChatType chatType;

}
