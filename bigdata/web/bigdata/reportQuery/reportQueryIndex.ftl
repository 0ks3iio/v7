<div class="box">
    <div class="box-header">
        <div class="filter-item block">
            <div class="position-relative select-height">
                <select multiple name="" id="select" <#--onchange="tagsChange();"--> class="form-control chosen-select"
                        data-placeholder="输入标签查询">
                    <#if tags?exists && tags?size gt 0>
                        <#list tags as tag>
                            <option value="${tag.id!}">${tag.name!}</option>
                        </#list>
                    </#if>
                </select>
                <div class="filter-item power" style="left: 330px;">
                    <div class="pos-rel pull-left">
                        <input type="text" id="chartName" placeholder="输入名称查询" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" style="width:234px;margin-right:0;">
                    </div>
                    <div class="input-group-btn">
                        <button type="button" class="btn btn-default" onclick="searchReport()" >
                            <i class="fa fa-search"></i>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="box-body padding-top-20">
        <div class="card-list clearfix report">

        </div>
            <div class="text-center" >
                <a href="javascript:void(0);" onclick="chartMore();" id="_get_more">查看更多 > ></a>
            </div>
    </div>
</div>
<input type="hidden" id="_pageSize"  />
<input type="hidden" id="_pageIndex"  />
<input type="hidden" id="_maxPageIndex"  />
<script>
    $(function () {
        $('.chosen-container').width(192);
        //下拉选择框
        function selectChosen() {
            if ($('.chosen-select').length > 0) {
                $('.chosen-select').chosen({
                    allow_single_deselect: true,
                    disable_search_threshold: 10,
                    no_results_text: '没有找到标签'
                });
                //resize the chosen on window resize

                $(window).off('resize.chosen')
                        .on('resize.chosen', function () {
                            $('.chosen-select').each(function () {
                                var $this = $(this);
                                $this.next().css({'width': $this.width()});
                            })
                        }).trigger('resize.chosen');

            }

        }
        selectChosen();

        //加载图表
        $('div.card-list').load('${request.contextPath}/bigdata/report/list?_pageIndex=1&_pageSize=20');

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
            $('div.card-list').load(_contextPath + '/bigdata/report/list?tags=' + tags
                    + "&chartName=" + $('#chartName').val() + "&_pageSize=20&_pageIndex=1");
        })
    });

    function searchReport() {
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
        $('div.card-list').load(_contextPath + '/bigdata/report/list?tags=' + tags
                + "&chartName=" + $('#chartName').val() + "&_pageSize=20&_pageIndex=1");
    }

    function chartMore() {
        var pageIndex = parseInt($('#_pageIndex').val()) + 1;
        var dom = '<div id="_div' + pageIndex + '"></div>';
        $("div.card-list").append(dom);
        var tags = "";
        $('#select').next().find('li.search-choice').each(function () {
            tags = tags + "," + $(this).find('span').text();
        });
        $('#_div' + pageIndex).load('${request.contextPath}/bigdata/report/list?_pageIndex='
                + pageIndex + "&_pageSize=8&tags=" + tags + "&chartName=" + $('#chartName').val());
        if (pageIndex === $('#_maxPageIndex').val()) {
            $('#_get_more').parent('div').remove();
        }
    }
    function tagsChange() {
        setTimeout(function () {
            var tags = "";
            $('#select').next().find('li.search-choice').each(function () {
                tags = tags + "," + $(this).find('span').text();
            });
            $('div.card-list').load('${request.contextPath}/bigdata/report/list?tags=' + tags
                    + "&chartName=" + $('#chartName').val() + "&_pageSize=8&_pageIndex=1");
        }, 20);
    }

    //新增基本图表
    function editChart(id) {
        openModel('708224', '报表模版设置', 1, '${request.contextPath}/bigdata/report/view?chartId=' + id, '大数据管理', '数据管理', null, false);
    }
</script>
