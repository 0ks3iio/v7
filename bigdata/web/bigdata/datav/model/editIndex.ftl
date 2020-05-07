<div class="box-header">
    <div class="btn-group">
        <button class="btn btn-blue js-db" onclick="showScreen(this);">全部大屏</button>
        <button class="btn btn-default js-api" onclick="showLibrary(this);">组件</button>

    </div>
    <div class="" style="right: 0px; top: 0px;float: right;" id="screen-btn">
        <button type="button" onclick="editCockpit('', '大屏设置', true);" class="btn btn-blue">新建大屏</button>
        <button type="button" class="btn btn-default js-subscription">批量操作</button>
        <#if bigdataSuperUser==true>
            <button type="button" onclick="goToClassicCase();" class="btn btn-default">经典案例</button>
        </#if>
    </div>
<#--    <div class="clearfix ">-->
<#--        <span class="fake-btn fake-btn-blue no-radius-right no-border-right js-db" onclick="showScreen(this);">大屏</span>-->
<#--        <span class="fake-btn fake-btn-default no-radius-left no-border-left js-api" onclick="showLibrary(this);">组件</span>-->

<#--    </div>-->
</div>

<!-- 大屏列表 -->
<div class="box data-page">
    <div class="box-body no-padding-side" id="screen-list">
        <div class="card-list clearfix report" id="cockpit-list">
        <#if cockpits?exists && cockpits?size gt 0>
            <#list cockpits as cockpit>
            <div class="card-item" id="${cockpit.id!}">
                <div class="bd-view">

                    <a href="javascript:void(0);">
                        <div class="drive  no-padding">
                            <img src="${fileUrl!}/bigdata/datav/${cockpit.unitId!}/${cockpit.id!}.jpg?${.now}" alt="">
                            <div class="coverage">
                                <div class="vetically-center">
                                    <#if currentUnitId?default('')==cockpit.unitId?default('-1')>
                                    <img src="${request.contextPath}/static/bigdata/images/remove.png"
                                         onclick="deleteCockpit('${cockpit.id!}');" width="20" height="20"
                                         class="remove"/>
                                    <img src="${request.contextPath}/static/bigdata/images/copy.png"
                                         onclick="copy('${cockpit.id!}');" width="20" height="20"
                                         class="remove"/>
                                    </#if>
                                    <#if cockpit.url?default('') == ''>
                                    <button class="btn btn-blue btn-long" type="button"
                                            onclick="editCockpit('${cockpit.id!}', '${cockpit.name!}', <#if currentUnitId?default('')==cockpit.unitId?default('-1')>true</#if>);">
                                        编辑
                                    </button>
                                    <#else>
                                        <button class="btn btn-blue btn-long" type="button"
                                                onclick="viewCockpit('${cockpit.id!}', '${cockpit.name!}');">
                                            查看
                                        </button>
                                    </#if>
                                </div>
                            </div>
                        </div>
                        <div class="padding-side-20">
                            <h3 style="overflow: hidden;">${cockpit.name!}</h3>
                            <div class="js-power row">
                                <div class="col-sm-4 no-padding">授权类型：</div>
                                <div class="col-sm-8 no-padding">
                                    <div class="filter"
                                         edit="<#if currentUnitId?default('')==cockpit.unitId?default('-1')>true<#else>false</#if>"
                                         cockpit-id="${cockpit.id!}" orderType="${cockpit.orderType?default(2)}"
                                         userId="${cockpit.createUserId?default("")}">
                                        <span>
                                        <#if cockpit.orderType?default(2) == 2>
                                            公开
                                        <#elseif cockpit.orderType?default(2) == 1>
                                            私有
                                        <#elseif cockpit.orderType?default(2) == 3>
                                            本单位公开
                                        <#elseif cockpit.orderType?default(2) == 4>
                                            单位授权
                                        <#elseif cockpit.orderType?default(2) == 5>
                                            单位授权个人需授权
                                        <#elseif cockpit.orderType?default(2) == 6>
                                            个人授权
                                        </#if>
                                        </span>
                                        <i class="fa fa-pencil"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </a>
                </div>
            </div>
            </#list>
        <#else>
            <p style="margin-left: 10px">
                列表空空，去新建吧～
            </p>
        </#if>
        </div>
    </div>
    <div class="box-body no-padding-side hide" id="library-list">

    </div>
