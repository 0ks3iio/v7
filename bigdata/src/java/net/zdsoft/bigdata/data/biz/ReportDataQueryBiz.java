package net.zdsoft.bigdata.data.biz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.entity.Api;
import net.zdsoft.bigdata.data.entity.ReportTemplate;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.manager.IReportQueryInvocation;
import net.zdsoft.bigdata.data.manager.IResult;
import net.zdsoft.bigdata.data.manager.InvocationBuilder;
import net.zdsoft.bigdata.data.manager.api.Invoker;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.data.service.ApiService;
import net.zdsoft.bigdata.data.service.ReportTemplateService;
import net.zdsoft.bigdata.data.utils.JasperExportUtils;
import net.zdsoft.bigdata.data.utils.JexlContextHolder;
import net.zdsoft.bigdata.data.vo.ReportVO;
import net.zdsoft.framework.utils.ServletUtils;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangdongdong on 2018/5/8.
 */
@Service
public class ReportDataQueryBiz {

    private static final Logger logger = LoggerFactory.getLogger(ReportDataQueryBiz.class);
    @Resource
    private Invoker invoker;
    @Resource
    private ReportTemplateService reportTemplateService;
    @Autowired
    private SysOptionRemoteService sysOptionRemoteService;
    @Resource
    private ApiService apiService;

    /**
     * 返回html页面
     *
     * @param request
     * @param response
     * @throws JRException
     * @throws IOException
     */
    public void showHtml(ReportVO reportVO, String jasperPath, HttpServletRequest request, HttpServletResponse response) throws JRException, IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, BigDataBusinessException {
        Result result = queryResult(request.getSession(), reportVO);
        if (result.hasError()) {
            logger.error("查询异常:" + result.getException().getMessage(), result.getException());
            throw new JRException("查询异常:" + result.getException().getMessage());
        }
        ResultSet rs = null;
        try {
            Object value = result.getValue();
            if (value instanceof ResultSet) {
                rs = (ResultSet) value;
                rs.last();
                if (rs.getRow() == 0) {
                    logger.info("查询无数据");
                    throw new BigDataBusinessException("查询无数据");
                }
                rs.beforeFirst();
                JRDataSource datasource = new JRResultSetDataSource(rs);
                JasperExportUtils.exportToHtml(jasperPath, datasource, request, response);
            } else if (value instanceof String) {
                JSONObject jsonObject = JSON.parseObject(String.valueOf(value));
                if (jsonObject.size() == 0) {
                    throw new JRException("请确认参数是否正确");
                }
                String data = jsonObject.getString("infolist");
                Map map = jsonObject.getJSONObject("params");
                if (data == null || data.equals("[]")) {
                    throw new BigDataBusinessException("查询无数据");
                }
                ByteArrayInputStream is = new ByteArrayInputStream(data.getBytes("utf-8"));
                JasperExportUtils.exportToHtml(jasperPath, map == null ? new HashMap<>() : map, new JsonDataSource(is), request, response);
            } else {
                logger.error("返回数据格式不正确");
                throw new JRException("返回数据格式不正确");
            }
        } catch (SQLException e) {
            throw new JRException(e.getMessage());
        } finally {
            closeResultSet(rs);
        }
    }

