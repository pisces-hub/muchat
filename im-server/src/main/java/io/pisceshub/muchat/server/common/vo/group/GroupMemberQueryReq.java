package io.pisceshub.muchat.server.common.vo.group;

import io.pisceshub.muchat.common.core.model.PageReq;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author xiaochangbai
 * @date 2023-07-08 11:48
 */
@Data
public class GroupMemberQueryReq extends PageReq {

    @NotNull(message = "群聊id不能为空")
    private Long   groupId;

    private String search;
}
