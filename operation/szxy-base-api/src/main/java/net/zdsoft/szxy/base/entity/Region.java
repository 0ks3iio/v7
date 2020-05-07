package net.zdsoft.szxy.base.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author shenke
 * @since 2019/3/19 上午11:32
 */
@Data
@Entity
@Table(name = "base_region")
public class Region implements Serializable {

    @Id
    private String id;
    /**
     * 全地区编码
     */
    private String fullCode;
    /**
     * 全地区名称
     */
    private String fullName;
    /**
     * 地区编码
     */
    private String regionCode;
    /**
     * 地区名称
     */
    private String regionName;
    /**
     * 行政区划码类型
     */
    private String type;
    /**
     * 经度
     */
    private String latitude;
    /**
     * 纬度
     */
    private String longitude;
    /**
     * 定位经度
     */
    private String locationLatitude;
    /**
     * 定位纬度
     */
    private String locationLongitude;
    /**
     * 缩放级别（地图显示专用）
     */
    private int zoomLevel;

}
