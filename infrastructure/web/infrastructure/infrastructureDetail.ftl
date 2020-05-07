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
            <td>${infrastructureProject.projectSchool!}
            </td>
            <td class="text-right">项目名称:</td>
            <td>${infrastructureProject.projectName!}
            </td>
        </tr>
        <tr>
            <td class="text-right">项目性质:</td>
            <td>
            ${mcodeSetting.getMcode("DM-XMXZ", infrastructureProject.projectNature?default(''))}
            </td>
            <td class="text-right">勘察单位:</td>
            <td>  ${infrastructureProject.surveyUnit!}
            </td>
        </tr>
        <tr>
            <td class="text-right">测绘单位:</td>
            <td> ${infrastructureProject.surveyingUnit!}
            </td>
            <td class="text-right">效果图设计单位:</td>
            <td> ${infrastructureProject.renderingDesignUnit!}
            </td>
        </tr>
        <tr>
            <td class="text-right">图纸设计单位:</td>
            <td> ${infrastructureProject.drawingDesignUnit!}
            </td>
            <td class="text-right">图纸审查单位:</td>
            <td> ${infrastructureProject.drawingReviewUnit!}
            </td>
        </tr>
        <tr>
            <td class="text-right">造价单位::</td>
            <td> ${infrastructureProject.costUnit!}
            </td>
            <td class="text-right">控制价（元）:</td>
            <td> ${infrastructureProject.controlPrice!}
            </td>
        </tr>
        <tr>
            <td class="text-right">代理机构:</td>
            <td>${infrastructureProject.agency!}
            </td>
            <td class="text-right">监理单位:</td>
            <td>  ${infrastructureProject.supervisoryUnit!}
            </td>
        </tr>
        <tr>
            <td class="text-right">施工企业:</td>
            <td> ${infrastructureProject.constructionCompany!}
            </td>
            <td class="text-right">合同价（元）:</td>
            <td>  ${infrastructureProject.contractPrice!}
            </td>
        </tr>
        <tr>
            <td class="text-right">建设时间:</td>
            <td>
            ${(infrastructureProject.constructionTime?string('yyyy-MM-dd'))!}
            </td>

        </tr>
        <tr>
            <td class="text-right">工程进度:</td>
            <td colspan="3" class="table-print-textarea" >${infrastructureProject.projectProgress!}
            </td>
        </tr>
        <tr>
            <td class="text-right">审计单位:</td>
            <td>${infrastructureProject.auditUnit!}</td>
            <td class="text-right">审定价（元）:</td>
            <td>
            ${infrastructureProject.pricing!}
            </td>
        </tr>
        </tbody>
    </table>
</form>


