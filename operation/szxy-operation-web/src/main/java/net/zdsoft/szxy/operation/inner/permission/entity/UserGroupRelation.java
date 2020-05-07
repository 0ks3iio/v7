package net.zdsoft.szxy.operation.inner.permission.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户和分组的关系表
 * @author shenke
 * @since 2019/3/29 下午3:08
 */
@Data
@Entity
@Table(name = "op_user_group_relation")
public class UserGroupRelation {

    @Id
    private String id;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 分组ID
     */
    private String groupId;
}
