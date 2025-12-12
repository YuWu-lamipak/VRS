package com.vrs.mapper;


import com.vrs.common.core.domain.ResultData;
import com.vrs.domain.VRSSupplierApp;
import com.vrs.domain.param.*;
import org.apache.ibatis.annotations.*;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约mapper层
 */
@Mapper
public interface CarApplicationMapper {
    /**
     * 根据条件查询我的预约列表
     */
    List<ResultData> getMyApplicationList(ApplicationQueryParam param);

    //根据条件查询字典类型列表
    List<ResultData> selectDictDataByType(String dictType);

    /**
     * 查询审核预约列表
     */
    List<ResultData> getApplicationList(ApplicationQueryParam param);

    /**
     * 查询门卫数据
     *
     * @param param
     * @return
     */
    List<ResultData> getGuardApplicationList(ApplicationQueryParam param);

    /**
     * 门卫根据条件查询列表
     * @param param
     * @return
     */
//    List<ResultData> getGuardPageList(ApplicationQueryParam param);

    /**
     * 门卫根据进出厂状态状态统计数据
     *
     * @param param
     * @return
     */
    int selectGuardDataByStatus(ApplicationQueryParam param);

    /**
     * 审核端今日数据统计
     *
     * @param param
     * @return
     */
    int selectCheckDataByStatus(ApplicationQueryParam param);

    /**
     * 查询自己的数据
     *
     * @param param
     * @return
     */
    int selectSelfDataByStatus(ApplicationQueryParam param);

    /**
     * 新增预约
     *
     * @param param
     * @return
     */
    int insertCarApplication(ApplicationAddParam param);

    /**
     * 查询预约单详情
     *
     * @param applicationId
     * @return
     */
    ResultData selectCarApplicationById(String applicationId);

    /**
     * 修改
     *
     * @param param
     * @return
     */
    int updateCarApplicationById(ApplicationEditParam param);

    List<VRSSupplierApp> selectVRSSupplierList();

    /**
     * 制卡信息新增
     *
     * @param param
     * @return
     */
    int updateCardInfo(ApplicationEditParam param);

    /**
     * 一磅上磅信息新增
     *
     * @param param
     * @return
     */
    int updateOneWeightingInfo(ApplicationEditParam param);

    /**
     * 通过车牌号查询
     *
     * @return
     */
    ResultData selectOneWeightingInfo(ApplicationQueryParam param);

    /**
     * 一磅写入信息新增
     *
     * @param param
     * @return
     */
    int updateOneWeightWriteInfo(ApplicationEditParam param);

    /**
     * 二磅上磅信息新增
     *
     * @param param
     * @return
     */
    int updateTwoWeightingInfo(ApplicationEditParam param);

    /**
     * 通过车牌号查询
     *
     * @return
     */
    ResultData selectTwoWeightingInfo(ApplicationQueryParam param);

    /**
     * 二磅写入信息新增
     *
     * @param param
     * @return
     */
    int updateTwoWeightWriteInfo(ApplicationEditParam param);

    /**
     * 二磅写入失败时更新预约状态
     *
     * @param param
     * @return
     */
    int updateAppointmentStatus(ApplicationEditParam param);

    /**
     * 退卡信息新增
     *
     * @param param
     * @return
     */
    int updateRefundCardInfo(ApplicationEditParam param);

    /**
     * 查询流水号最大值
     *
     * @param
     * @return 车辆预约
     */
    @Select("select max(serialNumber) from lm_car_application for update")
    Long selectMaxSerialNumber();

    /**
     * 通过当前时间查询车牌号的集合
     *
     * @param
     * @return 车辆预约
     */
    @Select("select car_number from lm_car_application where application_date=#{date} and appointmentStatus=0")
    List<String> selectCarNumberByApplicationDate(@Param("date") java.sql.Date date);

    /**
     * 通过车牌号查询最新预约时间
     *
     * @param param
     * @return
     */
    java.sql.Date selectApplicationDateByCarNumber(ApplicationQueryParam param);

    /**
     * 通过车牌号，最新预约时间查询预约状态
     *
     * @return
     */
    Integer selectAppointmentStatus(ApplicationQueryParam param);

    /**
     * 通过车牌号，最新预约时间查询审核状态
     *
     * @return
     */
    Integer selectStatus(ApplicationQueryParam param);

