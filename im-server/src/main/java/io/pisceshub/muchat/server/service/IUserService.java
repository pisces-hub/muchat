package io.pisceshub.muchat.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.pisceshub.muchat.server.common.entity.User;
import io.pisceshub.muchat.server.common.vo.user.*;
import me.zhyd.oauth.model.AuthUser;

import java.util.List;

public interface IUserService extends IService<User> {

    LoginResp login(LoginReq dto);

    LoginResp refreshToken(String refreshToken);

    void register(RegisterReq dto);

    User findUserByName(String username);

    void update(UserVO vo);

    List<UserVO> findUserByNickName(String nickname);

    List<Long> checkOnline(String userIds);

    UserVO findByUserIdAndFriendId(Long userId, Long friendId);

    LoginResp oauthLogin(String type, AuthUser authUser);

    LoginResp anonymousLogin(AnonymousLoginReq req);

    UserVO findByIde(long id);

    /**
     * 判断用户是否在线
     * 
     * @param userId
     * @return
     */
    boolean isOnline(Long userId);
}
