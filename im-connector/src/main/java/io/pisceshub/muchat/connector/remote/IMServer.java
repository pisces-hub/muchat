package io.pisceshub.muchat.connector.remote;

public interface IMServer {

    default boolean enable() {
        return false;
    }

    void start();

    void stop();
}
