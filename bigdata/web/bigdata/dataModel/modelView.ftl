<div class="box box-default height-1of1 no-margin clearfix" id="modelViewDiv">
            <div class="col-md-3 height-1of1 no-padding-side">
                <div class="tree scrollbar-made labels no-radius-right">
                    <p class="tree-name border-bottom-cfd2d4">
                        <b>多维数据</b>
                        <#if isBackgroundUser?string('yes', 'no') == 'yes'>
                            <img style="float: right;margin-top: 10px;margin-right: 5px;" src="${request.contextPath}/static/bigdata/images/edits.png" data-toggle="tooltip" data-placement="bottom" title="报表设置" class="js-reportSetting"/>
                            <img style="float: right;margin-top: 10px;margin-right: 5px;" src="${request.contextPath}/static/bigdata/images/powers.png" data-toggle="tooltip" data-placement="bottom" title="批量授权" class="js-subscription"/>
                        </#if>
                    </p>
                    <div class="filter clearfix">
                        <div class="filter-item width-1of1 pa-10">
                            <select name="" class="form-control width-1of1" data-placeholder="默认" id="modelSelect">
                                <option value="">选择多维数据</option>
                                <#list models as item>
                                    <option value="${item.code!}" modelId="${item.id!}" hasTimeDimension="${item.dateDimSwitch!}" hasUserDataset="${item.userDatasetSwitch!}">${item.name!}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                    <p class="tree-name border-bottom-cfd2d4 border-top-cfd2d4" hidden="hidden" id="userDatasetLable">
                        <b>数据集</b>
                    </p>
                    <div class="filter clearfix" hidden="hidden" id="userDatasetDiv">
                        <div class="filter-item width-1of1 pa-10">
                            <select name="" class="form-control width-1of1" data-placeholder="默认" id="userDataSelect">
                            </select>
                        </div>
                    </div>
                    <p class="tree-name border-bottom-cfd2d4 border-top-cfd2d4 no-margin">
                        <b>指标</b>
                    </p>
                    <div class="clearfix padding-10 scrollbar-made js-fa-kind" id="indexDiv">
                    </div>
                    <p class="tree-name border-bottom-cfd2d4 border-top-cfd2d4 no-margin">
                        <b>维度</b>
                    </p>
                    <div class="js-row-line after js-height-b pos-rel">
                        <div class="clearfix row-line js-target">
                            <div class="col-md-6 no-padding">
                                <p class="no-margin text-center"><b>行</b></p>
                            </div>
                            <div class="col-md-6 no-padding">
                                <p class="no-margin text-center"><b>列</b></p>
                            </div>
                        </div>
                        <div class="clearfix position-relative js-height scrollbar-made">
                            <div class="col-md-6 no-padding min-height-1of1 scale-img">
                                <div class="row-first" id="dimensionRowDiv">
                                </div>
                            </div>
                            <div class="col-md-6 no-padding min-height-1of1 scale-img">
                                <div class="line-first" id="dimensionColumnDiv">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-9 height-1of1 no-padding-side">
                <div class="tree labels no-radius-left no-border-left">
                    <p class="tree-name border-bottom-cfd2d4">
                        <span><b>多维报表</b></span>
                        <input type="hidden" id="query_sql">
                        <#if isBackgroundUser?string('yes', 'no') == 'yes'>
                            <button class="btn btn-lightblue js-add-kanban" style="display: none;right: 180px;position: absolute;top: 50%;transform: translateY(-50%);" id="saveBtn">保存</button>
                        </#if>
                        <span class="pos-right right-100" hidden="hidden" id="exportSpan">
													<img src="${request.contextPath}/static/bigdata/images/export-PDF.png"
                                                         class="pointer js-export-pdf" onclick="toPdf()"/>
													&nbsp;
													<img src="${request.contextPath}/static/bigdata/images/export-EXL.png"
                                                         class="pointer js-export-exl" onclick="toExcel()"/>
													&nbsp;
												</span>
                        <span class="tab-btn js-chart-type">
													<span class="fake-btn fake-btn-default no-radius-right no-border-right active" type="report">表格</span>
													<span class="fake-btn fake-btn-default no-radius-left no-border-left" type="chart">图表</span>
												</span>
                    </p>

                    <div class="report-wrap scrollbar-made" id="report_div">
                        <div class="wrap-1of1 centered no-data-state" style="height: 90%">
                            <div class="text-center">
                                <img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png" id="tipImg">
                                <p>报表需要至少需要一个‘指标’和一个‘行’维度，请在左侧选择相应的指标和维度</p>
                            </div>
                        </div>
                        <div class="box" id="reportDiv" style="display: none">
                            <table id="multiDimensionTable"
                               class="table table-bordered table-striped table-hover text-center display"
                               style="text-align: center" width="100%">
                            <thead id="head"></thead>
                            <tbody id="data"></tbody>
                        </table>
                        </div>
                        <div class="box" style="display: none">
                            <div class="score-title no-margin pos-rel">
                                <span id="chart_title">&nbsp;</span>
                                <span class="pos-right-c chart_type">
															<span class="fake-btn-img fake-btn-default no-radius-right no-border-right" type="line" id=""><img src="${request.contextPath}/static/bigdata/images/icon-line.png"/></span>
															<span class="fake-btn-img fake-btn-default no-radius active" type="bar"><img src="${request.contextPath}/static/bigdata/images/icon-bar.png"/></span>
															<span class="fake-btn-img fake-btn-default no-radius-left no-border-left" type="cookie"><img src="${request.contextPath}/static/bigdata/images/icon-pie.png"/></span>
														</span>
                            </div>
                            <div class="chart-bar" id="chartDiv"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
