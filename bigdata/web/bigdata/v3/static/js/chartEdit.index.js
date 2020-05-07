<!--  -->
$(function () {

    //加载列表
    $('#chart-load-list').load(_contextPath + '/bigdata/chart/editList?_pageIndex=1&_pageSize=20');
    $(document).ready(function(){
        var $width=$('.drive>img').width();
        $('.drive>img').height($width*3/4);
    });
    $(window).resize(function(){
        var $width=$('.drive>img').width();
        $('.drive>img').height($width*3/4);
    });
    //初始化下拉选择框
    // $('.chosen-container').width(192);
    $('#select').chosen({
        allow_single_deselect: true,
        disable_search_threshold: 10,
        no_results_text: '没有找到标签'
    }).change(searchChart);
    $('#forCockpit').chosen({
        allow_single_deselect: true,
        disable_search_threshold: 10,
        max_selected_options:1,
        no_results_text: '没有找到标签'
    }).change(searchChart);



    $(window).off('resize.chosen')
        .on('resize.chosen', function () {
            $('.chosen-select').each(function () {
                $(this).next().css({'width': '300px'});
            })
        }).trigger('resize.chosen');
    $('.chosen-choices').css('height', '32px');
    //$('#select').on('change', searchChart);

    //批量操作
    $('.js-subscription').on('click', function () {
        // openModel('70812113', '图表设置', 1, _contextPath + '/bigdata/chart/batchOperate', '大数据管理', '数据分析', null, false);
        router.go({
            path: '/bigdata/subcribe/batchOperate?type=chart',
            name: '批量操作',
            level: 2
        }, function () {
            $('.page-content').load(_contextPath + '/bigdata/subcribe/batchOperate?type=chart')
        })
    });
    //新增图表
    $('.js-add-chart').on('click', function () {
        httpInvoker.addOrEdit("");
    });
    //编辑
    $('.card-list').on('click', 'button', function () {
        if ($(this).attr('edit')=='true') {
            httpInvoker.addOrEdit($(this).attr('chart-id'));
        } else {
            // layer.msg('该图表是由上级单位创建的，您不能编辑！', {icon: 4, time:1500});
            showLayerTips('warn', '该图表是由上级单位创建的，您不能编辑！', 't')
        }
    });
    //删除
    $('.card-list').on('click', '.remove', function () {
        if ($(this).attr('edit')=='true') {
            var id = $(this).attr("chart-id");
            layer.confirm("确认删除该图表？", {btn: ['确定', '取消'], title: '确认信息', icon: 3, closeBtn: 0}, function (index){
                layer.close(index);
                httpInvoker.delete(id);
            });
        } else {
            // layer.msg('该图表是由上级单位创建的，您不能删除！', {icon: 4, time:1500});
            showLayerTips('warn', '该图表是由上级单位创建的，您不能删除！', 't')
        }
    });
    //加载更多
    $('#_get_more').on('click', function () {
        var pageIndex = parseInt($('#_pageIndex').val()) + 1;
        var tagIds = $('#select').val();
        var tags = "";
        if (tagIds != null) {
            $.each(tagIds, function (n, v) {
                if (n == 0){
                    tags = v;
                } else {
                    tags = tags + "," + v;
                }
            })
        }
        var loadDom = '<div></div>';
        var $loadDom = $(loadDom);
        let forCockpit =  "";
        $loadDom.load(_contextPath + '/bigdata/chart/editList?_pageIndex='
            + pageIndex + "&_pageSize=20&tags=" + tags + "&chartName=" + $('#chartName').val() + "&forCockpit=" + forCockpit);
        $('#chart-load-list').append($loadDom);
        if (pageIndex === $('#_maxPageIndex').val()) {
            $('#_get_more').parent('div').remove();
        }
    });

    var httpInvoker = {};

    httpInvoker.addOrEdit = function (id) {
        // openModel('70812112', '图表设置', 1, _contextPath + '/bigdata/chart/why-conflict-delete/chartEdit?chartId=' + id, '大数据管理', '数据分析', null, false);
        router.go({
            path: '/bigdata/chart/why-conflict-delete/chartEdit?chartId=' + id,
            name: '图表编辑',
            level: 2
        }, function () {
            $('.page-content').load(_contextPath + '/bigdata/chart/why-conflict-delete/chartEdit?chartId=' + id)
        })
    };

    httpInvoker.delete = function (id) {
        $.ajax({
            url: _contextPath + '/bigdata/chart/delete?chartId=' + id,
            type: 'GET',
            dataType: 'json',
            success: function (val) {
                if (val.success) {
                    showLayerTips('success', val.message, 't', function () {
                        searchChart();
                    })
                } else {
                    showLayerTips('error', val.message, 't')
                }
            }
        })
    }
});