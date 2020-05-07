package net.zdsoft.basedata.remote.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.dao.UnitDao;
import net.zdsoft.basedata.dto.UnitDto;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.enums.UnitTypeCode;
import net.zdsoft.basedata.remote.UnionCodeAlreadyExistsException;
import net.zdsoft.basedata.remote.UsernameAlreadyExistsException;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.RegionService;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;

import net.zdsoft.passport.exception.PassportException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("unitRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class UnitRemoteServiceImpl extends BaseRemoteServiceImpl<Unit, String> implements UnitRemoteService {

    @Autowired
    private UnitService unitService;
    @Autowired
    private RegionService regionService;

    @Autowired
    private UnitDao unitDao;

    @Override
    protected BaseService<Unit, String> getBaseService() {
        return unitService;
    }

    @Override
    public String findByUnitClassAndRegion(int unitClass, String... regionCodes) {
        return SUtils.s(unitService.findByUnitClassAndRegionCode(unitClass, regionCodes));
    }

    @Override
    public String findDirectUnits(String unitId, Integer unitClass) {
        List<Unit> units = unitService.findDirectUnitsByParentId(unitId, unitClass);
        return SUtils.s(units);
    }

    @Override
    public String findByUnionId(String unionId, int state, int unitClass) {
        List<Unit> units = unitService.findByUnionId(unionId, state, unitClass);
        return SUtils.s(units);
    }

    @Override
    public String findByUnitName(String... unitName) {
        return SUtils.s(unitService.findByUnitName(unitName));
    }

    @Override
    public String findByUnitName(String unitName, String page) {
        List<Unit> units = unitService.findByUnitName(unitName, Pagination.toPageable(page));
        long count = unitService.countByUnitName(unitName);
        return SUtils.s(units, count);
    }

    @Override
    public String findByParentIdAndUnitClass(String[] parentId, int state, int unitClass) {
        return SUtils.s(unitService.findByParentIdAndUnitClass(parentId, state, unitClass));
    }

    @Override
    public String findByParentIdAndUnitClass(String parentId, int state, int unitClass, String page) {
        List<Unit> units = unitService.findByParentIdAndUnitClass(parentId, state, unitClass,
                Pagination.toPageable(page));
        long count = unitService.countParentIdAndUnitClass(parentId, state, unitClass);
        return SUtils.s(units, count);
    }

    @Override
    public String findBySerialNumber(String sNumber, String eNumber) {
        return SUtils.s(unitService.findBySerialNumber(sNumber, eNumber));
    }

    @Override
    public String findByRegion(String region, int state) {
        return SUtils.s(unitService.findByRegion(region, state));
    }

    @Override
    public String findByUseType(int state, int useType) {
        return SUtils.s(unitService.findByUseType(state, useType));
    }

    @Override
    public String findByNameAndUnionCode(String unitName, String unionId, int state, int unitClass) {
        return SUtils.s(unitService.findByNameAndUnionCode(unitName, unionId, state, unitClass));
    }

    @Override
    public String findByUnderlingUnits(String unitName, String unionId) {
        return SUtils.s(unitService.findByUnderlingUnits(unitName, unionId));
    }

    @Override
    public String findByUnderlingUnits(String unitName, String unionId, String pagination) {
        List<Unit> ts = unitService.findByUnderlingUnits(unitName, unionId, Pagination.toPageable(pagination));
        return SUtils.s(ts, unitService.countUnderlingUnits(unitName, unionId));
    }

    @Override
    public String findByParentId(String parentId) {
        return SUtils.s(unitDao.findByParent(parentId));
    }

    @Override
    public String findByUnitClass(int unitClass) {
        return SUtils.s(unitService.findByUnitClass(unitClass));
    }

    @Override
    public String findTopUnit() {
        Unit topUnit = unitService.findTopUnit();
        return SUtils.s(topUnit);
    }

    @Override
    public String findTopUnit(String unitId) {
        Unit topUnit = unitService.findTopUnit(unitId);
        return SUtils.s(topUnit);
    }

    @Override
    public String findByUnitClassAndRegion(int unitClass, String regionCodes, Pagination page) {
        // TODO Auto-generated method stub
        return SUtils.s(unitService.findByUnitClassAndRegion(unitClass, regionCodes, page));
    }

    @Override
    public String findByRegionCode(String regionCode) {
        // TODO Auto-generated method stub
        return SUtils.s(unitDao.findByRegionCode(regionCode));
    }

    @Override
    public String findDirectUnits(String commitUnitId, Integer unitClassInteger, Pagination page) {
        // TODO Auto-generated method stub
        return SUtils.s(unitService.findDirectUnits(commitUnitId, unitClassInteger, page));
    }

    @Override
    public String findByRegionCodeUnitName(String unitName, String regionCode) {
        // TODO Auto-generated method stub
        return SUtils.s(unitDao.findByRegionCodeUnitName(unitName, regionCode));
    }

    @Override
    public Unit findTopUnitObject(String unitId) {
        return unitService.findTopUnit(unitId);
    }

    @Override
    public String findDirectUnits(String unitId, int unitClass) {
        List<Unit> units = unitService.findDirectUnitsByParentId(unitId, unitClass);
        return SUtils.s(units);
    }

    @Override
    public String findByUniCode(String unionCode) {
        // TODO Auto-generated method stub
        return SUtils.s(unitDao.findByUniCode(unionCode));
    }

    @Override
    public String findByUnionCode(String unionId, int state, int unitClass) {
        return SUtils.s(unitDao.findByUnionCode(unionId, state, unitClass));
    }

    @Override
    public String saveAllEntitys(String entitys) {
        Unit[] dt = SUtils.dt(entitys, new TR<Unit[]>() {
        });
        return SUtils.s(unitService.saveAllEntitys(dt));
    }

    @Override
    public String findByRegionAndUnitName(String region, String unitName, String page) {
        Pagination pagination = SUtils.dc(page, Pagination.class);
        List<Unit> unitList = unitService.findByRegionAndUnitName(region, unitName, pagination);

        return SUtils.s(findRegionName(unitList), pagination != null ? (long) pagination.getMaxRowCount() : 0);
    }

    @Override
    public String findByRegionAndUnitClassAndUnitIdIn(Integer[] unitClass, String regionCodes, String[] unitIds,
                                                      String page) {
        Pagination pagination = SUtils.dc(page, Pagination.class);
        List<Unit> unitList = unitService.findByRegionAndUnitClassAndUnitIdIn(unitClass, regionCodes, unitIds,
                pagination);

        return SUtils.s(findRegionName(unitList), pagination != null ? (long) pagination.getMaxRowCount() : 0);
    }

    @Override
    public String findByRegionAndUnitClassAndUnitIdNotIn(Integer[] unitClass, String regionCodes, String[] unitIds,
                                                         String page) {
        Pagination pagination = SUtils.dc(page, Pagination.class);
        List<Unit> unitList = unitService.findByRegionAndUnitClassAndUnitIdNotIn(unitClass, regionCodes, unitIds,
                pagination);
        return SUtils.s(findRegionName(unitList), pagination != null ? (long) pagination.getMaxRowCount() : 0);
    }

    /**
     * 查找行政区划
     *
     * @param unitList
     * @return
     * @author cuimq
     */
    private List<UnitDto> findRegionName(List<Unit> unitList) {
        List<UnitDto> unitDtoList = new ArrayList<UnitDto>();
        // 查找单位行政区划
        if (CollectionUtils.isNotEmpty(unitList)) {
            Set<String> regionCodeSet = new HashSet<String>();
            for (Unit unit : unitList) {
                if (StringUtils.isNotEmpty(unit.getRegionCode())) {
                    regionCodeSet.add(unit.getRegionCode());
                }
            }
            Map<String, String> fullCodeAndFullNameMap = new HashMap<String, String>();
            if (null != regionCodeSet && regionCodeSet.size() > 0) {
                fullCodeAndFullNameMap = regionService.findFullNameByFullCode(regionCodeSet
                        .toArray(new String[regionCodeSet.size()]));
            }

            for (Unit unit : unitList) {
                UnitDto unitDto = new UnitDto(unit);
                unitDto.setRegionName(fullCodeAndFullNameMap.get(unitDto.getRegionCode()));
                unitDtoList.add(unitDto);
            }

        }
        return unitDtoList;
    }

    @Override
    public void initTopUnit(String unitName, String regionCode) {
        unitService.addTopUnit(unitName, regionCode);
    }

    @Override
    public String findAutorizeServerUnit(Integer[] unitClass, String regionCode, Integer serverId, String page) {
        Pagination pagination = SUtils.dc(page, Pagination.class);
        List<Unit> unitList = unitService.findAutorizeServerUnit(unitClass, regionCode, serverId, pagination);
        return SUtils.s(findRegionName(unitList), pagination != null ? (long) pagination.getMaxRowCount() : 0);
    }

    @Override
    public String findUnAutorizeServerUnit(Integer[] unitClass, String regionCode, Integer serverId, String page) {
        Pagination pagination = SUtils.dc(page, Pagination.class);
        List<Unit> unitList = unitService.findUnAutorizeServerUnit(unitClass, regionCode, serverId, pagination);
        return SUtils.s(findRegionName(unitList), pagination != null ? (long) pagination.getMaxRowCount() : 0);
    }

    @Override
    public String findDirectUnitsByParentIds(Integer unitClass, String[] unitIds) {
        return SUtils.s(unitService.findDirectUnitsByParentIds(unitClass, unitIds));
    }

    @Override
    public String findNextUnionCode(String regionCode, int unitClass) {
        return unitService.findNextUnionCode(regionCode, unitClass);
    }

    @Override
    public String getAllSubUnitByParentId(String unitId) {
        Unit unit = unitDao.findById(unitId).orElse(null);
        if (unit != null) {
            return SUtils.s(unitDao.findAllSubUnitByUnionCode(unit.getUnionCode()));
        }
        return SUtils.s(Collections.emptyList());

    }

    @Override
    public String findByUnitClassAndUnionCode(int unitClassEdu, String... unionCodes) {
        return SUtils.s(unitService.findByUnitClassAndUnionCode(unitClassEdu, unionCodes));
    }

    @Override
    public String findTopUnitList() {
        List<Unit> units = unitService.findTopUnitList();
        return SUtils.s(units);
    }

    @Override
    public String findByOrganizationCode(String unitCode) {
        return SUtils.s(unitService.findByOrganizationCode(unitCode));
    }

    @Override
    public void deleteByUnitId(String unitId) {
        try {
            unitService.deleteUnit(unitId);
        } catch (PassportException e) {
            throw new RuntimeException("删除Passport 账号异常");
        }
    }

    @Override
    public String findUnionCodeSectionList(String unionCode, String section, boolean isedu, boolean isSchool) {
        return SUtils.s(unitService.findUnionCodeSectionList(unionCode, section, isedu, isSchool));
    }

    @Override
    public void createUnit(Unit unit, School school, String username, String password) throws UsernameAlreadyExistsException, UnionCodeAlreadyExistsException {
        try {
            unitService.addUnit(unit, school, username, password);
        } catch (PassportException e) {
            throw new RuntimeException("同步账号到Passport异常");
        }
    }

    @Override
    public String createUnionCode(String parentUnitId, Integer unitClass, Integer unitType, String fullCode) throws UnionCodeAlreadyExistsException {

        String unionCode = unitService.createUnionCode(parentUnitId, unitClass, unitType, fullCode);
        if (!UnitTypeCode.NO_EDUCATION_UNIT.equals(unitType) && unitDao.existsByUnionCodeAndIsDeleted(unionCode, 0)) {
            throw new UnionCodeAlreadyExistsException("该所在行政区域已存在相应的单位", unionCode);
        }
        return unionCode;
    }

    @Override
    public Boolean existsUnderUnits(String parentId) {
        return unitDao.countUnderUnits(parentId) > 0;
    }
    
//    @Override
//    public String findByAhUnitIds(String[] unitAhIds) {
//    	return SUtils.s(unitService.findByAhUnitIds(unitAhIds));
//    }
}
