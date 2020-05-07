package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmPlaceSettingDao;
import net.zdsoft.exammanage.data.dto.EmArrangePlaceSettingDto;
import net.zdsoft.exammanage.data.entity.EmArrange;
import net.zdsoft.exammanage.data.entity.EmPlaceSetting;
import net.zdsoft.exammanage.data.service.EmArrangeService;
import net.zdsoft.exammanage.data.service.EmPlaceSettingService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("emPlaceSettingService")
public class EmPlaceSettingServiceImpl extends BaseServiceImpl<EmPlaceSetting, String> implements EmPlaceSettingService {
    @Autowired
    private EmPlaceSettingDao emPlaceSettingDao;
    @Autowired
    private EmArrangeService emArrangeService;

    @Override
    public List<EmPlaceSetting> findByExamId(String examId) {
        return emPlaceSettingDao.findByExamId(examId);
    }

    /**
     * 取设置，
     * 如果未设置则默认随机排序
     * 列数为 7、8、8、7
     * 一个考场 默认安排30人
     */
    public EmArrangePlaceSettingDto findDtoByExamId(String examId) {
        EmArrangePlaceSettingDto dto = new EmArrangePlaceSettingDto();
        EmArrange arrange = emArrangeService.findByExamId(examId);
        if (arrange == null) {
            emPlaceSettingDao.deleteByExamId(examId);
            arrange = new EmArrange();
            arrange.setExamId(examId);
            arrange.setSumSeatNum(30);
            arrange.setType("0");
        }
        dto.setExamId(examId);
        dto.setSumSeatNum(arrange.getSumSeatNum());
        dto.setType(arrange.getType());
        List<EmPlaceSetting> settings = emPlaceSettingDao.findByExamId(examId);
        if (CollectionUtils.isEmpty(settings)) {
            settings = new ArrayList<>();
            EmPlaceSetting setting;
            for (int i = 1; i < 5; i++) {
                setting = new EmPlaceSetting();
                setting.setColumnNo(i);
                if (i == 2 || i == 3) {
                    setting.setSeatNum(8);
                } else {
                    setting.setSeatNum(7);
                }
                setting.setExamId(examId);
                settings.add(setting);
            }
        }
        dto.setSettings(settings);
        return dto;
    }

    @Override
    protected BaseJpaRepositoryDao<EmPlaceSetting, String> getJpaDao() {
        return emPlaceSettingDao;
    }

    @Override
    protected Class<EmPlaceSetting> getEntityClass() {
        return EmPlaceSetting.class;
    }

}
