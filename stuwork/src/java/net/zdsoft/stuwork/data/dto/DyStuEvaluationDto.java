package net.zdsoft.stuwork.data.dto;

import java.util.List;

import net.zdsoft.stuwork.data.entity.DyStuEvaluation;

public class DyStuEvaluationDto extends DyStuEvaluation {
	private List<DyBusinessOptionDto> dtoList;

	public List<DyBusinessOptionDto> getDtoList() {
		return dtoList;
	}

	public void setDtoList(List<DyBusinessOptionDto> dtoList) {
		this.dtoList = dtoList;
	}

}
