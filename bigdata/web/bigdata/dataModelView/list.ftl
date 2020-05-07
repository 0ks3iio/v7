<#if favorites?exists && favorites?size gt 0>
    <#list favorites as chart>
    <div class="card-item" id="${chart.id!}">
        <div class="bd-view">
            <a href="javascript:void(0);" chart-id="${chart.id}" >
                <div class="drive">
                    <img src="${request.contextPath}/static/bigdata/images/thumbnail-default.png" alt="">
                    <div class="coverage">
                        <div class="vetically-center">
                            <#if canDelete!>
                                <img src="${request.contextPath}/static/bigdata/images/remove.png" width="20" height="20"
                                     class="remove btn-chart-delete" onclick="deleteModelFavorite('${chart.id!}', '${chart.canDelete!?string}');" chart-id="${chart.id}" />
                            </#if>
                            <button chart-id="${chart.id}" onclick="showReport('${chart.id!}')" type="button" class="btn btn-blue btn-long">
                                 查看
                            </button>
                            &nbsp;
                            <button chart-id="${chart.id}" onclick="editReport('${chart.id!}')" type="button" class="btn btn-blue btn-long">
                                 编辑
                            </button>
                        </div>
                    </div>
                </div>
                <div class="padding-side-20">
                    <h3 style="overflow: hidden;">${chart.favoriteName!}</h3>
                    <#if canDelete!>
                        <div class="js-power row">
                            <div class="col-sm-4 no-padding">授权类型：</div>
                            <div class="col-sm-8 no-padding">
                                <div class="filter" id="orderTypeSelect_${chart.id!}"
                                     edit="<#if currentUnitId?default('')==chart.unitId?default('-1')>true<#else>false</#if>"
                                     chart-id="${chart.id!}" orderType="${chart.orderType?default(2)}">
                                            <span>
                                            <#if chart.orderType?default(2) == 2>
                                                公开
                                            <#elseif chart.orderType?default(2) == 1>
                                                私有
                                            <#elseif chart.orderType?default(2) == 3>
                                                本单位公开
                                            <#elseif chart.orderType?default(2) == 4>
                                                单位授权
                                            <#elseif chart.orderType?default(2) == 5>
                                                单位授权个人需授权
                                            <#elseif chart.orderType?default(2) == 6>
                                                个人授权
                                            </#if>
                                            </span>
                                    <i class="fa fa-pencil"></i>
                                </div>
                            </div>
                        </div>
                    </#if>
                    <div class="tag">
                    <#if chart.tags?exists && chart.tags?size gt 0>
                        <#list chart.tags as tag>
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

        $('.js-power .filter').click(function aa() {
            if ($(this).attr('edit') == 'false') {
                layer.msg('该报表是由上级单位创建的，您不能修改！', {icon: 4, time:1500});
                return;
            }
            var orderType = $(this).attr("orderType");
            var $input = '<div class="filter-item no-margin">' +
                    '<div class="filter-content">' +
                    '<select name="" id="js-select" onchange="changeOrderType(this);" class="js-select selected form-control" placeholder="请输入模板名称">' +
                    <#if orderTypes?? && orderTypes?size gt 0>
                        <#list orderTypes as ot>
                                '<option value="${ot.orderType}">${ot.orderName!}</option>' +
                        </#list>
                    </#if>
                    '</select></div></div>';
            var $father = $(this);
            let jQueryInput = $($input);
            $(this).html(jQueryInput.html());
            $(this).find('option[value="' + orderType + '"]').attr("selected", true);
            $('#js-select').click(function () {
                return false
            });	//阻止表单默认点击行为
            $('#js-select').trigger('focus');
            // $('.js-select option:eq(0)').attr('selected','selected');
            $('.js-select').blur(function () {
                var $newText = $('.js-select option:selected').text() + '&nbsp;&nbsp;<i class="fa fa-pencil"></i>';
                $father.html($newText)
            })

        });
    });
    
    function showReport(id) {
        var href = '/bigdata/model/report/view?id=' + id + '&isEdit=' + '${canDelete!?string}' + '&showTitle=true';
        routerProxy.go({
            path: href,
            level: 3,
            name: '多维报表'
        }, function () {
            $('.page-content').load("${springMacroRequestContext.contextPath}" + href);
        });
    }

    function changeOrderType(el) {
        var orderType = $(el).val();
        var id = $(el).parent().parent().attr("chart-id");
        $.ajax({
            url: '${request.contextPath}/bigdata/model/report/changeOrderType',
            data: {id:id, orderType:orderType},
            type: 'POST',
            dataType: 'json',
            success: function (val) {
                if (!val.success) {
                    layer.msg(val.message, {icon: 2, time: 2000});
                }
                $('#orderTypeSelect_' + id).attr('orderType', orderType);
            }
        });
    }
</script>