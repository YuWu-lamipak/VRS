package com.lemei.domain.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class LmDeliveryItemReal {
    public String lmId;
    public String lmDnnum;
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date lmActualdeliverydate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date lmLoadTime;
    public String lmDnline;
    public String lmOrderid;
    public String lmCarNumber;
    public String lmPackagingid;
    public BigDecimal lmGrossweight;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lmInsertTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lmUpdateTime;
}
