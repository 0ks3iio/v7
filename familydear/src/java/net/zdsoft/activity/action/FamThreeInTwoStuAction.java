package net.zdsoft.activity.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.activity.entity.FamDearThreeInTwoStu;
import net.zdsoft.activity.entity.FamDearThreeInTwoStuMember;
import net.zdsoft.activity.entity.FamilyDearPermission;
import net.zdsoft.activity.service.FamThreeInTwoStuMemberService;
import net.zdsoft.activity.service.FamThreeInTwoStuService;
import net.zdsoft.activity.service.FamilyDearPermissionService;
import net.zdsoft.basedata.entity.StorageDir;
import net.zdsoft.basedata.remote.service.StorageDirRemoteService;
import net.zdsoft.familydear.entity.FamDearAttachment;
import net.zdsoft.familydear.service.FamDearAttachmentService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StorageFileUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/familydear/threeInTwoStu")
public class FamThreeInTwoStuAction extends BaseAction {
	
	@Autowired
	private FamilyDearPermissionService familyDearPermissionService;
	@Autowired
	private FamThreeInTwoStuService famThreeInTwoStuService;
	@Autowired
	private FamThreeInTwoStuMemberService famThreeInTwoStuMemberService;
	@Autowired
	private McodeRemoteService mcodeRemoteService;
	@Autowired
	private FamDearAttachmentService famDearAttachmentService;
	@Autowired
	private StorageDirRemoteService storageDirRemoteService;

	@RequestMapping("/edu/stuManage/page")
	public String threeInTwoStuIndex(ModelMap map,HttpServletRequest request){
		List<FamilyDearPermission> familyDearPermissions = familyDearPermissionService.getFamilyDearPermissionListByPermissionType(getLoginInfo().getUnitId(),getLoginInfo().getUserId(),"7");
		map.put("hasPermission",false);
        if(CollectionUtils.isNotEmpty(familyDearPermissions)){
            map.put("hasPermission",true);
        }
        String currentPageIndex=request.getParameter("currentPageIndex");
		String currentPageSize=request.getParameter("currentPageSize");
		String stuName=request.getParameter("stuName");
		String ganbName=request.getParameter("ganbName");
		String stuPhone=request.getParameter("stuPhone");
		map.put("currentPageIndex",currentPageIndex);
        map.put("currentPageSize",currentPageSize);
        map.put("stuName",stuName);
        map.put("ganbName",ganbName);
        map.put("stuPhone",stuPhone);
		return "familydear/threeInTwoStu/threeInTwoStuIndex.ftl";
	}
	
	@RequestMapping("/edu/stuManage/List")
	public String threeInTwoStuList(HttpServletRequest req,ModelMap map,String stuName,String ganbName,String stuPhone){
		List<FamilyDearPermission> familyDearPermissions = familyDearPermissionService.getFamilyDearPermissionListByPermissionType(getLoginInfo().getUnitId(),getLoginInfo().getUserId(),"7");
		String currentPageIndexx=req.getParameter("currentPageIndex");
		String currentPageSizex=req.getParameter("currentPageSize");
		Pagination page = createPagination();
		int index = NumberUtils.toInt(req.getParameter("_pageIndex"));
		int row = NumberUtils.toInt(req.getParameter("_pageSize"));
		if(StringUtils.isNotBlank(currentPageIndexx)&&index <= 0){
			page.setPageIndex(Integer.valueOf(currentPageIndexx));
		}
		if(StringUtils.isNotBlank(currentPageSizex)&&row <= 0){
			page.setPageSize(Integer.valueOf(currentPageSizex));
		}
		List<FamDearThreeInTwoStu> famDearThreeInTwoStus=new ArrayList<>();
		if(CollectionUtils.isNotEmpty(familyDearPermissions)){//管理员
			famDearThreeInTwoStus=famThreeInTwoStuService.getDearStuByUnitIdAndOthers(getLoginInfo().getUnitId(), getLoginInfo().getUserId(), stuName, ganbName, stuPhone, true, page);
		}else{//普通老师
			famDearThreeInTwoStus=famThreeInTwoStuService.getDearStuByUnitIdAndOthers(getLoginInfo().getUnitId(), getLoginInfo().getUserId(), stuName, ganbName, stuPhone, false, page);
		}
		map.put("famDearThreeInTwoStus", famDearThreeInTwoStus);
		map.put("hasPermission",false);
        if(CollectionUtils.isNotEmpty(familyDearPermissions)){
            map.put("hasPermission",true);
        }
        map.put("Pagination", page);
        sendPagination(req, map, page);
        int currentPageIndex=page.getPageIndex();
		int currentPageSize=page.getPageSize();
		map.put("currentPageIndex",currentPageIndex);
        map.put("currentPageSize",currentPageSize);
		return "familydear/threeInTwoStu/threeInTwoStuList.ftl";
	}
	
