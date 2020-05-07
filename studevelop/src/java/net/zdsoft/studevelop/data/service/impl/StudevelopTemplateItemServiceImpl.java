package net.zdsoft.studevelop.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.dao.StudevelopTemplateItemDao;
import net.zdsoft.studevelop.data.entity.*;
import net.zdsoft.studevelop.data.service.StudevelopTemplateItemService;
import net.zdsoft.studevelop.data.service.StudevelopTemplateOptionsService;
import net.zdsoft.studevelop.data.service.StudevelopTemplateResultService;
import net.zdsoft.studevelop.data.service.StudevelopTemplateService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by luf on 2018/12/17.
 */
@Service("StudevelopTemplateItemService")
public class StudevelopTemplateItemServiceImpl extends BaseServiceImpl<StudevelopTemplateItem,String> implements StudevelopTemplateItemService {
    @Autowired
    private StudevelopTemplateItemDao studevelopTemplateItemDao;
    @Autowired
    private StudevelopTemplateOptionsService studevelopTemplateOptionsService;
    @Autowired
    private StudevelopTemplateService studevelopTemplateService;
    @Autowired
    private StudevelopTemplateResultService studevelopTemplateResultService;
    @Override
    protected BaseJpaRepositoryDao<StudevelopTemplateItem, String> getJpaDao() {
        return studevelopTemplateItemDao;
    }

    @Override
    protected Class<StudevelopTemplateItem> getEntityClass() {
        return StudevelopTemplateItem.class;
    }

    @Override
    public String saveOrUpdateStudevelopTemplateItem(StudevelopTemplateItem item) {

        List<StudevelopTemplate> templateList = studevelopTemplateService.getTemplateByCode(item.getAcadyear(),item.getSemester(),item.getGradeId(),item.getSection(),item.getCode() ,item.getUnitId());
        StudevelopTemplate template = null;
        if(CollectionUtils.isNotEmpty(templateList)){
            template = templateList.get(0);
        }else{
            template = new StudevelopTemplate();
            template.setAcadyear(item.getAcadyear());
            template.setSemester(item.getSemester());
            template.setSection(item.getSection());
            template.setGradeId(item.getGradeId());
            template.setCode(item.getCode());
            template.setId(UuidUtils.generateUuid());
            template.setCreationTime(new Date());
            template.setUnitId(item.getUnitId());
            studevelopTemplateService.save(template);
        }
        List<StudevelopTemplateItem> tempList = studevelopTemplateItemDao.getTemplateItemListByItemName(item.getItemName(),template.getId(),item.getObjectType(),item.getId());
        if(CollectionUtils.isNotEmpty(tempList)){
            return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("该项目名称在该类型下已存在！"));
        }
        if(StringUtils.isEmpty(item.getId())){
            item.setId(UuidUtils.generateUuid());
            item.setCreationTime(new Date());
            item.setTemplateId(template.getId());
            item.setIsClosed(StuDevelopConstant.HEALTH_IS_NOT_CLOSED);

        }else{
            item.setModifyTime(new Date());
        }
        if(StuDevelopConstant.TEMPLATE_MAINTAIN_OPTION.equals(item.getSingleOrInput())){
            List<StudevelopTemplateOptions> options  = null;
            options = item.getTemplateOptions();

            if(CollectionUtils.isNotEmpty(options)){
                Iterator<StudevelopTemplateOptions>  iterator = options.iterator();
                while(iterator.hasNext()){
                    StudevelopTemplateOptions opt = iterator.next();
                    if(opt == null){
                        iterator.remove();
                    }else if(StringUtils.isEmpty(opt.getOptionName())) {
                        iterator.remove();
                    }else{
                        if(StringUtils.isEmpty(opt.getId())){
                            opt.setId(UuidUtils.generateUuid());
                            opt.setCreationTime(new Date());
                            opt.setModifyTime(new Date());
                            opt.setTemplateItemId(item.getId());
                        }else{
                            opt.setModifyTime(new Date());
                        }
                    }
                }

            }
            studevelopTemplateOptionsService.saveAll(options.toArray(new StudevelopTemplateOptions[0]));
        }
        studevelopTemplateItemDao.save(item);
        return Json.toJSONString(new ResultDto().setSuccess(true).setCode("00").setMsg("操作成功！"));
    }

    @Override
    public List<StudevelopTemplateItem> getTemplateItemListByObjectType(String templateId, String objectType,String isClosed) {
        Specification<StudevelopTemplateItem> templateItemSpecification = new Specification<StudevelopTemplateItem>() {
            @Override
            public Predicate toPredicate(Root<StudevelopTemplateItem> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<>();
                ps.add(cb.equal(root.get("templateId").as(String.class), templateId));
                if (StringUtils.isNotEmpty(objectType)) {
                    ps.add(cb.equal(root.get("objectType").as(String.class), objectType));
                }
                if (StringUtils.isNotEmpty(isClosed)) {
                    ps.add(cb.equal(root.get("isClosed").as(String.class), isClosed));
                }
                cq.where(ps.toArray(new Predicate[0]));
//                cq.orderBy(cb.asc(root.get("objectType").as(String.class)));
                cq.orderBy(cb.asc(root.get("sortNumber").as(Integer.class)));
//                cq.orderBy(cb.desc(root.get("creationTime").as(Date.class)));
                return cq.getRestriction();
            }
        };
        List<StudevelopTemplateItem> list = findAll(templateItemSpecification);
        return list;
    }

    @Override
    public void deleteStudevelopTemplateItem(String templateItemId) {

        studevelopTemplateItemDao.deleteById(templateItemId);
        List<StudevelopTemplateOptions> options = studevelopTemplateOptionsService.findListBy("templateItemId" , templateItemId);
        studevelopTemplateOptionsService.deleteAll(options.toArray(new StudevelopTemplateOptions[0]));
    }

    @Override
    public List<StudevelopTemplateItem> getTemplateListByObjTypeStuId(String templateId, String studentId ,String acadyear ,String semester) {
        List<StudevelopTemplateItem> templateItemList = studevelopTemplateItemDao.getTemplateItemListByTemplateId(templateId);

        Set<String> templateIdSet = templateItemList.stream().map(t->t.getId()).collect(Collectors.toSet());
        List<StudevelopTemplateOptions> templateOptionsList = studevelopTemplateOptionsService.getOptionsListByTemplateItemId(templateIdSet.toArray(new String[0]));
        List<StudevelopTemplateResult> templateResultList = studevelopTemplateResultService.getTemplateResultByStudentId(templateIdSet.toArray(new String[0]), studentId ,acadyear,semester);

        Map<String, List<StudevelopTemplateOptions>> templateOptionsMap = templateOptionsList.stream().collect(Collectors.groupingBy(o -> o.getTemplateItemId()));
        Map<String, StudevelopTemplateResult> templateResultMap = templateResultList.stream().collect(Collectors.toMap(r -> r.getTemplateItemId(), Function.identity()));
        for (StudevelopTemplateItem item : templateItemList) {
            List<StudevelopTemplateOptions> list = templateOptionsMap.get(item.getId());
            if(CollectionUtils.isNotEmpty(list)){
                item.setTemplateOptions(list);
            }
            StudevelopTemplateResult result = templateResultMap.get(item.getId());
            if (result != null) {
                item.setTemplateResult(result);
            }

        }
        return templateItemList;
    }

