<div class="page-content-inner">
    <div class="box box-default">
        <div class="box-body no-padding-top">
            <div class="tab-container">
                <div class="tab-header clearfix">
                    <ul class="nav nav-tabs nav-tabs-1">
                        <li class="">
                            <a data-toggle="tab">学生与家长</a>
                        </li>
                    </ul>
                </div>
                <div class="tab-content">
                    <#if unitId?default('') != ''>
                        <div class="filter">
                            <div class="filter-item">
                                <button id="createAccount" onclick="openAccount();" type="button" class="btn btn-blue"
                                        disabled="disabled" data-toggle="tooltip" data-placement="top"
                                        title data-original-title="请先选择并生成账号">开通账号
                                </button>
                                <#--<button id="resetStudentOrFamilyPassword" type="button" class="btn btn-default"-->
                                        <#--disabled="disabled" data-toggle="tooltip" data-placement="right"-->
                                        <#--onclick="resetStudentAndFamilyPassword();"-->
                                        <#--title="请先选择账号">重置密码-->
                                <#--</button>-->
                                <button id="studentAndFamilyAccountRule" type="button" class="btn btn-default" onclick="showAccountRule();">
                                    账号生成规则
                                </button>
                                <button type="button" class="btn btn-default" id="StudentAndFamilyExport">导出Excel</button>
                            </div>

                            <div class="filter-item filter-item-right">
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
                            <div class="filter-item filter-item-right">
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
                        </div>
                    </#if>
                    <div id="studentAndFamilyAccountList">
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
                </div>
            </div>
        </div>
    </div>
</div>


<div class="layer layer-rule">
    <div class="layer-content">
        <div style="max-height: 300px;overflow: auto;">
            <div class="mb20">
                <div><b>当前规则</b></div>
                <div>${accountRule!}</div>
            </div>
            <div class="mb20">
                <div><b>{(s/p)_etohSchoolId_random(6)}</b></div>
                <div>1.(学生 s, 家长 p)+学校编号+6位随机数。</div>
            </div>
            <div class="mb20">
                <div><b>{(s/p)_etohSchoolId[0,6]_firstSpell}</b></div>
                <div>2.(学生 s, 家长 p)+学校编号取前6位+姓名拼音。</div>
            </div>
            <div class="mb20">
                <div><b>{N_userNamePrefix_stuCode},{p_etohSchoolId_random(6)}</b></div>
                <div>3.学生：用户名前缀+学号，家长：p+学校编号+6位随机数。</div>
            </div>
            <div class="mb20">
                <div><b>{x_fMobile},{(f/m)_mobile}</b></div>
                <div>4.学生：x+父亲手机号，父亲：f+自己手机号, 母亲 ：m+自己手机号。</div>
            </div>
            <div class="mb20">
                <div><b>{N_userNamePrefix_clsCode_maxcode},{(f/m)_tempUserName}</b></div>
                <div>5.学生：用户名前缀+学生所在班级编号+两位流水号，父亲：f+学生用户名，母亲：m+学生用户名。</div>
            </div>
            <div class="mb20">
                <div><b>{(G/F/M/H)_identitycard}</b></div>
                <div>6.学生：G+身份证号，父亲：F+学生身份证号，母亲：M+学生身份证号，监护人：H+学生身份证号。</div>
            </div>
            <div class="mb20">
                <div><b>{(s/f/m/g)_identitycard}</b></div>
                <div>7.学生：s+身份证号，父亲：f+学生身份证号，母亲：m+学生身份证号，监护人：g+学生身份证号。</div>
            </div>
            <div class="mb20">
                <div><b>{(s/f/m/g)_unitiveCode}</b></div>
                <div>8.学生：s+学籍号，父亲：f+学生学籍号，母亲：m+学生学籍号，监护人：g+学生学籍号。</div>
            </div>
            <div class="mb20">
                <div><b>{N_identitycard},{(f/m/g)_identitycard}</b></div>
                <div>9.学生：身份证号，父亲：f+学生身份证号，母亲：m+学生身份证号，监护人：g+学生身份证号。</div>
            </div>
            <div class="mb20">
                <div><b>{(s/f/m/g)_unitiveCode[-1,x]}</b></div>
                <div>10-x：x为数字，表示学籍号后几位，学生：s+学籍号后x位，父亲：f+学生学籍号后x位，母亲：m+学生学籍号后x位，监护人：g+学生学籍号后x位。</div>
            </div>
        </div>
    </div>
