<script src="${request.contextPath}/bigdata/v3/static/plugs/chosen/chosen.jquery.min.js"></script>
<!-- 基本图表首页 -->
<!--删除弹出-->
<div class="layer layer-remove">
    <div class="layer-content text-center">
        <div>
            确定删除?
        </div>
    </div>
</div>
<div class="box no-margin js-show">
    <div class="box-header clearfix">
        <div class="block">
            <div class="position-relative select-height">
                <button class="btn btn-default js-subscription">批量操作</button>
                <div class="form-group no-margin">
                    <select multiple name="" id="select"
                            class="form-control chosen-select"
                            data-placeholder="输入标签查询">
                            <#if tags?exists && tags?size gt 0>
                                <#list tags as tag>
                                    <option value="${tag.id!}">${tag.tagName!}</option>
                                </#list>
                            </#if>
                    </select>
                </div>
                <div class="form-group no-margin power">
                    <div class="input-group">
                        <input type="text" class="form-control" id="chartName" placeholder="输入名称查询">
                        <a href="javascript:;" onclick="searchReport();" class="input-group-addon">
                            <i class="wpfont icon-search"></i></a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="box-body padding-top-20">
        <div class="card-list clearfix report">
            <div id="chart-load-list">

            </div>
        </div>
        <div class="text-center" >
            <a href="javascript:void(0);" onclick="chartMore();" id="_get_more">查看更多 > ></a>
        </div>
    </div>
    <input type="hidden" id="_pageSize"  />
    <input type="hidden" id="_pageIndex"  />
    <input type="hidden" id="_maxPageIndex"  />
</div>
<div class="layer layer-save clearfix">

</div>
<script>
    $(function () {
        //load 列表
        $("#chart-load-list").load('${request.contextPath}/bigdata/model/report/list?_pageIndex=1&_pageSize=20&canDelete=true');

        //下拉选择框
        function selectChosen() {
            $('#select').chosen({
                allow_single_deselect: true,
                disable_search_threshold: 10,
                no_results_text: '没有找到标签'
            }).change(searchReport);

            $(window).off('resize.chosen')
                    .on('resize.chosen', function () {
                        $('.chosen-select').each(function () {
                            $(this).next().css({'width': '300px'});
                        })
                    }).trigger('resize.chosen');
            $('.chosen-choices').css('height', '32px');
        }
        selectChosen();

        $('.type').show();

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
            $('#chart-load-list').load(_contextPath + '/bigdata/model/report/list?tags=' + tags
                    + "&name=" + $('#chartName').val() + "&_pageSize=20&canDelete=true&_pageIndex=1");
        });
        $('.js-subscription').on('click', function () {
            var href = '/bigdata/subcribe/batchOperate?type=modelChart';
            routerProxy.go({
                path: href,
                level: 2,
                name: '批量授权'
            }, function () {
                $('.page-content').load("${springMacroRequestContext.contextPath}" + href);
            });
        });
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
        $('#chart-load-list').load(_contextPath + '/bigdata/model/report/list?tags=' + tags
                + "&name=" + $('#chartName').val() + "&_pageSize=20&canDelete=true&_pageIndex=1");
    }

    function chartMore() {
        var pageIndex = parseInt($('#_pageIndex').val()) + 1;
        var dom = '<div id="_div' + pageIndex + '"></div>';
        $("#chart-load-list").append(dom);
        var tags = "";
        $('#select').next().find('li.search-choice').each(function () {
            tags = tags + "," + $(this).find('span').text();
        });
        $('#_div' + pageIndex).load('${request.contextPath}/bigdata/model/report/list?_pageIndex='
                + pageIndex + "&_pageSize=8&canDelete=true&tags=" + tags + "&name=" + $('#chartName').val());
        if (pageIndex === $('#_maxPageIndex').val()) {
            $('#_get_more').parent('div').remove();
        }
    }

    function deleteModelFavorite(id, edit) {
        if (edit != 'true') {
            layer.msg('该报表是由上级单位创建的，您不能删除！', {icon: 4, time:1500});
            return;
        }
        layer.confirm("确认删除该报表？", {btn: ['确定', '取消'], title: '确认信息', icon: 3, closeBtn: 0}, function (index){
            layer.close(index);
            $.ajax({
                url: '${request.contextPath}/bigdata/model/report/delete',
                data:{favoriteId : id},
                dataType: 'json',
                success: function (val) {
                    showLayerTips('success', '删除成功', 't');
                    searchReport();
                }
            });
        });
    }
    
    function editReport1(id) {
        var isSubmit =false;
        $('.layer-save').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
        $('.layer-save').load("${request.contextPath}/bigdata/model/saveModelFavoriteUI?favoriteId=" + id);
        $('#favoriteName').val($('#modelSelect').find('option:selected').text());

        layer.open({
            type: 1,
            shade: .6,
            title: '设置',
            btn: ['确定', '取消'],
            area: ['800px', '620px'],
            yes: function (index, layero) {
                if (isSubmit) {
                    return;
                }

                if ($('#favoriteName').val() == "") {
                    layer.tips("不能为空", "#favoriteName", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                if ($('#orderId').val() == "") {
                    layer.tips("排序号不能为空", "#orderId", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                var tags = [];
                $('span.selected').each(function () {
                    tags.push($(this).attr('tag_id'));
                });
                if (tags.length > 3) {
                    console.log("tags more");
                }

                if ($('#folderId').val() == null) {
                    layer.tips("请选择文件夹", "#folderId", {
                        tipsMore: true,
                        tips: 3
                    });
                    isSubmit = false;
                    return;
                }

                var options = {
                    url: "${request.contextPath}/bigdata/model/updateDataModelFavorite",
                    data: {
                        conditionParam : $('#conditionSelect').val(),
                        tagArrays : tags
                    },
                    dataType: 'json',
                    success: function (data) {
                        if (!data.success) {
                            showLayerTips4Confirm('error', data.message);
                            isSubmit = false;
                        } else {
                            showLayerTips('success', '保存成功', 't');
                            layer.close(index);
                        }
                    },
                    clearForm: false,
                    resetForm: false,
                    type: 'post',
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }//请求出错
                };
                $("#myForm").ajaxSubmit(options);
            },
            content: $('.layer-save')
        });

    }

    function editReport(id) {
        var href = '/bigdata/model/query?favoriteId=' + id;
        routerProxy.go({
            path: href,
            level: 2,
            name: '多维报表设计'
        }, function () {
            $('.page-content').load("${springMacroRequestContext.contextPath}" + href);
        });
    }
</script>
