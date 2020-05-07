<!--  -->
$(function () {
    //加载列表
    $('#chart-load-list').load(_contextPath + '/bigdata/chartQuery/chartList?_pageIndex=1&_pageSize=20', function () {
        $(document).ready(function(){
            var $width=$('.drive>img').width();
            $('.drive>img').height($width*3/4);
        });
        $(window).resize(function(){
            var $width=$('.drive>img').width();
            $('.drive>img').height($width*3/4);
        });
    })
    //初始化下拉选择框
    $('.chosen-container').width(192);
    $('.chosen-select').chosen({
        allow_single_deselect: true,
        disable_search_threshold: 10,
        no_results_text: '没有找到标签'
    });//.change(editListTagsChange);
    //resize the chosen on window resize

    $(window).off('resize.chosen')
        .on('resize.chosen', function () {
            $('.chosen-select').each(function () {
                var $this = $(this);
                $this.next().css({'width': $this.width()});
            })
        }).trigger('resize.chosen');
    $('#select').on('change', function () {
        var tagIds = $(this).val();
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
        $('#chart-load-list').load(_contextPath + '/bigdata/chartQuery/chartList?tags=' + tags
            + "&chartName=" + $('#chartName').val() + "&_pageSize=20&_pageIndex=1");
    })
    //编辑
    $('.card-list').on('click', 'button', function () {
        httpInvoker.view($(this).attr('chart-id'));
    });
    $('.card-list').on('click', 'a', function () {
        httpInvoker.view($(this).attr('chart-id'));
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
        $loadDom.load(_contextPath + '/bigdata/chartQuery/chartList?_pageIndex='
            + pageIndex + "&_pageSize=20&tags=" + tags + "&chartName=" + $('#chartName').val());
        $('#chart-load-list').append($loadDom);
        if (pageIndex === $('#_maxPageIndex').val()) {
            $('#_get_more').parent('div').remove();
        }
    });

    var httpInvoker = {};

    httpInvoker.view = function (id) {
        openModel('70812112', '图表查询', 1, _contextPath + '/bigdata/chartQuery/' + id, '大数据管理', '数据分析', null, false);
    };
});