package com.vrs.system.domain.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class CarSaleQueryParam {
    private String queryCondition;
    private String vrsDnnum;
    private String vrsPackagingid;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss ")
    private Date yesterday;
}
