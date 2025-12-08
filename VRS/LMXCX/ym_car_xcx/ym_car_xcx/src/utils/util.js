import jwt from "@/utils/auth/jwt.js";
var app = getApp();
var reg_email = new RegExp(/^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/); //邮箱正则
var reg_tel = new RegExp(/^1(0|1|2|3|4|5|6|7|8|9)\d{9}$/); //手机号正则
var reg_password = new RegExp(/^[a-zA-Z]+[a-zA-Z0-9]+$/); //以字母开头，由数字和字母组成

/*   timeFormat 转换时间格式
 * **************************************************************************
 *                                                                          *
 *                                   _oo8oo_                                *
 *                                  o8888888o                               *
 *                                  88" . "88                               *
 *                                  (| -_- |)                               *
 *                                  0\  =  /0                               *
 *                                ___/'==='\___                             *
 *                              .' \\|     |// '.                           *
 *                             / \\|||  :  |||// \                          *
 *                            / _||||| -:- |||||_ \                         *
 *                           |   | \\\  -  /// |   |                        *
 *                           | \_|  ''\---/''  |_/ |                        *
 *                           \  .-\__  '-'  __/-.  /                        *
 *                         ___'. .'  /--.--\  '. .'___                      *
 *                      ."" '<  '.___\_<|>_/___.'  >' "".                   *
 *                     | | :  `- \`.:`\ _ /`:.`/ -`  : | |                  *
 *                     \  \ `-.   \_ __\ /__ _/   .-` /  /                  *
 *                 =====`-.____`.___ \_____/ ___.`____.-`=====              *
 *                                   `=---=`                                *
 * **************************************************************************
 */
export function timeFormat (dateStr, format, type) {
	const weekArr = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
	let regDate = null
	if (!dateStr) return ''
	if (!(type === 'date')) {
	  const regStr = dateStr.replace(/-/g, '/')
	  regDate = new Date(regStr)
	} else {
	  // type 为 date时，dataStr 应为 new Date（）形式
	  // 一般用于 ui组件返回日期格式为 date 的情况
	  regDate = dateStr
	}
	const year = regDate.getFullYear()
	const month = regDate.getMonth() + 1 < 10 ? '0' + (regDate.getMonth() + 1) : (regDate.getMonth() + 1)
	const week = regDate.getDay()
	const day = regDate.getDate() < 10 ? '0' + regDate.getDate() : regDate.getDate()
	const hour = regDate.getHours() < 10 ? '0' + regDate.getHours() : regDate.getHours()
	const ampm = hour >= 12 ? '下午' : '上午'
	const min = regDate.getMinutes() < 10 ? '0' + regDate.getMinutes() : regDate.getMinutes()
	const sec = regDate.getSeconds() < 10 ? '0' + regDate.getSeconds() : regDate.getSeconds()
	if (format === 'YYYY-MM-DD hh:mm:ss') {
	  return `${year}-${month}-${day} ${hour}:${min}:${sec}`
	}
	if (format === 'YYYY-MM-DD hh:mm') {
	  return `${year}-${month}-${day} ${hour}:${min}`
	}
	if (format === 'YYYY-MM-DD') {
	  return `${year}-${month}-${day}`
	}
	if (format === 'MM-DD') {
	  return `${month}-${day}`
	}
	if (format === 'MM-DD hh:mm') {
	  return `${month}-${day} ${hour}:${min}`
	}
	if (format === 'hh:mm') {
	  return `${hour}:${min}`
	}
	if (format === 'hh:mm:ss') {
	  return `${hour}:${min}:${sec}`
	}
	if (format === 'YYYY/MM/DD hh:mm:ss') {
	  return `${year}/${month}/${day} ${hour}:${min}:${sec}`
	}
	if (format === 'YYYY/MM/DD hh:mm') {
	  return `${year}/${month}/${day} ${hour}:${min}`
	}
	if (format === 'YYYY/MM/DD') {
	  return `${year}/${month}/${day}`
	}
	if (format === 'YYYY.MM.DD') {
	  return `${year}.${month}.${day}`
	}
	if (format === 'MM/DD') {
	  return `${month}/${day}`
	}
	if (format === 'MM.DD') {
	  return `${month}.${day}`
	}
	if (format === 'MM/DD hh:mm') {
	  return `${month}/${day} ${hour}:${min}`
	}
	if (format === '年月日 hh:mm:ss') {
	  return `${year}年${month}月${day}日 ${hour}:${min}:${sec}`
	}
	if (format === '年月日 hh:mm') {
	  return `${year}年${month}月${day}日 ${hour}:${min}`
	}
	if (format === '年月日') {
	  return `${year}年${month}月${day}日`
	}
	if (format === '月日') {
	  return `${month}月${day}日`
	}
	if (format === '月日 hh:mm') {
	  return `${month}月${day}日 ${hour}:${min}`
	}
	if (format === '月日 am:pm hh:mm') {
	  return `${month}月${day}日 ${ampm} ${hour}:${min}`
	}
	if (format === '月日 week am:pm hh:mm') {
	  return `${month}月${day}日 ${weekArr[week]} ${ampm} ${hour}:${min}`
	} else return `${year}年${month}月${day}日 ${hour}:${min}`
  }

