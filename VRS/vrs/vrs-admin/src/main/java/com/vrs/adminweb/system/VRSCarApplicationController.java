package com.vrs.adminweb.system;


import com.alibaba.fastjson.JSONObject;
import com.vrs.common.annotation.Log;
import com.vrs.common.core.controller.BaseController;
import com.vrs.common.core.domain.AjaxResult;
import com.vrs.common.core.domain.entity.SysRole;
import com.vrs.common.core.domain.entity.SysUser;
import com.vrs.common.core.domain.model.LoginUser;
import com.vrs.common.core.page.TableDataInfo;
import com.vrs.common.enums.BusinessType;
import com.vrs.common.service.WeChatService;
import com.vrs.common.utils.DataHandleUtil;
import com.vrs.common.utils.DateUtils;
import com.vrs.common.utils.SecurityUtils;
import com.vrs.common.utils.poi.ExcelUtil;
import com.vrs.common.utils.uuid.IdUtils;
import com.vrs.quartz.domain.VRSSupplier;
import com.vrs.quartz.service.RyTaskService;
import com.vrs.system.domain.VRSCarApplication;
import com.vrs.system.domain.param.CarApplicationQueryParam;
import com.vrs.system.domain.param.CarDeliveryItemReal;
import com.vrs.system.domain.param.CarSaleQueryParam;
import com.vrs.system.mapper.SysUserMapper;
import com.vrs.system.service.IVRSCarApplicationService;
import com.vrs.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 车辆预约
 *
 * @author zhangpeng、wuyu
 * @date 2023-07-05
 */
@RestController
@RequestMapping("/system/application")
public class VRSCarApplicationController extends BaseController {
    @Autowired
    private IVRSCarApplicationService VRSCarApplicationService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private RyTaskService ryTaskService;

    @Autowired
    RestTemplate restTemplate;

    @Value("${ESB.ip}") // 从配置文件中获取属性值
    private String ESBIP;

    /**
     * 查询车辆预约列表
     */
    @PreAuthorize("@ss.hasPermi('system:application:list')")
    @GetMapping("/list")
    public TableDataInfo list(CarApplicationQueryParam param) {
        LoginUser loginUser = SecurityUtils.getLoginUser();

        if ("01".equals(loginUser.getUser().getUserType())) {
            //承运商查询自己的数据
            param.setCarrierId(loginUser.getUserId());
        } else if (!loginUser.getUser().isAdmin()) {
            //判断是否为审核人.门卫与管理员可以看到全部数据，其他人只能看到自己的单子
            //查询当前用户是否拥有门卫角色
            SysUser user = new SysUser();
            user.setUserId(loginUser.getUserId());
            user.setRoleId(143L);
            SysUser check = sysUserMapper.selectUserByRole(user);
            SysUser user1 = new SysUser();
            user1.setUserId(loginUser.getUserId());
            user1.setRoleId(150L);
            SysUser check1 = sysUserMapper.selectUserByRole(user1);

            if (!(check == null || check1 == null)) {
                //既不是管理员，也不是门卫、仓库，则仅查询该审核员的数据或者自己创建的数据
                param.setCheckerId(loginUser.getUserId());
            }
            SysUser user2 = loginUser.getUser();
            for (SysRole role : user2.getRoles()) {
                if (role.getRoleId() == 144L){
                    param.setCheckerId(loginUser.getUserId());
                }
            }
        }
        //只对第一个查询生效
        startPage();
        List<VRSCarApplication> list = VRSCarApplicationService.selectVRSCarApplicationList(param);
        return getDataTable(list);
    }

    /**
     * 查询车辆预约列表 --预约今日进场且已审核通过数据
     */
    @PreAuthorize("@ss.hasPermi('system:application:todaylist')")
    @GetMapping("/todayList")
    public TableDataInfo todayList(CarApplicationQueryParam param) {
        startPage();
        param.setStatus(2);
        param.setApplicationDate(new Date());
        List<VRSCarApplication> list = VRSCarApplicationService.selectVRSCarApplicationList(param);
        return getDataTable(list);
    }

    /**
     * 导出车辆预约列表 --预约今日进厂且已审核通过数据
     */
    @PreAuthorize("@ss.hasPermi('system:application:todayExport')")
    @Log(title = "导出今日车辆预约记录", businessType = BusinessType.EXPORT)
    @PostMapping("/todayExport")
    public void todayExport(HttpServletResponse response, CarApplicationQueryParam VRSCarApplication) {
        VRSCarApplication.setApplicationDate(new Date());
        VRSCarApplication.setStatus(2);
        List<VRSCarApplication> list = VRSCarApplicationService.selectVRSCarApplicationList(VRSCarApplication);
        ExcelUtil<VRSCarApplication> util = new ExcelUtil<>(VRSCarApplication.class);
        util.exportExcel(response, list, "车辆预约数据");
    }

