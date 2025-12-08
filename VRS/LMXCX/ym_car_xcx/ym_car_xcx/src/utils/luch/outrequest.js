import Request from "luch-request";
import jwt from "../auth/jwt";

const http = new Request();
// 线上
// const baseURL = "https://api.dhfwzx-2022.com/api";
// 俞倩ip
const baseURL = "http://esb-test.lamipak.biz:18080/esb/d365/api";
// const baseURL = "https://esb-test.lamipak.biz:18443/esb/d365/api";

// 请求前
http.interceptors.request.use(
    async (config) => {
        // 可使用async await 做异步操作
        config.baseURL = baseURL;
        let headers = config.data && config.data.headers || {}
        // console.log(config)
        // console.log(headers)
        config.header = {
            ...config.header,
            ...headers,
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
