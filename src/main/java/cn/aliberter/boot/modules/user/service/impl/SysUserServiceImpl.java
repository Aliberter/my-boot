package cn.aliberter.boot.modules.user.service.impl;

import cn.aliberter.boot.common.core.JsonResult;
import cn.aliberter.boot.common.enums.ResultCode;
import cn.aliberter.boot.common.security.PasswordUtil;
import cn.aliberter.boot.common.security.TokenUtil;
import cn.aliberter.boot.common.utils.GlobalIdUtil;
import cn.aliberter.boot.modules.user.dto.LoginReqDto;
import cn.aliberter.boot.modules.user.dto.RegisterReqDto;
import cn.aliberter.boot.modules.user.entity.SysUser;
import cn.aliberter.boot.modules.user.mapper.SysUserMapper;
import cn.aliberter.boot.modules.user.service.facial.SysUserService;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

/**
 * 系统用户业务层
 *
 * @author : aliberter
 * @version : 1.0
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper sysUserMapper;
    private final AuthenticationManager authenticationManager;

    /**
     * 注册
     *
     * @param params 请求参数
     * @return JsonResult<Object>
     */
    @Override
    public JsonResult<Object> register(RegisterReqDto params) {
        //用户名
        String username = params.getUsername();
        //模拟前端对明文先做MD5加密
        String rawPassword = SecureUtil.md5(params.getPassword());
        //Spring Security再加密
        String password = PasswordUtil.encode(rawPassword);
        //封装SysUser对象
        SysUser sysUser = new SysUser().setId(GlobalIdUtil.simpleIdStr())
                .setUsername(username).setPassword(password);
        //入库
        try {
            sysUserMapper.insert(sysUser);
        } catch (DuplicateKeyException e) {
            return JsonResult.error(ResultCode.USER_EXISTED_ERROR);
        } catch (Exception e) {
            return JsonResult.error(ResultCode.USER_REGISTER_ERROR);
        }
        return JsonResult.ok();
    }

    /**
     * 登录
     *
     * @param params 请求参数
     * @return JsonResult<Object>
     */
    @Override
    public JsonResult<Object> login(LoginReqDto params) {
        //用户名
        String username = params.getUsername();
        //模拟前端对明文先做MD5加密
        String password = SecureUtil.md5(params.getPassword());
        try {
            //根据用户输入的用户名，通过loadUserByUsername方法去数据库中查找此用户，
            //查不到则抛出用户不存在异常，查到则loadUserByUsername方法会返回用户信息(密码)，
            //然后再对输入的密码与查到的数据库密码作对比，一致则验证通过，不一致则抛出密码错误异常。
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (AuthenticationException e) {
            return JsonResult.error(ResultCode.LOGIN_ERROR.getCode(), ExceptionUtil.getSimpleMessage(e));
        }
        //验证通过，生成token
        String token = TokenUtil.createToken(username, PasswordUtil.encode(password));
        return JsonResult.ok(token);
    }
}
