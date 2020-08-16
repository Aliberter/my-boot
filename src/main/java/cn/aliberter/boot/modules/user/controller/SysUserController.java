package cn.aliberter.boot.modules.user.controller;

import cn.aliberter.boot.common.core.JsonResult;
import cn.aliberter.boot.modules.user.dto.LoginReqDto;
import cn.aliberter.boot.modules.user.dto.RegisterReqDto;
import cn.aliberter.boot.modules.user.service.facial.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : aliberter
 * @version : 1.0
 */
@RestController
@AllArgsConstructor
@Api(tags = "用户接口类")
public class SysUserController {

    private final SysUserService service;

    /**
     * 注册
     *
     * @param params 请求参数
     * @return JsonResult<Object>
     */
    @ApiOperation(value = "注册")
    @PostMapping(value = "/register")
    public JsonResult<Object> register(@RequestBody @Validated RegisterReqDto params) {
        return service.register(params);
    }

    /**
     * 登录
     *
     * @param params 请求参数
     * @return JsonResult<Object>
     */
    @ApiOperation(value = "登录")
    @PostMapping(value = "/login")
    public JsonResult<Object> login(@RequestBody @Validated LoginReqDto params) {
        return service.login(params);
    }
}
