const tokenKey = "accessToken"; //键值
const userKey = "user"; // 用户信息
const IsManagement = "IsManagement"; // 菜单
const authorityKey = "authority"; // 按钮权限
const carData = "carData"; // 购物车
const smsKey = "smsKey"; // 短信key
const AccountKey = 'AccountKey' // 登陆账号


// token
const getAccessToken = function() {
	let token = "";
	try {
		token = "Bearer " + uni.getStorageSync(tokenKey);
	} catch (e) {}
	return token;
};
const setAccessToken = (access_token) => {
	try {
		uni.setStorageSync(tokenKey, access_token);
		return true;
	} catch (e) {
		return false;
	}
};
const clearAccessToken = function() {
	try {
		uni.removeStorageSync(tokenKey);
	} catch (e) {}
};
// account 账户
const getAccount = function() {
	try {
		return uni.getStorageSync(AccountKey);
	} catch (e) {
		return false;
	}
};
const setAccount = (Account) => {
	try {
		uni.setStorageSync(AccountKey, Account);
		return true;
	} catch (e) {
		return false;
	}
};
const clearAccount = function() {
	try {
		uni.removeStorageSync(AccountKey);
	} catch (e) {}
};
// userinfo
const setUser = (user) => {
	try {
		uni.setStorageSync(userKey, user);
		return true;
	} catch (e) {
		return false;
	}
};
const getUser = function() {
	try {
		return uni.getStorageSync(userKey);
	} catch (e) {
		return false;
	}
};
const clearUser = function() {
	try {
		uni.removeStorageSync(userKey);
	} catch (e) {}
};

// 权限
const setManagement = (data) => {
	try {
		uni.setStorageSync(IsManagement, data);
		return true;
	} catch (e) {
		return false;
	}
};
const getManagement = function() {
	try {
		return uni.getStorageSync(IsManagement);
	} catch (e) {
		return false;
	}
};

const clearManagement = function() {
	try {
		uni.removeStorageSync(IsManagement);
	} catch (e) {}
};
// 按钮权限
const setAuthority = (authority) => {
	try {
		uni.setStorageSync(authorityKey, authority);
		return true;
	} catch (e) {
		return false;
	}
};
const getAuthority = function() {
	try {
		return uni.getStorageSync(authorityKey);
	} catch (e) {
		return false;
	}
};
const clearAuthority = function() {
	try {
		uni.removeStorageSync(authorityKey);
	} catch (e) {}
};

// 购物车数据
const setCarData = (data) => {
	try {
		uni.setStorageSync(carData, data);
		return true;
	} catch (e) {
		return false;
	}
};
const getCarData = function() {
	try {
		return uni.getStorageSync(carData);
	} catch (e) {
		return false;
	}
};
const clearCarData = function() {
	try {
		uni.removeStorageSync(carData);
	} catch (e) {}
};

// 短信key
const getSmsKey = function() { 
	try {
		return uni.getStorageSync(smsKey);
	} catch (e) {
		return false;
	}
};
const setSmsKey = (data) => {
	try {
		uni.setStorageSync(smsKey, data);
		return true;
	} catch (e) {
		return false;
	}
};
const clearSmsKey = function() {
	try {
		uni.removeStorageSync(smsKey);
	} catch (e) {}
};
export default {
	getAccessToken,
	setAccessToken,
	clearAccessToken,
	// 账号
	getAccount,
	setAccount,
	clearAccount,
	// 个人信息
	getUser,
	setUser,
	clearUser,
	// 菜单
	setManagement,
	getManagement,
	clearManagement,
	// 按钮
	setAuthority,
	getAuthority,
	clearAuthority,
	// 购物车数据
	setCarData,
	getCarData,
	clearCarData,
	// 短信key
	getSmsKey,
	setSmsKey,
	clearSmsKey
};