    /**
     * 通过车牌号，最新预约时间查询进厂状态
     *
     * @return
     */
    Integer selectEnterStatus(ApplicationQueryParam param);

    /**
     * 通过流水号、预约时间、车牌号查询状态
     *
     * @return
     */
    ResultData selectStatusResult(ApplicationQueryParam param);

    /**
     * 通过车牌号，查询业务类型
     *
     * @return
     */
    Integer selectBusinessType(ApplicationQueryParam param);

    /**
     * 通过车牌号，查询上浮重量、下浮重量、实kg、启用管控
     *
     * @return
     */
    ResultData selectUDGE(ApplicationQueryParam param);

    /**
     * 查询是否存在特定交货单号和行号的记录
     *
     * @param vrsDnnum,vrsDnLine
     * @return 结果
     */
    @Select("SELECT EXISTS (SELECT 1 FROM lm_sale_item_insert WHERE lm_dnnum = #{vrsDnnum} AND lm_dnline = #{vrsDnLine})")
    boolean checkRecordExists(@Param("vrsDnnum") String vrsDnnum, @Param("vrsDnLine") String vrsDnLine);

    /**
     * 通过交货单号和行号删除
     *
     * @param vrsDnnum,vrsDnLine
     * @return 结果
     */
    @Delete("DELETE FROM lm_sale_item_insert WHERE lm_dnnum = #{vrsDnnum} AND lm_dnline = #{vrsDnLine}")
    int deleteByvrsDnnumAndvrsDnLine(@Param("vrsDnnum") String vrsDnnum, @Param("vrsDnLine") String vrsDnLine);

    /**
     * 通过交货单号和行号和时间删除
     *
     * @param vrsDnnum,vrsDnLine
     * @return 结果
     */
    @Delete("DELETE FROM lm_sale_item_real WHERE lm_loadTime >= #{yesterday} and lm_dnnum = #{vrsDnnum} AND lm_dnline = #{vrsDnLine} AND lm_order_id IS NULL")
    int deleteByvrsDnnumAndvrsDnLineAndDate(@Param("yesterday") java.sql.Date yesterday, @Param("vrsDnnum") String vrsDnnum, @Param("vrsDnLine") String vrsDnLine);

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
    @Results({
            @Result(column = "lm_dnnum", property = "vrsDnnum"),
            @Result(column = "lm_actualdeliverydate", property = "vrsActualdeliverydate"),
            @Result(column = "lm_loadTime", property = "lmLoadTime"),
            @Result(column = "lm_car_number", property = "vrsCarNumber"),
            @Result(column = "lm_dnline", property = "vrsDnLine"),
            @Result(column = "lm_packagingid", property = "vrsPackagingid"),
            @Result(column = "lm_grossweight", property = "vrsGrossweight")
    })
    @Select("SELECT lm_dnnum, lm_actualdeliverydate, lm_loadTime, lm_dnline, lm_car_number, lm_packagingid, lm_grossweight FROM lm_sale_item_insert where lm_loadTime >= #{yesterday} AND lm_match_flag IS NULL")
    List<VRSDeliveryItemField> selectVRSDeliveryItemFieldInsertList(@Param("yesterday") java.sql.Date yesterday);

    /**
     * 新增出货装箱单操作行
     *
     * @param VRSDeliveryItemReal 出货装箱单行
     * @return 结果
     */
    @Insert("INSERT INTO lm_sale_item_real (lm_Id, lm_dnnum, lm_actualdeliverydate, lm_loadTime, lm_dnline, lm_order_id, lm_car_number, lm_packagingid, lm_grossweight, lm_insert_time) " +
            "VALUES (#{vrsId}, #{vrsDnnum}, #{vrsActualdeliverydate}, #{lmLoadTime}, #{vrsDnLine}, #{vrsOrderid}, #{vrsCarNumber}, #{vrsPackagingid}, #{vrsGrossweight}, #{vrsInsertTime})")
    int insertVRSDeliveryItemFieldReal(VRSDeliveryItemReal VRSDeliveryItemReal);

