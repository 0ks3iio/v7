<div class="box-header">
    <h3 class="box-caption">授权管理</h3>
</div>
    <div class="box-body">
        <div class="form-horizontal" role="form">
            <div class="form-group">
                <label class="col-sm-2 control-label no-padding">单位名称：</label>
                <div class="col-sm-8" id="unitName"></div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label no-padding">单位有效期：</label>
                <div class="col-sm-8" id="unitExpireTime">

                </div>
            </div>


            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right">系统授权：</label>
                <div class="col-sm-8">
                    <div class="tab-container">
                        <div class="tab-header clearfix">
                            <ul class="nav nav-tabs nav-tabs-small">
                                <li class="active">
                                    <a data-toggle="tab" href="#a1">已授权</a>
                                </li>
                                <li class="">
                                    <a data-toggle="tab" href="#a2">未授权</a>
                                </li>
                            </ul>
                        </div>
                        <!-- tab切换开始 -->

                        <div class="tab-content">
                            <div id="a1" class="tab-pane active">
                                <table class="table table-bordered table-striped table-hover no-margin">
                                    <thead>
                                    <tr>
                                        <th>系统名称</th>
                                        <th>授权状态</th>
                                        <th>系统到期时间</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <#if unitInfo??>
                                    <#list authorizedSystem as sys>

                                        <#assign color>
                                            <#if sys.usingState gt 0 || sys.dayApart lte 0>
                                            color-ccc
                                            <#elseif sys.dayApart gt 0 && sys.dayApart lte 30>
                                            color-red
                                            <#else>
                                            color-null
                                            </#if>
                                        </#assign>

                                     <tr data=${sys.id!}>
                                         <td>${sys.name!}</td>
                                         <td>
                                             <#if sys.usingNature==1>
                                                 <span class=${color}>正式</span>
                                             <#else>
                                                 <span class=${color}>试用</span>
                                             </#if>
                                             <#if sys.usingState == 1>
                                                 <div class="badge badge-yellow ml10">停用</div>
                                             </#if>
                                         </td>

                                     <#-- 系统到期时间-->
                                         <#if !(sys.expireTime)??>
                                                <td class='${color} followUnit' ></td>

                                         <#else >
                                             <#if sys.dayApart lt 1>
                                                 <td class=${color}> 已到期</td>
                                             <#elseif sys.dayApart lte 30>
                                                <td class=${color}> 剩${sys.dayApart}天到期</td>
                                             <#else>
                                                 <td class=${color}>${(sys.expireTime)?string('yyyy/MM/dd')}</td>
                                             </#if>
                                         </#if>
                                         <td>

                                         <!-- 操作按钮-->
                                         <#if unitInfo.usingState==0>
                                            <#if sys.usingState gt 0>
                                                <a class="color-blue table-btn sysRecover" href="#">恢复使用</a>
                                            <#else>
                                                <#if unitInfo.usingNature==1>
                                                    <#if sys.usingNature==0><a class="color-blue table-btn sysToNomal" href="#">试用转正式</a></#if>
                                                </#if>
                                                <#if (sys.expireTime)??>
                                                    <#if (unitInfo.expireTime)??>
                                                        <#if (sys.expireTime)?date lt (unitInfo.expireTime)?date>
                                                            <a class="color-blue table-btn renewal pos-rel sysRenewal" href="#">续期</a>
                                                        </#if>
                                                    <#else>
                                                        <a class="color-blue table-btn renewal pos-rel sysRenewal" href="#">续期</a>
                                                    </#if>

                                                </#if>
                                                <a class="color-blue table-btn sysStop" href="#">停用</a>
                                            </#if>

                                         </#if>
                                         </td>

                                     </tr>
                                    </#list>

                                    </#if>
                                    </tbody>
                                </table>
                            </div>

                            <!-- ++++++++++++++++++++++++++++++++ 未授权 +++++++++++++++++++++++++++++++++++++++++++++++++ -->
                            <div id="a2" class="tab-pane" style="width:114%">
                                <div class="filter">
                                    <div class="filter-item">
                                        <button type="button" class="btn btn-blue btn-sm" id="save">保存</button>
                                    </div>
                                    <div class="filter-item">
                                        <label class="filter-name">搜索:</label>
                                        <input type="text" class="form-control" id="sysSearch">
                                    </div>
                                </div>
                                <div style="max-height: 500px;overflow-y: scroll;">
                                    <table class="table table-bordered table-striped table-hover no-margin">
                                        <tbody>
                                        <#list unAuthorizedSystem as sys>
                                        <tr data=${sys.code} id="unAuthorizedSys_${sys.code}" class="unAuthorizedSys">
                                            <td>${sys.name}</td>
                                            <td>
                                                <label class="no-margin-top"><input data-id="${sys.code}" type="radio"
                                                                                    name="unitType${sys_index}"
                                                                                    checked="" class="wp wsq"
                                                                                    value="-1"><span
                                                        class="lbl"> 未授权</span></label>
                                                <label class="no-margin-top"><input data-id="${sys.code}" type="radio"
                                                                                    name="unitType${sys_index}"
                                                                                    class="wp sy" value="0"><span
                                                        class="lbl"> 试用</span></label>
                                                <label class="no-margin-top"><input data-id="${sys.code}" type="radio"
                                                                                    name="unitType${sys_index}"
                                                                                    class="wp zs" value="1"><span
                                                        class="lbl"> 正式</span></label>
                                            </td>
                                            <td>
                                                <label class="float-left mt3">
                                                    <input type="radio" class="wp" id="yj_${sys.code}"
                                                           name="unitProperty${sys_index}" value="0">
                                                    <span class="lbl"> 永久</span>
                                                </label>
                                                <label class="float-left mt3">
                                                    <input type="radio" checked="checked" class="wp zdy" id="zdy_${sys.code}"
                                                           name="unitProperty${sys_index}" value="1">
                                                    <span class="lbl"> 自定义</span>
                                                </label>
                                                <div class="input-group float-left" style="width: 50%;">
                                                    <input class="form-control datetimepicker" type="text"
                                                           id="expireTime_${sys.code}">
                                                    <span class="input-group-addon">
																			<i class="fa fa-calendar"></i>
																		</span>
                                                </div>
                                            </td>

                                        </tr>
                                        </#list>
                                        </tbody>
                                    </table>
                                </div>

                            </div>
                        </div>

                        <!-- tab切换结束 -->
                    </div>
                </div>
            </div>


            <div class="form-group">
                <label class="col-sm-2 control-label no-padding-right"></label>
                <div class="col-sm-8">
                    <button class="btn btn-blue" id="back">返回</button>
                </div>
            </div>
        </div>
    </div>







