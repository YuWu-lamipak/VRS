package com.lemei.adminweb.system;

import com.lemei.common.config.LeMeiConFig;
import com.lemei.common.core.domain.AjaxResult;
import com.lemei.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页
 *
 * @author ruoyi
 */
@RestController
public class SysIndexController
{


    /** 系统基础配置 */
    @Autowired
    private LeMeiConFig leMeiConFig;

    /**
     * 访问首页，提示语
     */
    @RequestMapping("/")
    public String index()
    {
        return StringUtils.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。", leMeiConFig.getName(), leMeiConFig.getVersion());
    }



}
