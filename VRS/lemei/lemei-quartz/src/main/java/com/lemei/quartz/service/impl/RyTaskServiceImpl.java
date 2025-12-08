package com.lemei.quartz.service.impl;

import com.lemei.common.constant.ScheduleConstants;
import com.lemei.common.exception.job.TaskException;
import com.lemei.quartz.domain.LmGlobalVariable;
import com.lemei.quartz.domain.LmSupplier;
import com.lemei.quartz.domain.SysJob;
import com.lemei.quartz.mapper.RytaskMapper;
import com.lemei.quartz.mapper.SysJobMapper;
import com.lemei.quartz.service.ISysJobService;
import com.lemei.quartz.service.RyTaskService;
import com.lemei.quartz.util.CronUtils;
import com.lemei.quartz.util.ScheduleUtils;
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
    public int updateLmSupplier(List<LmSupplier> list){
        return  rytaskMapper.updateLmSupplier(list);
    }

    @Override
    public int deleteLmSuppliers(){
        return rytaskMapper.deleteLmSuppliers();
    }

    @Override
    public List<LmSupplier> selectLmSupplierList(){
        return rytaskMapper.selectLmSupplierList();
    }

    @Override
    public LmGlobalVariable selectLmGlobalVariable() {
        return rytaskMapper.selectLmGlobalVariable();
    }

    @Override
    public List<LmSupplier> selectLmSupplierLocalList() {
        return rytaskMapper.selectLmSupplierLocalList();
    }
}
