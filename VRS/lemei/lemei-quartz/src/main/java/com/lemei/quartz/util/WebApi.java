package com.lemei.quartz.util;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class WebApi {
	private static final Logger log = LoggerFactory.getLogger(WebApi.class);
	public static String postConnection(String url, String param, String type) throws Exception {
		PrintWriter printWriter = null;
		BufferedReader bufferedReader = null;
		HttpURLConnection httpURLConnection = null;

		StringBuffer responseResult = new StringBuffer();

		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			httpURLConnection = (HttpURLConnection) realUrl.openConnection();
			httpURLConnection.setConnectTimeout(30000);
			httpURLConnection.setReadTimeout(30000);
			// 设置通用的请求属性
			httpURLConnection.setRequestProperty("accept", "*/*");
			httpURLConnection.setRequestProperty("connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Content-Length", String.valueOf(param.length()));
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-type", "application/json");
			// 发送POST请求必须设置如下两行
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setRequestMethod(type);
			// 获取URLConnection对象对应的输出流
			//printWriter = new PrintWriter(httpURLConnection.getOutputStream());
			if("POST".equals(type)) {
				DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
				out.write(param.toString().getBytes("UTF-8"));
				// 发送请求参数
				//printWriter.write(new String(param.toString().getBytes(), "UTF-8"));
				// flush输出流的缓冲
				out.flush();


				out.close();
			}
			// 根据ResponseCode判断连接是否成功
			int responseCode = httpURLConnection.getResponseCode();
			//System.out.println("responseCode:"+responseCode);
			if (responseCode == httpURLConnection.HTTP_OK) {
				// 定义BufferedReader输入流来读取URL的ResponseData
				bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"));
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					responseResult.append(line);
				}
				return responseResult.toString();
			}
			return null;
		} catch (ConnectException e) {
			throw new Exception(e);
		} catch (MalformedURLException e) {
			throw new Exception(e);
		} catch (IOException e) {
			throw new Exception(e);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			httpURLConnection.disconnect();
			try {
				if (printWriter != null) {
					printWriter.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static String sendPost(String url,String bodyStr,String serverName) {
		
		StringBuffer resultBuffer = null;
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		String requestId = 	UUID.randomUUID().toString();
		String requestId1 = UUID.randomUUID().toString();
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.addHeader("trackId", requestId);
		httpPost.addHeader("requestId", requestId1);
		httpPost.addHeader("serviceName", serverName);
		httpPost.addHeader("sourceSystem", "ESB");
		BufferedReader br = null;
		try {
			StringEntity entity = new StringEntity(bodyStr, "UTF-8");
			httpPost.setEntity(entity);
			HttpResponse response = client.execute(httpPost);
//			Header header = response.getFirstHeader("esbDesc");
//			Header header = response.getFirstHeader("headergroup");
//			String esbMessage=header.getValue();
//			log.error("======返回请求头信息===="+esbMessage);
			resultBuffer = new StringBuffer();
//			br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			InputStreamReader iis = new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8);
			 br = new BufferedReader(iis);

			String temp;
			while((temp = br.
					readLine()) != null){
				resultBuffer.append(temp);
			}
			if("".equals(resultBuffer.toString())){
//				resultBuffer.append(esbMessage);
			}
		} catch (Exception e) {
			log.error("=====接口调用工具类报错=======",e);
			throw new RuntimeException(e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					br = null;
					throw new RuntimeException(e);
				}
			}
		}
		log.error("123456");
		log.error(resultBuffer.toString());
		return resultBuffer.toString();
	}

	public static String sendPostBysourceSystem(String url,String bodyStr,String serverName,String sourceSystem) {

		StringBuffer resultBuffer = null;
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		String requestId = 	UUID.randomUUID().toString();
		String requestId1 = UUID.randomUUID().toString();
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.addHeader("trackId", requestId);
		httpPost.addHeader("requestId", requestId1);
		httpPost.addHeader("serviceName", serverName);
		httpPost.addHeader("sourceSystem", sourceSystem);
		BufferedReader br = null;
		try {
			StringEntity entity = new StringEntity(bodyStr, "UTF-8");
			httpPost.setEntity(entity);
			HttpResponse response = client.execute(httpPost);
//			Header header = response.getFirstHeader("esbDesc");
//			Header header = response.getFirstHeader("headergroup");
//			String esbMessage=header.getValue();
//			log.error("======返回请求头信息===="+esbMessage);
			resultBuffer = new StringBuffer();
//			br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			InputStreamReader iis = new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8);
			br = new BufferedReader(iis);

			String temp;
			while((temp = br.
					readLine()) != null){
				resultBuffer.append(temp);
			}
			if("".equals(resultBuffer.toString())){
//				resultBuffer.append(esbMessage);
			}
		} catch (Exception e) {
			log.error("=====接口调用工具类报错=======",e);
			throw new RuntimeException(e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					br = null;
					throw new RuntimeException(e);
				}
			}
		}
		log.error("123456");
		log.error(resultBuffer.toString());
		return resultBuffer.toString();
	}

	public static String sendPostSRM(String url,String bodyStr,String serverName) {
		
		StringBuffer resultBuffer = null;
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		String requestId = 	UUID.randomUUID().toString();
		String requestId1 = UUID.randomUUID().toString();
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.addHeader("trackId", requestId);
		httpPost.addHeader("requestId", requestId1);
		httpPost.addHeader("serviceName", serverName);
		httpPost.addHeader("sourceSystem", "OA");
		//添加http头信息

		String encoding = null;  //username  password 自行修改  中间":"不可少
		try {
			encoding = DatatypeConverter.printBase64Binary("admin:admin".getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		httpPost.setHeader("Authorization", "Basic " + encoding);
		BufferedReader br = null;
		try {
			StringEntity entity = new StringEntity(bodyStr, "UTF-8");
			httpPost.setEntity(entity);
			HttpResponse response = client.execute(httpPost);
//			Header header = response.getFirstHeader("esbDesc");
//			String esbMessage=header.getValue();
//			log.error("======返回请求头信息===="+esbMessage);
			resultBuffer = new StringBuffer();
//			br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			InputStreamReader iis = new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8);
			br = new BufferedReader(iis);

			log.error("1234567");
			String temp;
			while((temp = br.
					readLine()) != null){
				resultBuffer.append(temp);
			}
			if("".equals(resultBuffer.toString())){
//				resultBuffer.append(esbMessage);
			}
		} catch (Exception e) {
			log.error("=====接口调用工具类报错=======",e);
			throw new RuntimeException(e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					br = null;
					throw new RuntimeException(e);
				}
			}
		}
		log.error("123456");
		log.error(resultBuffer.toString());
		return resultBuffer.toString();
	}

	/**
	 * @param url 访问地址
	 * @param headerMap  header 参数；可以通过下面工具类将string类型转换成map
	 * @param contentMap 需要传输参数参数；对象可以通过json转换成map
	 * @return 返回网页返回的数据
	 */
	public static String postMap(String url, Map<String, String> headerMap, Map<String, String> contentMap) {
		String result = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		List<NameValuePair> content = new ArrayList<NameValuePair>();
		//将content生成entity
		Iterator iterator = contentMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
			content.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
		}
		CloseableHttpResponse response = null;
		try {
			//循环增加header
			Iterator headerIterator = headerMap.entrySet().iterator();
			while (headerIterator.hasNext()) {
				Map.Entry<String, String> elem = (Map.Entry<String, String>) headerIterator.next();
				post.addHeader(elem.getKey(), elem.getValue());
			}
			if (content.size() > 0) {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(content, "UTF-8");
				post.setEntity(entity);
			}
			//发送请求并接收返回数据
			response = httpClient.execute(post);
			if (response != null && response.getStatusLine().getStatusCode() == 200) {
				//获取response的body部分
				HttpEntity entity = response.getEntity();
				//读取reponse的body部分并转化成字符串
				result = EntityUtils.toString(entity);
			}
			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return null;

	}

	public static void main(String[] args) throws Exception {

		String sr = postConnection("https://api.yonyouup.com/system/tradeid?from_account=ksfhsapi&app_key=opa0480610d92e342dc&token=32026d86a998473990991bae1a465991","","GET");
        //String sr = postConnection("http://127.0.0.1:7000/api/peixun/ryxx/getlastname2?workcode=test123","","POST");

        System.out.println(sr);
	}

}
