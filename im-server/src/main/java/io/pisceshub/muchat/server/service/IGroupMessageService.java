package io.pisceshub.muchat.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.pisceshub.muchat.common.core.model.GroupMessageInfo;
import io.pisceshub.muchat.server.common.entity.GroupMessage;
import io.pisceshub.muchat.server.common.vo.message.GroupMessageSendReq;

import java.util.List;


public interface IGroupMessageService extends IService<GroupMessage> {


    Long sendMessage(GroupMessageSendReq vo);

    void recallMessage(Long id);

    void pullUnreadMessage();

    List<GroupMessageInfo> findHistoryMessage(Long groupId, Long page, Long size);
}
