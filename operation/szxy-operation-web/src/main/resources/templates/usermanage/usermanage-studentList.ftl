<#import "../macro/pagination.ftl" as pagination />
<#if students?? && students.content?? && students.content?size gt 0>
    <table class="table table-bordered table-striped table-hover no-margin">
        <thead>
        <tr>
            <th>
                <label>
                    <input id="student-check-all" type="checkbox" name="" class="wp ">
                    <span class="lbl"></span>
                </label>
            </th>
            <th>姓名</th>
            <th>账号</th>
            <th>学校</th>
            <th>注册日期</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody id="studentAndFamilyAccounts">
            <#list students.content as student>
            <tr>
                <td>
                    <label>
                        <input type="checkbox" name="" class="wp student-check" value="${student.id}">
                        <span class="lbl"></span>
                    </label>
                </td>
                <td>${student.studentName!}</td>
                <td>${student.username!}</td>
                <td>${student.schoolName!}</td>
                <td>${student.creationTime?string('yyyy-MM-dd')}</td>
                <td>
                    <#--<a data-id="${student.id}" class="color-blue table-btn student-edit" href="javascript:;">编辑</a>-->
                        <#if student.username??>
                    <a data-id="${student.id}" class="color-blue table-btn student-reset-password" href="javascript:;">重置密码</a>
                        </#if>
                </td>
            </tr>
            </#list>
        </tbody>
    </table>
    <@pagination.paginataion pages=students containerId='studentListContainer' pageCallFunction='doGetStudentList' />
    <script>
        $(function () {
            $('#student-check-all').change(function () {
                if ($(this).is(':checked')) {
                    $('.student-check').prop('checked', true);
                } else {
                    $('.student-check').prop('checked', false);
                }
            });

            $('.student-reset-password').click(function () {
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