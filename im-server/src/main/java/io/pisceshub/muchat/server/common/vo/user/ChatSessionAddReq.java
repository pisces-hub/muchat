package io.pisceshub.muchat.server.common.vo.user;

import io.pisceshub.muchat.common.core.enums.ChatType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author xiaochangbai
 * @date 2023-06-15 21:44
 */
@Data
public class ChatSessionAddReq implements Serializable {


    @NotNull(message = "对方id不能为空")
    private Long targetId;

    @NotNull(message = "聊天类型不能为空")
    private ChatType chatType;


}
