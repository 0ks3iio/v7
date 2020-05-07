package net.zdsoft.framework.utils;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.Assert;

/**
 * Created by shenke on 2016/12/19.
 * 图片裁剪缩放，工具类
 */
public final class JsCropUtils {

    /**
     *  缩放图片
     * @param src   源图片
     * @param scale 缩放比例
     * @return
     */
    public static final BufferedImage zoom(BufferedImage src,double scale){
        Assert.notNull(src);
        if(scale == 1){
            return src;
        }
        Graphics g = null;

        BufferedImage image = new BufferedImage((int)(src.getWidth()*scale),(int)(src.getHeight()*scale),BufferedImage.TYPE_3BYTE_BGR);
        g = image.getGraphics();
        try {
            Image scaleImage =  src.getScaledInstance((int)(src.getWidth()*scale),(int)(src.getHeight()*scale),Image.SCALE_SMOOTH);
            g.drawImage(scaleImage,0,0,image.getWidth(),image.getHeight(),null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 缩放图片</br>
     * 若目标图片的宽度都
     * @param src
     * @param descWidth  目标宽度
     * @param descHeight 目标高度
     * @return
     */
    public static final BufferedImage zoom(BufferedImage src,int descWidth, int descHeight){
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        double scale = 1/getScale(srcWidth,srcHeight,descWidth,descHeight);
        return zoom(src,scale);
    }

    public static final BufferedImage zoom(byte[] bytes,int descWidth, int descHeight){
        return zoom(JsCropUtils.read(bytes),descWidth,descHeight);
    }

    public static final BufferedImage zoom(byte[] bytes,double scale){
        return zoom(JsCropUtils.read(bytes),scale);
    }
    public static final BufferedImage zoom(File file,int descWidth, int descHeight){
        try {
            Assert.notNull(file);
            byte[] bytes = ArrayUtils.EMPTY_BYTE_ARRAY;
            bytes = FileUtils.readFileToByteArray(file);
            return zoom(bytes,descWidth,descHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取缩放比
     * @param srcWidth
     * @param srcHeight
     * @param descWidth
     * @param descHeight
     * @return
     */
    public static double getScale(int srcWidth, int srcHeight, int descWidth, int descHeight){
        double widthScale = (double) srcWidth/(double) descWidth;
        double heightScale = (double) srcHeight/(double)descHeight;

        if(widthScale<1 && heightScale<1){
            return 1;
        }else if(widthScale<1 && heightScale>1){
            return heightScale;
        }else if(widthScale>1 && heightScale<1){
            return widthScale;
        }else {
            return widthScale>heightScale?widthScale:heightScale;
        }
    }

    /**
     * 裁剪照片
     * @param src 源图片
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static final BufferedImage cut(BufferedImage src , int x1, int y1, int x2, int y2){
        Assert.notNull(src);
        int descW = Math.abs(x2 - x1);
        int descH = Math.abs(y2 - y1);
        BufferedImage image = new BufferedImage(descW,descH,BufferedImage.TYPE_3BYTE_BGR);
        Graphics g = image.getGraphics();
        g.drawImage(src,0,0,descW,descH,x1,y1,x2,y2,null);
        return image;
    }

    public static final BufferedImage cut100(BufferedImage src){
        BufferedImage image = new BufferedImage(100,100,BufferedImage.TYPE_3BYTE_BGR);
        Graphics g = image.getGraphics();
        g.drawImage(src,0,0,100,100,null);
        return image;
    }

    /**
     * 获取图片宽度
     * @param bytes
     * @return
     */
    public static final int getRealWidth(byte[] bytes){
        int width = 0;
        try {
            width = ImageIO.read(new ByteArrayInputStream(bytes)).getWidth();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return width;
    }

    /**
     * 获取图片高度
     * @param bytes
     * @return
     */
    public static final int getRealHeight(byte[] bytes){
        int height = 0;
        try {
            height = ImageIO.read(new ByteArrayInputStream(bytes)).getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return height;
    }

    public static final BufferedImage read(byte[] bytes){
        try {
            return ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final void write(HttpServletResponse response, BufferedImage image){

    }

    public static byte[] toBytes(BufferedImage image){
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image,"png",out);
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
