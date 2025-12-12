package com.vrs.domain.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class VRSDeliveryItemField {
    public String vrsDnnum;
    //    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date vrsActualdeliverydate;
    public Date lmLoadTime;
    public String vrsDnLine;
    public String vrsDnLineref;
    public String lmBatch;
    public String vrsPackagingid;
    public BigDecimal vrsGrossweight;
    public String lmWeightunit;
    public String vrsCarNumber;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date vrsInsertTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date vrsUpdateTime;
}
