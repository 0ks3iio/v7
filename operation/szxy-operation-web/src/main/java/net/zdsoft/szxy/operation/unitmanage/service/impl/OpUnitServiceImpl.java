package net.zdsoft.szxy.operation.unitmanage.service.impl;

import com.alibaba.fastjson.JSON;
import net.zdsoft.szxy.base.api.SchoolRemoteService;
import net.zdsoft.szxy.base.api.UnitRemoteService;
import net.zdsoft.szxy.base.entity.School;
import net.zdsoft.szxy.base.entity.ServerExtension;
import net.zdsoft.szxy.base.entity.Unit;
import net.zdsoft.szxy.base.entity.UnitExtension;
import net.zdsoft.szxy.base.enu.UnitClassCode;
import net.zdsoft.szxy.base.enu.UnitExtensionExpireType;
import net.zdsoft.szxy.base.enu.UnitExtensionNature;
import net.zdsoft.szxy.base.enu.UnitExtensionState;
import net.zdsoft.szxy.base.enu.UnitStateCode;
import net.zdsoft.szxy.base.enu.UnitTypeCode;
import net.zdsoft.szxy.base.exception.SzxyPassportException;
import net.zdsoft.szxy.base.exception.UnionCodeAlreadyExistsException;
import net.zdsoft.szxy.base.exception.UsernameAlreadyExistsException;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.record.dto.OperateRecordDetail;
import net.zdsoft.szxy.operation.record.entity.OperationRecord;
import net.zdsoft.szxy.operation.record.enums.OperateType;
import net.zdsoft.szxy.operation.record.service.OperationRecordService;
import net.zdsoft.szxy.operation.servermanage.service.NoServerExtensionException;
import net.zdsoft.szxy.operation.servermanage.service.OpServerExService;
import net.zdsoft.szxy.operation.unitmanage.dao.OpUnitDao;
import net.zdsoft.szxy.operation.unitmanage.dao.UnitExtensionDao;
import net.zdsoft.szxy.operation.unitmanage.dto.UnitAddDto;
import net.zdsoft.szxy.operation.unitmanage.dto.UnitDto;
import net.zdsoft.szxy.operation.unitmanage.entity.OpUnitPrincipal;
import net.zdsoft.szxy.operation.unitmanage.service.IllegalRenewalTimeException;
import net.zdsoft.szxy.operation.unitmanage.service.NoUnitExtensionException;
import net.zdsoft.szxy.operation.unitmanage.service.OpUnitPrincipalService;
import net.zdsoft.szxy.operation.unitmanage.service.OpUnitService;
import net.zdsoft.szxy.operation.utils.DateUtils;
import net.zdsoft.szxy.operation.utils.ObjectUtils;
import net.zdsoft.szxy.utils.AssertUtils;
import net.zdsoft.szxy.utils.UuidUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.zdsoft.szxy.base.enu.UnitExtensionNature.OFFICIAL;
import static net.zdsoft.szxy.base.enu.UnitExtensionState.DISABLE;


/**
 * @author yangkj
 * @since 2019/1/16
 */
@Service("opUnitService")
public class OpUnitServiceImpl implements OpUnitService {

    @Resource
    private OpUnitDao opUnitDao;

    @Resource
    private UnitExtensionDao unitExtensionDao;
    @Resource
    private UnitRemoteService unitRemoteService;

    @Resource
    private SchoolRemoteService schoolRemoteService;

    @Resource
    private OpServerExService opServerExService;

    @Resource
    private OperationRecordService operationRecordService;

    @Resource
    private OpUnitPrincipalService opUnitPrincipalService;

    @Record(type = RecordType.Service)
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void stopUnit(String unitId, String operatorId) throws NoUnitExtensionException {
        checkUnitId(AssertUtils.notNull(unitId, "需指定停用单位ID"));
        OperationRecord operationRecord = new OperationRecord();
        operationRecord.setOperatorId(operatorId);
        operationRecord.setOperateUnitId(unitId);
        operationRecord.setOperateType(OperateType.UNIT_STOP.getOperateCode());
        List<OperateRecordDetail> detailList = new ArrayList<>();
        operationRecordService.saveOperationRecord(operationRecord, detailList);
        unitExtensionDao.updateUsingStateByUnitId(DISABLE, unitId);
    }

