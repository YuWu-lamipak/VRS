package com.lemei.system.domain.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class CarDeliveryItemReal implements Serializable {
    private String lmId;
    private String lmDnnum;
    private String lmDnLine;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lmActualdeliverydate;
    private String lmOrderid;
    private String lmCarNumber;
    private String lmPackagingid;
    private BigDecimal lmGrossweight;
}