<!-- 停用确认框 -->
		<div class="layer layer-stop-sys">
            <div class="layer-content">
                <table width="100%">
                    <tr>
                        <td class="text-right" valign="top">
                            <span class="fa fa-exclamation-circle color-yellow font-30 mr20"></span>
                        </td>
                        <td>
                            <div class="font-16 mb10">确定要停用此系统吗？</div>
                            <div class="color-grey">停用后与其相关的服务都会被停用!</div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>


<!-- 试用转正式 -->
		<div class="layer layer-nomal-sys">
            <div class="layer-content">
                <table width="100%">
                    <tr>
                        <td class="text-right" valign="top">
                            <span class="fa fa-exclamation-circle color-yellow font-30 mr20"></span>
                        </td>
                        <td>
                            <div class="font-16 mb10">确定将此系统由试用转为正式吗？</div>
                            <div class="color-grey">确定有将转为正式</div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
<!-- 恢复确认框 -->
		<div class="layer layer-recover-sys">
            <div class="layer-content">
                <table width="100%">
                    <tr>
                        <td class="text-right" valign="top">
                            <span class="fa fa-exclamation-circle color-yellow font-30 mr20"></span>
                        </td>
                        <td>
                            <div class="font-16 mb10">确定要恢复此系统吗？</div>
                            <div class="color-grey">恢复后可正常使用此系统!</div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>