    /**
     * 导出车辆预约列表
     */
    @PreAuthorize("@ss.hasPermi('system:application:export')")
    @Log(title = "导出车辆预约记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestParam(value = "ids") String[] ids, CarApplicationQueryParam VRSCarApplication) {
        if (ids.length > 0) {
            List<VRSCarApplication> list = VRSCarApplicationService.selectVRSCarApplicationListByIds(ids);
            ExcelUtil<VRSCarApplication> util = new ExcelUtil<>(VRSCarApplication.class);
            util.exportExcel(response, list, "车辆预约数据");
        } else {
            List<VRSCarApplication> list = VRSCarApplicationService.selectVRSCarApplicationList(VRSCarApplication);
            ExcelUtil<VRSCarApplication> util = new ExcelUtil<>(VRSCarApplication.class);
            util.exportExcel(response, list, "车辆预约数据");
        }
    }

    /**
     * 获取车辆预约详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:application:query')")
    @GetMapping(value = "/{applicationId}")
    public AjaxResult getInfo(@PathVariable("applicationId") String applicationId) {
        return AjaxResult.success(VRSCarApplicationService.selectVRSCarApplicationByApplicationId(applicationId));
    }

    /**
     * 新增车辆预约记录
     */
    @PreAuthorize("@ss.hasPermi('system:application:add')")
    @Log(title = "新增车辆预约记录", businessType = BusinessType.INSERT)
    @PostMapping
    @Transactional
    public AjaxResult add(@RequestBody VRSCarApplication VRSCarApplication) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        System.out.println(VRSCarApplication.getCheckerName());
        Long maxSerialNumber = VRSCarApplicationService.selectMaxSerialNumber();
        if (maxSerialNumber == null) {
            maxSerialNumber = 0L;
        }
        VRSCarApplication.setSerialNumber(maxSerialNumber + 1);
        VRSCarApplication.setCreatedBy(loginUser.getUserId());
//        VRSCarApplication.setApplicationNo(DataHandleUtil.getId_random("YY",9));
        VRSCarApplication.setCreatedDate(new Date());
        //默认后台预约提交为代提交方式
        if (!("01").equals(loginUser.getUser().getUserType())) {
            VRSCarApplication.setPostType(2);
        }

        VRSCarApplication.setApplicationId(IdUtils.fastSimpleUUID());
        VRSCarApplication.setApplicationNo(DataHandleUtil.getId_random("YY",9));
        VRSCarApplication.setCreatedName(loginUser.getUser().getNickName());
        VRSCarApplication.setFactoryType("KS");
//        VRSCarApplication.setFactoryType("IN");
        //判断身份证号是否被拉入黑名单
        if (!"".equals(VRSCarApplication.getIdcard())) {
            SysUser u = userService.getDetailByIdcard(VRSCarApplication.getIdcard());
            if (u != null && u.getIsBlacklist() == 1) {
                return AjaxResult.error("该司机已被拉入黑名单");
            }
        }
//        int res;
//        Integer num = VRSCarApplicationService.selectNonFinishedByCarNumber(VRSCarApplication);
//        List<Integer> enterStatusList = VRSCarApplicationService.selectEnterStatusListByCarNumber(VRSCarApplication.getCarNumber());
//        if (num == 0 && !(enterStatusList.contains(0) || enterStatusList.contains(1))) {
//            res = VRSCarApplicationService.insertVRSCarApplication(VRSCarApplication);
//        } else {
//            return AjaxResult.error("此车牌号您已预约或者未出厂，不能重复预约，请检查该车之前是否出厂！");
//        }
        String carNumber = VRSCarApplication.getCarNumber().replaceAll("[\\p{Z}\\s]+", "");
        String phone = VRSCarApplication.getPhone().replaceAll("[\\p{Z}\\s]+", "");
        VRSCarApplication.setCarNumber(carNumber);
        VRSCarApplication.setPhone(phone);
        int res = VRSCarApplicationService.insertVRSCarApplication(VRSCarApplication);
        // 0.设置消息内容
        String content = "乐美包装（昆山）车辆预约审核提醒\n" +
                "预约承运商：" + VRSCarApplication.getCarrierName() + "\n" +
                "预约人：" + VRSCarApplication.getCreatedName() + "\n" +
                "预约车辆：" + VRSCarApplication.getCarNumber() + "\n" +
                "预约时间：" + DateUtils.parseDateToStr("yyyy-MM-dd", VRSCarApplication.getApplicationDate()) + "\n";

