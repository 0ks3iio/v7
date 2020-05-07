package net.zdsoft.szxy.base.service;

import net.zdsoft.szxy.base.api.StudentRemoteService;
import net.zdsoft.szxy.base.entity.Student;
import net.zdsoft.szxy.base.query.StudentQuery;
import net.zdsoft.szxy.dubbo.jpa.DubboPageable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author shenke
 * @since 2019/4/17 上午10:12
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback
public class StudentServiceTest {

    @Resource
    private StudentRemoteService studentRemoteService;

    @Test
    public void test_Query() {
        StudentQuery studentQuery = new StudentQuery();
        studentQuery.setUsername("xs11");
        Page<Student> studentPage = studentRemoteService.queryStudents(studentQuery, new DubboPageable(1, 1, Sort.unsorted()));
        System.out.println(studentPage.getTotalElements());
    }
}
