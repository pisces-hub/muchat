package io.pisceshub.muchat.server.service.impl;

import cn.hutool.core.collection.CollUtil;
import io.pisceshub.muchat.common.core.enums.ChatType;
import io.pisceshub.muchat.common.core.model.CommonMessageInfo;
import io.pisceshub.muchat.common.core.model.GroupMessageInfo;
import io.pisceshub.muchat.common.core.model.PrivateMessageInfo;
import io.pisceshub.muchat.common.core.utils.Result;
import io.pisceshub.muchat.common.core.utils.ResultUtils;
import io.pisceshub.muchat.server.common.dto.ChatSessionInfoDto;
import io.pisceshub.muchat.server.common.vo.common.PageReq;
import io.pisceshub.muchat.server.common.vo.common.PageResp;
import io.pisceshub.muchat.server.common.vo.user.ChatSessionInfoResp;
import io.pisceshub.muchat.server.common.vo.user.GroupVO;
import io.pisceshub.muchat.server.common.vo.user.UserVO;
import io.pisceshub.muchat.server.exception.GlobalException;
import io.pisceshub.muchat.server.exception.NotJoinGroupException;
import io.pisceshub.muchat.server.service.*;
import io.pisceshub.muchat.server.service.business.chatsession.ChatSessionSave;
import io.pisceshub.muchat.server.common.vo.user.ChatSessionAddReq;
import io.pisceshub.muchat.server.util.BeanUtils;
import io.pisceshub.muchat.server.util.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author xiaochangbai
 * @date 2023-06-15 21:45
 */
@Service
public class ChatSessionServiceImpl implements IChatSessionService {

    @Autowired
    private ChatSessionSave chatSessionSave;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IGroupService iGroupService;

    @Autowired
    private IGroupMessageService iGroupMessageService;

    @Autowired
    private IPrivateMessageService iPrivateMessageService;

    @Override
    public boolean save(Long userId,ChatSessionAddReq vo) {

        //todo 校验对于id和类型的合法性
        ChatSessionInfoDto dto = new ChatSessionInfoDto();
        BeanUtils.copyProperties(vo,dto);
        return chatSessionSave.add(userId,dto);
    }

    @Override
    public Result<Set<ChatSessionInfoResp>> list() {
        Set<ChatSessionInfoDto> list = chatSessionSave.list(SessionContext.getUserId());
        if(CollUtil.isEmpty(list)){
            return ResultUtils.success(Collections.emptySet());
        }
        Set<ChatSessionInfoResp> result = new HashSet<>(list.size());
        Long userId = SessionContext.getUserId();
        for(ChatSessionInfoDto dto:list){
            ChatType chatType = dto.getChatType();
            Long targetId = dto.getTargetId();
            switch (chatType){
                case GROUP:
                    try {
                        //查询群信息
                        GroupVO groupVO = iGroupService.findById(targetId);
                        if(groupVO==null){
                            continue;
                        }
                        ChatSessionInfoResp chatSessionInfoResp = ChatSessionInfoResp.builder()
                                .chatType(chatType)
                                .targetId(targetId)
                                .name(groupVO.getName())
                                .headImage(groupVO.getHeadImage())
                                .unReadCount(0L).build();
                        //查询消息
                        List<GroupMessageInfo> historyMessage = iGroupMessageService.findHistoryMessage(targetId, null);
                        if(CollUtil.isNotEmpty(historyMessage)){
                            chatSessionInfoResp.setLastSendTime(historyMessage.get(0).getSendTime().getTime());
                            chatSessionInfoResp.setLastContent(historyMessage.get(0).getContent());
                            chatSessionInfoResp.setGroupMessages(historyMessage);
                        }else{
                            chatSessionInfoResp.setLastSendTime(dto.getCreateTime());
                        }

                        result.add(chatSessionInfoResp);
                    }catch (NotJoinGroupException e){
                    }
                    break;
                case PRIVATE:
                    UserVO userVO = iUserService.findByUserIdAndFriendId(targetId,userId);
                    if (userVO==null){
                        continue;
                    }
                    ChatSessionInfoResp chatSessionInfoResp = ChatSessionInfoResp.builder()
                            .chatType(chatType)
                            .targetId(targetId)
                            .name(userVO.getNickName())
                            .headImage(userVO.getHeadImage())
                            .unReadCount(0L).build();
                    //查询消息
                    List<PrivateMessageInfo> historyMessage = iPrivateMessageService.findHistoryMessage(targetId, 1L, 30L);
                    if(CollUtil.isNotEmpty(historyMessage)){
                        chatSessionInfoResp.setLastSendTime(historyMessage.get(0).getSendTime().getTime());
                        chatSessionInfoResp.setLastContent(historyMessage.get(0).getContent());
                    }else{
                        chatSessionInfoResp.setLastSendTime(dto.getCreateTime());
                    }

                    result.add(chatSessionInfoResp);
            }
        }

        return ResultUtils.success(result);
    }

    @Override
    public boolean del(ChatSessionAddReq vo) {
        return chatSessionSave.del(SessionContext.getUserId(),
                BeanUtils.copyProperties(vo,ChatSessionInfoDto.class));
    }

}
