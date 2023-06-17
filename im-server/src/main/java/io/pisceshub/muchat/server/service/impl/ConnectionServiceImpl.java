package io.pisceshub.muchat.server.service.impl;


import io.pisceshub.muchat.common.core.algorithm.RouteHandle;
import io.pisceshub.muchat.common.core.contant.AppConst;
import io.pisceshub.muchat.common.core.enums.NetProtocolEnum;
import io.pisceshub.muchat.server.service.ConnectionService;
import io.pisceshub.muchat.server.common.vo.connector.NodeInfoResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author XDD
 * @project muchat
 * @date 2021/7/24
 * @description Good Good Study,Day Day Up.
 */
@Service
public class ConnectionServiceImpl implements ConnectionService {

    @Autowired
    private RouteHandle routeHandle;

    @Override
    public List<NodeInfoResp> nodeList() {
        List<NodeInfoResp> l1 = AppConst.WS_NODES.stream().map(str -> {
            return assembleNode(str, NetProtocolEnum.WS);
        }).collect(Collectors.toList());
        List<NodeInfoResp> l2 = AppConst.TCP_NODES.stream().map(str -> {
            return assembleNode(str, NetProtocolEnum.TCP);
        }).collect(Collectors.toList());
        l1.addAll(l2);

        return l1;
    }

    @Override
    public NodeInfoResp node(NetProtocolEnum netProtocolEnum, Long identify) {
        if(identify==null){
            throw new RuntimeException();
        }
        String server = null;
        if(NetProtocolEnum.WS.equals(netProtocolEnum)){
            server = routeHandle.routeServer(new ArrayList<>(AppConst.WS_NODES),String.valueOf(identify));
        }else if(NetProtocolEnum.TCP.equals(netProtocolEnum)){
            server = routeHandle.routeServer(new ArrayList<>(AppConst.WS_NODES),String.valueOf(identify));
        }else{
            return null;
        }
        if(server==null){
            return null;
        }
        return assembleNode(server,netProtocolEnum);

    }

    private NodeInfoResp assembleNode(String server, NetProtocolEnum netProtocolEnum) {
        String[] split = server.split(":");
        NodeInfoResp node = new NodeInfoResp();
        node.setProtocol(netProtocolEnum.name());
        node.setIp(split[0]);
        node.setPort(split[1]);
        return node;
    }
}
