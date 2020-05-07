package net.zdsoft.studevelop.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.entity.StudevelopTemplateOptions;

import java.util.List;

/**
 * Created by luf on 2018/12/17.
 */
public interface StudevelopTemplateOptionsService extends BaseService<StudevelopTemplateOptions ,String> {

    public List<StudevelopTemplateOptions> getOptionsListByTemplateItemId(String[] itemIds);
}
