package com.vrs.domain.param;

import com.vrs.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yq
 * @CLassName ApplicationQueryParam
 * @Description
 * @date 2022/8/26 17:01
 **/
@Data
public class ApplicationQueryParam {

    /** 申请id */
    private String applicationId;

    /** 车牌号 */
    @ApiModelProperty(name = "车牌号")
    private String carNumber;

    /** 车牌号、预约人、承运商名称 */
    @ApiModelProperty("查询条件（车牌号、预约人、承运商名称）")
    private String queryCondition;

    @ApiModelProperty("创建人")
    private Long createdBy;
    /** 审核人id */
    @ApiModelProperty("审核人")
    private Long checkerId;
    /** 预约时间  */
    @ApiModelProperty("预约时间-开始")
    private String startDate;
    @ApiModelProperty("预约时间-结束")
    private String endDate;

    @ApiModelProperty("状态1已申请2审核通过3审核不通过")
    private Integer status;

    @ApiModelProperty("进厂状态 0未进厂 1已进厂 2已出厂")
    private Integer enterStatus;

    @NotNull
    @ApiModelProperty("每页条数")
    private Integer pageSize;

    @NotNull
    @ApiModelProperty("页数")
    private Integer pageIndex;

    @ApiModelProperty(value = "1未审核 2已审核")
    private Integer anthStatus;

    @ApiModelProperty(value = "1本人提交 2代提交")
    private Integer postType;

    @ApiModelProperty(value = "司机id")
    private Long driverId;

    @ApiModelProperty(name = "预约时间")
    private  java.sql.Date applicationDate;

    @ApiModelProperty(name = "业务类型")
    private Integer businessType;

    @ApiModelProperty(name = "正容差")
    private BigDecimal toleranceAdd;

    @ApiModelProperty(name = "负容差")
    private BigDecimal toleranceDec;

    @ApiModelProperty(name = "预约重量")
    private BigDecimal appointmentWeight;

    @ApiModelProperty(name = "2磅重量(实KG)")
    private BigDecimal goodsWeight;

    @ApiModelProperty(name = "司机姓名")
    private String driverName;

    @ApiModelProperty(name = "预约号码")
    private String orderId;

    @ApiModelProperty(name = "流水号码")
    private Long serialNumber;

    @ApiModelProperty(name = "预约状态")
    private Integer appointmentStatus;
}
