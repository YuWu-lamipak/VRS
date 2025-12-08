package com.lemei.domain.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ApplicationScrapAdvance {
    private String zName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;
}
