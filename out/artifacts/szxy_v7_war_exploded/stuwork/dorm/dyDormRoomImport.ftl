<#import "/fw/macro/dataImportMacro.ftl" as import />
<a href="javascript:" class="page-back-btn goBack" onclick="goBack()"><i class="fa fa-arrow-left"></i> 返回</a>
<script>
    //每个导入必须实现这个方法
    function businessDataImport(){
        $('#busDataImport').addClass('disabled');
        //处理逻辑　并将参数组织成json格式　调用公共的导入方法
        var acadyear=$("#acadyearStr").val();
        var semester=$("#semesterStr").val();
        var buildingId = $("#buildingId").val();
        var buildingName = $("#buildingName").val();
        var roomType = $("#roomType").val();
        var params='{"acadyear":"'+acadyear+'","semester":"'+semester+'","buildingId":"'+buildingId+'","buildingName":"'+buildingName+'","roomType":"'+roomType+'"}';
        dataimport(params);
    }

    function goBack(){
        var buildingId=$("#buildingId").val();
        var acadyear=$("#acadyearStr").val();
        var semester=$("#semesterStr").val();
        var roomType = $("#roomType").val();
        if(acadyear==undefined){
            acadyear="";
        }
        if(semester==undefined){
            semester="";
        }
        if(buildingId==undefined){
            buildingId="";
        }
        if(roomType==undefined){
            roomType="";
        }
        $("#itemShowDivId").load("${request.contextPath}/stuwork/dorm/room/index/page?acadyearStr="+acadyear+"&semester="+semester+"&buildingId="+buildingId+"&roomType="+roomType);
    }
</script>

<@import.import businessName="${businessName}" businessUrl="${businessUrl}" templateDownloadUrl="${templateDownloadUrl}" objectName="${objectName}" description="${description}" businessKey="${businessKey}" contextPath="${request.contextPath}" resourceUrl="${resourceUrl}">
<div class="import-step clearfix">
    <span class="import-step-num">✔</span>
    <div class="import-content">
        <p>导入信息相关属性</p>
        <div class="filter clearfix">
            <input type="hidden" name="buildingId" id="buildingId" value="${buildingId!}">
            <input type="hidden" name="buildingName" id="buildingName" value="${buildingName!}">
            <input type="hidden" name="roomType" id="roomType" value="${roomType!}">
            <#if !(buildingName?default("")=="")>
                <div class="filter-item">
                    <label for="" class="filter-name">寝室楼：</label>
                    <div class="filter-content">
                    ${buildingName!}
                    </div>
                </div>
            </#if>
        </div>
    </div>
</div>
</@import.import>