import {
    loginCode
} from '@/utils/auth/authorize';
import jwt from '@/utils/auth/jwt';
import {
    http
} from '@/utils/luch/request';

const login = function (detail) {
	console.log(detail,'detail--- auth');
    return new Promise((resolve, reject) => {
		console.log(resolve,'resolve');
		console.log(reject,'reject');
        loginCode().then(code => {
            detail.code = code;
            // return http.post('/auth/login', detail);
        }).then(res => {
            jwt.setAccessToken(res.data.data.access_token);
            jwt.setUser(res.data.data.userInfo);
            getApp().globalData.isLogin = true;
            resolve(res.data.data.userInfo);
        }).catch(err => {
            reject("登录失败")
        })
    })
}

export default {
    login
}