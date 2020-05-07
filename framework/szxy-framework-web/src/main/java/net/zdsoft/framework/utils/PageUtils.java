package net.zdsoft.framework.utils;

import net.zdsoft.framework.entity.Pagination;

/**
 * 主要用于页面显示的工具类.
 * 
 */
public final class PageUtils {
	
	private PageUtils(){};

	public static String paginationLoad(String url, Pagination page) {
		return createPagination(url, page).append(loadContent(url, page))
				.toString();
	}

	private static StringBuffer createPagination(String url, Pagination page) {

		int maxPage = page.getMaxPageIndex();//最大页数
		int maxRow = page.getMaxRowCount();//一共多少行
		int curPage = page.getPageIndex();//当前页
		int pageSize = page.getPageSize();//每页多少行
		// 分页栏当前页前后延续的长度
		int m = 2;
		int[] paginalValues=new int[]{10,20,50,100,150,200,500,1000};
		StringBuffer sb = new StringBuffer();
		/**
		 * <ul class="pagination pull-right">
			    <li><a href="#">&lt;</a></li>
			    <li class="active"><a href="#">1</a></li>
			    <li>...</li>
			    <li><a href="#">2</a></li>
			    <li><a href="#">3</a></li>
			    <li><a href="#">4</a></li>
			    <li><a href="#">5</a></li>
			    <li>...</li>
			    <li><a href="#">45</a></li>
			    <li><a href="#">&gt;</a></li>
			    <li class="pagination-other">
			    	跳到：
			    	<input type="text" class="form-control">
			    	<button class="btn btn-white">确定</button>
			    </li>
			    <li class="pagination-other">
			    	共400条
			    </li>
			    <li class="pagination-other">
			    	每页
			    	<select name="" id="" class="form-control">
						<option value="">600</option>
						<option value="">20</option>
						<option value="">30</option>
					</select>
			    	 条
			    </li>
			</ul>
		 */
		//计算中间页码，除去第一页和最后一页
		boolean firstDot = true;//前面是否需要省略...
		boolean lastDot = true;//后面是否需要省略...
		StringBuffer sb2 = new StringBuffer();
		if (maxPage > 2) {
			if (curPage - m > 0) {
				for (int i = (curPage + m > maxPage ? maxPage - m * 2 : curPage
						- m); i <= (curPage + m > maxPage ? maxPage : curPage
								+ m); i++) {
					if (i == maxPage - 1)
						lastDot = false;
					if (i == 2)
						firstDot = false;
					if (i <= 0 || i == 1 || i == maxPage) {
						continue;
					}
					if(curPage == i){
						sb2.append("<li class='active'><a href='#'>"+i+"</a></li>");
					}else{
						sb2.append("<li><a href='#' onclick='jumpPage("+i+")'>"+i+"</a></li>");
					}
				}
			} else {
				for (int i = 1; i <= (2 * m + 1 > maxPage ? maxPage : 2 * m + 1); i++) {
					if (i == maxPage - 1)
						lastDot = false;
					if (i == 2)
						firstDot = false;
					if (i == 1 || i == maxPage)
						continue;
					if(curPage == i){
						sb2.append("<li class='active'><a href='#'>"+i+"</a></li>");
					}else{
						sb2.append("<li><a href='#' onclick='jumpPage("+i+")'>"+i+"</a></li>");
					}
					
				}
			}
		}
		//开始拼装html
		sb.append("<input type='hidden' id='tarPageCurIndex' value='"+curPage+"'>");
		sb.append("<ul class='pagination pull-right'>");
		if(curPage != 1){
			if(maxPage > 1){
				sb.append("<li><a href='#' onclick='jumpPage("+(curPage-1)+")'>&lt;</a></li>");//上一页
			}
			sb.append("<li><a href='#' onclick='jumpPage(1)'>1</a></li>");
		}else{
			sb.append("<li class='active'><a href='#'>1</a></li>");
		}
		if(maxPage > 2 && firstDot){
			sb.append("\n<li>...</li>\n");
		}
		sb.append(sb2.toString());//中间页码
		if(maxPage > 2 && lastDot){
			sb.append("\n<li>...</li>\n");
		}
		if(maxPage > 1){
			if(curPage != maxPage){
				sb.append("<li><a href='#' onclick='jumpPage("+maxPage+")'>"+maxPage+"</a></li>");
					sb.append("<li><a href='#' onclick='jumpPage("+(curPage+1)+")'>&gt;</a></li>");//下一页
			}else{
				sb.append("<li class='active'><a href='#'>"+maxPage+"</a></li>");
			}
		}
		
		sb.append("<li class='pagination-other'>跳到：");
		sb.append("<input type='text' class='form-control' id='tarPageNumId'>\n");
		sb.append("<a href='#' class='btn btn-white' onclick='jumpPage()'>确定</a>");
		sb.append("</li>");
		
		sb.append("<li class='pagination-other'>");
		sb.append("共"+maxRow+"条");
		sb.append("</li>");
		
		sb.append("<li class='pagination-other'>");
		sb.append("每页<select id='tarPageSizeId' class='form-control' onchange='jumpPage(1)' style='width: 70px'>");
		for (int pageVlaue : paginalValues) {
			sb.append("<option value='"+pageVlaue+"' ");
			if(pageSize == pageVlaue){
				sb.append(" selected='selected' ");
			}
			sb.append(">"+pageVlaue+"</option>");
		}
		sb.append("</select>条");
		sb.append("</li>");
		
		sb.append("</ul>");
		
		return sb;
	}

