
package com.supadata.utils;

import com.supadata.utils.secret.Des3;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * 类说明: 
 * @ClassName: StringUtil
 * @author: pengxiuxiao
 * @date: 2016年12月13日 下午2:09:44
*/
public class StringUtil {

	private static Des3 des = new Des3(FileUtil.getProperValue("dkey"));
	
	private static Logger logger = Logger.getLogger(StringUtil.class);
	/**
	 * 非空校验
	 * @param str
	 * @return 空值返回false
	 */
	public static boolean checkNull(String str) {
		if (str == null || str.length() <= 0) {
			return false;
		}else {
			return true;
		}
	}
	
	/**
	 * 生成随机数
	 * @return
	 */
	public static String getRandom(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 生成6位验证码
	 * @return
	 */
	public static String getVerificaCode(){
		Integer code = (int) (1000000 * Math.random());
		if (code < 1000000) {
			code = (int) (1000000 * Math.random());
		}
		return code.toString();
	}

	/**
	 * 获取真实ip地址
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) { 
	       String ip = request.getHeader("x-forwarded-for"); 
	       String xforwardedIp = request.getHeader("x-forwarded-for"); 
	       String proxyClientIp = request.getHeader("Proxy-Client-IP"); 
	       String WLProxyClientIp = request.getHeader("WL-Proxy-Client-IP");
	       String remoteAddr = request.getRemoteAddr(); 
	       
	       logger.info("xforwardedIp=" + xforwardedIp + ", proxyClientIp=" + proxyClientIp + ", WLProxyClientIp=" 
	       + WLProxyClientIp + ", remoteAddr" + remoteAddr);
	       
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	           ip = request.getHeader("Proxy-Client-IP"); 
	       } 
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	           ip = request.getHeader("WL-Proxy-Client-IP"); 
	       } 
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	           ip = request.getRemoteAddr(); 
	       } 
	       return ip.split(",")[0]; 
	   }
	

	
	
	
	public static void main(String[] args) {
		

	}

}
