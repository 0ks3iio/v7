<#import "/fw/macro/dataImportMacro.ftl" as import />
<a href="javascript:" class="page-back-btn goBack" onclick="bac()"><i class="fa fa-arrow-left"></i> 返回</a>
<script>
    //每个导入必须实现这个方法
    function businessDataImport(){
        $('#busDataImport').addClass('disabled');
        //处理逻辑　并将参数组织成json格式　调用公共的导入方法
        var params="{'acadyear':'${acadyear!}','semester':'${semester!}'}";
        dataimport(params);
        hasImport = 1;
    }
    function bac() {
        if(hasImport == 0){
            $("#showTabDiv").html(container);
        }else {
            var studentId = $("#myStuId").val()
            stuClick(studentId);
            hasImport = 0;
            $("#myStuId").val("");
        }
    }

</script>
<@import.import businessName="${businessName}" businessUrl="${businessUrl}" templateDownloadUrl="${templateDownloadUrl}" objectName="${objectName}" description="${description}" businessKey="${businessKey}" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}">
    ✔  &nbsp;&nbsp;&nbsp;    导入条件：${acadyear!}学年，第<#if '${semester!}'=='1'>一<#else>二</#if>学期
</@import.import>