package com.vrs.quartz.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vrs.common.utils.StringUtils;
import com.vrs.quartz.domain.VRSSupplier;
import com.vrs.quartz.util.WebApi;
import com.vrs.quartz.service.RyTaskService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * 定时任务调度测试
 * 
 * @author ruoyi
 */
@Component("ryTask")
public class RyTask
{
    @Autowired
    private RyTaskService ryTaskService;

    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i)
    {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params)
    {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams()
    {
        System.out.println("执行无参方法");
    }


    @PostMapping("/getSupplier")
    @ResponseBody
    public void getSupplier(String str) throws SchedulerException {
        JSONObject jsonObjectbody =new JSONObject();
        jsonObjectbody.put("condition", "");
        jsonObjectbody.put("select", "*");
        jsonObjectbody.put("offset", "1");
        jsonObjectbody.put("size", "2000");
        jsonObjectbody.put("alias", "Id");
        jsonObjectbody.put("pk", "Id");
        jsonObjectbody.put("table", "VRS.dbo.SLC_SUPPLIER_A");

        JSONObject jsonObjectdata =new JSONObject();
        jsonObjectdata.put("data", jsonObjectbody);
        String postResult = WebApi.sendPostBysourceSystem("http://esb.lamipak.biz:18080/esb/sqlserver/api", jsonObjectdata.toJSONString(), "S_XXX_ESB_Vendorinfo_S", "TSS");
        JSONObject postResultJson = JSONObject.parseObject(postResult);
        JSONArray jsonObjectresult = postResultJson.getJSONArray("data");
        List<VRSSupplier> list = JSONObject.parseArray(jsonObjectresult.toJSONString(), VRSSupplier.class);
        ryTaskService.deleteVRSSuppliers();
        ryTaskService.updateVRSSupplier(list);
    }


}
