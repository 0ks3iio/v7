package net.zdsoft.szxy.operation.record.service;

import net.zdsoft.szxy.operation.record.entity.OperationRecord;
import net.zdsoft.szxy.operation.record.enums.OperateType;
import net.zdsoft.szxy.utils.UuidUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;

/**
 * @author shenke
 * @since 2019/1/22 下午3:55
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class OperationRecordServiceTest {

    @Resource
    private OperationRecordService operationRecordService;

    @Transactional
    @Test
    public void test_getOperationRecords() {
        //init some
        OperationRecord operationRecord = new OperationRecord();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        operationRecord.setOperateTime(calendar.getTime());
        operationRecord.setOperateType(OperateType.SYSTEM_STOP.getOperateCode());
        operationRecord.setOperateUnitId(UuidUtils.generateUuid());
        operationRecord.setOperatorId(UuidUtils.generateUuid());
        operationRecordService.saveOperationRecord(operationRecord, null);

        Page<OperationRecord> operationRecordPage = operationRecordService.getOperationRecords(operationRecord.getOperateUnitId(),
                null, null, null, PageRequest.of(1, 5));
        Assert.assertEquals(1, operationRecordPage.getTotalElements());

        operationRecordPage = operationRecordService.getOperationRecords(null,
                null, null, null, PageRequest.of(1, 5));
        Assert.assertTrue(operationRecordPage.getTotalElements() > 0);

        operationRecordPage = operationRecordService.getOperationRecords(null,
                null, operationRecord.getOperateTime(), null, PageRequest.of(1, 5));
        Assert.assertEquals(1, operationRecordPage.getTotalElements());
    }
}
