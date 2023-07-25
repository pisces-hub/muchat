package io.pisceshub.muchat.server.controller;

import io.pisceshub.muchat.common.core.utils.Result;
import io.pisceshub.muchat.common.core.utils.ResultUtils;
import io.pisceshub.muchat.common.log.annotation.ApiLog;
import io.pisceshub.muchat.server.aop.annotation.AnonymousUserCheck;
import io.pisceshub.muchat.server.common.vo.user.ChatSessionAddReq;
import io.pisceshub.muchat.server.common.vo.user.ChatSessionInfoResp;
import io.pisceshub.muchat.server.common.vo.user.ChatSessionUpdateReq;
import io.pisceshub.muchat.server.service.IChatSessionService;
import io.pisceshub.muchat.server.util.SessionContext;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

/**
 * @author xiaochangbai
 * @date 2023-06-15 21:43
 */
@Api(tags = "聊天会话")
@RestController
@RequestMapping("/chatSession")
public class ChatSessionController {


    @Autowired
    private IChatSessionService iChatSessionService;

    /**
     * 保存聊天会话
     * @param vo
     * @return
     */
    @ApiLog
    @AnonymousUserCheck
    @PostMapping("/save")
    public Result<String> save(@RequestBody @Valid ChatSessionAddReq vo){
        return iChatSessionService.save(SessionContext.getUserId(),vo)? ResultUtils.success():ResultUtils.error();
    }

    /**
     * 查询聊天会话
     * @return
     */
    @ApiLog
    @GetMapping("/list")
    public Result<Set<ChatSessionInfoResp>> pages(){
        return iChatSessionService.list();
    }


    /**
     * 删除聊天会话
     * @param vo
     * @return
     */
    @ApiLog
    @AnonymousUserCheck
    @DeleteMapping("/del")
    public Result<String> del(@RequestBody @Valid ChatSessionUpdateReq vo){
        vo.setUserId(null);
        return iChatSessionService.del(vo)? ResultUtils.success():ResultUtils.error();
    }
}
