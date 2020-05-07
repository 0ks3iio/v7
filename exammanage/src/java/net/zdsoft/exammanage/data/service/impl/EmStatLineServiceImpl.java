package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dao.EmStatLineDao;
import net.zdsoft.exammanage.data.dto.EmLineStatDto;
import net.zdsoft.exammanage.data.entity.EmStatLine;
import net.zdsoft.exammanage.data.entity.EmStatObject;
import net.zdsoft.exammanage.data.service.EmStatLineService;
import net.zdsoft.exammanage.data.service.EmStatObjectService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("emStatLineService")
public class EmStatLineServiceImpl extends BaseServiceImpl<EmStatLine, String> implements EmStatLineService {
    @Autowired
    private EmStatLineDao emStatLineDao;
    @Autowired
    private EmStatObjectService emStatObjectService;

    private static void sortDtoList(List<EmLineStatDto> list2, Map<String, EmLineStatDto> dtoMap2) {
        Map<String, List<EmLineStatDto>> maplist = new HashMap<>();
        for (EmLineStatDto e : list2) {
            if (!maplist.containsKey(e.getLine())) {
                maplist.put(e.getLine(), new ArrayList<>());
            }
            maplist.get(e.getLine()).add(e);
        }
        for (String line : maplist.keySet()) {
            List<EmLineStatDto> list = maplist.get(line);
            //上线人数排序
            Collections.sort(list, new Comparator<EmLineStatDto>() {
                @Override
                public int compare(EmLineStatDto o1, EmLineStatDto o2) {
                    int mm = o2.getNum() - o1.getNum();
                    if (mm > 0) {
                        return 1;
                    } else if (mm < 0) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
            int index = 1;// 排名
            int numpp = Integer.MAX_VALUE;
            for (int i = 0; i < list.size(); i++) {
                EmLineStatDto s = list.get(i);
                if (s.getNum() < numpp) {
                    numpp = s.getNum();
                    index = i + 1;
                }
                s.setRank(index);
                dtoMap2.put(s.getExamId() + "_" + s.getClassId() + "_" + s.getLine(), s);
            }
        }


    }

    @Override
    public List<EmStatLine> findByUnitIdAndExamId(String unitId, String examId, String isDouble, String subId, String subType) {
        EmStatObject object = emStatObjectService.findByUnitIdExamId(unitId, examId);
        if (object == null || StringUtils.equals(object.getIsStat(), "0")) {
            return null;
        }
        if ("1".equals(subType)) {
            subType = "3";
        }
        List<EmStatLine> list = emStatLineDao.findList(object.getId(), isDouble, subId, subType);
        return list;
    }

    @Override
    public List<EmStatLine> findByUnitIdAndExamId(String unitId, String examId, String isDouble, String subId,
                                                  String subType, String sumId) {
        EmStatObject object = emStatObjectService.findByUnitIdExamId(unitId, examId);
        if (object == null || StringUtils.equals(object.getIsStat(), "0")) {
            return null;
        }
        if ("1".equals(subType)) {
            subType = "3";
        }
        if (StringUtils.endsWith(isDouble, "1")) {
            String sumType = "";
            if (StringUtils.equals(ExammanageConstants.ZERO32, sumId)) {
                sumType = "1";
            } else {
                sumType = "2";
            }
            List<EmStatLine> list = emStatLineDao.findDoubleList(object.getId(), subId, subType, sumType);
            return list;
        } else {
            List<EmStatLine> list = emStatLineDao.findList(object.getId(), isDouble, subId, subType);
            return list;
        }

    }

    @Override
    public Map<String, EmLineStatDto> findByDoubleDtoMap2(String unitId, String subId, String subType, String sumId,
                                                          Set<String> referIds, List<EmStatLine> statLines) {
        Map<String, EmLineStatDto> dtoMap2 = new HashMap<>();
        for (String id : referIds) {
            List<EmStatLine> list = findByUnitIdAndExamId(unitId, id, "1", subId, subType, sumId);
            if (CollectionUtils.isEmpty(list)) {
                continue;
            }
            Map<String, EmStatLine> map = new HashMap<>();
            for (EmStatLine ee : list) {
                map.put(ee.getClassId() + "_" + ee.getLine(), ee);
            }
            List<EmLineStatDto> dtolist = new ArrayList<>();
            EmLineStatDto dto = null;
            for (EmStatLine e : statLines) {
                if (!map.containsKey(e.getClassId() + "_" + e.getLine())) {
                    continue;
                }
                EmStatLine statLine = map.get(e.getClassId() + "_" + e.getLine());
                dto = new EmLineStatDto();
                dto.setScoreNum(NumberUtils.toInt(statLine.getScoreNum()));
                dto.setBlance(statLine.getBlance());
                dto.setRank(statLine.getRank());
                dto.setExamId(id);
                dto.setClassId(e.getClassId());
                dto.setLine(e.getLine());
                dto.setNum(NumberUtils.toInt(e.getScoreNum()) - NumberUtils.toInt(statLine.getScoreNum()));//上线人数差= 本次-参照
                dtolist.add(dto);
            }
            if (CollectionUtils.isEmpty(dtolist)) {
                continue;
            }
            for (int i = 0; i < dtolist.size(); i++) {
                EmLineStatDto s = dtolist.get(i);
                dtoMap2.put(s.getExamId() + "_" + s.getClassId() + "_" + s.getLine(), s);
            }
        }
        return dtoMap2;
    }

    @Override
    public Map<String, EmLineStatDto> findByDtoMap2(String unitId, String subId, String subType, Set<String> referIds, List<EmStatLine> statLines) {
        Map<String, EmLineStatDto> dtoMap2 = new HashMap<>();
        for (String id : referIds) {
            List<EmStatLine> list = findByUnitIdAndExamId(unitId, id, "0", subId, subType);
            if (CollectionUtils.isEmpty(list)) {
                continue;
            }
            Map<String, EmStatLine> map = new HashMap<>();
            for (EmStatLine ee : list) {
                map.put(ee.getClassId() + "_" + ee.getLine(), ee);
            }
            List<EmLineStatDto> dtolist = new ArrayList<>();
            EmLineStatDto dto = null;
            for (EmStatLine e : statLines) {
                if (!map.containsKey(e.getClassId() + "_" + e.getLine())) {
                    continue;
                }
                EmStatLine statLine = map.get(e.getClassId() + "_" + e.getLine());
                dto = new EmLineStatDto();
                dto.setScoreNum(NumberUtils.toInt(statLine.getScoreNum()));
                dto.setBlance(statLine.getBlance());
                dto.setRank(statLine.getRank());
                dto.setExamId(id);
                dto.setClassId(e.getClassId());
                dto.setLine(e.getLine());
                dto.setNum(NumberUtils.toInt(e.getScoreNum()) - NumberUtils.toInt(statLine.getScoreNum()));//上线人数差= 本次-参照
                dtolist.add(dto);
            }
            if (CollectionUtils.isEmpty(dtolist)) {
                continue;
            }
            sortDtoList(dtolist, dtoMap2);
        }
        return dtoMap2;
    }

    @Override
    public void deleteByStatObjectId(String... statObjectId) {
        if (statObjectId != null && statObjectId.length > 0) {
            emStatLineDao.deleteByStatObjectIdIn(statObjectId);
        }
    }

    @Override
    public List<EmStatLine> saveAllEntitys(EmStatLine... emStatLine) {
        return emStatLineDao.saveAll(checkSave(emStatLine));
    }

    @Override
    protected BaseJpaRepositoryDao<EmStatLine, String> getJpaDao() {
        return emStatLineDao;
    }

    @Override
    protected Class<EmStatLine> getEntityClass() {
        return EmStatLine.class;
    }
}
