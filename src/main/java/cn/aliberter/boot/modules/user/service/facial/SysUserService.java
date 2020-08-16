package cn.aliberter.boot.modules.user.service.facial;

import cn.aliberter.boot.common.core.JsonResult;
import cn.aliberter.boot.modules.user.dto.LoginReqDto;
import cn.aliberter.boot.modules.user.dto.RegisterReqDto;

/**
 * @author : aliberter
 * @version : 1.0
 */
public interface SysUserService {

    /**
     * 注册
     *
     * @param params 请求参数
     * @return JsonResult<Object>
     */
    JsonResult<Object> register(RegisterReqDto params);

    /**
     * 登录
     *
     * @param params 请求参数
     * @return JsonResult<Object>
     */
    JsonResult<Object> login(LoginReqDto params);
}
