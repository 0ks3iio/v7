package net.zdsoft.comprehensive.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CompreParamInfoShow {
	private int prefix;

	private int suffix;

	private List<Float> scoreList = new ArrayList<Float>();

	public int getPrefix() {
		return prefix;
	}

	public void setPrefix(int prefix) {
		this.prefix = prefix;
	}

	public int getSuffix() {
		return suffix;
	}

	public void setSuffix(int suffix) {
		this.suffix = suffix;
	}

	public List<Float> getScoreList() {
		return scoreList;
	}

	public void setScoreList(ArrayList<Float> scoreList) {
		this.scoreList = scoreList;
	}

	@Override
	public String toString() {
		return "CompreParamInfoShow [prefix=" + prefix + ", suffix=" + suffix
				+ "]";
	}

}
