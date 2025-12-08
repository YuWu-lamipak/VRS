import { apiGet, apiPost, apiPut, apiDelete } from "../utils/http/api_function";

// 用户信息
export const login = (data) => {
    return apiPost("/user/login", data, { auth: true });
};

//微信授权登录
export const wxLogin = (data) => {
    return apiPost("/user/WxLogin", data, {});
};
