<div class="filter">
    <div class="filter-item">
        <button id="resetStudentOrFamilyPassword" type="button" class="btn btn-default"
                disabled="disabled" data-toggle="tooltip" data-placement="right"
                onclick="resetFamilyPassword();"
                title="请先选择账号">重置密码
        </button>
    </div>

    <div class="filter-item filter-item-left">
        <div class="filter-content">
            <span class="filter-name">姓名：</span>
            <input id="realName" type="text"
                   class="typeahead scrollable family-filter form-control "
                   autocomplete="off" data-provide="typeahead"
                   value="">
        </div>
    </div>
    <div class="filter-item filter-item-left">
        <div class="filter-content">
            <span class="filter-name">账号：</span>
            <input id="username" type="text"
                   class="typeahead scrollable form-control family-filter"
                   autocomplete="off" data-provide="typeahead"
                   value="">
        </div>
    </div>
    <div class="filter-item filter-item-left">
        <div class="filter-content">
            <span class="filter-name">手机号：</span>
            <input id="mobilePhone" type="text"
                   class="typeahead scrollable form-control family-filter"
                   autocomplete="off" data-provide="typeahead"
                   value="">
        </div>
    </div>
    <div class="filter-item filter-item-left">
        <div class="filter-content">
            <#if unitId?default('') != ''>
                <button class="btn btn-lightblue" onclick="doGetFamilyList('', true);">搜本单位</button>
            </#if>
            <button class="btn btn-blue" onclick="doGetFamilyList('', false);">搜全部</button>
        </div>
    </div>
</div>
<div id="familyListContainer">
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
        //学生和家长
        <#if unitId?default('') != ''>
            doGetFamilyList('', true);
        <#else>
            doGetFamilyList('', false);
        </#if>

        $('.family-filter').keypress(function (e) {
            if (e.which === 13) {
                doGetFamilyList('');
            }
        })
    });
    var lastCurrent;
    function doGetFamilyList(pageURL, isCurrent) {
        var dynamic= '';
        if (typeof isCurrent === 'undefined') {
            if (typeof lastCurrent === 'undefined') {
                if ("${unitId!}" != '') {
                    isCurrent = true;
                } else {
                    isCurrent = false;
                }
            } else {
                isCurrent = lastCurrent;
            }
        }
        lastCurrent = isCurrent;
        if (isCurrent) {
            if (isCurrent) {
                dynamic = '&unitId=${unitId!}';
            }
        }

        dynamic = dynamic + '&username=' + $('#username').val() + '&realName=' + $.trim($('#realName').val()) + '&mobilePhone=' + $('#mobilePhone').val() + '&' + pageURL;
        $('#familyListContainer').loading(_contextPath + '/operation/user/manage/family/list?' + dynamic);
    }

    /**
     * 重置密码
     */
    function resetFamilyPassword() {
        var userIdArray = doGetFamilyId();
        if (userIdArray.length === 0) {
            opLayer.warn("请选择需要重置密码的学生或家长");
            return;
        }
        $.ajax({
            url: _contextPath + '/operation/user/manage/reset-password/byOwnerIds',
            type: 'POST',
            data: {
                ownerIds: userIdArray,
            },
            // contentType: 'application/json',
            success: function (res) {
                if (res.success) {
                    message.successMessage(res.message);
                } else {
                    message.errorMessage(res.message);
                }
            }
        })
    }

    function doGetFamilyId() {
        var ids = [];
        $('.family-check:checked').each(function () {
            var id = $(this).val();
            if ($.trim(id) !== '') {
                ids.push(id);
            }
        });
        return ids;
    }
</script>