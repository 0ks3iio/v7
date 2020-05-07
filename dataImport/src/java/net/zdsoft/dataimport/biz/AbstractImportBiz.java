package net.zdsoft.dataimport.biz;

import static net.zdsoft.dataimport.biz.BeanUtils.getAnnotationWithAnnotation;
import static net.zdsoft.dataimport.biz.BeanUtils.getFiledValue;
import static net.zdsoft.dataimport.biz.BeanUtils.getFirstGenericityType;
import static net.zdsoft.dataimport.biz.BeanUtils.getFirstSuperInterfaceGenericityType;
import static net.zdsoft.dataimport.biz.BeanUtils.isNumer;
import static net.zdsoft.dataimport.biz.BeanUtils.newBean;
import static net.zdsoft.dataimport.biz.BeanUtils.setProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import net.zdsoft.dataimport.annotation.ExcelCell;
import net.zdsoft.dataimport.annotation.Exporter;
import net.zdsoft.dataimport.annotation.Valid;
import net.zdsoft.dataimport.core.DataCell;
import net.zdsoft.dataimport.core.DataExcel;
import net.zdsoft.dataimport.core.DataRow;
import net.zdsoft.dataimport.core.DataSheet;
import net.zdsoft.dataimport.ex.AnnotationVerify;
import net.zdsoft.dataimport.ex.ValueTransfer;
import net.zdsoft.dataimport.exception.ImportBusinessException;
import net.zdsoft.dataimport.exception.ImportParseException;
import net.zdsoft.dataimport.parse.ExcelParserFactory;
import net.zdsoft.dataimport.parse.Parser;
import net.zdsoft.dataimport.process.ExcutorHolder;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author shenke
 * @since 2017.07.31
 */
public abstract class AbstractImportBiz<T extends QImportEntity>  implements InitializingBean{

    protected static Logger logger = LoggerFactory.getLogger(AbstractImportBiz.class);

    private Map<String,Field> classCache;

    @Autowired private ExcutorHolder excutorHolder;
    @Autowired private ApplicationContext applicationContext;
    @Autowired protected McodeRemoteService mcodeRemoteService;

    private List<AnnotationVerify> annotationVerifies = Lists.newArrayList();
    private List<ValueTransfer> valueTransfers = Lists.newArrayList();

    private AnnotationVerify<Valid> validAnnotationVerify;
    private ValueTransfer valueTransfer;

    @Override
    public void afterPropertiesSet() throws Exception {
        initAnnotationCache();
        initAnnotationVerifyInterceptors();
    }

    protected void setValidAnnotationVerify(AnnotationVerify<Valid> validAnnotationVerify) {
        this.validAnnotationVerify = validAnnotationVerify;
    }

    protected void setValueTransfer(ValueTransfer valueTransfer) {
        this.valueTransfer = valueTransfer;
    }

    private void initAnnotationCache() {
        Class<T> tClass = getFirstGenericityType(this.getClass());
        List<Field> fields = getAnnotationWithAnnotation(tClass, ExcelCell.class);
        classCache = Maps.newHashMap();
        for (Field field : fields) {
            classCache.put(field.getAnnotation(ExcelCell.class).header(), field);
        }
        //classCache = fields.stream().collect(Collectors.toMap(field -> field.getAnnotation(ExcelCell.class).header(), field->field));
    }

    private void initAnnotationVerifyInterceptors() {
        Map<String, AnnotationVerify> annotationVerifyInterceptorMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext,AnnotationVerify.class, Boolean.TRUE, Boolean.TRUE);
        if ( annotationVerifyInterceptorMap != null && annotationVerifyInterceptorMap.size() > 0 ) {
            annotationVerifies.addAll(annotationVerifyInterceptorMap.values());
        }
        //默认校验
        if ( this.validAnnotationVerify == null ) {
            this.validAnnotationVerify = new ValidAnnotationVerify();
        }
        annotationVerifies.add(this.validAnnotationVerify);
        annotationVerifies.sort(Comparator.comparingInt(AnnotationVerify::order));
        //annotationVerifies.sort(Comparator.comparingInt(AnnotationVerify::order));

