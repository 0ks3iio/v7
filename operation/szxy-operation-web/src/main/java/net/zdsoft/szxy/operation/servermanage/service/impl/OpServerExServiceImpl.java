package net.zdsoft.szxy.operation.servermanage.service.impl;

import net.zdsoft.szxy.base.entity.ServerExtension;
import net.zdsoft.szxy.base.enu.ServerExtensionNature;
import net.zdsoft.szxy.base.enu.UnitExtensionState;
import net.zdsoft.szxy.operation.record.dto.OperateRecordDetail;
import net.zdsoft.szxy.operation.record.entity.OperationRecord;
import net.zdsoft.szxy.operation.record.enums.OperateType;
import net.zdsoft.szxy.operation.record.service.OperationRecordService;
import net.zdsoft.szxy.operation.servermanage.dao.OpServerExtensionDao;
import net.zdsoft.szxy.operation.servermanage.dao.ServerManageDao;
import net.zdsoft.szxy.operation.servermanage.dto.ServerExtensionDto;
import net.zdsoft.szxy.operation.servermanage.service.NoServerExtensionException;
import net.zdsoft.szxy.operation.servermanage.service.OpServerExService;
import net.zdsoft.szxy.operation.unitmanage.dto.EnableServerDto;
import net.zdsoft.szxy.operation.unitmanage.service.IllegalRenewalTimeException;
import net.zdsoft.szxy.utils.AssertUtils;
import net.zdsoft.szxy.utils.UuidUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 张帆远
 * @since 2019/1/17  上午11:16
 */

@Service("opServerExService")
public class OpServerExServiceImpl implements OpServerExService {
    private Logger logger = LoggerFactory.getLogger(OpServerExServiceImpl.class);

    @Resource
    private OpServerExtensionDao opServerExtensionDao;

