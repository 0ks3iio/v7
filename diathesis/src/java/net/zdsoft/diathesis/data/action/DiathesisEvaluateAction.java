package net.zdsoft.diathesis.data.action;

import com.alibaba.fastjson.JSON;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.diathesis.data.entity.DiathesisEvaluate;
import net.zdsoft.diathesis.data.service.DiathesisEvaluateService;
import net.zdsoft.framework.action.BaseAction;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @Author: panlf
 * @Date: 2019/6/10 14:42/diathesis/subject/gradeList
 */
@RestController
@RequestMapping("/diathesis/evaluate")
public class DiathesisEvaluateAction extends BaseAction {
    @Autowired
    private DiathesisEvaluateService diathesisEvaluateService;

    /**
     * 学生自述 和 教师评价保存
     * @param evaluate
     * @param errors
     * @return
     */
    @PostMapping("/saveEvaluate")
    public String saveEvaluate(@RequestBody @Valid DiathesisEvaluate evaluate, Errors errors){

        if(errors.hasFieldErrors())return error(errors.getFieldError().getDefaultMessage());
        try{
            if(StringUtils.isBlank(evaluate.getStudentId())){
                //自述
                if (!getLoginInfo().getOwnerType().equals(User.OWNER_TYPE_STUDENT)){
                    return error("老师评价时,studentId不能为空");
                }
                evaluate.setStudentId(getLoginInfo().getOwnerId());
            }else{
                //老师评价
                if (!getLoginInfo().getOwnerType().equals(User.OWNER_TYPE_TEACHER)){
                    return error("老师评价专用!");
                }
                evaluate.setTeacherId(getLoginInfo().getOwnerId());
            }
            evaluate.setUnitId(getLoginInfo().getUnitId());
            diathesisEvaluateService.saveEvaluate(evaluate);
        }catch (Exception e){
            return error(e.getMessage());
        }
        return success("保存成功");
    }

    /**
     * teacherId为空时 获得学生自评
     * 不为空时,获得教师评价
     * 有多份的时候 获取最新的那份
     * @param studentId
     * @return
     */
    @GetMapping("/findEvaluate")
    public String findEvaluate(@RequestParam(required = false)String studentId,String type){
        if(StringUtils.isBlank(type)){
            return error("type不能为空,0:学生自述,1:教师评价");
        }

        Predicate<DiathesisEvaluate> predicate;
        if("0".equals(type)){
            if(StringUtils.isBlank(studentId))studentId=getLoginInfo().getOwnerId();
            predicate=x->StringUtils.isBlank(x.getTeacherId());
        }else{
            predicate=x->StringUtils.isNotBlank(x.getTeacherId());
        }
        List<DiathesisEvaluate>evaluate = diathesisEvaluateService.findListBy(new String[]{"studentId"}, new String[]{studentId});
        List<DiathesisEvaluate> collect = evaluate.stream().filter(predicate).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(collect)){
            return error("还没有评价");
        }
        return JSON.toJSONString(collect.get(0));
    }
}
