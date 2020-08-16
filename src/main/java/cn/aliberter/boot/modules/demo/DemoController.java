package cn.aliberter.boot.modules.demo;

import cn.aliberter.boot.common.core.JsonResult;
import cn.aliberter.boot.common.properties.CustomSetting;
import cn.hutool.core.lang.Console;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * DemoController
 *
 * @author : aliberter
 * @version : 1.0
 */
@Slf4j
@RestController
@AllArgsConstructor
@Api(tags = "示例接口类")
public class DemoController {

    /**
     * 测试携带token请求
     *
     * @param params 请求参数
     * @return JsonResult<Object>
     */
    @ApiOperation(value = "示例接口Demo1")
    @PostMapping(value = "/demo1")
    public JsonResult<String> demo1(@RequestBody Map<String, Object> params) {
        Console.log("-----------{}", CustomSetting.getStr("token.secretKey"));
        return JsonResult.ok("身份校验通过，你已获得接口访问权限");
    }
}
