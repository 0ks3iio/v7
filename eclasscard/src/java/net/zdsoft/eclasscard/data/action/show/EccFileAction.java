package net.zdsoft.eclasscard.data.action.show;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.basedata.dto.AttFileDto;
import net.zdsoft.basedata.dto.BaseDto;
import net.zdsoft.basedata.entity.Attachment;
import net.zdsoft.basedata.remote.service.AttachmentRemoteService;
import net.zdsoft.eclasscard.data.constant.EccConstants;
import net.zdsoft.eclasscard.data.dto.PPTDetails;
import net.zdsoft.eclasscard.data.entity.EccAttachFolder;
import net.zdsoft.eclasscard.data.entity.EccClientLog;
import net.zdsoft.eclasscard.data.entity.EccPhotoAlbum;
import net.zdsoft.eclasscard.data.service.EccAttachFolderService;
import net.zdsoft.eclasscard.data.service.EccClientLogService;
import net.zdsoft.eclasscard.data.service.EccPhotoAlbumService;
import net.zdsoft.eclasscard.data.utils.EccUtils;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.config.ControllerException;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.RemoteCallUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StorageFileUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller
@RequestMapping("/eccShow/eclasscard")
public class EccFileAction extends BaseAction {
	public static final String PPT_DOWNLOAD_DOMAIN= "http://msyk.wpstatic.cn";
	@Autowired
	private EccPhotoAlbumService eccPhotoAlbumService;
	@Autowired
	private AttachmentRemoteService attachmentRemoteService;
	@Autowired
	private EccAttachFolderService eccAttachFolderService;
	@Autowired
	private EccClientLogService eccClientLogService;

	@RequestMapping(value = "/pptConvertIn", method = RequestMethod.POST)
	@ResponseBody
	public String pptConvertToolIn(@RequestParam(name = "timeStamp") long timeStamp) {
		try {
			if(timeStamp == 0){
				return pptError("参数错误");
			}
			RedisUtils.sadd(EccConstants.PPT_CONVERT_TIMESTAMP_SET,
					String.valueOf(timeStamp));
		} catch (Exception e) {
			return pptError("操作失败");
		}
		return pptSuccess();
	}

	@RequestMapping(value = "/pptConvertNotice", method = RequestMethod.POST)
	@ResponseBody
	public String pptConvertNotice(HttpServletRequest request) {
		String param=request.getParameter("param");
		String details=request.getParameter("details");
		String filePath=request.getParameter("filePath");
		String title=request.getParameter("title");
		// 根据参数下载存储
		try {
			String unitId = RemoteCallUtils.getParamValue(param, "unitId");
			String userId = RemoteCallUtils.getParamValue(param, "userId");
			String objectType = RemoteCallUtils.getParamValue(param, "objectType");
			String objectId = RemoteCallUtils.getParamValue(param, "objectId");
			String range = RemoteCallUtils.getParamValue(param, "range");
			if (StringUtils.isBlank(unitId) || StringUtils.isBlank(userId) || StringUtils.isBlank(objectType) || StringUtils.isBlank(details) || StringUtils.isBlank(filePath) || StringUtils.isBlank(title)) {
				return pptError("参数有误");
			}
			List<PPTDetails> pptdetails = SUtils.dt(details,new TR<List<PPTDetails>>() {});
			dealPPTtoImage(pptdetails,unitId,objectType,filePath,title,objectId,Integer.valueOf(range));
		} catch (Exception e) {
			e.printStackTrace();
			return pptError("操作失败");
		}
		return pptSuccess();
	}


