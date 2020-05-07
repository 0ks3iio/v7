<#import "../macro/pagination.ftl" as pagination />
<#if pages??>
    <#if pages.content?? && pages.content?size gt 0>
        <table class="table table-bordered table-striped table-hover no-margin">
            <thead>
            <tr>
                <th>序号</th>
                <th>单位名称</th>
                <th>单位编号</th>
                <th>行政区</th>
                <th>单位类型</th>
                <th>单位性质</th>
                <th>单位到期时间</th>
                <th>系统到期统计</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody id="unitAccounts">
            <#list pages.content as unitAccount>
                <tr data=${unitAccount.id}>
                    <td>${(pages.number)*pages.size + (unitAccount_index + 1)}</td>
                    <td id="unitFont_${unitAccount.id}">${unitAccount.unitName}</td>
                    <td>${unitAccount.unionCode!}</td>
                    <td>${unitAccount.regionName!}</td>
                    <td><#if unitAccount.unitType??>
                            <#if unitAccount.unitType==1>顶级教育局
                            <#elseif unitAccount.unitType==2>下属教育局
                            <#elseif unitAccount.unitType==3>托管中小学
                            <#elseif unitAccount.unitType==4>托管大中专学校
                            <#elseif unitAccount.unitType==5>托管幼儿园
                            <#elseif unitAccount.unitType==6>EISS中小学
                            <#elseif unitAccount.unitType==7>EISV大中专学校
                            <#elseif unitAccount.unitType==8>非教育局单位
                            <#elseif unitAccount.unitType==9>高职校
                            </#if>
                        <#else >${unitAccount.unitType!}
                        </#if>
                    </td>
                    <td <#if unitAccount.usingState??&&unitAccount.usingState==1>class="color-ccc"
                           <#elseif unitAccount.expireTime??&&unitAccount.expireDay<0>class="color-ccc"
                        <#elseif unitAccount.expireTime??&&unitAccount.expireDay<=30>class="color-red"
                            </#if>
                    >
                        <#if unitAccount.usingNature??>
                            <#if unitAccount.usingNature==1>正式
                            <#elseif unitAccount.usingNature==0>试用
                            </#if>
                        <#else >正式
                        </#if>
                        <span><#if unitAccount.usingState??&&unitAccount.usingState==1><a class="badge badge-yellow ml10">停用</a></#if></span>
                    </td>
                    <td <#if unitAccount.usingState??&&unitAccount.usingState==1>class="color-ccc"
                        <#elseif unitAccount.expireTime??&&unitAccount.expireDay<0>class="color-ccc"
                        <#elseif unitAccount.expireTime??&&unitAccount.expireDay<=30>class="color-red"
                        </#if>
                    >
                        <#if unitAccount.expireTime??>
                            <#if unitAccount.expireDay < 0>已到期
                            <#elseif unitAccount.expireDay <= 30>剩${unitAccount.expireDay+1}天到期
                                <#elseif 30 < unitAccount.expireDay>${unitAccount.expireTime?string('yyyy/MM/dd')}
                            </#if>
                        <#else >永久
                        </#if>
                    </td>
                    <td <#if unitAccount.usingState??&&unitAccount.usingState==1>class="color-ccc"
                        <#elseif unitAccount.expireTime??&&unitAccount.expireDay<0>class="color-ccc"
                        <#elseif unitAccount.expireTime??&&unitAccount.expireDay<=30>class="color-red"
                            </#if>
                    ><span>${unitAccount.count}</span><a class="color-blue ml10 detail pos-rel" href="#">
                            <i class="fa fa-angle-right"></i></a>
                    </td>
                    <td data1=${unitAccount.usingNature!}>
                            <#if unitAccount.usingState??&&unitAccount.usingState==1>
                                <a class="color-blue table-btn recoverUnit" href="javascript:void(0);">恢复使用</a>
                                <a data2="${unitAccount.usingState!}" class="color-blue table-btn more pos-rel" href="javascript:void(0);">更多</a>
                            <#elseif unitAccount.usingState??&&unitAccount.usingState==0>
                                <#if unitAccount.expireTime?? >
                                    <#if unitAccount.expireDay gte 0>
                                        <a class="color-blue table-btn authorize" href="javascript:void(0);" >授权</a>
                                    </#if>
                                <#else>
                                    <a class="color-blue table-btn authorize" href="javascript:void(0);" >授权</a>
                                </#if>


                                <#if unitAccount.expireTime??><a class="color-blue table-btn renewal pos-rel renewal1" href="javascript:void(0);">续期</a></#if>
                                <a data2="${unitAccount.usingState!}" class="color-blue table-btn more pos-rel" href="javascript:void(0);">更多</a>
                            <#else >
                                <a class="color-blue table-btn assert" href="javascript:void(0);">数据维护</a>
                                <a class="color-blue table-btn delete" href="javascript:void(0);">删除</a>
                            </#if>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
        <@pagination.paginataion pages=pages containerId='unitAccouts' pageCallFunction='doGetUnitAccountList' />
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
<!-- 删除确认框 -->
		<div class="layer layer-delete">
            <div class="layer-content">
                <table width="100%">
                    <tr>
                        <td class="text-right" valign="top">
                            <span class="fa fa-exclamation-circle color-yellow font-30 mr20"></span>
                        </td>
                        <td>
                            <div class="font-16 mb10">确定要删除此单位吗？</div>
                            <div class="color-grey">删除后与其相关的服务都会被删除!</div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>

