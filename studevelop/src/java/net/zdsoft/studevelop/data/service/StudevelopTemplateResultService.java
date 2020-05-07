package net.zdsoft.studevelop.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.entity.StudevelopTemplateResult;

import java.util.List;

/**
 * Created by luf on 2018/12/20.
 */
public interface StudevelopTemplateResultService  extends BaseService<StudevelopTemplateResult,String> {

    public List<StudevelopTemplateResult> getTemplateResultByStudentId(String[] templateItemIds , String studentId,String acadyear ,String semester);
    /**
     * 通过学生ids获取结果
     * @param templateItemIds
     * @param studentIds
     * @param acadyear
     * @param semester
     * @return
     */
    public List<StudevelopTemplateResult> getTemplateResultByStudentIds(String[] templateItemIds , String[] studentIds,String acadyear ,String semester);
    /**
     * 删除数据
     * @param templateItemIds
     * @param studentIds
     * @param acadyear
     * @param semester
     * @return
     */
    public Integer deleteByItemIdsStuIds(String[] templateItemIds , String[] studentIds,String acadyear ,String semester);
    /**
     * 删除数据
     * @param templateItemIds
     * @param studentIds
     * @param acadyear
     * @param semester
     * @return
     */
    public Integer deleteByItemIdsStuIdsSubId(String[] templateItemIds , String[] studentIds,String acadyear ,String semester,String subjectId);
}
