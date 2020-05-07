<#assign ORDER_SELF=1 />
<#assign ORDER_OPEN=2 />
<#assign ORDER_UNIT_OPEN=3 />
<#assign ORDER_UNIT_ORDER=4 />
<#assign ORDER_UNIT_ORDER_USER_AUTHORIZATION=5 />
<#assign USER_AUTHORIZATION=6 />
<input type="hidden" id="type" value="${type!}">
<div class="box box-default scroll-height">
    <div class="form-horizontal" id="myForm-two">
        <div class="form-group">
            <label class="col-sm-1 control-label text-left padding-left-25">类型&nbsp;：</label>
            <div class="col-sm-8 order_types">
                    <#if orderTypes?? && orderTypes?size gt 0>
                        <#list orderTypes as ot>
                        <label class="choice">
                            <input type="radio" value="${ot.orderType!}" class="wp" name="class-radio">
                            <span class="choice-name"> ${ot.orderName!}</span>
                        </label>
                        </#list>
                    </#if>
            </div>
            <div class="col-sm-3 text-right">
                <button type="button" class="btn btn-blue js-layer-power">批量授权</button>&nbsp;&nbsp;
                <button type="button" class="btn btn-default js-remove-all">批量删除</button>
            </div>
        </div>
    </div>

    <div class="table-container">
        <div class="table-container-body subscription">
            <table class="tables">
                <thead>
                <tr>
                    <th>
                        <label class="choice choice-all">
                            <input type="checkbox" class="wp">
                            <span class="choice-name">全选</span>
                        </label>
                    </th>
                    <th class="text-left">${columnName!}</th>
                    <th>用户</th>
                    <th>下属单位</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                    <#if models?? && models?size gt 0>
                        <#list models as ct>
                            <#assign disableDelete=!ct.delete && (ct.orderType=ORDER_SELF||ct.orderType=ORDER_OPEN||ct.orderType==ORDER_UNIT_OPEN||ct.orderType==ORDER_UNIT_ORDER)/>
                            <tr class="tr_order_type_${ct.orderType!}">
                                <td class="">
                                    <label class="choice">
                                        <input unitAuthorization="${ct.unitAuthorization?string}"
                                               userAuthorization="${ct.userAuthorization?string}"
                                               delete="${ct.delete?string}"
                                               chart-name="${ct.name!}"
                                               value="${ct.id!}" type="checkbox"
                                               <#if disableDelete>disabled</#if> class="wp">
                                        <span class="choice-name"></span>
                                    </label>
                                </td>
                                <td class="">${ct.name!}</td>
                                <!--无需给人授权-->
                                <td class="unit-self"><p>${ct.orderUsersName}</p></td>
                                <!-- 单位 -->
                                <td class="unit-other"><p>${ct.orderUnitsName}</p></td>
                                <td class="">
                                    <#if ct.orderType gt 3>
                                        <a href="javascript:void(0);" class="unfold">编辑</a><span class="tables-line">|</span>
                                        <#if ct.userAuthorization ||  ct.unitAuthorization>
                                            <a href="javascript:void(0);" class="single-authorization"
                                               unitAuthorization="${ct.unitAuthorization?string}" id="${ct.id!}">修改授权</a><span class="tables-line">|</span>
                                        </#if>
                                    </#if>
                                    <#if !disableDelete>
                                        <a href="javascript:void(0);"  id="${ct.id!}" class="remove" delete="${ct.delete?string}">删除</a>
                                    </#if>
                                </td>
                            </tr>
                        </#list>
                    </#if>
                </tbody>
            </table>
        </div>
    </div>
