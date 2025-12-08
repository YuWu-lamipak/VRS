import {
	http
} from '../luch/outrequest'
export function apiGet(url, params, custom) {
	return http.request({
		// 请求方法必须大写 [GET|POST|PUT|DELETE|CONNECT|HEAD|OPTIONS|TRACE]
		method: 'GET',
		url: url,
		dataType: 'json',
		params: params,
		// custom: {auth:true},
		custom: custom,
	})
}


export function apiGetData(url, params, data, custom) {
	return http.request({
		// 请求方法必须大写 [GET|POST|PUT|DELETE|CONNECT|HEAD|OPTIONS|TRACE]
		method: 'GET',
		url: url,
		dataType: 'json',
		data: data,
		params: params,
		custom: custom,
	})
}

export function apiPost(url, data, custom) {
	return http.request({
		// 请求方法必须大写 [GET|POST|PUT|DELETE|CONNECT|HEAD|OPTIONS|TRACE]
		method: 'POST',
		url: url,
		data: data,
		dataType: 'json',
		// custom: {auth:true},
		custom: custom,
	})
}

export function apiPostData(url, data, custom) {
	return http.request({
		// 请求方法必须大写 [GET|POST|PUT|DELETE|CONNECT|HEAD|OPTIONS|TRACE]
		method: 'POST',
		url: url,
		data: data,
		dataType: 'json',
		custom: custom,
	})
}
export function apiPut(url, data, custom) {
	return http.request({
		// 请求方法必须大写 [GET|POST|PUT|DELETE|CONNECT|HEAD|OPTIONS|TRACE]
		method: 'PUT',
		url: url,
		data: data,
		dataType: 'json',
		// custom: {auth:true},
		custom: custom,
	})
}
export function apiPutData(url, params, data, custom) {
	return http.request({
		// 请求方法必须大写 [GET|POST|PUT|DELETE|CONNECT|HEAD|OPTIONS|TRACE]
		method: 'PUT',
		url: url,
		data: data,
		params: params,
		dataType: 'json',
		// custom: {auth:true},
		custom: custom,
	})
}
export function apiDelete(url, data, params, custom) {
	return http.request({
		// 请求方法必须大写 [GET|POST|PUT|DELETE|CONNECT|HEAD|OPTIONS|TRACE]
		method: 'DELETE',
		url: url,
		dataType: 'json',
		data: data,
		// custom: {auth:true},
		params: params,
		custom: custom,
	})
}
