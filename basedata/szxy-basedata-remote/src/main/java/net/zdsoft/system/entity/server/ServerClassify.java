package net.zdsoft.system.entity.server;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 子系统分类表，用于桌面显示大分类，
 * 7。0桌面左侧第一级即取当前表的数据
 * 即一个大分类下可能包含多个子系统
 * ！学 ！<--------------------------- 大分类
 * ！校 ！----------
 * ！管 ！！课程开设 ！<---------------- 子系统
 * ！理 ！！系统管理 ！
 *       ！教务管理 ！
 * @author shenke
 * @since 2019/3/13 上午10:29
 */
@Entity
@Table(name = "base_server_classify")
public class ServerClassify extends BaseEntity<String> {

    public static final String SYSTEM_DEFAULT_CLASSIFY_UNIT_ID = "99999999999999999999999999999999";

    /**
     * 分类名称，页面展示，4个汉字（GBK编码）
     */
    @Column(
            length = 8
    )
    private String name;
    /**
     * 排序号
     */
    @Column(
            nullable = false
    )
    private Integer orderNumber;

    /**
     * 每个单位有不同的分类标准
     * 通过UnitId确定分类属于哪个单位
     * 当UnitId == 32个0时为系统内置分类
     * 有可能单位不会自定义分类，那么这时候就使用当前单位的顶级单位的配置
     *
     */
    @Column(
            updatable = false
    )
    private String unitId;

    private String iconPath;


    @Override
    public String fetchCacheEntitName() {
        return "serverClassify";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }
}
