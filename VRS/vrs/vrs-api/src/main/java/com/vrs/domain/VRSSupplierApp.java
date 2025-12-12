package com.vrs.domain;

import com.vrs.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ycc
 * @CLassName VRSSupplier
 * @Description
 **/
@Data
public class VRSSupplierApp extends BaseEntity {

    private String ID;
    private String code;
    private String name;
    private BigDecimal toleranceAdd;
    private BigDecimal toleranceDec;
}
