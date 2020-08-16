package cn.aliberter.boot.common.log;

import cn.aliberter.boot.common.core.JsonResult;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 日志切面类
 *
 * @author : aliberter
 * @version : 1.0
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    /**
     * 切点
     */
    @Pointcut("execution(public * cn.aliberter.boot.modules.*.controller.*.*(..))")
    public void requestLog() {
        //point cut
    }

    /**
     * 切点方法执行前执行此方法
     *
     * @param joinPoint joinPoint
     */
    @Before(value = "requestLog()")
    public void beforeRun(JoinPoint joinPoint) {
        //获取请求的类名
        String className = joinPoint.getTarget().getClass().getSimpleName();
        //获取执行方法名
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String methodName = method.getName();
        //获取方法参数值
        final Object[] argValues = joinPoint.getArgs();
        //获取方法说明
        String description = "";
        ApiOperation annotation = method.getAnnotation(ApiOperation.class);
        if (Objects.nonNull(annotation)) {
            description = annotation.value();
        }
        //打印日志
        log.info("START->[{}]: Class[{}] Method[{}] Params[{}]", description, className, methodName, JSONUtil.toJsonStr(argValues));
    }

    /**
     * 切点方法执行后执行此方法
     *
     * @param joinPoint joinPoint
     * @param ret       ret
     */
    @AfterReturning(returning = "ret", pointcut = "requestLog()")
    public void doAfterReturning(JoinPoint joinPoint, Object ret) {
        //获取方法说明
        String description = "";
        ApiOperation annotation = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(ApiOperation.class);
        if (Objects.nonNull(annotation)) {
            description = annotation.value();
        }
        //获取请求结果
        JsonResult<Object> jsonResult = Convert.convert(new TypeReference<JsonResult<Object>>() {
        }, ret);
        //打印日志
        if (Objects.isNull(jsonResult)) {
            jsonResult = JsonResult.ok();
        }
        log.info("ENDED->[{}]: ReturnCode[{}] ReturnMsg[{}]", description, jsonResult.getCode(), jsonResult.getMessage());
    }

    /**
     * 切点方法发生异常后执行此方法
     *
     * @param joinPoint joinPoint
     * @param ex        ex
     */
    @AfterThrowing(throwing = "ex", pointcut = "requestLog()")
    public void doAfterReturning(JoinPoint joinPoint, Throwable ex) {
        String description = "";
        ApiOperation annotation = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(ApiOperation.class);
        if (Objects.nonNull(annotation)) {
            description = annotation.value();
        }
        //log.error("ERROR->[{}]: Exception[{}]", description, ExceptionUtil.getSimpleMessage(ex));
    }
}