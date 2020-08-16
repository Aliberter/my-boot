package cn.aliberter.boot.modules.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author : aliberter
 * @version : 1.0
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "用户注册请求DTO")
public class RegisterReqDto {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Length(max = 32)
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Length(max = 64)
    @ApiModelProperty(value = "密码")
    private String password;
}
