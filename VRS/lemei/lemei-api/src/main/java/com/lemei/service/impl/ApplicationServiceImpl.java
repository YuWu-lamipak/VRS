package com.lemei.service.impl;

import com.github.pagehelper.PageHelper;
import com.lemei.common.core.domain.AjaxResult;
import com.lemei.common.core.domain.ResultData;
import com.lemei.common.core.domain.entity.SysUser;
import com.lemei.common.core.domain.model.LoginUser;
import com.lemei.common.exception.ServiceException;
import com.lemei.common.service.WeChatService;
import com.lemei.common.utils.*;
import com.lemei.common.utils.uuid.IdUtils;
import com.lemei.domain.LmSupplierApp;
import com.lemei.domain.param.*;
import com.lemei.mapper.CarApplicationMapper;
import com.lemei.mapper.UserMapper;
import com.lemei.service.ApplicationService;
import com.lemei.service.UserService;
import com.lemei.system.domain.LmCarApplication;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author yq  用户Service
 * @CLassName UserService
 * @Description
 * @date 2022/8/26 15:16
 **/
@Service
public class ApplicationServiceImpl implements ApplicationService {
    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private CarApplicationMapper carApplicationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private WeChatService weChatService;
    /**
     * 我的预约列表
     * @param param
     * @return
     */
    @Override
    public List<ResultData> getMyPageList(ApplicationQueryParam param){
        PageHelper.startPage(param.getPageIndex(),param.getPageSize());
        return  carApplicationMapper.getMyApplicationList(param);
    }

    @Override
    public List<ResultData> selectDictDataByType(String dictType){
        return  carApplicationMapper.selectDictDataByType(dictType);
    }

    /**
     * 查询预约列表 -- 默认查今天
     * @param param
     * @return
     */
    @Override
    public List<ResultData> getPageList(ApplicationQueryParam param){
        PageHelper.startPage(param.getPageIndex(),param.getPageSize());
        return  carApplicationMapper.getApplicationList(param);
    }

    @Override
    public List<ResultData> getGuardPageList(ApplicationQueryParam param){
        PageHelper.startPage(param.getPageIndex(),param.getPageSize());
        return  carApplicationMapper.getGuardApplicationList(param);
    }

    //门卫首页数据统计
    @Override
    public Object selectDataByStatus(){
        ApplicationQueryParam param = new ApplicationQueryParam();
        HashMap<String, Integer> map = new HashMap<>();
        param.setStatus(2);
        //未进厂
        param.setEnterStatus(0);
        int notIn = carApplicationMapper.selectGuardDataByStatus(param);
        //已进厂
        param.setEnterStatus(1);
        int in = carApplicationMapper.selectGuardDataByStatus(param);
        //已出厂
        param.setEnterStatus(2);
        int out = carApplicationMapper.selectGuardDataByStatus(param);
        map.put("notIn",notIn);
        map.put("in",in);
        map.put("out",out);
        return  map;
    }

    //审核首页数据统计
    @Override
    public Object selectCheckerDataByStatus(){
        LoginUser loginUser = SecurityUtils.getLoginUser();
        ApplicationQueryParam param = new ApplicationQueryParam();
        //判断是否为管理员  非管理员查询其下数据
        if (!loginUser.getUser().isAdmin()){
            param.setCheckerId(loginUser.getUserId());
        }
        HashMap<String, Integer> map = new HashMap<>();
        //未审核
        param.setAnthStatus(1);
        int unaudited = carApplicationMapper.selectCheckDataByStatus(param);
        //已审核
        param.setAnthStatus(2);
        int reviewed = carApplicationMapper.selectCheckDataByStatus(param);
        map.put("unaudited",unaudited);
        map.put("reviewed",reviewed);
        return  map;
    }

