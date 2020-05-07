/*
* Project: v7
* Author : shenke
* @(#) WebArticle.java Created on 2016-10-11
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.nbwebsite.entity;

import java.text.SimpleDateFormat;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * @description： 
 * @author shenke
 * @version：Reversion 1.0
 * @date：2016-10-11 10:59:12
 */
@Entity
@Table(name="sitedata_web_article")
public class WebArticle extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4079362152549593245L;
	private String title;
	private String titleLink;
	private String author;
	private String articleSource;
	private String content;
	private String commitUserId;
	
	//不加此注解,请开启spring日志可观察到详细错误
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date commitTime;
	private String clickNumber;
	private String commitState;
	private String commitUnitId;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date releaseTime;
	private String auditUserId;
	private String auditUnitId;
	private String isTop;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date topTime;
	private String type;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date creationTime;
	
	private String auditOpinion;
	private String createUserId;

	//add
	private String introduction; //文章介绍
	private String titleImageUrl;//标题图片链接（地址）（在图文翻动中作为图片路径）
	private String titleImageName;//图片名称
	private String isDeleted;
	
	//add
	
	private String noOpinion;  //未通过意见
	private String pushType;  //推送栏目
	
	
	@Transient
	private String totalRelease;  //发布总数
	
	@Transient
	private String adoptRelease;  //审核通过总数
	
	@Transient
	private String allRelease;  //总量
	
	public String getAllRelease() {
		return allRelease;
	}
	public void setAllRelease(String allRelease) {
		this.allRelease = allRelease;
	}

	@Transient
	private String clicksRelease; //总的点击量
	
	@Transient
	private String unitName; //发布部门
	
	public String getNoOpinion() {
		return noOpinion;
	}
	public void setNoOpinion(String noOpinion) {
		this.noOpinion = noOpinion;
	}
	public String getPushType() {
		return pushType;
	}
	public void setPushType(String pushType) {
		this.pushType = pushType;
	}
	
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getTotalRelease() {
		return totalRelease;
	}
	public void setTotalRelease(String totalRelease) {
		this.totalRelease = totalRelease;
	}
	public String getAdoptRelease() {
		return adoptRelease;
	}
	public void setAdoptRelease(String adoptRelease) {
		this.adoptRelease = adoptRelease;
	}
	

	
	
	
	public String getClicksRelease() {
		return clicksRelease;
	}
	public void setClicksRelease(String clicksRelease) {
		this.clicksRelease = clicksRelease;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	/**
	 * title 
	 */
	public void setTitle(String title){
		this.title = title;
	}
	/**
	 * title 
	 */
	public String getTitle(){
		return this.title;
	}
	
	/**
	 * title_link 
	 */
	public void setTitleLink(String titleLink){
		this.titleLink = titleLink;
	}
	/**
	 * title_link 
	 */
	public String getTitleLink(){
		return this.titleLink;
	}
	
	/**
	 * author 
	 */
	public void setAuthor(String author){
		this.author = author;
	}
	/**
	 * author 
	 */
	public String getAuthor(){
		return this.author;
	}
	
	/**
	 * article_source 
	 */
	public void setArticleSource(String articleSource){
		this.articleSource = articleSource;
	}
	/**
	 * article_source 
	 */
	public String getArticleSource(){
		return this.articleSource;
	}
	
	/**
	 * content 
	 */
	public void setContent(String content){
		this.content = content;
	}
	/**
	 * content 
	 */
	public String getContent(){
		return this.content;
	}
	
	/**
	 * commit_user_id 
	 */
	public void setCommitUserId(String commitUserId){
		this.commitUserId = commitUserId;
	}
	/**
	 * commit_user_id 
	 */
	public String getCommitUserId(){
		return this.commitUserId;
	}
	
	/**
	 * commit_time 
	 */
	public void setCommitTime(java.util.Date commitTime){
		this.commitTime = commitTime;
	}
	/**
	 * commit_time 
	 */
	public java.util.Date getCommitTime(){
		return this.commitTime;
	}
	
	/**
	 * click_number 
	 */
	public void setClickNumber(String clickNumber){
		this.clickNumber = clickNumber;
	}
	/**
	 * click_number 
	 */
	public String getClickNumber(){
		return this.clickNumber;
	}
	
	/**
	 * commit_state 
	 */
	public void setCommitState(String commitState){
		this.commitState = commitState;
	}
	/**
	 * commit_state 
	 */
	public String getCommitState(){
		return this.commitState;
	}
	
	/**
	 * commit_unit_id 
	 */
	public void setCommitUnitId(String commitUnitId){
		this.commitUnitId = commitUnitId;
	}
	/**
	 * commit_unit_id 
	 */
	public String getCommitUnitId(){
		return this.commitUnitId;
	}
	
	/**
	 * release_time 
	 */
	public void setReleaseTime(java.util.Date releaseTime){
		this.releaseTime = releaseTime;
	}
	/**
	 * release_time 
	 */
	public java.util.Date getReleaseTime(){
		return this.releaseTime;
	}
	
	/**
	 * audit_user_id 
	 */
	public void setAuditUserId(String auditUserId){
		this.auditUserId = auditUserId;
	}
	/**
	 * audit_user_id 
	 */
	public String getAuditUserId(){
		return this.auditUserId;
	}
	
	/**
	 * audit_unit_id 
	 */
	public void setAuditUnitId(String auditUnitId){
		this.auditUnitId = auditUnitId;
	}
	/**
	 * audit_unit_id 
	 */
	public String getAuditUnitId(){
		return this.auditUnitId;
	}
	
	/**
	 * is_top 
	 */
	public void setIsTop(String isTop){
		this.isTop = isTop;
	}
	/**
	 * is_top 
	 */
	public String getIsTop(){
		return this.isTop;
	}
	
	/**
	 * top_time 
	 */
	public void setTopTime(java.util.Date topTime){
		this.topTime = topTime;
	}
	/**
	 * top_time 
	 */
	public java.util.Date getTopTime(){
		return this.topTime;
	}
	
	/**
	 * type 
	 */
	public void setType(String type){
		this.type = type;
	}
	/**
	 * type 
	 */
	public String getType(){
		return this.type;
	}
	
	/**
	 * creation_time 
	 */
	public void setCreationTime(java.util.Date creationTime){
		this.creationTime = creationTime;
	}
	/**
	 * creation_time 
	 */
	public java.util.Date getCreationTime(){
		return this.creationTime;
	}
	@Override
	public String fetchCacheEntitName() {
		return this.getClass().getSimpleName();
	}
	public String getAuditOpinion() {
		return auditOpinion;
	}
	public void setAuditOpinion(String auditOpinion) {
		this.auditOpinion = auditOpinion;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getTitleImageUrl() {
		return titleImageUrl;
	}

	public void setTitleImageUrl(String titleImageUrl) {
		this.titleImageUrl = titleImageUrl;
	}

	public String getTitleImageName() {
		return titleImageName;
	}

	public void setTitleImageName(String titleImageName) {
		this.titleImageName = titleImageName;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getDateStr(String type, String pattern) {
		try{
			if("commitTime".equals(type)) {
				return new SimpleDateFormat(pattern).format(commitTime);
			}
			if("releaseTime".equals(type)){
				return new SimpleDateFormat(pattern).format(releaseTime);
			}
			if("creationTime".equals(type)){
				return new SimpleDateFormat(pattern).format(creationTime);
			}
		}catch (Exception e){
			return StringUtils.EMPTY;
		}
		return StringUtils.EMPTY;
	}
}
