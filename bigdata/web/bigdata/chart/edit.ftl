<link rel="stylesheet" href="${request.contextPath}/static/bigdata/css/myChart.css"/>
<div class="facing" id="chart_container">
    <div class="left-part" id="chart_type_list" chart-show="false">
        <#if chartTypes?exists && chartTypes?size gt 0>
            <#list chartTypes as ct>
                <#assign select=(ct_index==0 && chart.id?default('') == '') />
                <p class="<#if ct.seriesName == seriesName || select>bg-grey</#if>" data-toggle="collapse"
                   href="#collapseExample${ct.seriesName}">
                    ${ct.name!}
                    <#if ct.seriesName == seriesName || select>
                    <i class="arrow fa fa-angle-down"></i>
                    <#else >
                    <i class="arrow fa fa-angle-right"></i>
                    </#if>
                </p>
                <div id="collapseExample${ct.seriesName}"
                     class="collapse <#if seriesName == ct.seriesName || select> in</#if>"
                     aria-expanded="<#if ct.seriesName == seriesName|| select>true<#else>false</#if>">
                    <div class="box-boder text-center">
                        <#list ct.chartCategoryClassifications as ccc>
                            <img type="${ccc.chartType!}" src="${ccc.thumbnail!}"
                                 class="chart_${ccc.chartType!} <#if ccc.chartType == chart.chartType || select> border-active</#if>"/>
                        </#list>
                    </div>
                </div>
            </#list>
        </#if>
    <#--<#if reportTemplates?exists && reportTemplates?size gt 0>-->
    <#--<#list reportTemplates as template>-->
    <#--<p data-toggle="collapse" href="#${template.id}" style="overflow: hidden;">${template.templateName!}</p>-->
    <#--<div id="${template.id}" class="collapse " aria-expanded="false">-->
    <#--<div class="box-boder text-center">-->
    <#--<img type="0" src="${fileUrl!}/${template.thumbnailPath!}"-->
    <#--<#if chart.templateId?exists><#if chart.templateId == template.id>class="border-active"</#if></#if>-->
    <#--templateId="${template.id}"/>-->
    <#--</div>-->
    <#--</div>-->
    <#--</#list>-->
    <#--</#if>-->
    </div>

    <div class="right-part">
        <div class="data-style">
            <ul class="nav nav-tabs nav-tabs-1 chart-ul">
                <li class="text-center active col-md-6">
                    <a data-toggle="tab" href="#aa" id="datasource_tab">数据源</a>
                </li>
                <li class="text-center col-md-6">
                    <a data-toggle="tab" href="#bb" id="my_parameters">自定义参数</a>
                </li>
            </ul>

            <div class="tab-content padding-tab">
                <div id="aa" class="tab-pane active">
                    <div class="bs-docs-section" id="drop-down">
                        <div class="filter-item block">
                            <span class="filter-name">数据源类型</span>
                            <select name="datasourceType" id="datasource_type" style="float: right;"
                                    class="form-control chosen-select chosen-width"
                                    onchange="changeDatasourceType(this);"
                                    data-placeholder="<#if dataSourceTypeVOs?exists && dataSourceTypeVOs?size gt 0>请选择数据源类型<#else>请先维护数据源信息</#if>">
                                <#if dataSourceTypeVOs?exists && dataSourceTypeVOs?size gt 0>
                                    <#list dataSourceTypeVOs as datasourceVO>
                                        <option value="${datasourceVO.value!}"
                                                <#if chart.dataSourceType! == datasourceVO.value!>selected</#if>>${datasourceVO.name!}</option>
                                    </#list>
                                </#if>
                            </select>
                        </div>
                        <div class="filter-item block">
                            <span class="filter-name">数据源</span>
                            <select name="datasourceId" id="datasource_id" style="float: right;"
                                    class=" form-control  chosen-select chosen-width" data-placeholder="未选择">
                            </select>
                        </div>
                    </div>

                    <div class="text" id="ace_editor" <#--style="width: 262px;"-->>
                        <textarea id="data_set" style="width: 100%;" name="" rows="" cols=""
                                  placeholder="<请在此插入数据或者SQL>">${chart.dataSet!}</textarea>
                    </div>
                    <div id="text_blank" class="blank" style="display: none;"></div>
                </div>

                <div id="bb" class="tab-pane">
                <#--<div class="filter-item block">-->
                <#--<span class="filter-name">图表名称&nbsp;&nbsp;</span>-->
                <#--<input name="name" id="chart_name" style="float: right;" value="${chart.name!}" class="form-control" placeholder="请输入图表名称"/>-->
                <#--</div>-->
                    <div class="filter-item block">
                        <span class="filter-name">自动刷新&nbsp;&nbsp;</span>
                        <select name="autoUpdate" id="chart_autoUpdate" style="float: right;"
                                class=" form-control  chosen-select chosen-width" data-placeholder="未选择"
                                onchange="autoUpdate();">
                            <option value="1" <#if chart.autoUpdate?default(0) == 1>selected</#if>>是</option>
                            <option value="0" <#if chart.autoUpdate?default(0) == 0>selected</#if>>否</option>
                        </select>
                    </div>
                    <div class="filter-item block" <#if chart.autoUpdate?default(0)==0>style="display: none;"</#if>>
                        <span class="filter-name">刷新频率(秒)</span>
                        <input name="updateInterval" id="chart_updateInterval" style="float: right;"
                               value="${chart.updateInterval!0}" class="form-control" placeholder="建议30-60秒"/>
                    </div>
                    <div class="filter-item block map-select" style="display:none;">
                        <span class="filter-name">地图&nbsp;&nbsp;</span>
                        <select name="mapName" id="map_name" style="float: right;"
                                class=" form-control  chosen-select chosen-width" data-placeholder="请选择地区">
                            <#if regions?exists && regions?size gt 0>
                                <#list regions as region>
                                    <option value="${region.regionCode}">${region.regionName!}</option>
                                </#list>
                            </#if>
                        </select>
                    </div>
                    <div class="blank"></div>
                </div>
                <div class="btn-wrap">
                    <div class="col-md-6 col-sm-6">
                        <button class="btn btn-default btn-block" onclick="createChart();">运行数据</button>
                    </div>
                    <div class="col-md-6 col-sm-6">
                        <button class="btn btn-default btn-block" onclick="createChart(false, null, true);">基础设置
                        </button>
                    </div>
                </div>
                <button class="btn btn-blue btn-block" onclick="createChart(true);">保存图表</button>
            <#--<button class="btn btn-grey btn-block" onclick="go2ChartList();">返回</button>-->

                <!-- hidden parameters-->
                <input type="hidden" id="chart_id" value="${chart.id!}"/>
                <input type="hidden" id="chart_unitId" value="${chart.unitId!}"/>
                <input type="hidden" id="order_type" value="${chart.orderType!}"/>
                <input type="hidden" id="datasourceId" value="${datasourceId!}"/>
            </div>
        </div>
    </div>

    <div class="chart-show" >
        <div id="main" class="chart-echart-show"></div>
    </div>
