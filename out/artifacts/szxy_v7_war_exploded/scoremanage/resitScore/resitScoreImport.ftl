<#import "/fw/macro/dataImportMacro.ftl" as import />
<script>
    //每个导入必须实现这个方法
    function businessDataImport(){
        $('#busDataImport').addClass('disabled');
        //处理逻辑　并将参数组织成json格式　调用公共的导入方法
        var acadyear = '${acadyear!}';
        var semester = '${semester!}';
        var examId = '${examId!}';
        var gradeId = '${gradeId!}';
        var params='{"acadyear":"'+acadyear+'","semester":"'+semester+'","examId":"'+examId+'","gradeId":"'+gradeId+'"}';
        dataimport(params);
    }

    $(function(){
        showBreadBack(toBack,false,"补考管理");
    });

    function toBack(){
        var acadyear = $('#acadyear').val();
        var semester = $('#semester').val();
        var examId = $('#examId').val();
        var gradeId = $('#gradeId').val();
        var url="${request.contextPath}/scoremanage/resitScore/resitInfoList?acadyear="+acadyear+"&semester="+semester+"&examId="+examId+"&gradeId="+gradeId;
        $('#resitInfoList').load(url);
    }
</script>
<@import.import businessName="${businessName!}" businessUrl="${businessUrl!}" templateDownloadUrl="${templateDownloadUrl!}" objectName="${objectName!}" description="${description!}" businessKey="${businessKey!}" contextPath="${request.contextPath!}" resourceUrl="${resourceUrl!}">
</@import.import>