package com.newdumai.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;  
import net.coobird.thumbnailator.Thumbnails.Builder;  
import net.coobird.thumbnailator.geometry.Positions;  

public class ImageUtils {
    public static String IMAGE_TYPE_GIF = "gif";// 图形交换格式
    public static String IMAGE_TYPE_JPG = "jpg";// 联合照片专家组
    public static String IMAGE_TYPE_JPEG = "jpeg";// 联合照片专家组
    public static String IMAGE_TYPE_BMP = "bmp";// 英文Bitmap（位图）的简写，它是Windows操作系统中的标准图像文件格式
    public static String IMAGE_TYPE_PNG = "png";// 可移植网络图形
    public static String IMAGE_TYPE_PSD = "psd";// Photoshop的专用格式Photoshop
  
    /*
     * Java图片缩略图裁剪水印缩放旋转压缩转格式-Thumbnailator图像处理
     * http://blog.csdn.net/chenleixing/article/details/44685817
     * Thumbnailator 是一个优秀的图片处理的Google开源Java类库。处理效果远比Java API的好。从API提供现有的图像文件和图像对象的类中简化了处理过程，两三行代码就能够从现有图片生成处理后的图片，且允许微调图片的生成方式，同时保持了需要写入的最低限度的代码量。还支持对一个目录的所有图片进行批量处理操作。
	支持的处理操作：图片缩放，区域裁剪，水印，旋转，保持比例。
	另外值得一提的是，Thumbnailator至今仍不断更新，怎么样，感觉很有保障吧！
	 
     */
    public void testHandlePicture() throws IOException{  
        
    	//1、指定大小进行缩放
    	//size(宽度, 高度)  
    	/* 
    	 * 若图片横比200小，高比300小，不变 
    	 * 若图片横比200小，高比300大，高缩小到300，图片比例不变 
    	 * 若图片横比200大，高比300小，横缩小到200，图片比例不变 
    	 * 若图片横比200大，高比300大，图片按比例缩小，横为200或高为300 
    	 */  
    	Thumbnails.of("images/a380_1280x1024.jpg")   
    	    .size(200, 300)  
    	    .toFile("c:/a380_200x300.jpg");  
    	  
    	Thumbnails.of("images/a380_1280x1024.jpg")  
    	    .size(2560, 2048)  
    	    .toFile("c:/a380_2560x2048.jpg");  

    	//2、按照比例进行缩放
    	//scale(比例)  
    	Thumbnails.of("images/a380_1280x1024.jpg")  
    	    .scale(0.25f)  
    	    .toFile("c:/a380_25%.jpg");  
    	  
    	Thumbnails.of("images/a380_1280x1024.jpg")  
    	    .scale(1.10f)  
    	    .toFile("c:/a380_110%.jpg");  
    	 
    	//3、不按照比例，指定大小进行缩放
    	//keepAspectRatio(false)默认是按照比例缩放的  
    	Thumbnails.of("images/a380_1280x1024.jpg")  
    	    .size(200,200)  
    	    .keepAspectRatio(false)  
    	    .toFile("c:/a380_200x200.jpg");  

    	//4、旋转
    	//rotate(角度),正数：顺时针负数：逆时针  
    	Thumbnails.of("images/a380_1280x1024.jpg")  
    	    .size(1280,1024)  
    	    .rotate(90)  
    	    .toFile("c:/a380_rotate+90.jpg");  
    	  
    	Thumbnails.of("images/a380_1280x1024.jpg")  
    	    .size(1280,1024)  
    	    .rotate(-90)  
    	    .toFile("c:/a380_rotate-90.jpg");  



    	//5、水印
    	
    	//watermark(位置，水印图，透明度)  
    	Thumbnails.of("images/a380_1280x1024.jpg")  
    	    .size(1280,1024)  
    	    .watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File("images/watermark.png")),0.5f)  
    	    .outputQuality(0.8f)  
    	    .toFile("c:/a380_watermark_bottom_right.jpg");  
    	  
    	Thumbnails.of("images/a380_1280x1024.jpg")  
    	    .size(1280,1024)  
    	    .watermark(Positions.CENTER,ImageIO.read(new File("images/watermark.png")),0.5f)  
    	    .outputQuality(0.8f)  
    	    .toFile("c:/a380_watermark_center.jpg");  

    	//6、裁剪
    	//sourceRegion()  
    	  
    	//图片中心400*400的区域  
    	Thumbnails.of("images/a380_1280x1024.jpg")  
    	    .sourceRegion(Positions.CENTER,400,400)  
    	    .size(200,200)  
    	    .keepAspectRatio(false)  
    	    .toFile("c:/a380_region_center.jpg");  
    	  
    	//图片右下400*400的区域  
    	Thumbnails.of("images/a380_1280x1024.jpg")  
    	    .sourceRegion(Positions.BOTTOM_RIGHT,400,400)  
    	    .size(200,200)  
    	    .keepAspectRatio(false)  
    	    .toFile("c:/a380_region_bootom_right.jpg");  
    	  
    	//指定坐标  
    	Thumbnails.of("images/a380_1280x1024.jpg")  
    	    .sourceRegion(600,500,400,400)  
    	    .size(200,200)  
    	    .keepAspectRatio(false)  
    	    .toFile("c:/a380_region_coord.jpg");  





    	//7、转化图像格式
    	//outputFormat(图像格式)  
    	Thumbnails.of("images/a380_1280x1024.jpg")  
    	    .size(1280,1024)  
    	    .outputFormat("png")  
    	    .toFile("c:/a380_1280x1024.png");  
    	  
    	Thumbnails.of("images/a380_1280x1024.jpg")  
    	    .size(1280,1024)  
    	    .outputFormat("gif")  
    	    .toFile("c:/a380_1280x1024.gif");  
    	 
    	//8、输出到OutputStream
    	
    	
    	
    	//toOutputStream(流对象)  
    	  OutputStream os=new FileOutputStream("c:/a380_1280x1024_OutputStream.png");  
    	Thumbnails.of("images/a380_1280x1024.jpg")  
    	    .size(1280,1024)  
    	    .toOutputStream(os);  
    	 
    	// 9、输出到BufferedImage
     
    	//asBufferedImage()返回BufferedImage  
    	BufferedImage thumbnail=Thumbnails.of("images/a380_1280x1024.jpg")  
    	    .size(1280,1024)  
    	    .asBufferedImage();  
    	ImageIO.write(thumbnail,"jpg",new File("c:/a380_1280x1024_BufferedImage.jpg")); 
      
      
      
      //--------压缩至指定图片尺寸，保持图片不变形，多余部分裁剪掉--------------------------

    //压缩至指定图片尺寸（例如：横400高300），保持图片不变形，多余部分裁剪掉(这个是引的网友的代码)  
    	String fromPic="";
      BufferedImage image = ImageIO.read( new File("c:/a380_1280x1024_BufferedImage.jpg"));  
      Builder<BufferedImage> builder = null;  
        
      int imageWidth = image.getWidth();  
      int imageHeitht = image.getHeight();  
      if ((float)300 / 400 != (float)imageWidth / imageHeitht) {  
          if (imageWidth > imageHeitht) {  
              image = Thumbnails.of(fromPic).height(300).asBufferedImage();  
          } else {  
              image = Thumbnails.of(fromPic).width(400).asBufferedImage();  
          }  
          builder = Thumbnails.of(image).sourceRegion(Positions.CENTER, 400, 300).size(400, 300);  
      } else {  
          builder = Thumbnails.of(image).size(400, 300);  
      }  
      builder.outputFormat("jpg").toFile( new File("c:/toPic_1280x1024_BufferedImage.jpg") );  
      
    //--------压缩至指定图片尺寸，保持图片不变形，多余部分裁剪掉--------------------------
      
      
    }
    
    /**
     * 本地获取
     * */
      public void getFileImg() throws IOException{
           File picture = new File("C:/Users/aflyun/Pictures/Camera Roll/1.jpg");
           BufferedImage sourceImg =ImageIO.read(new FileInputStream(picture)); 
           System.out.println(String.format("%.1f",picture.length()/1024.0));// 源图大小
           System.out.println(sourceImg.getWidth()); // 源图宽度
           System.out.println(sourceImg.getHeight()); // 源图高度
    }
       /**
     * 获取服务器上的
     * @throws FileNotFoundException
     * @throws IOException
     * vFileUrl="http://img.mall.tcl.com/dev1/0/000/148/0000148235.fid"
     */
    public void getURLImg(String vFileUrl) throws FileNotFoundException, IOException{

        URL url = new URL(vFileUrl);
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        BufferedImage image = ImageIO.read(connection.getInputStream());  
        int srcWidth = image .getWidth();      // 源图宽度
        int srcHeight = image .getHeight();    // 源图高度

        System.out.println("srcWidth = " + srcWidth);
        System.out.println("srcHeight = " + srcHeight);

    }
    
	//============================================= 
    public static void main(String[] args) {
        // 1-缩放图像：
        // 方法一：按比例缩放
        ImageUtils.scale("e:/abc.jpg", "e:/abc_scale.jpg", 2, true);//测试OK
        // 方法二：按高度和宽度缩放
        ImageUtils.scale2("e:/abc.jpg", "e:/abc_scale2.jpg", 500, 300, true);//测试OK

        // 2-切割图像：
        // 方法一：按指定起点坐标和宽高切割
        ImageUtils.cut("e:/abc.jpg", "e:/abc_cut.jpg", 0, 0, 400, 400 );//测试OK
        // 方法二：指定切片的行数和列数
        ImageUtils.cut2("e:/abc.jpg", "e:/", 2, 2 );//测试OK
        // 方法三：指定切片的宽度和高度
        ImageUtils.cut3("e:/abc.jpg", "e:/", 300, 300 );//测试OK

        // 3-图像类型转换：
        ImageUtils.convert("e:/abc.jpg", "GIF", "e:/abc_convert.gif");//测试OK


        // 4-彩色转黑白：
        ImageUtils.gray("e:/abc.jpg", "e:/abc_gray.jpg");//测试OK


        // 5-给图片添加文字水印：
        // 方法一：
        ImageUtils.pressText("我是水印文字","e:/abc.jpg","e:/abc_pressText.jpg","宋体",Font.BOLD,Color.white,80, 0, 0, 0.5f);//测试OK
        // 方法二：
        ImageUtils.pressText2("我也是水印文字", "e:/abc.jpg","e:/abc_pressText2.jpg", "黑体", 36, Color.white, 80, 0, 0, 0.5f);//测试OK
        
        // 6-给图片添加图片水印：
        ImageUtils.pressImage("e:/abc2.jpg", "e:/abc.jpg","e:/abc_pressImage.jpg", 0, 0, 0.5f);//测试OK
    }


   
    public final static void scale(String srcImageFile, String result,
            int scale, boolean flag) {
        try {
            BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
            int width = src.getWidth(); // 得到源图宽
            int height = src.getHeight(); // 得到源图长
            if (flag) {// 放大
                width = width * scale;
                height = height * scale;
            } else {// 缩小
                width = width / scale;
                height = height / scale;
            }
            Image image = src.getScaledInstance(width, height,
                    Image.SCALE_DEFAULT);
            BufferedImage tag = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


   
    public final static void scale2(String srcImageFile, String result, int height, int width, boolean bb) {
        try {
            double ratio = 0.0; // 缩放比例
            File f = new File(srcImageFile);
            BufferedImage bi = ImageIO.read(f);
            Image itemp = bi.getScaledInstance(width, height, bi.SCALE_SMOOTH);
            // 计算比例
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                if (bi.getHeight() > bi.getWidth()) {
                    ratio = (new Integer(height)).doubleValue()
                            / bi.getHeight();
                } else {
                    ratio = (new Integer(width)).doubleValue() / bi.getWidth();
                }
                AffineTransformOp op = new AffineTransformOp(AffineTransform
                        .getScaleInstance(ratio, ratio), null);
                itemp = op.filter(bi, null);
            }
            if (bb) {//补白
                BufferedImage image = new BufferedImage(width, height,
                        BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, width, height);
                if (width == itemp.getWidth(null))
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                else
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                g.dispose();
                itemp = image;
            }
            ImageIO.write((BufferedImage) itemp, "JPEG", new File(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
   
    public final static void cut(String srcImageFile, String result,
            int x, int y, int width, int height) {
        try {
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getHeight(); // 源图宽度
            int srcHeight = bi.getWidth(); // 源图高度
            if (srcWidth > 0 && srcHeight > 0) {
                Image image = bi.getScaledInstance(srcWidth, srcHeight,
                        Image.SCALE_DEFAULT);
                // 四个参数分别为图像起点坐标和宽高
                // 即: CropImageFilter(int x,int y,int width,int height)
                ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
                Image img = Toolkit.getDefaultToolkit().createImage(
                        new FilteredImageSource(image.getSource(),
                                cropFilter));
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.drawImage(img, 0, 0, width, height, null); // 绘制切割后的图
                g.dispose();
                // 输出为文件
                ImageIO.write(tag, "JPEG", new File(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
   
    public final static void cut2(String srcImageFile, String descDir,
            int rows, int cols) {
        try {
            if(rows<=0||rows>20) rows = 2; // 切片行数
            if(cols<=0||cols>20) cols = 2; // 切片列数
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getHeight(); // 源图宽度
            int srcHeight = bi.getWidth(); // 源图高度
            if (srcWidth > 0 && srcHeight > 0) {
                Image img;
                ImageFilter cropFilter;
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                int destWidth = srcWidth; // 每张切片的宽度
                int destHeight = srcHeight; // 每张切片的高度
                // 计算切片的宽度和高度
                if (srcWidth % cols == 0) {
                    destWidth = srcWidth / cols;
                } else {
                    destWidth = (int) Math.floor(srcWidth / cols) + 1;
                }
                if (srcHeight % rows == 0) {
                    destHeight = srcHeight / rows;
                } else {
                    destHeight = (int) Math.floor(srcWidth / rows) + 1;
                }
                // 循环建立切片
                // 改进的想法:是否可用多线程加快切割速度
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        // 四个参数分别为图像起点坐标和宽高
                        // 即: CropImageFilter(int x,int y,int width,int height)
                        cropFilter = new CropImageFilter(j * destWidth, i * destHeight,
                                destWidth, destHeight);
                        img = Toolkit.getDefaultToolkit().createImage(
                                new FilteredImageSource(image.getSource(),
                                        cropFilter));
                        BufferedImage tag = new BufferedImage(destWidth,
                                destHeight, BufferedImage.TYPE_INT_RGB);
                        Graphics g = tag.getGraphics();
                        g.drawImage(img, 0, 0, null); // 绘制缩小后的图
                        g.dispose();
                        // 输出为文件
                        ImageIO.write(tag, "JPEG", new File(descDir
                                + "_r" + i + "_c" + j + ".jpg"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


   
    public final static void cut3(String srcImageFile, String descDir,
            int destWidth, int destHeight) {
        try {
            if(destWidth<=0) destWidth = 200; // 切片宽度
            if(destHeight<=0) destHeight = 150; // 切片高度
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getHeight(); // 源图宽度
            int srcHeight = bi.getWidth(); // 源图高度
            if (srcWidth > destWidth && srcHeight > destHeight) {
                Image img;
                ImageFilter cropFilter;
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                int cols = 0; // 切片横向数量
                int rows = 0; // 切片纵向数量
                // 计算切片的横向和纵向数量
                if (srcWidth % destWidth == 0) {
                    cols = srcWidth / destWidth;
                } else {
                    cols = (int) Math.floor(srcWidth / destWidth) + 1;
                }
                if (srcHeight % destHeight == 0) {
                    rows = srcHeight / destHeight;
                } else {
                    rows = (int) Math.floor(srcHeight / destHeight) + 1;
                }
                // 循环建立切片
                // 改进的想法:是否可用多线程加快切割速度
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        // 四个参数分别为图像起点坐标和宽高
                        // 即: CropImageFilter(int x,int y,int width,int height)
                        cropFilter = new CropImageFilter(j * destWidth, i * destHeight,
                                destWidth, destHeight);
                        img = Toolkit.getDefaultToolkit().createImage(
                                new FilteredImageSource(image.getSource(),
                                        cropFilter));
                        BufferedImage tag = new BufferedImage(destWidth,
                                destHeight, BufferedImage.TYPE_INT_RGB);
                        Graphics g = tag.getGraphics();
                        g.drawImage(img, 0, 0, null); // 绘制缩小后的图
                        g.dispose();
                        // 输出为文件
                        ImageIO.write(tag, "JPEG", new File(descDir
                                + "_r" + i + "_c" + j + ".jpg"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


   
    public final static void convert(String srcImageFile, String formatName, String destImageFile) {
        try {
            File f = new File(srcImageFile);
            f.canRead();
            f.canWrite();
            BufferedImage src = ImageIO.read(f);
            ImageIO.write(src, formatName, new File(destImageFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


   
    public final static void gray(String srcImageFile, String destImageFile) {
        try {
            BufferedImage src = ImageIO.read(new File(srcImageFile));
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
            ColorConvertOp op = new ColorConvertOp(cs, null);
            src = op.filter(src, null);
            ImageIO.write(src, "JPEG", new File(destImageFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


   
    public final static void pressText(String pressText,
            String srcImageFile, String destImageFile, String fontName,
            int fontStyle, Color color, int fontSize,int x,
            int y, float alpha) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            g.setColor(color);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            // 在指定坐标绘制水印文字
            g.drawString(pressText, (width - (getLength(pressText) * fontSize))
                    / 2 + x, (height - fontSize) / 2 + y);
            g.dispose();
            ImageIO.write((BufferedImage) image, "JPEG", new File(destImageFile));// 输出到文件流
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


   
    public final static void pressText2(String pressText, String srcImageFile,String destImageFile,
            String fontName, int fontStyle, Color color, int fontSize, int x,
            int y, float alpha) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            g.setColor(color);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            // 在指定坐标绘制水印文字
            g.drawString(pressText, (width - (getLength(pressText) * fontSize))
                    / 2 + x, (height - fontSize) / 2 + y);
            g.dispose();
            ImageIO.write((BufferedImage) image, "JPEG", new File(destImageFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


   
    public final static void pressImage(String pressImg, String srcImageFile,String destImageFile,
            int x, int y, float alpha) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);
            // 水印文件
            Image src_biao = ImageIO.read(new File(pressImg));
            int wideth_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            g.drawImage(src_biao, (wideth - wideth_biao) / 2,
                    (height - height_biao) / 2, wideth_biao, height_biao, null);
            // 水印文件结束
            g.dispose();
            ImageIO.write((BufferedImage) image,  "JPEG", new File(destImageFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


   
    public final static int getLength(String text) {
        int length = 0;
        for (int i = 0; i < text.length(); i++) {
            if (new String(text.charAt(i) + "").getBytes().length > 1) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return length / 2;
    }
}