package com.vrs.system.service.Impl;

import com.vrs.common.core.domain.ResultData;
import com.vrs.common.core.domain.model.LoginUser;
import com.vrs.common.utils.SecurityUtils;
import com.vrs.system.domain.VRSCarApplication;
import com.vrs.system.domain.param.CarApplicationQueryParam;
import com.vrs.system.domain.param.CarDeliveryItemReal;
import com.vrs.system.domain.param.CarSaleQueryParam;
import com.vrs.system.mapper.VRSCarApplicationMapper;
import com.vrs.system.service.IVRSCarApplicationService;
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
public class VRSCarApplicationServiceImpl implements IVRSCarApplicationService
{
    @Autowired
    private VRSCarApplicationMapper VRSCarApplicationMapper;


    /**
     * 查询车辆预约
     *
     * @param applicationId 车辆预约主键
     * @return 车辆预约
     */
    @Override
    public VRSCarApplication selectVRSCarApplicationByApplicationId(String applicationId)
    {
        return VRSCarApplicationMapper.selectVRSCarApplicationByApplicationId(applicationId);
    }

    /**
     * 查询车辆预约列表
     *
     * @param VRSCarApplication 车辆预约
     * @return 车辆预约
     */
    @Override
    public List<VRSCarApplication> selectVRSCarApplicationList(CarApplicationQueryParam VRSCarApplication)
    {
        List<VRSCarApplication> list;
        LoginUser loginUser = SecurityUtils.getLoginUser();
        list = VRSCarApplicationMapper.selectVRSCarApplicationList(VRSCarApplication);
        if (list.size()>0){
            for (VRSCarApplication lm:list){
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
     * @param VRSCarApplication 车辆预约
     * @return 结果
     */
    @Override
    public int insertVRSCarApplication(VRSCarApplication VRSCarApplication)
    {
        return VRSCarApplicationMapper.insertVRSCarApplication(VRSCarApplication);
    }

    /**
     * 修改车辆预约
     *
     * @param VRSCarApplication 车辆预约
     * @return 结果
     */
    @Override
    public int updateVRSCarApplication(VRSCarApplication VRSCarApplication)
    {
        return VRSCarApplicationMapper.updateVRSCarApplication(VRSCarApplication);
    }

    /**
     * 修改状态
     *
     * @param VRSCarApplication 车辆预约
     * @return 结果
     */
    @Override
    public int updateStatus(VRSCarApplication VRSCarApplication)
    {
        return VRSCarApplicationMapper.updateStatus(VRSCarApplication);
    }

    /**
     * 修改扣重
     *
     * @param VRSCarApplication 车辆预约
     * @return 结果
     */
    @Override
    public int updateLoseWeight(VRSCarApplication VRSCarApplication)
    {
        return VRSCarApplicationMapper.updateLoseWeight(VRSCarApplication);
    }

    /**
     * 修改销售预约重量
     *
     * @param VRSCarApplication 车辆预约
     * @return 结果
     */
    @Override
    public int updateSaleWeight(VRSCarApplication VRSCarApplication)
    {
        return VRSCarApplicationMapper.updateSaleWeight(VRSCarApplication);
    }

    /**
     * 修改废料预约重量
     *
     * @param VRSCarApplication 车辆预约
     * @return 结果
     */
    @Override
    public int updateScrapWeight(VRSCarApplication VRSCarApplication)
    {
        return VRSCarApplicationMapper.updateScrapWeight(VRSCarApplication);
    }

    /**
     * 修改出厂带栈板重量
     *
     * @param VRSCarApplication 车辆预约
     * @return 结果
     */
    @Override
    public int updateShippedWithPalletWeight(VRSCarApplication VRSCarApplication)
    {
        return VRSCarApplicationMapper.updateShippedWithPalletWeight(VRSCarApplication);
    }

    /**
     * 取消一次磅时修改车辆预约信息
     *
     * @param VRSCarApplication 车辆预约主键
     * @return 结果
     */
    @Override
    public int updateOneVRSCarApplicationByApplicationId(VRSCarApplication VRSCarApplication) {
        return VRSCarApplicationMapper.updateOneVRSCarApplicationByApplicationId(VRSCarApplication);
    }

    /**
     * 取消二次磅时修改车辆预约信息
     *
     * @param VRSCarApplication 车辆预约主键
     * @return 结果
     */
    @Override
    public int updateTwoVRSCarApplicationByApplicationId(VRSCarApplication VRSCarApplication) {
        return VRSCarApplicationMapper.updateTwoVRSCarApplicationByApplicationId(VRSCarApplication);
    }

    /**
     * 作废时修改车辆预约信息
     *
     * @param VRSCarApplication 车辆预约主键
     * @return 结果
     */
    @Override
    public int updateAllVRSCarApplicationByApplicationId(VRSCarApplication VRSCarApplication) {
        return VRSCarApplicationMapper.updateAllVRSCarApplicationByApplicationId(VRSCarApplication);
    }

    /**
     * 首页数据统计
     * @return
     */
    @Override
    public Object indexStatis()
    {
        //车辆预约总数
        int total= VRSCarApplicationMapper.selectApplicationCount();
        int today= VRSCarApplicationMapper.selectTodayApplicationCount();
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
    public int deleteVRSCarApplicationByApplicationIds(String[] applicationIds)
    {
        return VRSCarApplicationMapper.deleteVRSCarApplicationByApplicationIds(applicationIds);
    }

    /**
     * 删除车辆预约信息
     *
     * @param applicationId 车辆预约主键
     * @return 结果
     */
    @Override
    public int deleteVRSCarApplicationByApplicationId(String applicationId)
    {
        return VRSCarApplicationMapper.deleteVRSCarApplicationByApplicationId(applicationId);
    }

    /**
     * 查询流水号最大值
     *
     * @param
     * @return 车辆预约
     */
    @Override
    public Long selectMaxSerialNumber() {
        return VRSCarApplicationMapper.selectMaxSerialNumber();
    }

    /**
     * 通过车牌查询预约状态为非已完成的数目
     *
     * @param
     * @return 车辆预约
     */
    @Override
    public Integer selectNonFinishedByCarNumber(VRSCarApplication VRSCarApplication) {
        return VRSCarApplicationMapper.selectNonFinishedByCarNumber(VRSCarApplication);
    }

    /**
     * 查询车辆预约指定列表
     *
     * @param
     * @return 车辆预约
     */
    @Override
    public List<VRSCarApplication> selectVRSCarApplicationListByIds(String[] ids) {
        return VRSCarApplicationMapper.selectVRSCarApplicationListByIds(ids);
    }

    /**
     * 通过车牌号查询进厂状态
     *
     * @param
     * @return 进厂状态集合
     */
    @Override
    public List<Integer> selectEnterStatusListByCarNumber(String carNumber) {
        return VRSCarApplicationMapper.selectEnterStatusListByCarNumber(carNumber);
    }

    /**
     * 查询销售列表
     *
     * @param saleQueryParam
     * @return 销售集合
     */
    @Override
    public List<CarDeliveryItemReal> selectSaleList(CarSaleQueryParam saleQueryParam) {
        return VRSCarApplicationMapper.selectSaleList(saleQueryParam);
    }

    /**
     * 查询销售详细
     *
     * @param vrsId 销售主键
     */
    @Override
    public CarDeliveryItemReal selectSaleInfoByvrsId(String vrsId) {
        return VRSCarApplicationMapper.selectSaleInfoByvrsId(vrsId);
    }

    /**
     * 修改销售详细
     *
     * @param VRSDeliveryItemReal
     * @return 结果
     */
    @Override
    public int updateSaleInfo(CarDeliveryItemReal VRSDeliveryItemReal) {
        return VRSCarApplicationMapper.updateSaleInfo(VRSDeliveryItemReal);
    }

    /**
     * 删除销售详细
     *
     * @param vrsIds
     * @return 结果
     */
    @Override
    public int deleteSaleInfoByvrsIds(String[] vrsIds) {
        List<Map<String, String>> dnNumLinePairs = VRSCarApplicationMapper.selectDnNumLineByvrsIds(vrsIds);
        VRSCarApplicationMapper.deleteByDnNumLinePairsInsert(dnNumLinePairs);
        return VRSCarApplicationMapper.deleteByDnNumLinePairsReal(dnNumLinePairs);

    }

    /**
     * 通过预约号码查询审计表中是否存在该预约号码
     *
     * @param orderId
     * @return 结果
     */
    @Override
    public boolean checkExitOrderIdByOrderId(String orderId) {
        return VRSCarApplicationMapper.checkExitOrderIdByOrderId(orderId);
    }

    /**
     * 向审计表中插入数据
     *
     * @param VRSCarApplication
     * @return 结果
     */
    @Override
    public int insertAuditLine(VRSCarApplication VRSCarApplication) {
        return VRSCarApplicationMapper.insertAuditLine(VRSCarApplication);
    }

    /**
     * 通过预约号码更新审计表
     *
     * @param VRSCarApplication
     * @return 结果
     */
    @Override
    public int updateAuditLineByOrderId(VRSCarApplication VRSCarApplication) {
        return VRSCarApplicationMapper.updateAuditLineByOrderId(VRSCarApplication);
    }

    /**
     * 查询是否存在名称和时间
     *
     * @param name,date
     * @return 结果
     */
    @Override
    public boolean checkScrapAdvanceInfoExists(String name, String date) {
        return VRSCarApplicationMapper.checkScrapAdvanceInfoExists(name,date);
    }

    /**
     * 通过applicationId查询车牌号
     *
     * @param applicationId
     * @return 结果
     */
    @Override
    public String selectCarNumberByApplicationId(String applicationId) {
        return VRSCarApplicationMapper.selectCarNumberByApplicationId(applicationId);
    }

    /**
     * 通过车牌号查询是否存在已进厂记录
     *
     * @param carNumber
     * @return 结果
     */
    @Override
    public boolean checkEnterStatusInfoExists(String carNumber) {
        return VRSCarApplicationMapper.checkEnterStatusInfoExists(carNumber);
    }
}
