package com.vrs.quartz.mapper;

import com.vrs.quartz.domain.VRSGlobalVariable;
import com.vrs.quartz.domain.VRSSupplier;
import com.vrs.quartz.domain.SysJob;

import java.util.List;

/**
 * 调度任务信息 数据层
 * 
 * @author ruoyi
 */
public interface RytaskMapper
{
    int updateVRSSupplier(List<VRSSupplier> list);
    int deleteVRSSuppliers();
    List<VRSSupplier> selectVRSSupplierList();
    VRSGlobalVariable selectVRSGlobalVariable();

    List<VRSSupplier> selectVRSSupplierLocalList();
}
