package cn.aliberter.boot.common.exception;

import cn.aliberter.boot.common.enums.ResultCode;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.Serializable;

/**
 * 业务异常类
 *
 * @author aliberter
 * @version : 1.0
 */
public class BusinessException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 6423564066394407038L;

    public BusinessException(final String message) {
        super(message);
    }

    public BusinessException(final Integer code, final String message) {
        super(toJson(code, message));
    }

    public BusinessException(final ResultCode resultCode) {
        super(toJson(resultCode.getCode(), resultCode.getMessage()));
    }

    private static String toJson(final Integer code, final String message) {
        JSONObject jsonObject = new JSONObject(8, true);
        jsonObject.putOpt("code", code);
        jsonObject.putOpt("message", message);
        return JSONUtil.toJsonPrettyStr(jsonObject);
    }
}
