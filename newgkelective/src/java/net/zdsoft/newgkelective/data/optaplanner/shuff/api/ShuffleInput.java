package net.zdsoft.newgkelective.data.optaplanner.shuff.api;

import java.util.ArrayList;
import java.util.List;

@com.thoughtworks.xstream.annotations.XStreamAlias(value = "shuffleInput2")
public class ShuffleInput {
	
	List<ShuffleResult> resultList=new ArrayList<>();

	public List<ShuffleResult> getResultList() {
		return resultList;
	}

	public void setResultList(List<ShuffleResult> resultList) {
		this.resultList = resultList;
	}
	
	
}
