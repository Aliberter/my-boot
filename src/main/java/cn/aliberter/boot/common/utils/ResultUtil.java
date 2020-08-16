package cn.aliberter.boot.common.utils;

import cn.aliberter.boot.common.enums.ResultCode;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.ReflectUtil;

/**
 * @author : aliberter
 * @version : 1.0
 */
public class ResultUtil {

    public static void setMsg(ResultCode resultCode, String message) {
        ReflectUtil.setFieldValue(resultCode, "message", StrFormatter.format(resultCode.getMessage(), message));
    }

    private ResultUtil() {

    }
}
