<form id="subForm">
    <input type="hidden" name="creationTime" value="${(infrastructureProject.creationTime?string('yyyy-MM-dd HH:mm:ss'))?default('')}">
    <input type="hidden" name="unitId" value="${infrastructureProject.unitId!}">
    <input type="hidden" name="id" value="${infrastructureProject.id!}">
    <table class="table table-bordered table-striped no-margin">
        <thead>
        <tr>
            <th colspan="4" class="text-center">基建项目信息</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td class="text-right"><span style="color:red">*</span>项目学校:</td>
            <td><input type="text" class="form-control" style="width:200px;" name="projectSchool"
                       id="projectSchool" value="${infrastructureProject.projectSchool!}" nullable="false"
                       maxLength="10"/>
            </td>
            <td class="text-right">项目名称:</td>
            <td><input type="text" class="form-control" style="width:200px;" name="projectName" id="projectName"
                       value="${infrastructureProject.projectName!}" maxLength="500"/>
            </td>
        </tr>
        <tr>
            <td class="text-right">项目性质:</td>
            <td>
                <select name="projectNature"  style="width:200px;"   id="projectNature" class="form-control">
                ${mcodeSetting.getMcodeSelect("DM-XMXZ", infrastructureProject.projectNature?default(''), "")}
                </select>
            </td>
            <td class="text-right">勘察单位:</td>
            <td>  <input type="text" class="form-control" style="width:200px;"name="surveyUnit" id="surveyUnit"
                         value="${infrastructureProject.surveyUnit!}" maxLength="100"/>
            </td>
        </tr>
        <tr>
            <td class="text-right">测绘单位:</td>
            <td> <input type="text" class="form-control" style="width:200px;"name="surveyingUnit"
                        id="surveyingUnit" value="${infrastructureProject.surveyingUnit!}" maxLength="100"/>
            </td>
            <td class="text-right">效果图设计单位:</td>
            <td> <input type="text" class="form-control" style="width:200px;"name="renderingDesignUnit"
                        id="renderingDesignUnit" value="${infrastructureProject.renderingDesignUnit!}"
                        maxLength="100"/>
            </td>
        </tr>
        <tr>
            <td class="text-right">图纸设计单位:</td>
            <td> <input type="text" class="form-control" style="width:200px;"name="drawingDesignUnit"
                        id="drawingDesignUnit" value="${infrastructureProject.drawingDesignUnit!}"
                        maxLength="100"/>
            </td>
            <td class="text-right">图纸审查单位:</td>
            <td> <input type="text" class="form-control" style="width:200px;"name="drawingReviewUnit"
                        id="drawingReviewUnit" value="${infrastructureProject.drawingReviewUnit!}"
                        maxLength="100"/>
            </td>
        </tr>
        <tr>
            <td class="text-right">造价单位::</td>
            <td> <input type="text" class="form-control" style="width:200px;"name="costUnit" id="costUnit"
                        value="${infrastructureProject.costUnit!}" maxLength="100"/>
            </td>
            <td class="text-right">控制价（元）:</td>
            <td> <input type="text" class="form-control" style="width:200px;"name="controlPrice"
                        dataType="float"    min="0"  vtype="number" decimallength="2" maxLength="15"
                        id="controlPrice" value="${infrastructureProject.controlPrice!}" />
            </td>
        </tr>
        <tr>
            <td class="text-right">代理机构:</td>
            <td><input type="text" class="form-control" style="width:200px;"name="agency" id="agency"
                       value="${infrastructureProject.agency!}" maxLength="100"/>
            </td>
            <td class="text-right">监理单位:</td>
            <td>  <input type="text" class="form-control" style="width:200px;"name="supervisoryUnit"
                         id="supervisoryUnit" value="${infrastructureProject.supervisoryUnit!}" maxLength="100"/>
            </td>
        </tr>
        <tr>
            <td class="text-right">施工企业:</td>
            <td> <input type="text" class="form-control" style="width:200px;"name="constructionCompany"
                        id="constructionCompany" value="${infrastructureProject.constructionCompany!}"
                        maxLength="100"/>
            </td>
            <td class="text-right">合同价（元）:</td>
            <td>  <input type="text" class="form-control" style="width:200px;" name="contractPrice"
                         dataType="float"    min="0"  vtype="number" decimallength="2" maxLength="15"
                         id="contractPrice" value="${infrastructureProject.contractPrice!}" />
            </td>
        </tr>
        <tr>
            <td class="text-right">建设时间:</td>
            <td>
                <div id="constructionTimeDiv"  class="input-group">
                    <input type="text" vtype="data" style="width:200px;"  class="form-control date-picker" nullable="true" name="constructionTime" id="constructionTime" placeholder="建设时间" value="${(infrastructureProject.constructionTime?string('yyyy-MM-dd'))!}">

                </div>
            </td>

        </tr>
        <tr>
            <td class="text-right">工程进度:</td>
            <td colspan="3" ><textarea class="form-control" style="width:500px;"  row="3" name="projectProgress"
                                       id="projectProgress" maxLength="1000">${infrastructureProject.projectProgress!}</textarea>
            </td>
        </tr>
        <tr>
            <td class="text-right">审计单位:</td>
            <td><input type="text" class="form-control" style="width:200px;"name="auditUnit" id="auditUnit"
                       value="${infrastructureProject.auditUnit!}" maxLength="100"/></td>
            <td class="text-right">审定价（元）:</td>
            <td>
                <input type="text" class="form-control" style="width:200px;"name="pricing" id="pricing"
                       dataType="float"    min="0"  vtype="number" decimallength="2" maxLength="15"
                       value="${infrastructureProject.pricing!}" />
            </td>
        </tr>
        </tbody>
    </table>
</form>

	<div class="base-bg-gray text-center">
        <a class="btn btn-blue" onclick="save();"   href="#">保存</a>
        <a class="btn btn-white" onclick="cancelOperate();"   href="#">返回</a>
    </div>

<script>
    $(function () {
        //初始化日期控件
        var viewContent={
            'format' : 'yyyy-mm-dd',
            'minView' : '2'
        };
        initCalendarData("#constructionTimeDiv",".date-picker",viewContent);
    });


    var isSubmit = false;
    function save(){
        if (isSubmit) {
            return;
        }
        isSubmit = true;
        if (!checkValue('#subForm')) {
            isSubmit = false;
            return;
        }


        var options = {
            url: "${request.contextPath}/infrastructure/project/save",
            dataType: 'json',
            success: function (data) {
                var jsonO = data;
                if (!jsonO.success) {
                    layerTipMsg(jsonO.success,"",jsonO.msg);
                    isSubmit = false;
                    return;
                } else {
                    layer.msg("保存成功");
                    cancelOperate();
                }
            },
            clearForm: false,
            resetForm: false,
            type: 'post',
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }//请求出错
        };
        $("#subForm").ajaxSubmit(options);
    }
   function cancelOperate(){
       $(".model-div").load("${request.contextPath}/infrastructure/project/index/page");
   }
</script>

