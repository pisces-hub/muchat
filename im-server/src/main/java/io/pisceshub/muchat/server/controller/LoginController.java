package io.pisceshub.muchat.server.controller;


import io.pisceshub.muchat.common.core.utils.Result;
import io.pisceshub.muchat.common.core.utils.ResultUtils;
import io.pisceshub.muchat.server.service.IUserService;
import io.pisceshub.muchat.server.common.vo.user.LoginReq;
import io.pisceshub.muchat.server.common.vo.user.RegisterReq;
import io.pisceshub.muchat.server.common.vo.user.LoginResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;



@Api(tags = "用户登录和注册")
@RestController
public class LoginController {

    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    @ApiOperation(value = "用户登录",notes="用户登录")
    public Result register(@Valid @RequestBody LoginReq dto){
        LoginResp vo = userService.login(dto);
        return ResultUtils.success(vo);
    }


    @PutMapping("/refreshToken")
    @ApiOperation(value = "刷新token",notes="用refreshtoken换取新的token")
    public Result refreshToken(@RequestHeader("refreshToken")String refreshToken){
        LoginResp vo = userService.refreshToken(refreshToken);
        return ResultUtils.success(vo);
    }


    @PostMapping("/register")
    @ApiOperation(value = "用户注册",notes="用户注册")
    public Result register(@Valid @RequestBody RegisterReq dto){
        userService.register(dto);
        return ResultUtils.success();
    }
}
