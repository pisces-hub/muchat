package io.pisceshub.muchat.server.common.contant;


public class Constant {
    // 最大图片上传大小
    public static final long MAX_IMAGE_SIZE = 5*1024*1024;
    // 最大上传文件大小
    public static final long MAX_FILE_SIZE = 1000*1024*1024;
    // 群聊最大人数
    public static final long MAX_GROUP_MEMBER = 50000;
    // accessToken 过期时间(1小时)
    public static final Integer ACCESS_TOKEN_EXPIRE = 30 * 60;
    // refreshToken 过期时间(7天)
    public static final Integer REFRESH_TOKEN_EXPIRE = 7 * 24 * 60 * 60 ;
    // accessToken 加密秘钥
    public static final String ACCESS_TOKEN_SECRET = "MIIBIjANBgkq";
    // refreshToken 加密秘钥
    public static final String REFRESH_TOKEN_SECRET = "IKDiqVmn0VFU";

    public static final String[] defaultAnonymousUserHeader = new String[]{
      "http://43.138.164.74:9000/muchat/image/20230702/1688310033039.png",
            "http://43.138.164.74:9000/muchat/image/20230702/1688310042726.png",
            "http://43.138.164.74:9000/muchat/image/20230702/1688310058528.png",
            "http://43.138.164.74:9000/muchat/image/20230702/1688310067567.png",
            "http://43.138.164.74:9000/muchat/image/20230702/1688310078217.png",
            "http://43.138.164.74:9000/muchat/image/20230702/1688310088980.png",
            "http://43.138.164.74:9000/muchat/image/20230702/1688310101999.png",
            "http://43.138.164.74:9000/muchat/image/20230702/1688310113418.png",
            "http://43.138.164.74:9000/muchat/image/20230702/1688310123338.png",
            "http://43.138.164.74:9000/muchat/image/20230702/1688310132008.png",
            "http://43.138.164.74:9000/muchat/image/20230702/1688310140989.png",
            "http://43.138.164.74:9000/muchat/image/20230702/1688310150665.png"
    };

}
