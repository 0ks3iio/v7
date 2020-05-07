package net.zdsoft.szxy.operation.inner.permission.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * 授权分组
 * @author shenke
 * @since 2019/3/29 下午3:06
 */
@Data
@Entity
@Table(name = "op_group")
public class Group {

    /**
     * 所有地区的regionCode
     */
    public static final String ALL_REGION = "000000";

    /**
     * 所有地区的regionCode可视化名称
     */
    public static final String ALL_REGION_NAME = "所有地区";

    /**
     * 当根据RegionCode找不到对应的行政区划的时候
     */
    public static final String NO_REGION_NAME = "未知的地区";

    @Id
    private String id;
    /**
     * 分组名称
     */
    private String name;
    /**
     * 分组所处理的行政地区的数据
     */
    private String regionCode;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
}