package net.zdsoft.studevelop.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.entity.StuDevelopPerformItemCode;

import java.util.List;

public interface
StuDevelopPerformItemCodeService extends BaseService<StuDevelopPerformItemCode , String> {
    public void deleteByItemId(String itemId);

    public List<StuDevelopPerformItemCode> getAllByItemId(String itemId);

    public List<StuDevelopPerformItemCode> getPerformItemsByItemIds(String[] itemIds);

    public void deleteItemCodebyId(String unitId ,String id);
}