// 处理保留两位的小数
function numfloat(value) {
	if (value && value.length >= 1) {
	let first = value.substring(0, 1)
		if (first == '.') {
			let zero = '0'
			value = zero.concat(value)
			return value
		}
	}
	value = value.replace(/[^\d.]/g, ""); //清除“数字”和“.”以外的字符
	value = value.replace(/\.{2,}/g, "."); //只保留第一个. 清除多余的
	value = value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
	value = value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); //只能输入两个小数
	// 去除整数前的0和小数点后多余的0，如011.020300 =》 11.0203
	const reg1 = /0*([1-9]\d*|0\.\d+)/;
	const reg2 = /(?:\.0*|(\.\d+?)0+)$/;
	console.log(value.replace(reg1, '$1'))
	// console.log(value.replace(reg2, '$1'))
	// value = value.replace(reg1, '$1').replace(reg2, '$1');
	value = value.replace(reg1, '$1');
	return value
}

// 比较日期大小  第一个值大于第二个 返回true
function comparedate(startdate, enddate) {
	if (!startdate) {
		return false
	}
	if (!enddate) {
		return false
	}
	let date1 = null
	let date2 = null
	console.log(typeof(startdate))
	console.log(startdate)
	if (typeof(startdate) === 'number') {
		date1 = new Date(startdate)
	} else {
		// 特殊处理苹果 苹果日期不识别 -
		date1 = new Date(startdate.replace(/-/g, '/'))
	}
	if (typeof(enddate) === 'number') {
		date2 = new Date(enddate)
	} else {
		// 特殊处理苹果 苹果日期不识别 -
		date2 = new Date(enddate.replace(/-/g, '/'))
	}
	if (date1 > date2) {
		return true
	} else {
		return false
	}
}



///判断手机号格式是否正确
function PhoneRegExp(tel) {
	if (reg_tel.test(tel)) {
		return true;
	} else {
		uni.showToast({
			icon: 'none',
			title: "手机号格式不正确",
			duration: 1500
		})
		return false;
	}
}


///判断邮箱格式是否正确
function EmailRegExp(email) {
	if (reg_email.test(email)) {
		return true;
	} else {
		uni.showToast({
			icon: 'none',
			title: "邮箱格式不正确",
			duration: 1500
		})
		return false;
	}
}

///以字母开头，由数字和字母组成
function PasswordRegExp(password) {
	if (reg_password.test(password)) {
		return true;
	} else {
		uni.showToast({
			icon: 'none',
			title: "不支持中文、不能以数字开头",
			duration: 1500
		})
		return false;
	}
}


//  按钮权限 
function getButton(perms) {

	let data = jwt.getAuthority()
	var show = false
	data.map(function(item) {
		console.log('perms', perms, item.perms)

		if (item.perms == perms) {
			console.log('perms', perms)
			show = true
			return
		}
	})
	return show
}




module.exports = {
	PasswordRegExp: PasswordRegExp,
	PhoneRegExp: PhoneRegExp,
	EmailRegExp: EmailRegExp,
	getButton: getButton,
	timeFormat: timeFormat,
	comparedate: comparedate,
	numfloat: numfloat
}
