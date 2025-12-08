package com.lemei.quartz.mapper;

import com.lemei.quartz.domain.LmGlobalVariable;
import com.lemei.quartz.domain.LmSupplier;
import com.lemei.quartz.domain.SysJob;

import java.util.List;

/**
 * 调度任务信息 数据层
 * 
 * @author ruoyi
 */
public interface RytaskMapper
{
    int updateLmSupplier(List<LmSupplier> list);
    int deleteLmSuppliers();
    List<LmSupplier> selectLmSupplierList();
    LmGlobalVariable selectLmGlobalVariable();

    List<LmSupplier> selectLmSupplierLocalList();
}
