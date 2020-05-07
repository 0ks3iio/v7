<@dcm.reportData>
<table class="table  table-bordered table-hover">
<tr>
<th>操作</th>
<th>项目名称</th>
<th>项目编号</th>
<th>创建者</th>
<th>创建时间</th>
</tr>
<#list projects as project>
<tr>
<td>
<@dcm.dcRdButton value="查看" class="btn btn-green" url="/dc/report/listReportData/" + project.id + "?_viewPath=" />
</td>
<td>${project.reportName!}</td>
<td>${project.reportCode!}</td>
<td><@dcm.dcObjVal type="user" columnId="realName" id=project.createUserId /></td>
<td>${project.creationTime?string("yyyy-MM-dd HH:mm:ss")!}</td>
</tr>
</#list>
</table>

<script>
function showDatas(id){
var url = "${request.contextPath}/dc/report/listReportData/" + id + "?_viewPath=";
$("#model-div-super2").load(url);
}
</script>
</@dcm.reportData>