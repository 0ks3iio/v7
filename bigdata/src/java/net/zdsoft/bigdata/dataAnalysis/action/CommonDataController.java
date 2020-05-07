package net.zdsoft.bigdata.dataAnalysis.action;

import net.zdsoft.bigdata.data.entity.ModelDataset;
import net.zdsoft.bigdata.data.entity.ModelDatasetUser;
import net.zdsoft.bigdata.data.service.DataModelParamService;
import net.zdsoft.bigdata.data.service.DataModelService;
import net.zdsoft.bigdata.data.service.ModelDatasetService;
import net.zdsoft.bigdata.data.service.ModelDatasetUserService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wangdongdong on 2019/6/19 9:13.
 */
@Controller
@RequestMapping("/bigdata/analyse/common")
public class CommonDataController extends BigdataBaseAction {

    @Resource
    private DataModelService dataModelService;
    @Resource
    private DataModelParamService dataModelParamService;
    @Resource
    private ModelDatasetService modelDatasetService;
    @Resource
    private ModelDatasetUserService modelDatasetUserService;

    /**
     * 获取数据模型指标和维度
     *
     * @param code
     * @param type
     * @return
     */
    @RequestMapping("/getModelParam")
    @ResponseBody
    public Response getModelParam(String code, String type) {
        return Response.ok().data(dataModelParamService.findTopByCodeAndType(code, type)).build();
    }

    /**
     * 获取用户数据集
     *
     * @param modelId
     * @return
     */
    @RequestMapping("/getUserDataset")
    @ResponseBody
    public Response getUserDataset(String modelId) {
        String userId = getLoginInfo().getUserId();

        List<ModelDatasetUser> orderUserList = modelDatasetUserService
                .findListBy(new String[] { "modelId", "userId" }, new String[] {
                        modelId, userId });

        List<ModelDataset> modelDatasets = modelDatasetService
                .findAllByIdIn(orderUserList.stream()
                        .map(ModelDatasetUser::getDsId).toArray(String[]::new));

        return Response.ok().data(modelDatasets).build();
    }




}
