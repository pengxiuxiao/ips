package com.supadata.utils.secret;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;


/**
 * 3DES加密工具类
 * @author tzh
 * @version 1.0 
 * @since 1.0 
 */
public class Des3 {
	// 密钥
	private  String secretKey = "pengxiuxiaosupadata_test";

	// 向量
	private final  String iv = "01234567";
	// 加解密统一使用的编码方式
	private final  String encoding = "utf-8";

	public Des3(String secretKey)
	{
		this.secretKey = secretKey;
	}
	
	/**
	 * 3DES加密
	 * 
	 * @param plainText
	 *            普通文本
	 * @return
	 * @throws Exception
	 */
	public  String encode(String plainText) throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);

		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
		byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
		return Base64.encode(encryptData);
	}

	/**
	 * 3DES解密
	 * 
	 * @param encryptText
	 *            加密文本
	 * @return
	 * @throws Exception
	 */
	public  String decode(String encryptText) throws Exception {
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

		byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));

		return new String(decryptData, encoding);
	}
	
	
	
	public static void main(String[] args) {
//		Des3 des = new Des3("njxtqgjyptfromlianchuang");
		Des3 des = new Des3("pengxiuxiaosupadata_test");
		try {
			String test  = "454";
			System.out.println("加密前的字符：" + test);
			System.out.println("加密后的字符：" + des.encode(test));
			System.out.println("解密后的字符：" + des.decode(des.encode(test)));
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
