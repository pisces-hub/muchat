package io.pisceshub.muchat.common.publics.sensitive.common.utils;


/**
 * @author xiaochangbai
 */
public class ObjectUtil {
    public static <V> boolean isNull(Object values) {
        return values==null;
    }

    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }
}
