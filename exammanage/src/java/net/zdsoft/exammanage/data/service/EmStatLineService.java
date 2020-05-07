package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.dto.EmLineStatDto;
import net.zdsoft.exammanage.data.entity.EmStatLine;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EmStatLineService extends BaseService<EmStatLine, String> {

    public void deleteByStatObjectId(String... statObjectId);

    public List<EmStatLine> saveAllEntitys(EmStatLine... emStatLine);

    public List<EmStatLine> findByUnitIdAndExamId(String unitId, String examId, String isDouble, String subId,
                                                  String subType);

    public Map<String, EmLineStatDto> findByDtoMap2(String unitId, String subId, String subType, Set<String> referIds, List<EmStatLine> statLines);

    public List<EmStatLine> findByUnitIdAndExamId(String unitId, String examId, String isDouble, String subId,
                                                  String subType, String sumId);

    public Map<String, EmLineStatDto> findByDoubleDtoMap2(String unitId, String subId, String subType, String sumId,
                                                          Set<String> referIds, List<EmStatLine> statLines);

}
