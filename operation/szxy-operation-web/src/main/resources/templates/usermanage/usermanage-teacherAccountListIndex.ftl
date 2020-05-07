<#import "../macro/staticImport.ftl" as staticImport />
<@staticImport.datepicker />
<div class="filter">
    <div class="filter-item">
        <!--移除导出功能-->
    <#--<a href="${springMacroRequestContext.contextPath}/operation/user/manage/export/teacherAccount/export?unitId=${unitId}" type="button" class="btn btn-default">导出Excel</a>-->
        <button id="resetPassword" type="button" class="btn btn-default"
                disabled="disabled">重置密码
        </button>
    </div>
    <div class="filter-item">
        <span class="filter-name">状态：</span>
        <div class="filter-content">
            <select name="" id="userStateOption" onchange="doGetTeacherAccountList('');"
                    class="form-control">
                <option value="">全部</option>
                <#if userStateList?? && userStateList?size gt 0>
                    <#list userStateList as userStateVo>
                        <option value="${userStateVo.userState}">${userStateVo.humanText}</option>
                    </#list>
                </#if>
            </select>
        </div>
    </div>
    <div class="filter-item filter-item-left">
        <div class="filter-content">
            <span class="filter-name">姓名：</span>
            <input id="realName" type="text"
                   class="typeahead scrollable form-control user-teacher-filter"
                   autocomplete="off" data-provide="typeahead"
                   value="">
        </div>
    </div>
    <div class="filter-item filter-item-left">
        <div class="filter-content">
            <span class="filter-name">账号：</span>
            <input id="username" type="text"
                   class="typeahead scrollable form-control user-teacher-filter"
                   autocomplete="off" data-provide="typeahead"
                   value="">
        </div>
    </div>
    <div class="filter-item filter-item-left">
        <div class="filter-content">
            <span class="filter-name">手机号：</span>
            <input id="mobilePhone" type="text"
                   class="typeahead scrollable form-control user-teacher-filter"
                   autocomplete="off" data-provide="typeahead"
                   value="">
        </div>
    </div>

    <div class="filter-item filter-item-left">
        <div class="filter-content">
            <#if unitId?default('') != ''>
                <button class="btn btn-lightblue" onclick="doGetTeacherAccountList('', true);">搜本单位</button>
            </#if>
            <button class="btn btn-blue" onclick="doGetTeacherAccountList('', false);">搜全部</button>
        </div>
    </div>
</div>
<div id="teacherAccountList">
    <#if unitId?default('') == ''>
        <div class="no-data-container">
            <div class="no-data">
            <span class="no-data-img">
                <img src="${springMacroRequestContext.contextPath}/static/images/public/nodata4.png" alt="">
            </span>
                <div class="no-data-body">
                    <p class="no-data-txt">请选择单位或搜索</p>
                </div>
            </div>
        </div>
    </#if>
</div>

<script>
    $(function () {
        <#if unitId?default('') != ''>
            doGetTeacherAccountList('', true);
        <#else>
            doGetTeacherAccountList('', false);
        </#if>
        $('#resetPassword').on('click', function () {
            resetPassword();
        });

        $('.user-teacher-filter').keypress(function (e) {
            if (e.which === 13) {
                doGetTeacherAccountList('');
            }
        });
    });
    var lastTeacher;
    function doGetTeacherAccountList(pageURL, isCurrent) {
        let pURL = doBuildDynamicParameter();
        if ($.trim(pageURL) !== '') {
            pURL = pURL + '&' + pageURL;
        }
        if (typeof isCurrent === "undefined" ) {
            if (typeof lastTeacher === 'undefined') {
                if ('${unitId!}' != '') {
                    isCurrent = true;
                } else {
                    isCurrent = false;
                }
            } else {
                isCurrent = lastTeacher;
            }
        }
        if (isCurrent) {
            pURL = pURL + '&unitId=${unitId!}';
        }
        lastTeacher = isCurrent;

        $('#teacherAccountList').loading(_contextPath + '/operation/user/manage/teacher/list?' + pURL);
    }

    function doBuildDynamicParameter() {
        var pURL = 'userState=' + $('#userStateOption').val();

        pURL = pURL + '&username=' + $('#username').val();
        pURL = pURL + '&realName=' + $('#realName').val();
        return pURL + '&mobilePhone=' + $('#mobilePhone').val();
    }

    function resetPassword() {
        $.ajax({
            url: _contextPath + '/operation/user/manage/reset-password',
            type: 'POST',
            data: {
                userIds: doGetCheckedUserIdArray(),
            },
            success: function (res) {
                if (res.success) {
                    message.successMessage(res.message);
                } else {
                    message.errorMessage(res.message);
                }
            }
        })
    }

    function doGetCheckedUserIdArray() {
        let userIdArray = [];
        $("#teacherAccounts").find('input.wp:checked').each(function () {
            userIdArray.push($(this).val());
        });
        return userIdArray;
    }

</script>