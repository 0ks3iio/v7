<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="box box-default">
    <div class="box-body">
        <!-- PAGE CONTENT BEGINS -->
        <div class="filter clearfix">
            <div class="filter-item pull-right">
                <a class="btn btn-blue js-addTerm" onclick="goBack();">返回</a>
                <a class="btn btn-blue js-addTerm" onclick="doExport();">导出Excel</a>
            </div>
        </div><!-- 筛选结束 -->
        <div class="table-container-body js-scroll" id="showItemDiv">
            <table class="table table-bordered table-striped table-hover" width="100%">
                <thead>
                <tr>
                    <#if nameList?exists && nameList?size gt 0>
                    <#list nameList as item>
                        <th style="white-space: nowrap;">${item!}</th>
                    </#list>
                    </#if>
                </tr>
                </thead>
                <tbody>
                <#if detailList?exists && detailList?size gt 0>
                    <#list detailList as detail>
                        <tr>
                            <#list nameList as name>
                                <td style="white-space: nowrap;">${detail[name]!}</td>
                            </#list>
                        </tr>
                    </#list>
                <#else>
                    <tr>
                        <td colspan="${nameList?size}" class="text-center">暂无相关数据，请先新增！</td>
                    </tr>
                </#if>
                </tbody>
            </table>
        </div>
    </div>
</div>
<#if detailList?exists>
    <@htmlcom.pageToolBar container="#showList"/>
</#if>
<script>
    $(function(){
        $('.js-scroll').css({
            overflow: 'auto',
            height: $(window).innerHeight() - $('.js-scroll').offset().top - 100
        });
    });
    function goBack(){
        var url = "${request.contextPath}/scoremanage/plan/head/page?acadyear=${acadyear!}&semester=${semester!}";
        $("#showList").load(url);
    }

    function doExport(){
        window.location.href="${request.contextPath}/scoremanage/plan/export?planId=${planId!}";
    }
</script>
