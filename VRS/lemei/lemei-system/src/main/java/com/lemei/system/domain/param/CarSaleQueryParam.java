package com.lemei.system.domain.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class CarSaleQueryParam {
    private String queryCondition;
    private String lmDnnum;
    private String lmPackagingid;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss ")
    private Date yesterday;
}
