package net.zdsoft.framework.xss;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * @author shenke
 * @since 2019/4/29 上午10:54
 */
public class XssHttpServletRequest extends HttpServletRequestWrapper {
	private  Set<String> notEscapeParam;

    public XssHttpServletRequest(HttpServletRequest request,String[] notEscapeParam) {
        super(request);
        this.notEscapeParam = new HashSet<>(Arrays.asList(notEscapeParam));
    }

    @Override
    public String getHeader(String name) {
        return StringEscapeUtils.escapeHtml4(super.getHeader(name));
    }

    @Override
    public String getQueryString() {
        return StringEscapeUtils.escapeHtml4(super.getQueryString());
    }

    @Override
    public String getParameter(String name) {
        return StringEscapeUtils.escapeHtml4(super.getParameter(name));
    }

    @Override
    public String[] getParameterValues(String parameter) {
    	if(notEscapeParam.contains(parameter)){//不想过滤的参数，此处content参数是 富文本内容
            return super.getParameterValues(parameter);
        }
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        return executeArray(values);
    }

    private String[] executeArray(String[] values) {
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = StringEscapeUtils.escapeHtml4(values[i]);
        }
        return encodedValues;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = super.getParameterMap();
        if (map == null || map.isEmpty()) {
            return map;
        }
        Map<String, String[]> newMap = new HashMap<>(map.size());
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            String[] values = entry.getValue();
            if (values == null) {
                newMap.put(entry.getKey(), entry.getValue());
            }
            else {
                newMap.put(entry.getKey(), executeArray(entry.getValue()));
            }
        }
        return newMap;
    }
}
