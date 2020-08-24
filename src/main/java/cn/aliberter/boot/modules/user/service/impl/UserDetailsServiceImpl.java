package cn.aliberter.boot.modules.user.service.impl;

import cn.aliberter.boot.common.enums.ResultCode;
import cn.aliberter.boot.common.exception.BusinessException;
import cn.aliberter.boot.modules.user.entity.SysUser;
import cn.aliberter.boot.modules.user.mapper.SysUserMapper;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author : aliberter
 * @version : 1.0
 */
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {
        SysUser sysUser = sysUserMapper.selectOne(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getUsername, username));
        if (Objects.isNull(sysUser)) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND_ERROR.getMessage());
        }
        //是否锁定
        boolean isLocked = BooleanUtil.toBoolean(String.valueOf(sysUser.getIsLocked()));
        return User.withUsername(username)
                .password(sysUser.getPassword())
                .authorities("admin")
                .accountExpired(false)
                .accountLocked(isLocked)
                .credentialsExpired(false)
                .disabled(isLocked)
                .build();
    }
}
