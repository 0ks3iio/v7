<#if showAdd>
<input type="button" value="Add" onclick="javascript:showData('${reportCode}', '${templatePath}', '${dataId!}');"  />
</#if>
<p />
<style>
td {border-style: solid; border-width: 1px; padding:5px;}
</style>
<#if showColumns?exists && showColumns != "">
<table >
<tr>
<#list showColumnNames?split(",") as scn>
<td>${scn}</td>
</#list>
<td>操作</td>
</tr>
<#list datas as data>
	<tr>
	<#list showColumns?split(",") as column>
	<td><span>${data[column]!}</span></td>
	</#list>
	<td><input type="button" value="修改" onclick="javascript:showData('${reportCode}', '${templatePath}', '${data.id!}');"  /></td>
	<tr>
</#list>
</table>
<#else>
<table >
<tr><td>发布单位</td><td>发布者</td><td>发布日期</td><td>学年</td><td>学期</td><td>操作</td></tr>
<#list datas as data>
	<tr>
	<td><span><$dc.unit.object.${data.unit_id!}@unitName$></span></td>
	<td><span><$dc.user.object.${data.create_user_id!}@realName$></span></td>
	<td><span>${data.creation_time!}</span></td>
	<td><span>${data.acadyear!}学年</span></td>
	<td><span>第${data.semester!}学期</span></td>
	<td><input type="button" value="修改" onclick="javascript:showData('${reportCode}', '${templatePath}', '${data.id!}');"  /></td>
	<tr>
</#list>
</table>
</#if>

<script>
function onAdd(code, path){
var url = "${request.contextPath}/dc/report/index?_reportCode=" + code + "&_viewName=" + path;
$("#model-div-super2").load(url);
}

function showData(code, path, dataId){
var url = "${request.contextPath}/dc/report/index?_dataId=" + dataId + "&_reportCode=" + code + "&_viewName=" + path;
$("#model-div-super2").load(url);
}
</script>

<@dcm.analyzeReport  />