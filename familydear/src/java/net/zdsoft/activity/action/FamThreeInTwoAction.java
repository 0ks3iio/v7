package net.zdsoft.activity.action;

import static net.zdsoft.familydear.common.FamDearConstant.PLAN_PUBLISH;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.activity.entity.FamDearThreeInTwo;
import net.zdsoft.activity.entity.FamDearThreeInTwoReport;
import net.zdsoft.activity.entity.FamDearThreeInTwoStu;
import net.zdsoft.activity.entity.FamilyDearPermission;
import net.zdsoft.activity.service.FamThreeInTwoReportService;
import net.zdsoft.activity.service.FamThreeInTwoService;
import net.zdsoft.activity.service.FamThreeInTwoStuService;
import net.zdsoft.activity.service.FamilyDearPermissionService;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.StorageDir;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.StorageDirRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.familydear.common.FamDearConstant;
import net.zdsoft.familydear.entity.FamDearAttachment;
import net.zdsoft.familydear.service.FamDearAttachmentService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StorageFileUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

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

@Controller
@RequestMapping("/familydear/threeInTwo")
public class FamThreeInTwoAction extends BaseAction{
	@Autowired
	private FamilyDearPermissionService familyDearPermissionService;
	@Autowired
	private FamThreeInTwoService famThreeInTwoService;
	@Autowired
	private FamDearAttachmentService famDearAttachmentService;
	@Autowired
	private StorageDirRemoteService storageDirRemoteService;
	@Autowired
	private FamThreeInTwoReportService famThreeInTwoReportService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private FamThreeInTwoStuService famThreeInTwoStuService;
	@Autowired
	private DeptRemoteService deptRemoteService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	
	@RequestMapping("/edu/index/page")
    public String threeInTwoIndex(ModelMap map){
		map.put("acadyearList", getYearList());
		map.put("nowYear", DateUtils.date2String(new Date(), "yyyy"));
		List<FamilyDearPermission> familyDearPermissions = familyDearPermissionService.getFamilyDearPermissionListByPermissionType(getLoginInfo().getUnitId(),getLoginInfo().getUserId(),"7");
        map.put("hasPermission",false);
        if(CollectionUtils.isNotEmpty(familyDearPermissions)){
            map.put("hasPermission",true);
        }
		return "familydear/threeInTwo/threeInTwoIndex.ftl";
	}
	
	@RequestMapping("/edu/List/page")
    public String threeInTwoList(HttpServletRequest req,ModelMap map,String year,String title) throws UnsupportedEncodingException{
		Pagination page=createPagination();
		title=java.net.URLDecoder.decode(title, "UTF-8");
		List<FamilyDearPermission> familyDearPermissions = familyDearPermissionService.getFamilyDearPermissionListByPermissionType(getLoginInfo().getUnitId(),getLoginInfo().getUserId(),"7");
	    map.put("hasPermission",false);
	    if(CollectionUtils.isNotEmpty(familyDearPermissions)){
	    	map.put("hasPermission",true);
	    	List<FamDearThreeInTwo> famDearThreeInTwos=famThreeInTwoService.getListByUnitIdAndYearAndTitleAndState(getLoginInfo().getUnitId(), year, title, null, page);
	    	map.put("famDearThreeInTwos", famDearThreeInTwos);
	    }else{
	    	List<FamDearThreeInTwo> famDearThreeInTwos=famThreeInTwoService.getListByUnitIdAndYearAndTitleAndState(getLoginInfo().getUnitId(), year, title, FamDearConstant.PLAN_PUBLISH, page);
	    	map.put("famDearThreeInTwos", famDearThreeInTwos);
	    }
	    sendPagination(req, map, page);
		return "familydear/threeInTwo/threeInTwoList.ftl";
	}
	
	@RequestMapping("/edu/edit/page")
    public String threeInTwoEdit(ModelMap map,String id){
		if(StringUtils.isNotBlank(id)){
			FamDearThreeInTwo famDearThreeInTwo=famThreeInTwoService.findOne(id);
			map.put("acadyearList", getYearList());
			map.put("nowYear", famDearThreeInTwo.getYear());
			map.put("famDearThreeInTwo", famDearThreeInTwo);
		}else{
			map.put("acadyearList", getYearList());
			map.put("nowYear", DateUtils.date2String(new Date(), "yyyy"));
			FamDearThreeInTwo famDearThreeInTwo=new FamDearThreeInTwo();
			String uuid = UUID.randomUUID().toString().replaceAll("-","");
			famDearThreeInTwo.setId(uuid);
			map.put("famDearThreeInTwo", famDearThreeInTwo);
		}
		List<FamDearAttachment> atts = new ArrayList<>();
        atts = famDearAttachmentService.getAttachmentByObjId(id, "famDearThreeInTwoPicture");
        map.put("actDetails", atts);
        
        List<FamDearAttachment> fileAtts = new ArrayList<>();
        fileAtts = famDearAttachmentService.getAttachmentByObjId(id, "famDearThreeInTwoFile");
        map.put("fileAtts", fileAtts);
		
		return "familydear/threeInTwo/threeInTwoEdit.ftl";
	}
	
