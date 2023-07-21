package io.pisceshub.muchat.server.core;

import cn.hutool.core.collection.CollUtil;
import io.pisceshub.muchat.common.core.enums.NetProtocolEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @description:
 * @author: xiaochangbai
 * @date: 2023/7/18 12:14
 */
@Component
public class NodeContainer {


    public static final Map<NetProtocolEnum,Set<WNode>> map = new ConcurrentHashMap<>(10);

    /**
     * 查询所有节点
     * @param protocolEnum  为空时查询所有节点类型
     * @return
     */
    public List<WNode> list(NetProtocolEnum protocolEnum){
        List<NetProtocolEnum> protocolEnums = null;
        if(protocolEnum==null){
            protocolEnums = Arrays.asList(NetProtocolEnum.values());
        }else{
            protocolEnums = List.of(protocolEnum);
        }
        Set<WNode> wNodes = new HashSet<>();
        for(NetProtocolEnum protocol:protocolEnums){
            Set<WNode> tmpSet = map.get(protocol);
            if(CollUtil.isNotEmpty(tmpSet)){
                wNodes.addAll(tmpSet);
            }
        }

        return new ArrayList<>(wNodes);
    }

    /**
     * 添加节点
     * @param protocolEnum
     * @param nodes
     */
    public void add(NetProtocolEnum protocolEnum, Collection<WNode> nodes){
        if(CollUtil.isEmpty(nodes)){
            return;
        }
        Set<WNode> wNodes = map.get(protocolEnum);
        if(wNodes==null){
            wNodes = new CopyOnWriteArraySet<>(nodes);
            map.put(protocolEnum,wNodes);
        }
        wNodes.addAll(nodes);
    }


    /**
     * 删除节点
     * @param protocolEnum
     * @param node
     */
    public void remove(NetProtocolEnum protocolEnum, WNode node){
        Set<WNode> wNodes = map.get(protocolEnum);
        if(wNodes==null){
            return;
        }
        wNodes.remove(node);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WNode implements Serializable {

        private String ip;

        private Integer port;

        private NetProtocolEnum protocolEnum;

        private Long registerTime;


        @Override
        public boolean equals(Object o) {
            if (this == o){
                return true;
            };
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            WNode wNode = (WNode) o;
            return Objects.equals(ip, wNode.ip) && Objects.equals(port, wNode.port) && protocolEnum == wNode.protocolEnum;
        }

        @Override
        public int hashCode() {
            return Objects.hash(ip, port, protocolEnum);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if(protocolEnum!=null){
                sb.append(protocolEnum.name().toLowerCase());
            }
            sb.append("://");
            sb.append(this.ip);
            sb.append(":");
            sb.append(this.port);
            return sb.toString();
        }
    }

}
