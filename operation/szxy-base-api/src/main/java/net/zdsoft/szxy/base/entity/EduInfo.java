//package net.zdsoft.szxy.base.entity;
//
//import lombok.Data;
//
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//import java.io.Serializable;
//import java.util.Date;
//
///**
// * @author shenke
// * @since 2019/3/20 下午4:19
// */
//@Data
//@Entity
//@Table(name = "base_eduinfo")
//public class EduInfo implements Serializable {
//
//    @Id
//    private String id;
//
//    private String principal;
//    private Integer nationPoverty;
//    private Integer isAutonomy;
//    private Integer isFrontier;
//    private String manager;
//    private String director;
//    private String statistician;
//    private String eduCode;
//    private Integer isUseDomain;
//    private String domainUrl;
//    private String homepage;
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date creationTime;
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date modifyTime;
//    private Integer isDeleted;
//}
