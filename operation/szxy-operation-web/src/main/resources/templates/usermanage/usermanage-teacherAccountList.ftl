<#import "../macro/pagination.ftl" as pagination />
<#if pages??>
    <#if pages.content?? && pages.content?size gt 0>
        <table class="table table-bordered table-striped table-hover no-margin">
            <thead>
            <tr>
                <th><label><input id="teacher_accounts_checkbox_all" type="checkbox" name="" class="wp "><span
                        class="lbl"></span></label></th>
                <th>账号</th>
                <th>姓名</th>
                <th>手机号</th>
                <th>单位</th>
                <th>账号状态</th>
                <th>有效期</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody id="teacherAccounts">
            <#list pages.content as teacherAccount>
            <tr>
                <td>
                    <label>
                        <input value="${teacherAccount.id}" type="checkbox" name=""
                               class="wp teacher_accounts_checkbox">
                        <span class="lbl"></span>
                    </label>
                </td>
                <td>${teacherAccount.username}</td>
                <td>${teacherAccount.realName}</td>
                <td>${teacherAccount.mobilePhone!}</td>
                <td>${teacherAccount.unitName!}</td>
                <td>
                        <#if teacherAccount.expire?default(false)>
                            <i class="fa fa-circle color-red font-12"></i> 已过期
                        <#else >
                            <#if teacherAccount.userState == 0>
                            <i class="fa fa-circle color-red font-12"></i> 未审核
                            <#elseif teacherAccount.userState == 1>
                            <i class="fa fa-circle color-green font-12"></i> 正常
                            <#elseif teacherAccount.userState == 2>
                            <i class="fa fa-circle color-ccc font-12"></i> 锁定
                            <#elseif teacherAccount.userState == 3>
                            <i class="fa fa-circle color-yellow font-12"></i> 注销
                            <#else >
                            <i class="fa fa-circle color-green font-12"></i> 未知状态
                            </#if>
                        </#if>
                </td>
                <td>
                        <#if teacherAccount.expireDate??>
                            <#if teacherAccount.expire?default(false)>
                            <i class="fa fa-circle color-red font-12"></i>
                            </#if>
                            ${teacherAccount.expireDate?string('yyyy-MM-dd')}
                        <#else >
                            永久
                        </#if>
                </td>
                <td>
                    <a data-id="${teacherAccount.id}" class="color-blue table-btn teacherAccount-edit" href="javascript:;">编辑</a>
                    <a data-id="${teacherAccount.id}" class="color-blue table-btn teacherAccount-reset-password" href="javascript:;">重置密码</a>
                </td>
            </tr>
            </#list>
            </tbody>
        </table>
        <@pagination.paginataion pages=pages containerId='teacherAccouts' pageCallFunction='doGetTeacherAccountList' />
        <script>
            $(function () {
                //全选
                $('#teacher_accounts_checkbox_all').change(function () {
                    if ($(this).is(':checked')) {
                        $('#teacherAccounts').find('input.wp:not(:checked)').trigger('click')
                    } else {
                        $('#teacherAccounts').find('input.wp:checked').trigger('click');
                    }
                });
                //
                var hasChecked = 0;
                $('.teacher_accounts_checkbox').change(function () {
                    if ($(this).is(':checked')) {
                        hasChecked++;
                    } else {
                        hasChecked--;
                    }
                    if (hasChecked > 0) {
                        $('#resetPassword').attr('disabled', false);
                        $('#activeButton').attr('disabled', false);
                    } else {
                        $('#resetPassword').attr('disabled', true);
                        $('#activeButton').attr('disabled', true);
                    }
                });


                //编辑
                $('.teacherAccount-edit').on('click', function () {
                    var id = $(this).data('id');
                    $.ajaxGetLoading(_contextPath + '/operation/user/manage/teacher/edit?userId=' + id, function (html) {
                        layer.open({
                            type: 1,
                            shadow: 0.5,
                            title: '编辑',
                            area: '620px',
                            btn: ['确定', '取消'],
                            content: html,
                            yes: function (index) {
                                var validator = $('#user-edit-form').data('bootstrapValidator');
                                validator.validate();
                                if (!validator.isValid()) {
                                    return;
                                }
                                $.ajax({
                                    url: _contextPath + '/operation/user/manage/teacher/update',
                                    type: 'PUT',
                                    data: $('#user-edit-form').serialize(),
                                    success: function (res) {
                                        if (res.success) {
                                            layer.close(index)
                                            message.successMessage(res.message, function () {
                                                doGetTeacherAccountList('');
                                            })
                                        } else {
                                            message.errorMessage(res.message)
                                        }
                                    }
                                })
                            }
                        });
                    })
                });

                $('.teacherAccount-reset-password').click(function () {
                    var ids = [];
                    ids.push($(this).data('id'));
                    $.ajax({
                        url: _contextPath + '/operation/user/manage/reset-password',
                        type: 'POST',
                        data: {
                            userIds: ids,
                            ownerType: 2,
                        },
                        success: function (res) {
                            if (res.success) {
                                message.successMessage(res.message);
                            } else {
                                message.errorMessage(res.message);
                            }
                        }
                    })
                });
            });
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
</#if>