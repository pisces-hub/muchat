package io.pisceshub.muchat.common.core.contant;


public class AppConst {

    public static final long ONLINE_TIMEOUT_SECOND = 30;
    // 消息允许撤回时间 300s
    public static final long ALLOW_RECALL_SECOND = 300;

    /**
     * 长连接节点最小端口号
     */
    public static final Integer RANDOM_MIN_PORT = 11111;


    // accessToken 加密秘钥
    public static final String ACCESS_TOKEN_SECRET = "asdfj11akslf111asl)(*)(^afasf*&^*&@$FJSLDKJF1234123";
    // refreshToken 加密秘钥
    public static final String REFRESH_TOKEN_SECRET = "089JJOFSF&^*(qjworwjqjafa2LJ4LJJ33";

    // accessToken 过期时间(6小时)
    public static final Integer ACCESS_TOKEN_EXPIRE = 6* 60 * 60;
    // refreshToken 过期时间(7天)
    public static final Integer REFRESH_TOKEN_EXPIRE = 7 * 24 * 60 * 60 ;

}
