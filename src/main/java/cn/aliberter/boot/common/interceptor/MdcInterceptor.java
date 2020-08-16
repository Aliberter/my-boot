package cn.aliberter.boot.common.interceptor;

import cn.hutool.core.util.IdUtil;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * MDC拦截器
 *
 * @author : aliberter
 * @version : 1.0
 */
public class MdcInterceptor extends HandlerInterceptorAdapter {

    private static final String MDC_TRACE_ID = "traceId";

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        MDC.put(MDC_TRACE_ID, IdUtil.fastSimpleUUID());
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable Exception ex) throws Exception {
        MDC.remove(MDC_TRACE_ID);
        super.afterCompletion(request, response, handler, ex);
    }
}