package net.zdsoft.studevelop.data.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.dao.StuDevelopPerformItemDao;
import net.zdsoft.studevelop.data.entity.StuDevelopPerformItem;
import net.zdsoft.studevelop.data.entity.StuDevelopPerformItemCode;
import net.zdsoft.studevelop.data.service.StuDevelopPerformItemCodeService;
import net.zdsoft.studevelop.data.service.StudevelopMonthPerformanceService;
import net.zdsoft.studevelop.data.service.StudevelopPerformItemService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service("StuDevelopPerformItemService")
public class StudevelopPerformItemServiceImpl extends BaseServiceImpl<StuDevelopPerformItem ,String> implements StudevelopPerformItemService {
    @Autowired
    private StuDevelopPerformItemDao stuDevelopPerformItemDao;
    @Autowired
    private StuDevelopPerformItemCodeService stuDevelopPerformItemCodeService;

    @Autowired
    private StudevelopMonthPerformanceService studevelopMonthPerformanceService;
    protected BaseJpaRepositoryDao<StuDevelopPerformItem, String> getJpaDao() {
        return (BaseJpaRepositoryDao<StuDevelopPerformItem, String>) stuDevelopPerformItemDao;
    }

    @Override
    protected Class<StuDevelopPerformItem> getEntityClass() {
        return StuDevelopPerformItem.class;
    }

    @Override
    public void savePerformItem(StuDevelopPerformItem stuDevelopPerformItem,  List<StuDevelopPerformItemCode> addPerformItemCodeList , List<StuDevelopPerformItemCode> updateformItemCodeList) {
        int max = getMaxDisplayOrderByUnitId(stuDevelopPerformItem.getUnitId());
        if(max < 0 || max > 99){
            max = 0;
        }else{
            max ++;
        }
        int displayOrder = 1;
        List<StuDevelopPerformItemCode> allPerformItemCodeList = new ArrayList<StuDevelopPerformItemCode>();
        if(StringUtils.isEmpty(stuDevelopPerformItem.getId())){
            stuDevelopPerformItem.setId(UuidUtils.generateUuid());

            stuDevelopPerformItem.setDisplayOrder(max);
            stuDevelopPerformItem.setCreationTime(new Date());

            save(stuDevelopPerformItem);
            if(CollectionUtils.isNotEmpty(updateformItemCodeList)){
                Iterator<StuDevelopPerformItemCode> codeIterator = updateformItemCodeList.iterator();
                while (codeIterator.hasNext()){
                    StuDevelopPerformItemCode code = codeIterator.next();
                    if(code == null || StringUtils.isEmpty(code.getItemId())){
                        codeIterator.remove();
                    }else{
                        code.setItemId(stuDevelopPerformItem.getId());
                        code.setId(UuidUtils.generateUuid());
                        code.setCreationTime(new Date());
                    }
                }
                allPerformItemCodeList.addAll(updateformItemCodeList);
            }

        }else{
            stuDevelopPerformItem.setModifyTime(new Date());
            update(stuDevelopPerformItem,stuDevelopPerformItem.getId() , new String[]{"itemName"});

            if(CollectionUtils.isNotEmpty(updateformItemCodeList)){
                Iterator<StuDevelopPerformItemCode> codeIterator = updateformItemCodeList.iterator();
                while (codeIterator.hasNext()){
                    StuDevelopPerformItemCode code = codeIterator.next();
                    if(code == null || StringUtils.isEmpty(code.getItemId())){
                        codeIterator.remove();
                    }else{
                        code.setModifyTime(new Date());
                    }
                }
                allPerformItemCodeList.addAll(updateformItemCodeList);
                StuDevelopPerformItemCode itemCode = updateformItemCodeList.get(updateformItemCodeList.size()-1);
                displayOrder = itemCode.getDisplayOrder();
            }


        }
        if(CollectionUtils.isNotEmpty(addPerformItemCodeList)){
            for(StuDevelopPerformItemCode code : addPerformItemCodeList){
                code.setItemId(stuDevelopPerformItem.getId());
                code.setId(UuidUtils.generateUuid());
                code.setCreationTime(new Date());
                code.setDisplayOrder(displayOrder++);
            }
            allPerformItemCodeList.addAll(addPerformItemCodeList);

        }
        stuDevelopPerformItemCodeService.saveAll(allPerformItemCodeList.toArray(new StuDevelopPerformItemCode[]{}));
    }

