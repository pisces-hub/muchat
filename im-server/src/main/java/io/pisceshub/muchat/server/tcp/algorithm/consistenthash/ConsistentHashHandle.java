package io.pisceshub.muchat.server.tcp.algorithm.consistenthash;


import io.pisceshub.muchat.common.core.enums.NetProtocolEnum;
import io.pisceshub.muchat.server.tcp.algorithm.RouteHandle;
import io.pisceshub.muchat.server.tcp.listener.zk.INodeUpdateNodeEventListener;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class ConsistentHashHandle implements RouteHandle, INodeUpdateNodeEventListener {

    private final static int defaultVirtualNodeSize = 200;
    private Map<NetProtocolEnum,AbstractConsistentHash> tmpMap = new ConcurrentHashMap<>();
    @Override
    public String routeServer(NetProtocolEnum protocolEnum, List<String> nodes, String key) {
        AbstractConsistentHash hash = tmpMap.getOrDefault(protocolEnum,
                new TreeMapConsistentHash(nodes,defaultVirtualNodeSize));
        return hash.process(key);
    }

    @Override
    public void list(NetProtocolEnum netProtocolEnum, Collection<String> nodes){
        AbstractConsistentHash abstractConsistentHash = tmpMap.get(netProtocolEnum);
        if(abstractConsistentHash==null){
            abstractConsistentHash = new TreeMapConsistentHash(nodes,defaultVirtualNodeSize);
        }else{
            abstractConsistentHash.reBuildRing(nodes);
        }
        tmpMap.put(netProtocolEnum,abstractConsistentHash);
        log.info("节点发生变更，重建hash环{},{}",netProtocolEnum,nodes);
    }
}
