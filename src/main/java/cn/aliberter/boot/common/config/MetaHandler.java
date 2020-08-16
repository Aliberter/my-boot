package cn.aliberter.boot.common.config;

import cn.aliberter.boot.common.utils.GlobalIdUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * 执行SQL时自动维护创建时间与更新时间
 *
 * @author : aliberter
 * @version : 1.0
 */
@Slf4j
@Component
public class MetaHandler implements MetaObjectHandler {

    /**
     * 新增数据执行
     *
     * @param metaObject metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("id", GlobalIdUtil.simpleIdStr(), metaObject);
        this.setFieldValByName("createTime", DateUtil.date(), metaObject);
    }

    /**
     * 更新数据执行
     *
     * @param metaObject metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", DateUtil.date(), metaObject);
    }

}
