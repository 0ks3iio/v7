package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmOptionDao;
import net.zdsoft.exammanage.data.entity.EmOption;
import net.zdsoft.exammanage.data.entity.EmOptionSchool;
import net.zdsoft.exammanage.data.entity.EmPlace;
import net.zdsoft.exammanage.data.entity.EmRegion;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("emOptionService")
public class EmOptionServiceImpl extends BaseServiceImpl<EmOption, String> implements EmOptionService {
    @Autowired
    private EmOptionDao emOptionDao;
    @Autowired
    private EmOptionSchoolService emOptionSchoolService;
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private EmExamRegionService emExamRegionService;
    @Autowired
    private EmPlaceSettingService emPlaceSettingService;
    @Autowired
    private EmPlaceStudentService emPlaceStudentService;
    @Autowired
    private EmPlaceService emPlaceService;

    @Override
    public List<EmOption> findByExamIdAndExamRegionId(String examId,
                                                      String examRegionId) {
        List<EmOption> emOptions = null;
        if (StringUtils.isBlank(examRegionId)) {
            emOptions = emOptionDao.findByExamId(examId);
        } else {
            emOptions = emOptionDao.findByExamIdAndExamRegionId(examId, examRegionId);
        }
        if (CollectionUtils.isNotEmpty(emOptions)) {
            Set<String> optionSet = new HashSet<String>();
            for (EmOption emOption : emOptions) {
                optionSet.add(emOption.getId());
            }
            List<EmOptionSchool> emOptionSchools = emOptionSchoolService.findByOptionIdWithMaster(examId, optionSet.toArray(new String[0]));
            Set<String> schoolIds = new HashSet<String>();
            Map<String, List<EmOptionSchool>> smSchMap = new HashMap<String, List<EmOptionSchool>>();
            for (EmOptionSchool emOptionSchool : emOptionSchools) {
                schoolIds.add(emOptionSchool.getJoinSchoolId());
                if (smSchMap.containsKey(emOptionSchool.getOptionId() + "_" + emOptionSchool.getExamId())) {
                    List<EmOptionSchool> emOptionSchools2 = smSchMap.get(emOptionSchool.getOptionId() + "_" + emOptionSchool.getExamId());
                    emOptionSchools2.add(emOptionSchool);
                    smSchMap.put(emOptionSchool.getOptionId() + "_" + emOptionSchool.getExamId(), emOptionSchools2);
                } else {
                    List<EmOptionSchool> emOptionSchools2 = new ArrayList<EmOptionSchool>();
                    emOptionSchools2.add(emOptionSchool);
                    smSchMap.put(emOptionSchool.getOptionId() + "_" + emOptionSchool.getExamId(), emOptionSchools2);
                }
            }
            List<Unit> units = SUtils.dt(
                    unitRemoteService.findListByIds(schoolIds.toArray(new String[0])),
                    new TR<List<Unit>>() {
                    });
            Map<String, Unit> unitMap = EntityUtils.getMap(units, "id");
            for (EmOption emOption : emOptions) {
                Map<String, String> map = new HashMap<String, String>();
                if (smSchMap.containsKey(emOption.getId() + "_" + emOption.getExamId())) {
                    List<EmOptionSchool> emList = smSchMap.get(emOption.getId() + "_" + emOption.getExamId());
                    if (CollectionUtils.isNotEmpty(emList)) {
                        for (EmOptionSchool emOptionSchool : emList) {
                            Unit unit = unitMap.get(emOptionSchool.getJoinSchoolId());
                            if (unit != null) {
                                map.put(emOptionSchool.getJoinSchoolId(), unit.getUnitName());
                            } else {
                                map.put(emOptionSchool.getJoinSchoolId(), "未选择");
                            }
                        }
                        emOption.setLkxzSelectMap(map);
                    }
                }
            }
        }
        return emOptions;
    }

    @Override
    public List<EmOption> findByExamIdAndSchools(String examId,
                                                 String... schools) {
        return emOptionDao.findByExamIdAndSchools(examId, schools);
    }

    @Override
    public Set<String> getExamRegionId(String examId) {
        return emOptionDao.getExamRegionId(examId);
    }

    @Override
    public List<EmOption> findByExamIdWithMaster(String examId) {
        return emOptionDao.findByExamId(examId);
    }

    @Override
    public void deleteByExamIdAndExamRegionId(String examId, String examRegionId) {
        emOptionDao.deleteByExamIdAndExamRegionId(examId, examRegionId);
    }

    @Override
    public void deleteByOptionIdAndExamId(String optionId, String examId) {
        EmOption emOption = emOptionDao.findById(optionId).orElse(null);
        EmRegion emRegionOld = emExamRegionService.findOne(emOption.getExamRegionId());
        if (emRegionOld != null && emRegionOld.getExamOptionNum() > 0) {
            emRegionOld.setExamOptionNum(emRegionOld.getExamOptionNum() - 1);
            emExamRegionService.saveAllEntitys(emRegionOld);
        }
        emOptionDao.deleteById(optionId);
        emOptionSchoolService.deleteByOptionIdsAndExamId(examId, optionId);

        List<EmPlace> emPlaces = emPlaceService.findByExamIdAndOptionIds(examId, new String[]{optionId});
        if (CollectionUtils.isNotEmpty(emPlaces)) {
            Set<String> emPlacesId = EntityUtils.getSet(emPlaces, "id");
            emPlaceService.deleteByExamIdAndOptionId(examId, optionId);
            emPlaceStudentService.deleteByEmPlaceIds(emPlacesId.toArray(new String[0]));
        }
    }

