package com.newdumai.setting.interface_source.in_interface.tianchuang.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加解密工具
 * 
 * @author lei.sun-1
 * @date 2015年12月21日
 */
public class AESUtil {
	/**
	 * 加密
	 * 
	 * @param key
	 *            128｜196｜256位密钥
	 * @param data
	 *            明文
	 * @return 密文
	 */
	public static String encode(String key, String data) {
		try {
			SecretKey secretKey = new SecretKeySpec(hex2bin(key), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] bytes = cipher.doFinal(data.getBytes("UTF-8"));
			return bin2hex(bytes);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 解密
	 * 
	 * @param key
	 *            128｜196｜256位密钥
	 * @param data
	 *            密文
	 * @return 明文
	 */
	public static String decode(String key, String data) {
		try {
			SecretKey secretKey = new SecretKeySpec(hex2bin(key), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] bytes = cipher.doFinal(hex2bin(data));
			return new String(bytes);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将2进制转换成16进制
	 * 
	 * @param bin
	 *            2进制数组
	 * @return 16进制字符串
	 */
	private static String bin2hex(byte[] bin) throws Exception {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < bin.length; i++) {
			String hex = Integer.toHexString(bin[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			buf.append(hex.toUpperCase());
		}
		return buf.toString();
	}

	/**
	 * 将16进制转换为2进制
	 * 
	 * @param hex
	 *            16进制字符串
	 * @return 2进制数组
	 */
	private static byte[] hex2bin(String hex) throws Exception {
		if (hex.length() < 1)
			return null;
		byte[] result = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length() / 2; i++) {
			int high = Integer.parseInt(hex.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hex.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}
}
