<#assign DEPLOY_REGION_BINJIANG = stack.findValue("@net.zdsoft.studevelop.data.constant.StuDevelopConstant@DEPLOY_BINJIANG") >
<script type="text/javascript" src="${resourceUrl}/components/jquery/dist/jquery.min.js"></script>
<script type="text/javascript" src="${resourceUrl}/js/LodopFuncs.js"></script>
<link rel="stylesheet" href="${resourceUrl}/css/pages.css" />
<link rel="stylesheet" href="${resourceUrl}/css/page-desktop.css" />
<link rel="stylesheet" href="${resourceUrl}/components/bootstrap/dist/css/bootstrap.min.css" />
<link rel="stylesheet" href="${resourceUrl}/css/components.css" />
<link rel="stylesheet" href="${resourceUrl}/css/iconfont.css" />

<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">报告单</h4>
	</div>
	<input type="hidden" id="batchIdLeft" value="${batchId!}">
    <input type="hidden" id="doNotPrint" value="${doNotPrint!}">
    <input type="hidden" id="acadyearPrint" value="${acadyear!}">
    <input type="hidden" id="semesterPrint" value="${semester!}">
	<div class="box-body">
		<div class="button-bar text-right">
			<button class="btn btn-sm btn-waterblue" onClick="printArea2();">打印</button>
			<button class="btn btn-sm btn-waterblue" onClick="searchStudent();">返回</button>
		</div>
		<div class="print">
		<#if deployRegion?default('') != DEPLOY_REGION_BINJIANG &&   stuDevelopQualityReportSet?exists>
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
<script>
function printArea2(){
	LODOP=getLodop();  
	<#if deployRegion?default('') != DEPLOY_REGION_BINJIANG  &&  stuDevelopQualityReportSet?exists>
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
        LODOP.ADD_PRINT_HTM("5mm","4mm","RightMargin:4mm","BottomMargin:4mm",getPrintContent(jQuery(".print")));
        LODOP.PREVIEW();
	</#if>
}
$(document).ready(function(){
	window.parent.onPrint();	
});

function getSubContent() {
	return getPrintContent(jQuery(".print"));
}
</script>