package io.xiaochangbai.muchat.server.service;


import io.xiaochangbai.muchat.common.core.enums.NetProtocolEnum;
import io.xiaochangbai.muchat.server.vo.NodeInfoVo;

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
