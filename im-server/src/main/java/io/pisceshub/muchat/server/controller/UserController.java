package io.pisceshub.muchat.server.controller;

import io.pisceshub.muchat.server.aop.annotation.AnonymousUserCheck;
import io.pisceshub.muchat.server.common.entity.User;
import io.pisceshub.muchat.server.service.IUserService;
import io.pisceshub.muchat.server.util.SessionContext;
import io.pisceshub.muchat.server.util.BeanUtils;
import io.pisceshub.muchat.common.core.utils.Result;
import io.pisceshub.muchat.common.core.utils.ResultUtils;
import io.pisceshub.muchat.server.common.vo.user.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;


@Api(tags = "用户")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;


    @GetMapping("/online")
    @ApiOperation(value = "判断用户是否在线",notes="返回在线的用户id集合")
    public Result checkOnline(@NotEmpty @RequestParam("userIds") String userIds){
        List<Long> onlineIds = userService.checkOnline(userIds);
        return ResultUtils.success(onlineIds);
    }

    @GetMapping("/self")
    @ApiOperation(value = "获取当前用户信息",notes="获取当前用户信息")
    public Result findSelfInfo(){
        SessionContext.UserSessionInfo session = SessionContext.getSession();
        User user = userService.getById(session.getId());
        UserVO userVO = BeanUtils.copyProperties(user,UserVO.class);
        return ResultUtils.success(userVO);
    }

    @GetMapping("/find/{id}")
    @ApiOperation(value = "查找用户",notes="根据id查找用户")
    public Result findByIde(@NotEmpty @PathVariable("id") long id){

        UserVO userVO = userService.findByIde(id);
        return ResultUtils.success(userVO);
    }

    @AnonymousUserCheck
    @PutMapping("/update")
    @ApiOperation(value = "修改用户信息",notes="修改用户信息，仅允许修改登录用户信息")
    public Result update(@Valid @RequestBody UserVO vo){
        userService.update(vo);
        return ResultUtils.success();
    }



    @AnonymousUserCheck
    @GetMapping("/findByNickName")
    @ApiOperation(value = "查找用户",notes="根据昵称查找用户")
    public Result findByNickName(@NotEmpty(message = "用户昵称不可为空") @RequestParam("nickName") String nickName){
           return ResultUtils.success( userService.findUserByNickName(nickName));
    }
}