    /**
     * 查询是否存在特定交货单号和包装的记录
     *
     * @param vrsDnnum,vrsDnLine
     * @return 结果
     */
    @Select("SELECT EXISTS (SELECT 1 FROM lm_sale_item_real WHERE lm_loadTime >= #{yesterday} and lm_dnnum = #{vrsDnnum} AND lm_dnline= #{vrsDnLine})")
    boolean checkSaleExists(@Param("yesterday") java.sql.Date yesterday, @Param("vrsDnnum") String vrsDnnum, @Param("vrsDnLine") String vrsDnLine);

    /**
     * 查询是否存在特定交货单号和包装的记录
     *
     * @param vrsDnnum,vrsDnLine
     * @return 结果
     */
    @Select("SELECT EXISTS (SELECT 1 FROM lm_sale_item_real WHERE lm_dnnum = #{vrsDnnum} AND lm_dnline= #{vrsDnLine})")
    boolean checkSaleExistsNoDate(@Param("vrsDnnum") String vrsDnnum, @Param("vrsDnLine") String vrsDnLine);

    /**
     * 更新出货装箱单操作行
     *
     * @param VRSDeliveryItemReal 出货装箱单行
     * @return 结果
     */
    @Update({
            "UPDATE lm_sale_item_real",
            "SET lm_actualdeliverydate = #{vrsActualdeliverydate},",
            "lm_loadTime = #{lmLoadTime},",
            "lm_dnline = #{vrsDnLine},",
            "lm_order_id = CASE",
            "    WHEN #{vrsOrderid} IS NOT NULL OR lm_order_id IS NOT NULL THEN COALESCE(lm_order_id, #{vrsOrderid})",
            "    ELSE #{vrsOrderid}",
            "END,",
            "lm_car_number = #{vrsCarNumber},",
            "lm_grossweight = #{vrsGrossweight},",
            "lm_update_time = #{vrsUpdateTime}",
            "WHERE lm_dnnum = #{vrsDnnum}",
            "AND lm_dnline = #{vrsDnLine}"
    })
    int updateVRSDeliveryItemFieldReal(VRSDeliveryItemReal VRSDeliveryItemReal);

    /**
     * 查询预约重量
     *
     * @param carNumber
     * @return 结果
     */
    @Select("SELECT SUM(lm_grossweight) AS total_grossweight " +
            "FROM lm_sale_item_real " +
            "WHERE lm_order_id IN (" +
            "    SELECT order_id " +
            "    FROM lm_car_application " +
            "    WHERE car_number = #{carNumber} " +
            "      AND appointmentStatus = '3' " +
            ") " +
            "GROUP BY lm_order_id")
    BigDecimal getTotalGrossWeightByCarNumber(@Param("carNumber") String carNumber);

    /**
     * 查询预约号码
     *
     * @param carNumber
     * @return 结果
     */
    @Select("SELECT order_id " +
            "FROM lm_car_application " +
            "WHERE car_number = #{carNumber} " +
            "AND appointmentStatus = '3' ")
    String getOrderIdByCarNumberAndAppointmentStatus(@Param("carNumber") String carNumber);

    /**
     * 查询车牌号，预约号码键值对
     *
     * @param
     * @return 结果
     */
    @Select("SELECT car_number, order_id FROM lm_car_application WHERE appointmentStatus = '3'")
    @MapKey("car_number")
    List<Map<String, String>> getMapByAppointmentStatus();

    /**
     * 通过预约号码不为空，对lm_sale_item_insert中的lm_match_flag标记进行赋值为Yes
     *
     * @return 结果
     */
    @Update("UPDATE lm_sale_item_insert si " +
            "JOIN lm_sale_item_real sr " +
            "ON si.lm_dnnum = sr.lm_dnnum AND si.lm_dnline = sr.lm_dnline " +
            "SET si.lm_match_flag = 'Yes' " +
            "WHERE sr.lm_order_id IS NOT NULL " +
            "AND si.lm_loadTime >= #{yesterday}")
    int updateLmMatchFlag(@Param("yesterday") java.sql.Date yesterday);

