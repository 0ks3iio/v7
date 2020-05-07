package net.zdsoft.framework.dao;

import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.JdbcUtils;
import net.zdsoft.framework.utils.MapRowMapper;
import net.zdsoft.framework.utils.MultiRowMapper;
import net.zdsoft.framework.utils.SQLParser;
import net.zdsoft.framework.utils.SingleRowMapper;
import net.zdsoft.framework.utils.Validators;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于处理数据库操作的DAO基类.
 * 
 * @author liangxiao
 * @author yukh
 * @author wangjw
 * @author huangwj
 * @author linqz
 * @version $Revision: 1.38 $, $Date: 2008/07/31 11:04:03 $
 */
public class BasicDAO {

    /**
     * 数据库操作的超时时间，默认为300000毫秒，如果超时会在日志里严重警告
     */
    private static final int EXECUTE_OVERTIME = 300000;

    /**
     * 默认SQL批处理操作的记录数量.
     */
    private static int DEFAULT_BATCH_SIZE = 500;

    /**
     * 默认数据库IN SQL中参数的允许的最大数量.
     */
    private static int DEFAULT_MAX_IN_SQL_PARAM_COUNT = 300;

    private int batchSize = DEFAULT_BATCH_SIZE;
    private int maxInSQLParamCount = DEFAULT_MAX_IN_SQL_PARAM_COUNT;

    private NamedParameterJdbcDaoSupport namedParameterJdbcDaoSupport;
    @Autowired
    @Qualifier("dataSourceJap")
    private DataSource dataSource;

    @Autowired
    public void setNamedParameterJdbcDaoSupport(
            NamedParameterJdbcDaoSupport namedParameterJdbcDaoSupport) {
        this.namedParameterJdbcDaoSupport = namedParameterJdbcDaoSupport;
    }

    public JdbcTemplate getJdbcTemplate() {
        if (namedParameterJdbcDaoSupport.getJdbcTemplate().getDataSource() != null
                && dataSource != null
                && namedParameterJdbcDaoSupport.getJdbcTemplate().getDataSource() != dataSource) {
            namedParameterJdbcDaoSupport.getJdbcTemplate().setDataSource(dataSource);
        }
        return namedParameterJdbcDaoSupport.getJdbcTemplate();
    }

    private JdbcTemplate jdbcTemplate;

    /**
     * Return a SimpleJdbcTemplate wrapping the configured JdbcTemplate.
     */
    public JdbcTemplate getSimpleJdbcTemplate() {
        return this.jdbcTemplate;
    }

    /**
     * Get a JDBC Connection, either from the current transaction or a new one.
     * 
     * @return the JDBC Connection
     * @throws CannotGetJdbcConnectionException
     *             if the attempt to get a Connection failed
     * @see org.springframework.jdbc.datasource.DataSourceUtils#getConnection(javax.sql.DataSource)
     */
    protected final Connection getConnection() throws CannotGetJdbcConnectionException {
        return DataSourceUtils.getConnection(getDataSource());
    }

    /**
     * Close the given JDBC Connection, created via this DAO's DataSource,
     * if it isn't bound to the thread.
     * 
     * @param con
     *            Connection to close
     * @see org.springframework.jdbc.datasource.DataSourceUtils#releaseConnection
     */
    protected final void releaseConnection(Connection con) {
        DataSourceUtils.releaseConnection(con, getDataSource());
    }

    /**
     * Return the JDBC DataSource used by this DAO.
     */
    public final DataSource getDataSource() {
        return (this.getJdbcTemplate() != null ? this.getJdbcTemplate().getDataSource() : null);
    }

    /**
     * Set the JDBC DataSource to be used by this DAO.
     */
    public final void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public BasicDAO() {

    }

    private class MapResultExtractor<K, V> implements ResultSetExtractor<Map<K, V>> {

        private MapRowMapper<K, V> rowMapper = null;

        public MapResultExtractor(MapRowMapper<K, V> rowMapper) {
            this.rowMapper = rowMapper;
        }

        public Map<K, V> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<K, V> results = new HashMap<K, V>();
            int index = 0;
            while (rs.next()) {
                results.put(rowMapper.mapRowKey(rs, index++), rowMapper.mapRowValue(rs, index++));
            }