        //转换接口
        Map<String, ValueTransfer> valueTransferMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext,ValueTransfer.class, Boolean.TRUE, Boolean.TRUE);
        if ( valueTransferMap != null && valueTransferMap.size() > 0 ) {
            valueTransfers.addAll(valueTransferMap.values());
        }
        if ( valueTransfer == null ) {
            this.valueTransfer = new DefaultTransfer();
        }
        valueTransfers.add(this.valueTransfer);
        //valueTransfers.sort(Comparator.comparing(ValueTransfer::order));
        valueTransfers.sort(Comparator.comparingInt(ValueTransfer::order));
    }

    private void initDadaCell(List<DataCell> dataCells) {
        for (DataCell e : dataCells) {
            e.setFieldName(classCache.get(e.getHeader()).getName());
        }
    }

    Reply<T> execute(String filePath, String id) {
        final Reply<T> reply = new Reply<T>();
        reply.setId(id);
        excutorHolder.run(new Callable<Reply<T>>() {
            @Override
            public Reply<T> call() throws Exception {
                reply.setImportState(ImportState.ING);
                Parser parser = ExcelParserFactory.getParser();
                if ( filePath == null ) {
                    reply.setGlobeError("文件路径不合法");
                    logger.error("filePath 不能为空");
                    return reply;
                }
                File excel = new File(filePath);
                if ( !excel.exists() ) {
                    logger.error("文件不存在 {}", filePath);
                    reply.setGlobeError("excel不存在");
                    return reply;
                }
                long parseStart = System.currentTimeMillis();
                try {
                    DataExcel dataExcel = parser.parse(new FileInputStream(excel), reply, header -> {
                        for (String s : header) {
                            if ( classCache.get(s) == null){
                                return Boolean.FALSE;
                            }
                        }
                        return Boolean.TRUE;
                    });
                    List<DataSheet> dataSheetList = dataExcel.getDataSheetList();
                    if ( dataSheetList.isEmpty() ) {
                        logger.error("导入Excle中无数据");
                        reply.setGlobeError("导入Excel不包含数据");
                        return reply;
                    }
                    List<T> objects = new ArrayList<>();
                    List<T> correctObjects = new ArrayList<>();
                    List<T> errorObjects = new ArrayList<>();
                    for (DataSheet dataSheet : dataSheetList) {
                        reply.notifyClient(ImportState.ING.getCode(), "正在进行数据转换和校验");
                        reply.setHeaders(Lists.newArrayList(dataSheet.getHeaders()));
                        if ( reply.getHeaders().size() > classCache.size() ) {
                            reply.setGlobeError("模板错误");
                            return reply;
                        }
                        objects.addAll(transferTo(dataSheet));
                    }
                    for (T e : objects) {
                        if ( e.createQImportError().isHasError() ) {
                            errorObjects.add(e);
                        } else {
                            correctObjects.add(e);
                        }
                    }

                    reply.setErrorObjects(errorObjects);
                    reply.setJavaObjects(correctObjects);
                    logger.debug("解析转换耗时：{}ms", (System.currentTimeMillis() - parseStart));

                } catch (FileNotFoundException e) {
                    logger.error("FileNotFound, {}, {}", e.getMessage(), filePath);
                    reply.setGlobeError("excel不存在");
                } catch (ImportParseException e) {
                    logger.error("文件解析异常 {}", e.getMessage());
                    reply.setGlobeError(e.getParseErrorMessage());
                } catch (Exception e){
                    reply.setGlobeError(e.getMessage());
                }finally {
                    if ( reply.getGlobeError() == null ) {
                        reply.setImportState(ImportState.DONE);
                        reply.notifyClient(ImportState.DONE.getCode(), "数据解析校验完成");
                    }
                }
                //更新生成时间，延长存活时间
                reply.setCreationTime(System.currentTimeMillis());
                return reply;
            }
        });
        return reply;
    }

    /**
     * 转换为java对象，所有入库之前校验在此完成，记录错误信息页面展示</br>
     * @see QImportEntity#createQImportError()
     * @see QImportError
     *
     */
    public List<T> transferTo(DataSheet dataSheet) {
        List<T> os = new ArrayList<T>(dataSheet.getDataRowList().size());
        for (DataRow dataRow : dataSheet.getDataRowList()) {
            initDadaCell(dataRow.getDataCellList());
            T t = newBean(getFirstGenericityType(this.getClass()));
            QImportError qImportError = t.createQImportError();
            for (DataCell dataCell : dataRow.getDataCellList()) {
                Object value = null;
                for (ValueTransfer transfer : valueTransfers) {
                    if ( (value = transfer.transfer(dataCell.getData(), classCache.get(dataCell.getHeader())) ) != null ){
                        break;
                    }
                }
                if (!setProperty(t, dataCell.getFieldName(), value) ){
                    qImportError.error(dataCell.getFieldName(), "赋值转换失败");
                }
                qImportError.value(dataCell.getFieldName(), dataCell.getData());
                checkForValid(t,dataCell.getHeader());
            }
            os.add(t);
        }
        return os;
    }

    /**
     * 注解校验
     */
    void checkForValid(T t, String header) {
        Field field = classCache.get(header);
        Object value = getFiledValue(t, field);
        //扩展注解校验 调用注解扩展
        for (AnnotationVerify e : annotationVerifies) {
            boolean ok = e.verify(value, getAnnotationByHeader(header, getFirstSuperInterfaceGenericityType(e.getClass())), t.createQImportError(), field );
            if ( ok ) {
                break;
            }
        }
    }

    boolean importConfirm(Reply reply) {
        List<T> os = reply.getJavaObjects();
        try {
            if ( globeVerify(os) ){
                importData(os);
                return Boolean.TRUE;
            } else{
                return Boolean.FALSE;
            }
        } catch (ImportBusinessException e) {
            T t = os.get(e.getIndex());
            os.remove(t);
            t.createQImportError().error(e.getField(), e.getMessage());
            reply.setErrorObjects(new ArrayList(){{add(t);}});
            return Boolean.FALSE;
        } catch (Exception e) {
            reply.setGlobeError(e.getMessage());
            return Boolean.FALSE;
        }
    }

    /**
     * 导出模板
     * @return
     */
    Workbook exportTemplate(List<String> headers) {
        Class<T> tClass = getFirstGenericityType(this.getClass());
        List<Field> fields = Lists.newArrayList();
        for (Field e : tClass.getDeclaredFields()) {
            if ( e.getAnnotation(Exporter.class) != null ) {
                if ( headers == null || headers.contains(e.getAnnotation(ExcelCell.class).header()) ) {
                    fields.add(e);
                }
            }
        }
        //fields.sort(Comparator.comparingInt(e1 -> e1.getAnnotation(Exporter.class).displayOrder()));
        Collections.sort(fields, new Comparator<Field>() {
            @Override
            public int compare(Field o1, Field o2) {
                return o1.getAnnotation(Exporter.class).displayOrder() - o2.getAnnotation(Exporter.class).displayOrder();
            }
        });

        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        workbook.setSheetName(0, "test");
        Row row = sheet.createRow(0);
        //CellRangeAddress rangeAddress = new CellRangeAddress(0, 0, 0 , exporters.size() - 1);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        Font font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints( (short) 10);
        font.setBold(true);

        cellStyle.setFont(font);

        for (Field e : fields) {
            Cell cell = row.createCell(fields.indexOf(e));
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(e.getAnnotation(ExcelCell.class).header());

            //下拉选择
            Exporter exporter = e.getAnnotation(Exporter.class);
            setSelect(exporter, sheet, fields.indexOf(e));
            //setCellJavaType(exporter, sheet, e);

            cell.setCellStyle(cellStyle);
        }
        return workbook;
    }

    /**
     * 该方法的最大不足之处就是数据不能超过128 个
     * @param exporter
     * @param sheet
     * @param col
     */
    private void setSelect(Exporter exporter, Sheet sheet, int col) {
        String[] selectItems = null;
        if ( exporter != null && !exporter.mcode().equals("") ){
            List<McodeDetail> mcodeDetails = SUtils.dt(mcodeRemoteService.findAllByMcodeIds(exporter.mcode()), McodeDetail.class );
            mcodeDetails.sort(Comparator.comparing(McodeDetail::getDisplayOrder));
            selectItems = mcodeDetails.stream().map(McodeDetail::getMcodeContent).collect(Collectors.toList()).toArray(new String[0]);
        }
        if ( exporter != null && !exporter.selectItems().equals("") ) {
            selectItems = exporter.selectItems();
        }
        if ( selectItems != null ) {
            //设置下拉范围 1-65535行
            CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(1, 65535, col, col);
            DVConstraint dvConstraint = DVConstraint.createExplicitListConstraint(selectItems);
            DataValidation dataValidation = new HSSFDataValidation(cellRangeAddressList, dvConstraint);
            sheet.addValidationData(dataValidation);
        }
    }

    List<JSONObject> templates() {
        Class<T> tClass = getFirstGenericityType(this.getClass());
        List<Field> fields = Lists.newArrayList();
        for (Field field : tClass.getDeclaredFields()) {
            if ( field.getAnnotation(Exporter.class) != null ) {
                fields.add(field);
            }
        }
        List<JSONObject> templates = Lists.newArrayList();
        for (Field e : fields) {
            JSONObject data = new JSONObject();
            Exporter exporter = e.getAnnotation(Exporter.class);
            ExcelCell excelCell = e.getAnnotation(ExcelCell.class);
            data.put(Exporter.HEADER, excelCell.header());
            data.put(Exporter.CHECKED, exporter.defaultChecked());
            String type = e.getType().equals(Date.class) ? "日期型 ": e.getType().equals(String.class) ? "字符型 " : "数字型 ";
            data.put(Exporter.DESCRIPTION, type + exporter.description());
            data.put(Exporter.EXAMPLE, exporter.example());
            templates.add(data);
        }
        return templates;
    }

    private static String error(String msg, Object ... values) {
        for (Object value : values) {
            msg = msg.replaceFirst("\\{\\}", String.valueOf(value));
        }
        return msg;
    }

    protected <A extends Annotation> A getAnnotationByHeader(String header, Class<A> annotation) {
        Field field = classCache.get(header);
        return field.getAnnotation(annotation);
    }

    /**
     * 全局性校验，子类覆盖
     * @param os
     * @return false， 出现错误，页面回显 true正常
     */
    public abstract boolean globeVerify(List<T> os);

    /**
     * 执行业务操作(保存等)，子类必须重写该方法
     *
     * @param os
     * @throws ImportBusinessException
     */
    public abstract void importData(List<T> os) throws ImportBusinessException;

    static class ValidAnnotationVerify implements AnnotationVerify<Valid> {

        @Override
        public boolean verify(Object value, Valid valid, QImportError error, Field field) {

            ExcelCell excelCell = field.getAnnotation(ExcelCell.class);
            if ( valid == null || error.isHasError() ) {
                return Boolean.TRUE;
            }
            //空校验
            if ( valid.notNull() && value == null ) {
                logger.error("[{}]不能为空", excelCell.header());
                error.error(field.getName(), "[" + excelCell.header() + "] 不能为空");
                return Boolean.TRUE;
            }
            if ( value == null ) {
                return Boolean.TRUE;
            }

            if ( field.getType().equals(String.class) ) {
                if ( StringUtils.length(String.class.cast(value)) > valid.length() ) {
                    logger.error("[{}:{}] 超过最大长度 [{}]", excelCell.header(), value, valid.length() );
                    error.error(field.getName(), error("[{}] 超过最大长度 [{}]", excelCell.header(), valid.length() ) );
                }
            } else if ( isNumer(field.getType()) ){
                Number number = NumberUtils.toDouble(value.toString());
                if ( valid.nonNegative() && number.doubleValue() < 0 ) {
                    logger.error("[{}] 不能为负数 [{}]", excelCell.header(), value);
                    error.error(field.getName(), error("[{}] 不能为负数 [{}]", excelCell.header(), value) );
                }
                //小数校验
                if ( field.getType().equals(Double.class) || field.getType().equals(Float.class) ) {
                    String pre = value.toString().substring(0,value.toString().indexOf("."));
                    String dec = value.toString().substring(value.toString().indexOf(".") + 1);
                    if ( pre.length() > valid.precision() ) {
                        logger.error("[{}] 整数位超过最大值[{}]位", excelCell.header(), valid.precision());
                        error.error(field.getName(), error("[{}] 整数位超过最大值[{}]位", excelCell.header(), valid.precision() ) );
                    }
                    if ( dec.length() > valid.decimal() ) {
                        logger.error("[{}] 小数位超过长度[{}]位", excelCell.header(), valid.decimal());
                        error.error(field.getName(), error("[{}] 小数位超过长度[{}]位", excelCell.header(), valid.decimal() ) );
                    }
                }
            }
            return Boolean.TRUE;
        }

        @Override
        public int order() {
            return 1;
        }
    }

    class DefaultTransfer implements ValueTransfer {

        private Map<String, String> mcodeCache = Maps.newConcurrentMap();

        @Override
        public Object transfer(Object srcValue, Field descField) {
            //处理微代码
            if ( srcValue == null ) {
                return null;
            }

            Exporter exporter = descField.getAnnotation(Exporter.class);
            if ( exporter != null ) {

                //处理微代码
                String mcode = exporter.mcode();
                Object value = mcodeCache.get(mcode + srcValue.toString().trim());
                if ( !"".equals(mcode) && value == null ) {
                    List<McodeDetail> mcodeDetails = SUtils.dt(mcodeRemoteService.findByMcodeIds(mcode), McodeDetail.class);
                    mcodeCache.putAll(mcodeDetails.stream().collect(Collectors.toMap(e -> {
                        return mcode + e.getMcodeContent();
                    }, McodeDetail::getThisId)));
                    return mcodeCache.get(mcode + srcValue.toString().trim());
                }
                //日期转换
                //数字转换
                return value;
            }
            return srcValue;
        }

        @Override
        public int order() {
            return Integer.MAX_VALUE;
        }
    }
}
