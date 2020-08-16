package cn.aliberter.boot.modules.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统用户表
 *
 * @author : aliberter
 * @version : 1.0
 */
@Data
@Accessors(chain = true)
@TableName(value = "sys_user")
@ApiModel(description = "系统用户表")
public class SysUser implements Serializable {
    /**
     * id
     */
    @TableId
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 是否冻结：0否 1是
     */
    @ApiModelProperty(value = "是否冻结：0否 1是")
    private Integer isLocked;

    /**
     * 是否删除：0否 1是
     */
    @TableLogic
    @ApiModelProperty(value = "是否删除：0否 1是")
    private Integer isDelete;

    private static final long serialVersionUID = 6230276659805628723L;
}

