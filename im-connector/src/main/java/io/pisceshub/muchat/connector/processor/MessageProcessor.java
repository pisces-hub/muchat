package io.pisceshub.muchat.connector.processor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.netty.channel.ChannelHandlerContext;
import io.pisceshub.muchat.common.core.contant.AppConst;
import io.pisceshub.muchat.common.core.utils.JwtUtil;
import io.pisceshub.muchat.connector.utils.SendMessageUtils;

public interface MessageProcessor {

  void process(ChannelHandlerContext ctx, Object data);

  default Long parseUserId(ChannelHandlerContext ctx, String token) {
    if (StrUtil.isEmpty(token)) {
      SendMessageUtils.sendError(ctx, "未登录");
      throw new IllegalArgumentException("未登录");
    }

    try {
      // 验证 token
      JwtUtil.checkSign(token, AppConst.ACCESS_TOKEN_SECRET);
    } catch (JWTVerificationException e) {
      SendMessageUtils.sendError(ctx, "token已失效");
      throw new IllegalArgumentException("token已失效");
    }

    try {
      return JwtUtil.getUserId(token);
    } catch (Exception e) {
      SendMessageUtils.sendError(ctx, "token有误");
      throw new IllegalArgumentException("token有误");
    }
  }

}
