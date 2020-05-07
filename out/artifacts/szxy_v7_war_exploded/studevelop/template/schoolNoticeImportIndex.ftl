<div class="filter-item filter-item-right">
    <a href="javascript:;" class="btn btn-blue"  onclick="toTeacherGroupList();">返回</a>
</div>
<#--**********Start********-->
<#import "/fw/macro/dataImportMacro.ftl" as import />
<script>
    //每个导入必须实现这个方法
    function businessDataImport(){
        //处理逻辑　并将参数组织成json格式　调用公共的导入方法
        var params='${monthPermance!}';
        dataimport(params);
    }

</script>
<@import.import businessName="${businessName}" businessUrl="${businessUrl}" templateDownloadUrl="${templateDownloadUrl}" objectName="${objectName}" description="${description}" businessKey="${businessKey}" contextPath="${request.contextPath}" resourceUrl="${resourceUrl!}" validateUrl="${validateUrl!}" validRowStartNo="${validRowStartNo?default(0)}">

</@import.import>
<script>
    $(function(){
        $("#templateParams").val('${monthPermance!}');
    });
</script>

<#--**********End********-->
</div>
<script>
    function toTeacherGroupList(){
        var url = '${request.contextPath}/basedata/teachgroup/list/index/page';
        $('#itemShowDiv').load(url);
    }
</script>