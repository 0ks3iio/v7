package net.zdsoft.basedata.action;

import net.zdsoft.basedata.dto.StudentSelectSubjectDto;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.service.*;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

@Controller
@RequestMapping("/basedata/stuselect")
public class StudentSelectSubjectAction extends BaseAction {

    @Autowired
    private GradeService gradeService;
    @Autowired
    private SemesterService semesterService;
    @Autowired
    private ClassService classService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private StudentSelectSubjectService studentSelectSubjectService;
    @Autowired
    private StudentService studentService;

    @RequestMapping("/index/page")
    public String index(ModelMap map) {
        List<String> acadyearList = semesterService.findAcadeyearList();

        if(CollectionUtils.isEmpty(acadyearList)){
            return errorFtl(map,"学年学期不存在");
        }

        String unitId = getLoginInfo().getUnitId();
        Semester semester = semesterService.findCurrentSemester(2,unitId);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);

        List<Grade> gradeList = getGradeList(unitId);
        map.put("gradeList", gradeList);
        if(gradeList.size() != 0) {
            List<String> gradeIds = new ArrayList<String>();
            gradeIds.add(gradeList.get(0).getId());
            List<Clazz> clazzList = classService.findByGradeIdIn(gradeIds.toArray(new String[gradeIds.size()]));
            map.put("clazzList", clazzList);
        }
        return "basedata/student/studentSelectIndex.ftl";
    }

    @RequestMapping("/detail/index/page")
    public String showStuSelectDetail(ModelMap map, HttpServletRequest request) {
        String acadyear = request.getParameter("acadyear");
        String semester = request.getParameter("semester");
        String unitId = getLoginInfo().getUnitId();
        String classId = request.getParameter("classId");
        List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(unitId), new TR<List<Course>>() {});
        Map<String,String> courseNameMap = EntityUtils.getMap(courseList, Course::getId,Course::getSubjectName);
        map.put("courseList", courseList);
        map.put("courseNameMap", courseNameMap);
//        List<StudentSelectSubjectDto> selectList = studentSelectSubjectService.findStuSelectByClass(acadyear, Integer.valueOf(semester), unitId, classId);
        List<StudentSelectSubject> selectSubjectList = studentSelectSubjectService.findListBySchoolIdWithMaster(acadyear, Integer.valueOf(semester), unitId);
        if(CollectionUtils.isEmpty(selectSubjectList)) {
        	selectSubjectList=new ArrayList<StudentSelectSubject>();
        }
        List<StudentSelectSubjectDto> selectList = studentSelectSubjectService.findStuSelectByClass(selectSubjectList, classId);
        map.put("selectList", selectList);
        return "basedata/student/studentSelectDetail.ftl";
    }

    @RequestMapping("/edit/page")
    public String editSelect(String studentId, String acadyear, String semester, ModelMap map) {
        Student student = studentService.findOne(studentId);
        map.put("student", student);
        String unitId = getLoginInfo().getUnitId();
        List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(unitId), new TR<List<Course>>() {});
        map.put("coursesList", courseList);
        List<StudentSelectSubject> selectSubjectList = studentSelectSubjectService.findListBySchoolIdWithMaster(acadyear, Integer.valueOf(semester), unitId);
        if(CollectionUtils.isEmpty(selectSubjectList)) {
        	selectSubjectList=new ArrayList<StudentSelectSubject>();
        }
        List<StudentSelectSubjectDto> selectList = studentSelectSubjectService.findStuSelectByClass(selectSubjectList, student.getClassId());
        StudentSelectSubjectDto oneSelect = null;
        for (StudentSelectSubjectDto one : selectList) {
            if (studentId.equals(one.getStudentId())) {
                oneSelect = one;
            }
        }
        map.put("oneSelect", oneSelect);
        /**
         * TODO
         * 备注
         */
        String mark = "";
        map.put("mark", StringUtils.isBlank(mark)?"无":mark);
        return "basedata/student/studentSelectEdit.ftl";
    }

    @ResponseBody
    @RequestMapping("/edit/save")
    public String editSave(String acadyear, String semester, String gradeId, String studentId, String subjectIds){
        try {
            studentSelectSubjectService.updateStudentSelect(acadyear, Integer.valueOf(semester), gradeId, studentId, subjectIds);
        } catch (Exception e) {
            e.printStackTrace();
            return error("操作失败");
        }
        return success("操作成功");
    }

    @RequestMapping("/export")
    @ResponseBody
    public String exportStudentSelect(String acadyear, String semester, String classId, HttpServletResponse response) {
        String unitId = getLoginInfo().getUnitId();
        Clazz clazz = SUtils.dc(classRemoteService.findOneById(classId), Clazz.class);
        List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(unitId), new TR<List<Course>>() {});
