<div class="filter">
    <div class="filter-item">
        <button id="resetStudentOrFamilyPassword" type="button" class="btn btn-default"
                disabled="disabled" data-toggle="tooltip" data-placement="right"
                onclick="resetStudentPassword();"
                title="请先选择账号">重置密码
        </button>
    </div>

    <#if gradeList??>
    <div class="filter-item filter-item-left">
        <div class="filter-content">
            <span class="filter-name">年级：</span>
            <select name="" id="gradeOption" class="form-control"
                    onchange="onChangeGrade(this);">
                <#if gradeList?? && gradeList?size gt 0>
                    <#list gradeList as grade>
                        <option value="${grade.id}">${grade.gradeName!}</option>
                    </#list>
                </#if>
            </select>
        </div>
    </div>

    <div class="filter-item filter-item-left">
        <div class="filter-content">
            <span class="filter-name">班级：</span>
            <select name="" id="clazzOption" class="form-control"
                    onchange="onChangeClazz();">
                <#if classList?? && classList?size gt 0>
                    <#list classList as clazz>
                        <option value="${clazz.id}">${clazz.className!}</option>
                    </#list>
                </#if>
            </select>
        </div>
    </div>
    </#if>
    <div class="filter-item filter-item-left">
        <div class="filter-content">
            <span class="filter-name">姓名：</span>
            <input id="realName" type="text"
                   class="typeahead scrollable form-control student-filter"
                   autocomplete="off" data-provide="typeahead"
                   value="">
        </div>
    </div>
    <div class="filter-item filter-item-left">
        <div class="filter-content">
            <span class="filter-name">账号：</span>
            <input id="username" type="text"
                   class="typeahead scrollable form-control student-filter"
                   autocomplete="off" data-provide="typeahead"
                   value="">
        </div>
    </div>
    <div class="filter-item filter-item-left">
        <div class="filter-content">
            <#if unitId?default('') != ''>
                <button class="btn btn-lightblue" onclick="doGetStudentList('', true);">搜本单位</button>
            </#if>
            <button class="btn btn-blue" onclick="doGetStudentList('', false);">搜全部</button>
        </div>
    </div>
</div>
<div id="studentListContainer">
    <#if unitId?default('') == ''>
        <div class="no-data-container">
            <div class="no-data">
            <span class="no-data-img">
                <img src="${springMacroRequestContext.contextPath}/static/images/public/nodata4.png" alt="">
            </span>
                <div class="no-data-body">
                    <p class="no-data-txt">请选择单位</p>
                </div>
            </div>
        </div>
    </#if>
</div>
<script>
    $(function () {
        //学生和家长
        <#if unitId?default('') != ''>
            doGetStudentList('', true);
        <#else >
            doGetStudentList('', false);
        </#if>
        $('.student-filter').keypress(function (e) {
            if (e.which === 13) {
                doGetStudentList('');
            }
        })
        onChangeGrade($('#gradeOption'));
    });

    var lastStudent;
    function doGetStudentList(pageURL, isCurrent) {
        var dynamic = '';
        if (typeof isCurrent === 'undefined' ) {
            if (typeof lastStudent === 'undefined') {
                if ("${unitId!}" != '') {
                    isCurrent = true
                }
                else {
                    isCurrent = false;
                }
            } else {
                isCurrent = lastStudent;
            }
        }
        if (isCurrent) {
            dynamic = '&unitId=${unitId!}';
        }
        lastStudent = isCurrent;

        let clazzId = $('#clazzOption').val();
        if (clazzId) {
            dynamic = dynamic + '&classId=' + clazzId;
        }
        dynamic = dynamic + '&username=' + $('#username').val() + '&realName=' + $.trim($('#realName').val())
        if ($.trim(pageURL) !== '') {
            dynamic = dynamic + '&' + pageURL;
        }
        $('#studentListContainer').loading(_contextPath + '/operation/user/manage/student/list?' + dynamic);
    }

    function onChangeGrade(el) {
        let gradeId = $(el).val();
        $.ajax({
            url: _contextPath + '/operation/user/manage/student/' + gradeId + '/classList',
            type: 'GET',
            success: function (res) {
                if (res.success) {
                    let clazzOptionHtml = '';
                    if (typeof res.classList == 'undefined') {
                        $('#clazzOption').html('');
                        return;
                    }
                    for (let index in res.classList) {
                        clazzOptionHtml = clazzOptionHtml + '<option value="' + res.classList[index].id + '">' + res.classList[index].className + '</option>'
                    }
                    $('#clazzOption').html(clazzOptionHtml);
                    //重新加载数据
                    doGetStudentList('');
                } else {
                    message.errorMessage("加载班级数据出错");
                }
            }
        })
    }

    function onChangeClazz(el) {
        doGetStudentList('', true);
    }

    /**
     * 重置密码
     */
    function resetStudentPassword() {
        var userIdArray = doGetCheckedStudentId();
        if (userIdArray.length === 0) {
            opLayer.warn("请选择学生");
            return;
        }
        $.ajax({
            url: _contextPath + '/operation/user/manage/reset-password/byOwnerIds',
            type: 'POST',
            data: {
                ownerIds: userIdArray,
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

    function doGetCheckedStudentId() {
        var ids = [];
        $('.student-check:checked').each(function () {
            var id = $(this).val();
            if ($.trim(id) != '') {
                ids.push(id);
            }
        });
        return ids;
    }
</script>