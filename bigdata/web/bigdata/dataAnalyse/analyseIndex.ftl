<!--多维分析图标-->
<link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/css/mult/mult_iconfont.css"/>
<!--mult图标--->
<script src="${request.contextPath}/bigdata/v3/static/css/mult/iconfont.js" async="async" defer="defer"
        type="text/javascript" charset="utf-8"></script>
<!--可拖拽-->
<script src="${request.contextPath}/bigdata/v3/static/js/sorttable/Sortable.min.js" type="text/javascript"
        charset="utf-8"></script>
<style type="text/css">
    #report-table tr:nth-child(2n) {
        background-color: #FAFAFA
    }

    .dropdown-menu li {
        cursor: pointer;
    }
</style>

<div class="mult-content" id="mainModelDiv">
    <div class="mult-title">
        <img src="${request.contextPath}/bigdata/v3/static/images/mult/title.png">
        <span class="mult-title-span"><span>结果管理</span>&nbsp;<i class="iconfont icon-caret-down"></i></span>
    </div>

    <div class="mult-fun-body">
        <div class="mult-fun-li exportReport">
            <i class="mult-iconfont iconexport"></i>
            <span>导出</span>
        </div>
        <div class="mult-fun-li refreshData">
            <i class="mult-iconfont iconrefresh"></i>
            <span>刷新</span>
        </div>
        <div class="mult-fun-li saveBtn">
            <i class="mult-iconfont iconsave"></i>
            <span>另存</span>
        </div>
        <#--<div class="mult-fun-li authorization">-->
            <#--<i class="mult-iconfont iconauthorization"></i>-->
            <#--<span>授权</span>-->
        <#--</div>-->
        <div class="mult-fun-line"></div>
        <div class="mult-fun-li dimensionRotate">
            <i class="mult-iconfont iconrotating"></i>
            <span>旋转</span>
        </div>
        <div class="mult-fun-li dimensionWarning">
            <i class="mult-iconfont iconwarning"></i>
            <span>预警</span>
        </div>
        <div class="mult-fun-li clearAll">
            <i class="mult-iconfont iconempty"></i>
            <span>清空</span>
        </div>
        <div class="mult-fun-line"></div>
        <div class="mult-fun-li  mult-chart-change <#if dataModelFavorite.id?default('') == ''>active</#if><#if dataModelFavorite.isShowReport?default(0) == 1>active</#if>"
             data-id="1">
            <i class="mult-iconfont iconform"></i>
            <span>表格</span>
        </div>
        <div class="mult-fun-li mult-chart-change <#if dataModelFavorite.isShowChart?default(0) == 1>active</#if>"
             data-id="2">
            <i class="mult-iconfont iconhistogram"></i>
            <span>可视化</span>
        </div>
    </div>

    <div class="mult-box">
        <!--左半边--->
        <div class="mult-left-box">
            <div class="mult-left-title">
                <span>数据模型</span>
                <i class="mult-iconfont iconunfold"></i>
            </div>

            <div class="mult-left-select">
                <select name="" class="form-control no-radius" data-placeholder="选择数据模型" id="modelSelect">
                    <option value="">选择数据模型</option>
                    <#list models as item>
                        <option value="${item.code!}" modelId="${item.id!}" hasTimeDimension="${item.dateDimSwitch!}"
                                hasUserDataset="${item.userDatasetSwitch!}">${item.name!}</option>
                    </#list>
                </select>
                <select name="" class="form-control no-radius hide" data-placeholder="选择用户集" id="userDatasetSelect">
                </select>
            </div>

            <div class="mult-dimension">
                <div class="mult-canshu-title">维度</div>
                <div class="mult-canshu-body" id="dimension-box">
                </div>
            </div>

            <div class="mult-measure">
                <div class="mult-canshu-title">度量</div>
                <div class="mult-canshu-body" id="measure-box">
                </div>
            </div>

        </div>
        <!--左半边--->
        <!--右半边--->
        <div class="mult-right-box">
            <div class="mult-screen-box" id="screen-box">
                <div class="mult-no-content">
                    <i class="mult-iconfont iconinformation"></i>
                    <span>拖拽维度到此处作为页面参数</span>
                </div>
            </div>

            <div class="mult-screen-box hide" id="chart-screen-box">
                <div class="mult-no-content">
                    <i class="mult-iconfont iconinformation"></i>
                    <span>拖拽维度到此处作为页面参数</span>
                </div>
            </div>

            <!--表格-->
            <div class="mult-dynamic-box mult-show-one">
                <table class="table tables-border">
                    <tr>
                        <td class="mult-zuo-td"></td>
                        <td class="nopadding-td lie-td" style="height: 80px;">
                            <div class="mult-lie-box" id="lie-box" style="z-index: 1000;">
                                <div class="mult-no-content">
                                    <i class="mult-iconfont iconinformation"></i>
                                    <span>拖拽维度到此处作为页面参数</span>
                                </div>
                            </div>

                            <div class="mult-lie-table">
                                <div class="mult-lie-body">
                                    <table class="table tables-border" id="column-table">

                                    </table>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="nopadding-td hang-td" style="width: 200px;">
                            <div class="mult-hang-box" id="hang-box" style="z-index: 1000;">
                                <div class="mult-no-content">
                                    <i class="mult-iconfont iconinformation"></i>
                                    <span>拖拽维度到<br>此处作为页面参数</span>
                                </div>
                            </div>
                            <div class="mult-hang-table">
                                <div class="mult-hang-body">
                                    <table class="table tables-border" id="row-table">

                                    </table>
                                </div>
                            </div>
                        </td>
                        <td class="nopadding-td biao-td">
                            <div class="mult-biao-box" id="biao-box">
                                <div class="mult-no-content">
                                    <i class="mult-iconfont iconinformation"></i>
                                    <span>拖拽度量到此处作为页面参数</span>
                                </div>
                            </div>

                            <div class="mult-biao-table">
                                <div class="mult-biao-body">
                                    <table class="table tables-border" id="report-table">

                                    </table>
                                    <table class="table tables-border hide" id="temp-table">

                                    </table>
                                </div>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>

            <!--图表-->
            <div class="mult-dynamic-box clearfix mult-show-two" style="display: none;">
                <div class="box-left">
                    <div class="mult-type-choose">
                        <div class="change-place-box">
                            <i class="iconfont iconswap"></i>
                        </div>
                        <div class="mult-hang-box">
                            <div class="mult-canshu-li" data-id="1" draggable="false" style="">
                                <div class="mult-place">横轴</div>
                                <div class="btn-group fn-left">
                                    <button class="btn btn-primary" id="chart-dimension"><span>将维度拖到这里</span></button>
                                    <button class="btn btn-primary dropdown-toggle"></button>
                                    <ul class="dropdown-menu">
                                        <li class="chartFilter"><i class="mult-iconfont iconscreening"></i>筛选</li>
                                        <li class="active chart"><i class="mult-iconfont icondefault"></i>默认<i
                                                    class="mult-iconfont iconcorrect"></i></li>
                                        <li class="chart" type="asc"><i class="mult-iconfont iconascending"></i>升序<i
                                                    class="mult-iconfont iconcorrect"></i>
                                        </li>
                                        <li class="chart have-line" type="desc"><i
                                                    class="mult-iconfont icondescendingorder"></i>降序<i
                                                    class="mult-iconfont iconcorrect"></i></li>
                                        <li class="chartDimensionDelete chartDimension"><i
                                                    class="mult-iconfont icondelete"></i>删除
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="mult-hang-box">
                            <div class="mult-canshu-li" data-id="1" draggable="false" style="">
                                <div class="mult-place">纵轴</div>
                                <div class="btn-group fn-left">
                                    <button class="btn btn-primary" id="chart-measure">将度量拖到这里</button>
                                    <button class="btn btn-primary dropdown-toggle"></button>
                                    <ul class="dropdown-menu">
                                        <li class="chartDimensionDelete chartMeasure"><i
                                                    class="mult-iconfont icondelete"></i>删除
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="mult-hang-box">
                            <div class="mult-canshu-li" data-id="1" draggable="false" style="">
                                <div class="mult-place">图例</div>
                                <div class="btn-group fn-left">
                                    <button class="btn btn-primary" id="chart-legend">将维度拖到这里</button>
                                    <button class="btn btn-primary dropdown-toggle"></button>
                                    <ul class="dropdown-menu">
                                        <li class="chartFilter"><i class="mult-iconfont iconscreening"></i>筛选</li>
                                        <li class="active chart"><i class="mult-iconfont icondefault"></i>默认<i
                                                    class="mult-iconfont iconcorrect"></i></li>
                                        <li class="chart" type="asc"><i class="mult-iconfont iconascending"></i>升序<i
                                                    class="mult-iconfont iconcorrect"></i>
                                        </li>
                                        <li class="have-line chart" type="desc"><i
                                                    class="mult-iconfont icondescendingorder"></i>降序<i
                                                    class="mult-iconfont iconcorrect"></i></li>
                                        <li class="chartDimensionDelete chartLegend"><i
                                                    class="mult-iconfont icondelete"></i>删除
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>


                    </div>

                </div>

                <div class="box-right">
                    <div class="box-right-head">
                        <div class="pos-r">
                            <div class="chart-type active" type="bar">
                                <i class="iconfont iconhistogram"></i>
                                <div>柱状图</div>
                            </div>
                            <div class="chart-type" type="line">
                                <i class="iconfont iconlinechart"></i>
                                <div>折线图</div>
                            </div>
                            <div class="chart-type" type="cookie">
                                <i class="iconfont iconpie"></i>
                                <div>饼图</div>
                            </div>
                        </div>
                    </div>
                    <div class="box-right-content">
                        <div class="echart-box wrap-full pa-20" id="chartDiv">

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- /.col -->
    <div class="dropdown-modal">
        <ul class="dropdown-menu">
            <li style="cursor: pointer" class="have-line"><a hidefocus="true" id="measureDelete"><i
                            class="mult-iconfont icondelete"></i>删除</a>
            </li>
            <li style="cursor: pointer" class="dimensionWarning"><a hidefocus="true" id="warningMeasureId"><i
                            class="mult-iconfont iconwarning"></i>预警</a>
            </li>
        </ul>
    </div>

    <div class="dropdown-child" style="position: absolute;z-index: 101;">
        <ul class="dropdown-menu childDimension">
        </ul>
    </div>
</div><!-- /.row -->
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
<div class="layer layer-warning">
    <div class="earli-li">
        <div>
            <span class="earli-li-title">作用度量</span>
        </div>
        <select id="warningMeasure" class="form-control">
        </select>
    </div>
    <div class="earli-li">
        <div>
            <span class="earli-li-title">预警提示</span>
        </div>
        <select id="warningStyleSelect" class="form-control no-radius">
            <option value="1">交通灯</option>
            <option value="2">箭头</option>
        </select>
    </div>
    <div class="earli-li">
        <div class="earli-title">
            <span class="earli-li-title">条件</span>
            <a href="#" id="clearWarning">清除预警</a>
        </div>
        <div class="earli-basic">
            <div class="earli-left">
                <i class="earli-basic-img red1"></i>
                <span>当前值</span>
            </div>
            <div class="earli-right">
                <select id="firstSymbolSelect" name="" class="form-control no-radius">
                    <option value="&gt;=">&gt; =</option>
                    <option value="&gt;">&gt;</option>
                </select>
                <input class="form-control no-radius" type="text" id="num1" maxlength="20"
                       onkeyup="value=value.replace(/[^\d]/g,'') "
                       onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))">
            </div>
        </div>
        <div class="earli-basic">
            <div class="earli-left">
                <i class="earli-basic-img yellow1"></i>
                <span>当前值</span>
                <span class="earli-li-span">&lt; <span class="clone-num1">X</span> 且</span>
            </div>
            <div class="earli-right">
                <select id="secondSymbolSelect" name="" class="form-control no-radius">
                    <option value="&gt;=">&gt; =</option>
                    <option value="&gt;">&gt;</option>
                </select>
                <input class="form-control no-radius" type="text" id="num2" maxlength="20"
                       onkeyup="value=value.replace(/[^\d]/g,'') "
                       onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))">
            </div>
        </div>
        <div class="earli-basic">
            <div class="earli-left">
                <i class="earli-basic-img green1"></i>
                <span>当前值</span>
                <span class="earli-li-span">&lt; <span class="clone-num2">X</span></span>
            </div>
        </div>
    </div>
