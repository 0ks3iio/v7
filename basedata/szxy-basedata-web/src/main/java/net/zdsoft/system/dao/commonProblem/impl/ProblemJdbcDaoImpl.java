/* 
 * @(#)ProblemJdbcDaoImpl.java    Created on 2017-5-16
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.dao.commonProblem.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.utils.MultiRowMapper;
import net.zdsoft.system.dao.commonProblem.ProblemJdbcDao;
import net.zdsoft.system.remote.dto.ProblemRemoteDto;

/**
 * @author xuxiyu
 * @version $Revision: 1.0 $, $Date: 2017-5-16 上午10:09:40 $
 */
@Repository
public class ProblemJdbcDaoImpl extends BaseDao<ProblemRemoteDto> implements ProblemJdbcDao {

    @Override
    public List<ProblemRemoteDto> findByCondition(ProblemRemoteDto contidtion) {
        List<Object> args = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer(
                "select p.id id ,p.question question,p.answer answer,p.modify_time modify_time,p.type_id type_id,pt.name name from  sys_problem p ,sys_problem_type pt where pt.id=p.type_id and server_code =? and is_deleted=0");
        args.add(contidtion.getServerCode());
        if (StringUtils.isNotEmpty(contidtion.getQuestion())) {
            args.add("%" + contidtion.getQuestion() + "%");
            sql.append(" and question like ?");
        }
        sql.append(" order by type_id");
        return query(sql.toString(), args.toArray(new Object[0]), new MultiRowMapper<ProblemRemoteDto>() {

            @Override
            public ProblemRemoteDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                ProblemRemoteDto p = new ProblemRemoteDto();
                p.setId(rs.getString("id"));
                p.setQuestion(rs.getString("question"));// 问题
                p.setTypeName(rs.getString("name"));// 类型名称
                p.setTypeId(rs.getString("type_id"));
                p.setAnswer(rs.getString("answer"));
                p.setModifyTime(rs.getDate("modify_time"));
                return p;
            }
        });
    }

    @Override
    public ProblemRemoteDto setField(ResultSet rs) throws SQLException {
        // ProblemRemoteDto p = new ProblemRemoteDto();
        // p.setId(rs.getString("id"));
        // p.setQuestion(rs.getString("question"));// 问题
        // p.setTypeName(rs.getString("name"));// 类型名称
        // p.setTypeId(rs.getString("type_id"));
        // p.setAnswer(rs.getString("answer"));
        // p.setModifyTime(rs.getDate("modify_time"));
        // return p;
        return null;
    }

}
