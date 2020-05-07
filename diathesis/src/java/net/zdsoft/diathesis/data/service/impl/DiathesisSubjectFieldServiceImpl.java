package net.zdsoft.diathesis.data.service.impl;

import net.zdsoft.diathesis.data.constant.DiathesisConstant;
import net.zdsoft.diathesis.data.dao.DiathesisSubjectFieldDao;
import net.zdsoft.diathesis.data.dto.DiathesisFieldDto;
import net.zdsoft.diathesis.data.entity.DiathesisSubjectField;
import net.zdsoft.diathesis.data.service.DiathesisCustomAuthorService;
import net.zdsoft.diathesis.data.service.DiathesisSubjectFieldService;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: panlf
 * @Date: 2019/8/13 10:08
 */
@Service("diathesisSubjectFieldService")
public class DiathesisSubjectFieldServiceImpl implements DiathesisSubjectFieldService {

    @Autowired
    private DiathesisCustomAuthorService diathesisCustomAuthorService;
    @Autowired
    private DiathesisSubjectFieldDao diathesisSubjectFieldDao;

    @Override
    public void saveAllEntity(List<DiathesisSubjectField> fieldList) {
        diathesisSubjectFieldDao.saveAll(fieldList);
    }

    @Override
    public List<DiathesisSubjectField> findByUnitAndSubjectType(String unitId, String subjectType) {
        List<DiathesisSubjectField> byUnitId = findByUnitId(unitId);
        return EntityUtils.filter2(byUnitId,x->x.getSubjectType().equals(subjectType));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<DiathesisSubjectField> findByUnitId(String unitId) {
        RedisUtils.hasLocked("diathesisSubFieldInitLock_" + unitId + "_78,");
        List<DiathesisSubjectField> list=diathesisSubjectFieldDao.findByUnitId(unitId);
        if(CollectionUtils.isEmpty(list)){
            autoSubjectField(unitId);
        }
        RedisUtils.unLock("diathesisSubFieldInitLock_" + unitId + "_78,");
        return diathesisSubjectFieldDao.findByUnitId(unitId);
    }

    @Override
    public Map<String, List<DiathesisFieldDto>> findMapByUnitId(String unitId) {
        List<DiathesisSubjectField> byUnitId = findByUnitId(unitId);
        return byUnitId.stream().collect(Collectors.groupingBy(x -> x.getSubjectType(), Collectors.mapping(x -> {
            DiathesisFieldDto dto = new DiathesisFieldDto();
            dto.setFieldId(x.getId());
            dto.setFieldName(x.getFieldName());
            dto.setIsUsing(x.getIsUsing());
            if(DiathesisConstant.COMPULSORY_MAP.keySet().contains(x.getFieldCode()) ||
                    DiathesisConstant.ELECTIVE_MAP.keySet().contains(x.getFieldCode())){
                dto.setCanModify(DiathesisConstant.DIATHESIS_NO);
            }else{
                dto.setCanModify(DiathesisConstant.DIATHESIS_YES);
            }
            if(DiathesisConstant.COMPULSORY_SCORE.equals(x.getFieldCode()) ||
                    DiathesisConstant.ELECTIVE_SCORE.equals(x.getFieldCode())){
                dto.setMustUsing(DiathesisConstant.DIATHESIS_YES);
            }else{
                dto.setMustUsing(DiathesisConstant.DIATHESIS_NO);
            }
            return dto;
        }, Collectors.toList())));
    }

    @Override
    public void deleteByIds(List<String> delFieldList) {
        if(CollectionUtils.isNotEmpty(delFieldList)) diathesisSubjectFieldDao.deleteByIds(delFieldList);
    }

    @Override
    public List<DiathesisSubjectField> findUsingField(String unitId) {
        return EntityUtils.filter2(findByUnitId(diathesisCustomAuthorService
                .findUsingUnitId(unitId, DiathesisConstant.AUTHOR_GOBAL_SET))
                ,x->DiathesisConstant.DIATHESIS_YES.equals(x.getIsUsing()));
    }

    @Override
    public DiathesisSubjectField findByUnitIdAndFieldCode(String unitId, String fieldCode) {
        List<DiathesisSubjectField> usingField = findUsingField(unitId);
        return usingField.stream().filter(x->x.getFieldCode().equals(fieldCode)).findFirst().get();
    }

    @Override
    public List<DiathesisSubjectField> findUsingFieldByUnitIdAndSubjectType(String unitId, String type) {
        List<DiathesisSubjectField> byUnitId = findByUnitId(diathesisCustomAuthorService
                .findUsingUnitId(unitId, DiathesisConstant.AUTHOR_GOBAL_SET));
        return EntityUtils.filter2(byUnitId
                ,x->DiathesisConstant.DIATHESIS_YES.equals(x.getIsUsing()) && type.equals(x.getSubjectType()));
    }


    private void autoSubjectField(String unitId){
        List<DiathesisSubjectField> list=new ArrayList<>();
        setFieldList(unitId,DiathesisConstant.SUBJECT_FEILD_BX,list);
        setFieldList(unitId,DiathesisConstant.SUBJECT_FEILD_XX,list);
        setFieldList(unitId,DiathesisConstant.SUBJECT_FEILD_XY,list);
        diathesisSubjectFieldDao.saveAll(list);
    }
    private void setFieldList(String unitId, String subjectType, List<DiathesisSubjectField> fieldList) {
        Map<String, String> map=new HashMap<>();
        if(DiathesisConstant.SUBJECT_FEILD_BX.equals(subjectType)){
            map=DiathesisConstant.COMPULSORY_MAP;
        }else if(DiathesisConstant.SUBJECT_FEILD_XX.equals(subjectType)){
            map=DiathesisConstant.ELECTIVE_MAP;
        }else if(DiathesisConstant.SUBJECT_FEILD_XY.equals(subjectType)){
            map=DiathesisConstant.ACADEMIC_MAP;
        }
        int i=0;
        for (String key :map.keySet()) {
            DiathesisSubjectField field = new DiathesisSubjectField();
            field.setId(UuidUtils.generateUuid());
            field.setFieldName(map.get(key));
            field.setFieldCode(key);
            field.setIsUsing("1");
            field.setModifyTime(new Date());
            field.setUnitId(unitId);
            field.setSortNum(++i);
            field.setSubjectType(subjectType);
            fieldList.add(field);
        }
    }
}
