package com.lemei.domain.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 打印磅单请求参数
 * 
 * @author system
 * @date 2025-12-10
 */
@Data
public class PrintWeightTicketParam {

    @ApiModelProperty(value = "业务ID", required = true)
    @NotBlank(message = "业务ID不能为空")
    private String orderId;

    @ApiModelProperty(value = "业务类型", required = true)
    @NotNull(message = "业务类型不能为空")
    private Integer businessType;
}