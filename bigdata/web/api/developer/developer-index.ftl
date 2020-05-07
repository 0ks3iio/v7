<div class="box box-default scroll-height">
    <div class="table-container">
            <#if developers?? && developers?size gt 0>
                <div class="table-container-body">
                    <table class="tables">
                        <thead>
                        <tr>
                            <th>姓名</th>
                            <th>应用数量</th>
                            <th>注册时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list developers as developer>
                        <tr>
                            <td>${developer.realName}</td>
                            <td>${developer.appNumber!0}</td>
                            <td>${developer.creationTime?string('yyyy-MM-dd')}</td>
                            <td>
                                <a href="javascript:void(0);" data-id="${developer.id!}" class="developer-view">查看</a>
                                <a href="javascript:void(0);" data-id="${developer.id!}" class="developer-reset">重置密码</a>
                                <#if developer.hasApply>
                                <span class="badge badge-blue badge-sm">有待审核信息</span>
                                </#if>
                            </td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
            <#else >
                <div class="wrap-1of1 centered no-data-state">
                    <div class="text-center">
                        <img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png">
                        <p>暂无数据</p>
                    </div>
                </div>
            </#if>

    </div>
</div>
<script>
    $(function () {
        $('.developer-view').click(function () {
            var developerId = $(this).data('id');
            router.go({
                path: '/bigdata/developer/view?developerId=' + developerId,
                type: 'item',
                name: '开发者信息',
                length: 3,
            }, function () {
                $('.page-content').load('${request.contextPath}/bigdata/developer/view?developerId=' + developerId);
            })
        });
        $('.developer-reset').click(function () {
            var developerId = $(this).data('id');
            var index = layer.load(2);
            $.post('${request.contextPath}/bigdata/developer/reset-password', {developerId: developerId}, function (res) {
                layer.close(index);
                if (res.success) {
                    showLayerTips('success', '密码重置成功', 't');
                } else {
                    showLayerTips('error', res.message, 't');
                }
            })
        })
    });
</script>