	@RequestMapping("/viewThreeInTwo")
	public String threeInTwoView(ModelMap map,String id){
		FamDearThreeInTwo famDearThreeInTwo=famThreeInTwoService.findOne(id);
		map.put("famDearThreeInTwo", famDearThreeInTwo);
		map.put("acadyearList", getYearList());
		map.put("nowYear", famDearThreeInTwo.getYear());
		List<FamDearAttachment> atts = new ArrayList<>();
        atts = famDearAttachmentService.getAttachmentByObjId(id, "famDearThreeInTwoPicture");
        map.put("actDetails", atts);
        
        List<FamDearAttachment> fileAtts = new ArrayList<>();
        fileAtts = famDearAttachmentService.getAttachmentByObjId(id, "famDearThreeInTwoFile");
        map.put("fileAtts", fileAtts);
		return "familydear/threeInTwo/threeInTwoView.ftl";
	}
	
	@RequestMapping("/edu/save/page")
    @ControllerInfo(value = "保存三进两联")
    @ResponseBody
    public String threeInTwoSave(FamDearThreeInTwo famDearThreeInTwo){
		FamDearThreeInTwo old=famThreeInTwoService.findOne(famDearThreeInTwo.getId());
		if(old!=null){
			try {
				famThreeInTwoService.save(famDearThreeInTwo);
				return returnSuccess();
			} catch (Exception e) {
				e.printStackTrace();
				return returnError();
			}
		}else{
			try {
				famDearThreeInTwo.setState(FamDearConstant.PLAN_UNPUBLISH);
				famDearThreeInTwo.setCreateTime(new Date());
				famDearThreeInTwo.setCreateUserId(getLoginInfo().getUserId());
				famDearThreeInTwo.setUnitId(getLoginInfo().getUnitId());
				famDearThreeInTwo.setIsDelete("0");
				famThreeInTwoService.save(famDearThreeInTwo);
				return returnSuccess();
			} catch (Exception e) {
				e.printStackTrace();
				return returnError();
			}
		}
	}
	
	 @RequestMapping("/updateThreeInTwoState")
	 @ResponseBody
	 public String updateThreeInTwoState(String id,String state){
		 FamDearThreeInTwo old=famThreeInTwoService.findOne(id);
	     if(state.equals(FamDearConstant.PLAN_UNPUBLISH)){
	    	 old.setState(PLAN_PUBLISH);
	     }else {
	    	 old.setState(FamDearConstant.PLAN_UNPUBLISH);
	     }
	     try {
	    	 famThreeInTwoService.update(old,id,new String[]{"state"});
	     } catch (Exception e) {
	         e.printStackTrace();
	         return returnError();
	     }
	     return returnSuccess();
	 }
	