</div>
<script>
    $(function () {
        //学生和家长
        <#if unitId?default('') != ''>
            doGetStudentAndFamilyList('');
        </#if>
    });

    function doGetStudentAndFamilyList(pageURL) {
        let dynamic= '?clazzId=';
        let clazzId = $('#clazzOption').val();
        if (clazzId) {
            dynamic = dynamic + clazzId;
        }
        if ($.trim(pageURL) !== '') {
            dynamic = dynamic + '&' + pageURL;
        }
        $('#studentAndFamilyAccountList').load(_contextPath + '/operation/unit/manage/page/${unitId}/studentAndFamilyList' + dynamic);
    }

    function onChangeGrade(el) {
        let gradeId = $(el).val();
        $.ajax({
            url: _contextPath + '/operation/user/manage/' + gradeId + '/classList',
            type: 'GET',
            success: function (res) {
                if (res.success) {
                    let clazzOptionHtml = '';
                    if (typeof res.classList == 'undefined') {
                        $('#clazzOption').html('');
                        return;
                    }
                    for (let index in res.classList) {
                        clazzOptionHtml =  clazzOptionHtml + '<option value="' + res.classList[index].id + '">' + res.classList[index].className + '</option>'
                    }
                    $('#clazzOption').html(clazzOptionHtml);
                    //重新加载数据
                    doGetStudentAndFamilyList('');
                } else {
                    opLayer.error("加载班级数据出错", "提示");
                }
            },
            error: function () {
                opLayer.error("网络异常", "提示");
            }
        })
    }

    function onChangeClazz(el) {
        doGetStudentAndFamilyList('');
    }

    /**
     * 重置密码
     */
    function resetStudentAndFamilyPassword() {
        let userIdArray = doGetCheckedStudentAndFamilyId();
        if (userIdArray.length == 0) {
            opLayer.warn("请选择需要重置密码的学生或家长");
            return;
        }
        $.ajax({
            url: _contextPath + '/operation/user/manage/password/reset/',
            type: 'POST',
            data: {
                userIds: userIdArray,
                ownerType: 2,
            },
            // contentType: 'application/json',
            success: function (res) {
                if (res.success) {
                    opLayer.success(res.message, "提示");
                } else {
                    opLayer.error(res.message, "提示");
                }
            },
            error: function () {
                opLayer.error("网络异常", "提示");
            }
        })
    }

    function doGetCheckedStudentAndFamilyId() {
        let ids = [];
        $('.accounts:checked').each(function () {
            let id = $(this).data('accountid')
            if ($.trim(id) != '') {
                ids.push(id);
            }
        });
        return ids;
    }

    function doGetOpenAccountInfos() {
        let accounts = [];
        $('.new-username').each(function () {
            let ac = {};
            ac.id = $(this).data('id')
            ac.username = $(this).val();
            if ($(this).hasClass('family')) {
                ac.ownerType = 3;
            } else {
                ac.ownerType = 1;
            }
            accounts.push(ac)
        });
        return accounts;
    }

    /**
     * 开通账号
     */
    function openAccount() {
        if ($('.accounts:checked').length === 0) {
            opLayer.warn("请选择需要开通的账号");
            return;
        }
        let usernameArray = [];
        $('.accounts:checked').each(function () {
            let id = $(this).val();
            let $username = $('#studentUsername_'+id).find('input[data-id="'+id+'"]');
            if ($username.length > 0) {
                usernameArray.push({
                    id: id,
                    ownerType: 1,
                    username: $username.val(),
                    unitId: ztreeId
                });
            }
            $username = $('#familyUsername_'+id).find('input[data-id="'+id+'"]');
            if ($username.length > 0) {
                usernameArray.push({
                    id: id,
                    ownerType: 3,
                    username: $username.val(),
                    unitId: ztreeId
                });
            }
        });
        if (usernameArray.length === 0) {
            opLayer.warn("请选择需要开通的账号");
            return;
        }
        $.ajax({
            url: _contextPath + '/operation/user/manage/open/account',
            type: 'POST',
            data: JSON.stringify(usernameArray),
            contentType: 'application/json;charset=utf-8',
            success: function (res) {
                opLayer.layerMsg(opLayerState.from(res.success), res.message)
                if (res.success) {
                    doGetStudentAndFamilyList('');
                }
            },
            error: function () {
                opLayer.error("网络异常");
            }
        })
    }

    function showAccountRule() {
        layer.open({
            type: 1,
            shadow: 0.5,
            title: '账号生成规则',
            area: '550px',
            btn: ['知道了'],
            content: $('.layer-rule')
        });
    }

    /**
     * 导出excel
     */
    $("#StudentAndFamilyExport").click(function () {
        let dynamic= '?clazzId=';
        let clazzId = $('#clazzOption').val();
        if (clazzId) {
            dynamic = dynamic + clazzId;
        }
        window.location.href = _contextPath + '/operation/unit/manage/export/${unitId}/getAllStudentAndFamilyAccounts'+dynamic;
    })
</script>