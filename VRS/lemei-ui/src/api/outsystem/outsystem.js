import outrequest from '@/utils/outrequest'

// 第三方 审核接口 （创建订单）
export function outform(query) {
  return outrequest({
    url: '',
    method: 'post',
    data: query
  })
}
