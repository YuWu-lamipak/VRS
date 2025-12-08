import Request from "luch-request";
import jwt from "../auth/jwt";

const http = new Request();
// 线上
// const baseURL = "https://101.132.238.169:8600/api";
// const baseURL = "https://lemeiyuyue.feizhisoft.com/api";
// 俞倩ip
const baseURL = "http://172.23.81.116:8601/api";
// var baseURL = "http://192.168.1.10:8601/api"; // 请求前
// var baseURL = "http://carbooking-test.lamipak.biz:8601/api"; // 请求前
// const baseURL = "https://carbookingminpro-prod.lamipak.biz:8600/api";

// 请求前
http.interceptors.request.use(
    async (config) => {
        // 可使用async await 做异步操作
        config.baseURL = baseURL;
        config.header = {
            ...config.header,
            "Content-Type": "application/json;charset=UTF-8",
        };
        // 演示custom 用处
        if (!config.custom.auth) {
            config.header.Authorization = jwt.getAccessToken();
        }
        if (config.custom.loading) {
            uni.showLoading({
                title: "加载中",
            });
        }

        return config;
    },
    (config) => {
        // 可使用async await 做异步操作
        return Promise.reject(config);
    }
);
// 请求后
http.interceptors.response.use(
    async (response) => {
        //如果是需要权限认证的路由
        // if (response.config.custom.auth) {
        //     if (response.data.code == 401) {
        //         //刷新token
        //         uni.reLaunch({
        //             url: "/pages/login/login",
        //         });
        //     }
        // } else {
        // }
        if (response.data && response.data.code == 401) {
            //刷新token
            uni.reLaunch({
                url: "/pages/login/login",
            });
        }
        return response;
    },
    (response) => {
        /*  对响应错误做点什么 （statusCode !== 200）*/
        if (response.data && response.data.code == 401) {
            console.log(123);
        }
        if (response.data && response.data.code == 404) {
        }

        return response;
        // return Promise.reject(response)
    }
);

export { http, baseURL };
