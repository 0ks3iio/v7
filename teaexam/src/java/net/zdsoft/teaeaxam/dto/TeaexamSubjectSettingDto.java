package net.zdsoft.teaeaxam.dto;

import java.util.List;

import net.zdsoft.teaeaxam.entity.TeaexamSiteSetting;
import net.zdsoft.teaeaxam.entity.TeaexamSubject;

/**
 * 
 * @author weixh 2018年10月26日
 */
public class TeaexamSubjectSettingDto {
	private TeaexamSubject subject;
	private List<TeaexamSiteSetting> sites;
	private int stuCount;// 学生总数
	private int hasCount;// 已分配数
	private int noCount;// 待分配数

	public TeaexamSubject getSubject() {
		return subject;
	}

	public void setSubject(TeaexamSubject subject) {
		this.subject = subject;
	}

	public List<TeaexamSiteSetting> getSites() {
		return sites;
	}

	public void setSites(List<TeaexamSiteSetting> sites) {
		this.sites = sites;
	}

	public int getStuCount() {
		return stuCount;
	}

	public void setStuCount(int stuCount) {
		this.stuCount = stuCount;
	}

	public int getHasCount() {
		return hasCount;
	}

	public void setHasCount(int hasCount) {
		this.hasCount = hasCount;
	}

	public int getNoCount() {
		return noCount;
	}

	public void setNoCount(int noCount) {
		this.noCount = noCount;
	}

}
