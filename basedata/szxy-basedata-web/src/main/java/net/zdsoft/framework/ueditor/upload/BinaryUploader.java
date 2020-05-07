package net.zdsoft.framework.ueditor.upload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.framework.ueditor.PathFormat;
import net.zdsoft.framework.ueditor.define.AppInfo;
import net.zdsoft.framework.ueditor.define.BaseState;
import net.zdsoft.framework.ueditor.define.State;
import net.zdsoft.system.utils.PathUtils;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * 
 * update by shenke 2016-10-9下午6:56:31
 * 修改
 * @author shenke 
 *
 */
public class BinaryUploader {
	
	//private static StorageDirService storageDirService = (StorageDirService) ContainerManager.getComponent("storageDirService");
	
	private static final String DEFAULT_ENCODE = "utf-8";
	
	public static final State save(HttpServletRequest request,
			Map<String, Object> conf) {
		
		CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		
		//設置編碼格式 by shenke
		resolver.setDefaultEncoding(DEFAULT_ENCODE);
		try {
			InputStream in = request.getInputStream();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (resolver.isMultipart(request) ) {
			MultipartHttpServletRequest mRequest = null;
			if(request instanceof MultipartHttpServletRequest) {
				mRequest = (MultipartHttpServletRequest) request;
			}else{
				mRequest = resolver.resolveMultipart(request);
			}
			/*Return an Iterator of String objects containing the parameter names of the multipart files contained in this request.
			 *  These are the field names of the form (like with normal parameters), 
			 *  not the original file names.*/
			Iterator<String> e = mRequest.getFileNames();
			if (!e.hasNext()) {
				return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
			}
			// 有上传文件
			if (e.hasNext()) {
				long maxSize = ((Long) conf.get("maxSize")).longValue();
				//
				String fieldName = e.next();
				MultipartFile uploadedFile = mRequest.getFile(fieldName);
				
				try {
					if (uploadedFile.getBytes().length > maxSize) {
						BaseState baseState = new BaseState(false, AppInfo.MAX_SIZE);
						baseState.setInfo("文件超出"+maxSize/(1000*1000)+"M限制");
						return baseState;
					}
					// 不能上传空文档
					if (uploadedFile.getBytes().length == 0) {
						return new BaseState(false, AppInfo.ZERO_LENGTH);
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				String fileName = uploadedFile.getOriginalFilename();
				
				try {
					byte[] data = null;
				try {
					data = readInputStream(uploadedFile.getInputStream());
				} catch (Exception e1) {
					e1.printStackTrace();
				} 
				//StorageDir dir = storageDirService.getActiveStorageDir();
				
				String savePath = (String) conf.get("savePath");
				SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd");
				String filePath = "photo" + File.separator + "attached" +File.separator + savePath + File.separator + df1.format(new Date());
				//检查扩展名
				String fileExt1 = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt1;
				//String detailPath = dir.getDir() + File.separator + filePath + File.separator;
				//暫時定死，
				String detailPath = PathUtils.getFilePath() + File.separator + filePath + File.separator;
				File saveDirFile = new File(detailPath);
				//如果不存在，那么创建一个
				if (!saveDirFile.exists()) {
					saveDirFile.mkdirs();
				}
				File tempFile = new File(detailPath+newFileName);
				FileOutputStream outStream = new FileOutputStream(tempFile);
		        //写入数据 
		        outStream.write(data);
		        outStream.close();
		        String filename = newFileName;
		        State storageState = new BaseState(true);
				if (storageState.isSuccess()) {
					storageState.putInfo("url", PathFormat.format(request.getContextPath()+"/common/downloadFile.action?filePath="+filePath+"&filename="+filename));
					storageState.putInfo("type", fileExt1);
					storageState.putInfo("original", fileName);//这个存的是老文件的名称，word文档图片转存用到
				}

				return storageState;
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	public static byte[] readInputStream(InputStream inStream) throws Exception{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        //创建一个Buffer字符串  
        byte[] buffer = new byte[1024];  
        //每次读取的字符串长度，如果为-1，代表全部读取完毕  
        int len = 0;  
        //使用一个输入流从buffer里把数据读取出来  
        while( (len=inStream.read(buffer)) != -1 ){  
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度  
            outStream.write(buffer, 0, len);  
        }  
        //关闭输入流  
        inStream.close();  
        //把outStream里的数据写入内存  
        return outStream.toByteArray();  
    }
}
