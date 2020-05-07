<#import "../macro/pagination.ftl" as pagination />
<#if pages??>
    <#if pages.content?? && pages.content?size gt 0>
        <table class="table table-bordered table-striped table-hover no-margin">
            <thead>
            <tr>
                <th>序号</th>
                <th>单位名称</th>
                <th>重要层级</th>
                <th>到期时间</th>
                <th>联系人</th>
                <th>类型</th>
                <th>手机号</th>
                <th>备注</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody id="unitPrincipals">
            <#list pages.content as unitPrincipalVo>
                <#assign i = 0>
                <#list unitPrincipalVo.unitPrincipals as unitPrincipal>
                    <#assign i = i + 1>
                        <tr data="${unitPrincipalVo.id}">
                        <#if i==1>
                        <td rowspan="${unitPrincipalVo.unitPrincipals?size}">${(pages.number)*pages.size+(unitPrincipalVo_index + 1)}</td>
                        <td rowspan="${unitPrincipalVo.unitPrincipals?size}" id="unitPrincipalFont_${unitPrincipalVo.id}">${unitPrincipalVo.unitName}</td>
                        <td rowspan="${unitPrincipalVo.unitPrincipals?size}" width="17%"><#if unitPrincipalVo.starLevel??>
                                <i id="starLevelState" class="float-left fa fa-arrow-up mt6
                                    <#if unitPrincipalVo.starLevel == 1> color-red"
                                    <#elseif unitPrincipalVo.starLevel == 2> color-orange"
                                    <#elseif unitPrincipalVo.starLevel == 3> color-grey"
                                    </#if>
                                ></i>
                                <#-- 修改星级-->
                                <select class="form-control js-select-level" name="unitStarLevelOption" id="unitStarLevelOption" onchange="doEditUnitLevel('${unitPrincipalVo.id}',this.value, this);" style="width: 75%;border: none;box-shadow: none;background: transparent;">
                                    <#if unitStarLevelList?? && unitStarLevelList?size gt 0>
                                        <#list unitStarLevelList as unitStarLevelVo>
                                            <#if unitStarLevelVo.unitStarLevel == unitPrincipalVo.starLevel>
                                                <option value="${unitStarLevelVo.unitStarLevel}" selected="selected">${unitStarLevelVo.represent}</option>
                                                <#else>
                                                <option value="${unitStarLevelVo.unitStarLevel}">${unitStarLevelVo.represent}</option>
                                            </#if>
                                        </#list>
                                    </#if>
                                </select>
                            </#if>
                        </td>
                        <#--判断是否过期-->
                        <td rowspan="${unitPrincipalVo.unitPrincipals?size}"
                            <#if unitPrincipalVo.expireTime?? && unitPrincipalVo.expireDay<0>class="color-ccc"
                            <#elseif unitPrincipalVo.expireTime?? && unitPrincipalVo.expireDay<=30>class="color-red"
                            </#if>
                        >
                            <#if unitPrincipalVo.expireTime??>
                                <#if unitPrincipalVo.expireDay < 0>已到期
                                <#elseif unitPrincipalVo.expireDay < 30>剩${unitPrincipalVo.expireDay}天到期
                                <#else>${unitPrincipalVo.expireTime?string('yyyy/MM/dd')}
                                </#if>
                                <#else>永久
                            </#if>
                        </td>
                        </#if>
                            <td>${unitPrincipal.realName}</td>
                            <td>${unitPrincipalTypeList[unitPrincipal.type].represent}</td>
                            <td>${unitPrincipal.phone}</td>
                            <td>${unitPrincipal.remark}</td>
                        <#if i==1>
                            <td rowspan="${unitPrincipalVo.unitPrincipals?size}">
                                <a data-id="${unitPrincipalVo.id}" class="table-btn color-blue js-edit" href="#">编辑</a><#--javascript:;-->
                            </td>
                      </#if>
                </tr>
                </#list>
            </#list>
            </tbody>
        </table>
        <@pagination.paginataion pages=pages containerId='unitPrincipals' pageCallFunction='doGetUnitPrincipalList' />
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
<!-- 编辑框 -->
<div class="layer layer-edit" id="unitPrincipal-edit-div">
    <input type="hidden" value="" id="unitAccountId"/>
    <div class="layer-content">
        <p><span class="mr20" id="unitName"></span><span class="mr20" id="unitExpireTime"></span></p>
        <table class="table table-bordered table-striped table-hover no-margin ppp">
            <thead>
            <tr>
                <th>姓名</th>
                <th>类型</th>
                <th>电话</th>
                <th>备注</th>
            </tr>
            </thead>
            <tbody>
            <tr class="row-0 row-index">
                <input class="id-row-0" type="hidden" value="" id="unitPrincipalId"/>
                <td class="name-row-0" width="20%"></td>
                <td class="type-row-0" width="20%">
                    <select class="form-control" name="" id="">
                        <#if unitPrincipalTypeList?? && unitPrincipalTypeList?size gt 0>
                            <#list unitPrincipalTypeList as unitPrincipalTypVo>
                                <option value="${unitPrincipalTypVo.unitPrincipalType}">${unitPrincipalTypVo.represent}</option>
                            </#list>
                        </#if>
                    </select>
                </td>
                <td class="phone-row-0 form-group">
                    <input id="mobilePhone" name="mobilePhone" type="text" class="form-control">
                </td>
                <td class="remark-row-0">
                    <input type="text" class="form-control">
                </td>
            </tr>
            <tr class="row-1 row-index">
                <input class="id-row-1" type="hidden" value="" id="unitPrincipalId"/>
                <td class="name-row-1"></td>
                <td class="type-row-1" width="20%">
                    <select class="form-control" name="" id="">
                        <#if unitPrincipalTypeList?? && unitPrincipalTypeList?size gt 0>
                            <#list unitPrincipalTypeList as unitPrincipalTypVo>
                                <option value="${unitPrincipalTypVo.unitPrincipalType}">${unitPrincipalTypVo.represent}</option>
                            </#list>
                        </#if>
                    </select>
                </td>
                <td class="phone-row-1 form-group">
                    <input id="mobilePhone" name="mobilePhone" type="text" class="form-control")>
                </td>
                <td class="remark-row-1">
                    <input type="text" class="form-control">
                </td>
            </tr>
            <tr class="row-2 row-index">
                <input class="id-row-2" type="hidden" value="" id="unitPrincipalId"/>
                <td class="name-row-2"></td>
                <td class="type-row-2" width="20%">
                    <select class="form-control" name="" id="">
                        <#if unitPrincipalTypeList?? && unitPrincipalTypeList?size gt 0>
                            <#list unitPrincipalTypeList as unitPrincipalTypVo>
                                <option value="${unitPrincipalTypVo.unitPrincipalType}">${unitPrincipalTypVo.represent}</option>
                            </#list>
                        </#if>
                    </select>
                </td>
                <td class="phone-row-2 form-group">
                    <input id="mobilePhone" name="mobilePhone" type="text" class="form-control">
                </td>
                <td class="remark-row-2">
                    <input type="text" class="form-control">
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>