</div>

<div class="layer layer-save">
    <div class="filter-item block">
        <span class="filter-name">图表名称　</span>
        <div class="filter-content">
            <input type="text" id="chart_name" value="${chart.name!}" class="form-control width-300"
                   placeholder="请输入图表名称">
        </div>
    </div>
<#--<div class="filter-item block">-->
<#--<span class="filter-name">使用类型　</span>-->
<#--<div class="filter-content">-->
<#--<input type="text" id="chart_name" value="${chart.name!}" class="form-control width-300"-->
<#--placeholder="请输入图表名称">-->
<#--</div>-->
<#--</div>-->
    <div id="tagsList">
        <div class="filter-item block">
            <span class="filter-name">设置标签　</span>
            <div class="">
                <select multiple name="" id="tag_selection" class="form-control chosen-select"
                        data-placeholder="<#if tagVOS?exists && tagVOS?size gt 0>请选择合适的标签<#else>没有可选标签，去新建一个吧</#if>"
                        style="width:300px;">
                <#if tagVOS?exists && tagVOS?size gt 0>
                    <#list tagVOS as tag>
                        <option value="${tag.tagId!}" <#if tag.selected?default(false)>selected</#if>>${tag.tagName}</option>
                    </#list>
                </#if>
                </select>
            </div>
        </div>
        <#--<div class="filter-item block add-label">-->
            <#--<div class="filter-content">-->
                <#--<span>新增标签</span>-->
            <#--</div>-->
        <#--</div>-->
    </div>