	 @RequestMapping("/delThreeInTwo")
	 @ResponseBody
	 public String delThreeInTwo(String id){
	     try {
	    	 FamDearThreeInTwo old=famThreeInTwoService.findOne(id);
	         if(old.getState().equals(FamDearConstant.PLAN_PUBLISH)){
	             return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("请先取消提交再进行删除！"));
	         }
	         old.setIsDelete("1");
	         famThreeInTwoService.save(old);
	     } catch (Exception e) {
	         e.printStackTrace();
	         return returnError();
	     }
	     return returnSuccess();
	 }
	 
	 @ControllerInfo("新增附件")
	    @RequestMapping("/saveAttachment")
	    public String saveAtt(ModelMap map, HttpServletRequest request){
	        try {
	            List<MultipartFile> files = StorageFileUtils.getFiles(request);
	            if(CollectionUtils.isEmpty(files)){
	                return "";
	            }
	            String objType = request.getParameter("objType").trim();
	            String objId = request.getParameter("objId");
	            for (int i=0;i<files.size();i++) {
	                MultipartFile file = files.get(i);
	                FamDearAttachment att = new FamDearAttachment();
	                att.setIsDelete("0");
	                att.setObjecttype(objType);
	                att.setObjId(objId);
	                att.setUnitId(getLoginInfo().getUnitId());
	                try {
	                	boolean isPicture=false;
	                	if(StringUtils.equals("famDearThreeInTwoPicture", objType)||StringUtils.equals("famDearThreeInTwoReportPicture", objType)){
	                		isPicture=true;
	                	}
	                    famDearAttachmentService.saveAttachment(att, file,isPicture);
	                } catch (Exception e) {
	                    log.error("第"+(i+1)+"个附件保存失败："+e.getMessage(), e);
	                }
	            }
	        } catch (Exception e) {
	            log.error(e.getMessage(), e);
	        }
	        return "";
	    }

	    @ControllerInfo("显示图片附件")
	    @RequestMapping("/showAllpic")
	    public String Edit(String id,ModelMap modelMap){
	        List<FamDearAttachment> atts = new ArrayList<>();
	        atts = famDearAttachmentService.getAttachmentByObjId(id, "famDearThreeInTwoPicture");
	        modelMap.put("id",id);
	        modelMap.put("actDetails", atts);
	        return "familydear/threeInTwo/famDearImages.ftl";
	    }
	    
	    @ControllerInfo("显示附件")
	    @RequestMapping("/showAllFile")
	    public String showAllFile(String id,ModelMap modelMap){
	    	List<FamDearAttachment> atts = new ArrayList<>();
	    	atts = famDearAttachmentService.getAttachmentByObjId(id, "famDearThreeInTwoFile");
	    	modelMap.put("id",id);
	    	modelMap.put("actDetails", atts);
	    	return "familydear/threeInTwo/famDearFiles.ftl";
	    }
	    
	    @ResponseBody
	    @ControllerInfo("删除附件")
	    @RequestMapping("/delPic")
	    public String deleteAtts(String id, ModelMap map){
	        if(org.apache.commons.lang3.StringUtils.isEmpty(id)){
	            return error("没有选择要删除的记录！");
	        }
	        try {
	            FamDearAttachment famDearAttachment = famDearAttachmentService.findOne(id);
	            famDearAttachment.setIsDelete("1");
	            famDearAttachmentService.save(famDearAttachment);
	        } catch (Exception e) {
	            log.error(e.getMessage(), e);
	            return error("删除失败！");
	        }
	        return success("删除成功！");
	    }
	    
	    @ControllerInfo("显示图片附件")
	    @RequestMapping("/showPic")
	    public String showPic(String id, String showOrigin, HttpServletResponse response){
	        try {
	            FamDearAttachment att = famDearAttachmentService.findOne(id);
	            if(att == null){
	                return null;
	            }
	            StorageDir sd = SUtils.dc(storageDirRemoteService.findOneById(att.getDirId()), StorageDir.class);
	            if(sd == null){
	                return null;
	            }
	            File img = new File(sd.getDir() + File.separator + att.getFilePath());
//				File img = new File("D:\\store" + File.separator + att.getFilePath());

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
	            }
	        } catch (IOException e) {
	            log.error(e.getMessage(), e);
	        }
	        return null;
	    }
	    
	    @ControllerInfo("下载附件")
	    @RequestMapping("/downFile")
	    public void downFile(String id, String showOrigin, HttpServletResponse response){
	    	InputStream fis = null;
    		OutputStream toClient = null;
	    	try {
	    		FamDearAttachment att = famDearAttachmentService.findOne(id);
	    		if(att == null){
	    			return;
	    		}
	    		StorageDir sd = SUtils.dc(storageDirRemoteService.findOneById(att.getDirId()), StorageDir.class);
	    		if(sd == null){
	    			return;
	    		}
//	    		String filePath ="D:\\store" + File.separator + att.getFilePath();
	    		String filePath =sd.getDir() + File.separator + att.getFilePath();
	    		File img = new File(filePath);
	    		
	    		if(img == null || !img.exists()) {
	    			return;
	    		}
	    		// 取得文件名。
				String filename = att.getFilename();
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
				response.addHeader("Content-Length", "" + img.length());
				toClient = new BufferedOutputStream(response.getOutputStream());
				response.setContentType("application/octet-stream");
				toClient.write(buffer);
				toClient.flush();
				toClient.close();
	    	} catch (IOException e) {
	    		log.error(e.getMessage(), e);
	    	}finally {
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
	    	return;
	    }
	
	@RequestMapping("/edu/report/page")
	public String threeInTwoReport(ModelMap map){
		return "familydear/threeInTwoReport/reportIndex.ftl";
	}
	
	@RequestMapping("/edu/report/edit")
	public String threeInTwoReportEdit(ModelMap map,String id){
		if(StringUtils.isNotBlank(id)){
			FamDearThreeInTwoReport famDearThreeInTwoReport=famThreeInTwoReportService.findOne(id);
			map.put("famDearThreeInTwoReport", famDearThreeInTwoReport);
		}else{
			FamDearThreeInTwoReport famDearThreeInTwoReport=new FamDearThreeInTwoReport();
			String uuid = UUID.randomUUID().toString().replaceAll("-","");
			famDearThreeInTwoReport.setId(uuid);
			map.put("famDearThreeInTwoReport", famDearThreeInTwoReport);
		}
		List<McodeDetail> mcodeDetailLxs = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-XJHDLX") ,McodeDetail.class);
        map.put("mcodeDetailLxs" ,mcodeDetailLxs);
		List<FamDearAttachment> atts = new ArrayList<>();
        atts = famDearAttachmentService.getAttachmentByObjId(id, "famDearThreeInTwoReportPicture");
        map.put("actDetails", atts);
        
        List<FamDearThreeInTwoStu> famDearThreeInTwoStus=famThreeInTwoStuService.getDearStuByUnitIdAndTeacherId(getLoginInfo().getUnitId(), getLoginInfo().getOwnerId());
        map.put("famDearThreeInTwoStus", famDearThreeInTwoStus);
		return "familydear/threeInTwoReport/reportEdit.ftl";
	}
	
	@RequestMapping("/edu/report/list")
	public String threeInTwoReportList(ModelMap map,HttpServletRequest request,Date startTime,Date endTime,String title,String stuName){
		Pagination page=createPagination();
		List<FamDearThreeInTwoReport> famDearThreeInTwoReports=new ArrayList<>();
		Set<String> stuSet=new HashSet<>();
		try {
			stuName=java.net.URLDecoder.decode(stuName, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(StringUtils.isNotBlank(stuName)){
			List<FamDearThreeInTwoStu> famDearThreeInTwoStus=famThreeInTwoStuService.getFamDearThreeInTwoStuByStuName(getLoginInfo().getUnitId(), stuName);
			if(CollectionUtils.isNotEmpty(famDearThreeInTwoStus)){
				stuSet=EntityUtils.getSet(famDearThreeInTwoStus, e->e.getId());
				famDearThreeInTwoReports=famThreeInTwoReportService.getFamDearThreeInTwoReportByUnitIdAndOthers(getLoginInfo().getUnitId(), getLoginInfo().getUserId(),
						startTime, endTime, title, stuSet.toArray(new String[0]), page);
				map.put("famDearThreeInTwoReports", famDearThreeInTwoReports);
			}else{
				map.put("famDearThreeInTwoReports", famDearThreeInTwoReports);
			}
		}else{
			famDearThreeInTwoReports=famThreeInTwoReportService.getFamDearThreeInTwoReportByUnitIdAndOthers(getLoginInfo().getUnitId(), getLoginInfo().getUserId(),
					startTime, endTime, title, null, page);
			map.put("famDearThreeInTwoReports", famDearThreeInTwoReports);
		}
		sendPagination(request, map, page);
		return "familydear/threeInTwoReport/reportList.ftl";
	}
	
	
	@ControllerInfo("显示图片附件")
    @RequestMapping("/showReportpic")
    public String showReportpic(String id,ModelMap modelMap){
        List<FamDearAttachment> atts = new ArrayList<>();
        atts = famDearAttachmentService.getAttachmentByObjId(id, "famDearThreeInTwoReportPicture");
        modelMap.put("id",id);
        modelMap.put("actDetails", atts);
        return "familydear/threeInTwo/famDearImages.ftl";
    }
	
	@ResponseBody
    @RequestMapping("/edu/report/save")
    @ControllerInfo(value = "保存")
    public String threeInTwoReportSave(FamDearThreeInTwoReport famDearThreeInTwoReport) {
        try{

            if (ArrayUtils.isNotEmpty(famDearThreeInTwoReport.getTypeArray())) {
                StringBuilder builder = new StringBuilder();
                for (String str:famDearThreeInTwoReport.getTypeArray() ) {
                    builder.append(str + ",");

                }
                famDearThreeInTwoReport.setType(builder.substring(0,builder.length() - 1));
            }
            if (ArrayUtils.isNotEmpty(famDearThreeInTwoReport.getStuArray())) {
            	StringBuilder builder = new StringBuilder();
            	for (String str:famDearThreeInTwoReport.getStuArray()) {
            		builder.append(str + ",");
            	}
            	famDearThreeInTwoReport.setObjStu(builder.substring(0,builder.length() - 1));
            }
            FamDearThreeInTwoReport old=famThreeInTwoReportService.findOne(famDearThreeInTwoReport.getId());
            if (old==null) {
            	famDearThreeInTwoReport.setCreateTime(new Date());
            	famDearThreeInTwoReport.setCreateUserId(getLoginInfo().getUserId());
            }
            famDearThreeInTwoReport.setUnitId(getLoginInfo().getUnitId());
            famDearThreeInTwoReport.setIsDelete("0");
           
            famThreeInTwoReportService.save(famDearThreeInTwoReport);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
		return returnSuccess();

    }
	
	 @RequestMapping("/edu/report/delete")
	 @ResponseBody
	 public String threeInTwoReportDelete(String id){
	     try {
	    	 FamDearThreeInTwoReport old=famThreeInTwoReportService.findOne(id);
	         old.setIsDelete("1");
	         famThreeInTwoReportService.save(old);
	     } catch (Exception e) {
	         e.printStackTrace();
	         return returnError();
	     }
	     return returnSuccess();
	 }
	 
	@RequestMapping("/edu/statistic/page")
	public String threeInTwoStatistic(ModelMap map){
		List<Dept> depts = SUtils.dt(deptRemoteService.findByUnitId(getLoginInfo().getUnitId()),new TR<List<Dept>>() {});
		map.put("depts", depts);
		return "familydear/threeInTwoReport/reportStatisticIndex.ftl";
	}
	
	@RequestMapping("/edu/statistic/list")
	public String threeInTwoStatisticList(ModelMap map,HttpServletRequest request,Date startTime,Date endTime,String deptId,String teaName,String title){
		Pagination page=createPagination();
		Set<String> userSet=new HashSet<>();
		try {
			teaName=java.net.URLDecoder.decode(teaName, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(StringUtils.isNotBlank(deptId)){
			List<Teacher> teachers = SUtils.dt(teacherRemoteService.findByDeptId(deptId),Teacher.class);
			Set<String> teacherIds = EntityUtils.getSet(teachers, e->e.getId());
			if(CollectionUtils.isNotEmpty(teacherIds)){
				List<User> users = SUtils.dt(userRemoteService.findByOwnerIds(teacherIds.toArray(new String[0])),User.class);
				if(StringUtils.isNotBlank(teaName)){
					for (User user : users) {
						if(user.getRealName().indexOf(teaName)!=-1){
							userSet.add(user.getId());
						}else{
							userSet.add("11");
						}
					}
				}else{
					userSet=EntityUtils.getSet(users, e->e.getId());
				}
			}else{
				userSet.add("11");
			}
		}else{
			if(StringUtils.isNotBlank(teaName)){
				List<User> users=SUtils.dt(userRemoteService.findByUnitIdAndLikeRealName(getLoginInfo().getUnitId(), "%" + teaName + "%"),User.class);
				userSet=EntityUtils.getSet(users, e->e.getId());
				if(CollectionUtils.isEmpty(userSet)){
					userSet.add("11");
				}
			}
		}
		List<FamDearThreeInTwoReport> famDearThreeInTwoReports=famThreeInTwoReportService.getFamDearThreeInTwoReportsByUnitIdAndUserIds(getLoginInfo().getUnitId(), userSet.toArray(new String[0]), 
				startTime,endTime, title, page);
		map.put("famDearThreeInTwoReports", famDearThreeInTwoReports);
		sendPagination(request, map, page);
		return "familydear/threeInTwoReport/reportStatisticList.ftl";
	}
	
	public List<String> getYearList() {
		List<String> acadyears = new ArrayList<String>();
		String currentYear = DateUtils.date2String(new Date(), "yyyy");
		acadyears.add(Integer.parseInt(currentYear)+2+"");
		acadyears.add(Integer.parseInt(currentYear)+1+"");
		acadyears.add(Integer.parseInt(currentYear)+"");
		acadyears.add(Integer.parseInt(currentYear)-1+"");
		acadyears.add(Integer.parseInt(currentYear)-2+"");
		return acadyears;
	}

}
