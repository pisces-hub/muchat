package io.pisceshub.muchat.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.pisceshub.muchat.server.common.entity.GroupMember;
import org.apache.ibatis.annotations.Param;

public interface GroupMemberMapper extends BaseMapper<GroupMember> {

    Page<GroupMember> findGroupMembersV2(IPage<GroupMember> page, @Param("groupId") Long groupId,
                                         @Param("search") String search);
}
