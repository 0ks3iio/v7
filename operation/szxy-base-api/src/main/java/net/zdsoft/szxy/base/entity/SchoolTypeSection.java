package net.zdsoft.szxy.base.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author shenke
 * @since 2019/3/20 下午4:35
 */
@Data
@Entity
@Table(name = "base_schtype_section")
public class SchoolTypeSection implements Serializable {

    @Id
    private String id;

    private String schoolType;
    private String section;
    private int isDeleted;
    private int eventSource;
}
