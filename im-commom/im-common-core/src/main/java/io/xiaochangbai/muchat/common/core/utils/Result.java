package io.xiaochangbai.muchat.common.core.utils;

import lombok.Data;


@Data
public class Result<T> {


    private int code;

    private String message;

    private T data;

}
