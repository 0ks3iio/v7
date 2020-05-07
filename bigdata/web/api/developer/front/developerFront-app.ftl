<#import '../../app/appStatusMacro.ftl' as appStatus/>
<div class="row dev-app-box ">
    <div class="col-xs-12">
        <p>
            <button class="btn btn-blue" id="app-front-add">新增应用</button>
        </p>
        <div class="row ">
            <#if apps?? && apps?size gt 0>
                <#list apps as app>
                <div class="col-sm-6 box" id="app_container_${app.id!}">
                    <div class="box box-default">
                        <div class="row box-body base-apply">
                            <div class="app-basic-group">
                                <div class="btn-group fn-left">
                                    <i class="iconfont icon-ellipsis-fill fa-trash dropdown-toggle"></i>
                                    <ul class="dropdown-menu">
                                        <li><a data-id="${app.id}" class="app-front-delete" href="javascript:void(0);" hidefocus="true">删除</a></li>
                                    </ul>
                                </div>
                            </div>
                            <div class="col-md-2 col-sm-12">
                                <div class="base-apply-img"><img src="${fileUrl}/${app.iconUrl!}" alt="" /></div>
                            </div>
                            <div class="col-md-10 col-sm-12">
                                <div class="base-apply-title">${app.name}
                                    <@appStatus.appStatus status=app.status />
                                </div>
                                <div class="base-apply-time">${app.creationTime?string('yyyy-MM-dd')}</div>
                                <div class="base-apply-des">
                                    ${app.description!}
                                </div>
                                <div class="text-right">
                                    <button data-id="${app.id}" type="button" class="btn btn-primary app-front-view">查看</button>
                                    <#if app.status==3 || app.status==4>
                                    <button data-id="${app.id}" type="button" class="btn btn-primary app-front-submit">提交</button>
                                    </#if>
                                    <#if app.status!=5>
                                    <button data-id="${app.id}" type="button" class="btn btn-default app-front-modify">修改</button>
                                    </#if>
                                    <#if app.status==4 || app.status==3>
                                    <button data-id="${app.id}" type="button" class="btn btn-danger app-front-delete">删除</button>
                                    </#if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                </#list>
            <#else >
                <div class="no-data-common">
                    <div class="text-center">
                        <img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
                        <p class="color-999">暂无数据请在上方点击"新增应用"按钮添加</p>
                    </div>
                </div>
            </#if>
        </div>

        <!-- PAGE CONTENT ENDS -->
    </div><!-- /.col -->
</div>
<script>
    $(function () {
        $('#app-front-add').click(function () {
            modify('');
        });
        $('.app-front-modify').click(function () {
            modify($(this).data('id'));
        });
        $('.app-front-view').click(function () {
            var id = $(this).data('id');
            router.go({
                path: '/developer/front/app/view?id='+id,
                level: 1,
                name: '应用详情'
            }, function () {
                $('.page-content').load('${request.contextPath}/bigdata/api/developer/front/app/view?id=' + id);
            })
        });
        $('.app-front-delete').click(function () {
            var id = $(this).data('id');
            showConfirmTips('warn', '提示', '删除操作数据不可恢复，是否删除该应用？', function () {
                $.get('${request.contextPath}/bigdata/api/developer/front/app/delete', {id: id}, function (res) {
                    if (res.success) {
                        showLayerTips('success', '删除成功', 't', function () {
                            $('#app_container_' + id).remove();
                        })
                    } else {
                        showLayerTips('error', '删除失败', 't')
                    }
                })
            });
        });
        $('.app-front-submit').click(function () {
            var id = $(this).data('id');
            $.get('${request.contextPath}/bigdata/api/developer/front/app/commit', {id: id}, function (res) {
                if (res.success) {
                    showLayerTips('success', '提交成功', 't', function () {
                        router.reload({
                            path: '/bigdata/api/developer/front/app/index',
                            name: '应用管理',
                            level: '0'
                        })
                    })
                } else {
                    showLayerTips('error', '提交失败', 't')
                }
            })
        });
    });

    function modify(id) {
        router.go({
            path: id ? '/developer/front/app/edit?id='+id : '/developer/front/app/add',
            level: 1,
            name: id ? '修改应用' : '新增应用'
        }, function () {
            $('.page-content').load('${request.contextPath}/bigdata/api/developer/front/app/add?id=' + id);
        })
    }
</script>