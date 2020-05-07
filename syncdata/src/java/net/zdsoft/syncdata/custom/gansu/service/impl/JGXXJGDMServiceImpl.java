package net.zdsoft.syncdata.custom.gansu.service.impl;

import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.BaseSyncSaveRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.syncdata.custom.gansu.constant.GanSuConstant;
import net.zdsoft.syncdata.custom.gansu.dao.JGXXJGDMDao;
import net.zdsoft.syncdata.custom.gansu.entity.JGXXJGDM;
import net.zdsoft.syncdata.custom.gansu.entity.JGXXJGSXBHQK;
import net.zdsoft.syncdata.custom.gansu.service.JGXXJGDMService;
import net.zdsoft.syncdata.custom.gansu.service.JGXXJGSXBHQKService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Designed By luf
 *
 * @author luf
 * @date 2019/8/20 11:02
 */
@Service("jGXXJGDMService")
public class JGXXJGDMServiceImpl  implements JGXXJGDMService {
    @Autowired
    private JGXXJGDMDao jgxxjgdmDao;
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private SchoolRemoteService schoolRemoteService;
    @Autowired
    private JGXXJGSXBHQKService jGXXJGSXBHQKService;
    @Autowired
    private BaseSyncSaveRemoteService baseSyncSaveService;
    @Override
    public void transforSch()  {

        List<JGXXJGDM>  list = jgxxjgdmDao.getAllList();
        List<School> schoolList = new ArrayList<>();
        List<Unit> unitList = SUtils.dt(unitRemoteService.findByUnionId(GanSuConstant.GAN_SU_UNION_CODE,-1,Unit.UNIT_CLASS_EDU),Unit.class);
        List<Unit> schList = SUtils.dt(unitRemoteService.findByUnionId(GanSuConstant.GAN_SU_UNION_CODE,-1,Unit.UNIT_CLASS_SCHOOL),Unit.class);
        Map<String, Unit> schMap = schList.stream().collect(Collectors.toMap(Unit::getId, Function.identity()));
//        Map<String, Unit> unitMap = unitList.stream().filter(item->Unit.UNIT_NOTEDU_NOTSCH !=item.getUnitType()).collect(Collectors.toMap(Unit::getRegionCode, Function.identity()));
        Map<String, Unit> unitMap = new HashMap<>();
        Set<String> regionCodeSet = new HashSet<>();
        for (Unit unit : unitList) {
            if (unit.getUnitType() != null && unit.getUnitType() != Unit.UNIT_NOTEDU_NOTSCH) {
                unitMap.put(unit.getRegionCode(), unit);
                regionCodeSet.add(unit.getRegionCode());
            }
        }
        List<School> existedSch = SUtils.dt(schoolRemoteService.findByRegionCodes(regionCodeSet.toArray(new String[0])) , School.class);
        Set<String> schoolIdSet = new HashSet<>();
        if (CollectionUtils.isNotEmpty(existedSch)) {
            schoolIdSet = existedSch.stream().map(item->item.getId()).collect(Collectors.toSet());
        }
        List<JGXXJGSXBHQK> list2 = jGXXJGSXBHQKService.getList();
        Map<String, JGXXJGDM> map1 = list.stream().collect(Collectors.toMap(JGXXJGDM::getXXJGID, Function.identity()));
//        List<School> schools = SUtils.dt(schoolRemoteService.findAll(), School.class);
//        Map<String, School> baseSchMap = schools.stream().collect(Collectors.toMap(School::getId, Function.identity()));
        Map<String, Integer> sequentialVal = new HashMap<>();
        for (JGXXJGSXBHQK jgxxjgsxbhqk : list2) {
            JGXXJGDM jgxxjgdm = map1.get(jgxxjgsxbhqk.getXXJGID());
            if (jgxxjgdm == null) {
                continue;
            }
            try {
                Method method = JGXXJGDM.class.getDeclaredMethod("set" + jgxxjgsxbhqk.getBGNR(), String.class);
                Method method2 = JGXXJGDM.class.getDeclaredMethod("get" + jgxxjgsxbhqk.getBGNR());
                String resutl = (String) method2.invoke(jgxxjgdm);
                if (resutl != null && resutl.equals(jgxxjgsxbhqk.getBGQ())) {
                    method.invoke(jgxxjgdm, jgxxjgsxbhqk.getBGH());
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        List<Unit> schoolUnitList = new ArrayList<>();
        for (JGXXJGDM jgxxjgdm : list) {
            School sch = new School();
            String id = jgxxjgdm.getXXJGID().replaceAll("-", "");
            sch.setId(id);
            sch.setCreationTime(DateUtils.string2Date(jgxxjgdm.getXTGXRQ(),"yyyy-MM-dd HH:mm:ss"));
            String updateTime = jgxxjgdm.getNF() + "-" + getDateStr(jgxxjgdm.getBBMC());
            sch.setModifyTime(DateUtils.string2Date(updateTime));
            sch.setSchoolName(jgxxjgdm.getXXJGMC());
            sch.setSchoolCode(jgxxjgdm.getXXJGBSM());
            sch.setRegionCode(jgxxjgdm.getXXJGDZDM().substring(0,6));
            sch.setSchoolType(jgxxjgdm.getXXJGBXLXM());
            if ("在用".equals(jgxxjgdm.getSFCX())) {
                sch.setIsDeleted(0);
            }else{
                sch.setIsDeleted(1);
            }
            if (!schoolIdSet.contains(sch.getId())) {
                schoolList.add(sch);
            }
            Unit unit = new Unit();
            String schoolType = sch.getSchoolType();
            Integer unitType;
            if(schoolType.startsWith("1")){
                unitType = Unit.UNIT_SCHOOL_KINDERGARTEN;
            }else if (schoolType.startsWith("2")) {
                unitType = Unit.UNIT_SCHOOL_ASP;
            }else if (schoolType.startsWith("3")) {
                unitType = Unit.UNIT_SCHOOL_ASP;
            }else if (schoolType.startsWith("5")) {
                unitType = Unit.UNIT_NOTEDU_NOTSCH;
            }else if (schoolType.startsWith("9")) {
                unitType = Unit.UNIT_NOTEDU_NOTSCH;
            }else{
                unitType = Unit.UNIT_SCHOOL_ASP;
            }
            unit.setUnitType(unitType);
            unit.setSchoolType(sch.getSchoolType());
            unit.setId(sch.getId());
            unit.setUnitName(sch.getSchoolName());
            unit.setCreationTime(sch.getCreationTime());
            unit.setModifyTime(sch.getModifyTime());
            unit.setRegionCode(sch.getRegionCode());
            Unit parent = unitMap.get(sch.getRegionCode());
            if (parent != null) {
                unit.setParentId(parent.getId());
            }
            String regionCode = sch.getRegionCode();
            Integer val = sequentialVal.get(regionCode);
            if (val == null) {
                val = 1;
            }else{
                val +=1;
            }
            sequentialVal.put(regionCode, val);
            String unionCode = regionCode +  StringUtils.leftPad(String.valueOf(val), 6, "0");
            unit.setUnionCode(unionCode);
            unit.setRegionLevel(
                    StringUtils.endsWith(regionCode, "0000") ? 3 : org.apache.commons.lang3.StringUtils.endsWith(regionCode, "00") ? 4 : 5);
            unit.setIsDeleted(0);
            unit.setUnitClass(Unit.UNIT_CLASS_SCHOOL);
            unit.setModifyTime(new Date());
            unit.setUnitState(Unit.UNIT_MARK_NORAML);
            unit.setEventSource(0);
            unit.setOrgVersion(1);
            if (!schMap.containsKey(sch.getId())) {
                schoolUnitList.add(unit);
            }
        }

         baseSyncSaveService.saveUnit(schoolUnitList.toArray(new Unit[schoolUnitList.size()]));

        schoolRemoteService.saveAll(schoolList.toArray(new School[0]));
    }

    public String getDateStr(String quanterName) {
        if (quanterName.indexOf("1")>=0) {
            return "01-01";
        }else if (quanterName.indexOf("2")>=0) {
            return "04-01";
        }else if (quanterName.indexOf("3")>=0) {
            return "07-01";
        }else{
            return "10-01";
        }
    }

}
