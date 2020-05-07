<div class="box-header">
    <h3 class="box-caption" id="nextTitleName">新增单位</h3>
</div>
<div class="box-body">
    <div class="form-horizontal" role="form">
        <div class="form-group">
            <label class="col-sm-2 control-title no-padding-right"><span class="form-title">授权管理</span></label>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">系统授权：</label>
            <div class="col-sm-8">
                <div class="filter">
                    <div class="filter-item">
                        <div class="filter-content">
                            <button class="btn btn-blue js-accredit">批量授权</button>
                        </div>
                    </div>
                    <div class="filter-item filter-item-right">
                        <div class="filter-content">
                            <div class="input-group input-group-search">
                                <div class="pull-left">
                                    <input type="text" class="form-control" id="searchSystems">
                                </div>
                                <div class="input-group-btn">
                                    <button type="button" class="btn btn-default">
                                        <i class="fa fa-search" onclick="searchSys()"></i>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <table class="table table-bordered table-striped table-hover no-margin">
                    <thead>
                    <tr>
                        <th><label><input id="system_accounts_checkbox_all" type="checkbox" name="" class="wp systemChecked"><span
                                        class="lbl"></span></label></th>
                        <th width="150">系统名称</th>
                        <th width="300">是否授权</th>
                        <th width="300">系统到期时间</th>
                    </tr>
                    <#list allSystem as allS>
                        <tr data=${allS.code} id="unAuthorizedSys_${allS.code}" class="unAuthorizedSys">
                            <td>
                                <label>
                                    <input value="${allS.code}" type="checkbox" name=""
                                           class="wp system_accounts_checkbox systemChecked">
                                    <span class="lbl"></span>
                                </label>
                            </td>
                            <td>${allS.name}</td>
                            <td>
                                <label class="no-margin-top"><input data-id="${allS.code}" type="radio" name="unitType${allS.code}" checked="" class="wp wsq" value="-1"><span class="lbl"> 未授权</span></label>
                                <label class="no-margin-top"><input data-id="${allS.code}" type="radio" name="unitType${allS.code}" class="wp sy" value="0"><span class="lbl"> 试用</span></label>
                                <label class="no-margin-top"><input data-id="${allS.code}" type="radio" name="unitType${allS.code}" class="wp zs" value="1"><span class="lbl"> 正式</span></label>
                            </td>
                            <td>
                                <label class="float-left mt3">
                                    <input type="radio" class="wp hideDate" data-id="${allS.code}" id="yj_${allS.code}" name="unitProperty${allS.code}" value="0">
                                    <span class="lbl"> 永久</span>
                                </label>
                                <label class="float-left mt3">
                                    <input type="radio" class="wp showDate" data-id="${allS.code}" id="zdy_${allS.code}" name="unitProperty${allS.code}" value="1">
                                    <span class="lbl"> 自定义</span>
                                </label>
                                <div class="input-group float-left dateTime" style="width: 100%;" id="timeDiv_${allS.code}">
                                    <input class="form-control authorizedatetimepicker" type="text" id="expireTime_${allS.code}">
                                    <span class="input-group-addon">
																			<i class="fa fa-calendar"></i>
																		</span>
                                </div>
                            </td>

                        </tr>
                    </#list>
                    </thead>
                </table>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right"></label>
            <div class="col-sm-8">
                <button class="btn btn-blue btn-infirm">确定</button>
                <button class="btn btn-white btn-return">上一步</button>
                <button class="btn btn-white btn-cancel">取消</button>
            </div>
        </div>
    </div>
</div>


<!-- 错误框 -->
<div class="layer layer-error">
    <div class="layer-content">
        <table width="100%">
            <tr>
                <td class="text-right" valign="top">
                    <span class="fa fa-times-circle color-red font-30 mr20"></span>
                </td>
                <td>
                    <div class="font-16 mb10">抱歉，新增单位失败</div>
                    <div class="color-grey" id="errorMes"></div>
                </td>
            </tr>
        </table>
    </div>
</div>

<!-- 正确框 -->
<div class="layer layer-correct">
    <div class="layer-content">
        <table width="100%">
            <tr>
                <td class="text-right" valign="top">
                    <span class="fa fa-check-circle color-green font-30 mr20"></span>
                </td>
                <td>
                    <div class="font-16 mb10">恭喜，新增单位成功</div>
                    <div class="box-body background-f5f5f5 mt20">
                        <div>
                            <span>账号信息<span class="color-red font-12 ml10">密码过于简单，请修改</span></span>
                        </div>
                        <div>
									<span class="inline-block mr20">
										<span class="color-grey">用户名：</span>
										<span id="user"></span>
									</span>
                            <span class="inline-block mr20">
										<span class="color-grey">密码：</span>
										<span>123456</span>
									</span>
                        </div>
                    </div>
                </td>
            </tr>
        </table>
    </div>
