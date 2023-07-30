package io.pisceshub.muchat.common.publics.sensitive.common.instance;

/**
 * @author xiaochangbai
 */
public final class Instances {

    private Instances(){
    }

    public static <T> T singleton(Class<T> tClass) {
        return InstanceFactory.getInstance().singleton(tClass);
    }

    public static <T> T singleton(Class<T> tClass, String groupName) {
        return InstanceFactory.getInstance().singleton(tClass, groupName);
    }

    public static <T> T threadLocal(Class<T> tClass) {
        return InstanceFactory.getInstance().threadLocal(tClass);
    }

    public static <T> T threadSafe(Class<T> tClass) {
        return InstanceFactory.getInstance().threadSafe(tClass);
    }

    public static <T> T multiple(Class<T> tClass) {
        return InstanceFactory.getInstance().multiple(tClass);
    }
}
