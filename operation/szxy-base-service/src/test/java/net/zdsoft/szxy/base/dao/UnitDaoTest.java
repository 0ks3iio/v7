package net.zdsoft.szxy.base.dao;

import net.zdsoft.szxy.base.entity.Unit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shenke
 * @since 2019/3/22 下午4:58
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback
public class UnitDaoTest {

    @Resource
    private UnitDao unitDao;

    @Test
    public void test_getTopUnits() {
        List<Unit> topUnits = unitDao.getTopUnits();
        topUnits.forEach(e->System.out.println(e.getUnitName()));
    }

}
