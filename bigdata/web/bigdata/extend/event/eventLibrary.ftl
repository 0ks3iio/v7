<#if eventFavorites?exists && eventFavorites?size gt 0>
    <#list eventFavorites as item>
        <tr>
            <td style="width: 60px;"><a href="javascript:void(0);" title="${item.favoriteName!}" style="display: block;width: 85px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;" onclick="editEventFavorite('${item.id!}')" id="${item.id!}">${item.favoriteName!}</a></td>
            <td>${(item.modifyTime?string('yyyy-MM-dd'))!""}</td>
            <td>
                <a href="javascript:void(0);" onclick="copyEventFavorite('${item.id!}')">复制条件</a>
                <a href="javascript:void(0);" onclick="exportEventFavorite('${item.id!}')">导出报表</a>
                <a href="javascript:void(0);" onclick="deleteEventFavorite('${item.id!}')" hidefocus="true">删除</a>
            </td>
        </tr>
    </#list>
<#else>
    <div class="no-data">
        <img src="${request.contextPath}/bigdata/v3/static/images/folder-settings/img-folder.png"/>
        <div>暂无数据</div>
    </div>
</#if>
<script>
    function deleteEventFavorite(id) {
        var title = '确定删除【' + $('#' + id).text() + "】？";
        layer.confirm(title, {btn: ['确定', '取消'], title: '删除', icon: 3, closeBtn: 0}, function (index) {
            layer.close(index);
            $.ajax({
                url: '${request.contextPath}/bigdata/event/deleteEventFavorite',
                type: 'POST',
                data: {
                    id: id
                },
                dataType: 'json',
                success: function (val) {
                    if (!val.success) {
                        layer.msg(val.message, {icon: 2});
                    } else {
                        layer.msg('删除成功', {icon: 1});
                        $('#' + id).parent().remove();
                    }
                }
            });
        });
    }

    function editEventFavorite(id) {
        var href = '${request.contextPath}/bigdata/event/query?favoriteId=' + id;
        routerProxy.go({
            path: href,
            level: 1,
            name: '事件分析'
        }, function () {
            $('.page-content').load("${springMacroRequestContext.contextPath}" + href);
        });
    }

    function copyEventFavorite(id) {
        var href = '${request.contextPath}/bigdata/event/query?favoriteId=' + id + "&isCopy=true";
        routerProxy.go({
            path: href,
            level: 1,
            name: '事件分析'
        }, function () {
            $('.page-content').load("${springMacroRequestContext.contextPath}" + href);
        });
    }

    function exportEventFavorite(id) {
        var href = '${request.contextPath}/bigdata/event/exportReportById?favoriteId=' + id;
        window.location.href = href;
    }
</script>