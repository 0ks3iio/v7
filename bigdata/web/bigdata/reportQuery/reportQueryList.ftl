<#if charts?exists && charts?size gt 0>
    <#list charts as chart>
    <div class="card-item" id="${chart.id!}">
        <div class="bd-view">


            <a href="javascript:void(0);" chart-id="${chart.id}" hidefocus="true">
                <div class="drive no-padding">
                    <img src="${chart.thumbnailPath!}" alt="">
                    <div class="coverage">
                        <div class="vetically-center">
                            <#if canDelete!>
                            <img src="${request.contextPath}/static/bigdata/images/remove.png" width="20" height="20"
                                 class="remove btn-chart-delete" onclick="deleteChart('${chart.id!}', '${chart.edit!?string}');" chart-id="${chart.id}" />
                            </#if>
                            <button chart-id="${chart.id}" onclick="editChart('${chart.id!}', '${chart.edit!?string}');" type="button" class="btn btn-blue btn-long">
                                <#if canDelete!>
                                 编辑
                                </#if>
                                <#if !canDelete!>
                                 查看
                                </#if>
                            </button>
                        </div>
                    </div>
                </div>
                <div class="padding-side-20">
                    <h3 style="overflow: hidden;">${chart.name!}</h3>
                    <#if canDelete!>
                        <p>授权类型：${chart.orderName!}</p>
                    </#if>
                    <div class="tag">
                    <#if chart.tagNameList?exists && chart.tagNameList?size gt 0>
                        <#list chart.tagNameList as tag>
                            <span>${tag!}</span>
                        </#list>
                    </#if>
                    </div>
                </div>
            </a>
        </div>
    </div>
    </#list>
<#else>
    <p style="margin-left: 10px">
    <#if isSearch?? && isSearch==true>
        搜索不到你要的结果，换个关键词试试哦～
    <#elseif isTagSearch?? && isTagSearch==true>
        搜索不到你要的结果，换个标签试试哦～
    <#else>
        列表空空，去新建吧～
    </#if>
    </p>
</#if>

<script>
    function imgHeight(){
        var $dh = ($('.drive').first().width()/4*3).toFixed(0);
        $('.drive').each(function (index,ele){
            $(this).css({
                'height': $dh
            })
        });
    }
    imgHeight();
    $(window).resize(function(){
        imgHeight();
    });
    $(function () {
        $('#_pageIndex').val('${pagination.pageIndex}');
        $('#_maxPageIndex').val('${pagination.maxPageIndex}');
        <#if pagination.pageIndex == pagination.maxPageIndex || pagination.maxPageIndex == 0>
            $('#_get_more').parent().hide();
        <#else>
            $('#_get_more').parent().show();
        </#if>
    })
</script>