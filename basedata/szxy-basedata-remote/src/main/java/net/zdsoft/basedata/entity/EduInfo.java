package net.zdsoft.basedata.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "base_eduinfo")
public class EduInfo extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;
	private String principal;
	private Integer nationPoverty;
	private Integer isAutonomy;
	private Integer isFrontier;
	private String manager;
	private String director;
	private String statistician;
	private String eduCode;
	private Integer isUseDomain;
	private String domainUrl;
	private String homepage;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	private Integer isDeleted;

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public Integer getNationPoverty() {
		return nationPoverty;
	}

	public void setNationPoverty(Integer nationPoverty) {
		this.nationPoverty = nationPoverty;
	}

	public Integer getIsAutonomy() {
		return isAutonomy;
	}

	public void setIsAutonomy(Integer isAutonomy) {
		this.isAutonomy = isAutonomy;
	}

	public Integer getIsFrontier() {
		return isFrontier;
	}

	public void setIsFrontier(Integer isFrontier) {
		this.isFrontier = isFrontier;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getStatistician() {
		return statistician;
	}

	public void setStatistician(String statistician) {
		this.statistician = statistician;
	}

	public String getEduCode() {
		return eduCode;
	}

	public void setEduCode(String eduCode) {
		this.eduCode = eduCode;
	}

	public Integer getIsUseDomain() {
		return isUseDomain;
	}

	public void setIsUseDomain(Integer isUseDomain) {
		this.isUseDomain = isUseDomain;
	}

	public String getDomainUrl() {
		return domainUrl;
	}

	public void setDomainUrl(String domainUrl) {
		this.domainUrl = domainUrl;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String fetchCacheEntitName() {
		return "eduInfo";
	}
}
