package cn.aliberter.boot.common.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SpringSecurity过滤器配置
 *
 * @author : aliberter
 * @version : 1.0
 */
public class SecurityFilterConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Override
    public void configure(HttpSecurity http) {
        //Token鉴权过滤器
        http.addFilterBefore(new TokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}