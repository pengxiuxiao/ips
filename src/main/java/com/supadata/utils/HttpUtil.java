package com.supadata.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.protocol.HTTP;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;


/**
 * 模拟HTTP请求工具类
 * 
 * @author tzh
 * @version 1.0 
 * @since 1.0 
 * */
public class HttpUtil {

	private static final int CONNECTION_TIMEOUT = 20000;
	private static final String CHARCODE="UTF-8"; 

	public HttpUtil() {

	}

	/**
	 * Using GET method.
	 * 
	 * @param url
	 *            The remote URL.
	 * @param queryString
	 *            The query string containing parameters
	 * @return Response string.
	 * @throws Exception
	 */
	public static String httpGet(String url, String queryString) throws Exception {
		String responseData = null;

		if (queryString != null && !queryString.equals("")) {
			url += "?" + queryString;
		}

		HttpClient httpClient = new HttpClient();
		GetMethod httpGet = new GetMethod(url);
		httpGet.getParams().setParameter("http.socket.timeout",
				new Integer(CONNECTION_TIMEOUT));

		try {
			int statusCode = httpClient.executeMethod(httpGet);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("HttpGet Method failed: "
						+ httpGet.getStatusLine());
			}
			// Read the response body.
			responseData = httpGet.getResponseBodyAsString();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			httpGet.releaseConnection();
			httpClient = null;
		}

		return responseData;
	}

	/**
	 * Using POST method.
	 * 
	 * @param url
	 *            The remote URL.
	 * @param queryString
	 *            The query string containing parameters
	 * @return Response string.
	 * @throws Exception
	 */
	public static String httpPost(String url, String queryString) throws Exception {
		String responseData = null;
		HttpClient httpClient = new HttpClient();
		PostMethod httpPost = new PostMethod(url);
		httpPost.addParameter("Content-Type",
				"application/x-www-form-urlencoded");
		httpPost.getParams().setParameter("http.socket.timeout",
				new Integer(CONNECTION_TIMEOUT));

		if (queryString != null && !queryString.equals("")) {
//			httpPost.setRequestEntity(new ByteArrayRequestEntity(queryString.getBytes()));
			httpPost.setRequestEntity(new ByteArrayRequestEntity(queryString.getBytes("UTF-8")));
		}

		try {
			int statusCode = httpClient.executeMethod(httpPost);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("HttpPost Method failed: "
						+ httpPost.getStatusLine());
			}
			responseData = httpPost.getResponseBodyAsString();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			httpPost.releaseConnection();
			httpClient = null;
		}

		return responseData;
	}
	
	public static String doPost(String reqUrl, Map<String, String> parameters,String charset) {
		HttpURLConnection urlConn = null;
		try {
			urlConn = sendPost(reqUrl, parameters);
			String responseContent = getContent(urlConn,charset);
			return responseContent.trim();
		}catch(Exception e){
			return null;
		}finally {
			if (urlConn != null) {
				urlConn.disconnect();
				urlConn = null;
			}
		}
	}	
	public static String doPost(String reqUrl, Map<String, String> parameters) {
		return doPost(reqUrl,parameters,CHARCODE);
	}
	
	public static String doGet(String reqUrl, Map<String, String> parameters,String charset) {
		HttpURLConnection urlConn = null;
		try {
			urlConn = sendGet(reqUrl, parameters);
			String responseContent = getContent(urlConn,charset);
			return responseContent.trim();
		}catch(Exception e){
			return null;
		}finally {
			if (urlConn != null) {
				urlConn.disconnect();
				urlConn = null;
			}
		}
	}	
	public static String doGet(String reqUrl, Map<String, String> parameters) {
		return doGet(reqUrl,parameters,CHARCODE);
	}
	
	private static String getContent(HttpURLConnection urlConn,String charset) {
		int code;		
		try {
			code = urlConn.getResponseCode();
			
			String responseContent = null;
			InputStream in = null;
			if (code == HttpURLConnection.HTTP_OK) {
				in = urlConn.getInputStream();
			}else{
				in = urlConn.getErrorStream(); 
			}			
			BufferedReader rd = new BufferedReader(new InputStreamReader(in,
					charset));
			String tempLine = rd.readLine();
			StringBuffer tempStr = new StringBuffer();
			String crlf = System.getProperty("line.separator");
			while (tempLine != null) {
				tempStr.append(tempLine);
				tempStr.append(crlf);
				tempLine = rd.readLine();
			}
			responseContent = tempStr.toString();
			rd.close();
			in.close();
			return responseContent;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private static HttpURLConnection sendPost(String reqUrl,
			Map<String, String> parameters) {
		HttpURLConnection urlConn = null;
		try {
			String params = generatorParamString(parameters);
			String urlStr=reqUrl+"?"+generatorParamString(parameters);
			System.out.println("请求的完整地址为：" + urlStr);
			URL url = new URL(reqUrl);
			System.out.println(url);
			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestMethod("POST");
			//urlConn.setConnectTimeout(50000);
			//urlConn.setReadTimeout(50000);
			urlConn.setDoOutput(true);
			byte[] b = params.getBytes();
			urlConn.getOutputStream().write(b, 0, b.length);
			urlConn.getOutputStream().flush();
			urlConn.getOutputStream().close();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return urlConn;
	}

	private static HttpURLConnection sendGet(String reqUrl,
			Map<String, String> parameters) {
		HttpURLConnection urlConn = null;
		String urlStr=reqUrl+"?"+generatorParamString(parameters);
		System.out.println("请求的完整地址为：" + urlStr);
		try {			
			URL url = new URL(urlStr);
			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestMethod("GET");
			urlConn.setConnectTimeout(50000);
			urlConn.setReadTimeout(50000);
			urlConn.setDoInput(true); 
			urlConn.setDoOutput(true);			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return urlConn;
	}
	
	public static String generatorParamString(Map<String, String> parameters) {
		StringBuffer params = new StringBuffer();
		if (parameters != null) {
			for (Iterator<String> iter = parameters.keySet().iterator(); iter
					.hasNext();) {
				String name = iter.next();
				String value = parameters.get(name);
				params.append(name + "=");
				try {
					params.append(URLEncoder.encode(value, CHARCODE));
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (Exception e) {
					String message = String.format("'%s'='%s'", name, value);
					throw new RuntimeException(message, e);
				}
				if (iter.hasNext()) {
					params.append("&");
				}
			}
		}
		return params.toString();
	}
	
	/**
	 * 获取post请求的json参数
	 * @param request
	 * @return
	 */
	public static String getPostParameter(HttpServletRequest request) {
		String requestBody = "";
		 try {
	        	// 读取请求内容
	        	BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
	        	String line = null;
	        	StringBuilder sb = new StringBuilder();
	        	while((line = br.readLine())!=null){
	        		sb.append(line);
	        	}
	        	// 将资料解码
	        	String reqBody = sb.toString();
	        	requestBody = URLDecoder.decode(reqBody, HTTP.UTF_8);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return requestBody;
	}
}
