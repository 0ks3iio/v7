package net.zdsoft.studevelop.mobile.action;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import net.zdsoft.basedata.entity.StorageDir;
import net.zdsoft.basedata.remote.service.StorageDirRemoteService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StorageFileUtils;
import net.zdsoft.framework.utils.StringUtils;

@Controller
@RequestMapping("/mobile/open/studevelop")
public class StuImageAction {
	
	@Autowired
	private StorageDirRemoteService storageDirRemoteService;
	
	@RequestMapping("/upload")
	public void uploadImage(HttpServletRequest request){
		MultipartFile file = StorageFileUtils.getFile(request);
		System.out.println(file.getName());
	}
	
	@RequestMapping("/showImage")
	public void showImage(String dirId, String filePath, HttpServletResponse response){
		File file = null;
		FileInputStream in = null;
		ServletOutputStream out = null;
		
		try {
			
			if(StringUtils.isNotBlank(filePath)){
				if(StringUtils.isBlank(dirId)){
					file = new File(filePath);
				}else{
					StorageDir dir = SUtils.dc(storageDirRemoteService.findOneById(dirId), StorageDir.class);
					file = new File(dir.getDir() + File.separator + filePath);
				}
			}
			if(file==null || !file.exists()){
				filePath = Evn.getRequest().getRealPath("/studevelop/mobile/images/icon/img_default_photo.png");
				file = new File(filePath);
			}
			
			in = new FileInputStream(file);
			out = response.getOutputStream();
			
			byte[] buffer = new byte[1024];
			int i = -1;
			while((i = in.read(buffer)) != -1){
				out.write(buffer, 0, i);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(in!=null)
					in.close();
				if(out!=null)
					out.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
	
}
