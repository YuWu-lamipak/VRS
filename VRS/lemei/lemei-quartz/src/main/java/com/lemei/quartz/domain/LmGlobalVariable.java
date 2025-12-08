package com.lemei.quartz.domain;

import com.lemei.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LmGlobalVariable extends BaseEntity {
    private BigDecimal soToleranceAdd;
    private BigDecimal soToleranceDec;
    private BigDecimal soPalletWeight;
}
