<#import "/fw/macro/dataImportMacro.ftl" as import />
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/webuploader/webuploader.css">
<a href="javascript:void(0);" class="page-back-btn  " onclick="goBack();" ><i class="fa fa-arrow-left"></i> 返回</a>
<script>
    //每个导入必须实现这个方法
    function businessDataImport(){
    $('#busDataImport').addClass('disabled');
        //处理逻辑　并将参数组织成json格式　调用公共的导入方法
        var params='${monthPermance!}';
        dataimport(params);
    }
    function goBack() {
        $(".model-div").load("${request.contextPath}/studevelop/performance/index/page?acadyear=${acadyear}&semester=${semester}&classId=${classId}&performMonth=${performMonth!}");
    }
</script>
<@import.import businessName="${businessName}" businessUrl="${businessUrl}" templateDownloadUrl="${templateDownloadUrl}" objectName="${objectName}" description="${description}" businessKey="${businessKey}" contextPath="${request.contextPath}" resourceUrl="${resourceUrl!}">
<div class="import-step clearfix">
    <span class="import-step-num">✔</span>
    <div class="import-content">
        <p>选择每月在校表现相关属性</p>
        <div class="filter clearfix">
            <div class="filter-item">
                <label for="" class="filter-name">学年：</label>
                <div class="filter-content">
                    <select id="acadyear" name="acadyear" class="form-control" disabled="disabled">
                        <#if acadyearList?exists && (acadyearList?size gt 0)>
                            <#list acadyearList as item>
                                <option value="${item!}" <#if acadyear == item?default('')> selected </#if> >${item!}学年</option>
                            </#list>
                        <#else>
                            <option value="">暂无数据</option>
                        </#if>
                    </select>
                </div>
            </div>
            <div class="filter-item">
                <label for="" class="filter-name">学期：</label>
                <div class="filter-content">
                    <select  name="semester" id="semester" class="form-control" disabled="disabled" >
                    ${mcodeSetting.getMcodeSelect('DM-XQ',(semester?default(0))?string,'0')}
                    </select>
                </div>
            </div>
            <div class="filter-item">
                <span class="filter-name">班级：</span>
                <div class="filter-content">
                    <select name="classId" id="classId" class="form-control" disabled="disabled"  >
                        <#list classList as class>
                            <option value="${class.id!}" <#if classId== class.id >selected</#if>>${class.classNameDynamic!}</option>
                        </#list>

                    </select>
                </div>
            </div>
            <div class="filter-item">
                <span class="filter-name">月份：</span>
                <div class="filter-content">
                    <select name="performMonth" id="performMonth" class="form-control" disabled="disabled" >
                        <#list 1..12 as i>
                            <option value="${i}" <#if performMonth == i> selected </#if> >${i}月</option>
                        </#list>

                    </select>
                </div>
            </div>
        </div>
    </div>
</div>
</@import.import>