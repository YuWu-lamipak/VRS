import { apiGet, apiPost, apiPut, apiDelete } from "../utils/http/api_function";

// 获取用户信息
export const userinfo = (data) => {
    return apiPost("/user/userinfo", data, {});
};

// 修改密码
export const updatePassword = (data) => {
    return apiPost("/user/updatePassword", data, {});
};

// 修改用户信息
export const userupdate = (data) => {
    return apiPost("/user/update", data, {});
};
