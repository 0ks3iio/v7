package net.zdsoft.studevelop.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.entity.StudevelopTemplateItem;

import java.util.List;
import java.util.Map;

/**
 * Created by luf on 2018/12/17.
 */
public interface StudevelopTemplateItemService extends BaseService<StudevelopTemplateItem ,String> {

    public String saveOrUpdateStudevelopTemplateItem(StudevelopTemplateItem item);

    public List<StudevelopTemplateItem> getTemplateItemListByObjectType(String templateId , String objectType,String isClosed);

    public void deleteStudevelopTemplateItem(String templateItemId);

    public List<StudevelopTemplateItem> getTemplateListByObjTypeStuId(String templateId ,String studentId ,String acadyear,String semester );

//    public List<StudevelopTemplateItem> getTemplateItemListByTemplateId(String templateId ,String isClosed);
}
