package com.lemei.system.service.Impl;

import com.lemei.common.core.domain.ResultData;
import com.lemei.common.core.domain.model.LoginUser;
import com.lemei.common.utils.SecurityUtils;
import com.lemei.system.domain.LmCarApplication;
import com.lemei.system.domain.param.CarApplicationQueryParam;
import com.lemei.system.domain.param.CarDeliveryItemReal;
import com.lemei.system.domain.param.CarSaleQueryParam;
import com.lemei.system.mapper.LmCarApplicationMapper;
import com.lemei.system.service.ILmCarApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 车辆预约Service业务层处理
 *
 * @author yuqian
 * @date 2022-08-24
 */
@Service
public class LmCarApplicationServiceImpl implements ILmCarApplicationService
{
    @Autowired
    private LmCarApplicationMapper lmCarApplicationMapper;


    /**
     * 查询车辆预约
     *
     * @param applicationId 车辆预约主键
     * @return 车辆预约
     */
    @Override
    public LmCarApplication selectLmCarApplicationByApplicationId(String applicationId)
    {
        return lmCarApplicationMapper.selectLmCarApplicationByApplicationId(applicationId);
    }

    /**
     * 查询车辆预约列表
     *
     * @param lmCarApplication 车辆预约
     * @return 车辆预约
     */
    @Override
    public List<LmCarApplication> selectLmCarApplicationList(CarApplicationQueryParam lmCarApplication)
    {
        List<LmCarApplication> list;
        LoginUser loginUser = SecurityUtils.getLoginUser();
        list = lmCarApplicationMapper.selectLmCarApplicationList(lmCarApplication);
        if (list.size()>0){
            for (LmCarApplication lm:list){
                //当前用户是管理员或者是审核人员.可以审核
                if (loginUser.getUser().isAdmin()||loginUser.getUserId().equals(lm.getCheckerId())){
                    lm.setCheck(true);
                }else{
                    lm.setCheck(false);
                }
                if (loginUser.getUserId().equals(lm.getCreatedBy())){
                    lm.setSelf(true);
                }else{
                    lm.setSelf(false);
                }
            }
        }
        return list;
    }

    /**
     * 新增车辆预约
     *
     * @param lmCarApplication 车辆预约
     * @return 结果
     */
    @Override
    public int insertLmCarApplication(LmCarApplication lmCarApplication)
    {
        return lmCarApplicationMapper.insertLmCarApplication(lmCarApplication);
    }

    /**
     * 修改车辆预约
     *
     * @param lmCarApplication 车辆预约
     * @return 结果
     */
    @Override
    public int updateLmCarApplication(LmCarApplication lmCarApplication)
    {
        return lmCarApplicationMapper.updateLmCarApplication(lmCarApplication);
    }

    /**
     * 修改状态
     *
     * @param lmCarApplication 车辆预约
     * @return 结果
     */
    @Override
    public int updateStatus(LmCarApplication lmCarApplication)
    {
        return lmCarApplicationMapper.updateStatus(lmCarApplication);
    }

    /**
     * 修改扣重
     *
     * @param lmCarApplication 车辆预约
     * @return 结果
     */
    @Override
    public int updateLoseWeight(LmCarApplication lmCarApplication)
    {
        return lmCarApplicationMapper.updateLoseWeight(lmCarApplication);
    }

    /**
     * 修改销售预约重量
     *
     * @param lmCarApplication 车辆预约
     * @return 结果
     */
    @Override
    public int updateSaleWeight(LmCarApplication lmCarApplication)
    {
        return lmCarApplicationMapper.updateSaleWeight(lmCarApplication);
    }

    /**
     * 修改废料预约重量
     *
     * @param lmCarApplication 车辆预约
     * @return 结果
     */
    @Override
    public int updateScrapWeight(LmCarApplication lmCarApplication)
    {
        return lmCarApplicationMapper.updateScrapWeight(lmCarApplication);
    }

    /**
     * 修改出厂带栈板重量
     *
     * @param lmCarApplication 车辆预约
     * @return 结果
     */
    @Override
    public int updateShippedWithPalletWeight(LmCarApplication lmCarApplication)
    {
        return lmCarApplicationMapper.updateShippedWithPalletWeight(lmCarApplication);
    }

    /**
     * 取消一次磅时修改车辆预约信息
     *
     * @param lmCarApplication 车辆预约主键
     * @return 结果
     */
    @Override
    public int updateOneLmCarApplicationByApplicationId(LmCarApplication lmCarApplication) {
        return lmCarApplicationMapper.updateOneLmCarApplicationByApplicationId(lmCarApplication);
    }

    /**
     * 取消二次磅时修改车辆预约信息
     *
     * @param lmCarApplication 车辆预约主键
     * @return 结果
     */
    @Override
    public int updateTwoLmCarApplicationByApplicationId(LmCarApplication lmCarApplication) {
        return lmCarApplicationMapper.updateTwoLmCarApplicationByApplicationId(lmCarApplication);
    }

    /**
     * 作废时修改车辆预约信息
     *
     * @param lmCarApplication 车辆预约主键
     * @return 结果
     */
    @Override
    public int updateAllLmCarApplicationByApplicationId(LmCarApplication lmCarApplication) {
        return lmCarApplicationMapper.updateAllLmCarApplicationByApplicationId(lmCarApplication);
    }

