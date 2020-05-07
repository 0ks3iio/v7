package net.zdsoft.bigdata.extend.data.dto;

import java.util.List;

import com.google.common.collect.Lists;

public class ScaleDto {
	private long total;
	private List<Data> datas = Lists.newArrayList();
	
	
	public class Data{
		private String name;
		private long count;
		private float proportion;
		
		public String getName() {
			return name;
		}
		public Data setName(String name) {
			this.name = name;
			return this;
		}
		public long getCount() {
			return count;
		}
		public Data setCount(long count) {
			this.count = count;
			return this;
		}
		public float getProportion() {
			return proportion;
		}
		public Data setProportion(float proportion) {
			this.proportion = proportion;
			return this;
		}
		
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public Data getData() {
		return new Data();
	}

	public List<Data> getDatas() {
		return datas;
	}

	public void setDatas(List<Data> datas) {
		this.datas = datas;
	}
	/**
	 * 计算比例
	 */
	public void computeScale() {
		datas.parallelStream().forEach(t-> this.total+=t.getCount());
		if(this.total>0)
		datas.parallelStream().forEach(t-> t.setProportion(t.getCount()*100/this.total));
	}
	
}