            return results;
        }
    }

    private class MultiResultSetExtractor<T> implements ResultSetExtractor<List<T>> {

        private MultiRowMapper<T> multiRowMapper = null;

        public MultiResultSetExtractor(MultiRowMapper<T> multiRowMapper) {
            this.multiRowMapper = multiRowMapper;
        }

        public List<T> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<T> data = new ArrayList<T>();
            int index = 0;
            while (rs.next()) {
                data.add(multiRowMapper.mapRow(rs, index++));
            }
            return data;
        }
    }

    private class MultiTopResultSetExtractor<T> implements ResultSetExtractor<List<T>> {

        private MultiRowMapper<T> multiRowMapper = null;
        private int topLimit;

        public MultiTopResultSetExtractor(MultiRowMapper<T> multiRowMapper, int topLimit) {
            this.multiRowMapper = multiRowMapper;
            this.topLimit = topLimit;
        }

        public List<T> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<T> data = new ArrayList<T>();
            int index = 0;
            while (rs.next() && index < topLimit) {
                data.add(multiRowMapper.mapRow(rs, index++));
            }
            return data;
        }
    }

    private class SingleResultSetExtractor<T> implements ResultSetExtractor<T> {
        private SingleRowMapper<T> singleRowMapper = null;

        public SingleResultSetExtractor(SingleRowMapper<T> singleRowMapper) {
            this.singleRowMapper = singleRowMapper;
        }

        public T extractData(ResultSet rs) throws SQLException, DataAccessException {
            if (rs.next()) {
                return singleRowMapper.mapRow(rs);
            }
            return null;
        }
    }

    private class SpecialBatchPreparedStatementSetter implements BatchPreparedStatementSetter {

        private int[] argTypes = null;
        private List<Object[]> listOfArgs = null;

        public SpecialBatchPreparedStatementSetter(List<Object[]> listOfArgs, int[] argTypes) {
            this.listOfArgs = listOfArgs;
            this.argTypes = argTypes;
        }

        public int getBatchSize() {
            return listOfArgs.size();
        }

        public void setValues(PreparedStatement ps, int i) throws SQLException {
            Object[] args = (Object[]) listOfArgs.get(i);
            JdbcUtils.setSuitedParamsToStatement(args, argTypes, ps);
        }
    }

    private static Logger logger = Logger.getLogger(BasicDAO.class);

    /**
     * 批量更新数据.
     * 
     * @param sql
     *            sql语句
     * @param listOfArgs
     *            参数数组的List集合
     * @param argTypes
     *            参数类型数组
     * @return 更新的记录条数数组
     */
    protected int[] batchUpdate(String sql, List<Object[]> listOfArgs, int[] argTypes) {
        if (listOfArgs == null || listOfArgs.isEmpty()) {
            return new int[0];
        }

        if (logger.isDebugEnabled()) {
            for (int i = 0; i < listOfArgs.size(); i++) {
                Object[] args = (Object[]) listOfArgs.get(i);
                logger.debug(JdbcUtils.getSQL(sql, args));
            }
        }
        long startTime = System.currentTimeMillis();

        try {
            int destPos = 0;
            int size = listOfArgs.size();
            int[] totalResults = new int[size];
            int batchExecCount = (size % batchSize == 0) ? size / batchSize : size / batchSize + 1;

            if (logger.isDebugEnabled()) {
                logger.debug("Batch executed times: " + batchExecCount);
            }

            for (int i = 0; i < batchExecCount; i++) {
                int from = batchSize * i;
                int to = 0;
                if (i == batchExecCount - 1) {
                    to = size;
                }
                else {
                    to = batchSize * (i + 1);
                }

                List<Object[]> batchListOfArgs = listOfArgs.subList(from, to);
                int[] batchResults = getJdbcTemplate().batchUpdate(sql,
                        new SpecialBatchPreparedStatementSetter(batchListOfArgs, argTypes));
                System.arraycopy(batchResults, 0, totalResults, destPos, batchResults.length);
                destPos += batchResults.length;
            }
            return totalResults;
        }
        finally {
            // 如果超时只记录第一条sql
            processOvertime(startTime, sql, (Object[]) listOfArgs.get(0));
        }
    }

    /**
     * 统计记录条数.
     * 
     * @param sql
     *            sql语句(如：SELECT * FROM a),自动替换成(SELECT COUNT(1) FROM a)
     * @return 记录条数
     */
    protected int count(String sql) {
        return count(sql, null);
    }

    /**
     * 统计记录条数
     * 
     * @param sql
     *            sql语句
     * @param args
     *            参数数组(全部不能等于null)
     * @return 记录条数
     */
    protected int count(String sql, Object[] args) {
        String lowCaseSql = sql.toLowerCase();
        int indexOfUnion = lowCaseSql.indexOf(" union ");
        if (indexOfUnion == -1) {
            int countValue = queryForInt(JdbcUtils.getCountSQL(sql), args);

            int indexOfTop = lowCaseSql.indexOf(" top ");
            if (indexOfTop == -1) {
                return countValue;
            }
            else {
                int indexOfBlank = lowCaseSql.indexOf(" ", indexOfTop + " top ".length());
                String topLimitString = lowCaseSql.substring(indexOfTop + " top ".length(),
                        indexOfBlank);
                int topLimit = Integer.parseInt(topLimitString);
                return countValue > topLimit ? topLimit : countValue;
            }
        }

        // 处理带union的SQL的结果数量统计, 只支持单个union
        // 使用union很可能会影响执行效率, 应该避免!
        String sqlA = sql.substring(0, indexOfUnion);
        String sqlB = sql.substring(indexOfUnion + " union ".length());
        Object[] argsOfA = null;
        Object[] argsOfB = null;
        if (!ArrayUtils.isEmpty(args)) {
            final String mark = "?";
            int argCountOfA = StringUtils.countMatches(sqlA, mark);
            int argCountOfB = StringUtils.countMatches(sqlB, mark);
            argsOfA = new Object[argCountOfA];
            argsOfB = new Object[argCountOfB];
            for (int i = 0; i < argCountOfA; i++) {
                argsOfA[i] = args[i];
            }
            for (int i = 0; i < argCountOfB; i++) {
                argsOfB[i] = args[argCountOfA + i];
            }
        }
        int amount = queryForInt(JdbcUtils.getCountSQL(sqlA), argsOfA);
        amount += queryForInt(JdbcUtils.getCountSQL(sqlB), argsOfB);
        return amount;
    }

    /**
     * 获取结果对象列表.
     * 
     * @param sql
     *            sql语句(不包含动态参数?)
     * @param multiRowMapper
     *            处理多行结果集的接口
     * @return 结果对象列表
     */
    protected <T> List<T> query(String sql, MultiRowMapper<T> multiRowMapper) {
        if (logger.isDebugEnabled()) {
            logger.debug(sql);
        }
        long startTime = System.currentTimeMillis();

        try {
            List<T> list = (List<T>) getJdbcTemplate().query(sql,
                    new MultiResultSetExtractor<T>(multiRowMapper));
            return list;
        }
        finally {
            processOvertime(startTime, sql, null);
        }
    }

    /**
     * 通过分页的方式获取多行结果集.
     * 
     * @param sql
     *            sql语句(不包含动态参数?)
     * @param multiRowMapper
     *            处理多行结果集的接口
     * @param page
     *            分页对象
     * @return 结果对象列表
     */
    protected <T> List<T> query(String sql, MultiRowMapper<T> multiRowMapper, Pagination page) {
        return query(sql, null, null, multiRowMapper, page);
    }

    /**
     * @param prefix
     *            In查询SQL语句前部分 <br>
     *            eg:SELECT * FROM dt_teacher WHERE c_schid = ? AND c_teachid IN
     * @param prefixArgs
     *            In查询SQL语句前部分的参数 <br>
     *            eg:new Object[] { schoolId }
     * @param inArgs
     *            In参数 <br>
     *            eg:teacherIds
     * @param postfix
     *            sql的最后意部份 <br>
     *            eg:ORDER BY c_stuid
     * @param multiRowMapper
     * @return List
     */
    protected <T> List<T> queryForInSQL(String prefix, Object[] prefixArgs, Object[] inArgs,
            MultiRowMapper<T> multiRowMapper, String postfix, Pagination page) {
        List<T> result = new ArrayList<T>();
        if (inArgs == null || inArgs.length == 0) {
            return result;
        }

        return queryForInSQL(prefix, prefixArgs, inArgs, multiRowMapper, postfix, result, page);
    }

    /**
     * @param prefix
     *            In查询SQL语句前部分 <br>
     *            eg:SELECT * FROM dt_teacher WHERE c_schid = ? AND c_teachid IN
     * @param prefixArgs
     *            In查询SQL语句前部分的参数 <br>
     *            eg:new Object[] { schoolId }
     * @param inArgs
     *            In参数 <br>
     *            eg:teacherIds
     * @param postfix
     *            sql的最后意部份 <br>
     *            eg:ORDER BY c_stuid
     * @param MapRowMapper
     * @return List
     */
    protected <K, T> Map<K, T> queryForInSQL(String prefix, Object[] prefixArgs, Object[] inArgs,
            MapRowMapper<K, T> rowMapper, String postfix, Pagination page) {
        Map<K, T> result = new HashMap<K, T>();
        if (inArgs == null || inArgs.length == 0) {
            return result;
        }

        return queryForInSQL(prefix, prefixArgs, inArgs, rowMapper, postfix, result, page);
    }

    private <T> List<T> queryForInSQL(String prefix, Object[] prefixArgs, Object[] inArgs,
            MultiRowMapper<T> multiRowMapper, String postfix, List<T> result, Pagination page) {
        if (ArrayUtils.isEmpty(inArgs))
            return new ArrayList<T>();
        if (prefixArgs == null) {
            prefixArgs = new Object[0];
        }
        int count = maxInSQLParamCount;
        for (int i = 0, length = inArgs.length; i < length; i += count) {

            if (i + maxInSQLParamCount > length) {
                count = length - i;
            }

            String sql = prefix + JdbcUtils.getInSQL(count);
            if (!Validators.isEmpty(postfix)) {
                sql += postfix;
            }
            Object[] args = new Object[count + prefixArgs.length];
            for (int _i = 0, _length = prefixArgs.length; _i < _length; _i++) {
                args[_i] = prefixArgs[_i];
            }

            System.arraycopy(inArgs, i, args, prefixArgs.length, count);
            result.addAll(query(sql, args, null, multiRowMapper, page));
        }
        return result;
    }

    private <K, T> Map<K, T> queryForInSQL(String prefix, Object[] prefixArgs, Object[] inArgs,
            MapRowMapper<K, T> rowMapper, String postfix, Map<K, T> result, Pagination page) {
        if (prefixArgs == null) {
            prefixArgs = new Object[0];
        }

        int count = maxInSQLParamCount;
        for (int i = 0, length = inArgs.length; i < length; i += count) {

            if (i + maxInSQLParamCount > length) {
                count = length - i;
            }

            String sql = prefix + JdbcUtils.getInSQL(count);
            if (!Validators.isEmpty(postfix)) {
                sql += postfix;
            }
            Object[] args = new Object[count + prefixArgs.length];
            for (int _i = 0, _length = prefixArgs.length; _i < _length; _i++) {
                args[_i] = prefixArgs[_i];
            }
            System.arraycopy(inArgs, i, args, prefixArgs.length, count);
            result.putAll(queryForMap(sql, args, null, rowMapper, page));
        }

        return result;
    }

    /**
     * 获取结果对象列表.
     * 
     * @param sql
     *            sql语句
     * @param args
     *            参数数组
     * @param argTypes
     *            参数类型数组
     * @param multiRowMapper
     *            处理多行结果集的接口
     * @return 结果对象列表
     */
    protected <T> List<T> query(String sql, Object[] args, int[] argTypes,
            MultiRowMapper<T> multiRowMapper) {
        if (logger.isDebugEnabled()) {
            logger.debug(JdbcUtils.getSQL(sql, args));
        }
        long startTime = System.currentTimeMillis();

        try {
            List<T> list = (List<T>) getJdbcTemplate().query(sql, args, argTypes,
                    new MultiResultSetExtractor<T>(multiRowMapper));
            return list;
        }
        finally {
            processOvertime(startTime, sql, args);
        }
    }

    /**
     * 通过分页的方式获取结果对象列表.
     * 
     * @param sql
     *            sql语句
     * @param args
     *            参数数组(如：new Object[] {id, name})
     * @param argTypes
     *            参数类型数组(如：new int[] {Types.VARCHAR, Types.INTEGER})，长度必须与参数数组一致
     * @param multiRowMapper
     *            处理多行结果集的接口(注意: 实现中不必调用rs.next())
     * @param page
     *            分页对象
     * @return 结果对象列表
     */
    protected <T> List<T> query(final String sql, final Object[] args, final int[] argTypes,
            final MultiRowMapper<T> multiRowMapper, final Pagination page) {
        return queryForPage(sql, args, argTypes, new ReturnResult<List<T>>() {

            @Override
            public void fillResult(List<T> results, ResultSet rs, int rowNum) throws SQLException {
                results.add(multiRowMapper.mapRow(rs, rowNum));
            }

            @Override
            public List<T> getResult() {
                return new ArrayList<T>();
            }
        }, page);
    }

    private <T> T queryForPage(final String sql, final Object[] args, final int[] argTypes,
            final ReturnResult<T> returnResult, final Pagination page) {
        if (page == null) {
            throw new IllegalArgumentException("the parameter: page cannot be null.");
        }

        if (logger.isDebugEnabled()) {
            logger.debug(JdbcUtils.getSQL(sql, args));
        }

        final boolean isUsedCursor = page.isUseCursor();
        if (!isUsedCursor && page.isUseDBPage()) {
            return queryForOracle(sql, args, argTypes, returnResult, page);
        }

        long startTime = System.currentTimeMillis();

        try {

            PreparedStatementCreator psCreator = new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con)
                        throws SQLException {
                    if (isUsedCursor) {
                        return con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        // ResultSet.TYPE_SCROLL_SENSITIVE,
                                ResultSet.CONCUR_READ_ONLY);
                    }

                    return con.prepareStatement(sql);
                }

            };

            PreparedStatementCallback<T> psCallback = new PreparedStatementCallback<T>() {

                public T doInPreparedStatement(PreparedStatement ps) throws SQLException,
                        DataAccessException {
                    // Set parameters to statement
                    if (args == null) {
                        ; // Do not set parameters
                    }
                    else if (argTypes != null) {
                        JdbcUtils.setSuitedParamsToStatement(args, argTypes, ps);
                    }
                    else {
                        JdbcUtils.setParamsToStatement(args, ps);
                    }

                    ResultSet rs = null;

                    try {
                        rs = ps.executeQuery();

                        if (isUsedCursor) { // Use cursor for paging
                            rs.last();
                            page.setMaxRowCount(rs.getRow());
                        }
                        else { // Not use cursor for paging
                            page.setMaxRowCount(count(sql, args));
                        }

                        page.initialize(); // Initialize the pagination

                        // Get records of current page

                        T results = returnResult.getResult();
                        if (page.getMaxRowCount() > 0) {
                            if (isUsedCursor) {
                                // If current row is first one, move cursor
                                // before
                                // it
                                if (page.getCurRowNum() == 1) {
                                    rs.beforeFirst();
                                }
                                else {
                                    rs.absolute(page.getCurRowNum() - 1);
                                }
                            }
                            else {
                                // If current row is not before the current page
                                // index,
                                // move cursor to next row
                                while (rs.getRow() != (page.getCurRowNum() - 1)) {
                                    rs.next();
                                }
                            }

                            // Add value objects into list
                            int cursor = 0;
                            while (rs.next() && cursor++ < page.getPageSize()) {
                                returnResult.fillResult(results, rs, cursor);
                            }
                        }

                        return results;
                    }
                    finally {
                        org.springframework.jdbc.support.JdbcUtils.closeResultSet(rs);
                    }
                }
            };
            T t = (T) getJdbcTemplate().execute(psCreator, psCallback);
            return t;
        }
        finally {
            processOvertime(startTime, sql, args);
        }
    }

    private interface ReturnResult<T> {
        public T getResult();

        public void fillResult(T results, ResultSet rs, int rowNum) throws SQLException;
    }

    // 2010-09-28 用数据库的分页代替原默认分页，以提高性能------------------------
    /**
     * oracle下的分页查询，对order by只做简单的判断处理，
     * 
     * @param oldSql
     * @return
     */
    private String buildOraclePageSql(String sql) {
        StringBuilder sb = new StringBuilder();
        String s = null;
        boolean contail = SQLParser.isContailClause(sql, "order");
        int index = StringUtils.lastIndexOfIgnoreCase(sql, " order ");;
        int index_from = StringUtils.lastIndexOfIgnoreCase(sql, " from ");
        if (contail && index > index_from) {
            s = sql + " ,rownum ";
        }
        else {
            s = sql + " order by rownum ";
        }

        sb.append("select * from ( ");
        sb.append("select a.*,rownum rn  from (");
        sb.append(s);
        sb.append(") a where rownum < ? ");
        sb.append(") where rn >= ? ");
        if (logger.isDebugEnabled()) {
            logger.debug(sb.toString());
        }

        return sb.toString();
    }

    /**
     * @param sql
     * @param args
     * @param argTypes
     * @param multiRowMapper
     * @param page
     * @return
     */
    private <T> T queryForOracle(final String sql, final Object[] args, final int[] argTypes,
            final ReturnResult<T> returnResult, final Pagination page) {
        long startTime = System.currentTimeMillis();

        try {

            PreparedStatementCreator psCreator = new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con)
                        throws SQLException {
                    return con.prepareStatement(buildOraclePageSql(sql));
                }

            };

            PreparedStatementCallback<T> psCallback = new PreparedStatementCallback<T>() {

                public T doInPreparedStatement(PreparedStatement ps) throws SQLException,
                        DataAccessException {
                    page.setMaxRowCount(count(sql, args));
                    page.initialize(); // Initialize the pagination

                    // Set parameters to statement
                    // 设置分页参数
                    Object[] destArgs = null;
                    int[] destTypes = null;
                    if (args != null) {
                        int len = args.length;
                        destArgs = new Object[len + 2];
                        System.arraycopy(args, 0, destArgs, 0, len);
                        destArgs[len] = Integer.valueOf(page.getCurRowNum() + page.getPageSize());
                        destArgs[len + 1] = Integer.valueOf(page.getCurRowNum());
                    }

                    if (argTypes != null) {
                        int len = argTypes.length;
                        destTypes = new int[len + 2];
                        System.arraycopy(argTypes, 0, destTypes, 0, len);
                        destTypes[len] = Types.INTEGER;
                        destTypes[len + 1] = Types.INTEGER;
                    }

                    if (args == null) {
                        JdbcUtils.setSuitedParamsToStatement(new Object[] {
                                page.getCurRowNum() + page.getPageSize(), page.getCurRowNum() },
                                new int[] { Types.INTEGER, Types.INTEGER }, ps);
                    }
                    else if (argTypes != null) {
                        JdbcUtils.setSuitedParamsToStatement(destArgs, destTypes, ps);
                    }
                    else {
                        JdbcUtils.setParamsToStatement(destArgs, ps);
                    }

                    ResultSet rs = null;

                    try {
                        rs = ps.executeQuery();

                        // Get records of current page
                        T results = returnResult.getResult();
                        if (page.getMaxRowCount() > 0) {
                            // Add value objects into list
                            int cursor = 0;
                            while (rs.next()) {
                                cursor++;
                                returnResult.fillResult(results, rs, cursor);

                            }
                        }

                        return results;
                    }
                    finally {
                        org.springframework.jdbc.support.JdbcUtils.closeResultSet(rs);
                    }
                }
            };

            T t = (T) getJdbcTemplate().execute(psCreator, psCallback);
            return t;
        }
        finally {
            processOvertime(startTime, sql, args);
        }
    }

    // -----------------------------------------------------------------

    /**
     * 获取单行结果集对应的对象.
     * 
     * @param sql
     *            sql语句
     * @param args
     *            参数数组
     * @param argTypes
     *            参数类型数组
     * @param singleRowMapper
     *            处理单行结果集的接口，把数据库记录转换成对象
     * @return 结果对象
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected <T> T query(String sql, Object[] args, int[] argTypes,
            SingleRowMapper<T> singleRowMapper) {
        if (logger.isDebugEnabled()) {
            logger.debug(JdbcUtils.getSQL(sql, args));
        }
        long startTime = System.currentTimeMillis();

        try {
            T t = (T) getJdbcTemplate().query(sql, args, argTypes,
                    new SingleResultSetExtractor(singleRowMapper));
            return t;
        }
        finally {
            processOvertime(startTime, sql, args);
        }
    }

    /**
     * 获取结果对象列表.
     * 
     * @param sql
     *            sql语句
     * @param args
     *            参数数组(全部不能等于null)
     * @param multiRowMapper
     *            处理多行结果集的接口
     * @return 结果对象列表
     */
    protected <T> List<T> query(String sql, Object[] args, MultiRowMapper<T> multiRowMapper) {
        if (logger.isDebugEnabled()) {
            logger.debug(JdbcUtils.getSQL(sql, args));
        }
        long startTime = System.currentTimeMillis();

        try {
            List<T> list = (List<T>) getJdbcTemplate().query(sql, args,
                    new MultiResultSetExtractor<T>(multiRowMapper));
            return list;
        }
        finally {
            processOvertime(startTime, sql, args);
        }
    }

    /**
     * 通过分页的方式获取多行结果集.
     * 
     * @param sql
     *            sql语句
     * @param args
     *            参数数组(全部不能等于null)
     * @param multiRowMapper
     *            处理多行结果集的接口
     * @param page
     *            分页对象
     * @return 结果对象列表
     */
    protected <T> List<T> query(String sql, Object[] args, MultiRowMapper<T> multiRowMapper,
            Pagination page) {
        return query(sql, args, null, multiRowMapper, page);
    }

    /**
     * 获取单行结果集对应的对象.
     * 
     * @param sql
     *            sql语句
     * @param args
     *            参数数组(全部不能等于null)
     * @param singleRowMapper
     *            处理单行结果集的接口
     * @return 结果对象
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected <T> T query(String sql, Object[] args, SingleRowMapper<T> singleRowMapper) {
        if (logger.isDebugEnabled()) {
            logger.debug(JdbcUtils.getSQL(sql, args));
        }
        long startTime = System.currentTimeMillis();

        try {
            T t = (T) getJdbcTemplate().query(sql, args,
                    new SingleResultSetExtractor(singleRowMapper));
            return t;
        }
        finally {
            processOvertime(startTime, sql, args);
        }
    }

    /**
     * 获取单行结果集对应的对象.
     * 
     * @param sql
     *            sql语句(不包含动态参数?)
     * @param singleRowMapper
     *            处理单行结果集的接口
     * @return 结果对象
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected <T> T query(String sql, SingleRowMapper<T> singleRowMapper) {
        if (logger.isDebugEnabled()) {
            logger.debug(sql);
        }
        long startTime = System.currentTimeMillis();

        try {
            T t = (T) getJdbcTemplate().query(sql, new SingleResultSetExtractor(singleRowMapper));
            return t;
        }
        finally {
            processOvertime(startTime, sql, null);
        }
    }

    /**
     * 获取结果对象列表.
     * 
     * @param sql
     *            sql语句
     * @param arg
     *            单个参数(不能等于null)
     * @param multiRowMapper
     *            处理多行结果集的接口
     * @return 结果对象列表
     */
    protected <T> List<T> query(String sql, String arg, MultiRowMapper<T> multiRowMapper) {
        if (logger.isDebugEnabled()) {
            logger.debug(JdbcUtils.getSQL(sql, new Object[] { arg }));
        }
        long startTime = System.currentTimeMillis();

        try {
            List<T> list = (List<T>) getJdbcTemplate().query(sql, new Object[] { arg },
                    new MultiResultSetExtractor<T>(multiRowMapper));
            return list;
        }
        finally {
            processOvertime(startTime, sql, new Object[] { arg });
        }
    }

    /**
     * 通过分页的方式获取多行结果集.
     * 
     * @param sql
     *            sql语句
     * @param arg
     *            单个参数(不能等于null)
     * @param multiRowMapper
     *            处理多行结果集的接口
     * @param page
     *            分页对象
     * @return 结果对象列表
     */
    protected <T> List<T> query(String sql, String arg, MultiRowMapper<T> multiRowMapper,
            Pagination page) {
        return query(sql, new Object[] { arg }, null, multiRowMapper, page);
    }

    /**
     * 获取单行结果集对应的对象.
     * 
     * @param sql
     *            sql语句
     * @param arg
     *            单个参数(不能等于null)
     * @param singleRowMapper
     *            处理单行结果集的接口
     * @return 结果对象
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected <T> T query(String sql, String arg, SingleRowMapper<T> singleRowMapper) {
        if (logger.isDebugEnabled()) {
            logger.debug(JdbcUtils.getSQL(sql, new Object[] { arg }));
        }
        long startTime = System.currentTimeMillis();

        try {
            T t = (T) getJdbcTemplate().query(sql, new Object[] { arg },
                    new SingleResultSetExtractor(singleRowMapper));
            return t;
        }
        finally {
            processOvertime(startTime, sql, new Object[] { arg });
        }
    }

    /**
     * @param prefix
     *            In查询SQL语句前部分 <br>
     *            eg:SELECT * FROM dt_teacher WHERE c_schid = ? AND c_teachid IN
     * @param prefixArgs
     *            In查询SQL语句前部分的参数 <br>
     *            eg:new Object[] { schoolId }
     * @param inArgs
     *            In参数 <br>
     *            eg:teacherIds
     * @param mapRowMapper
     * @return Map
     */
    protected <K, V> Map<K, V> queryForInSQL(String prefix, Object[] prefixArgs, Object[] inArgs,
            MapRowMapper<K, V> mapRowMapper) {
        return queryForInSQL(prefix, prefixArgs, inArgs, mapRowMapper, null);
    }

    /**
     * @param prefix
     *            In查询SQL语句前部分 <br>
     *            eg:SELECT * FROM dt_teacher WHERE c_schid = ? AND c_teachid IN
     * @param prefixArgs
     *            In查询SQL语句前部分的参数 <br>
     *            eg:new Object[] { schoolId }
     * @param inArgs
     *            In参数 <br>
     *            eg:teacherIds
     * @param postfix
     *            sql的最后意部份 <br>
     *            eg:ORDER BY c_stuid
     * @param mapRowMapper
     * @return Map
     */
    protected <K, V> Map<K, V> queryForInSQL(String prefix, Object[] prefixArgs, Object[] inArgs,
            MapRowMapper<K, V> mapRowMapper, String postfix) {
        Map<K, V> result = new HashMap<K, V>();
        if (inArgs == null || inArgs.length == 0) {
            return result;
        }

        queryForInSQL(prefix, prefixArgs, inArgs, mapRowMapper, postfix, result);

        return result;
    }

    /**
     * @param prefix
     *            In查询SQL语句前部分 <br>
     *            eg:SELECT * FROM dt_teacher WHERE c_schid = ? AND c_teachid IN
     * @param prefixArgs
     *            In查询SQL语句前部分的参数 <br>
     *            eg:new Object[] { schoolId }
     * @param inArgs
     *            In参数 <br>
     *            eg:teacherIds
     * @param multiRowMapper
     * @return List
     */
    protected <T> List<T> queryForInSQL(String prefix, Object[] prefixArgs, Object[] inArgs,
            MultiRowMapper<T> multiRowMapper) {
        return queryForInSQL(prefix, prefixArgs, inArgs, multiRowMapper, null);

    }

    /**
     * @param prefix
     *            In查询SQL语句前部分 <br>
     *            eg:SELECT * FROM dt_teacher WHERE c_schid = ? AND c_teachid IN
     * @param prefixArgs
     *            In查询SQL语句前部分的参数 <br>
     *            eg:new Object[] { schoolId }
     * @param inArgs
     *            In参数 <br>
     *            eg:teacherIds
     * @param postfix
     *            sql的最后意部份 <br>
     *            eg:ORDER BY c_stuid
     * @param multiRowMapper
     * @return List
     */
    protected <T> List<T> queryForInSQL(String prefix, Object[] prefixArgs, Object[] inArgs,
            MultiRowMapper<T> multiRowMapper, String postfix) {
        List<T> result = new ArrayList<T>();
        if (inArgs == null || inArgs.length == 0) {
            return result;
        }

        queryForInSQL(prefix, prefixArgs, inArgs, multiRowMapper, postfix, result);
        return result;
    }

    private void queryForInSQL(String prefix, Object[] prefixArgs, Object[] inArgs,
            Object rowMapper, String postfix, Object result) {
        if (prefixArgs == null) {
            prefixArgs = new Object[0];
        }

        int count = maxInSQLParamCount;
        for (int i = 0, length = inArgs.length; i < length; i += count) {

            if (i + maxInSQLParamCount > length) {
                count = length - i;
            }

            String sql = prefix + JdbcUtils.getInSQL(count);
            if (!Validators.isEmpty(postfix)) {
                sql += postfix;
            }
            Object[] args = new Object[count + prefixArgs.length];
            for (int _i = 0, _length = prefixArgs.length; _i < _length; _i++) {
                args[_i] = prefixArgs[_i];
            }

            System.arraycopy(inArgs, i, args, prefixArgs.length, count);

            fillResultForInSQL(sql, args, rowMapper, result);
        }
    }

    /**
     * @param prefix
     *            In查询SQL语句前部分 <br>
     *            eg:SELECT * FROM dt_teacher WHERE c_schid = ? AND c_teachid IN
     * @param prefixArgs
     *            In查询SQL语句前部分的参数 <br>
     *            eg:new Object[] { schoolId }
     * @param inArgs
     *            In参数 <br>
     *            eg:teacherIds
     * @return 结果
     */
    protected int queryForIntInSQL(String prefix, Object[] prefixArgs, Object[] inArgs) {
        if (prefixArgs == null) {
            prefixArgs = new Object[0];
        }

        int resultInt = 0;
        int count = maxInSQLParamCount;
        for (int i = 0, length = inArgs.length; i < length; i += count) {

            if (i + maxInSQLParamCount > length) {
                count = length - i;
            }

            String sql = prefix + JdbcUtils.getInSQL(count);
            Object[] args = new Object[count + prefixArgs.length];
            for (int _i = 0, _length = prefixArgs.length; _i < _length; _i++) {
                args[_i] = prefixArgs[_i];
            }

            System.arraycopy(inArgs, i, args, prefixArgs.length, count);
            resultInt += fillResultForIntInSQL(sql, args);
        }
        return resultInt;
    }

    private int fillResultForIntInSQL(String sql, Object[] args) {
        return queryForInt(sql, args);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void fillResultForInSQL(String sql, Object[] args, Object rowMapper, Object result) {
        if (rowMapper instanceof MapRowMapper) {
            ((Map) result).putAll(queryForMap(sql, args, (MapRowMapper) rowMapper));
        }
        else if (rowMapper instanceof MultiRowMapper) {
            ((List) result).addAll(query(sql, args, (MultiRowMapper) rowMapper));
        }
    }

    /**
     * 获取单条记录的整型字段，当不存在记录时会抛出异常
     * 
     * @param sql
     *            sql语句
     * @return 单条记录的整型字段值
     */
    protected int queryForInt(String sql) {
        return queryForInt(sql, new Object[] {});
    }

    /**
     * 获取单条记录的整型字段，当不存在记录时会抛出异常
     * 
     * @param sql
     *            sql语句
     * @param args
     *            参数数组(全部不能等于null)
     * @return 单条记录的整型字段值
     */
    protected int queryForInt(String sql, Object... args) {
        if (logger.isDebugEnabled()) {
            logger.debug(JdbcUtils.getSQL(sql, args));
        }
        long startTime = System.currentTimeMillis();

        try {
            return args == null || args.length == 0 ? getJdbcTemplate().queryForObject(sql, Integer.class)
                    : getJdbcTemplate().queryForObject(sql, args, Integer.class);
        }
        finally {
            processOvertime(startTime, sql, args);
        }
    }

    /**
     * 获取单条记录的长整型字段，当不存在记录时会抛出异常.
     * 
     * @param sql
     *            sql语句
     * @return 单条记录的长整型字段值
     */
    protected long queryForLong(String sql) {
        return queryForLong(sql, null);
    }

    /**
     * 获取单条记录的长整型字段，当不存在记录时会抛出异常.
     * 
     * @param sql
     *            sql语句
     * @param args
     *            参数数组(全部不能等于null)
     * @return 单条记录的长整型字段值
     */
    protected long queryForLong(String sql, Object[] args) {
        if (logger.isDebugEnabled()) {
            logger.debug(JdbcUtils.getSQL(sql, args));
        }
        long startTime = System.currentTimeMillis();

        try {
            return args == null ? getJdbcTemplate().queryForObject(sql, Long.class)
                    : getJdbcTemplate().queryForObject(sql, args, Long.class);
        }
        finally {
            processOvertime(startTime, sql, args);
        }
    }

    /**
     * 获取结果集的map
     * 
     * @param sql
     *            sql语句
     * @param mapRowMapper
     *            处理多行记录集的接口,指定map的key和value
     * @return 结果对象的map
     */
    protected <K, V> Map<K, V> queryForMap(String sql, MapRowMapper<K, V> mapRowMapper) {
        return queryForMap(sql, null, null, mapRowMapper);
    }

    /**
     * 获取结果集的map
     * 
     * @param sql
     *            sql语句
     * @param args
     *            参数数组
     * @param argTypes
     *            参数类型数组
     * @param mapRowMapper
     *            处理多行记录集的接口，指定map的key和value
     * @return 结果对象的map
     */
    protected <K, V> Map<K, V> queryForMap(String sql, Object[] args, int[] argTypes,
            MapRowMapper<K, V> mapRowMapper) {
        if (logger.isDebugEnabled()) {
            logger.debug(JdbcUtils.getSQL(sql, args));
        }
        long startTime = System.currentTimeMillis();

        try {
            if (args == null && argTypes == null) {
                Map<K, V> map = (Map<K, V>) getJdbcTemplate().query(sql,
                        new MapResultExtractor<K, V>(mapRowMapper));
                return map;
            }

            if (argTypes == null) {
                Map<K, V> map = (Map<K, V>) getJdbcTemplate().query(sql, args,
                        new MapResultExtractor<K, V>(mapRowMapper));
                return map;
            }

            Map<K, V> map = (Map<K, V>) getJdbcTemplate().query(sql, args, argTypes,
                    new MapResultExtractor<K, V>(mapRowMapper));
            return map;
        }
        finally {
            processOvertime(startTime, sql, args);
        }
    }

    /**
     * 通过分页的方式获取结果集的map
     * 
     * @param sql
     *            sql语句
     * @param args
     *            参数
     * @param argTypes
     *            参数类型
     * @param mapRowMapper
     *            处理每行结果集的接口, 指定map的key和value <br>
     *            (注意: 实现中不必调用rs.next())
     * @param page
     *            分页对象
     * @return 结果对象的map
     */
    protected <K, V> Map<K, V> queryForMap(final String sql, final Object[] args,
            final int[] argTypes, final MapRowMapper<K, V> mapRowMapper, final Pagination page) {
        return queryForPage(sql, args, argTypes, new ReturnResult<Map<K, V>>() {

            @Override
            public void fillResult(Map<K, V> results, ResultSet rs, int rowNum) throws SQLException {
                results.put(mapRowMapper.mapRowKey(rs, rowNum),
                        mapRowMapper.mapRowValue(rs, rowNum));
            }

            @Override
            public Map<K, V> getResult() {
                return new HashMap<K, V>();
            }
        }, page);
    }

    /**
     * 获取结果集的map
     * 
     * @param sql
     *            sql语句
     * @param args
     *            参数数组(全部不能等于null)
     * @param mapRowMapper
     *            处理多行记录集的接口,指定map的key和value
     * @return 结果对象的map
     */
    protected <K, V> Map<K, V> queryForMap(String sql, Object[] args,
            MapRowMapper<K, V> mapRowMapper) {
        return queryForMap(sql, args, null, mapRowMapper);
    }

    /**
     * 获取单条记录的字符型字段，该字段值不允许null，当不存在记录时返回null
     * 
     * @param sql
     *            sql语句
     * @param args
     *            参数数组(全部不能等于null)
     * @return 单条记录的字符型字段值
     */
    protected String queryForNotNullString(String sql, Object[] args) {
        return (String) query(sql, args, new SingleRowMapper<String>() {
            public String mapRow(ResultSet rs) throws SQLException {
                return rs.getString(1);
            }
        });
    }

    /**
     * 获取单条记录的字符型字段，当不存在记录时会抛出异常
     * 
     * @param sql
     *            sql语句
     * @param args
     *            参数数组(全部不能等于null)
     * @return 单条记录的字符型字段值
     */
    protected String queryForString(String sql, Object[] args) {
        if (logger.isDebugEnabled()) {
            logger.debug(JdbcUtils.getSQL(sql, args));
        }
        long startTime = System.currentTimeMillis();

        try {
            return (String) getJdbcTemplate().queryForObject(sql, args, String.class);
        }
        finally {
            processOvertime(startTime, sql, args);
        }
    }

    /**
     * 获取结果对象列表, 数量不超过topLimit.
     * 
     * @param sql
     *            sql语句
     * @param args
     *            参数数组
     * @param argTypes
     *            参数类型数组
     * @param multiRowMapper
     *            处理多行结果集的接口
     * @param topLimit
     *            数量限制
     * @return 结果对象列表
     */
    protected <T> List<T> queryForTop(String sql, Object[] args, int[] argTypes,
			MultiRowMapper<T> multiRowMapper, int topLimit) {
		if (logger.isDebugEnabled()) {
			logger.debug(JdbcUtils.getSQL(sql, args));
		}
		long startTime = System.currentTimeMillis();

		try {
			if (argTypes == null)
				return (List<T>) getJdbcTemplate().query(sql, args,
						new MultiTopResultSetExtractor<T>(multiRowMapper, topLimit));
			else
				return (List<T>) getJdbcTemplate().query(sql, args, argTypes,
						new MultiTopResultSetExtractor<T>(multiRowMapper, topLimit));
		} finally {
			processOvertime(startTime, sql, args);
		}
	}

    /**
     * 更新数据.
     * 
     * @param sql
     *            sql语句
     * @return 更新的记录条数
     */
    protected int update(String sql) {
        return update(sql, null, null);
    }

    /**
     * 更新数据.
     * 
     * @param sql
     *            sql语句
     * @param args
     *            参数数组(全部不能等于null)
     * @return 更新的记录条数
     */
    protected int update(String sql, Object[] args) {
        return update(sql, args, null);
    }

    /**
     * 更新数据.
     * 
     * @param sql
     *            sql语句
     * @param args
     *            参数数组
     * @param argTypes
     *            参数类型数组
     * @return 更新的记录条数
     */
    protected int update(String sql, Object[] args, int[] argTypes) {
        if (logger.isDebugEnabled()) {
            logger.debug(JdbcUtils.getSQL(sql, args));
        }
        long startTime = System.currentTimeMillis();

        try {
            if (args == null && argTypes == null) {
                return getJdbcTemplate().update(sql);
            }

            if (argTypes == null) {
                return getJdbcTemplate().update(sql, args);
            }

            return getJdbcTemplate().update(sql, args, argTypes);
        }
        finally {
            processOvertime(startTime, sql, args);
        }
    }

    /**
     * 更新数据.
     * 
     * @param sql
     *            sql语句
     * @param arg
     *            参数
     * @return 更新的记录条数
     */
    protected int update(String sql, String arg) {
        return update(sql, new Object[] { arg }, null);
    }

    /**
     * 含in参数的更新.
     * 
     * @param prefix
     *            In查询SQL语句前部分 <br>
     *            eg:UPDATE dt_teacher WHERE c_schid = ? AND c_teachid IN
     * @param prefixArgs
     *            In查询SQL语句前部分的参数 <br>
     *            eg:new Object[] { schoolId }
     * @param inArgs
     *            In参数 <br>
     *            eg:teacherIds
     * @return
     */
    protected int updateForInSQL(String prefix, Object[] prefixArgs, Object[] inArgs) {
        return updateForInSQL(prefix, prefixArgs, inArgs, null);
    }

    /**
     * 含in参数的更新.
     * 
     * @param prefix
     *            In查询SQL语句前部分 <br>
     *            eg:UPDATE dt_teacher WHERE c_schid = ? AND c_teachid IN
     * @param prefixArgs
     *            In查询SQL语句前部分的参数 <br>
     *            eg:new Object[] { schoolId }
     * @param inArgs
     *            In参数 <br>
     *            eg:teacherIds
     * @param postfix
     *            sql的最后意部份 <br>
     *            eg:))
     * @return
     */
    protected int updateForInSQL(String prefix, Object[] prefixArgs, Object[] inArgs, String postfix) {
        int result = 0;

        if (prefixArgs == null) {
            prefixArgs = new Object[0];
        }

        int count = maxInSQLParamCount;
        for (int i = 0, length = inArgs.length; i < length; i += count) {
            if (i + maxInSQLParamCount > length) {
                count = length - i;
            }

            String sql = prefix + JdbcUtils.getInSQL(count);
            if (!Validators.isEmpty(postfix)) {
                sql += postfix;
            }
            Object[] args = new Object[count + prefixArgs.length];
            for (int _i = 0, _length = prefixArgs.length; _i < _length; _i++) {
                args[_i] = prefixArgs[_i];
            }

            System.arraycopy(inArgs, i, args, prefixArgs.length, count);
            result += update(sql, args);
        }
        return result;
    }

    /**
     * 把处理多行记录集的接口转化为处理单行记录集的接口
     * 
     * @param multiRowMapper
     * @return
     */
    protected <T> SingleRowMapper<T> beSingle(final MultiRowMapper<T> multiRowMapper) {
        return new SingleRowMapper<T>() {

            public T mapRow(ResultSet rs) throws SQLException {
                return multiRowMapper.mapRow(rs, 0);
            }

        };
    }

    /**
     * 把MapRowMapper转化为处理单行记录集的接口
     * 
     * @param mapRowMapper
     * @return
     */
    protected <K, V> SingleRowMapper<V> beSingle(final MapRowMapper<K, V> mapRowMapper) {
        return new SingleRowMapper<V>() {

            public V mapRow(ResultSet rs) throws SQLException {
                return mapRowMapper.mapRowValue(rs, 0);
            }

        };
    }

    /**
     * 把MapRowMapper转化为处理多行记录集的接口
     * 
     * @param mapRowMapper
     * @return
     */
    protected <K, V> MultiRowMapper<V> beMulti(final MapRowMapper<K, V> mapRowMapper) {
        return new MultiRowMapper<V>() {

            public V mapRow(ResultSet rs, int rowNum) throws SQLException {
                return mapRowMapper.mapRowValue(rs, rowNum);
            }

        };
    }

    /**
     * 处理执行超时，日志记录
     * 
     * @param startTime
     * @param endTime
     * @param sql
     * @param args
     */
    private void processOvertime(long startTime, String sql, Object[] args) {
        long spendTime = System.currentTimeMillis() - startTime;
        if (spendTime > EXECUTE_OVERTIME) {
            sql = JdbcUtils.getSQL(sql, args);
            logger.error("Overtime " + spendTime + "ms " + sql);
        }
    }

    /**
     * 设置SQL批处理操作的记录数量.
     * 
     * @param batchSize
     *            批处理记录大小
     */
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    /**
     * 获取IN SQL中参数允许的最大数量.
     * 
     * @return IN SQL中参数允许的最大数量.
     */
    public int getMaxInSQLParamCount() {
        return maxInSQLParamCount;
    }

    /**
     * 设置IN SQL中参数允许的最大数量.
     * 
     * @param maxInSQLParamCount
     *            IN SQL中参数允许的最大数量
     */
    public void setMaxInSQLParamCount(int maxInSQLParamCount) {
        this.maxInSQLParamCount = maxInSQLParamCount;
    }

}
