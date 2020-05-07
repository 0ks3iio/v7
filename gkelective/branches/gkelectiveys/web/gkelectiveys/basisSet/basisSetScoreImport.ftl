<#import "/fw/macro/dataImportMacro.ftl" as import />
<script>
    //每个导入必须实现这个方法
    function businessDataImport(){
    	$('#busDataImport').addClass('disabled');
        //处理逻辑　并将参数组织成json格式　调用公共的导入方法
        var params='${monthPermance!}';
        dataimport(params);
    }
    function toFilterBack(){
    	url = '${request.contextPath}/gkelective/${arrangeId}/basisSet/score/index/page';
		$("#itemShowDivId").load(url);
    }
</script>
<div class="text-right">
	<a href="javascript:" class="btn btn-blue"  onclick="toFilterBack();">返回</a>
</div>
<@import.import businessName="${businessName}" businessUrl="${businessUrl}" templateDownloadUrl="${templateDownloadUrl}" objectName="${objectName}" description="${description}" businessKey="${businessKey}" contextPath="${request.contextPath}" resourceUrl="${resourceUrl!}" validateUrl="${validateUrl!}" validRowStartNo="${validRowStartNo?default(0)}">

</@import.import>
