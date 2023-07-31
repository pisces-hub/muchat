package io.pisceshub.muchat.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.pisceshub.muchat.server.common.entity.Friend;
import io.pisceshub.muchat.server.common.vo.user.FriendVO;

import java.util.List;

public interface IFriendService extends IService<Friend> {

  Boolean isFriend(Long userId1, Long userId2);

  List<Friend> findFriendByUserId(Long UserId);

  void addFriend(Long friendId);

  void delFriend(Long friendId);

  void update(FriendVO vo);

  FriendVO findFriend(Long friendId);
}
