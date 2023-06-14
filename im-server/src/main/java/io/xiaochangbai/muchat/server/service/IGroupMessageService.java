package io.xiaochangbai.muchat.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.xiaochangbai.muchat.common.core.model.GroupMessageInfo;
import io.xiaochangbai.muchat.server.entity.GroupMessage;
import io.xiaochangbai.muchat.server.vo.GroupMessageVO;

import java.util.List;


public interface IGroupMessageService extends IService<GroupMessage> {


    Long sendMessage(GroupMessageVO vo);

    void recallMessage(Long id);

    void pullUnreadMessage();

    List<GroupMessageInfo> findHistoryMessage(Long groupId, Long page, Long size);
}
