package com.vrs.quartz.service.impl;

import com.vrs.common.constant.ScheduleConstants;
import com.vrs.common.exception.job.TaskException;
import com.vrs.quartz.domain.VRSGlobalVariable;
import com.vrs.quartz.domain.VRSSupplier;
import com.vrs.quartz.domain.SysJob;
import com.vrs.quartz.mapper.RytaskMapper;
import com.vrs.quartz.mapper.SysJobMapper;
import com.vrs.quartz.service.ISysJobService;
import com.vrs.quartz.service.RyTaskService;
import com.vrs.quartz.util.CronUtils;
import com.vrs.quartz.util.ScheduleUtils;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 定时任务调度信息 服务层
 * 
 * @author ruoyi
 */
@Service
public class RyTaskServiceImpl implements RyTaskService
{
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private RytaskMapper rytaskMapper;

    /**
     * 同步供应商
     * @param list
     * @return
     */
    @Override
    public int updateVRSSupplier(List<VRSSupplier> list){
        return  rytaskMapper.updateVRSSupplier(list);
    }

    @Override
    public int deleteVRSSuppliers(){
        return rytaskMapper.deleteVRSSuppliers();
    }

    @Override
    public List<VRSSupplier> selectVRSSupplierList(){
        return rytaskMapper.selectVRSSupplierList();
    }

    @Override
    public VRSGlobalVariable selectVRSGlobalVariable() {
        return rytaskMapper.selectVRSGlobalVariable();
    }

    @Override
    public List<VRSSupplier> selectVRSSupplierLocalList() {
        return rytaskMapper.selectVRSSupplierLocalList();
    }
}
