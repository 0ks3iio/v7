package net.zdsoft.szxy.operation.unitmanage.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author shenke
 * @since 2019/3/4 下午5:21
 */
@Data
public final class UnitDto {

    private String id;
    private String unitName;
    private String unionCode;
    private String regionCode;
    private Integer unitType;
    private Date expireTime;
    private Integer expireTimeType;
    private Integer usingNature;
    private Integer usingState;
    private Integer count;
    private String regionName;
    private Integer expireDay;
}
