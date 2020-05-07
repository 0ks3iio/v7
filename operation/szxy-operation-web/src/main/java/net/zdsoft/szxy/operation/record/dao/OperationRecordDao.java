package net.zdsoft.szxy.operation.record.dao;

import net.zdsoft.szxy.operation.record.entity.OperationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * 服务记录的查询和新增
 *
 * @author panlf   2019年1月15日
 */
@Repository
public interface OperationRecordDao extends JpaSpecificationExecutor<OperationRecord>, JpaRepository<OperationRecord, String> {
    /**
     * 查询
     * @param unitId
     * @return
     */
    Integer countByOperateUnitIdAndOperateType(String unitId,Integer operateType);
}
