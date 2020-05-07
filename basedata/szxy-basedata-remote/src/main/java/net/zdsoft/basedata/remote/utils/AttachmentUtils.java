package net.zdsoft.basedata.remote.utils;

import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class AttachmentUtils {

    /**
     * 根据附件图片文件截取部分，并保存截取文件
     * @param attachment
     * @param x
     * @param y
     * @param width
     * @param height
     * @param nameSuffix
     * @throws Exception
     */
	public static void saveCutPictrue(String dirPath, String filePath, int x, int y,
			int width, int height,String nameSuffix) throws Exception {
		File srcImg = getFileLocalPath(dirPath, filePath,
				null);
		
		String newImgPath = getAddSuffixName(filePath, nameSuffix);
		File newImg = getFileLocalPath(dirPath, newImgPath,
				null);
		cutImage(srcImg, newImg, x, y, width, height);
	}
	
	/**
	 * 根据源文件路径，获取加后缀的文件路径
	 * @param filePath
	 * @param nameSuffix
	 * @return
	 */
	public static String getAddSuffixName(String filePath,String nameSuffix){
		String filePathNoPostfix = filePath.substring(0, filePath.lastIndexOf("."));
		String Postfix = filePath.substring(filePath.lastIndexOf("."));
		String newPath = filePathNoPostfix + nameSuffix + Postfix;
		return newPath;
	}
	
	public static  void cutImage(File srcImg, File newImg, int x, int y,
			int width, int height)  throws RuntimeException {
		cutImage(srcImg, newImg, new java.awt.Rectangle(x, y, width, height));
	}

	private static String getExtension(String fileName) {
		if (StringUtils.isBlank(fileName)) {
			return null;
		}

		int pointIndex = fileName.lastIndexOf(".");
		return pointIndex > 0 && pointIndex < fileName.length() ? fileName.substring(pointIndex + 1).toLowerCase()
				: null;
	}

	/**
	 * <p>
	 * Title: cutImage
	 * </p>
	 * <p>
	 * Description: 根据原图与裁切size截取局部图片
	 * </p>
	 * 
	 * @param srcImg
	 *            源图片
	 * @param newImg
	 *            截取的图片
	 * @param rect
	 *            需要截取部分的坐标和大小
	 */
	public static  void cutImage(File srcImg, File newImg, Rectangle rect) throws RuntimeException {
		if (srcImg.exists()) {
			java.io.FileInputStream fis = null;
			ImageInputStream iis = null;
			try {
				fis = new FileInputStream(srcImg);
				// ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG,
				// JPEG, WBMP, GIF, gif]
				String types = Arrays.toString(ImageIO.getReaderFormatNames())
						.replace("]", ",");
				// 获取图片后缀
				String fileType = getExtension(srcImg.getName());
				// 类型和图片后缀全部小写，然后判断后缀是否合法
				if (fileType == null
						|| types.toLowerCase().indexOf(
								fileType.toLowerCase() + ",") < 0) {
					throw new RuntimeException("非图片类型文件");
				}
				// 将FileInputStream 转换为ImageInputStream
				iis = ImageIO.createImageInputStream(fis);
				// 根据图片类型获取该种类型的ImageReader
				ImageReader reader = ImageIO.getImageReadersBySuffix(fileType)
						.next();
				reader.setInput(iis, true);
				ImageReadParam param = reader.getDefaultReadParam();
				param.setSourceRegion(rect);
				BufferedImage bi = reader.read(0, param);
				ImageIO.write(bi, fileType, newImg);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fis != null)
						fis.close();
					if (iis != null)
						iis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			throw new RuntimeException("要截取的文件不存在！");
		}
	}

	/**
	 * 拷贝临时文件到实际路径，
	 * 
	 * @param srcFile
	 * @param newfile
	 * @throws RuntimeException
	 */
	public static  void moveFile(File srcFile, File newfile) throws RuntimeException {
		try {
			if (srcFile != null) {
				org.apache.commons.io.FileUtils.copyFile(srcFile, newfile);
			}
			// 删除临时文件
			srcFile.delete();
		} catch (Exception e) {
			// 在该处捕获IOException将其封装为UnchekedException抛出，表示文件上传失败
			throw new RuntimeException("由于网络或其他原因导致文件上传失败");
		}
	}
	/**
	 * 获取附件文件，位于服务器绝对路径
	 * 
	 * @param dirPath
	 * @param filePath
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static  File getFileLocalPath(String dirPath, String filePath,
			String fileName) throws IOException {
		if (null == fileName) {
			String fullDir = dirPath + File.separator + filePath;
			File file = new File(fullDir);
			return file;
		} else {
			String fullDir = dirPath + File.separator + filePath;
			File file = new File(fullDir);
			file.mkdirs();

			if (StringUtils.isNotBlank(fileName)) {
				file = new File(fullDir, fileName);
			}
			return file;
		}
	}
	
	public static void downloadByUrl(String fromUrl,File file) {
		URL url = null;
		try {
			url = new URL(fromUrl);
			DataInputStream dataInputStream = new DataInputStream(url.openStream());
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length;
			while ((length = dataInputStream.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			fileOutputStream.write(output.toByteArray());
			dataInputStream.close();
			fileOutputStream.close();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

