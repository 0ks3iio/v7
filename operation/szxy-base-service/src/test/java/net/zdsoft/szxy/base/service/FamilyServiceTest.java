package net.zdsoft.szxy.base.service;

import net.zdsoft.szxy.base.api.FamilyRemoteService;
import net.zdsoft.szxy.base.entity.Family;
import net.zdsoft.szxy.base.query.FamilyQuery;
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
 * @since 2019/4/16 上午11:45
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback
public class FamilyServiceTest {


    @Resource
    private FamilyRemoteService familyRemoteService;

    @Test
    public void test_Query() {

        FamilyQuery query = new FamilyQuery();

        Page<Family> familyPage = familyRemoteService.queryFamilies(query, new DubboPageable(1, 1, Sort.unsorted()));
        System.out.println(familyPage.getTotalElements());
    }

}
