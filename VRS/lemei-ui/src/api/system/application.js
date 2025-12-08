import request from '@/utils/request'

// 查询车辆预约列表
export function listApplication(query) {
  return request({
    url: '/system/application/list',
    method: 'get',
    params: query
  })
}

// 查询车辆预约列表 --今日预约进场且已审核通过
export function todayListApplication(query) {
  return request({
    url: '/system/application/todayList',
    method: 'get',
    params: query
  })
}

// 查询承运商下面的司机列表
export function getDriverList(query) {
  return request({
    url: '/system/user/getDriverList/' + query,
    method: 'get'
  })
}

// 查询审核人列表
export function getCheckerList() {
  return request({
    url: '/system/user/getCheckerList/',
    method: 'get'
  })
}
// 承运商列表
export function getCarrierList() {
  return request({
    url: '/system/user/getCarrierList',
    method: 'get'
  })
}

// 查询车辆预约详细
export function getApplication(applicationId) {
  return request({
    url: '/system/application/' + applicationId,
    method: 'get'
  })
}
// 查询用户详细
export function getDetailByUserId(userId) {
  return request({
    url: '/system/user/getDetailByUserId/' + userId,
    method: 'get'
  })
}

// 新增车辆预约
export function addApplication(data) {
  return request({
    url: '/system/application',
    method: 'post',
    data: data
  })
}

// 修改车辆预约
export function updateApplication(data) {
  return request({
    url: '/system/application',
    method: 'put',
    data: data
  })
}

// 修改状态
export function statusApplication(data) {
  return request({
    url: '/system/application/statusEdit',
    method: 'put',
    data: data
  })
}

// 修改扣重
export function loseWeightApplication(data) {
  return request({
    url: '/system/application/loseWeightEdit',
    method: 'put',
    data: data
  })
}

// 修改销售预约重量
export function saleAppointmentWeightApplication(data) {
  return request({
    url: '/system/application/saleAppointmentWeightEdit',
    method: 'put',
    data: data
  })
}

// 修改废料预约重量
export function scrapAppointmentWeightApplication(data) {
  return request({
    url: '/system/application/scrapAppointmentWeightEdit',
    method: 'put',
    data: data
  })
}

// 修改出厂带栈板重量
export function updateShippedWithPalletWeightApplication(data) {
  return request({
    url: '/system/application/shippedWithPalletWeightEdit',
    method: 'put',
    data: data
  })
}

// 审核
export function authenApplication(data) {
  return request({
    url: '/system/application/authen',
    method: 'put',
    data: data
  })
}

// 进出厂
export function enterOrOut(data) {
  return request({
    url: '/system/application/enterOrOut',
    method: 'put',
    data: data
  })
}

// 出入厂凭证
export function enterOrOutMark(data) {
  return request({
    url: '/system/application/enterOrOutMark',
    method: 'put',
    data: data
  })
}

// 删除车辆预约
export function delApplication(applicationId) {
  return request({
    url: '/system/application/' + applicationId,
    method: 'delete'
  })
}

// 删除车辆预约-逻辑删除
export function deleteApplication(data) {
  console.log(data)
  return request({
    url: '/system/application/delete',
    method: 'put',
    data: data
  })
}

// 导出车辆预约
export function exportApplication(query) {
  return request({
    url: '/system/application/export',
    method: 'get',
    params: query
  })
}

// 首页数据统计
export function statis(query) {
  return request({
    url: '/system/application/statis',
    method: 'get',
    params: query
  })
}

// 获取供应商数据
export function getSupplierList() {
  return request({
    url: '/system/application/getSupplierList',
    method: 'get'
  })
}

// 获取全局变量数据
export function getGlobalVariable() {
  return request({
    url: '/system/application/getGlobalVariable',
    method: 'get'
  })
}

// 取消称重或作废称重
export function weightCancelInfo(data) {
  return request({
    url: '/system/application/weightCancelInfo',
    method: 'post',
    data: data
  })
}

// 查询销售列表
export function saleListApplication(query) {
  return request({
    url: '/system/application/saleList',
    method: 'get',
    params: query
  })
}

// 查询销售列表详细
export function getSaleApplication(lmId) {
  return request({
    url: '/system/application/saleInfo/' + lmId,
    method: 'get'
  })
}

// 修改销售列表
export function updateSaleApplication(data) {
  return request({
    url: '/system/application/saleUpdate',
    method: 'put',
    data: data
  })
}

// 删除销售列表
export function deleteSaleApplication(lmId) {
  return request({
    url: '/system/application/saleDelete/' + lmId,
    method: 'delete'
  })
}

// 请求ESB更新交货单
export function getDeliveryOrderByThird() {
  return request({
    url: '/system/application/getDeliveryOrderByThird',
    method: 'get'
  })
}

// 取消预约
export function cancelReservationApplication(data) {
  return request({
    url: '/system/application/cancelReservation',
    method: 'put',
    data: data
  })
}

// 重新上1磅
export function reWeight1(data) {
  return request({
    url: '/system/application/reWeight1',
    method: 'put',
    data: data
  })
}

// 重新上2磅
export function reWeight2(data) {
  return request({
    url: '/system/application/reWeight2',
    method: 'put',
    data: data
  })
}