    /**
     * 通过预约号码进行分组，查询毛重的和存入到车辆预约表lm_car_application中
     *
     * @return 结果
     */
//    @Update("UPDATE lm_car_application ca " +
//            "SET ca.appointmentWeight = (" +
//            "    SELECT SUM(sir.lm_grossweight) " +
//            "    FROM lm_sale_item_real sir " +
//            "    WHERE sir.lm_order_id = ca.order_id" +
//            ")," +
//            "ca.upFloatingWeight = (" +
//            "    SELECT (SUM(sir.lm_grossweight) + COALESCE(ca.loseWeight, 0)) * (1+ca.toleranceAdd) " +
//            "    FROM lm_sale_item_real sir " +
//            "    WHERE sir.lm_order_id = ca.order_id" +
//            ")," +
//            "ca.downFloatingWeight = (" +
//            "    SELECT (SUM(sir.lm_grossweight) + COALESCE(ca.loseWeight, 0)) * (1+ca.toleranceDec) " +
//            "    FROM lm_sale_item_real sir " +
//            "    WHERE sir.lm_order_id = ca.order_id" +
//            ")" +
//            "WHERE EXISTS (" +
//            "    SELECT 1 " +
//            "    FROM lm_sale_item_real sir " +
//            "    WHERE sir.lm_order_id = ca.order_id" +
//            "    AND ca.editFlag = 'No'" +
//            ")")
//    int updateTotalGrossWeightByOrderId();

    @Update("UPDATE lm_car_application ca " +
            "JOIN ( " +
            " SELECT sir.lm_order_id, " +
            " SUM(sir.lm_grossweight) AS total_weight " +
            " FROM lm_sale_item_real sir " + "WHERE sir.lm_loadTime>=#{yesterday}"+
            " GROUP BY sir.lm_order_id " +
            ") AS subquery " +
            "ON ca.order_id = subquery.lm_order_id " +
            "SET ca.appointmentWeight = subquery.total_weight, " +
            " ca.upFloatingWeight = (subquery.total_weight) * (1 + ca.toleranceAdd), " +
            " ca.downFloatingWeight = (subquery.total_weight) * (1 + ca.toleranceDec) " +
            "WHERE ca.appointmentStatus = 3 " + "AND ca.editFlag = 'No'")
    int updateTotalGrossWeightByOrderId(java.sql.Date yesterday);

    /**
     * 查询修改销售预约重量标记
     *
     * @param carNumber
     * @return 结果
     */
    @Select("SELECT editFlag " +
            "FROM lm_car_application " +
            "WHERE car_number = #{carNumber} " +
            "AND appointmentStatus IN (0, 1, 2, 3, 4, 5)")
    String getEditFlagByCarNumberAndAppointmentStatus(@Param("carNumber") String carNumber);

    /**
     * 通过当前时间，车牌号，预约状态为待制卡查询最小流水号
     *
     * @param param
     * @return 结果
     */
    @Select("select min(serialNumber) from lm_car_application where application_date=#{param.applicationDate} and car_number=#{param.carNumber} and appointmentStatus=#{param.appointmentStatus}")
    Long getMinSerialNumberByCurrentTimeAndCarNumberAndAppointmentStatus(@Param("param") ApplicationQueryParam param);

    /**
     * 通过车牌号查询进厂状态
     *
     * @param carNumber
     * @return 结果
     */
    @Select("select enter_status from lm_car_application where car_number = #{carNumber} and serialNumber < #{serialNumber}")
    List<Integer> getEnterStatusByCarNumber(@Param("carNumber") String carNumber,@Param("serialNumber") Long serialNumber);

    /**
     * 查询容差
     *
     * @return
     */
    ApplicationEditParam getTolerance();

    /**
     * 通过交货单号和行号和时间删除
     *
     * @param yesterday,vrsDnnums,vrsDnLines
     * @return 结果
     */
    @InsertProvider(type = SqlProvider.class, method = "batchDeleteByvrsDnnumAndvrsDnLineAndDate")
    void batchDeleteByvrsDnnumAndvrsDnLineAndDate(@Param("yesterday") java.sql.Date yesterday, @Param("vrsDnnums") List<String> vrsDnnums, @Param("vrsDnLines") List<String> vrsDnLines);

    /**
     * 通过交货单号和行号删除
     *
     * @param vrsDnnums,vrsDnLines
     * @return 结果
     */
    @InsertProvider(type = SqlProvider.class, method = "batchDeleteByvrsDnnumAndvrsDnLineReal")
    void batchDeleteByvrsDnnumAndvrsDnLineReal(@Param("vrsDnnums") List<String> vrsDnnums, @Param("vrsDnLines") List<String> vrsDnLines);