	private void dealPPTtoImage(List<PPTDetails> pptdetails, String unitId,
			String objectType, String filePath, String title,String objectId,Integer range) {
		List<AttFileDto> fileDtos = Lists.newArrayList();
		EccAttachFolder attachFolder = new EccAttachFolder();
		attachFolder.setId(UuidUtils.generateUuid());
		if(EccConstants.ECC_FOLDER_RANGE_2 == range){
			attachFolder.setRange(EccConstants.ECC_FOLDER_RANGE_2);
			attachFolder.setSendType(EccConstants.ECC_SENDTYPE_9);
		}else{
			attachFolder.setRange(EccConstants.ECC_FOLDER_RANGE_1);
			attachFolder.setSendType(EccConstants.ECC_SENDTYPE_0);
		}
		String afTile = EccUtils.getFileNameNoExt(title);
		if(afTile.length()>25){
			afTile = afTile.substring(0, 25);
		}
		Date newDate = new Date();
		attachFolder.setShow(false);
		attachFolder.setTitle(afTile);
		attachFolder.setType(EccConstants.ECC_FOLDER_TYPE_3);
		attachFolder.setUnitId(unitId);
		attachFolder.setCreateTime(newDate);
		eccAttachFolderService.saveNewFolder(attachFolder,objectId);
		Map<String,Integer> oderMap = getOrderNum(pptdetails);
		for(PPTDetails details:pptdetails){
			for(int i=0;i<details.getCount();i++){
				Calendar calendar = Calendar.getInstance();
				int secondAmount = oderMap.get(details.getNum()+"-"+i);
				AttFileDto fileDto = new AttFileDto();
				String imageName = getImageName(i);
				String fromUrl = PPT_DOWNLOAD_DOMAIN+filePath+"/"+details.getUrl()+"/"+imageName;
				fileDto.setObjectType(EccConstants.ECC_ATTACHMENT_TYPE);
				fileDto.setObjectId(attachFolder.getId());
				fileDto.setObjectUnitId(unitId);
				fileDto.setFileName(imageName);
				fileDto.setFromUrl(fromUrl);
				calendar.setTime(newDate);
		        calendar.add(Calendar.SECOND, -secondAmount);
				fileDto.setCreateTime(calendar.getTime());
				if(i==(details.getCount()-1)){
					fileDto.setConStatus(AttFileDto.PPT_STATUS_PAGE_END);
				}//目前取每页最后一张
				fileDtos.add(fileDto);
			}
		}
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					List<Attachment> attachments = SUtils.dt(attachmentRemoteService.saveAttachmentByUrls(SUtils.s(fileDtos)),new TR<List<Attachment>>() {});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private Map<String,Integer> getOrderNum(List<PPTDetails> pptdetails) {
		Map<String,Integer> map = Maps.newHashMap();
		Map<Integer,Integer> numMap = EntityUtils.getMap(pptdetails,PPTDetails::getNum,PPTDetails::getCount);
		Map<Integer,Integer> countNumMap = Maps.newHashMap();
		pptdetails.stream().sorted((a1,a2) -> (a1.getNum()- a2.getNum()));
		for(PPTDetails details:pptdetails){
			if(numMap.get(details.getNum()-1)==null){
				countNumMap.put(details.getNum(),0);
			}else{
				countNumMap.put(details.getNum(), numMap.get(details.getNum()-1)+countNumMap.get(details.getNum()-1));
			}
		}
		for(PPTDetails details:pptdetails){
			for(int i=0;i<details.getCount();i++){
				map.put(details.getNum()+"-"+i, countNumMap.get(details.getNum())+i);
			}
		}
		return map;
	}

	private String getImageName(int count) {
		if(count<10){
			return "page_00"+count+".jpg";
		}else if(count<100){
			return "page_0"+count+".jpg";
		}
		return "page_"+count+".jpg";
	}

	@RequestMapping(value = "/pptConvertTime")
	@ResponseBody
	public String pptConvertTimeQuery(String timeStamp) {
		Set<String> timeSet = RedisUtils
				.smembers(EccConstants.PPT_CONVERT_TIMESTAMP_SET);
		if (CollectionUtils.isNotEmpty(timeSet)) {
			if (timeSet.contains(timeStamp)) {
				RedisUtils.srem(EccConstants.PPT_CONVERT_TIMESTAMP_SET,
						timeStamp);
				return returnSuccess();
			}
		}
		return returnError();
	}

	/**
	 * 定制版图片显示
	 * 
	 * @param id
	 * @param response
	 */
	@RequestMapping(value = "/showpicture")
	@ResponseBody
	public void showPictrue(String id, HttpServletResponse response) {
		String fileSystemPath = Evn.<SysOptionRemoteService> getBean(
				"sysOptionRemoteService").findValue(Constant.FILE_PATH);// 文件系统地址
		EccPhotoAlbum album = eccPhotoAlbumService.findOne(id);
		if (album != null && StringUtils.isNotBlank(album.getPictrueDirpath())) {
			File file = new File(fileSystemPath + File.separator
					+ album.getPictrueDirpath());
			if (file.exists()) {
				response.setContentType("image/jpeg; charset=GBK");
				response.addHeader("Cache-Control", "max-age=604800");
				ServletOutputStream outputStream = null;
				FileInputStream inputStream = null;
				try {
					outputStream = response.getOutputStream();
					inputStream = new FileInputStream(file);
					byte[] buffer = new byte[1024];
					int i = -1;
					while ((i = inputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, i);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (outputStream != null) {
						try {
							outputStream.flush();
							outputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (inputStream != null) {
						try {
							inputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	/**
	 * 客户端日志上传
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
    @RequestMapping(value="/upload/clientlog", method = RequestMethod.POST)
    public String remoteFileUpload(HttpServletRequest request) throws Exception {
        try {
            MultipartFile file = StorageFileUtils.getFile(request);
            String cardId=request.getParameter("cardId");
            String filename=request.getParameter("filename");
            String fileType=request.getParameter("fileType");
            filename = URLDecoder.decode(filename,"utf-8");
            eccClientLogService.saveClientLog(cardId, filename,fileType,file);
            return success("上传成功");
		}catch (Exception e) {
			e.printStackTrace();
			return error("上传失败");
		}
    }
	
	/**
	 * 客户端日志下载
	 * @param id
	 * @param loadType
	 * @param response
	 */
	@RequestMapping(value = "/download/clientlog")
	@ResponseBody
	public void downloadClienLog(String id, HttpServletResponse response) {
		String filePathHead = Evn.<SysOptionRemoteService> getBean("sysOptionRemoteService").findValue(Constant.FILE_PATH);// 文件系统地址
		EccClientLog log = eccClientLogService.findOne(id);
		if(log==null|| StringUtils.isBlank(log.getFilePath())){
			return;
		}
		String filePath = filePathHead+File.separator+log.getFilePath()+File.separator+log.getFileName();
		InputStream fis = null;
		OutputStream toClient = null;
		try {
			// path是指欲下载的文件的路径。
			File file = new File(filePath);
			if(!file.exists()){
				return;
			}
			// 取得文件名。
			String filename = file.getName();
			// 以流的形式下载文件。
			fis = new BufferedInputStream(new FileInputStream(filePath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename="
					+ URLEncoder.encode(filename, "utf-8"));
			response.addHeader("Content-Length", "" + file.length());
			toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();


		} catch (Exception x) {
			x.printStackTrace();
		} finally {
			if (toClient != null) {
				try {
					toClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 标准版附件删除
	 * 
	 * @param ids
	 * @param nameSuffixs
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/delete/attachment")
	@ResponseBody
	public String deleteAttachment(String[] ids, String[] nameSuffixs,
			HttpServletResponse response) {
		try {
			attachmentRemoteService.deleteAttachments(ids, nameSuffixs);
		} catch (Exception e) {
			e.printStackTrace();
			return error("删除失败");
		}
		return success("删除成功");
	}

	/**
	 * 定制版图片删除
	 * 
	 * @param ids
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/deletepicture")
	@ResponseBody
	public String deletePictrue(String[] ids, HttpServletResponse response) {
		String fileSystemPath = Evn.<SysOptionRemoteService> getBean(
				"sysOptionRemoteService").findValue(Constant.FILE_PATH);// 文件系统地址
		List<EccPhotoAlbum> albums = eccPhotoAlbumService.findListByIds(ids);
		Set<String> pictrueDirpaths = EntityUtils.getSet(albums,
				"pictrueDirpath");
		if (!pictrueDirpaths.isEmpty()) {// 删除没有其他引用的图片
			Set<String> paths = eccPhotoAlbumService
					.findPictrueDirpathMore(pictrueDirpaths
							.toArray(new String[pictrueDirpaths.size()]));
			for (EccPhotoAlbum album : albums) {
				if (StringUtils.isNotBlank(album.getPictrueDirpath())
						&& !paths.contains(album.getPictrueDirpath())) {
					File file = new File(fileSystemPath + File.separator
							+ album.getPictrueDirpath());
					if (file.exists()) {
						file.delete();
					}
				}
			}
		}
		if (!albums.isEmpty()) {
			eccPhotoAlbumService.deleteAll(albums
					.toArray(new EccPhotoAlbum[albums.size()]));
		}
		return success("删除成功");
	}
	
	@RequestMapping(value = "/loadface")
	@ResponseBody
	public void showPictrue(String id,String loadType, HttpServletResponse response) {
		File file = null;
		String filePath = "";
		List<Attachment> attachments = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(id),new TR<List<Attachment>>(){});
		Attachment attachment = attachments.get(0);
		if(attachment!=null)filePath = attachment.getFilePath();
		String filePathHead = Evn.<SysOptionRemoteService> getBean("sysOptionRemoteService").findValue(Constant.FILE_PATH);// 文件系统地址
		if (StringUtils.isNotBlank(filePathHead) && StringUtils.isNotBlank(filePath)) {
			String fileSystemPath = filePathHead+File.separator+"store"+File.separator;
			file= new File(fileSystemPath + File.separator + filePath);
		}
		if (file!=null && file.exists()) {
			response.setContentType("image/jpeg; charset=GBK");
			response.addHeader("Cache-Control", "max-age=604800");
			response.setContentLength((int)file.length());
			ServletOutputStream outputStream = null;
			FileInputStream inputStream = null;
			try {
				outputStream = response.getOutputStream();
				inputStream = new FileInputStream(file);
				byte[] buffer = new byte[1024];
				int i = -1;
				while ((i = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, i);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (outputStream != null) {
					try {
						outputStream.flush();
						outputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public String pptSuccess() {
		return Json.toJSONString(new PptResultDto().setCode("10000").setMessage(
				"操作成功！"));
	}
	
	public String pptError(String message) {
		return Json.toJSONString(new PptResultDto().setCode("10005").setMessage(
				message));
	}

	public class PptResultDto extends BaseDto {
		private static final long serialVersionUID = 1L;
		private String code;
		private String message;

		public String getCode() {
			return code;
		}

		public PptResultDto setCode(String code) {
			this.code = code;
			return this;
		}

		public String getMessage() {
			return message;
		}

		public PptResultDto setMessage(String message) {
			this.message = message;
			return this;
		}

	}
	
	@Override
	public String runtimeExceptionHandler(ControllerException runtimeException, WebRequest request) {
		log.error("controller error ,code {} , msg {}", runtimeException);
		return pptError("操作失败");
	}
	@Override
	public String runtimeExceptionHandler2(Exception runtimeException, WebRequest request) {
		log.error("controller error ,code {} , msg {}", runtimeException);
		return pptError("操作失败");
	}
}
