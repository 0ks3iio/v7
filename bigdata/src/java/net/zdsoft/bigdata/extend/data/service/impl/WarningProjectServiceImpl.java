package net.zdsoft.bigdata.extend.data.service.impl;

import com.google.common.collect.Lists;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.extend.data.dao.WarningProjectDao;
import net.zdsoft.bigdata.extend.data.entity.TagRuleRelation;
import net.zdsoft.bigdata.extend.data.entity.WarningProject;
import net.zdsoft.bigdata.extend.data.entity.WarningProjectUser;
import net.zdsoft.bigdata.extend.data.entity.WarningResultStat;
import net.zdsoft.bigdata.extend.data.enums.WarningProjectTimeEnum;
import net.zdsoft.bigdata.extend.data.listener.BgCalChannelConstant;
import net.zdsoft.bigdata.extend.data.service.*;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:42.
 */
@Service
public class WarningProjectServiceImpl extends BaseServiceImpl<WarningProject, String> implements WarningProjectService {

    @Resource
    private WarningProjectDao warningProjectDao;
    @Resource
    private TagRuleRelationService tagRuleRelationService;
    @Resource
    private WarningResultService warningResultService;
    @Resource
    private WarningResultStatService warningResultStatService;
    @Resource
    private BigLogService bigLogService;
    @Resource
    private WarningProjectUserService warningProjectUserService;

    @Override
    protected BaseJpaRepositoryDao<WarningProject, String> getJpaDao() {
        return warningProjectDao;
    }

    @Override
    protected Class<WarningProject> getEntityClass() {
        return WarningProject.class;
    }

    @Override
    public void saveWarningProject(WarningProject warningProject, List<TagRuleRelation> tagRuleRelations) {
        Json json = new Json();
        json.put("cron", warningProject.getScheduleParam());
        warningProject.setModifyTime(new Date());
        if (warningProject.getIsAllTime() == WarningProjectTimeEnum.ALL_TIME.getCode()) {
            warningProject.setEndTime(null);
        }
        if (StringUtils.isBlank(warningProject.getId())) {
            warningProject.setCreationTime(new Date());
            warningProject.setId(UuidUtils.generateUuid());
            json.put("operation", "add");
            warningProjectDao.save(warningProject);
            //业务日志埋点  新增
            LogDto logDto=new LogDto();
            logDto.setBizCode("insert-warningProject");
            logDto.setDescription("预警规则 "+warningProject.getProjectName());
            logDto.setNewData(warningProject);
            logDto.setBizName("预警规则设置");
            bigLogService.insertLog(logDto);
        } else {
            WarningProject oldWarningProject = warningProjectDao.findById(warningProject.getId()).get();
            json.put("operation", "modify");
            warningProjectDao.update(warningProject, new String[]{"projectName", "startTime", "isAllTime",
                    "endTime", "modifyTime", "scheduleParam", "callbackApis", "warnLevel", "warnResultType",
                    "effectiveDay", "feedbackType", "jobId"});
            //业务日志埋点  修改
            LogDto logDto=new LogDto();
            logDto.setBizCode("update-warningProject");
            logDto.setDescription("预警规则 "+oldWarningProject.getProjectName());
            logDto.setOldData(oldWarningProject);
            logDto.setNewData(warningProject);
            logDto.setBizName("预警规则设置");
            bigLogService.updateLog(logDto);

        }
        warningProjectUserService.deleteByProjectId(warningProject.getId());
        if (StringUtils.isNotEmpty(warningProject.getUserIds())) {
            List<WarningProjectUser> userList = Lists.newArrayList();
            WarningProjectUser projectUser = null;
            String[] userIds = warningProject.getUserIds().split(",");
            for (int i=0;i<userIds.length;i++) {
                projectUser = new WarningProjectUser();
                projectUser.setId(UuidUtils.generateUuid());
                projectUser.setProjectId(warningProject.getId());
                projectUser.setUsersId(userIds[i]);
                projectUser.setCreationTime(new Date());
                userList.add(projectUser);
            }
            warningProjectUserService.saveAll(userList.toArray(new WarningProjectUser[userList.size()]));
        }
        json.put("jobId", warningProject.getId());
        RedisUtils.publish(BgCalChannelConstant.BG_CAL_REDIS_CHANNEL, json.toJSONString());
        tagRuleRelationService.saveWarningRuleRelation(tagRuleRelations, warningProject.getId());
    }

