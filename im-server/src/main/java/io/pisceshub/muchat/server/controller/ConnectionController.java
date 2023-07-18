package io.pisceshub.muchat.server.controller;

import io.pisceshub.muchat.common.log.annotation.ApiLog;
import io.pisceshub.muchat.server.util.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.pisceshub.muchat.common.core.enums.NetProtocolEnum;
import io.pisceshub.muchat.common.core.enums.ResultCode;
import io.pisceshub.muchat.common.core.utils.Result;
import io.pisceshub.muchat.common.core.utils.ResultUtils;
import io.pisceshub.muchat.server.service.ConnectionService;
import io.pisceshub.muchat.server.common.vo.connector.NodeInfoResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xiaochangbai
 * @date 2023-06-12 20:36
 */
@Api(tags = "连接信息")
@RestController
@RequestMapping("/connector")
public class ConnectionController {


    @Autowired
    private ConnectionService connectionService;

    @ApiOperation("查看所有在线的长连接机器")
    @GetMapping("/list")
    public Result<List<NodeInfoResp>> conneList(){
        List<NodeInfoResp> nodeInfoVoList = connectionService.nodeList();
        return ResultUtils.success(nodeInfoVoList);
    }


    @ApiLog
    @ApiOperation("选择一台可用的长连接")
    @GetMapping("/node")
    public Result<NodeInfoResp> node(NetProtocolEnum protocol){
        if(protocol==null){
            protocol = NetProtocolEnum.WS;
        }
        NodeInfoResp nodeInfoVo = connectionService.node(protocol, SessionContext.getUserIdIfExist());
        if(nodeInfoVo==null){
            return ResultUtils.error(ResultCode.NO_AVAILABLE_SERVICES);
        }
        return ResultUtils.success(nodeInfoVo);
    }





}
