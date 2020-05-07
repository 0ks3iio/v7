package net.zdsoft.studevelop.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.entity.StudevelopTemplate;

import java.util.List;

/**
 * Created by luf on 2018/12/14.
 */
public interface StudevelopTemplateService extends BaseService<StudevelopTemplate,String> {

    public List<StudevelopTemplate> getTemplateByCode(String acadyear , String semester , String gradeId , String section , String code,String unitId);
}
