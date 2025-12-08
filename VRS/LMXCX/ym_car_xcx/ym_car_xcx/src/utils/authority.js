import jwt from "@/utils/auth/jwt.js";
// 判断角色身份
function checkauthority(role) {
    let roleid = null
    if (role === 'guard') {
        // 门卫
        roleid = 143
    } else if (role === 'admin') {
        // 审核员
        roleid = 144
    } else {
        return false
    }
    let userInfo = jwt.getUser() || {}
    // console.log(userInfo)
    let roleList = userInfo.roleList || []
    return roleList.includes(roleid)
}

// 判断用户类型
function checkuserType(userType) {
    let userTypeid = null
    if (userType === 'admin') {
        // 内部人员
        userTypeid = '00'
    } else if (userType === 'carrier') {
        // 承运商
        userTypeid = '01'
    } else if (userType === 'driver') {
        // 司机
        userTypeid = '02'
    } else {
        return false
    }
    let userInfo = jwt.getUser() || {}
    // console.log(userInfo)
    return userTypeid === (userInfo.userType || '')
}

module.exports = {
	checkauthority: checkauthority,
    checkuserType: checkuserType
}
