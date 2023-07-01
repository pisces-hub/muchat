package io.pisceshub.muchat.server.controller;


import io.pisceshub.muchat.common.core.model.GroupMessageInfo;
import io.pisceshub.muchat.common.core.utils.Result;
import io.pisceshub.muchat.common.core.utils.ResultUtils;
import io.pisceshub.muchat.common.log.annotation.ApiLog;
import io.pisceshub.muchat.server.service.IGroupMessageService;
import io.pisceshub.muchat.server.common.vo.message.GroupMessageSendReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;


@Api(tags = "群聊消息")
@RestController
@RequestMapping("/message/group")
public class GroupMessageController {

    @Autowired
    private IGroupMessageService groupMessageService;


    @ApiLog
    @PostMapping("/send")
    @ApiOperation(value = "发送群聊消息",notes="发送群聊消息")
    public Result<Long> sendMessage(@Valid @RequestBody GroupMessageSendReq vo){
        return ResultUtils.success(groupMessageService.sendMessage(vo));
    }

    @DeleteMapping("/recall/{id}")
    @ApiOperation(value = "撤回消息",notes="撤回群聊消息")
    public Result<Long> recallMessage(@NotNull(message = "消息id不能为空") @PathVariable Long id){
        groupMessageService.recallMessage(id);
        return ResultUtils.success();
    }

    @PostMapping("/pullUnreadMessage")
    @ApiOperation(value = "拉取未读消息",notes="拉取未读消息")
    public Result pullUnreadMessage(){
        groupMessageService.pullUnreadMessage();
        return ResultUtils.success();
    }

    @GetMapping("/history")
    @ApiOperation(value = "查询聊天记录",notes="查询聊天记录")
    public Result<List<GroupMessageInfo>> recallMessage(@NotNull(message = "群聊id不能为空") @RequestParam Long groupId,
                                                        @RequestParam(required = false) Long lastMessageId){
        return ResultUtils.success(groupMessageService.findHistoryMessage(groupId,lastMessageId));
    }
}

