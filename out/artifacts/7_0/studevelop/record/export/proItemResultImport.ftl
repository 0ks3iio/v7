<#import "/fw/macro/dataImportMacro.ftl" as import />
<script>
    //每个导入必须实现这个方法
    function businessDataImport(){
    $('#busDataImport').addClass('disabled');
        //处理逻辑　并将参数组织成json格式　调用公共的导入方法
        var params='${monthPermance!}';
        dataimport(params);
    }
    function toResultBack(){
		$("#isExportId").val("0");//进入列表页
		$(".attBtn").show();
		var code = $("#code").find("li[class = 'active']").find("a").attr("val");
		doSearch(code);
	}
</script>
<@import.import businessName="${businessName}" businessUrl="${businessUrl}" templateDownloadUrl="${templateDownloadUrl}" objectName="${objectName}" description="${description}" businessKey="${businessKey}" contextPath="${request.contextPath}" resourceUrl="${resourceUrl!}" validateUrl="${validateUrl!}" validRowStartNo="${validRowStartNo?default(0)}">
   
</@import.import>
<script>
   $(function(){
   		$("#templateParams").val('${monthPermance!}');
   		<#if !canSave?default(false)>
   			$(".import-footer button").each(function(){
   				$(this).attr("disabled", true);
   			})
   		</#if>
   });
</script>