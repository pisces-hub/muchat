package io.xiaochangbai.muchat.server.admin.controller;

import io.xiaochangbai.muchat.common.core.contant.AppConst;
import io.xiaochangbai.muchat.common.core.enums.NetProtocolEnum;
import io.xiaochangbai.muchat.server.admin.dto.ServerConnectionInfoItemVo;
import io.xiaochangbai.muchat.server.admin.dto.ServerInfoVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;



@Controller
@RequestMapping("/admin/connector")
public class ConnectorNodeController {



    /**
     * 查询服务信息
     * @return
     */
    @RequestMapping("/info")
    public String info(Model model){
        ServerInfoVo serverInfo = new ServerInfoVo();

        List<ServerConnectionInfoItemVo> items = new ArrayList<>();
        for(String l:AppConst.TCP_NODES){
            ServerConnectionInfoItemVo vo = new ServerConnectionInfoItemVo();
            vo.setProtocol(NetProtocolEnum.TCP.name());
            vo.setNetAddress(l);
            vo.setConnectorCount(-1L);
            items.add(vo);
        }
        for(String l:AppConst.WS_NODES){
            ServerConnectionInfoItemVo vo = new ServerConnectionInfoItemVo();
            vo.setProtocol(NetProtocolEnum.WS.name());
            vo.setNetAddress(l);
            vo.setConnectorCount(-1L);
            items.add(vo);
        }
        serverInfo.setServerCount(items.size());
        serverInfo.setItems(items);
        model.addAttribute("datas",serverInfo);
        return "info";
    }




}