<@dcm.reportData  reportCode=_report.reportCode dataId=_dataId>
<p ></p>
<@dcm.dcEditTemplate value="编辑本模板" reportId=_report.id dataId=_dataId templateType="detail" />
<@dcm.saveButton preSave="doAutoReportCode2()" />
<@dcm.dcRdButton value="返回" url="/dc/report/listReportData/" + _report.id reportId=_report.id />
<p align="center">
<@dcm.dcDataVal reportCode=_report.reportCode dataId=_dataId columnId="report_name" />数据维护
</p>
<table align="center">
	<@dcm.rowOne label="名称" value=_data.report_name nullable=false columnId="report_name" colspan=3/>
	<tr height="30">
		<#if _data.report_code?default("") == "">
        <#assign expendHtml = "<input type='checkbox' ignoreSave=true id='autoReportCode' onclick='doAutoReportCode()' /><label for='autoReportCode'>自动生成</label>" />
		<@dcm.columnOne label="编号" value=_data.report_code nullable=false columnId="report_code" expendHtml=expendHtml />	
		<#else>
		<@dcm.columnOne label="编号" readonly=true value=_data.report_code nullable=false columnId="report_code" expendHtml=expendHtml />	
		</#if>
		<@dcm.columnOne label="类型" value=_data.report_type columnId="report_type" type="select" cselect="1,单位;2,个人;3,其他" />
	</tr>
        
        <@dcm.rowOne label="参数" value=_data.parameter columnId="parameter" colspan=5 />
	<@dcm.rowOne label="默认明细模板" value=_data.template_path columnId="template_path" colspan=3 />
	<@dcm.rowOne label="默认列表模板" value=_data.list_template_path columnId="list_template_path" colspan=3 />
</table>
<p ></p>
<script>
function doAutoReportCode(){
	var checked = $("#autoReportCode").prop("checked");
	if(checked == true){
		$("#report_code").attr("disabled", true);
		$("#report_code").val("自动生成");
	}
	else{
		$("#report_code").removeAttr("disabled");
		$("#report_code").val("");
	}
}

function doAutoReportCode2(){
	if("自动生成" != $("#report_code").val()){
		return true;
	}
	var rtn=false;
	$.getJSON({
	async:false,
	url:"${request.contextPath}/dc/report/maxReportCode",
	success:function(data){
		$("#report_code").val(data);
		$("#report_code").removeAttr("disabled");
		$("#autoReportCode").prop("checked", false)
		rtn = true;
	}
	});
	return rtn;
}
</script>
</@dcm.reportData>

