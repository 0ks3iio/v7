package net.zdsoft.bg;

import net.zdsoft.BaseTest;
import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.dao.ApiDao;
import net.zdsoft.bigdata.data.entity.Api;
import net.zdsoft.bigdata.data.manager.InvocationBuilder;
import net.zdsoft.bigdata.data.manager.api.Invoker;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.framework.utils.UuidUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author ke_shen@126.com
 * @since 2018/4/9 下午5:24
 */

@Transactional(transactionManager = "txManagerJap")
@Rollback
public class ApiDataSourceTest extends BaseTest {

    @Resource
    private ApiDao apiDao;
    @Resource
    private Invoker invoker;

    private Api api;

    @Before
    public void createMockmvc() {

        api = new Api();
        api.setUrl("http://192.168.22.140:8013/uc/access_token/get_token");
        api.setId(UuidUtils.generateUuid());
        api.setCreationTime(new Date());
        api.setModifyTime(api.getCreationTime());
        api.setName("api");
        api.setUnitId(UuidUtils.generateUuid());
        apiDao.save(api);
    }

    @Test
    public void _executeQuery() {
        Result object = invoker.invoke(
                InvocationBuilder.getInstance().dataSourceId(api.getId())
                        .queryStatement(api.getUrl()).type(DataSourceType.API)
                        .build()
        );
        System.out.println(object.getValue().toString());
    }
}
