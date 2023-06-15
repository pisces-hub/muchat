package io.xiaochangbai.muchat.server.controller;

import io.swagger.annotations.Api;
import io.xiaochangbai.muchat.common.core.utils.Result;
import io.xiaochangbai.muchat.common.core.utils.ResultUtils;
import io.xiaochangbai.muchat.server.service.IChatSessionService;
import io.xiaochangbai.muchat.server.vo.ChatSessionAddVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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


    @PostMapping("/save")
    public Result<String> save(@RequestBody @Valid ChatSessionAddVo vo){
        return iChatSessionService.save(vo)? ResultUtils.success():ResultUtils.error();
    }

}
