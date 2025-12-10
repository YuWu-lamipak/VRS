package com.lemei.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lemei.common.annotation.Log;
import com.lemei.common.core.domain.AjaxResult;
import com.lemei.common.core.domain.ResultData;
import com.lemei.common.core.domain.entity.SysUser;
import com.lemei.common.core.domain.model.LoginUser;
import com.lemei.common.enums.BusinessType;
import com.lemei.common.service.WeChatService;
import com.lemei.common.utils.DateUtils;
import com.lemei.common.utils.SecurityUtils;
import com.lemei.domain.param.*;
import com.lemei.service.ApplicationService;
import com.lemei.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 车辆预约API管理
 *
 * @author zhangpeng，wuyu
 * @date 2023-07-05
 **/
@RestController
@RequestMapping(value = "/api/application")
@Api(description = "车辆预约API管理", tags = "application")
public class CarApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private UserService userService;

    @Autowired
    private WeChatService weChatService;


    @ApiOperation("我的预约列表")
    @PostMapping(value = "/my/list", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getCarApplicationList(@RequestBody @Validated ApplicationQueryParam param) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        param.setCreatedBy(loginUser.getUserId());
        param.setDriverId(loginUser.getUserId());
        //获取当前所属当前用户的列表
        return AjaxResult.pageSuccess(applicationService.getMyPageList(param));
    }

    @ApiOperation("预约列表(审核端)")
    @PostMapping(value = "/check/list", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getCheckCarApplicationList(@RequestBody @Validated ApplicationQueryParam param) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //非待提交方式，则查询预约人是自己的数据

        if (param.getPostType() == null || !param.getPostType().equals(2)) {
            //非管理员，查询审核人自己的 数据
            if (!loginUser.getUser().isAdmin()) {
                param.setCheckerId(loginUser.getUserId());
            }
        }
        //提交方式是待提交时，则查询本人代预约数据
        if (param.getPostType() != null && param.getPostType().equals(2)) {
            param.setCreatedBy(loginUser.getUserId());
        }
        return AjaxResult.pageSuccess(applicationService.getPageList(param));
    }

    @ApiOperation("门卫列表")
    @PostMapping(value = "/guard/list", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getGuardCarApplicationList(@RequestBody @Validated ApplicationQueryParam param) {
        //查看审核通过的全部
        param.setStatus(2);
        return AjaxResult.pageSuccess(applicationService.getGuardPageList(param));
    }

    @ApiOperation("历史数据")
    @PostMapping(value = "/history/list", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getHistoryCarApplicationList(@RequestBody @Validated ApplicationQueryParam param) {
        //查看历史数据 - 即今天 23:59:59之前的数据
        param.setEndDate(DateUtils.dateTime("yyyy-MM-dd"));
        return AjaxResult.pageSuccess(applicationService.getPageList(param));
    }

    @ApiOperation("门卫首页数据统计")
    @GetMapping(value = "/guard/statistics", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object selectDataByStatus() {
        return AjaxResult.success(applicationService.selectDataByStatus());
    }

    @ApiOperation("审核首页数据统计")
    @GetMapping(value = "/checker/statistics", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object selectCheckerDataByStatus() {
        return AjaxResult.success(applicationService.selectCheckerDataByStatus());
    }

    @ApiOperation("本人首页数据统计")
    @GetMapping(value = "/self/statistics", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object selectSelfDataByStatus() {
        return AjaxResult.success(applicationService.selectSelfDataByStatus());
    }

    @ApiOperation("新增预约")
    @PostMapping(value = "/insert", produces = "application/json;charset=UTF-8")
    @Log(title = "新增车辆预约记录", businessType = BusinessType.INSERT)
    @ResponseBody
    @Transactional
    public Object insertCarApplication(@RequestBody @Validated ApplicationAddParam param) {
        Long maxSerialNumber = applicationService.selectMaxSerialNumber();
        if (maxSerialNumber == null) {
            maxSerialNumber = 0L;
        }
        param.setSerialNumber(maxSerialNumber + 1);
        //判断当前用户是否黑名单
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser.getUser().getIsBlacklist().equals(1)) {
            return AjaxResult.error("您已被禁止预约，请联系管理员");
        }
        if ("02".equals(loginUser.getUser().getUserType())) {
            return AjaxResult.error("司机无法预约");
        }
        //判断司机是否被拉入黑名单
        if (!"".equals(param.getIdcard())) {
            SysUser u = userService.getDetailByIdcard(param.getIdcard());
            if (u != null && u.getIsBlacklist() == 1) {
                return AjaxResult.error("该司机已被拉入黑名单");
            }
        }
        String carNumber = param.getCarNumber().replaceAll("[\\p{Z}\\s]+", "");
        String phone = param.getPhone().replaceAll("[\\p{Z}\\s]+", "");
        param.setCarNumber(carNumber);
        param.setPhone(phone);
        return applicationService.insertCarApplication(param);
    }

    @ApiOperation("修改预约单")
    @PutMapping(value = "/update", produces = "application/json;charset=UTF-8")
    @Log(title = "修改车辆预约记录", businessType = BusinessType.UPDATE)
    @ResponseBody
    public Object updateCarApplication(@RequestBody @Validated ApplicationEditParam param) {
        ResultData detail = applicationService.carApplicationDetail(param.getApplicationId());
        if (detail == null) {
            return AjaxResult.error("预约单不存在");
        }

        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (!detail.get("createdBy").equals(loginUser.getUserId())) {
            return AjaxResult.error("您没有修改的权限");
        }
        if (!detail.get("status").equals(1L)) {
            return AjaxResult.error("预约单已审核，不能修改");
        }
        //判断司机是否被拉入黑名单
        if (!"".equals(param.getIdcard())) {
            SysUser u = userService.getDetailByIdcard(param.getIdcard());
            if (u != null && u.getIsBlacklist() == 1) {
                return AjaxResult.error("该司机已被拉入黑名单");
            }
        }
        param.setCreatedName(detail.get("createdName").toString());
        String carNumber = param.getCarNumber().replaceAll("[\\p{Z}\\s]+", "");
        String phone = param.getPhone().replaceAll("[\\p{Z}\\s]+", "");
        param.setCarNumber(carNumber);
        param.setPhone(phone);

        return applicationService.updateCarApplication(param);
    }


    @ApiOperation("进厂")
    @PutMapping(value = "/enter", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object carApplicationEnter(@RequestBody @Validated ApplicationEditParam param) {
        ResultData detail = applicationService.carApplicationDetail(param.getApplicationId());
        if (detail == null) {
            return AjaxResult.error("预约单不存在");
        }
        if (!detail.get("enterStatus").equals(0L)) {
            return AjaxResult.error("状态异常");
        }
        param.setEnterDate(new Date());
        param.setEnterStatus(1);
        return AjaxResult.success(applicationService.updateApplication(param));
    }

    @ApiOperation("出厂")
    @PutMapping(value = "/out", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object carApplicationOut(@RequestBody @Validated ApplicationEditParam param) {
        ResultData detail = applicationService.carApplicationDetail(param.getApplicationId());
        if (detail == null) {
            return AjaxResult.error("预约单不存在");
        }
//        if (!detail.get("enterStatus").equals(1L)) {
//            return AjaxResult.error("状态异常");
//        }
        if (!detail.get("appointmentStatus").equals(6)) {
            return AjaxResult.error("状态异常");
        }
        param.setOutDate(new Date());
        param.setEnterStatus(2);
        return AjaxResult.success(applicationService.updateApplication(param));
    }

    @ApiOperation("空车")
    @PutMapping(value = "/emptyOut", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object carApplicationEmptyOut(@RequestBody @Validated ApplicationEditParam param) {
        param.setDownFloatingWeight(new BigDecimal(0));
        param.setUpFloatingWeight(new BigDecimal(50));
        param.setEditFlag("Yes");
        return AjaxResult.success(applicationService.updateApplication(param));
    }

    @ApiOperation("审核")
    @PutMapping(value = "/authen", produces = "application/json;charset=UTF-8")
    @Log(title = "审核", businessType = BusinessType.UPDATE)
    @ResponseBody
    public Object carApplicationAuthen(@RequestBody @Validated ApplicationEditParam param) {
        if (param.getStatus() != 2 && param.getStatus() != 3) {
            return AjaxResult.error("审核状态异常");
        }
        if (param.getStatus() != 3) {
            param = applicationService.updateApplicationWeight(param);
        }
        if (param.getStatus() == 3) {
            param.setEnterStatus(3);
            param.setAppointmentStatus(7);
        }
        if (param.getBusinessType() != null){
            if (param.getBusinessType() == 0){
                if (param.getSupplyCode() == null){
                    return AjaxResult.error("供应商未选择，审核失败");
                }
                if (param.getEnableControl() == null){
                    return AjaxResult.error("启用管控未选择，审核失败");
                }
            } else if (param.getBusinessType() == 1) {
                param.setAppointmentWeight(BigDecimal.ZERO);
                param.setUpFloatingWeight(BigDecimal.ZERO);
                param.setDownFloatingWeight(BigDecimal.ZERO);
            } else if (param.getBusinessType() == 2) {
                param.setAppointmentWeight(BigDecimal.ZERO);
                param.setUpFloatingWeight(BigDecimal.ZERO);
                param.setDownFloatingWeight(BigDecimal.ZERO);
                ResultData detail = applicationService.carApplicationDetail(param.getApplicationId());
                boolean b = applicationService.checkScrapAdvanceInfoExists(detail.get("carrierName").toString(), detail.get("applicationDate").toString());
                if (!b){
                    return AjaxResult.error("OA未查到废料预付款信息，审核失败");
                }
            }
        }
        ResultData detail = applicationService.carApplicationDetail(param.getApplicationId());
        if (detail == null) {
            return AjaxResult.error("预约单不存在");
        }
        if (!detail.get("status").equals(1L)) {
            return AjaxResult.error("该订单已审核，请勿重复操作");
        }
        param.setEditFlag("No");
        return AjaxResult.success(applicationService.updateApplication(param));
    }

//    @ApiOperation("删除订单")
//    @PutMapping(value = "/delete", produces = "application/json;charset=UTF-8")
//    @ResponseBody
//    public Object carApplicationDelete(@RequestBody @Validated ApplicationEditParam param) {
//
//        ResultData detail = applicationService.carApplicationDetail(param.getApplicationId());
//        if (detail == null) {
//            return AjaxResult.error("预约单不存在");
//        }
//        if (!detail.get("status").equals(1L)) {
//            return AjaxResult.error("该订单已审核，无法删除");
//        }
//        param.setIsDeleted(1);
//        return AjaxResult.success(applicationService.updateApplication(param));
//    }

    @ApiOperation("查看详情")
    @GetMapping(value = "/detail")
    @ResponseBody
    public Object getCarApplicationDetail(@RequestParam @Validated String applicationId) {
        ResultData detail = applicationService.carApplicationDetail(applicationId);
        return AjaxResult.success(detail);
    }

    @ApiOperation("原因列表")
    @GetMapping("/getReasonList")
    public AjaxResult reasonList() {
        return AjaxResult.success(applicationService.selectDictDataByType("application_reason"));
    }

    @ApiOperation("废料列表")
    @GetMapping("/getScrapList")
    public AjaxResult scrapList() {
        return AjaxResult.success(applicationService.selectDictDataByType("application_scrap"));
    }

    @ApiOperation("消息")
    @GetMapping("/message")
    public AjaxResult ceShi(@RequestParam("userId") String userId) {
        // 0.设置消息内容
        String content = "乐美包装（昆山）车辆预约审核提醒\n" + "预约承运商：x\n" + "预约人：xxx\n" + "预约车辆：xxxxxxx\n" + "预约时间：xxxx-xx-xx xx:xx:xx\n";
//        String content ="skffhk";
        //userId为企业用户的id
//        String userId = "WeiLanDeXing";
        // 3.发送消息：调用业务类，发送消息
        //消息内容如下
        // 乐美包装（昆山）车辆预约审核提醒
        //预约承运商：x
        //预约人：xxx
        //预约车辆：xxxxxxx
        //预约时间：xxxx-xx-xx xx:xx:xx
        // userId ="L210623";
        return AjaxResult.success(weChatService.sendMessage(userId, content));
    }

    @ApiOperation("供应商列表")
    @GetMapping("/getSupplierList")
    public AjaxResult getSupplierList() {
        return AjaxResult.success(applicationService.selectLmSupplierList());
    }

    @ApiOperation("制卡")
    @PostMapping(value = "/proCard", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject setCardInfo(@RequestBody String cardInfo) throws NullPointerException, JsonProcessingException {
        ApplicationEditParam editParam = new ApplicationEditParam();
        ApplicationQueryParam queryParam = new ApplicationQueryParam();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(cardInfo);
        //获取内部json字符串
        String innerJsonString = jsonNode.get("json").asText();
        //解析内部json字符串
        JsonNode innerNode = objectMapper.readTree(innerJsonString);
        String carNumber = innerNode.get("TruckNo1").asText();
        editParam.setCardId(innerNode.get("CardID").asText());
        editParam.setCardType(innerNode.get("CardType").asInt());
        editParam.setCarNumber(carNumber);
        editParam.setTruckNo2(innerNode.get("TruckNo2").asText());
        editParam.setIsUpdate(innerNode.get("isUpdate").asText());
        editParam.setTruckType(innerNode.get("TruckType").asInt());
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.now();
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        editParam.setCreateCardTime(date);
        java.sql.Date currentDate = java.sql.Date.valueOf(localDate);
        List<String> carNumberList = applicationService.selectCarNumberByApplicationDate(currentDate);
//        List<Integer> enterStatusList = applicationService.getEnterStatusByCarNumber(editParam.getCarNumber());
        JSONObject responseFail = new JSONObject();
        
        //未预约导致制卡失败
        if (!carNumberList.contains(carNumber)) {
            responseFail.put("Message", "制卡失败,此车牌号今日未预约或已制卡");
            responseFail.put("Code", "1");
            responseFail.put("Voice", "制卡失败,此车牌号今日未预约或已制卡");
            responseFail.put("$id", "1");
            responseFail.put("Result", null);
            return responseFail;
        }
//        if (enterStatusList.contains(1)) {
//            responseFail.put("Message", "制卡失败,此车牌号存在未出厂的记录");
//            responseFail.put("Code", "1");
//            responseFail.put("Voice", "制卡失败,此车牌号存在未出厂的记录");
//            responseFail.put("$id", "1");
//            responseFail.put("Result", null);
//            return responseFail;
//        }
        queryParam.setCarNumber(carNumber);
//        java.sql.Date applicationDateLatest = applicationService.selectApplicationDateByCarNumber(queryParam);
        queryParam.setApplicationDate(currentDate);
        queryParam.setAppointmentStatus(0);
        Long minSerialNumber = applicationService.getMinSerialNumberByCurrentTimeAndCarNumberAndAppointmentStatus(queryParam);
        queryParam.setSerialNumber(minSerialNumber);
        editParam.setSerialNumber(minSerialNumber);
//        Integer appointmentStatus = applicationService.selectAppointmentStatus(queryParam);
//        Integer status = applicationService.selectStatus(queryParam);
//        Integer enterStatus = applicationService.selectEnterStatus(queryParam);
        List<Integer> enterStatusList = applicationService.getEnterStatusByCarNumber(editParam.getCarNumber(), editParam.getSerialNumber());
        if (enterStatusList.contains(1)) {
            responseFail.put("Message", "制卡失败,此车牌号存在未出厂的记录");
            responseFail.put("Code", "1");
            responseFail.put("Voice", "制卡失败,此车牌号存在未出厂的记录");
            responseFail.put("$id", "1");
            responseFail.put("Result", null);
            return responseFail;
        }
        ResultData statusResult = applicationService.selectStatusResult(queryParam);
        Long status = (Long) statusResult.get("status");
        Long enterStatus = (Long) statusResult.get("enter_status");
        Integer appointmentStatus = (Integer) statusResult.get("appointmentStatus");
        //未释放状态导致制卡失败
        if (appointmentStatus == 1) {
            responseFail.put("Message", "制卡失败,此车牌号未释放预约状态：待上一磅");
            responseFail.put("Code", "1");
            responseFail.put("Voice", "制卡失败,此车牌号未释放预约状态：待上一磅");
            responseFail.put("$id", "1");
            responseFail.put("Result", null);
            return responseFail;
        } else if (appointmentStatus == 2) {
            responseFail.put("Message", "制卡失败,此车牌号未释放预约状态：待确认一磅");
            responseFail.put("Code", "1");
            responseFail.put("Voice", "制卡失败,此车牌号未释放预约状态：待确认一磅");
            responseFail.put("$id", "1");
            responseFail.put("Result", null);
            return responseFail;
        } else if (appointmentStatus == 3) {
            responseFail.put("Message", "制卡失败,此车牌号未释放预约状态：待上二磅");
            responseFail.put("Code", "1");
            responseFail.put("Voice", "制卡失败,此车牌号未释放预约状态：待上二磅");
            responseFail.put("$id", "1");
            responseFail.put("Result", null);
            return responseFail;
        } else if (appointmentStatus == 4) {
            responseFail.put("Message", "制卡失败,此车牌号未释放预约状态：待确认二磅");
            responseFail.put("Code", "1");
            responseFail.put("Voice", "制卡失败,此车牌号未释放预约状态：待确认二磅");
            responseFail.put("$id", "1");
            responseFail.put("Result", null);
            return responseFail;
        } else if (appointmentStatus == 5) {
            responseFail.put("Message", "制卡失败,此车牌号未释放预约状态：待退卡");
            responseFail.put("Code", "1");
            responseFail.put("Voice", "制卡失败,此车牌号未释放预约状态：待退卡");
            responseFail.put("$id", "1");
            responseFail.put("Result", null);
            return responseFail;
        }
        //审核状态为审核通过，进厂状态为已进厂，预约状态为待制卡才能制卡
        if (status == 2 && enterStatus == 1 && appointmentStatus == 0) {
            editParam.setAppointmentStatus(1);
            applicationService.updateCardInfo(editParam);
            JSONObject responseSuccess = new JSONObject();
            responseSuccess.put("Message", "制卡成功,可以刷卡上磅");
            responseSuccess.put("Code", "0");
            responseSuccess.put("Voice", "制卡成功,可以刷卡上磅");
            responseSuccess.put("$id", "1");
            responseSuccess.put("Result", null);
            return responseSuccess;
        }
        responseFail.put("Message", "制卡失败,请检查审核状态或进厂状态");
        responseFail.put("Code", "1");
        responseFail.put("Voice", "制卡失败,请检查审核状态或进厂状态");
        responseFail.put("$id", "1");
        responseFail.put("Result", null);
        return responseFail;
    }

    @ApiOperation("一磅确认上磅")
    @PostMapping(value = "/upOnePound", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getOneWeightingInfo(@RequestBody String oneWeightingInfo) throws JsonProcessingException {
        ApplicationEditParam editParam = new ApplicationEditParam();
        ApplicationQueryParam queryParam = new ApplicationQueryParam();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(oneWeightingInfo);
        //获取内部json字符串
        String innerJsonString = jsonNode.get("json").asText();
        //解析内部json字符串
        JsonNode innerNode = objectMapper.readTree(innerJsonString);
        String carNumber = innerNode.get("TruckNo1").asText();
        editParam.setCardId(innerNode.get("CardID").asText());
        editParam.setCarNumber(carNumber);
        editParam.setTruckNo2(innerNode.get("TruckNo2").asText());
        editParam.setLoadoMeterID1(innerNode.get("LoadoMeterID").asText());
        queryParam.setCarNumber(carNumber);
//        java.sql.Date applicationDateLatest = applicationService.selectApplicationDateByCarNumber(queryParam);
        LocalDate localDate = LocalDate.now();
        java.sql.Date currentDate = java.sql.Date.valueOf(localDate);
        queryParam.setApplicationDate(currentDate);
        queryParam.setAppointmentStatus(1);
        Long minSerialNumber = applicationService.getMinSerialNumberByCurrentTimeAndCarNumberAndAppointmentStatus(queryParam);
        queryParam.setSerialNumber(minSerialNumber);
        ResultData statusResult = applicationService.selectStatusResult(queryParam);
        Map<String, Object> map = new HashMap<>();
        if (statusResult == null) {
            map.put("error", "请检查入厂状态");
            JSONObject responseFail = new JSONObject();
            responseFail.put("Message", "一磅确认上磅失败,请检查入厂状态");
            responseFail.put("Code", "1");
            responseFail.put("Voice", "一磅确认上磅失败,请检查入厂状态");
            responseFail.put("$id", "1");
            responseFail.put("Result", map);
            return responseFail;
        }
        Long status = (Long) statusResult.get("status");
        ;
        Long enterStatus = (Long) statusResult.get("enter_status");
        Integer appointmentStatus = (Integer) statusResult.get("appointmentStatus");
        //审核状态为审核通过，进厂状态为已进厂，预约状态为待上一磅，将预约状态变为待确认一磅
        if (status != null && enterStatus != null && appointmentStatus != null) {
            // 三个集合都不为空
            if (status == 2 && enterStatus == 1 && appointmentStatus == 1) {
                ResultData resultData = applicationService.selectOneWeightingInfo(queryParam);
                Integer businessType = (Integer) resultData.get("businessType");
                String driverName = (String) resultData.get("driver_name");
                String orderId = (String) resultData.get("order_id");
                String supplyCode = (String) resultData.get("supplyCode");
                String supplyName = (String) resultData.get("supplyName");
                Long carrier_id = (Long) resultData.get("carrier_id");
                editParam.setAppointmentStatus(2);
                applicationService.updateOneWeightingInfo(editParam);
                map.put("ShipName", "");
                map.put("CarrierName", "");
                map.put("CustCode", "");
                map.put("BusinessType", businessType.toString());
                map.put("Driver", driverName);
                map.put("DocumentType", businessType.toString());
                map.put("AppointmentNo", orderId);
                map.put("ShipNo", "");
                map.put("CustName", "");
                map.put("MaterialDesc", "");
                map.put("SupplyCode", supplyCode);
                map.put("FactoryCode", "");
                map.put("AppointmentID", orderId);
                map.put("SupplyName", supplyName);
                map.put("CarrierCode", carrier_id.toString());
                map.put("DocumentCode", "");
                map.put("MaterialCode", "");
                map.put("CompanyCode", "");
                map.put("$id", "2");
                map.put("DocumentName", "");
                map.put("ATMSTackID", "");
                JSONObject responseSuccess = new JSONObject();
                responseSuccess.put("Message", "一磅确认上磅成功");
                responseSuccess.put("Code", "0");
                responseSuccess.put("Voice", "一磅确认上磅成功");
                responseSuccess.put("$id", "1");
                responseSuccess.put("Result", map);
                return responseSuccess;
            }
        }
        map.put("error", "请检查入厂状态");
        JSONObject responseFail = new JSONObject();
        responseFail.put("Message", "一磅确认上磅失败,请检查入厂状态");
        responseFail.put("Code", "1");
        responseFail.put("Voice", "一磅确认上磅失败,请检查入厂状态");
        responseFail.put("$id", "1");
        responseFail.put("Result", map);
        return responseFail;
    }

    @ApiOperation("一磅写入")
    @PostMapping(value = "/oneWrite", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject oneWeightWriteInfo(@RequestBody String oneWriteInfo) throws JsonProcessingException {
        ApplicationEditParam editParam = new ApplicationEditParam();
        ApplicationQueryParam queryParam = new ApplicationQueryParam();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(oneWriteInfo);
        //获取内部json字符串
        String innerJsonString = jsonNode.get("json").asText();
        //解析内部json字符串
        JsonNode innerNode = objectMapper.readTree(innerJsonString);
        String carNumber = innerNode.get("TruckNo1").asText();
        editParam.setCardId(innerNode.get("CardID").asText());
        editParam.setCarNumber(carNumber);
        editParam.setTruckNo2(innerNode.get("TruckNo2").asText());
        editParam.setTaskId(innerNode.get("TaskID").asText());
        editParam.setFirstWeight(new BigDecimal(innerNode.get("Weight").asText()));
        String firstWeightTime = innerNode.get("FirstWeightTime").asText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(firstWeightTime, formatter);
        Date date = java.sql.Timestamp.valueOf(dateTime);
        editParam.setFirstWeightTime(date);
        editParam.setLoadoMeterID1(innerNode.get("LoadoMeterID").asText());
        editParam.setAtmsId(innerNode.get("ATMSTaskID").asText());
        editParam.setUserID1(innerNode.get("UserID").asText());
        editParam.setUserName1(innerNode.get("UserName").asText());
        queryParam.setCarNumber(carNumber);
//        java.sql.Date applicationDateLatest = applicationService.selectApplicationDateByCarNumber(queryParam);
        LocalDate localDate = LocalDate.now();
        java.sql.Date currentDate = java.sql.Date.valueOf(localDate);
        queryParam.setApplicationDate(currentDate);
        queryParam.setAppointmentStatus(2);
        Long minSerialNumber = applicationService.getMinSerialNumberByCurrentTimeAndCarNumberAndAppointmentStatus(queryParam);
        queryParam.setSerialNumber(minSerialNumber);
        ResultData statusResult = applicationService.selectStatusResult(queryParam);
        Map<String, Object> map = new HashMap<>();
        if (statusResult == null) {
            map.put("error", "请检查预约状态");
            JSONObject responseFail = new JSONObject();
            responseFail.put("Message", "一磅写入失败,请检查预约状态");
            responseFail.put("Code", "1");
            responseFail.put("Voice", "一磅写入失败,请检查预约状态");
            responseFail.put("$id", "1");
            responseFail.put("Result", map);
            return responseFail;
        }
        Long status = (Long) statusResult.get("status");
        ;
        Long enterStatus = (Long) statusResult.get("enter_status");
        Integer appointmentStatus = (Integer) statusResult.get("appointmentStatus");
        //审核状态为审核通过，进厂状态为已进厂，此時预约状态为待确认一磅，将预约状态变为待上二磅
        if (status != null && enterStatus != null && appointmentStatus != null) {
            // 三个集合都不为空
            if (status == 2 && enterStatus == 1 && appointmentStatus == 2) {
                editParam.setAppointmentStatus(3);
                applicationService.updateOneWeightWriteInfo(editParam);
                map.put("ATMSTaskID", editParam.getAtmsId());
                map.put("TicketNo", editParam.getAtmsId());
                map.put("$id", "2");
                JSONObject responseSuccess = new JSONObject();
                responseSuccess.put("Message", "一磅写入成功");
                responseSuccess.put("Code", "0");
                responseSuccess.put("Voice", "一磅写入成功");
                responseSuccess.put("$id", "1");
                responseSuccess.put("Result", map);
                return responseSuccess;
            }
        }
        map.put("error", "未知原因");
        JSONObject responseFail = new JSONObject();
        responseFail.put("Message", "一磅写入失败,未知原因");
        responseFail.put("Code", "1");
        responseFail.put("Voice", "一磅写入失败,未知原因");
        responseFail.put("$id", "1");
        responseFail.put("Result", map);
        return responseFail;
    }

    @ApiOperation("二磅确认上磅")
    @PostMapping(value = "/upTwoPound", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getTwoWeightingInfo(@RequestBody String twoWeightingInfo) throws JsonProcessingException {
        ApplicationEditParam editParam = new ApplicationEditParam();
        ApplicationQueryParam queryParam = new ApplicationQueryParam();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(twoWeightingInfo);
        //获取内部json字符串
        String innerJsonString = jsonNode.get("json").asText();
        //解析内部json字符串
        JsonNode innerNode = objectMapper.readTree(innerJsonString);
        String carNumber = innerNode.get("TruckNo1").asText();
        editParam.setCardId(innerNode.get("CardID").asText());
        editParam.setCarNumber(carNumber);
        editParam.setTruckNo2(innerNode.get("TruckNo2").asText());
        editParam.setLoadoMeterID2(innerNode.get("LoadoMeterID").asText());
        editParam.setTaskId(innerNode.get("TaskID").asText());
        editParam.setAtmsId(innerNode.get("ATMSTaskID").asText());
        queryParam.setCarNumber(carNumber);
//        LocalDate localDate = LocalDate.now();
//        java.sql.Date currentDate = java.sql.Date.valueOf(localDate);
//        queryParam.setApplicationDate(currentDate);
        queryParam.setAppointmentStatus(3);
        java.sql.Date applicationDate = applicationService.selectApplicationDateByCarNumber(queryParam);
        queryParam.setApplicationDate(applicationDate);
        Long minSerialNumber = applicationService.getMinSerialNumberByCurrentTimeAndCarNumberAndAppointmentStatus(queryParam);
        queryParam.setSerialNumber(minSerialNumber);
        ResultData statusResult = applicationService.selectStatusResult(queryParam);
        Map<String, Object> map = new HashMap<>();
        if (statusResult == null) {
            map.put("error", "请检查预约状态");
            JSONObject responseFail = new JSONObject();
            responseFail.put("Message", "二磅确认上磅失败,请检查预约状态");
            responseFail.put("Code", "1");
            responseFail.put("Voice", "二磅确认上磅失败,请检查预约状态");
            responseFail.put("$id", "1");
            responseFail.put("Result", map);
            return responseFail;
        }
        Long status = (Long) statusResult.get("status");
        Long enterStatus = (Long) statusResult.get("enter_status");
        Integer appointmentStatus = (Integer) statusResult.get("appointmentStatus");
        Map<String, Object> mapTask = new HashMap<>();
        //审核状态为审核通过，进厂状态为已进厂，预约状态为待上二磅，将预约状态变为待确认二磅
        if (status != null && enterStatus != null && appointmentStatus != null) {
            // 三个集合都不为空
            if (status == 2 && enterStatus == 1 && appointmentStatus == 3) {
                ResultData resultData = applicationService.selectTwoWeightingInfo(queryParam);
                Integer businessType = (Integer) resultData.get("businessType");
                String driverName = (String) resultData.get("driver_name");
                String orderId = (String) resultData.get("order_id");
                String supplyCode = (String) resultData.get("supplyCode");
                String supplyName = (String) resultData.get("supplyName");
                Long carrier_id = (Long) resultData.get("carrier_id");
                BigDecimal appointmentWeight = (BigDecimal) resultData.get("appointmentWeight");
                if (businessType == 1) {
                    if (appointmentWeight.compareTo(BigDecimal.ZERO) == 0) {
                        editParam.setAppointmentStatus(3);
                        applicationService.updateTwoWeightingInfo(editParam);
                        map.put("error", carNumber + "不允许上二磅,销售重量未传入预约系统");
                        JSONObject responseFail = new JSONObject();
                        responseFail.put("Message", carNumber + "不允许上二磅,销售重量未传入预约系统");
                        responseFail.put("Code", "1");
                        responseFail.put("Voice", carNumber + "不允许上二磅,销售重量未传入预约系统");
                        responseFail.put("$id", "1");
                        responseFail.put("Result", map);
                        return responseFail;
                    }
                } else if (businessType == 2 || businessType == 4) {
                    if (appointmentWeight.compareTo(BigDecimal.ZERO) == 0) {
                        editParam.setAppointmentStatus(3);
                        applicationService.updateTwoWeightingInfo(editParam);
                        map.put("error", carNumber + "不允许上二磅,废料重量未传入预约系统");
                        JSONObject responseFail = new JSONObject();
                        responseFail.put("Message", carNumber + "不允许上二磅,废料重量未传入预约系统");
                        responseFail.put("Code", "1");
                        responseFail.put("Voice", carNumber + "不允许上二磅,废料重量未传入预约系统");
                        responseFail.put("$id", "1");
                        responseFail.put("Result", map);
                        return responseFail;
                    }
                }
                editParam.setAppointmentStatus(4);
                applicationService.updateTwoWeightingInfo(editParam);
                mapTask.put("ShipName", "");
                mapTask.put("CarrierName", "");
                mapTask.put("ATMSTaskID", editParam.getTaskId());
                mapTask.put("CustCode", "");
                mapTask.put("BusinessType", businessType.toString());
                mapTask.put("Driver", driverName);
                mapTask.put("DocumentType", businessType.toString());
                mapTask.put("AppointmentNo", orderId);
                mapTask.put("ShipNo", "");
                mapTask.put("CustName", "");
                mapTask.put("MaterialDesc", "");
                mapTask.put("SupplyCode", supplyCode);
                mapTask.put("State", "");
                mapTask.put("FactoryCode", "");
                mapTask.put("AppointmentID", orderId);
                mapTask.put("SupplyName", supplyName);
                mapTask.put("TicketNo", "");
                mapTask.put("CarrierCode", carrier_id.toString());
                mapTask.put("DocumentCode", "");
                mapTask.put("MaterialCode", "");
                mapTask.put("CompanyCode", "");
                mapTask.put("$id", "3");
                mapTask.put("DocumentName", "");
                mapTask.put("ATMSTackID", editParam.getAtmsId());
                map.put("Task", mapTask);
                map.put("Deduction", null);
                map.put("DeductionWeightTotal", "0");
                map.put("$id", "2");
                JSONObject responseSuccess = new JSONObject();
                responseSuccess.put("Message", "二磅确认上磅成功");
                responseSuccess.put("Code", "0");
                responseSuccess.put("Voice", "二磅确认上磅成功");
                responseSuccess.put("$id", "1");
                responseSuccess.put("Result", map);
                return responseSuccess;
            }
        }
        map.put("error", "未知原因");
        JSONObject responseFail = new JSONObject();
        responseFail.put("Message", "二磅确认上磅失败,未知原因");
        responseFail.put("Code", "1");
        responseFail.put("Voice", "二磅确认上磅失败,未知原因");
        responseFail.put("$id", "1");
        responseFail.put("Result", map);
        return responseFail;
    }

    @ApiOperation("二磅写入")
    @PostMapping(value = "/twoWrite", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject twoWeightWriteInfo(@RequestBody String twoWriteInfo) throws JsonProcessingException {
        ApplicationEditParam editParam = new ApplicationEditParam();
        ApplicationQueryParam queryParam = new ApplicationQueryParam();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(twoWriteInfo);
        //获取内部json字符串
        String innerJsonString = jsonNode.get("json").asText();
        //解析内部json字符串
        JsonNode innerNode = objectMapper.readTree(innerJsonString);
        String carNumber = innerNode.get("TruckNo1").asText();
        editParam.setCardId(innerNode.get("CardID").asText());
        editParam.setCarNumber(carNumber);
        editParam.setTruckNo2(innerNode.get("TruckNo2").asText());
        editParam.setLoadoMeterID2(innerNode.get("LoadoMeterID").asText());
        editParam.setUserID2(innerNode.get("UserID").asText());
        editParam.setUserName2(innerNode.get("UserName").asText());
        editParam.setTaskId(innerNode.get("TaskID").asText());
        editParam.setAtmsId(innerNode.get("ATMSTaskID").asText());
        editParam.setSecondWeight(new BigDecimal(innerNode.get("Gross").asText()));//毛
        editParam.setTruckWeight(new BigDecimal(innerNode.get("Tare").asText()));//皮
        editParam.setNetWeight(new BigDecimal(innerNode.get("Net").asText()));//净
        editParam.setPackWeight(new BigDecimal(innerNode.get("PackWeight").asText()));//扣
        editParam.setGoodsWeight(new BigDecimal(innerNode.get("Fact").asText()));//实
        String secondWeightTime = innerNode.get("SecondWeightTime").asText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(secondWeightTime, formatter);
        Date date = java.sql.Timestamp.valueOf(dateTime);
        editParam.setSecondWeightTime(date);
        queryParam.setCarNumber(carNumber);
//        LocalDate localDate = LocalDate.now();
//        java.sql.Date currentDate = java.sql.Date.valueOf(localDate);
//        queryParam.setApplicationDate(currentDate);
        queryParam.setAppointmentStatus(4);
        java.sql.Date applicationDate = applicationService.selectApplicationDateByCarNumber(queryParam);
        queryParam.setApplicationDate(applicationDate);
        Long minSerialNumber = applicationService.getMinSerialNumberByCurrentTimeAndCarNumberAndAppointmentStatus(queryParam);
        queryParam.setSerialNumber(minSerialNumber);
        ResultData statusResult = applicationService.selectStatusResult(queryParam);
        Map<String, Object> map = new HashMap<>();
        if (statusResult == null) {
            map.put("error", "请检查预约状态");
            JSONObject responseFail = new JSONObject();
            responseFail.put("Message", "二磅写入失败,请检查预约状态");
            responseFail.put("Code", "1");
            responseFail.put("Voice", "二磅写入失败,请检查预约状态");
            responseFail.put("$id", "1");
            responseFail.put("Result", map);
            return responseFail;
        }
        Long status = (Long) statusResult.get("status");
        ;
        Long enterStatus = (Long) statusResult.get("enter_status");
        Integer appointmentStatus = (Integer) statusResult.get("appointmentStatus");
        BigDecimal appointmentWeight = (BigDecimal) statusResult.get("appointmentWeight");
        int comparisonResult = appointmentWeight.compareTo(BigDecimal.ZERO);
        //审核状态为审核通过，进厂状态为已进厂，预约状态为待确认二磅，将预约状态变为待退卡
        if (status != null && enterStatus != null && appointmentStatus != null) {
            // 三个集合都不为空
            if (status == 2 && enterStatus == 1 && appointmentStatus == 4) {
                ResultData resultData = applicationService.selectUDGE(queryParam);
                Integer businessType = applicationService.selectBusinessType(queryParam);
                BigDecimal upFloatingWeight = (BigDecimal) resultData.get("upFloatingWeight");
                BigDecimal downFloatingWeight = (BigDecimal) resultData.get("downFloatingWeight");
                BigDecimal goodsWeight = editParam.getGoodsWeight();
                Integer enableControl = resultData.get("enableControl") != null ? (Integer) resultData.get("enableControl") : null;
                int down = goodsWeight.compareTo(downFloatingWeight);
                int up = goodsWeight.compareTo(upFloatingWeight);
//            //获取销售预约重量
//            BigDecimal saleAppointmentWeight = applicationService.getTotalGrossWeightByCarNumber(carNumber);
//            //获取正负容差
//            BigDecimal toleranceAdd = (BigDecimal) resultData.get("toleranceAdd");
//            BigDecimal toleranceDec = (BigDecimal) resultData.get("toleranceDec");
//            //获取销售的上下浮重量
//            BigDecimal saleUpFloatingWeight = (saleAppointmentWeight != null) ? saleAppointmentWeight.multiply(BigDecimal.ONE.add(toleranceAdd)) : BigDecimal.ZERO;
//            BigDecimal saleDownFloatingWeight = (saleAppointmentWeight != null) ? saleAppointmentWeight.multiply(BigDecimal.ONE.add(toleranceDec)) : BigDecimal.ZERO;
//            int saleDown = goodsWeight.compareTo(saleDownFloatingWeight);
//            int saleUp = goodsWeight.compareTo(saleUpFloatingWeight);
                //未启用管控直接退卡 0 1 null
                if (enableControl != null) {
                    if (enableControl == 0) {
                        editParam.setAppointmentStatus(5);
                        applicationService.updateTwoWeightWriteInfo(editParam);
                        map.put("$id", "2");
                        JSONObject responseSuccess = new JSONObject();
                        responseSuccess.put("Message", "二磅写入成功");
                        responseSuccess.put("Code", "0");
                        responseSuccess.put("Voice", "二磅写入成功");
                        responseSuccess.put("$id", "1");
                        responseSuccess.put("Result", map);
                        return responseSuccess;
                    } else if (enableControl == 1) {
                        //实kg在上浮重量和下浮重量之间直接退卡
                        if (down >= 0 && up <= 0) {
                            editParam.setAppointmentStatus(5);
                            applicationService.updateTwoWeightWriteInfo(editParam);
                            map.put("$id", "2");
                            JSONObject responseSuccess = new JSONObject();
                            responseSuccess.put("Message", "二磅写入成功");
                            responseSuccess.put("Code", "0");
                            responseSuccess.put("Voice", "二磅写入成功");
                            responseSuccess.put("$id", "1");
                            responseSuccess.put("Result", map);
                            return responseSuccess;
                        } else {
                            editParam.setAppointmentStatus(3);
                            applicationService.updateTwoWeightWriteInfo(editParam);
//                        applicationService.updateAppointmentStatus(editParam);
                            map.put("error", "二磅写入失败,二磅重量(实KG)不在下浮重量和上浮重量之间");
                            JSONObject responseFail = new JSONObject();
                            responseFail.put("Message", "二磅写入失败,二磅重量(实KG)不在下浮重量和上浮重量之间");
                            responseFail.put("Code", "1");
                            responseFail.put("Voice", "二磅写入失败,二磅重量(实KG)不在下浮重量和上浮重量之间");
                            responseFail.put("$id", "1");
                            responseFail.put("Result", map);
                            return responseFail;
                        }
                    }
                } else {
                    if (businessType == 1) {
                        //实kg在上浮重量和下浮重量之间直接退卡
                        if (down >= 0 && up <= 0) {
                            editParam.setAppointmentStatus(5);
                            applicationService.updateTwoWeightWriteInfo(editParam);
                            map.put("$id", "2");
                            JSONObject responseSuccess = new JSONObject();
                            responseSuccess.put("Message", "二磅写入成功");
                            responseSuccess.put("Code", "0");
                            responseSuccess.put("Voice", "二磅写入成功");
                            responseSuccess.put("$id", "1");
                            responseSuccess.put("Result", map);
                            return responseSuccess;
                        } else {
                            editParam.setAppointmentStatus(3);
                            applicationService.updateTwoWeightWriteInfo(editParam);
                            map.put("error", "二磅写入失败,二磅重量(实KG)不在下浮重量和上浮重量之间");
                            JSONObject responseFail = new JSONObject();
                            responseFail.put("Message", "二磅写入失败,二磅重量(实KG)不在下浮重量和上浮重量之间");
                            responseFail.put("Code", "1");
                            responseFail.put("Voice", "二磅写入失败,二磅重量(实KG)不在下浮重量和上浮重量之间");
                            responseFail.put("$id", "1");
                            responseFail.put("Result", map);
                            return responseFail;
                        }
                    }
//                    else if (businessType == 2){
//                        if (comparisonResult > 0) {
//                            editParam.setAppointmentStatus(5);
//                            applicationService.updateTwoWeightWriteInfo(editParam);
//                            map.put("$id", "2");
//                            JSONObject responseSuccess = new JSONObject();
//                            responseSuccess.put("Message", "二磅写入成功");
//                            responseSuccess.put("Code", "0");
//                            responseSuccess.put("Voice", "二磅写入成功");
//                            responseSuccess.put("$id", "1");
//                            responseSuccess.put("Result", map);
//                            return responseSuccess;
//                        }else {
//                            editParam.setAppointmentStatus(3);
//                            applicationService.updateTwoWeightWriteInfo(editParam);
//                            map.put("error", "二磅写入失败,废料重量未传入预约系统");
//                            JSONObject responseFail = new JSONObject();
//                            responseFail.put("Message", "二磅写入失败,废料重量未传入预约系统");
//                            responseFail.put("Code", "1");
//                            responseFail.put("Voice", "二磅写入失败,废料重量未传入预约系统");
//                            responseFail.put("$id", "1");
//                            responseFail.put("Result", map);
//                            return responseFail;
//                        }
//                    }
                    //这里需要修改业务类型，其他与固废危废无需处理（3,4）
                    else if (businessType == 2 || businessType == 3|| businessType == 4) {
                        editParam.setAppointmentStatus(5);
                        applicationService.updateTwoWeightWriteInfo(editParam);
                        map.put("$id", "2");
                        JSONObject responseSuccess = new JSONObject();
                        responseSuccess.put("Message", "二磅写入成功");
                        responseSuccess.put("Code", "0");
                        responseSuccess.put("Voice", "二磅写入成功");
                        responseSuccess.put("$id", "1");
                        responseSuccess.put("Result", map);
                        return responseSuccess;
                    }
                }
            }
        }
        map.put("error", "未知原因");
        JSONObject responseFail = new JSONObject();
        responseFail.put("Message", "二磅写入失败,未知原因");
        responseFail.put("Code", "1");
        responseFail.put("Voice", "二磅写入失败,未知原因");
        responseFail.put("$id", "1");
        responseFail.put("Result", map);
        return responseFail;
    }

    @ApiOperation("退卡")
    @PostMapping(value = "/refundCard", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject refundCardInfo(@RequestBody String cardInfo) throws JsonProcessingException {
        ApplicationEditParam editParam = new ApplicationEditParam();
        ApplicationQueryParam queryParam = new ApplicationQueryParam();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(cardInfo);
        //获取内部json字符串
        String innerJsonString = jsonNode.get("json").asText();
        //解析内部json字符串
        JsonNode innerNode = objectMapper.readTree(innerJsonString);
        String carNumber = innerNode.get("TruckNo1").asText();
        editParam.setCardId(innerNode.get("CardID").asText());
        editParam.setCardType(innerNode.get("CardType").asInt());
        editParam.setCarNumber(carNumber);
        editParam.setTruckNo2(innerNode.get("TruckNo2").asText());
        String refundTime = innerNode.get("RefundTime").asText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(refundTime, formatter);
        Date date = java.sql.Timestamp.valueOf(dateTime);
        editParam.setReturnCardTime(date);
        queryParam.setCarNumber(carNumber);
//        LocalDate localDate = LocalDate.now();
//        java.sql.Date currentDate = java.sql.Date.valueOf(localDate);
//        queryParam.setApplicationDate(currentDate);
        queryParam.setAppointmentStatus(5);
        java.sql.Date applicationDate = applicationService.selectApplicationDateByCarNumber(queryParam);
        queryParam.setApplicationDate(applicationDate);
        Long minSerialNumber = applicationService.getMinSerialNumberByCurrentTimeAndCarNumberAndAppointmentStatus(queryParam);
        queryParam.setSerialNumber(minSerialNumber);
        ResultData statusResult = applicationService.selectStatusResult(queryParam);
        Map<String, Object> map = new HashMap<>();
        if (statusResult == null) {
            map.put("error", "请检查预约状态");
            JSONObject responseFail = new JSONObject();
            responseFail.put("Message", "退卡失败,请检查预约状态");
            responseFail.put("Code", "1");
            responseFail.put("Voice", "退卡失败,请检查预约状态");
            responseFail.put("$id", "1");
            responseFail.put("Result", map);
            return responseFail;
        }
        Long status = (Long) statusResult.get("status");
        ;
        Long enterStatus = (Long) statusResult.get("enter_status");
        Integer appointmentStatus = (Integer) statusResult.get("appointmentStatus");
        //审核状态为审核通过，进厂状态为已进厂，预约状态为待退卡，将预约状态变为已完成
        if (status != null && enterStatus != null && appointmentStatus != null) {
            // 三个集合都不为空
            if (status == 2 && enterStatus == 1 && appointmentStatus == 5) {
                editParam.setAppointmentStatus(6);
                applicationService.updateRefundCardInfo(editParam);
                JSONObject responseSuccess = new JSONObject();
                responseSuccess.put("Message", "退卡成功");
                responseSuccess.put("Code", "0");
                responseSuccess.put("Voice", "退卡成功");
                responseSuccess.put("$id", "1");
                responseSuccess.put("Result", null);
                return responseSuccess;
            }
        }
        JSONObject responseFail = new JSONObject();
        responseFail.put("Message", "退卡失败");
        responseFail.put("Code", "1");
        responseFail.put("Voice", "退卡失败");
        responseFail.put("$id", "1");
        responseFail.put("Result", null);
        return responseFail;
    }

    @ApiOperation("sap导入销售交货单体")
    @PostMapping(value = "/saleDeliveryOrderBodyInfo", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getSaleDeliveryOrderBodyInfo(@RequestBody String saleDeliveryOrderBodyInfo) throws ParseException, InterruptedException {
        com.alibaba.fastjson.JSONObject response = new com.alibaba.fastjson.JSONObject();
        Gson gson = new Gson();
        JsonArray jsonArray = (JsonArray) gson.fromJson(saleDeliveryOrderBodyInfo, JsonArray.class);
        LocalDateTime localDateTime = LocalDateTime.now();
        Date currentTime = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        List<LmDeliveryItemField> itemsToInsertForInsert = new ArrayList<>();
        List<LmDeliveryItemField> itemsToUpdateForInsert = new ArrayList<>();
//        List<String> lmDnNumsToDeleteForInsert = new ArrayList<>();
//        List<String> lmDnLinesToDeleteForInsert = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 添加一个标志用于指示是否应该跳出循环
        boolean shouldBreak = false;
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            String actualDeliveryDate = jsonObject.get("lm_actualdeliverydate") != null && !jsonObject.get("lm_actualdeliverydate").isJsonNull() && !jsonObject.get("lm_actualdeliverydate").getAsString().isEmpty() ? jsonObject.get("lm_actualdeliverydate").getAsString() : "1900-01-01";
            String loadTime = jsonObject.get("lm_loadTime") != null && !jsonObject.get("lm_loadTime").isJsonNull() && !jsonObject.get("lm_loadTime").getAsString().isEmpty() ? jsonObject.get("lm_loadTime").getAsString() : "1900-01-01";
            Date date = format.parse(actualDeliveryDate);
            Date date1 = inputFormat.parse(loadTime.substring(0, 8));  // 解析日期字符串为 Date 对象
            String formattedDate = outputFormat.format(date1);  // 格式化日期为指定格式的字符串
            Date parse = outputFormat.parse(formattedDate);
            // 获取ITEM数组
            JsonArray itemArray = jsonObject.getAsJsonArray("ITEM");
            // 遍历ITEM数组
            for (JsonElement itemElement : itemArray) {
                LmDeliveryItemField lmDeliveryItemField = new LmDeliveryItemField();
                JsonObject itemObject = itemElement.getAsJsonObject();
                lmDeliveryItemField.setLmActualdeliverydate(date);
                lmDeliveryItemField.setLmLoadTime(parse);
                lmDeliveryItemField.setLmDnnum(itemObject.get("lm_dnnum") != null && !itemObject.get("lm_dnnum").isJsonNull() ? itemObject.get("lm_dnnum").getAsString() : "");
                lmDeliveryItemField.setLmCarNumber(itemObject.get("lm_car_number") != null && !itemObject.get("lm_car_number").isJsonNull() ? itemObject.get("lm_car_number").getAsString() : "");
                lmDeliveryItemField.setLmDnline(itemObject.get("lm_dnline") != null && !itemObject.get("lm_dnline").isJsonNull() ? itemObject.get("lm_dnline").getAsString() : "");
                lmDeliveryItemField.setLmDnlineref(itemObject.get("lm_dnlineref") != null && !itemObject.get("lm_dnlineref").isJsonNull() ? itemObject.get("lm_dnlineref").getAsString() : "");
                lmDeliveryItemField.setLmBatch(itemObject.get("lm_batch") != null && !itemObject.get("lm_batch").isJsonNull() ? itemObject.get("lm_batch").getAsString() : "");
                lmDeliveryItemField.setLmGrossweight(itemObject.get("lm_grossweight").getAsBigDecimal());
                lmDeliveryItemField.setLmWeightunit(itemObject.get("lm_weightunit") != null && !itemObject.get("lm_weightunit").isJsonNull() ? itemObject.get("lm_weightunit").getAsString() : "");
                lmDeliveryItemField.setLmPackagingid(itemObject.get("lm_packagingid") != null && !itemObject.get("lm_packagingid").isJsonNull() ? itemObject.get("lm_packagingid").getAsString() : "");
                if (lmDeliveryItemField.getLmWeightunit().equalsIgnoreCase("g")) {
                    lmDeliveryItemField.setLmGrossweight(lmDeliveryItemField.getLmGrossweight().divide(BigDecimal.valueOf(1000)));
                    lmDeliveryItemField.setLmWeightunit("KG");
                }
//                lmDnNumsToDeleteForInsert.add(lmDeliveryItemField.getLmDnnum());
//                lmDnLinesToDeleteForInsert.add(lmDeliveryItemField.getLmDnline());
//
                if (!lmDeliveryItemField.getLmDnlineref().equals("0")) {
                    boolean b = applicationService.checkRecordExists(lmDeliveryItemField.getLmDnnum(), lmDeliveryItemField.getLmDnline());
                    if (b) {
                        lmDeliveryItemField.setLmUpdateTime(currentTime);
                        itemsToUpdateForInsert.add(lmDeliveryItemField);
                    } else {
                        lmDeliveryItemField.setLmInsertTime(currentTime);
                        itemsToInsertForInsert.add(lmDeliveryItemField);
                    }
                }

//                if (!lmDeliveryItemField.getLmDnlineref().equals("0")) {
//                    boolean b = applicationService.checkRecordExists(lmDeliveryItemField.getLmDnnum(), lmDeliveryItemField.getLmDnline());
//                    if (b) {
//                        lmDeliveryItemField.setLmUpdateTime(currentTime);
//                        applicationService.updateLmSalesOrderItemByLmDnNumAndLmDnLine(lmDeliveryItemField);
//                    } else {
//                        lmDeliveryItemField.setLmInsertTime(currentTime);
//                        applicationService.insertLmDeliveryItemFieldThird(lmDeliveryItemField);
//                    }
//                }

                // 如果循环执行完毕后标志仍然为 false，表示循环已经完成
                if (!shouldBreak) {
                    // 设置标志为 true，表示循环已经完成
                    shouldBreak = true;
                }
            }

        }
//        applicationService.batchDeleteByLmDnNumAndLmDnLine(lmDnNumsToDeleteForInsert, lmDnLinesToDeleteForInsert);
//        applicationService.batchInsertLmDeliveryItemFieldThird(itemsToInsertForInsert);

        if (!itemsToUpdateForInsert.isEmpty()) {
            applicationService.batchUpdateLmDeliveryItemFieldThird(itemsToUpdateForInsert);
        }
        if (!itemsToInsertForInsert.isEmpty()) {
            applicationService.batchInsertLmDeliveryItemFieldThird(itemsToInsertForInsert);
        }

        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 将日期减去一天
        LocalDate yesterday = currentDate.minusDays(1);
        LocalDate yesterday2 = currentDate.minusDays(10);

        List<LmDeliveryItemField> lmDeliveryItemFieldList = applicationService.selectLmDeliveryItemFieldInsertList(java.sql.Date.valueOf(yesterday));
        List<LmDeliveryItemReal> itemsToInsertForReal = new ArrayList<>();
        List<LmDeliveryItemReal> itemsToUpdateForReal = new ArrayList<>();
//        List<String> lmDnNumsToDeleteForReal = new ArrayList<>();
//        List<String> lmDnLinesToDeleteForReal = new ArrayList<>();
        List<Map<String, String>> listByAppointmentStatus = applicationService.getMapByAppointmentStatus();
        Map<String, String> mapByAppointmentStatus = new HashMap<>();
        for (Map<String, String> row : listByAppointmentStatus) {
            String key = row.get("car_number");
            String value = row.get("order_id");
            mapByAppointmentStatus.put(key, value); // 存入Map
        }

        for (LmDeliveryItemField lmDeliveryItemField1 : lmDeliveryItemFieldList) {
            LmDeliveryItemReal lmDeliveryItemReal = new LmDeliveryItemReal();
            lmDeliveryItemReal.setLmId(UUID.randomUUID().toString());
            lmDeliveryItemReal.setLmDnnum(lmDeliveryItemField1.getLmDnnum());
            lmDeliveryItemReal.setLmActualdeliverydate(lmDeliveryItemField1.getLmActualdeliverydate());
            lmDeliveryItemReal.setLmLoadTime(lmDeliveryItemField1.getLmLoadTime());
            lmDeliveryItemReal.setLmDnline(lmDeliveryItemField1.getLmDnline());
            lmDeliveryItemReal.setLmOrderid(mapByAppointmentStatus.get(lmDeliveryItemField1.getLmCarNumber()));
            lmDeliveryItemReal.setLmCarNumber(lmDeliveryItemField1.getLmCarNumber());
            lmDeliveryItemReal.setLmPackagingid(lmDeliveryItemField1.getLmPackagingid());
            lmDeliveryItemReal.setLmGrossweight(lmDeliveryItemField1.getLmGrossweight());

//            lmDnNumsToDeleteForReal.add(lmDeliveryItemField1.getLmDnnum());
//            lmDnLinesToDeleteForReal.add(lmDeliveryItemField1.getLmDnline());

            Boolean b = applicationService.checkSaleExistsNoDate(lmDeliveryItemField1.getLmDnnum(), lmDeliveryItemField1.getLmDnline());
            if (b) {
                lmDeliveryItemReal.setLmUpdateTime(currentTime);
                itemsToUpdateForReal.add(lmDeliveryItemReal);
            } else {
                lmDeliveryItemReal.setLmInsertTime(currentTime);
                itemsToInsertForReal.add(lmDeliveryItemReal);
            }
        }
//        applicationService.batchDeleteByLmDnNumAndLmDnLineAndDate(java.sql.Date.valueOf(yesterday2), lmDnNumsToDeleteForReal, lmDnLinesToDeleteForReal);
//        applicationService.batchDeleteByLmDnNumAndLmDnLineReal(lmDnNumsToDeleteForReal, lmDnLinesToDeleteForReal);

        if (!itemsToUpdateForReal.isEmpty()) {
            applicationService.batchUpdateLmDeliveryItemFieldReal(itemsToUpdateForReal);
        }
        if (!itemsToInsertForReal.isEmpty()) {
            applicationService.batchInsertLmDeliveryItemFieldReal(itemsToInsertForReal);
        }
        applicationService.updateLmMatchFlag(java.sql.Date.valueOf(yesterday));

        applicationService.updateTotalGrossWeightByOrderId(java.sql.Date.valueOf(yesterday));
        // 跳出循环的条件：循环完全执行完毕并且标志为 true
        if (shouldBreak) {
            response.put("Message", "Success");
        } else {
            response.put("Message", "Fail");
        }

        return response;
    }

    @ApiOperation("sap导入供应商")
    @PostMapping(value = "/supplierHeadInfo", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getSupplierHeadInfo(@RequestBody String supplierHeadInfo) throws ParseException {
        ApplicationSupplier applicationSupplier = new ApplicationSupplier();
        com.alibaba.fastjson.JSONObject response = new com.alibaba.fastjson.JSONObject();
        Gson gson = new Gson();
        JsonArray jsonArray = (JsonArray) gson.fromJson(supplierHeadInfo, JsonArray.class);
        LocalDateTime localDateTime = LocalDateTime.now();
        Date currentTime = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        // 添加一个标志用于指示是否应该跳出循环
        boolean shouldBreak = false;
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            String code = jsonObject.get("code") != null && !jsonObject.get("code").isJsonNull() ? jsonObject.get("code").getAsString() : "";
            String name = jsonObject.get("name") != null && !jsonObject.get("name").isJsonNull() ? jsonObject.get("name").getAsString() : "";
            applicationSupplier.setID(code);
            applicationSupplier.setCode(code);
            applicationSupplier.setZName(name);
            boolean b = applicationService.checkIDExists(applicationSupplier.getID());
            if (b) {
                applicationSupplier.setUpdateTime(currentTime);
                applicationService.updateSupplierByID(applicationSupplier);
            } else {
                applicationSupplier.setInsertTime(currentTime);
                applicationService.insertSupplier(applicationSupplier);
            }
            // 如果循环执行完毕后标志仍然为 false，表示循环已经完成
            if (!shouldBreak) {
                // 设置标志为 true，表示循环已经完成
                shouldBreak = true;
            }
        }
        // 跳出循环的条件：循环完全执行完毕并且标志为 true
        if (shouldBreak) {
            response.put("Message", "Success");
        } else {
            response.put("Message", "Fail");
        }
        return response;
    }

    @ApiOperation("oa导入废料预付款信息")
    @PostMapping(value = "/scrapAdvanceInfo", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getScrapAdvanceInfo(@RequestBody String scrapAdvanceInfo) throws ParseException {
        ApplicationScrapAdvance applicationScrapAdvance = new ApplicationScrapAdvance();
        com.alibaba.fastjson.JSONObject response = new com.alibaba.fastjson.JSONObject();
        Gson gson = new Gson();
        JsonArray jsonArray = (JsonArray) gson.fromJson(scrapAdvanceInfo, JsonArray.class);
        LocalDateTime localDateTime = LocalDateTime.now();
        Date currentTime = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        // 添加一个标志用于指示是否应该跳出循环
        boolean shouldBreak = false;
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            String date = jsonObject.get("date")!= null && !jsonObject.get("date").isJsonNull() && !jsonObject.get("date").getAsString().isEmpty() ? jsonObject.get("date").getAsString() : "1900-01-01";
            String name = jsonObject.get("name") != null && !jsonObject.get("name").isJsonNull() ? jsonObject.get("name").getAsString() : "";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date d = format.parse(date);
            applicationScrapAdvance.setDateTime(d);
            applicationScrapAdvance.setZName(name);
            boolean b = applicationService.checkNameAndDateExists(applicationScrapAdvance.getZName(),applicationScrapAdvance.getDateTime());
            if (b) {
                applicationScrapAdvance.setModifyTime(currentTime);
                applicationService.updateScrapAdvanceByNameAndDate(applicationScrapAdvance);
            } else {
                applicationScrapAdvance.setModifyTime(currentTime);
                applicationService.insertScrapAdvance(applicationScrapAdvance);
            }
            // 如果循环执行完毕后标志仍然为 false，表示循环已经完成
            if (!shouldBreak) {
                // 设置标志为 true，表示循环已经完成
                shouldBreak = true;
            }
        }
        // 跳出循环的条件：循环完全执行完毕并且标志为 true
        if (shouldBreak) {
            response.put("Message", "Success");
        } else {
            response.put("Message", "Fail");
        }
        return response;
    }

    @ApiOperation("打印磅单请求查询DN与IDcard")
    @PostMapping(value = "/appointment/record/info", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getPrintWeightTicketInfo(@RequestBody PrintWeightTicketParam param) {
        try {
            ResultData result = applicationService.getPrintWeightTicketInfo(param.getOrderId(), param.getBusinessType());
            if (result != null) {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("orderid", result.get("order_id"));
                responseBody.put("idcard", result.get("IDCard"));
                responseBody.put("xdrivernameorders", result.get("driver_name"));
                responseBody.put("lmdnnum", result.get("lm_dnnum"));
                responseBody.put("carumber", result.get("car_number"));
                responseBody.put("businesstype", result.get("businessType"));
                
                Map<String, Object> response = new HashMap<>();
                response.put("code", 0);
                response.put("Message", "");
                response.put("body", responseBody);
                return response;
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 1);
                response.put("Message", "未找到相关记录");
                response.put("body", null);
                return response;
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 1);
            response.put("Message", "查询失败：" + e.getMessage());
            response.put("body", null);
            return response;
        }
    }

}
