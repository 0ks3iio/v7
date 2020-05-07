<#assign DEPLOY_REGION_BINJIANG = stack.findValue("@net.zdsoft.studevelop.data.constant.StuDevelopConstant@DEPLOY_BINJIANG") >
<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">报告单</h4>
	</div>
	<div class="box-body">
		<#if !isOne?default(false)>
			<div class="button-bar text-right">
				<button class="btn btn-sm btn-waterblue" onClick="printArea2();">打印</button>
				<button class="btn btn-sm btn-waterblue" onClick="backToList();">返回</button>
			</div>
		</#if>
		<div class="print">
            <#--&&  stuDevelopQualityReportSet?exists-->
		<#if deployRegion?default('') != DEPLOY_REGION_BINJIANG &&  stuDevelopQualityReportSet?exists >
		    <#if stuDevelopQualityReportSet.section == 1>
		         <#if stuDevelopQualityReportSet.template == '1'>
		             <#include "/studevelop/query/stuQualityReportTemplate.ftl">
		         <#elseif stuDevelopQualityReportSet.template == '2'>
		             <#include "/studevelop/query/stuQualityReportTemplate3.ftl">
		         </#if>
		    <#elseif stuDevelopQualityReportSet.section == 2>
		         <#if stuDevelopQualityReportSet.template == '1'>
		             <#include "/studevelop/query/stuQualityReportTemplate2.ftl">
		         <#elseif stuDevelopQualityReportSet.template == '2'>
		             <#include "/studevelop/query/stuQualityReportTemplate4.ftl">
		         </#if>
		    </#if>
		 <#else>
			<#include "/studevelop/query/stuQualityReportTemplate5.ftl">
		</#if>
		</div>				 				
	</div>
</div>
<script src="${request.contextPath}/static/js/LodopFuncs.js" />
<script>

$(function(){
	var studentId = '${studentId!}';
	$('#studentId').val(studentId);
});
function backToList(){
	$('#studentId').val("");
	searchStudent();
}
function printArea2(){
		LODOP=getLodop();  
		<#if deployRegion?default('') != DEPLOY_REGION_BINJIANG  && stuDevelopQualityReportSet?exists>
		    <#if stuDevelopQualityReportSet.section == 2 && stuDevelopQualityReportSet.template == '2'>
                LODOP.SET_PRINT_PAGESIZE(1, 0, 0,"A4");
	  	        LODOP.ADD_PRINT_HTM("5mm","5mm","RightMargin:5mm","BottomMargin:5mm",getPrintContent(jQuery(".print")));
	  	        LODOP.PREVIEW();
	  	    <#else>
	  	        LODOP.SET_PRINT_PAGESIZE(2, 0, 0,"A4");
		        LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);
	  	        LODOP.ADD_PRINT_HTM("4mm","4mm","RightMargin:5mm","BottomMargin:5mm",getPrintContent(jQuery(".print")));
	  	        LODOP.PREVIEW();
		    </#if>
		<#else>
            LODOP.SET_PRINT_PAGESIZE(1, 0, 0,"A4");
            LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);
            LODOP.ADD_PRINT_HTM("5mm","4mm","RightMargin:4mm","BottomMargin:2mm",getPrintContent(jQuery(".print")));
            LODOP.PREVIEW();
		</#if>		
}
</script>