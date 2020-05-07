package net.zdsoft.studevelop.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.entity.StuDevelopPerformItem;
import net.zdsoft.studevelop.data.entity.StuDevelopPerformItemCode;

import java.util.List;

public interface StudevelopPerformItemService extends BaseService<StuDevelopPerformItem,String> {

    public void savePerformItem(StuDevelopPerformItem stuDevelopPerformItem , List<StuDevelopPerformItemCode> adddPerformItemCodeList , List<StuDevelopPerformItemCode> updateformItemCodeList );

    public int getMaxDisplayOrderByUnitId(String unitId);

    public List<StuDevelopPerformItem> getStuDevelopPerformItemsByUnitIdAndGrade(String unitId, String itemGrade );

    public void deleteItemAndItemCode(String itemId);

    public StuDevelopPerformItem getStuDevelopPerformItemsByIdGrade(String unitId , String gradeCode ,String itemName ,String itemId);

    public void copyToGrade(String unitId,String[] toGradeCodes,String fromGradeCode);

    public List<StuDevelopPerformItem> getStuDevelopPerformItemsByUnitIdAndGrades(String unitId, String[] itemGrades );

    public void deleteByItemId(String unitId,String itemId);
}
