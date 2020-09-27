package cn.aliberter.boot.common.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * 全局唯一ID生成器
 *
 * @author : aliberter
 * @version : 1.0
 */
public class GlobalIdUtil {

    private static final Snowflake SNOWFLAKE = IdUtil.getSnowflake(1, 1);

    public static long simpleId() {
        return SNOWFLAKE.nextId();
    }

    public static String simpleIdStr() {
        return SNOWFLAKE.nextIdStr();
    }

    private GlobalIdUtil() {

    }
}