    @Record(type = RecordType.Service)
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void recoverUnit(String unitId, String operatorId) throws NoUnitExtensionException {
        checkUnitId(AssertUtils.notNull(unitId, "需指定恢复单位ID"));
        OperationRecord operationRecord = new OperationRecord();
        operationRecord.setOperatorId(operatorId);
        operationRecord.setOperateUnitId(unitId);
        operationRecord.setOperateType(OperateType.UNIT_TO_RECOVER.getOperateCode());
        List<OperateRecordDetail> detailList = new ArrayList<>();
        OperateRecordDetail operateRecordDetail = new OperateRecordDetail();
        operateRecordDetail.setTime(unitExtensionDao.findByUnitId(unitId).getExpireTime());
        detailList.add(operateRecordDetail);
        operationRecordService.saveOperationRecord(operationRecord, detailList);
        unitExtensionDao.updateUsingStateByUnitId(UnitExtensionState.NORMAL, unitId);
    }

    @Record(type = RecordType.Service)
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void turnToOfficial(String unitId, String operatorId) throws NoUnitExtensionException {
        checkUnitId(AssertUtils.notNull(unitId, "需指定调为正式单位ID"));
        unitExtensionDao.updateUsingNatureByUnitId(OFFICIAL, unitId);
        operationRecordService.logOperate(unitId, OperateType.UNIT_TO_FORMAL.getOperateCode(), "转为正式单位");
    }

    @Record(type = RecordType.Service)
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateExpireTimeToPermanent(String unitId, String operatorId) throws NoUnitExtensionException {
        checkUnitId(AssertUtils.notNull(unitId, "需指定单位ID"));
        UnitExtension unitExtension = checkUnitId(unitId);
        unitExtension.setExpireTime(null);
        unitExtension.setExpireTimeType(UnitExtensionExpireType.PERMANENT);
        unitExtensionDao.save(unitExtension);
        operationRecordService.logOperate(unitId, OperateType.UNIT_TO_RENEWAL.getOperateCode(), "变更单位使用时间为永久");
    }

    @Record(type = RecordType.Service)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void renewal(Date extendTime, String unitId, String operatorId) throws NoUnitExtensionException, IllegalRenewalTimeException {
        UnitExtension unitExtension = checkUnitId(unitId);
        if (unitExtension.getExpireTime().after(extendTime)) {
            throw new IllegalRenewalTimeException("续期时间不能在现有的过期时间之前", extendTime);
        }
        unitExtension.setExpireTime(extendTime);
        unitExtension.setExpireTimeType(UnitExtensionExpireType.SPECIFY_TIME);
        operationRecordService.logOperate(unitId, OperateType.UNIT_TO_RENEWAL.getOperateCode(),
                "单位续期至：" + DateUtils.toString(extendTime));
        unitExtensionDao.save(unitExtension);
    }

    @Record(type = RecordType.Service)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void renewalServiceTime(Date extendTime, String unitId) throws NoUnitExtensionException, IllegalRenewalTimeException {
        UnitExtension unitExtension = checkUnitId(unitId);
        if (unitExtension.getServiceExpireTime().after(extendTime)) {
            throw new IllegalRenewalTimeException("续期时间不能在现有的服务过期时间之前", extendTime);
        }
        unitExtension.setServiceExpireTime(extendTime);
        unitExtensionDao.save(unitExtension);
        operationRecordService.logOperate(unitId, OperateType.UNIT_TO_RENEWAL.getOperateCode(),
                "单位服务时间续期至：" + DateUtils.toString(extendTime));
    }

    private UnitExtension checkUnitId(String unitId) throws NoUnitExtensionException {
        UnitExtension unitExtension = unitExtensionDao.findByUnitId(unitId);
        if (Optional.ofNullable(unitExtension).isPresent()) {
            return unitExtension;
        }
        throw new NoUnitExtensionException(String.format("没有指定单位:%s的扩展信息", unitId));
    }

    @Override
    public UnitExtension findByUnitId(String unitId) throws NoUnitExtensionException {
        return checkUnitId(unitId);
    }

    @Record(type = RecordType.Service)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveUnit(UnitAddDto unitAddDto, String operatorId) throws UnionCodeAlreadyExistsException, UsernameAlreadyExistsException, SzxyPassportException {
        Unit unit = unitAddDto.getUnit();
        unit.setId(UuidUtils.generateUuid());
        initUnit(unit);

        UnitExtension unitExtension = unitAddDto.getUnitExtension();
        initUnitExtension(unitExtension, unit.getId());

        School school = null;
        if (isEdu(unit.getUnitType())) {
            unit.setUnitClass(UnitClassCode.EDUCATION);
        } else {
            unit.setUnitClass(UnitClassCode.SCHOOL);
            school = unitAddDto.getSchool();
            school.setId(unit.getId());
            initSchool(school, unit.getUnitName(), unit.getRegionCode());
        }

        unitRemoteService.addUnit(unit, null, unitAddDto.getUsername(), "123456");
        unitExtensionDao.save(unitExtension);

        operationRecordService.logOperate(unit.getId(), OperateType.UNIT_ADD.getOperateCode(), record -> {
            return new StringBuilder().append("新增单位（")
                    .append(UnitExtensionNature.TRIAL.equals(unitExtension.getUsingNature()) ? "试用" : "正式")
                    .append("，到期时间：")
                    .append(UnitExtensionExpireType.PERMANENT.equals(unitExtension.getExpireTimeType()) ? "永久" : DateUtils.toString(unitExtension.getExpireTime()))
                    .append(")").toString();
        });

        if (!"[]".equals(unitAddDto.getServerExtensions())) {
            List<ServerExtension> opServerExtensions = JSON.parseArray(unitAddDto.getServerExtensions(), ServerExtension.class);
            for (ServerExtension s : opServerExtensions) {
                s.setUnitId(unit.getId());
            }
            opServerExService.saveAuthoringSystem(opServerExtensions, operatorId);
        }

        /**
         * 新增单位联系人信息
         */
        List<OpUnitPrincipal> collect = OpUnitPrincipalsToList(unitAddDto, unit);
        opUnitPrincipalService.addUnitPrincipalsByUnit(collect);
    }

