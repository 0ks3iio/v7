package net.zdsoft.studevelop.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.framework.utils.StringUtils;
/**
 * studevelop_master_words
 * @author 
 * 
 */
@Entity
@Table(name="studevelop_master_words")
public class StudevelopMasterWords extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String unitId;
	/**
	 * 
	 */
	private String words11;
	/**
	 * 
	 */
	private String words12;
	/**
	 * 
	 */
	private String words13;
	/**
	 * 
	 */
	private String words14;
	/**
	 * 
	 */
	private String words15;
	/**
	 * 
	 */
	private String words16;
	/**
	 * 
	 */
	private String words21;
	/**
	 * 
	 */
	private String words22;
	/**
	 * 
	 */
	private String words23;
	/**
	 * 
	 */
	private String words24;
	/**
	 * 
	 */
	private String words31;
	/**
	 * 
	 */
	private String words32;
	/**
	 * 
	 */
	private String words33;
	/**
	 * 
	 */
	private Date modifyTime;
	/**
	 * 
	 */
	private Date creationTime;

	/**
	 * 设置
	 */
	public void setUnitId(String unitId){
		this.unitId = unitId;
	}
	/**
	 * 获取
	 */
	public String getUnitId(){
		return this.unitId;
	}
	/**
	 * 设置
	 */
	public void setWords11(String words11){
		this.words11 = words11;
	}
	/**
	 * 获取
	 */
	public String getWords11(){
		return this.words11;
	}
	/**
	 * 设置
	 */
	public void setWords12(String words12){
		this.words12 = words12;
	}
	/**
	 * 获取
	 */
	public String getWords12(){
		return this.words12;
	}
	/**
	 * 设置
	 */
	public void setWords13(String words13){
		this.words13 = words13;
	}
	/**
	 * 获取
	 */
	public String getWords13(){
		return this.words13;
	}
	/**
	 * 设置
	 */
	public void setWords14(String words14){
		this.words14 = words14;
	}
	/**
	 * 获取
	 */
	public String getWords14(){
		return this.words14;
	}
	/**
	 * 设置
	 */
	public void setWords15(String words15){
		this.words15 = words15;
	}
	/**
	 * 获取
	 */
	public String getWords15(){
		return this.words15;
	}
	/**
	 * 设置
	 */
	public void setWords16(String words16){
		this.words16 = words16;
	}
	/**
	 * 获取
	 */
	public String getWords16(){
		return this.words16;
	}
	/**
	 * 设置
	 */
	public void setWords21(String words21){
		this.words21 = words21;
	}
	/**
	 * 获取
	 */
	public String getWords21(){
		return this.words21;
	}
	/**
	 * 设置
	 */
	public void setWords22(String words22){
		this.words22 = words22;
	}
	/**
	 * 获取
	 */
	public String getWords22(){
		return this.words22;
	}
	/**
	 * 设置
	 */
	public void setWords23(String words23){
		this.words23 = words23;
	}
	/**
	 * 获取
	 */
	public String getWords23(){
		return this.words23;
	}
	/**
	 * 设置
	 */
	public void setWords24(String words24){
		this.words24 = words24;
	}
	/**
	 * 获取
	 */
	public String getWords24(){
		return this.words24;
	}
	/**
	 * 设置
	 */
	public void setWords31(String words31){
		this.words31 = words31;
	}
	/**
	 * 获取
	 */
	public String getWords31(){
		return this.words31;
	}
	/**
	 * 设置
	 */
	public void setWords32(String words32){
		this.words32 = words32;
	}
	/**
	 * 获取
	 */
	public String getWords32(){
		return this.words32;
	}
	/**
	 * 设置
	 */
	public void setWords33(String words33){
		this.words33 = words33;
	}
	/**
	 * 获取
	 */
	public String getWords33(){
		return this.words33;
	}
	/**
	 * 设置
	 */
	public void setModifyTime(Date modifyTime){
		this.modifyTime = modifyTime;
	}
	/**
	 * 获取
	 */
	public Date getModifyTime(){
		return this.modifyTime;
	}
	/**
	 * 设置
	 */
	public void setCreationTime(Date creationTime){
		this.creationTime = creationTime;
	}
	/**
	 * 获取
	 */
	public Date getCreationTime(){
		return this.creationTime;
	}
	@Override
	public String fetchCacheEntitName() {
		return "studevelopMasterWords";
	}
	
	public String getWordsVal(String words){
		String methodName = "get"+StringUtils.upperCase(words.charAt(0)+"")+words.substring(1);
		try {
			return (String) this.getClass().getMethod(methodName).invoke(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}