</div>
<div class="layer layer-power">
</div>
<script>
    $(function () {
        //表格
        $('.unfold').each(function () {
            var $unitSelf = $(this).parent().siblings('.unit-self').find('p').text();
            var $unitOther = $(this).parent().siblings('.unit-other').find('p').text();
            var $father = $(this).parent().parent();
            var str = '<tr class="none"><td class="text-right" colspan="1">用户：</td><td colspan="4"><a>' + $unitSelf + '</a></td></tr><tr class="none"><td class="text-right" colspan="1">下属单位：</td><td colspan="4"><a>' + $unitOther + '</a></td></tr>';
            $father.after(str);
        });
        // 展开收起
        $('.unfold').click(function () {
            var $father = $(this).parent().parent();
            var $text = $(this).text();
            $father.next().toggle();
            $father.next().next().toggle();
            if ($text == '展开') {
                $(this).text('收起');
                $(this).css('color', '#000')
            } else {
                $(this).text('展开');
                $(this).css('color', '#00CCE3')
            }
        });

        //单个修改授权
        $('.single-authorization').on('click', function () {
            var chartId = $(this).attr('id');
            var chartIds = [chartId];
            //当前的类型信息
            var orderType = $('.order_types input[type=radio].wp:checked').val();
            if (orderType == '6') {
                openUserTree(chartIds, orderType);
            } else {
                openUnitTree(chartIds, orderType);
            }
        });

        //批量授权
        $('.js-layer-power').on('click', function (e) {
            e.preventDefault();
            //检查是否选择数据
            if ($('.tables tbody input[type=checkbox].wp:checked').length == 0) {
                layer.msg("请选择需要授权的数据");
            } else {
                var chartIdArray = [];
                var containCurrentUnit = false;
                var notUnit = false;
                $('.tables tbody input[type=checkbox].wp:checked').each(function () {
                    chartIdArray.push($(this).val());
                    if ($(this).attr("unitAuthorization") == 'true') {
                        containCurrentUnit = true;
                    }
                    if ($(this).attr("unitAuthorization") == 'false') {
                        notUnit = true;
                    }
                });

                if (notUnit && containCurrentUnit) {
                    layer.msg("上级单位创建的和本单位创建的无法同时授权");
                    return;
                }

                //当前的类型信息
                var orderType = $('.order_types input[type=radio].wp:checked').val();
                if (orderType == '6') {
                    openUserTree(chartIdArray, orderType);
                } else {
                    openUnitTree(chartIdArray, orderType);
                }
            }
        });

        function openUnitTree(chartIdArray, orderType) {
            $.ajax({
                url: '${request.contextPath}/bigdata/subcribe/unit',
                type: 'GET',
                data: {
                    chartId: chartIdArray[0]
                },
                success: function (response) {
                    $.ajax({
                        url: '${request.contextPath}/bigdata/common/tree/unitTreeIndex',
                        type: 'POST',
                        data: {units:response.data},
                        dataType: 'html',
                        beforeSend: function(){
                            $('.layer-power').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
                        },
                        success: function (response) {
                            $('.layer-power').html(response);
                        }
                    });
                }
            });

            var index = layer.open({
                type: 1,
                title: '用户选择',
                shade: .5,
                shadeClose: true,
                closeBtn: 1,
                btn :['确定','取消'],
                area: ['70%','550px'],
                yes:function(index, layero){
                    var unitArray = [];
                    zTreeSelectedUnitIdMap.forEach(function (value, key, map) {
                        unitArray.push(key);
                    });
                    authorization(chartIdArray, unitArray, '', orderType, index);
                },
                content: $('.layer-power')
            })
        }

        function openUserTree(chartIdArray, orderType) {
            $.ajax({
                url: '${request.contextPath}/bigdata/subcribe/user',
                type: 'GET',
                data: {
                    orderType: orderType,
                    chartId: chartIdArray[0]
                },
                success: function (response) {
                    $.ajax({
                        url: '${request.contextPath}/bigdata/common/tree/userTreeIndex',
                        type: 'POST',
                        data: {users:response.data},
                        dataType: 'html',
                        beforeSend: function(){
                            $('.layer-power').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
                        },
                        success: function (response) {
                            $('.layer-power').html(response);
                        }
                    });
                }
            });

            var index = layer.open({
                type: 1,
                title: '用户选择',
                shade: .5,
                shadeClose: true,
                closeBtn: 1,
                btn :['确定','取消'],
                area: ['70%','550px'],
                yes:function(index, layero){
                    var teacherArray = [];
                    zTreeSelectedUserIdMap.forEach(function (value, key, map) {
                        teacherArray.push(key);
                    });
                    authorization(chartIdArray, '', teacherArray, orderType, index);
                },
                content: $('.layer-power')
            })
        }

        //选择不同的类型，过滤显示
        $('div.order_types').on('click', 'input', function () {
            var orderType = $(this).val();
            $('.unfold').text('展开');
            $('.unfold').css('color', '#00CCE3');
            $('tbody').find('tr').each(function () {
                var $this = $(this);
                if ($this.hasClass('tr_order_type_' + orderType)) {
                    $this.show();
                } else {
                    $this.hide();
                }
                //当授权类型为4、5 才有授权的概念
                if (orderType == 4) {
                    $('.js-layer-power').show();
                    $('.table-striped tr.tr_order_type_5').find('span').each(function () {
                        if ($(this).text() == '收起') {
                            $(this).click();
                        }
                    })
                } else if (orderType == 5) {
                    $('.js-layer-power').show();
                    $('.table-striped tr.tr_order_type_4').find('span').each(function () {
                        if ($(this).text() == '收起') {
                            $(this).click();
                        }
                    })
                } else if (orderType == 6) {
                    $('.js-layer-power').show();
                    $('.table-striped tr.tr_order_type_4').find('span').each(function () {
                        if ($(this).text() == '收起') {
                            $(this).click();
                        }
                    })
                } else {
                    $('.js-layer-power').hide();
                }
            });
            $('.choice-all').find('input[type=checkbox].wp').attr('checked', false);
            $('.tables tbody input[type=checkbox].wp').attr('checked', false);
            $('.tables tr.tr_order_type_' + orderType + ' input[type=checkbox].wp').attr('checked', false);
        });

        $('.order_types input[value="'+ ${orderType?default('6')} +'"].wp').trigger('click');

        //选中删除、批量删除
        $('.tables thead .choice-name').click(function () {
            //当前的类型信息
            var orderType = $('.order_types input[type=radio].wp:checked').val();
            if ($('.tables thead input[type=checkbox].wp').is(':checked')) {
                $('.tables tr.tr_order_type_' + orderType + ' input[type=checkbox].wp').attr('checked', false);
            } else {
                $('.tables tr.tr_order_type_' + orderType + ' input[type=checkbox]:not(:disabled).wp').each(function (i, v) {
                    if (!$(v).is(':checked')) {
                        $(v).trigger('click');
                    }
                });
            }
        });

        //单个删除
        $('.remove').click(function () {
            var $this = $(this);
            if ($this.attr('delete') == 'false') {
                layer.msg('上级单位创建的不能删除！', {icon: 4, time: 1500});
                return;
            }
            var $parent = $this.parent().parent();
            layer.confirm('确认删除?', {icon: 3, title: '提示'}, function (index) {
                var chartIds = [];
                chartIds.push($this.attr('id'));
                deleteChart(chartIds, function () {
                    var $next1 = $parent.next('.none');
                    var $next2 = $next1.next('.none');
                    $parent.remove();
                    $next1.remove();
                    $next2.remove();
                });
                layer.close(index);
            });
        });
        //批量删除
        $('.js-remove-all').click(function () {
            var canDelete = true;
            var illegalName = '';
            if ($('.tables tbody input[type=checkbox].wp:checked').length == 0) {
                layer.msg("请选择需要删除的数据");
            } else {
                var chartIds = [];
                var deleteDoms = [];
                var notDel = [];
                $('.tables tbody input[type=checkbox].wp:checked').each(function () {
                    if ($(this).attr('delete') == 'false') {
                        $(this).click();
                        notDel.push($(this).attr('chart-name'));
                        canDelete = false;
                        illegalName = $(this).attr('chart-name');
                        return;
                    } else {
                        chartIds.push($(this).val());
                        var $parent = $(this).parent().parent().parent();
                        deleteDoms.push($parent);
                        var $next1 = $parent.next('.none');
                        deleteDoms.push($next1);
                        deleteDoms.push($next1.next('.none'));
                    }
                });
                if (!canDelete) {
                    layer.msg(illegalName + '是上级单位创建的不能删除！', {icon: 4, time: 1500});
                    return;
                }

                layer.confirm('是否删除?', {icon: 3, title: '提示'}, function () {
                    deleteChart(chartIds, function () {
                        for (var i in deleteDoms) {
                            deleteDoms[i].remove();
                        }
                    });
                });
            }
        });

        function deleteChart(modelIds, success) {
            $.ajax({
                url: _contextPath + '/bigdata/subcribe/batchDelete',
                type: 'POST',
                data: {
                    type: $('#type').val(),
                    modelIds: modelIds
                },
                dataType: 'json',
                success: function (response) {
                    if (response.success) {
                        showLayerTips('success', '删除成功', 't', function () {
                            success();
                        });
                    } else {
                        showLayerTips4Confirm('error', '删除失败', 't', null);
                    }
                },
                error: function (jqXHR, status, errorThrown) {
                    showLayerTips4Confirm('error', '删除失败', 't', null);
                }
            });
        };

        /**
         * 授权
         */
        function authorization (chartIds, orderUnits, orderUsers, orderType, index) {
            $.ajax({
                url: _contextPath + '/bigdata/subcribe/authorization',
                type: 'POST',
                dataType: 'json',
                data: {
                    type: $('#type').val(),
                    chartIds: chartIds,
                    orderUnits: orderUnits,
                    orderUsers: orderUsers,
                    orderType: orderType
                },
                success: function (response) {
                    if (response.success) {
                        showLayerTips('success', '授权成功', 't', function () {
                            $('.page-content').load('/bigdata/subcribe/batchOperate?type=' + $('#type').val() + '&orderType=' + orderType);
                            layer.close(index);
                        });
                    } else {
                        showLayerTips4Confirm('error', '授权失败', 't', null);
                    }
                },
                error: function (jqXHR, status, errorThrown) {
                    showLayerTips4Confirm('error', '授权失败', 't', null);
                }
            });
        };
    });
</script>