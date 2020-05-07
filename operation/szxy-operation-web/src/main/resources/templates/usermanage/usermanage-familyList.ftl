<#import "../macro/pagination.ftl" as pagination />
<#if families?? && families.content?? && families.content?size gt 0>
    <table class="table table-bordered table-striped table-hover no-margin">
        <thead>
        <tr>
            <th>
                <label>
                    <input id="family-check-all" type="checkbox" name="" class="wp ">
                    <span class="lbl"></span>
                </label>
            </th>
            <th>姓名</th>
            <th>账号</th>
            <th>手机号</th>
            <th>学生学校</th>
            <th>注册日期</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody id="studentAndFamilyAccounts">
            <#list families.content as family>
            <tr>
                <td>
                    <label>
                        <input value="${family.id!}" type="checkbox" name="" class="wp family-check">
                        <span class="lbl"></span>
                    </label>
                </td>
                <td>${family.realName}</td>
                <td>${family.username}</td>
                <td>${family.mobilePhone!}</td>
                <td>${family.unitName!}</td>
                <td>${family.creationTime?string('yyyy-MM-dd')}</td>
                <td>
                    <a data-id="${family.id}" class="color-blue table-btn family-edit" href="javascript:;">编辑</a>
                    <a data-id="${family.id}" class="color-blue table-btn family-reset-password" href="javascript:;">重置密码</a>
                </td>
            </tr>
            </#list>
        </tbody>
    </table>
    <@pagination.paginataion pages=families containerId='familyListContainer' pageCallFunction='doGetFamilyList' />
    <script>
        $(function () {
            $('#family-check-all').change(function () {
                if ($(this).is(':checked')) {
                    $('.family-check').prop('checked', true);
                } else {
                    $('.family-check').prop('checked', false);
                }
            });

            $('.family-edit').click(function () {
                var id = $(this).data('id');
                $.ajaxGetLoading(_contextPath + '/operation/user/manage/family/' + id, function (html) {
                    layer.open({
                        type: 1,
                        shadow: 0.5,
                        title: '编辑',
                        area: '620px',
                        btn: ['确定', '取消'],
                        content: html,
                        yes: function (index) {

                            var validator = $('#family-edit-form').data('bootstrapValidator');
                            validator.validate();
                            if (!validator.isValid()) {
                                return;
                            }

                            $.ajax({
                                url: _contextPath + '/operation/user/manage/family/' + id,
                                type: 'PUT',
                                data: $('#family-edit-form').serialize(),
                                success: function (res) {
                                    if (res.success) {
                                        layer.close(index)
                                        message.successMessage(res.message, function () {
                                            doGetFamilyList('');
                                        })
                                    } else {
                                        message.errorMessage(res.message)
                                    }
                                }
                            })
                        }
                    });
                });
            });

            $('.family-reset-password').click(function () {
                var ids = [];
                ids.push($(this).data('id'));
                $.ajax({
                    url: _contextPath + '/operation/user/manage/reset-password/byOwnerIds',
                    type: 'POST',
                    data: {
                        ownerIds: ids,
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
        })
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