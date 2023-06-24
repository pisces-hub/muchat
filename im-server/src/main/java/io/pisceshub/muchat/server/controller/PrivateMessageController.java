package io.pisceshub.muchat.server.controller;


import io.pisceshub.muchat.common.core.model.PrivateMessageInfo;
import io.pisceshub.muchat.common.core.utils.Result;
import io.pisceshub.muchat.common.core.utils.ResultUtils;
import io.pisceshub.muchat.server.aop.annotation.AnonymousUserCheck;
import io.pisceshub.muchat.server.service.IPrivateMessageService;
import io.pisceshub.muchat.server.common.vo.message.PrivateMessageSendReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api(tags = "私聊消息")
@RestController
@RequestMapping("/message/private")
public class PrivateMessageController {

    @Autowired
    private IPrivateMessageService privateMessageService;

    @AnonymousUserCheck
    @PostMapping("/send")
    @ApiOperation(value = "发送消息",notes="发送私聊消息")
    public Result<Long> sendMessage(@Valid @RequestBody PrivateMessageSendReq vo){
        return ResultUtils.success(privateMessageService.sendMessage(vo));
    }


    @AnonymousUserCheck
    @DeleteMapping("/recall/{id}")
    @ApiOperation(value = "撤回消息",notes="撤回私聊消息")
    public Result<Long> recallMessage(@NotNull(message = "消息id不能为空") @PathVariable Long id){
        privateMessageService.recallMessage(id);
        return ResultUtils.success();
    }


    @AnonymousUserCheck
    @PostMapping("/pullUnreadMessage")
    @ApiOperation(value = "拉取未读消息",notes="拉取未读消息")
    public Result pullUnreadMessage(){
        privateMessageService.pullUnreadMessage();
        return ResultUtils.success();
    }


    @AnonymousUserCheck
    @GetMapping("/history")
    @ApiOperation(value = "查询聊天记录",notes="查询聊天记录")
    public Result<List<PrivateMessageInfo>> recallMessage(@NotNull(message = "好友id不能为空") @RequestParam Long friendId,
                                                          @NotNull(message = "页码不能为空") @RequestParam Long page,
                                                          @NotNull(message = "size不能为空") @RequestParam Long size){
        return ResultUtils.success( privateMessageService.findHistoryMessage(friendId,page,size));
    }

}

