package net.zdsoft.bigdata.property.vo;

import java.util.List;

import com.google.common.collect.Lists;

public class MetaRelationVo {
	List<MetaRelationName> data = Lists.newArrayList();
	List<MetaRelationTo> links = Lists.newArrayList();

	public List<MetaRelationName> getData() {
		return data;
	}

	public void setData(List<MetaRelationName> data) {
		this.data = data;
	}

	public List<MetaRelationTo> getLinks() {
		return links;
	}

	public void setLinks(List<MetaRelationTo> links) {
		this.links = links;
	}

	public	class MetaRelationName {
		private String name;
		private int category;
		private String label;
		private String labelSub;
		private boolean draggable = true;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getCategory() {
			return category;
		}

		public void setCategory(int category) {
			this.category = category;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public String getLabelSub() {
			return MetaRelationVo.getSubStr(label, 9);
		}

		public void setLabelSub(String labelSub) {
			this.labelSub = labelSub;
		}

		public boolean isDraggable() {
			return draggable;
		}

		public void setDraggable(boolean draggable) {
			this.draggable = draggable;
		}

	}

	public class MetaRelationTo {
		private String source;
		private String target;

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}

		public String getTarget() {
			return target;
		}

		public void setTarget(String target) {
			this.target = target;
		}

	}
	
	 public static String getSubStr(String value,int length) {
	        int valueLength = 0;
	        String chinese = "[\u0391-\uFFE5]";
	        for (int i = 0; i < value.length(); i++) {
	            String temp = value.substring(i, i + 1);
	            if (temp.matches(chinese)) {
	                valueLength += 2;
	            } else {
	                valueLength += 1;
	            }
	            if(valueLength>=length){
	            	return value.substring(0, i)+"...";
	            }
	        }
	        return value;
	    }
	
}


