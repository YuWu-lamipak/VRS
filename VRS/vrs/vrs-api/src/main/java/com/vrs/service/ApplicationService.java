package com.vrs.service;

import com.vrs.common.core.domain.ResultData;
import com.vrs.domain.VRSSupplierApp;
import com.vrs.domain.param.*;
import com.vrs.mapper.CarApplicationMapper;
import com.vrs.system.domain.VRSCarApplication;
import com.vrs.system.domain.param.CarApplicationQueryParam;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author yq
 * @CLassName ApplicationService
 * @Description
 * @date 2022/8/26 17:11
 **/
public interface ApplicationService {

    /**
     * 我的预约列表
     * @param param
     * @return
     */
    List<ResultData> getMyPageList(ApplicationQueryParam param);
    //数据字典列表
    List<ResultData> selectDictDataByType(String dictType);

    /**
     * 查询预约列表
     * @param param
     * @return
     */
    List<ResultData> getPageList(ApplicationQueryParam param);

    /**
     * 门卫列表数据
     * @param param
     * @return
     */
    List<ResultData> getGuardPageList(ApplicationQueryParam param);

    /**
     * 根据状态查询对应的车辆预约数
     * @return
     */
    Object selectDataByStatus();

    /**
     * 审核人员首页数据统计
     * @return
     */
    Object selectCheckerDataByStatus();

    /**
     * 本人
     * @return
     */
    Object selectSelfDataByStatus();

    /**
     * 新增预约
     * @param param
     * @return
     */
    Object insertCarApplication(ApplicationAddParam param);

    /**
     * 详情
     * @param applicationId
     * @return
     */
    ResultData carApplicationDetail(String applicationId);

    /**
     * 审核、进出厂、删除
     * @param param
     * @return
     */
    int updateApplication(ApplicationEditParam param);

    /**
     * 修改预约单
     * @param param
     * @return
     */
    Object updateCarApplication(ApplicationEditParam param);

    List<VRSSupplierApp> selectVRSSupplierList();

    /**
     * 制卡信息新增
     */
    Object updateCardInfo(ApplicationEditParam param);

    /**
     * 一磅上磅信息新增
     */
    Object updateOneWeightingInfo(ApplicationEditParam param);

    /**
     * 通过车牌号查询
     */
    ResultData selectOneWeightingInfo(ApplicationQueryParam param);

    /**
     * 一磅写入信息新增
     */
    Object updateOneWeightWriteInfo(ApplicationEditParam param);

    /**
     * 二磅上磅信息新增
     */
    Object updateTwoWeightingInfo(ApplicationEditParam param);

    /**
     * 通过车牌号查询
     */
    ResultData selectTwoWeightingInfo(ApplicationQueryParam param);

    /**
     * 二磅写入信息新增
     */
    Object updateTwoWeightWriteInfo(ApplicationEditParam param);

    /**
     * 二磅写入失败时更新预约状态
     */
    Object updateAppointmentStatus(ApplicationEditParam param);

    /**
     * 退卡信息新增
     */
    Object updateRefundCardInfo(ApplicationEditParam param);
    /**
     * 查询流水号最大值
     */
    Long selectMaxSerialNumber();
    /**
     * 通过当前时间查询车牌号的集合
     *
     * @param
     * @return 车辆预约
     */
    List<String> selectCarNumberByApplicationDate(java.sql.Date date);

    /**
     * 通过车牌号查询最新预约时间
     */
    java.sql.Date selectApplicationDateByCarNumber(ApplicationQueryParam param);

    /**
     * 通过车牌号，最新预约时间查询预约状态
     */
    Integer selectAppointmentStatus(ApplicationQueryParam param);

    /**
     * 通过车牌号，最新预约时间查询审核状态
     */
    Integer selectStatus(ApplicationQueryParam param);

    /**
     * 通过车牌号，最新预约时间查询进厂状态
     */
    Integer selectEnterStatus(ApplicationQueryParam param);

