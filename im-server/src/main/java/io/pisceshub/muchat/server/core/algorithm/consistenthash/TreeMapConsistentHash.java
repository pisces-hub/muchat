package io.pisceshub.muchat.server.core.algorithm.consistenthash;



import io.pisceshub.muchat.server.core.NodeContainer;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Function:TreeMap 实现
 */
public class TreeMapConsistentHash extends AbstractConsistentHash {
    private TreeMap<Long,NodeContainer.WNode> treeMap;

    /**
     * 虚拟节点数量
     */
    private int virtualNodeSize;

    public TreeMapConsistentHash(){
        treeMap = new TreeMap<Long, NodeContainer.WNode>();
        virtualNodeSize = 2;
    }
    public TreeMapConsistentHash(Collection<NodeContainer.WNode> values, int virtualNodeSize){
        treeMap = new TreeMap<Long, NodeContainer.WNode>();
        this.virtualNodeSize = virtualNodeSize;
        this.reBuildRing(values);
    }


    @Override
    public void add(long key, NodeContainer.WNode value) {

        for (int i = 0; i < virtualNodeSize; i++) {
            Long hash = super.hash("vir" + key + i*1000);
            treeMap.put(hash,value);
        }
        treeMap.put(key, value);
    }

    @Override
    public void clear(){
        treeMap.clear();
    }

    @Override
    public NodeContainer.WNode getFirstNodeValue(String value) {
        long hash = super.hash(value);
        SortedMap<Long, NodeContainer.WNode> last = treeMap.tailMap(hash);
        if (!last.isEmpty()) {
            return last.get(last.firstKey());
        }
        if (treeMap.size() == 0){
            throw new RuntimeException() ;
        }
        return treeMap.firstEntry().getValue();
    }
}