    @Override
    public int getMaxDisplayOrderByUnitId(String unitId) {
        Integer max = stuDevelopPerformItemDao.getMaxDisplayOrderByUnitId(unitId);
        if(max == null){
            max = 0;
        }
        return  max;
    }

    @Override
    public List<StuDevelopPerformItem> getStuDevelopPerformItemsByUnitIdAndGrade(String unitId, String itemGrade) {
        List<StuDevelopPerformItem> itemList = stuDevelopPerformItemDao.getStuDevelopPerformItemsByUnitIdAndGrade(unitId ,itemGrade);
        if(CollectionUtils.isEmpty(itemList)){
            return itemList ;
        }
        List<String> itemIds = new ArrayList<String>();
        for(StuDevelopPerformItem item : itemList){
            itemIds.add(item.getId());
        }
        List<StuDevelopPerformItemCode> codeList = stuDevelopPerformItemCodeService.getPerformItemsByItemIds(itemIds.toArray(new String[0]));
        Map<String,List<StuDevelopPerformItemCode>> itemMap = new HashMap<String, List<StuDevelopPerformItemCode>>();
        for(StuDevelopPerformItemCode code : codeList){
            List<StuDevelopPerformItemCode> list = itemMap.get(code.getItemId());
            if( list == null){
                list = new ArrayList<StuDevelopPerformItemCode>();
                itemMap.put(code.getItemId() ,list);
            }
            list.add(code);
        }
        for(StuDevelopPerformItem item : itemList){
            List<StuDevelopPerformItemCode> list = itemMap.get(item.getId());
            if(CollectionUtils.isNotEmpty(list)){
                Collections.sort(list);
            }
            item.setCodeList(list);
        }
        return itemList;
    }

    @Override
    public void deleteItemAndItemCode(String itemId) {
        stuDevelopPerformItemDao.deleteById(itemId);
        stuDevelopPerformItemCodeService.deleteByItemId(itemId);
    }

    @Override
    public StuDevelopPerformItem getStuDevelopPerformItemsByIdGrade(final String unitId, final String itemGrade, final String itemName , final String itemId) {
        Specification<StuDevelopPerformItem> specification = new Specification<StuDevelopPerformItem>() {
            @Override
            public Predicate toPredicate(Root<StuDevelopPerformItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                list.add(cb.equal(root.get("unitId").as(String.class) ,unitId));
                list.add(cb.equal(root.get("itemGrade").as(String.class),itemGrade));
                list.add(cb.equal(root.get("itemName").as(String.class),itemName));
                if(StringUtils.isNotEmpty(itemId)){
                    list.add(cb.notEqual(root.get("id").as(String.class) ,itemId));
                }
                Predicate[] p = new Predicate[list.size()];
                query.where(list.toArray(p));

                return query.getRestriction();

            }
        };
        List<StuDevelopPerformItem> items = stuDevelopPerformItemDao.findAll(specification);
        if(CollectionUtils.isNotEmpty(items)){
            return items.get(0);
        }else{
            return  null;
        }
    }

