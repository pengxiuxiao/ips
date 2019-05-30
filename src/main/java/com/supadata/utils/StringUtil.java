
package com.supadata.utils;

import com.supadata.constant.Config;
import com.supadata.utils.secret.Des3;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * 类说明: 
 * @ClassName: StringUtil
 * @author: pengxiuxiao
 * @date: 2016年12月13日 下午2:09:44
*/
public class StringUtil {

	@Autowired
	private Config config;
	
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



	/**
	 * 功能描述:16进制转为十进制 补齐空位
	 * @auther: pxx
	 * @param:
	 * @return:
	 * @date: 2019/5/29 19:15
	 */
	public static String HexToLongString(String data) {
		//读到卡的数据0047E325
		// TODO: 2018/1/29 其中有一个卡号是0047E325，这个卡号十进制值是4711205
		Long card = Long.parseLong(data.trim(), 16);          //16转10进制
		String cardTemp=String.valueOf(card);
		StringBuilder sb =new StringBuilder(cardTemp);
		int tempLength=sb.toString().length();
		if(tempLength!=10){
			switch (tempLength){
				case 6:
					sb.insert(0,"0000");
					break;
				case 7:
					sb.insert(0,"000");
					break;
				case 8:
					sb.insert(0,"00");
					break;
				case 9:
					sb.insert(0,"0");
					break;
			}
		}
		return sb.toString();
	}

	/**
	 * 功能描述: 翻转字符顺序
	 * @auther: pxx
	 * @param:
	 * @return:
	 * @date: 2019/5/29 19:14
	 */
	public static String overturnHexString(String data) {
		System.out.println(data.length());
		//01234567
		//12345678
		StringBuffer sb = new StringBuffer(data.substring(data.length()-2));
		sb = sb.append(new StringBuffer(data.substring(data.length()-4, data.length()-2)));
		sb = sb.append(new StringBuffer(data.substring(data.length()-6, data.length()-4)));
		sb = sb.append(new StringBuffer(data.substring(data.length()-8, data.length()-6)));

		return sb.toString();
	}
	
	
	public static void main(String[] args) {
		

	}

}