        //查询审核人所属企业微信的userid
        SysUser checker = userService.getDetailByUserId(VRSCarApplication.getCheckerId());
        if (checker != null && checker.getWeComId() != null) {
            weChatService.sendMessage(checker.getWeComId(), content);
        }

        return toAjax(res);
    }

    /**
     * 修改车辆预约记录
     */
    @PreAuthorize("@ss.hasPermi('system:application:edit')")
    @Log(title = "修改车辆预约记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody VRSCarApplication VRSCarApplication) {
        if (VRSCarApplication.getDriverId() == null) {
            //司机id为空之后,判断是否要置为空
            VRSCarApplication.setDriverId(-1L);
        }
        //判断身份证号是否被拉入黑名单
        if (!"".equals(VRSCarApplication.getIdcard())) {
            SysUser u = userService.getDetailByIdcard(VRSCarApplication.getIdcard());
            if (u != null && u.getIsBlacklist() == 1) {
                return AjaxResult.error("该司机已被拉入黑名单");
            }
        }

        String carNumber = VRSCarApplication.getCarNumber().replaceAll("[\\p{Z}\\s]+", "");
        String phone = VRSCarApplication.getPhone().replaceAll("[\\p{Z}\\s]+", "");
        VRSCarApplication.setCarNumber(carNumber);
        VRSCarApplication.setPhone(phone);
        int res = VRSCarApplicationService.updateVRSCarApplication(VRSCarApplication);

        // 0.设置消息内容
        String content = "乐美包装（昆山）车辆预约审核提醒\n" +
                "预约承运商：" + VRSCarApplication.getCarrierName() + "\n" +
                "预约人：" + VRSCarApplication.getCreatedName() + "\n" +
                "预约车辆：" + VRSCarApplication.getCarNumber() + "\n" +
                "预约时间：" + DateUtils.parseDateToStr("yyyy-MM-dd", VRSCarApplication.getApplicationDate()) + "\n";
        //查询审核人所属企业微信的userid
        SysUser checker = userService.getDetailByUserId(VRSCarApplication.getCheckerId());
        if (checker != null && checker.getWeComId() != null) {
            weChatService.sendMessage(checker.getWeComId(), content);
        }

        return toAjax(res);
    }

    /**
     * 修改状态
     */
    @PreAuthorize("@ss.hasPermi('system:application:statusEdit')")
    @Log(title = "修改状态", businessType = BusinessType.UPDATE)
    @PutMapping("/statusEdit")
    public AjaxResult statusEdit(@RequestBody VRSCarApplication VRSCarApplication) {
        return toAjax(VRSCarApplicationService.updateStatus(VRSCarApplication));
    }

    /**
     * 修改扣重
     */
    @PreAuthorize("@ss.hasPermi('system:application:loseWeightEdit')")
    @Log(title = "修改扣重", businessType = BusinessType.UPDATE)
    @PutMapping("/loseWeightEdit")
    public AjaxResult loseWeightEdit(@RequestBody VRSCarApplication VRSCarApplication) {
        VRSCarApplication lm = VRSCarApplicationService.selectVRSCarApplicationByApplicationId(VRSCarApplication.getApplicationId());
        VRSCarApplication.setUpFloatingWeight(((VRSCarApplication.getAppointmentWeight() != null ? VRSCarApplication.getAppointmentWeight() : BigDecimal.ZERO).add(VRSCarApplication.getLoseWeight() != null ? VRSCarApplication.getLoseWeight() : BigDecimal.ZERO).add(VRSCarApplication.getShippedWithPalletWeight() != null ? VRSCarApplication.getShippedWithPalletWeight() : BigDecimal.ZERO)).multiply(BigDecimal.ONE.add(lm.getToleranceAdd().divide(new BigDecimal(100)))));
        VRSCarApplication.setDownFloatingWeight(((VRSCarApplication.getAppointmentWeight() != null ? VRSCarApplication.getAppointmentWeight() : BigDecimal.ZERO).add(VRSCarApplication.getLoseWeight() != null ? VRSCarApplication.getLoseWeight() : BigDecimal.ZERO).add(VRSCarApplication.getShippedWithPalletWeight() != null ? VRSCarApplication.getShippedWithPalletWeight() : BigDecimal.ZERO)).multiply(BigDecimal.ONE.add(lm.getToleranceDec().divide(new BigDecimal(100)))));
        VRSCarApplication.setEditFlag("Yes");
        return toAjax(VRSCarApplicationService.updateLoseWeight(VRSCarApplication));
    }

    /**
     * 修改销售预约重量
     */
    @PreAuthorize("@ss.hasPermi('system:application:saleAppointmentWeightEdit')")
    @Log(title = "修改销售预约重量", businessType = BusinessType.UPDATE)
    @PutMapping("/saleAppointmentWeightEdit")
    public AjaxResult saleAppointmentWeightEdit(@RequestBody VRSCarApplication VRSCarApplication) {
        VRSCarApplication lm = VRSCarApplicationService.selectVRSCarApplicationByApplicationId(VRSCarApplication.getApplicationId());
        VRSCarApplication.setOrderId(lm.getOrderId());
        VRSCarApplication.setOriginalWeight(lm.getAppointmentWeight() != null ? lm.getAppointmentWeight() : BigDecimal.ZERO );
        LocalDateTime localDateTime = LocalDateTime.now();
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        VRSCarApplication.setEditTime(date);
        Boolean b = VRSCarApplicationService.checkExitOrderIdByOrderId(lm.getOrderId());
        if (b){
            VRSCarApplicationService.updateAuditLineByOrderId(VRSCarApplication);
        }else{
            VRSCarApplicationService.insertAuditLine(VRSCarApplication);
        }
        VRSCarApplication.setEditFlag("Yes");
        VRSCarApplication.setUpFloatingWeight(((VRSCarApplication.getEditWeight() != null ? VRSCarApplication.getEditWeight() : BigDecimal.ZERO).add(VRSCarApplication.getLoseWeight() != null ? VRSCarApplication.getLoseWeight() : BigDecimal.ZERO).add(VRSCarApplication.getShippedWithPalletWeight() != null ? VRSCarApplication.getShippedWithPalletWeight() : BigDecimal.ZERO)).multiply(BigDecimal.ONE.add(lm.getToleranceAdd().divide(new BigDecimal(100)))));
        VRSCarApplication.setDownFloatingWeight(((VRSCarApplication.getEditWeight() != null ? VRSCarApplication.getEditWeight() : BigDecimal.ZERO).add(VRSCarApplication.getLoseWeight() != null ? VRSCarApplication.getLoseWeight() : BigDecimal.ZERO).add(VRSCarApplication.getShippedWithPalletWeight() != null ? VRSCarApplication.getShippedWithPalletWeight() : BigDecimal.ZERO)).multiply(BigDecimal.ONE.add(lm.getToleranceDec().divide(new BigDecimal(100)))));
        return toAjax(VRSCarApplicationService.updateSaleWeight(VRSCarApplication));
    }

    /**
     * 修改废料预约重量
     */
    @PreAuthorize("@ss.hasPermi('system:application:scrapAppointmentWeightEdit')")
    @Log(title = "修改废料预约重量", businessType = BusinessType.UPDATE)
    @PutMapping("/scrapAppointmentWeightEdit")
    public AjaxResult scrapAppointmentWeightEdit(@RequestBody VRSCarApplication VRSCarApplication) {
        VRSCarApplication lm = VRSCarApplicationService.selectVRSCarApplicationByApplicationId(VRSCarApplication.getApplicationId());
        VRSCarApplication.setOrderId(lm.getOrderId());
        VRSCarApplication.setOriginalWeight(lm.getAppointmentWeight() != null ? lm.getAppointmentWeight() : BigDecimal.ZERO );
        LocalDateTime localDateTime = LocalDateTime.now();
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        VRSCarApplication.setEditTime(date);
        Boolean b = VRSCarApplicationService.checkExitOrderIdByOrderId(lm.getOrderId());
        if (b){
            VRSCarApplicationService.updateAuditLineByOrderId(VRSCarApplication);
        }else{
            VRSCarApplicationService.insertAuditLine(VRSCarApplication);
        }
        VRSCarApplication.setEditFlag("Yes");
        VRSCarApplication.setUpFloatingWeight(VRSCarApplication.getEditWeight());
        VRSCarApplication.setDownFloatingWeight(VRSCarApplication.getEditWeight());
        return toAjax(VRSCarApplicationService.updateSaleWeight(VRSCarApplication));
    }

    /**
     * 修改出厂带栈板重量
     */
    @PreAuthorize("@ss.hasPermi('system:application:palletWeightEdit')")
    @Log(title = "修改出厂带栈板重量", businessType = BusinessType.UPDATE)
    @PutMapping("/shippedWithPalletWeightEdit")
    public AjaxResult shippedWithPalletWeightEdit(@RequestBody VRSCarApplication VRSCarApplication) {
        VRSCarApplication lm = VRSCarApplicationService.selectVRSCarApplicationByApplicationId(VRSCarApplication.getApplicationId());
        VRSCarApplication.setUpFloatingWeight(((VRSCarApplication.getAppointmentWeight() != null ? VRSCarApplication.getAppointmentWeight() : BigDecimal.ZERO).add(VRSCarApplication.getLoseWeight() != null ? VRSCarApplication.getLoseWeight() : BigDecimal.ZERO).add(VRSCarApplication.getShippedWithPalletWeight() != null ? VRSCarApplication.getShippedWithPalletWeight() : BigDecimal.ZERO)).multiply(BigDecimal.ONE.add(lm.getToleranceAdd().divide(new BigDecimal(100)))));
        VRSCarApplication.setDownFloatingWeight(((VRSCarApplication.getAppointmentWeight() != null ? VRSCarApplication.getAppointmentWeight() : BigDecimal.ZERO).add(VRSCarApplication.getLoseWeight() != null ? VRSCarApplication.getLoseWeight() : BigDecimal.ZERO).add(VRSCarApplication.getShippedWithPalletWeight() != null ? VRSCarApplication.getShippedWithPalletWeight() : BigDecimal.ZERO)).multiply(BigDecimal.ONE.add(lm.getToleranceDec().divide(new BigDecimal(100)))));
        return toAjax(VRSCarApplicationService.updateShippedWithPalletWeight(VRSCarApplication));
    }

    /**
     * 进出厂
     */
    @PreAuthorize("@ss.hasPermi('system:application:enter')||@ss.hasPermi('system:application:out')||@ss.hasPermi('system:application:empty')")
    @Log(title = "进出厂", businessType = BusinessType.UPDATE)
    @PutMapping("/enterOrOut")
    public AjaxResult enterOrOut(@RequestBody VRSCarApplication VRSCarApplication) {
        String carNumber = VRSCarApplicationService.selectCarNumberByApplicationId(VRSCarApplication.getApplicationId());
        boolean b = VRSCarApplicationService.checkEnterStatusInfoExists(carNumber);
        if (VRSCarApplication.getEnterStatus() != null){

            if (VRSCarApplication.getEnterStatus().equals(1)) {
                if (b){
                    return AjaxResult.error("车辆" + " " + carNumber + " " + "已进厂，不可重复操作！");
                } else {
                    //进厂
                    VRSCarApplication.setEnterDate(new Date());
                }
            }

            if (VRSCarApplication.getEnterStatus().equals(2)) {
                //出厂
                VRSCarApplication.setOutDate(new Date());
            }

        }
        return toAjax(VRSCarApplicationService.updateVRSCarApplication(VRSCarApplication));
    }

    /**
     * 审核
     */
    @PreAuthorize("@ss.hasPermi('system:application:authen')")
    @Log(title = "审核", businessType = BusinessType.UPDATE)
    @PutMapping("/authen")
    public AjaxResult authen(@RequestBody VRSCarApplication VRSCarApplication) {
        VRSCarApplication.setEditFlag("No");
        if (VRSCarApplication.getBusinessType() != null){
            if (VRSCarApplication.getBusinessType() == 1 || VRSCarApplication.getBusinessType() == 4  ){
                // 销售业务：默认启用容差控制，预约重量从SAP接口获取，审核时设置为0
                // 固废危废处置业务：目前不启用容差控制，审核时设置重量为0
                VRSCarApplication.setAppointmentWeight(BigDecimal.ZERO);
                VRSCarApplication.setUpFloatingWeight(BigDecimal.ZERO);
                VRSCarApplication.setDownFloatingWeight(BigDecimal.ZERO);
            } else if (VRSCarApplication.getBusinessType() == 2) {
                // 废料业务：目前默认不启用容差控制，预约重量从SAP接口获取
                // 管控要求预约重量不为0，审核通过时需要OA预付款审批
                VRSCarApplication.setAppointmentWeight(BigDecimal.ZERO);
                VRSCarApplication.setUpFloatingWeight(BigDecimal.ZERO);
                VRSCarApplication.setDownFloatingWeight(BigDecimal.ZERO);
                // 检查废料预付款信息
                VRSCarApplication carApplication = VRSCarApplicationService.selectVRSCarApplicationByApplicationId(VRSCarApplication.getApplicationId());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = sdf.format(carApplication.getApplicationDate());
                boolean b = VRSCarApplicationService.checkScrapAdvanceInfoExists(carApplication.getCarrierName(),formattedDate);
                if (!b){
                    return AjaxResult.error("OA未查到废料预付款信息，审核失败");
                }
            }
        }
        return toAjax(VRSCarApplicationService.updateVRSCarApplication(VRSCarApplication));
    }

    /**
     * 标记出入厂凭证
     */
    @PreAuthorize("@ss.hasPermi('system:application:certificateConfirm')")
    @Log(title = "标记出入厂凭证", businessType = BusinessType.UPDATE)
    @PutMapping("/enterOrOutMark")
    public AjaxResult enterOrOutMark(@RequestBody VRSCarApplication VRSCarApplication) {
        return toAjax(VRSCarApplicationService.updateVRSCarApplication(VRSCarApplication));
    }

    /**
     * 删除车辆预约记录
     */
    @PreAuthorize("@ss.hasPermi('system:application:remove')")
    @Log(title = "删除车辆预约记录", businessType = BusinessType.DELETE)
    @PutMapping("/delete")
    public AjaxResult remove(@RequestBody VRSCarApplication VRSCarApplication) {
        VRSCarApplication.setIsdeleted(1);
        return toAjax(VRSCarApplicationService.deleteVRSCarApplicationByApplicationId(VRSCarApplication.getApplicationId()));
    }

    /**
     * 数据统计
     */
    @GetMapping("/statis")
    public AjaxResult statis() {
        //查询车辆预约数量
        return AjaxResult.success(VRSCarApplicationService.indexStatis());
    }

    /**
     * 查询供应商
     */
    @GetMapping("/getSupplierList")
    public AjaxResult getSupplierList() {
        AjaxResult ajax = AjaxResult.success();
        List<VRSSupplier> VRSSupplier = ryTaskService.selectVRSSupplierList();
        ajax.put("VRSSupplier", VRSSupplier);
        return ajax;
    }

    /**
     * 查询全局变量
     */
    @GetMapping("/getGlobalVariable")
    public AjaxResult getGlobalVariable() {
        return AjaxResult.success(ryTaskService.selectVRSGlobalVariable());
    }

    /**
     * 取消称重或作废称重
     */
    @PreAuthorize("@ss.hasPermi('system:application:weightCancelInfo')")
    @Log(title = "取消称重或作废称重", businessType = BusinessType.UPDATE)
    @PostMapping("/weightCancelInfo")
    public AjaxResult weightCancelInfo(@RequestBody VRSCarApplication VRSCarApplication) {

        // 设置URL
//        String url = "http://47.103.124.65:7087/esb/webscript?scriptname=WeightCancelInfo";
        // 正式老ESB
//        String url = "http://esb.lamipak.biz:18080/esb/comm/service";
        // 测试老ESB
//        String url ="http://esb-test.lamipak.biz:18080/esb/comm/service";
        // 测试新ESB
        String url = "https://172.18.165.151:8020/esb/comm/service";

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentType(MediaType.TEXT_XML);
//        headers.add("scriptname", "WeightCancelInfo");
        headers.add("serviceName", "S_D365_TSS_SyncCancel_S");
//        headers.add("sourceSystem", "D365");
//        headers.add("requestId", "35b8d072-2aae-11e6-9e71-0a0a012d6888");
//        headers.add("trackId", "35b8d072baae11e69e710a0a012d6888");
        // 获取当前时间
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentTime = localDateTime.format(formatter);
        // 通过前端id查询预约信息
        VRSCarApplication rb = VRSCarApplicationService.selectVRSCarApplicationByApplicationId(VRSCarApplication.getApplicationId());
        // 获取预约状态
        Integer appointmentStatus = VRSCarApplication.getAppointmentStatus();
        // 获取请求标识
        Integer requestId = VRSCarApplication.getRequestId();
        // 创建请求体
//        Map<String, Object> requestBody = new HashMap<>();
        String requestBody;
        //如果状态为待上2磅才允许执⾏【Request=1】，且状态更新为待上1磅，同时清除本系统一磅信息。
        if (appointmentStatus == 3) {
            if (requestId == 1) {
                requestBody = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"
                        + "<soap:Body>"
                        + "<WeightCancelInfo xmlns=\"http://tempuri.org/\">"
                        + "<json>{\"TaskID\":\"" + rb.getTaskId() + "\",\"Request\":\"1\",\"Reason\":\"" + rb.getReason() + "\",\"UserID\":\"" + rb.getUserID1() + "\",\"UserName\":\"" + rb.getUserName1() + "\",\"RequestTime\":\"" + currentTime + "\"}</json>"
                        + "</WeightCancelInfo>"
                        + "</soap:Body>"
                        + "</soap:Envelope>";
//                requestBody.put("TaskID", rb.getTaskId());
//                requestBody.put("Request", "1");
//                requestBody.put("Reason", rb.getReason());
//                requestBody.put("UserID", rb.getUserID1());
//                requestBody.put("UserName", rb.getUserName1());
//                requestBody.put("RequestTime", currentTime);
                // 创建HttpEntity对象并设置请求头和请求体
//                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
                HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
                // 发送POST请求
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                String responseBody = response.getBody();
                String cleanedJsonString = responseBody.substring(responseBody.indexOf("{"));
                // 解析响应体数据
                JSONObject responseJson = (JSONObject) JSONObject.parse(cleanedJsonString);
                String code = (String) responseJson.get("Code");
                if ("0".equals(code)) {
                    //取消一次磅时修改车辆预约信息
                    return toAjax(VRSCarApplicationService.updateOneVRSCarApplicationByApplicationId(VRSCarApplication));
                } else {
                    return AjaxResult.error("取消一次磅失败");
                }
            }
        } else if (appointmentStatus == 5) {
            if (requestId == 3) {
                requestBody = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"
                        + "<soap:Body>"
                        + "<WeightCancelInfo xmlns=\"http://tempuri.org/\">"
                        + "<json>{\"TaskID\":\"" + rb.getTaskId() + "\",\"Request\":\"3\",\"Reason\":\"" + rb.getReason() + "\",\"UserID\":\"" + rb.getUserID2() + "\",\"UserName\":\"" + rb.getUserName2() + "\",\"RequestTime\":\"" + currentTime + "\"}</json>"
                        + "</WeightCancelInfo>"
                        + "</soap:Body>"
                        + "</soap:Envelope>";
//                requestBody.put("TaskID", rb.getTaskId());
//                requestBody.put("Request", Integer.toString(VRSCarApplication.getRequestId()));
//                requestBody.put("Reason", rb.getReason());
//                requestBody.put("UserID", rb.getUserID2());
//                requestBody.put("UserName", rb.getUserName2());
//                requestBody.put("RequestTime", currentTime);
                // 创建HttpEntity对象并设置请求头和请求体
                HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
                // 发送POST请求
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                String responseBody = response.getBody();
                String cleanedJsonString = responseBody.substring(responseBody.indexOf("{"));
                // 解析响应体数据
                JSONObject responseJson = (JSONObject) JSONObject.parse(cleanedJsonString);
                String code = (String) responseJson.get("Code");
                if ("0".equals(code)) {
                    //作废时修改车辆预约信息
                    return toAjax(VRSCarApplicationService.updateAllVRSCarApplicationByApplicationId(VRSCarApplication));
                } else {
                    return AjaxResult.error("作废称重失败");
                }
            } else {
                requestBody = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"
                        + "<soap:Body>"
                        + "<WeightCancelInfo xmlns=\"http://tempuri.org/\">"
                        + "<json>{\"TaskID\":\"" + rb.getTaskId() + "\",\"Request\":\"1\",\"Reason\":\"" + rb.getReason() + "\",\"UserID\":\"" + rb.getUserID2() + "\",\"UserName\":\"" + rb.getUserName2() + "\",\"RequestTime\":\"" + currentTime + "\"}</json>"
                        + "</WeightCancelInfo>"
                        + "</soap:Body>"
                        + "</soap:Envelope>";
//                requestBody.put("TaskID", rb.getTaskId());
//                requestBody.put("Request", "2");
//                requestBody.put("Reason", rb.getReason());
//                requestBody.put("UserID", rb.getUserID2());
//                requestBody.put("UserName", rb.getUserName2());
//                requestBody.put("RequestTime", currentTime);
                // 创建HttpEntity对象并设置请求头和请求体
                HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
                // 发送POST请求
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                String responseBody = response.getBody();
                String cleanedJsonString = responseBody.substring(responseBody.indexOf("{"));
                // 解析响应体数据
                JSONObject responseJson = (JSONObject) JSONObject.parse(cleanedJsonString);
                String code = (String) responseJson.get("Code");
                if ("0".equals(code)) {
                    //取消二次磅时修改车辆预约信息
                    return toAjax(VRSCarApplicationService.updateTwoVRSCarApplicationByApplicationId(VRSCarApplication));
                } else {
                    return AjaxResult.error("取消二次磅失败");
                }
            }
        }
        return AjaxResult.error("操作失败，请检查相关信息");
    }

    /**
     * 查询销售列表
     */
    @PreAuthorize("@ss.hasPermi('system:application:saleList')")
    @GetMapping("/saleList")
    public TableDataInfo saleList(CarSaleQueryParam param) {
        startPage();
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 将日期减去一天
        LocalDate yesterday = currentDate.minusDays(1);
        param.setYesterday(java.sql.Date.valueOf(yesterday));
        List<CarDeliveryItemReal> list = VRSCarApplicationService.selectSaleList(param);
        return getDataTable(list);
    }

    /**
     * 查询销售详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:application:saleEdit')")
    @GetMapping(value = "/saleInfo/{vrsId}")
    public AjaxResult getSaleInfo(@PathVariable("vrsId") String vrsId) {
        return AjaxResult.success(VRSCarApplicationService.selectSaleInfoByvrsId(vrsId));
    }

    /**
     * 修改销售详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:application:saleEdit')")
    @Log(title = "销售详细", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/saleUpdate")
    public AjaxResult saleUpdate(@RequestBody CarDeliveryItemReal VRSDeliveryItemReal) {
        return toAjax(VRSCarApplicationService.updateSaleInfo(VRSDeliveryItemReal));
    }

    /**
     * 删除交货单
     */
    @PreAuthorize("@ss.hasPermi('system:application:saleDelete')")
    @Log(title = "删除交货单", businessType = BusinessType.DELETE)
    @DeleteMapping(value = "/saleDelete/{vrsIds}")
    public AjaxResult saleDelete(@PathVariable String vrsIds[]) {
        return toAjax(VRSCarApplicationService.deleteSaleInfoByvrsIds(vrsIds));
    }

    /**
     * 更新交货单
     */
    @PreAuthorize("@ss.hasPermi('system:application:updateDeliveryOrder')")
    @Log(title = "更新交货单", businessType = BusinessType.UPDATE)
    @GetMapping("/getDeliveryOrderByThird")
    public AjaxResult getDeliveryOrderByThird() {
        // 创建RestTemplate实例
        RestTemplate restTemplate = new RestTemplate();
        // 设置URL
        //测试
//        String url = "http://172.18.165.151:9020/sap/SD024/third/noparams";
        //正式
        String url = "https://esbapi.lamipak.biz:9020/sap/SD024/third/noparams";
        // 创建请求体对象
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        // 发送 POST 请求并获取响应
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        String responseBody = responseEntity.getBody();

        // 处理响应数据
        if (responseBody != null) {
            // 响应成功，进行相应处理逻辑
            return AjaxResult.success("操作成功");
        } else {
            // 响应失败，返回错误信息
            return AjaxResult.error("操作失败，请检查相关信息");
        }

    }

    /**
     * 取消预约
     */
    @PreAuthorize("@ss.hasPermi('system:application:cancelReservation')")
    @Log(title = "取消预约", businessType = BusinessType.UPDATE)
    @PutMapping("/cancelReservation")
    public AjaxResult cancelReservation(@RequestBody VRSCarApplication VRSCarApplication) throws ParseException {
        String applicationDate = "1900-01-01";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(applicationDate);
        VRSCarApplication.setApplicationDate(date);
        return toAjax(VRSCarApplicationService.updateVRSCarApplication(VRSCarApplication));
    }
    /**
     * 重新上1磅
     */
    @PreAuthorize("@ss.hasPermi('system:application:reWeight1')")
    @Log(title = "重新上1磅", businessType = BusinessType.UPDATE)
    @PutMapping("/reWeight1")
    public AjaxResult reWeight1(@RequestBody VRSCarApplication VRSCarApplication){
        return toAjax(VRSCarApplicationService.updateVRSCarApplication(VRSCarApplication));
    }
    /**
     * 重新上2磅
     */
    @PreAuthorize("@ss.hasPermi('system:application:reWeight2')")
    @Log(title = "重新上2磅", businessType = BusinessType.UPDATE)
    @PutMapping("/reWeight2")
    public AjaxResult reWeight2(@RequestBody VRSCarApplication VRSCarApplication){
        return toAjax(VRSCarApplicationService.updateVRSCarApplication(VRSCarApplication));
    }
}