package io.pisceshub.muchat.server.core.algorithm.consistenthash;

import io.pisceshub.muchat.common.core.enums.NetProtocolEnum;
import io.pisceshub.muchat.server.BaseSpringBootTest;
import io.pisceshub.muchat.server.core.NodeContainer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ConsistentHashHandleTest extends BaseSpringBootTest {

    @Autowired
    private ConsistentHashHandle consistentHashHandle;

    @Test
    void routeServer1() {
        String key = "1";
        List<NodeContainer.WNode> wNodes = buildMockData(5);
        consistentHashHandle.list(NetProtocolEnum.WS, wNodes);
        NodeContainer.WNode n1 = consistentHashHandle.routeServer(NetProtocolEnum.WS, wNodes, key);
        NodeContainer.WNode n2 = consistentHashHandle.routeServer(NetProtocolEnum.WS, wNodes, key);
        NodeContainer.WNode n3 = consistentHashHandle.routeServer(NetProtocolEnum.WS, wNodes, key);
        Assertions.assertTrue(n1.equals(n2) && n2.equals(n3));

        wNodes = buildMockData(8);
        consistentHashHandle.list(NetProtocolEnum.WS, wNodes);
        NodeContainer.WNode wn1 = consistentHashHandle.routeServer(NetProtocolEnum.WS, wNodes, key);
        NodeContainer.WNode wn2 = consistentHashHandle.routeServer(NetProtocolEnum.WS, wNodes, key);
        NodeContainer.WNode wn3 = consistentHashHandle.routeServer(NetProtocolEnum.WS, wNodes, key);
        Assertions.assertTrue(wn1.equals(wn2) && wn2.equals(wn3));
    }

    @Test
    void routeServer2() {
        Map<NodeContainer.WNode, Integer> statistic = new HashMap(10);

        List<NodeContainer.WNode> wNodes = buildMockData(5);
        consistentHashHandle.list(NetProtocolEnum.WS, wNodes);

        int nodeCount = 100;
        for (int i = 0; i < nodeCount; i++) {
            String key = String.valueOf(i);
            NodeContainer.WNode n = consistentHashHandle.routeServer(NetProtocolEnum.WS, wNodes, key);
            Integer count = statistic.getOrDefault(n, 0);
            statistic.put(n, ++count);
        }
        for (NodeContainer.WNode n : statistic.keySet()) {
            Integer count = statistic.get(n);
            log.info("节点信息[{}],次数:{},频率:{}", n.toString(), count, count * 1.0 / nodeCount);
        }
    }

    public List<NodeContainer.WNode> buildMockData(int count) {
        List<NodeContainer.WNode> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            NodeContainer.WNode node = NodeContainer.WNode.builder()
                .protocolEnum(NetProtocolEnum.WS)
                .ip("127.0.0." + i)
                .port(8080)
                .build();
            list.add(node);
        }
        return list;
    }
}
