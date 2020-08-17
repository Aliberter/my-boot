package cn.aliberter.boot.modules.wechat.service.impl;

import cn.aliberter.boot.common.properties.CustomSetting;
import cn.aliberter.boot.modules.wechat.service.facial.SendMsgService;
import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author : aliberter
 * @version : 1.0
 */
@Slf4j
@Service
@AllArgsConstructor
public class SendMsgServiceImpl implements SendMsgService {

    private final WxMpService wxService;

    /**
     * 发送模板消息
     */
    @Async
    @Override
    public void sendTemplateMsg() {

        try {

            WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                    .toUser(CustomSetting.getStr("wechat.users"))
                    .templateId(CustomSetting.getStr("wechat.templateId"))
                    .build();

            templateMessage.addData(new WxMpTemplateData("first",
                    "尊敬的开发者：\r\n\r\n您的项目已经构建完成。\r\n "))
                    .addData(new WxMpTemplateData("keyword1", "my-boot"))
                    .addData(new WxMpTemplateData("keyword2", DateUtil.now()));

            this.wxService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (Exception e) {
            log.error("发送模板消息异常", e);
        }
    }
}
