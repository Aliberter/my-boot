package cn.aliberter.boot.common.security;

import cn.aliberter.boot.common.properties.CustomSetting;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsUtils;

/**
 * SpringSecurity配置
 *
 * @author : aliberter
 * @version : 1.0
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Boolean PERMIT_ALL = BooleanUtil.toBoolean(CustomSetting.getStr("security.permitAll"));
    private static final String[] WHITELIST = StrUtil.split(CustomSetting.getStr("security.whitelist"), StrUtil.COMMA);

    static {
        log.info("安全框架配置已加载");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.cors().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        if (Boolean.TRUE.equals(PERMIT_ALL)) {
            http.authorizeRequests().anyRequest().permitAll();
        } else {
            // 设置白名单
            http.authorizeRequests()
                    .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                    .antMatchers(WHITELIST).permitAll()
                    .anyRequest().authenticated();
        }

        http.exceptionHandling()
                //未经授权的请求
                .accessDeniedHandler((req, resp, e) -> {
                    resp.setStatus(HttpStatus.HTTP_FORBIDDEN);
                    ServletUtil.write(resp, new SecurityErrorResp(req.getRequestURI()).toString(), ContentType.JSON.toString());
                });

        //开启cookie保存用户数据register
        http.rememberMe();

        // 过滤器配置
        http.apply(new SecurityFilterConfig());
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/v2/api-docs")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/swagger-ui.html")
                .antMatchers("/doc.html")
                .antMatchers("/configuration/**")
                .antMatchers("/webjars/**")
                .antMatchers("/public")
                .antMatchers("/druid/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
