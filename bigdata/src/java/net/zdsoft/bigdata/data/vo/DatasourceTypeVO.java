/**
 * FileName: DatasourceTypeVO
 * Author:   shenke
 * Date:     2018/4/18 下午3:06
 * Descriptor:
 */
package net.zdsoft.bigdata.data.vo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import net.zdsoft.bigdata.data.DataSourceType;

/**
 * @author shenke
 * @since 2018/4/18 下午3:06
 */
public class DatasourceTypeVO {

    private String name;
    private Integer value;

    @Transient
    public static List<DatasourceTypeVO> datasourceTypeVOs;

    static {
        DataSourceType[] dataSourceTypes = DataSourceType.values();
        datasourceTypeVOs = new ArrayList<>(dataSourceTypes.length);
        for (DataSourceType dataSourceType : dataSourceTypes) {
            datasourceTypeVOs.add(new DatasourceTypeVO(dataSourceType.getName(), dataSourceType.getValue()));
        }
    }

    private DatasourceTypeVO(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }
}