    /**
     *  根据unitId更新单位扩展表星级
     * @param unitId
     * @param starLevel
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateUnitStarLevel(String unitId, Integer starLevel) {
        unitExtensionDao.updateStarLevelByUnitId(starLevel, unitId);
    }

    private void initUnitExtension(UnitExtension unitExtension, String unitId) {
        unitExtension.setId(UuidUtils.generateUuid());
        unitExtension.setUnitId(unitId);
        unitExtension.setUsingState(UnitExtensionState.NORMAL);
    }

    private void initSchool(School school, String unitName, String regionCode) {
        school.setSchoolName(unitName);
        school.setRegionCode(regionCode);
        school.setCreationTime(new Date());
        school.setModifyTime(new Date());
        school.setIsDeleted(0);
        school.setEventSource(0);
    }

    private void initUnit(Unit unit) {
        unit.setCreationTime(new Date());
        unit.setModifyTime(new Date());
        unit.setIsDeleted(0);
        unit.setEventSource(0);
        unit.setUseType(1);
        unit.setOrgVersion(1);
        unit.setUnitState(UnitStateCode.NORMAL);
    }

    private boolean isEdu(Integer unitType) {
        return ObjectUtils.equalsIn(unitType, UnitTypeCode.TOP_EDUCATION, UnitTypeCode.UNDERLING_EDUCATION, UnitTypeCode.NO_EDUCATION);
    }

    @Record(type = RecordType.Service)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUnitAndSchoolAndUnitEx(UnitAddDto unitAddDto, String operatorId) throws UnionCodeAlreadyExistsException, NoServerExtensionException, IllegalRenewalTimeException {
        Unit unit = unitAddDto.getUnit();
        String id = unit.getId();
        //是学校?更新unit school
        //不是?更新unit
        if (!(unit.getUnitType().equals(UnitTypeCode.TOP_EDUCATION)
                || unit.getUnitType().equals(UnitTypeCode.UNDERLING_EDUCATION)
                || unit.getUnitType().equals(UnitTypeCode.NO_EDUCATION))) {
            School school = unitAddDto.getSchool();
            school.setId(id);
            school.setSchoolName(unit.getUnitName());
            school.setRegionCode(unit.getRegionCode());
            school.setModifyTime(new Date());
            String[] schoolParam = {"schoolName", "regionCode", "schoolType", "runSchoolType", "englishName", "schoolmaster"
                    , "legalPerson", "partyMaster", "homepage", "emphases", "buildDate", "anniversary", "governor"
                    , "organizationCode", "area", "builtupArea", "greenArea", "sportsArea", "postalCode", "linkPhone"
                    , "fax", "email", "introduction", "remark", "modifyTime"};
            schoolRemoteService.updateSchool(school, schoolParam, unit.getParentId());

        } else {
            unitRemoteService.updateUnit(unit.getUnitName(), id);
        }
        UnitExtension unitEx = unitExtensionDao.findByUnitId(id);
        // 存在?试用转正式(可修改)，编辑(不进行操作)
        // 不存在?维护单位(做新增操作)
        if (unitEx == null) {
            UnitExtension unitExtension = unitAddDto.getUnitExtension();
            unitExtension.setId(UuidUtils.generateUuid());
            unitExtension.setUnitId(id);
            unitExtension.setUsingState(UnitExtensionState.NORMAL);
            unitExtensionDao.save(unitExtension);
        } else {
            //判断单位性质是否变化,保存操作记录
            String oldNature = unitEx.getUsingNature().toString();
            String newNature = unitAddDto.getUnitExtension().getUsingNature().toString();
            if (!oldNature.equals(newNature)) {
                OperationRecord record = new OperationRecord();
                record.setOperateUnitId(id);
                record.setOperateType(OperateType.UNIT_TO_FORMAL.getOperateCode());
                record.setOperatorId(operatorId);
                OperateRecordDetail detail = new OperateRecordDetail();
                detail.setTime(unitEx.getExpireTime());
                List<OperateRecordDetail> list = new ArrayList<>();
                list.add(detail);
                operationRecordService.saveOperationRecord(record, list);
            }
            if (unitEx.getExpireTime() != null) {
                if (unitAddDto.getUnitExtension().getExpireTime() == null) {
                    OperationRecord operationRecord = new OperationRecord();
                    operationRecord.setOperatorId(operatorId);
                    operationRecord.setOperateUnitId(id);
                    operationRecord.setOperateType(OperateType.UNIT_TO_RENEWAL.getOperateCode());
                    List<OperateRecordDetail> detailList = new ArrayList<>();
                    OperateRecordDetail operateRecordDetail = new OperateRecordDetail();
                    operateRecordDetail.setNewState(UnitExtensionExpireType.PERMANENT);
                    detailList.add(operateRecordDetail);
                    operationRecordService.saveOperationRecord(operationRecord, detailList);
                } else if (unitAddDto.getUnitExtension().getExpireTime().after(unitEx.getExpireTime())) {
                    OperationRecord operationRecord = new OperationRecord();
                    operationRecord.setOperatorId(operatorId);
                    operationRecord.setOperateUnitId(id);
                    operationRecord.setOperateType(OperateType.UNIT_TO_RENEWAL.getOperateCode());
                    List<OperateRecordDetail> detailList = new ArrayList<>();
                    OperateRecordDetail operateRecordDetail = new OperateRecordDetail();
                    operateRecordDetail.setTime(unitAddDto.getUnitExtension().getExpireTime());
                    detailList.add(operateRecordDetail);
                    operationRecordService.saveOperationRecord(operationRecord, detailList);
                }
            }
            unitEx.setExpireTime(unitAddDto.getUnitExtension().getExpireTime());
            unitEx.setExpireTimeType(unitAddDto.getUnitExtension().getExpireTimeType());
            unitEx.setUsingNature(unitAddDto.getUnitExtension().getUsingNature());
            unitEx.setContractNumber(unitAddDto.getUnitExtension().getContractNumber());
            unitExtensionDao.save(unitEx);
        }
        if (!StringUtils.isBlank(unitAddDto.getServerExtensions()) && !unitAddDto.getServerExtensions().equals("[]")) {
            List<ServerExtension> list = JSON.parseArray(unitAddDto.getServerExtensions(), ServerExtension.class);
            for (ServerExtension l : list) {
                l.setUnitId(id);
            }
            opServerExService.updateSystem(list, operatorId);
        }
        /**
         * 更新单位联系人信息
         */
        List<OpUnitPrincipal> collect = OpUnitPrincipalsToList(unitAddDto, unit);
        opUnitPrincipalService.delUnitPrincipalsByUnitId(unit.getId());
        opUnitPrincipalService.addUnitPrincipalsByUnit(collect);
    }

    public List<OpUnitPrincipal> OpUnitPrincipalsToList(UnitAddDto unitAddDto, Unit unit) {
        List<OpUnitPrincipal> opUnitPrincipals = new ArrayList<>();
        if (unitAddDto.getOpUnitPrincipalDtos()!=null){
            opUnitPrincipals = JSON.parseArray(unitAddDto.getOpUnitPrincipalDtos(), OpUnitPrincipal.class);
            return opUnitPrincipals.stream().map(x -> {
                OpUnitPrincipal opUnitPrincipal = new OpUnitPrincipal();
                opUnitPrincipal.setId(UuidUtils.generateUuid());
                opUnitPrincipal.setPhone(x.getPhone());
                opUnitPrincipal.setRealName(x.getRealName());
                opUnitPrincipal.setRemark(x.getRemark());
                opUnitPrincipal.setType(x.getType());
                opUnitPrincipal.setUnitId(unit.getId());
                return opUnitPrincipal;
            }).collect(Collectors.toList());
        }
        return opUnitPrincipals;
    }

    @Override
    public Page<UnitDto> findPageByParentId(String parentId, String unitType, String usingNature, Pageable pageable) {
        Page<UnitDto> page = opUnitDao.findPageByParentId(parentId, unitType, usingNature, pageable);
        return page;
    }

    @Record(type = RecordType.Service)
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void deleteUnit(String unitId) {
        unitRemoteService.deleteByUnitId(unitId);
        unitExtensionDao.deleteUnitExtensionsByUnitId(unitId);
    }

    @Override
    public List<Unit> findByUnitClassAndUnitType(Integer unitClass, Integer unitType) {
        return opUnitDao.findByUnitClassAndUnitType(unitClass, unitType);
    }


}
