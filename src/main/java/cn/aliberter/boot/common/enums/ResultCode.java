package cn.aliberter.boot.common.enums;

import cn.hutool.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回码与返回信息
 *
 * @author aliberter
 * @version : 1.0
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    // 接口成功码
    SUCCESS(HttpStatus.HTTP_OK, "处理成功"),
    // 接口失败码
    ERROR(HttpStatus.HTTP_INTERNAL_ERROR, "服务器内部错误"),

    //业务错误码
    LOGIN_ERROR(10001, "登录失败"),
    USER_REGISTER_ERROR(10002, "注册失败"),
    USER_NOT_FOUND_ERROR(10003, "用户名不存在"),
    USER_EXISTED_ERROR(10004, "用户名已存在"),
    ;
    private final Integer code;
    private final String message;
}
