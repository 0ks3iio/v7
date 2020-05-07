package net.zdsoft.scoremanage.data.action;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.scoremanage.data.service.HwStatisService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/11/15 17:16
 */
@Controller
@RequestMapping("/scoremanage/personal")
public class HwPersonalScoreAction extends BaseAction {
    private static final Logger logger = LoggerFactory
            .getLogger(HwRecommendedAction.class);


    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private HwStatisService hwStatisService;



    @RequestMapping("/index/page")
    public String index(){
        return "/scoremanage/personalReport/personalIndex.ftl";
    }

    /**
     * 统计年级的数据
     * @param gradeId
     * @return
     */
    @RequestMapping("/statistic")
    @ResponseBody
    public String statistic(String gradeId){
        if(StringUtils.isBlank(gradeId)){
            return error("gradeId不能为空!");
        }
        try {
            hwStatisService.savePersonalGrade(getLoginInfo().getUnitId(),gradeId);
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
        return success("统计成功!");
    }



    /**
     * 获取一个学生的报表信息
     * @param modelMap
     * @param studentId
     * @return
     */
    @RequestMapping("/stuReport")
    public String stuReport(ModelMap modelMap, String studentId){
        try {
            Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
            Clazz clazz = SUtils.dc(classRemoteService.findOneById(student.getClassId()), Clazz.class);
            modelMap.put("name",student.getStudentName());
            modelMap.put("className",clazz.getClassNameDynamic());
            if (student.getSex()!=null){
                modelMap.put("sex",student.getSex()== User.USER_WOMAN_SEX?"女":"男");
            }
            modelMap.put("stuCode",student.getStudentCode());

            List<List<String>> infoList = hwStatisService.getPersonalReportByStudentId(getLoginInfo().getUnitId(), studentId);

            if(CollectionUtils.isNotEmpty(infoList)){
                modelMap.put("schName",infoList.get(0).get(1));
                modelMap.put("sectionName",infoList.get(0).get(2));
                List<String> titleList = infoList.get(2);
                infoList=infoList.subList(3,infoList.size()-2);
                modelMap.put("infoList",infoList);
                modelMap.put("titleList",titleList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/scoremanage/recommended/stuReport.ftl";
    }

}
