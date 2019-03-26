
package com.supadata.utils;

import sun.misc.BASE64Decoder;

import java.io.*;
import java.util.Properties;
import java.util.UUID;

/**
 * 类说明: 操作文件工具类
 * @ClassName: FileUtil
 * @author: pengxiuxiao
 * @date: 2016年12月29日 下午4:28:24
*/
public class FileUtil {
	 /**
     * 读取配置文件
     * @return value
     */
    public static String getProperValue(String key) {
    	
    	Properties prop=new Properties();
    	String value="";
    	try {
    		InputStream input = new FileInputStream(FileUtil.class.getClassLoader()
    				.getResource("config.properties").getPath());
    		InputStreamReader instr = new InputStreamReader(input, "utf-8");
    		prop.load(instr);
    		value = prop.getProperty(key);
    	} catch (Exception e) {
    		// TODO: handle exception
    		e.printStackTrace();
    	}
    	return value;
    }
	/**
	 * 生成随机数
	 * @return
	 */
	public static String getRandom(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	*
	* @Title: saveFile
	* @Description: TODO(保存文件)
	* @param @param str
	* @param @param path    设定文件
	* @return void    返回类型
	* @throws
	*/
	public static boolean saveFile(String file,String path,String fileName){
		boolean status = true;
		String str=file.substring(file.indexOf(",")+1);
		BASE64Decoder decoder=new BASE64Decoder();
		try {
			byte [] bs= decoder.decodeBuffer(str);
			for(int i=0;i<bs.length;i++){
				if(bs[i]<0){
					bs[i]+=256;
				}
			}
			if(createFolder(path)){
				OutputStream outputStream=new FileOutputStream(path+fileName);
				outputStream.write(bs);
				outputStream.flush();
				outputStream.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			status  =false;
		}
		return status;
	}
	
	/**
	 * 创建文件目录
	 * @param path
	 * @return
	 */
	public static boolean createFolder(String path){
		File file=new File(path);
		try {
			if(!file.exists()){
				file.mkdirs();
			}
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}
	
	
}
