package io.pisceshub.muchat.server.tcp.algorithm.consistenthash;



import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Function:TreeMap 实现
 */
public class TreeMapConsistentHash extends AbstractConsistentHash {
    private TreeMap<Long,String> treeMap;

    /**
     * 虚拟节点数量
     */
    private int virtualNodeSize;

    public TreeMapConsistentHash(){
        treeMap = new TreeMap<Long, String>();
        virtualNodeSize = 2;
    }
    public TreeMapConsistentHash(Collection<String> values, int virtualNodeSize){
        treeMap = new TreeMap<Long, String>();
        this.virtualNodeSize = virtualNodeSize;
        this.reBuildRing(values);
    }


    @Override
    public void add(long key, String value) {

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
    public String getFirstNodeValue(String value) {
        long hash = super.hash(value);
        SortedMap<Long, String> last = treeMap.tailMap(hash);
        if (!last.isEmpty()) {
            return last.get(last.firstKey());
        }
        if (treeMap.size() == 0){
            throw new RuntimeException() ;
        }
        return treeMap.firstEntry().getValue();
    }
}
