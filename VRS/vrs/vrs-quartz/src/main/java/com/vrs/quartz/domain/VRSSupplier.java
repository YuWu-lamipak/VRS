package com.vrs.quartz.domain;

import com.vrs.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ycc
 * @CLassName VRSSupplier
 * @Description
 **/
@Data
public class VRSSupplier extends BaseEntity {

    private String ID;
    private String code;
    private String name;
    private BigDecimal toleranceAdd;
    private BigDecimal toleranceDec;
}
