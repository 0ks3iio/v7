<#import "/fw/macro/dataImportMacro.ftl" as import />
<#--<a href="javascript:" class="page-back-btn goBack" onclick="goBack()"><i class="fa fa-arrow-left"></i> 返回</a>-->
<script>
    //每个导入必须实现这个方法
    function businessDataImport(){
        $('#busDataImport').addClass('disabled');
        //处理逻辑　并将参数组织成json格式　调用公共的导入方法
        var params="{'acadyear':'${acadyear!}','semester':'${semester!}'}";
        dataimport(params);
    }

    $(function () {
        // $(".float-left _back_div").hide();
        showBreadBack(function () {
            goBack()
        },true,"返回");
    })
    function goBack(){
        var url =  '${request.contextPath}/exammanage/credit/creditStatSumScore?gradeId='+'${gradeId}'+"&year="+'${year}'+"&gradeName="+'${gradeName}'+"&semester="+'${semester}';
        $("#mydiv").load(url);
        <#--var url =  '${request.contextPath}/exammanage/credit/creditStatSumScore?gradeId='+gradeId+"&year="+year+"&gradeName="+gradeName+"&semester="+semester;-->
        <#--$("#mydiv").load(url);-->
    }
</script>
<@import.import businessName="${businessName}" businessUrl="${businessUrl}" templateDownloadUrl="${templateDownloadUrl}" objectName="${objectName}" description="${description}" businessKey="${businessKey}" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}">
</@import.import>