	@RequestMapping("/edu/edit/page")
    public String threeInTwoEdit(ModelMap map,String id,HttpServletRequest request){
		List<FamDearThreeInTwoStuMember> memberList=new ArrayList<>();
		if(StringUtils.isNotBlank(id)){
			FamDearThreeInTwoStu famDearThreeInTwoStu=famThreeInTwoStuService.findOne(id);
			memberList = famThreeInTwoStuMemberService.findListBy("stuId", id);
			map.put("famDearThreeInTwoStu", famDearThreeInTwoStu);
		}else{
			FamDearThreeInTwoStu famDearThreeInTwoStu=new FamDearThreeInTwoStu();
			String uuid = UUID.randomUUID().toString().replaceAll("-","");
			famDearThreeInTwoStu.setId(uuid);
			map.put("famDearThreeInTwoStu", famDearThreeInTwoStu);
		}
		List<FamDearAttachment> atts = new ArrayList<>();
        atts = famDearAttachmentService.getAttachmentByObjId(id, "famDearThreeInTwoStu");
        map.put("actDetails", atts);
		List<McodeDetail> mcodelList = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-GX"),McodeDetail.class);
        map.put("mcodelList", mcodelList);
		map.put("memberList", memberList);
		map.put("memSize", CollectionUtils.isNotEmpty(memberList)?memberList.size()+1:3);
		String currentPageIndex=request.getParameter("currentPageIndex");
		String currentPageSize=request.getParameter("currentPageSize");
		String stuName=request.getParameter("stuName");
		String ganbName=request.getParameter("ganbName");
		String stuPhone=request.getParameter("stuPhone");
		map.put("currentPageIndex",currentPageIndex);
        map.put("currentPageSize",currentPageSize);
        map.put("stuName",stuName);
        map.put("ganbName",ganbName);
        map.put("stuPhone",stuPhone);
		return "familydear/threeInTwoStu/threeInTwoStuEdit.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/edu/delete/page")
    public String threeInTwoDelete(ModelMap map,String id){
		try {
			FamDearThreeInTwoStu famDearThreeInTwoStu=famThreeInTwoStuService.findOne(id);
			famDearThreeInTwoStu.setIsDelete("1");
			famThreeInTwoStuService.save(famDearThreeInTwoStu);
		} catch (Exception e) {
			e.printStackTrace();
			return error("删除失败！");
		}
		return success("删除成功！");
	}
	
	@RequestMapping("/cadreAddLink")
	@ControllerInfo(value = "干部设置")
	public String addCadreLink(String objId ,HttpServletRequest request,ModelMap map) {
	    map.put("objId" ,objId);
	    String currentPageIndex=request.getParameter("currentPageIndex");
		String currentPageSize=request.getParameter("currentPageSize");
		map.put("currentPageIndex",currentPageIndex);
        map.put("currentPageSize",currentPageSize);
	    return "/familydear/threeInTwoStu/cadresRelationSet.ftl";
	}
	
	@ResponseBody
    @RequestMapping("/cadreAdd")
    @ControllerInfo(value = "保存")
    public String cadreAdd(String objId, String teacherId) {
        try{
            famThreeInTwoStuService.saveCadre(objId,teacherId);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }
	
	@ResponseBody
    @RequestMapping("/edu/stuManage/save")
    @ControllerInfo(value = "保存")
    public String stuManageSave(FamDearThreeInTwoStu famDearThreeInTwoStu) {
        try{

            List<FamDearThreeInTwoStu> list = null;
            FamDearThreeInTwoStu old=famThreeInTwoStuService.findOne(famDearThreeInTwoStu.getId());
            if (old!=null) {
                if (StringUtils.isNotEmpty(famDearThreeInTwoStu.getIdentityCard())) {
                    list = famThreeInTwoStuService.getDearStuByIdentityCard(famDearThreeInTwoStu.getIdentityCard(), famDearThreeInTwoStu.getId());
                }

            }else{
                if (StringUtils.isNotEmpty(famDearThreeInTwoStu.getIdentityCard())) {
                    list = famThreeInTwoStuService.findListBy("identityCard", famDearThreeInTwoStu.getIdentityCard());
                }
                famDearThreeInTwoStu.setCreateTime(new Date());;
                famDearThreeInTwoStu.setCreateUserId(getLoginInfo().getUserId());
            }
            famDearThreeInTwoStu.setUnitId(getLoginInfo().getUnitId());
            famDearThreeInTwoStu.setIsDelete("0");
            if (CollectionUtils.isNotEmpty(list)) {
                return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("操作失败！该身份证号已存在"));
            }
            famThreeInTwoStuService.saveFamilyMember(famDearThreeInTwoStu);
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
            String objType = request.getParameter("objType");
            String objId = request.getParameter("objId");
            List<FamDearAttachment> atts = new ArrayList<>();
            atts = famDearAttachmentService.getAttachmentByObjId(objId, "famDearThreeInTwoStu");
            if(CollectionUtils.isNotEmpty(atts)){
            	for (FamDearAttachment famDearAttachment : atts) {
					famDearAttachment.setIsDelete("1");
				}
            	famDearAttachmentService.saveAll(atts.toArray(new FamDearAttachment[0]));
            }
            for (int i=0;i<files.size();i++) {
                MultipartFile file = files.get(i);
                FamDearAttachment att = new FamDearAttachment();
                att.setIsDelete("0");
                att.setObjecttype(objType);
                att.setObjId(objId);
                att.setUnitId(getLoginInfo().getUnitId());
                try {
                    famDearAttachmentService.saveAttachment(att, file,true);
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
        atts = famDearAttachmentService.getAttachmentByObjId(id, "famDearThreeInTwoStu");
        modelMap.put("id",id);
        modelMap.put("actDetails", atts);
        return "familydear/threeInTwoStu/famDearImages.ftl";
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
    
}
