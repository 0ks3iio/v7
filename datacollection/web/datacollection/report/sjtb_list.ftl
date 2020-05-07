<@dcm.reportData  reportCode=_report.reportCode>
<@dcm.dcRdButton value="新增数据" templatePath="sjtb.ftl" url="/dc/report/detailByDataId/" + _report.id + "/0" reportId=_report.id />
<@dcm.dcEditTemplate value="编辑本模板" reportId=_report.id templateType="list" />
<@dcm.dcRdButton value="返回填报列表" templatePath="index.ftl" url="/dc/report/listReports" />
<p></p>

<table class="table  table-bordered table-hover"><tr>
<@dcm.columnOne label="名称" isCondition=true operation="like" columnId="report_name" operation="like" value=report_name /> 
<@dcm.columnOne label="类型" isCondition=true columnId="report_type"  value=report_type type="select" cselect=",-- 请选择 --;1,单位;2,个人;3,其他" /> 
<td><@dcm.dcRdButton isConditionButton=true value="查找" url="/dc/report/listReportData/" + _report.id reportId=_report.id /></td>
</tr></table>
<p ></p>

<table class="table  table-bordered table-hover">
<tr>
<th>操作</th>
<th>编号</th>
<th>名称</th>
<th>类型</th>
<th>创建单位</th>
<th>创建者</th>
<th>创建时间</th>
</tr>
<#list _datas as data>
<tr>
<td><@dcm.dcRdButton class="btn btn-orange" value="修改" url="/dc/report/detailByDataId/" + _report.id + "/" + data.id reportId=_report.id /></td>
<td>${data.report_code!}</td>
<td>${data.report_name!}</td>
<td><@dcm.dcCSelectVal cselect="1,单位;2,个人;3,其他" value=data.report_type! /></td>
<td><@dcm.dcObjVal type="unit" id=data.unit_id! columnId="unitName" /></td>
<td><@dcm.dcObjVal type="user" id=data.create_user_id! columnId="realName" /></td>
<td>${(data.creation_time)!}</td>
</tr>
</#list>
</table>
</@dcm.reportData>

