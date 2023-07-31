package io.pisceshub.muchat.server.util;

import cn.hutool.core.util.StrUtil;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class SecurityUtils {

  public static String base64Obj(String str) {
    if (StrUtil.isBlank(str)) {
      return null;
    }
    // Base64 加密
    // encodeToString 返回String
    return Base64.getEncoder().encodeToString(str.trim().getBytes(StandardCharsets.UTF_8));
  }

}
