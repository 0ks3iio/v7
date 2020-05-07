package net.zdsoft.careerplan.trade.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/**
 * 使用了google zxing作为二维码生成工具
 */
public class ZxingUtils {
	private static Log log = LogFactory.getLog(ZxingUtils.class);

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    
    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    private static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        Graphics2D g = image.createGraphics();		           
		/**读取Logo图片*/	 
        File logoFile=new File("G:/11/aliplogo.png");
		BufferedImage logo = ImageIO.read(logoFile);	            
		/** * 设置logo的大小,本人设置为二维码图片的20%,因为过大会盖掉二维码*/	            
		int widthLogo = logo.getWidth(null)>image.getWidth()*3/20?(image.getWidth()*3/20):logo.getWidth(null), 	                
		heightLogo = logo.getHeight(null)>image.getHeight()*3/20?(image.getHeight()*3/20):logo.getWidth(null);		            
		/**logo放在中心 */	             
		int x = (image.getWidth() - widthLogo) / 2;	             
		int y = (image.getHeight() - heightLogo) / 2;	             
		//开始绘制图片
		g.drawImage(logo, x, y, widthLogo, heightLogo, null);
		g.drawRoundRect(x, y, widthLogo, heightLogo, 15, 15);
		//g.setStroke(new BasicStroke(logoConfig.getBorder()));
		//g.setColor(logoConfig.getBorderColor());
		g.drawRect(x, y, widthLogo, heightLogo);            	
		g.dispose();            	            	
		logo.flush();
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }

    /** 将内容contents生成长宽均为width的图片，图片路径由imgPath指定
     */
    public static File getQRCodeImge(String contents, int width, String imgPath) {
        return getQRCodeImge(contents, width, width, imgPath);
    }

    /** 将内容contents生成长为width，宽为width的图片，图片路径由imgPath指定
     */
	public static File getQRCodeImge(String contents, int width, int height, String imgPath) {
		try {
            Map<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF8");

			BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, width, height, hints);

            File imageFile = new File(imgPath);
			writeToFile(bitMatrix, "png", imageFile);

            return imageFile;

		} catch (Exception e) {
			log.error("create QR code error!", e);
            return null;
		}
	}
	
	public static BufferedImage getQRCode(String contents, int width,File logoFile) throws WriterException, IOException {
		Map<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF8");
		BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, width, width, hints);
		BufferedImage image = toBufferedImage(bitMatrix);
		if(logoFile!=null && logoFile.exists()) {
			Graphics2D g = image.createGraphics();
			BufferedImage logo = ImageIO.read(logoFile);
			/** * 设置logo的大小,本人设置为二维码图片的10%,因为过大会盖掉二维码*/	            
			int widthLogo = logo.getWidth(null)>image.getWidth()*3/20?(image.getWidth()*3/20):logo.getWidth(null), 	                
			heightLogo = logo.getHeight(null)>image.getHeight()*3/20?(image.getHeight()*3/20):logo.getWidth(null);		            
			/**logo放在中心 */	             
			int x = (image.getWidth() - widthLogo) / 2;	             
			int y = (image.getHeight() - heightLogo) / 2;
			//开始绘制图片
			g.drawImage(logo, x, y, widthLogo, heightLogo, null);
			g.drawRoundRect(x, y, widthLogo, heightLogo, 15, 15);
			g.drawRect(x, y, widthLogo, heightLogo);            	
			g.dispose();   
			logo.flush();
		}
		return image;
	}
}
