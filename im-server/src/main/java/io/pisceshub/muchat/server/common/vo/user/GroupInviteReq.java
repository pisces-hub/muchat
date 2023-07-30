package io.pisceshub.muchat.server.common.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("邀请好友进群请求VO")
public class GroupInviteReq {

    @NotNull(message = "群id不可为空")
    @ApiModelProperty(value = "群id")
    private Long       groupId;

    @NotEmpty(message = "群id不可为空")
    @ApiModelProperty(value = "好友id列表不可为空")
    private List<Long> friendIds;

    private Long       userId;
}
