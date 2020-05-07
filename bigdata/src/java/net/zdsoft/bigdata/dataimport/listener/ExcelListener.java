package net.zdsoft.bigdata.dataimport.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import net.zdsoft.bigdata.dataimport.service.BgDaCustomTableService;
import net.zdsoft.bigdata.frame.data.mysql.MysqlClientService;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:zhujy
 * @since:2019/6/18 21:24
 */
public class ExcelListener extends AnalysisEventListener<List<String>> {

    /**
     * 控制是读还是写 false表示读 true表示写
     */
    private boolean readAndWrite=false;

    public ExcelListener(boolean readAndWrite) {
        this.readAndWrite = readAndWrite;
    }

    private boolean firstTitle = false;
    private boolean deleteAll = false;
    private Integer sheetNum = 1;
    private String tableName;
    private List<String> fieldList;
    private List<String> indexList;
    /**
     * 用来判断是否是第一次进方法
     */
    boolean flag = true;
    Integer dataNum=0;
    private MysqlClientService mysqlClientService;
    private BgDaCustomTableService bgDaCustomTableService;

    public ExcelListener() {

    }



    public ExcelListener(boolean firstTitle, boolean deleteAll, Integer sheetNum, String tableName,
    List<String> fieldList,List<String> indexList,MysqlClientService mysqlClientService,BgDaCustomTableService bgDaCustomTableService) {
        this.firstTitle = firstTitle;
        this.deleteAll = deleteAll;
        this.sheetNum = sheetNum;
        this.tableName = tableName;
        this.fieldList=fieldList;
        this.indexList=indexList;
        this.readAndWrite=true;
        this.mysqlClientService=mysqlClientService;
        this.bgDaCustomTableService=bgDaCustomTableService;
    }
    //自定义用于暂时存储data,可以通过实例获取该值
    private List<List<String>> datas = new ArrayList<>();
    private List<String> sqlList=new ArrayList<>();

    @Override
    public void invoke(List<String> object, AnalysisContext context) {
        //数据存储到list，供批量处理，或后续自己业务逻辑处理。
        datas.add(object);
        //每读一行，就插入(写)
        if (readAndWrite){
            doSomething(object, context);
            flag = false;
        }
    }

    private void doSomething(List<String> object, AnalysisContext context) {
        if (flag) {
            //第一次执行的时候，如果勾选了删除全部后添加，则，在第一次时清空表
            if (deleteAll) {
                String deleteSql = "delete from " + tableName;
                /*MysqlClientService mysqlClientService = (MysqlClientService) Evn
                        .getBean("mysqlClientService");*/
                mysqlClientService.execSql(null,null,deleteSql,null);
                //BgDaCustomTableService bgDaCustomTableService =(BgDaCustomTableService) Evn.getBean("bgDaCustomTableService");
                bgDaCustomTableService.updateDataNumToZeroByTableName("0",tableName);
            }
        }
        if (context.getCurrentSheet().getSheetNo() == sheetNum) {
            //勾选了第一行作为标题
            if (firstTitle) {
                if (context.getCurrentRowNum() != 0) {
                    //第二行开始入库
                    insertSqlToMysql(object);
                }
            } else {
                //入库
                insertSqlToMysql(object);
            }
        }
    }

    private void insertSqlToMysql(List<String> object) {

        String sql = "insert into " + tableName +"(id,"+ StringUtils.join(fieldList,",") + ") values('" + UuidUtils.generateUuid() + "',";
        for (int i = 0; i < indexList.size(); i++) {
            sql += "'"+object.get(Integer.valueOf(indexList.get(i)))+"'";
            if (i < indexList.size() - 1) {
                sql += ",";
            }
        }
        sql += ");";
        dataNum++;
        System.out.println("新增sql:" + sql);
        sqlList.add(sql);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        /*MysqlClientService mysqlClientService = (MysqlClientService) Evn
                .getBean("mysqlClientService");*/
        mysqlClientService.execSqls(null,null,sqlList);
    }

    public Integer getDataNum() {
        return dataNum;
    }

    public void setDataNum(Integer dataNum) {
        this.dataNum = dataNum;
    }

    public boolean isFirstTitle() {
        return firstTitle;
    }

    public void setFirstTitle(boolean firstTitle) {
        this.firstTitle = firstTitle;
    }

    public boolean isDeleteAll() {
        return deleteAll;
    }

    public void setDeleteAll(boolean deleteAll) {
        this.deleteAll = deleteAll;
    }

    public Integer getSheetNum() {
        return sheetNum;
    }

    public void setSheetNum(Integer sheetNum) {
        this.sheetNum = sheetNum;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<List<String>> getDatas() {
        return datas;
    }

    public void setDatas(List<List<String>> datas) {
        this.datas = datas;
    }

    public boolean isReadAndWrite() {
        return readAndWrite;
    }

    public void setReadAndWrite(boolean readAndWrite) {
        this.readAndWrite = readAndWrite;
    }
}
