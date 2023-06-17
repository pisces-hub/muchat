package io.xiaochangbai.muchat.server.vo;

import io.xiaochangbai.muchat.common.core.enums.ChatType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author xiaochangbai
 * @date 2023-06-15 21:44
 */
@Data
public class ChatSessionAddVo implements Serializable {


    @NotNull(message = "对方id不能为空")
    private Long targetId;

    @NotNull(message = "聊天类型不能为空")
    private ChatType chatType;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatSessionAddVo that = (ChatSessionAddVo) o;
        return Objects.equals(targetId, that.targetId) && chatType == that.chatType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetId, chatType);
    }
}
