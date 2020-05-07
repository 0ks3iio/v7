package net.zdsoft.szxy.operation.unitmanage.dto;

import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author ZJY
 */
@Data
public class UnitAndSchoolAndExDto {
   /**
    * ---单位的字段
    */
   private String id;
   private Integer unitType;
   private String unitName;
   private String regionCode;
   private String unionCode;
   private String schoolType;
   private String unitProperty;
   private String parentId;
   @Temporal(TemporalType.TIMESTAMP)
   private Date creationTime;
   private String postalcode;
   private String linkPhone;
   private String fax;
   private String email;
   @Temporal(TemporalType.TIMESTAMP)
   private Date modifyTime;
   private Integer unitClass;
   private String remark;


   /**
    * -------------------学校的字段
    */
   private Integer runSchoolType;
   private String englishName;
   private String schoolmaster;
   private String partyMaster;
   private String homepage;
   private Integer emphases;
   private Date buildDate;
   private String anniversary;
   private String governor;
   private Double builtupArea;
   private Double greenArea;
   private Double sportsArea;
   private String natureType;
   private String area;
   private String introduction;
   private String legalPerson;
   private String organizationCode;
   /**
    * ------单位扩展表
    */
   private String unitId;
   private Integer expireTimeType;
   @Temporal(TemporalType.TIMESTAMP)
   private Date expireTime;
   private Integer usingNature;
   private Integer usingState;

   @Override
   public String toString() {
      return "UnitAndSchoolAndExDto{" +
              "id='" + id + '\'' +
              ", unitType=" + unitType +
              ", unitName='" + unitName + '\'' +
              ", regionCode='" + regionCode + '\'' +
              ", unionCode='" + unionCode + '\'' +
              ", schoolType='" + schoolType + '\'' +
              ", unitProperty='" + unitProperty + '\'' +
              ", parentId='" + parentId + '\'' +
              ", creationTime=" + creationTime +
              ", postalcode='" + postalcode + '\'' +
              ", linkPhone='" + linkPhone + '\'' +
              ", fax='" + fax + '\'' +
              ", email='" + email + '\'' +
              ", modifyTime=" + modifyTime +
              ", unitClass=" + unitClass +
              ", remark='" + remark + '\'' +
              ", runSchoolType=" + runSchoolType +
              ", englishName='" + englishName + '\'' +
              ", schoolmaster='" + schoolmaster + '\'' +
              ", partyMaster='" + partyMaster + '\'' +
              ", homepage='" + homepage + '\'' +
              ", emphases=" + emphases +
              ", buildDate=" + buildDate +
              ", anniversary='" + anniversary + '\'' +
              ", governor='" + governor + '\'' +
              ", builtupArea=" + builtupArea +
              ", greenArea=" + greenArea +
              ", sportsArea=" + sportsArea +
              ", natureType='" + natureType + '\'' +
              ", area='" + area + '\'' +
              ", introduction='" + introduction + '\'' +
              ", legalPerson='" + legalPerson + '\'' +
              ", organizationCode='" + organizationCode + '\'' +
              ", unitId='" + unitId + '\'' +
              ", expireTimeType=" + expireTimeType +
              ", expireTime=" + expireTime +
              ", usingNature=" + usingNature +
              ", usingState=" + usingState +
              '}';
   }
}
