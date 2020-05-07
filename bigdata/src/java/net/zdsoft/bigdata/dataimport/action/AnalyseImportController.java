package net.zdsoft.bigdata.dataimport.action;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.support.ExcelTypeEnum;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.dataimport.dto.DataSubmitDto;
import net.zdsoft.bigdata.dataimport.entity.BgDaCustomTable;
import net.zdsoft.bigdata.dataimport.entity.BgDaCustomTableColumn;
import net.zdsoft.bigdata.dataimport.listener.ExcelListener;
import net.zdsoft.bigdata.dataimport.service.BgDaCustomTableColumnService;
import net.zdsoft.bigdata.dataimport.service.BgDaCustomTableService;
import net.zdsoft.bigdata.dataimport.utils.ExcelReaderFactory;
import net.zdsoft.bigdata.frame.data.mysql.MysqlClientService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author:zhujy
 * @since:2019/6/17 13:39
 */
@Controller
@RequestMapping(value = "/bigdata/data/analyse/")
public class AnalyseImportController extends BaseAction {

    private static Logger logger = LoggerFactory
            .getLogger(AnalyseImportController.class);

    @Autowired
    private BgDaCustomTableService bgDaCustomTableService;

    @Autowired
    private BgDaCustomTableColumnService bgDaCustomTableColumnService;

    @Autowired
    private MysqlClientService mysqlClientService;

    @RequestMapping("import")
    public String analyse(ModelMap modelMap,String fileName,String filePath,String tableName) {
        System.out.println(fileName);
        List<BgDaCustomTable> bgDaCustomTables = bgDaCustomTableService.findAll();
        List<String> tableNames = bgDaCustomTables.stream().
                filter(x -> "excel".equals(x.getSource())).
                map(x -> x.getTableName()).collect(Collectors.toList());
        modelMap.put("bgDaCustomTableList", bgDaCustomTables);
        modelMap.put("tableNameList", tableNames);
        modelMap.put("fileName",fileName);
        modelMap.put("filePath",filePath);
        modelMap.put("tableName",tableName);
        return "/bigdata/dataModelView/import.ftl";
    }

    @RequestMapping("turnToCsv")
    public String turnToCsv(ModelMap modelMap) {
        List<BgDaCustomTable> bgDaCustomTables = bgDaCustomTableService.findAll();
        List<String> tableNames = bgDaCustomTables.stream().
                filter(x -> "csv".equals(x.getSource())).
                map(x -> x.getTableName()).collect(Collectors.toList());
        modelMap.put("bgDaCustomTableList", bgDaCustomTables);
        modelMap.put("tableNames", tableNames);
        return "/bigdata/dataModelView/importCsv.ftl";
    }

    /**
     * 根据表名展示所有字段
     * @param tableName
     * @return
     */
    @RequestMapping("showFields")
    @ResponseBody
    public Response showFields(String tableName) {
        String id = bgDaCustomTableService.findIdByTableName(tableName);
        List<String> fieldList = bgDaCustomTableColumnService.findColumnNameByTableId(id);
        return Response.ok().data(fieldList).build();
    }


    /**
     * 展示第一行标题
     * @param filePath
     * @param fileName
     * @return
     */
    @RequestMapping("showFirstTitle")
    @ResponseBody
    public Response showFirstTitle(String filePath, String fileName) {
        String fullFilePath = filePath + File.separator + fileName;
        List<String> titleList = new ArrayList<>();
        try (InputStream inputStream = new FileInputStream(fullFilePath)) {
            ExcelListener listener = readExcel(fullFilePath, inputStream);
            List<List<String>> datas = listener.getDatas();
            titleList = datas.get(0);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return Response.error().message("Excel读取时发生错误，请检查").build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.error().message("新建是发生错误").build();
        }
        return Response.ok().data(titleList).build();
    }


