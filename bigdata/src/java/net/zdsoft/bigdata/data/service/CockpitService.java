package net.zdsoft.bigdata.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.dto.DashboardCloneDto;
import net.zdsoft.bigdata.data.dto.DashboardSaveDto;
import net.zdsoft.bigdata.data.entity.Cockpit;

/**
 * @author ke_shen@126.com
 * @since 2018/4/13 下午1:34
 */
public interface CockpitService extends BaseService<Cockpit, String> {

    /** 获取某一个单位下面的所有大屏数据 */
    List<Cockpit> getCockpitsByUnitId(String unitId);

    /** 根据Id删除大屏，同时删除大屏相关的CockpitChart */
    void deleteCockpit(String id);

    /** 更新大屏模版，同时清除愿大屏的cockpitChart数据 */
    void updateCockpitTemplate(String template, String cockpitId);

    List<Cockpit> getCurrentUserCockpits(String userId, String unitId, boolean editPage);

    void saveCockpit(DashboardSaveDto dashboardSaveDto);

    void batchDelete(String[] ids);

    void doClone(DashboardCloneDto dashboardCloneDto);
}
