package com.vrs.controller;

import com.vrs.common.config.LeMeiConFig;
import com.vrs.common.core.domain.AjaxResult;
import com.vrs.common.utils.StringUtils;
import com.vrs.common.utils.file.FileUploadUtils;
import com.vrs.domain.param.LoginParam;
import com.vrs.domain.param.UserEditParam;
import com.vrs.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yq 用户类
 * @CLassName UserController
 * @Description
 * @date 2022/8/26 15:15
 **/
@RestController
@RequestMapping(value = "/api/upload")
@Api(description = "文件上传",tags = "upload")
public class UploadController {
    @PostMapping("/uploadImg")
    @ApiOperation(value = "上传图片")
    public Object avatar(@RequestParam("file") MultipartFile file) throws IOException
    {
        if (!file.isEmpty())
        {
            String avatar = FileUploadUtils.upload(LeMeiConFig.getAvatarPath(), file);

            Map<String ,Object> map = new HashMap();
            map.put("imgUrl", avatar);
            return AjaxResult.success(map);
        }
        return AjaxResult.error("上传图片异常，请联系管理员");
    }
}