</div>
<#--<script src="${request.contextPath}/static/bigdata/js/jquery/jquery.min.js"></script>-->
<script src="${request.contextPath}/static/bigdata/js/echarts/echarts.min.js"></script>
<script src="${request.contextPath}/static/bigdata/js/echarts/myCharts.js"></script>
<script src="${request.contextPath}/static/bigdata/js/html2canvas.0.4.12/html2canvas.min.js"></script>
<script src="${request.contextPath}/static/bigdata/js/chart.edit.js"></script>
<script src="${request.contextPath}/static/bigdata/editor/ace.js"/>
<script src="${request.contextPath}/static/bigdata/editor/ext-language_tools.js"/>
<script src="${request.contextPath}/static/bigdata/editor/mode-json.js"/>
<script src="${request.contextPath}/static/bigdata/editor/mode-sql.js"/>
<script src="${request.contextPath}/static/bigdata/editor/theme-xcode.js"/>
<script>
    $(function () {
        changeDatasourceType($('#datasource_type'), $('#datasourceId').val());
        $('.chosen-container').width('180').css('float', 'right');
        // $('#map_name').on('change', function (event, params) {
        //     var map_name = params.selected;
        //     var mapEchart = echarts.init(document.getElementById('main'));
        //     $.get(_contextPath + '/static/bigdata/js/echarts/map/json/province/' + map_name + '.json', function (geoJson) {
        //         //地图涂层加载
        //         mapEchart.hideLoading();
        //         echarts.registerMap(map_name, geoJson);
        //         //createChart(false, '91', false);
        //         mapEchart.setOption({
        //             title: {
        //                 text: ''
        //             },
        //             series:[
        //                 {
        //                     type: 'map',
        //                     mapType: map_name,
        //                     itemStyle:{
        //                         normal:{label:{show:true}},
        //                         emphasis:{label:{show:true}}
        //                     }
        //                 }
        //             ]
        //         });
        //     });
        // });
    })
    // function changeEditor(type) {
    //     var aceEditor = ace.edit('ace_editor');
    //     if (type === '-1') {
    //
    //     }
    //
    //     aceEditor.session.setMode("ace/mode/json");
    // }
    //更新数据源类型需要更新数据源列表选择
    function changeDatasourceType(el, datasourceId) {
        var type = $(el).val();
        //changeEditor(type);
        //静态数据源不需要选择
        if (type === '-1') {
            $('#ace_editor').hide();
            $('#text_blank').hide();
            $('#datasource_id').parent('div.filter-item').hide();
            return;
        }
        else if (type === '1') {
            $('#text_blank').hide();
            $('#datasource_id').parent('div.filter-item').show();
            $('#ace_editor').show().attr('placeholder', '<请在此插入SQL>');
        }
        else if (type === '2') {
            $('#ace_editor').hide();
            $('#text_blank').height($('#sidebar').height() - 307).show();
            $('#datasource_id').parent('div.filter-item').show();
        }
        else if (type === '3') {
            $('#datasource_id').parent('div.filter-item').hide();
            $('#text_blank').hide();
            $('#data_set').attr('placeholder', '<请在此插入数据>').parent().show();
            return;
        }

        $.ajax({
            url: '${request.contextPath}/bigdata/datasource/' + type,
            type: 'GET',
            dataType: 'json',
            success: function (val) {
                if (!val.success) {
                    layer.msg(val.message, {icon: 2});
                }
                else {
                    //更新
                    var optionText = '';
                    $.each(val.data, function (n, e) {
                        var selected = '';
                        if (e.id === datasourceId) {
                            selected = 'selected';
                        }
                        optionText += '<option value="' + e.id + '"' + selected + '>' + e.name + '</option>';
                    });
                    $('#datasource_id').html(optionText);
                }
            }
        });
    }

    //是否自动更细
    function autoUpdate() {
        var autoUpdate = $('#chart_autoUpdate').val();
        if (autoUpdate === '1') {
            $('#chart_updateInterval').parent('div').show()
        } else {
            $('#chart_updateInterval').val(60).parent('div').hide();
        }
    }

    function nextStep(data) {
        $.post(_contextPath + '/bigdata/chart/nextStep', data, function (response) {
            try {
                var val = JOSN.parse(response);
                if (!val.success) {
                    //error
                }
            } catch (e) {
                // html
                $('#chart_container').parent().html(response);
            }
        })
    }

    //运行，查询-解析-生成图表
    function createChart(save, type, next) {
        type = type || $('img.border-active').attr('type');
        var templateId = $('img.border-active').attr('templateId');
        var data = {
            'id': $('#chart_id').val(),
            'unitId': $('#chart_unitId').val(),
            'autoUpdate': $('#chart_autoUpdate').val(),
            'updateInterval': $('#chart_updateInterval').val(),
            'name': $('#chart_name').val(),
            'datasourceType': $('#datasource_type').val(),
            'datasourceId': $('#datasource_id').val(),
            'dataSet': $('#data_set').val(),
            'type': type,
            'templateId': templateId,
            'orderType': $('#order_type').val()
        };
        if (data.autoUpdate === '0') {
            data.updateInterval = 0;
        }

        if (!checkVal(data)) {
            return;
        }

        if (next) {
            nextStep(data);
            return;
        }

        if (save) {
            saveChart(data);
            return;
        }
        //在用户查询数据时给定默认的刷新时间，这用可以提供数据的加载速度，提升用户的体验
        if (data.autoUpdate === '0') {
            data.autoUpdate = 1;
            data.updateInterval = 60;
        }
        console.log(data.type)
        var loadding = layer.load(1);
        $.ajax({
            url: '${request.contextPath}/bigdata/chart/query',
            data: data,
            type: 'POST',
            dataType: 'json',
            success: function (val) {
                try {
                    if (!val.success) {
                        layer.msg(val.message, {icon: 2});
                    }
                    else {
                        //生成echarts图表
                        var op = JSON.parse(val.data);
                        console.log(op.echarts);
                        if (op.echarts) {
                            //再次判断区分地图
                            //根据不同的地图获取GeoJson数据，再初始化数据
                            var echart_div = echarts.init(document.getElementById('main'));
                            echart_div.setOption(op.option);
                        }
                        else {
                            myChart.init(document.getElementById("main"), op.option, op.chartType, data.title, true);
                        }
                        $('#chart_type_list').attr('chart-show', 'true')
                    }
                } catch (e) {
                    console.log(e);
                } finally {
                    layer.close(loadding);
                }

            },
            error: function () {
                layer.close(loadding);
            }
        });
    }

    function createScreenshot() {
        $('#main').find("canvas").css("background-color", "#ffffff");
        //延时执行，让页面渲染完成
        setTimeout(function () {
            html2canvas($('#main')[0], {
                useCORS: true,
                background: '#fff',
                onrendered: function (canvas) {
                    var data = canvas.toDataURL("image/jpeg");
                    $('#main').find("canvas").css("background-color", "");
                    $.ajax({
                        type: "POST",
                        url: '${request.contextPath}/bigdata/chart/upload/screenshot',
                        data: {
                            data: data,
                            chartId: $('#chart_id').val()
                        },
                        timeout: 60000,
                        success: function (data) {

                        }
                    });
                }
            });
        }, 1000);
    }

    function saveChart(data) {
        var templateId = $('img.border-active').attr('templateId');
        //e.preventDefault();
        var index = layer.open({
            type: 1,
            title: '保存图表',
            shade: .5,
            shadeClose: true,
            btn: ['确定', '取消'],
            area: '410px',
            content: $('.layer-save'),
            yes: function (save_index) {
                var chartName = $('#chart_name').val();
                if (!chartName || $.trim(chartName) === '') {
                    tips('图表名称不能为空', '#chart_name');
                    return
                }
                if ($.trim(chartName).length > 50) {
                    tips('图表名称最大长度为50个字符', '#chart_name')
                    return;
                }
                var toLength = false;
                var tags = $('#tag_selection').val();

                // if (tags.length == 0) {
                //     tips("请输入标签", ".chosen-container");
                //     return;
                // }

                data.name = chartName;
                //区分新增和非新增的tag
                data.tags = tags;
                $.ajax({
                    url: '${request.contextPath}/bigdata/chart/save',
                    data: data,
                    type: 'POST',
                    dataType: 'json',
                    success: function (val) {
                        layer.close(save_index);
                        if (!val.success) {
                            layer.msg(val.message, {icon: 2, time: 2000});
                        }
                        else {
                            $('#chart_id').val(val.data);
                            if (templateId == null) {
                                createScreenshot();
                            } else {
                                createReportScreen();
                            }
                            $('#tagsList').load('${request.contextPath}/bigdata/chart/layer/tags?chartId=' + $('#chart_id').val());
                            showLayerTips('success', '保存成功', 't');
                        }
                    },
                    error: function () {
                        $('#tagsList').load('${request.contextPath}/bigdata/chart/layer/tags?chartId=' + $('#chart_id').val());
                    }
                });
            }
        });
        // $('#tag_selection').next().find("ul.chosen-choices input").click();
        // $("div.layui-layer .layui-layer-page").click();
        layer.style(index, {
            'border-radius': '15px'
        });
        $(document).find('#tag_selection').css({width: '300px', height: '36px'});
        $('#tag_selection').next().css({width: '300px', height: '36px'});
        $(document).find('.chosen-container-multi .chosen-choices').css('padding', '0px');
    }

    function checkVal(data) {
        var success = true;
        if (!data.type || $.trim(data.type) === '') {
            document.getElementById('datasource_tab').click();
            success = false;
            tips('请选择图表类型', '#chart_type_list');
        }
        if (!data.datasourceType || $.trim(data.datasourceType) === ''
                || $.trim(data.datasourceType) === '-1') {
            document.getElementById('datasource_tab').click();
            success = false;
            tips('数据源类型不能为空', '#datasource_type');
        } else {
            //检查datasourceId
            if ($.trim(data.datasourceType) !== '3') {
                if (!data.datasourceId || $.trim(data.datasourceId) === '') {
                    document.getElementById('datasource_tab').click();
                    success = false;
                    tips('数据源不能为空', '#datasource_id');
                }
                if (data.datasourceType === '1' && (!data.dataSet || $.trim(data.dataSet) === '')) {
                    document.getElementById('datasource_tab').click();
                    success = false;
                    tips('请输入sql', '#data_set');
                }
            } else {
                if (!data.dataSet || $.trim(data.dataSet) === '') {
                    document.getElementById('datasource_tab').click();
                    success = false;
                    tips('静态数据不能为空', '#data_set');
                }
            }
        }

        if (success) {
            /*if (!data.name || $.trim(data.name) === '') {
                //document.getElementById('my_parameters').click();
                tips('图表名称不能为空', '#chart_name');
                success = false;
            }*/
            if (data.autoUpdate === '1') {
                if (data.updateInterval <= 0) {
                    document.getElementById('my_parameters').click();
                    tips('刷新频率 非负', '#chart_updateInterval');
                    success = false;
                }
            }
        }
        return success;
    }

    function tips(msg, key) {
        layer.tips(msg, key, {
            tipsMore: true,
            tips: 3,
            time: 2000
        });
    }

</script>
<script>

</script>