package io.pisceshub.muchat.server.service.impl;


import cn.hutool.core.bean.BeanUtil;
import io.pisceshub.muchat.common.core.enums.NetProtocolEnum;
import io.pisceshub.muchat.server.common.vo.connector.NodeInfoResp;
import io.pisceshub.muchat.server.core.NodeContainer;
import io.pisceshub.muchat.server.core.algorithm.RouteHandle;
import io.pisceshub.muchat.server.service.ConnectionService;
import io.pisceshub.muchat.server.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private NodeContainer nodeContainer;

    @Override
    public List<NodeInfoResp> nodeList() {
        return nodeContainer.list(null).stream().map(e->{
            NodeInfoResp nodeInfoResp = BeanUtil.copyProperties(e, NodeInfoResp.class);
            nodeInfoResp.setProtocol(e.getProtocolEnum().name());
            return nodeInfoResp;
        }).collect(Collectors.toList());
    }

    @Override
    public NodeInfoResp node(NetProtocolEnum netProtocolEnum, Long identify) {
        if(identify==null){
            identify = Long.valueOf(IpUtil.getIntIp());
        }
        List<NodeContainer.WNode> nodes = nodeContainer.list(netProtocolEnum);
        NodeContainer.WNode wNode = routeHandle.routeServer(netProtocolEnum,nodes,String.valueOf(identify));
        if(wNode==null){
            return null;
        }
        NodeInfoResp resp = new NodeInfoResp();
        resp.setProtocol(wNode.getProtocolEnum().name());
        resp.setIp(wNode.getIp());
        resp.setPort(wNode.getPort());
        return resp;

    }
}
