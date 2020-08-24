package cn.aliberter.boot.common.config;

import cn.aliberter.boot.common.properties.CustomSetting;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : aliberter
 * @version : 1.0
 */
@Configuration
@AllArgsConstructor
public class WeChatConfig {

    @Bean
    public WxMpService wxMpService() {

        WxMpService service = new WxMpServiceImpl();
        WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();
        configStorage.setAppId(CustomSetting.getStr("wechat.appId"));
        configStorage.setSecret(CustomSetting.getStr("wechat.secret"));
        configStorage.setToken(CustomSetting.getStr("wechat.token"));
        service.setWxMpConfigStorage(configStorage);
        return service;
    }

    @Bean
    public WxMpMessageRouter messageRouter(WxMpService wxMpService) {
        return new WxMpMessageRouter(wxMpService);
    }
}
