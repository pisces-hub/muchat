package io.pisceshub.muchat.server.common.vo.user;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("群成员信息VO")
public class GroupMemberResp {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("群内显示名称")
    private String aliasName;

    @ApiModelProperty("头像")
    private String headImage;

    @ApiModelProperty("是否已退出")
    private Boolean quit;

    @ApiModelProperty("备注")
    private String remark;

}
