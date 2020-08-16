package cn.aliberter.boot;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.spring4all.swagger.EnableSwagger2Doc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author : aliberter
 * @version : 1.0
 */
@Slf4j
@EnableAsync
@EnableCaching
@EnableScheduling
@EnableSwagger2Doc
@EnableTransactionManagement
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
public class MyBootApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MyBootApplication.class, args);
    }

    /**
     * 项目环境信息
     *
     * @param args args
     * @throws Exception Exception
     */
    @Override
    public void run(String... args) throws Exception {
        log.info("项目启动完成😀");
    }
}
