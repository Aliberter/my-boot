package cn.aliberter.boot.common.core;

import cn.aliberter.boot.common.enums.ResultCode;
import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口统一返回类
 *
 * @author : aliberter
 * @version : 1.0
 */
@Data
@ApiModel("JSON返回结果")
public class JsonResult<T> implements Serializable {

    private static final long serialVersionUID = -7134993993446969970L;

    @ApiModelProperty("成功标识")
    private boolean success;
    @ApiModelProperty("返回码")
    private Integer code;
    @ApiModelProperty("返回信息")
    private String message;
    @ApiModelProperty("返回数据")
    private T data;
    @ApiModelProperty("返回时间")
    private String time = DateUtil.now();

    /**
     * 返回成功：默认成功码与成功信息，无业务数据
     *
     * @param <T> 数据类型
     * @return 数据类型
     */
    public static <T> JsonResult<T> ok() {
        return new JsonResult<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    /**
     * 返回成功：默认成功码与成功信息，带业务数据（常用）
     *
     * @param <T> 数据类型
     * @return 数据类型
     */
    public static <T> JsonResult<T> ok(T data) {
        return new JsonResult<>(data);
    }

    /**
     * 返回成功：默认成功码，带成功信息，业务数据(少数情况使用)
     *
     * @param <T> 数据类型
     * @return 数据类型
     */
    public static <T> JsonResult<T> ok(String message, T data) {
        return new JsonResult<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 返回失败：带错误码，错误信息（常用）
     *
     * @param <T> 数据类型
     * @return 数据类型
     */
    public static <T> JsonResult<T> error(Integer code, String message) {
        return new JsonResult<>(code, message);
    }

    /**
     * 返回失败：带错误码，错误信息（方便书写，直接传枚举类即可）
     *
     * @param resultCode 结果码枚举类
     * @param <T>        数据类型
     * @return 数据类型
     */
    public static <T> JsonResult<T> error(ResultCode resultCode) {
        return new JsonResult<>(resultCode.getCode(), resultCode.getMessage());
    }

    /**
     * 返回失败：默认失败参数（少数情况下使用）
     *
     * @param <T> 数据类型
     * @return 数据类型
     */
    public static <T> JsonResult<T> error(Integer code, String message, T data) {
        return new JsonResult<>(false, code, message, data);
    }

    /**
     * 返回失败：默认失败参数（少数情况下使用，比如业务错误时需要返回某些业务数据）
     * 方便书写，直接传枚举类即可
     *
     * @param resultCode 结果码枚举类
     * @param <T>        数据类型
     * @return 数据类型
     */
    public static <T> JsonResult<T> error(ResultCode resultCode, T data) {
        return new JsonResult<>(false, resultCode.getCode(), resultCode.getMessage(), data);
    }

//    构造方法

    public JsonResult() {
    }

    /**
     * 成功状态：
     *
     * @param data 返回数据
     */
    public JsonResult(T data) {
        this.success = true;
        this.data = data;
        this.code = ResultCode.SUCCESS.getCode();
        this.message = ResultCode.SUCCESS.getMessage();
    }

    /**
     * 成功状态：
     *
     * @param code    返回码
     * @param message 返回信息
     * @param data    返回数据
     */
    public JsonResult(Integer code, String message, T data) {
        this.success = true;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 失败状态：
     *
     * @param code    返回码
     * @param message 返回信息
     */
    public JsonResult(Integer code, String message) {
        this.success = false;
        this.code = code;
        this.message = message;
    }

    /**
     * 成功/失败通用：
     *
     * @param success 是否成功
     * @param code    返回码
     * @param message 返回信息
     * @param data    返回数据
     */
    public JsonResult(boolean success, Integer code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
