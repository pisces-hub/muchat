package io.pisceshub.muchat.server.tcp.listener.zk;

import io.pisceshub.muchat.common.core.enums.NetProtocolEnum;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author xiaochangbai
 * @date 2023-07-03 21:19
 * 长连接节点发送变化事件
 */
public interface INodeUpdateNodeEventListener {

    default void delete(NetProtocolEnum netProtocolEnum,String node){

    }

    default void add(NetProtocolEnum netProtocolEnum,Collection<String> node){

    }

    default void list(NetProtocolEnum netProtocolEnum, Collection<String> nodes){

    }

}
