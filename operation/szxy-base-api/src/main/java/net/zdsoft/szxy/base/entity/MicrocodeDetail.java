package net.zdsoft.szxy.base.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 微代码
 * @author shenke
 * @since 2019/3/20 下午2:40
 */
@Data
@Entity
@Table(name = "base_mcode_detail")
public class MicrocodeDetail implements Serializable {

    @Id
    private String id;
    @Column(name = "mcode_id")
    private String microcodeId;
    private String thisId;
    @Column(name = "mcode_content")
    private String microcodeContent;
    private Integer isUsing;
    @Column(name = "mcode_type")
    private Integer microcodeType;
    /**
     * 排序号
     */
    private Integer displayOrder;
}
