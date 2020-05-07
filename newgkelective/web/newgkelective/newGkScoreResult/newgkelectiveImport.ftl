<#import "/fw/macro/dataImportMacro.ftl" as import />
<#--<a href="javascript:" class="page-back-btn goBack" onclick="goBack()"><i class="fa fa-arrow-left"></i> 返回</a>-->
<script>
//showBreadBack(goBack,false,"成绩导入");
//每个导入必须实现这个方法
function businessDataImport(){
	$('#busDataImport').addClass('disabled');
	//处理逻辑　并将参数组织成json格式　调用公共的导入方法
	var gradeId = '${gradeId!}';
	var params='{"gradeId":"'+gradeId+'"}';
	dataimport(params);
}

function scoreTable(){
    var url = '${request.contextPath}/newgkelective/newGkScoreResult/index?gradeId=${gradeId}';
    $("#gradeTableList").load(url);
}
/*function goBack(){
    var gradeId = '${gradeId!}';
    if(${showpage!} == '1'){
        var url = '${request.contextPath}/newgkelective/newGkScoreResult/index?unitId=${chioceId!}&gradeId='+gradeId;
    }else{
        var url = '${request.contextPath}/newgkelective/${chioceId!}/chosen/tabHead/page?gradeId='+gradeId;
    }
    $("#showList").load(url);
}*/
</script>
<#if showReturn="true">
	<button class="btn btn-blue" onClick="scoreTable()">返回成绩列表</button>
	<div style="height: 10px"></div>
</#if>
<@import.import businessName="${businessName!}" businessUrl="${businessUrl!}" templateDownloadUrl="${templateDownloadUrl!}" exportErrorExcelUrl="${exportErrorExcelUrl}" validateUrl="${validateUrl!}"  validRowStartNo="${validRowStartNo!}" objectName="${objectName!}" description="${description!}" businessKey="${businessKey!}" contextPath="${request.contextPath!}" resourceUrl="${resourceUrl!}">
<div class="import-step clearfix">
</@import.import>