</div>
<div class="layer layer-save clearfix">
</div>
<div class="layer layer-reportList clearfix">
</div>
<div class="layer layer-authorization clearfix">
</div>
<!-- 错误弹窗 -->
<div class="js-error" style="display: none;">
    <div class="mult-tan-body">
        <i class="iconfont iconinformation"></i>
        <div>查询出错，请检查维度是否配置正确!</div>
    </div>
    <div class="mult-tan-content" id="errorDetail">

    </div>
</div>
<input type="hidden" id="favoriteId"
       value="<#if dataModelFavorite.isShowReport?default(0) == 1>${dataModelFavorite.id!}</#if>">
<input type="hidden" id="chartFavoriteId"
       value="<#if dataModelFavorite.isShowChart?default(0) == 1>${dataModelFavorite.id!}</#if>">
<script type="text/javascript">
    $(function () {
        $('#modelSelect').change(function () {
            changeModelSelect($(this).val());
        });
    });

    function changeModelSelect(code) {
        clearReport();
        clearChart();
        // 如果开启了时间维度
        var hasUserDataset = $('#modelSelect').find('option:selected').attr('hasUserDataset');
        if (hasUserDataset == '1') {
            $('#userDatasetSelect').removeClass('hide');
            var modelId = $('#modelSelect').find('option:selected').attr('modelId');
            $.ajax({
                url: '${request.contextPath}/bigdata/analyse/common/getUserDataset',
                data: {modelId: modelId},
                type: 'POST',
                dataType: 'json',
                success: function (response) {
                    if (!response.success) {
                        layer.msg(response.message, {icon: 2});
                    } else {
                        var data = response.data;
                        $('#userDatasetSelect').empty();
                        $.each(data, function (i, v) {
                            $('#userDatasetSelect').append('<option value="' + v.id + '">' + v.dsName + '</option>');
                        });
                    }
                }
            });
        } else {
            $('#userDatasetSelect').addClass('hide');
        }

        $('#dimension-box').empty();
        $('#measure-box').empty();
        $('#warningMeasure').empty();
        $.ajax({
            url: '${request.contextPath}/bigdata/analyse/common/getModelParam',
            data: {code: code, type: 'index'},
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    layer.msg(response.message, {icon: 2});
                } else {
                    var data = response.data;
                    $.each(data, function (i, v) {
                        if (v.isFilter != '1') {
                            var measure = '<div class="mult-canshu-li" data-id="' + v.id + '">\n' +
                                '                        <i class="mult-iconfont iconicon-test"></i>\n' +
                                '                        <span>' + v.name + '</span>\n' +
                                '                    </div>';
                            $('#measure-box').append(measure);
                            $('#warningMeasure').append('<option value="' + v.id + '">' + v.name + '</option>');
                        }
                    });
                }
            }
        });

        // 如果开启了时间维度
        var hasTime = $('#modelSelect').find('option:selected').attr('hasTimedimension');
        if (hasTime == '1') {
            $.ajax({
                url: '${request.contextPath}/bigdata/analyse/common/getModelParam',
                data: {code: 'date', type: 'dimension'},
                type: 'POST',
                dataType: 'json',
                success: function (response) {
                    if (!response.success) {
                        layer.msg(response.message, {icon: 2});
                    } else {
                        var data = response.data;
                        $.each(data, function (i, v) {
                            if (v.isFilter != '1') {
                                var dimension = '<div class="mult-canshu-li" data-id="' + v.id + '">\n' +
                                    '                        <i class="mult-iconfont iconABC"></i>\n' +
                                    '                        <span>' + v.name + '</span>\n' +
                                    '                    </div>';
                                $('#dimension-box').append(dimension);
                            }
                        });
                    }
                }
            });
        }

        $.ajax({
            url: '${request.contextPath}/bigdata/analyse/common/getModelParam',
            data: {code: code, type: 'dimension'},
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    layer.msg(response.message, {icon: 2});
                } else {
                    var data = response.data;
                    var img = '${request.contextPath}/static/bigdata/images/filtrate.png';
                    $.each(data, function (i, v) {
                        if (v.isFilter != '1') {
                            var dimension = '<div class="mult-canshu-li" data-id="' + v.id + '">\n' +
                                '                        <i class="mult-iconfont iconABC"></i>\n' +
                                '                        <span>' + v.name + '</span>\n' +
                                '                    </div>';
                            $('#dimension-box').append(dimension);
                        }
                    });
                }
            }
        });
    }
