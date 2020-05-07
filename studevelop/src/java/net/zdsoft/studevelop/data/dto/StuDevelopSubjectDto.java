package net.zdsoft.studevelop.data.dto;

import java.util.List;

import net.zdsoft.studevelop.data.entity.StuDevelopCateGory;
import net.zdsoft.studevelop.data.entity.StuDevelopSubject;

public class StuDevelopSubjectDto {

	private StuDevelopSubject stuDevelopSubject;
	private List<StuDevelopCateGory> stuDevelopCateGoryList;
	public StuDevelopSubject getStuDevelopSubject() {
		return stuDevelopSubject;
	}
	public void setStuDevelopSubject(StuDevelopSubject stuDevelopSubject) {
		this.stuDevelopSubject = stuDevelopSubject;
	}
	public List<StuDevelopCateGory> getStuDevelopCateGoryList() {
		return stuDevelopCateGoryList;
	}
	public void setStuDevelopCateGoryList(
			List<StuDevelopCateGory> stuDevelopCateGoryList) {
		this.stuDevelopCateGoryList = stuDevelopCateGoryList;
	}	
}
