package net.zdsoft.szxy.base.query;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询指定学校的学生
 * @author shenke
 * @since 2019/3/23 下午1:19
 */
@Data
public final class StudentQuery extends AbstractRegionsQuery implements Serializable {

    /**
     * 班级ID
     */
    private String classId;
    /**
     * 学生姓名 模糊查 like 'name%'
     */
    private String studentName;
    /**
     * 学号 模糊查询 like 'code%'
     */
    private String studentCode;
    /**
     * 学校ID
     */
    private String unitId;
    /**
     * 将会关联User表进行查询
     */
    private String username;
}
