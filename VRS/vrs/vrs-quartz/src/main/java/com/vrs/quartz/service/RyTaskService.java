package com.vrs.quartz.service;

import com.vrs.common.exception.job.TaskException;
import com.vrs.quartz.domain.VRSGlobalVariable;
import com.vrs.quartz.domain.VRSSupplier;
import com.vrs.quartz.domain.SysJob;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * 定时任务调度信息信息 服务层
 * 
 * @author ruoyi
 */
public interface RyTaskService
{
    int updateVRSSupplier(List<VRSSupplier> list);

    int deleteVRSSuppliers();

    List<VRSSupplier> selectVRSSupplierList();

    VRSGlobalVariable selectVRSGlobalVariable();

    List<VRSSupplier> selectVRSSupplierLocalList();

}
