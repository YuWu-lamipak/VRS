package com.vrs.domain.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class VRSDeliveryItemReal {
    public String vrsId;
    public String vrsDnnum;
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date vrsActualdeliverydate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date lmLoadTime;
    public String vrsDnLine;
    public String vrsOrderid;
    public String vrsCarNumber;
    public String vrsPackagingid;
    public BigDecimal vrsGrossweight;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date vrsInsertTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date vrsUpdateTime;
}
