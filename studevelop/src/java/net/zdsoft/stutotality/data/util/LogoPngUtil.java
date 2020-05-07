package net.zdsoft.stutotality.data.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LogoPngUtil {

    /**
     * 为图片添加图片文字
     * @param sourceImg 原图
     * @param targetPath 制作完成的图片路径、
     * @param png 图片名称
     * @param content   内容
     * @return
     * @throws IOException
     */
    public static File markImgMark(String sourceImg, String targetPath,String png,String content) throws IOException {

        GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fa=ge.getAvailableFontFamilyNames();

        File file = new File(getBlankPng(sourceImg,"blank.png"));
        Image img = ImageIO.read(file);
        int width = img.getWidth(null);//水印宽度
        int height = img.getHeight(null);//水印高
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        //字体
//        Font font = new Font("宋体",Font.PLAIN, 10);
//        g.setBackground(Color.WHITE);
//        g.fillRect(10, 10, 100, 10);
        g.setPaint(Color.BLACK);
        g.setColor(Color.BLACK);
        Font font = new Font("微软雅黑",Font.PLAIN, 10);
//        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics(font);

        FontRenderContext context = g.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(content, context);
        double x = (width - bounds.getWidth()) / 2;
        double y = (height - bounds.getHeight()) / 2;
        double ascent = -bounds.getY();
        double baseY = y + ascent;

        //绘制字符串
        g.drawString(content, (int)x, (int)baseY);
        File targetFile = new File(targetPath);
        if(!targetFile .exists()) {
            targetFile.mkdirs();
        }
        File sf = new File(targetPath+File.separator+png);
        ImageIO.write(bi, "png", sf); // 保存图片
        return sf;
    }

    public static String getBlankPng(String sourcePath,String pngName){
        File pathFile = new File(sourcePath);
        if(!pathFile.exists()){
            pathFile.mkdirs();
        }
        File file =new File(sourcePath+File.separator+pngName);
        if(!file.exists()){
            int width = 16;
            int height = 17;
            try {
                BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
                // 获取Graphics2D
                Graphics2D g2d = image.createGraphics();
               /* // ---------- 增加下面的代码使得背景透明 -----------------
                image = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
                g2d.dispose();
                g2d = image.createGraphics();*/
                g2d.setBackground(Color.WHITE);
                g2d.clearRect(0, 0, width, height);
                g2d.setPaint(Color.RED);
                // ---------- 背景透明代码结束 -----------------
                g2d.setColor(new Color(255,0,0));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawImage(image, 0, 0, null);
//                g2d.drawString("测试",0,0);
                //透明度设置 结束
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                //释放对象
                g2d.dispose();
                // 保存文件
                ImageIO.write(image, "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {

            }
        }
        return sourcePath+File.separator+pngName;
    }
}
