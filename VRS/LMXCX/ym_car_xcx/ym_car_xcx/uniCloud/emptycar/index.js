// 云函数入口文件
const cloud = require('wx-server-sdk')

//引入request-promise
var rp = require('request-promise');

cloud.init({
  env: 'yhtext-6gn3sc84170f77e1',
  traceUser: true
})

// 云函数入口函数
exports.main = async (event, context) => {
  // const wxContext = cloud.getWXContext()
  let headers = event.headers || {}
  return new Promise((resolve, reject) => {
    // 生产：
    // https://d365-prod.lamipak.biz/namespaces/AXSF/api/services/IWS_LMPInterfaceServiceGroup/IWS_LMPInterfaceService/ApiMain
    // UAT：
    // https://d365-uat.lamipak.biz/namespaces/AXSF/api/services/IWS_LMPInterfaceServiceGroup/IWS_LMPInterfaceService/ApiMain
    // Dev226:
    // https://usnconeboxax1aos.cloud.onebox.dynamics.com/api/services/IWS_LMPInterfaceServiceGroup/IWS_LMPInterfaceService/ApiMain
    // 正式环境
    // https://esb.lamipak.biz:18443/esb/d365/api
    // https://esb.lamipak.biz:18080/esb/d365/api
    // 测试环境
    // https://esb-test.lamipak.biz:18443/esb/d365/api
    // https://esb-test.lamipak.biz:18080/esb/d365/api
      rp('https://esb.lamipak.biz:18443/esb/d365/api',{
        method:'POST',
        dataType:'json',
        headers:{
          ...headers,
          "Content-Type": "application/json;charset=UTF-8",
        },
        body: JSON.stringify(event)
      }).then(res => {
        console.log(headers)
        console.log(res)
        if (res) {
            res = JSON.parse(res)
        }
        resolve(res)
      })
  })
}