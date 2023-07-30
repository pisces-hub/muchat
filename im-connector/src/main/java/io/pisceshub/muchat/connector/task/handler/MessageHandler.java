package io.pisceshub.muchat.connector.task.handler;

public interface MessageHandler<T> {

    public void handler(T data);

}