    //本人首页审核数据统计
    @Override
    public Object selectSelfDataByStatus(){
        LoginUser loginUser = SecurityUtils.getLoginUser();
        ApplicationQueryParam param = new ApplicationQueryParam();
        //判断是否为管理员  非管理员查询其下数据
        if (!loginUser.getUser().isAdmin()){
            param.setCreatedBy(loginUser.getUserId());
            param.setDriverId(loginUser.getUserId());
        }
        HashMap<String, Integer> map = new HashMap<>();
        //未审核
        param.setAnthStatus(1);
        int unaudited = carApplicationMapper.selectSelfDataByStatus(param);
        //已审核
        param.setAnthStatus(2);
        int reviewed = carApplicationMapper.selectSelfDataByStatus(param);
        map.put("unaudited",unaudited);
        map.put("reviewed",reviewed);
        return  map;
    }

    /**
     * 新增车辆预约
     * @param param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object insertCarApplication(ApplicationAddParam param){
        LoginUser loginUser = SecurityUtils.getLoginUser();
        param.setCreatedBy(loginUser.getUserId());
        param.setCreatedName(loginUser.getUser().getNickName());
        param.setApplicationId(IdUtils.fastSimpleUUID());
        param.setApplicationNo(DataHandleUtil.getId_random("YY",9));
        param.setCreatedDate(new Date());
        param.setFactoryType("KS");
        //查询司机、承运商、审核人是否存在
        if(param.getCarrierId() != null){
            UserQueryParam userParam = new UserQueryParam();
            userParam.setUserId(param.getCarrierId());
            userParam.setUserType("01");
            //承运商
            SysUser carrUser = userMapper.selectUserByQueryParam(userParam);
            if (carrUser==null){
                return AjaxResult.error("承运商不存在");
            }
        }

        if(param.getCarrierId() != null){
            UserQueryParam userParam = new UserQueryParam();
            userParam.setUserId(param.getDriverId());
            userParam.setUserType("02");
            //司机
            SysUser driUser = userMapper.selectUserByQueryParam(userParam);
            if (driUser==null){
                return AjaxResult.error("司机不存在");
            }
        }
        if (param.getCheckerId()!=null){
            UserQueryParam userParam = new UserQueryParam();
            userParam.setUserId(param.getCheckerId());
            //审核人--角色固定144
            userParam.setRoleId(144);
            SysUser checkerUser = userMapper.selectUserByQueryParam(userParam);
            if (checkerUser==null){
                return AjaxResult.error("审核人不存在");
            }
        }
        try {
            carApplicationMapper.insertCarApplication(param);
            //给审核人发送企业微信消息
            String content = "乐美包装（昆山）车辆预约审核提醒\n"+
                    "预约承运商："+param.getCarrierName()+"\n"+
                    "预约人："+param.getCreatedName()+"\n"+
                    "预约车辆："+param.getCarNumber()+"\n"+
                    "预约时间："+param.getApplicationDate()+"\n";
            //查询审核人所属企业微信的userid
            SysUser checker = userService.getUserInfoById(param.getCheckerId());
            if (checker!=null && checker.getWeComId()!=null){
                weChatService.sendMessage(checker.getWeComId(), content);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("新增异常");
        }

        return  AjaxResult.success(param.getApplicationId());
    }

    /**
     * 预约详情
     * @param applicationId
     * @return
     */
    @Override
    public ResultData carApplicationDetail(String applicationId){
        return  carApplicationMapper.selectCarApplicationById(applicationId);
    }


    /**
     * 修改预约（审核、进出厂、删除)
     * @param param
     * @return
     */
    @Override
    public int updateApplication(ApplicationEditParam param){
        return  carApplicationMapper.updateCarApplicationById(param);
    }

