package net.zdsoft.szxy.operation.unitmanage.dto;

import lombok.Data;

/**
 * 学生和家长账号列表查询Dto
 * @author yangkj
 * @since 2019/4/17 上午11:41
 */
@Data
public class StudentAndFamilyAccountQuery {
    private String gradeId;
    private String clazzId;

    public static StudentAndFamilyAccountQuery of(String clazzId) {
        StudentAndFamilyAccountQuery query = new StudentAndFamilyAccountQuery();
        query.setClazzId(clazzId);
        return query;
    }
}