    /**
     * 通过流水号、预约时间、车牌号查询状态
     *
     * @return
     */
    ResultData selectStatusResult(ApplicationQueryParam param);

    /**
     * 通过车牌号，最新预约时间，进厂状态为已进厂（1）查询业务类型
     */
    Integer selectBusinessType(ApplicationQueryParam param);

    /**
     * 通过车牌号，查询上浮重量、下浮重量、实kg、启用管控
     */
    ResultData selectUDGE(ApplicationQueryParam param);

    /**
     * 查询是否存在特定交货单号和行号的记录
     *
     * @param vrsDnnum,vrsDnLine
     * @return 结果
     */
    boolean checkRecordExists(String vrsDnnum, String vrsDnLine);

    /**
     * 根据交货单号和行号进行体更新
     *
     * @return
     */
    int updateLmSalesOrderItemByvrsDnnumAndvrsDnLine(VRSDeliveryItemField VRSDeliveryItemField);

    /**
     * 新增出货装箱单行
     *
     * @param VRSDeliveryItemField 出货装箱单行
     * @return 结果
     */
    int insertVRSDeliveryItemFieldThird(VRSDeliveryItemField VRSDeliveryItemField);

    /**
     * 查询出货装箱单操作行
     *
     * @return 结果
     */
    List<VRSDeliveryItemField> selectVRSDeliveryItemFieldInsertList(java.sql.Date yesterday);

    /**
     * 新增出货装箱单操作行
     *
     * @param VRSDeliveryItemReal 出货装箱单行
     * @return 结果
     */
    int insertVRSDeliveryItemFieldReal(VRSDeliveryItemReal VRSDeliveryItemReal);

    /**
     * 查询是否存在特定交货单号和行号的记录
     *
     * @param vrsDnnum,vrsDnLine
     * @return 结果
     */
    boolean checkSaleExists(java.sql.Date yesterday, String vrsDnnum, String vrsDnLine);

    /**
     * 查询是否存在特定交货单号和行号的记录
     *
     * @param vrsDnnum,vrsDnLine
     * @return 结果
     */
    boolean checkSaleExistsNoDate(String vrsDnnum, String vrsDnLine);

    /**
     * 通过交货单号和行号删除
     *
     * @param vrsDnnum,vrsDnLine
     * @return 结果
     */
    int deleteByvrsDnnumAndvrsDnLine(String vrsDnnum, String vrsDnLine);

    /**
     * 通过交货单号和行号和时间删除
     *
     * @param vrsDnnum,vrsDnLine,yesterday
     * @return 结果
     */
    int deleteByvrsDnnumAndvrsDnLineAndDate(java.sql.Date yesterday,String vrsDnnum,String vrsDnLine);

    /**
     * 更新出货装箱单操作行
     *
     * @param VRSDeliveryItemReal 出货装箱单行
     * @return 结果
     */
    int updateVRSDeliveryItemFieldReal(VRSDeliveryItemReal VRSDeliveryItemReal);

    /**
     * 查询预约重量
     *
     * @param carNumber
     * @return 结果
     */
    BigDecimal getTotalGrossWeightByCarNumber(String carNumber);

    /**
     * 查询预约号码
     *
     * @param carNumber
     * @return 结果
     */
    String getOrderIdByCarNumberAndAppointmentStatus(String carNumber);

    /**
     * 查询车牌号，预约号码键值对
     *
     * @param
     * @return 结果
     */
    List<Map<String, String>> getMapByAppointmentStatus();

    /**
     * 通过预约号码不为空，对lm_sale_item_insert中的lm_match_flag标记进行赋值为Yes
     *
     * @return 结果
     */
    int updateLmMatchFlag(java.sql.Date yesterday);


    /**
     * 通过预约号码进行分组，查询毛重的和存入到车辆预约表lm_car_application中
     *
     * @return 结果
     */
    int updateTotalGrossWeightByOrderId(java.sql.Date yesterday);

