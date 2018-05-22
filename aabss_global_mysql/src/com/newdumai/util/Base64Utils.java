package com.newdumai.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

public class Base64Utils {

    //把文件转换成字节数组
    public static byte[] getBytes(File f) throws Exception {
        FileInputStream in = new FileInputStream(f);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int n;
        while ((n = in.read(b)) != -1) {
            out.write(b, 0, n);
        }
        in.close();
        return out.toByteArray();
    }

    public static void main(String[] args) {
        Base64Utils t = new Base64Utils();
        //String str = t.GetImageStr("D:/My Documents/scriptxReg.zip");
        //String str = t.GetImageStr("D:/My Documents/ip.jpg");
        String str = t.getImageStr("D:/My Documents/哎呀妈呀哎呀妈呀真漂亮.mp3");
        System.out.println(str);
        //boolean flag = t.GenerateImage(str, "D:/My Documents/scriptxReg.zip");
        //boolean flag = t.GenerateImage(str, "D:/My Documents/ip.jpg");
        boolean flag = t.generateImage(str, "D:/My Documents/哎呀妈呀哎呀妈呀真漂亮B.mp3");
        System.out.println(flag);
    }

    /**
     * 将文件转化为字节数组字符串，并对其进行Base64编码处理
     *
     * @param imgFile
     * @return
     */
    public static String getImageStr(String imgFile) {
        InputStream in = null;
        byte[] data = null;
        // 读取文件字节数组  
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码  
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串  
        return encoder.encode(data);
    }

    /**
     * 根据字节数组字符串进行Base64解码并生成文件
     *
     * @param imgStr
     * @param savedImagePath
     * @return
     */
    public static boolean generateImage(String imgStr, String savedImagePath) {
        // 文件字节数组字符串数据为空  
        if (imgStr == null)
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码  
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                {// 调整异常数据  
                    if (b[i] < 0)
                        b[i] += 256;
                }
            }
            // 生成文件  
            // String sangImageStr = "D:/My Documents/ip.jpg" ;  // 要生成文件的路径.  
            OutputStream out = new FileOutputStream(savedImagePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
