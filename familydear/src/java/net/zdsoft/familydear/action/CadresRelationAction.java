package net.zdsoft.familydear.action;

import com.alibaba.fastjson.TypeReference;
import net.zdsoft.activity.entity.FamilyDearPermission;
import net.zdsoft.activity.service.FamilyDearPermissionService;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.familydear.common.FamDearConstant;
import net.zdsoft.familydear.dto.CadresRelationSearchDto;
import net.zdsoft.familydear.entity.FamilyDearMember;
import net.zdsoft.familydear.entity.FamilyDearObject;
import net.zdsoft.familydear.entity.FamilyDearServant;
import net.zdsoft.familydear.service.FamilyDearMemberService;
import net.zdsoft.familydear.service.FamilyDearObjectService;
import net.zdsoft.familydear.service.FamilyDearServantService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Designed By luf
 *
 * @author luf
 * @date 2019/5/8 19:07
 */
@Controller
@RequestMapping("/familydear/cadresRelation")
public class CadresRelationAction extends BaseAction {
    @Autowired
    private FamilyDearObjectService familyDearObjectService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private FamilyDearServantService familyDearServantService;
    @Autowired
    private TeacherRemoteService teacherRemoteService;
    @Autowired
    private DeptRemoteService deptRemoteService;
    @Autowired
    private FamilyDearMemberService familyDearMemberService;
    @Autowired
    private FamilyDearPermissionService familyDearPermissionService;

    private int currentPageIndex,currentPageSize;
    @RequestMapping("/cadresRelationManagerIndex")
    @ControllerInfo(value = "结亲对象管理")
    public String cadresRelationAdmin( ModelMap map) {
        if (isAdmin()) {
            map.put("isAdmin", true);
        }
        return "/familydear/cadresRelation/cadresRelationAdmin.ftl";
    }

    @RequestMapping("/head")
    @ControllerInfo(value = "结亲对象管理head")
    public String cadresRelationHead(String tabType, ModelMap map) {
        map.put("tabType", tabType);
        String unitId = getLoginInfo().getUnitId();
        List<FamilyDearPermission> list = familyDearPermissionService.getFamilyDearPermissionListByPermissionType(unitId,getLoginInfo().getUserId(),"5");
        if(CollectionUtils.isNotEmpty(list)){
            map.put("isAdmin", true);
        }else{
            map.put("teacherId", getLoginInfo().getOwnerId());
        }

        List<Dept> list1 = SUtils.dt(deptRemoteService.findByUnitId(getLoginInfo().getUnitId()),new TR<List<Dept>>() {});
        map.put("deptList",list1);
        return "/familydear/cadresRelation/cadresRelationHead.ftl";
    }

