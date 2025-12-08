package com.lemei.domain.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lemei.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yq
 * @CLassName ApplicationEditParam
 * @Description
 * @date 2022/8/29 13:37
 **/
@Data
public class ApplicationEditParam {


    /** 申请id */
    @NotBlank
    private String applicationId;

    /** 司机姓名 */
    @ApiModelProperty(name = "司机姓名")
    private String driverName;

    /** 车牌号 */
    @ApiModelProperty(name = "车牌号")
    private String carNumber;

    /** 司机ID */
    private Long driverId;

    /** 手机号 */
    @ApiModelProperty(name = "手机号")
    private String phone;

    /** 身份证号 */
    @ApiModelProperty(name = "身份证号")
    private String idcard;

    /** 预约时间 */
    @ApiModelProperty(name = "预约时间")
    private String applicationDate;

    /** 出场时间 */
    @ApiModelProperty(name = "出场时间")
    private Date outDate;

    /** 入场时间 */
    @ApiModelProperty(name = "入场时间")
    private Date enterDate;

    /** 进场状态 0未进厂 1已进厂 2已出厂 */
    @ApiModelProperty(name = "进场状态 0未进厂 1已进厂 2已出厂")
    private Integer enterStatus;

    /** 1已申请2审核通过3审核不通过 */
    @ApiModelProperty(name = "1已申请2审核通过3审核不通过")
    private Integer status;

    /** 承运商id */
    @ApiModelProperty(name = "承运商id")
    private Long carrierId;

    /** 承运商姓名 */
    @ApiModelProperty(name = "承运商姓名")
    private String carrierName;

    /** 送/提货预计重量 */
    @ApiModelProperty(name = "送/提货预计重量")
    private Long beforeWeight;

    /** 原因 */
    @ApiModelProperty(name = "原因")
    private String reason;

    /**原因key值 **/
    private String reasonKey;

    /** 废料名称 */
    @ApiModelProperty(name = "废料名称")
    private String scrap;

    /** 废料名称 数据字典key值 */
    private String scrapKey;

    /** 提交方式 1本人提交2代提交 */
    private Integer postType;

    private Integer isDeleted;

    /** 审核人id */
    private Long checkerId;

    /** 审核人姓名 */
    private String checkerName;

    private String remark;

    /**  第三方接口id  **/
    private String orderId;
    /**  第三方 业务类型 0: 采购 1：销售 2：其它**/
    private Integer businessType;

    private String createdName;

    /**
     *供应商编号
     */
    private String supplyCode;

    /**
     * 供应商名称
     */
    private String supplyName;
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
     * 业务主键
     */
    private String taskId;
    /**
     * 预约重量
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal appointmentWeight;
    /**
     * 地磅卡号
     */
    private String cardId;
    /**
     * 卡类型
     */
    private Integer cardType;
    /**
     * 制卡时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "制卡时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createCardTime;
    /**
     * 退卡时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "退卡时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date returnCardTime;
    /**
     * 预约状态
     */
    private Integer appointmentStatus;
    /**
     * 1磅重量(KG)
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal firstWeight;
    /**
     * 1磅过磅时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "1磅过磅时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date firstWeightTime;
    /**
     * 1地磅编号
     */
    private String loadoMeterID1;
    /**
     * 1磅⽤户编号
     */
    private String userID1;
    /**
     * 1磅⽤户名
     */
    private String userName1;
    /**
     * 2磅重量(毛KG)
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal secondWeight;
    /**
     * 2磅重量(皮KG)
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal truckWeight;
    /**
     * 2磅重量(净KG)
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal netWeight;
    /**
     * 2磅重量(扣KG)
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal packWeight;
    /**
     * 2磅重量(实KG)
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal goodsWeight;
    /**
     * 正容差
     */
    private BigDecimal toleranceAdd;
    /**
     * 负容差
     */
    private BigDecimal toleranceDec;
    /**
     * 上浮重量
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal upFloatingWeight;
    /**
     * 下浮重量
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal downFloatingWeight;
    /**
     * 2磅过磅时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "2磅过磅时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date secondWeightTime;
    /**
     * 2地磅编号
     */
    private String loadoMeterID2;
    /**
     * 2磅用户编号
     */
    private String userID2;
    /**
     * 2磅用户名
     */
    private String userName2;

    /**
     * 挂车号
     */
    private String truckNo2;

    /**
     * ATMSID
     */
    private String atmsId;

    /**
     * 是否更新
     */
    private String isUpdate;
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
     * 车辆类别
     */
    private Integer truckType;
    /**
     * 取消类型
     */
    private String request;
    /**
     * 原因说明
     */
    private String reasonState;
    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发送时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date requestTime;
    @ApiModelProperty(name = "流水号码")
    private Long serialNumber;
    /**
     * 启用管控
     */
    private Integer enableControl;
    /**
     * 修改销售预约重量标记
     */
    private String editFlag;
    /**
     * 业务描述
     */
    private String businessDescription;

}
