$(function () {


    //--------------------------- Http请求 ----------------------------/
    var httpInvoke = {};

    httpInvoke.saveChart = function (data, success, error) {
        $.ajax({
            url: _contextPath + '/bigdata/chart/save',
            data: JSON.stringify(data),
            contentType: 'application/json;charset=UTF-8',
            type: 'POST',
            dataType: 'json',
            success: function (val) {
                if (val.success) {
                    if (typeof success === 'function') {
                        success();
                    }
                } else {
                    showLayerTips('error', val.message, 't')
                    if (typeof error === 'function') {
                        error();
                    }
                }
            }
        })
    };

    /**
     * 删除标签
     * @param tagId 标签ID
     * @param success 成功之后的回调函数
     */
    httpInvoke.deleteTag = function (tagId, chartId, success) {
        $.ajax({
            url: _contextPath + '/bigdata/tag/' + tagId + "?chartId=" + chartId,
            type: 'DELETE',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    layer.msg(response.message, {icon: 2, time: 800});
                } else {
                    if (typeof success === 'function') {
                        success();
                    }
                }
            }
        })
    };

    httpInvoke.addTag = function (tagName, success, error) {
        var data = {};
        data.tagName = tagName;
        data.tagType = $('#tag_type').val();
        $.ajax({
            url: _contextPath + '/bigdata/tag/',
            type: 'POST',
            data: data,
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    if (typeof error === 'function') {
                        error(response);
                    }
                } else {
                    if (typeof success === 'function') {
                        success(response);
                    }
                }
            }
        });
    };

    resizeTreeContainer();

    //标签管理
    $('.control-label').click(function () {
        if ($('.tag-set>span i').css('display') == 'none') {
            $('.tag-set i').show();
            $('span.plus-label').hide();
        } else {
            $('.tag-set i').hide();
            $('span.plus-label').show();
        }
    });
    //删除标签
    $('.tag-set').on('click', '.fa-minus', function () {
        var $this = $(this);
        httpInvoke.deleteTag($this.prev('span').attr('tag_id'), _chart_json.id, function () {
            $this.parent().remove();
        });
    });
    //新增标签
    $('.plus-label').click(function () {
        var str = $('<span><span><input type="" value="" maxlength="4" id="label-input"/></span><i class="fa fa-minus"></i></span>');
        $(this).parent().before(str);
        $('#label-input').focus();
        $('.tag-set input').css({'width': '100%', 'height': '23px', 'border': 'none', 'padding': 0})
    });
    var tag_index = 0;
    $('.tag-set').on('blur', 'input', function () {
        var $this = $(this);
        var newTagText = $.trim($this.val());
        if (newTagText !== '') {
            if (newTagText.length > 4) {
                $this.attr('id', '_newTag');
                layer.tips('标签长度不能超过四个字', '#_newTag', {
                    tipsMore: true,
                    tips: 3,
                    time: 2000
                });
                return;
            }
            httpInvoke.addTag($this.val(), function (res) {
                $this.parent().text($this.val()).attr("tag_id", res.data);
                $('.tag-set>span>span').addClass('label-name');
                $(this).parent().remove();
            }, function (res) {
                $this.attr("id", "_new_tag_input" + tag_index);
                layer.tips(res.message, '#_new_tag_input' + tag_index, {
                    tipsMore: true,
                    tips: 3,
                    time: 2000
                });
                $('#_new_tag_input' + tag_index).parent().parent().remove();
                tag_index++;
            });
        } else {
            $this.parent().parent().remove();
        }
    });
    //选中效果
    var $lastTag = null;
    $('.tag-set').on('click', '.label-name', function () {
        var $this = $(this);
        if ($(".tag-set span.label-name.selected").size() === 3 && !$(this).hasClass('selected')) {
            if ($lastTag) {
                $lastTag.toggleClass('selected');
            } else {
                var $selected = $($(".tag-set span.label-name.selected")[0]);
                $selected.toggleClass('selected');
            }
        }

        $lastTag = $this;
        $this.toggleClass('selected')
    });

    //不同的授权类型点击事件
    $('.order-type').on('click', 'input', function () {
        var orderType = $(this).val();
        if (orderType == '6') {
            $('.order-tree').show();
            $.ajax({
                url: _contextPath + '/bigdata/subcribe/user',
                type: 'GET',
                data: {
                    orderType: orderType,
                    chartId: _chart_json.id
                },
                success: function (response) {
                    $.ajax({
                        url: _contextPath + '/bigdata/common/tree/userTreeIndex',
                        type: 'POST',
                        data: {users: response.data, isChart: $('.order-main-div').hasClass('chart-tree')},
                        dataType: 'html',
                        beforeSend: function () {
                            $('.order-main-div').empty().html("<div class='pos-middle-center'><h4><img src='" + _contextPath + "/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
                        },
                        success: function (response) {
                            $('.order-main-div').empty().html(response);
                        }
                    });
                }
            });
        }
        //单位授权显示单位树
        if (orderType == '4') {
            $('.order-tree').show();
            $.ajax({
                url: _contextPath + '/bigdata/subcribe/unit',
                type: 'GET',
                data: {
                    orderType: orderType,
                    chartId: _chart_json.id
                },
                success: function (response) {
                    $.ajax({
                        url: _contextPath + '/bigdata/common/tree/unitTreeIndex',
                        type: 'POST',
                        data: {units: response.data, isChart: $('.order-main-div').hasClass('chart-tree')},
                        dataType: 'html',
                        beforeSend: function () {
                            $('.order-main-div').empty().html("<div class='pos-middle-center'><h4><img src='" + _contextPath + "/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
                        },
                        success: function (response) {
                            $('.order-main-div').empty().html(response);
                        }
                    });
                }
            });
        }


        if (orderType < 4) {
            $('.order-tree').hide();
        }

    });
    $('.order-type').find('.wp:checked').click();

    function resizeTreeContainer() {
        $('.choose-item').height(300);
        $('.tree-wrap').height(291);
        $('.no-data').css('line-height', ($('.choose-item').height() - 2) + 'px');
    }

});