//    @Override
//    public List<StudevelopTemplateItem> getTemplateItemListByTemplateId(String templateId,String isClosed) {
//
//        return studevelopTemplateItemDao.getTemplateItemListByTemplateId(templateId);
//    }


    public List<StudevelopTemplateItem> getTeemplateMapByObjType(String templateId) {
//        studevelopTemplateItemDao.f
        List<StudevelopTemplateItem> templateItemList = studevelopTemplateItemDao.getTemplateItemListByTemplateId(templateId);

        Set<String> templateIdSet = templateItemList.stream().map(t->t.getId()).collect(Collectors.toSet());
        List<StudevelopTemplateOptions> templateOptionsList = studevelopTemplateOptionsService.getOptionsListByTemplateItemId(templateIdSet.toArray(new String[0]));
//        List<StudevelopTemplateResult> templateResultList = studevelop
        Map<String, List<StudevelopTemplateOptions>> templateOptionsMap = templateOptionsList.stream().collect(Collectors.groupingBy(o -> o.getTemplateItemId()));
        Map<String, List<StudevelopTemplateItem>> map = new HashMap<>();
        for (StudevelopTemplateItem item : templateItemList) {
            List<StudevelopTemplateOptions> list = templateOptionsMap.get(item.getId());
            if(CollectionUtils.isNotEmpty(list)){
                item.setTemplateOptions(list);
            }

            List<StudevelopTemplateItem> itemList = map.get(item.getObjectType());
            if(CollectionUtils.isEmpty(itemList)){
                itemList = new ArrayList<>();

                map.put(item.getObjectType(), itemList);
            }
            itemList.add(item);
        }
        return null;
    }
}
