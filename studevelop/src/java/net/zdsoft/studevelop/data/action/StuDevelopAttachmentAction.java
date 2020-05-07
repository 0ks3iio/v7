package net.zdsoft.studevelop.data.action;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.framework.utils.JsCropUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import net.zdsoft.basedata.entity.StorageDir;
import net.zdsoft.basedata.remote.service.StorageDirRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StorageFileUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.entity.StudevelopAttachment;
import net.zdsoft.studevelop.data.entity.StudevelopAttachment.StudevelopAttachmentObjType;
import net.zdsoft.studevelop.data.service.StudevelopAttachmentService;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

/**
 * 
 * @author weixh
 * @since 2017-8-4 下午2:20:50
 */
@Controller
@RequestMapping("/studevelop/common/attachment")
public class StuDevelopAttachmentAction extends CommonAuthAction {
	@Autowired
	private StudevelopAttachmentService studevelopAttachmentService;
	@Autowired
	private StorageDirRemoteService storageDirRemoteService;
	
	@ControllerInfo("新增附件")
	@RequestMapping("/save")
	public String saveAtt(ModelMap map, HttpServletRequest request){
		try {
			List<MultipartFile> files = StorageFileUtils.getFiles(request);
			if(CollectionUtils.isEmpty(files)){
				return "";
			}
			String objType = request.getParameter("objType");
			String objId = request.getParameter("objId");
			for (int i=0;i<files.size();i++) {
				MultipartFile file = files.get(i);
				StudevelopAttachment att = new StudevelopAttachment();
				att.setObjecttype(objType);
				att.setObjId(objId);
				att.setUnitId(getLoginInfo().getUnitId());
				try {
					studevelopAttachmentService.saveAttachment(att, file);
				} catch (Exception e) {
					log.error("第"+(i+1)+"个附件保存失败："+e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return "";
	}
	
	@ResponseBody
	@ControllerInfo("新增附件，从临时目录保存文件到附件表")
	@RequestMapping("/saveFromDir")
	public String saveFromDir(ModelMap map, HttpServletRequest request){
		try {
			String objType = request.getParameter("objType");
			String objId = request.getParameter("objId");
			if(StringUtils.isEmpty(objId)){
				return error("没有取到保存对象id！");
			}
			
			String fileSystemPath = Evn.<SysOptionRemoteService> getBean(
					"sysOptionRemoteService").findValue(Constant.FILE_PATH);// 文件系统地址
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String ymd = sdf.format(new Date());

			String filePath = "upload" + File.separator + ymd + File.separator
					+ objId;
			String tempDirPath = fileSystemPath + File.separator + filePath;
			System.out.println(objType+"保存附件，提取文件的临时目录："+tempDirPath);
			File dir = new File(tempDirPath);
			if(dir == null || !dir.exists()){
				return error("临时目录不存在或已被删除，取不到要保存的文件！");
			}
			File[] files = dir.listFiles();
			if(ArrayUtils.isEmpty(files)){
				return error("临时目录下没有待保存的文件！");
			}
			for (int i=0;i<files.length;i++) {
				File file = files[i];
				StudevelopAttachment att = new StudevelopAttachment();
				att.setObjecttype(objType);
				att.setObjId(objId);
				att.setUnitId(getLoginInfo().getUnitId());
				try {
					studevelopAttachmentService.saveAttachment(att, file);
				} catch (Exception e) {
					log.error("第"+(i+1)+"个附件保存失败："+e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error("图片保存失败！");
		}
		return success("图片保存成功！");
	}

	@ResponseBody
	@ControllerInfo("删除附件")
	@RequestMapping("/delete")
	public String deleteAtts(String ids, ModelMap map){
		if(StringUtils.isEmpty(ids)){
			return error("没有选择要删除的记录！");
		}
		try {
			studevelopAttachmentService.delete(StringUtils.split(ids, ","));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error("删除失败！");
		}
		return success("删除成功！");
	}
	class MyThread implements Runnable{
		//
		private List<StudevelopAttachment> atts;
		private int count;
		private String dir;
		//已经处理的数据量
		private int havedCount = 0;
		@Override
		public void run() {
			//while(count - havedCount > 0){//线程会循环执行，直到所有数据都处理完
		      //synchronized(this){//在分包时需要线程同步，避免线程间处理重复的数据
					File smallFile=null;
					File orginFile=null;
					File insertFile=null;
					StudevelopAttachmentObjType objType=null;
					//StudevelopAttachment att=atts.get(havedCount);
					for(StudevelopAttachment att:atts){
						smallFile=new File(dir+ File.separator + att.getFilePath());
						String parentPath=smallFile.getParent();
						orginFile=new File(parentPath+File.separator+StuDevelopConstant.PIC_ORIGIN_NAME+"."+att.getExtName());
						if(!orginFile.exists()){
							//System.out.println(parentPath+File.separator+StuDevelopConstant.PIC_ORIGIN_NAME+"."+att.getExtName()+":原图不存在");
							continue;
						}
						if(smallFile.exists()){
							continue;
							//smallFile.delete();
						}
						havedCount++;
						System.out.println(havedCount);
						objType = StudevelopAttachmentObjType.getType(att.getObjecttype());
						if(objType != null){
							BufferedImage zoomImage = JsCropUtils.zoom(orginFile, objType.getWight(), objType.getHeight());
							insertFile = new File(parentPath + File.separator+ att.getId()+"."+att.getExtName());
							try {
								FileUtils.writeByteArrayToFile(insertFile, JsCropUtils.toBytes(zoomImage));
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				//}
	     // }
		}
		public List<StudevelopAttachment> getAtts() {
			return atts;
		}
		public void setAtts(List<StudevelopAttachment> atts) {
			this.atts = atts;
		}
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
		public String getDir() {
			return dir;
		}
		public void setDir(String dir) {
			this.dir = dir;
		}
	}
	@ResponseBody
	@ControllerInfo("附件")
	@RequestMapping("/saveSmall")
	public String saveSmall(HttpServletResponse response){
		try {
			List<StorageDir> dirs = SUtils.dt(storageDirRemoteService.findByTypeAndActiove(0, "1"), new TR<List<StorageDir>>(){});
			if(CollectionUtils.isEmpty(dirs)){
				return returnError();
			}
			String dir=dirs.get(0).getDir();
			List<StudevelopAttachment> atts=studevelopAttachmentService.findAll();
			List<List<StudevelopAttachment>> result=averageAssign(atts, 20);
			for(List<StudevelopAttachment> values:result){
				MyThread myThread=new MyThread();
				myThread.setAtts(values);
				myThread.setDir(dir);
				Thread thread=new Thread(myThread);
				thread.start();
			}
			/*	
			if(CollectionUtils.isNotEmpty(atts)){
				File smallFile=null;
				File orginFile=null;
				File insertFile=null;
				StudevelopAttachmentObjType objType=null;
				int i=0;
				int j=0;
				for(StudevelopAttachment att:atts){
					smallFile=new File(dir+ File.separator + att.getFilePath());
					String parentPath=smallFile.getParent();
					orginFile=new File(parentPath+File.separator+StuDevelopConstant.PIC_ORIGIN_NAME+"."+att.getExtName());
					if(!orginFile.exists()){
						//System.out.println(parentPath+File.separator+StuDevelopConstant.PIC_ORIGIN_NAME+"."+att.getExtName()+":原图不存在");
						i++;
						System.out.println("无原图:"+i);
						continue;
					}
					j++;
					if(smallFile.exists()){
						smallFile.delete();
					}
					objType = StudevelopAttachmentObjType.getType(att.getObjecttype());
					if(objType != null){
						BufferedImage zoomImage = JsCropUtils.zoom(orginFile, objType.getWight(), objType.getHeight());
						insertFile = new File(parentPath + File.separator+ att.getId()+"."+att.getExtName());
						FileUtils.writeByteArrayToFile(insertFile, JsCropUtils.toBytes(zoomImage));
					}
					System.out.println("有原图:"+j);
				}
				long time2=System.currentTimeMillis();
				System.out.println(time2-time1+"ms");
			}*/
			
		} catch (Exception e) {
			return returnError();
		}
		return returnSuccess();
	}
	public  List<List<StudevelopAttachment>> averageAssign(List<StudevelopAttachment> source,int n){  
	    List<List<StudevelopAttachment>> result=new ArrayList<List<StudevelopAttachment>>();  
	    int remaider=source.size()%n;  //(先计算出余数)  
	    int number=source.size()/n;  //然后是商  
	    int offset=0;//偏移量  
	    for(int i=0;i<n;i++){  
	        List<StudevelopAttachment> value=null;  
	        if(remaider>0){  
	            value=source.subList(i*number+offset, (i+1)*number+offset+1);  
	            remaider--;  
	            offset++;  
	        }else{  
	            value=source.subList(i*number+offset, (i+1)*number+offset);  
	        }  
	        result.add(value);  
	    }  
	    return result;  
	} 
	@ControllerInfo("显示图片附件")
	@RequestMapping("/showPic")
	public String showPic(String id, String showOrigin, HttpServletResponse response){
		try {
			StudevelopAttachment att = studevelopAttachmentService.findOne(id);
			if(att == null){
				return null;
			}
			StorageDir sd = SUtils.dc(storageDirRemoteService.findOneById(att.getDirId()), StorageDir.class);
			if(sd == null){
				return null;
			}
			File img = new File(sd.getDir() + File.separator + att.getFilePath());
//			File img = new File("D:\\store" + File.separator + att.getFilePath());
			
			if(img == null || !img.exists()) {
				return null;
			}
			File pic = null;
			if(Constant.IS_TRUE_Str.equals(showOrigin)){
				String dirPath = img.getParent();
				String originFilePath = dirPath + File.separator + StuDevelopConstant.PIC_ORIGIN_NAME+"."+att.getExtName();
				pic = new File(originFilePath);
			} else if(StuDevelopConstant.IS_MOBILE_STR.equals(showOrigin)){//当2时  显示手机端的图片(更小)
				String originFilePath = img.getParent() + File.separator + StuDevelopConstant.PIC_MOBILE_NAME+"."+att.getExtName();
				pic = new File(originFilePath);
			}else{
				pic = img;
			}
			if(pic == null || !pic.exists()) {
				pic = img;
			}
			if(pic != null && pic.exists()){
				response.getOutputStream().write(FileUtils.readFileToByteArray(pic));
				/*long time1=System.currentTimeMillis();
				BasicFileAttributes bfa=Files.readAttributes(pic.toPath(),BasicFileAttributes.class);
				System.out.println(bfa.creationTime().toString());
				System.out.println(bfa.lastModifiedTime().toString());
				long time2=System.currentTimeMillis();
				System.out.println(time2-time1);*/
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value="/masterpic/show")
	@ControllerInfo(value="显示校长照片")
	public String showMasterPic(String unitId, String showOrigin, HttpServletResponse response){
		try {
			List<StudevelopAttachment> atts = studevelopAttachmentService.getAttachmentByObjId(unitId, StuDevelopConstant.OBJTYPE_MASTER_PIC);
			File pic;
			if(CollectionUtils.isNotEmpty(atts)){
				StudevelopAttachment att = atts.get(0);
				if (Constant.IS_TRUE_Str.equals(showOrigin)) {
					pic = att.getOriginFile();// 原图
				} else {
					pic = att.getSmallFile();// 缩略图
				}
			} else {
				String defalutPath = Evn.getRequest().getRealPath("/static/jscrop/images/portrait_big.png");
				pic = new File(defalutPath);
			}
			if(pic != null && pic.exists()){
				response.getOutputStream().write(FileUtils.readFileToByteArray(pic));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
