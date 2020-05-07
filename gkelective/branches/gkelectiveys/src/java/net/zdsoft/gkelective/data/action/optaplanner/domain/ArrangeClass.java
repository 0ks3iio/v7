package net.zdsoft.gkelective.data.action.optaplanner.domain;

import java.util.Set;


/**
 * 注意只有在2+x的情况下使用本类，如需修改考虑继承本类
 * @author shensiping
 *
 */
public class ArrangeClass {

    // 新建行政班id,不能为空，如uuid
    private String classId;
    // 选择的科目,一般为2门
    private Set<String> choosedSubjects;
    // 学生
    private Set<String> studentIds;
    
    
    /**
     * 获取classId
     * @return classId
     */
    public String getClassId() {
        return classId;
    }
    /**
     * 设置classId
     * @param classId classId
     */
    public void setClassId(String classId) {
        this.classId = classId;
    }
    /**
     * 获取choosedSubjects
     * @return choosedSubjects
     */
    public Set<String> getChoosedSubjects() {
        return choosedSubjects;
    }
    /**
     * 设置choosedSubjects
     * @param choosedSubjects choosedSubjects
     */
    public void setChoosedSubjects(Set<String> choosedSubjects) {
        this.choosedSubjects = choosedSubjects;
    }
    /**
     * 获取studentIds
     * @return studentIds
     */
    public Set<String> getStudentIds() {
        return studentIds;
    }
    /**
     * 设置studentIds
     * @param studentIds studentIds
     */
    public void setStudentIds(Set<String> studentIds) {
        this.studentIds = studentIds;
    }
    
    
    
    
}
