package net.zdsoft.newgkelective.data.dto;

import java.util.ArrayList;
import java.util.List;

public class NewGkResultSaveDto {

	List<StudentResultDto> resultDtoList=new ArrayList<StudentResultDto>();

	public List<StudentResultDto> getResultDtoList() {
		return resultDtoList;
	}

	public void setResultDtoList(List<StudentResultDto> resultDtoList) {
		this.resultDtoList = resultDtoList;
	}
	
}
