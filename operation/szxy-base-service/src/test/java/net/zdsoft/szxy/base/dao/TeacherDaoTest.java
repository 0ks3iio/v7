package net.zdsoft.szxy.base.dao;

import net.zdsoft.szxy.base.entity.Teacher;
import net.zdsoft.szxy.base.enu.DeleteCode;
import net.zdsoft.szxy.utils.UuidUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author shenke
 * @since 2019/3/19 下午3:02
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback
public class TeacherDaoTest {

    @Resource
    private TeacherDao teacherDao;

    private Teacher deleteTeacher;
    private Teacher normalTeacher;

    @Before
    public void init() {
        deleteTeacher = new Teacher();
        deleteTeacher.setId(UuidUtils.generateUuid());
        deleteTeacher.setUnitId(UuidUtils.generateUuid());
        deleteTeacher.setSex(1);
        deleteTeacher.setIsDeleted(0);
        deleteTeacher.setTeacherName("删除教师");
        deleteTeacher.setEventSource(1);
        deleteTeacher.setCreationTime(new Date());
        deleteTeacher.setModifyTime(deleteTeacher.getCreationTime());
        deleteTeacher.setTeacherCode("000001");
        deleteTeacher.setIsDeleted(DeleteCode.DELETED);
        deleteTeacher.setDeptId(UuidUtils.generateUuid());
        teacherDao.save(deleteTeacher);

        normalTeacher = new Teacher();
        normalTeacher.setId(UuidUtils.generateUuid());
        normalTeacher.setUnitId(UuidUtils.generateUuid());
        normalTeacher.setSex(1);
        normalTeacher.setIsDeleted(0);
        normalTeacher.setTeacherName("正常教师");
        normalTeacher.setEventSource(1);
        normalTeacher.setCreationTime(new Date());
        normalTeacher.setModifyTime(normalTeacher.getCreationTime());
        normalTeacher.setTeacherCode("000002");
        normalTeacher.setDeptId(UuidUtils.generateUuid());
        normalTeacher.setIsDeleted(DeleteCode.NOT_DELETED);
        teacherDao.save(normalTeacher);
    }

    @Test
    public void test_findOne() {
        Assert.assertEquals(teacherDao.findById(normalTeacher.getId()).get(), normalTeacher);
        Assert.assertNull(teacherDao.findById(deleteTeacher.getId()).orElse(null));
    }
}
