package net.zdsoft.desktop.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilePermission;
import java.io.IOException;

/**
 * @author ke_shen@126.com
 * @since 2018/1/11 下午2:55
 */
public final class WebFileUtils {

	private static Logger logger = LoggerFactory.getLogger(WebFileUtils.class);

	/**
	 * filePath不能为null
	 * 判断绝对路径下的文件是否存在
	 * @param filePath 绝对路径
	 * @return true 存在 false 不存在
	 */
	public static boolean existsOfAbsolutePath(String filePath) {
		File tempFile = new File(filePath);
		return tempFile.exists();
	}

	/***/
	public static void authorFile(String absolutePath, int permission) {
		//windows 操作系统
		if (SystemUtil.getOsInfo().isWindows()) {
			//not support
			logger.error("not support os[{}]", SystemUtil.getOsInfo().getName());
		}
		//OS X Linux Unix 或者类Unix
		else {
			try {
				File file = new File(absolutePath);
				if (!file.exists()) {
					return ;
				}
				Runtime.getRuntime().exec("chmod -R " + permission + " " + absolutePath);
			} catch (IOException e) {
				logger.error("文件授权失败 {}", e.getMessage());
			}
		}
	}

	public static boolean createDirs(File file) {
		return file != null && (file.exists() || file.mkdirs());
	}

}
