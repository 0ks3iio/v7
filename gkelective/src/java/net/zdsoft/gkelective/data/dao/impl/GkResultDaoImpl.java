/* 
 * @(#)GkResultDaoImpl.java    Created on 2017年2月16日
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id: GkResultDaoImpl.java 4790 2017-09-19 07:54:20Z zengzt $
 */
package net.zdsoft.gkelective.data.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.utils.MapRowMapper;
import net.zdsoft.framework.utils.SQLUtils;
import net.zdsoft.gkelective.data.constant.GkElectveConstants;
import net.zdsoft.gkelective.data.dao.GkResultJdbcDao;
import net.zdsoft.gkelective.data.dto.ChosenSubjectSearchDto;
import net.zdsoft.gkelective.data.entity.GkResult;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * @author zhaosheng
 * @version $Revision: 4790 $, $Date: 2017-09-19 15:54:20 +0800 (周二, 19 9月 2017) $
 */
@Repository
public class GkResultDaoImpl extends BaseDao<GkResult> implements GkResultJdbcDao {
    @Override
    public GkResult setField(ResultSet rs) throws SQLException {
    	GkResult ent =new GkResult();
    	ent.setId(rs.getString("id"));
    	ent.setSubjectArrangeId(rs.getString("subject_arrange_id"));
    	ent.setStudentId(rs.getString("student_id"));
    	ent.setSubjectId(rs.getString("subject_id"));
    	ent.setStatus(rs.getInt("status"));
    	ent.setCreationTime(rs.getDate("creation_time"));
    	ent.setModifyTime(rs.getDate("modify_time"));
        return ent;
    }

    @Override
    public Map<String, Integer> findResultCountByArrangeIdAndCourseIds(String arrangeId, String... courseIds) {
        String sql = "select subject_id, count(*) c  from gkelective_result where  subject_arrange_id =? and subject_id IN";
        return queryForInSQL(sql, new Object[] { arrangeId }, courseIds, new MapRowMapper<String, Integer>() {
            @Override
            public String mapRowKey(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1);
            }

            @Override
            public Integer mapRowValue(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt(2);
            }
        }, " GROUP BY subject_id ORDER BY c");
    }
    
    @Override
    public Map<String, Integer> findResultCountBySubIdsStuIds(String arrangeId, String[] courseIds,String[] stuids) {
        String sql = "select subject_id, count(*) c  from gkelective_result where  subject_arrange_id =? and subject_id in"+SQLUtils.toSQLInString(courseIds)+" and student_id in";
        return queryForInSQL(sql, new Object[] { arrangeId }, stuids, new MapRowMapper<String, Integer>() {
            @Override
            public String mapRowKey(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1);
            }

            @Override
            public Integer mapRowValue(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt(2);
            }
        }, " GROUP BY subject_id ORDER BY c");
    }
    
    public List<GkResult> findResultByChosenSubjectSearchDto(String arrangeId,String gradeId, ChosenSubjectSearchDto dto){
    	List<Object> cslist=new ArrayList<Object>();
    	StringBuffer sql=new StringBuffer("select * from gkelective_result gr where gr.subject_arrange_id=? and exists(select 1 from base_student bs where gr.student_id =bs.id");
    	cslist.add(arrangeId);
    	if(StringUtils.isNotBlank(dto.getSearchSex())){
    		sql.append(" and bs.sex =? ");
    		cslist.add(dto.getSearchSex());
    	}
    	if(StringUtils.isNotBlank(dto.getSearchCondition())){
    		if("1".equals(dto.getSearchSelectType())){//学号
    			sql.append(" and bs.student_code like ? ");
        		cslist.add(dto.getSearchCondition()+"%");
    		}else if("2".equals(dto.getSearchSelectType())){//姓名
    			sql.append(" and bs.student_name like ? ");
        		cslist.add(dto.getSearchCondition()+"%");
    		}
    	}
    	if(StringUtils.isBlank(dto.getSearchClassId())){
    		sql.append(" and exists(select 1 from base_class bc where bc.grade_id=? and bc.is_deleted=0 and bc.id=bs.class_id) and bs.is_deleted=0)");
    		cslist.add(gradeId);
    	}else{
    		sql.append(" and bs.class_id =? and bs.is_deleted=0)");
    		cslist.add(dto.getSearchClassId());
    	}
    	if(StringUtils.isNotBlank(dto.getSearchSubject())){
    		String[] subids=dto.getSearchSubject().split(",");
    		for(String str:subids){
    			sql.append(" and exists(select 1 from gkelective_result gr1 where gr.student_id=gr1.student_id and gr1.subject_arrange_id=? and gr1.subject_id=?) ");
    			cslist.add(arrangeId);
    			cslist.add(str);
    		}
    		sql.append(" order by gr.subject_id");
    		return this.query(sql.toString(), cslist.toArray(new Object[0]), new MultiRow());
    	}else{
    		return this.query(sql.toString(), cslist.toArray(new Object[0]), new MultiRow());
    		
    	}
    	
    }

	@Override
	public void deleteBySubjectArrangeIdAndSubjectId(String arrangeId,
			String[] subjectIds) {
		String sqlString="delete from gkelective_result where gkelective_result.subject_arrange_id=? "
				+ "and student_id in(select student_id from gkelective_result aa where subject_arrange_id=? "
				+ "and aa.subject_id=? "
				+ "and exists(select 1 from  gkelective_result bb where aa.student_id =bb.student_id and aa.subject_arrange_id=bb.subject_arrange_id and bb.subject_id=?) "
				+ "and exists(select 1 from  gkelective_result cc where aa.student_id =cc.student_id and aa.subject_arrange_id=cc.subject_arrange_id and cc.subject_id=?))";
		this.update(sqlString, new Object[]{arrangeId,arrangeId,subjectIds[0],subjectIds[1],subjectIds[2]});
	}
}
