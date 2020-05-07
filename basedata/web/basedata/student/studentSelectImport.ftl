<#import "/fw/macro/dataImportMacro.ftl" as import />
<#--<a href="javascript:" class="page-back-btn goBack" onclick="goBack()"><i class="fa fa-arrow-left"></i> 返回</a>-->
<script>
    $(function () {
        $(".row .box-default").css({"box-shadow":"0px 1px 3px rgba(255, 255, 255, 0.6)"});
        $("#subSelName").append($("#acadyear option:selected").text() + "学年第" + $("#semester option:selected").val() + "学期" + $("#gradeId option:selected").text() + "年级选课");
    });

    function businessDataImport(){
        $('#busDataImport').addClass('disabled');
        //处理逻辑　并将参数组织成json格式　调用公共的导入方法
        let params='{"acadyear":"'+$("#acadyear option:selected").val()+'","semester":"'+$("#semester option:selected").val()+'","gradeId":"'+$("#gradeId option:selected").val()+'"}';
        dataimport(params);
    }
</script>
<@import.import businessName="${businessName!}" businessUrl="${businessUrl!}" templateDownloadUrl="${templateDownloadUrl!}" exportErrorExcelUrl="${exportErrorExcelUrl!}" objectName="${objectName!}" description="${description!}" businessKey="${businessKey!}" contextPath="${request.contextPath!}" resourceUrl="${resourceUrl!}" validateUrl="${validateUrl!}"  validRowStartNo="${validRowStartNo!}">
<div class="import-step clearfix">
    <span class="import-step-num">✔</span>
    <div class="import-content">
        <p>导入信息相关属性</p>
        <div class="filter clearfix">
            <div class="filter-item">
                <div id="subSelName" class="filter-content">

                </div>
            </div>
        </div>
    </div>
</div>
</@import.import>