package net.zdsoft.exammanage.xj.action;

import net.zdsoft.exammanage.xj.constant.XjExamConstants;
import net.zdsoft.exammanage.xj.entity.XjexamInfo;
import net.zdsoft.exammanage.xj.service.XjExamContrastService;
import net.zdsoft.exammanage.xj.service.XjExamInfoService;
import net.zdsoft.exammanage.xj.service.XjExamTypeService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yangsj  2017年10月12日下午5:02:11
 */
@Controller
@RequestMapping("/examinfo/teacher")
public class ExamTeacherAction extends BaseAction {

    @Autowired
    private XjExamInfoService xjExamInfoService;
    @Autowired
    private XjExamContrastService xjExamContrastService;
    @Autowired
    private XjExamTypeService xjExamTypeService;


    @ResponseBody
    @RequestMapping("/findExam")
    @ControllerInfo("查询考生是否存在")
    public String findExam(String studentName, String admission, String examType) {
        try {
            XjexamInfo examInfos = xjExamInfoService.findStuInfo(studentName, admission);
            if (examInfos == null) {
                return error("查无此人，请核对信息");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return error("查询失败");
        }
        return success("查询成功");
    }


    @RequestMapping("/showSeatList")
    @ControllerInfo("查看学生的座位号")
    public String showSeatList(ModelMap map, String studentName, String admission, String examType) {

        if (XjExamConstants.EXAM_STU_SEAT_TYPE.equals(examType)) {


//			map.put("seatsList", getStuExamInfo(studentName,admission));

            return "/exammanage/xjExam/student/examShowSeat.ftl";
        } else {
//			map.put("stuScoredto", getStuExamScore(studentName,admission));
            return "/exammanage/xjExam/student/examShowScore.ftl";
        }
    }


}		
