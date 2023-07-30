package io.pisceshub.muchat.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.pisceshub.muchat.server.common.entity.GroupMessage;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface GroupMessageMapper extends BaseMapper<GroupMessage> {

    List<GroupMessage> findHistoryMessage(@Param("groupId") Long groupId, @Param("startTime") Date startTime,
                                          @Param("beforeMessageId") Long beforeMessageId,
                                          @Param("count") Integer count);
}
