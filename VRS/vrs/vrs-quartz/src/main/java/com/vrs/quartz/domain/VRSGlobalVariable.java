package com.vrs.quartz.domain;

import com.vrs.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class VRSGlobalVariable extends BaseEntity {
    private BigDecimal soToleranceAdd;
    private BigDecimal soToleranceDec;
    private BigDecimal soPalletWeight;
}
