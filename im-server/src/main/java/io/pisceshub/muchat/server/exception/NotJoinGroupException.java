package io.pisceshub.muchat.server.exception;

import io.pisceshub.muchat.common.core.enums.ResultCode;

/**
 * @author xiaochangbai
 * @date 2023-06-17 16:58
 */
public class NotJoinGroupException extends BusinessException {

  public NotJoinGroupException(ResultCode resultCode, String message) {
    code = resultCode.getCode();
    message = message;
  }

}
