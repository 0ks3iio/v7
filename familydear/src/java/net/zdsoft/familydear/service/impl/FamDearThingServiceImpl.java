package net.zdsoft.familydear.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.familydear.dao.FamDearThingDao;
import net.zdsoft.familydear.entity.FamDearPlan;
import net.zdsoft.familydear.entity.FamDearThing;
import net.zdsoft.familydear.service.FamDearPlanService;
import net.zdsoft.familydear.service.FamDearThingService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.familydear.service.impl
 * @ClassName: FamDearThingServiceImpl
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2019/5/7 9:13
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/5/7 9:13
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Service("famDearThingService")
public class FamDearThingServiceImpl extends BaseServiceImpl<FamDearThing, String> implements FamDearThingService {
    @Autowired
    private FamDearThingDao famDearThingDao;
    @Override
    protected BaseJpaRepositoryDao<FamDearThing, String> getJpaDao() {
        return famDearThingDao;
    }

    @Override
    protected Class<FamDearThing> getEntityClass() {
        return FamDearThing.class;
    }
}