<script>
    //未授权子系统key:map---->name:id
    var unAthorizedMap={};
    <#if unAuthorizedSystem??>
        <#list unAuthorizedSystem as sys>
            unAthorizedMap.id_${sys.code}='${sys.name}'
        </#list>
    </#if>

    $(function () {

        $('#unitName').html(trUnit.unitName);

        $('#unitExpireTime').html(trUnit.expireTime);

        $(".form-group").on("click", ".btn-infirm", saveInsert)
                .on("click", ".sy", controllerRadio1)
                .on("click", ".zs", controllerRadio2)
                .on("click", ".hideDate", hidedate)
                .on("click", ".showDate", showdate);


        $("#save").click(function () {
            saveInsert();
        });
        $("#back").click(function () {
             $("#createUnit").removeData("params");
             $('#createUnit').addClass('hide').siblings('div').removeClass('hide');
        });
        //恢复使用
        $(".sysRecover").click(function () {

            var id = $(this).parents("tr").attr("data");
            doChange(id, "/state/0", "layer-recover-sys");
        });
        //试用->正式
        $(".sysToNomal").click(function () {
            var id = $(this).parents("tr").attr("data");
            doChange(id, "/nature/official", "layer-nomal-sys");
        });
        //停用
        $(".sysStop").click(function () {
            var id = $(this).parents("tr").attr("data");
            doChange(id, "/state/1", "layer-stop-sys");
        });
        //续期弹窗----弹窗加载
        $(".sysRenewal").each(function () {
            var id = $(this).parents("tr").attr("data");
            var deleteLayer = '<div class="modify-name-layer" style="width: 350px; display: block;">\
									<table width="100%" class="mt20 mb20">\
										<tbody>\
											<tr>\
												<td rowspan="2" valign="top" width="70">系统续期：</td>\
												<td>\
													<p class="mb10"><label class="permanent"><input type="radio"  name="renewalType_' +id+ '" class="wp yj xqyj" value="0"><span class="lbl"> 永久</span></label></p>\
												</td>\
											</tr>\
											<tr>\
												<td>\
													<div class="clearfix">\
														<label class="float-left mt3 try"><input type="radio" name="renewalType_' +id+ '" class="wp try-radio" value="1"><span class="lbl"> 自定义</span></label>\
														<div class="input-group float-left"  style="width: 60%;">\
															<input class="form-control datetimepicker"   type="text">\
															<span class="input-group-addon" >\
																<i class="fa fa-calendar"></i>\
															</span>\
														</div>\
													</div>\
												</td>\
											</tr>\
										</tbody>\
									</table>\
									<div class="text-right">\
										<button class="btn btn-sm btn-white ml10 cancel">取消</button>\
										<button class="btn btn-sm btn-blue ml10 ensure1">确定</button>\
									</div>\
								</div>';
            $(this).append(deleteLayer);
            //如果单位是试用,那么永久按钮就 不可用
            if(trUnit.usingNature.indexOf("试用")!=-1){
                $('.xqyj').prop('disabled',true);
                $('.try-radio').prop('checked',true);
            }else{
                $('.xqyj').prop('checked',true);
            }
        });
        //续期弹窗----第一次进入页面,把所有的系统续期弹窗 隐藏
        $(".modify-name-layer").hide();

        //续期弹窗----页面进来的时候 如果系统是试用,就不能点击正式 和 永久按钮
        <#if unitInfo??>
            <#if unitInfo.usingNature==0>
                $(".mt3 input[type=radio][value=0]").prop("disabled",true);//永久不能用
                $(".mt3 input[type=radio][value=1]").prop("checked","checked");//默认自定义选定
                $(".zs").prop("disabled",true);//正式不能选
            <#else>
                $(".mt3 input[type=radio][value=0]").prop("disabled",false);//永久不能用
                $(".mt3 input[type=radio][value=0]").prop("checked","checked");//默认永久选定
            </#if>

        </#if>
        //续期弹窗----续期确定按钮
        $('.ensure1').click(function () {
            var id=$(this).parents('tr').attr("data");
            var val= $("input[name=renewalType_"+id+"]:checked").val();
            var date=$(this).parents('.modify-name-layer').find('.datetimepicker').val();
            //如果选了自定义，则必须填时间
            if(val=='1' && date==''){
                opLayer.error("请输入时间","错误");
                return;
            }
            var url=_contextPath+"/operation/serverEx/manage/expire";
            var params;
            //0 永久  1 试用
            if(val=='0'){
                params={
                    id:id
                }
            }else{
                //试用续期 时间判定
                var unitExpireTime='${(unitInfo.expireTime)!''}';
                if(unitExpireTime!='' && new Date(date)>new Date(unitExpireTime) ){
                    opLayer.error("系统续期时间不能超过单位时间","错误");
                    return;
                }
                params={
                    id:id,
                    expireTime:date
                }

            }
            $.post(url,params,function(result){
                if(result.success==true){
                    $(".modify-name-layer").hide();
                    turnToAuthorizePage(trUnit.id);
                    opLayer.success(result.message,"提示");

                }else{
                    opLayer.error(result.message,"错误");
                }
            })
        });


        //全局----点击操作按钮时,隐藏页面上的其它弹窗,弹出当前窗口
        $(".stop,.detail,.more,.renewal").click(function(e){
            $(".modify-name-layer").hide();
            $(this).children(".modify-name-layer").show();
        });





        //数据展示--如果系统到期时间为空,就跟随单位时间
        $(".followUnit").each(function () {
            $(this).html(trUnit.expireTime);
            if(trUnit.expireTime.indexOf('剩')!=-1){
                $(this).addClass('color-red');
                $(this).parent("tr").children().eq(1).children('span').addClass('color-red');
            }
            if(trUnit.expireTime.indexOf('已')!=-1){
                $(this).addClass('color-ccc');
                $(this).parent("tr").children().eq(1).children('span').addClass('color-ccc');
            }
        })

        //未授权页面----未授权子系统搜索框
        $('#sysSearch').bind('input propertychange',searchSys);
        $('#sysSearch').bind('keypress',function(event){
            if(event.keyCode == 13) {searchSys}
        });

    })
    //未授权页面----子系统搜索
    function searchSys() {
        var str=$('#sysSearch').val().trim()
        //全部隐藏
       $(".unAuthorizedSys").hide();
        $.each(unAthorizedMap,function(id,name){//能和搜索的匹配则显示
            if(name.indexOf(str)!=-1){
                $('#unAuthorizedSys_'+id.substring(3,id.length)).show();
            }
        })
    }

    //授权页面----停用 使用转正式 恢复使用 等操作
    function doChange(id, param, layerName) {
        var url = _contextPath + "/operation/serverEx/manage/" + id + param;
        layer.open({
            type: 1,
            shadow: 0.5,
            title: false,
            area: '350px',
            btn: ['确认', '取消'],
            content: $('.' + layerName),
            yes: function (index, layero) {
                $.post(url, function (result) {
                    if (result.success) {
                        opLayer.success(result.message, "提示");
                        turnToAuthorizePage(trUnit.id);
                    } else {
                        opLayer.error(result.message, "错误");
                    }
                });
                layer.close(index);
            }
        });

    }

    //点击试用按钮 不能选择永久时间(试用时间不能为永久)
    function controllerRadio1() {
        var systemId = $(this).data('id');
        $('#zdy_' + systemId).prop('checked', true);
        $('#yj_' + systemId).prop('disabled', true);
    }

    //正式按钮函数
    function controllerRadio2() {
        var params = $(".box-default").data("params");
        //todo
        //if(params.expireTimeType==0){
        var systemId = $(this).data('id');
        $('#yj_' + systemId).prop('disabled', false);
        $('#yj_' + systemId).prop('checked', true);
        // }
    }


    //系统授权 确定 按钮
    function saveInsert() {
        var flag = true;
        var unitTime='${(unitInfo.expireTime)!''}';
        var systems = new Array();
        var expireTimeType=trUnit.expireTime.trim()=="永久"?0:1;
        $('#a2 table tr').each(function () {   // 遍历 tr
            var aParams = {};
            var radioVal = $(this).children('td:eq(1)').find("input[type=radio]:checked").val();
            if (radioVal == -1) {
                return true;
            }
            var radioVal2 = $(this).children('td:eq(2)').find("input[type=radio]:checked").val();
            aParams.usingState = 1;//默认使用状态为正常
            aParams.serverCode = $(this).attr("data");//关联系统id
            var system_id = $(this).attr("data");
            //设置系统到期时间
            if (radioVal2 == 0) {//永久
                aParams.expireTime = null;
            } else if (radioVal2 == 1) {//自定义
                if ($("#expireTime_" + system_id).val() == null || $("#expireTime_" + system_id).val() == "") {
                    if(radioVal=='1'){
                        flag = false;
                        opLayer.warn("请填写系统的自定义时间", "警告");
                        return false;
                    }
                    /*if (expireTimeType == 1) {
                        flag = false;
                        opLayer.warn("请填写系统的自定义时间", "警告");
                        return false;
                    } else if (expireTimeType == 0) {
                        //系统过期时间为永久的话
                        aParams.expireTime=unitTime;
                    }*/
                    //时间框为空,跟随单位时间

                    aParams.expireTime = null;
                } else {//系统设置了时间，如果单位为永久单位，则系统时间就为设置时间；如果单位为自定义时间，则需要判断系统时间不能超过单位时间
                    if (expireTimeType == 0) {
                        aParams.expireTime = $("#expireTime_" + system_id).val();
                    } else {
                        var serverTime = $("#expireTime_" + system_id).val();
                        var unit = new Date(unitTime);
                        var server = new Date(serverTime);
                        if (unit < server) {
                            flag = false;
                            opLayer.warn("系统时间不能超过单位时间", "警告");
                            console.log(123);
                            return false;
                        } else {
                            aParams.expireTime = $("#expireTime_" + system_id).val();
                        }
                    }
                }
            }
            aParams.unitId = trUnit.id;
            //参数 使用性质 试用还是正式
            if (radioVal == 0) {
                aParams.usingNature = 0;//试用
                systems.push(aParams);
            } else if (radioVal == 1) {
                aParams.usingNature = 1;//正式
                systems.push(aParams);
            }

        });
        if(flag && systems.length==0){
            opLayer.warn("您未选择任何子系统!","提示");
            return;
        }
        if (flag) {
            var url = _contextPath + '/operation/serverEx/manage/saveAuthoringSystem';
            $.post(url, {systems: JSON.stringify(systems)}, function (result) {
                if (result.success) {
                    $(".box-default").removeData("params");
                    opLayer.success("授权成功", "提示");
                    turnToAuthorizePage(trUnit.id);
                } else {
                    opLayer.error("抱歉，授权失败", "提示");
                }
            })
        }


    }



    //点击试用按钮 不能选择永久时间(试用时间不能为永久)
    function controllerRadio1() {
        var systemId = $(this).data('id');
        $('#zdy_' + systemId).prop('checked', true);
        $('#yj_' + systemId).prop('disabled', true);
        $("#timeDiv_" + systemId).removeClass("hide")
    }

    //正式按钮函数
    function controllerRadio2() {
        if (trUnit.expireTime.trim() == "永久") {
            var systemId = $(this).data('id');
            $('#yj_' + systemId).prop('disabled', false);
            $('#yj_' + systemId).prop('checked', true);
            $("#timeDiv_" + systemId).addClass("hide")
        }
    }

    function hidedate() {
        var system_id = $(this).data("id");
        $("#timeDiv_" + system_id).addClass("hide")
    }

    function showdate() {
        var system_id = $(this).data("id");
        $("#timeDiv_" + system_id).removeClass("hide")
    }

    //时间组件js
    $('.datetimepicker').datepicker({
        language: 'zh-CN',
        autoclose: true,
        todayHighlight: true,
        format: 'yyyy/mm/dd',
        startDate: new Date()
    }).next().on('click', function () {    //点击图标显示日历
        $(this).prev().focus();
    });
</script>