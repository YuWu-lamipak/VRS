package com.lemei.quartz.service;

import com.lemei.common.exception.job.TaskException;
import com.lemei.quartz.domain.LmGlobalVariable;
import com.lemei.quartz.domain.LmSupplier;
import com.lemei.quartz.domain.SysJob;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * 定时任务调度信息信息 服务层
 * 
 * @author ruoyi
 */
public interface RyTaskService
{
    int updateLmSupplier(List<LmSupplier> list);

    int deleteLmSuppliers();

    List<LmSupplier> selectLmSupplierList();

    LmGlobalVariable selectLmGlobalVariable();

    List<LmSupplier> selectLmSupplierLocalList();

}