    @Autowired
    private OperationRecordService operationRecordService;
    @Resource
    private ServerManageDao serverManageDao;

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void stopServer(String id,String operatorId) throws NoServerExtensionException {
        checkId(AssertUtils.notNull(id, "需指定停用子系统扩展id"));
        saveRecord(id,operatorId,OperateType.SYSTEM_STOP.getOperateCode(),new OperateRecordDetail());
        opServerExtensionDao.updateUsingStateById(UnitExtensionState.DISABLE, id);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void recoverServer(String id ,String operatorId) throws NoServerExtensionException {
        checkId(AssertUtils.notNull(id, "需指定恢复子系统扩展id"));
        saveRecord(id,operatorId,OperateType.SYSTEM_TO_RECOVER.getOperateCode(),new OperateRecordDetail());
        opServerExtensionDao.updateUsingStateById(UnitExtensionState.NORMAL, id);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void turnToOfficial(String id ,String operatorId) throws NoServerExtensionException {
        checkId(AssertUtils.notNull(id, "需指定调为正式子系统扩展id"));

        saveRecord(id,operatorId,OperateType.SYSTEM_TO_FORMAL.getOperateCode(),new OperateRecordDetail());

        opServerExtensionDao.updateUsingNatureById(ServerExtensionNature.OFFICIAL, id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void renewal(Date extendTime, String id ,String operatorId) throws NoServerExtensionException, IllegalRenewalTimeException {
        Optional<ServerExtension> opServerEx = checkId(id);
        OperateRecordDetail detail = new OperateRecordDetail();
        detail.setTime(extendTime);
        saveRecord(id,operatorId,OperateType.SYSTEM_TO_RENEWAL.getOperateCode(),detail);

        if (extendTime == null) {
            opServerEx.get().setExpireTime(null);
            opServerExtensionDao.save(opServerEx.get());
            return;
        }
        if (opServerEx.get().getExpireTime()!=null && opServerEx.get().getExpireTime().after(extendTime)) {
            throw new IllegalRenewalTimeException("续期时间不能在现有的过期时间之前", extendTime);
        }
        opServerEx.get().setExpireTime(extendTime);
        opServerExtensionDao.save(opServerEx.get());
    }

    @Override
    public List<EnableServerDto> getAllEnableServers() {
        return serverManageDao.getAllEnableServers();
    }

    @Override
    public List<ServerExtensionDto> findSystemByUnitId(String unitId) {
        return opServerExtensionDao.findSystemByUnitId(unitId);
    }



    @Override
    public List<EnableServerDto> unAuthorizeSystem(String unitId) {
        List<String> serverCode = opServerExtensionDao.findServerCodeByUnitId(unitId);
        List<EnableServerDto> allSystem = serverManageDao.getAllEnableServers();
        List<EnableServerDto> result = allSystem.stream().filter(item->!serverCode.contains(item.getCode()  ))
                .collect(Collectors.toList());
        return result;
    }


    @Override
    public String saveAuthoringSystem(List<ServerExtension> opServerExtensions, String operatorId) {
        List<ServerExtension> checked = opServerExtensions.stream().filter(x -> paramsCheck(x))
                .map(x -> {
                    x.setId(UuidUtils.generateUuid());
                    x.setUsingState(0);
                    return x;
                }).collect(Collectors.toList());


        if (opServerExtensions.size() == checked.size()) {
            opServerExtensionDao.saveAll(checked);

            saveRecord(opServerExtensions, operatorId);
            return "ok";
        } else {
            return "error";
        }
    }

    @Override
    public void updateSystem(List<ServerExtension> opServerExtensions, String operatorId) throws NoServerExtensionException,IllegalRenewalTimeException {
        for (ServerExtension x:opServerExtensions) {
            // 新增
            if(StringUtils.isBlank(x.getId())){
                ArrayList<ServerExtension> list = new ArrayList<>();
                list.add(x);
                saveAuthoringSystem(list,operatorId);
                continue;
            }
            Optional<ServerExtension> op = opServerExtensionDao.findById(x.getId());
            ServerExtension ex = op.get();
            //试用转正式
            if(ex.getUsingNature()==0 && x.getUsingNature()==1){
                turnToOfficial(x.getId(),operatorId);
                continue;
            }
            //续期
            if(!((x.getExpireTime()==null && ex.getExpireTime()==null) || ex.getExpireTime().after(x.getExpireTime()))){
                renewal(x.getExpireTime(),x.getId(),operatorId);
            }
        }
    }

    /**
     * 保存操作记录
     *
     * @param opServerExtensions
     */
    private void saveRecord(List<ServerExtension> opServerExtensions, String operatorId) {
        List<OperateRecordDetail> list = new ArrayList<>();
        for (ServerExtension ex : opServerExtensions) {
            OperateRecordDetail detail = new OperateRecordDetail();
            detail.setTime(ex.getExpireTime());
            detail.setNewState(ex.getUsingNature());
            detail.setServereCode(ex.getServerCode());
            list.add(detail);
        }

        OperationRecord record = new OperationRecord();
        record.setOperatorId(operatorId);
        record.setOperateUnitId(opServerExtensions.get(0).getUnitId());
        record.setOperateType(6);

        operationRecordService.saveOperationRecord(record, list);
    }

    private void saveRecord(String id,String operatorId,Integer type,OperateRecordDetail opDetail){
        Optional<ServerExtension> opEx = opServerExtensionDao.findById(id);
        OperationRecord record = new OperationRecord();
        record.setOperatorId(operatorId);
        record.setOperateUnitId(opEx.get().getUnitId());
        record.setOperateType(type);


        List<OperateRecordDetail> list = new ArrayList<>();
        OperateRecordDetail detail = new OperateRecordDetail();
        detail.setServereCode(opEx.get().getServerCode());
        if(type!=OperateType.SYSTEM_TO_RENEWAL.getOperateCode()){
            detail.setTime(opEx.get().getExpireTime());
        }
        detail.setOldState(opEx.get().getUsingNature());
        if(opDetail.getTime()!=null){
            detail.setTime(opDetail.getTime());
        }
        if(opDetail.getNewState()!=null){
            detail.setNewState(opDetail.getNewState());
        }
        if(opDetail.getOldState()!=null){
            detail.setOldState(opDetail.getOldState());
        }
        list.add(detail);
        operationRecordService.saveOperationRecord(record,list);
    }
    /**
     * 参数有效性验证
     *
     * @param opServerExtension
     * @return
     */
    private boolean paramsCheck(ServerExtension opServerExtension) {
        return ((opServerExtension.getUsingNature() == 0 || opServerExtension.getUsingNature() == 1)
                && (opServerExtension.getUsingState() == 0 || opServerExtension.getUsingState() == 1)
                && opServerExtension.getServerCode().length()>0 && opServerExtension.getServerCode().length()<=20
                && opServerExtensionDao.existsByUnitId(opServerExtension.getUnitId()) > 0);
    }

    private Optional checkId(String id) throws NoServerExtensionException {
        Optional<ServerExtension> opServerEx = opServerExtensionDao.findById(id);
        if (opServerEx == null) {
            throw new NoServerExtensionException(String.format("没有指定子系统:%s的扩展信息", id));
        }
        return opServerEx;
    }

}
