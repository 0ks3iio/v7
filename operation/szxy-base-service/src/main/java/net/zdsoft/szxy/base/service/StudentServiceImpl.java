package net.zdsoft.szxy.base.service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import net.zdsoft.szxy.base.api.StudentRemoteService;
import net.zdsoft.szxy.base.dao.StudentDao;
import net.zdsoft.szxy.base.entity.Student;
import net.zdsoft.szxy.base.entity.User;
import net.zdsoft.szxy.base.model.QStudent;
import net.zdsoft.szxy.base.model.QUser;
import net.zdsoft.szxy.base.query.StudentQuery;
import net.zdsoft.szxy.dubbo.jpa.DubboPageImpl;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.utils.AssertUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author shenke
 * @since 2019/3/21 下午3:37
 */
@Service("studentRemoteService")
public class StudentServiceImpl implements StudentRemoteService {

    private Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Resource
    private StudentDao studentDao;

    @Override
    public Student getStudentById(String id) {
        return studentDao.getStudentById(id);
    }

    @Override
    public List<Student> getStudentsById(String[] ids) {
        AssertUtils.hasElements(ids, "学生ids不能为空");
        return studentDao.getStudentsById(ids);
    }

    @Record(type = RecordType.Call)
    @Override
    public Page<Student> queryStudents(StudentQuery studentQuery, Pageable pageable) {
        AssertUtils.notNull(studentQuery, "查询条件StudentQuery不能为空");

        Specification<Student> specification = (Specification<Student>) (root, query, criteriaBuilder) -> {
            List<Predicate> ps = new ArrayList<>(6);
            //班级ID
            if (StringUtils.isNotBlank(studentQuery.getClassId())) {
                ps.add(criteriaBuilder.equal(root.get(QStudent.classId), studentQuery.getClassId()));
            }
            if (StringUtils.isNotBlank(studentQuery.getStudentCode())) {
                ps.add(criteriaBuilder.like(root.get(QStudent.studentCode), studentQuery.getStudentCode() + "%"));
            }
            if (StringUtils.isNotBlank(studentQuery.getStudentName())) {
                ps.add(criteriaBuilder.like(root.get(QStudent.studentName), studentQuery.getStudentName() + "%"));
            }
            if (StringUtils.isNotBlank(studentQuery.getUnitId())) {
                ps.add(criteriaBuilder.equal(root.get(QStudent.schoolId), studentQuery.getUnitId()));
            }
            if (CollectionUtils.isNotEmpty(studentQuery.getRegions())) {
                ps.add(criteriaBuilder.or(
                        studentQuery.getRegions().stream().map(region->{
                            return criteriaBuilder.like(root.get(QStudent.regionCode), region + "%");
                        }).toArray(Predicate[]::new)
                ));
            }
            if (Objects.nonNull(studentQuery.getUsername())) {
                Join<Student, User> join = root.join(QStudent.users, JoinType.INNER);
                ps.add(criteriaBuilder.like(join.get(QUser.username), studentQuery.getUsername() + "%"));
            }
            return query.where(ps.toArray(new Predicate[0])).getRestriction();
        };
        if (pageable != null) {
            return DubboPageImpl.of(studentDao.findAll(specification, pageable));
        }
        return DubboPageImpl.of(studentDao.findAll(specification));
    }
}
