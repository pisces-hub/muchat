package io.pisceshub.muchat.server.service;


import io.pisceshub.muchat.common.core.enums.NetProtocolEnum;
import io.pisceshub.muchat.server.vo.NodeInfoVo;

import java.util.List;

/**
 * @author XDD
 * @project muchat
 * @date 2021/7/24
 * @description Good Good Study,Day Day Up.
 */
public interface ConnectionService {

    List<NodeInfoVo> nodeList();

    NodeInfoVo node(NetProtocolEnum netProtocolEnum,Long identify);
}
