package io.pisceshub.muchat.common.core.enums;

public enum ResultCode {

  SUCCESS(200, "成功"), NO_LOGIN(400, "未登录"), INVALID_TOKEN(401, "token已失效"),
  PROGRAM_ERROR(500, "系统繁忙，请稍后再试"), PASSWOR_ERROR(10001, "密码不正确"),
  USERNAME_ALREADY_REGISTER(10003, "该用户名已注册"), NO_AVAILABLE_SERVICES(10004,
      "没有可用的服务"),

  COMMON_ERROR(501, "操作失败"), ANONYMOUSE_USER_NO_ACTION(502, "匿名用户禁止操作");

  private int code;
  private String msg;

  // 构造方法
  ResultCode(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}
