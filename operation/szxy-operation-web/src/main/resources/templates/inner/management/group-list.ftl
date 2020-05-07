<#import "../../macro/pagination.ftl" as page />
<#if groups?? && groups.content?? && groups.content?size gt 0>
<table class="table table-bordered table-striped table-hover no-margin">
    <thead>
    <tr>
        <th>序号</th>
        <th>组名称</th>
        <th>负责区块</th>
        <th>小组人数</th>
        <th>创建日期</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <#list groups.content as group>
    <tr>
        <td>${group_index + 1}</td>
        <td>${group.name}</td>
        <td>${group.regionName}</td>
        <td>${group.userCount}</td>
        <td>${group.creationTime?string('yyyy-MM-dd')}</td>
        <td>
            <a class="table-btn color-blue group-user" href="javascript:void(0);" data-id="${group.id}">小组成员管理</a>
            <a class="table-btn color-blue group-auth" href="javascript:void(0);" data-id="${group.id}">授权管理</a>
            <a class="table-btn color-blue pos-rel change-group-name" href="javascript:void(0);" data-id="${group.id}">编辑组名称</a>
            <a class="table-btn color-blue group-delete" href="javascript:void(0);" data-id="${group.id}">解散组</a>
        </td>
    </tr>
    </#list>
    </tbody>
</table>
    <@page.paginataion pages=groups containerId='groupContainer' pageCallFunction='doGetGroupList' />
<script>
    $(function () {
        //删除
        $('.group-delete').on('click', function () {
            var id = $(this).data('id');
            $.ajax({
                url: _contextPath + '/operation/management/group/' + id,
                type: 'DELETE',
                success: function (res) {
                    if (res.success) {
                        message.successMessage(res.message, function () {
                            routeUtils.reload();
                        })
                    } else {
                        message.errorMessage(res.message);
                    }
                }
            })
        });

        //更新名称 start
        $('.change-group-name').each(function () {
            var modifyNameLayer = '<div class="modify-name-layer ">\
                                    <h5>修改名称</h5>\
                                    <div class="form-group">\
                                    <p><input name="name" type="text" class="form-control" placeholder="请输入名称"></p>\
                                    </div>\
                                    <div class="text-right">\
                                    <button class="btn btn-sm btn-white change-group-name-cancel"">取消</button>\
                                    <button class="btn btn-sm btn-blue ml10 change-group-name-yes">确定</button>\
                                    </div>\
                                   </div>';
            $(this).append(modifyNameLayer);
        });
        $(document).unbind('click', autoHideLayer);
        $(document).click(autoHideLayer);

        function autoHideLayer(e) {
            if(!$(e.target).hasClass("change-group-name")
                    && !$(e.target).parent().hasClass("change-group-name")
                    && !$(e.target).parents().hasClass("change-group-name")){
                $(".modify-name-layer").hide();//点页面其他地方 隐藏表情区
            }
        }

        $('.change-group-name').click(function (e) {
            e.preventDefault();
            $(".modify-name-layer").hide();
            $(this).children(".modify-name-layer").css("display", "block")
            $(this).find('.change-group-name-yes').attr('data-id', $(this).data('id'));
        });

        $('.change-group-name-cancel').click(function () {
            $(this).parent().parent().hide();
        });

        $('.change-group-name-yes').click(function () {
            var id = $(this).data('id');
            if (!validateGroupName($('a[data-id="'+id+'"]').children('.modify-name-layer'))) {
                return;
            }
            var name = $('a[data-id="'+id+'"]').children('.modify-name-layer').find('input').val();

            $.ajax({
                url: _contextPath + '/operation/management/group/' + id,
                type: 'PUT',
                data: {
                    name: name
                },
                success: function (res) {
                    if (res.success) {
                        message.successMessage(res.message, 1500, function () {
                            routeUtils.reload();
                        })
                    } else {
                        message.errorMessage(res.message);
                    }
                }
            })
        });

        function validateGroupName($el) {
            var validator = $el.data('bootstrapValidator');
            if (validator) {
                validator.validate();
                return validator.isValid();
            }
            $el.bootstrapValidator({
                excluded:[":disabled"],
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                autoFocus: true,
                fields: {
                    "name": {
                        validators: {
                            notEmpty: {
                                message: "分组名称不能为空"
                            },
                            chineseStringLength: {
                                max: 64,
                                message: "组名过长 32个汉字或64个英文字符"
                            },
                            remote: {
                                url: _contextPath + '/operation/management/group/checkGroupName',
                                data: {"name": $('#groupName').val()},
                                type: 'GET'
                            }
                        }
                    }
                }
            });
            $el.data('bootstrapValidator').validate();
            return $el.data('bootstrapValidator').isValid();
        }
        //更新名称 over

        //变更分组人员
        $('.group-user').click(function () {
            var id = $(this).data('id');
            $.get('${springMacroRequestContext.contextPath}/operation/management/group/' + id + '/users', function (html) {
                layer.open({
                    type: 1,
                    shadow: 0.5,
                    title: '小组成员管理',
                    area: ['520px'],
                    btn: ['确定', '取消'],
                    content: html,
                    yes: function () {
                        $.ajax({
                            url:  _contextPath + '/operation/management/group/' + id + '/users',
                            type: 'POST',
                            data: $('#group-user-form').serialize(),
                            success: function (res) {
                                if (res.success) {
                                    message.successMessage(res.message, function () {
                                        layer.closeAll();
                                        routeUtils.reload();
                                    });
                                } else {
                                    message.errorMessage(res.message);
                                }
                            }
                        })
                    }
                });
            })
        });

        //变更分组授权
        $('.group-auth').click(function () {
            var id = $(this).data('id');
            $.get(_contextPath + '/operation/management/group/' + id + '/auth', function (html) {
                layer.open({
                    type: 1,
                    shadow: 0.5,
                    title: '授权管理',
                    area: ['700px'],
                    btn: ['确定', '取消'],
                    content: html,
                    yes: function(){
                        $.ajax({
                            url:  _contextPath + '/operation/management/group/' + id + '/auth',
                            type: 'POST',
                            data: $('#group-auth-form').serialize(),
                            success: function (res) {
                                if (res.success) {
                                    message.successMessage(res.message, function () {
                                        layer.closeAll();
                                        routeUtils.reload();
                                    });
                                } else {
                                    message.errorMessage(res.message);
                                }
                            }
                        })
                    }
                });
            })
        });
    });

    function doGetGroupList(pageParams) {
        $('#groupContainer').load(_contextPath + '/operation/management/group/list?' + pageParams);
    }
</script>
<#else >
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