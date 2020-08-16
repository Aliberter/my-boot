package cn.aliberter.boot.utils;

import cn.aliberter.boot.MyBootApplication;
import cn.aliberter.boot.common.cache.LocalCache;
import cn.hutool.core.lang.Console;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author : wangsj
 * @version : 1.0
 * @date : 2020/7/14 下午5:33
 */
@Slf4j
@WebAppConfiguration
@ContextConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyBootApplication.class)
public class JasyptUtil {

    @Autowired
    private StringEncryptor encryptor;

    @Test
    public void encrypt() {
        String toBeEncrypted = "Wsj19940429@!_redis";
        Console.log("The encrypt text is: {}", encryptor.encrypt(toBeEncrypted));
    }

    @Test
    public void decrypt() {
        String toBeDecrypted = "";
        Console.log("The decrypt text is: {}", encryptor.decrypt(toBeDecrypted));
    }
}
