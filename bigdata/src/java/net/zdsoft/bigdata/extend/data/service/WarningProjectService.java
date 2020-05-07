package net.zdsoft.bigdata.extend.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.extend.data.entity.TagRuleRelation;
import net.zdsoft.bigdata.extend.data.entity.WarningProject;
import net.zdsoft.framework.entity.Pagination;

import java.util.Date;
import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:41.
 */
public interface WarningProjectService extends BaseService<WarningProject, String> {

    /**
     * 保存预警项目
     * @param warningProject
     * @param tagRuleRelations
     */
    void saveWarningProject(WarningProject warningProject, List<TagRuleRelation> tagRuleRelations);

    /**
     * 删除项目
     * @param id
     */
    void deleteWarningProject(String id);

    /**
     * 分页查询所有项目
     * @param page
     * @return
     */
    List<WarningProject> findALlByPage(Pagination page, String projectName, String unitId);

    /**
     * 分页查询预警结果
     * @param page
     * @return
     */
    List<WarningProject> findResultProjectByPage(Pagination page, String projectName);

    /**
     * 清理预警结果
     * @param id
     */
    void clearWarningResult(String id, String unitId);

    /**
     * 查询可用的预警项目
     * @return
     */
    List<WarningProject> findAvailableProject();

    /**
     * 更新预警项目 预警次数和上次预警时间
     * @param id
     * @param lastWarnDate
     */
    void updateWarningProject(String id, Date lastWarnDate);

    /**
     * 查找提醒人相关的预警项目
     * @param projectName
     * @param projectIds
     * @param page
     * @return
     */
    List<WarningProject> findByProjectNameAndIdInPage(String projectName, String[] projectIds, Pagination page);
}