    public boolean isAdmin() {
        String unitId = getLoginInfo().getUnitId();
        List<FamilyDearPermission> list = familyDearPermissionService.getFamilyDearPermissionListByPermissionType(unitId,getLoginInfo().getUserId(),"5");
        if(CollectionUtils.isNotEmpty(list)) {
            return true;
        }else{
            return false;
        }

    }
    @RequestMapping("/list")
    @ControllerInfo(value = "结亲对象管理list")
    public String cadresRelationList(CadresRelationSearchDto cadresRelationSearchDto , String tabType, HttpServletRequest request, ModelMap map) {
        String unitId = getLoginInfo().getUnitId();
        Pagination page = createPagination();
        currentPageIndex = page.getPageIndex();
        currentPageSize = page.getPageSize();
        Set<String> teacherIdSet1 = new HashSet<>();
        List<FamilyDearObject> objectList = new ArrayList<>();
        if(StringUtils.isNotBlank(cadresRelationSearchDto.getDeptId())){
            List<Teacher> teacherList1 = SUtils.dt(teacherRemoteService.findListBy(new String[]{"deptId"},new String[]{cadresRelationSearchDto.getDeptId()}), Teacher.class);
            if(CollectionUtils.isNotEmpty(teacherList1)){
                teacherIdSet1 = teacherList1.stream().map(Teacher::getId).collect(Collectors.toSet());
                objectList = familyDearObjectService.getObjectsByCondiston(cadresRelationSearchDto,unitId,teacherIdSet1.toArray(new String[0]) ,page);
            }
        }else {
            objectList = familyDearObjectService.getObjectsByCondiston(cadresRelationSearchDto,unitId,null ,page);
        }
        Set<String> teachId = new HashSet<>();
        if (CollectionUtils.isNotEmpty(objectList)) {
            teachId = objectList.stream().map(FamilyDearObject::getTeacherId).collect(Collectors.toSet());
        }
        List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findListByIds(teachId.toArray(new String[0])), Teacher.class);
        Map<String, Teacher> teacherMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(teacherList)) {
            teacherMap = teacherList.stream().collect(Collectors.toMap(Teacher::getId, Function.identity()));
        }
        Map<String, Map<String, McodeDetail>> mcodeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeIds(new String[]{"DM-JQLB"}),
                new TypeReference<Map<String, Map<String, McodeDetail>>>() {
                });
        Map<String, McodeDetail> detailMap = mcodeMap.get("DM-JQLB");
        if (CollectionUtils.isNotEmpty(objectList)) {
            for (FamilyDearObject dearObject : objectList) {
                String teacherId = dearObject.getTeacherId();
                Teacher teacher = teacherMap.get(teacherId);
                if (teacher != null) {
                    dearObject.setTeacherName(teacher.getTeacherName());
                }
                String type = dearObject.getType();
                StringBuilder builder = new StringBuilder();

                if (StringUtils.isNotEmpty(type)) {
                    String[] types = type.split(",");
                    for (String str : types) {
                        McodeDetail detail = detailMap.get(str);
                        if (detail != null) {
                            builder.append(detail.getMcodeContent() + ",");
                        }
                    }
                    if (builder.length() >= 1) {
                        dearObject.setType(builder.substring(0,builder.length()-1));
                    }

                }

            }
        }
        map.put("Pagination", page);
        sendPagination(request, map, page);
        map.put("objectList",objectList);
        map.put("currentPageIndex",currentPageIndex);
        map.put("currentPageSize",currentPageSize);
        if (isAdmin()) {
            map.put("isAdmin", true);
        }
        if ("1".equals(tabType)) {
            return "/familydear/cadresRelation/cadresRelationList.ftl";
        }else{
            return "/familydear/cadresRelation/cadresRelationFlozenObjList.ftl";
        }


    }

    @RequestMapping("/add")
    @ControllerInfo(value = "结亲对象新增")
    public String addObj(String objId ,ModelMap map) {
        Pagination page=createPagination();
        currentPageIndex = page.getPageIndex();
        currentPageSize = page.getPageSize();
        FamilyDearObject familyDearObject = new FamilyDearObject();
        if (StringUtils.isNotEmpty(objId)) {
            List<McodeDetail> list = new ArrayList<>();
            familyDearObject = familyDearObjectService.findOne(objId);
//            if(StringUtils.isNotBlank(familyDearObject.getVillage())) {
//                list = SUtils.dt(mcodeRemoteService.findByMcodeContentLike("DM-XJJQC", familyDearObject.getVillage()), new TR<List<McodeDetail>>() {
//                });
//            }
//            if(CollectionUtils.isNotEmpty(list)) {
//                familyDearObject.setVillageValue(list.get(0).getThisId());
//            }
        }
        List<McodeDetail> mcodeDetails = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-JQLB") ,McodeDetail.class);
        map.put("mcodeDetails" ,mcodeDetails);
        map.put("familyDearObject" ,familyDearObject);
        map.put("currentPageIndex",currentPageIndex);
        map.put("currentPageSize",currentPageSize);
        return "/familydear/cadresRelation/relationObjectEdit.ftl";
    }
    @ResponseBody
    @RequestMapping("/object/save")
    @ControllerInfo(value = "保存")
    public String objectSave(FamilyDearObject familyDearObject) {
        try{

            if(StringUtils.isNotBlank(familyDearObject.getVillageValue())){
                familyDearObject.setVillage(SUtils.dc(mcodeRemoteService.findByMcodeAndThisId("DM-XJJQC", familyDearObject.getVillageValue()),McodeDetail.class).getMcodeContent());
            }



            if (ArrayUtils.isNotEmpty(familyDearObject.getTypeArray())) {
                StringBuilder builder = new StringBuilder();
                for (String str:familyDearObject.getTypeArray() ) {
                    builder.append(str + ",");

                }
                familyDearObject.setType(builder.substring(0,builder.length() - 1));
            }
            List<FamilyDearObject> list = null;
            if (StringUtils.isNotEmpty(familyDearObject.getId())) {
                if (StringUtils.isNotEmpty(familyDearObject.getIdentityCard())) {
                    list = familyDearObjectService.getDearObjByIdentityCard(familyDearObject.getIdentityCard(), familyDearObject.getId());
                }

                familyDearObject.setModifyTime(new Date());
            }else{
                if (StringUtils.isNotEmpty(familyDearObject.getIdentityCard())) {
                    list = familyDearObjectService.findListBy("identityCard", familyDearObject.getIdentityCard());
                }
                familyDearObject.setId(UuidUtils.generateUuid());
                familyDearObject.setCreationTime(new Date());
                familyDearObject.setState(FamDearConstant.OBJECT_STATE_INITIA);
            }
            familyDearObject.setUnitId(getLoginInfo().getUnitId());
            if (CollectionUtils.isNotEmpty(list)) {
                return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("操作失败！该身份证号已存在"));
            }
            if(StringUtils.isNotBlank(familyDearObject.getName())){
                String name=familyDearObject.getName();
                name = StringUtils.replace(name, "&quot;", "\"");
                name = StringUtils.replace(name, "&lt;", "<");
                name = StringUtils.replace(name, "&gt;", ">");
                name = StringUtils.replace(name, "&gt;", ">");
                name = StringUtils.replace(name, "&sim;", "~");
                name = StringUtils.replace(name, "&and;", "^");
                name = StringUtils.replace(name, "&hellip;", "...");
                name = StringUtils.replace(name, "&middot;", "·");
                familyDearObject.setName(name);
            }
            familyDearObjectService.save(familyDearObject);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
		return returnSuccess();

    }
    @RequestMapping("/cadreAddLink")
    @ControllerInfo(value = "结亲干部新增")
    public String addCadreLink(String objId ,ModelMap map) {
        map.put("objId" ,objId);
        Pagination page=createPagination();
        currentPageIndex = page.getPageIndex();
        currentPageSize = page.getPageSize();
        map.put("currentPageIndex",currentPageIndex);
        map.put("currentPageSize",currentPageSize);
        return "/familydear/cadresRelation/cadresRelationSet.ftl";
    }
    @RequestMapping("/cadreReleaseLink")
    @ControllerInfo(value = "解除结亲")
    public String releaseCadreLink(String objIds ,ModelMap map) {
        map.put("objIds" ,objIds);
        Pagination page=createPagination();
        currentPageIndex = page.getPageIndex();
        currentPageSize = page.getPageSize();
        map.put("currentPageIndex",currentPageIndex);
        map.put("currentPageSize",currentPageSize);
        return "/familydear/cadresRelation/cadreRelationRelease.ftl";
    }
    @RequestMapping("/objMemberLink")
    @ControllerInfo(value = "家庭成员")
    public String objectMemberLink(String objId ,ModelMap map) {
        Pagination page=createPagination();
        currentPageIndex = page.getPageIndex();
        currentPageSize = page.getPageSize();
        map.put("objId" ,objId);
        List<FamilyDearMember> memberList = familyDearMemberService.findListBy("objectId", objId);
        map.put("memberList", memberList);
        FamilyDearObject familyDearObject = familyDearObjectService.findOne(objId);
        map.put("familyDearObject" ,familyDearObject);
        List<McodeDetail> mcodelList = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-GX"),McodeDetail.class);
        map.put("mcodelList", mcodelList);
        map.put("currentPageIndex",currentPageIndex);
        map.put("currentPageSize",currentPageSize);
        return "/familydear/cadresRelation/familyMemberList.ftl";
    }
    @ResponseBody
    @RequestMapping("/objMemberAdd")
    @ControllerInfo(value = "保存")
    public String familyMemeberAdd(FamilyDearObject familyDearObject) {

        try{

            familyDearObjectService.saveFamilyMember(familyDearObject);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }
    @ResponseBody
    @RequestMapping("/objMemberDelete")
    @ControllerInfo(value = "删除")
    public String familyMemeberDelete(String ids) {
        try{
            if (StringUtils.isNotEmpty(ids)) {
                String[] idArr = ids.split(",");
                List<FamilyDearMember> memberList = familyDearMemberService.findListByIds(idArr);
                familyDearMemberService.deleteAll(memberList.toArray(new FamilyDearMember[0]));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }
    @RequestMapping("/historyCadreLink")
    @ControllerInfo(value = "历史结亲")
    public String historyCadreLink(String objId ,ModelMap map) {

        List<FamilyDearServant> cadreList= familyDearServantService.findListBy(new String[]{"objectId", "isDeleted"}, new Object[]{objId, FamDearConstant.RELEASE_RELATION});
        String unitId = getLoginInfo().getUnitId();
        List<Dept> deptList = SUtils.dt(deptRemoteService.findByUnitId(unitId), Dept.class);
        Map<String, Dept> deptMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(deptList)) {
            deptMap = deptList.stream().collect(Collectors.toMap(Dept::getId, Function.identity()));
        }
        if (CollectionUtils.isNotEmpty(cadreList)) {

            Set<String> teachId = new HashSet<>();
            if (CollectionUtils.isNotEmpty(cadreList)) {
                teachId = cadreList.stream().map(FamilyDearServant::getTeacherId).collect(Collectors.toSet());
            }
            List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findListByIds(teachId.toArray(new String[0])), Teacher.class);
            Map<String, Teacher> teacherMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(teacherList)) {
                teacherMap = teacherList.stream().collect(Collectors.toMap(Teacher::getId, Function.identity()));
            }

            for (FamilyDearServant servant : cadreList) {
                String teacherId = servant.getTeacherId();
                Teacher teacher = teacherMap.get(teacherId);
                if (teacher != null) {
                    servant.setTeacherName(teacher.getTeacherName());
                    servant.setMobilePhone(teacher.getMobilePhone());
                    Dept dept = deptMap.get(teacher.getDeptId());
                    if (dept != null) {
                        servant.setDeptName(dept.getDeptName());
                    }
                }
            }
        }
        map.put("cadreList", cadreList);
        return "/familydear/cadresRelation/historyCadreList.ftl";
    }

    @ResponseBody
    @RequestMapping("/cadreAdd")
    @ControllerInfo(value = "保存")
    public String cadreAdd(String objId, String teacherId) {
        try{

            familyDearObjectService.saveCadre(objId,teacherId);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }
    @ResponseBody
    @RequestMapping("/cadreRelease")
    @ControllerInfo(value = "解除")
    public String cadreRelease(String remark, String objIds) {
        try{

            if (StringUtils.isNotEmpty(objIds)) {
                String[] idArr = objIds.split(",");

                List<FamilyDearObject> list = familyDearObjectService.getAllByidsAndState(idArr, FamDearConstant.OBJECT_STATE_INITIA);

                if (CollectionUtils.isNotEmpty(list)) {
                    return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("操作失败！已设置结亲干部才能解除。"));
                }
                familyDearObjectService.saveReleaseRemark(remark,idArr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }
    @ResponseBody
    @RequestMapping("/objFlozen")
    @ControllerInfo(value = "冻结")
    public String objectFlozenOrRelease(String objIds,String state) {
        try{
            if (StringUtils.isNotEmpty(objIds)) {
                String[] idArr = objIds.split(",");
                List<FamilyDearObject> list = familyDearObjectService.getAllByidsAndState(idArr, FamDearConstant.OBJECT_STATE_RELATIONED);

                if (CollectionUtils.isNotEmpty(list)) {
                    return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("操作失败！未设置过结亲干部才能冻结。"));
                }
                familyDearObjectService.updateFlozenObject(idArr,state);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }
    @ResponseBody
    @RequestMapping("/objFlozenDel")
    public String objFlozenDel(String objIds){
        try{
            if (StringUtils.isNotEmpty(objIds)) {
                FamilyDearObject familyDearObject = familyDearObjectService.findOne(objIds);
                familyDearObjectService.delete(familyDearObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();

    }

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    public void setCurrentPageIndex(int currentPageIndex) {
        this.currentPageIndex = currentPageIndex;
    }

    public int getCurrentPageSize() {
        return currentPageSize;
    }

    public void setCurrentPageSize(int currentPageSize) {
        this.currentPageSize = currentPageSize;
    }
}
