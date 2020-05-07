package net.zdsoft.bigdata.data.utils;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.export.*;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Jasperreports 报表导出工具
 */
public class JasperExportUtils {

    private static final String ENCODING = "utf-8";
    private static final String PDF_CONTENT_TYPE = "application/octet-stream";
    private static final String XLS_CONTENT_TYPE = "application/x-msdownload";

    /**
     * 返回html页面
     *
     * @param jasperPath 模版路径
     * @param datasource 数据源
     * @param request
     * @param response
     * @throws JRException
     * @throws IOException
     */
    public static void exportToHtml(String jasperPath, JRDataSource datasource, HttpServletRequest request, HttpServletResponse response) throws JRException, IOException {
        exportToHtml(jasperPath, new HashMap<String, Object>(), datasource, request, response);
    }

    /**
     * 返回html页面
     *
     * @param jasperPath 模版路径
     * @param params     参数
     * @param datasource 数据源
     * @param request
     * @param response
     * @throws JRException
     * @throws IOException
     */
    public static void exportToHtml(String jasperPath, Map<String, Object> params, JRDataSource datasource, HttpServletRequest request, HttpServletResponse response) throws JRException, IOException {
        //指定模板文件
        JasperPrint jasperPrint = fillReport(jasperPath, params, datasource);
        request.setCharacterEncoding(ENCODING);
        response.setCharacterEncoding(ENCODING);
        HtmlExporter exporter = new HtmlExporter();
        // html样式
        exporter.setConfiguration(simpleHtmlExporterConfiguration());
        // 报表样式
        exporter.setConfiguration(simpleHtmlReportConfiguration());
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

        exporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getWriter()));
        // 图表输出
        HtmlResourceHandler imageHandler = new CustomerHtmlResourceHandler();
        exporter.setImageHandler(imageHandler);
        exporter.exportReport();
    }

    public static BufferedImage exportToPhoto(String jasperPath, Map<String, Object> params, JRDataSource datasource, HttpServletRequest request, HttpServletResponse response) throws JRException, IOException {
        //指定模板文件
        JasperPrint jasperPrint = fillReport(jasperPath, params, datasource);
        request.setCharacterEncoding(ENCODING);
        response.setCharacterEncoding(ENCODING);
        JRGraphics2DExporter exporter = new JRGraphics2DExporter();//创建graphics输出器
        //创建一个影像对象
        BufferedImage bufferedImage = new BufferedImage(jasperPrint.getPageWidth() * 4, jasperPrint.getPageHeight() * 4, BufferedImage.TYPE_INT_RGB);//取graphics
        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();//设置相应参数信息
        exporter.setParameter(JRGraphics2DExporterParameter.GRAPHICS_2D, g);
        exporter.setParameter(JRGraphics2DExporterParameter.ZOOM_RATIO, Float.valueOf(4));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.exportReport();
        g.dispose();//释放资源信息//这里的bufferedImage就是最终的影像图像信息,可以通过这个对象导入到cm中了.
        return bufferedImage;
    }

    /**
     * 导出pdf
     *
     * @param jasperPath 模版路径
     * @param fileName   文件名
     * @param params
     * @param datasource
     * @param response
     * @throws IOException
     * @throws JRException
     */
    public static void exportToPdf(String jasperPath, String fileName, Map<String, Object> params, JRDataSource datasource, HttpServletResponse response) throws IOException, JRException {
        //指定模板文件
        JasperPrint jasperPrint = fillReport(jasperPath, params, datasource);
        response.setCharacterEncoding(ENCODING);
        response.setContentType(PDF_CONTENT_TYPE);
        response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO-8859-1") + ".pdf");
        exportReport(new JRPdfExporter(), jasperPrint, response);
    }

    /**
     * 导出excel
     *
     * @param jasperPath 模版路径
     * @param fileName   文件名
     * @param params
     * @param datasource
     * @param response
     * @throws IOException
     * @throws JRException
     */
    public static void exportToXls(String jasperPath, String fileName, Map<String, Object> params, JRDataSource datasource, HttpServletResponse response) throws IOException, JRException {
        //指定模板文件
        JasperPrint jasperPrint = fillReport(jasperPath, params, datasource);
        response.setCharacterEncoding(ENCODING);
        response.setContentType(XLS_CONTENT_TYPE);
        response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO-8859-1") + ".xls");
        exportReport(new JRXlsExporter(), jasperPrint, response);
    }

    private static void exportReport(JRAbstractExporter exporter, JasperPrint jasperPrint, HttpServletResponse response) throws JRException, IOException {
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
        exporter.exportReport();
    }

    private static JasperPrint fillReport(String jasperPath, Map<String, Object> params, JRDataSource datasource) throws JRException, IOException {
        //指定模板文件
        URL url = new URL(jasperPath);
        return JasperFillManager.fillReport(url.openStream(), params, datasource);
    }

    /**
     * html样式设置
     * @return
     */
    private static SimpleHtmlExporterConfiguration simpleHtmlExporterConfiguration() {
        // 页面样式设置
        SimpleHtmlExporterConfiguration config = new SimpleHtmlExporterConfiguration();
        // 头尾去除
//        config.setHtmlHeader(StringUtils.EMPTY);
        config.setBetweenPagesHtml(StringUtils.EMPTY);
//        config.setHtmlFooter(StringUtils.EMPTY);
        config.setFlushOutput(Boolean.FALSE);
        config.setOverrideHints(Boolean.FALSE);
        return config;
    }

    /**
     * 报表样式设置
     * @return
     */
    private static SimpleHtmlReportConfiguration simpleHtmlReportConfiguration() {
        // 报表样式设置
        SimpleHtmlReportConfiguration configuration = new SimpleHtmlReportConfiguration();
        // 设置没有背景
        configuration.setWhitePageBackground(Boolean.FALSE);
        configuration.setRemoveEmptySpaceBetweenRows(Boolean.TRUE);
        return configuration;
    }
}
