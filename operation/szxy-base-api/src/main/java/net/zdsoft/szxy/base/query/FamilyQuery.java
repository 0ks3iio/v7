package net.zdsoft.szxy.base.query;

import lombok.Data;

import java.io.Serializable;

/**
 * family动态查询条件
 * @author shenke
 * @since 2019/4/15 下午2:55
 */
@Data
public final class FamilyQuery extends AbstractRegionsQuery implements Serializable {

    private String realName;
    private String mobilePhone;
    private String unitId;
    /**
     * 将会关联用户表进行查询
     */
    private String username;
}
