package io.pisceshub.muchat.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.pisceshub.muchat.server.entity.User;
import io.pisceshub.muchat.server.dto.LoginDTO;
import io.pisceshub.muchat.server.dto.RegisterDTO;
import io.pisceshub.muchat.server.vo.LoginVO;
import io.pisceshub.muchat.server.vo.UserVO;

import java.util.List;


public interface IUserService extends IService<User> {

    LoginVO login(LoginDTO dto);

    LoginVO refreshToken(String refreshToken);

    void register(RegisterDTO dto);

    User findUserByName(String username);

    void update(UserVO vo);

    List<UserVO> findUserByNickName(String nickname);

    List<Long> checkOnline(String userIds);

}
