<#import "/fw/macro/dataImportMacro.ftl" as import />
<a href="javascript:" class="page-back-btn goBack" onclick="goBack()"><i class="fa fa-arrow-left"></i> 返回</a>
<script>
//每个导入必须实现这个方法
function businessDataImport(){
	$('#busDataImport').addClass('disabled');
	//处理逻辑　并将参数组织成json格式　调用公共的导入方法
	var params="{'acadyear':'${acadyear!}','semester':'${semester!}','studentId':'${studentId!}'}";
	dataimport(params);
}

function goBack(){
    $("#importDiv").load("${request.contextPath}/studevelop/scoreRecord/index/page");
}
</script>
<#assign s=description+"<br>
<div>	
<div>成绩登记</div><div><img src='${request.contextPath}/studevelop/images/importModel1.png' alt='用例图片1' /></div>
<div>报告单成绩</div><div><img src='${request.contextPath}/studevelop/images/importModel2.png' alt='用例图片2' /></div>
</div>">
<@import.import businessName="${businessName}" businessUrl="${businessUrl}" templateDownloadUrl="${templateDownloadUrl}" objectName="${objectName}" description="${s!}" businessKey="${businessKey}" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}">
✔  &nbsp;&nbsp;&nbsp;    导入条件：${acadyear!}学年，第<#if '${semester!}'=='1'>一<#else>二</#if>学期
</@import.import>

