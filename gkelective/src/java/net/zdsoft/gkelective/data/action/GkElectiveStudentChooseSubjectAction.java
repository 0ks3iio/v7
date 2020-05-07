/*
 * @(#)GkElectiveStudentChooseSubjectAction.java    Created on 2017年2月18日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id: GkElectiveStudentChooseSubjectAction.java 9578 2018-05-31 08:40:11Z zengzt $
 */
package net.zdsoft.gkelective.data.action;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.gkelective.data.constant.GkElectveConstants;
import net.zdsoft.gkelective.data.dto.GkSubjectArrangeDto;
import net.zdsoft.gkelective.data.entity.GkLimitSubject;
import net.zdsoft.gkelective.data.entity.GkRelationship;
import net.zdsoft.gkelective.data.entity.GkResult;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;
import net.zdsoft.gkelective.data.service.GkLimitSubjectService;
import net.zdsoft.gkelective.data.service.GkRelationshipService;
import net.zdsoft.gkelective.data.service.GkResultService;
import net.zdsoft.gkelective.data.service.GkSubjectArrangeService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.TypeReference;

/**
 * @author wangk
 * @version $Revision: 9578 $, $Date: 2018-05-31 16:40:11 +0800 (周四, 31 5月 2018) $
 */
@Controller
@RequestMapping("/gkelective")
public class GkElectiveStudentChooseSubjectAction extends BaseAction {

    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private GkSubjectArrangeService gkSubjectArrangeService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private SemesterRemoteService semesterService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private GkResultService gkResultService;
    @Autowired
    private GkRelationshipService gkRelationshipService;
    @Autowired
    private GkLimitSubjectService gkLimitSubjectService;

    @RequestMapping("/studentChooseSubject/index/page")
    @ControllerInfo(value = "学生选课index")
    public String showIndex(ModelMap map) {
    	List<String> acadyearList = SUtils.dt(semesterService.findAcadeyearList(), new TR<List<String>>(){}); 
        if(CollectionUtils.isEmpty(acadyearList)){
			return errorFtl(map,"学年学期不存在");
		} 
        Semester semester = SUtils.dc(semesterService.getCurrentSemester(2), Semester.class);
		String acadyearSearch = semester.getAcadyear();
		String semesterSearch=semester.getSemester()+"";
		map.put("acadyearList", acadyearList);
		map.put("acadyearSearch", acadyearSearch);
		map.put("semesterSearch", semesterSearch);
        return "/gkelective/studentChooseSubject/studentChooseSubjectIndex.ftl";
    }

    @RequestMapping("/studentChooseSubject/list/page")
    @ControllerInfo(value = "学生选课List")
    public String showList(String acadyearSearch,String semesterSearch,String canEdit,ModelMap map) {
    	if(StringUtils.isBlank(canEdit)){
    		map.put("canEdit", "0");
    	}else{
    		map.put("canEdit", canEdit);
    	}
    	LoginInfo loginInfo = getLoginInfo();
    	if(loginInfo.getOwnerType() != User.OWNER_TYPE_STUDENT){
    		return errorFtl(map,"你登录的不是学生账号！");
    	}
        String studentId = getLoginInfo().getOwnerId();
        Student student = SUtils.dt(studentRemoteService.findOneById(studentId), new TypeReference<Student>() {
        });
        if (student == null) {
            return errorFtl(map,"帐号已被删除！");
        }
        Clazz calzz = SUtils.dt(classRemoteService.findOneById(student.getClassId()), new TypeReference<Clazz>() {
        });
        Grade grade = SUtils.dt(gradeRemoteService.findOneById(calzz.getGradeId()), new TypeReference<Grade>() {
        });
        GkSubjectArrangeDto gkSubjectArrange = gkSubjectArrangeService.findByGradeId(calzz.getGradeId());
        // getGradeId="14781382457241076396216000012501" 测试账号sx201 12345678
        if (gkSubjectArrange.getGsaEnt() != null && NumberUtils.toInt(gkSubjectArrange.getGsaEnt().getIsUsing()+"") == 1) {
//        	gkSubjectArrange.getGsaEnt().setArrangeName(grade.getOpenAcadyear().substring(0, 4)+"级"+grade.getGradeName()+"新高考项目");
        	map.put("arrangeId", gkSubjectArrange.getGsaEnt().getId());
        	map.put("notice", gkSubjectArrange.getGsaEnt().getNotice());
            List<GkRelationship>  gkrsent =gkRelationshipService.findByTypePrimaryIdIn(GkElectveConstants.RELATIONSHIP_TYPE_03, gkSubjectArrange.getGsaEnt().getId());
            Set<String> subIds = EntityUtils.getSet(gkrsent, "relationshipTargetId");
            List<Course> subList = SUtils.dt(courseRemoteService.findListByIds(subIds.toArray(new String[0])),
                    new TR<List<Course>>() {});
            map.put("subList", subList);
            Map<String,String> courseMap = new HashMap<String,String>();
            for (Course course : subList) {
    			courseMap.put(course.getId(), course.getSubjectName());
    		}
            List<GkLimitSubject> GkLimitSubjectList = gkLimitSubjectService.findBySubjectArrangeId(gkSubjectArrange.getGsaEnt().getId());
            String limitSubjectName = "";
            for (GkLimitSubject gkLimitSubject : GkLimitSubjectList) {
            	String[] subjectIds = gkLimitSubject.getSubjectVal().split(",");
            	String subName = "";
            	for (String str : subjectIds) {
            		subName += courseMap.get(str) + "、";
            	}
            	subName = "(" + subName.substring(0, subName.length()-1) + ")";
            	limitSubjectName += subName + ",";
            }
            if (StringUtils.isNotBlank(limitSubjectName)) {
            	limitSubjectName = limitSubjectName.substring(0, limitSubjectName.length()-1);
            }
            map.put("limitSubjectName", limitSubjectName);
            GkSubjectArrangeDto gkdto = new GkSubjectArrangeDto();
            gkdto.setGsaEnt(gkSubjectArrange.getGsaEnt());
            gkdto.setGksubList(gkSubjectArrange.getGksubList());
            gkdto.setGradeName(grade.getGradeName());
            student.setClassName(calzz.getClassName());
            gkdto.setStu(student);
            
            List<GkResult> gkResult = gkResultService.findGkByStuId(new String[]{studentId}, gkSubjectArrange.getGsaEnt().getId());
            map.put("gkdto", gkdto);
            map.put("gkResult", gkResult);
            
            boolean isShowCommitButton;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String startTime = dateFormat.format(gkSubjectArrange.getGsaEnt().getStartTime());
            String limitTime = dateFormat.format(gkSubjectArrange.getGsaEnt().getLimitedTime());
            String nowTime = dateFormat.format(new Date());
		    try {
		    	Date startTimeDate=dateFormat.parse(startTime);
		    	Date limitTimeDate=dateFormat.parse(limitTime);
				Date nowTimeDate=dateFormat.parse(nowTime);
				
				if(startTimeDate.getTime()<=nowTimeDate.getTime() && nowTimeDate.getTime()<limitTimeDate.getTime()){
					isShowCommitButton = true;
				}else{
					isShowCommitButton = false;
				}
				 map.put("isShowCommitButton", isShowCommitButton);
			} catch (ParseException e) {
				e.printStackTrace();
			}
        }
        return "/gkelective/studentChooseSubject/studentChooseSubjectList.ftl";
    }
    
