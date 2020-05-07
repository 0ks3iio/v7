package net.zdsoft.basedata.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import net.zdsoft.basedata.dao.TeachClassJdbcDao;
import net.zdsoft.basedata.dto.TeachClassSearchDto;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.utils.MultiRowMapper;

@Repository
public class TeachClassJdbcDaoImpl extends BaseDao<TeachClass> implements TeachClassJdbcDao{

	@Override
	public List<TeachClass> findListByDto(TeachClassSearchDto dto) {
		StringBuffer sql=new StringBuffer("select * from base_teach_class where is_deleted=0 and acadyear= ? "
				+ " and semester = ? ");
		List<Object> objList=new ArrayList<Object>();
		objList.add(dto.getAcadyearSearch());
		objList.add(dto.getSemesterSearch());
		if(StringUtils.isNotBlank(dto.getUnitId())){
        	sql.append(" and unit_id =? ");
        	objList.add(dto.getUnitId());
        }
        if(StringUtils.isNotBlank(dto.getClassType())){
        	sql.append(" and class_type = ? ");
        	objList.add(dto.getClassType());
        }
        if(StringUtils.isNotBlank(dto.getIsUsing())){
        	sql.append(" and  is_using = ? ");
        	objList.add(dto.getIsUsing());
        	
        }
        if(StringUtils.isNotBlank(dto.getSubjectId())){
        	sql.append(" and  course_id = ? ");
        	objList.add(dto.getSubjectId());
        }
        if(StringUtils.isNotBlank(dto.getTeachClassName())){
        	sql.append(" and name like '%"+dto.getTeachClassName()+"%' ");
        }
        if(StringUtils.isNotBlank(dto.getGradeIds())){
        	if(dto.getGradeIds().indexOf(",") >=0){
        		String[] gradeIdArr = dto.getGradeIds().split(",");
        		String ss="";
        		for(String g:gradeIdArr){
        			if(StringUtils.isNotBlank(g)){
        				if(StringUtils.isNotBlank(ss)){
        					ss=ss+" or grade_id like '%"+g+"%' ";
        				}else{
        					ss=" grade_id like '%"+g+"%' ";
        				}
        			}
        		}
        		if(StringUtils.isNotBlank(ss)){
        			sql.append(" and ( "+ss+" )");
        		}
        		
        	}else{
        		sql.append(" and grade_id like ? ");
        		objList.add("%"+dto.getGradeIds()+"%");
        	}
        	
        }
        if(StringUtils.isNotBlank(dto.getIsUsingMerge())){
        	sql.append(" and is_using_merge = ? ");
        	objList.add(dto.getIsUsingMerge());
        }
        
        if(StringUtils.isNotBlank(dto.getTeacherId())){
        	sql.append(" and teacher_id = ? ");
        	objList.add(dto.getTeacherId());
        }
        if(StringUtils.isNotBlank(dto.getRelaCourseId())) {
        	sql.append(" and  rela_course_id = ? ");
        	objList.add(dto.getRelaCourseId());
        }
        
        if(StringUtils.isNotBlank(dto.getTeacherName())){
        	sql.append(" and teacher_id in (select id from base_teacher where unit_id = ? and teacher_name like ? and is_deleted = 0)");
        	objList.add(dto.getUnitId());
        	objList.add("%"+dto.getTeacherName()+"%");
        }

        if(StringUtils.isNotBlank(dto.getTeachClassType())){
        	String teachClassType = dto.getTeachClassType();
        	if("1".equals(teachClassType)){
        		//普通班
        		sql.append(" and is_merge != ? ");
        		objList.add("1");
        		sql.append(" and is_using_merge != ? ");
        		objList.add("1");
        	}else if("2".equals(teachClassType)){
        		//用于合并的小班
        		sql.append(" and is_using_merge = ? ");
        		objList.add("1");
        	}else if("3".equals(teachClassType)){
        		//合并大班
        		sql.append(" and is_merge = ? ");
        		objList.add("1");
        	}
        }
		return query(sql.toString(), objList.toArray(new Object[]{}) ,new MultiRow());
	}

	@Override
	public TeachClass setField(ResultSet rs) throws SQLException {
		TeachClass teachClass=new TeachClass();
		teachClass.setId(rs.getString("id"));
		teachClass.setUnitId(rs.getString("unit_id"));
		teachClass.setAcadyear(rs.getString("acadyear"));
		teachClass.setSemester(rs.getString("semester"));
		teachClass.setName(rs.getString("name"));
		teachClass.setClassType(rs.getString("class_type"));
		teachClass.setTeacherId(rs.getString("teacher_id"));
		teachClass.setCourseId(rs.getString("course_id"));
		teachClass.setCreationTime(rs.getTimestamp("creation_time"));
		teachClass.setModifyTime(rs.getTimestamp("modify_time"));
		teachClass.setIsDeleted(rs.getInt("is_deleted"));
		teachClass.setAssistantTeachers(rs.getString("assistant_teachers"));
		teachClass.setGradeId(rs.getString("grade_id"));
		teachClass.setIsUsing(rs.getString("is_using"));
		teachClass.setPunchCard(rs.getInt("punch_card"));
		teachClass.setCredit(rs.getInt("credit"));
		teachClass.setPassMark(rs.getInt("pass_mark"));
		teachClass.setFullMark(rs.getInt("full_mark"));
		teachClass.setIsUsingMerge(rs.getString("is_using_merge"));
		teachClass.setIsMerge(rs.getString("is_merge"));
		teachClass.setParentId(rs.getString("parent_id"));
		teachClass.setRelaCourseId(rs.getString("rela_course_id"));
		teachClass.setPlaceId(rs.getString("place_id"));
		return teachClass;
	}

	@Override
	public Map<String, List<String>> findMapByVirtualIds(String unitId, String acadyear, String semester, String gradeId, String[] virtualIds) {
		String sql = "select tc.rela_course_id courseid,s.class_id classid from base_teach_class_stu ts, base_teach_class tc, base_student s"
				+ " where s.id=ts.student_id and tc.id=ts.class_id"
				+ " and ts.is_deleted=0"
				+ " and tc.unit_id=? and tc.acadyear=? and tc.semester=? and tc.grade_id like ? and tc.is_deleted=0 and tc.is_using=1 and tc.rela_course_id in";
		List<Object[]> list =queryForInSQL(sql, new Object[]{unitId, acadyear, semester, "%"+gradeId+"%"}, virtualIds,new MultiRowMapper<Object[]>(){

			@Override
			public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
				Object[] str =new Object[2];
				str[0]=rs.getString("courseid");
				str[1]=rs.getString("classid");
				return str;
			}
			 
		});
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		if(CollectionUtils.isNotEmpty(list)){
			for(Object[] s:list){
				String key = s[0].toString();
				if(!map.containsKey(key)){
					map.put(key, new ArrayList<String>()); 
				}
				if(!map.get(key).contains(s[1].toString())){
					map.get(s[0]).add(s[1].toString());
				}
			}
		}
		return map;
	}
	
	@Override
	public List<TeachClass> findClassHasEx(String unitId, String acadyear, String semester, String gradeId, String[] classTypes){
		Object[] args = new Object[] {unitId,acadyear,semester,gradeId};
		String sql = "select * From base_Teach_Class a where unit_Id=? and acadyear=? and semester=? and grade_Id = ? " + 
				" and is_Deleted = 0 and exists(select 1 from base_Teach_Class_Ex b where a.id = b.teach_Class_Id)  and class_Type in ";
		return queryForInSQL(sql, args, classTypes, new MultiRow());
	}
}
