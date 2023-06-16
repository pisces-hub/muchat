package io.pisceshub.muchat.common.core.contant;


import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class AppConst {

    // 在线状态过期时间 600s
    public static final long ONLINE_TIMEOUT_SECOND = 600;
    // 消息允许撤回时间 300s
    public static final long ALLOW_RECALL_SECOND = 300;


    public static final Integer RANDOM_MIN_PORT = 11111;

    //长连接服务器节点列表
    public static final Set<String> WS_NODES = new CopyOnWriteArraySet<>();
    public static final Set<String> TCP_NODES = new CopyOnWriteArraySet<>();
}