    /**
     * 修改预约单
     * @param param
     * @return
     */
    @Override
    public Object updateCarApplication(ApplicationEditParam param){

        //查询司机、承运商、审核人是否存在
        if(param.getCarrierId() != null){
            UserQueryParam userParam = new UserQueryParam();
            userParam.setUserId(param.getCarrierId());
            userParam.setUserType("01");
            //承运商
            SysUser carrUser = userMapper.selectUserByQueryParam(userParam);
            if (carrUser==null){
                return AjaxResult.error("承运商不存在");
            }
        }

        if(param.getCarrierId() != null){
            UserQueryParam userParam = new UserQueryParam();
            userParam.setUserId(param.getDriverId());
            userParam.setUserType("02");
            //司机
            SysUser driUser = userMapper.selectUserByQueryParam(userParam);
            if (driUser==null){
                return AjaxResult.error("司机不存在");
            }
        }
        if (param.getCheckerId()!=null){
            UserQueryParam userParam = new UserQueryParam();
            userParam.setUserId(param.getCheckerId());
            //审核人--角色固定144
            userParam.setRoleId(144);
            SysUser checkerUser = userMapper.selectUserByQueryParam(userParam);
            if (checkerUser==null){
                return AjaxResult.error("审核人不存在");
            }
        }
        carApplicationMapper.updateCarApplicationById(param);
        //给审核人发送企业微信消息
        String content = "乐美包装（昆山）车辆预约审核提醒\n"+
                "预约承运商："+param.getCarrierName()+"\n"+
                "预约人："+param.getCreatedName()+"\n"+
                "预约车辆："+param.getCarNumber()+"\n"+
                "预约时间："+param.getApplicationDate()+"\n";
        //查询审核人所属企业微信的userid
        SysUser checker = userService.getUserInfoById(param.getCheckerId());
        if (checker!=null && checker.getWeComId()!=null){
            weChatService.sendMessage(checker.getWeComId(), content);
        }
        return  AjaxResult.success("成功");
    }

    public List<LmSupplierApp> selectLmSupplierList(){
        return carApplicationMapper.selectLmSupplierList();
    }

    /**
     * 制卡信息新增
     * @param param
     * @return
     */
    @Override
    public Object updateCardInfo(ApplicationEditParam param) {
        return carApplicationMapper.updateCardInfo(param);
    }

    /**
     * 一磅上磅信息新增
     * @param param
     * @return
     */
    @Override
    public Object updateOneWeightingInfo(ApplicationEditParam param) {
        return carApplicationMapper.updateOneWeightingInfo(param);
    }

    /**
     * 通过车牌号查询
     * @return
     */
    @Override
    public ResultData selectOneWeightingInfo(ApplicationQueryParam param) {
        return carApplicationMapper.selectOneWeightingInfo(param);
    }

    /**
     * 一磅写入信息新增
     * @param param
     * @return
     */
    @Override
    public Object updateOneWeightWriteInfo(ApplicationEditParam param) {
        return carApplicationMapper.updateOneWeightWriteInfo(param);
    }

    /**
     * 二磅上磅信息新增
     * @param param
     * @return
     */
    @Override
    public Object updateTwoWeightingInfo(ApplicationEditParam param) {
        return carApplicationMapper.updateTwoWeightingInfo(param);
    }

    /**
     * 通过车牌号查询
     * @return
     */
    @Override
    public ResultData selectTwoWeightingInfo(ApplicationQueryParam param) {
        return carApplicationMapper.selectTwoWeightingInfo(param);
    }

    /**
     * 二磅写入信息新增
     * @param param
     * @return
     */
    @Override
    public Object updateTwoWeightWriteInfo(ApplicationEditParam param) {
        return carApplicationMapper.updateTwoWeightWriteInfo(param);
    }

    /**
     * 二磅写入失败时更新预约状态
     * @param param
     * @return
     */
    @Override
    public Object updateAppointmentStatus(ApplicationEditParam param) {
        return carApplicationMapper.updateAppointmentStatus(param);
    }