    /**
     * 批量新增
     *
     * @param itemsToInsert
     * @return 结果
     */
    @InsertProvider(type = SqlProvider.class, method = "batchInsertVRSDeliveryItemFieldReal")
    void batchInsertVRSDeliveryItemFieldReal(@Param("items") List<VRSDeliveryItemReal> itemsToInsert);

    /**
     * 批量更新
     *
     * @param itemsToUpdate
     * @return 结果
     */
    @UpdateProvider(type = SqlProvider.class, method = "batchUpdateVRSDeliveryItemFieldReal")
    void batchUpdateVRSDeliveryItemFieldReal(@Param("items") List<VRSDeliveryItemReal> itemsToUpdate);

    /**
     * 批量新增
     *
     * @param itemsToInsert
     * @return 结果
     */
    @InsertProvider(type = SqlProvider.class, method = "batchInsertVRSDeliveryItemFieldThird")
    void batchInsertVRSDeliveryItemFieldThird(@Param("items") List<VRSDeliveryItemField> itemsToInsert);

    /**
     * 批量新增
     *
     * @param itemsToUpdate
     * @return 结果
     */
    @InsertProvider(type = SqlProvider.class, method = "batchUpdateVRSDeliveryItemFieldThird")
    void batchUpdateVRSDeliveryItemFieldThird(@Param("items") List<VRSDeliveryItemField> itemsToUpdate);

    /**
     * 批量删除
     *
     * @param vrsDnnums,vrsDnLines
     * @return 结果
     */
    @DeleteProvider(type = SqlProvider.class, method = "batchDeleteByvrsDnnumAndvrsDnLine")
    void batchDeleteByvrsDnnumAndvrsDnLine(@Param("dnNums") List<String> vrsDnnums, @Param("dnLines") List<String> vrsDnLines);

    class SqlProvider {

        public String batchDeleteByvrsDnnumAndvrsDnLineAndDate(@Param("yesterday") java.sql.Date yesterday, @Param("vrsDnnums") List<String> vrsDnnums, @Param("vrsDnLines") List<String> vrsDnLines) {
            StringBuilder sql = new StringBuilder();
            sql.append("DELETE FROM lm_sale_item_Real WHERE lm_loadTime >= #{yesterday} and lm_order_id is null and lm_dnnum IN (");
            for (int i = 0; i < vrsDnnums.size(); i++) {
                sql.append("#{vrsDnnums[").append(i).append("]}");
                if (i < vrsDnnums.size() - 1) {
                    sql.append(",");
                }
            }
            sql.append(") AND lm_dnline IN (");
            for (int i = 0; i < vrsDnLines.size(); i++) {
                sql.append("#{vrsDnLines[").append(i).append("]}");
                if (i < vrsDnLines.size() - 1) {
                    sql.append(",");
                }
            }
            sql.append(");");
            return sql.toString();
        }

        public String batchDeleteByvrsDnnumAndvrsDnLineReal(@Param("vrsDnnums") List<String> vrsDnnums, @Param("vrsDnLines") List<String> vrsDnLines) {
            StringBuilder sql = new StringBuilder();
            sql.append("DELETE FROM lm_sale_item_Real WHERE lm_order_id is null and lm_dnnum IN (");
            for (int i = 0; i < vrsDnnums.size(); i++) {
                sql.append("#{vrsDnnums[").append(i).append("]}");
                if (i < vrsDnnums.size() - 1) {
                    sql.append(",");
                }
            }
            sql.append(") AND lm_dnline IN (");
            for (int i = 0; i < vrsDnLines.size(); i++) {
                sql.append("#{vrsDnLines[").append(i).append("]}");
                if (i < vrsDnLines.size() - 1) {
                    sql.append(",");
                }
            }
            sql.append(");");
            return sql.toString();
        }

