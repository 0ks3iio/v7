package net.zdsoft.newgkelective.data.optaplanner2.domain;

public class ArrangeCapacityRange {

	private int classNum;
    private int minCapacity;//最小值
    private int maxCapacity;//最大值
    //新算法用到
    private int avgNum;//平均值
    private int folNum;//浮动值
    
    public int getClassNum() {
		return classNum;
	}
	public void setClassNum(int classNum) {
		this.classNum = classNum;
	}
	/**
     * 获取minCapacity
     * @return minCapacity
     */
    public int getMinCapacity() {
        return minCapacity;
    }
    /**
     * 设置minCapacity
     * @param minCapacity minCapacity
     */
    public void setMinCapacity(int minCapacity) {
        this.minCapacity = minCapacity;
    }
    /**
     * 获取maxCapacity
     * @return maxCapacity
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }
    /**
     * 设置maxCapacity
     * @param maxCapacity maxCapacity
     */
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
	public int getAvgNum() {
		return avgNum;
	}
	public void setAvgNum(int avgNum) {
		this.avgNum = avgNum;
	}
	public int getFolNum() {
		return folNum;
	}
	public void setFolNum(int folNum) {
		this.folNum = folNum;
	}
    
    
}
