package cn.aliberter.boot.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : aliberter
 * @version : 1.0
 */
@Getter
@AllArgsConstructor
public enum RedisKeyEnum {

    /**
     * 唯一性的Token签名Key
     */
    LOGIN_UNIQUE_SIGN("Token:{}");

    private final String key;
}
