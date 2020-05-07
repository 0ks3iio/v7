package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmOptionSchoolDao;
import net.zdsoft.exammanage.data.dto.EmOptionSchoolDto;
import net.zdsoft.exammanage.data.entity.EmEnrollStuCount;
import net.zdsoft.exammanage.data.entity.EmJoinexamschInfo;
import net.zdsoft.exammanage.data.entity.EmOption;
import net.zdsoft.exammanage.data.entity.EmOptionSchool;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("emOptionSchoolService")
public class EmOptionSchoolServiceImpl extends BaseServiceImpl<EmOptionSchool, String> implements EmOptionSchoolService {
    @Autowired
    private EmOptionSchoolDao emOptionSchoolDao;
    @Autowired
    private EmJoinexamschInfoService emJoinexamschInfoService;
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private EmOptionService emOptionService;
    @Autowired
    private EmEnrollStuCountService emEnrollStuCountService;
    @Autowired
    private EmPlaceSettingService emPlaceSettingService;
    @Autowired
    private EmPlaceStudentService emPlaceStudentService;

    @Override
    protected BaseJpaRepositoryDao<EmOptionSchool, String> getJpaDao() {
        return emOptionSchoolDao;
    }

    @Override
    protected Class<EmOptionSchool> getEntityClass() {
        return EmOptionSchool.class;
    }

    @Override
    public List<EmOptionSchool> saveAllEntitys(
            EmOptionSchool... emOptionSchools) {
        return emOptionSchoolDao.saveAll(checkSave(emOptionSchools));
    }

    @Override
    public void deleteByOptionIdsAndExamId(String examId, String... optionIds) {
        emOptionSchoolDao.deleteByExamIdAndOptionIdIn(examId, optionIds);
    }

    @Override
    public void updateOptionStuNum(List<EmOptionSchool> ens) {
        saveAll(ens.toArray(new EmOptionSchool[0]));
        //统计每一个考点被分配到的人数
        Set<String> optionIds = new HashSet<>();
        Map<String, Integer> optionNum = new HashMap<>();
        for (EmOptionSchool e : ens) {
            if (!optionNum.containsKey(e.getOptionId())) {
                optionNum.put(e.getOptionId(), 0);
            }
            optionNum.put(e.getOptionId(), optionNum.get(e.getOptionId()) + e.getJoinStudentCount());
            optionIds.add(e.getOptionId());
        }

        List<EmOption> optionlist = emOptionService.findListByIdIn(optionIds.toArray(new String[0]));
        for (EmOption e : optionlist) {
            e.setOptionStudentCount(optionNum.get(e.getId()));
        }
        emOptionService.saveAll(optionlist.toArray(new EmOption[0]));

    }

    @Override
    public List<EmOptionSchool> findByOptionIdWithMaster(String examId, String... optionIds) {
        return emOptionSchoolDao.findByExamIdAndOptionIdIn(examId, optionIds);
    }

    @Override
    public List<EmOptionSchool> findByExamId(String examId) {
        return emOptionSchoolDao.findByExamId(examId);
    }

    @Override
    public List<EmOptionSchoolDto> findByDtos(String unitId, String examId, Pagination page) {
        List<EmOptionSchoolDto> dtos = new ArrayList<>();
        List<EmJoinexamschInfo> joinSchs = emJoinexamschInfoService.findByExamIdAndPage(examId, page);
        if (CollectionUtils.isEmpty(joinSchs)) {
            return dtos;
        }
        Set<String> schIds = EntityUtils.getSet(joinSchs, "schoolId");
        List<EmOptionSchool> optionStulist = emOptionSchoolDao.findByExamIdAndSchoolIdIn(examId, schIds.toArray(new String[0]));
        if (CollectionUtils.isEmpty(optionStulist)) {
            return dtos;
        }
        Map<String, Unit> unitMap = EntityUtils.getMap(SUtils.dt(unitRemoteService.findListByIds(schIds.toArray(new String[0])), new TR<List<Unit>>() {
        }), "id");
        int arrangeCount = 0;
        Map<String, EmOptionSchool> optStuMap = new HashMap<>();
        Map<String, Set<String>> schOption = new HashMap<>();
        Set<String> optionIds = new HashSet<>();
        for (EmOptionSchool e : optionStulist) {
            arrangeCount = arrangeCount + e.getJoinStudentCount();
            optStuMap.put(e.getOptionId() + "," + e.getJoinSchoolId(), e);
            optionIds.add(e.getOptionId());
            if (schOption.containsKey(e.getJoinSchoolId())) {
                schOption.get(e.getJoinSchoolId()).add(e.getOptionId());
            } else {
                Set<String> optionIds1 = new HashSet<>();
                optionIds1.add(e.getOptionId());
                schOption.put(e.getJoinSchoolId(), optionIds1);
            }
        }
        //得到各个学校的通过审核人数
        List<EmEnrollStuCount> schCountMap = emEnrollStuCountService.findByExamIdAndSchoolIdIn(examId, schIds.toArray(new String[0]));
        Map<String, Integer> schNumMap = EntityUtils.getMap(schCountMap, "schoolId", "passNum");

        Map<String, EmOption> optionMap = emOptionService.findMapByIdIn(optionIds.toArray(new String[0]));

        EmOptionSchoolDto dto;
        int subCount = 0;
        for (EmJoinexamschInfo sch : joinSchs) {
            if (!schOption.containsKey(sch.getSchoolId())) {
                continue;
            }
            dto = new EmOptionSchoolDto();
            subCount = 0;
            Unit unit = unitMap.get(sch.getSchoolId());
            dto.setSchoolId(sch.getSchoolId());
            dto.setSchoolName(unit.getUnitName());
            dto.setCount(schNumMap.containsKey(sch.getSchoolId()) ? schNumMap.get(sch.getSchoolId()) : 0);
            List<EmOptionSchool> enlist = dto.getEnlist();
            Set<String> optIds = schOption.get(sch.getSchoolId());
            if (arrangeCount > 0) {
                //有安排过则直接取数据库
                for (String optId : optIds) {
                    EmOptionSchool e = optStuMap.get(optId + "," + sch.getSchoolId());
                    subCount = subCount + e.getJoinStudentCount();
                    e.setOptionName(optionMap.get(optId).getOptionName());
                    enlist.add(e);
                }
            } else {
                //默认平均安排人数
                int optNum = optIds.size();
                subCount = dto.getCount();
                int num1 = subCount / optNum;
                int num2 = subCount % optNum;
                for (String optId : optIds) {
                    EmOptionSchool e = optStuMap.get(optId + "," + sch.getSchoolId());
                    if (num2 > 0) {
                        e.setJoinStudentCount(num1 + 1);
                        num2--;
                    } else {
                        e.setJoinStudentCount(num1);
                    }
                    e.setOptionName(optionMap.get(optId).getOptionName());
                    enlist.add(e);
                }
            }
            dto.setArrangeCount(subCount);
            dto.setNotArrangeCount(dto.getCount() - subCount);
            dtos.add(dto);
        }
        return dtos;
    }
}
