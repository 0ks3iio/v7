package net.zdsoft.szxy.operation.record.service.impl;


import net.zdsoft.szxy.operation.record.dao.OperationRecordDao;
import net.zdsoft.szxy.operation.record.dto.OperateRecordDetail;
import net.zdsoft.szxy.operation.record.entity.OperationRecord;
import net.zdsoft.szxy.operation.record.enums.OperateCode;
import net.zdsoft.szxy.operation.record.enums.OperateType;
import net.zdsoft.szxy.operation.record.service.OperationRecordService;
import net.zdsoft.szxy.operation.security.SecurityUser;
import net.zdsoft.szxy.operation.servermanage.dao.OpServerExtensionDao;
import net.zdsoft.szxy.operation.servermanage.service.OpServerExService;
import net.zdsoft.szxy.operation.unitmanage.dao.UnitExtensionDao;
import net.zdsoft.szxy.operation.unitmanage.dto.EnableServerDto;
import net.zdsoft.szxy.utils.AssertUtils;
import net.zdsoft.szxy.utils.UuidUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("operationRecordService")
public class OperationRecordServiceImpl implements OperationRecordService {

    @Autowired
    private OperationRecordDao operationRecordDao;

    @Resource
    private OpServerExtensionDao opServerExtensionDao;

    @Resource
    private OpServerExService opServerExService;
    @Resource
    private UnitExtensionDao unitExtensionDao;

    @Override
    public Page<OperationRecord> getOperationRecords(String unitId, OperateCode operateType, Date start, Date end, Pageable page) {
        return operationRecordDao.findAll((Specification<OperationRecord>) (root, query, criteriaBuilder) -> {
            List<Predicate> ps = new ArrayList<>(4);

            if (StringUtils.isNotBlank(unitId)) {
                ps.add(criteriaBuilder.equal(root.<String>get("operateUnitId"), unitId));
            }
            if (null != operateType && null != operateType.getOperateCode() ) {
                ps.add(criteriaBuilder.equal(root.<Integer>get("operateType"), operateType.getOperateCode()));
            }
            if (null != start) {
                ps.add(criteriaBuilder.greaterThanOrEqualTo(root.get("operateTime"), start));
            }
            if (null != end) {
                ps.add(criteriaBuilder.lessThanOrEqualTo(root.get("operateTime"), end));
            }
            return query.where(ps.toArray(new Predicate[0])).orderBy(criteriaBuilder.desc(root.get("operateTime"))).getRestriction();
        }, page);
    }

    @Override
    public Integer getRenewalCount(String unitId) {
        Integer renewalCount = operationRecordDao.countByOperateUnitIdAndOperateType(unitId,3);
        return renewalCount;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void logOperate(String operateUnitId, Integer operateType, String content) {
        AssertUtils.notNull(operateType, "操作类型不能为空");
        AssertUtils.notNull(content, "日志内容不能为空");
        OperationRecord record = build(operateType);
        record.setOperateUnitId(operateUnitId);
        record.setOperateDetail(content);
        operationRecordDao.save(record);
    }

    private OperationRecord build(Integer operateType) {
        OperationRecord record = new OperationRecord();
        record.setId(UuidUtils.generateUuid());
        try {
            SecurityUser user = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            record.setOperatorId(user.getId());
            record.setId(UuidUtils.generateUuid());
            record.setOperateTime(new Date());
            record.setOperateType(operateType);
        } catch (Exception e) {
            //实际环境不会产生这个问题
        }
        return record;
    }

    @Override
    public void logOperate(String operateUnitId, Integer operateType, Function<OperationRecord, String> contentBuilder) {
        logOperate(operateUnitId, operateType, contentBuilder.apply(build(operateType)));
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void saveOperationRecord(OperationRecord record,List<OperateRecordDetail> detailList) {
        AssertUtils.notNull(record, "操作记录不能为空");
        AssertUtils.hasLength(record.getOperateUnitId(), 32, "操作单位不能为空");
        AssertUtils.notNull(record.getOperateType(), "操作码不能为空");
        if(detailList.size()==0){
            record.setId(UuidUtils.generateUuid());
            record.setOperateTime(Calendar.getInstance().getTime());
            saveDetail(record,detailList);
            operationRecordDao.save(record);
        }else{
            for (int i = 0; i <detailList.size() ; i+=5) {
                List<OperateRecordDetail> list = detailList.stream().skip(i).limit(5).collect(Collectors.toList());
                record.setId(UuidUtils.generateUuid());
                record.setOperateTime(Calendar.getInstance().getTime());
                saveDetail(record,list);
                operationRecordDao.save(record);
            }
        }

    }

    private void saveDetail(OperationRecord record,List<OperateRecordDetail> detailList) {
        if(detailList==null){
            return;
        }

        Integer type=record.getOperateType();
        Date unitExpireTime=null;
        if(type==OperateType.SYSTEM_TO_RENEWAL.getOperateCode()){
            unitExpireTime = unitExtensionDao.findByUnitId(record.getOperateUnitId()).getExpireTime();
        }
        String detail="";
        if(type<5){
            detail=getDetail(type,type==OperateType.UNIT_STOP.getOperateCode()?null:detailList.get(0),null);
        }else{
            List<EnableServerDto> all = opServerExService.getAllEnableServers();
            //获取 id-name 的map映射

            HashMap<String, String> serverMap = new HashMap<>();
            for (EnableServerDto dto:all){
                serverMap.put("serverCode_"+dto.getCode(),dto.getName());
            }
            for (int i = 0; i <detailList.size() ; i++) {
                if(i>0){
                    detail+=", ";
                }
                detail+=serverMap.get("serverCode_"+detailList.get(i).getServereCode())+"(";
                detail+=getDetail(type,detailList.get(i),unitExpireTime)+")";
            }
        }

        record.setOperateDetail(detail);
    }

    private String getDetail(Integer type, OperateRecordDetail detail,Date unitExpireTime) {

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

        int type1=type%5;
        if(type1==0){
            return "停用";
        }
        String opDetail="";
        String expireTime=detail.getTime()==null ?"永久":sdf.format(detail.getTime());
        if(type==OperateType.SYSTEM_TO_RENEWAL.getOperateCode() && detail.getTime()==null){
            expireTime=(unitExpireTime!=null?sdf.format(unitExpireTime):"永久")+"[跟随单位时间]";
        }
        switch (type1){
            case 1:
                opDetail=(detail.getNewState().equals(1)?"正式":"试用")+",到期时间为: "+expireTime;
                break;
            case 2:
                opDetail="试用转正式,到期时间为: "+expireTime;
                break;
            case 3:
                opDetail="续期,到期时间为: "+expireTime;
                break;
            case 4:
                opDetail="恢复使用";
                break;
        }
        return opDetail;
    }

}
