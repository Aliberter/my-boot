package cn.aliberter.boot.common.exception;

import cn.aliberter.boot.common.core.JsonResult;
import cn.aliberter.boot.common.enums.ResultCode;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 异常统一处理类
 *
 * @author aliberter
 * @version : 1.0
 */
@Slf4j
@ControllerAdvice
@SuppressWarnings("unchecked")
public class ExceptionHandler {

    /**
     * 异常统一处理
     *
     * @param request  request
     * @param response response
     * @param e        e
     * @return JsonResult
     */
    @ResponseBody
    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    public JsonResult<Object> handler(HttpServletRequest request, HttpServletResponse response, Exception e) {

        //定义参数
        String type;
        Integer resultCode;
        String resultMessage;
        String rootCauseMessage;
        String className;
        String methodName;
        String lineNumber;
        String currentTime = DateUtil.now();

        //获取异常的类名方法名和行号
        StackTraceElement se = e.getStackTrace()[0];
        className = se.getClassName();
        methodName = se.getMethodName();
        lineNumber = String.valueOf(se.getLineNumber());

        Class<? extends Exception>[] exceptionClasses = new Class[]{
                BusinessException.class,
                MethodArgumentNotValidException.class};

        //异常分类
        if (ExceptionUtil.isCausedBy(e, exceptionClasses[0])) {
            BusinessException businessException = (BusinessException) e;
            type = "业务异常";
            String message = businessException.getMessage();
            if (JSONUtil.isJson(message)) {
                JSONObject jsonObject = JSONUtil.parseObj(message);
                resultCode = jsonObject.getInt("code");
                resultMessage = jsonObject.getStr("message");
            } else {
                resultCode = ResultCode.ERROR.getCode();
                resultMessage = message;
            }
            rootCauseMessage = "";
        } else if (ExceptionUtil.isCausedBy(e, exceptionClasses[1])) {
            List<FieldError> fieldErrors = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors();
            List<String> collect = fieldErrors.stream().map(error -> StrFormatter.format("[{}]{}", error.getField(), "字段不能为空")
            ).collect(Collectors.toList());
            type = "参数异常";
            resultCode = ResultCode.ERROR.getCode();
            resultMessage = String.join(";", collect);
            rootCauseMessage = "";
            className = "";
            methodName = "";
            lineNumber = "";
        } else {
            type = "系统异常";
            resultCode = ResultCode.ERROR.getCode();
            resultMessage = ExceptionUtil.getMessage(e);
            rootCauseMessage = ExceptionUtil.getRootCauseMessage(e);
            log.error("异常信息：", e);
        }
        //打印日志
        String errInfo = StrFormatter.format("发生了：{}，时间：{}，错误码：{}，错误信息：{}，异常根信息：{}，类名：{}，方法名：{}，行号：{}",
                type, currentTime, resultCode, resultMessage, rootCauseMessage, className, methodName, lineNumber);
        log.error(errInfo);
        return new JsonResult<>(resultCode, resultMessage);
    }
}
