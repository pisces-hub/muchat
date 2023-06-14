package io.xiaochangbai.muchat.connector.listener.event;

import io.xiaochangbai.muchat.common.core.enums.NetProtocolEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.event.SpringApplicationEvent;

/**
 * @description:
 * @author: xiaochangbai
 * @date: 2023/6/12 16:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NodeRegisterEvent {

    private NetProtocolEnum netProtocolEnum;

    private Integer port;

}
