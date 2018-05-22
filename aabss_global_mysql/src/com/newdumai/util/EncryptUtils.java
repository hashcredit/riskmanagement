package com.newdumai.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * Created by zhang on 2017/6/21.
 */
public class EncryptUtils {

    /**
     * DES加密+ Base64 编码(智汇接口用)
     *
     * @param encryptString
     * @param password
     * @return
     */
    public static String encrypt(String encryptString, String password) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            //创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            //Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            //用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            //现在，获取数据并加密
            //正式执行加密操作
            BASE64Encoder enc = new BASE64Encoder();
            return enc.encode(cipher.doFinal(encryptString.getBytes()));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * DES解密+ Base64 编码
     *
     * @param decryptString
     * @param password
     * @return
     */
    public static String decrypt(String decryptString, String password) {
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom random = new SecureRandom();
            // 创建一个DESKeySpec对象
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            // 创建一个密匙工厂
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // 将DESKeySpec对象转换成SecretKey对象
            SecretKey securekey = keyFactory.generateSecret(desKey);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance("DES");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            // 真正开始解密操作
            BASE64Decoder dec = new BASE64Decoder();
            byte[] decryptByte = cipher.doFinal(dec.decodeBuffer(decryptString));
            return new String(decryptByte, "UTF-8");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * MD5加密
     *
     * @param encryptString
     * @return
     */
    public static String md5(String encryptString) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(encryptString.getBytes("UTF-8"));
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            re_md5 = buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re_md5;
    }

    public static void main(String[] args) {
        String tt = "张三dfsfas";

        System.out.println(md5(tt));

    }
}
