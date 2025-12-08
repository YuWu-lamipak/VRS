package com.lemei.domain.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class LmDeliveryItemField {
    public String lmDnnum;
    //    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date lmActualdeliverydate;
    public Date lmLoadTime;
    public String lmDnline;
    public String lmDnlineref;
    public String lmBatch;
    public String lmPackagingid;
    public BigDecimal lmGrossweight;
    public String lmWeightunit;
    public String lmCarNumber;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lmInsertTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lmUpdateTime;
}
