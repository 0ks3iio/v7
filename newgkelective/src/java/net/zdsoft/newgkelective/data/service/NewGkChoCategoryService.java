package net.zdsoft.newgkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkChoCategory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface NewGkChoCategoryService extends BaseService<NewGkChoCategory, String>{

    void saveAndDeleteList(List<NewGkChoCategory> newGkChoCategoryList, String[] toArray);
	List<NewGkChoCategory> findByChoiceId(String unitId, String choiceId);

    /**
     * 返回各科目Id所对应的类目Id集合
     * @param unitId
     * @param choiceId
     * @return
     */
    Map<String, String> findMapByChoiceId(String unitId, String choiceId);
}
