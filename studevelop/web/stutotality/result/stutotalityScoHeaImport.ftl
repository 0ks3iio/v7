<#import "/fw/macro/dataImportMacro.ftl" as import />
<a href="javascript:" class="page-back-btn goBack" onclick="bac()"><i class="fa fa-arrow-left"></i> 返回</a>
<script>
    //每个导入必须实现这个方法
    function businessDataImport(){
        $('#busDataImport').addClass('disabled');
        //处理逻辑　并将参数组织成json格式　调用公共的导入方法
        var params="{'acadyear':'${year!}','semester':'${semester!}'}";
        dataimport(params);
    }
    function bac() {
        var importType = '${importType!}';
        if(importType==0){
            var studentId = $("#myStuId").val();
            if(studentId){
                var url = '${request.contextPath}/stutotality/result/getStuPhysicalQualityList?studentId='+studentId;
                $("#showTabDiv").load(url);
            }else {
                $("#showTabDiv").html(container)
            }
        }else {
            var studentId=$("#studentId").val();
            if(studentId && studentId!=""){
                showStu(studentId);
            }else{
                var classId=$("#classId").val();
                showClass(classId);
            }
            //$("#showTabDiv").html(container);
        }
    }
</script>
<@import.import businessName="${businessName}" businessUrl="${businessUrl}" templateDownloadUrl="${templateDownloadUrl}" objectName="${objectName}" description="${description}" businessKey="${businessKey}" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}">
    ✔  &nbsp;&nbsp;&nbsp;    导入条件：${year!}学年，第<#if '${semester!}'=='1'>一<#else>二</#if>学期,${className!}
</@import.import>