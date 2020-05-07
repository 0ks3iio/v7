<div class="filter clearfix mb-20">
    <div class="filter-item block">
        <span class="filter-name">授权类型：</span>
        <select name="" id="orderTypeSelect"
                <#if dataModel.canEdit?string('yes', 'no') == 'yes'>disabled="disabled"</#if>
                onchange="changeOrderTypeSelect(this, '')" class="form-control chosen-select chosen-width"
                data-placeholder="未选择">
                                    <#if orderTypes?exists && orderTypes?size gt 0>
                                        <#list orderTypes as orderType>
                                        <label class="pos-rel">
                                            <option <#if orderType.orderType ==dataModel.orderType?default(2)>selected</#if>
                                                    value="${orderType.orderType!}">${orderType.orderName!}</option>
                                        </label>
                                        </#list>
                                    </#if>
        </select>
    </div>
</div>
<input type="hidden" id="modelId" value="${dataModel.id!}">
<input type="hidden" id="canEdit" value="${dataModel.canEdit?string('yes', 'no')}">
<div class="bs-callout bs-callout-danger clearfix order-main-div">
</div>
<input type="hidden" id="dataModelUnitId" value="${dataModel.unitId}">

<script>
    $(function () {
        changeOrderTypeSelect(null, '${dataModel.orderType!}');
    });

    function changeOrderTypeSelect(e, orderType) {
        if (orderType == null || orderType == '') {
            orderType = $(e).val();
        }
        $('.choose-item').find('.fa-times-circle').click();
        if (orderType == '6') {
            $('.order-main-div').show();
            $.ajax({
                url: _contextPath + '/bigdata/subcribe/user',
                type: 'GET',
                data: {
                    orderType: orderType,
                    chartId: $('#modelId').val()
                },
                success: function (response) {
                    $.ajax({
                        url: _contextPath + '/bigdata/common/tree/userTreeIndex',
                        type: 'POST',
                        data: {users: response.data, isChart:true},
                        dataType: 'html',
                        beforeSend: function () {
                            $('.order-main-div').html("<div class='pos-middle-center'><h4><img src='" + _contextPath + "/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
                        },
                        success: function (response) {
                            $('.order-main-div').html(response);
                            $('.model-order').removeClass('col-sm-8').removeClass('col-sm-offset-2');
                        }
                    });
                }
            });
        }

        //单位授权显示单位树
        if (orderType == '4') {
            $('.order-main-div').show();
            $.ajax({
                url: _contextPath + '/bigdata/subcribe/unit',
                type: 'GET',
                data: {
                    orderType: orderType,
                    chartId: $('#modelId').val()
                },
                success: function (response) {
                    $.ajax({
                        url: _contextPath + '/bigdata/common/tree/unitTreeIndex',
                        type: 'POST',
                        data: {units: response.data, isChart:true},
                        dataType: 'html',
                        beforeSend: function () {
                            $('.order-main-div').html("<div class='pos-middle-center'><h4><img src='" + _contextPath + "/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
                        },
                        success: function (response) {
                            $('.order-main-div').html(response);
                            $('.model-order').removeClass('col-sm-8').removeClass('col-sm-offset-2');
                            $('#cascade_span').text('级联');
                        }
                    });
                }
            });
        }

        if (orderType < 4) {
            $('.order-main-div').hide();
        }
    }

    function saveModelAuth() {
        if ($('#canEdit').val() == 'no') {
            if ($('#currentUnitId').val() != $('#dataModelUnitId').val()) {
                layer.msg('该数据模型是由上级单位创建的，您不能编辑！', {icon: 4, time: 1500});
                return;
            }
        }
        var data = {};
        var orderType = $('#orderTypeSelect').val();
        data.modelId = $('#modelId').val();
        data.orderType = orderType;
        //授权单位 和 人
        var unitArray = [];
        if (orderType == '4') {
            zTreeSelectedUnitIdMap.forEach(function (value, key, map) {
                unitArray.push(key);
            });
        }

        var teacherArray = [];
        if (orderType == '6') {
            zTreeSelectedUserIdMap.forEach(function (value, key, map) {
                teacherArray.push(key);
            });
        }

        data.orderUnit = unitArray;
        data.orderTeacher = teacherArray;

        //ajax调用保存
        $.ajax({
            url: '${request.contextPath}/bigdata/model/saveAuth',
            data: data,
            type: 'POST',
            dataType: 'json',
            success: function (val) {
                if (!val.success) {
                    showLayerTips4Confirm('error', val.message);
                }
                else {
                    showLayerTips('success','保存成功!','t');
                }
            }
        });
    }

    function resizeTreeContainer() {
        $('.choose-item').height($(window).height() - $('.choose-item').offset().top);
        $('.no-data').css('line-height', ($('.choose-item').height() - 2) + 'px');
    }
</script>