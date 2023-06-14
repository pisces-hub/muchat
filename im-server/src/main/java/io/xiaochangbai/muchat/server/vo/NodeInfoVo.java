package io.xiaochangbai.muchat.server.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author XDD
 * @project muchat
 * @date 2021/7/24
 * @description Good Good Study,Day Day Up.
 */
@Data
public class NodeInfoVo {

    @ApiModelProperty("协议,ws,wss,mqtt等等")
    private String protocol;

    @ApiModelProperty("ip信息")
    private String ip;

    @ApiModelProperty("端口信息")
    private String port;

}