    /**
     * 退卡信息新增
     * @param param
     * @return
     */
    @Override
    public Object updateRefundCardInfo(ApplicationEditParam param) {
        return carApplicationMapper.updateRefundCardInfo(param);
    }
    /**
     * 查询流水号最大值
     * @param
     * @return 车辆预约
     */
    @Override
    public Long selectMaxSerialNumber() {
        return carApplicationMapper.selectMaxSerialNumber();
    }

    /**
     * 通过当前时间查询车牌号的集合
     *
     * @param
     * @return 车辆预约
     */
    @Override
    public List<String> selectCarNumberByApplicationDate(java.sql.Date date) {
        return carApplicationMapper.selectCarNumberByApplicationDate(date);
    }

    /**
     * 通过车牌号查询最新预约时间
     *
     * @param param
     * @return
     */
    @Override
    public java.sql.Date selectApplicationDateByCarNumber(ApplicationQueryParam param) {
        return carApplicationMapper.selectApplicationDateByCarNumber(param);
    }

    /**
     * 通过车牌号，最新预约时间查询预约状态
     * @return
     */
    @Override
    public Integer selectAppointmentStatus(ApplicationQueryParam param) {
        return carApplicationMapper.selectAppointmentStatus(param);
    }

    /**
     * 通过车牌号，最新预约时间查询审核状态
     * @return
     */
    @Override
    public Integer selectStatus(ApplicationQueryParam param) {
        return carApplicationMapper.selectStatus(param);
    }

    /**
     * 通过车牌号，最新预约时间查询进厂状态
     * @return
     */
    @Override
    public Integer selectEnterStatus(ApplicationQueryParam param) {
        return carApplicationMapper.selectEnterStatus(param);
    }

    /**
     * 通过流水号、预约时间、车牌号查询状态
     *
     * @return
     */
    @Override
    public ResultData selectStatusResult(ApplicationQueryParam param){
        return carApplicationMapper.selectStatusResult(param);
    };

    /**
     * 通过车牌号，查询业务类型
     * @return
     */
    @Override
    public Integer selectBusinessType(ApplicationQueryParam param) {
        return carApplicationMapper.selectBusinessType(param);
    }

    /**
     * 通过车牌号，查询上浮重量、下浮重量、实kg、启用管控
     * @return
     */
    @Override
    public ResultData selectUDGE(ApplicationQueryParam param) {
        return carApplicationMapper.selectUDGE(param);
    }

    /**
     * 查询是否存在特定交货单号和行号的记录
     *
     * @param lmDnNum,lmDnLine
     * @return 结果
     */
    @Override
    public boolean checkRecordExists(String lmDnNum, String lmDnLine) {
        return carApplicationMapper.checkRecordExists(lmDnNum,lmDnLine);
    }

    /**
     * 根据交货单号和行号进行体更新
     *
     * @return
     */
    @Override
    public int updateLmSalesOrderItemByLmDnNumAndLmDnLine(LmDeliveryItemField lmDeliveryItemField) {
        return carApplicationMapper.updateLmSalesOrderItemByLmDnNumAndLmDnLine(lmDeliveryItemField);
    }

    /**
     * 新增出货装箱单行
     *
     * @return
     */
    @Override
    public int insertLmDeliveryItemFieldThird(LmDeliveryItemField lmDeliveryItemField) {
        return carApplicationMapper.insertLmDeliveryItemFieldThird(lmDeliveryItemField);
    }

    /**
     * 查询出货装箱单操作行
     *
     * @return 结果
     */
    @Override
    public List<LmDeliveryItemField> selectLmDeliveryItemFieldInsertList(java.sql.Date yesterday) {
        return carApplicationMapper.selectLmDeliveryItemFieldInsertList(yesterday);
    }

    /**
     * 新增出货装箱单操作行
     *
     * @param lmDeliveryItemReal 出货装箱单行
     * @return 结果
     */
    @Override
    public int insertLmDeliveryItemFieldReal(LmDeliveryItemReal lmDeliveryItemReal) {
        return carApplicationMapper.insertLmDeliveryItemFieldReal(lmDeliveryItemReal);
    }

