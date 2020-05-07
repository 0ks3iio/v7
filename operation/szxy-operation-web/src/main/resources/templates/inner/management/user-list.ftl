<#if users?? && users.content?size gt 0>
<#import "../../macro/pagination.ftl" as page />
<table class="table table-bordered table-striped table-hover no-margin">
    <thead>
    <tr>
        <th>序号</th>
        <th>姓名</th>
        <th>账号</th>
        <th>手机号</th>
        <th>所属运营组</th>
        <th>状态</th>
        <th>创建日期</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <#list users.content as user>
    <tr>
        <td>${user_index + 1}</td>
        <td>${user.realName}</td>
        <td>${user.username}</td>
        <td>${user.phone}</td>
        <td>${user.groupNames!}</td>
        <td>
            <i class="fa fa-circle <#if user.state==0>color-red<#else>color-green</#if> font-12"></i> ${user.stateName}
        </td>
        <td>${user.creationTime?string('yyyy-MM-dd')}</td>
        <td>
            <#if user.username!='admin'>
                <a class="table-btn color-blue user-auth" href="javascript:void(0);" data-id="${user.id}">授权管理</a>
                <#--<a class="table-btn color-blue js-limit" href="#" data-id="${user.id}">查看权限</a>-->
                <a class="table-btn color-blue user-more pos-rel" href="javascript:void(0);" data-id="${user.id}" data-state="${user.state}">
                    更多
                </a>
            </#if>
        </td>
    </tr>
    </#list>
    </tbody>
</table>
    <@page.paginataion pages=users containerId='userContainer' pageCallFunction='doGetUserList' />
<script>
    function doGetUserList(pageParams) {
        $('#userContainer').load(_contextPath + '/operation/management/user/list?' + pageParams);
    }

    $(function () {
        $('.user-more').each(function () {
            var id = $(this).data('id');
            var more = '<div class="more-layer modify-name-layer" style="width: 90px;">\
                            <ul class="mt20">\
                                <li><a class="color-blue user-reset-password" href="javascript:void(0);" data-id="'+id+'">重置密码</a></li>\
                                <li><a class="color-blue user-stop" href="javascript:void(0);" data-id="'+id+'" data-state="$\\{state}">$\\{stateName}</a></li>\
                                <li><a class="color-blue user-edit" href="javascript:void(0);" data-id="'+id+'">编辑</a></li>\
                                <li><a class="color-blue user-delete" data-id="'+id+'">删除</a></li>\
                            </ul>\
                        </div>';
            more = more.replace('$\\{state}', $(this).data('state'));
            if ($(this).data('state') == '0') {
                more = more.replace('$\\{stateName}', '启用');
            } else {
                more = more.replace('$\\{stateName}', '停用');
            }
            $(this).append(more);
        });
        $(document).click(autoHide);
        function autoHide(e) {
            if (isMore(e)) {
                $(".more-layer").hide();
            }
        }
        function isMore(e) {
            return !$(e.target).hasClass("user-more")
                    && !$(e.target).parent().hasClass("user-more")
                    && !$(e.target).parents().hasClass("user-more")
        }

        $('.user-more').click(function (e) {
            e.preventDefault();
            $(".modify-name-layer").hide();
            $(this).children(".more-layer").css("display", "block")
        });

        //删除
        $('.user-more .user-delete').on('click', function () {
            var id = $(this).data('id');
            $.ajax({
                url: _contextPath + '/operation/management/user/' + id,
                type: 'DELETE',
                success: function (res) {
                    if (res.success) {
                        $('.more-layer').hide();
                        message.successMessage(res.message, function () {
                            routeUtils.reload();
                        })
                    } else {
                        $.message.errorMessage(res.message);
                    }
                }
            })
        });
        //重置密码
        $('.user-reset-password').on('click', function () {
            var id = $(this).data('id');
            $.ajax({
                url: _contextPath + '/operation/management/user/' + id + '/reset-password',
                type: 'POST',
                success: function (res) {
                    if (res.success) {
                        $('.more-layer').hide();
                        message.successMessage(res.message,)
                    } else {
                        $.message.errorMessage(res.message);
                    }
                }
            })
        });
        //变更权限
        $('.user-auth').click(function () {
            var id = $(this).data('id');
            $.get(_contextPath + '/operation/management/user/' + id + '/auth', function (html) {
                layer.open({
                    type: 1,
                    shadow: 0.5,
                    title: '授权管理',
                    area: ['700px'],
                    btn: ['确定', '取消'],
                    content: html,
                    yes: function () {
                        var validator = $('#user-auth-form').data('bootstrapValidator');
                        validator.validate();
                        if (!validator.isValid()) {
                            return;
                        }
                        $.ajax({
                            url: _contextPath + '/operation/management/user/' + id + '/auth',
                            type: 'POST',
                            data: $('#user-auth-form').serialize(),
                            success: function (res) {
                                if (res.success) {
                                    message.successMessage(res.message, function() {
                                        layer.closeAll();
                                        routeUtils.reload();
                                    })
                                } else {
                                    message.errorMessage(res.message);
                                }
                            }
                        })
                    }
                });
            })
        });

        $('.user-edit').click(function () {
           var id = $(this).data('id');
           routeUtils.go('/operation/management/user/' + id + '/edit', function () {
               $('#managementContainer').loading(_contextPath + '/operation/management/user/' + id + '/edit');
           })
        });

        //状态变更
        $('.user-stop').click(function () {
            var id = $(this).data('id');
            var state = $(this).data('state');
            $.ajax({
               url: _contextPath + '/operation/management/user/' + id,
               type: 'PUT',
               data: {
                   state: state=='0'?1:0
               },
               success: function (res) {
                   if (res.success) {
                       message.successMessage(res.message, function () {
                           routeUtils.reload()
                       })
                   } else {
                       message.errorMessage(res.message);
                   }
               }
            });
        })
    });
</script>
<#else>
    <div class="no-data-container">
        <div class="no-data">
            <span class="no-data-img">
                <img src="${springMacroRequestContext.contextPath}/static/images/public/nodata4.png" alt="">
            </span>
            <div class="no-data-body">
                <p class="no-data-txt">没有相关数据</p>
            </div>
        </div>
    </div>
</#if>