        public String batchInsertVRSDeliveryItemFieldReal(@Param("items") List<VRSDeliveryItemReal> itemsToInsert) {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO lm_sale_item_real (lm_Id, lm_dnnum, lm_actualdeliverydate, lm_loadTime, lm_dnline, lm_order_id, lm_car_number, lm_packagingid, lm_grossweight, lm_insert_time) VALUES ");
            for (int i = 0; i < itemsToInsert.size(); i++) {
                sql.append("(")
                        .append("#{items[").append(i).append("].vrsId}, ")
                        .append("#{items[").append(i).append("].vrsDnnum}, ")
                        .append("#{items[").append(i).append("].vrsActualdeliverydate}, ")
                        .append("#{items[").append(i).append("].lmLoadTime}, ")
                        .append("#{items[").append(i).append("].vrsDnLine}, ")
                        .append("#{items[").append(i).append("].vrsOrderid}, ")
                        .append("#{items[").append(i).append("].vrsCarNumber}, ")
                        .append("#{items[").append(i).append("].vrsPackagingid}, ")
                        .append("#{items[").append(i).append("].vrsGrossweight}, ")
                        .append("#{items[").append(i).append("].vrsInsertTime})");
                if (i < itemsToInsert.size() - 1) {
                    sql.append(",");
                }
            }
            return sql.toString();
        }

        public String batchUpdateVRSDeliveryItemFieldReal(@Param("items") List<VRSDeliveryItemReal> itemsToUpdate) {
            StringBuilder sql = new StringBuilder("UPDATE lm_sale_item_real SET ");
            sql.append("lm_actualdeliverydate = CASE ");

            for (int i = 0; i < itemsToUpdate.size(); i++) {
                VRSDeliveryItemReal item = itemsToUpdate.get(i);
                sql.append("WHEN lm_dnnum = #{items[").append(i).append("].vrsDnnum} AND lm_dnline = #{items[").append(i).append("].vrsDnLine} THEN #{items[").append(i).append("].vrsActualdeliverydate} ");
            }

            sql.append("END, ");
            sql.append("lm_loadTime = CASE ");

            for (int i = 0; i < itemsToUpdate.size(); i++) {
                VRSDeliveryItemReal item = itemsToUpdate.get(i);
                sql.append("WHEN lm_dnnum = #{items[").append(i).append("].vrsDnnum} AND lm_dnline = #{items[").append(i).append("].vrsDnLine} THEN #{items[").append(i).append("].lmLoadTime} ");
            }

            sql.append("END, ");
            sql.append("lm_order_id = CASE ");

            for (int i = 0; i < itemsToUpdate.size(); i++) {
                VRSDeliveryItemReal item = itemsToUpdate.get(i);
                sql.append("WHEN lm_dnnum = #{items[").append(i).append("].vrsDnnum} AND lm_dnline = #{items[").append(i).append("].vrsDnLine} THEN COALESCE(lm_order_id, #{items[").append(i).append("].vrsOrderid}) ");
//                sql.append("WHEN lm_dnnum = #{items[").append(i).append("].vrsDnnum} AND lm_dnline = #{items[").append(i).append("].vrsDnLine} THEN #{items[").append(i).append("].vrsOrderid} ");
            }

            sql.append("END, ");
            sql.append("lm_car_number = CASE ");

            for (int i = 0; i < itemsToUpdate.size(); i++) {
                VRSDeliveryItemReal item = itemsToUpdate.get(i);
                sql.append("WHEN lm_dnnum = #{items[").append(i).append("].vrsDnnum} AND lm_dnline = #{items[").append(i).append("].vrsDnLine} THEN #{items[").append(i).append("].vrsCarNumber} ");
            }

            sql.append("END, ");
            sql.append("lm_packagingid = CASE ");

            for (int i = 0; i < itemsToUpdate.size(); i++) {
                VRSDeliveryItemReal item = itemsToUpdate.get(i);
                sql.append("WHEN lm_dnnum = #{items[").append(i).append("].vrsDnnum} AND lm_dnline = #{items[").append(i).append("].vrsDnLine} THEN #{items[").append(i).append("].vrsPackagingid} ");
            }

            sql.append("END, ");
            sql.append("lm_grossweight = CASE ");

            for (int i = 0; i < itemsToUpdate.size(); i++) {
                VRSDeliveryItemReal item = itemsToUpdate.get(i);
                sql.append("WHEN lm_dnnum = #{items[").append(i).append("].vrsDnnum} AND lm_dnline = #{items[").append(i).append("].vrsDnLine} THEN #{items[").append(i).append("].vrsGrossweight} ");
            }

            sql.append("END, ");
            sql.append("lm_update_time = CURRENT_TIMESTAMP ");
            sql.append("WHERE (lm_dnnum, lm_dnline) IN (");

            for (int i = 0; i < itemsToUpdate.size(); i++) {
                VRSDeliveryItemReal item = itemsToUpdate.get(i);
                sql.append("(#{items[").append(i).append("].vrsDnnum}, #{items[").append(i).append("].vrsDnLine})");
                if (i < itemsToUpdate.size() - 1) {
                    sql.append(", ");
                }
            }

            sql.append(")");
            return sql.toString();
        }