<input type="hidden" value="5" id="tag_type" />
<div class="layer layer-filtrate">
    <input type="hidden" id="filterParamId">
    <div class="layer-content clearfix">
        <div class="col-sm-6">
            <p class="choose-num">筛选对象
                <label class="pos-rel pos-right js-fullChioce">
                    <input name="course-checkbox" type="checkbox" class="wp" onclick="selectAllFilterData(this)">
                    <span class="lbl"> 全选</span>
                </label>
            </p>
            <div class="choose-item padding-t-10" id="filterDataDiv">
            </div>
        </div>

        <div class="col-sm-6">
            <p class="choose-num">已选（<span>0</span>）</p>
            <div class="choose-item position-relative js-choice-target">
                <div class="no-data pos-centered">
                    <img src="${request.contextPath}/static/bigdata/images/no-choice.png"/>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="layer layer-save clearfix">

</div>
<script type="text/javascript">
    $(function () {
        $('[data-toggle="tooltip"]').tooltip();

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

        $('.js-reportSetting').on('click', function () {
            var href = '/bigdata/model/report/manage';
            routerProxy.go({
                path: href,
                level: 2,
                name: '多维报表管理'
            }, function () {
                $('.page-content').load("${springMacroRequestContext.contextPath}" + href);
            });
        });

        $('.js-fa-kind').each(function () {
            $(this).css({
                'max-height': ($(window).height() - $(this).offset().top - 41 - 48 - 20) / 2,
                overflowY: 'auto'
            });
        });

        $('body').on('click', '.js-add-active>div', function () {
            $(this).addClass('active').siblings().removeClass('active')
        }).on('mouseenter', '.js-add-active>div', function () {
            $(this).addClass('hover')
        }).on('mouseleave', '.js-add-active>div', function () {
            $(this).removeClass('hover')
        });

        //选择图表类型
        $('body').on('click','.chart-type',function () {
            $('#chartType').val($(this).attr('type'));
        });

        height();

        //选择图表类型
        $('body').on('click','.screen-type',function () {
            $('#windowSize').val($(this).attr('type'));
        });

        scrollShow();

        //导出
        $('.js-export-pdf').hover(function () {
            $(this).attr('src', '${request.contextPath}/static/bigdata/images/export-PDF-active.png')
        }, function () {
            $(this).attr('src', '${request.contextPath}/static/bigdata/images/export-PDF.png')
        });
        $('.js-export-exl').hover(function () {
            $(this).attr('src', '${request.contextPath}/static/bigdata/images/export-EXL-active.png')
        }, function () {
            $(this).attr('src', '${request.contextPath}/static/bigdata/images/export-EXL.png')
        });

        $('#modelSelect').change(function () {
            $('.no-data-state').show();
            $('#exportSpan').hide();
            $('#saveBtn').hide();
            $('#chartBtn').hide();
            $('#chartDiv').parent().hide();
            $('#multiDimensionTable_wrapper').hide();
            dimensionRowParam = [];
            dimensionColumnParam = [];
            var code = $(this).val();
            if (code == "") {
                $('#indexDiv').empty();
                $('#dimensionRowDiv').empty();
                $('#dimensionColumnDiv').empty();
                return;
            }
            changeModelSelect(code);
        });

        $('#userDataSelect').change(function () {
            if ($('.js-chart-type').find('.active').attr('type') == 'chart') {
                // 当前图表类型
                var type = $('.chart_type').find('.active').attr('type');
                showChart(type);
            } else {
                showReport();
            }
        });

        $('.choose-item').on('click', '.fa-times-circle', function () {
            $(this).parent().remove();
            $('.js-choice[data-index=' + $(this).parent().data('index') + ']').find('.filterItem').removeAttr('checked');
            num--;
            $('.choose-num>span').text(num);
            $('.js-fullChioce').find('input').prop('checked', false);
            if (num == 0) {
                $('.no-data').show()
            }
        });

        //下拉
        $('#dimensionRowDiv').on('click','.js-down', function(){
            $(this).find('img').toggleClass('active');
            $(this).parent().next('.dowm-part').toggleClass('hide')
        });

        $('#dimensionColumnDiv').on('click','.js-down', function(){
            $(this).find('img').toggleClass('active');
            $(this).parent().next('.dowm-part').toggleClass('hide')
        });

        $('.chart_type').on('click','span.fake-btn-img',function(){
            $(this).addClass('active').siblings('span').removeClass('active');
            var type = $(this).attr('type');
            showChart(type);
        });

        $('.tree-name').on('click','.js-chart-type span',function(){
            $(this).addClass('active').siblings().removeClass('active');
            if ($('#modelSelect').val() == '') {
                return;
            }
            // 判断选择的图表还是报表
            var type = $(this).attr('type');
            if (type == 'report') {
                showReport();
            } else {
                var chart_type = $('.chart_type').find('.active').attr('type');
                showChart(chart_type);
            }
        });

        $('#saveBtn').on('click', function () {
            var isSubmit =false;
            $('.layer-save').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
            $('.layer-save').load("${request.contextPath}/bigdata/model/saveModelFavoriteUI?code=" + $('#modelSelect').val() + "&favoriteId=" + '${dataModelFavorite.id!}');
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

                    var indexParam = [];
                    $('.modelParam').each(function (i, v) {
                        if ($(v).is(':checked') == true && $(v).hasClass('index')) {
                            indexParam.push($(v).attr('paramId'));
                        }
                    });
                    var code = $('#modelSelect').val();

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
                        url: "${request.contextPath}/bigdata/model/saveDataModelFavorite",
                        data: {
                            code: code,
                            indexParam: JSON.stringify(indexParam),
                            dimensionRowParam: JSON.stringify(dimensionRowParam),
                            dimensionColumnParam: JSON.stringify(dimensionColumnParam),
                            filterDataMap: JSON.stringify(filterDataMap),
                            modelDatasetId : $('#userDataSelect').val(),
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
        });

    });
    //已选
    var num = 0;

    var filterDataMap = {};

    function initFilterDataMap() {
        var filterData = '${filterDataMap!}';
        if (filterData != '') {
            filterDataMap = JSON.parse(filterData);
            $.each(filterDataMap, function (k, v) {
                $('.modelParam.dimension_row[paramId="'+ k +'"]').parent().prev().append('<span class="badge badge-yellow badge-made">' + v.length + '</span>');
            })
        }
    }

    function scrollShow() {
        var a = $('.row-first').height() + 20;
        var b = $('.js-height').height();
        if (a > b) {
            $('.js-target').addClass('padding-right-7')
        } else {
            $('.js-target').removeClass('padding-right-7')
        }
    }

    function height() {
        var datasetHeight = 0;
        var hasUserDataset = $('#modelSelect').find('option:selected').attr('hasUserDataset');
        if (hasUserDataset == '1') {
            datasetHeight = 104;
        }
        $('.js-height').each(function(){
            $(this).css({
                height: $('.tree').height() - 48 - 52 - 48 - datasetHeight - $('.js-fa-kind').height() - 20 - 48 - 41 - 2,
                overflowY: 'auto',
                overflowX: 'hidden'
            });
        });
        $('.js-height-b').each(function(){
            $(this).css({
                height: $('.js-height').height() + 41,
                overflow: 'hidden'
            });
        });
    }

    function changeModelSelect(code) {
        // 如果开启了时间维度
        var hasUserDataset = $('#modelSelect').find('option:selected').attr('hasUserDataset');
        if (hasUserDataset == '1') {
            $('#userDatasetLable').show();
            $('#userDatasetDiv').show();
            var modelId = $('#modelSelect').find('option:selected').attr('modelId');
            $.ajax({
                url: '${request.contextPath}/bigdata/model/user/getUserDataset',
                data: {modelId: modelId},
                type: 'POST',
                dataType: 'json',
                async:false,
                success: function (response) {
                    if (!response.success) {
                        layer.msg(response.message, {icon: 2});
                    } else {
                        var data = response.data;
                        $('#userDataSelect').empty();
                        $.each(data, function (i, v) {
                            $('#userDataSelect').append('<option value="'+v.id+'">'+ v.dsName +'</option>');
                        });
                        height();
                        scrollShow();
                    }
                }
            });
        } else {
            $('#userDatasetLable').hide();
            $('#userDatasetDiv').hide();
        }

        $('#dimensionRowDiv').empty();
        $('#dimensionColumnDiv').empty();
        $.ajax({
            url: '${request.contextPath}/bigdata/model/getModelParam',
            data: {code: code, type: 'index'},
            type: 'POST',
            async: false,
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    layer.msg(response.message, {icon: 2});
                } else {
                    var data = response.data;
                    $('#indexDiv').empty();
                    var c = 0;
                    $.each(data, function (i, v) {
                        if (v.isFilter != '1') {
                            var element = "<label class=\"pos-rel js-stu-num\">\n" +
                                    "                            <input name=\"course-checkbox\" onchange='changeModelParam(this)' paramId='" + v.id + "' paramName='" + v.name + "' type=\"checkbox\" class=\"wp modelParam index\">\n" +
                                    "                            <span class=\"lbl\" style='display: inline'>" + v.name + "</span>\n" +
                                    "                        </label>";
                            if (c % 2 != 0) {
                                element = "<div class=\"col-md-6\">" + element + "<br><br></div>";
                            } else {
                                element = "<div class=\"col-md-6\">" + element + "</div>";
                            }
                            $('#indexDiv').append(element);
                            c++;
                        }
                    });
                    height();
                    scrollShow();
                }
            }
        });

        // 如果开启了时间维度
        var hasTime = $('#modelSelect').find('option:selected').attr('hasTimedimension');
        if (hasTime == '1') {
            $.ajax({
                url: '${request.contextPath}/bigdata/model/getModelParam',
                data: {code: 'date', type: 'dimension'},
                type: 'POST',
                dataType: 'json',
                async:false,
                success: function (response) {
                    if (!response.success) {
                        layer.msg(response.message, {icon: 2});
                    } else {
                        var data = response.data;
                        var img = '${request.contextPath}/static/bigdata/images/filtrate.png';
                        var downImg = '${request.contextPath}/static/bigdata/images/icon-down.png';

                        var time_row = "<div><label class=\"pos-rel js-kind js-down\">\n" +
                                "<img src=\""+downImg+"\" class=\"icon-down pointer active\"/>"+
                                "<span class=\"lbl pointer\">日期</span>\n" +
                                "</label>\n" +
                                "</div>\n";

                        var time_column = "<div><label class=\"pos-rel js-kind js-down\">\n" +
                                "<img src=\""+downImg+"\" class=\"icon-down pointer active\"/>"+
                                "<span class=\"lbl pointer\">日期</span>\n" +
                                "</label>\n" +
                                "</div>\n";

                        $('#dimensionRowDiv').append(time_row);
                        $('#dimensionColumnDiv').append(time_column);

                        var row = '<div class="dowm-part hide">';
                        var column = '<div class="dowm-part hide">';

                        $.each(data, function (i, v) {
                            if (v.isFilter != '1') {
                                var element_row = "<div><div class=\"pos-right pointer js-layer\" data-number='date_row" + i + "' onclick='filterUI(this)' paramId='" + v.id + "'>\n" +
                                        "<img src=\"" + img + "\" alt=\"\">\n" +
                                        "</div>\n" +
                                        "<label class=\"pos-rel js-kind\">\n" +
                                        "<input name=\"course-checkbox\" onchange='changeModelParam(this)' paramId='" + v.id + "' type=\"checkbox\" class=\"wp modelParam dimension_row\">\n" +
                                        "<span class=\"lbl\">" + v.name + "</span>\n" +
                                        "</label></div>\n";

                                var element_column = "<div><div class=\"pos-right pointer js-layer\" data-number='date_column" + i + "' onclick='filterUI(this)' paramId='" + v.id + "'>\n" +
                                        "<img src=\"" + img + "\" alt=\"\">\n" +
                                        "</div>\n" + "<label class=\"pos-rel js-kind\">\n" +
                                        "<input name=\"course-checkbox\" onchange='changeModelParam(this)' paramId='" + v.id + "' type=\"checkbox\" class=\"wp modelParam dimension_column\">\n" +
                                        "<span class=\"lbl\">" + v.name + "</span>\n" +
                                        "</label></div>\n";
                                row = row + element_row;
                                column = column + element_column;
                            }
                        });

                        row = row + '</div>';
                        column = column + '</div>';

                        $('#dimensionRowDiv').append(row);
                        $('#dimensionColumnDiv').append(column);

                        height();
                        scrollShow();
                    }
                }
            });
        }

        $.ajax({
            url: '${request.contextPath}/bigdata/model/getModelParam',
            data: {code: code, type: 'dimension'},
            type: 'POST',
            dataType: 'json',
            async: false,
            success: function (response) {
                if (!response.success) {
                    layer.msg(response.message, {icon: 2});
                } else {
                    var data = response.data;
                    var img = '${request.contextPath}/static/bigdata/images/filtrate.png';
                    $.each(data, function (i, v) {
                        if (v.isFilter != '1') {
                            var element_row = "<div class=\"\">\n" +
                                    "<div class=\"pos-right pointer js-layer\" data-number='row" + i + "' onclick='filterUI(this)' paramId='" + v.id + "'>\n" +
                                    "<img src=\"" + img + "\" alt=\"\">\n" +
                                    "</div>\n" +
                                    "<label class=\"pos-rel js-kind\">\n" +
                                    "<input name=\"course-checkbox\" onchange='changeModelParam(this)' paramId='" + v.id + "' type=\"checkbox\" class=\"wp modelParam dimension_row\">\n" +
                                    "<span class=\"lbl\">" + v.name + "</span>\n" +
                                    "</label>\n" +
                                    "</div>\n";

                            var element_column = "<div class=\"\">\n" +
                                    "<div class=\"pos-right pointer js-layer\" data-number='column" + i + "' onclick='filterUI(this)' paramId='" + v.id + "'>\n" +
                                    "<img src=\"" + img + "\" alt=\"\">\n" +
                                    "</div>\n" +
                                    "<label class=\"pos-rel js-kind\">\n" +
                                    "<input name=\"course-checkbox\" onchange='changeModelParam(this)' paramId='" + v.id + "' type=\"checkbox\" class=\"wp modelParam dimension_column\">\n" +
                                    "<span class=\"lbl\">" + v.name + "</span>\n" +
                                    "</label>\n" +
                                    "</div>\n";
                            $('#dimensionRowDiv').append(element_row);
                            $('#dimensionColumnDiv').append(element_column);
                        }
                    });
                    height();
                    scrollShow();
                }
            }
        });
    }

    function selectFilterData(e) {
        if ($(e).find('input').is(':checked')) {
            var str = '<div data-index="' + $(e).data('index') + '">' + $(e).find('.lbl').text() + '<i class="fa fa-times-circle"></i></div>';
            $('.no-data').hide();
            $('.js-choice-target').append(str);
            num = $('.js-choice-target>div').length;
            $('.choose-num>span').text(num);
            if (num == $('.filterItem').length) {
                $('.js-fullChioce').find('input').prop('checked', true);
            }
        } else {
            $('.js-choice-target').find('div[data-index=' + $(e).data('index') + ']').remove();
            num = $('.js-choice-target>div').length;
            $('.choose-num>span').text(num);
            if (num == 0) {
                $('.no-data').show()
            }
            $('.js-fullChioce').find('input').prop('checked', false);
        }
    }
    
    function selectAllFilterData(e) {
        if ($(e).prop('checked')) {
            $('.no-data').hide();
            $('.js-choice-target').empty();
            $('.filterItem').each(function () {
                $(this).prop('checked', true);
                var lab = $(this).parent();
                var str = '<div data-index="' + lab.data('index') + '">' + lab.find('.lbl').text() + '<i class="fa fa-times-circle"></i></div>';
                $('.js-choice-target').append(str);
            });
            num = $('.js-choice-target>div').length;
            $('.choose-num>span').text(num);
        } else {
            $('.js-choice-target').empty();
            $('.no-data').show();
            $('.filterItem').each(function () {
                $(this).prop('checked', false);
            });
            num = 0;
            $('.choose-num>span').text(0);
        }
    }

    function filterUI(e) {
        if ($(e).hasClass('failure')) {
            layer.msg('不能同时筛选行和列！', {icon: 4, time:1500});
            return;
        }
        // 判断维度有没有选择
        var checkbox = $(e).next().find('.modelParam');
        /*if (checkbox.is(':checked') != true) {
            layer.msg('请先选择维度，再进行筛选！', {icon: 4, time:1500});
            return;
        }*/
        var dimensionId = $(e).attr('paramId');
        $('#filterParamId').val(dimensionId);
        const $text = $(e).next().find('.lbl').text();
        const $num = $(e).data('number');
        $('#filterDataDiv').empty();
        $('.js-choice-target').empty();
        $('.choose-num>span').text(0);
        $('.js-fullChioce').find('input').prop('checked', false);
        // 查询过滤数据
        $.ajax({
            url: '${request.contextPath}/bigdata/model/getFilterData',
            data: {
                code: $('#modelSelect').val(),
                dimensionId: dimensionId,
                modelDatasetId : $('#userDataSelect').val()
            },
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    layer.msg(response.message, {icon: 2});
                } else {
                    var data = response.data;
                    $.each(data, function (i, v) {
                        var s = "<div class=\"margin-b-10\">\n" +
                                "                    <label class=\"pos-rel js-choice\" onclick='selectFilterData(this)'  data-index='" + i + "'>\n" +
                                "                        <input name=\"course-checkbox\" type=\"checkbox\" class=\"wp filterItem\">\n" +
                                "                        <span class=\"lbl\">" + v + "</span>\n" +
                                "                    </label>\n" +
                                "                </div>";
                        $('#filterDataDiv').append(s);
                    });
                    if (filterDataMap[dimensionId] != num) {
                        var array = filterDataMap[dimensionId];
                        var items = $('#filterDataDiv').find('.filterItem');
                        $.each(items, function (i, v) {
                            var text = $(v).next().text();
                            if ($.inArray(text, array) >= 0) {
                                $(v).attr('checked', true);
                                var str = '<div data-index="' + $(v).parent().data('index') + '">' + text + '<i class="fa fa-times-circle"></i></div>';
                                $('.no-data').hide();
                                $('.js-choice-target').append(str);
                            }
                        });
                        num = $('.js-choice-target>div').length;
                        $('.choose-num>span').text(num);
                        if (num == response.data.length) {
                            $('.js-fullChioce').find('input').prop('checked', true);
                        }
                    }
                    $('.layui-layer-btn0').attr('paramId', checkbox.attr('paramId'));
                    if (checkbox.hasClass('dimension_row')) {
                        $('.layui-layer-btn0').addClass('dimension_row');
                    }
                    if (checkbox.hasClass('dimension_column')) {
                        $('.layui-layer-btn0').addClass('dimension_column');
                    }
                }
            }
        });

        layer.open({
            type: 1,
            shade: .6,
            title: $text,
            btn: ['确定', '取消'],
            area: ['450px', '500px'],
            content: $('.layer-filtrate')
        });

        $('.layui-layer-btn0').click(function () {
            var array = [];
            var items = $('#filterDataDiv').find('.filterItem');
            $.each(items, function (i, v) {
                if ($(v).is(":checked")) {
                    array.push($(v).next().text());
                }
            });
            filterDataMap[dimensionId] = array;
            if (num > 0) {
                $('div[data-number=' + $num + ']').append('<span class="badge badge-yellow badge-made">' + num + '</span>')
            } else {
                $('div[data-number=' + $num + ']').find('.badge').remove()
            }

            var paramId = $(this).attr('paramId');
            if ($(this).hasClass('dimension_row')) {
                $('.dimension_column').each(function (i, v) {
                    if (paramId == $(v).attr('paramId')) {
                        if (num > 0) {
                            $(v).attr('disabled', 'disabled');
                            $(v).parent().prev().addClass('failure');
                        } else {
                            // 如果维度没有被选中
                            if ($('#dimensionRowDiv').find('.modelParam[paramId=' + paramId + ']').is(':checked')) {
                                return;
                            }
                            $(v).removeAttr('disabled');
                            $(v).parent().prev().removeClass('failure');
                        }
                    }
                });
            }

            if ($(this).hasClass('dimension_column')) {
                $('.dimension_row').each(function (i, v) {
                    if ($(v).attr('paramId') == paramId) {
                        if (num > 0) {
                            $(v).attr('disabled', 'disabled');
                            $(v).parent().prev().addClass('failure');
                        } else {
                            // 如果维度没有被选中
                            if ($('#dimensionColumnDiv').find('.modelParam[paramId=' + paramId + ']').is(':checked')) {
                                return;
                            }
                            $(v).removeAttr('disabled');
                            $(v).parent().prev().removeClass('failure');
                        }
                    }
                });
            }

            renew();
            if ($('.js-chart-type').find('.active').attr('type') == 'chart') {
                // 当前图表类型
                var type = $('.chart_type').find('.active').attr('type');
                showChart(type);
            } else {
                showReport();
            }
        });
    }

    function renew() {
        num = 0;
        $('.choose-num>span').text(0);
        $('.no-data').show();
        $('.no-data').siblings().remove();
        $('.js-choice').find('input').removeProp('checked');
    }

    var table;
    var originalColumnDefs = [
        {
            targets: 0,
            createdCell: function (td, cellData, rowData, row, col) {
                //第0列需要合并的行数然后移除未设置num的行
                var rowspan = rowData.num0; //拿到第一列需要合并的数据
                if (rowspan == undefined) {
                    $(td).remove();
                } else {
                    $(td).attr('rowspan', rowspan)
                }
            }
        },
        {
            targets: 1,
            createdCell: function (td, cellData, rowData, row, col) {
                //第0列需要合并的行数然后移除未设置num的行
                var rowspan = rowData.num1; //拿到第一列需要合并的数据
                if (rowspan == undefined) {
                    $(td).remove();
                } else {
                    $(td).attr('rowspan', rowspan)
                }
            }
        },
        {
            targets: 2,
            createdCell: function (td, cellData, rowData, row, col) {
                //第0列需要合并的行数然后移除未设置num的行
                var rowspan = rowData.num2; //拿到第一列需要合并的数据
                if (rowspan == undefined) {
                    $(td).remove();
                } else {
                    $(td).attr('rowspan', rowspan)
                }
            }
        },
        {
            targets: 3,
            createdCell: function (td, cellData, rowData, row, col) {
                //第0列需要合并的行数然后移除未设置num的行
                var rowspan = rowData.num3; //拿到第一列需要合并的数据
                if (rowspan == undefined) {
                    $(td).remove();
                } else {
                    $(td).attr('rowspan', rowspan)
                }
            }
        },
        {
            targets: 4,
            createdCell: function (td, cellData, rowData, row, col) {
                //第0列需要合并的行数然后移除未设置num的行
                var rowspan = rowData.num4; //拿到第一列需要合并的数据
                if (rowspan == undefined) {
                    $(td).remove();
                } else {
                    $(td).attr('rowspan', rowspan)
                }
            }
        }
    ];

    var dimensionRowParam = [];
    var dimensionColumnParam = [];


    function changeModelParam(obj) {
        var paramId = $(obj).attr('paramId');
        if ($(obj).is(':checked') == true) {
            if ($(obj).hasClass('dimension_row')) {
                dimensionRowParam.push(paramId);
            }
            if ($(obj).hasClass('dimension_column')) {
                dimensionColumnParam.push(paramId);
            }
        } else {
            if ($(obj).hasClass('dimension_row')) {
                var index = dimensionRowParam.indexOf(paramId);
                dimensionRowParam.splice(index, 1);
            }
            if ($(obj).hasClass('dimension_column')) {
                var index = dimensionColumnParam.indexOf(paramId);
                dimensionColumnParam.splice(index, 1);
            }

            // 删除过滤的数据
            filterDataMap[paramId] = [];
            // 数字清除
            $(obj).parent().prev().find('.badge-yellow').remove();
        }

        if (dimensionRowParam.length > 5) {
            layer.msg('最多只允许选择5个行维度', {icon: 2});
            dimensionRowParam.pop();
            $(obj).removeAttr('checked');
            return;
        }

        if (dimensionColumnParam.length > 2) {
            layer.msg('最多只允许选择2个列维度', {icon: 2});
            dimensionColumnParam.pop();
            $(obj).removeAttr('checked');
            return;
        }

        if ($(obj).hasClass('dimension_row')) {
            $('.dimension_column').each(function (i, v) {
                if ($(v).attr('paramId') == $(obj).attr('paramId')) {
                    if ($(obj).is(':checked')) {
                        $(v).attr('disabled', 'disabled');
                        $(v).parent().prev().addClass('failure');
                    } else {
                        $(v).removeAttr('disabled');
                        $(v).parent().prev().removeClass('failure');
                    }
                }
            });
        }

        if ($(obj).hasClass('dimension_column')) {
            $('.dimension_row').each(function (i, v) {
                if ($(v).attr('paramId') == $(obj).attr('paramId')) {
                    if ($(obj).is(':checked')) {
                        $(v).attr('disabled', 'disabled');
                        $(v).parent().prev().addClass('failure');
                    } else {
                        $(v).removeAttr('disabled');
                        $(v).parent().prev().removeClass('failure');
                    }
                }
            });
        }

        if ($('.js-chart-type').find('.active').attr('type') == 'chart') {
            // 当前图表类型
            var type = $('.chart_type').find('.active').attr('type');
            showChart(type);
        } else {
            showReport();
        }

    }

    var offsetTop = $('#tipImg').offset().top;
    var offsetLeft = $('#tipImg').offset().left + 30;

    function showReport() {
        $('.no-data-state').hide();
        var index = layer.load(1, {
            shade: [0], //0.1透明度的白色背景
            offset: [offsetTop +'px', offsetLeft + 'px']
        });
        $('#chartDiv').parent().hide();
        var indexParam = [];
        $('.modelParam').each(function (i, v) {
            if ($(v).is(':checked') == true && $(v).hasClass('index')) {
                indexParam.push($(v).attr('paramId'));
            }
        });

        if (table) {
            $("#head").empty();
            $("#data").empty();
        }
        var code = $('#modelSelect').val();
        $.ajax({
            url: '${request.contextPath}/bigdata/model/showReport',
            data: {
                code: code,
                indexParam: JSON.stringify(indexParam),
                dimensionRowParam: JSON.stringify(dimensionRowParam),
                dimensionColumnParam: JSON.stringify(dimensionColumnParam),
                filterDataMap: JSON.stringify(filterDataMap),
                modelDatasetId : $('#userDataSelect').val()
            },
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                layer.close(index);
                if (!response.success) {
                    $('.no-data-state').show();
                    $('#exportSpan').hide();
                    $('#saveBtn').hide();
                    $('#chartBtn').hide();
                    $('#query_sql').val(response.message);
                    return;
                } else {
                    $('#query_sql').val(response.message);
                    $('#report_div').css({
                        overflow: 'auto'
                    });
                    $('.no-data-state').hide();
                    $('#reportDiv').show();
                    $('#exportSpan').show();
                    $('#saveBtn').show();
                    $('#chartBtn').show();
                    var dataList = [];
                    var columnList = [];
                    var columnDefs = [];
                    if (table) {
                        table.destroy(); //还原初始化了的dataTable
                        $("#head").empty();
                        $("#data").empty();
                    }
                    var result = JSON.parse(response.data);
                    var columnSize = parseInt(result.totalColumnSize);
                    var dimRowSize = parseInt(result.dimRowSize);
                    for (var i = 0; i < result.data.length; i++) {
                        var dataObj = {};
                        for (var j = 0; j < columnSize; j++) {
                            dataObj['' + j] = result.data[i][j];
                        }
                        for (var ii = 0; ii < dimRowSize; ii++) {
                            dataObj['num' + ii] = result.data[i]['num' + ii];
                        }
                        dataList.push(dataObj);
                    }
                    for (var i = 0; i < columnSize; i++) {
                        var columnObj = {};
                        columnObj['data'] = i;
                        columnObj['orderable'] = false;
                        columnObj['searchable'] = false;
                        columnList.push(columnObj);
                    }

                    $("#head").html(result.head);

                    for (var i = 0; i < dimRowSize - 1; i++) {
                        columnDefs.push(originalColumnDefs[i])
                    }
                    createTable(dataList, columnList, columnDefs);
                }
            },
            error: function () {
                layer.close(index);
            }
        });
    }
    
    function showChart(type) {

        $('.no-data-state').hide();
        var index = layer.load(1, {
            shade: [0], //0.1透明度的白色背景
            offset: [offsetTop +'px', offsetLeft + 'px']
        });

        var indexParam = [];
        $('.modelParam').each(function (i, v) {
            if ($(v).is(':checked') == true && $(v).hasClass('index')) {
                indexParam.push($(v).attr('paramId'));
            }
        });

        var code = $('#modelSelect').val();
        var title = $('#modelSelect').find('option:selected').text();
        // $('#chart_title').empty().text(" ");
        $.ajax({
            url: '${request.contextPath}/bigdata/model/showChart',
            data: {
                code: code,
                indexParam: JSON.stringify(indexParam),
                dimensionRowParam: JSON.stringify(dimensionRowParam),
                dimensionColumnParam: JSON.stringify(dimensionColumnParam),
                filterDataMap: JSON.stringify(filterDataMap),
                type : type,
                modelDatasetId : $('#userDataSelect').val()
            },
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                layer.close(index);
                if (!response.success) {
                    $('#reportDiv').hide();
                    $('.no-data-state').show();
                    $('#chartDiv').parent().hide();
                    $('#query_sql').val(response.message);
                    return;
                } else {
                    $('#reportDiv').hide();
                    $('.no-data-state').hide();
                    $('#exportSpan').hide();
                    $('#saveBtn').show();
                    $('#chartDiv').parent().show();
                    $('#chartDiv').css({
                        width: '100%',
                        height: $('#report_div').height() + 40
                    });
                    $('#report_div').css({
                        overflow: 'hidden'
                    });
                    document.getElementById('chartDiv').setAttribute('_echarts_instance_', '');
                    var echart_div = echarts.init(document.getElementById('chartDiv'));
                    var data = JSON.parse(response.data);
                    $('#query_sql').val(response.message);
                    echart_div.setOption(data);
                    echart_div.resize();
                }
            },
            error: function () {
                layer.close(index);
            }
        });
    }

    function createTable(dataList, columnList, columnDefs) {
        table = $("#multiDimensionTable").DataTable({
            //因为需要多次初始化，所以需要设置允许销毁实例
            destroy: true,
            paging: false,
            processing: true,
            serverSide: false,
            searching: false, //搜索栏
            info: false, //
            ordering: false, //全局定义是否启用排序，优先级比columns.orderable高
            language: {
                "sProcessing": "处理中...",
                "sZeroRecords": "没有匹配结果",
                "sSearch": "搜索:",
                "sLoadingRecords": "载入中..."
            },
            data: dataList,
            columns: columnList,
            columnDefs: columnDefs
        });
        table.draw(true);
    }

    function toExcel() {
        $('#multiDimensionTable').table2excel({
            name: "Excel Document Name",
            filename: "多维报表分析.xls",
            exclude_img: true,
            exclude_links: true,
            exclude_inputs: true
        });
    }

    function toPdf() {
        var targetDom = $("#multiDimensionTable");
        var copyDom = targetDom.clone();
        copyDom.width(targetDom.width() + "px");
        copyDom.height(targetDom.height() + "px");
        copyDom.css('background', '#fff');
        copyDom.css({
            "position": "absolute",
            "top": "0px",
            "z-index": "-1"
        });
        $('body').append(copyDom);
        //要转成PDF的标签的范围。
        html2canvas(copyDom, {
            //Whether to allow cross-origin images to taint the canvas
            allowTaint: true,
            //Whether to test each image if it taints the canvas before drawing them
            taintTest: false,
            onrendered: function (canvas) {
                var contentWidth = canvas.width;
                var contentHeight = canvas.height;
                //一页pdf显示html页面生成的canvas高度;
                var pageHeight = contentWidth / 592.28 * 841.89;
                //未生成pdf的html页面高度
                var leftHeight = contentHeight;
                //页面偏移
                var position = 0;
                //a4纸的尺寸[595.28,841.89]，html页面生成的canvas在pdf中图片的宽高
                var imgWidth = 595.28;
                var imgHeight = 592.28 / contentWidth * contentHeight;
                var pageData = canvas.toDataURL('image/jpeg', 1.0);
                copyDom.remove();
                //注①
                var pdf = new jsPDF('', 'pt', 'a4');
                //有两个高度需要区分，一个是html页面的实际高度，和生成pdf的页面高度(841.89)
                //当内容未超过pdf一页显示的范围，无需分页
                if (leftHeight < pageHeight) {
                    pdf.addImage(pageData, 'JPEG', 0, 0, imgWidth, imgHeight);
                } else {
                    while (leftHeight > 0) {
                        //arg3-->距离左边距;arg4-->距离上边距;arg5-->宽度;arg6-->高度
                        pdf.addImage(pageData, 'JPEG', 0, position, imgWidth, imgHeight)
                        leftHeight -= pageHeight;
                        position -= 841.89;
                        //避免添加空白页
                        if (leftHeight > 0) {
                            //注②
                            pdf.addPage();
                        }
                    }
                }

                pdf.save('多维报表分析.pdf');
            }
        });

    }

    // 编辑模型
    var modelId = '${dataModelFavorite.modelId!}';
    if (modelId != '') {
        initModel(modelId);
        initIndex();
        initDimension();
        initFilterDataMap();
        showReport();
    }

    function initModel(modelId) {
        // 选择模型
        $('#modelSelect').find('option[modelId="'+modelId+'"]').selected();
        var code = $('#modelSelect').find('option[modelId="'+modelId+'"]').val();
        changeModelSelect(code);
    }

    function initIndex() {
        // 选择指标
        var indexParam = '${dataModelFavorite.queryIndex!}';
        var indexList = JSON.parse(indexParam);
        $.each(indexList, function (i, v) {
            $('.modelParam.index[paramName="'+v.name+'"]').prop('checked', true);
        });
    }

    function initDimension() {
        // 选择行维度
        var rowParam = '${dataModelFavorite.queryRow!}';
        var rowList = JSON.parse(rowParam);
        $.each(rowList, function (i, v) {
            $('.modelParam.dimension_row[paramId="'+v.paramId+'"]').prop('checked', true);
            dimensionRowParam.push(v.paramId);
        });
        // 选择列维度
        var columnParam = '${dataModelFavorite.queryColumn!}';
        var columnList = JSON.parse(columnParam);
        $.each(columnList, function (i, v) {
            $('.modelParam.dimension_column[paramId="'+v.paramId+'"]').prop('checked', true);
            dimensionColumnParam.push(v.paramId);
        });
    }

</script>
<script type="text/javascript">

    var dataTableJs = document.createElement('script');
    dataTableJs.type = 'text/javascript';
    dataTableJs.async = true;
    dataTableJs.src = '${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js';

    var html2canvas = document.createElement('script');
    html2canvas.type = 'text/javascript';
    html2canvas.async = true;
    html2canvas.src = 'https://cdnjs.cloudflare.com/ajax/libs/html2canvas/0.4.1/html2canvas.js';

    var jspdf = document.createElement('script');
    jspdf.type = 'text/javascript';
    jspdf.async = true;
    jspdf.src = 'https://cdnjs.cloudflare.com/ajax/libs/jspdf/1.0.272/jspdf.debug.js';

    var table2excel = document.createElement('script');
    table2excel.type = 'text/javascript';
    table2excel.async = true;
    table2excel.src = '${request.contextPath}/static/bigdata/js/jquery.table2excel.js';

    var $div = document.getElementById('modelViewDiv');
    $div.parentNode.insertBefore(dataTableJs, $div);
    $div.parentNode.insertBefore(html2canvas, $div);
    $div.parentNode.insertBefore(jspdf, $div);
    $div.parentNode.insertBefore(table2excel, $div);
</script>