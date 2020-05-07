package net.zdsoft.syncdata.custom.gansu.service.impl;

import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.BaseSyncSaveRemoteService;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.syncdata.custom.gansu.constant.GanSuConstant;
import net.zdsoft.syncdata.custom.gansu.dto.CorporationDto;
import net.zdsoft.syncdata.custom.gansu.dto.DepartmentDto;
import net.zdsoft.syncdata.custom.gansu.service.GanSuService;
import net.zdsoft.syncdata.custom.gansu.service.JGXXJGDMService;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Designed By luf
 *
 * @author luf
 * @date 2019/8/19 9:58
 */
@Service("ganSuServiceImpl")
@Lazy(true)
public class GanSuServiceImpl implements GanSuService {

    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private DeptRemoteService deptRemoteService;
    @Autowired
    private JGXXJGDMService jgxxjgdmService;
    @Autowired
    private SysOptionRemoteService sysOptionRemoteService;
    @Autowired
    private BaseSyncSaveRemoteService baseSyncSaveService;
    @Override
    public void parserXml(String filePath) {
        List<Unit> unitEduList = SUtils.dt(unitRemoteService.findByUnionId(GanSuConstant.GAN_SU_UNION_CODE,-1,Unit.UNIT_CLASS_EDU),Unit.class);
        Map<String, Unit> unitMap = unitEduList.stream().collect(Collectors.toMap(Unit::getId, Function.identity()));
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new File(filePath));
            Element root = document.getRootElement();
            Element corporations = root.element("corporations");
            List nodes = corporations.elements("corporation");
            List<CorporationDto> corporationDTos = getDtoList(nodes, CorporationDto.class);
            List<Unit> unitList = new ArrayList<>();
            Random random = new Random();
            for (CorporationDto corporationDTo : corporationDTos) {
                Unit unit = new Unit();
                unit.setUnitName(corporationDTo.getOrganName());
                unit.setId(StringUtils.leftPad(corporationDTo.getOrganId(), 32, "0"));
                if (unitMap.containsKey(unit.getId())) {
                    continue;
                }
                unit.setParentId(StringUtils.leftPad(corporationDTo.getParentId(), 32, "0"));
                String regionCode = StringUtils.substring(corporationDTo.getOrganCode(),0,6);
                unit.setRegionLevel(
                       StringUtils.endsWith(regionCode, "0000") ? 3 : org.apache.commons.lang3.StringUtils.endsWith(regionCode, "00") ? 4 : 5);
                unit.setRegionCode(regionCode);
                unit.setUnitClass(Unit.UNIT_CLASS_EDU);
                int index = regionCode.indexOf("00");
                String organCode = corporationDTo.getOrganCode();
                int num =0;
                if (organCode.length() > 6) {
                   String code = organCode.substring(6);
                    num = NumberUtils.toInt(code);
                }
                if (num  == 0) {
                    if (index == -1  || index %2 !=0) {
                        unit.setUnionCode(regionCode );
                    } else {
                        unit.setUnionCode(regionCode.substring(0, index));
                    }
                    unit.setUnitType(Unit.UNIT_EDU_SUB);
                }else{
                    unit.setUnionCode(regionCode + StringUtils.leftPad(String.valueOf(num), 6, "0"));
                    unit.setUnitType(Unit.UNIT_NOTEDU_NOTSCH);
                }

                unit.setCreationTime(new Date());
//                if (index == 2) {
//                    unit.setUnitType(Unit.UNIT_EDU_TOP);
//                }else{
//                    unit.setUnitType(Unit.UNIT_EDU_SUB);
//                }
                unit.setIsDeleted(0);
                unit.setAddress(corporationDTo.getOrganAddress());
                unit.setLinkPhone(corporationDTo.getOrganPhone());
                unit.setFax(corporationDTo.getOrganFax());
                unit.setPostalcode(corporationDTo.getOrganZipCode());
                unit.setDisplayOrder(corporationDTo.getStruOrder());
                unit.setRemark(corporationDTo.getOrganDescription());
                unit.setModifyTime(new Date());
                unit.setUnitState(Unit.UNIT_MARK_NORAML);
                unit.setEventSource(0);
                unit.setOrgVersion(1);
                //shortName base_unit 里面没有该字段
                //顶级教育局 parent_id 为 00000000000000000000000000000000
                if (GanSuConstant.GAN_SU_TOP_UNIT_ID.equals(unit.getId())) {
                    unit.setParentId(net.zdsoft.framework.entity.Constant.GUID_ONE);
                    unit.setUnitType(Unit.UNIT_EDU_TOP);
                }
                unitList.add(unit);

            }
            Element departments = root.element("departments");
            List nodes2 = departments.elements("department");
            List<DepartmentDto> departmentDtos = getDtoList(nodes2, DepartmentDto.class);
            List<Dept> deptList = new ArrayList<>();
            List<Dept> deptTempList = SUtils.dt(deptRemoteService.findByUnitId(GanSuConstant.GAN_SU_TOP_UNIT_ID) ,Dept.class);
            Set<String> deptIdSet = deptTempList.stream().map(Dept::getId).collect(Collectors.toSet());
            for (DepartmentDto departmentDto : departmentDtos) {
                Dept dept = new Dept();
                dept.setUnitId(StringUtils.leftPad(departmentDto.getParentId(), 32, "0"));
                dept.setId(StringUtils.leftPad(departmentDto.getOrganId(), 32, "0"));
                if (deptIdSet.contains(dept.getId())) {
                    continue;
                }
                dept.setDisplayOrder(Integer.valueOf(departmentDto.getStruOrder()));
                dept.setDeptName(departmentDto.getOrganName());
                dept.setDeptShortName(departmentDto.getShortName());
                dept.setDeptType(1);
                dept.setIsDeleted(0);
                dept.setParentId("00000000000000000000000000000000");
                dept.setInstituteId("00000000000000000000000000000000");
                dept.setCreationTime(new Date());
                dept.setModifyTime(new Date());
                dept.setEventSource(0);
                dept.setDeptCode(StringUtils.substring(departmentDto.getOrganCode(),-1,-7));
                deptList.add(dept);
            }
            deptRemoteService.saveAll(deptList.toArray(new Dept[0]));
            if(CollectionUtils.isNotEmpty(unitList))
                baseSyncSaveService.saveUnit(unitList.toArray(new Unit[unitList.size()]));
        } catch (DocumentException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void saveEduAndSch() {
        String filePath = sysOptionRemoteService.findValue(Constant.FILE_PATH) + File.separator + "store" + File.separator  + "gansu" + File.separator + "edu20190814.xml";
//        parserXml("D:\\store\\gansu\\edu20190814.xml" );
        parserXml(filePath);
        jgxxjgdmService.transforSch();

    }

    public <T> List<T> getDtoList(List nodes, Class<T> t) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        List<T> dtos = new ArrayList<>();
        for (Iterator it = nodes.iterator(); it.hasNext(); ) {
            Element element = (Element) it.next();
            T obj = t.newInstance();
            for (Iterator iterator = element.elementIterator(); iterator.hasNext();) {
                Element ele = (Element) iterator.next();
                String name = ele.getQualifiedName();
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                Method method = t.getDeclaredMethod("set" +name, String.class);
                method.invoke(obj, ele.getText());
            }
            dtos.add( obj);
        }
        return dtos;
    }

//    public static void main(String[] args) {
////        new GanSuServiceImpl().parserXml("C:\\Users\\user\\Desktop\\223\\question.xml");
//        System.out.println("O0000000000000000296".length());
//    }
}