    /**
     * 查询是否存在特定交货单号和包装的记录
     *
     * @param lmDnNum,lmDnline
     * @return 结果
     */
    @Override
    public boolean checkSaleExists(java.sql.Date yesterday,String lmDnNum, String lmDnLine) {
        return carApplicationMapper.checkSaleExists(yesterday,lmDnNum, lmDnLine);
    }

    /**
     * 查询是否存在特定交货单号和包装的记录
     *
     * @param lmDnNum,lmDnline
     * @return 结果
     */
    @Override
    public boolean checkSaleExistsNoDate(String lmDnNum, String lmDnLine) {
        return carApplicationMapper.checkSaleExistsNoDate(lmDnNum, lmDnLine);
    }

    /**
     * 通过交货单号和行号删除
     *
     * @param lmDnNum,lmDnLine
     * @return 结果
     */
    @Override
    public int deleteByLmDnNumAndLmDnLine(String lmDnNum, String lmDnLine) {
        return carApplicationMapper.deleteByLmDnNumAndLmDnLine(lmDnNum,lmDnLine);
    }

    /**
     * 通过交货单号和行号和时间删除
     *
     * @param lmDnNum,lmDnLine,yesterday
     * @return 结果
     */
    @Override
    public int deleteByLmDnNumAndLmDnLineAndDate(java.sql.Date yesterday, String lmDnNum, String lmDnLine) {
        return carApplicationMapper.deleteByLmDnNumAndLmDnLineAndDate(yesterday, lmDnNum, lmDnLine);
    }

    /**
     * 更新出货装箱单操作行
     *
     * @param lmDeliveryItemReal 出货装箱单行
     * @return 结果
     */
    @Override
    public int updateLmDeliveryItemFieldReal(LmDeliveryItemReal lmDeliveryItemReal) {
        return carApplicationMapper.updateLmDeliveryItemFieldReal(lmDeliveryItemReal);
    }

    /**
     * 查询预约重量
     *
     * @param carNumber
     * @return 结果
     */
    @Override
    public BigDecimal getTotalGrossWeightByCarNumber(String carNumber) {
        return carApplicationMapper.getTotalGrossWeightByCarNumber(carNumber);
    }

    /**
     * 查询预约号码
     *
     * @param carNumber
     * @return 结果
     */
    @Override
    public String getOrderIdByCarNumberAndAppointmentStatus(String carNumber) {
        return carApplicationMapper.getOrderIdByCarNumberAndAppointmentStatus(carNumber);
    }

    /**
     * 查询车牌号，预约号码键值对
     *
     * @param
     * @return 结果
     */
    @Override
    public List<Map<String, String>> getMapByAppointmentStatus() {
        return carApplicationMapper.getMapByAppointmentStatus();
    }

    /**
     * 通过预约号码不为空，对lm_sale_item_insert中的lm_match_flag标记进行赋值为Yes
     *
     * @return 结果
     */
    @Override
    public int updateLmMatchFlag(java.sql.Date yesterday){
        return carApplicationMapper.updateLmMatchFlag(yesterday);
    };

    /**
     * 通过预约号码进行分组，查询毛重的和存入到车辆预约表lm_car_application中
     *
     * @return 结果
     */
    @Override
    public int updateTotalGrossWeightByOrderId(java.sql.Date yesterday) {
        return carApplicationMapper.updateTotalGrossWeightByOrderId(yesterday);
    }

    /**
     * 查询修改销售预约重量标记
     *
     * @param carNumber
     * @return 结果
     */
    @Override
    public String getEditFlagByCarNumberAndAppointmentStatus(String carNumber) {
        return carApplicationMapper.getEditFlagByCarNumberAndAppointmentStatus(carNumber);
    }