    /**
     * 展示csv文件前100行
     * @param filePath
     * @param fileName
     * @param code
     * @param separators
     * @return
     */
    @RequestMapping("showCsv")
    public String showCsv(String filePath, String fileName, String code, String separators,ModelMap modelMap) {
        String fullFilePath = filePath + File.separator + fileName;
        List<String[]> result = new ArrayList<>();
        try (InputStream inputStream = new FileInputStream(fullFilePath)) {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = br.readLine()) != null) {
                String lineCode = new String(line.getBytes(), code);
                result.add(lineCode.split(separators));
            }
            result = result.stream().limit(101).collect(Collectors.toList());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        modelMap.put("csvList",result);
        return "/bigdata/dataModelView/showCsv.ftl";
    }

    /**
     * 导入数据
     * @param
     * @return
     */
    @RequestMapping("submitCsv")
    @ResponseBody
    public Response submitCsv(DataSubmitDto dataSubmitDto, String code, String separators) {
        List<String> fieldList = Arrays.asList(dataSubmitDto.getFieldList().split("\\|"));
        List<String> indexList = Arrays.asList(dataSubmitDto.getIndexList().split("\\|"));
        String fullFilePath = dataSubmitDto.getFilePath() + File.separator + dataSubmitDto.getFileName();
        List<String[]> result = new ArrayList<>();
        try (InputStream inputStream = new FileInputStream(fullFilePath)) {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            //每读一行，进行转码和分割
            while ((line = br.readLine()) != null) {
                String lineCode = new String(line.getBytes(), code);
                result.add(lineCode.split(separators));
            }
            //删除全部 再进行重新导入
            if (dataSubmitDto.isDeleteAll()) {
                String deleteSql = "delete from " + dataSubmitDto.getTableName();
                mysqlClientService.execSql(null, null, deleteSql, null);
                bgDaCustomTableService.updateDataNumToZeroByTableName("0",dataSubmitDto.getTableName());
            }
            if (dataSubmitDto.isFirstTitle()) {
                result.remove(0);
            }

            List<String> sqlList = new ArrayList<>();
            for (int i = 0; i < result.size(); i++) {
                String sql = "insert into " + dataSubmitDto.getTableName()+"(id,"+StringUtils.join(fieldList,",") + ") values('" + UuidUtils.generateUuid() + "',";
                for (int j = 0; j < indexList.size(); j++) {
                    sql += "'" + result.get(i)[Integer.valueOf(indexList.get(j))] + "'";
                    if (j < indexList.size() - 1) {
                        sql += ",";
                    }
                }
                sql += ");";
                System.out.println(sql);
                sqlList.add(sql);
            }
           mysqlClientService.execSqls(null, null, sqlList);
           bgDaCustomTableService.updateDataNumByTableName(String.valueOf(result.size()),dataSubmitDto.getTableName());

        } catch (IOException e) {
            e.printStackTrace();
            return Response.error().message("导入发生异常，请检查csv文件和字段是否对应").build();
        } catch (Exception e) {
            return Response.error().message("导入发生异常，请检查csv文件是否有特殊字符导致分割异常").build();
        }
        return Response.ok().message("导入csv文件成功").build();
    }

    /**
     * excel导入
     */
    @RequestMapping(value = "submit")
    @ResponseBody
    public Response submit(DataSubmitDto dataSubmitDto) {
        List<String> fieldList = Arrays.asList(dataSubmitDto.getFieldList().split("\\|"));
        List<String> indexList = Arrays.asList(dataSubmitDto.getIndexList().split("\\|"));
        //获取上传路径，读入excel数据
        String fullFilePath = dataSubmitDto.getFilePath() + File.separator + dataSubmitDto.getFileName();
        ExcelTypeEnum excelType = ExcelTypeEnum.XLSX;
        if (fullFilePath.endsWith(".xls")) {
            excelType = ExcelTypeEnum.XLS;
        }
        try (InputStream inputStream = new FileInputStream(fullFilePath)) {
            //获取上传路径，读入excel数据
            // 解析每行结果在listener中处理
            ExcelListener listener = new ExcelListener(dataSubmitDto.isFirstTitle()
                    , dataSubmitDto.isDeleteAll(), dataSubmitDto.getSheetNum(), dataSubmitDto.getTableName(),
                   fieldList,indexList,mysqlClientService,bgDaCustomTableService);
            ExcelReader excelReader = ExcelReaderFactory.getExcelReader(
                    inputStream, excelType, null, listener, true);
            excelReader.read();
            //导入数据同时更新自定义表 表的数据量
            bgDaCustomTableService.updateDataNumByTableName(listener.getDataNum().toString(),dataSubmitDto.getTableName());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return Response.error().message("数据添加入mysql时出现问题,请检查").build();
        }
        return Response.ok().message("导入成功").build();
    }

    /**
     * 显示Excel前100条数据
     * @param filePath
     * @param fileName
     * @return
     */
    @RequestMapping("showExcel")
    public String readExcel(String filePath, String fileName,ModelMap modelMap) {
        List<List<String>> dataList=new ArrayList<>();
        String fullFilePath = filePath + File.separator + fileName;
        try (InputStream inputStream = new FileInputStream(fullFilePath)) {
            ExcelListener listener = readExcel(fullFilePath, inputStream);
            dataList = listener.getDatas().stream().limit(101).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error(e.getMessage());
            //return Response.error().message("请检查excel,格式不正确，或者没有内容").build();
        }
        modelMap.put("excelList",dataList);
        return "/bigdata/dataModelView/showExcel.ftl";

    }

    /**
     * 清空mysql表数据
     * @param tableName
     * @return
     */
    @RequestMapping("deleteTable")
    @ResponseBody
    public Response deleteTable(String tableName) {
        try {
            String sql = "delete from " + tableName;
            mysqlClientService.execSql(null, null, sql, null);
            bgDaCustomTableService.updateDataNumToZeroByTableName("0",tableName);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return Response.error().message("删除mysql数据时发生异常").build();
        }
        return Response.ok().message("清空"+tableName+"表成功").build();
    }

    /**
     * 删除操作
     * @param id
     * @return
     */
    @RequestMapping("dropTable")
    @ResponseBody
    public Response dropTable(String id) {
        try {
            bgDaCustomTableService.deleteTableList(id);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.error().message("删除时发生异常").build();
        }
        return Response.error().message("删除成功").build();
    }

    /**
     * 新增表操作
     * @param dataSubmitDto
     * @return
     */
    @RequestMapping(value = "createTable")
    @ResponseBody
    public Response createDatabase(DataSubmitDto dataSubmitDto) {
        String[] columns = dataSubmitDto.getColumns().split("\\|");
        String[] types = dataSubmitDto.getTypes().split("\\|");
        //生成六位随机数+用户维护表名 生成表
        String result = createSixRandom();
        String realTableName = dataSubmitDto.getTableName() + result;
        dataSubmitDto.setTableName(realTableName);

        try{
            if (dataSubmitDto.getSource().equals("csv")) {
                //保存到数据分析自定义表
                String id = saveBgDaCustomTable(dataSubmitDto);
                //一对多保存到字段表
                saveBgDaCustomTableColumn(columns, types, id);
            } else {
                //保存到数据分析自定义表
                String id = saveBgDaCustomTable(dataSubmitDto);
                //一对多保存到字段表
                saveBgDaCustomTableColumn(columns, types, id);
            }
            //创建mysql表
            String sql = createTableSql(columns, types, realTableName);
            System.out.println("建表语句是：" + sql);
            mysqlClientService.execSql(null, null, sql, null);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.error().message("创建表时发生异常，请检查！").build();
        }
        return Response.ok().data(dataSubmitDto.getTableName()).message("创建表成功").build();
    }

    private ExcelListener readExcel(String fullFilePath, InputStream inputStream) throws IOException, InvalidFormatException {
        ExcelTypeEnum excelType = ExcelTypeEnum.XLSX;
        if (fullFilePath.endsWith(".xls")) {
            excelType = ExcelTypeEnum.XLS;
        }
        ExcelListener listener = new ExcelListener(false);
        ExcelReader excelReader = ExcelReaderFactory.getExcelReader(
                inputStream, excelType, null, listener, true);
        excelReader.read();
        return listener;
    }

    private String createTableSql(String[] columns, String[] types, String realTableName) {
        String sql = "create table " + realTableName + "(id char(32) primary key not null,";
        for (int i = 0; i < columns.length; i++) {
            sql += columns[i] + " " + types[i];
            if (i < columns.length - 1) {
                sql += ",";
            }
        }
        sql += ")DEFAULT CHARSET=utf8;";
        return sql;
    }

    private String saveBgDaCustomTable(DataSubmitDto dataSubmitDto) {
        String id = UuidUtils.generateUuid();
        BgDaCustomTable bgDaCustomTable = new BgDaCustomTable();
        bgDaCustomTable.setId(id);
        bgDaCustomTable.setName(dataSubmitDto.getFileName());
        bgDaCustomTable.setCreationTime(new Date());
        bgDaCustomTable.setDataNum(0+"");
        bgDaCustomTable.setUnitId(getLoginInfo().getUnitId());
        bgDaCustomTable.setUserId(getLoginInfo().getUserId());
        bgDaCustomTable.setRemark(dataSubmitDto.getRemark());
        bgDaCustomTable.setModifyTime(new Date());
        bgDaCustomTable.setSource(dataSubmitDto.getSource());
        bgDaCustomTable.setTableName(dataSubmitDto.getTableName());
        bgDaCustomTableService.save(bgDaCustomTable);
        return id;
    }

    private void saveBgDaCustomTableColumn(String[] columns, String[] types, String id) {
        List<BgDaCustomTableColumn> bgDaCustomTableColumns = new ArrayList<>();
        for (int i = 0; i < columns.length; i++) {
            BgDaCustomTableColumn bgDaCustomTableColumn = new BgDaCustomTableColumn();
            bgDaCustomTableColumn.setId(UuidUtils.generateUuid());
            bgDaCustomTableColumn.setTableId(id);
            bgDaCustomTableColumn.setColumnName(columns[i]);
            bgDaCustomTableColumn.setColumnType(types[i]);
            bgDaCustomTableColumns.add(bgDaCustomTableColumn);
        }
        bgDaCustomTableColumnService.saveAll(bgDaCustomTableColumns.toArray(new BgDaCustomTableColumn[0]));
    }

    private String createSixRandom() {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < 6; i++) {
            result += random.nextInt(10);
        }
        return result;
    }



}