    @Override
    public void deleteWarningProject(String id) {
        WarningProject warningProject = warningProjectDao.findById(id).get();
        //业务日志埋点  删除
        LogDto logDto=new LogDto();
        logDto.setBizCode("delete-warningProject");
        logDto.setDescription("预警规则 "+warningProject.getProjectName());
        logDto.setBizName("预警规则设置");
        logDto.setOldData(warningProject);
        bigLogService.deleteLog(logDto);
        Json json = new Json();
        json.put("cron", this.findOne(id).getScheduleParam());
        json.put("operation", "delete");
        json.put("jobId", id);
        warningProjectDao.deleteById(id);
        RedisUtils.publish(BgCalChannelConstant.BG_CAL_REDIS_CHANNEL, json.toJSONString());
        tagRuleRelationService.deleteByProjectId(id);
        // 删除预警结果数据
        warningResultStatService.deletWarnResultStatByProjectId(id);

    }

    @Override
    public List<WarningProject> findALlByPage(Pagination page, String projectName, String unitId) {
        projectName = StringUtils.isBlank(projectName) ? "%%" : "%" + projectName + "%";
        Long count = warningProjectDao.countByProjectNameLikeAndUnitIdEquals(projectName, unitId);
        page.setMaxRowCount(count == null ? 0 : count.intValue());
        return warningProjectDao.findAllByPageAndProjectNameLike(Pagination.toPageable(page), projectName, unitId);
    }

    @Override
    public List<WarningProject> findResultProjectByPage(Pagination page, String projectName) {
        projectName = StringUtils.isBlank(projectName) ? "%%" : "%" + projectName + "%";
        Integer count = warningProjectDao.countWarningProjectByLastWarnDateIsNotNullAndProjectNameLike(projectName);
        page.setMaxRowCount(count == null ? 0 : count.intValue());
        return warningProjectDao.findResultProjectByPage(Pagination.toPageable(page), projectName);
    }

    @Override
    public void clearWarningResult(String id, String unitId) {
        // 清除次数和上次预警时间
        this.updateWarningProject(id, new Date());
        // 清除预警结果次数
        List<WarningResultStat> resultStats = warningResultStatService.findListBy(new String[]{"unitId", "projectId"}, new String[]{unitId, id});
        if (resultStats.size() > 0) {
            WarningResultStat warningResultStat = resultStats.get(0);
            warningResultStat.setStatResult(0l);
            warningResultStatService.update(warningResultStat,warningResultStat.getId(), new String[]{"statResult"});
        }
        warningResultService.deleteByProjectIdAndUnitId(id, unitId);
    }

    @Override
    public List<WarningProject> findAvailableProject() {
        return warningProjectDao.findAvailableProject();
    }

    @Override
    public void updateWarningProject(String id, Date lastWarnDate) {
        Integer warnTimes = warningResultService.countWarningResultByProjectId(id);
        WarningProject warningProject = new WarningProject();
        warningProject.setId(id);
        warningProject.setWarnTimes(warnTimes);
        warningProject.setLastWarnDate(lastWarnDate);
        warningProject.setModifyTime(new Date());
        warningProjectDao.update(warningProject, new String[]{"warnTimes", "lastWarnDate", "modifyTime"});
    }

    @Override
    public List<WarningProject> findByProjectNameAndIdInPage(String projectName, String[] projectIds, Pagination page) {
        projectName = "%" + (StringUtils.isNotBlank(projectName) ? projectName : "") + "%";
        Page<WarningProject> warningProjects = warningProjectDao.findByProjectNameAndIdInPage(projectName, projectIds,Pagination.toPageable(page));
        page.setMaxRowCount((int) warningProjects.getTotalElements());
        return warningProjects.getContent();
    }
}
