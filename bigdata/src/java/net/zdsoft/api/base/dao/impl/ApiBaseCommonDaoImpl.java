package net.zdsoft.api.base.dao.impl;

import static net.zdsoft.bigdata.data.DatabaseType.MYSQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import net.zdsoft.api.base.dao.ApiBaseCommonDao;
import net.zdsoft.api.base.entity.eis.ApiDataSetRule;
import net.zdsoft.api.base.service.ApiDataSetRuleService;
import net.zdsoft.api.base.service.ApiDataSetService;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.entity.Database;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.manager.datasource.IDataSourceUtils;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.bigdata.frame.data.phoenix.PhoenixClientService;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.JdbcUtils;
import net.zdsoft.framework.utils.MultiRowMapper;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

@Repository
public class ApiBaseCommonDaoImpl extends BaseDao<Object> implements ApiBaseCommonDao {
	@Autowired
	private MetadataService metadataService;
	@Autowired
    private OptionService optionService;
	@Autowired
	private PhoenixClientService phoenixClientService;
	@Autowired
	private ApiDataSetService apiDataSetService;
	@Autowired
	private ApiDataSetRuleService apiDataSetRuleService;
	
    @Override
    public List<Map<String, Object>> getDataMapByParamMap(String physicalTableName, Map<String, String> paramMap, String metadataId) {
        if (StringUtils.isBlank(physicalTableName)) {
            return new ArrayList<Map<String, Object>>();
        }
        String sql = "SELECT * FROM " + physicalTableName;
        updateDataSource(metadataId);
        sql = updateParamMap(paramMap, sql);
		return dealDataMapByParam(paramMap, sql, physicalTableName);
    }

	@Override
	public int updateData(String sql, List<Object[]> insertObjList,int[] argTypes, String metadataId) {
    	updateDataSource(metadataId);
    	int[] updateNum = batchUpdate(sql,insertObjList,argTypes);
		return updateNum.length;
	}