        public String batchInsertVRSDeliveryItemFieldThird(@Param("items") List<VRSDeliveryItemField> itemsToInsert) {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO lm_sale_item_insert (lm_dnnum, lm_actualdeliverydate, lm_loadTime, lm_dnline, lm_dnlineref, lm_batch, lm_grossweight, lm_weightunit, lm_packagingid, lm_car_number, lm_insert_time) VALUES ");
            for (int i = 0; i < itemsToInsert.size(); i++) {
                sql.append("(")
                        .append("#{items[").append(i).append("].vrsDnnum}, ")
                        .append("#{items[").append(i).append("].vrsActualdeliverydate}, ")
                        .append("#{items[").append(i).append("].lmLoadTime}, ")
                        .append("#{items[").append(i).append("].vrsDnLine}, ")
                        .append("#{items[").append(i).append("].vrsDnLineref}, ")
                        .append("#{items[").append(i).append("].lmBatch}, ")
                        .append("#{items[").append(i).append("].vrsGrossweight}, ")
                        .append("#{items[").append(i).append("].lmWeightunit}, ")
                        .append("#{items[").append(i).append("].vrsPackagingid}, ")
                        .append("#{items[").append(i).append("].vrsCarNumber}, ")
                        .append("#{items[").append(i).append("].vrsInsertTime})");
                if (i < itemsToInsert.size() - 1) {
                    sql.append(",");
                }
            }
            return sql.toString();
        }

        public String batchUpdateVRSDeliveryItemFieldThird(@Param("items") List<VRSDeliveryItemField> itemsToUpdate) {
            StringBuilder sql = new StringBuilder("UPDATE lm_sale_item_insert SET ");
            sql.append("lm_actualdeliverydate = CASE ");

            for (int i = 0; i < itemsToUpdate.size(); i++) {
                VRSDeliveryItemField item = itemsToUpdate.get(i);
                sql.append("WHEN lm_dnnum = #{items[").append(i).append("].vrsDnnum} AND lm_dnline = #{items[").append(i).append("].vrsDnLine} THEN #{items[").append(i).append("].vrsActualdeliverydate} ");
            }

            sql.append("END, ");
            sql.append("lm_loadTime = CASE ");

            for (int i = 0; i < itemsToUpdate.size(); i++) {
                VRSDeliveryItemField item = itemsToUpdate.get(i);
                sql.append("WHEN lm_dnnum = #{items[").append(i).append("].vrsDnnum} AND lm_dnline = #{items[").append(i).append("].vrsDnLine} THEN #{items[").append(i).append("].lmLoadTime} ");
            }

            sql.append("END, ");
            sql.append("lm_dnlineref = CASE ");

            for (int i = 0; i < itemsToUpdate.size(); i++) {
                VRSDeliveryItemField item = itemsToUpdate.get(i);
                sql.append("WHEN lm_dnnum = #{items[").append(i).append("].vrsDnnum} AND lm_dnline = #{items[").append(i).append("].vrsDnLine} THEN #{items[").append(i).append("].vrsDnLineref} ");
            }

            sql.append("END, ");
            sql.append("lm_batch = CASE ");

            for (int i = 0; i < itemsToUpdate.size(); i++) {
                VRSDeliveryItemField item = itemsToUpdate.get(i);
                sql.append("WHEN lm_dnnum = #{items[").append(i).append("].vrsDnnum} AND lm_dnline = #{items[").append(i).append("].vrsDnLine} THEN #{items[").append(i).append("].lmBatch} ");
            }

            sql.append("END, ");
            sql.append("lm_grossweight = CASE ");

            for (int i = 0; i < itemsToUpdate.size(); i++) {
                VRSDeliveryItemField item = itemsToUpdate.get(i);
                sql.append("WHEN lm_dnnum = #{items[").append(i).append("].vrsDnnum} AND lm_dnline = #{items[").append(i).append("].vrsDnLine} THEN #{items[").append(i).append("].vrsGrossweight} ");
            }

            sql.append("END, ");
            sql.append("lm_weightunit = CASE ");

            for (int i = 0; i < itemsToUpdate.size(); i++) {
                VRSDeliveryItemField item = itemsToUpdate.get(i);
                sql.append("WHEN lm_dnnum = #{items[").append(i).append("].vrsDnnum} AND lm_dnline = #{items[").append(i).append("].vrsDnLine} THEN #{items[").append(i).append("].lmWeightunit} ");
            }

            sql.append("END, ");
            sql.append("lm_packagingid = CASE ");

            for (int i = 0; i < itemsToUpdate.size(); i++) {
                VRSDeliveryItemField item = itemsToUpdate.get(i);
                sql.append("WHEN lm_dnnum = #{items[").append(i).append("].vrsDnnum} AND lm_dnline = #{items[").append(i).append("].vrsDnLine} THEN #{items[").append(i).append("].vrsPackagingid} ");
            }

            sql.append("END, ");
            sql.append("lm_car_number = CASE ");

            for (int i = 0; i < itemsToUpdate.size(); i++) {
                VRSDeliveryItemField item = itemsToUpdate.get(i);
                sql.append("WHEN lm_dnnum = #{items[").append(i).append("].vrsDnnum} AND lm_dnline = #{items[").append(i).append("].vrsDnLine} THEN #{items[").append(i).append("].vrsCarNumber} ");
            }

            sql.append("END, ");
            sql.append("lm_update_time = CURRENT_TIMESTAMP ");
            sql.append("WHERE (lm_dnnum,lm_dnline) IN (");

            for (int i = 0; i < itemsToUpdate.size(); i++) {
                VRSDeliveryItemField item = itemsToUpdate.get(i);
                sql.append("(#{items[").append(i).append("].vrsDnnum}, #{items[").append(i).append("].vrsDnLine})");
                if (i < itemsToUpdate.size() - 1) {
                    sql.append(", ");
                }
            }

            sql.append(")");
            return sql.toString();
        }

