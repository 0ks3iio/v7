package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.dto.EmArrangePlaceSettingDto;
import net.zdsoft.exammanage.data.entity.EmPlaceSetting;

import java.util.List;

public interface EmPlaceSettingService extends BaseService<EmPlaceSetting, String> {

    public List<EmPlaceSetting> findByExamId(String examId);

    public EmArrangePlaceSettingDto findDtoByExamId(String examId);

}
