package io.pisceshub.muchat.server.core.algorithm.consistenthash;

import io.pisceshub.muchat.common.core.enums.NetProtocolEnum;
import io.pisceshub.muchat.server.BaseSpringBootTest;
import io.pisceshub.muchat.server.core.NodeContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ConsistentHashHandleTest extends BaseSpringBootTest {

    @Autowired
    private ConsistentHashHandle consistentHashHandle;

    @Test
    void routeServer() {
        String key = "1";
        List<NodeContainer.WNode> wNodes = buildList(5);
        consistentHashHandle.list(NetProtocolEnum.WS,wNodes);
        NodeContainer.WNode n1 =consistentHashHandle.routeServer(NetProtocolEnum.WS,wNodes,key);
        NodeContainer.WNode n2 =consistentHashHandle.routeServer(NetProtocolEnum.WS,wNodes,key);
        NodeContainer.WNode n3 =consistentHashHandle.routeServer(NetProtocolEnum.WS,wNodes,key);

        wNodes = buildList(6);
        consistentHashHandle.list(NetProtocolEnum.WS,wNodes);
        System.out.println(consistentHashHandle.routeServer(NetProtocolEnum.WS,wNodes,key));
        System.out.println(consistentHashHandle.routeServer(NetProtocolEnum.WS,wNodes,key));
        System.out.println(consistentHashHandle.routeServer(NetProtocolEnum.WS,wNodes,key));
    }


    public List<NodeContainer.WNode> buildList(int count){
        List<NodeContainer.WNode> list = new ArrayList<>();
        for(int i=0;i<count;i++){
            NodeContainer.WNode node = NodeContainer.WNode.builder()
                    .protocolEnum(NetProtocolEnum.WS)
                    .ip(UUID.randomUUID().toString().replaceAll("-",""))
                    .port(8080)
                    .build();
            list.add(node);
        }
        return list;
    }
}