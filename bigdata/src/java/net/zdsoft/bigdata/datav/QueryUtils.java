package net.zdsoft.bigdata.datav;

import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.DatabaseType;
import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.entity.Api;
import net.zdsoft.bigdata.data.entity.Database;
import net.zdsoft.bigdata.data.entity.NosqlDatabase;
import net.zdsoft.bigdata.data.manager.IResult;
import net.zdsoft.bigdata.data.manager.api.Invoker;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.data.service.ApiService;
import net.zdsoft.bigdata.data.service.DatabaseService;
import net.zdsoft.bigdata.data.service.NosqlDatabaseService;
import net.zdsoft.bigdata.data.utils.JexlContextHolder;
import net.zdsoft.bigdata.datasource.*;
import net.zdsoft.bigdata.datasource.jdbc.JdbcDatabaseAdapter;
import net.zdsoft.bigdata.datasource.redis.RedisDatabaseAdapter;
import net.zdsoft.bigdata.datav.entity.AbstractDiagram;
import net.zdsoft.bigdata.datav.entity.InteractionBinding;
import net.zdsoft.bigdata.datav.entity.ScreenInteractionElement;
import net.zdsoft.bigdata.datav.interaction.InteractionJexlContext;
import net.zdsoft.bigdata.datav.service.InteractionActiveService;
import net.zdsoft.bigdata.datav.service.InteractionBindingService;
import net.zdsoft.bigdata.datav.service.ScreenInteractionElementService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.ServletUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2018/10/15 13:13
 */