</script>
<script type="text/javascript">

    // ---------------------------------报表---------------------------------------
    var dimensionBox = document.getElementById('dimension-box');
    var measureBox = document.getElementById('measure-box');
    var screenBox = document.getElementById('screen-box');
    var lieBox = document.getElementById('lie-box');
    var hangBox = document.getElementById('hang-box');
    var biaoBox = document.getElementById('biao-box');

    // 列维度
    var columnDimensionArray = [];
    // 行维度
    var rowDimensionArray = [];
    // 指标
    var measureArray = [];
    // 筛选条件
    var filterDataMap = {};
    // 已选择的筛选维度
    var filterDimensionArray = [];
    // 报表字段排序
    var orderDataMap = {};
    // 下钻过滤参数
    var childDimensionFilterMap = {};
    // 下钻索引
    var childRowIndex;
    var childColumnIndex;
    var expandRowIndex;
    // 父级位置
    var parentRowIndex;
    var parentColumnIndex;
    // 下钻位置 row column
    var childPosition;
    // 当前下钻图标
    var childIcon;
    // 预警数据
    var warningDataMap = {};

    var paramNameMap = {};

    // --------------------------------图表---------------------------------------------

    var chartDimensionBox = document.getElementById('chart-dimension');
    var chartMeasureBox = document.getElementById('chart-measure');
    var chartLegend = document.getElementById('chart-legend');
    var chartFilterBox = document.getElementById('chart-screen-box');

    // 指标集合
    var chartMeasureArray = [];
    // 筛选条件
    var chartFilterDataMap = {};
    // 已选择的筛选维度
    var chartFilterDimensionArray = [];
    // 图表字段排序
    var chartOrderDataMap = {};
    // 编辑模型
    var modelId = '${dataModelFavorite.modelId!}';

    initParamNameMap();

    if (modelId != '') {
        initModel(modelId);
        if ('${dataModelFavorite.isShowReport?default(0)}' == '1') {

            $(".mult-show-one").show();
            $(".mult-show-two").hide();
            $('#chart-screen-box').addClass('hide');
            $('#screen-box').removeClass('hide');

            initMeasure();
            initDimension();
            initFilterDataMap();
            initWarningDataMap();
            loadFilterReport();
        } else if ('${dataModelFavorite.isShowChart?default(0)}' == '1') {

            $(".mult-show-one").hide();
            $(".mult-show-two").show();
            $('#chart-screen-box').removeClass('hide');
            $('#screen-box').addClass('hide');

            initChartMeasure();
            initChartDimension();
            initChartLegend();
            initChartFilterDataMap();
            initChartType();
            loadChart();
        }
    }

    function initChartType() {
        var chartType = '${dataModelFavorite.chartType?default('line')}';
        $('.chart-type').removeClass('active');
        $('.chart-type[type="' + chartType + '"]').addClass('active');
    }

    function initParamNameMap() {
        var paramNames = '${paramNameMap!}';
        if (paramNames != '{}') {
            paramNameMap = JSON.parse(paramNames);
        }
    }

    function initModel(modelId) {
        // 选择模型
        $('#modelSelect').find('option[modelId="' + modelId + '"]').selected();
        var code = $('#modelSelect').find('option[modelId="' + modelId + '"]').val();
        changeModelSelect(code);
    }

    function initMeasure() {
        // 选择指标
        var measureParam = '${paramMap["index"]!}';
        if (measureParam != 'null' && measureParam != '' && measureParam != '[]') {
            $("#biao-box").find(".mult-no-content").addClass('hide');
            measureArray = JSON.parse(measureParam);
            $.each(measureArray, function (i, v) {
                var measureDiv = '<div class="mult-canshu-li" data-id="' + v + '" draggable="false" style="transform: translateZ(0px);">\n' +
                    '                        <i class="mult-iconfont iconicon-test"></i>\n' +
                    '                        <span>' + paramNameMap[v] + '</span>\n' +
                    '                    </div>';
                $('#biao-box').append(measureDiv);
            });
        }
    }

    function initChartMeasure() {
        // 选择指标
        var measureParam = '${paramMap["index"]!}';
        if (measureParam != 'null' && measureParam != '' && measureParam != '[]') {
            chartMeasureArray = JSON.parse(measureParam);
            $.each(chartMeasureArray, function (i, v) {
                $('#chart-measure').text(paramNameMap[v]).attr('data-id', v);
                $('#chart-measure').css('cssText', 'border: 1px solid rgb(0, 204, 227); z-index: 1;');
            });
        }
    }

    function initDimension() {
        // 选择行维度
        var rowParam = '${paramMap["row"]!}';
        if (rowParam != 'null' && rowParam != '' && rowParam != '[]') {
            $("#hang-box").find(".mult-no-content").addClass('hide');
            rowDimensionArray = JSON.parse(rowParam);
            $.each(rowDimensionArray, function (i, v) {
                var itemname = paramNameMap[v];
                var rowDiv = '<div class="mult-canshu-li" data-id="' + v + '" draggable="false" style=""><div id="' + v + '" class="btn-group fn-left">\n' +
                    '                              <button class="btn btn-primary">' + itemname + '</button>\n' +
                    '                              <button class="btn btn-primary dropdown-toggle"></button>\n' +
                    '                              <ul class="dropdown-menu">\n' +
                    '                                <li onclick="filterDimension(\'' + v + '\')"><i class="mult-iconfont iconscreening"></i>筛选</li>\n' +
                    '                                <li class="have-line dimensionWarning"><i class="mult-iconfont iconwarning"></i>预警</li>\n' +
                    '                                <li class="active"><i class="mult-iconfont icondefault"></i>默认<i class="mult-iconfont iconcorrect"></i></li>\n' +
                    '                                <li type="asc"><i class="mult-iconfont iconascending"></i>升序<i class="mult-iconfont iconcorrect"></i></li>\n' +
                    '                                <li class="have-line" type="desc"><i class="mult-iconfont icondescendingorder"></i>降序<i class="mult-iconfont iconcorrect"></i></li>\n' +
                    '                                <li onclick="deleteDimension(\'' + v + '\')"><i class="mult-iconfont icondelete"></i>删除</li>\n' +
                    '                              </ul>\n' +
                    '                            </div></div>';

                $('#hang-box').append(rowDiv);
            });
        }
        var columnParam = '${paramMap["column"]!}';

        if (columnParam != 'null' && columnParam != '' && columnParam != '[]') {
            // 选择列维度
            columnDimensionArray = JSON.parse(columnParam);
            $("#lie-box").find(".mult-no-content").addClass('hide');
            $.each(columnDimensionArray, function (i, v) {
                var itemname = paramNameMap[v];
                var rowDiv = '<div class="mult-canshu-li" data-id="' + v + '" draggable="false" style=""><div id="' + v + '" class="btn-group fn-left">\n' +
                    '                              <button class="btn btn-primary">' + itemname + '</button>\n' +
                    '                              <button class="btn btn-primary dropdown-toggle"></button>\n' +
                    '                              <ul class="dropdown-menu">\n' +
                    '                                <li onclick="filterDimension(\'' + v + '\')"><i class="mult-iconfont iconscreening"></i>筛选</li>\n' +
                    '                                <li class="have-line dimensionWarning"><i class="mult-iconfont iconwarning"></i>预警</li>\n' +
                    '                                <li class="active"><i class="mult-iconfont icondefault"></i>默认<i class="mult-iconfont iconcorrect"></i></li>\n' +
                    '                                <li type="asc"><i class="mult-iconfont iconascending"></i>升序<i class="mult-iconfont iconcorrect"></i></li>\n' +
                    '                                <li class="have-line" type="desc"><i class="mult-iconfont icondescendingorder"></i>降序<i class="mult-iconfont iconcorrect"></i></li>\n' +
                    '                                <li onclick="deleteDimension(\'' + v + '\')"><i class="mult-iconfont icondelete"></i>删除</li>\n' +
                    '                              </ul>\n' +
                    '                            </div></div>';

                $('#lie-box').append(rowDiv);
            });
        }
    }

    function initChartDimension() {
        // 选择行维度
        var rowParam = '${paramMap["chartDimensionId"]!}';
        if (rowParam != 'null' && rowParam != '' && rowParam != '[]') {
            $('#chart-dimension').text(paramNameMap[rowParam]).attr('data-id', rowParam);
            $('#chart-dimension').css('cssText', 'border: 1px solid rgb(0, 204, 227); z-index: 1;');
        }
    }

    function initChartLegend() {
        // 选择指标
        var chartLegendId = '${paramMap["chartLegendId"]!}';
        if (chartLegendId != 'null' && chartLegendId != '' && chartLegendId != '[]') {
            $('#chart-legend').text(paramNameMap[chartLegendId]).attr('data-id', chartLegendId);
            $('#chart-legend').css('cssText', 'border: 1px solid rgb(0, 204, 227); z-index: 1;');
        }
    }

    function initFilterDataMap() {
        var filterData = '${paramMap["filterDataMap"]!}';
        if (filterData != 'null' && filterData != '' && filterData != '{}') {
            $("#screen-box").find(".mult-no-content").addClass('hide');
            filterDataMap = JSON.parse(filterData);
            $.each(filterDataMap, function (k, v) {
                filterDimensionArray.push(k);
                var itemName = paramNameMap[k];
                var filterDiv = '<div class="mult-canshu-li" data-id="' + k + '" draggable="false" style="transform: none;" id="filter_' + k + '">\n' +
                    '                        <i class="mult-iconfont iconABC"></i>\n' +
                    '                        <span onclick="filterUI(\'' + k + '\')" name="' + itemName + '">' + itemName + '(' + (v.length > 0 ? v.join(",") : '无') + ')</span>\n' +
                    '                    <i class="fa fa-close" onclick="deleteFilter(\'' + k + '\')"></i></div>';
                $('#screen-box').append(filterDiv);
            })
        }
    }

    function initChartFilterDataMap() {
        var filterData = '${paramMap["filterDataMap"]!}';
        if (filterData != 'null' && filterData != '' && filterData != '{}') {
            $("#chart-screen-box").find(".mult-no-content").addClass('hide');
            chartFilterDataMap = JSON.parse(filterData);
            $.each(chartFilterDataMap, function (k, v) {
                chartFilterDimensionArray.push(k);
                var itemName = paramNameMap[k];
                var filterDiv = '<div class="mult-canshu-li" data-id="' + k + '" draggable="false" style="transform: none;" id="chart_filter_' + k + '">\n' +
                    '                        <i class="mult-iconfont iconABC"></i>\n' +
                    '                        <span onclick="chartFilterUI(\'' + k + '\')" name="' + itemName + '">' + itemName + '(' + (v.length > 0 ? v.join(",") : '无') + ')</span>\n' +
                    '                    <i class="fa fa-close" onclick="deleteChartFilter(\'' + k + '\')"></i></div>';
                $('#chart-screen-box').append(filterDiv);
            })
        }
    }

    function initWarningDataMap() {
        var warningData = '${paramMap["warningDataMap"]?default('')}';
        if (warningData != 'null' && warningData != '' && warningData != '{}') {
            warningDataMap = JSON.parse(warningData);
            $.each(warningDataMap, function (k, v) {
                $('#firstSymbolSelect').val(v[0]);
                $('#secondSymbolSelect').val(v[1]);
                $('#num1').val(v[2]);
                $('#num2').val(v[3]);
                $('.clone-num1').empty().text(v[2]);
                $('.clone-num2').empty().text(v[3]);
                $('#warningStyleSelect').val(v[4]);
            })
        }
    }

    new Sortable(dimensionBox, {
        group: {
            name: 'shared1',
            pull: 'clone',
            put: false // Do not allow items to be put into this list
        },
        animation: 150,
        sort: false,// To disable sorting: set sort to false
        onMove: function (evt) {
            $("#screen-box").css("border", "0");
            $("#lie-box").css("border", "0");
            $("#chart-screen-box").css("border", "0");
            $("#hang-box").css("border", "0");
            $(evt.to).css({
                "border": "1px solid #00CCE3",
                'z-index': 1
            });
        },
        onEnd: function (evt) {
            $("#screen-box").css("border", "0");
            $("#lie-box").css("border", "0");
            $("#chart-screen-box").css("border", "0");
            $("#hang-box").css("border", "0");
            setTimeout(function () {
                $(evt.item).css("transform", "none");
            }, 100);
        }
    });

    new Sortable(measureBox, {
        group: {
            name: 'shared2',
            pull: 'clone',
            put: false // Do not allow items to be put into this list
        },
        animation: 150,
        sort: false,// To disable sorting: set sort to false
        onMove: function (evt) {
            $("#biao-box").css("border", "0");
            $(evt.to).css({
                "border": "1px solid #00CCE3",
                'z-index': 1
            });
        },
        onEnd: function (evt) {
            $("#biao-box").css("border", "0");
        }
    });

    new Sortable(screenBox, {
        group: {
            name: 'shared1',
            pull: false,
        },
        animation: 150,
        onAdd: function (evt) {
            var dimensionId = $(evt.item).attr("data-id");
            if (filterDimensionArray.indexOf(dimensionId) > -1) {
                $(evt.item).remove();
                //来个错误提示
                showLayerTips4Confirm('error', '该筛选维度已存在!');
            } else {
                $(evt.item).attr('id', 'filter_' + dimensionId);
                $(evt.item).find('span').attr('onclick', 'filterUI(\'' + dimensionId + '\')');
                var dimensionName = $(evt.item).find('span').text();
                $(evt.item).find('span').attr('name', dimensionName).text(dimensionName + '(无)');
                $(evt.item).append('<i class="fa fa-close" onclick="deleteFilter(\'' + dimensionId + '\')"></i>');
                $("#screen-box").find(".mult-no-content").addClass('hide');
                filterDimensionArray.push(dimensionId);
                filterUI(dimensionId);
            }
        },
        //列表单元在列表容器中的排序发生变化后的回调函数
        onUpdate: function (evt) {
            //数据更新啥的
        },
        onEnd: function (evt) {
            setTimeout(function () {
                $(evt.item).css("transform", "none");
            }, 100)
        }
    });

    function deleteFilter(dimensionId) {
        var index = filterDimensionArray.indexOf(dimensionId);
        if (index > -1) {
            filterDimensionArray.splice(index, 1);
        }
        if (filterDataMap[dimensionId] != null) {
            delete filterDataMap[dimensionId];
            if (measureArray.length > 0 && rowDimensionArray.length < 1 && columnDimensionArray.length < 1) {
                loadReportTotal();
            } else {
                loadFilterReport();
            }
        }
        $('#filter_' + dimensionId).remove();
        if (filterDimensionArray.length == 0) {
            $("#screen-box").find(".mult-no-content").removeClass('hide');
        }
    }

    //列的拖拽
    new Sortable(lieBox, {
        group: {
            name: 'shared1',
            pull: false,
        },
        animation: 150,
        onAdd: function (evt) {
            var lineId = $(evt.item).attr("data-id");
            if (isRepeat(lineId)) {
                $(evt.item).remove();
                //来个错误提示
                showLayerTips4Confirm('error', '该维度已存在!');
            } else {
                columnDimensionArray = [];
                $.each($('#lie-box').find('.mult-canshu-li'), function (i, v) {
                    columnDimensionArray.push($(this).attr('data-id'));
                });

                $(".lie-td").css("height", "auto");
                $("#lie-box").find(".mult-no-content").addClass('hide');
                $(evt.item).find('i').remove();
                let itemname = $(evt.item).find('span').html();
                $(evt.item).html('<div id="' + lineId + '" class="btn-group fn-left">\n' +
                    '                              <button class="btn btn-primary">' + itemname + '</button>\n' +
                    '                              <button class="btn btn-primary dropdown-toggle"></button>\n' +
                    '                              <ul class="dropdown-menu">\n' +
                    '                                <li onclick="filterDimension(\'' + lineId + '\')"><i class="mult-iconfont iconscreening"></i>筛选</li>\n' +
                    '                                <li class="have-line dimensionWarning"><i class="mult-iconfont iconwarning"></i>预警</li>\n' +
                    '                                <li class="active"><i class="mult-iconfont icondefault"></i>默认<i class="mult-iconfont iconcorrect"></i></li>\n' +
                    '                                <li type="asc"><i class="mult-iconfont iconascending"></i>升序<i class="mult-iconfont iconcorrect"></i></li>\n' +
                    '                                <li class="have-line" type="desc"><i class="mult-iconfont icondescendingorder"></i>降序<i class="mult-iconfont iconcorrect"></i></li>\n' +
                    '                                <li onclick="deleteDimension(\'' + lineId + '\')"><i class="mult-iconfont icondelete"></i>删除</li>\n' +
                    '                              </ul>\n' +
                    '                            </div>');

                loadFilterReport();
            }
        },
        //列表单元在列表容器中的排序发生变化后的回调函数
        onUpdate: function (evt) {
            //顺序发生变化
            columnDimensionArray = [];
            $.each($('#lie-box').find('.mult-canshu-li'), function (i, v) {
                columnDimensionArray.push($(this).attr('data-id'));
            });
            loadFilterReport();
        },
        onEnd: function (evt) {
            setTimeout(function () {
                $(evt.item).css("transform", "none");
            }, 100)
        }
    });

    //行表格的拖拽
    new Sortable(hangBox, {
        group: {
            name: 'shared1',
            pull: false,
        },
        animation: 150,
        onAdd: function (evt) {
            var rowId = $(evt.item).attr("data-id");
            if (isRepeat(rowId)) {
                $(evt.item).remove();
                //来个错误提示
                showLayerTips4Confirm('error', '该维度已存在!');
            } else {
                rowDimensionArray = [];
                $.each($('#hang-box').find('.mult-canshu-li'), function (i, v) {
                    rowDimensionArray.push($(this).attr('data-id'));
                })

                $("#hang-box").find(".mult-no-content").addClass('hide');

                $(".hang-td").css("width", "auto");
                $(evt.item).find('i').remove();
                let itemname = $(evt.item).find('span').html();
                $(evt.item).html('<div id="' + rowId + '" class="btn-group fn-left">\n                              ' +
                    '<button class="btn btn-primary">' + itemname + '</button>\n                              ' +
                    '<button class="btn btn-primary dropdown-toggle"></button>\n                              ' +
                    '<ul class="dropdown-menu">\n                                <li onclick="filterDimension(\'' + rowId + '\')">' +
                    '<i class="mult-iconfont iconscreening"></i>筛选</li>\n                               ' +
                    ' <li class="have-line dimensionWarning">' +
                    '<i class="mult-iconfont iconwarning"></i>预警</li>\n                                ' +
                    '<li class="active"><i class="mult-iconfont icondefault"></i>默认' +
                    '<i class="mult-iconfont iconcorrect"></i></li>\n                                <li type="asc">' +
                    '<i class="mult-iconfont iconascending"></i>升序<i class="mult-iconfont iconcorrect"></i></li>\n  ' +
                    '                              <li class="have-line" type="desc"><i class="mult-iconfont icondescendingorder desc"></i>降序' +
                    '<i class="mult-iconfont iconcorrect"></i>' +
                    '  <li onclick="deleteDimension(\'' + rowId + '\')"><i class="mult-iconfont icondelete"></i>删除</li>\n                              </ul>\n ' +
                    '                           </div>');

                loadFilterReport();
            }
        },
        //列表单元在列表容器中的排序发生变化后的回调函数
        onUpdate: function (evt) {
            //顺序发生变化
            rowDimensionArray = [];
            $.each($('#hang-box').find('.mult-canshu-li'), function (i, v) {
                rowDimensionArray.push($(this).attr('data-id'));
            })
            loadFilterReport();
        },
        onEnd: function (evt) {
            setTimeout(function () {
                $(evt.item).css("transform", "none");
            }, 100)
        }
    });

    //数据表格的拖拽
    new Sortable(biaoBox, {
        group: {
            name: 'shared2',
            pull: false,
        },
        animation: 150,
        onAdd: function (evt) {
            var measureId = $(evt.item).attr("data-id");
            if (isRepeat(measureId)) {
                $(evt.item).remove();
                //来个错误提示
                showLayerTips4Confirm('error', '该指标已存在!');
            } else {
                $("#biao-box").find(".mult-no-content").addClass('hide');
                measureArray.push($(evt.item).attr("data-id"));
                // 如果只选择了指标 显示合计
                if (rowDimensionArray.length < 1 && columnDimensionArray.length < 1 && measureArray.length > 0) {
                    loadReportTotal();
                } else
                    loadFilterReport();
            }
        },
        //列表单元在列表容器中的排序发生变化后的回调函数
        onUpdate: function (evt) {
            //数据更新啥的
        }
    });

    function loadFilterReport() {

        if (rowDimensionArray.length < 1 && columnDimensionArray.length < 1 && measureArray.length < 1) {
            return;
        }


        if (rowDimensionArray.length < 1 && columnDimensionArray.length < 1 && measureArray.length > 0) {
            loadReportTotal();
            return;
        }
        //加载层
        var index = layer.msg('正在计算,请稍等......', {
            icon: 16,
            time: 0,
            shade: 0.01
        });
        var modelId = $('#modelSelect').find('option:selected').attr('modelId');
        $.ajax({
            url: '${request.contextPath}/bigdata/data/analyse/queryReportAll',
            data: {
                dimensionColumnParam: columnDimensionArray,
                dimensionRowParam: rowDimensionArray,
                measureParam: measureArray,
                modelId: modelId,
                filterDataMap: JSON.stringify(filterDataMap),
                warningDataMap: JSON.stringify(warningDataMap),
                orderDataMap: JSON.stringify(orderDataMap)
            },
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    showError(response.message);
                } else {
                    $("#report-table").html(response.data.table);
                    $('#row-table').html(response.data.rowHeader);
                    $('#column-table').html(response.data.columnHeader);
                    uniteTdCells('row-table');
                    uniteTrCells('column-table');
                    $('#biao-box').find('.mult-no-content').addClass('hide');
                    $('#hang-box').find('.mult-no-content').addClass('hide');

                    if ($('#warningStyleSelect').val() == '1') {
                        $('#report-table td.red_warning').find('span').after('<i class="mult-red1"></i>');
                        $('#report-table td.yellow_warning').find('span').after('<i class="mult-yellow1"></i>');
                        $('#report-table td.green_warning').find('span').after('<i class="mult-green1"></i>');
                    } else {
                        $('#report-table td.red_warning').find('span').after('<i class="mult-red2"></i>');
                        $('#report-table td.yellow_warning').find('span').after('<i class="mult-yellow2"></i>');
                        $('#report-table td.green_warning').find('span').after('<i class="mult-green2"></i>');
                    }

                    allresize();
                }
                layer.close(index);
            }
        });
    }

    function loadReportTotal() {
        //加载层
        var index = layer.msg('正在计算,请稍等......', {
            icon: 16,
            time: 0,
            shade: 0.01
        });
        var modelId = $('#modelSelect').find('option:selected').attr('modelId');
        $.ajax({
            url: '${request.contextPath}/bigdata/data/analyse/queryReportTotal',
            data: {
                measureParam: measureArray,
                filterDataMap: JSON.stringify(filterDataMap),
                warningDataMap: JSON.stringify(warningDataMap),
                modelId: modelId
            },
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    allresize();
                    showError(response.message);
                } else {
                    $(".mult-hang-box").find(".mult-no-content").addClass('hide');
                    $('#row-table').empty().append('<tr><th>合计</th></tr>');
                    var tr = '<tr>';
                    // 获取指标
                    $.each($('#biao-box').find('.mult-canshu-li'), function (i, v) {
                        tr = tr + '<th class="measure" data-id="' + $(this).attr('data-id') + '">' + $(this).find('span').text() + '</th>';
                    });
                    tr = tr + '</tr>';
                    $('#column-table').empty().append(tr);

                    $("#report-table").html(response.data);

                    if ($('#warningStyleSelect').val() == '1') {
                        $('#report-table td.red_warning').find('span').after('<i class="mult-red1"></i>');
                        $('#report-table td.yellow_warning').find('span').after('<i class="mult-yellow1"></i>');
                        $('#report-table td.green_warning').find('span').after('<i class="mult-green1"></i>');
                    } else {
                        $('#report-table td.red_warning').find('span').after('<i class="mult-red2"></i>');
                        $('#report-table td.yellow_warning').find('span').after('<i class="mult-yellow2"></i>');
                        $('#report-table td.green_warning').find('span').after('<i class="mult-green2"></i>');
                    }
                    allresize();
                }
                layer.close(index);
            }
        });
    }

    function deleteDimension(id) {

        // 删除排序字段
        if (orderDataMap[id] != null) {
            delete orderDataMap[id];
        }

        var lineIndex = columnDimensionArray.indexOf(id);
        if (lineIndex > -1) {
            columnDimensionArray.splice(lineIndex, 1);
        }

        var rowIndex = rowDimensionArray.indexOf(id);
        if (rowIndex > -1) {
            rowDimensionArray.splice(rowIndex, 1);
        }

        if (columnDimensionArray.length < 1) {
            $('#lie-box').find('.mult-no-content').removeClass('hide');
        }

        if (rowDimensionArray.length < 1 && columnDimensionArray.length < 1 && measureArray.length > 0) {
            loadReportTotal();
        } else if (rowDimensionArray.length < 1 && columnDimensionArray.length < 1 && measureArray.length < 1) {
            clearReport();
        } else {
            loadFilterReport();
        }


        $('#' + id).parent().remove();
    }

    //已选
    var num = 0;

    function filterUI(dimensionId) {
        // 判断维度有没有选择
        $('#filterParamId').val(dimensionId);
        var $text = $('#filter_' + dimensionId).find('span').attr('name');
        $('#filterDataDiv').empty();
        $('.js-choice-target').empty();
        $('.choose-num>span').text(0);
        $('.js-fullChioce').find('input').prop('checked', false);

        var modelId = $('#modelSelect').find('option:selected').attr('modelId');
        // 查询过滤数据
        $.ajax({
            url: '${request.contextPath}/bigdata/data/analyse/getFilterData',
            data: {
                modelId: modelId,
                dimensionId: dimensionId,
                modelDatasetId: $('#userDataSelect').val()
            },
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    showError(response.message);
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
                }
            }
        });

        layer.open({
            type: 1,
            shade: .6,
            title: $text,
            btn: ['确定', '取消'],
            area: ['450px', '500px'],
            yes: function () {
                var array = [];
                var items = $('#filterDataDiv').find('.filterItem');
                $.each(items, function (i, v) {
                    if ($(v).is(":checked")) {
                        array.push($(v).next().text());
                    }
                });
                filterDataMap[dimensionId] = array;
                if (array.length > 0) {
                    $('#filter_' + dimensionId).find('span').text($text + '(' + array.join(',') + ')');

                    if (rowDimensionArray.length > 0 || columnDimensionArray.length > 0 || measureArray.length > 0) {
                        if (measureArray.length > 0 && rowDimensionArray.length < 1 && columnDimensionArray.length < 1) {
                            loadReportTotal();
                        } else {
                            loadFilterReport();
                        }
                    }
                } else {
                    $('#filter_' + dimensionId).find('span').text($text + '(无)');
                    if (filterDataMap[dimensionId] != null) {
                        delete filterDataMap[dimensionId];

                        if (rowDimensionArray.length > 0 || columnDimensionArray.length > 0 || measureArray.length > 0) {
                            if (measureArray.length > 0 && rowDimensionArray.length < 1 && columnDimensionArray.length < 1) {
                                loadReportTotal();
                            } else {
                                loadFilterReport();
                            }
                        }
                    }
                }
                layer.closeAll();
            },
            cancel: function () {
                layer.closeAll();
            },
            content: $('.layer-filtrate')
        });
    }

    function filterDimension(dimensionId) {

        if ($('#screen-box').find('.mult-canshu-li[id="' + 'filter_' + dimensionId + '"]').length < 1) {
            console.info($('#dimension-box').find('.mult-canshu-li[data-id="' + dimensionId + '"]'));
            $('#dimension-box').find('.mult-canshu-li[data-id="' + dimensionId + '"]').clone().attr('id', 'filter_' + dimensionId).appendTo($('#screen-box'));

            $('#filter_' + dimensionId).find('span').attr('onclick', 'filterUI(\'' + dimensionId + '\')');
            var dimensionName = $('#filter_' + dimensionId).find('span').text();
            $('#filter_' + dimensionId).find('span').attr('name', dimensionName).text(dimensionName + '(无)');
            $('#filter_' + dimensionId).append('<i class="fa fa-close" onclick="deleteFilter(\'' + dimensionId + '\')"></i>');
            $("#screen-box").find(".mult-no-content").addClass('hide');
            filterDimensionArray.push(dimensionId);
        }

        filterUI(dimensionId);
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

    function refreshTable() {
        $("#column-table").empty();
        $('#row-table').empty();
        $('#report-table').empty();
        $(".mult-hang-box").find(".mult-no-content").removeClass('hide');
        $(".mult-biao-box").find(".mult-no-content").removeClass('hide');
        $(".mult-lie-box").find(".mult-no-content").removeClass('hide');
        allresize();
    }

    function uniteTrCells(tableId) {
        var table = document.getElementById(tableId);
        if (table.rows.length > 1) {
            // 遍历每一行
            for (i = 0; i < table.rows.length - 1; i++) {
                // 遍历每一列
                for (c = 0; c < table.rows[i].cells.length - 1; c++) {

                    for (d = c + 1; d < table.rows[i].cells.length; d++) {

                        var cell1 = table.rows[i].cells[c].innerHTML;
                        var cell2 = table.rows[i].cells[d].innerHTML;
                        if (cell1 == cell2) {
                            if (i > 0) {
                                var p1 = table.rows[i - 1].cells[c].innerHTML;
                                var p2 = table.rows[i - 1].cells[d].innerHTML;
                                var isNone = table.rows[i - 1].cells[d].style.display;

                                if (p1 == p2 && isNone == 'none') {
                                    table.rows[i].cells[d].style.display = 'none';
                                    table.rows[i].cells[d].className = 'noExport';
                                    table.rows[i].cells[c].colSpan++;
                                } else break;
                            } else {
                                table.rows[i].cells[d].style.display = 'none';
                                table.rows[i].cells[d].className = 'noExport';
                                table.rows[i].cells[c].colSpan++;
                            }
                        } else break;
                    }

                }
            }
        }
    }

    function uniteTdCells(tableId) {
        var table = document.getElementById(tableId);
        if (table.rows.length > 1) {
            for (c = 0; c < table.rows[0].cells.length - 1; c++) {
                for (i = 0; i < table.rows.length - 1; i++) {
                    for (j = i + 1; j < table.rows.length; j++) {
                        var cell1 = table.rows[i].cells[c].innerHTML;
                        var cell2 = table.rows[j].cells[c].innerHTML;
                        if (cell1 == cell2) {
                            if (c > 0) {
                                if (j - i >= 1) {
                                    var p1 = table.rows[j].cells[c - 1].innerHTML;
                                    var p2 = table.rows[i].cells[c - 1].innerHTML;
                                    var isNone = table.rows[j].cells[c - 1].style.display;
                                    if (p1 == p2 && isNone == 'none') {
                                        table.rows[j].cells[c].style.display = 'none';
                                        table.rows[j].cells[c].className = 'noExport';
                                        table.rows[i].cells[c].rowSpan++;
                                    } else break;
                                } else {
                                    table.rows[j].cells[c].style.display = 'none';
                                    table.rows[j].cells[c].className = 'noExport';
                                    table.rows[i].cells[c].rowSpan++;
                                }
                            } else {
                                table.rows[j].cells[c].style.display = 'none';
                                table.rows[j].cells[c].className = 'noExport';
                                table.rows[i].cells[c].rowSpan++;
                            }
                        } else break;
                    }
                }
            }
        }
    }

    function isRepeat(data_id) {
        if (rowDimensionArray.indexOf(data_id) > -1) {
            return true;
        }

        if (columnDimensionArray.indexOf(data_id) > -1) {
            return true;
        }

        if (measureArray.indexOf(data_id) > -1) {
            return true;
        }
        return false;
    }

    // 横轴维度
    new Sortable(chartDimensionBox, {
        group: {
            name: 'shared1',
            pull: false,
        },
        animation: 150,
        onAdd: function (evt) {
            var dimensionId = $(evt.item).attr('data-id');
            if (dimensionId == $('#chart-legend').attr('data-id')) {
                showLayerTips4Confirm('error', '您拖放的维度已存在图例项中，不能拖放!');
                return;
            }
            $(evt.target).text($(evt.clone).find("span").text()).attr('data-id', dimensionId);
            loadChart();
        }
    });
    // 纵轴指标
    new Sortable(chartMeasureBox, {
        group: {
            name: 'shared2',
            pull: false,
        },
        animation: 150,
        onAdd: function (evt) {
            var measureId = $(evt.item).attr('data-id');
            if (chartMeasureArray.indexOf(measureId) > -1) {
                $(evt.item).remove();
                //来个错误提示
            } else {
                chartMeasureArray = [];
                chartMeasureArray.push(measureId);
                $(evt.target).text($(evt.clone).find("span").text());
                loadChart();
            }
        }
    });
    // 图例维度
    new Sortable(chartLegend, {
        group: {
            name: 'shared1',
            pull: false,
        },
        animation: 150,
        onAdd: function (evt) {
            var dimensionId = $(evt.item).attr('data-id');
            if (dimensionId == $('#chart-dimension').attr('data-id')) {
                showLayerTips4Confirm('error', '您拖放的维度已存在于横轴中，不能拖放!');
                return;
            }
            $(evt.target).text($(evt.clone).find("span").text()).attr('data-id', $(evt.item).attr('data-id'));
            loadChart();
        }
    });
    // 筛选
    new Sortable(chartFilterBox, {
        group: {
            name: 'shared1',
            pull: false,
        },
        animation: 150,
        onAdd: function (evt) {
            var dimensionId = $(evt.item).attr("data-id");
            if (chartFilterDimensionArray.indexOf(dimensionId) > -1) {
                $(evt.item).remove();
                //来个错误提示
                showLayerTips4Confirm('error', '该筛选维度已存在!');
            } else {
                $(evt.item).attr('id', 'chart_filter_' + dimensionId);
                $(evt.item).find('span').attr('onclick', 'chartFilterUI(\'' + dimensionId + '\')');
                var dimensionName = $(evt.item).find('span').text();
                $(evt.item).find('span').attr('name', dimensionName).text(dimensionName + '(无)');
                $(evt.item).append('<i class="fa fa-close" onclick="deleteChartFilter(\'' + dimensionId + '\')"></i>');
                $("#chart-screen-box").find(".mult-no-content").addClass('hide');
                chartFilterDimensionArray.push(dimensionId);
                chartFilterUI(dimensionId);
            }
        },
        //列表单元在列表容器中的排序发生变化后的回调函数
        onUpdate: function (evt) {
            //数据更新啥的
        }
    });

    function chartFilterUI(dimensionId) {
        // 判断维度有没有选择
        $('#filterParamId').val(dimensionId);
        var $text = $('#chart_filter_' + dimensionId).find('span').attr('name');
        $('#filterDataDiv').empty();
        $('.js-choice-target').empty();
        $('.choose-num>span').text(0);
        $('.js-fullChioce').find('input').prop('checked', false);

        var modelId = $('#modelSelect').find('option:selected').attr('modelId');
        // 查询过滤数据
        $.ajax({
            url: '${request.contextPath}/bigdata/data/analyse/getFilterData',
            data: {
                modelId: modelId,
                dimensionId: dimensionId,
                modelDatasetId: $('#userDataSelect').val()
            },
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    showError(response.message);
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
                    if (chartFilterDataMap[dimensionId] != num) {
                        var array = chartFilterDataMap[dimensionId];
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
                }
            }
        });

        layer.open({
            type: 1,
            shade: .6,
            title: $text,
            btn: ['确定', '取消'],
            area: ['450px', '500px'],
            yes: function () {
                var array = [];
                var items = $('#filterDataDiv').find('.filterItem');
                $.each(items, function (i, v) {
                    if ($(v).is(":checked")) {
                        array.push($(v).next().text());
                    }
                });
                chartFilterDataMap[dimensionId] = array;

                if (array.length > 0) {
                    $('#chart_filter_' + dimensionId).find('span').text($text + '(' + array.join(',') + ')');
                    loadChart();
                } else {
                    $('#chart_filter_' + dimensionId).find('span').text($text + '(无)');
                }
                layer.closeAll();
            },
            cancel: function () {
                layer.closeAll();
            },
            content: $('.layer-filtrate')
        });
    }

    function chartFilterDimension(dimensionId) {

        if ($('#chart-screen-box').find('.mult-canshu-li[id="' + 'chart_filter_' + dimensionId + '"]').length < 1) {
            console.info($('#dimension-box').find('.mult-canshu-li[data-id="' + dimensionId + '"]'));
            $('#dimension-box').find('.mult-canshu-li[data-id="' + dimensionId + '"]').clone().attr('id', 'chart_filter_' + dimensionId).appendTo($('#chart-screen-box'));

            $('#chart_filter_' + dimensionId).find('span').attr('onclick', 'chartFilterUI(\'' + dimensionId + '\')');
            var dimensionName = $('#chart_filter_' + dimensionId).find('span').text();
            $('#chart_filter_' + dimensionId).find('span').attr('name', dimensionName).text(dimensionName + '(无)');
            $('#chart_filter_' + dimensionId).append('<i class="fa fa-close" onclick="deleteChartFilter(\'' + dimensionId + '\')"></i>');
            $("#chart-screen-box").find(".mult-no-content").addClass('hide');
            chartFilterDimensionArray.push(dimensionId);
        }

        chartFilterUI(dimensionId);
    }

    function deleteChartFilter(dimensionId) {
        var index = chartFilterDimensionArray.indexOf(dimensionId);
        if (index > -1) {
            chartFilterDimensionArray.splice(index, 1);
        }
        if (chartFilterDataMap[dimensionId] != null) {
            delete chartFilterDataMap[dimensionId];
            loadChart();
        }
        $('#chart_filter_' + dimensionId).remove();
        if (chartFilterDimensionArray.length == 0) {
            $("#chart-screen-box").find(".mult-no-content").removeClass('hide');
        }
    }

    function loadChart() {

        // 横轴维度id
        var chartDimensionId = $('#chart-dimension').attr('data-id');
        // 图例维度id
        var chartLegendId = $('#chart-legend').attr('data-id');

        var chartType = $('.chart-type.active').attr('type');

        if (chartDimensionId == null) {
            chartDimensionId = '';
        }

        if (chartMeasureArray.length < 1) {
            echartShow();
            return;
        }

        var index = layer.msg('数据加载中......', {
            icon: 16,
            time: 0,
            shade: 0.01
        });

        var modelId = $('#modelSelect').find('option:selected').attr('modelId');
        $.ajax({
            url: '${request.contextPath}/bigdata/data/analyse/queryChart',
            data: {
                chartDimensionId: chartDimensionId,
                chartLegendId: chartLegendId,
                chartMeasureArray: chartMeasureArray,
                modelId: modelId,
                filterDataMap: JSON.stringify(chartFilterDataMap),
                chartOrderDataMap: JSON.stringify(chartOrderDataMap),
                chartType: chartType
            },
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    showError(response.message);
                } else {
                    document.getElementById('chartDiv').setAttribute('_echarts_instance_', '');
                    var echart_div = echarts.init(document.getElementById('chartDiv'));
                    var data = JSON.parse(response.data);
                    echart_div.setOption(data);
                    echart_div.resize();
                }
                layer.close(index);
            }
        });

    }

    $('.change-place-box').on('click', function () {
        chartDimensionRotate();
    });

    function chartDimensionRotate() {
        var dimensionId = $('#chart-dimension').attr('data-id');
        var dimensionName = $('#chart-dimension').text();
        var legendId = $('#chart-legend').attr('data-id');
        var legendName = $('#chart-legend').text();

        if (legendId == null) {
            $('#chart-dimension').removeAttr('data-id').text(legendName);
        } else {
            $('#chart-dimension').attr('data-id', legendId).text(legendName);
        }

        if (dimensionId == null) {
            $('#chart-legend').removeAttr('data-id').text(dimensionName);
        } else {
            $('#chart-legend').attr('data-id', dimensionId).text(dimensionName);
        }
        loadChart();
    }

    var multheight;
    $(function () {
        multheight = $(".mult-dynamic-box").height();
        var hangeheight = multheight - $(".lie-td").height();
        $(".hang-td").css("height", hangeheight - 3);
        $(".mult-biao-box").css("height", hangeheight - 20);

        $('.mult-content').on('click', '.dropdown-shan', function (e) {
            e.preventDefault();
            if ($('body').width() - this.getBoundingClientRect().left < 100) {
                $(".dropdown-modal").css("left", this.getBoundingClientRect().left - $('.page-content')[0].getBoundingClientRect().left - 100);
            } else {
                $(".dropdown-modal").css("left", this.getBoundingClientRect().left - $('.page-content')[0].getBoundingClientRect().left);
            }
            $(".dropdown-modal").css("top", this.getBoundingClientRect().top - 40);
            $(".dropdown-modal").find(".dropdown-menu").show();
            var measureId = $(this).parent().parent().attr('data-id');
            $('#measureDelete').attr('data-id', measureId);
            $('#warningMeasureId').attr('data-id', measureId);
        });

        $('.mult-content').on('click', '.dropdown-xia', function (e) {
            e.preventDefault();
            $(".dropdown-child").css("left", this.getBoundingClientRect().left - $('.page-content')[0].getBoundingClientRect().left);
            $(".dropdown-child").css("top", this.getBoundingClientRect().top - 40);
            // 选中的图标
            childIcon = $(this);
            // 选中的th
            var childTh = $(this).parent().parent();
            var dimensionId = childTh.attr('data-id');
            var tr = childTh.parent();
            childRowIndex = tr[0].sectionRowIndex;
            expandRowIndex = tr[0].sectionRowIndex;
            if (childTh.hasClass('row') && childTh.attr('rowspan') != null) {
                childRowIndex = childRowIndex + parseInt(childTh.attr('rowspan')) - 1;
                expandRowIndex = expandRowIndex + parseInt(childTh.attr('rowspan')) - 1;
            } else if (childTh.hasClass('column') && childTh.attr('colspan') != null) {
                childColumnIndex = childColumnIndex + parseInt(childTh.attr('colspan')) - 1;
            }

            if (childTh.attr('parentColumnIndex') != null) {
                childColumnIndex = parseInt(childTh.attr('parentColumnIndex'));
            } else {
                childColumnIndex = childTh[0].cellIndex;
            }

            // 父级位置，下钻后放到子级上面
            parentRowIndex = tr[0].sectionRowIndex;
            parentColumnIndex = childTh[0].cellIndex;

            if (childTh.hasClass('row')) {
                // 获取父级参数
                var parentMap = childTh.attr('childDimensionFilterMap');
                if (parentMap == null) {
                    childDimensionFilterMap = {};
                    for (var i = 0; i <= childColumnIndex; i++) {
                        childDimensionFilterMap[rowDimensionArray[i]] = tr.find('th').eq(i).text().trim();
                    }
                } else {
                    childDimensionFilterMap = JSON.parse(parentMap);
                    childDimensionFilterMap[dimensionId] = childTh.text().trim();
                }
                childPosition = 'row';
            } else {
                // 获取父级参数
                var parentMap = childTh.attr('childDimensionFilterMap');
                if (parentMap == null) {
                    childDimensionFilterMap = {};
                    for (var i = 0; i <= childRowIndex; i++) {
                        childDimensionFilterMap[columnDimensionArray[i]] = $('#column-table').find('tr').eq(i).find('th').eq(childColumnIndex).text().trim();
                    }
                } else {
                    childDimensionFilterMap = JSON.parse(parentMap);
                    childDimensionFilterMap[dimensionId] = childTh.text().trim();
                }
                childPosition = 'column';
            }

            // 查询过滤数据
            $.ajax({
                url: '${request.contextPath}/bigdata/data/analyse/getChildDimension',
                data: {
                    dimensionId: dimensionId
                },
                type: 'POST',
                dataType: 'json',
                success: function (response) {
                    if (!response.success) {
                        layer.msg(response.message, {icon: 2});
                    } else {
                        var data = response.data;
                        $('.childDimension').empty();
                        $.each(data, function (i, v) {
                            $('.childDimension').append('<li class="childDimensionSelect" data-id="' + v.id + '"><a hidefocus="true"><font style="color: darkgray">下钻</font>&nbsp;|&nbsp;<i class="mult-iconfont iconABC" style="color: #3582DF;font-size: 12px;margin-right: 8px;"></i>' + v.name + '</a></li>');
                        });
                        $(".dropdown-child").find(".dropdown-menu").show();
                    }
                }
            });
        });

        // 维度下钻
        $('.mult-content').on('click', '.childDimensionSelect', function (e) {
            var childDimensionId = $(this).attr('data-id');
            var childDimensionIdArray = [];
            childDimensionIdArray.push(childDimensionId);
            if (childPosition == 'row') {

                // 取最大的一个
                var th = $('#row-table tr').eq(childRowIndex).find('th').eq(childColumnIndex);
                while (th.attr('parentColumnIndex') != null) {
                    // 获取父级expandSize
                    var parentRow = parseInt(th.attr('parentRowIndex'));
                    var parentColumn = parseInt(th.attr('parentColumnIndex'));
                    th = $('#row-table tr').eq(parentRow).find('th').eq(parentColumn);
                    childColumnIndex = parentColumn;
                }

                for (var i = childColumnIndex + 1; i < rowDimensionArray.length; i++) {
                    childDimensionIdArray.push(rowDimensionArray[i]);
                }
            } else {
                for (var i = childRowIndex + 1; i < columnDimensionArray.length; i++) {
                    childDimensionIdArray.push(columnDimensionArray[i]);
                }
            }

            //加载层
            var index = layer.msg('正在计算,请稍等......', {
                icon: 16,
                time: 0,
                shade: 0.01
            });

            var modelId = $('#modelSelect').find('option:selected').attr('modelId');
            $.ajax({
                url: '${request.contextPath}/bigdata/data/analyse/queryChildReport',
                data: {
                    dimensionColumnParam: columnDimensionArray,
                    dimensionRowParam: rowDimensionArray,
                    measureParam: measureArray,
                    modelId: modelId,
                    filterDataMap: JSON.stringify(filterDataMap),
                    orderDataMap: JSON.stringify(orderDataMap),
                    childDimensionFilterMap: JSON.stringify(childDimensionFilterMap),
                    childDimensionId: childDimensionId,
                    childPosition: childPosition,
                    childDimensionIdArray: childDimensionIdArray,
                    warningDataMap: JSON.stringify(warningDataMap)
                },
                type: 'POST',
                dataType: 'json',
                success: function (response) {
                    if (!response.success) {
                        showLayerTips4Confirm('error', response.message);
                    } else {

                        var oldTrSize = $('#report-table tr').length;
                        var oldTdSize = $('#report-table tr').first().find('td').length;
                        childIcon.removeClass('fa-plus-square').removeClass('dropdown-xia').addClass('fa-minus-square');
                        if (childPosition == 'row') {
                            $('#temp-table').html(response.data.rowHeader);
                            $('#temp-table th.hasChild')
                                .attr('parentRowIndex', parentRowIndex)
                                .attr('parentColumnIndex', parentColumnIndex)
                                .attr('childDimensionFilterMap', JSON.stringify(childDimensionFilterMap));
                            uniteTdCells('temp-table');

                            $('#row-table tr').eq(childRowIndex).after($('#temp-table tr'));

                            $('#temp-table').html(response.data.table);

                            // 预警
                            if ($('#warningStyleSelect').val() == '1') {
                                $('#temp-table td.red_warning').find('span').after('<i class="mult-red1"></i>');
                                $('#temp-table td.yellow_warning').find('span').after('<i class="mult-yellow1"></i>');
                                $('#temp-table td.green_warning').find('span').after('<i class="mult-green1"></i>');
                            } else {
                                $('#temp-table td.red_warning').find('span').after('<i class="mult-red2"></i>');
                                $('#temp-table td.yellow_warning').find('span').after('<i class="mult-yellow2"></i>');
                                $('#temp-table td.green_warning').find('span').after('<i class="mult-green2"></i>');
                            }

                            $('#report-table tr').eq(childRowIndex).after($('#temp-table tr'));

                            var newTrSize = $('#report-table tr').length;
                            var table = document.getElementById('row-table');
                            var expandSize = newTrSize - oldTrSize;

                            // 递归修改父级expandSize
                            var parent = childIcon.parent().parent();
                            while (parent.attr('parentRowIndex') != null) {
                                // 获取父级expandSize
                                var parentRow = parseInt(parent.attr('parentRowIndex'));
                                var parentColumn = parseInt(parent.attr('parentColumnIndex'));

                                var parentTh = $('#row-table tr').eq(parentRow).find('th').eq(parentColumn).find('.fa-minus-square');
                                var parentExpandSize = parseInt(parentTh.attr('expandSize'));
                                parentTh.attr('expandSize', parentExpandSize + expandSize);
                                parent = parentTh.parent().parent();
                            }

                            childIcon.attr('expandSize', expandSize);
                            for (var i = 0; i < childColumnIndex; i++) {
                                var rindex = expandRowIndex;
                                // 计算扩展的行索引
                                while (table.rows[rindex].cells.length < rowDimensionArray.length || table.rows[rindex].cells[i].style.display == 'none') {
                                    rindex--;
                                }

                                table.rows[rindex].cells[i].rowSpan += expandSize;
                            }
                        } else {

                            $('#temp-table').html(response.data.columnHeader);
                            $('#temp-table th.hasChild')
                                .attr('parentRowIndex', parentRowIndex)
                                .attr('parentColumnIndex', parentColumnIndex)
                                .attr('childDimensionFilterMap', JSON.stringify(childDimensionFilterMap));
                            uniteTrCells('temp-table');
                            var expandSize = $('#temp-table tr').first().find('th').length;

                            var table = document.getElementById('column-table');
                            // 扩充下钻的列
                            table.rows[childRowIndex].cells[childColumnIndex].colSpan += expandSize;
                            // 如果行高是1则下钻一次只添加第一行
                            if (table.rows[childRowIndex].cells[childColumnIndex].rowSpan > 1) {
                                $('#column-table tr').eq(childRowIndex + 1).find('th').eq(childColumnIndex).before($('#temp-table tr').first().find('th'));
                            } else {
                                $('#column-table tr').eq(childRowIndex).after($('#temp-table tr').first());
                            }

                            // 下钻列之前的所有列行高+1
                            for (var i = 0; i < childColumnIndex; i++) {
                                table.rows[childRowIndex].cells[i].rowSpan++;
                            }
                            // 下钻列之后的所有列行高+1
                            for (var i = childColumnIndex + 1; i < table.rows[childRowIndex].cells.length; i++) {
                                table.rows[childRowIndex].cells[i].rowSpan++;
                            }
                            // 删除下钻下一行以前的th
                            if (childRowIndex < $('#column-table tr').length) {
                                $('#row-table tr').eq(childRowIndex + 1).find('th').slice(parentColumnIndex, childColumnIndex).remove();
                            }

                            // if (measureArray.length > 0) {
                            //     // 如果有指标合并指标
                            //     $('#column-table tr').last().find('th').eq(childColumnIndex).after($('#temp-table tr').last().find('th'));
                            //     $('#column-table tr').last().find('th').eq(childColumnIndex).remove();
                            //     $('#temp-table tr').last().remove();
                            //     $('#column-table tr').eq(childRowIndex).after($('#temp-table tr'));
                            // } else {
                            //
                            //
                            // }

                            $('#temp-table').html(response.data.table);
                            // 原来的td去掉 替换成下钻的table
                            var report = document.getElementById('report-table');
                            for (var i = 0; i < report.rows.length; i++) {
                                $('#report-table tr').eq(i).find('td').slice(parentColumnIndex, childColumnIndex + 1).remove();
                                $('#report-table tr').eq(i).find('td').eq(parentColumnIndex).before($('#temp-table tr').eq(i).find('td'));
                            }
                            childIcon.attr('expandSize', expandSize);
                        }
                        layer.close(index);
                        allresize();
                    }
                }
            });
        });

        // 收起下钻维度
        $('.mult-content').on('click', '.fa-minus-square', function (e) {
            // 选中的th
            var childTh = $(this).parent().parent();
            var dimensionId = childTh.attr('data-id');
            var tr = childTh.parent();
            childRowIndex = tr[0].sectionRowIndex;
            expandRowIndex = tr[0].sectionRowIndex;
            if (childTh.hasClass('row') && childTh.attr('rowspan') != null) {
                childRowIndex = childRowIndex + parseInt(childTh.attr('rowspan')) - 1;
            }

            if (childTh.attr('parentColumnIndex') != null) {
                childColumnIndex = parseInt(childTh.attr('parentColumnIndex'));
                if (childColumnIndex == 0) {
                    // 取最大的一个
                    var th = childTh;
                    while (th.attr('parentColumnIndex') != null) {
                        // 获取父级expandSize
                        var parentRow = parseInt(th.attr('parentRowIndex'));
                        var parentColumn = parseInt(th.attr('parentColumnIndex'));
                        th = $('#row-table tr').eq(parentRow).find('th').eq(parentColumn);
                        childColumnIndex = parentColumn;
                    }
                }
            } else {
                childColumnIndex = childTh[0].cellIndex;
            }

            var table = document.getElementById('row-table');

            var expandSize = parseInt($(this).attr('expandSize'));
            // 递归父级扩展宽度减去
            // 获取父级expandSize

            var parTh = childTh;
            while (parTh.attr('parentRowIndex') != null) {
                var parentRow = parseInt(parTh.attr('parentRowIndex'));
                var parentColumn = parseInt(parTh.attr('parentColumnIndex'));
                parTh = $('#row-table tr').eq(parentRow).find('th').eq(parentColumn).find('.fa-minus-square');
                var parentExpandSize = parseInt(parTh.attr('expandSize'));
                parTh.attr('expandSize', parentExpandSize - expandSize);
                parTh = parTh.parent().parent();
            }

            for (var i = 0; i < childColumnIndex; i++) {
                var rindex = expandRowIndex;
                // 计算扩展的行索引
                while (table.rows[rindex].cells.length < rowDimensionArray.length || table.rows[rindex].cells[i].style.display == 'none') {
                    rindex--;
                }

                table.rows[rindex].cells[i].rowSpan -= expandSize;
            }

            $('#row-table tr').slice(childRowIndex + 1, childRowIndex + expandSize + 1).remove();
            $('#report-table tr').slice(childRowIndex + 1, childRowIndex + expandSize + 1).remove();

            $(this).removeClass('fa-minus-square').addClass('dropdown-xia').addClass('fa-plus-square');
        });

        $(document).click(function (event) {
            var eo = $(event.target);
            if ($('.dropdown-menu').is(':visible') && eo.attr('class') != 'dropdown-xia' && !eo.parent('.btn-group').length && !eo.parents('.dropdown-menu').length)
                $(".dropdown-child").find(".dropdown-menu").hide();
        });

        $('.mult-content').on('click', '.dropdown-menu li', function (e) {
            $(".dropdown-child").find(".dropdown-menu").hide();
        });

        $(document).click(function (event) {
            var eo = $(event.target);
            if ($('.dropdown-menu').is(':visible') && eo.attr('class') != 'dropdown-shan' && !eo.parent('.btn-group').length && !eo.parents('.dropdown-menu').length)
                $(".dropdown-modal").find(".dropdown-menu").hide();
        });

        $('.mult-content').on('click', '#measureDelete', function () {
            var measureId = $('#measureDelete').attr('data-id');
            var index = measureArray.indexOf(measureId);
            if (index > -1) {
                measureArray.splice(index, 1);
                if (columnDimensionArray.length < 1 && rowDimensionArray.length < 1 && measureArray.length < 1) {
                    refreshTable();
                } else
                    loadFilterReport();
            }
            $('#biao-box').find('.mult-canshu-li[data-id="' + measureId + '"]').remove();
            $(".dropdown-modal").find(".dropdown-menu").hide();
        });

        $('.mult-content').on('click', '.dropdown-toggle', function (e) {
            e.preventDefault();
            $('.dropdown-toggle').each(function () {
                var $g = $(this).parent('.btn-group');
                if ($g.hasClass('open')) {
                    $g.removeClass('open');
                }
            });
            $(this).parent('.btn-group').toggleClass('open');
            $('.dropdown-menu').css('z-index', '1000');
        });

        $('.mult-content').on('click', '.dropdown-menu li', function (e) {
            var $btnGroup = $(this).parents('.btn-group');

            if ($(this).find('i').hasClass('icondescendingorder') || $(this).find('i').hasClass('iconascending') || $(this).find('i').hasClass('icondefault')) {
                // 排序重新查询报表
                if (!$(this).hasClass('active')) {
                    if ($(this).hasClass('chart')) {
                        var dimensionId = $(this).parent().prev().prev().attr('data-id');
                        if ($(this).attr('type') != null) {
                            chartOrderDataMap[dimensionId] = $(this).attr('type');
                        } else {
                            if (chartOrderDataMap[dimensionId] != null) {
                                delete chartOrderDataMap[dimensionId];
                            }
                        }
                        loadChart();
                    } else {
                        var dimensionId = $(this).parent().parent().attr('id');
                        if ($(this).attr('type') != null) {
                            orderDataMap[dimensionId] = $(this).attr('type');
                        } else {
                            if (orderDataMap[dimensionId] != null) {
                                delete orderDataMap[dimensionId];
                            }
                        }
                        loadFilterReport();
                    }
                }
                $(this).addClass('active').siblings('li').removeClass('active');
            }
            $btnGroup.removeClass('open');
            if ($btnGroup.hasClass('filter')) {
                e.preventDefault();
                var txt = $(this).text();
                $btnGroup.children('.dropdown-toggle').text(txt);
            }
        });

        $(document).click(function (event) {
            var eo = $(event.target);
            if ($('.dropdown-menu').is(':visible') && eo.attr('class') != 'dropdown-toggle' && !eo.parent('.btn-group').length && !eo.parents('.dropdown-menu').length)
                $('.btn-group').removeClass('open');
        });
        // 旋转
        $('.mult-content').on('click', '.dimensionRotate', function () {
            if ($(".mult-chart-change.active").attr('data-id') == 2) {
                chartDimensionRotate();
                return;
            }

            if (rowDimensionArray.length < 1 && columnDimensionArray.length < 1) {
                showLayerTips4Confirm('error', "请选择维度!");
                return;
            }

            var lieC = $('#lie-box').children();
            var hangC = $('#hang-box').children();
            $("#hang-box").empty().append(lieC);
            $("#lie-box").empty().append(hangC);

            columnDimensionArray = [];
            $.each($('#lie-box').find('.mult-canshu-li'), function (i, v) {
                columnDimensionArray.push($(this).attr('data-id'));
            });

            rowDimensionArray = [];
            $.each($('#hang-box').find('.mult-canshu-li'), function (i, v) {
                rowDimensionArray.push($(this).attr('data-id'));
            })

            if (columnDimensionArray.length == 0) {
                $('#lie-box').find('.mult-no-content').removeClass('hide');
            }
            $('#hang-box').find('.mult-no-content').addClass('hide');
            loadFilterReport();
        })

        // 预警
        $('.mult-content').on('click', '.dimensionWarning', function () {
            $(".dropdown-modal").find(".dropdown-menu").hide();
            if ($(".mult-chart-change.active").attr('data-id') == 2) {
                showLayerTips4Confirm('error', "请选择报表再进行预警!");
                return;
            }

            if (Object.keys(warningDataMap).length < 1) {
                if ($(this).find('a').attr('data-id') != null) {
                    $('#warningMeasure').val($(this).find('a').attr('data-id'));
                }
            }

            layer.open({
                type: 1,
                shade: .6,
                title: '预警',
                btn: ['确定', '取消'],
                area: ['450px', '500px'],
                yes: function () {

                    if ($('#warningMeasure').val() == null) {
                        showLayerTips4Confirm('error', "请选择度量!");
                        return;
                    }
                    warningDataMap = {};
                    var dataArray = [];
                    dataArray.push($('#firstSymbolSelect').val());
                    dataArray.push($('#secondSymbolSelect').val());
                    dataArray.push($('#num1').val());
                    dataArray.push($('#num2').val());
                    dataArray.push($('#warningStyleSelect').val());
                    warningDataMap[$('#warningMeasure').val()] = dataArray;

                    loadFilterReport();
                    layer.closeAll();
                },
                cancel: function () {
                    layer.closeAll();
                },
                content: $('.layer-warning')
            });


        });

        // 保存
        $('.mult-content').on('click', '.saveBtn', function () {

            if ($('.mult-chart-change.active').attr("data-id") == 1) {
                if (rowDimensionArray.length < 1 && columnDimensionArray < 1 && measureArray.length < 1) {
                    showLayerTips4Confirm('error', "请选择维度或者度量!");
                    return;
                }

                var isSubmit = false;

                $('.layer-save').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
                $('.layer-save').load("${request.contextPath}/bigdata/data/analyse/saveReport?code=" + $('#modelSelect').val() + "&favoriteId=" + $('#favoriteId').val());
                $('#favoriteName').val($('#modelSelect').find('option:selected').text());
                var modelId = $('#modelSelect').find('option:selected').attr('modelId');
                layer.open({
                    type: 1,
                    shade: .6,
                    title: '保存报表',
                    btn: ['确定', '取消'],
                    area: ['750px', '680px'],
                    yes: function (index, layero) {
                        if (isSubmit) {
                            return;
                        }

                        if ($('#favoriteName').val() == "") {
                            layer.tips("请输入名称", "#favoriteName", {
                                tipsMore: true,
                                tips: 3
                            });
                            isSubmit = false;
                            return;
                        }

                        if ($('#rOrderId').val() == "") {
                            layer.tips("排序号不能为空", "#rOrderId", {
                                tipsMore: true,
                                tips: 3
                            });
                            isSubmit = false;
                            return;
                        }

                        if ($('#folderId').val() == null) {
                            layer.tips("请选择文件夹", "#folderId", {
                                tipsMore: true,
                                tips: 3
                            });
                            isSubmit = false;
                            return;
                        }

                        var code = $('#modelSelect').val();

                        var tags = [];
                        $('span.selected').each(function () {
                            tags.push($(this).attr('tag_id'));
                        });

                        var options = {
                            url: "${request.contextPath}/bigdata/data/analyse/saveDataModelFavorite",
                            data: {
                                code: code,
                                dimensionColumnParam: columnDimensionArray,
                                dimensionRowParam: rowDimensionArray,
                                measureParam: measureArray,
                                modelId: modelId,
                                filterDataMap: JSON.stringify(filterDataMap),
                                warningDataMap: JSON.stringify(warningDataMap),
                                orderDataMap: JSON.stringify(orderDataMap),
                                modelDatasetId: $('#userDataSelect').val(),
                                conditionParam: $('#conditionSelect').val(),
                                tagArrays: tags
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
            } else {

                if (chartMeasureArray.length < 1) {
                    showLayerTips4Confirm('error', "请选择度量!");
                    return;
                }

                var isSubmit = false;

                $('.layer-save').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
                $('.layer-save').load("${request.contextPath}/bigdata/data/analyse/saveReport?isChart=true&code=" + $('#modelSelect').val() + "&favoriteId=" + $('#chartFavoriteId').val());
                $('#favoriteName').val($('#modelSelect').find('option:selected').text());
                var modelId = $('#modelSelect').find('option:selected').attr('modelId');
                layer.open({
                    type: 1,
                    shade: .6,
                    title: '保存图表',
                    btn: ['确定', '取消'],
                    area: ['750px', '780px'],
                    yes: function (index, layero) {
                        if (isSubmit) {
                            return;
                        }

                        if ($('#favoriteName').val() == "") {
                            layer.tips("请输入名称", "#favoriteName", {
                                tipsMore: true,
                                tips: 3
                            });
                            isSubmit = false;
                            return;
                        }

                        if ($('#rOrderId').val() == "") {
                            layer.tips("排序号不能为空", "#rOrderId", {
                                tipsMore: true,
                                tips: 3
                            });
                            isSubmit = false;
                            return;
                        }

                        if ($('#folderId').val() == null) {
                            layer.tips("请选择文件夹", "#folderId", {
                                tipsMore: true,
                                tips: 3
                            });
                            isSubmit = false;
                            return;
                        }

                        var code = $('#modelSelect').val();

                        var tags = [];
                        $('span.selected').each(function () {
                            tags.push($(this).attr('tag_id'));
                        });

                        // 横轴维度id
                        var chartDimensionId = $('#chart-dimension').attr('data-id');
                        // 图例维度id
                        var chartLegendId = $('#chart-legend').attr('data-id');

                        var options = {
                            url: "${request.contextPath}/bigdata/data/analyse/saveDataModelFavorite?isChart=true",
                            data: {
                                code: code,
                                chartDimensionId: chartDimensionId,
                                chartLegendId: chartLegendId,
                                measureParam: chartMeasureArray,
                                modelId: modelId,
                                filterDataMap: JSON.stringify(chartFilterDataMap),
                                orderDataMap: JSON.stringify(chartOrderDataMap),
                                modelDatasetId: $('#userDataSelect').val(),
                                conditionParam: $('#conditionSelect').val(),
                                tagArrays: tags
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
        });

        // 授权
        $('.mult-content').on('click', '.authorization', function () {

            if ($('#favoriteId').val() == '') {
                showLayerTips4Confirm('error', "请先保存报表!");
                return;
            }

            $('.layer-authorization').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
            $('.layer-authorization').load("${request.contextPath}/bigdata/data/analyse/authorization?favoriteId=" + $('#favoriteId').val());
            $('#favoriteName').val($('#modelSelect').find('option:selected').text());
            var isSubmit = false;
            layer.open({
                type: 1,
                shade: .6,
                title: '授权',
                btn: ['确定', '取消'],
                area: ['750px', '680px'],
                yes: function (index, layero) {
                    if (isSubmit) {
                        return;
                    }
                    var data = {};
                    data.favoriteId = $('#favoriteId').val();
                    var orderType = $('input:radio:checked').val();
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
                        url: '${request.contextPath}/bigdata/data/analyse/saveAuthorization',
                        data: data,
                        type: 'POST',
                        dataType: 'json',
                        success: function (val) {
                            if (!val.success) {
                                showLayerTips4Confirm('error', val.message);
                            } else {
                                layer.close(index);
                                showLayerTips('success', '保存成功', 't');
                            }
                            isSubmit = false;
                        }
                    });
                },
                content: $('.layer-authorization')
            });
        });

        // 预警
        $('#warningStyleSelect').change(function () {
            var type = $(this).val();
            if (type == '1') {
                $('.earli-li .earli-basic-img').eq(0).removeClass('red2').addClass('red1');
                $('.earli-li .earli-basic-img').eq(1).removeClass('yellow2').addClass('yellow1');
                $('.earli-li .earli-basic-img').eq(2).removeClass('green2').addClass('green1');
            } else {
                $('.earli-li .earli-basic-img').eq(0).removeClass('red1').addClass('red2');
                $('.earli-li .earli-basic-img').eq(1).removeClass('yellow1').addClass('yellow2');
                $('.earli-li .earli-basic-img').eq(2).removeClass('green1').addClass('green2');
            }
        });

        // 导出
        $('.mult-content').on('click', '.exportReport', function () {

            if ($(".mult-chart-change.active").attr('data-id') != '1') {
                showLayerTips4Confirm('error', "请选择报表再进行导出!");
                return;
            }

            if (rowDimensionArray.length < 1 && columnDimensionArray < 1 && measureArray.length < 1) {
                showLayerTips4Confirm('error', "请选择维度或者度量!");
                return;
            }

            $('#temp-table').empty();
            var row = $('#row-table').clone();
            var column = $('#column-table').clone();
            var table = $('#report-table').clone();

            for (var t = 0; t < column.find('tr').length; t++) {
                var tr = column.find('tr').eq(t).clone();

                if (rowDimensionArray.length < 1 && columnDimensionArray < 1) {
                    tr.find('th').first().before('<th>统计度量</th>')
                } else if (rowDimensionArray.length < 1 && columnDimensionArray.length > 0) {
                    var name = paramNameMap[columnDimensionArray[t]];
                    if (name == null) {
                        tr.find('th').first().before('<th>统计度量</th>');
                    } else {
                        tr.find('th').first().before('<th>' + paramNameMap[columnDimensionArray[t]] + '</th>');
                    }
                } else if (columnDimensionArray.length < 1 && rowDimensionArray.length > 0) {
                    var l = rowDimensionArray.length;
                    for (var i = 0; i < l; i++) {
                        tr.find('th').first().before('<th>' + paramNameMap[rowDimensionArray[(l-i-1)]] + '</th>');
                    }
                } else {
                    var l = rowDimensionArray.length;
                    if (t == columnDimensionArray.length) {
                        for (var i = 0; i < l; i++) {
                            tr.find('th').first().before('<th>' + paramNameMap[rowDimensionArray[(l-i-1)]] + '</th>');
                        }
                    } else {
                        tr.find('th').first().before('<th colspan="' + rowDimensionArray.length + '">' + paramNameMap[columnDimensionArray[t]] + '</th>');
                    }
                }
                $('#temp-table').append(tr);
            }

            for (var i = 0; i < row.find('tr').length; i++) {
                var tr = row.find('tr').eq(i).clone();
                tr.append(table.find('tr').eq(i).find('td'));
                $('#temp-table').append(tr);
            }

            $('#temp-table').table2excel({
                filename: "多维报表分析.xls",
                exclude: '.noExport'
            });
        });

        // 预警
        $('#clearWarning').click(function () {
            clearWarning();
        });

        $('.mult-content').on('click', '.refreshData', function () {
            if ($(".mult-chart-change.active").attr('data-id') == 2) {
                loadChart();
            } else {
                if (rowDimensionArray.length > 0 || columnDimensionArray.length > 0 || measureArray.length > 0) {
                    loadFilterReport();
                } else {
                    showLayerTips4Confirm('error', "请选择维度或者度量!");
                }
            }
        });

        $('.mult-content').on('click', '.clearAll', function () {
            clearAll();
        })

        $('.mult-content').on('click', '.chart-type', function () {
            loadChart();
        })

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

        //------------------------图表--------------------------------------------------------
        $('.mult-content').on('click', '.chartDimensionDelete', function () {
            if ($(this).hasClass('chartDimension')) {
                $('#chart-dimension').text('将维度拖到这里').removeAttr('data-id');
            } else if ($(this).hasClass('chartMeasure')) {
                chartMeasureArray = [];
                $('#chart-measure').text('将度量拖到这里').removeAttr('data-id');
            } else if ($(this).hasClass('chartLegend')) {
                $('#chart-legend').text('将维度拖到这里').removeAttr('data-id');
            }
            loadChart();
        });

        $('.mult-content').on('click', '.chartFilter', function () {
            var dimensionId = $(this).parent().prev().prev().attr('data-id');
            if (dimensionId == null || dimensionId == '') {
                showLayerTips4Confirm('error', '请先选择维度再进行筛选!');
                return;
            }
            chartFilterDimension(dimensionId);
        });
    });

    function clearAll() {
        if ($(".mult-chart-change.active").attr('data-id') == 1) {
            $('#favoriteId').val('');
            clearReport();
        } else {
            $('#chartFavoriteId').val('');
            clearChart();
        }
    }

    function clearWarning() {
        warningDataMap = {};
        $('#firstSymbolSelect').val('>=');
        $('#secondSymbolSelect').val('>=');
        $('#num1').val('');
        $('#num2').val('');
        $('.clone-num1').empty().text('X');
        $('.clone-num2').empty().text('X');
        layer.closeAll();
        loadFilterReport();
    }

    function clearChart() {
        chartMeasureArray = [];
        chartFilterDataMap = {};

        $('#chart-dimension').text('将维度拖到这里').removeAttr('data-id');
        $('#chart-measure').text('将度量拖到这里').removeAttr('data-id');
        $('#chart-legend').text('将维度拖到这里').removeAttr('data-id');

        $("#chart-screen-box").find('.mult-canshu-li').remove();
        $("#chart-screen-box").find('.mult-no-content').removeClass('hide');

        if ($('.mult-chart-change.active').attr('data-id') == '2') {
            echartShow();
        }
    }

    function clearReport() {
        columnDimensionArray = [];
        rowDimensionArray = [];
        measureArray = [];
        filterDataMap = {};
        orderDataMap = {};
        clearWarning();

        $("#hang-box").find('.mult-canshu-li').remove();
        $("#lie-box").find('.mult-canshu-li').remove();
        $("#biao-box").find('.mult-canshu-li').remove();
        $("#screen-box").find('.mult-canshu-li').remove();

        $('#row-table').empty();
        $('#column-table').empty();
        $('#report-table').empty();

        $('#hang-box').find('.mult-no-content').removeClass('hide');
        $('#lie-box').find('.mult-no-content').removeClass('hide');
        $('#biao-box').find('.mult-no-content').removeClass('hide');
        $("#screen-box").find('.mult-no-content').removeClass('hide');
        $('#hang-box').css('bottom', 0);
        allresize();
    }

    //修改表格高度
    function hangresize() {
        var hangeheight = multheight - $(".lie-td").height();
        $(".hang-td").css("height", hangeheight - 3);
        if (rowDimensionArray != null && rowDimensionArray.length > 0) {
            $(".mult-hang-table").css("height", hangeheight - 4);
        }

        $(".biao-td").css("height", hangeheight - 3);
        $(".mult-biao-box").css("height", hangeheight - 20);
    }

    function showError(errorDetail) {
        //错误弹窗
        $(".mult-tan-content").hide();
        $(".mult-tan-content").addClass('hide');
        $('#errorDetail').text(errorDetail);
        layer.open({
            type: 1,
            title: '错误提示',
            area: ['520px', 'auto'],
            content: $('.js-error'),
            btn: ['确定', '详情'],
            yes: function () {
                layer.closeAll();
            },
            btn2: function (index, layero) {
                //按钮【按钮二】的回调
                $(".layui-layer-content").css("height", "auto");
                if ($(".mult-tan-content").hasClass('hide')) {
                    $(".mult-tan-content").removeClass('hide');
                    $(".mult-tan-content").show();
                } else {
                    $(".mult-tan-content").hide();
                    $(".mult-tan-content").addClass('hide');
                }
                return false;
            }
        })
    }

    //窗口变化，图表resize
    $(window).resize(function () {
        allresize();
    })

    //尺寸发生变换
    function allresize() {
        //列
        $(".mult-lie-body").css("width", "100000px");
        $(".mult-lie-body").css("width", $(".mult-lie-body table").width());
        hangresize();
        //行
        if (rowDimensionArray.length > 0) {
            $("#hang-box").css("bottom", $("#hang-box").height() + 1);
            var hangwidth = 140 * rowDimensionArray.length;
            $(".mult-hang-table").css("width", hangwidth);
            $(".hang-td").css("width", hangwidth);
        } else {
            $(".mult-hang-table").css("width", 200);
            $(".hang-td").css("width", 200);
        }

        $(".mult-lie-table").css("width", $(".mult-dynamic-box").width() - hangwidth - 4);
        $(".biao-td").css("width", $(".mult-dynamic-box").width() - hangwidth - 4);
        hangresize()

        //表
        $(".mult-biao-body").css("width", "100000px");
        $(".mult-biao-body").css("width", $(".mult-biao-body table").width());
        if (rowDimensionArray.length == 0) {
            $(".mult-lie-table").css("width", $(".mult-dynamic-box").width() - 200 - 4);
            $(".biao-td").css("width", $(".mult-dynamic-box").width() - 200 - 4);
        }

        if (rowDimensionArray == 0 && columnDimensionArray == 0 && measureArray == 0) {
            $("#hang-box").css("bottom", 0);
        }

        $('#report-table td').css('width', 140);
        $('#row-table th').css('width', 140);
        $('#column-table th').css('width', 140);
        $('#report-table td').css('text-align', 'center');
        $('#row-table th').css('text-align', 'center');
        $('#column-table th').css('text-align', 'center');

        // 遍历所有th 设置指标图标
        $.each($('#column-table th'), function (i, v) {
            if ($(this).hasClass('measure') && $(this).find('div').length < 1) {
                $(this).append('<div class="btn-group dropdown-shan-box">\n' +
                    '                                    <i class="mult-iconfont iconscreening dropdown-shan"></i>\n' +
                    '                                  </div>');
            }

            // if ($(this).hasClass('hasChild') && $(this).find('div').length < 1) {
            //     $(this).append('<div class="btn-group dropdown-xia-box">\n' +
            //         '                                <i class="fa fa-plus-square dropdown-xia"></i>\n' +
            //         '                              </div>');
            // }
        });

        $.each($('#row-table th'), function (i, v) {
            if ($(this).hasClass('hasChild') && $(this).find('div').length < 1) {
                $(this).append('<div class="btn-group dropdown-xia-box">\n' +
                    '                                <i class="fa fa-plus-square dropdown-xia"></i>\n' +
                    '                              </div>');
            }
        });

        var tablleTr = $('#report-table').find('tr');
        $("#row-table tr").each(function (i, v) {
            tablleTr.eq(i).height($(v).height());
        });

        $(".mult-lie-table").css("width", $(".mult-dynamic-box").width() - hangwidth - 4);
        $(".biao-td").css("width", $(".mult-dynamic-box").width() - hangwidth - 4);

        //表格滚动条移动，列滚动条跟着动
        $(".biao-td").scroll(function (event) {
            $(".mult-lie-table").scrollLeft($(".biao-td").scrollLeft());
            $(".mult-hang-table").scrollTop($(".biao-td").scrollTop());
            $(".mult-biao-box").css("left", $(".biao-td").scrollLeft());
        });

        hangresize();
        $('#biao-box').css('left', $('.mult-lie-body').css('left'));
    }

    //侧边栏缩小
    $(".iconunfold").click(function () {
        if ($(".mult-left-box").hasClass("active")) {
            $(".mult-left-box").removeClass("active");
            $(".mult-left-box").css("width", 250);
        } else {
            $(".mult-left-box").addClass("active");
            $(".mult-left-box").css("width", 40);
        }
        setTimeout(function () {
            allresize()
        }, 1000)
    })

    //修改文件
    $('.mult-title-span').click(function () {
        $('.layer-reportList').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
        $('.layer-reportList').load('${request.contextPath}/bigdata/data/analyse/reportManage');
        layer.open({
            type: 1,
            shade: .6,
            title: '报表管理',
            btn: ['关闭'],
            area: ['550px', '480px'],
            content: $('.layer-reportList')
        });
    });

    $(".mult-modal-body").find("td").click(function () {
        $(this).parents("tr").addClass("active").siblings().removeClass("active");
    })

    //预警数字修改
    $("#num1").on('input', function () {
        $(".clone-num1").html($("#num1").val());
    })
    $("#num2").on('input', function () {
        $(".clone-num2").html($("#num2").val());
    })
    $(".earli-title").find("a").click(function () {
        $(".earli-basic").find("input").val("");
        $(".clone-num1").html("X");
        $(".clone-num2").html("X");
    })

    //切换表格
    $(".mult-chart-change").click(function () {
        $(this).addClass("active").siblings(".mult-chart-change").removeClass("active");
        if ($(this).attr("data-id") == 1) {
            $(".mult-show-one").show();
            $(".mult-show-two").hide();
            $('#chart-screen-box').addClass('hide');
            $('#screen-box').removeClass('hide');
            allresize();
        } else {
            $(".mult-show-one").hide();
            $(".mult-show-two").show();
            $('#chart-screen-box').removeClass('hide');
            $('#screen-box').addClass('hide');
            if ($('#chart-dimension').attr('data-id') == null && chartMeasureArray.length < 1) {
                clearChart();
            }
        }
    })
</script>
<script type="text/javascript">
    $('.chart-type').click(function () {
        $(this).addClass('active').siblings().removeClass('active')
    });

    //echartShow();
    function echartShow() {
        var dataAxis = ['点', '击', '柱', '子', '或', '者', '两', '指', '在', '触', '屏', '上', '滑', '动', '能', '够', '自', '动', '缩', '放'];
        var data = [220, 182, 191, 234, 290, 330, 310, 123, 442, 321, 90, 149, 210, 122, 133, 334, 198, 123, 125, 220];
        var yMax = 500;
        var dataShadow = [];

        for (var i = 0; i < data.length; i++) {
            dataShadow.push(yMax);
        }
        var option = {
            title: {
                text: '示例',
                subtext: 'Feature Sample: Gradient Color, Shadow, Click Zoom'
            },
            xAxis: {
                data: dataAxis,
                axisLabel: {
                    inside: true,
                    textStyle: {
                        color: '#fff'
                    }
                },
                axisTick: {
                    show: false
                },
                axisLine: {
                    show: false
                },
                z: 10
            },
            yAxis: {
                axisLine: {
                    show: false
                },
                axisTick: {
                    show: false
                },
                axisLabel: {
                    textStyle: {
                        color: '#999'
                    }
                }
            },
            dataZoom: [
                {
                    type: 'inside'
                }
            ],
            series: [
                { // For shadow
                    type: 'bar',
                    itemStyle: {
                        normal: {color: 'rgba(0,0,0,0.05)'}
                    },
                    barGap: '-100%',
                    barCategoryGap: '40%',
                    data: dataShadow,
                    animation: false
                },
                {
                    type: 'bar',
                    itemStyle: {
                        normal: {
                            color: new echarts.graphic.LinearGradient(
                                0, 0, 0, 1,
                                [
                                    {offset: 0, color: '#83bff6'},
                                    {offset: 0.5, color: '#188df0'},
                                    {offset: 1, color: '#188df0'}
                                ]
                            )
                        },
                        emphasis: {
                            color: new echarts.graphic.LinearGradient(
                                0, 0, 0, 1,
                                [
                                    {offset: 0, color: '#2378f7'},
                                    {offset: 0.7, color: '#2378f7'},
                                    {offset: 1, color: '#83bff6'}
                                ]
                            )
                        }
                    },
                    data: data
                }
            ]
        };
        document.getElementById('chartDiv').setAttribute('_echarts_instance_', '');
        var myChart = echarts.init(document.getElementById('chartDiv'));
        myChart.setOption(option);
        var zoomSize = 6;
        myChart.on('click', function (params) {
            console.log(dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)], params);
            myChart.dispatchAction({
                type: 'dataZoom',
                startValue: dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)],
                endValue: dataAxis[Math.min(params.dataIndex + zoomSize / 2, data.length - 1)]
            });
        });
    }
</script>
<script type="text/javascript">

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

    var $div = document.getElementById('mainModelDiv');

    $div.parentNode.insertBefore(html2canvas, $div);
    $div.parentNode.insertBefore(jspdf, $div);
    $div.parentNode.insertBefore(table2excel, $div);
</script>