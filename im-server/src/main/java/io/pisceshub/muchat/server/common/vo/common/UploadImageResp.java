package io.pisceshub.muchat.server.common.vo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("图片上传VO")
public class UploadImageResp {

  @ApiModelProperty(value = "原图")
  private String originUrl;

  @ApiModelProperty(value = "缩略图")
  private String thumbUrl;
}
