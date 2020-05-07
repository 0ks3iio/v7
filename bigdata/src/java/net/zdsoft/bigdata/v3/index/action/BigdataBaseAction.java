package net.zdsoft.bigdata.v3.index.action;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.config.ControllerException;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.UrlUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BigdataBaseAction extends BaseAction {

    protected static final Logger log = Logger
            .getLogger(BigdataBaseAction.class);

    @ResponseBody
    @ExceptionHandler(ControllerException.class)
    public String runtimeExceptionHandler(ControllerException runtimeException,
                                          WebRequest request) {
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        log.error("controller error ,code {" + code + "} , msg {"
                + runtimeException.getMessage() + "}", runtimeException);
        return loadErrorMsg("哎呀~您访问的页面开小差啦(code=" + code + ")").toString();
        // return Json.toJSONString(new
        // ResultDto().setSuccess(false).setCode(runtimeException.getCode())
        // .setMsg(runtimeException.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public String runtimeExceptionHandler2(Exception runtimeException,
                                           WebRequest request) {
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        log.error("controller error ,code {" + code + "} , msg {"
                + runtimeException.getMessage() + "}", runtimeException);
        return loadErrorMsg("哎呀~您访问的页面开小差啦(code=" + code + ")").toString();// Json.toJSONString(new
        // ResultDto().setSuccess(false).setCode("-1").setMsg(runtimeException.getMessage()));
    }

    private StringBuffer loadErrorMsg(String errorMsg) {
        StringBuffer sb = new StringBuffer();
        sb.append("<div class='no-data-404'>\n");
        sb.append("<img id='errorImageId' src=''/>\n");
        sb.append("<div class='ml-30'>\n");
        sb.append("<div class='word-404' style='font-size: 22px;'>\n");
        if (StringUtils.isNotBlank(errorMsg))
            sb.append(errorMsg);
        else
            sb.append("哎呀~您访问的页面开小差啦\n");
        sb.append("</div>\n");
        sb.append("</div>\n");
        sb.append("</div>\n");

        sb.append("<script language='javascript'>\n");
        sb.append("		$(document).ready(function(){\n");

        sb.append("			var imageUrl=" + getRequest().getContextPath()
                + "'/bigdata/v3/static/images/public/no-data-face.png'\n");
        sb.append("			$('#errorImageId').attr('src',imageUrl);\n");
        sb.append("		});\n");
        sb.append("	</script>\n");
        return sb;
    }

    protected void sendPagination(HttpServletRequest request, ModelMap map, String style,
                                  Pagination page) {
        map.put("pageContent", page);
        String htmlOfPaginationLoad = "";
        Map<String, String[]> mop = request.getParameterMap();
        Map<String, String> mapOfParameter = new HashMap<String, String>();
        for (String key : mop.keySet()) {
            mapOfParameter.put(key, mop.get(key)[0]);
        }
        Map<String, String> mapOfParameterOpe = new HashMap<String, String>();
        mapOfParameterOpe.putAll(mapOfParameter);
        if (mapOfParameterOpe.containsKey("_pageIndex")) {
            mapOfParameterOpe.remove("_pageIndex");
        }
        if (mapOfParameterOpe.containsKey("_pageSize")) {
            mapOfParameterOpe.remove("_pageSize");
        }
        String[] keys = mapOfParameterOpe.keySet().toArray(new String[0]);
        Object[] values = new Object[0];
        if (keys != null) {
            values = new Object[keys.length];
            for (int i = 0; i < keys.length; i++) {
                values[i] = mapOfParameterOpe.get(keys[i]);
            }
        }
        String actionName = request.getRequestURI();
        String url = UrlUtils.addQueryString(actionName, keys, values);
        htmlOfPaginationLoad = paginationLoad(url, style, page);
        map.put("htmlOfPaginationLoad", htmlOfPaginationLoad);
    }

    private String paginationLoad(String url, String style, Pagination page) {
        return createPagination(url, page).append(loadContent(url, style, page))
                .toString();
    }

    private StringBuffer createPagination(String url, Pagination page) {

        int maxPage = page.getMaxPageIndex();// 最大页数
        int maxRow = page.getMaxRowCount();// 一共多少行
        int curPage = page.getPageIndex();// 当前页
        int pageSize = page.getPageSize();// 每页多少行
        // 分页栏当前页前后延续的长度
        int m = 2;
        int[] paginalValues = new int[]{10, 20, 50, 100, 150, 200, 500, 1000};
        StringBuffer sb = new StringBuffer();
        // 计算中间页码，除去第一页和最后一页
        boolean firstDot = true;// 前面是否需要省略...
        boolean lastDot = true;// 后面是否需要省略...
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
                    if (curPage == i) {
                        sb2.append("<li class='active'><a href='javascript:void(0);'>"
                                + i + "</a></li>");
                    } else {
                        sb2.append("<li><a href='javascript:void(0);' onclick='jumpPage("
                                + i + ")'>" + i + "</a></li>");
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
                    if (curPage == i) {
                        sb2.append("<li class='active'><a href='javascript:void(0);'>"
                                + i + "</a></li>");
                    } else {
                        sb2.append("<li><a href='javascript:void(0);' onclick='jumpPage("
                                + i + ")'>" + i + "</a></li>");
                    }

                }
            }
        }
        // 开始拼装html
        sb.append("<input type='hidden' id='tarPageCurIndex' value='" + curPage
                + "'>");
        sb.append("<ul class='pagination-list'>");
        if (curPage != 1) {
            if (maxPage > 1) {
                sb.append("<li><a href='javascript:void(0);' onclick='jumpPage("
                        + (curPage - 1) + ")'>&lt;</a></li>");// 上一页
            }
            sb.append("<li><a href='javascript:void(0);' onclick='jumpPage(1)'>1</a></li>");
        } else {
            sb.append("<li class='active'><a href='javascript:void(0);'>1</a></li>");
        }
        if (maxPage > 2 && firstDot) {
            sb.append("\n<li>...</li>\n");
        }
        sb.append(sb2.toString());// 中间页码
        if (maxPage > 2 && lastDot) {
            sb.append("\n<li>...</li>\n");
        }
        if (maxPage > 1) {
            if (curPage != maxPage) {
                sb.append("<li><a href='javascript:void(0);' onclick='jumpPage("
                        + maxPage + ")'>" + maxPage + "</a></li>");
                sb.append("<li><a href='javascript:void(0);' onclick='jumpPage("
                        + (curPage + 1) + ")'>&gt;</a></li>");// 下一页
            } else {
                sb.append("<li class='active'><a href='javascript:void(0);'>"
                        + maxPage + "</a></li>");
            }
        }
        sb.append(" </ul>");

        sb.append("  <div class='form-group ml-10'>");
        sb.append(" <span class='label'>跳到：</span>");
        sb.append(" <input type='text' class='form-control form-paging' id='tarPageNumId'/>");
        sb.append("  <button class='btn btn-default' onclick='jumpPage();return false;'>确定</button>");
        sb.append(" </div>");
        sb.append(" <div class='form-group ml-10'>");
        sb.append(" <span class='label'>共" + maxRow + "条</span>");
        sb.append(" <span class='label ml-10'>每页</span>");
        sb.append("<select id='tarPageSizeId' class='form-control' onchange='jumpPage(1)'>");

        for (int pageVlaue : paginalValues) {
            sb.append("<option value='" + pageVlaue + "' ");
            if (pageSize == pageVlaue) {
                sb.append(" selected='selected' ");
            }
            sb.append(">" + pageVlaue + "</option>");
        }
        sb.append(" </select>");
        sb.append(" <span class='label'>条</span>");
        sb.append(" </div>");
        return sb;
    }

    private StringBuffer loadContent(String url, String style, Pagination page) {
        StringBuffer sb = new StringBuffer();
        sb.append("<script language='javascript'>\n");
        // 跳转页码
        sb.append("function jumpPage(pp) {\n");
        sb.append("	var tarPageSize = document.getElementById('tarPageSizeId').value;\n");// 每页多少行
        sb.append("	if(pp){\n");
        sb.append("		var tarPageNumId = pp;\n");// 跳转到第几页
        sb.append("	}else{\n");
        sb.append("		var tarPageNumId = document.getElementById('tarPageNumId').value;\n");// 跳转到第几页
        sb.append("		if(tarPageNumId == ''){\n");
        sb.append("			return;\n");
        sb.append("		}\n");
        sb.append("		if(isNaN(parseInt(tarPageNumId))){\n");
        if ("2".equals(style))
            sb.append("			showBIErrorTips('提示','请输入正确的页码','390px','auto');\n");
        else
            sb.append("			showLayerTips('warn','请输入正确的页码','t');\n");
        sb.append("			return;\n");
        sb.append("		}\n");
        sb.append("		tarPageNumId = parseInt(tarPageNumId);\n");// 跳转到第几页
        sb.append("		if(tarPageNumId < 1 || tarPageNumId > ");
        sb.append(page.getMaxPageIndex() == 0 ? 1 : page.getMaxPageIndex());
        sb.append("){\n");
        if ("2".equals(style))
            sb.append("			showBIErrorTips('提示','请输入正确的页码','390px','auto');\n");
        else
            sb.append("			showLayerTips('warn','请输入正确的页码','t');\n");
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
        // 获取分页参数
        sb.append("function getPageParameter() {\n");
        sb.append("	var tarPageSize = document.getElementById('tarPageSizeId').value;\n");// 每页多少行
        sb.append("	var tarPageNumId = " + page.getPageIndex() + ";\n");// 跳转到第几页
        sb.append("	return '"
                + "_pageSize=' + tarPageSize + '&_pageIndex=' + tarPageNumId"
                + ";\n");
        sb.append("}\n");
        // 刷新当前页
        sb.append("function refreshJumpPage() {\n");
        sb.append("	var tarPageSize = document.getElementById('tarPageSizeId').value;\n");// 每页多少行
        sb.append("	var tarPageNumId = " + page.getPageIndex() + ";\n");// 跳转到第几页
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
