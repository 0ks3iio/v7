package net.zdsoft.studevelop.mobile.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.StorageFileUtils;
import net.zdsoft.framework.utils.StringUtils;

public class ImageUtils {
	
	public static String getImageUrl(String dirId, String filePath, String type){
		if(StringUtils.isBlank(dirId)){
			return Evn.getRequest().getContextPath()+"/mobile/open/studevelop/showImage?filePath="+filePath+"&type="+type;
		}else{
			return Evn.getRequest().getContextPath()+"/mobile/open/studevelop/showImage?dirId="+dirId+"&filePath="+filePath+"&type="+type;
		}
	}
	
	public static File orientationRotate(MultipartFile multfile){
		CommonsMultipartFile cf = (CommonsMultipartFile)multfile;
        //这个myfile是MultipartFile的  
        DiskFileItem fi = (DiskFileItem) cf.getFileItem();
        File file = fi.getStoreLocation();  
        orientationRotate(file, StorageFileUtils.getFileExtension(cf.getOriginalFilename()));
        return file;
	}
	
	public static void orientationRotate(File file,String fileEx){
		//处理图片旋转 TODO
		try {
			Metadata metadata = ImageMetadataReader.readMetadata(file);
			
			Directory exif = metadata.getDirectory(ExifIFD0Directory.class);
			if(exif!=null){
				if(exif.containsTag(ExifIFD0Directory.TAG_ORIENTATION)){
					int model = exif.getInt(ExifIFD0Directory.TAG_ORIENTATION);
					orientationRotate(model, file, fileEx);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
     * 对图片文件进行顺时针旋转90度
     * @param file
     * @param fileEx 文件名后缀
     * @param isClockwise 是否
     * @return
     * @throws Exception
     */
    public static void rotateImage(File file,String fileEx) throws Exception{
        BufferedImage bufferedimage = ImageIO.read(file);
        int w = bufferedimage.getWidth();
        int h = bufferedimage.getHeight();
        int type = bufferedimage.getColorModel().getTransparency();
        BufferedImage img;
        Graphics2D graphics2d;
        (graphics2d = (img = new BufferedImage(h, w, type)).createGraphics()).setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        //根据0，0坐标旋转	 试过非90度旋转 显示会有问题，所以这里写死90度了
        graphics2d.rotate(Math.toRadians(90), w/2 - (w-h)/2, h/2 );
//        graphics2d.rotate(Math.toRadians(90));
//        graphics2d.translate(0,-h);
//        graphics2d.rotate(Math.toRadians(-90));
//        graphics2d.translate(-w,0);
//        graphics2d.rotate(Math.toRadians(180));
//        graphics2d.translate(-w,-h);
//        graphics2d.drawImage(bufferedimage, 0, 0,w,h, null);
        graphics2d.drawImage(bufferedimage, 0, 0, null);
        graphics2d.dispose();
        if(StringUtils.isNotBlank(fileEx)){
        	ImageIO.write(img,fileEx,file);
        }else{
        	ImageIO.write(img,"jpg",file);
        }
    }
    /**
     * 根据orientation传入的参数进行图片旋转
     * @param model orientation传入的参数
     * @param file 
     * @param fileEx 文件后缀
     * @throws Exception
     */
    public static void orientationRotate(int model,File file,String fileEx) throws Exception{
    	/**
		 * 参数	处理
		 * 1	不旋转
		 * 3	顺时针180
		 * 6	顺时针90
		 * 8	顺时针270
		 * 2，4，5，7 功能类似 Photoshop 的水平翻转、垂直翻转，照像时不会出现的
		 */
		switch (model) {
            case 1: 
            	//System.out.println("Top, left side (Horizontal / normal)");
            	break;
            case 2: 
            	//System.out.println("Top, right side (Mirror horizontal)");
            	break;
            case 3: 
            	//System.out.println("Bottom, right side (Rotate 180)");
            	rotateImage(file,fileEx);
            	rotateImage(file,fileEx);
            	break;
            case 4: 
            	//System.out.println("Bottom, left side (Mirror vertical)");
            	break;
            case 5: 
            	//System.out.println("Left side, top (Mirror horizontal and rotate 270 CW)");
            	break;
            case 6: 
            	//System.out.println("Right side, top (Rotate 90 CW)");
            	rotateImage(file,fileEx);
            	break;
            case 7: 
            	//System.out.println("Right side, bottom (Mirror horizontal and rotate 90 CW)");
            	break;
            case 8: 
            	//System.out.println("Left side, bottom (Rotate 270 CW)");
            	rotateImage(file,fileEx);
            	rotateImage(file,fileEx);
            	rotateImage(file,fileEx);
            	break;
            default:break;
        }
    }
	
}
