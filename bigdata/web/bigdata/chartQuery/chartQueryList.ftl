<#if charts.content?exists && charts.content?size gt 0>
    <#list charts.content as chart>
    <div class="card-item" id="${chart.id!}">
        <div class="bd-view">
            <a href="javascript:void(0);" chart-id="${chart.id}">

                <div class="drive">
                    <img src="${chart.thumbnailPath!}" alt="">
                    <div class="coverage">
                        <div class="vetically-center">
                            <#--<img src="../images/big-data/remove.png" width="20" height="20" class="remove"/>-->
                            <button chart-id="${chart.id}" type="button" class="btn btn-blue btn-long">查看</button>
                        </div>
                    </div>
                </div>
                <div class="padding-side-20">
                    <h3 style="overflow: hidden;">${chart.name!}</h3>
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
    $(function () {
        $('#_pageIndex').val('${charts.getNumber}');
        $('#_maxPageIndex').val('${charts.getTotalPages}');
        <#if charts.getNumber == charts.getTotalPages || charts.getTotalPages == 0>
            $('#_get_more').parent().hide();
        <#else>
            $('#_get_more').parent().show();
        </#if>

        var $width=$('.drive>img').width();
        $('.drive>img').height($width*3/4);
    })
</script>