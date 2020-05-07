package net.zdsoft.szxy.operation.unitmanage.service;

import net.zdsoft.szxy.operation.unitmanage.dto.StudentAndFamilyAccount;
import net.zdsoft.szxy.operation.unitmanage.dto.StudentAndFamilyAccountQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserManageService {

    /**
     * 获取指定单位的学生和家长信息
     * @param unitId 单位ID
     * @param query 动态查询条件
     * @param page 分页参数
     * @return
     */
    Page<StudentAndFamilyAccount> getStudentAndFamilyAccountsByUnitId(String unitId, StudentAndFamilyAccountQuery query, Pageable page);
}
