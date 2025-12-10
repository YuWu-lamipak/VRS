package com.lemei.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lemei.common.annotation.Excel;
import com.lemei.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 车辆预约对象 lm_car_application
 *
 * @author zhangpeng
 * @date 2023-08-24
 */

public class LmCarApplication extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 申请id
     */
    private String applicationId;

    @Excel(name = "流水号")
    private Long serialNumber;

    @Excel(name = "出入厂凭证")
    private String mark;

    @Excel(name = "车牌号")
    public String carNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划入厂时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date applicationDate;

    @Excel(name = "承运商")
    private String carrierName;

    @Excel(name = "司机姓名")
    private String driverName;

    @Excel(name = "废料名称")
    private String scrap;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Excel(name = "二磅重量（净kg）")
    private BigDecimal netWeight;

    @Excel(name = "业务描述")
    private String businessDescription;

    @Excel(name = "备注")
    private String remark;

    @Excel(name = "业务类型", readConverterExp = "0=采购,1=销售,2=废料,3=其他,4=固废危废处置")
    private Integer businessType;

    @Excel(name = "启用管控", readConverterExp = "0=否,1=是")
    private Integer enableControl;

    @Excel(name = "原因")
    private String reason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "实际入厂时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date enterDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "实际出厂时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date outDate;

    @Excel(name = "工厂类型")
    private String factoryType;

    @Excel(name = "手机号")
    private String phone;

    @Excel(name = "创建人")
    private String createdName;

    @Excel(name = "受访人")
    private String checkerName;

    @Excel(name = "预约状态", readConverterExp = "0=待制卡,1=待上一磅,2=待确认一磅,3=待上二磅,4=待确认二磅,5=待退卡,6=已完成,7=取消预约")
    private Integer appointmentStatus;

    @Excel(name = "进厂状态", readConverterExp = "0=未进厂,1=已进厂,2=已出厂,3=取消进厂")
    private Integer enterStatus;

    @Excel(name = "审核状态", readConverterExp = "1=已申请,2=审核通过,3=审核不通过,4=取消申请")
    private Integer status;

    @Excel(name = "身份证号")
    private String idcard;

    @Excel(name = "送/提货预计重量")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private BigDecimal beforeWeight;

    @Excel(name = "提交方式", readConverterExp = "1=本人提交,2=代提交")
    private Integer postType;

    @Excel(name = "供应商编号")
    private String supplyCode;

    @Excel(name = "供应商名称")
    private String supplyName;

    @Excel(name = "业务主键")
    private String taskId;

    @Excel(name = "预约号码")
    public String orderId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Excel(name = "预约重量（kg）")
    private BigDecimal appointmentWeight;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Excel(name = "扣重（kg）")
    private BigDecimal loseWeight;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Excel(name = "出厂带栈板重量（kg）")
    private BigDecimal shippedWithPalletWeight;

    @Excel(name = "预约重量变更原因")
    private String floatReason;

    @Excel(name = "地磅卡号")
    private String cardId;

    @Excel(name = "卡类型", readConverterExp = "1=IC 卡号,2=电子标签")
    private Integer cardType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "制卡时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createCardTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "退卡时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date returnCardTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Excel(name = "一磅重量（kg）")
    private BigDecimal firstWeight;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "一磅过磅时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date firstWeightTime;

    @Excel(name = "一地磅编号")
    private String loadoMeterID1;

    @Excel(name = "一磅⽤户编号")
    private String userID1;

    @Excel(name = "一磅⽤户名")
    private String userName1;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Excel(name = "二磅重量（毛kg）")
    private BigDecimal secondWeight;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Excel(name = "二磅重量（皮kg）")
    private BigDecimal truckWeight;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Excel(name = "二磅重量（扣kg）")
    private BigDecimal packWeight;

    @Excel(name = "二磅重量（实kg）")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal goodsWeight;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Excel(name = "正容差（%）")
    private BigDecimal toleranceAdd;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Excel(name = "负容差（%）")
    private BigDecimal toleranceDec;

    @Excel(name = "上浮重量（kg）")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal upFloatingWeight;

    @Excel(name = "下浮重量（kg）")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal downFloatingWeight;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "二磅过磅时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date secondWeightTime;

    @Excel(name = "二地磅编号")
    private String loadoMeterID2;

    @Excel(name = "二磅用户编号")
    private String userID2;

    @Excel(name = "二磅用户名")
    private String userName2;

    @Excel(name = "是否更新", readConverterExp = "0=新增,1=更正，卡号不变，更改车号")
    private String isUpdate;

    private Long createdBy;

    private String applicationNo;

    private Long driverId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @Excel(name = "申请时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate;

    /**
     * 1删除0未删除
     */
    private Integer isdeleted;

    /**
     * 承运商id
     */
    private Long carrierId;

    private String reasonKey;

    private String scrapKey;

    /**
     * 审核人id
     */
    private Long checkerId;

    /**
     * 判断当前预约单是否本人申请，识别是否可修改当前信息
     */
    private Boolean isSelf;

    /**
     * 判断是否可以审核
     */
    private Boolean isCheck;

    /**
     * 客户编号
     */
    private String custCode;

    /**
     * 客户名称
     */
    private String custName;

    /**
     * 单据编号
     */
    private String documentCode;

    /**
     * 账套编号
     */
    private String dataAreaId;

    /**
     * 工厂代码
     */
    private String factoryCode;

    /**
     * 地磅消息
     */
    private String message;

    private String truckNo2;

    /**
     * ATMSID
     */
    private String atmsId;

    /**
     * x磅用户编号
     */
    private String userId;
    /**
     * x磅用户名
     */
    private String userName;
    /**
     * x地磅编号
     */
    private String loadoMeterID;
    /**
     * 车辆类型
     */
    private Integer truckType;
    /**
     * 请求标识
     */
    private Integer requestId;
    /**
     * 交货单号
     */
    private String wMSPickingRouteIDs;
    /**
     * 交货单号2
     */
    private String wMSPickingRouteIDs2;
    /**
     * 修改销售预约重量标记
     */
    private String editFlag;
    /**
     * 原重
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal originalWeight;
    /**
     * 修改后的预约重量
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal editWeight;
    /**
     * 修改人
     */
    private String editPerson;
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date editTime;
    /**
     * 修改原因
     */
    private String editReason;

    public LmCarApplication() {
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setOutDate(Date outDate) {
        this.outDate = outDate;
    }

    public Date getOutDate() {
        return outDate;
    }

    public void setEnterDate(Date enterDate) {
        this.enterDate = enterDate;
    }

    public Date getEnterDate() {
        return enterDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    public String getCreatedName() {
        return createdName;
    }

    public void setIsdeleted(Integer isdeleted) {
        this.isdeleted = isdeleted;
    }

    public Integer getIsdeleted() {
        return isdeleted;
    }

    public void setEnterStatus(Integer enterStatus) {
        this.enterStatus = enterStatus;
    }

    public Integer getEnterStatus() {
        return enterStatus;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setCarrierId(Long carrierId) {
        this.carrierId = carrierId;
    }

    public Long getCarrierId() {
        return carrierId;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setBeforeWeight(BigDecimal beforeWeight) {
        this.beforeWeight = beforeWeight;
    }

    public BigDecimal getBeforeWeight() {
        return beforeWeight;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setScrap(String scrap) {
        this.scrap = scrap;
    }
    public String getScrap() {
        return scrap;
    }

    public void setPostType(Integer postType) {
        this.postType = postType;
    }

    public Integer getPostType() {
        return postType;
    }

    public void setCheckerId(Long checkerId) {
        this.checkerId = checkerId;
    }

    public Long getCheckerId() {
        return checkerId;
    }

    public void setCheckerName(String checkerName) {
        this.checkerName = checkerName;
    }

    public String getCheckerName() {
        return checkerName;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public Boolean getSelf() {
        return isSelf;
    }

    public void setSelf(Boolean self) {
        isSelf = self;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Boolean getCheck() {
        return isCheck;
    }

    public void setCheck(Boolean check) {
        isCheck = check;
    }

    public String getReasonKey() {
        return reasonKey;
    }

    public void setReasonKey(String reasonKey) {
        this.reasonKey = reasonKey;
    }

    public String getScrapKey() {
        return scrapKey;
    }

    public void setScrapKey(String scrapKey) {
        this.scrapKey = scrapKey;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getBusinessDescription() {
        return businessDescription;
    }

    public void setBusinessDescription(String businessDescription) {
        this.businessDescription = businessDescription;
    }

    public String getSupplyCode() {
        return supplyCode;
    }

    public void setSupplyCode(String supplyCode) {
        this.supplyCode = supplyCode;
    }

    public String getSupplyName() {
        return supplyName;
    }

    public void setSupplyName(String supplyName) {
        this.supplyName = supplyName;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getDocumentCode() {
        return documentCode;
    }

    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }

    public String getDataAreaId() {
        return dataAreaId;
    }

    public void setDataAreaId(String dataAreaId) {
        this.dataAreaId = dataAreaId;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public BigDecimal getAppointmentWeight() {
        return appointmentWeight;
    }

    public void setAppointmentWeight(BigDecimal appointmentWeight) {
        this.appointmentWeight = appointmentWeight;
    }

    public BigDecimal getLoseWeight() {
        return loseWeight;
    }

    public void setLoseWeight(BigDecimal loseWeight) {
        this.loseWeight = loseWeight;
    }

    public BigDecimal getShippedWithPalletWeight() {
        return shippedWithPalletWeight;
    }

    public void setShippedWithPalletWeight(BigDecimal shippedWithPalletWeight) {
        this.shippedWithPalletWeight = shippedWithPalletWeight;
    }

    public String getFloatReason() {
        return floatReason;
    }

    public void setFloatReason(String floatReason) {
        this.floatReason = floatReason;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public Date getCreateCardTime() {
        return createCardTime;
    }

    public void setCreateCardTime(Date createCardTime) {
        this.createCardTime = createCardTime;
    }

    public Date getReturnCardTime() {
        return returnCardTime;
    }

    public void setReturnCardTime(Date returnCardTime) {
        this.returnCardTime = returnCardTime;
    }

    public Integer getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(Integer appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BigDecimal getFirstWeight() {
        return firstWeight;
    }

    public void setFirstWeight(BigDecimal firstWeight) {
        this.firstWeight = firstWeight;
    }

    public Date getFirstWeightTime() {
        return firstWeightTime;
    }

    public void setFirstWeightTime(Date firstWeightTime) {
        this.firstWeightTime = firstWeightTime;
    }

    public String getLoadoMeterID1() {
        return loadoMeterID1;
    }

    public void setLoadoMeterID1(String loadoMeterID1) {
        this.loadoMeterID1 = loadoMeterID1;
    }

    public String getUserID1() {
        return userID1;
    }

    public void setUserID1(String userID1) {
        this.userID1 = userID1;
    }

    public String getUserName1() {
        return userName1;
    }

    public void setUserName1(String userName1) {
        this.userName1 = userName1;
    }

    public BigDecimal getSecondWeight() {
        return secondWeight;
    }

    public void setSecondWeight(BigDecimal secondWeight) {
        this.secondWeight = secondWeight;
    }

    public BigDecimal getTruckWeight() {
        return truckWeight;
    }

    public void setTruckWeight(BigDecimal truckWeight) {
        this.truckWeight = truckWeight;
    }

    public BigDecimal getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(BigDecimal netWeight) {
        this.netWeight = netWeight;
    }

    public BigDecimal getPackWeight() {
        return packWeight;
    }

    public void setPackWeight(BigDecimal packWeight) {
        this.packWeight = packWeight;
    }

    public BigDecimal getGoodsWeight() {
        return goodsWeight;
    }

    public void setGoodsWeight(BigDecimal goodsWeight) {
        this.goodsWeight = goodsWeight;
    }

    public Integer getEnableControl() {
        return enableControl;
    }

    public void setEnableControl(Integer enableControl) {
        this.enableControl = enableControl;
    }

    public BigDecimal getToleranceAdd() {
        return toleranceAdd;
    }

    public void setToleranceAdd(BigDecimal toleranceAdd) {
        this.toleranceAdd = toleranceAdd;
    }

    public BigDecimal getToleranceDec() {
        return toleranceDec;
    }

    public void setToleranceDec(BigDecimal toleranceDec) {
        this.toleranceDec = toleranceDec;
    }

    public BigDecimal getUpFloatingWeight() {
        return upFloatingWeight;
    }

    public void setUpFloatingWeight(BigDecimal upFloatingWeight) {
        this.upFloatingWeight = upFloatingWeight;
    }

    public BigDecimal getDownFloatingWeight() {
        return downFloatingWeight;
    }

    public void setDownFloatingWeight(BigDecimal downFloatingWeight) {
        this.downFloatingWeight = downFloatingWeight;
    }

    public Date getSecondWeightTime() {
        return secondWeightTime;
    }

    public void setSecondWeightTime(Date secondWeightTime) {
        this.secondWeightTime = secondWeightTime;
    }

    public String getLoadoMeterID2() {
        return loadoMeterID2;
    }

    public void setLoadoMeterID2(String loadoMeterID2) {
        this.loadoMeterID2 = loadoMeterID2;
    }

    public String getUserID2() {
        return userID2;
    }

    public void setUserID2(String userID2) {
        this.userID2 = userID2;
    }

    public String getUserName2() {
        return userName2;
    }

    public void setUserName2(String userName2) {
        this.userName2 = userName2;
    }

    public String getTruckNo2() {
        return truckNo2;
    }

    public void setTruckNo2(String truckNo2) {
        this.truckNo2 = truckNo2;
    }

    public String getAtmsId() {
        return atmsId;
    }

    public void setAtmsId(String atmsId) {
        this.atmsId = atmsId;
    }

    public String getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(String isUpdate) {
        this.isUpdate = isUpdate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoadoMeterID() {
        return loadoMeterID;
    }

    public void setLoadoMeterID(String loadoMeterID) {
        this.loadoMeterID = loadoMeterID;
    }

    public Integer getTruckType() {
        return truckType;
    }

    public void setTruckType(Integer truckType) {
        this.truckType = truckType;
    }

    public Long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getFactoryType() {
        return factoryType;
    }

    public void setFactoryType(String factoryType) {
        this.factoryType = factoryType;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public String getwMSPickingRouteIDs() {
        return wMSPickingRouteIDs;
    }

    public void setwMSPickingRouteIDs(String wMSPickingRouteIDs) {
        this.wMSPickingRouteIDs = wMSPickingRouteIDs;
    }

    public String getwMSPickingRouteIDs2() {
        return wMSPickingRouteIDs2;
    }

    public void setwMSPickingRouteIDs2(String wMSPickingRouteIDs2) {
        this.wMSPickingRouteIDs2 = wMSPickingRouteIDs2;
    }

    public String getEditFlag() {
        return editFlag;
    }

    public void setEditFlag(String editFlag) {
        this.editFlag = editFlag;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public BigDecimal getOriginalWeight() {
        return originalWeight;
    }

    public void setOriginalWeight(BigDecimal originalWeight) {
        this.originalWeight = originalWeight;
    }

    public BigDecimal getEditWeight() {
        return editWeight;
    }

    public void setEditWeight(BigDecimal editWeight) {
        this.editWeight = editWeight;
    }

    public String getEditPerson() {
        return editPerson;
    }

    public void setEditPerson(String editPerson) {
        this.editPerson = editPerson;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public String getEditReason() {
        return editReason;
    }

    public void setEditReason(String editReason) {
        this.editReason = editReason;
    }

    @Override
    public String toString() {
        return "LmCarApplication{" +
                "applicationId='" + applicationId + '\'' +
                ", serialNumber=" + serialNumber +
                ", mark='" + mark + '\'' +
                ", carNumber='" + carNumber + '\'' +
                ", applicationDate=" + applicationDate +
                ", carrierName='" + carrierName + '\'' +
                ", driverName='" + driverName + '\'' +
                ", scrap='" + scrap + '\'' +
                ", netWeight=" + netWeight +
                ", businessDescription='" + businessDescription + '\'' +
                ", remark='" + remark + '\'' +
                ", factoryType='" + factoryType + '\'' +
                ", orderId='" + orderId + '\'' +
                ", businessType=" + businessType +
                ", phone='" + phone + '\'' +
                ", createdBy=" + createdBy +
                ", checkerName='" + checkerName + '\'' +
                ", createdName='" + createdName + '\'' +
                ", status=" + status +
                ", enterStatus=" + enterStatus +
                ", applicationNo='" + applicationNo + '\'' +
                ", driverId=" + driverId +
                ", idcard='" + idcard + '\'' +
                ", enterDate=" + enterDate +
                ", outDate=" + outDate +
                ", beforeWeight=" + beforeWeight +
                ", reason='" + reason + '\'' +
                ", postType=" + postType +
                ", createdDate=" + createdDate +
                ", isdeleted=" + isdeleted +
                ", carrierId=" + carrierId +
                ", reasonKey='" + reasonKey + '\'' +
                ", scrapKey='" + scrapKey + '\'' +
                ", checkerId=" + checkerId +
                ", isSelf=" + isSelf +
                ", isCheck=" + isCheck +
                ", supplyCode='" + supplyCode + '\'' +
                ", supplyName='" + supplyName + '\'' +
                ", custCode='" + custCode + '\'' +
                ", custName='" + custName + '\'' +
                ", documentCode='" + documentCode + '\'' +
                ", dataAreaId='" + dataAreaId + '\'' +
                ", factoryCode='" + factoryCode + '\'' +
                ", taskId='" + taskId + '\'' +
                ", appointmentWeight=" + appointmentWeight +
                ", loseWeight=" + loseWeight +
                ", shippedWithPalletWeight=" + shippedWithPalletWeight +
                ", floatReason='" + floatReason + '\'' +
                ", cardId='" + cardId + '\'' +
                ", cardType=" + cardType +
                ", createCardTime=" + createCardTime +
                ", returnCardTime=" + returnCardTime +
                ", appointmentStatus=" + appointmentStatus +
                ", message='" + message + '\'' +
                ", firstWeight=" + firstWeight +
                ", firstWeightTime=" + firstWeightTime +
                ", loadoMeterID1='" + loadoMeterID1 + '\'' +
                ", userID1='" + userID1 + '\'' +
                ", userName1='" + userName1 + '\'' +
                ", secondWeight=" + secondWeight +
                ", truckWeight=" + truckWeight +
                ", packWeight=" + packWeight +
                ", goodsWeight=" + goodsWeight +
                ", enableControl=" + enableControl +
                ", toleranceAdd=" + toleranceAdd +
                ", toleranceDec=" + toleranceDec +
                ", upFloatingWeight=" + upFloatingWeight +
                ", downFloatingWeight=" + downFloatingWeight +
                ", secondWeightTime=" + secondWeightTime +
                ", loadoMeterID2='" + loadoMeterID2 + '\'' +
                ", userID2='" + userID2 + '\'' +
                ", userName2='" + userName2 + '\'' +
                ", truckNo2='" + truckNo2 + '\'' +
                ", atmsId='" + atmsId + '\'' +
                ", isUpdate='" + isUpdate + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", loadoMeterID='" + loadoMeterID + '\'' +
                ", truckType=" + truckType +
                ", requestId=" + requestId +
                ", wMSPickingRouteIDs='" + wMSPickingRouteIDs + '\'' +
                ", wMSPickingRouteIDs2='" + wMSPickingRouteIDs2 + '\'' +
                ", editFlag='" + editFlag + '\'' +
                ", originalWeight=" + originalWeight +
                ", editWeight=" + editWeight +
                ", editPerson='" + editPerson + '\'' +
                ", editTime=" + editTime +
                ", editReason='" + editReason + '\'' +
                '}';
    }

}