package io.pisceshub.muchat.common.core.model;

import io.pisceshub.muchat.common.core.serializer.DateToLongSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.Date;

@Data
public class GroupMessageInfo extends CommonMessageInfo{

    /*
     * 群聊id
     */
    private Long groupId;

}
