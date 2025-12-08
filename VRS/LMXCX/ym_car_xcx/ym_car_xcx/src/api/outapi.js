import { apiGet, apiPost, apiPut, apiDelete } from "../utils/http/outapi_function";

// 外部接口
// 新增订单
export const CreateTruckAppointmentList = (data) => {
    return apiPost("", data, { auth: true });
};

