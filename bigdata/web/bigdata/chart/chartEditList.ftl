<#if charts?exists && charts?size gt 0>
    <#list charts as chart>
    <div class="card-item" id="${chart.id!}">
        <div class="bd-view">
            <#--<#if chart.delete>-->
            <#--<button type="button" class="btn btn-default btn-xs pop-up btn-chart-delete" chart-id="${chart.id}">-->
                <#--<i class="fa fa-trash"></i>-->
            <#--</button>-->
            <#--</#if>-->
            <a href="javascript:void(0);" chart-id="${chart.id}" edit="${chart.edit?string}">
            <#--<h3 style="overflow: hidden;">${chart.name!}</h3>-->
                <div class="drive">
                    <img src="${chart.thumbnailPath!}" alt="" style="width: 238px; height: 140px;">
                    <div class="coverage">
                        <div class="vetically-center">
                            <#if chart.delete?default(true)>
                            <img src="${request.contextPath}/static/bigdata/images/remove.png" width="20" height="20"
                                 class="remove btn-chart-delete" chart-id="${chart.id}" edit="${chart.edit?string}"/>
                            <img src="${request.contextPath}/static/bigdata/images/copy.png" width="20" height="20"
                                 class="js-copy" chart-id="${chart.id}" edit="${chart.edit?string}"/>
                            </#if>
                            <button type="button" class="btn btn-blue btn-long btn-chart-edit" chart-id="${chart.id}" edit="${chart.edit?string}">编辑</button>
                        </div>
                    </div>
                </div>
                <div class="padding-side-20">
                    <h3 style="overflow: hidden;">${chart.name!}</h3>
                    <p>授权类型：${chart.orderName!}</p>
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
        $('#_pageIndex').val('${pagination.pageIndex}');
        $('#_maxPageIndex').val('${pagination.maxPageIndex}');
        <#if pagination.pageIndex == pagination.maxPageIndex || pagination.maxPageIndex == 0>
            $('#_get_more').parent().hide();
        <#else>
            $('#_get_more').parent().show();
        </#if>

        var $width=$('.drive>img').width();
        $('.drive>img').height($width*3/4);

        $('.js-copy').on('click', function () {
            var id = $(this).attr('chart-id');
            layer.prompt({title: '请输入新的图表名称', formType: 3, maxlength: 50}, function (cockpitName, index) {
                layer.close(index);
                $.ajax({
                    url: '${request.contextPath}/bigdata/chart/clone',
                    type: 'POST',
                    data: {
                        name: cockpitName,
                        id: id
                    },
                    dataType: 'json',
                    success: function (res) {
                        if (res.success) {
                            searchChart();
                        } else {
                            layer.msg(res.message, {icon: 2});
                        }
                    },
                    error: function () {
                        layer.msg("网络异常", {icon: 2});
                    }
                });
            });
        })
    })
</script>