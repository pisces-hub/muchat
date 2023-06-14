package io.xiaochangbai.muchat.connector.netty;

public interface IMServer {

    default boolean enable(){
        return false;
    }

    boolean isReady();

    void start();

    void stop();
}
