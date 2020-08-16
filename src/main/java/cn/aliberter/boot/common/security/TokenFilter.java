package cn.aliberter.boot.common.security;

import cn.aliberter.boot.common.enums.ResultCode;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Token校验过滤器
 *
 * @author : aliberter
 * @version : 1.0
 */
@Slf4j
@Component
public class TokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest httpServletRequest, @NonNull HttpServletResponse httpServletResponse, @NonNull FilterChain filterChain) {
        String token = TokenUtil.resolveToken(httpServletRequest);
        try {
            if (StrUtil.isNotBlank(token) && TokenUtil.validateToken(token)) {
                Authentication auth = TokenUtil.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
            httpServletResponse.setStatus(HttpStatus.HTTP_FORBIDDEN);
            ServletUtil.write(httpServletResponse, new SecurityErrorResp(httpServletRequest.getRequestURI()).toString(), ContentType.JSON.toString());
        }
    }
}