    @Override
    public List<EmOption> findByExamIdAndExamRegionIdIn(String examId, String examRegionId, String[] examRegionIds) {
        List<EmOption> emOptions = new ArrayList<EmOption>();
        if (StringUtils.isNotEmpty(examRegionId)) {
            emOptions = emOptionDao.findByExamIdAndExamRegionId(examId, examRegionId);
        } else if (examRegionIds != null && examRegionIds.length > 0) {
            emOptions = emOptionDao.findByExamIdAndExamRegionIdIn(examId, examRegionIds);
        }
        return emOptions;
    }

    @Override
    public List<EmOption> saveAllEntitys(EmOption... emOptions) {
        return emOptionDao.saveAll(checkSave(emOptions));
    }

    @Override
    public void saveEmOptionsAndEmOptionSchools(List<EmOption> emOptions,
                                                List<EmOptionSchool> emOptionSchools, String examId, String regionId, int optionSize) {
        List<EmOption> emOptions2 = emOptionDao.findByExamIdAndExamRegionId(examId, regionId);
        Map<String, EmOption> emMap = new HashMap<String, EmOption>();
        Set<String> optionIds = new HashSet<String>();
        if (CollectionUtils.isNotEmpty(emOptions2)) {
            for (EmOption emOption : emOptions2) {
                emMap.put(emOption.getId(), emOption);
                optionIds.add(emOption.getId());
            }
        }

        Set<String> schoolIds = new HashSet<String>();
        for (EmOption emOption : emOptions) {
            schoolIds.add(emOption.getOptionSchoolId());
        }
        List<Unit> units = SUtils.dt(
                unitRemoteService.findListByIds(schoolIds.toArray(new String[0])),
                new TR<List<Unit>>() {
                });
        Map<String, Unit> unitMap = EntityUtils.getMap(units, "id");
        for (EmOption emOption : emOptions) {
            if (unitMap.containsKey(emOption.getOptionSchoolId())) {
                Unit unit = unitMap.get(emOption.getOptionSchoolId());
                if (unit != null) {
                    emOption.setOptionName(unit.getUnitName());
                }
            }
            if (emMap.containsKey(emOption.getId())) {
                EmOption emOption2 = emMap.get(emOption.getId());
                if (emOption2 != null) {
                    emOption.setOptionStudentCount(emOption2.getOptionStudentCount());
                    emOption.setOptionPlaceCount(emOption2.getOptionPlaceCount());
                }
            }
        }

        Map<String, EmOptionSchool> emSchMap = new HashMap<String, EmOptionSchool>();
        List<EmOptionSchool> emOptionSchoolsOld = emOptionSchoolService.findByOptionIdWithMaster(examId, optionIds.toArray(new String[0]));
        for (EmOptionSchool emOptionSchool : emOptionSchoolsOld) {
            emSchMap.put(emOptionSchool.getOptionId() + "_" + emOptionSchool.getExamId() + "_" + emOptionSchool.getJoinSchoolId(), emOptionSchool);
        }
        for (EmOptionSchool emOptionSchool : emOptionSchools) {
            if (emSchMap.containsKey(emOptionSchool.getOptionId() + "_" + emOptionSchool.getExamId() + "_" + emOptionSchool.getJoinSchoolId())) {
                EmOptionSchool emOptionSchool2 = emSchMap.get(emOptionSchool.getOptionId() + "_" + emOptionSchool.getExamId() + "_" + emOptionSchool.getJoinSchoolId());
                if (emOptionSchool2 != null) {
                    emOptionSchool.setJoinStudentCount(emOptionSchool2.getJoinStudentCount());
                }
            }
        }
        if (CollectionUtils.isNotEmpty(emOptions)) {
            EmRegion emRegionOld = emExamRegionService.findOne(regionId);
            emRegionOld.setExamOptionNum(optionSize);
            emExamRegionService.saveAllEntitys(emRegionOld);
            emOptionSchoolService.deleteByOptionIdsAndExamId(examId, optionIds.toArray(new String[0]));
            emOptionDao.deleteByExamIdAndExamRegionId(examId, regionId);
            this.saveAllEntitys(emOptions.toArray(new EmOption[0]));
            emOptionSchoolService.saveAllEntitys(emOptionSchools.toArray(new EmOptionSchool[0]));

        }

    }

    @Override
    protected BaseJpaRepositoryDao<EmOption, String> getJpaDao() {
        return emOptionDao;
    }

    @Override
    protected Class<EmOption> getEntityClass() {
        return EmOption.class;
    }

}
