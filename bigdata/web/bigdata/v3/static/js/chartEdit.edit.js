$(function () {
    var chartRun = false;
    $('.scroll-height').each(function(){
        // $(this).css({
        //     height: $(window).height() - $(this).offset().top - 20,
        // })
    });

    $('.left-part>p').click(function () {
        $(this).siblings('.bg-grey').trigger('click');
        $(this).toggleClass('bg-grey');
        $('.left-part>p>i').attr('class', 'arrow fa fa-angle-right');
        $('.left-part>p.bg-grey>i').attr('class', 'arrow fa fa-angle-down');
    });
    $('.collapse img').click(function () {
        $(this).addClass('border-active').siblings().removeClass('border-active');
        $(this).parent().parent('.collapse').siblings('.collapse').find('img').removeClass('border-active');
    });

    //设置高度
    var $height = $('#sidebar').height() - 40;
    //$('.text').find('textarea').height($height - 279);
    $('.text').each(function(){
        $(this).css({
            height: $height -40 - $(this).offset().top - 10
        })
    });
    //$('.blank').height($height - 252);
    $('.left-part').height($height);
    $('.chart-show').height($height);

    var $leftHeight=$('.left-part').height();
    $('.right-part').height($leftHeight);
    $(document).scroll(function(){
        //$('.left-part').height($leftHeight+$(document).scrollTop())
    })

    //中间echarts图表
    $('#main').height($('.chart-show').height() - 100).width($('.chart-show').width() - 20);
    $(window).resize(function () {
        $('#main').height($('.chart-show').height() - 100).width($('.chart-show').width() - 20);
    });
    //初始化返回按钮
    showBreadBack(go2ChartList, true, '图表设置');

    var editor = null;
    if (_chartEdit_json.dataSourceType !=2 ) {

        editor = creteAceEditor(_chartEdit_json.dataSourceType == 3, _chartEdit_json.dataSet);
    }
    var sql_text = null;
    var json_text = null;

    function aceEditor() {
        editor = ace.edit("editor");
        // editor.setTheme("ace/theme/twilight");
        editor.session.setMode("ace/mode/json");
        return editor;
    }

    function destroyAceEditor() {
        if (editor != null) {
            editor.destroy();
            editor = null;
        }
        $('#editor').html("").hide();
    }

    function creteAceEditor(json, value) {
        destroyAceEditor();
        $('#editor').html("").show();
        editor = ace.edit("editor");
        // editor.setTheme("ace/theme/twilight");
        if (json) {
            editor.session.setMode("ace/mode/json");
        } else {
            editor.session.setMode("ace/mode/sql");
        }
        editor.setOption({
            enableBasicAutocompletion: true,
            enableLiveAutocompletion: true
        });
        if (value) {
            editor.session.setValue(value);
        }
        return editor;
    }

    function go2ChartList() {
        openModel('708033', '图表设置', 1, _contextPath + '/bigdata/chart/index', '大数据管理', '数据分析', null, false);
    }

    //初始化下拉选择框
    $('#map').chosen({
        allow_single_deselect: true,
        disable_search_threshold: 10,
        no_results_text: '没找到您输入的地区'
    });
    $('#map').on('change', function () {
        if (chartRun) {
            doGetVal();
            if (!doCheck(_chartEdit_json)) {
                return;
            }
            var index = layer.load(1, {time: 15 * 1000});
            httpInvoker.runChart(_chartEdit_json, function () {
                layer.close(index);
            });
        } else {
            httpInvoker.doGetMap($(this).val());
        }
    })
    //数据源类型
    $('#select_datasourceType').chosen({
        allow_single_deselect: true,
        disable_search_threshold: 10,
        no_results_text: '暂时还不支持该数据源类型哦！'
    });
    //标签下拉
    $('#tag_selection').chosen({
        allow_single_deselect: true,
        disable_search_threshold: 10,
        max_selected_options: 3,
        no_results_text: '没有找到该标签，您可以前往基础设置新增标签'
    });
    $('#select_datasourceId').chosen({
        allow_single_deselect: true,
        disable_search_threshold: 10,
        no_results_text: '没有找到该标签，您可以前往基础设置新增标签'
    });
    $('#chart_autoUpdate').chosen({
        allow_single_deselect: true,
        disable_search_threshold: 10,
        no_results_text: '没有找到该标签，您可以前往基础设置新增标签'
    });
    $(document).ready(function(){
        $('.chosen-container.chosen-container-single').width('73%')
    });
    $('#chart_autoUpdate').on('change', function () {
        var autoUpdate = $(this).val();
        if (autoUpdate == 1) {
            $('.update-inteval').show();
        } else {
            $('.update-inteval').hide();
        }
    });
    $(window).off('resize.chosen').on('resize.chosen', function () {
        $('.chosen-select').each(function () {
            var $this = $(this);
            $this.next().css({'width': $this.width()});
        })
    }).trigger('resize.chosen');
    //绑定事件
    //1 数据源类型选择判定
    $('#select_datasourceType').on('change', function (event, params) {
        var type = $(this).val();
        if (type === '1') {
            $('#text_blank').hide();
            $('#select_datasourceId').parent('div.filter-item').show();
            //$('#dataSet').show().attr('placeholder', '<请在此插入SQL>');
            if (editor != null) {
                json_text = editor.session.getValue();
            }
            creteAceEditor(false, sql_text);
        }
        else if (type === '2') {
            // $('#dataSet').hide();

            if (editor.session.getMode().$id == 'ace/mode/json') {
                json_text = editor.session.getValue();
            } else {
                sql_text = editor.session.getValue();
            }
            destroyAceEditor();
            $('#ace_editor').hide();
            //$('#text_blank').height($('#sidebar').height() - 307).show();
            $('#select_datasourceId').parent('div.filter-item').show();
        }
        else if (type === '3') {
            //设置高度
            $('#select_datasourceId').parent('div.filter-item').hide();
            $('#text_blank').hide();
            //$('#dataSet').attr('placeholder', '<请在此插入数据>').show();
            if (editor != null) {
                sql_text = editor.session.getValue();
            }
            creteAceEditor(true, json_text);
            return;
        }
        httpInvoker.doGetDatasource(type, function (data) {
            var optionText = '';
            $.each(data, function (n, e) {
                optionText += '<option value="' + e.id + '">' + e.name + '</option>';
            });
            $('#select_datasourceId').html(optionText);
            $('#select_datasourceId').chosen("destroy")
            $('#select_datasourceId').chosen({
                allow_single_deselect: true,
                disable_search_threshold: 10,
                no_results_text: '没有找到该标签，您可以前往基础设置新增标签'
            });
            $('.chosen-select').each(function () {
                var $this = $(this);
                $this.next().css({'width': $this.width()});
            })
        })
    });
    //切换不同的图表类型
    $('#chart_type_list').on('click', 'img', function () {

        if ($(this).attr("type") == 91 || $(this).attr("type")==93) {
            $('#map').parent('.filter-item').show();
        } else {
            $('#map').parent('.filter-item').hide();
        }
        if (chartRun) {
            doGetVal();
            if (!doCheck(_chartEdit_json)) {
                return;
            }
            var index = layer.load(1, {time: 15 * 1000});
            httpInvoker.runChart(_chartEdit_json, function () {
                layer.close(index);
            });
        } else {
            if ($(this).attr("type") == 91) {
                runDemo();
            } else {
                if ($('#dataSet').val() =='' && $('#select_datasourceType').val() == '3') {
                    runDemo();
                }
            }

        }

    });

    $('#run_chart').on('click', function () {
        doGetVal();
        if (!doCheck(_chartEdit_json)) {
            return;
        }
        chartRun = true;
        var index = layer.load(1, {time: 15 * 1000});
        httpInvoker.runChart(_chartEdit_json, function () {
            //httpInvoker.createScreenshot(_chartEdit_json.id, _chartEdit_json.chartType);
            setTimeout(function () {
                httpInvoker.createScreenshot2(_chartEdit_json.id, _chartEdit_json.chartType);
            }, 1000);
            layer.close(index);
        });
    });

    $('#basic_edit').on('click', function () {
        doGetVal();
        if (!doCheck(_chartEdit_json)) {
            return;
        }
        $.post(_contextPath + '/bigdata/chart/next', _chartEdit_json, function (response) {
            try {
                var val = JOSN.parse(response);
                if (!val.success) {
                    //error
                }
            } catch (e) {
                $('#chart_container').parent().html(response);
            }
        })
    });

    var save = false;
    $('#chart_save').on('click', function () {
        doGetVal();
        if (!doCheck(_chartEdit_json)) {
            return;
        }
        $.post(_contextPath + '/bigdata/chart/next', _chartEdit_json, function (response) {
            try {
                var val = JOSN.parse(response);
                if (!val.success) {
                    //error
                }
            } catch (e) {
                $('#chart_container').parent().html(response);
            }
        });
        // var index = layer.open({
        //     type: 1,
        //     title: '保存图表',
        //     shade: .5,
        //     shadeClose: true,
        //     btn: ['确定', '取消'],
        //     area: '410px',
        //     content: $('.layer-save'),
        //     yes: function () {
        //         var chartName = $('#chart_name').val();
        //         if (!chartName || $.trim(chartName) === '') {
        //             tips('图表名称不能为空', '#chart_name');
        //             return
        //         }
        //         if ($.trim(chartName).length > 50) {
        //             tips('图表名称最大长度为50个字符', '#chart_name')
        //             return;
        //         }
        //         _chartEdit_json.tags = $('#tag_selection').val();
        //         _chartEdit_json.name = chartName;
        //         //ajax调用保存
        //         httpInvoker.saveChart(_chartEdit_json, function () {
        //             layer.close(index);
        //             layer.msg('保存成功', {icon: 1, time: 1000}, function () {
        //                 go2ChartList();
        //                 layer.closeAll();
        //             });
        //         });
        //     }
        // });
    });


    var currentEcharts = null;
    var httpInvoker = {};
    httpInvoker.runChart = function (chart, call) {
        //TODO 有待进一步区分图表类型
        $.ajax({
            // url: _contextPath + '/bigdata/chart/query',
            url: _contextPath + '/bigdata/echarts/view/editChart',
            data: chart,
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                try {
                    if (!response.success) {
                        layer.msg(response.message, {icon: 2});
                    }
                    else {
                        var op = JSON.parse(response.data);
                        if (op.echarts) {
                            var old = echarts.getInstanceByDom(document.getElementById('main'));
                            if (old != null) {
                                echarts.dispose(document.getElementById('main'));
                            }
                            if (op.map) {
                                httpInvoker.doGetMap(op.mapName, op.option);
                            } else {
                                var echart_div = echarts.init(document.getElementById('main'));
                                echart_div.setOption(op.option);
                                currentEcharts = echart_div;
                            }
                        }
                        else {
                            myChart.init(document.getElementById("main"), op.option, op.chartType, chart.name, true);
                        }
                    }
                } catch (e) {
                    console.log(e);
                } finally {
                    if (typeof call === 'function') {
                        call();
                    }
                }
            }
        });
    };

    httpInvoker.doGetMap = function (regionCode, option) {
        $.get(_contextPath + '/static/bigdata/js/echarts/map/json/province/' + regionCode + ".json", function (geoJson) {
            var old = echarts.getInstanceByDom(document.getElementById('main'));
            if (old != null) {
                echarts.dispose(document.getElementById('main'));
            }
            currentEcharts = echarts.init(document.getElementById("main"));
            currentEcharts.hideLoading();
            echarts.registerMap(regionCode, geoJson);
            if (typeof option === 'undefined') {
                currentEcharts.setOption({
                    series: [
                        {
                            name:'',
                            zoom:1.2,
                            type: 'map',
                            mapType: regionCode,
                            label: {show: true}
                        }
                    ]
                });
            } else {
                option.series[0].mapType = regionCode;
                currentEcharts.setOption(option);
            }
        });
    };

    httpInvoker.doGetTemplateData = function(type, call) {
        $.ajax({
           url: _contextPath + '/bigdata/chart/template/data',
           data: {
               chartType: type,
               mapName:65,
           },
            dataType: 'json',
            success: function (val) {
                if (val.success) {
                    call(val.data);
                }
            }
        });
    };

    httpInvoker.saveChart = function (data, success) {
        $.ajax({
            url: _contextPath + '/bigdata/chart/save',
            data: data,
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    layer.msg(response.message, {icon: 2});
                }
                else {
                    layer.msg('保存成功', {icon: 1, time: 1000});
                    if (typeof success === 'function') {
                        success(response.data);
                    }
                }
            }
        })
    };

    httpInvoker.doGetDatasource = function (type, success) {
        $.ajax({
            url: _contextPath + '/bigdata/datasource/' + type,
            type: 'GET',
            dataType: 'json',
            success: function (response) {
                if (response.success) {
                    if (typeof success === 'function') {
                        success(response.data);
                    }
                }
                else {
                    layer.msg(response.message, {icon: 2});
                }
            }
        });
    };
    httpInvoker.createScreenshot = function (chartId, chartType) {
        var notMyChart = chartType != '98' && chartType != '99' && chartType!=97;
        if (notMyChart) {
            $('#main').find("canvas").css("background-color", "#ffffff");
        }
        setTimeout(function () {
            var $dom = null;
            if (!notMyChart) {
                $dom = $('#main').children('div')[0];
            } else {
                $dom = $('#main')[0];
            }
            html2canvas($dom, {
                useCORS: true,
                // background: '#ffff',
                onrendered: function (canvas) {
                    var data = canvas.toDataURL("image/jpeg");
                    if (notMyChart) {
                        $('#main').find("canvas").css("background-color", "");
                    }
                    $.ajax({
                        type: "POST",
                        url: _contextPath + '/bigdata/chart/upload/screenshot?chartId=' + chartId,
                        data: {
                            data: data
                        },
                        timeout: 60000,
                        success: function (response) {
                            if (response.success) {
                                _chartEdit_json.thumbnailPath = response.data;
                            }
                        }
                    });
                }
            });
        }, 500);
    };

    httpInvoker.createScreenshot2 = function (chartId, chartType) {
        var notMyChart = chartType != '98' && chartType != '99';
        if (notMyChart) {
            var data = null;
            if (currentEcharts != null) {
                data = currentEcharts.getDataURL();
            } else {
                return;
            }
            httpInvoker.uploadShot(chartId, data, function () {
            });
        } else {
            html2canvas(document.getElementById('main'), {canvas: $('#main').find('canvas')[0]}).then(function (canvas) {
                var data = canvas.toDataURL("image/jpeg");
                //并将图片上传到服务器用作图片分享
                httpInvoker.uploadShot(chartId, data);
            });
        }
    };

    httpInvoker.uploadShot = function (chartId, data, call) {
        if (typeof chartId === 'undefined') {
            chartId = "";
        }
        $.ajax({
            type: "POST",
            url: _contextPath + '/bigdata/chart/upload/screenshot?chartId=' + chartId,
            data: {
                data: data
            },
            timeout: 60000,
            success: function (response) {
                if (response.success) {
                    _chartEdit_json.thumbnailPath = response.data;
                }
                try {

                } catch (e) {
                    console.log(e);
                } finally {
                    if (typeof call === 'function') {
                        call();
                    }
                }
            }
        });
    }

    /**
     *
     */
    function doGetVal() {
        if (editor != null) {
            $('#dataSet').text(editor.session.getValue());
        }
        _chartEdit_json.updateInterval = $('#chart_updateInterval').val();
        _chartEdit_json.autoUpdate = $('#chart_autoUpdate').val();
        _chartEdit_json.chartType = $('img.border-active').attr('type');
        _chartEdit_json.dataSourceType = $('#select_datasourceType').val();
        if/*数据库*/ (_chartEdit_json.dataSourceType == '1') {
            _chartEdit_json.databaseId = $('#select_datasourceId').val();
            _chartEdit_json.apiId = null;
        } /*API*/ else if (_chartEdit_json.dataSourceType == '2') {
            _chartEdit_json.apiId = $('#select_datasourceId').val();
            _chartEdit_json.databaseId = null;
        } else {
            _chartEdit_json.databaseId = null;
            _chartEdit_json.apiId = null;
        }
        _chartEdit_json.dataSet = $('#dataSet').val();
        //若图表是新增的则默认该图表是私有的
        if (typeof _chartEdit_json.orderType === 'undefined' || _chartEdit_json.orderType == 0) {
            _chartEdit_json.orderType = 1;
        }
        if (_chartEdit_json.chartType == 91 || _chartEdit_json.chartType==93) {
            _chartEdit_json.map = $('#map').val();
        }
    }

    function doCheck(data) {
        var success = true;
        if (!data.chartType || $.trim(data.chartType) === '') {
            document.getElementById('datasource_tab').click();
            success = false;
            tips('请选择图表类型', '#chart_type_list');
        }
        if (!data.dataSourceType || $.trim(data.dataSourceType) === ''
            || $.trim(data.dataSourceType) === '-1') {
            document.getElementById('datasource_tab').click();
            success = false;
            tips('数据源类型不能为空', '#select_datasourceType');
        } else {
            //检查datasourceId
            if ($.trim(data.dataSourceType) != '3') {
                var nullDatasourceId = data.databaseId || data.apiId;
                if (!nullDatasourceId) {
                    document.getElementById('datasource_tab').click();
                    success = false;
                    tips('数据源不能为空', '#select_datasourceId_chosen');
                }
                if (data.dataSourceType == '1' && (!data.dataSet || $.trim(data.dataSet) == '')) {
                    document.getElementById('datasource_tab').click();
                    success = false;
                    tips('请输入sql', '#editor');
                }
            } else {
                if (!data.dataSet || $.trim(data.dataSet) == '') {
                    document.getElementById('datasource_tab').click();
                    success = false;
                    tips('静态数据不能为空', '#editor');
                }
            }
        }

        if (success) {
            if (data.autoUpdate == '1') {
                if (data.updateInterval <= 0) {
                    document.getElementById('my_parameters').click();
                    tips('刷新频率 非负', '#chart_updateInterval');
                    success = false;
                }
            }
            if (data.chartType == 91) {
                if (!data.map || $.trim(data.chartType) == '') {
                    document.getElementById('my_parameters').click();
                    tips('刷新频率 非负', '#map');
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

    //
    if (_chartEdit_json.id && _chartEdit_json.id != '' || $('#run_run').val()=='true') {
        var index = layer.load(1, {time: 15 * 1000});
        httpInvoker.runChart(_chartEdit_json, function () {
            chartRun = true;
            layer.close(index);
        });
    } else {
        runDemo();
    }

    function runDemo() {
        httpInvoker.doGetTemplateData($('img.border-active').attr('type'), function (data) {
            //$('#dataSet').attr('placeholder', JSON.stringify(data.data));
            editor.session.setValue(JSON.stringify(data.data, null, 2));
            json_text = editor.session.getValue();
            httpInvoker.runChart(data.chart);
        })
    }
});