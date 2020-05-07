package net.zdsoft.newstusys.service;

import java.util.List;

import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.newstusys.entity.BaseStudent;
import net.zdsoft.newstusys.entity.StudentAbnormalFlow;
import net.zdsoft.newstusys.entity.StudentResume;

/**
 * Created by Administrator on 2018/3/1.
 */
public interface BaseStudentService extends BaseService<Student,String> {

    public void saveStudent(BaseStudent student , List<Family> familyList, List<StudentResume> studentResumeList ,boolean hasAddPic);
    
    /**
     * 异动新增学生
     */
    public void saveStudentForAbnormal(BaseStudent student , List<Family> familyList, 
    		List<StudentResume> studentResumeList, StudentAbnormalFlow flow, boolean hasAddPic);
    
    public void deleteByStuIds(String[] stuIds);
    
    /**
     * 校验处理学校下学生临时图片
     * @param unitId
     */
    public String saveStudentPics(String unitId);
    
    public List<Student> findStudentByClsIds(String[] clsIds, String searchType, Pagination page);
}
