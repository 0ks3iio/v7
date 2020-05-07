package net.zdsoft.szxy.base.dao;

import net.zdsoft.szxy.base.entity.Clazz;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author shenke
 * @since 2019/3/20 下午5:33
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback
public class ClassDaoTest {

    @Resource
    private ClassDao classDao;

    private Clazz clazz;

    @Test
    public void testDelete() {
        Clazz clazz = classDao.getOne("00000000330108082702201320162108");
        classDao.deleteClazzBySchoolId(clazz.getSchoolId());

        Clazz other = classDao.getOne("00000000330108082702201320162104");
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(other.getModifyTime()));
    }

    @Test
    public void test_getClazzesByGradeId() {
        //section ASC, acadyear DESC,classCode ASC
        Sort sort = Sort.by(Sort.Order.asc("section"), Sort.Order.asc("classCode"), Sort.Order.desc("acadyear"));
        classDao.getClazzesByGradeId("00000000003133000378500201620172", null);
        List<Clazz> clazzList = classDao.getClazzesByGradeId("00000000003133000378500201620172", sort);
        clazzList.stream().forEach(e-> System.out.println(e.getClassName()));
    }
}
