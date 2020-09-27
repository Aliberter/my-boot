package cn.aliberter.boot.common.properties;

import cn.hutool.setting.Setting;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义参数配置
 *
 * @author : aliberter
 * @version : 1.0
 */
@Slf4j
public class CustomSetting {

    private static final Setting SETTING = new Setting("custom.setting");

    static {
        log.info("自定义参数配置已加载，总配置数：{}", SETTING.size());
    }

    public static String getStr(String key) {
        return SETTING.getStr(key);
    }

    public static String getStr(String key, String group) {
        return SETTING.getByGroup(key, group);
    }

    public static Integer getInt(String key) {
        return SETTING.getInt(key);
    }

    public static Long getLong(String key) {
        return SETTING.getLong(key);
    }

    private CustomSetting() {

    }
}
