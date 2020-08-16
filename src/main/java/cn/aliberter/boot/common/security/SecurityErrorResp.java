package cn.aliberter.boot.common.security;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * SpringSecurity响应信息<br>
 *
 * @author aliberter
 * @version : 1.0
 */
@Data
public class SecurityErrorResp {

    @ApiModelProperty(value = "时间戳")
    private final long timestamp = DateUtil.current(false);

    @ApiModelProperty(value = "响应码")
    private int status = HttpStatus.HTTP_FORBIDDEN;

    @ApiModelProperty(value = "错误原因")
    private String error = "Forbidden";

    @ApiModelProperty(value = "响应信息")
    private String message = "";

    @ApiModelProperty(value = "请求域")
    private String path;

    public SecurityErrorResp(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject(8, true);
        jsonObject.putOpt("timestamp", timestamp);
        jsonObject.putOpt("status", status);
        jsonObject.putOpt("error", error);
        jsonObject.putOpt("message", message);
        jsonObject.putOpt("path", path);
        return JSONUtil.toJsonPrettyStr(jsonObject);
    }
}
