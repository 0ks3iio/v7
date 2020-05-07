package net.zdsoft.scoremanage.data.dto;

import java.util.List;

import net.zdsoft.scoremanage.data.entity.ScoreConversion;

public class ScoreConversionSaveDto {
	
	private List<ScoreConversion> scoreConversionList;

	public List<ScoreConversion> getScoreConversionList() {
		return scoreConversionList;
	}

	public void setScoreConversionList(List<ScoreConversion> scoreConversionList) {
		this.scoreConversionList = scoreConversionList;
	}
	
}
