<#import "../macro/pagination.ftl" as pagination />
<#if pages??>
    <#if pages.content?? && pages.content?size gt 0>
        <table class="table table-bordered table-striped table-hover no-margin">
            <thead>
            <tr>
                <th>序号</th>
                <th>
                    <label>
                        <input id="student_accounts_checkbox_all" type="checkbox" name="" class="wp ">
                        <span class="lbl"></span>
                    </label>
                    学生姓名
                </th>
                <th>
                    <span class="float-left mt5">
                    学生账号
                    </span>
                    <#if createStudentUsername?? && createStudentUsername>
                    <button onclick="generateStudentUsername();" class="btn btn-sm btn-blue float-right">生成</button>
                    </#if>
                </th>
                <th>
                    <label>
                        <input id="family_accounts_checkbox_all" type="checkbox" name="" class="wp ">
                        <span class="lbl"></span>
                    </label>
                    家长姓名
                </th>
                <th>
                    <span class="float-left mt5">
                    家长账号
                    </span>
                    <#if createFamilyUsername?? && createFamilyUsername>
                    <button onclick="generateFamilyUsername();" class="btn btn-sm btn-blue float-right">生成</button>
                    </#if>
                </th>
                <th>家长手机号</th>
            </tr>
            </thead>
            <tbody id="studentAndFamilyAccounts">
            <#list pages.content as studentAndFamilyAccount>
            <tr>
                <td>
                    ${studentAndFamilyAccount_index + 1}
                </td>
                <td>
                    <label>
                        <input value="${studentAndFamilyAccount.studentId}" type="checkbox" name=""
                               class="wp student_accounts_checkbox accounts" data-accountid="${studentAndFamilyAccount.studentUserId!}">
                        <span class="lbl"></span>
                    </label>
                    ${studentAndFamilyAccount.studentName}
                </td>
                <td id="studentUsername_${studentAndFamilyAccount.studentId}">
                    ${studentAndFamilyAccount.studentUsername!}
                </td>
                <td>
                    <#if studentAndFamilyAccount.familyId??>
                    <label>
                        <input value="${studentAndFamilyAccount.familyId!}" type="checkbox" name=""
                               class="wp family_accounts_checkbox accounts" data-accountid="${studentAndFamilyAccount.familyUserId!}">
                        <span class="lbl"></span>
                    </label>
                    </#if>
                    ${studentAndFamilyAccount.familyName!}
                </td>
                <td id="familyUsername_${studentAndFamilyAccount.familyId!}">
                    ${studentAndFamilyAccount.familyUsername!}
                </td>
                <td>
                    ${studentAndFamilyAccount.familyPhone!}
                </td>
            </tr>
            </#list>
            </tbody>
        </table>
        <@pagination.paginataion pages=pages containerId='studentAndFamilyAccounts' pageCallFunction='doGetStudentAndFamilyList' />
        <script>
            $(function () {
                //全选
                $('#student_accounts_checkbox_all').change(function () {
                    if ($(this).is(':checked')) {
                        $('#studentAndFamilyAccounts').find('input.student_accounts_checkbox:not(:checked)').trigger('click')
                    } else {
                        $('#studentAndFamilyAccounts').find('input.student_accounts_checkbox:checked').trigger('click');
                    }
                });
                $('#family_accounts_checkbox_all').change(function () {
                    if ($(this).is(':checked')) {
                        $('#studentAndFamilyAccounts').find('input.family_accounts_checkbox:not(:checked)').trigger('click')
                    } else {
                        $('#studentAndFamilyAccounts').find('input.family_accounts_checkbox:checked').trigger('click');
                    }
                });
                //
                var hasChecked = 0;
                $('.accounts').change(function () {
                    if ($(this).is(':checked')) {
                        hasChecked++;
                    } else {
                        hasChecked--;
                    }
                    if (hasChecked > 0) {
                        $('#resetStudentOrFamilyPassword').attr('disabled', false);
                        // $('#createAccount').attr('disabled', false);
                    } else {
                        $('#resetStudentOrFamilyPassword').attr('disabled', true);
                        // $('#createAccount').attr('disabled', true);
                    }
                });
            });

            /**
             * 批量生成账号
             */
            function generateStudentUsername() {
                let studentIdArray = [];
                $('.student_accounts_checkbox:checked').each(function () {
                    studentIdArray.push($(this).val());
                });
                $.ajax({
                    url: _contextPath + '/operation/user/manage/student/username',
                    data: {
                        studentIds: studentIdArray,
                        classId: $('#clazzOption').val()
                    },
                    type: 'POST',
                    traditional:true,
                    success: function (res) {
                        if (res.success) {
                            $('.student_accounts_checkbox:checked').each(function () {
                                let studentId = $(this).val();
                                let $studentUsername = $('#studentUsername_' + studentId);
                                if ($.trim($studentUsername.html()) === '') {
                                    $studentUsername.html(doGetUsernameTemplate(res.usernames[studentId], studentId, 'student'));
                                }
                            });
                            $('#createAccount').attr('disabled', false);
                        } else {
                            opLayer.error(res.message, "提示");
                        }
                    },
                    error: function () {
                        opLayer.error("网络异常");
                    }
                });
            }

            function generateFamilyUsername() {
                let familyIdArray = [];
                $('.family_accounts_checkbox:checked').each(function () {
                    familyIdArray.push($(this).val());
                });
                $.ajax({
                    url: _contextPath + '/operation/user/manage/family/username',
                    data: {
                        familyIds: familyIdArray,
                        classId: $('#clazzOption').val()
                    },
                    type: 'POST',
                    success: function (res) {
                        if (res.success) {
                            $('.family_accounts_checkbox:checked').each(function () {
                                let familyId = $(this).val();
                                let $familyUsername = $('#familyUsername_' + familyId);
                                if ($.trim($familyUsername.html()) === '') {
                                    $familyUsername.html(doGetUsernameTemplate(res.usernames[familyId], familyId, 'family'));
                                }
                            });
                            $('#createAccount').attr('disabled', false);
                        } else {
                            opLayer.error(res.message);
                        }
                    },
                    error: function () {
                        opLayer.error("网络异常");
                    }
                })
            }

            function doGetUsernameTemplate(username, id, studentOrFamilyClass) {
                return '<div class="input-group">\n' +
                '<input data-id="'+id+'" onchange="doCheckUsernameExists(this);" class="form-control new-username '+studentOrFamilyClass+'" type="text" value="'+username+'">\n' +
                '<span class="input-group-btn">\n' +
                '<button onclick="removeUsername(this);" class="btn btn-default del" type="button"><span>×</span></button>\n' +
                '</span>\n' +
                '</div>';
            }

            function removeUsername(el) {
                $(el).parent().parent().remove();
                setCreateAccountButton();
            }

            function setCreateAccountButton() {
                if (doGetOpenAccountInfos().length<=0) {
                    $('#createAccount').attr('disabled', true);
                } else {
                    $('#createAccount').attr('disabled', false);
                }
            }

            function doCheckUsernameExists(el) {
                $.get(_contextPath + '/operation/user/manage/username/checked?username='+$(el).val(), function (res) {
                    if (!res.success) {
                        opLayer.error(res.message);
                    }
                })
            }

            $('.editAccount').click(function () {

            });

        </script>
    <#else >
        <@no_data />
    </#if>
<#else >
    <@no_data />
</#if>
<#macro no_data message=''>
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
</#macro>