    /**
     * 查询修改销售预约重量标记
     *
     * @param carNumber
     * @return 结果
     */
    String getEditFlagByCarNumberAndAppointmentStatus(String carNumber);

    /**
     * 通过当前时间，车牌号，预约状态为待制卡查询最小流水号
     *
     * @param param
     * @return 结果
     */
    Long getMinSerialNumberByCurrentTimeAndCarNumberAndAppointmentStatus(ApplicationQueryParam param);

    /**
     * 通过车牌号查询进厂状态
     *
     * @param carNumber
     * @return 结果
     */
    List<Integer> getEnterStatusByCarNumber(String carNumber, Long serialNumber);

    /**
     * 更新重量
     *
     * @param param
     * @return 结果
     */
    ApplicationEditParam updateApplicationWeight(ApplicationEditParam param);

    /**
     * 通过交货单号和行号和时间删除
     *
     * @param yesterday,vrsDnnums,vrsDnLines
     * @return 结果
     */
    void batchDeleteByvrsDnnumAndvrsDnLineAndDate(java.sql.Date yesterday,List<String> vrsDnnums,List<String> vrsDnLines);

    /**
     * 通过交货单号和行号删除
     *
     * @param vrsDnnums,vrsDnLines
     * @return 结果
     */
    void batchDeleteByvrsDnnumAndvrsDnLineReal(List<String> vrsDnnums,List<String> vrsDnLines);

    /**
     * 批量新增
     *
     * @param itemsToInsert
     * @return 结果
     */
    void batchInsertVRSDeliveryItemFieldReal(List<VRSDeliveryItemReal> itemsToInsert);

    /**
     * 批量更新
     *
     * @param itemsToUpdate
     * @return 结果
     */
    void batchUpdateVRSDeliveryItemFieldReal(List<VRSDeliveryItemReal> itemsToUpdate);

    /**
     * 批量新增
     *
     * @param itemsToInsert
     * @return 结果
     */
    void batchInsertVRSDeliveryItemFieldThird(List<VRSDeliveryItemField> itemsToInsert);

    /**
     * 批量更新
     *
     * @param itemsToUpdate
     * @return 结果
     */
    void batchUpdateVRSDeliveryItemFieldThird(List<VRSDeliveryItemField> itemsToUpdate);

    /**
     * 批量删除
     *
     * @param vrsDnnums,vrsDnLines
     * @return 结果
     */
    void batchDeleteByvrsDnnumAndvrsDnLine(List<String> vrsDnnums,List<String> vrsDnLines);

    /**
     * 查询是否存在供应商ID
     *
     * @param ID
     * @return 结果
     */
    boolean checkIDExists(String ID);

    /**
     * 查询是否存在名称和时间
     *
     * @param name,date
     * @return 结果
     */
    boolean checkNameAndDateExists(String name,Date date);

    /**
     * 根据ID更新供应商
     *
     * @return
     */
    int updateSupplierByID(ApplicationSupplier applicationSupplier);

    /**
     * 新增供应商
     *
     * @param applicationSupplier 供应商
     * @return 结果
     */
    int insertSupplier(ApplicationSupplier applicationSupplier);

    /**
     * 根据名称和时间更新最后一次修改时间
     *
     * @return
     */
    int updateScrapAdvanceByNameAndDate(ApplicationScrapAdvance applicationScrapAdvance);

    /**
     * 新增废料预付款信息
     *
     * @param applicationScrapAdvance
     * @return 结果
     */
    int insertScrapAdvance(ApplicationScrapAdvance applicationScrapAdvance);

    /**
     * 查询是否存在名称和时间
     *
     * @param name,date
     * @return 结果
     */
    boolean checkScrapAdvanceInfoExists(String name,String date);

    /**
     * 打印磅单请求查询DN与IDcard
     *
     * @param orderId 业务ID
     * @param businessType 业务类型
     * @return 结果
     */
    ResultData getPrintWeightTicketInfo(String orderId, Integer businessType);
}
