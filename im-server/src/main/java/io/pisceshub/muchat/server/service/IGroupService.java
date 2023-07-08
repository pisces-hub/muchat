package io.pisceshub.muchat.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.pisceshub.muchat.common.core.model.PageResp;
import io.pisceshub.muchat.server.common.entity.Group;
import io.pisceshub.muchat.server.common.entity.GroupMember;
import io.pisceshub.muchat.server.common.vo.group.GroupMemberQueryReq;
import io.pisceshub.muchat.server.common.vo.user.GroupInviteReq;
import io.pisceshub.muchat.server.common.vo.user.GroupMemberResp;
import io.pisceshub.muchat.server.common.vo.user.GroupVO;

import java.util.List;


public interface IGroupService extends IService<Group> {


    GroupVO createGroup(String groupName);

    GroupVO modifyGroup(GroupVO vo);

    void deleteGroup(Long groupId);

    void quitGroup(Long groupId);

    void kickGroup(Long groupId,Long userId);

    List<GroupVO>  findGroups();

    void invite(GroupInviteReq vo);

    Group findBaseInfoById(Long groupId);

    GroupVO findById(Long groupId);

    List<GroupMemberResp> findGroupMembers(Long groupId);

    List<Group> findByGroupType(Integer code);
}
