package com.lemei.system.mapper;

import com.lemei.system.domain.LmCarApplication;
import com.lemei.system.domain.param.CarApplicationQueryParam;
import com.lemei.system.domain.param.CarDeliveryItemReal;
import com.lemei.system.domain.param.CarSaleQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 车辆预约Mapper接口
 *
 * @author yuqian
 * @date 2022-08-24
 */
@Mapper
public interface LmCarApplicationMapper
{
    /**
     * 查询车辆预约
     *
     * @param applicationId 车辆预约主键
     * @return 车辆预约
     */
    LmCarApplication selectLmCarApplicationByApplicationId(String applicationId);

    /**
     * 查询车辆预约列表
     *
     * @param lmCarApplication 车辆预约
     * @return 车辆预约集合
     */
    List<LmCarApplication> selectLmCarApplicationList(CarApplicationQueryParam lmCarApplication);

    /**
     * 新增车辆预约
     *
     * @param lmCarApplication 车辆预约
     * @return 结果
     */
    int insertLmCarApplication(LmCarApplication lmCarApplication);

    /**
     * 修改车辆预约
     *
     * @param lmCarApplication 车辆预约
     * @return 结果
     */
    int updateLmCarApplication(LmCarApplication lmCarApplication);

    /**
     * 修改状态
     *
     * @param lmCarApplication 车辆预约
     * @return 结果
     */
    int updateStatus(LmCarApplication lmCarApplication);

    /**
     * 修改扣重
     *
     * @param lmCarApplication 车辆预约
     * @return 结果
     */
    int updateLoseWeight(LmCarApplication lmCarApplication);

    /**
     * 修改销售预约重量
     *
     * @param lmCarApplication 车辆预约
     * @return 结果
     */
    int updateSaleWeight(LmCarApplication lmCarApplication);

    /**
     * 修改废料预约重量
     *
     * @param lmCarApplication 车辆预约
     * @return 结果
     */
    int updateScrapWeight(LmCarApplication lmCarApplication);

    /**
     * 修改出厂带栈板重量
     *
     * @param lmCarApplication 车辆预约
     * @return 结果
     */
    int updateShippedWithPalletWeight(LmCarApplication lmCarApplication);

    /**
     * 取消一次磅时修改车辆预约信息
     *
     * @param lmCarApplication 车辆预约主键
     * @return 结果
     */
    int updateOneLmCarApplicationByApplicationId(LmCarApplication lmCarApplication);

    /**
     * 取消二次磅时修改车辆预约信息
     *
     * @param lmCarApplication 车辆预约主键
     * @return 结果
     */
    int updateTwoLmCarApplicationByApplicationId(LmCarApplication lmCarApplication);

    /**
     * 作废时修改车辆预约信息
     *
     * @param lmCarApplication 车辆预约主键
     * @return 结果
     */
    int updateAllLmCarApplicationByApplicationId(LmCarApplication lmCarApplication);

    /**
     * 统计总数
     * @return
     */
    int selectApplicationCount();
    int selectTodayApplicationCount();

    /**
     * 删除车辆预约
     *
     * @param applicationId 车辆预约主键
     * @return 结果
     */
    int deleteLmCarApplicationByApplicationId(String applicationId);

    /**
     * 批量删除车辆预约
     *
     * @param applicationIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteLmCarApplicationByApplicationIds(String[] applicationIds);

    /**
     * 查询流水号最大值
     *
     * @param
     * @return 车辆预约
     */
    @Select("select max(serialNumber) from lm_car_application for update")
    Long selectMaxSerialNumber();

    /**
     * 通过车牌查询预约状态为非已完成并且进厂状态为的数目
     *
     * @param
     * @return 车辆预约
     */
    @Select("select count(*) from lm_car_application where car_number=#{lmCarApplication.carNumber} and appointmentStatus not in (6,7) ")
    Integer selectNonFinishedByCarNumber(@Param("lmCarApplication") LmCarApplication lmCarApplication);

