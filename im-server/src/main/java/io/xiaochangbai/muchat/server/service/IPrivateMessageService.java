package io.xiaochangbai.muchat.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.xiaochangbai.muchat.common.core.model.PrivateMessageInfo;
import io.xiaochangbai.muchat.server.entity.PrivateMessage;
import io.xiaochangbai.muchat.server.vo.PrivateMessageVO;

import java.util.List;


public interface IPrivateMessageService extends IService<PrivateMessage> {

    Long sendMessage(PrivateMessageVO vo);

    void recallMessage(Long id);

    List<PrivateMessageInfo> findHistoryMessage(Long friendId, Long page,Long size);

    void pullUnreadMessage();

}