	private static StringBuffer loadContent(String url, Pagination page) {
		StringBuffer sb = new StringBuffer();
		sb.append("<script language='javascript'>\n");
		//跳转页码
		sb.append("function jumpPage(pp) {\n");
		sb.append("	var tarPageSize = document.getElementById('tarPageSizeId').value;\n");//每页多少行
		sb.append("	if(pp){\n");
		sb.append("		var tarPageNumId = pp;\n");//跳转到第几页
		sb.append("	}else{\n");
		sb.append("		var tarPageNumId = document.getElementById('tarPageNumId').value;\n");//跳转到第几页
		sb.append("		if(tarPageNumId == ''){\n");
		sb.append("			return;\n");
		sb.append("		}\n");
		sb.append("		if(isNaN(parseInt(tarPageNumId))){\n");
		sb.append("			alert('请输入正确的页码');\n");
		sb.append("			return;\n");
		sb.append("		}\n");
		sb.append("		tarPageNumId = parseInt(tarPageNumId);\n");//跳转到第几页
		sb.append("		if(tarPageNumId < 1 || tarPageNumId > ");
				sb.append(page.getMaxPageIndex() == 0 ? 1 : page
						.getMaxPageIndex());
				sb.append("){\n");
		sb.append("			alert('请输入正确的页码');\n");
		sb.append("			return;\n");
		sb.append("		}\n");
		sb.append("	}\n");
		sb.append("	$(reloadDataContainer).load('" + url);
		if (url.indexOf("?") == -1) {
			sb.append("?");
		} else {
			sb.append("&");
		}
		sb.append("_pageSize=' + tarPageSize + '&_pageIndex=' + tarPageNumId);\n");
		sb.append("}\n");
		//获取分页参数
		sb.append("function getPageParameter() {\n");
		sb.append("	var tarPageSize = document.getElementById('tarPageSizeId').value;\n");//每页多少行
		sb.append("	var tarPageNumId = "+page.getPageIndex()+";\n");//跳转到第几页
		sb.append("	return '"+"_pageSize=' + tarPageSize + '&_pageIndex=' + tarPageNumId"+";\n");
		sb.append("}\n");
		//刷新当前页
		sb.append("function refreshJumpPage() {\n");
		sb.append("	var tarPageSize = document.getElementById('tarPageSizeId').value;\n");//每页多少行
		sb.append("	var tarPageNumId = "+page.getPageIndex()+";\n");//跳转到第几页
		sb.append("	$(reloadDataContainer).load('" + url);
		if (url.indexOf("?") == -1) {
			sb.append("?");
		} else {
			sb.append("&");
		}
		sb.append("_pageSize=' + tarPageSize + '&_pageIndex=' + tarPageNumId);\n");
		sb.append("}\n");
		
		sb.append("</script>\n");
		return sb;
	}

}
