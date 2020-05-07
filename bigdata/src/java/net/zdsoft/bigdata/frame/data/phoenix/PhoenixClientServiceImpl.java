package net.zdsoft.bigdata.frame.data.phoenix;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.echarts.EntryUtils.DataException;
import net.zdsoft.bigdata.data.entity.Database;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.service.DatabaseService;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.bigdata.datasource.*;
import net.zdsoft.bigdata.datasource.jdbc.JdbcDatabaseAdapter;
import net.zdsoft.bigdata.frame.data.hbase.HbaseClientService;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.entity.MetadataIndex;
import net.zdsoft.bigdata.metadata.entity.MetadataTableColumn;
import net.zdsoft.bigdata.metadata.service.MetadataIndexService;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.bigdata.metadata.service.MetadataTableColumnService;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service("phoenixClientService")
public class PhoenixClientServiceImpl implements PhoenixClientService,
        ApplicationContextAware {
    private static Logger logger = LoggerFactory
            .getLogger(PhoenixClientServiceImpl.class);

    private static DatasourceQueryChecker datasourceQueryChecker;

    private static Database database;

    @Autowired
    private OptionService optionService;

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private HbaseClientService hbaseClientService;

    @Autowired
    private MetadataService metadataService;

    @Autowired
    private MetadataTableColumnService metadataTableColumnService;

    @Autowired
    private MetadataIndexService metadataIndexService;

    public Adapter getAdapter(String dataSourceId) {
        if (StringUtils.isBlank(dataSourceId)) {
            OptionDto hbaseDto = optionService.getAllOptionParam("hbase");
            if (hbaseDto == null || hbaseDto.getStatus() == 0) {
                return JdbcDatabaseAdapter.JdbcDatabaseAdapterBuilder.builder()
                        .build();
            }
            initDatabase(hbaseDto);
            return JdbcDatabaseAdapter.JdbcDatabaseAdapterBuilder.builder()
                    .domain(database.getDomain()).port(database.getPort())
                    .type(DatabaseType.parse(database.getType())).build();
        } else {
            Database db = databaseService.findOne(dataSourceId);
            if (db == null) {
                return JdbcDatabaseAdapter.JdbcDatabaseAdapterBuilder.builder()
                        .build();
            }
            return JdbcDatabaseAdapter.JdbcDatabaseAdapterBuilder.builder()
                    .domain(db.getDomain())
                    .port(db.getPort()).type(DatabaseType.parse(db.getType()))
                    .build();
        }
    }

    private void initDatabase(OptionDto hbaseDto) {
        database = new Database();
        database.setPort(Integer.valueOf(hbaseDto.getFrameParamMap()
                .get("port")));
        database.setDomain(hbaseDto.getFrameParamMap().get("domain"));
        database.setType(DatabaseType.HBASE.getType());
    }


    @Override
    public List<Json> getDataListFromPhoenix(String dataSourceId, String sql,
                                             List<Json> paramList) {
        List<Json> resultList = new ArrayList<Json>();
        try {
            Adapter hbaseAdapt = getAdapter(dataSourceId);
            QueryExtractor extractor = QueryExtractor
                    .extractorResultSetForJSONList();
            QueryStatement<Adapter> queryStatement = new QueryStatement<>(
                    hbaseAdapt, sql);

            QueryResponse queryResponse = datasourceQueryChecker.query(
                    queryStatement, extractor);
            if (queryResponse.getError() != null) {
                throw new BigDataBusinessException(queryResponse.getError());
            } else {
                return resultList = parse(queryResponse.getQueryValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return resultList;
        }
    }

    public Boolean createTableByMetadata(String mdId) throws Exception {
        Metadata md = metadataService.findOne(mdId);
        Boolean isExist = hbaseClientService.isExistTable(md.getTableName().toUpperCase());
        if (isExist) {
            return false;
        }
        List<MetadataTableColumn> columnList = metadataTableColumnService.findByMetadataId(mdId);
        StringBuffer sql = new StringBuffer("CREATE TABLE ");
        sql.append(md.getTableName()).append("( ID varchar not null primary key,");
        int index = 0;
        for (MetadataTableColumn column : columnList) {
            String[] columnArray = column.getColumnName().split(":");
            if (index == 0)
                sql.append(" \"").append(columnArray[0]).append("\".\"").append(columnArray[1]).append("\" ").append("varchar");
            else
                sql.append(", \"").append(columnArray[0]).append("\".\"").append(columnArray[1]).append("\" ").append("varchar");
            index++;
        }
        sql.append(")");
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            Adapter adapter = getAdapter(null);
            Statement<Adapter> statement = new Statement<>(adapter);
            JdbcTemplate template = datasourceQueryChecker.execute(statement, DatasourceExecutor.jdbcTemplate());
            conn = template.getDataSource().getConnection();
            ps = conn.prepareStatement(sql.toString());
            ps.execute();
        } catch (Exception e) {
            logger.error("数据库异常", e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    logger.error("数据库PreparedStatement关闭异常", e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("数据库Connection关闭异常", e);
                }
            }
            return true;
        }
    }

    @Override
    public Boolean createTableViewByMetadata(String mdId) throws BigDataBusinessException {
        Metadata md = metadataService.findOne(mdId);
        Metadata table = metadataService.findOne(md.getParentId());

        StringBuffer sql = new StringBuffer("CREATE VIEW ");
        sql.append(md.getName()).append("(");
        String[] columns = StringUtils.split(md.getTableName(), ",");
        for (String column : columns) {
            sql.append(" \"").append(column).append(" varchar");
        }
        sql.append(")").append(" AS SELECT * FROM ").append(table.getTableName());
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            Adapter adapter = getAdapter(null);
            Statement<Adapter> statement = new Statement<>(adapter);
            JdbcTemplate template = datasourceQueryChecker.execute(statement, DatasourceExecutor.jdbcTemplate());
            conn = template.getDataSource().getConnection();
            ps = conn.prepareStatement(sql.toString());
            ps.execute();
        } catch (Exception e) {
            logger.error("数据库异常", e);
            throw new BigDataBusinessException(e.getMessage(), e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    logger.error("数据库PreparedStatement关闭异常", e);
                    throw new BigDataBusinessException(e.getMessage(), e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("数据库Connection关闭异常", e);
                    throw new BigDataBusinessException(e.getMessage(), e);
                }
            }
            return true;
        }
    }

    @Override
    public Boolean createTableIndexByMetadataIndex(String indexId) throws BigDataBusinessException {
        MetadataIndex metadataIndex = metadataIndexService.findOne(indexId);
        Metadata table = metadataService.findOne(metadataIndex.getMdId());

        List<String> columnIds = JSON.parseArray(metadataIndex.getColumns(), String.class);
        List<MetadataTableColumn> columns = metadataTableColumnService.findListByIdIn(columnIds.toArray(new String[columnIds.size()]));
        List<String> nameList = columns.stream().map(MetadataTableColumn::getColumnName).collect(Collectors.toList());

        StringBuffer sql = new StringBuffer("CREATE INDEX ");
        sql.append(metadataIndex.getName()).append(" ON ").append(table.getTableName()).append("(");

        sql.append(StringUtils.join(nameList, ",")).append(")");
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            Adapter adapter = getAdapter(null);
            Statement<Adapter> statement = new Statement<>(adapter);
            JdbcTemplate template = datasourceQueryChecker.execute(statement, DatasourceExecutor.jdbcTemplate());
            conn = template.getDataSource().getConnection();
            ps = conn.prepareStatement(sql.toString());
            ps.execute();
        } catch (Exception e) {
            logger.error("数据库异常", e);
            throw new BigDataBusinessException(e.getMessage(), e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    logger.error("数据库PreparedStatement关闭异常", e);
                    throw new BigDataBusinessException(e.getMessage(), e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("数据库Connection关闭异常", e);
                    throw new BigDataBusinessException(e.getMessage(), e);
                }
            }
            return true;
        }
    }

    public void upsert(List<String> sqls) throws SQLException {

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            Adapter adapter = getAdapter(null);
            Statement<Adapter> statement = new Statement<>(adapter);
            JdbcTemplate template = datasourceQueryChecker.execute(statement,
                    DatasourceExecutor.jdbcTemplate());
            conn = template.getDataSource().getConnection();
            conn.setAutoCommit(false);// 1,首先把Auto commit设置为false,不让它自动提交
            for (String sql : sqls) {
                ps = conn.prepareStatement(sql);
                ps.execute();
            }
            conn.commit();
        } catch (Exception e) {
            logger.error("数据库异常", e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    logger.error("数据库PreparedStatement关闭异常", e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("数据库Connection关闭异常", e);
                }
            }
        }

    }


    private List<Json> parse(Object value) throws DataException {
        try {
            if (value == null) {
                return Collections.emptyList();
            }

            String text = null;
            if (value instanceof String) {
                text = (String) value;
            } else {
                text = JSONObject.toJSONString(value);
            }

            List<Json> list;
            DefaultJSONParser parser = new DefaultJSONParser(text,
                    ParserConfig.getGlobalInstance());

            JSONLexer lexer = parser.lexer;
            int token = lexer.token();
            if (token == JSONToken.NULL) {
                lexer.nextToken();
                list = null;
            } else if (token == JSONToken.EOF && lexer.isBlankInput()) {
                list = null;
            } else {
                list = new ArrayList<>();
                parser.parseArray(Json.class, list);

                parser.handleResovleTask(list);
            }
            parser.close();

            return list;
        } catch (Exception e) {
            throw new DataException("数据解析异常");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        datasourceQueryChecker = applicationContext.getBean(
                DatasourceQueryChecker.NAME, DatasourceQueryChecker.class);
    }

    @Override
    public DataSource getDataSource() {
        try {
            Adapter adapter = getAdapter(null);
            Statement<Adapter> statement = new Statement<>(adapter);
            JdbcTemplate template = datasourceQueryChecker.execute(statement,
                    DatasourceExecutor.jdbcTemplate());
            return template.getDataSource();
        } catch (Exception e) {
            logger.error("数据库异常", e);
        }
        return null;
    }

}
