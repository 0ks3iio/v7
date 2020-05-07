package net.zdsoft.newgkelective.data.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PageInfo {
	// 每页多少条记录
	private int pageSize = 10;
	// 当前页
	private int pageIndex = 1;
	// 总页数
	private int pageCount;
	
	private int itemsNum;
	//页面显示页数5个
	private List<Integer> showCount;
	//如果传值 显示每页条数select
	private List<Integer> pageList;
	
	public void refresh() {
		pageCount = (int)Math.ceil(1.0*itemsNum/pageSize);
		if(pageIndex > pageCount) {
			pageIndex = pageCount;
		}
		if(pageIndex < 1) {
			pageIndex = 1;
		}
	}
	
	/**
	 * 返回指定页的第一条数据在所有数据中的位置，从 1 开始
	 * @return
	 */
	public int getStartIndex() {
		return pageSize*(pageIndex-1) + 1;
	}
	/**
	 * 返回指定页的最后一条数据在所有数据中的位置，从 1 开始
	 * @return
	 */
	public int getEndIndex() {
		if(pageSize*pageIndex > itemsNum) {
			return itemsNum;
		}
		return pageSize*pageIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public int getItemsNum() {
		return itemsNum;
	}
	public void setItemsNum(int itemsNum) {
		this.itemsNum = itemsNum;
	}
	/**
	 * 页面显示数据
	 */
	public void makeShowCount() {
			
		List<Integer> showCount=new ArrayList<>();
		//总共页数
		int total=this.pageCount<=0?1:this.pageCount;
		//当前页
		int nowIndex=this.pageIndex<=0?1:this.pageIndex;
        //中间取5个数
		if(total<=5) {
			for(int i=1;i<= total;i++) {
				showCount.add(i);
			}
		}else {
			//12345
			if(nowIndex<=3) {
				for(int i=1;i<= 5;i++) {
					showCount.add(i);
				}
			}else {
				//往前2个
				int k=0;
				for(int i=nowIndex-2;i<=nowIndex;i++) {
					showCount.add(i);
					k++;
				}
				for(int i=nowIndex+1;i<=total;i++) {
					if(k<5) {
						showCount.add(i);
						k++;
					}
				}
				if(k<5) {
					
					for(int i=nowIndex-3;i>0;i--) {
						showCount.add(i);
						k++;
						if(k>=5) {
							break;
						}
					}
					
				}
				//showCount排序
				Collections.sort(showCount);
			}
		}
		this.showCount=showCount;
		
	}
	public List<Integer> getShowCount() {
		return showCount;
	}
	public void setShowCount(List<Integer> showCount) {
		this.showCount = showCount;
	}

	public List<Integer> getPageList() {
		return pageList;
	}

	public void setPageList(List<Integer> pageList) {
		this.pageList = pageList;
	}
		
}
