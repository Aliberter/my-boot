package cn.aliberter.boot.modules.jenkins.controller;

import cn.aliberter.boot.common.security.SecurityErrorResp;
import cn.aliberter.boot.modules.wechat.service.facial.SendMsgService;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JenkinsController
 *
 * @author : aliberter
 * @version : 1.0
 */
@Slf4j
@RestController
@AllArgsConstructor
@Api(tags = "Jenkins接口类")
public class JenkinsController {

    private static final String HEADER = "F@Z4>K*?s)hD]^BR:oum";

    private final SendMsgService sendMsgService;

    /**
     * Jenkins构建完成
     *
     * @param request  请求
     * @param response 响应
     */
    @ApiOperation(value = "Jenkins构建完成")
    @PostMapping(value = "/jenkins/success")
    public void jenkinsSuccess(HttpServletRequest request, HttpServletResponse response) {
        String authorization = request.getHeader("jenkins");
        if (HEADER.equals(authorization)) {
            response.setStatus(HttpStatus.HTTP_OK);
            ServletUtil.write(response, "项目启动完成😀", ContentType.TEXT_PLAIN.toString());
            sendMsgService.sendTemplateMsg();
        } else {
            response.setStatus(HttpStatus.HTTP_FORBIDDEN);
            ServletUtil.write(response, new SecurityErrorResp(request.getRequestURI()).toString(), ContentType.JSON.toString());
        }
    }
}