    @ResponseBody
    @RequestMapping("/studentChooseSubject/save")
    @ControllerInfo(value = "学生选课save")
    public String doSave(String arrangeId, String subjectId, ModelMap map) {
        if (StringUtils.isEmpty(arrangeId)) {
            return error("参数为空");
        }
        if (StringUtils.isEmpty(subjectId)) {
            return error("参数为空");
        }
        
        try {
        	//判断限选
        	List<GkLimitSubject> GkLimitSubjectList = gkLimitSubjectService.findBySubjectArrangeId(arrangeId);
        	List<String> subjectIdsList = Arrays.asList(subjectId.split(","));
        	for (GkLimitSubject gkLimitSubject : GkLimitSubjectList) {
        		List<String> limitSubjectList = Arrays.asList(gkLimitSubject.getSubjectVal().split(","));
        		if (limitSubjectList.containsAll(subjectIdsList)) {
        			 return returnError("保存失败","您选择的是限选组合，请调整！");
        		}
        	}
        	//判断时间
        	GkSubjectArrange gkSubjectArrange = gkSubjectArrangeService.findArrangeById(arrangeId);
        	boolean isShowCommitButton;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String startTime = dateFormat.format(gkSubjectArrange.getStartTime());
            String limitTime = dateFormat.format(gkSubjectArrange.getLimitedTime());
            String nowTime = dateFormat.format(new Date());
            
            Date startTimeDate=dateFormat.parse(startTime);
            Date limitTimeDate=dateFormat.parse(limitTime);
    	    Date nowTimeDate=dateFormat.parse(nowTime);
    		if(startTimeDate.getTime()<= nowTimeDate.getTime() && nowTimeDate.getTime()<limitTimeDate.getTime()){
    			isShowCommitButton = true;
    		}else{
    			isShowCommitButton = false;
    		}
    		map.put("isShowCommitButton", isShowCommitButton);       	
        	if(false == isShowCommitButton){
        		 return returnError("保存失败","已超过选课截止时间！");
        	}
            String studentId = getLoginInfo().getOwnerId();
            String[] subjectIds = subjectId.split(",");
            Student student = SUtils.dt(studentRemoteService.findOneById(studentId), new TypeReference<Student>() {});
            GkResult gkResult;
            List<GkResult> gkResultList = new ArrayList<GkResult>();
            for (int i = 0; i < subjectIds.length; i++) {
                gkResult = new GkResult();
                gkResult.setId(UuidUtils.generateUuid());
                gkResult.setSubjectArrangeId(arrangeId);
                gkResult.setStudentId(studentId);
                gkResult.setSubjectId(subjectIds[i]);
                gkResult.setCreationTime(new Date());
                gkResult.setModifyTime(new Date());
                gkResultList.add(gkResult);
            }
            gkResultService.saveGkResultList(gkResultList,studentId,arrangeId);
        }
        catch (Exception e) {
            return error(e.getMessage());
        }
        return returnSuccess();
    }
}