	private void updateDataSource(String metadataId) {
		Metadata table = metadataService.findOne(metadataId);
		String type = table.getDbType();
    	// 查询表信息
        if ("mysql".equals(type)) {
            try {
                    Map<String, String> mysqlParamMap = getMysqlParamMap();
                    String url = new StringBuilder("jdbc:")
                            .append(MYSQL.getName())
                            .append("://")
                            .append(mysqlParamMap.get("domain"))
                            .append(":")
                            .append(mysqlParamMap.get("port")).append("/")
                            .append(mysqlParamMap.get("database"))
                            .append("?useUnicode=true&characterEncoding=")
                            .append(Database.DEFAULT_CHASET_ENCODING).toString();
                    DataSource dataSource = IDataSourceUtils.createNativeDataSource(mysqlParamMap.get("user"),
                            mysqlParamMap.get("password"), url, MYSQL);
                    setDataSource(dataSource);
            } catch (Exception e) {
            	e.printStackTrace();
            }
        } else if ("hbase".equals(type)) {
			try {
				setDataSource(phoenixClientService.getDataSource());
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    }
	
	//添加数据集
	private String updateParamMap(Map<String, String> paramMap,String sql) {
			// TODO Auto-generated method stub
		if(StringUtils.isNotBlank(paramMap.get("dataSetRule"))){
			ApiDataSetRule dataSetRule = apiDataSetRuleService.findOne(paramMap.get("dataSetRule"));
			paramMap.remove("dataSetRule");
			if(dataSetRule != null && StringUtils.isNotBlank(dataSetRule.getParamValue())){
				String value = dataSetRule.getParamValue();
				if(!value.contains("where")){
					sql += " where "; 
				}else{
					sql += " ";
				}
				sql += value;
			}
		}
		return sql;
    }
	
    
    private Map<String, String> getMysqlParamMap() throws BigDataBusinessException {
        OptionDto mysqlDto = optionService.getAllOptionParam("mysql");
        if (mysqlDto == null || mysqlDto.getStatus() == 0) {
            throw new BigDataBusinessException("mysql服务未启动");
        }
        return mysqlDto.getFrameParamMap();
    }
    
    
    
    /**
     * 得到排序参数，后面可以从表属性中得到这个字段
     * @return
     */
    private String getOrderBySqlString (){
          return  " ORDER BY sequence_int_id";
    }
    
    /**
     * 得到where 语句
     * @return
     */
    private Map<String, List<Object>> getWhereAndValuesMap (Map<String, String> paramMap){
    	//查询参数
    	List<Object> wheres = new ArrayList<Object>();
        List<Object> values = new ArrayList<Object>();
        //in的参数
        List<Object> inParam = new ArrayList<Object>();
        List<Object> inValues = new ArrayList<Object>();
    	if (paramMap == null) {
            paramMap = new HashMap<String, String>();
        }
    	for (String key : paramMap.keySet()) {
        	if(!StringUtils.startsWith(key, "_") && !StringUtils.startsWith(key, "in_")){
        		wheres.add(key + " = ? ");
        		values.add(paramMap.get(key));
        	}
        	//增加in的单位条件
        	if(StringUtils.startsWith(key, "in_")) {
        		inParam.add(key.substring(3) + " in ");
        		JSONArray jArray = JSON.parseArray(paramMap.get(key));
        		inValues.addAll(jArray.toJavaList(String.class));
        	}
        }
    	Map<String, List<Object>> whereMap = new HashMap<String, List<Object>>();
    	whereMap.put("where", wheres);
    	whereMap.put("wvalue", values);
    	whereMap.put("inParam", inParam);
    	whereMap.put("inValue", inValues);
    	return whereMap;
    }
    
    /**
     * 得到分页 Pagination
     * @param limit
     * @param page
     * @return
     */
    private Pagination getPagination(String limit,String page){
        if (NumberUtils.toInt(limit) > 0) {
        	return new Pagination(NumberUtils.toInt(page), NumberUtils.toInt(limit), false);
        }
        return null;
    }
    

	protected List<Map<String, Object>> dealDataMapByParam(Map<String, String> paramMap, final String sql,
            String physicalTableName) {
        if (paramMap == null) {
            paramMap = new HashMap<String, String>();
        }
        String limit = paramMap.get("_limit");
        String page = paramMap.get("_page");
        String modifyTime = paramMap.get("_dataModifyTime");
        String isDeleted = paramMap.get("_isDeleted");
        String sortId = paramMap.get("_dataSortMaxId");
        List<String> wheres = new ArrayList<String>();
        //查询参数
        List<Object> values = new ArrayList<Object>();
        //inArgs
        Set<String> inArgs = new HashSet<>();
        String inParam = "";
        
        if (StringUtils.isNotBlank(modifyTime)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                values.add(sdf.parse(modifyTime));
                wheres.add("modify_time > ? ");
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }

//        if (StringUtils.isBlank(isDeleted)) {
//            isDeleted = "0";
//        }
//
//        if (!StringUtils.equals("-1", isDeleted)) {
//            values.add(isDeleted);
//            wheres.add("is_deleted = ? ");
//        }

//        if (StringUtils.isNotBlank(sortId)) {
//            values.add(sortId);
//            wheres.add("sequence_int_id > ? ");
//        }

        for (String key : paramMap.keySet()) {
        	if(!StringUtils.startsWith(key, "_") && !StringUtils.startsWith(key, "in_")){
        		wheres.add(key + " = ? ");
        		values.add(paramMap.get(key));
        	}
        	//增加in的单位条件
        	if(StringUtils.startsWith(key, "in_")) {
        		inParam = key.substring(3) + " in ";
        		JSONArray jArray = JSON.parseArray(paramMap.get(key));
        		inArgs.addAll(jArray.toJavaList(String.class));
        	}
        }
        
        //判断是否需要in，放在最后面
        if(StringUtils.isNotBlank(inParam)) {
        	wheres.add(inParam);
        }
        
        String where = " " ;
		if(CollectionUtils.isNotEmpty(wheres)){
			if(!sql.contains("where")){
				where = " where " + StringUtils.join(wheres.toArray(new String[0]), " AND ");
			}else{
				where = " AND " + StringUtils.join(wheres.toArray(new String[0]), " AND ");
			}
		}
        String post = "";        
        //根据数据源来判断是否 加 排序
//        post = post + " ORDER BY sequence_int_id";
//    	if (NumberUtils.toInt(limit) > 0) {
//    		post = post + ", modify_time";
//    	}
        
        if (NumberUtils.toInt(limit) > 0) {
            Pagination pagination = new Pagination(NumberUtils.toInt(page), NumberUtils.toInt(limit), false);
            pagination.setUseDBPage(Boolean.FALSE);
            List<Map<String, Object>> data;
            if(CollectionUtils.isNotEmpty(inArgs)) {
            	String endSql = sql + where;
            	data = queryForInSQL(endSql, values.toArray(new Object[0]), inArgs.toArray(new String[0]), new MultiRowMapper<Map<String, Object>>() {
            		@Override
            		public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
            			return setFieldMapWithRs(rs, DigestUtils.md5Hex(sql));
            		}
            	}, post, pagination);
            }else {
            	data = query(sql + where + post ,
            			values.toArray(new Object[0]), new MultiRowMapper<Map<String, Object>>() {
            		@Override
            		public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
            			return setFieldMapWithRs(rs, DigestUtils.md5Hex(sql));
            		}
            	}, pagination);
            }
            Map<String, Object> pageMap = new HashMap<String, Object>();
            pageMap.put("_maxRow", "" + pagination.getMaxRowCount());
            pageMap.put("_maxPage", "" + pagination.getMaxPageIndex());
            pageMap.put("_page", "" + pagination.getPageIndex());
            pageMap.put("_limit", "" + pagination.getPageSize());
            data.add(pageMap);
            return data;
        }
        else {
        	 List<Map<String, Object>> data;
        	 if(CollectionUtils.isNotEmpty(inArgs)) {
        		 String endSql = sql + where;
        		 data = queryForInSQL(endSql, values.toArray(new Object[0]), inArgs.toArray(new String[0]), new MultiRowMapper<Map<String, Object>>() {
             		@Override
             		public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
             			return setFieldMapWithRs(rs, DigestUtils.md5Hex(sql));
             		}
             	}, post );
        	 }else{
        		 data = query(sql + where + post,
                         values.toArray(new Object[0]), new MultiRowMapper<Map<String, Object>>() {
                             @Override
                             public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                                 return setFieldMapWithRs(rs, DigestUtils.md5Hex(sql.toString()));
                             }
                         }); 
        		 
        	 }
            Map<String, Object> pageMap = new HashMap<String, Object>();
            pageMap.put("_maxRow", "" + data.size());
            pageMap.put("_maxPage", "1");
            pageMap.put("_page", "" + page);
            pageMap.put("_limit", "" + data.size());
            data.add(pageMap);
            return data;
        }
    }

    @Override
    public Object setField(ResultSet rs) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void execSql(String sql, Object[] objs) {
        JdbcUtils.getSQL(sql, objs);
        update(sql, objs);
    }

    @Override
    public List<Object[]> querySql(String sql, Object[] objs) {
        return query(sql, new MultiRowMapper<Object[]>() {
            @Override
            public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
                int count = rs.getMetaData().getColumnCount();
                List<Object> res = new ArrayList<Object>();
                for (int i = 1; i <= count; i++) {
                    res.add(rs.getObject(i));
                }
                return res.toArray(new Object[0]);
            }
        });
    }

    @Override
    public List<Map<String, Object>> getDataMapByParamMapMT(String physicalTableName, Map<String, String> paramMap,
            Map<String, String> columnMap, String metadataId) {
    	//根据metadataId 设置datasource
       // getEndDataSource(metadataId);
        String[] tablesAndContacts = physicalTableName.split("#");
        String[] tables = tablesAndContacts[0].split(",");
        String[] contacts = tablesAndContacts[1].split(",");

        String limit = paramMap.get("_limit");
        String page = paramMap.get("_page");
        String isDeleted = paramMap.get("_isDeleted");
        String sortId = paramMap.get("_dataSortMaxId");

        final StringBuffer sql = new StringBuffer("select ");
        Iterator<String> iterator = columnMap.keySet().iterator();
        StringBuffer colsql = new StringBuffer("");
        while (iterator.hasNext()) {
            String col = iterator.next();
            sql.append(" t1." + col + " as " + col);
            colsql.append(" t1." + col);
            if (iterator.hasNext()) {
                sql.append(",");
                colsql.append(",");
            }
        }
        sql.append(" from ");
        for (int i = 1; i <= tables.length; i++) {
            sql.append(tables[i - 1] + " t" + i);
            if (i != tables.length) {
                sql.append(",");
            }
        }
        sql.append(" where ");
        StringBuffer contsql = new StringBuffer();
        for (String cont : contacts) {
            String[] con = cont.split("=");
            String[] col1 = con[0].split("@");
            String[] col2 = con[1].split("@");
            contsql.append(" t" + col1[0] + "." + col1[1] + "=" + "t" + col2[0] + "." + col2[1] + " and ");
        }

        // Map<String,String> contactsMap = new HashMap<String,String>();
        // for (String cont : contacts) {
        // String[] con = cont.split("@");
        // contactsMap.put(con[0], con[1]);
        // }
        // Set<String> alcont = new HashSet<String>();
        // Iterator<String> iterator2 = contactsMap.keySet().iterator();
        // StringBuffer contsql = new StringBuffer();
        // while(iterator2.hasNext()){
        // String key = iterator2.next();
        // if(alcont.contains(key)){
        // continue;
        // }
        // char[] charArray = key.toCharArray();
        // String newKey = String.valueOf(charArray[1])+String.valueOf(charArray[0]);
        // alcont.add(key);
        // alcont.add(newKey);
        // String col1 = contactsMap.get(key);
        // String col2 = contactsMap.get(newKey);
        // contsql.append(" t"+charArray[0]+"."+col1+"="+"t"+charArray[1]+"."+col2+" and ");
        // }
        sql.append(contsql.substring(0, contsql.length() - 4));
        List<String> wheres = new ArrayList<String>();
        List<Object> values = new ArrayList<Object>();
        //inArgs
        Set<String> inArgs = new HashSet<>();
        String inParam = "";
        
        if (StringUtils.isBlank(isDeleted)) {
            isDeleted = "0";
        }

        if (!StringUtils.equals("-1", isDeleted)) {
            values.add(isDeleted);
            wheres.add("t1.is_deleted = ? ");
        }

        if (StringUtils.isNotBlank(sortId)) {
            values.add(sortId);
            wheres.add("t1.sequence_int_id > ? ");
        }
//        for (String key : paramMap.keySet()) {
//            if (key.indexOf("@") != -1) {
//                String[] colInd = key.split("@");
//                wheres.add(" t" + colInd[0] + "." + colInd[1] + " = ? ");
//                values.add(paramMap.get(key));
//            }
//        }
        
        for (String key : paramMap.keySet()) {
        	if(key.indexOf("@") != -1 && !StringUtils.startsWith(key, "in_")){
        		 String[] colInd = key.split("@");
                 wheres.add(" t" + colInd[0] + "." + colInd[1] + " = ? ");
                 values.add(paramMap.get(key));
        	}
        	//增加in的单位条件
        	if(StringUtils.startsWith(key, "in_")) {
        		inParam = "t1."+key.substring(3) + " in ";
        		inArgs.addAll(Arrays.asList(paramMap.get(key).split(",")));
        	}
        }
        //判断是否需要in，放在最后面
        if(StringUtils.isNotBlank(inParam)) {
        	wheres.add(inParam);
        }
        
        
        String where = CollectionUtils.isNotEmpty(wheres) ? (" AND " + StringUtils.join(wheres.toArray(new String[0]),
                " AND ")) : "";

        if (NumberUtils.toInt(limit) > 0) {
            Pagination pagination = new Pagination(NumberUtils.toInt(page), NumberUtils.toInt(limit), false);
            StringBuffer finSql = new StringBuffer("select * from (");
            finSql.append(sql.toString() + where);
            List<Map<String, Object>> data;
            if(CollectionUtils.isNotEmpty(inArgs)) {
            	String post = " group by " + colsql.toString() + ") ORDER BY sequence_int_id";
            	data = queryForInSQL(finSql.toString(), values.toArray(new Object[0]), inArgs.toArray(new String[0]),  new MultiRowMapper<Map<String, Object>>() {
                    @Override
                    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return setFieldMapWithRs(rs, DigestUtils.md5Hex(sql.toString()));
                    }
            	}, post, pagination);
            }else {
            	finSql.append(" group by " + colsql.toString() + ") ORDER BY sequence_int_id");
            	data = query(finSql.toString(), values.toArray(new Object[0]),
                        new MultiRowMapper<Map<String, Object>>() {
                            @Override
                            public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                                return setFieldMapWithRs(rs, DigestUtils.md5Hex(sql.toString()));
                            }
                }, pagination);
            }

            Map<String, Object> pageMap = new HashMap<String, Object>();
            pageMap.put("_maxRow", "" + pagination.getMaxRowCount());
            pageMap.put("_maxPage", "" + pagination.getMaxPageIndex());
            pageMap.put("_page", "" + pagination.getPageIndex());
            pageMap.put("_limit", "" + pagination.getPageSize());
            data.add(pageMap);
            return data;
        }
        else {
            StringBuffer finSql = new StringBuffer("select * from (");
            finSql.append(sql.toString() + where);
            finSql.append(" group by " + colsql.toString() + ") ORDER BY sequence_int_id");
            List<Map<String, Object>> data = query(finSql.toString(), values.toArray(new Object[0]),
                    new MultiRowMapper<Map<String, Object>>() {
                        @Override
                        public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                            return setFieldMapWithRs(rs, DigestUtils.md5Hex(sql.toString()));
                        }
                    });

            Map<String, Object> pageMap = new HashMap<String, Object>();
            pageMap.put("_maxRow", "" + data.size());
            pageMap.put("_maxPage", "1");
            pageMap.put("_page", "" + page);
            pageMap.put("_limit", "" + data.size());
            data.add(pageMap);
            return data;
        }
    }
}