    /**
     * 通过当前时间，车牌号，预约状态为待制卡查询最小流水号
     *
     * @param param
     * @return 结果
     */
    @Override
    public Long getMinSerialNumberByCurrentTimeAndCarNumberAndAppointmentStatus(ApplicationQueryParam param) {
        return carApplicationMapper.getMinSerialNumberByCurrentTimeAndCarNumberAndAppointmentStatus(param);
    }

    /**
     * 通过车牌号查询进厂状态
     *
     * @param carNumber
     * @return 结果
     */
    @Override
    public List<Integer> getEnterStatusByCarNumber(String carNumber, Long serialNumber) {
        return carApplicationMapper.getEnterStatusByCarNumber(carNumber, serialNumber);
    }

    /**
     * 更新重量
     *
     * @param param
     * @return 结果
     */
    @Override
    public ApplicationEditParam updateApplicationWeight(ApplicationEditParam param){
        ApplicationEditParam param2= carApplicationMapper.getTolerance();
        if (param.getBusinessType()==0){
            param.setToleranceAdd(param2.getToleranceAdd());
            param.setToleranceDec(param2.getToleranceDec());
            param.setAppointmentWeight(param.getAppointmentWeight() != null ? param.getAppointmentWeight() : BigDecimal.ZERO);
            param.setUpFloatingWeight((param.getAppointmentWeight()).multiply(BigDecimal.ONE.add(param.getToleranceAdd())));
            param.setDownFloatingWeight((param.getAppointmentWeight()).multiply(BigDecimal.ONE.add(param.getToleranceDec())));
        }else if (param.getBusinessType()==1){
            param.setToleranceAdd(param2.getToleranceAdd());
            param.setToleranceDec(param2.getToleranceDec());
            param.setAppointmentWeight(BigDecimal.ZERO);
            param.setUpFloatingWeight((param.getAppointmentWeight()).multiply(BigDecimal.ONE.add(param.getToleranceAdd())));
            param.setDownFloatingWeight((param.getAppointmentWeight()).multiply(BigDecimal.ONE.add(param.getToleranceDec())));
        }
        else{
            param.setToleranceAdd(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            param.setToleranceDec(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            param.setAppointmentWeight(param.getAppointmentWeight() != null ? param.getAppointmentWeight() : BigDecimal.ZERO);
            param.setUpFloatingWeight((param.getAppointmentWeight()).multiply(BigDecimal.ONE.add(param.getToleranceAdd())));
            param.setDownFloatingWeight((param.getAppointmentWeight()).multiply(BigDecimal.ONE.add(param.getToleranceDec())));
        }
        return param;
    }

    /**
     * 通过交货单号和行号和时间删除
     *
     * @param yesterday,lmDnNums,lmDnLines
     * @return 结果
     */
    @Override
    public void batchDeleteByLmDnNumAndLmDnLineAndDate(java.sql.Date yesterday, List<String> lmDnNums, List<String> lmDnLines) {
        if (lmDnNums != null && lmDnLines != null && !lmDnNums.isEmpty() && !lmDnLines.isEmpty()) {
            carApplicationMapper.batchDeleteByLmDnNumAndLmDnLineAndDate(yesterday, lmDnNums, lmDnLines);
        }
    }

    /**
     * 通过交货单号和行号删除
     *
     * @param lmDnNums,lmDnLines
     * @return 结果
     */
    @Override
    public void batchDeleteByLmDnNumAndLmDnLineReal(List<String> lmDnNums, List<String> lmDnLines) {
        if (lmDnNums != null && lmDnLines != null && !lmDnNums.isEmpty() && !lmDnLines.isEmpty()) {
            carApplicationMapper.batchDeleteByLmDnNumAndLmDnLineReal(lmDnNums, lmDnLines);
        }
    }

    /**
     * 批量新增
     *
     * @param itemsToInsert
     * @return 结果
     */
    @Override
    public void batchInsertLmDeliveryItemFieldReal(List<LmDeliveryItemReal> itemsToInsert) {
        if (itemsToInsert != null && !itemsToInsert.isEmpty()) {
            carApplicationMapper.batchInsertLmDeliveryItemFieldReal(itemsToInsert);
        }
    }

    /**
     * 批量更新
     *
     * @param itemsToUpdate
     * @return 结果
     */
    @Override
    public void batchUpdateLmDeliveryItemFieldReal(List<LmDeliveryItemReal> itemsToUpdate) {
        if (itemsToUpdate != null && !itemsToUpdate.isEmpty()) {
            carApplicationMapper.batchUpdateLmDeliveryItemFieldReal(itemsToUpdate);
        }
    }

    /**
     * 批量新增
     *
     * @param itemsToInsert
     * @return 结果
     */
    @Override
    public void batchInsertLmDeliveryItemFieldThird(List<LmDeliveryItemField> itemsToInsert) {
        if (itemsToInsert != null && !itemsToInsert.isEmpty()) {
            carApplicationMapper.batchInsertLmDeliveryItemFieldThird(itemsToInsert);
        }
    }

    /**
     * 批量更新
     *
     * @param itemsToUpdate
     * @return 结果
     */
    @Override
    public void batchUpdateLmDeliveryItemFieldThird(List<LmDeliveryItemField> itemsToUpdate) {
        if (itemsToUpdate != null && !itemsToUpdate.isEmpty()) {
            carApplicationMapper.batchUpdateLmDeliveryItemFieldThird(itemsToUpdate);
        }
    }

    /**
     * 批量删除
     *
     * @param lmDnNums,lmDnLines
     * @return 结果
     */
    @Override
    public void batchDeleteByLmDnNumAndLmDnLine(List<String> lmDnNums, List<String> lmDnLines) {
        if (lmDnNums != null && lmDnLines != null && !lmDnNums.isEmpty() && !lmDnLines.isEmpty()) {
            carApplicationMapper.batchDeleteByLmDnNumAndLmDnLine(lmDnNums, lmDnLines);
        }
    }

    /**
     * 查询是否存在供应商ID
     *
     * @param ID
     * @return 结果
     */
    @Override
    public boolean checkIDExists(String ID) {
        return carApplicationMapper.checkIDExists(ID);
    }

    /**
     * 查询是否存在名称和时间
     *
     * @param name,date
     * @return 结果
     */
    @Override
    public boolean checkNameAndDateExists(String name,Date date) {
        return carApplicationMapper.checkNameAndDateExists(name,date);
    }

    /**
     * 根据ID更新供应商
     *
     * @return
     */
    @Override
    public int updateSupplierByID(ApplicationSupplier applicationSupplier) {
        return carApplicationMapper.updateSupplierByID(applicationSupplier);
    }

    /**
     * 新增供应商
     *
     * @param applicationSupplier 供应商
     * @return 结果
     */
    @Override
    public int insertSupplier(ApplicationSupplier applicationSupplier) {
        return carApplicationMapper.insertSupplier(applicationSupplier);
    }

    /**
     * 根据名称和时间更新最后一次修改时间
     *
     * @return
     */
    @Override
    public int updateScrapAdvanceByNameAndDate(ApplicationScrapAdvance applicationScrapAdvance) {
        return carApplicationMapper.updateScrapAdvanceByNameAndDate(applicationScrapAdvance);
    }

    /**
     * 新增废料预付款信息
     *
     * @param applicationScrapAdvance
     * @return 结果
     */
    @Override
    public int insertScrapAdvance(ApplicationScrapAdvance applicationScrapAdvance) {
        return carApplicationMapper.insertScrapAdvance(applicationScrapAdvance);
    }

    /**
     * 查询是否存在名称和时间
     *
     * @param name,date
     * @return 结果
     */
    @Override
    public boolean checkScrapAdvanceInfoExists(String name, String date) {
        return carApplicationMapper.checkScrapAdvanceInfoExists(name,date);
    }
}