</div>
<script>
    
    function goToClassicCase() {
        router.go({
            path: '/bigdata/cockpit/classic',
            name: '经典案例',
            level: 2
        }, function () {
            var url = '${request.contextPath}/bigdata/cockpit/classic';
            $('.page-content').load(url);
        });
    }

    function showScreen(dom) {
        $('#screen-list').removeClass('hide');
        $('#library-list').addClass('hide');
        $('#screen-btn').show();
        $(dom).addClass('btn-blue').removeClass('btn-default').siblings('.btn').removeClass('btn-blue').addClass('btn-default');

    }

    function showLibrary(dom) {
        $('#library-list').removeClass('hide');
        $('#screen-list').addClass('hide');
        $('#screen-btn').hide();
        $(dom).addClass('btn-blue').removeClass('btn-default').siblings('.btn').removeClass('btn-blue').addClass('btn-default');
    }

    $(function () {

        $('#library-list').load('${request.contextPath}/bigdata/diagramLibrary/collect/');

        $('.js-power .filter').click(function aa() {

            if ($(this).attr('edit') == 'false') {

                return;
            }
            var orderType = $(this).attr("orderType");
            let createUserId = $(this).attr('userId');
            var $input = '<div class="filter-item no-margin">' +
                    '<div class="filter-content">' +
                    '<select name="" id="js-select" onchange="changeOrderType(this);" class="js-select selected form-control" placeholder="请输入模板名称">' +
                    <#if orderTypes?? && orderTypes?size gt 0>
                        <#list orderTypes as ot>
                                '<option value="${ot.orderType}">${ot.orderName!}</option>' +
                        </#list>
                    </#if>
                    '</select></div></div>';
            var $father = $(this);
            let jQueryInput = $($input);
            if (createUserId != "${userId}" && orderType != "1") {
                jQueryInput.find('option[value="1"]').remove();
            }
            $(this).html(jQueryInput.html());
            $(this).find('option[value="' + orderType + '"]').attr("selected", true);
            $('#js-select').click(function () {
                return false
            });	//阻止表单默认点击行为
            $('#js-select').trigger('focus');
            // $('.js-select option:eq(0)').attr('selected','selected');
            $('.js-select').blur(function () {
                $father.attr('ordertype', $('.js-select option:selected').val());
                var $newText = $('.js-select option:selected').text() + '&nbsp;&nbsp;<i class="fa fa-pencil"></i>';
                $father.html($newText)
            })

        });
        //批量操作
        $('.js-subscription').on('click', function () {
            // openModel('70812113', '大屏设置', 1, _contextPath + '/bigdata/datav/screen/order/batch', '大数据管理', '数据分析', null, false);
            router.go({
                path: '/bigdata/subcribe/batchOperate?type=screen',
                name: '批量操作',
                level: 2
            }, function () {
                $('.page-content').load(_contextPath + '/bigdata/subcribe/batchOperate?type=screen');
            })
        });

        var $dh = ($('.drive').first().width() / 4 * 3).toFixed(0);
        $('.drive').each(function (index, ele) {
            $(this).children('img').first().css({
                'height': $dh
            })
        });
    });

    function changeOrderType(el) {
        console.log($(el).parent().parent().parent())
        var orderType = $(el).val();
        var id = $(el).parent().parent().attr("cockpit-id");
        $.ajax({
            url: '${request.contextPath}/bigdata/datav/screen/order_type/' + id + '/' + orderType,
            type: 'GET',
            dataType: 'json',
            success: function (val) {
                if (!val.success) {
                    // layer.msg(val.message, {icon: 2, time: 2000});
                    showLayerTips ('error', val.message, 't');
                }
                //
            }
        });
    }

    function viewCockpit(cockpitId, name) {
        window.open('${request.contextPath}/bigdata/datav/screen//view/' + cockpitId, name);
    }

    function editCockpit(cockpitId, name, edit) {
        if (edit) {
            if (cockpitId === '') {
                window.open('${request.contextPath}/bigdata/cockpit/template/');
            <#--layer.prompt({title: '请输入大屏名称', formType: 3, maxlength: 50}, function (cockpitName, index) {-->
            <#--layer.close(index);-->
            <#--$.ajax({-->
            <#--url: '${request.contextPath}/bigdata/datav/screen/',-->
            <#--data: {-->
            <#--name: cockpitName-->
            <#--},-->
            <#--type: 'POST',-->
            <#--dataType: 'json',-->
            <#--async:false,-->
            <#--success: function (data) {-->
            <#--if (data.success) {-->
            <#--window.open('${request.contextPath}/bigdata/datav/screen/' + data.data, cockpitName);-->
            <#--&lt;#&ndash;$('#cockpit-list').parent().load('${request.contextPath}/bigdata/cockpit/index');&ndash;&gt;-->
            <#--}-->
            <#--else {-->
            <#--layer.msg("驾驶舱创建失败", {icon:3, time:1000});-->
            <#--}-->
            <#--}-->
            <#--});-->
            <#--});-->
                //window.open('${request.contextPath}/bigdata/dashboard/create', name);
                return;
            }
            window.open('${request.contextPath}/bigdata/datav/screen/' + cockpitId, name);
        } else {
            // layer.msg('该大屏是由上级单位创建的，您不能编辑！', {icon: 4, time: 1500});
            showLayerTips ('warn', '该大屏是由上级单位创建的，您不能编辑！', 't');
        }

        //延迟刷新列表
        setTimeout(function () {

        }, 1000 * 30);
    }

    function deleteCockpit(cockpitId) {
        layer.confirm("确认删除该大屏？", {btn: ['确定', '取消'], title: '确认信息', icon: 3, closeBtn: 0}, function (index) {
            layer.close(index);
            let ids = [];
            ids.push(cockpitId);
            $.ajax({
                url: '${request.contextPath}/bigdata/datav/screen/delete',
                type: 'POST',
                data: {
                    ids: ids
                },
                dataType: 'json',
                success: function (data) {
                    if (!data.success) {
                        showLayerTips ('error', data.message, 't');
                    } else {
                        $('#' + cockpitId).remove();
                        showLayerTips ('success', data.message, 't');
                    }
                }
            })
        })
    }

    function copy(id) {
        layer.prompt({title: '请输入新的大屏名称', formType: 3, maxlength: 50}, function (name, index) {
            layer.close(index);
            $.ajax({
                url: '${request.contextPath}/bigdata/cockpit/clone',
                type: 'POST',
                data: {
                    name: name,
                    id: id
                },
                dataType: 'json',
                success: function (res) {
                    if (res.success) {

                        //openModel('70803311111', '大屏设置', 1, _contextPath + '/bigdata/cockpit/index', '大数据管理', '数据分析', null, false);
                        router.reload({
                            path:'',
                            name: '数据大屏设计',
                            level: 1
                        });
                    } else {
                        // layer.msg(res.message, {icon: 2});
                        showLayerTips ('success', res.message, 't');
                    }
                },
                error: function () {
                    showLayerTips ('error', '网络异常', 't');
                }
            });
        });
    }

</script>