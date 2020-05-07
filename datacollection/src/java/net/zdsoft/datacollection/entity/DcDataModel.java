package net.zdsoft.datacollection.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DcDataModel {
	List<DcOperationData> dcOperationDatas = new ArrayList<DcOperationData>();
	Set<String> keySet = new HashSet<String>();

	public List<DcOperationData> getDcOperationDatas() {
		return dcOperationDatas;
	}

	public Set<String> getKeySet() {
		return keySet;
	}
}