<script>
$(function () {
    // 编辑
    $('.js-edit').on('click', function(){
        $('.ppp').find("input").val('');
        loadUnitPrincipalInfo($(this).data('id'), function(){
            layer.open({
                type: 1,
                shadow: 0.5,
                title: '编辑',
                area: ['580px'],
                btn: ['确定', '取消'],
                content: $('.layer-edit'),
                yes: function (index) {
                    layer.close(index);
                    saveUnitPrincipalsInfo();
                },
                cancel: function (index) {
                    layer.close(index);
                    doGetUnitPrincipalList('');
                }
            });
        });
    });

    function loadUnitPrincipalInfo(id, call) {
        $.ajax({
            url: _contextPath + '/operation/unitPrincipal/manage/unitPrincipalAccount',
            data: {
                unitId:id
            },
            type: "GET",
            success: function (res) {
                if (res.success) {
                    let unitPrincipalAccountVo = res.unitPrincipalAccount;
                    let unitPrincipalList = unitPrincipalAccountVo.unitPrincipals;
                    // 隐藏域单位id传递数据
                    $("#unitAccountId").val(unitPrincipalAccountVo.id);
                    // 单位数据
                    $("#unitName").text("单位名称："+unitPrincipalAccountVo.unitName).val(unitPrincipalAccountVo.unitName);
                    // 计算剩余天数
                    let expireTime = unitPrincipalAccountVo.expireTime;
                    let expireDay;
                    if (expireTime != null) {
                        expireDay = Math.ceil(Number((expireTime-new Date().getTime())/(1000*60*60*24)));
                        if (expireDay < 0) {
                            $("#unitExpireTime").text("到期时间：已到期");
                        }else if (expireDay < 30) {
                            $("#unitExpireTime").text("到期时间：剩余"+expireDay+"天");
                            $("#unitExpireTime").addClass("color-red");
                        } else {
                            let expireDate = new Date(expireTime);
                            $("#unitExpireTime").text("到期时间："+timeToDate(expireDate));
                        }
                    }else {
                        $("#unitExpireTime").text("到期时间：永久");
                    }
                    // 初始化
                    for (var index = 0; index <= 2; index++) {
                        $(".row-"+index).hide();
                    }
                    for (var index = 0; index < unitPrincipalList.length; index++)  {
                        $(".row-"+index).show();
                        $(".id-row-"+index).val('');
                        $(".name-row-"+index).first().text('').val('');
                        $(".phone-row-"+index+" input").first().text('').val('');
                        $(".remark-row-"+index+" input").first().text('').val('');
                        $(".type-row-"+index+" select").first().find("option[value='0']").attr("selected",true);
                    }

                    $.each(unitPrincipalList,function (index, element) {
                        $(".id-row-"+index).val(element.id);
                        $(".name-row-"+index).first().text(element.realName).val(element.realName);
                        $(".phone-row-"+index+" input").first().text(element.phone).val(element.phone);
                        $(".remark-row-"+index+" input").first().text(element.remark).val(element.remark);
                        // 设置单位联系人类型下拉框选中
                        $(".type-row-"+index+" select").first().val(element.type)
                    })
                    if (typeof call === 'function') {
                        call();
                    }
                } else {
                    opLayer.error(res.message);
                }
            }
        })
    }
    // 手机号校验
    $("#unitPrincipal-edit-div").bootstrapValidator({
        excluded: [":disabled"],
        feedbackIcons: {
            valid: 'glyphicon /*glyphicon-ok*/',
            invalid: 'glyphicon /*glyphicon-remove*/',
            validating: 'glyphicon /*glyphicon-refresh*/'
        },
        autoFocus: true,
        fields: {
            "mobilePhone":{
                validators: {
                    notEmpty: {
                        message: "手机号不能为空!"
                    },
                    chineseMobile: {
                    }
                }
            }
        }
    });
    // 更新联系人信息
    function saveUnitPrincipalsInfo() {
        // 获取单位id
        var unitId = $("#unitAccountId").val();
        var unitName = $("#unitName").val();
        // 获取单位联系人列表
        var json = '{"unitId":"'+unitId+'","unitName":"'+unitName+'", "unitPrincipals":[';
        $.each($(".row-index"), function (index) {
            if ($(".id-row-"+index).first().val().trim() != '') {
                json += JSON.stringify({
                    id: $(".id-row-" + index).first().val().trim(),
                    realName: $(".name-row-" + index).val(),
                    type: $(".type-row-" + index + " option:selected").first().val(),
                    phone: $(".phone-row-" + index + " input").first().val(),
                    remark: $(".remark-row-" + index + " input").first().val(),
                    unitId: unitId
                });
                if (index <= 1) {
                    json += ",";
                }
            }
        })
        json = (json.substring(json.length-1) == ',') ? json.substring(0,json.length - 1) : json;
        json += "]}";
       // 请求到后台
       $.ajax({
               url: _contextPath + "/operation/unitPrincipal/manage/unitPrincipalAccount",
               type: "PUT",
               data:json,
               contentType:'application/json',
               success:function(res) {
                   opLayer.layerMsg(opLayerState.from(res.success), res.message);
                   doGetUnitPrincipalList('');
               }
           }
       );
    }
    function timeToDate(expireTime) {
        var expireDate = new Date(expireTime);
        var year = expireDate.getFullYear();
        var month = expireDate.getMonth() + 1 < 10?"0"+(expireDate.getMonth() + 1):expireDate.getMonth() + 1;
        var day = expireDate.getDate()+1 < 10?"0"+(expireDate.getDate()+1):expireDate.getDate()+1;
        return year + "-" + month + "-" + day;
    }

})
// 修改单位等级
function doEditUnitLevel(unitId, starLevel, obj) {
    // 修改箭头颜色 updateUnitStarlLevel
    if (starLevel == 1) {
        $(obj).siblings("i").addClass("color-red").removeClass("color-orange color-grey")
    } else if (starLevel == 2) {
        $(obj).siblings("i").addClass("color-orange").removeClass("color-red color-grey")
    } else if (starLevel == 3) {
        $(obj).siblings("i").addClass("color-grey").removeClass("color-red color-orange")
    }
    $.ajax({
        url:_contextPath + "/operation/unitPrincipal/updateUnitStarLevel",
        type:'PUT',
        data:{
            'unitId':unitId,
            'starLevel':starLevel
        },
        success: function (res) {
            opLayer.layerMsg(opLayerState.from(res.success), res.message);
            doGetUnitPrincipalList('');
        }
    })
}
</script>