    /**
     * 首页数据统计
     * @return
     */
    @Override
    public Object indexStatis()
    {
        //车辆预约总数
        int total= lmCarApplicationMapper.selectApplicationCount();
        int today= lmCarApplicationMapper.selectTodayApplicationCount();
        ResultData r = new ResultData();
        r.put("total",total);
        r.put("today",today);
        //今日预约数
        return r;
    }

    /**
     * 批量删除车辆预约
     *
     * @param applicationIds 需要删除的车辆预约主键
     * @return 结果
     */
    @Override
    public int deleteLmCarApplicationByApplicationIds(String[] applicationIds)
    {
        return lmCarApplicationMapper.deleteLmCarApplicationByApplicationIds(applicationIds);
    }

    /**
     * 删除车辆预约信息
     *
     * @param applicationId 车辆预约主键
     * @return 结果
     */
    @Override
    public int deleteLmCarApplicationByApplicationId(String applicationId)
    {
        return lmCarApplicationMapper.deleteLmCarApplicationByApplicationId(applicationId);
    }

    /**
     * 查询流水号最大值
     *
     * @param
     * @return 车辆预约
     */
    @Override
    public Long selectMaxSerialNumber() {
        return lmCarApplicationMapper.selectMaxSerialNumber();
    }

    /**
     * 通过车牌查询预约状态为非已完成的数目
     *
     * @param
     * @return 车辆预约
     */
    @Override
    public Integer selectNonFinishedByCarNumber(LmCarApplication lmCarApplication) {
        return lmCarApplicationMapper.selectNonFinishedByCarNumber(lmCarApplication);
    }

    /**
     * 查询车辆预约指定列表
     *
     * @param
     * @return 车辆预约
     */
    @Override
    public List<LmCarApplication> selectLmCarApplicationListByIds(String[] ids) {
        return lmCarApplicationMapper.selectLmCarApplicationListByIds(ids);
    }

    /**
     * 通过车牌号查询进厂状态
     *
     * @param
     * @return 进厂状态集合
     */
    @Override
    public List<Integer> selectEnterStatusListByCarNumber(String carNumber) {
        return lmCarApplicationMapper.selectEnterStatusListByCarNumber(carNumber);
    }

    /**
     * 查询销售列表
     *
     * @param saleQueryParam
     * @return 销售集合
     */
    @Override
    public List<CarDeliveryItemReal> selectSaleList(CarSaleQueryParam saleQueryParam) {
        return lmCarApplicationMapper.selectSaleList(saleQueryParam);
    }

    /**
     * 查询销售详细
     *
     * @param lmId 销售主键
     */
    @Override
    public CarDeliveryItemReal selectSaleInfoByLmId(String lmId) {
        return lmCarApplicationMapper.selectSaleInfoByLmId(lmId);
    }

    /**
     * 修改销售详细
     *
     * @param lmDeliveryItemReal
     * @return 结果
     */
    @Override
    public int updateSaleInfo(CarDeliveryItemReal lmDeliveryItemReal) {
        return lmCarApplicationMapper.updateSaleInfo(lmDeliveryItemReal);
    }

    /**
     * 删除销售详细
     *
     * @param lmIds
     * @return 结果
     */
    @Override
    public int deleteSaleInfoByLmIds(String[] lmIds) {
        List<Map<String, String>> dnNumLinePairs = lmCarApplicationMapper.selectDnNumLineByLmIds(lmIds);
        lmCarApplicationMapper.deleteByDnNumLinePairsInsert(dnNumLinePairs);
        return lmCarApplicationMapper.deleteByDnNumLinePairsReal(dnNumLinePairs);

    }

    /**
     * 通过预约号码查询审计表中是否存在该预约号码
     *
     * @param orderId
     * @return 结果
     */
    @Override
    public boolean checkExitOrderIdByOrderId(String orderId) {
        return lmCarApplicationMapper.checkExitOrderIdByOrderId(orderId);
    }

    /**
     * 向审计表中插入数据
     *
     * @param lmCarApplication
     * @return 结果
     */
    @Override
    public int insertAuditLine(LmCarApplication lmCarApplication) {
        return lmCarApplicationMapper.insertAuditLine(lmCarApplication);
    }

    /**
     * 通过预约号码更新审计表
     *
     * @param lmCarApplication
     * @return 结果
     */
    @Override
    public int updateAuditLineByOrderId(LmCarApplication lmCarApplication) {
        return lmCarApplicationMapper.updateAuditLineByOrderId(lmCarApplication);
    }

    /**
     * 查询是否存在名称和时间
     *
     * @param name,date
     * @return 结果
     */
    @Override
    public boolean checkScrapAdvanceInfoExists(String name, String date) {
        return lmCarApplicationMapper.checkScrapAdvanceInfoExists(name,date);
    }

    /**
     * 通过applicationId查询车牌号
     *
     * @param applicationId
     * @return 结果
     */
    @Override
    public String selectCarNumberByApplicationId(String applicationId) {
        return lmCarApplicationMapper.selectCarNumberByApplicationId(applicationId);
    }

    /**
     * 通过车牌号查询是否存在已进厂记录
     *
     * @param carNumber
     * @return 结果
     */
    @Override
    public boolean checkEnterStatusInfoExists(String carNumber) {
        return lmCarApplicationMapper.checkEnterStatusInfoExists(carNumber);
    }
}
