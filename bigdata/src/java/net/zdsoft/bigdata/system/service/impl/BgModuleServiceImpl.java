package net.zdsoft.bigdata.system.service.impl;

import java.util.*;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dto.OptionDto;
import net.zdsoft.bigdata.data.service.OptionService;
import net.zdsoft.bigdata.system.dao.BgModuleDao;
import net.zdsoft.bigdata.system.entity.BgModule;
import net.zdsoft.bigdata.system.service.BgModuleService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bgModuleService")
public class BgModuleServiceImpl extends BaseServiceImpl<BgModule, String> implements BgModuleService {

    @Autowired
    private BgModuleDao bgModuleDao;

    @Autowired
    private OptionService optionService;

    @Override
    protected BaseJpaRepositoryDao<BgModule, String> getJpaDao() {
        return bgModuleDao;
    }

    @Override
    protected Class<BgModule> getEntityClass() {
        return BgModule.class;
    }

    @Override
    public List<BgModule> findAllModuleList() {
        return convertModule(bgModuleDao.findAllModuleList());
    }

    @Override
    public List<BgModule> findModuleListByParentId(String parentId) {
        return convertModule(bgModuleDao.findModuleListByParentId(parentId));
    }

    @Override
    public void updateMarkById(String id, Integer mark) {
        bgModuleDao.updateMarkById(id, mark, new Date());
    }

    private List<BgModule> convertModule(List<BgModule> moduleList) {
//        Map<String, Integer> topModuleSizeMap = new HashMap<String, Integer>();
//        for (BgModule module : moduleList) {
//
//        }
        List<BgModule> topModuleList = bgModuleDao.findModuleListByParentId(Constant.GUID_ZERO);
        Map<String, BgModule> topModuleMap = new HashMap<String, BgModule>();
        for (BgModule module : topModuleList) {
            topModuleMap.put(module.getId(), module);
        }

        int style = 1;
        OptionDto styleDto = optionService.getAllOptionParam("pageStyle");
        if (styleDto != null) {
            style = Integer.valueOf(styleDto.getFrameParamMap()
                    .get("style"));
        }
        List<BgModule> resultList = new ArrayList<>();
        for (BgModule module : moduleList) {
            //增加页面模式的过滤
            if (module.getUserType().contains(BgModule.USER_TYPE_NORMAL) && style != module.getStyle()) {
                continue;
            }

            if (topModuleMap.containsKey(module.getParentId())) {
                module.setParentModuleName(topModuleMap.get(module.getParentId()).getName());
            }
            StringBuffer userTypeName = new StringBuffer();
            if (module.getUserType().contains(BgModule.USER_TYPE_NORMAL)) {
                userTypeName.append("普通用户,");
            }
            if (module.getUserType().contains(BgModule.USER_TYPE_BACKGROUND)) {
                userTypeName.append("后台用户,");
            }
            module.setUserTypeName(userTypeName.toString().substring(0, userTypeName.toString().length() - 1));
            resultList.add(module);
        }
        return resultList;
    }

}