    /**
     * 查询车辆预约指定列表
     *
     * @param
     * @return 车辆预约
     */
     List<LmCarApplication> selectLmCarApplicationListByIds(String[] ids);

    /**
     * 通过车牌号查询进厂状态
     *
     * @param
     * @return 进厂状态集合
     */
    @Select("SELECT enter_status FROM lm_car_application WHERE car_number = #{carNumber}")
    List<Integer> selectEnterStatusListByCarNumber(String carNumber);

    /**
     * 查询销售列表
     *
     * @param saleQueryParam
     * @return 销售集合
     */
    List<CarDeliveryItemReal> selectSaleList(CarSaleQueryParam saleQueryParam);

    /**
     * 查询销售详细
     *
     * @param lmId 销售主键
     */
    CarDeliveryItemReal selectSaleInfoByLmId(String lmId);

    /**
     * 修改销售详细
     *
     * @param lmDeliveryItemReal
     * @return 结果
     */
    int updateSaleInfo(CarDeliveryItemReal lmDeliveryItemReal);

    /**
     * 删除销售详细
     *
     * @param dnNumLinePairs
     * @return 结果
     */
    int deleteByDnNumLinePairsReal(@Param("list") List<Map<String, String>> dnNumLinePairs);

    /**
     * 删除销售详细
     *
     * @param dnNumLinePairs
     * @return 结果
     */
    int deleteByDnNumLinePairsInsert(@Param("list") List<Map<String, String>> dnNumLinePairs);

    /**
     * 查询销售详细
     *
     * @param lmIds
     * @return 结果
     */
    List<Map<String, String>> selectDnNumLineByLmIds(@Param("array") String[] lmIds);

    /**
     * 通过预约号码查询审计表中是否存在该预约号码
     *
     * @param orderId
     * @return 结果
     */
    @Select("SELECT EXISTS (SELECT 1 FROM lm_audit WHERE order_id = #{orderId})")
    boolean checkExitOrderIdByOrderId(@Param("orderId") String orderId);

    /**
     * 向审计表中插入数据
     *
     * @param lmCarApplication
     * @return 结果
     */
    @Insert("INSERT INTO lm_audit (order_id,originalWeight,editWeight,editPerson,editTime,editReason,businessType) VALUES (#{orderId},#{originalWeight},#{editWeight},#{editPerson},#{editTime},#{editReason},#{businessType})")
    int insertAuditLine(LmCarApplication lmCarApplication);

    /**
     * 通过预约号码更新审计表
     *
     * @param lmCarApplication
     * @return 结果
     */
    @Update("UPDATE lm_audit SET originalWeight = #{originalWeight},editWeight = #{editWeight},editPerson = #{editPerson},editTime = #{editTime},editReason = #{editReason},businessType = #{businessType} WHERE order_id = #{orderId}")
    int updateAuditLineByOrderId(LmCarApplication lmCarApplication);

    /**
     * 查询是否存在名称和时间
     *
     * @param name,date
     * @return 结果
     */
    @Select("SELECT EXISTS (SELECT 1 FROM lm_scrap_advance WHERE oa_name = #{name} and oa_date_time = #{date} )")
    boolean checkScrapAdvanceInfoExists(@Param("name") String name,@Param("date") String date);

    /**
     * 通过applicationId查询车牌号
     *
     * @param applicationId
     * @return 结果
     */
    @Select("SELECT car_number from lm_car_application WHERE application_id = #{applicationId}")
    String selectCarNumberByApplicationId(String applicationId);

    /**
     * 通过车牌号查询是否存在已进厂记录
     *
     * @param carNumber
     * @return 结果
     */
    @Select("SELECT EXISTS (SELECT 1 FROM lm_car_application WHERE enter_status = '1' and car_number = #{carNumber} )")
    boolean checkEnterStatusInfoExists(String carNumber);

}