    @Override
    public void copyToGrade(String unitId ,String[] toGradeCodes, String fromGradeCode) {
        String[] arr = new String[toGradeCodes.length+1];
        for(int i=0;i<arr.length;i++){
            if(i == arr.length -1){
                arr[i] = fromGradeCode;
            }else{
                arr[i] = toGradeCodes[i];
            }

        }
        Map<String,Set<String>> itemNameMap = new HashMap<String, Set<String>>();
        Map<String, List<StuDevelopPerformItem>> gradeItemMap = getCodeList(unitId,arr ,itemNameMap);

        List<StuDevelopPerformItem> fromList = gradeItemMap.get(fromGradeCode);
        Set<String> fromName = itemNameMap.get(fromGradeCode);
        List<StuDevelopPerformItem> batchItem = new ArrayList<StuDevelopPerformItem>();
        List<StuDevelopPerformItemCode> batchCodeItem = new ArrayList<StuDevelopPerformItemCode>();
        for(String grade : toGradeCodes){
            Set<String> nameSet = itemNameMap.get(grade);
            if(nameSet == null){
                nameSet = new HashSet<String>();
            }
            for(StuDevelopPerformItem item : fromList){
                if(nameSet.contains(item.getItemName())){
                    continue;
                }else{
                    StuDevelopPerformItem performItem = (StuDevelopPerformItem) item.clone();
                    performItem.setId(UuidUtils.generateUuid());
                    performItem.setCreationTime(new Date());
                    performItem.setItemGrade(grade);
                    batchItem.add(performItem);
                    List<StuDevelopPerformItemCode> itemCodeList = performItem.getCodeList();
                    for(StuDevelopPerformItemCode  code : itemCodeList){
                        StuDevelopPerformItemCode  performItemCode = (StuDevelopPerformItemCode) code.clone();
                        performItemCode.setId(UuidUtils.generateUuid());
                        performItemCode.setCreationTime(new Date());
                        performItemCode.setItemId(performItem.getId());
                        batchCodeItem.add(performItemCode);
                    }
                }
            }
        }

        saveAll(batchItem.toArray(new StuDevelopPerformItem[0]));

        stuDevelopPerformItemCodeService.saveAll(batchCodeItem.toArray(new StuDevelopPerformItemCode[0]));

    }

    @Override
    public List<StuDevelopPerformItem> getStuDevelopPerformItemsByUnitIdAndGrades(String unitId, String[] itemGrades) {
        return stuDevelopPerformItemDao.getStuDevelopPerformItemsByUnitIdAndGrades(unitId,itemGrades);
    }

    @Override
    public void deleteByItemId(String unitId, String itemId) {
        delete(itemId);
        studevelopMonthPerformanceService.deleteByItemId(unitId,itemId);
    }

    private Map<String, List<StuDevelopPerformItem>> getCodeList(String unitId ,String[] arrGradeCodes , Map<String,Set<String>> itemNameMap){
        List<StuDevelopPerformItem> itemList = getStuDevelopPerformItemsByUnitIdAndGrades(unitId ,arrGradeCodes);
        List<String> itemIds = new ArrayList<String>();
        Map<String, List<StuDevelopPerformItem>> gradeItemMap = new HashMap<String, List<StuDevelopPerformItem>>();
        for(StuDevelopPerformItem item : itemList){
            itemIds.add(item.getId());
            List<StuDevelopPerformItem> list = gradeItemMap.get(item.getItemGrade());
            if(list == null){
                list = new ArrayList<StuDevelopPerformItem>();
                gradeItemMap.put(item.getItemGrade(),list);
            }
            list.add(item);
            Set<String>  nameSet = itemNameMap.get(item.getItemGrade());
            if(nameSet == null){
                nameSet = new HashSet<String>();
                itemNameMap.put(item.getItemGrade(),nameSet);
            }
            nameSet.add(item.getItemName());
        }
        List<StuDevelopPerformItemCode> codeList = stuDevelopPerformItemCodeService.getPerformItemsByItemIds(itemIds.toArray(new String[0]));

        Map<String,List<StuDevelopPerformItemCode>> itemMap = new HashMap<String, List<StuDevelopPerformItemCode>>();
        for(StuDevelopPerformItemCode code : codeList){
            List<StuDevelopPerformItemCode> list = itemMap.get(code.getItemId());
            if(list == null){
                list = new ArrayList<StuDevelopPerformItemCode>();
                itemMap.put(code.getItemId() , list);
            }
            list.add(code);
        }
        for(StuDevelopPerformItem item : itemList){
            List<StuDevelopPerformItemCode> list = itemMap.get(item.getId());
            item.setCodeList(list);
        }
        return gradeItemMap;
    }

}
