package cn.aliberter.boot.common.security;

import cn.aliberter.boot.common.utils.SpringUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码工具类
 *
 * @author : aliberter
 * @version : 1.0
 */
public class PasswordUtil {

    private static final PasswordEncoder ENCODER = SpringUtil.getBean(PasswordEncoder.class);

    public static String encode(String password) {
        return ENCODER.encode(password);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return ENCODER.matches(rawPassword, encodedPassword);
    }

    private PasswordUtil() {

    }
}
