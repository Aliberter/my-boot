package cn.aliberter.boot.common.enums;

import lombok.AllArgsConstructor;

/**
 * 日志信息枚举
 *
 * @author : aliberter
 * @version : 1.0
 */
@AllArgsConstructor
public enum LogMsgEnum {

    //redis异常
    REDIS_EXCEPTION_LOG("Redis Exception：");

    private final String message;

    public String print() {
        return this.message;
    }
}
