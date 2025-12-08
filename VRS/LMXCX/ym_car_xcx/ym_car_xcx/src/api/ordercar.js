import { apiGet, apiPost, apiPut, apiDelete, apiGetWithoutParams } from "../utils/http/api_function";

// 新增车辆预约
export const insert = (data) => {
    return apiPost("/application/insert", data, {});
};

// 修改车辆预约
export const update = (data) => {
    return apiPut("/application/update", data, {});
};

// 司机、承运商列表
export const getUserList = (data) => {
    return apiGet("/user/getUserList", data, {});
};

// 审核人列表
export const getCheckerList = (data) => {
    return apiGet("/user/getCheckerList", data, {});
};

// 原因列表
export const getReasonList = (data) => {
    return apiGet("/application/getReasonList", data, {});
};

// 供应商列表
export const getSupplierList = () => {
    return apiGetWithoutParams("/application/getSupplierList", {});
};

// 废料名称列表
export const getScrapList = () => {
  return apiGetWithoutParams("/application/getScrapList", {});
};

// 预约列表（历史）--门卫
export const guardlist = (data) => {
    return apiPost("/application/guard/list", data, {});
};

// 预约列表（历史）--审核员
export const checklist = (data) => {
    return apiPost("/application/check/list", data, {});
};

// 预约列表（历史）-- 非 门卫 / 审核员
export const historylist = (data) => {
    return apiPost("/application/history/list", data, {});
};

// 预约列表（历史）-- 非 门卫 / 审核员 （我的）
export const mylist = (data) => {
    return apiPost("/application/my/list", data, {});
};

// 预约详情
export const historydetail = (data) => {
    return apiGet("/application/detail", data, {});
};

// 删除订单
export const historydelete = (data) => {
    return apiPut("/application/delete", data, {});
};

// 审核
export const authen = (data) => {
    return apiPut("/application/authen", data, {});
};

// 进厂
export const enter = (data) => {
    return apiPut("/application/enter", data, {});
};

// 出厂
export const out = (data) => {
    return apiPut("/application/out", data, {});
};

// 门卫数据统计
export const guardstatistics = (data) => {
    return apiGet("/application/guard/statistics", data, {});
};

// 审核数据统计
export const checkerstatistics = (data) => {
    return apiGet("/application/checker/statistics", data, {});
};

// 本人数据统计
export const selfstatistics = (data) => {
    return apiGet("/application/self/statistics", data, {});
};

