//package net.zdsoft.diathesis.data.service.impl;
//
//
//import net.zdsoft.diathesis.data.constant.DiathesisConstant;
//import net.zdsoft.diathesis.data.dao.DiathesisImportStatusDao;
//import net.zdsoft.diathesis.data.entity.DiathesisImportStatus;
//import net.zdsoft.diathesis.data.service.DiathesisImportStatusService;
//import net.zdsoft.diathesis.data.service.DiathesisSubjectFieldService;
//import net.zdsoft.framework.utils.EntityUtils;
//import org.apache.commons.collections.CollectionUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//
///**
// * @Author: panlf
// * @Date: 2019/8/15 14:14
// */
//@Service
//public class DiathesisImportStatusImpl implements DiathesisImportStatusService {
//    @Autowired
//    private DiathesisImportStatusDao diathesisImportStatusDao;
//    @Autowired
//    private DiathesisSubjectFieldService diathesisSubjectFieldService;
//
//    @Override
//    public DiathesisImportStatus findByUnitIdAndFieldId(String unitId, String fieldId) {
//        return diathesisImportStatusDao.findByUnitIdAndFieldId(unitId,fieldId);
//    }
//
//    @Override
//    public List<DiathesisImportStatus> findByUnitIdAndFieldIdIn(String unitId, String[] fieldArr) {
//        if(fieldArr==null || fieldArr.length==0)return new ArrayList<>();
//        return diathesisImportStatusDao.findByUnitIdAndFieldIdIn(unitId,fieldArr);
//    }
//
//    /**
//     *     public static final String COMPULSORY_SCORE="COMPULSORY_SCORE";
//     *     public static final String COMPULSORY_GREDIT="COMPULSORY_GREDIT";
//     *     public static final String COMPULSORY_AUDITOR="COMPULSORY_AUDITOR";
//     *     public static final String COMPULSORY_PRINCIPAL="COMPULSORY_PRINCIPAL";
//     */
//    @Override
//    public List<String> saveAutoField(String unitId, String[] fieldArr) {
//        if(fieldArr==null || fieldArr.length==0)return new ArrayList<>();
//        List<String> fieldList = Arrays.asList(fieldArr);
//        Map<String, String> fieldMap = EntityUtils.getMap(diathesisSubjectFieldService.findByUnitId(unitId), x -> x.getId(), x -> x.getFieldCode());
//        List<DiathesisImportStatus> statusList = findByUnitIdAndFieldIdIn(unitId, fieldArr);
//        if(CollectionUtils.isEmpty(statusList))return new ArrayList<>();
//
//        EntityUtils.filter2(statusList,x->fieldList.contains(x.getFieldId()));
//
//        List<String> list = new ArrayList<>();
//        for (String s : fieldArr) {
//            if(DiathesisConstant.COMPULSORY_GREDIT.equals(fieldMap.get(s))
//                || DiathesisConstant.COMPULSORY_AUDITOR.equals(fieldMap.get(s))){
//                list.add(s);
//            }
//        }
//
//        return null;
//    }
//}
