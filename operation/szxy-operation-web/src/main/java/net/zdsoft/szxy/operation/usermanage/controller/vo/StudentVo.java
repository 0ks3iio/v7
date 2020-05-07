package net.zdsoft.szxy.operation.usermanage.controller.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author shenke
 * @since 2019/4/16 下午1:42
 */
@Getter
@Setter
public final class StudentVo {

    private String id;
    private String studentName;
    private Date creationTime;
    private String schoolName;
    private String username;
}