<!-- 停用确认框 -->
		<div class="layer layer-stop">
            <div class="layer-content">
                <table width="100%">
                    <tr>
                        <td class="text-right" valign="top">
                            <span class="fa fa-exclamation-circle color-yellow font-30 mr20"></span>
                        </td>
                        <td>
                            <div class="font-16 mb10">确定要停用此单位吗？</div>
                            <div class="color-grey">停用后与其相关的服务都会被停用!</div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>


<script src="${springMacroRequestContext.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<script src="${springMacroRequestContext.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
<script src="${springMacroRequestContext.contextPath}/static/components/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js"></script>
<script src="${springMacroRequestContext.contextPath}/static/components/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>

<script>

    $(function(){
        $(document).click(function(e){
            if( $(e.target).hasClass("cancel") || (
                    !$(e.target).hasClass("stop") && !$(e.target).parent().hasClass("stop") &&
                    !$(e.target).parents().hasClass("stop") && !$(e.target).hasClass("detail") &&
                    !$(e.target).parent().hasClass("detail") && !$(e.target).parents().hasClass("detail") &&
                    !$(e.target).hasClass("more") && !$(e.target).parent().hasClass("more") &&
                    !$(e.target).parents().hasClass("more") && !$(e.target).hasClass("renewal") &&
                    !$(e.target).parent().hasClass("renewal") && !$(e.target).parents().hasClass("renewal"))){
                $('.permanent-radio').prop('checked',true);
                $('.datetimepicker1').val('');
                $(".modify-name-layer").hide();//点页面其他地方 隐藏表情区
            }

        });

        $('#unitFont_'+ztreeId).html($('#unitFont_'+ztreeId).html()+'<font  color="red">（本单位）</font>');

        /*
        更多操作
         */
        $(".more").each(function(){
                var deleteLayer;
                var usingState=$(this).attr('data2');
                var usingNature=$(this).parent().attr('data1');
                if (usingNature==1){
                    deleteLayer = '<div class="modify-name-layer" style="width: 100px;">\
									<ul class="mt20">\
										<li><a class="color-blue stop pos-rel" href="#">停用</a></li>\
										<li><a class="color-blue official" href="#">编辑</a></li>\
										<li><a class="color-blue openAccount" href="#">账号开通</a></li>\
										<li><a class="color-blue delete" href="javascript:void(0)">删除</a></li>\
									</ul>\
								</div>';
                }
                if (usingNature==0){
                    deleteLayer = '<div class="modify-name-layer" style="width: 100px;">\
									<ul class="mt20">\
									    <li><a class="color-blue official" href="#">试用转正式</a></li>\
										<li><a class="color-blue stop pos-rel" href="#">停用</a></li>\
										<li><a class="color-blue official" href="#">编辑</a></li>\
										<li><a class="color-blue openAccount" href="#">账号开通</a></li>\
										<li><a class="color-blue delete" href="javascript:void(0)">删除</a></li>\
									</ul>\
								</div>';
                }
                if (usingState==1){
                    deleteLayer = '<div class="modify-name-layer" style="width: 90px;">\
									<ul class="mt20">\
										<li><a class="color-blue delete" href="javascript:void(0)">删除</a></li>\
									</ul>\
								</div>';
                }
            $(this).append(deleteLayer);
        });

        /*
        子系统详情展示
         */
        $('.detail').click(function () {
            var t=$(this);
            id=$(this).parents('tr').attr("data");
            if(t.children('.modify-name-layer').length>0){
                t.children('.modify-name-layer').show();
                return;
            }
            var url=_contextPath + '/operation/serverEx/manage/' + id + '/authorize';
            $.ajaxSettings.async = false;
            $.get(url,function (result) {
                var count=0;
                var s=result.authorizeSystem;
                var unitDate;
                if (result.unitDate==null){
                    unitDate='永久';
                }else{unitDate=timestampToTime(result.unitDate);}
                var prefix = '<div class="modify-name-layer"><div class="mt20">'
                var sufix = '</div></div>';
                var nbody='<div class="no-data-container">\n' +
                        '            <div class="no-data">\n' +
                        '            <span class="no-data-img">\n' +
                        '                <img src="${springMacroRequestContext.contextPath}/static/images/public/nodata4.png" alt="">\n' +
                        '            </span>\n' +
                        '                <div class="no-data-body">\n' +
                        '                    <p class="no-data-txt">没有相关数据</p>\n' +
                        '                </div>\n' +
                        '            </div>\n' +
                        '        </div>'
                var body=[];
                if (typeof (s)=="undefined"||s.length==0) {
                    t.append(prefix+nbody+sufix);
                }else{
                    for(var i=0;i<s.length;i++){
                        if (s[i].usingNature==1){
                            s[i].usingNature = '正式';
                        } else {s[i].usingNature = '试用';}
                        var date = timestampToTime(s[i].expireTime);
                        var timestamp = new Date(new Date().toLocaleDateString()).getTime();
                        if (s[i].usingState!=1) {
                            if (s[i].expireTime>=timestamp) {
                                body += '<div>' + s[i].name + '（' + s[i].usingNature + '，' + date + '）' + '</div>'
                            }else if (s[i].expireTime == null && result.unitDate==null){
                                body += '<div>' + s[i].name + '（' + s[i].usingNature + '，' + unitDate + '）' + '</div>'
                            } else if (s[i].expireTime == null && result.unitDate>=timestamp) {
                                body += '<div>' + s[i].name + '（' + s[i].usingNature + '，' + unitDate + '）' + '</div>'
                            } else {
                                body += '<div class="color-red">' + s[i].name + '（' + s[i].usingNature + '，' + '已到期' + '）' + '</div>'
                            }
                        }else if (s[i].usingState==1) {
                            count++;
                            if (s.length==count){t.append(prefix+nbody+sufix);return;}
                        }
                    }
                    t.append(prefix+body+sufix);
                }
            })
            $.ajaxSettings.async = true;
        });

        /* 续期 */
        $(".renewal").each(function(){

            var obj=$(this);
            var id=$(this).parents('tr').attr("data");
            var url=_contextPath+"/operation/systemLog/renewalCount";
            $.ajaxSettings.async = false;
            $.get(url,{unitId:id},function(result){
                if(result.success){
                    ren(obj,result.renewalCount,id);
                }
            });
            $.ajaxSettings.async = true;
        });

        /* 试用 和 永久 的radio 标签选择转换*/
        $('.try').click(function () {
            $(this).parents('tbody').find('.try-radio').prop('checked',true);
            $(this).parent('tbody').find('.permanent-radio').prop('checked',false);
            $('.timepick').removeClass('hide');
        });
        $('.permanent').click(function () {
            $(this).parents('tbody').find('.try-radio').prop('checked',false);
            $(this).parents('tbody').find('.permanent-radio').prop('checked',true);
            $('.timepick').addClass('hide');
        });


        $('.ensure').click(function () {
            var id=$(this).parents('tr').attr("data");
            var val= $("input[name=renewalType_"+id+"]:checked").val();
            var time = $(this).parents('.modify-name-layer').find('.datetimepicker1').val();
            var url=_contextPath+"/operation/unit/manage/expire";
            var params;
            if(val=='0'){
                params={
                    unitId:id,
                    expireTimeType:0
                }
            }else{
                if (time == ''){
                    opLayer.error("请输入时间");
                    return;
                }
                params={
                    unitId:id,
                    expireTimeType:1,
                    expireTime:time
                }

            }
            $.post(url,params,function(result){
                if(result.success){
                    $(".modify-name-layer").hide();
                    f(ztreeId);
                    opLayer.success(result.message,"提示");
                }else{
                    opLayer.error(result.message,"错误");
                }
            })
        });

        $(".stop,.detail,.more,.renewal").click(function(e){

            e.preventDefault();
            $(".modify-name-layer").hide();
            $(this).children(".modify-name-layer").show();
        });

        //服务记录
        $(".operationRecord").click(function () {
            recordId=$(this).parents('tr').attr("data");
            $('#createUnit').load(_contextPath + '/operation/systemLog/page');
            $('#unitListContainer').addClass('hide');
            $('#createUnit').removeClass('hide');

        });

        $('.datetimepicker1').datepicker({
            language: 'zh-CN',
            autoclose: true,
            todayHighlight: true,
            format: 'yyyy/mm/dd'
        }).next().on('click', function(){    //点击图标显示日历
            $(this).prev().focus();
        });


        //单位恢复
        $(".recoverUnit").click(function () {
            var unitId=$(this).parents('tr').attr("data");
            $.ajax({
                url: _contextPath + '/operation/unit/manage/state/usingState/?unitId='+unitId+'&'+'usingState'+'='+0,
                type: 'GET',
                contentType: 'application/json',
                success: function (res) {
                    if (res.success) {
                        opLayer.success(res.message, "提示");
                        f(ztreeId);
                    } else {
                        opLayer.error(res.message, "提示");
                    }
                }
            })
        });

        //更多--->删除
        $('.delete').on('click', function(){
            var unitId=$(this).parents('tr').attr("data");
            console.log(unitId);
            layer.open({
                type: 1,
                shadow: 0.5,
                title:false,
                area: '350px',
                btn: ['确认', '取消'],
                content: $('.layer-delete'),
                yes: function(index, layero){
                    var url= _contextPath + '/operation/unit/manage/'+unitId;

                    $.post(url,function(result){
                        if (result.success) {
                            opLayer.success(result.message, "提示");
                            $('.page-content').load(_contextPath + "/operation/unit/manage/page/index");
                        } else {
                            opLayer.error(result.message, "提示");
                        }
                    })

                    layer.close(index);
                }
            });

        });
        //更多--->停用
        $('.stop').on('click', function(){
            var unitId=$(this).parents('tr').attr("data");
            layer.open({
                type: 1,
                shadow: 0.5,
                title:false,
                area: '350px',
                btn: ['确认', '取消'],
                content: $('.layer-stop'),
                yes: function(index, layero){
                    var url= _contextPath + '/operation/unit/manage/state/usingState';
                    var params={
                        unitId:unitId,
                        usingState:1
                    }
                    $.get(url,params,function(result){
                        if (result.success) {
                            opLayer.success(result.message, "提示");
                            f(ztreeId);
                        } else {
                            opLayer.error(result.message, "提示");
                        }
                    })
                    layer.close(index);
                }
            });
        });

        /* 授权*/
        $('.authorize').click(function () {
            var obj=$(this).parents('tr');
            //authorizeUnitId=obj.attr("data");

            trUnit.unitName=obj.children('td').eq(1).html();
            trUnit.id=obj.attr("data");
            //正式还是试用
            trUnit.usingNature=obj.children('td').eq(5).html();
            //到期时间
            trUnit.expireTime=obj.children('td').eq(6).html();

            turnToAuthorizePage(trUnit.id);
        });

        $(".mt20").on("click",'.official',turnOfficial);
        $(".assert").on("click",turnOfficial);

    })
    function turnAuthorize() {
        $('#unitListContainer').addClass('hide');
        $('#createUnit').removeClass('hide');
    }

    function turnToAuthorizePage(unitId) {

        $('#createUnit').load(_contextPath + '/operation/serverEx/manage/page/authorManage?unitId='+unitId);
        turnAuthorize();
    }
    function timestampToTime(timestamp) {
        if(timestamp==""||typeof (timestamp)=='undefined'||timestamp==null){
            return "";
        }else {
            var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
            Y = date.getFullYear() + '/';
            M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '/';
            D = date.getDate()<10?'0'+date.getDate():date.getDate();
            // h = date.getHours() + ':';
            // m = date.getMinutes() + ':';
            // s = date.getSeconds();
            return Y+M+D;
        }
    }

    function ren(obj,renewalCount,id){
        var deleteLayer ='<div class="modify-name-layer" style="width: 350px;">\
									<table width="100%" class="mt20 mb20">\
										<tbody>\
											<tr>\
												<td rowspan="2" valign="top" width="70">单位续期：</td>\
												<td>\
													<p class="mb10"><label class="float-left mt3 permanent" ><input type="radio"  name="renewalType_'+id+'" class="wp permanent-radio" checked value="0"><span class="lbl"> 永久</span></label></p>\
												</td>\
											</tr>\
											<tr>\
												<td>\
													<div class="clearfix">\
														<label class="float-left mt3 try"><input type="radio" name="renewalType_'+id+'"   class="wp try-radio" value="1"><span class="lbl"> 自定义</span></label>\
														<div class="input-group float-left hide timepick" style="width: 60%;">\
															<input class="form-control datetimepicker1" type="text">\
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
										<span class="color-red">已续期'+renewalCount+'次</span>\
										<button class="btn btn-sm btn-white ml10 cancel">取消</button>\
										<button class="btn btn-sm btn-blue ml10 ensure" >确定</button>\
									</div>\
								</div>';
        obj.append(deleteLayer);
    }
    //编辑 试用转正式 维护按钮跳转
    function turnOfficial() {
        var unitId=$(this).parents('tr').attr("data");
        var titleName=$(this).html();
        $('#unitListContainer').addClass('hide');
        $('#createUnit').removeClass('hide');
        if(titleName=='编辑'){
            $('#createUnit').load(_contextPath + '/operation/unit/manage/page/edit?flag=false&unitId='+unitId);
        }else if(titleName=='数据维护'){
            $('#createUnit').load(_contextPath + '/operation/unit/manage/page/maintain?flag=false&unitId='+unitId);
        }else if(titleName=='试用转正式'){
            $('#createUnit').load(_contextPath + '/operation/unit/manage/page/formal?flag=true&unitId='+unitId);
        }
    }

    /**
     * 账号开通页面跳转
     */
    $('.openAccount').on('click',function () {
        var unitId = $(this).parents('tr').attr("data");
        $('#createUnit').removeClass('hide');
        $('#unitListContainer').addClass('hide');
        $('#createUnit').load(_contextPath + "/operation/unit/manage/page/student-and-family/index?unitId="+unitId);
    });
</script>