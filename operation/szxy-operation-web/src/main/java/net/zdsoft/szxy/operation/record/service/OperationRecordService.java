package net.zdsoft.szxy.operation.record.service;

import net.zdsoft.szxy.operation.record.dto.OperateRecordDetail;
import net.zdsoft.szxy.operation.record.entity.OperationRecord;
import net.zdsoft.szxy.operation.record.enums.OperateCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * 查询 和 保存服务记录的接口
 *
 * @author panlf   2019年1月21日
 */
public interface OperationRecordService {


    void saveOperationRecord(OperationRecord operationRecord, List<OperateRecordDetail> detailList);

    /**
     * 记录操作
     * @param operateUnitId 操作所发生的数字校园的单位ID 可以为空，记录的是操作运营平台本身数据的日志
     * @param operateType 操作类型
     * @param content 操作内容
     */
    void logOperate(String operateUnitId, Integer operateType, String content);

    /**
     * 记录操作明细
     * @param operateUnitId 操作所发生的数字校园的单位ID 可以为空，记录的是操作运营平台本身数据的日志
     * @param operateType 操作类型
     * @param contentBuilder 方便代码编写
     */
    void logOperate(String operateUnitId, Integer operateType, Function<OperationRecord, String> contentBuilder);

    /**
     * 查询操作记录，可指定时间段、操作类型、单位
     * @see net.zdsoft.szxy.operation.record.enums.OperateType
     * @param unitId 不为空时查询指定单位的操作记录，为空时查询所有的操作记录
     * @param operateType 操作类型，查询指定操作类型的记录，可以为空
     * @param start 起始时间
     * @param end
     * @param page 分页参数
     */
    Page<OperationRecord> getOperationRecords(String unitId, OperateCode operateType, Date start, Date end, Pageable page);

    /**
     * 查询单位的续期次数
     * @param unitId
     * @return
     */
    Integer getRenewalCount(String unitId);
}
