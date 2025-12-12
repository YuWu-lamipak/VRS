package com.vrs.system.domain.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class CarDeliveryItemReal implements Serializable {
    private String vrsId;
    private String vrsDnnum;
    private String vrsDnLine;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date vrsActualdeliverydate;
    private String vrsOrderid;
    private String vrsCarNumber;
    private String vrsPackagingid;
    private BigDecimal vrsGrossweight;
}