        public String batchDeleteByvrsDnnumAndvrsDnLine(@Param("dnNums") List<String> vrsDnnums, @Param("dnLines") List<String> vrsDnLines) {
            StringBuilder sql = new StringBuilder();
            sql.append("DELETE FROM lm_sale_item_insert WHERE lm_dnnum IN (");
            for (int i = 0; i < vrsDnnums.size(); i++) {
                sql.append("#{dnNums[").append(i).append("]}");
                if (i < vrsDnnums.size() - 1) {
                    sql.append(",");
                }
            }
            sql.append(") AND lm_dnline IN (");
            for (int i = 0; i < vrsDnLines.size(); i++) {
                sql.append("#{dnLines[").append(i).append("]}");
                if (i < vrsDnLines.size() - 1) {
                    sql.append(",");
                }
            }
            sql.append(");");
            return sql.toString();
        }
    }

    /**
     * 查询是否存在供应商ID
     *
     * @param ID
     * @return 结果
     */
    @Select("SELECT EXISTS (SELECT 1 FROM lm_supplier_local WHERE ID = #{ID})")
    boolean checkIDExists(@Param("ID") String ID);

    /**
     * 查询是否存在名称和时间
     *
     * @param name,date
     * @return 结果
     */
    @Select("SELECT EXISTS (SELECT 1 FROM lm_scrap_advance WHERE oa_name = #{name} and oa_date_time = #{date} )")
    boolean checkNameAndDateExists(@Param("name") String name,@Param("date") Date date);

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
    @Select("SELECT EXISTS (SELECT 1 FROM lm_scrap_advance WHERE oa_name = #{name} and oa_date_time = #{date} )")
    boolean checkScrapAdvanceInfoExists(@Param("name") String name,@Param("date") String date);

    /**
     * 打印磅单请求查询DN与IDcard
     *
     * @param orderId 业务ID
     * @param businessType 业务类型
     * @return 结果
     */
    ResultData getPrintWeightTicketInfo(@Param("orderId") String orderId, @Param("businessType") Integer businessType);

}