    /**
     * 导出pdf文件
     * @param request TODO
     * @param response
     *
     * @throws JRException
     * @throws IOException
     */
    public void exportToPdf(ReportVO reportVO, String jasperPath, HttpServletRequest request, HttpServletResponse response) throws JRException, IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Result result = queryResult(request.getSession(), reportVO);
        if (result.hasError()) {
            logger.error("sql查询异常:" + result.getException().getMessage(), result.getException());
            throw new JRException("sql查询异常:" + result.getException().getMessage());
        }
        ResultSet rs = null;
        try {
            Object value = result.getValue();
            if (value instanceof ResultSet) {
                rs = (ResultSet) value;
                JRDataSource datasource = new JRResultSetDataSource(rs);
                JasperExportUtils.exportToPdf(jasperPath, reportVO.getName(), new HashMap<>(), datasource, response);
            } else if (value instanceof String) {
                JSONObject jsonObject = JSON.parseObject(String.valueOf(value));
                if (jsonObject.size() == 0) {
                    throw new JRException("请确认参数是否正确");
                }
                String data = jsonObject.getString("infolist");
                if (data == null) {
                    data = JSON.toJSONString(Lists.newArrayList());
                }
                Map map = jsonObject.getJSONObject("params");
                ByteArrayInputStream is = new ByteArrayInputStream(data.getBytes("utf-8"));
                JasperExportUtils.exportToPdf(jasperPath, reportVO.getName(), map == null ? new HashMap<>() : map, new JsonDataSource(is), response);
            } else {
                logger.error("查询结果数据格式不正确");
                throw new JRException("查询结果数据格式不正确");
            }
        } finally {
            closeResultSet(rs);
        }
    }

    /**
     * 导出excel文件
     * @param request TODO
     * @param response
     *
     * @throws JRException
     * @throws IOException
     */
    public void exportToExcel(ReportVO reportVO, String jasperPath, HttpServletRequest request, HttpServletResponse response) throws JRException, IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Result result = queryResult(request.getSession(), reportVO);
        if (result.hasError()) {
            logger.error("查询异常:" + result.getException().getMessage(), result.getException());
            throw new JRException("查询异常:" + result.getException().getMessage());
        }
        ResultSet rs = null;
        try {
            Object value = result.getValue();
            if (value instanceof ResultSet) {
                rs = (ResultSet) value;
                JRDataSource datasource = new JRResultSetDataSource(rs);
                JasperExportUtils.exportToXls(jasperPath, reportVO.getName(), new HashMap<>(), datasource, response);
            } else if (value instanceof String) {
                JSONObject jsonObject = JSON.parseObject(String.valueOf(value));
                if (jsonObject.size() == 0) {
                    throw new JRException("请确认参数是否正确");
                }
                String data = jsonObject.getString("infolist");
                if (data == null) {
                    data = JSON.toJSONString(Lists.newArrayList());
                }
                Map map = jsonObject.getJSONObject("params");
                ByteArrayInputStream is = new ByteArrayInputStream(data.getBytes("utf-8"));
                JasperExportUtils.exportToXls(jasperPath, reportVO.getName(), map == null ? new HashMap<>() : map, new JsonDataSource(is), response);
            } else {
                logger.error("查询结果数据格式不正确");
                throw new JRException("查询结果数据格式不正确");
            }
        } finally {
            closeResultSet(rs);
        }
    }

    /**
     * 输出图片
     * @param reportVO
     * @param jasperPath
     * @param request
     * @param response
     * @return
     * @throws JRException
     * @throws IOException
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public BufferedImage exportPhoto(ReportVO reportVO, String jasperPath, HttpServletRequest request, HttpServletResponse response) throws JRException, IOException, SQLException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Result result = queryResult(request.getSession(), reportVO);
        if (result.hasError()) {
            logger.error("报表输出异常:" + result.getException().getMessage(), result.getException());
            throw new JRException("报表输出异常:" + result.getException().getMessage());
        }
        ResultSet rs = null;
        try {
            Object value = result.getValue();
            if (value instanceof ResultSet) {
                rs = (ResultSet) value;
                JRDataSource datasource = new JRResultSetDataSource(rs);
                return JasperExportUtils.exportToPhoto(jasperPath, new HashMap<>(), datasource, request, response);
            } else if (value instanceof String) {
                ByteArrayInputStream is = new ByteArrayInputStream(String.valueOf(value).getBytes("utf-8"));
                return JasperExportUtils.exportToPhoto(jasperPath, new HashMap<>(), new JsonDataSource(is), request, response);
            } else {
                logger.error("查询结果数据格式不正确");
                throw new JRException("查询结果数据格式不正确");
            }
        } finally {
            closeResultSet(rs);
        }
    }

    /**
     * 查询
     * @param session TODO
     * @param reportVO
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    private Result queryResult(HttpSession session, ReportVO reportVO) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map map = BeanUtils.describe(ServletUtils.getLoginInfo(session));
        map.putAll(reportVO.getParamMap());
        JexlContextHolder.setJexlContext(map);
        try {
            DataSourceType dataSourceType = DataSourceType.parse(reportVO.getDataSourceType());
            switch (dataSourceType) {
                case DB:
                    return invoker.invoke(IReportQueryInvocation.getInstance(dataSourceType,
                            reportVO.getDataSet(),
                            reportVO.getDataSourceId(),
                            reportVO.getUpdateInterval(),
                            0, -1));
                case API:
                    Api api = apiService.findOne(reportVO.getDataSourceId());
                    return invoker.invoke(
                            InvocationBuilder.getInstance().dataSourceId(reportVO.getDataSourceId())
                                    .updateInterval(reportVO.getUpdateInterval())
                                    .queryStatement(api.getUrl())
                                    .type(DataSourceType.API).build());
                case STATIC:
                    return new IResult(reportVO.getDataSet(), null);
                default:
                    throw new RuntimeException("不支持的数据源类型");
            }
        } finally {
            JexlContextHolder.clearJexlContext();
        }
    }

    public String getJasperPath(ReportVO reportVO, ReportTemplate template, HttpServletRequest request) {
        if (StringUtils.isNotBlank(reportVO.getTemplateId())) {
            ReportTemplate reportTemplate = reportTemplateService.findOne(reportVO.getTemplateId());
            return sysOptionRemoteService.getFileUrl(request.getServerName()) + "/" + reportTemplate.getTemplatePath();
        }

        String templateTempPath = template.getTemplatePath();
        if (!templateTempPath.contains(template.getTemplateFileName())) {
            templateTempPath = templateTempPath + File.separator + template.getTemplateFileName();
        }
        if (templateTempPath.contains("upload")) {
            templateTempPath = templateTempPath.substring(templateTempPath.indexOf("upload"));
        }
        return sysOptionRemoteService.getFileUrl(request.getServerName()) + "/" + templateTempPath;
    }

    /**
     * 关闭ResultSet
     * @param rs
     * @throws JRException
     */
    private void closeResultSet(ResultSet rs) throws JRException {
        if (rs != null) {
            try {
                Statement statement = rs.getStatement();
                Connection connection = statement.getConnection();
                // 关闭连接
                rs.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                logger.error("result 关闭异常", e);
                throw new JRException("result 关闭异常");
            }
        }
    }

    /**
     * 处理JSON单引号
     * @param ors
     * @return
     */
    public String toJSONString(String ors) {
        ors= ors == null ? "" : ors;
        StringBuffer buffer = new StringBuffer(ors);
        int i = 0;
        while(i < buffer.length()) {
            if(buffer.charAt(i) == '\'') {
                buffer.insert(i,'\\');
                i+= 2;
            }else {
                i++;
            }
        }
        return buffer.toString();
    }
}
