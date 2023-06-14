package io.xiaochangbai.muchat.common.core.utils;

import io.xiaochangbai.muchat.common.core.contant.AppConst;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @description:
 * @author: xiaochangbai
 * @date: 2023/6/12 16:44
 */
public class MixUtils {

    private final static Set<Integer> USE_PORT = new HashSet<>(10);

    public static Integer findAvailablePort(){
        for(int i = 0;i<=1000;i++){
            Integer port = AppConst.RANDOM_MIN_PORT+i;
            if(USE_PORT.contains(port)){
                continue;
            }
            try(ServerSocket serverSocket = new ServerSocket(port)) {
                USE_PORT.add(port);
                System.out.println("可用端口:"+port);
                return port;
            } catch (IOException e) {
            }
        }
        throw new RuntimeException("没有可用的端口");
    }


    public static String getInet4Address() {
        Enumeration<NetworkInterface> nis;
        String ip = null;
        try {
            nis = NetworkInterface.getNetworkInterfaces();
            for (; nis.hasMoreElements();) {
                NetworkInterface ni = nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                for (; ias.hasMoreElements();) {
                    InetAddress ia = ias.nextElement();
                    //ia instanceof Inet6Address && !ia.equals("")
                    if (ia instanceof Inet4Address && !ia.getHostAddress().equals("127.0.0.1")) {
                        ip = ia.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ip;
    }

}