@Lazy(false)
@Component
public class QueryUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    private static Invoker invoker;
    private static ApiService apiService;
    private static InteractionActiveService interactionActiveService;
    private static InteractionBindingService interactionBindingService;
    private static ScreenInteractionElementService screenInteractionElementService;
    private static DatasourceQueryChecker datasourceQueryChecker;
    private static DatabaseService databaseService;
    private static NosqlDatabaseService nosqlDatabaseService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        QueryUtils.applicationContext = applicationContext;
        invoker = (Invoker) applicationContext.getBean("invoker");
        apiService = (ApiService) applicationContext.getBean("apiService");
        databaseService = (DatabaseService) applicationContext.getBean("databaseService");
        nosqlDatabaseService = (NosqlDatabaseService) applicationContext.getBean("nosqlDatabaseService");
        datasourceQueryChecker = applicationContext.getBean(DatasourceQueryChecker.NAME, DatasourceQueryChecker.class);

        interactionActiveService = (InteractionActiveService) applicationContext.getBean("interactionActiveService");
        interactionBindingService = (InteractionBindingService) applicationContext.getBean("interactionBindingService");
        screenInteractionElementService = (ScreenInteractionElementService) applicationContext.getBean("screenInteractionElementService");
    }

    public static Result query(AbstractDiagram diagram, String screenId) throws EntryUtils.DataException {
        JexlContextHolder.setJexlContext(buildContext(diagram, screenId));
        try {
            if (Integer.valueOf(DataSourceType.DB.getValue()).equals(diagram.getDatasourceType())) {
                QueryExtractor extractor = null;
                Database database = databaseService.findOne(diagram.getDatasourceId());
                //1 try to jdbc
                Adapter adapter;
                if (database != null) {
                    extractor = QueryExtractor.extractorResultSetForJSONList();
                    adapter = jdbcDatabaseAdapter(database);
                }
                //2 try no sql
                else {
                    NosqlDatabase nosqlDatabase = nosqlDatabaseService.findOne(diagram.getDatasourceId());
                    if (nosqlDatabase != null) {
                        switch (nosqlDatabase.getType()) {
                            case "09":
                            case "07":
                                Database db = new Database();
                                db.setPort(nosqlDatabase.getPort());
                                db.setDomain(nosqlDatabase.getDomain());
                                db.setDbName(nosqlDatabase.getDbName());
                                db.setUsername(nosqlDatabase.getUserName());
                                db.setPassword(nosqlDatabase.getPassword());
                                db.setType(nosqlDatabase.getType());
                                adapter = jdbcDatabaseAdapter(db);
                                extractor = QueryExtractor.extractorResultSetForJSONList();
                                break;
                            case "12":
                                adapter = RedisDatabaseAdapter.RedisDatabaseKeyBuilder.builder()
                                        .dataType(DatabaseType.REDIS).withDomain(nosqlDatabase.getDomain())
                                        .withPassword(nosqlDatabase.getPassword()).withPort(nosqlDatabase.getPort())
                                        .build();
                                extractor = QueryExtractor.identity();
                                break;
                            default:
                                return new IResult(null, new RuntimeException("Not Support type"));
                        }
                    } else {
                        return new IResult(null, new RuntimeException("Database is null"));
                    }
                }
                QueryStatement<Adapter> queryStatement = new QueryStatement<>(adapter, diagram.getDatasourceValueSql());
                QueryResponse queryResponse = datasourceQueryChecker.query(queryStatement, extractor);
                return new IResult(queryResponse.getQueryValue(), queryResponse.getError());
            } else if (Integer.valueOf(DataSourceType.API.getValue()).equals(diagram.getDatasourceType())) {
                Api api = apiService.findOne(diagram.getDatasourceId());
                NoopAdapter adapter = NoopAdapter.noopAdapter(DataType.api());
                QueryStatement<NoopAdapter> queryStatement = new QueryStatement<>(adapter, api.getUrl());
                QueryResponse<String> queryResponse = datasourceQueryChecker.query(queryStatement, QueryExtractor.identity());
                return new IResult(queryResponse.getQueryValue(), queryResponse.getError());
            } else if (Integer.valueOf(DataSourceType.STATIC.getValue()).equals(diagram.getDatasourceType())) {
                return new IResult(diagram.getDatasourceValueJson(), null);
            } else {
                throw new EntryUtils.DataException("数据源类型为空");
            }
        } finally {
            JexlContextHolder.clearJexlContext();
        }
    }

    private static Adapter jdbcDatabaseAdapter(Database database) {
        return JdbcDatabaseAdapter.JdbcDatabaseAdapterBuilder.builder()
                .characterEncoding(database.getCharacterEncoding())
                .dbName(database.getDbName()).username(database.getUsername())
                .domain(database.getDomain()).password(database.getPassword())
                .port(database.getPort()).type(DatabaseType.parse(database.getType())).build();
    }

    private static InteractionJexlContext buildContext(AbstractDiagram diagram, String screenId) {

        //if (Active.disable.equals(interactionActiveService.isActice(diagram.getId()))) {
        //    return new InteractionJexlContext(ServletUtils.getLoginInfo(Evn.getRequest().getSession()));
        //}
        //TODO cache 取默认值
        Map<String, ScreenInteractionElement> interactionElementMap =
                screenInteractionElementService.getScreenDefaultInteractionItems(screenId)
                        .stream().collect(Collectors.toMap(ScreenInteractionElement::getBindKey, Function.identity()));
        //TODO cache 取交互配置参数
        Set<String> keyBindKeyMap = interactionBindingService.getBindingsByScreenId(screenId).stream()
                .map(InteractionBinding::getBindKey).collect(Collectors.toSet());
        //取登陆信息参数
        LoginInfo info = ServletUtils.getLoginInfo(Evn.getRequest().getSession());
        HttpServletRequest request = Evn.getRequest();
        InteractionJexlContext context = new InteractionJexlContext(info);
        for (String entry : keyBindKeyMap) {
            String bindValue = request.getParameter(entry);
            if (StringUtils.isBlank(bindValue)) {
                ScreenInteractionElement element = interactionElementMap.get(entry);
                if (element == null) {
                    context.put(entry, "");
                } else {
                    context.put(entry, element.getDefaultValue());
                }
            } else {
                context.put(entry, bindValue);
            }
        }
        return context;
    }
}