</div>

<!-- 批量授权 -->
<div class="layer layer-accredit">
    <div class="layer-content">
        <div class="form-horizontal" role="form">
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right">授权类型：</label>
                <div class="col-sm-7">
                    <label><input type="radio" class="wp" name="typeRadio" value="-1" checked=""><span class="lbl"> 未授权</span></label>
                    <label><input type="radio" class="wp" name="typeRadio" value="0" onclick="controllerRadio1()"><span class="lbl"> 试用</span></label>
                    <label><input type="radio" class="wp" name="typeRadio" value="1" onclick="controllerRadio2()"><span class="lbl"> 正式</span></label>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right">系统到期时间：</label>
                <div class="col-sm-7">
                    <label class="float-left"><input type="radio" class="wp" name="timeRadio" id="yjRadio" value="0" onclick="hidedate()" checked=""><span class="lbl"> 永久</span></label>
                    <label class="float-left"><input type="radio" class="wp" name="timeRadio" id="zdyRadio" value="1" onclick="showdate()"><span class="lbl"> 自定义</span></label>
                    <div class="float-left hide" id="date-picker-div" style="width: 35%;">
                        <div class="input-group">
                            <input class="form-control date-picker authorizedatetimepicker" type="text">
                            <span class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>




<script>
    $(function(){
        //全选
        $('#system_accounts_checkbox_all').change(function () {
            if ($(this).is(':checked')) {
                //$('#unitAuthorizationContainer').find('input.systemChecked:not(:checked)').trigger('click')
                $('.system_accounts_checkbox').each(function () {
                    if(!$(this).parents('tr').hasClass('hide')){
                        $(this).trigger('click');
                    }
                })
            } else {
                $('#unitAuthorizationContainer').find('input.systemChecked:checked').trigger('click');
            }
        });
        /**
         * 根据上一个页面初始化下一步的名称和按钮
         */
        $("#nextTitleName").html($("#titleConvert").html())
        $("#user").html($("#username").val());
        if($("#tryUse").prop("checked")==true){
            $(".no-margin-top input[type=radio][value=1]").attr("disabled",true);
            $("input[name='typeRadio'][value=1]").attr("disabled",true);
        }
        if ($("#custom").prop("checked")==true) {
            $(".mt3 input[type=radio][value=0]").prop("disabled",true);//永久不能用
            $(".mt3 input[type=radio][value=1]").prop("checked","checked");//默认自定义选定
            $("input[name='timeRadio'][value=0]").attr("disabled",true);
            $("input[name='timeRadio'][value=1]").prop("checked","checked");
            $('#date-picker-div').removeClass("hide");
        }else {
            $(".mt3 input[type=radio][value=0]").prop("checked","checked");//默认永久选定
        }
        if ($(".mt3 input[type=radio][value=0]").prop("checked")==true){
            $(".dateTime").addClass("hide")
        }

        //freemarker进行按钮回显选定
        <#if authorizeSystem??>
            <#list authorizeSystem as authorizeS>
            <#if authorizeS.serverCode??>
                    $("input[type=radio][name=unitType${authorizeS.serverCode}]").eq(0).parents('tr').attr('ex_id','${authorizeS.id}');
                <#if authorizeS.usingNature==1>
                    $("input[type=radio][name=unitType${authorizeS.serverCode}][value=1]").prop("checked","checked")
                <#elseif authorizeS.usingNature==0>
                     $("input[type=radio][name=unitType${authorizeS.serverCode}][value=0]").prop("checked","checked")
                </#if>
                <#if authorizeS.expireTime??>
                    $("input[type=radio][name=unitProperty${authorizeS.serverCode}][value=1]").prop("checked","checked")
                    $("#timeDiv_${authorizeS.serverCode}").removeClass("hide")
                    $("#expireTime_${authorizeS.serverCode}").val('${authorizeS.expireTime?string('yyyy/MM/dd')}')
                <#else>
                    if($("#permanent").prop("checked")==true){
                        $("input[type=radio][name=unitProperty${authorizeS.serverCode}][value=0]").prop("checked","checked")
                        $("#timeDiv_${authorizeS.serverCode}").addClass("hide");
                    }else{
                        $("input[type=radio][name=unitProperty${authorizeS.serverCode}][value=1]").prop("checked","checked")
                        $("#timeDiv_${authorizeS.serverCode}").removeClass("hide");
                        $("#expireTime_${authorizeS.serverCode}").val($("#expireTime").val().trim());
                    }
                </#if>
            </#if>
            </#list>
        </#if>
        /**
         * 按钮事件
         */
        $(".form-group").on("click",".btn-return",returnToInsert)
                .on("click",".btn-cancel",returnList)
                .on("click",".btn-infirm",saveInsert)
                .on("click",".sy",controllerRadio1)
                .on("click",".zs",controllerRadio2)
                .on("click",".hideDate",hidedate)
                .on("click",".showDate",showdate);

    })

    //点击试用按钮
    function controllerRadio1() {
        var systemId = $(this).data('id');
        $('#zdy_'+systemId).prop('checked',true);
        $('#yj_'+systemId).prop('disabled',true);
        $("#timeDiv_"+systemId).removeClass("hide");
        $('#zdyRadio').prop('checked',true);
        $('#yjRadio').prop('disabled',true);
        $('#date-picker-div').removeClass("hide");
    }
    //正式按钮
    function controllerRadio2() {
        if($("#permanent").prop("checked")==true){
            var systemId = $(this).data('id');
            $('#yj_'+systemId).prop('disabled',false);
            $('#yj_'+systemId).prop('checked',true);
            $("#timeDiv_"+systemId).addClass("hide")
        }
        $('#yjRadio').prop('disabled',false);
        $('#yjRadio').prop('checked',true);
        $('#date-picker-div').addClass("hide");
    }

    //上一步
    function returnToInsert() {
        $("#unitAuthorizationContainer").addClass("hide")
        $('#createUnit').removeClass("hide")
    }
    //取消按钮
    function returnList() {
        $('#unitAuthorizationContainer').addClass('hide');
        $("#unitListContainer").removeClass('hide');
    }

    //确定 按钮
    function saveInsert() {
        var flag = true;
        var systems = [];
        $('#unitAuthorizationContainer table tr').each(function () {   // 遍历 tr
            var aParams = {};
            var radioVal = $(this).children('td:eq(2)').find("input[type=radio]:checked").val();
            if (radioVal == -1 || typeof (radioVal) == 'undefined') {
                return true;
            }
            var radioVal2 = $(this).children('td:eq(3)').find("input[type=radio]:checked").val();
            aParams.usingState = 0;//默认使用状态为正常
            aParams.serverCode = $(this).attr("data");//关联系统id
            var system_id = $(this).attr("data");
            //设置系统到期时间
            if (radioVal2 == 0) {//永久

                aParams.expireTime = null;
            } else if (radioVal2 == 1) {//自定义
                if ($("#expireTime_" + system_id).val() == null || $("#expireTime_" + system_id).val() == "") {
                    if (radioVal == '1') {
                        flag = false;
                        opLayer.warn("请填写系统的自定义时间", "警告");
                        return false;
                    }
                    aParams.expireTime = null;
                } else {//系统设置了时间，如果单位为永久单位，则系统时间
                    // 需要大于当前时间；如果单位为自定义时间，则需要判断系统时间不能超过单位时间
                    if ($("#permanent").prop("checked")==true) {
                        var expireTime = $("#expireTime_" + system_id).val();
                        var nowDate = getNowTime();
                        var expire = new Date(expireTime);
                        var now = new Date(nowDate);
                        if (expire < now) {
                            flag = false;
                            opLayer.warn("自定义时间不能早于当前时间", "警告");
                            return false;
                        } else {
                            aParams.expireTime = $("#expireTime_" + system_id).val();
                        }
                    } else {
                        var unitTime = $("#expireTime").val();
                        var serverTime = $("#expireTime_" + system_id).val();
                        var unit = new Date(unitTime);
                        var server = new Date(serverTime);
                        if (unit < server) {
                            flag = false;
                            opLayer.warn("系统时间不能超过单位时间", "警告");
                            return false;
                        } else {
                            aParams.expireTime = $("#expireTime_" + system_id).val();
                        }
                    }
                }
            }
            //参数 使用性质 试用还是正式
            if (radioVal == 0) {
                var s_id = $(this).attr("ex_id");
                aParams.id = s_id;
                aParams.usingNature = 0;//试用
                systems.push(aParams);
            } else if (radioVal == 1) {
                var s_id = $(this).attr("ex_id");
                aParams.id = s_id;
                aParams.usingNature = 1;//正式
                systems.push(aParams);
            }
        });
        var serverExtensions=JSON.stringify(systems);
        let unitInfo = $('#unitAddForm').serialize();
        var opUnitPrincipalDtos = new Array();
        $(".opUnitPrincipalDto").each(function () {
            var params = {
                realName:$(this).find(".unitPrincipalRealName").val(),
                type:$(this).find(".unitPrincipalType").val(),
                phone:$(this).find(".unitPrincipalPhone").val(),
                remark:$(this).find(".unitPrincipalRemark").val()
            }
            opUnitPrincipalDtos.push(params);
        });
        unitInfo=unitInfo+"&unit.id="+$("#toConfirm").attr("data")+"&unit.unitType="+$("#unitType").val()
        if (flag) {
            var url = _contextPath + '/operation/unit/manage/page/saveAllUnit';
            var url2 = _contextPath + '/operation/unit/manage/page/updateUnitById';
            if ($("#titleConvert").html()!='新增单位') {
                $.ajax({
                    url: url2,
                    type: 'POST',
                    data: unitInfo + '&' + "serverExtensions="+serverExtensions + '&opUnitPrincipalDtos=' + JSON.stringify(opUnitPrincipalDtos),
                    success: function (res) {
                        if (res.success) {
                            message.successMessage($("#nextTitleName").html() + "成功",function () {
                                returnUnitList();
                                $('#unitAccounts').load(_contextPath + '/operation/unit/manage/page/unitList?parentId='+ztreeId);
                            });
                        } else {
                            opLayer.error(res.message, "提示");
                        }
                    }
                })
            } else {
                $.ajax({
                    url: url,
                    type: 'POST',
                    data: unitInfo + '&' + "serverExtensions="+serverExtensions + '&opUnitPrincipalDtos=' + JSON.stringify(opUnitPrincipalDtos),
                    success: function (res) {
                        if (res.success) {
                            layer.open({
                                type: 1,
                                shadow: 0.5,
                                title: false,
                                area: '450px',
                                btn: ['确定'],
                                content: $('.layer-correct'),
                                yes: function (index) {
                                    layer.close(index);
                                    $('.page-content').load(_contextPath + "/operation/unit/manage/page/index");
                                },
                                cancel: function () {
                                    $('.page-content').load(_contextPath + "/operation/unit/manage/page/index");
                                }

                            });
                        } else {
                            $("#errorMes").html(res.message);
                            layer.open({
                                type: 1,
                                shadow: 0.5,
                                title: false,
                                area: '300px',
                                btn: ['取消', '重试'],
                                content: $('.layer-error'),
                                btn2: function () {
                                    saveInsert();
                                }
                            });
                        }
                    }
                })
            }
        }
    }
    //点击永久按钮 隐藏当前行的时间框
    function hidedate(){
        var system_id=$(this).data("id");
        $("#timeDiv_"+system_id).addClass("hide")
        $('#date-picker-div').addClass("hide")
    }
    //点击自定义按钮 显示当前行的时间框
    function showdate(){
        var system_id=$(this).data("id");
        $("#timeDiv_"+system_id).removeClass("hide")
        $('#date-picker-div').removeClass("hide")
    }
    //时间组件js
    $('.authorizedatetimepicker').datepicker({
        language: 'zh-CN',
        autoclose: true,
        todayHighlight: true,
        format: 'yyyy/mm/dd',
        startDate:new Date()
    }).next().on('click', function(){    //点击图标显示日历
        $(this).prev().focus();
    });
    //获取当前时间
    function getNowTime(){
        var d = new Date(),str = '';
        str += d.getFullYear()+'/';
        str  += d.getMonth() + 1+'/';
        str  += d.getDate();
        return str;
    }

    // 批量授权
    $('.js-accredit').on('click', function(){
        layer.open({
            type: 1,
            shadow: 0.5,
            title: '批量授权',
            area: '620px',
            btn: ['确定', '取消'],
            content: $('.layer-accredit'),
            yes:function (index) {
                $('#unitAuthorizationContainer').find('input.systemChecked:checked').each(function () {
                    var typeCode = $("input[name='typeRadio']:checked").val();
                    var timeCode = $("input[name='timeRadio']:checked").val();
                    if (typeCode==-1){
                        $(this).parents('tr').find('.wsq').prop('checked',true);
                    } else if (typeCode==0){
                        $(this).parents('tr').find('.sy').prop('checked',true);
                    }else {
                        $(this).parents('tr').find('.zs').prop('checked',true);
                    }
                    if (timeCode==0){
                        $(this).parents('tr').find('.hideDate').prop('checked',true).click();
                    } else {
                        $(this).parents('tr').find('.showDate').prop('checked',true).click();
                        $(this).parents('tr').find('.authorizedatetimepicker').val($('.date-picker').val());
                    }
                });
                layer.close(index);
            }
        });
    });

    /**
     * 搜索
     */
    $('#searchSystems').bind('input propertychange',searchSys);
    $('#searchSystems').bind('keypress',function(event){
        if(event.keyCode == 13) {searchSys}
    });

    var unAthorizedMap={};
    <#if allSystem??>
    <#list allSystem as sys>
    unAthorizedMap.id_${sys.code}='${sys.name}'
    </#list>
    </#if>

    function searchSys() {
        var str=$('#searchSystems').val().trim()
        //全部隐藏
        $(".unAuthorizedSys").addClass('hide');
        $.each(unAthorizedMap,function(id,name){//能和搜索的匹配则显示
            if(name.indexOf(str)!=-1){
                $('#unAuthorizedSys_'+id.substring(3,id.length)).removeClass('hide');
            }
        })
    }
</script>