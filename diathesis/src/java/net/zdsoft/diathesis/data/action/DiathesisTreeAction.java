package net.zdsoft.diathesis.data.action;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: panlf
 * @Date: 2019/8/22 17:28
 */
@RestController
@RequestMapping("/diathesis/tree")
public class DiathesisTreeAction extends BaseAction {

    @Autowired
    private ClassRemoteService classRemoteService;

    @Autowired
    private StudentRemoteService studentRemoteService;

    @Autowired
    private GradeRemoteService gradeRemoteService;


    /**
     * 根据年级id 返回 班级和学生树
     * @param gradeId
     * @return
     */
    @RequestMapping("/gradeTree")
    public String getGradeTree(String gradeId){
        if(StringUtils.isBlank(gradeId)){
            return error("参数缺失");
        }
        List<Clazz> classList = SUtils.dt(classRemoteService.findByGradeIdSortAll(gradeId), Clazz.class);
        String[] classIds = EntityUtils.getArray(classList, x -> x.getId(), String[]::new);
        Map<String, List<Json>> classStuMap = SUtils.dt(studentRemoteService.findByClassIds(classIds), Student.class)
                .stream().collect(Collectors.groupingBy(x -> x.getClassId(), Collectors.mapping(x -> {
                    Json json = new Json();
                    json.put("stuId", x.getId());
                    json.put("stuName", x.getStudentName());
                    return json;
                }, Collectors.toList())));

        return Json.toJSONString(EntityUtils.getList(classList,x->{
            Json clazz = new Json();
            clazz.put("classId",x.getId());
            clazz.put("className",x.getClassNameDynamic());
            clazz.put("children",classStuMap.get(x.getId()));
            return clazz;
        }));
    }
}
