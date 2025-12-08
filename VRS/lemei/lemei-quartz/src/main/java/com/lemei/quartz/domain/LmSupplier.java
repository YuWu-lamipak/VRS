package com.lemei.quartz.domain;

import com.lemei.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ycc
 * @CLassName LmSupplier
 * @Description
 **/
@Data
public class LmSupplier extends BaseEntity {

    private String ID;
    private String code;
    private String name;
    private BigDecimal toleranceAdd;
    private BigDecimal toleranceDec;
}
