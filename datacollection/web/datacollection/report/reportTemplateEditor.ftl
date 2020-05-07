<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">编辑模板（<#if _templateType == "list">${_report.listTemplatePath!}<#else>${_report.templatePath!}</#if>）</h4>
		<p />
		<#if _templateType == "detail">
		<h4 class="box-title">URL:/dc/report/detailByDataId/${_report.id!}/${_dataId!}</h4>
		<#else>
		<h4 class="box-title">URL:/dc/report/listReportData/${_report.id!}</h4>
		</#if> 
	</div>
	<div class="box-body">
		<div>
			<textarea onchange="javascript:onChangeText();" id="content" name="content" type="text/plain" style="width:100%;height:400px;">${_templateContent!}</textarea>
		</div>
		<div class="text-right" style="margin-top:10px">
			覆盖&nbsp;<input type="checkbox" checked=true id="convered">（不选择的话，修改会新增模板）
			<a href="javascript:void(0);" class="btn btn-blue" onclick="saveTemplate();">保存</a>
			<@dcm.dcRdButton value="返回" url=_url templatePath=_viewPath reportId=_reportId checkReturn="rtnList()" />
		</div>
	</div>
</div>
<script>
function saveTemplate(){
	if(!confirm("模板修改会影响到数据的显示，请确保能充分理解此模板内容的修改，如果不确定修改的内容，请取消！！")){
		return false;
	}
		
	if($("#convered").prop("checked") == true){
		if(!confirm("现在勾选了“覆盖”选项，所有引用到此模板的都会发生改变，如果不确定是否有其他地方引用了，请去掉“覆盖”选项后再保存（会产生一个新的模板），是否要继续覆盖保存？")){
			return false;
		}
	}
	
	$.getJSON({
		url:"${request.contextPath}/dc/report/saveTemplate/${_reportId!}?_templateType=${_templateType!}&_convered=" + $("#convered").prop("checked"),
	 	data: $("#content").val(),  
	    type:'post',  
	    cache:false,  
	    contentType: "application/json",
	    success:function(data){
	    	alert(data.msg);
	    }
	});
	changed = false;
}
var changed = false;

function onChangeText(){
	changed = true;
}
function rtnList(){
	if(changed && !confirm("模板内容已经修改，尚未保存，确定要取消？")){
		return false;
	}
	return true;
}
</script>
