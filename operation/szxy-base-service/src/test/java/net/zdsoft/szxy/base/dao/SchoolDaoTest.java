package net.zdsoft.szxy.base.dao;

import net.zdsoft.szxy.base.api.SchoolRemoteService;
import net.zdsoft.szxy.base.entity.School;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author zhujy
 * 日期:2019/3/19 0019
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback
public class SchoolDaoTest {
    @Resource
    private SchoolDao schoolDao;
    @Resource
    private SchoolRemoteService schoolRemoteService;

    @Test
    public void getTest(){
        School school = schoolDao.getSchoolById("00EA65CEB7D5DE8DE050A8C09B006DCE");
        System.out.println(school.getSchoolName());
        System.out.println(school.getAddress());
    }


    @Test
    public void updateTest(){
        School school=new School();
        school.setId("00EA65CEB7D5DE8DE050A8C09B006DCE");
        school.setSchoolName("xjymmmmmmmm");
        school.setAddress("西湖区");
        school.setIsDeleted(0);
        school.setCreationTime(new Date());
        school.setModifyTime(new Date());
        school.setEventSource(0);
        String[] schoolParam={"schoolName","address"};
        schoolDao.updateSchool(school,schoolParam);
        School school1 = schoolDao.getSchoolById("00EA65CEB7D5DE8DE050A8C09B006DCE");
        System.out.println(school1.getSchoolName());
        System.out.println(school1.getAddress());
    }

    @Test
    public void test_updateSchool() {
        School school = schoolDao.getSchoolById("00EA65CEB7D5DE8DE050A8C09B006DCE");
        School t = new School();
        t.setIntroduction("instro");
        t.setId(school.getId());
        t.setRegionCode(school.getRegionCode());
        t.setSchoolName("test-test");
        schoolRemoteService.updateSchool(t, new String[]{ "introduction"}, null);
    }
}
