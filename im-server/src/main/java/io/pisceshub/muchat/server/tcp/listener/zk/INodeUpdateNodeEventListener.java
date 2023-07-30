package io.pisceshub.muchat.server.tcp.listener.zk;

import io.pisceshub.muchat.common.core.enums.NetProtocolEnum;
import io.pisceshub.muchat.server.core.NodeContainer;

import java.util.Collection;

/**
 * @author xiaochangbai
 * @date 2023-07-03 21:19 长连接节点发送变化事件
 */
public interface INodeUpdateNodeEventListener {

    default void delete(NetProtocolEnum netProtocolEnum, NodeContainer.WNode node) {

    }

    default void add(NetProtocolEnum netProtocolEnum, Collection<NodeContainer.WNode> node) {

    }

    default void list(NetProtocolEnum netProtocolEnum, Collection<NodeContainer.WNode> nodes) {

    }

}
