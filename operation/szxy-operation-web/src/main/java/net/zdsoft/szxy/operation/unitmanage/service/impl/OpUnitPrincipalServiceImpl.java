package net.zdsoft.szxy.operation.unitmanage.service.impl;

import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.unitmanage.dao.OpUnitPrincipalDao;
import net.zdsoft.szxy.operation.unitmanage.entity.OpUnitPrincipal;
import net.zdsoft.szxy.operation.unitmanage.vo.UnitPrincipalEditVo;
import net.zdsoft.szxy.operation.unitmanage.vo.UnitPrincipalVo;
import net.zdsoft.szxy.operation.unitmanage.dto.OpUnitPrincipalQueryDto;
import net.zdsoft.szxy.operation.unitmanage.service.OpUnitPrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 *  单位负责人业务实现
 * @author zhanWenze
 * @since 2019年4月3日
 */
@Service("opUnitPrincipalService")
public class OpUnitPrincipalServiceImpl implements OpUnitPrincipalService {

    @Autowired
    private OpUnitPrincipalDao opUnitPrincipalDao;

    @Override
    public List<OpUnitPrincipal> findUnitPrincipalsByUnitId(String unitId) {
        return opUnitPrincipalDao.findByUnitId(unitId);
    }

    @Record(type = RecordType.Service)
    @Override
    public Page<UnitPrincipalVo> findPageByParentId(Set<String> regionsSet, String parentId, OpUnitPrincipalQueryDto opUnitPrincipalQueryDto, Pageable pageable) {
       return opUnitPrincipalDao.findPageByParentId(regionsSet,parentId, opUnitPrincipalQueryDto, pageable);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void addUnitPrincipalsByUnit(List<OpUnitPrincipal> opUnitPrincipal) {
        opUnitPrincipalDao.saveAll(opUnitPrincipal);
    }
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateUnitPrincipals(UnitPrincipalEditVo editVo) {
        List<OpUnitPrincipal> unitPrincipalList = editVo.getUnitPrincipals();
        opUnitPrincipalDao.saveAll(unitPrincipalList);
    }

    @Override
    public void delUnitPrincipalsByUnitId(String unitId) {
        opUnitPrincipalDao.deleteByUnitId(unitId);
    }
}
