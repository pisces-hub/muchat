package io.pisceshub.muchat.common.publics.sensitive.common.utils;

import io.pisceshub.muchat.common.publics.sensitive.common.handler.IHandler;

import java.util.*;

/**
 * @author xiaochangbai
 */
public class CollectionUtil {

    public static boolean isEmpty(Collection<?> denyList) {
        return denyList == null || denyList.size() < 1;
    }

    public static <K, V> List<K> toList(Iterable<V> values, IHandler<? super V, K> keyFunction) {
        return ObjectUtil.isNull(values) ? Collections.emptyList() : toList(values.iterator(), keyFunction);
    }

    public static <E> List<E> difference(Collection<E> collectionOne, Collection<E> collectionTwo) {
        Set<E> set = new LinkedHashSet();
        set.addAll(collectionOne);
        set.removeAll(collectionTwo);
        return new ArrayList(set);
    }

    public static <K, V> List<K> toList(Iterator<V> values, IHandler<? super V, K> keyFunction) {
        if (ObjectUtil.isNull(values)) {
            return Collections.emptyList();
        } else {
            List<K> list = new ArrayList();

            while (values.hasNext()) {
                V value = values.next();
                K key = keyFunction.handle(value);
                list.add(key);
            }

            return list;
        }
    }
}
