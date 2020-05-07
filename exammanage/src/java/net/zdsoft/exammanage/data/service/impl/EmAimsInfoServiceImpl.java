package net.zdsoft.exammanage.data.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmAimsInfoDao;
import net.zdsoft.exammanage.data.entity.EmAimsInfo;
import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.exammanage.data.service.EmAimsInfoService;
import net.zdsoft.exammanage.data.service.EmExamInfoService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("emAimsInfoService")
public class EmAimsInfoServiceImpl extends BaseServiceImpl<EmAimsInfo, String> implements EmAimsInfoService {
    @Autowired
    private EmAimsInfoDao emAimsInfoDao;
    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private SchoolRemoteService schoolRemoteService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;

    private static String getSchoolStr(String schoolIds, Map<String, School> schMap) {
        StringBuilder schStr = new StringBuilder();
        String[] schIds = schoolIds.split(",");
        for (int i = 0; i < schIds.length; i++) {
            if (i > 0) {
                schStr.append(",");
            }
            schStr.append(schMap.get(schIds[i]));
        }
        return schStr.toString();
    }

    @Override
    public void saveAims(EmAimsInfo aims) {
        String examId = aims.getExamId();
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        emAimsInfoDao.deleteByExamId(examId);
        aims.setAcadyear(examInfo.getAcadyear());
        aims.setSemester(examInfo.getSemester());
        checkSave(aims);
        save(aims);
    }

    @Override
    public List<EmAimsInfo> findOpenByExamIds(String[] examIds) {
        List<EmAimsInfo> list = emAimsInfoDao.findListByOpenAndExamIdIn("1", examIds);
        return list;
    }

    @Override
    public EmAimsInfo findByExamIds(String examId) {
        List<EmAimsInfo> list = emAimsInfoDao.findListByExamIdIn(new String[]{examId});
        EmAimsInfo emAimsInfo = new EmAimsInfo();
        if (CollectionUtils.isNotEmpty(list)) {
            emAimsInfo = list.get(0);
        } else {
            emAimsInfo.setExamId(examId);
            emAimsInfo.setIsOpen("0");
        }
        Map<String, School> schMap = new HashMap<>();
        if (StringUtils.isNotBlank(emAimsInfo.getSchoolIds())) {
            Set<String> schoolIds = new HashSet<>();
            String[] schIds = emAimsInfo.getSchoolIds().split(",");
            for (String schId : schIds) {
                schoolIds.add(schId);
            }
            if (schoolIds.size() > 0) {
                List<School> schList = SUtils.dt(
                        schoolRemoteService.findListByIds(schoolIds.toArray(new String[0])), new TR<List<School>>() {
                        });
                schMap = EntityUtils.getMap(schList, School::getId);
                emAimsInfo.setSchoolName(getSchoolStr(emAimsInfo.getSchoolIds(), schMap));
            }
        }
        return emAimsInfo;
    }

    @Override
    public List<EmAimsInfo> findByExams(String unitId, String acadyear, String semester) {
        List<EmAimsInfo> list = new ArrayList<>();
        List<EmExamInfo> emExamInfos = emExamInfoService.findByUnitIdAndAcadyear(unitId, acadyear, semester);
        if (CollectionUtils.isEmpty(emExamInfos)) {
            return list;
        }
        Map<String, EmExamInfo> examMap = EntityUtils.getMap(emExamInfos, EmExamInfo::getId);
        Set<String> examIds = examMap.keySet();
        List<EmAimsInfo> infoList = emAimsInfoDao.findListByExamIdIn(examIds.toArray(new String[0]));
        Set<String> schoolIds = new HashSet<>();
        Map<String, EmAimsInfo> infoMap = new HashMap<>();
        for (EmAimsInfo emAimsInfo : infoList) {
            infoMap.put(emAimsInfo.getExamId(), emAimsInfo);
            if (StringUtils.isBlank(emAimsInfo.getSchoolIds())) {
                continue;
            }
            String[] schIds = emAimsInfo.getSchoolIds().split(",");
            for (String schId : schIds) {
                schoolIds.add(schId);
            }
        }
        Map<String, School> schMap = new HashMap<>();
        if (schoolIds.size() > 0) {
            List<School> schList = SUtils.dt(
                    schoolRemoteService.findListByIds(schoolIds.toArray(new String[0])), new TR<List<School>>() {
                    });
            schMap = EntityUtils.getMap(schList, School::getId);
        }
        EmAimsInfo e;
        for (EmExamInfo exam : emExamInfos) {
            e = new EmAimsInfo();
            if (infoMap.containsKey(exam.getId())) {
                EmAimsInfo ii = infoMap.get(exam.getId());
                e.setIsOpen(ii.getIsOpen());
                e.setStartTime(ii.getStartTime());
                e.setEndTime(ii.getEndTime());
                e.setSchoolIds(ii.getSchoolIds());
                e.setSchoolName(getSchoolStr(ii.getSchoolIds(), schMap));
            } else {
                e.setIsOpen("0");
            }
            e.setExamCode(exam.getExamCode());
            e.setExamType(exam.getExamType());
            e.setExamName(exam.getExamName());
            e.setGradeCode(exam.getGradeCodes());
            e.setExamId(exam.getId());
            list.add(e);
        }
        makeGradeNames(list);
        return list;
    }
    
    private void makeGradeNames(List<EmAimsInfo> emAimsInfos) {
    	if(CollectionUtils.isNotEmpty(emAimsInfos)){
    		// 年级Code列表
    		Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds = SUtils.dt(
    				mcodeRemoteService.findMapMapByMcodeIds(new String[]{
    						"DM-RKXD-0", "DM-RKXD-1", "DM-RKXD-2", "DM-RKXD-3", "DM-RKXD-9"}),
    						new TR<Map<String, Map<String, McodeDetail>>>() {
    				});
    		for (EmAimsInfo item : emAimsInfos) {
    			if (StringUtils.isNotBlank(item.getGradeCode())) {
    				String str1 = item.getGradeCode().substring(0, 1);
    				String str2 = item.getGradeCode().substring(1);
    				Map<String, McodeDetail> map = findMapMapByMcodeIds
    						.get("DM-RKXD-" + str1);
    				if (map != null && map.containsKey(str2)) {
    					item.setGradeCode(map.get(str2).getMcodeContent());
    				}
    			}
    		}
    	}
    }

    @Override
    protected BaseJpaRepositoryDao<EmAimsInfo, String> getJpaDao() {
        return emAimsInfoDao;
    }

    @Override
    protected Class<EmAimsInfo> getEntityClass() {
        return EmAimsInfo.class;
    }
}