//        courseList.sort(new Comparator<Course>() {
//            @Override
//            public int compare(Course o1, Course o2) {
//                return o1.getOrderId()-o2.getOrderId();
//            }
//        });
//        Map<String,String> courseNameMap = EntityUtils.getMap(courseList, Course::getId,Course::getSubjectName);
       // List<StudentSelectSubjectDto> selectList = studentSelectSubjectService.findStuSelectByClass(acadyear, Integer.valueOf(semester), unitId, classId);
        List<StudentSelectSubject> selectSubjectList = studentSelectSubjectService.findListBySchoolIdWithMaster(acadyear, Integer.valueOf(semester), unitId);
        if(CollectionUtils.isEmpty(selectSubjectList)) {
        	selectSubjectList=new ArrayList<StudentSelectSubject>();
        }
        List<StudentSelectSubjectDto> selectList = studentSelectSubjectService.findStuSelectByClass(selectSubjectList, classId);
        //表头
        List<String> titleList = new ArrayList<String>();
        titleList.add("班级");
        titleList.add("姓名");
        titleList.add("学号");
        titleList.add("性别");
        titleList.addAll(EntityUtils.getList(courseList, Course::getSubjectName));
        List<List<String>> datas = new ArrayList<List<String>>();
        // 数据组装
        if (CollectionUtils.isNotEmpty(selectList)) {
            for (StudentSelectSubjectDto one : selectList) {
                List<String> inList = new ArrayList<String>() ;
                inList.add(one.getClassName());
                inList.add(one.getStudentName());
                inList.add(one.getStudentCode());
                if ("1".equals(one.getSex())) {
                	 inList.add("男");
                } else if ("2".equals(one.getSex())){
                	 inList.add("女");
                } else{
                	inList.add("");
                }
                for (Course oneCourse : courseList) {
                    if (CollectionUtils.isNotEmpty(one.getSelectList())&&one.getSelectList().contains(oneCourse.getId())) {
                    	inList.add(oneCourse.getSubjectName());
                    }else{
                    	inList.add("");
                    }
                }
                datas.add(inList);
            }
        }
        HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("学生选课结果");
		//标题行固定
		sheet.createFreezePane(0, 1);
		int rownum=0;
		HSSFRow rowTitle = sheet.createRow(rownum++);
		for(int i=0;i<titleList.size();i++){
			sheet.setColumnWidth(i, 10 * 256);//列宽
			HSSFCell cell = rowTitle.createCell(i);
			cell.setCellValue(new HSSFRichTextString(titleList.get(i)));
		}
		if(CollectionUtils.isNotEmpty(datas)){
			for (List<String> data : datas) {
				HSSFRow inRow = sheet.createRow(rownum++);
				for(int i=0;i<titleList.size();i++){
					HSSFCell inCell = inRow.createCell(i);
					inCell.setCellValue(new HSSFRichTextString(data.get(i)));
				}
			}
		}
		ExportUtils.outputData(workbook, clazz.getClassNameDynamic()+"选课结果", response);
        return returnSuccess();
    }

    private List<Grade> getGradeList(String unitId) {
        List<String> ids = new ArrayList<String>();
        ids.add(unitId);
        List<School> schoolList = schoolService.findListByIdIn(ids.toArray(new String[ids.size()]));
        School school = schoolList.get(0);
        String sections = school.getSections();
        String[] section = sections.split(",");
        List<Integer> sectionList = new ArrayList<Integer>();
        for(String string : section) {
            sectionList.add(Integer.parseInt(string));
        }
        List<Grade> gradeList = new ArrayList<Grade>();
        gradeList = gradeService.findByUnitId(getLoginInfo().getUnitId());
        for (int i = 0; i < gradeList.size(); i++) {
            int flag = 0;
            for (Integer integer : sectionList) {
                if(gradeList.get(i).getSection() == integer) {
                    flag = 1;
                    break;
                }
            }
            if(flag == 0){
                gradeList.remove(i);
                i--;
            }
        }
        return gradeList;
    }

}
