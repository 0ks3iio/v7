$(function () {
    // showBreadBack(function () {
    //     openModel('708033', '图表设置', 1, _contextPath + '/bigdata/chart/index', '大数据管理', '数据分析', null, false);
    // }, true, '图表设置');

    var step_number = 1;

    /**
     * 计算左侧的高度
     */
    function resize() {
        console.log("resize")
        $('.scroll-height').each(function () {
            $(this).css({
                height: $(window).height() - $(this).offset().top,
                overflow: 'auto'
            })
        });
        $('.scroll-height-inner').each(function () {
            $(this).css({
                height: $(window).height() - $(this).offset().top - 57,
                overflowY: 'auto',
                overflowX: 'hidden'
            })
        });
        $('.js-inner-height').each(function () {
            $(this).css({
                height: /*.left-part 重复了*/$('.left-part-echarts-parameters').height(),
                overflow: 'auto'
            })
        });
        $('.scroll-height-area').each(function () {
            $(this).css({
                height: $(window).height() - $(this).offset().top - 57 -124  - 20 - 15 - 5,
                overflow: 'auto'
            })
        });
        $('.js-height-93').each(function () {
            $(this).css({
                height: 93,
                overflow: 'auto'
            })
        });
    }

    //初始化的时候刷新高度
    resize();

    //步骤选择
    $('a.p-step').click(function () {
        var step = $(this).attr("step");
        //当
        //由数据源步骤点击下一步时需要参数校验
        if (step === 'echarts_parameters') {
            if (!datasourceVerify()) {
                return;
            } else {
                //get option and render
            }
        }
        var current_step = parseInt($(this).find('span.step-big').text());
        if (current_step > step_number) {
            step_number = current_step;
        }

        $(this).find('span.step-big').addClass('look');
        $(this).parents('li').siblings().find('.step-big').removeClass('look');

        //选择不同的步骤，显示不同的页面
        if ((step === 'echarts_parameters' || step === 'tag_authorization') && data_changed) {
            //run and next
            doCreateOption(true, function (ex) {
                doRender(ex);
                loadParameters(true, resize);
                data_changed = false;
                $('#body_' + step).show().siblings('._step').hide();
                if (step === 'echarts_parameters') {
                    $('#next_tag_authorization').show().siblings('.next-button').hide();
                } else {
                    $('#next_save').show().siblings('.next-button').hide();
                }
            }, function () {
                //has error
            });
        }
        else {
            $('#body_' + step).show().siblings('._step').hide();
            resize();
            switch (step) {
                case 'template':
                    $('#next_datasource').show().siblings('.next-button').hide();
                    break;
                case 'datasource':
                    if (aceEditor != null) {
                        aceEditor.resize();
                    }
                    $('#next_echarts_parameters').show().siblings('.next-button').hide();
                    break;
                case 'echarts_parameters':
                    $('#next_tag_authorization').show().siblings('.next-button').hide();
                    break;
                case 'tag_authorization':
                    $('#next_save').show().siblings('.next-button').hide();
                    break;
            }
        }
        //下一步按钮
    })
    //$('.step-big').click();

    //显示数据源配置
    $("#next_datasource").on('click', function () {
        $("a[step='datasource']").click();
    });
    //显示数据源配置页面
    $("#next_echarts_parameters").on('click', function () {
        $("a[step='echarts_parameters']").click();
    });
    $('#next_tag_authorization').on('click', function () {
        $('a[step="tag_authorization"]').trigger('click');
    })
    $("#next_save").on('click', function () {
        doSave();
        // $.ajax({
        //     url: _contextPath + '/bigdata/chart/next',
        //     dataType: 'text',
        //     contentType:'application/json;charset=UTF-8',
        //     type: 'post',
        //     data: JSON.stringify(oe_dom.doGetVal()),
        //     success: function (response) {
        //         try {
        //             var val = JOSN.parse(response);
        //             if (!val.success) {
        //                 //error
        //             }
        //         } catch (e) {
        //             $('#chart_container').parent().html(response);
        //         }
        //     },
        //     error: function () {
        //
        //     }
        // });
    });

    $('.left-part-two>p').click(function () {
        var index = $(this).index();
        $(this).addClass('active').siblings().removeClass('active');
        $(this).parent().next().find('.litimg').eq(index).removeClass('none').siblings().addClass('none')
    });
    //选择不同的配置，刷新高度问题
    // $('#echarts_parameters_tab').on('click', 'a', resize);

    //选择图表
    $('.imgs-choice .litimg img').each(function (index, ele) {
        $(ele).click(function () {
            $(this).addClass('active').siblings().removeClass('active');
            $(this).parent().siblings().find('img').removeClass('active');
            var type = $(this).attr('type');
            //
            if ($('#datasourceType').val() != '3') {
                $('#datasourceType').val("3").trigger('change');
            }
            loadDemoData(type);
        });
    });

    $('.imgs-choice .litimg img').on('click', function () {
        //切换不同的图表类型，清空demo数据，清空图表
            // layer.confirm('切换模版数据源配置将还原', function (index) {
            //     if (aceEditor != null) {
            //         aceEditor.session.setValue("");
            //     }
            //     layer.close(index);
            // });

        //当图表类型是地图时，数据源显示地图选项
    });

    function loadDemoData(type) {
        if (step_number > 1) {
            var ec = echarts.getInstanceByDom(document.getElementById('main'));
            if (ec != null) {
                ec.dispose();
                ec = null;
                $('#main').html('');
            }
            if (aceEditor != null) {
                aceEditor.session.setValue("");
            }
        }
        if (type == 91 || type == 93) {
            $('#map').parent().parent().show();
            $('#city_region').show();
        } else {
            $('#map').parent().parent().hide();
            $('#city_region').hide();
        }

        // $('#data-demo').click();
        getDemoDataAndRun(false, function () {
            loadParameters(false);
        });
        //不同的图表类型加载不同的配置页面



        // $('#body_echarts_parameters').load(_contextPath + "/bigdata/chart/parameters/echarts?type=" + type, onLinsten_echarts_parameters);
    }

    function loadParameters(expose, call) {
        var ct = oe_dom.doGetVal();
        if (!expose || ct.optionExpose.colors.length == 0) {
            ct.optionExpose = {};
        }
        $.ajax({
            url: _contextPath + "/bigdata/chart/parameters/echarts?type=" + ct.chartType,
            data:JSON.stringify(ct),
            contentType:'application/json;charset=UTF-8',
            type:'POST',
            dataType:'text',
            success: function (val) {
                $('#body_echarts_parameters').html(val);
                onLinsten_echarts_parameters();
                if (typeof call === 'function') {
                    call();
                }
            }
        });
    }

    function render_real_time() {
        console.log("render real time")
        let datasourceType = $('#datasourceType').val();
        if (datasourceType == 2) {
            //run and return
            doCreateOption(true, doRender);
            return;
        }
        let isStatic = datasourceType == 3;
        let isJson = false;
        try{
            let text = aceEditor.session.getValue();
            if ($.trim(text) == '' ) {
                return;
            }
            JSON.parse(text);
            isJson = true;
        }catch (e) {
            isJson = false;
        }

        let isStaticAndJson = isStatic && isJson;
        let isSqlAndNotJson = !isStatic && !isJson;
        if (isSqlAndNotJson || isStaticAndJson) {
            doCreateOption(true, doRender, function () {

            })
        }
    }

    //**************************************数据源部分start**************************************
    $('.js-nav li').click(function () {
        $('.area-height').height($('#aa .text').height())
    });

    $('#map').chosen({
        allow_single_deselect: true,
        disable_search_threshold: 10,
        no_results_text: '没找到您输入的地区'
    }).change(function () {
        changeRegionCode();
    });

    function changeRegionCode() {
        if ($('#map').val() == '00') {
            $('#map_sub').val("")
            doCreateOption(true, doRender);
        }
        loadCity(function () {
            doCreateOption(true, doRender);
        });
    }

    function loadCity(call) {
        let map = $('#map').val();
        if (map == '00') {
            $('#city_region').hide();
            return;
        } else {
            $('#city_region').show();
        }
        $('#city_region').load(_contextPath + '/bigdata/city/' + map + "?selectedRegionCode=" + $('#city_region').attr('regionCode'), function () {
            $('#map_sub').chosen({
                allow_single_deselect: true,
                disable_search_threshold: 10,
                no_results_text: '没找到您输入的地区'
            }).change(function () {
                doCreateOption(true, doRender)
            })
            if (typeof call === 'function') {
                call();
            }
        });
    }

    $(window).off('resize.chosen').on('resize.chosen', function () {
        $('.chosen-select').each(function () {
            var $this = $(this);
            $this.next().css({'width': $this.width()});
        })
    }).trigger('resize.chosen');

    var jsonText = "";
    var sqlText = "";

    $('#datasourceType').on('change', function () {
        var datasourceType = $(this).val();
        //静态数据
        if (datasourceType == 3) {
            if (aceEditor != null) {
                sqlText = aceEditor.session.getValue();
            }
            createAceEditor(true, jsonText);
            $('#api').parent('div').parent().hide();
            //$('#data-demo').show();
            render_real_time();
        }
        //API
        else if (datasourceType == 2) {
            if (aceEditor != null) {
                if (aceEditor.session.getMode().$id == 'ace/mode/json') {
                    jsonText = aceEditor.session.getValue();
                } else {
                    sqlText = aceEditor.session.getValue();
                }
            }
            destroyAceEditor();
            $('#api').show().siblings('select').hide().parent('div').parent().show();
            //$('#data-demo').hide();
            render_real_time();
        }
        //DB
        else if (datasourceType == 1) {
            if (aceEditor != null) {
                jsonText = aceEditor.session.getValue();
            }
            createAceEditor(false, sqlText);
            $('#database').show().siblings('select').hide().parent('div').parent().show();
            //$('#data-demo').hide();
            render_real_time();
        }
    });

    var aceEditor = null;
    var data_changed = false;

    /**
     * 先销毁再重新创建
     * 因为数据源类型发生变化可能是SQL也可能是JSON格式的字符串
     * @param isJsonFormat 是否是JSON格式
     * @param text 需要填充的数据
     */
    function createAceEditor(isJsonFormat, text) {
        //先销毁
        destroyAceEditor();
        $('#editor').show();
        //创建
        aceEditor = ace.edit("editor");
        if (isJsonFormat) {
            aceEditor.session.setMode("ace/mode/json");
        } else {
            aceEditor.session.setMode("ace/mode/sql");
        }
        //设置默认数据
        if (text) {
            aceEditor.session.setValue(text);
        }
        aceEditor.on('change', function (data) {
            data_changed = true;
            if (data.id != 1) {
                render_real_time();
            }
        });
    }

    function destroyAceEditor() {
        if (aceEditor != null) {
            aceEditor.destroy();
            aceEditor = null;
        }
        //隐藏输入框
        $('#editor').html("").hide();
    }

    $('#data-demo').on('click', function () {
        if (aceEditor != null) {
            getDemoDataAndRun(true);
        }
    });

    function getDemoDataAndRun(expose, call) {
        $.ajax({
            url: _contextPath + '/bigdata/chart/template/data',
            data: {
                chartType: $('img.active').attr('type'),
                mapName: 65,
            },
            dataType: 'json',
            success: function (val) {
                createAceEditor(true, JSON.stringify(val.data.data, null, 2))
                doCreateOption(expose, doRender);
                if (typeof call === 'function') {
                    call();
                }
            },
            error: function () {
                layer.msg("网络异常", {icon: 2});
            }
        });
    }
    //**************************************数据源部分end**************************************


    //**************************************参数检验部分start**************************************
    /**
     * 校验数据源参数
     * @return false has error true is ok
     */
    function datasourceVerify() {
        var verifyOk = true;
        var dt = $('#datasourceType').val();
        //db
        if (dt == 1) {
            var database = $('#database').val();
            if (!database) {
                layer.tips('请选择数据源', '#database', {
                    tipsMore: true,
                    tips: 3,
                    time: 2000
                });
                verifyOk = false;
            }
            var sql = aceEditor.session.getValue();
            if ($.trim(sql) == '') {
                layer.tips('请输入SQL', '#editor', {
                    tipsMore: true,
                    tips: 3,
                    time: 2000
                });
                verifyOk = false;
            }
        }
        if (dt == 2) {
            var api = $('#api').val();
            if (!api) {
                layer.tips('请选择数据源', '#api', {
                    tipsMore: true,
                    tips: 3,
                    time: 2000
                });
                verifyOk = false;
            }
        }
        if (dt == 3) {
            var json = aceEditor.session.getValue();
            if ($.trim(json) == '') {
                layer.tips('请输入静态数据', '#editor', {
                    tipsMore: true,
                    tips: 3,
                    time: 2000
                });
                verifyOk = false;
            }
        }

        return verifyOk;
    }

    /**
     * echarts参数校验
     */
    function echartsParametersVerify() {

    }

    /**
     * 标签授权的参数校验
     */
    function tagAuthorizationVerify() {
        var chartName = $.trim($('#chart_name').val());
        if (chartName == '') {
            layer.tips('图表名称不能为空', '#chart_name', {
                tipsMore: true,
                tips: 3,
                time: 2000
            });
            return false;
        }
        if ($('#folderId').val() == null) {
            layer.tips('请选择文件夹', '#folderId', {
                tipsMore: true,
                tips: 3,
                time: 2000
            });
            return false;
        }
        if (chartName.length > 50) {
            layer.tips('图表名称名称过长', '#chart_name', {
                tipsMore: true,
                tips: 3,
                time: 2000
            });
            return false;
        }
        return true;
    }

    function otherVerify() {

    }

    //**************************************参数检验部分end**************************************


    //**************************************后台交互部分start**************************************
    /**
     * 后台交互获取Charts对应的Option数据
     */
    var optionEx = null;

    function doCreateOption(expose, call, error, parent, parentParent) {
        var chart = oe_dom.doGetVal();
        if (!expose) {
            chart.optionExpose = {};
        }
        chart.parent = parent ? parent : null;
        chart.parentParent = parentParent ? parentParent : null;

        $.ajax({
            url: _contextPath + '/bigdata/echarts/view/editChart',
            data: JSON.stringify(chart),
            contentType:'application/json;chaset=UTF-8',
            type: 'POST',
            dataType: 'json',
            success: function (res) {
                if (res.success) {
                    //渲染图表
                    var op = JSON.parse(res.data);
                    optionEx = op;
                    if (typeof call === 'function') {

                        call(op);
                    }
                } else {
                    layer.msg(res.message, {icon: 2});
                    if (typeof  error === 'function') {
                        error();
                    }
                }
            },
            error: function () {
                layer.msg("网络异常，请检查您的网络是否正常", {icon: 2});
            }
        });
    }

    //var ec_instance = null;
    /**
     * 渲染图表
     * 先destroy 再init
     * @param optionEx 扩展的option，不同于Echarts Option
     */
    function doRender(optionEx, call) {
        $('#main').height($('#main').width());
        var op = optionEx;
        if (op.echarts) {
            var old = echarts.getInstanceByDom(document.getElementById('main'));
            if (old != null) {
                echarts.dispose(document.getElementById('main'));
            }
            if (op.map) {
                registerMap(op.mapName, function () {
                    var echart_div = echarts.init(document.getElementById('main'));
                    echart_div.setOption(op.option);
                    ec_instance = echart_div;
                    if (typeof call === 'function') {
                        call(echart_div);
                    }
                    ec_instance.on('click', function (params) {
                        params.parentParent = op.mapName;
                        if (isMapClick(op.mapName) && params.name != "" && params.seriesType == 'map') {
                            mapClick(params);
                        }
                    });
                    ec_instance.on('contextmenu', function (params) {
                        params.top = op.top;
                        params.parentParent = op.parentParent;
                        params.parent = op.parentMap;
                        let bp = isBack(params);
                        if (bp.back) {
                            bp.name = bp.parent;
                            bp.parent = bp.parentParent;
                            mapClick(bp)
                        }
                    })
                });
            } else {
                var echart_div = echarts.init(document.getElementById('main'));
                echart_div.setOption(op.option);
                ec_instance = echart_div;
                if (typeof call === 'function') {
                    call(echart_div);
                }
            }
        }
        else {
            myChart.init(document.getElementById("main"), op.option, op.chartType, "", true);
        }
    }

    function isMapClick(parent) {
        if (parent.length == 4) {
            return false;
        }
        if (/11|12|31|50|81|82/.test(parent)) {
            return false;
        }
        return true;
    }

    function isBack(params) {
        let bp  = {};
        if (!params.parent) {
            bp.back = false;
            return bp;
        }
        if (params.top == params.parentParent) {
            bp.back = true;
            bp.parent = params.top;
            bp.parentParent = params.top;
            return bp;
        }
        if (params.top == '00') {
            if (params.parent.length == 4 && params.parentParent.length == 2) {
                bp.back = true;
                bp.parent = params.parentParent;
                bp.parentParent = '00';
                return bp;
            }
        }
    }

    /**
     * 下钻
     */
    function mapClick(params) {
        doCreateOption(true, function (optionEx) {
            doRender(optionEx)
        }, function () {

        }, params.name, params.parentParent)
    }

    function registerMap(mapName, call) {
        //避免重复请求
        if (echarts.getMap(mapName) == null) {
            $.get(_contextPath + '/static/bigdata/js/echarts/map/json/province/' + mapName + ".json", function (geoJson) {
                echarts.registerMap(mapName, geoJson);
                if (typeof call === 'function') {
                    call();
                }
            });
        } else {
            if (typeof call === 'function') {
                call();
            }
        }
    }

    var oe_dom = {};

    var saved = false;
    function doSave() {
        if (!tagAuthorizationVerify()) {
            return ;
        }
        if (saved) {
            return;
        }
        var ct = oe_dom.doGetVal();
        saved = true;
        $.ajax({
            url: _contextPath + '/bigdata/chart/save',
            data: JSON.stringify(ct),
            contentType: 'application/json; charset=UTF-8',
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    saved = false;
                    layer.msg(response.message, {icon: 2});
                }
                else {
                    showLayerTips('success', '保存成功！', 't', function () {
                        router.go({
                            path: _contextPath + '/bigdata/chart/index',
                            name: '数据图表设计',
                            level: 2
                        }, function () {
                            // $('.page-content')
                        });
                    });
                }
            }
        })
    }

    oe_dom.doGetVal = function () {
        var ct = {};
        ct = $.extend(ct, _chart_json);
        ct.chartType = $("img.active").attr('type');
        ct.dataSourceType = $('#datasourceType').val();
        if (ct.dataSourceType == '1') {
            ct.databaseId = $('#database').val();
            ct.dataSet = aceEditor.session.getValue();
            ct.apiId = null;
        } else if (ct.dataSourceType == '2') {
            ct.apiId = $('#api').val();
            ct.databaseId = null;
            ct.dataSet = null;
        } else {
            ct.dataSet = aceEditor.session.getValue();
            ct.databaseId = null;
            ct.apiId = null;
        }
        if (ct.chartType == '91' || ct.chartType == '93') {
            console.log($('#map_sub').val())
            if ($('#map_sub').val()) {
                ct.map = $('#map_sub').val();
            } else {
                ct.map = $('#map').val();
            }
        } else {
            ct.map = null;
        }
        ct.updateInterval = $('#updateInterval').val();
        this.doGetTagAuthorization(ct);

        var oe = {};
        this.doGetOptionBase(oe);
        this.doGetLegend(oe);
        this.doGetXY(oe);
        this.doGetTooltip(oe);
        this.doGetSLabel(oe);
        this.doGetCrs(oe);
        this.doGetSeriesVal(oe);
        this.doGetIndicatorVal(oe);
        this.doGetGaugeCrs(oe);
        this.doGetGaugeStyles(oe);
        this.doGetTitle(oe);
        this.doGetMapParameters(oe);
        this.doGetGraphParameters(oe);
        this.doGetGrid(oe);
        ct.optionExpose = oe;
        //文件夹
        ct.folderId = $('#folderId').val();
        ct.orderId = $('#orderId').val();
        //个人授权
        return ct;
    };

    oe_dom.doGetGrid = function(oe) {
        oe.gridLeft = $('#gridLeft').val();
        oe.gridRight = $('#gridRight').val();
        oe.gridTop = $('#gridTop').val();
        oe.gridBottom = $('#gridBottom').val();
    };

    oe_dom.doGetGraphParameters = function(oe) {
        oe.graphSymbol = $('#graphSymbol').attr('v');
        oe.graphSymbolSize = $('#graphSymbolSize').val();
        oe.graphLayout = $('#graphLayout').attr('v');
        oe.force = {};
        oe.force.repulsion = $('#repulsion').val();
        oe.force.edgeLength = $('#edgeLength').val();
        oe.showBreadCrumb = $('#showBreadCrumb').attr('v');
        oe.height = $('#height').val();
        oe.borderColor = $('#borderColor').val();
        oe.borderWidth = $('#borderWidth').val();
        oe.breadColor = $('#breadColor').val();
        oe.left = $('#left').attr('v');
        oe.top = $('#top').attr('v');
        oe.leafDepth = $('#leafDepth').val();
        oe.drillDownIcon = $('#drillDownIcon').val();
        oe.visibleMin = $('#visibleMin').val();
        oe.childrenVisibleMin = $('#childrenVisibleMin').val();
        this.doGetDataCategory(oe);
        this.doGetLinkCategory(oe);
    };

    oe_dom.doGetDataCategory = function(oe) {
        var dataCategories = [];
        $('.datacategory-child').each(function () {
            let dataCategory = {};
            oe_dom.doGetValTempateFunction($(this), dataCategory, true);
            dataCategories.push(dataCategory);
        });
        oe.dataCategories = dataCategories;
    };
    oe_dom.doGetLinkCategory = function(oe) {
        var linkCategories = [];
        $('.linkcategory-child').each(function () {
            let linkCategory = {};
            oe_dom.doGetValTempateFunction($(this), linkCategory, true);
            linkCategories.push(linkCategory);
        });
        oe.linkCategories = linkCategories;
    };

    oe_dom.doGetTagAuthorization = function(ct) {
        var tags = [];
        $('#body_tag_authorization').find('span.selected').each(function () {
            tags.push($(this).attr('tag_id'));
        });
        ct.tags = tags;
        ct.name = $.trim($('#chart_name').val());
        ct.orderType = $('input:radio:checked').val();
        //授权单位 和 人
        var unitArray = [];
        if (ct.orderType == '4') {
            $('.choose-item .unit-tree-item').each(function () {
                unitArray.push($(this).attr("id"));
            });
        }
        var teacherArray = [];
        if (ct.orderType == '6') {
            //选取的教师数据
            $('.choose-item .teacher-user-tree').each(function () {
                teacherArray.push($(this).attr("id"));
            });
        }

        ct.orderUnits = unitArray;
        ct.orderTeachers = teacherArray;
        var isForCockpit = $('#isForCockpit').is(':checked');
        if (isForCockpit) {
            ct.isForCockpit = 1;
        } else {
            ct.isForCockpit = 0;
        }
    };

    oe_dom.doGetOptionBase = function (oe){
        var colors = [];
        $('.js-color-group').find('input').each(function () {
            colors.push($(this).val());
        });
        oe.colors = colors;
        oe.barWidth = $('#barWidth').val();
        oe.barBorderRadius = $('#barBorderRadius').val();
        oe.barGap = $('#barGap').val();
        oe.barCategoryGap = $('#barCategoryGap').val();
        oe.smoothLine = $('#smoothLine').attr('v');
    };

    oe_dom.doGetMapParameters = function(oe) {
        oe_dom.doGetValTemplateNoS($('.mapcommon-parameters'), oe);
    };

    oe_dom.doGetTitle = function(oe) {
        let exposeTitle = {};
        oe_dom.doGetValTemplateNoS($('.title-parameters'), exposeTitle);
        oe.exposeTitle = exposeTitle;
    };

    oe_dom.doGetValTemplateNoS = function(jQueryObj, obj) {
        jQueryObj.find('span').each(function () {
            let id = $(this).attr('id');
            if (id) {
                let setValCall = "obj." + id + "=\"" + $(this).attr('v') + "\"";
                eval(setValCall);
            }
        });
        jQueryObj.find('input').each(function () {
            let id = $(this).attr('id');
            if (id) {
                let setValCall = "obj." + id + "=\"" + $(this).val() + "\"";
                eval(setValCall);
            }
        });
    };

    oe_dom.doGetLegend = function(oe) {
        this.doGetValTempateFunction($('.legend-parameters'), oe);
    };

    oe_dom.doGetXY = function(oe) {
        this.doGetValTempateFunction($('.option-xy'), oe);
    };

    oe_dom.doGetTooltip = function(oe) {
        this.doGetValTempateFunction($('.tooltip-parameters'), oe);
    };

    oe_dom.doGetSLabel = function(oe) {
        //common
        this.doGetValTempateFunction($('.slabel-parameters'), oe);
        //slabels
        var pielabels = [];
        $('.pielabel-child').each(function () {
           var label = {};
           oe_dom.doGetValTempateFunction($(this), label);
           pielabels.push(label);
        });
        oe.exposeLabels = pielabels;
    };
    oe_dom.doGetCrs = function(oe){
        var crs = [];
        $('.crs-child').each(function () {
            var cr = {};
            oe_dom.doGetValTempateFunction($(this), cr);
            crs.push(cr);
        })
        oe.exposeCrs = crs;
    };

    oe_dom.doGetSeriesVal = function(oe) {
        var series = [];
        $('.series-child').each(function () {
            var sc = {};
            oe_dom.doGetValTempateFunction($(this), sc);
            series.push(sc);
        });
        oe.exposeSeries = series;
    };
    oe_dom.doGetIndicatorVal = function(oe) {
        var series = [];
        $('.ict-child').each(function () {
            var sc = {};
            oe_dom.doGetValTempateFunction($(this), sc);
            sc.indicatorName = sc.name;
            series.push(sc);
        });
        oe.exposeIndicators = series;
    };

    oe_dom.doGetGaugeCrs = function(oe) {
        var crs = [];
        $('.gaugecrs-child').each(function () {
            var cr = {};
            oe_dom.doGetValTempateFunction($(this), cr);
            crs.push(cr);
        })
        oe.crs = crs;
    };

    oe_dom.doGetGaugeStyles = function(oe) {
        var styles = [];
        $('.gaugestyle-child').each(function () {
            var style = {};
            oe_dom.doGetValTempateFunction($(this), style);
            styles.push(style);
        })
        oe.gaugeStyles = styles;
    };

    oe_dom.doGetValTempateFunction = function (jQueryObj, oe, isCategory) {
        jQueryObj.find('span').each(function () {
            var id = $(this).attr('id');
            if (id) {
                var setValCall = "oe." + id + "=\"" + $(this).attr('v') + "\"";
                let name = $(this).data('s');
                if (isCategory) {
                    oe.linkCategoryName =  name;
                    oe.dataCategoryName =  name;
                } else {
                    oe.name = name;
                }
                eval(setValCall);
            }
        });
        jQueryObj.find('input').each(function () {
            var id = $(this).attr('id');
            if (id) {
                var setValCall = "oe." + id + "=\"" + $(this).val() + "\"";
                let name = $(this).data('s');
                if (isCategory) {
                    oe.linkCategoryName =  name;
                    oe.dataCategoryName =  name;
                } else {
                    oe.name = name;
                }
                eval(setValCall);
            }
        });
    };




    //**************************************后台交互部分end**************************************


    //**************************************Echarts参数配置start**************************************



    /**
     * 刷新Option默认颜色
     */
    function changeOptionColors(index) {
        //刷新颜色

    }

    var ec = {};
    ec.option = {};

    ec.on = function (id, event) {
        if (ec_instance == null) {
            return;
        }

        //特殊的方法需要特殊处理参数
        var op = null;
        switch (id) {
            case 'exchangeXY':
                if (event.value !== event.oldValue) {
                    this.exchangeXY(event.value);
                }
                break;
            case 'tooltipBackgroundColor':
            case 'tooltipBackgroundColorTransparent':
                var hex =  $("#tooltipBackgroundColor").val();
                var transparent = $('#tooltipBackgroundColorTransparent').val()/10;
                if (transparent > 1 || transparent<0) {
                    this.utils.showError('#tooltipBackgroundColorTransparent', '不透明度在0-10之间');
                    return;
                }
                op = this.tooltipBackgroundColor(this.utils.toRGB(hex, transparent));
                break;
            case 'sLabelBackgroundColor':
            case 'sLabelBackgroundColorTransparent':
                var hex =  $("#sLabelBackgroundColor").val();
                var transparent = $('#sLabelBackgroundColorTransparent').val()/10;
                if (transparent > 1 || transparent<0) {
                    this.utils.showError('#sLabelBackgroundColorTransparent', '不透明度在0-10之间');
                    return;
                }
                op = this.sLabelBackgroundColor(this.utils.toRGB(hex, transparent));
                break;
            case 'sLabelBorderColor':
            case 'sLabelBorderColorTransparent':
                var hex =  $("#sLabelBorderColor").val();
                var transparent = $('#sLabelBorderColorTransparent').val()/10;
                if (transparent > 1 || transparent<0) {
                    this.utils.showError('#sLabelBorderColorTransparent', '不透明度在0-10之间');
                    return;
                }
                op = this.sLabelBorderColor(this.utils.toRGB(hex, transparent));
                break;
            case 'legendBackgroundColorTransparent':
            case 'legendBackgroundColor':
                var hex = $('#legendBackgroundColor').val();
                var transparent = $('#legendBackgroundColorTransparent').val()/10;
                if (transparent > 1 || transparent<0) {
                    this.utils.showError('#legendBackgroundColorTransparent', '不透明度在0-10之间');
                    return;
                }
                op = this.legendBackgroundColor(this.utils.toRGB(hex, transparent));
                break;
            default:
                if (id && id != '') {
                    var function_call = "ec." + id + "(\"" + event.value + "\",\"" + id + "\")";
                    function_call = "ec." + id + "(\"" + event.value + "\",\"" + id + "\",\""+ $.trim(event.s) + "\")";
                    if (event.s) {
                    } else {
                        // function_call = "ec." + id + "(\"" + event.value + "\",\"" + id + "\")";
                    }
                    op = eval(function_call);
                }
        }

        //刷新全局的colors
        if (event.optionColors) {
            var colors = [];
            $('.js-color-group').find('.iColor').each(function () {
                colors.push($(this).val());
            });
            if (optionEx != null) {
                if (parseInt(event.last_index) + 1 > optionEx.option.series.length) {
                    //do nothing
                } else {

                }
                op = {color: colors};
                ec_instance.setOption(op);
            }
        }

        if (op != null) {
            console.log("before op:" + op);
            this.utils.extend(op);
            console.log("after  op:" + op);
        }
    };

    ec.gridLeft = function(value, id) {
        let v = parseInt(value);
        if (value!='' && isNaN(v)) {
            this.utils.showError('#' + id, "不合法的参数");
            return;
        }

        let grids = optionEx.option.grid;
        let ngs = [];
        if (grids.length > 1) {
            for (let i = 0; i < grids.length; i++) {
                if (parseInt(grids[i].left) >= 50) {
                    ngs.push({});
                } else {
                    ngs.push({left:value});
                }
            }
        }else {
            ngs.push({left:value})
        }
        let op ={grid:ngs};
        ec_instance.setOption(op);
        return op;
    };
    ec.gridRight = function(value, id) {
        let v = parseInt(value);
        if (value!='' && isNaN(v)) {
            this.utils.showError('#' + id, "不合法的参数");
            return;
        }

        let grids = optionEx.option.grid;
        let ngs = [];
        if (grids.length > 1) {
            for (let i = 0; i < grids.length; i++) {
                if (parseInt(grids[i].right) >= 50) {
                    ngs.push({});
                } else {
                    ngs.push({right:value});
                }
            }
        } else {
            ngs.push({right:value})
        }
        let op ={grid:ngs};
        ec_instance.setOption(op);
        return op;
    };

    ec.gridBottom = function(value, id) {
        let v = parseInt(value);
        if (value!='' && isNaN(v)) {
            this.utils.showError('#' + id, "不合法的参数");
            return;
        }

        let grids = optionEx.option.grid;
        let ngs = [];
        for (let i = 0; i < grids.length; i++) {
            ngs.push({bottom:value});
        }
        let op ={grid:ngs};
        ec_instance.setOption(op);
        return op;
    };
    ec.gridTop = function(value, id) {
        let v = parseInt(value);
        if (value!='' && isNaN(v)) {
            this.utils.showError('#' + id, "不合法的参数");
            return;
        }

        let grids = optionEx.option.grid;
        let ngs = [];
        for (let i = 0; i < grids.length; i++) {
            ngs.push({top:value});
        }
        let op ={grid:ngs};
        ec_instance.setOption(op);
        return op;
    };

    ec.graphSymbol = function(value) {
        let series = optionEx.option.series;
        let ns = [];
        for (let i = 0; i < series.length; i++) {
            if (series[i].type === 'graph') {
                ns.push({symbol: value});
            } else {
                ns.push({});
            }
        }
        let op = {series: ns};
        ec_instance.setOption(op);
        return op;
    };

    ec.graphSymbolSize = function(value, id) {
        let v = parseInt(value);
        if (isNaN(v)) {
            this.utils.showError('#' + id, "不合法的参数");
            return;
        }
        let series = optionEx.option.series;
        let ns = [];
        for (let i = 0; i < series.length; i++) {
            ns.push({symbolSize: v});
        }
        let op = {series: ns};
        ec_instance.setOption(op);
        return op;
    };
    ec.graphLayout = function(value) {
        let series = optionEx.option.series;
        let ns = [];
        for (let i = 0; i < series.length; i++) {
            if (series[i].type === 'graph') {
                ns.push({layout: value});
            } else {
                ns.push({});
            }
        }
        let op = {series: ns};
        ec_instance.setOption(op);
        return op;
    };
    ec.repulsion = function(value) {
        let v = parseInt(value);
        let series = optionEx.option.series;
        let ns = [];
        for (let i = 0; i < series.length; i++) {
            if (series[i].type === 'graph') {
                ns.push({force: {repulsion: v}});
            } else {
                ns.push({});
            }
        }
        let op = {series: ns};
        ec_instance.setOption(op);
        return op;
    };
    ec.edgeLength = function(value) {
        let v = parseInt(value);
        let series = optionEx.option.series;
        let ns = [];
        for (let i = 0; i < series.length; i++) {
            if (series[i].type === 'graph') {
                ns.push({force: {edgeLength: v}});
            } else {
                ns.push({});
            }
        }
        let op = {series: ns};
        ec_instance.setOption(op);
        return op;
    };
    ec.dataCategorySymbol = function(value, id, s) {
        let category = s;
        let datas = optionEx.option.series[0].data;
        for (let i = 0; i < datas.length; i++) {
            if (datas[i].category == category) {
                datas[i].symbol = value;
            }
        }
        ec_instance.setOption({series:{data:datas}})
    };
    ec.dataCategorySymbolSize = function(value, id, s) {
        if (value ==0 || value == "") {
            return;
        }
        value = parseInt(value);
        let category = s;
        let datas = optionEx.option.series[0].data;
        for (let i = 0; i < datas.length; i++) {
            if (datas[i].category == category) {
                datas[i].symbolSize = value;
            }
        }
        ec_instance.setOption({series:{data:datas}})
    };
    ec.dataCategoryColor = function(value, id, s) {
        let category = s;
        let datas = optionEx.option.series[0].data;
        for (let i = 0; i < datas.length; i++) {
            if (datas[i].category && datas[i].category == category) {
                if (datas[i].itemStyle) {

                } else {
                    datas[i].itemStyle = {};
                }
                datas[i].itemStyle.color = value;
            }
        }
        ec_instance.setOption({series:{data:datas}})
    };
    ec.dataCategoryShowLabel = function(value, id, s) {
        value = this.utils.toBoolean(value);
        let category = s;
        let datas = optionEx.option.series[0].data;
        for (let i = 0; i < datas.length; i++) {
            if (datas[i].category == category) {
                if (datas[i].label) {

                } else {
                    datas[i].label = {};
                }
                datas[i].label.show = value;
            }
        }
        ec_instance.setOption({series:{data:datas}})
    };
    ec.dataCategoryLabelFontSize = function(value, id, s) {
        value = parseInt(value);
        let category = s;
        let datas = optionEx.option.series[0].data;
        for (let i = 0; i < datas.length; i++) {
            if (datas[i].category == category) {
                if (datas[i].label) {

                } else {
                    datas[i].label = {};
                }
                datas[i].label.fontSize = value;
            }
        }
        ec_instance.setOption({series:{data:datas}})
    };
    ec.dataCategoryLabelFontBold = function(value, id, s) {
        value = this.utils.toBoolean(value);
        let category = s;
        let datas = optionEx.option.series[0].data;
        for (let i = 0; i < datas.length; i++) {
            if (datas[i].category == category) {
                if (datas[i].label) {

                } else {
                    datas[i].label = {};
                }
                datas[i].label.fontWeight = value ? 'bold' : 'normal';
            }
        }
        ec_instance.setOption({series:{data:datas}})
    };
    ec.dataCategoryLabelFontItalic = function(value, id, s) {
        value = this.utils.toBoolean(value);
        let category = s;
        let datas = optionEx.option.series[0].data;
        for (let i = 0; i < datas.length; i++) {
            if (datas[i].category == category) {
                if (datas[i].label) {

                } else {
                    datas[i].label = {};
                }
                datas[i].label.fontStyle = value ? 'italic' : 'normal';
            }
        }
        ec_instance.setOption({series:{data:datas}})
    };
    ec.dataCategoryLabelFontColor = function(value, id, s) {
        let category = s;
        let datas = optionEx.option.series[0].data;
        for (let i = 0; i < datas.length; i++) {
            if (datas[i].category == category) {
                if (datas[i].label) {

                } else {
                    datas[i].label = {};
                }
                datas[i].label.color = value ;
            }
        }
        ec_instance.setOption({series:{data:datas}})
        ec_instance.setOption({series:{data:datas}})
    };
    ec.linkCategoryLineColor = function(value, id, s) {
        console.log("colors" + value)
        let links = optionEx.option.series[0].links;
        for (let i = 0; i < links.length; i++) {
            if (links[i].category == s) {
                if (!links[i].lineStyle) {
                    links[i].lineStyle = {};
                }
                links[i].lineStyle.color = value;
            }
        }
        ec_instance.setOption({series:{links:links}});
    };
    ec.linkCategoryLineWidth = function(value, id, s) {
        value = parseInt(value);
        let links = optionEx.option.series[0].links;
        for (let i = 0; i < links.length; i++) {
            if (links[i].category == s) {
                if (!links[i].lineStyle) {
                    links[i].lineStyle = {};
                }
                links[i].lineStyle.width = value;
            }
        }
        ec_instance.setOption({series:{links:links}});
    };
    ec.linkCategoryLineType = function(value, id, s) {
        let links = optionEx.option.series[0].links;
        for (let i = 0; i < links.length; i++) {
            if (links[i].category == s) {
                if (!links[i].lineStyle) {
                    links[i].lineStyle = {};
                }
                links[i].lineStyle.type = value;
            }
        }
        ec_instance.setOption({series:{links:links}});
    };
    ec.linkCategoryShowLabel = function(value, id, s) {
        value = this.utils.toBoolean(value);
        let links = optionEx.option.series[0].links;
        for (let i = 0; i < links.length; i++) {
            if (links[i].category == s) {
                if (!links[i].label) {
                    links[i].label = {};
                }
                links[i].label.show = value;
            }
        }
        ec_instance.setOption({series:{links:links}});
    };
    ec.linkCategoryLabelFontSize = function(value, id, s) {
        value = parseInt(value);
        let links = optionEx.option.series[0].links;
        for (let i = 0; i < links.length; i++) {
            if (links[i].category == s) {
                if (!links[i].label) {
                    links[i].label = {};
                }
                links[i].label.fontSize = value;
            }
        }
        ec_instance.setOption({series:{links:links}});
    };
    ec.linkCategoryLabelFontBold = function(value, id, s) {
        value = this.utils.toBoolean(value);
        let links = optionEx.option.series[0].links;
        for (let i = 0; i < links.length; i++) {
            if (links[i].category == s) {
                if (!links[i].label) {
                    links[i].label = {};
                }
                links[i].label.fontWeight = value ? "bold" : "normal";
            }
        }
        ec_instance.setOption({series:{links:links}});
    };
    ec.linkCategoryLabelFontItalic = function(value, id, s) {
        value = this.utils.toBoolean(value);
        let links = optionEx.option.series[0].links;
        for (let i = 0; i < links.length; i++) {
            if (links[i].category == s) {
                if (!links[i].label) {
                    links[i].label = {};
                }
                links[i].label.fontStyle = value ? "italic" : "normal";
            }
        }
        ec_instance.setOption({series:{links:links}});
    };
    ec.linkCategoryLabelFontColor = function(value, id, s) {
        let links = optionEx.option.series[0].links;
        for (let i = 0; i < links.length; i++) {
            if (links[i].category == s) {
                if (!links[i].label) {
                    links[i].label = {};
                }
                links[i].label.color = value;
            }
        }
        ec_instance.setOption({series:{links:links}});
    };
    ec.leafDepth = function(value, id) {
        value = this.utils.toInt(value);
        if (isNaN(value)) {
            this.utils.showError('#' + id, "不合法的参数");
            return  {};
        }
        let series = optionEx.option.series;
        let ns = [];
        for (let i = 0; i < series.length; i++) {
            if (series[i].type === 'treemap') {
                ns.push({leafDepth: value});
            } else {
                ns.push({});
            }
        }
        let op = {series: ns}
        ec_instance.setOption(op);
        return op;
    };
    ec.drillDownIcon = function(value, id) {
        let series = optionEx.option.series;
        let ns = [];
        for (let i = 0; i < series.length; i++) {
            if (series[i].type === 'treemap') {
                ns.push({drillDownIcon: value});
            } else {
                ns.push({});
            }
        }
        let op = {series: ns}
        ec_instance.setOption(op);
        return op;
    };
    ec.visibleMin = function(value, id) {
        if (value != '') {
            value = this.utils.toInt(value);
            if (isNaN(value)) {
                this.utils.showError('#' + id, "不合法的参数");
                return  {};
            }
        } else {
            value = 10;
        }
        let series = optionEx.option.series;
        let ns = [];
        for (let i = 0; i < series.length; i++) {
            if (series[i].type === 'treemap') {
                ns.push({visibleMin: value});
            } else {
                ns.push({});
            }
        }
        let op = {series: ns}
        ec_instance.setOption(op);
        return op;
    };
    ec.childrenVisibleMin = function(value, id) {
        if (value != '') {
            value = this.utils.toInt(value);
            if (isNaN(value)) {
                this.utils.showError('#' + id, "不合法的参数");
                return  {};
            }
        } else {
            value = null;
        }
        let series = optionEx.option.series;
        let ns = [];
        for (let i = 0; i < series.length; i++) {
            if (series[i].type === 'treemap') {
                ns.push({childrenVisibleMin: value});
            } else {
                ns.push({});
            }
        }
        let op = {series: ns}
        ec_instance.setOption(op);
        return op;
    };
    ec.showBreadCrumb = function(value) {
        value = this.utils.toBoolean(value);
        let series = optionEx.option.series;
        let ns = [];
        for (let i = 0; i < series.length; i++) {
            if (series[i].type === 'treemap') {
                ns.push({breadcrumb:{show: value}})
            } else {
                ns.push({});
            }
        }
        let op = {series: ns}
        ec_instance.setOption(op);
        return op;
    };
    ec.height = function(value, id) {

        value = this.utils.toInt(value);
        if (isNaN(value)) {
            this.utils.showError('#' + id, "不合法的参数");
            return  {};
        }
        let series = optionEx.option.series;
        let ns = [];
        for (let i = 0; i < series.length; i++) {
            if (series[i].type === 'treemap') {
                ns.push({breadcrumb:{height: value}})
            } else {
                ns.push({});
            }
        }
        let op = {series: ns}
        ec_instance.setOption(op);
        return op;
    };
    ec.breadColor = function(value) {
        let series = optionEx.option.series;
        let ns = [];
        for (let i = 0; i < series.length; i++) {
            if (series[i].type === 'treemap') {
                ns.push({breadcrumb:{itemStyle: {color:value}}})
            } else {
                ns.push({});
            }
        }
        let op = {series: ns}
        ec_instance.setOption(op);
        return op;
    };
    ec.borderWidth = function (value, id) {
        value = this.utils.toInt(value);
        if (isNaN(value)) {
            this.utils.showError('#' + id, "不合法的参数");
            return  {};
        }
        let series = optionEx.option.series;
        let ns = [];
        for (let i = 0; i < series.length; i++) {
            if (series[i].type === 'treemap') {
                ns.push({breadcrumb:{itemStyle: {borderWidth:value}}})
            } else {
                ns.push({});
            }
        }
        let op = {series: ns}
        ec_instance.setOption(op);
        return op;
    };
    ec.borderColor = function(value) {
        let series = optionEx.option.series;
        let ns = [];
        for (let i = 0; i < series.length; i++) {
            if (series[i].type === 'treemap') {
                ns.push({breadcrumb:{itemStyle: {borderColor:value}}})
            } else {
                ns.push({});
            }
        }
        let op = {series: ns}
        ec_instance.setOption(op);
        return op;
    };
    ec.top = function(value) {
        let series = optionEx.option.series;
        let ns = [];
        for (let i = 0; i < series.length; i++) {
            if (series[i].type === 'treemap') {
                ns.push({breadcrumb:{top: value}})
            } else {
                ns.push({});
            }
        }
        let op = {series: ns}
        ec_instance.setOption(op);
        return op;
    };
    ec.left = function(value) {
        let series = optionEx.option.series;
        let ns = [];
        for (let i = 0; i < series.length; i++) {
            if (series[i].type === 'treemap') {
                ns.push({breadcrumb:{left: value}})
            } else {
                ns.push({});
            }
        }
        let op = {series: ns}
        ec_instance.setOption(op);
        return op;
    };
    ec.showScatter = function(value) {
        value = this.utils.toBoolean(value);
        let ns = [];
        let series = optionEx.option.series;
        if (value) {
            for (let i = 0; i < series.length; i++) {
                if (series[i].type === 'scatter') {
                    ns.push({symbolSize:$('#scatterSize').val()})
                } else if (series[i].type === 'lines') {
                    ns.push(series[i])
                }
                else {
                    ns.push({});
                }
            }
        } else {
            for (let i = 0; i < series.length; i++) {
                if (series[i].type === 'scatter') {
                    ns.push({symbolSize:0})
                } else if (series[i].type === 'lines') {
                    ns.push(series[i])
                }
                else {
                    ns.push({});
                }
            }
        }
        ec_instance.setOption({series:ns});
    };
    ec.showGeoLabel = function(value) {
        value = this.utils.toBoolean(value);
        let series = optionEx.option.series;
        let ns = [];
        for (let i = 0; i < series.length; i++) {
            if (series[i].type === 'map') {
                //series[i].label.show=value;
                ns.push({label:{show:value}})
            } else if (series[i].type === 'lines') {
                ns.push(series[i])
            }
            else {
                ns.push({});
            }
        }

        ec_instance.setOption({series:ns, geo:{label:{show:value}}});
    };
    ec.scatterSize = function(value) {
        value = this.utils.toInt(value);
        let ns = [];
        let series = optionEx.option.series;
        for (let i = 0; i < series.length; i++) {
            if (series[i].type === 'scatter') {
                ns.push({symbolSize:value})
            } else {
                ns.push(series[i]);
            }
        }
        ec_instance.setOption({series:ns});
    };
    ec.scatterColor = function(value) {
        let ns = [];
        let series = optionEx.option.series;
        for (let i = 0; i < series.length; i++) {
            if (series[i].type === 'scatter') {
                ns.push({itemStyle:{color:value}})
            } else if (series[i].type === 'lines') {
                ns.push(series[i])
            }
            else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});
    };

    ec.showTitle = function(value) {
        value = this.utils.toBoolean(value);
        let op = {title:{show:value}};
        let title = '';
        if (optionEx.option.title) {
            title = optionEx.option.title.text;
        }
        if ($.trim(title) == '') {
            op = {title: {show: value, text: '图表名称'}};
        } else {
            op = {title: {show: value, text: title}};
        }
        ec_instance.setOption(op);
        return op;
    };
    ec.titleFontSize = function(value) {
        value = this.utils.toInt(value);
        let op = {title:{textStyle:{fontSize:value}}};
        ec_instance.setOption(op);
        return op;
    };
    ec.titleFontBold = function(value) {
        value = this.utils.toBoolean(value);
        let op = {title:{textStyle:{fontWeight:value?'bold':'normal'}}};
        ec_instance.setOption(op);
        return op;
    };
    ec.titleFontItalic = function(value) {
        value = this.utils.toBoolean(value);
        let op = {title:{textStyle:{fontStyle:value?'italic':'normal'}}};
        ec_instance.setOption(op);
        return op;
    };
    ec.titleFontColor = function(value) {
        let op = {title:{textStyle:{color:value}}};
        ec_instance.setOption(op);
        return op;
    };
    ec.titleLink = function(value, id) {
        if ($.trim(value) == '') {
            return;
        }
        if (!this.utils.urlVerify(value)) {
            this.utils.showError('#' + id, "不合法的链接");
            return ;
        }
        let op = {title:{link:value}};
        ec_instance.setOption(op);
        return op;
    };
    ec.titleLinkTarget = function(value) {
        let op = {title:{target: value}};
        ec_instance.setOption(op);
        return op
    };
    ec.titleLeft = function(value) {
        let op = {title:{left:value}};
        ec_instance.setOption(op);
        return op
    };
    ec.titleTop = function(value) {
        let op = {title:{top:value}};
        ec_instance.setOption(op);
        return op
    };

    ec.gaugeCenter = function(value, id, s) {
        value = value.substring(1, value.length-1).split(",");
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({center:value});
            } else {
                ns.push({});
            }
        }
        var op = {series:ns};
        ec_instance.setOption(op);
    };
    ec.gaugeRadius = function(value,id, s) {
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({radius:value});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});
    };
    ec.gaugeStartAngle = function(value,id, s) {
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({startAngle:value});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});
    };
    ec.gaugeEndAngle = function(value,id, s) {
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({endAngle:value});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});

    };

    ec.gaugeMax = function(value,id, s) {
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({max:value});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});

    };
    ec.gaugeMin = function(value,id, s) {
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({min:value});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});
    };
    ec.showGaugeAxisLine = function(value,id, s) {
        value = this.utils.toBoolean(value);
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({axisLine:{show:value}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});
    };
    ec.gaugeAxisLineWidth = function(value,id, s) {
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({axisLine:{lineStyle:{width:value}}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});
    };
    ec.showGaugeSplitLine = function(value,id, s) {
        value = this.utils.toBoolean(value);
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({splitLine:{show:value}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});
    };
    ec.gaugeSplitLineWidth = function(value,id, s) {
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({splitLine:{lineStyle:{width:value}}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});

    };
    ec.gaugeSplitLineLength = function(value,id, s) {
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({splitLine:{length:value}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});
    };

    ec.showGaugeAxisTick = function(value, id, s) {
        value = this.utils.toBoolean(value);
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({axisTick:{show:value}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});
    };

    ec.gaugeAxisTickSplitNumber = function(value, id, s) {
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({axisTick:{splitNumber:value}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});
    };
    ec.gaugeAxisTickLength = function(value, id, s) {
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({axisTick:{length:value}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});

    };
    ec.gaugeAxisTickWidth = function(value, id, s) {
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({axisTick:{lineStyle:{width:value}}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});

    };
    ec.showGaugeAxisLabel = function(value, id, s) {
        value = this.utils.toBoolean(value);
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({axisLabel:{show:value}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});

    };
    ec.gaugeAxisLabelDistance = function(value, id, s) {
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({axisLabel:{distance:value}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});

    };
    ec.gaugeAxisLabelFontSize = function(value, id, s) {
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({axisLabel:{fontSize:value}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});

    };
    ec.gaugeAxisLabelFontBold = function(value, id, s) {
        value = this.utils.toBoolean(value);
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({axisLabel:{fontWeight:value?'bold':'normal'}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});

    };
    ec.gaugeAxisLabelFontItalic = function(value, id, s) {
        value = this.utils.toBoolean(value);
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({axisLabel:{fontStyle:value?'italic':'normal'}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});

    };
    ec.showGaugePointer = function(value, id, s) {
        value = this.utils.toBoolean(value);
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({pointer:{show:value}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});

    };
    ec.gaugePointerLength = function(value, id, s) {
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({pointer:{length:value}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});

    };
    ec.gaugePointerWidth = function(value, id, s) {
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({pointer:{width:value}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});

    };
    ec.showGaugeTitle = function(value, id, s) {
        value = this.utils.toBoolean(value);
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({title:{show:value}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});

    };

    ec.gaugeTitleOffsetCenter = function(value, id, s) {
        value = value.substring(1, value.length-1).split(",");
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({title:{offsetCenter:value}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});

    };

    ec.gaugeTitleFontColor = function(value, id, s) {
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({title:{color:value}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});

    };

    ec.gaugeTitleFontSize = function(value, id, s) {
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({title:{fontSize:value}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});

    };
    ec.gaugeTitleFontBold = function(value, id, s) {
        value = this.utils.toBoolean(value);
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({title:{fontWeight:value?'bold':'normal'}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});

    };
    ec.gaugeTitleFontItalic = function(value, id, s) {
        value = this.utils.toBoolean(value);
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({title:{fontStyle:value?'italic':'normal'}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});

    };
    ec.showGaugeDetail = function(value, id, s) {
        value = this.utils.toBoolean(value);
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({detail:{show:value}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});

    };
    ec.gaugeDetailOffsetCenter = function(value, id, s) {
        value = value.substring(1, value.length - 1).split(",");
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({detail:{offsetCenter:value}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});

    };
    ec.gaugeDetailFontSize = function(value, id, s) {
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({detail:{fontSize:value}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});

    };
    ec.gaugeDetailFontBold = function(value, id, s) {
        value = this.utils.toBoolean(value);
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({detail:{fontWeight:value?'bold':'normal'}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});

    };
    ec.gaugeDetailFontItalic = function(value, id, s) {
        value = this.utils.toBoolean(value);
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({detail:{fontStyle:value?'italic':'normal'}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});

    };
    ec.gaugeDetailFormatter = function(value, id, s) {
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i< series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({detail:{formatter:value}});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});
    };

    ec.indicatorMax = function(value, id, s) {
        var ict = ec_instance.getOption().radar[0].indicator;
        var nict = [];
        for (var i=0;i<ict.length; i++) {
            if (ict[i].name == s) {
                nict.push({name:s,max:value})
            } else {
                nict.push({name:ict[i].name, max:ict[i].max});
            }
        }
        ec_instance.setOption({radar:{indicator:nict}})
    };
    ec.indicatorMin = function(value, id, s) {
        var ict = ec_instance.getOption().radar[0].indicator;
        var nict = [];
        for (var i=0;i<ict.length; i++) {
            if (ict[i].name == s) {
                nict.push({name:s,min:value})
            } else {
                nict.push({name:ict[i].name, min:ict[i].max});
            }
        }
        ec_instance.setOption({radar:{indicator:nict}})
    };

    ec.lineArea = function(value, id, s) {
        value = this.utils.toBoolean(value);
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i<series.length; i++) {
            if ($.trim(series[i].name) == s && series[i].type == 'line') {
                if (value) {
                    ns.push({areaStyle: {origin: 'start'}})
                } else {
                    ns.push({areaStyle:null});
                }
            } else {
                ns.push({})
            }
        }
        ec_instance.setOption({series:ns});
    };

    ec.type = function(value, id, s) {
        var series = optionEx.option.series;

        for (var i=0; i<series.length; i++) {
            if ($.trim(series[i].name) == s) {
                optionEx.option.series[i].type = value;
            }
        }
        doRender(optionEx, function () {
            ec_instance.setOption(ec.option);
        })

    };
    ec.color = function(value, id, s) {
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i<series.length; i++) {
            if ($.trim(series[i].name) == s) {
                ns.push({itemStyle:{color:value}})
            } else {
                ns.push({})
            }
        }
        ec_instance.setOption({series:ns})

    };
    ec.lineType = function(value, id, s) {
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i<series.length; i++) {
            if ($.trim(series[i].name) == s && series[i].type=='line') {
                ns.push({lineStyle:{type:value}})
            } else {
                ns.push({})
            }
        }
        ec_instance.setOption({series:ns})

    };

    ec.pieCenter = function(value, id, s) {
        value = value.substring(1, value.length-1).split(",");
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i<series.length;i++) {
            if (s && $.trim(series[i].name)==s) {
                //optionEx.option.series[i].center = value;
                ns.push({center:value});
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});
    };
    ec.pieRadius = function(value, id, s) {
        value = value.substring(1, value.length-1).split(",");
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i<series.length;i++) {
            if (s && $.trim(series[i].name)==s) {
                ns.push({radius:value})
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});
    };

    ec.pieClockWise = function(value, id, s) {
        value = this.utils.toBoolean(value);
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i<series.length;i++) {
            if (s && $.trim(series[i].name)==s) {
                // optionEx.option.series[i].radius = value;
                ns.push({clockwise:value})
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns});
    };
    ec.pieRoseType = function(value, id, s) {
        // value = this.utils.toBoolean(value);
        if ("false" === value) {
            value = null;
        }
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i<series.length;i++) {
            if (s && $.trim(series[i].name)==s) {
                // optionEx.option.series[i].radius = value;
                ns.push({roseType:value})
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns})
    };
    ec.pieSelectedMode = function(value, id, s) {
        if ("false" === value) {
            value = null;
        }
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i<series.length;i++) {
            if (s && $.trim(series[i].name)==s) {
                // optionEx.option.series[i].radius = value;
                ns.push({selectedMode:value})
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns})
    };
    ec.pieSelectedOffset = function(value, id, s) {
        value = this.utils.toInt(value);
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i<series.length;i++) {
            if (s && $.trim(series[i].name)==s) {
                // optionEx.option.series[i].radius = value;
                ns.push({selectedOffset:value})
            } else {
                ns.push({});
            }
        }
        ec_instance.setOption({series:ns})
    };

    ec.showPieLabelLine = function(value, id, s) {
        value = this.utils.toBoolean(value);
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i<series.length; i++) {
            if (series[i].type == 'pie' && s == $.trim(series[i].name)) {
                ns.push({labelLine:{show:value}});
            } else {
                ns.push({});
            }
        }
        var op = {series:ns};
        ec_instance.setOption(op);
        return op;
    };

    ec.pieLabelLineType = function(value, id, s) {
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i<series.length; i++) {
            if (series[i].type == 'pie'  && s == $.trim(series[i].name)) {
                ns.push({labelLine:{lineStyle:{type:value}}});
            } else {
                ns.push({});
            }
        }
        var op = {series:ns};
        ec_instance.setOption(op);
        return op;
    };

    ec.pieLabelLineWidth = function(value, id, s) {
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i<series.length; i++) {
            if (series[i].type == 'pie' && s == $.trim(series[i].name)) {
                ns.push({labelLine:{lineStyle:{width:value}}});
            } else {
                ns.push({});
            }
        }
        var op = {series:ns};
        ec_instance.setOption(op);
        return op;
    };
    ec.pieLabelLineSmooth = function(value, id, s) {
        value = this.utils.toInt(value)/10;

        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i<series.length; i++) {
            if (series[i].type == 'pie' && s == $.trim(series[i].name)) {
                ns.push({labelLine:{smooth:value}});
            } else {
                ns.push({});
            }
        }
        var op = {series:ns};
        ec_instance.setOption(op);
        return op;
    };

    ec.barGap = function(value, id) {
        if (value == "") {
            value = "30%";
        }
        if (!this.utils.validate(id, "数据格式错误", value, /^[0-9]*%|[0-9]*$/)) {
            return;
        }
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i<series.length; i++) {
            if (series[i].type == 'bar') {
                ns.push({barGap:value});
            } else {
                ns.push({});
            }
        }
        var op = {series: ns};
        ec_instance.setOption(op);
        return op;
    };
    ec.barCategoryGap = function(value, id) {
        if (value == "") {
            value = "20%";
        }
        if (!this.utils.validate(id, "数据格式错误", value, /^[0-9]*%|[0-9]*$/)) {
            return;
        }
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i<series.length; i++) {
            if (series[i].type == 'bar') {
                ns.push({barCategoryGap:value});
            } else {
                ns.push({});
            }
        }
        var op = {series: ns};
        ec_instance.setOption(op);
        return op;
    };

    ec.smoothLine = function(value) {
        value = this.utils.toBoolean(value);
        var series = optionEx.option.series;
        var ns = [];
        for (var i=0; i<series.length; i++) {
            if (series[i].type == 'line') {
                ns.push({smooth:value});
            } else {
                ns.push({});
            }
        }
        var op = {series: ns};
        ec_instance.setOption(op);
        return op;
    };
    ec.barWidth = function(value) {
        value = this.utils.toInt(value);
        var size = optionEx.option.series.length;
        var series = [];
        for (var i=0; i<size;i++) {
            series.push({barWidth:value});
        }
        var op = {series: series};
        ec_instance.setOption(op);
        return op;
    };
    ec.barBorderRadius = function(value, id) {
        if (value == "") {
            value = 0;
        }
        var originValue = value;
        value = this.utils.toInt(value);
        if (isNaN(value)) {
            try {
                value = JSON.parse(originValue);
            } catch (e) {
                ec.utils.showError('#'+id, "不合法的参数(您可以输入大于等于0的数字或者'[1,2]'这种格式的数据)")
            }
        }
        if (value < 0) {
            ec.utils.showError('#'+id, "不合法的参数(您可以输入大于等于0的数字或者'[1,2]'这种格式的数据)")
        }
        var size = optionEx.option.series.length;
        var series = [];
        for (var i=0; i<size;i++) {
            series.push({itemStyle:{barBorderRadius:value}});
        }
        var op = {series: series};
        ec_instance.setOption(op);
        return op;
    };

    ec.showSLabel = function(value, id, s) {
        value = this.utils.toBoolean(value);
        if (optionEx != null) {
            var size = optionEx.option.series.length;
            var series = [];
            for (var i=0; i<size;i++) {
                if (s) {
                    if ($.trim(optionEx.option.series[i].name) == s) {
                        series.push({label: {show: value}});
                    } else {
                        series.push({});
                    }
                } else {
                    series.push({label: {show: value}});
                }
            }
            var op = {series: series};
            ec_instance.setOption(op)
            return op;
        }
    };
    ec.sLabelPosition = function(value, id, s) {
        if (optionEx != null) {
            var size = optionEx.option.series.length;
            var series = [];
            for (var i=0; i<size;i++) {
                if (s) {
                    if ($.trim(optionEx.option.series[i].name) == s) {
                        series.push({label:{position:value}});
                    } else {
                        series.push({});
                    }
                } else {
                    series.push({label: {position: value}});
                }
            }
            var op = {series: series};
            ec_instance.setOption(op)
            return op;
        }
    };
    ec.sLabelTextFontSize = function(value, id, s) {
        value = this.utils.toInt(value);
        var size = optionEx.option.series.length;
        var series = [];
        for (var i=0; i<size;i++) {
            if (s) {
                if ($.trim(optionEx.option.series[i].name) == s) {
                    series.push({label:{fontSize:value}});
                } else {
                    series.push({});
                }
            } else {
                series.push({label:{fontSize:value}});
            }

        }
        var op = {series: series};
        ec_instance.setOption(op)
        return op;
    };
    ec.sLabelTextFontColor = function(value, id, s) {
        var size = optionEx.option.series.length;
        var series = [];
        for (var i=0; i<size;i++) {
            if (s) {
                if ($.trim(optionEx.option.series[i].name) == s) {
                    series.push({label:{color:value}});
                } else {
                    series.push({});
                }
            } else {
                series.push({label:{color:value}});
            }

        }
        var op = {series: series};
        ec_instance.setOption(op)
        return op;
    };
    ec.sLabelTextFontBold = function(value, id, s) {
        value = this.utils.toBoolean(value);
        var size = optionEx.option.series.length;
        var series = [];
        for (var i=0; i<size;i++) {
            if (s) {
                if ($.trim(optionEx.option.series[i].name) == s) {
                    series.push({label:{fontWeight:value?'bold':'normal'}});
                } else {
                    series.push({});
                }
            } else {
                series.push({label:{fontWeight:value?'bold':'normal'}});
            }

        }
        var op = {series: series};
        ec_instance.setOption(op)
        return op;
    };
    ec.sLabelTextFontItalic = function(value, id, s) {
        value = this.utils.toBoolean(value);
        var size = optionEx.option.series.length;
        var series = [];
        for (var i=0; i<size;i++) {
            if (s) {
                if ($.trim(optionEx.option.series[i].name) == s) {
                    series.push({label:{fontStyle:value?'italic':'normal'}});
                } else {
                    series.push({});
                }
            } else {
                series.push({label:{fontStyle:value?'italic':'normal'}});
            }

        }
        var op = {series: series};
        ec_instance.setOption(op)
        return op;
    };
    /**
     *
     * @param value RGB
     */
    ec.sLabelBackgroundColor = function(value) {
        var size = optionEx.option.series.length;
        var series = [];
        for (var i=0; i<size;i++) {
            series.push({label:{backgroundColor:value}});
        }
        var op = {series: series};
        ec_instance.setOption(op)
        return op;
    };

    /**
     *
     * @param value RGB
     */
    ec.sLabelBorderColor = function(value) {
        var size = optionEx.option.series.length;
        var series = [];
        for (var i=0; i<size;i++) {
            series.push({label:{borderColor:value}});
        }
        var op = {series: series};
        ec_instance.setOption(op)
        return op;
    };
    ec.sLabelBorderWidth = function(value) {
        value = this.utils.toInt(value);
        var size = optionEx.option.series.length;
        var series = [];
        for (var i=0; i<size;i++) {
            series.push({label:{borderWidth:value}});
        }
        var op = {series: series};
        ec_instance.setOption(op);
        return op;
    };

    ec.showTooltip = function (value) {
        value = this.utils.toBoolean(value);
        var op = {tooltip: {show: value}};
        ec_instance.setOption(op);
        return op;
    };
    ec.tooltipTrigger = function (value) {
        var op = {tooltip: {trigger: value}};
        ec_instance.setOption(op);
        return op;
    };
    ec.tooltipConfine = function (value) {
        value = this.utils.toBoolean(value);
        var op ={tooltip: {confine: value}};
        ec_instance.setOption(op);
        return op;
    };
    /**
     * @param value rgb 颜色值
     */
    ec.tooltipBackgroundColor = function (value) {
        var op = {tooltip: {backgroundColor: value}};
        ec_instance.setOption(op);
        return op;
    };

    ec.tooltipBorderWidth = function(value) {
        value = this.utils.toInt(value);
        var op = {tooltip:{borderWidth:value}};
        ec_instance.setOption(op);
        return op;
    };
    ec.tooltipBorderColor = function(value) {
        var op = {tooltip:{borderColor:value}};
        ec_instance.setOption(op);
        return op;
    };
    ec.tooltipFormatter = function(value) {
        var op = {tooltip:{formatter:value}};
        ec_instance.setOption(op);
        return op;
    };

    ec.showLegend = function (value) {
        value = this.utils.toBoolean(value);
        var op = {legend: {show: value}};
        ec_instance.setOption(op);
        return op;
    };

    ec.legendOrient = function (value) {
        var op = {legend: {orient: value}};
        ec_instance.setOption(op);
        return op;
    };

    ec.legendLeft = function (value) {
        var op = {legend: {left: value}};
        ec_instance.setOption(op);
        return op;
    };
    ec.legendTop = function (value) {
        var op = {legend: {top: value}};
        ec_instance.setOption(op);
        return op;
    };
    ec.legendTextFontSize = function (value) {
        value = this.utils.toInt(value);
        var op = {legend: {textStyle: {fontSize: value}}};
        ec_instance.setOption(op);
        return op;
    };
    ec.legendTextFontBold = function (value) {
        value = this.utils.toBoolean(value);
        var op = {legend: {textStyle: {fontWeight: value ? 'bold' : 'normal'}}};
        ec_instance.setOption(op);
        return op;
    };
    ec.legendTextFontItalic = function (value) {
        value = this.utils.toBoolean(value);
        var op = {legend: {textStyle: {fontStyle: value ? 'italic' : 'normal'}}};
        ec_instance.setOption(op);
        return op;
    };
    ec.legendTextFontColor = function (value) {
        var op = {legend: {textStyle: {color: value}}};
        ec_instance.setOption(op);
        return op;
    };
    ec.legendBackgroundColor = function (value) {
        var op = {legend: {backgroundColor: value}};
        ec_instance.setOption(op);
        return op;
    };
    ec.legendBorderWidth = function (value) {
        value = this.utils.toInt(value);
        var op = {legend: {borderWidth: value}};
        ec_instance.setOption(op);
        return op;
    };
    ec.legendBorderRadius = function (value) {
        value = this.utils.toInt(value);
        var op = {legend: {borderRadius: value}};
        ec_instance.setOption(op);
        return op;
    };
    ec.legendBorderColor = function (value) {
        var op = {legend: {borderColor: value}};
        ec_instance.setOption(op);
        return op;
    };

    ec.exchangeXY = function (value) {
        if (optionEx != null) {
            var xAxis = optionEx.option.xAxis;
            var yAxis = optionEx.option.yAxis;
            var series = optionEx.option.series;
            for (var i = 0; i < series.length; i++) {
                var data = series[i].data;
                for (var j = 0; j < data.length; j++) {
                    if (data[j].value instanceof Array) {
                        series[i].data[j].value = series[i].data[j].value.reverse();
                    }
                }
            }
            optionEx.option.xAxis = yAxis;
            optionEx.option.yAxis = xAxis;
            doRender(optionEx, function (ec_i) {
                ec_i.setOption(ec.option);
            });
        }
    };

    ec.showX = function (value) {
        if (ec_instance != null) {
            value = this.utils.toBoolean(value);
            var size = optionEx.option.xAxis.length;
            var xAxis = [];
            for (var i = 0; i < size; i++) {
                xAxis.push({show:value});
            }
            var op = {xAxis: xAxis};
            ec_instance.setOption(op);
            return op;
        }
    };

    ec.xZ = function (value) {
        value = this.utils.toInt(value);
        var size = optionEx.option.xAxis.length;
        var xAxis = [];
        for (var i = 0; i < size; i++) {
            xAxis.push({z: value});
        }

        var op = {xAxis: xAxis};
        ec_instance.setOption(op);
        return op;
    };
    ec.xTitle = function (value) {
        var size = optionEx.option.xAxis.length;
        var xAxis = [];
        for (var i = 0; i < size; i++) {
            xAxis.push({name: value});
        }

        var op = {xAxis: xAxis};
        ec_instance.setOption(op);
        return op;
    };

    ec.xTitleFontSize = function (value) {
        value = this.utils.toInt(value);
        var size = optionEx.option.xAxis.length;
        var xAxis = [];
        for (var i = 0; i < size; i++) {
            xAxis.push({nameTextStyle: {fontSize: value}});
        }
        var op ={xAxis: xAxis};
        ec_instance.setOption(op);
        return op;
    };

    ec.xTitleFontBold = function (value) {
        var size = optionEx.option.xAxis.length;
        var xAxis = [];
        for (var i = 0; i < size; i++) {
            xAxis.push({nameTextStyle: {fontWeight: value ? 'bold' : 'normal'}});
        }
        var op = {xAxis: xAxis};
        ec_instance.setOption(op);
        return op;
    };

    ec.xTitleFontItalic = function (value) {
        value = this.utils.toBoolean(value);
        var size = optionEx.option.xAxis.length;
        var xAxis = [];
        for (var i = 0; i < size; i++) {
            xAxis.push({nameTextStyle: {fontStyle: value ? 'italic' : 'normal'}});
        }
        var op = {xAxis: xAxis};
        ec_instance.setOption(op);
        return op;
    };

    ec.xTitleFontColor = function (value) {
        var size = optionEx.option.xAxis.length;
        var xAxis = [];
        for (var i = 0; i < size; i++) {
            xAxis.push({nameTextStyle: {color: value}});
        }
        var op = {xAxis: xAxis};
        ec_instance.setOption(op);
        return op;
    };
    ec.xPosition = function (value) {
        var size = optionEx.option.xAxis.length;
        var xAxis = [];
        for (var i = 0; i < size; i++) {
            xAxis.push({position: value});
        }
        var op = {xAxis: xAxis};
        ec_instance.setOption(op);
        return op;
    };
    ec.xInverse = function (value) {
        value = this.utils.toBoolean(value);
        var size = optionEx.option.xAxis.length;
        var xAxis = [];
        for (var i = 0; i < size; i++) {
            xAxis.push({inverse: value});
        }
        var op = {xAxis: xAxis};
        ec_instance.setOption(op);
        return op;
    };

    ec.showXTick = function (value) {
        if (ec_instance != null) {
            value = this.utils.toBoolean(value);
            var size = optionEx.option.xAxis.length;
            var xAxis = [];
            for (var i = 0; i < size; i++) {
                xAxis.push({axisTick: {show: value}});
            }
            var op = {xAxis: xAxis};
            ec_instance.setOption(op);
            return op;
        }
    };
    ec.xTickColor = function (value) {
        var size = optionEx.option.xAxis.length;
        var xAxis = [];
        for (var i = 0; i < size; i++) {
            xAxis.push({axisTick: {lineStyle: {color: value}}});
        }
        var op = {xAxis: xAxis};
        ec_instance.setOption(op);
        return op;
    };

    ec.showXLabel = function (value) {
        if (ec_instance != null) {
            value = this.utils.toBoolean(value);
            var size = optionEx.option.xAxis.length;
            var xAxis = [];
            for (var i = 0; i < size; i++) {
                xAxis.push({axisLabel: {show: value}});
            }
            var op = {xAxis: xAxis};
            ec_instance.setOption(op);
            return op;
        }
    };
    ec.xLabelColor = function (value) {
        var size = optionEx.option.xAxis.length;
        var xAxis = [];
        for (var i = 0; i < size; i++) {
            xAxis.push({axisLabel: {color: value}});
        }
        var op = {xAxis: xAxis};
        ec_instance.setOption(op);
        return op;
    };
    ec.xLabelInside = function(value) {
        value = this.utils.toBoolean(value);
        let xAxis = [];
        let size = optionEx.option.xAxis.length;
        for (let i=0; i<size; i++) {
            xAxis.push({axisLabel:{inside:value}})
        }
        let op = {xAxis: xAxis};
        ec_instance.setOption(op);
        return op;
    };
    ec.xLabelRotate = function(value) {
        value = this.utils.toInt(value);
        let xAxis = [];
        let size = optionEx.option.xAxis.length;
        for (let i=0; i<size; i++) {
            xAxis.push({axisLabel:{rotate:value}})
        }
        let op = {xAxis: xAxis};
        ec_instance.setOption(op);
        return op;
    };
    ec.xLabelInterval = function(value) {
        value = this.utils.toInt(value);
        let xAxis = [];
        let size = optionEx.option.xAxis.length;
        for (let i=0; i<size; i++) {
            xAxis.push({axisLabel:{interval:value}})
        }
        let op = {xAxis: xAxis};
        ec_instance.setOption(op);
        return op;
    };
    ec.xLabelMargin = function(value) {
        value = this.utils.toInt(value);
        let xAxis = [];
        let size = optionEx.option.xAxis.length;
        for (let i=0; i<size; i++) {
            xAxis.push({axisLabel:{margin:value}})
        }
        let op = {xAxis: xAxis};
        ec_instance.setOption(op);
        return op;
    };

    ec.showXLine = function (value) {
        if (ec_instance != null) {
            value = this.utils.toBoolean(value);
            var size = optionEx.option.xAxis.length;
            var xAxis = [];
            for (var i = 0; i < size; i++) {
                xAxis.push({axisLine: {show: value}});
            }
            var op = {xAxis: xAxis};
            ec_instance.setOption(op);
            return op;
        }
    };

    ec.showXSplitLine = function(value) {
        value = this.utils.toBoolean(value);
        var size = optionEx.option.xAxis.length;
        var seriesSize = optionEx.option.series.length;
        if (seriesSize<size) {
            size = seriesSize;
        }
        var xAxis = [];
        for (var i = 0; i < size; i++) {
            xAxis.push({splitLine: {show: value}});
        }
        var op = {xAxis: xAxis};
        ec_instance.setOption(op);
        return op;
    };

    ec.xSplitLineColor = function(value) {
        var size = optionEx.option.xAxis.length;
        var xAxis = [];
        for (var i = 0; i < size; i++) {
            xAxis.push({splitLine: {lineStyle: {color: value}}});
        }
        var op = {xAxis: xAxis};
        ec_instance.setOption(op);
        return op;
    };

    ec.xLineColor = function (value) {
        var size = optionEx.option.xAxis.length;
        var xAxis = [];
        for (var i = 0; i < size; i++) {
            xAxis.push({axisLine: {lineStyle: {color: value}}});
        }
        var op = {xAxis: xAxis};
        ec_instance.setOption(op);
        return op;
    };

    ec.showY = function (value) {
        if (ec_instance != null) {
            value = this.utils.toBoolean(value);
            var size = optionEx.option.yAxis.length;
            var yAxis = [];
            for (var i = 0; i < size; i++) {
                yAxis.push({show: value});
            }
            var op = {yAxis: yAxis};
            ec_instance.setOption(op);
            return op;
        }
    };

    ec.yZ = function(value) {
        value = this.utils.toInt(value);
        var size = optionEx.option.yAxis.length;
        var yAxis = [];
        for (var i = 0; i < size; i++) {
            yAxis.push({z: value});
        }
        var op = {yAxis: yAxis};
        ec_instance.setOption(op);
        return op;
    };

    ec.yTitle = function (value) {
        var size = optionEx.option.yAxis.length;
        var yAxis = [];
        for (var i = 0; i < size; i++) {
            yAxis.push({name: value});
        }
        var op = {yAxis: yAxis};
        ec_instance.setOption(op);
        return op;
    };

    ec.yTitleFontSize = function (value) {
        value = this.utils.toInt(value);
        var size = optionEx.option.yAxis.length;
        var yAxis = [];
        for (var i = 0; i < size; i++) {
            yAxis.push({nameTextStyle: {fontSize: value}});
        }
        var op = {yAxis: yAxis};
        ec_instance.setOption(op);
        return op;
    };

    ec.yTitleFontBold = function (value) {
        value = this.utils.toBoolean(value);
        var size = optionEx.option.yAxis.length;
        var yAxis = [];
        for (var i = 0; i < size; i++) {
            yAxis.push({nameTextStyle: {fontWeight: value ? 'bold' : 'normal'}});
        }
        var op = {yAxis: yAxis};
        ec_instance.setOption(op);
        return op;
    };

    ec.yTitleFontItalic = function (value) {
        value = this.utils.toBoolean(value);
        var size = optionEx.option.yAxis.length;
        var yAxis = [];
        for (var i = 0; i < size; i++) {
            yAxis.push({nameTextStyle: {fontStyle: value ? 'italic' : 'normal'}});
        }
        var op = {yAxis: yAxis};
        ec_instance.setOption(op);
        return op;
    };

    ec.yTitleFontColor = function (value) {
        var size = optionEx.option.yAxis.length;
        var yAxis = [];
        for (var i = 0; i < size; i++) {
            yAxis.push({nameTextStyle: {color: value}});
        }
        var op = {yAxis: yAxis};
        ec_instance.setOption(op);
        return op;
    };
    ec.yPosition = function (value) {
        var size = optionEx.option.yAxis.length;
        var yAxis = [];
        for (var i = 0; i < size; i++) {
            yAxis.push({position: value});
        }
        var op = {yAxis: yAxis};
        ec_instance.setOption(op);
        return op;
    };
    ec.yInverse = function (value) {
        value = this.utils.toBoolean(value);
        var size = optionEx.option.yAxis.length;
        var yAxis = [];
        for (var i = 0; i < size; i++) {
            yAxis.push({inverse: value});
        }
        var op = {yAxis: yAxis};
        ec_instance.setOption(op);
        return op;
    };

    ec.showYTick = function (value) {
        if (ec_instance != null) {
            value = this.utils.toBoolean(value);
            var size = optionEx.option.yAxis.length;
            var seriesSize = optionEx.option.series.length;
            if (seriesSize<size) {
                size = seriesSize;
            }
            var yAxis = [];
            for (var i = 0; i < size; i++) {
                yAxis.push({axisTick: {show: value}});
            }
            var op = {yAxis: yAxis};
            ec_instance.setOption(op);
            return op;
        }
    };
    ec.yTickColor = function (value) {
        var size = optionEx.option.yAxis.length;
        var yAxis = [];
        for (var i = 0; i < size; i++) {
            yAxis.push({axisTick: {lineStyle: {color: value}}});
        }
        var op = {yAxis: yAxis};
        ec_instance.setOption(op);
        return op;
    };

    ec.showYLabel = function (value) {
        if (ec_instance != null) {
            value = this.utils.toBoolean(value);
            var size = optionEx.option.yAxis.length;
            var seriesSize = optionEx.option.series.length;
            if (seriesSize<size) {
                size = seriesSize;
            }
            var yAxis = [];
            for (var i = 0; i < size; i++) {
                yAxis.push({axisLabel: {show: value}});
            }
            var op = {yAxis: yAxis};
            ec_instance.setOption(op);
            return op;
        }
    };
    ec.yLabelColor = function (value) {
        var size = optionEx.option.yAxis.length;
        var yAxis = [];
        for (var i = 0; i < size; i++) {
            yAxis.push({axisLabel: {color: value}});
        }
        var op = {yAxis: yAxis};
        ec_instance.setOption(op);
        return op;
    };

    ec.yLabelInside = function(value) {
        value = this.utils.toBoolean(value);
        let yAxis = [];
        let size = optionEx.option.yAxis.length;
        for (let i=0; i<size; i++) {
            yAxis.push({axisLabel:{inside:value}})
        }
        let op = {yAxis: yAxis};
        ec_instance.setOption(op);
        return op;
    };
    ec.yLabelRotate = function(value) {
        value = this.utils.toInt(value);
        let yAxis = [];
        let size = optionEx.option.yAxis.length;
        for (let i=0; i<size; i++) {
            yAxis.push({axisLabel:{rotate:value}})
        }
        let op = {yAxis: yAxis};
        ec_instance.setOption(op);
        return op;
    };
    ec.yLabelInterval = function(value) {
        value = this.utils.toInt(value);
        let yAxis = [];
        let size = optionEx.option.yAxis.length;
        for (let i=0; i<size; i++) {
            yAxis.push({axisLabel:{interval:value}})
        }
        let op = {yAxis: yAxis};
        ec_instance.setOption(op);
        return op;
    };
    ec.yLabelMargin = function(value) {
        value = this.utils.toInt(value);
        let yAxis = [];
        let size = optionEx.option.yAxis.length;
        for (let i=0; i<size; i++) {
            yAxis.push({axisLabel:{margin:value}})
        }
        let op = {yAxis: yAxis};
        ec_instance.setOption(op);
        return op;
    };

    ec.showYLine = function (value) {
        if (ec_instance != null) {
            value = this.utils.toBoolean(value);
            var size = optionEx.option.yAxis.length;
            var seriesSize = optionEx.option.series.length;
            if (seriesSize<size) {
                size = seriesSize;
            }
            var yAxis = [];
            for (var i = 0; i < size; i++) {
                yAxis.push({axisLine: {show: value}});
            }
            var op = {yAxis: yAxis};
            ec_instance.setOption(op);
            return op;
        }
    };

    ec.showYSplitLine = function(value) {
        value = this.utils.toBoolean(value);
        var size = optionEx.option.yAxis.length;
        var seriesSize = optionEx.option.series.length;
        if (seriesSize<size) {
            size = seriesSize;
        }
        var yAxis = [];
        for (var i = 0; i < size; i++) {
            yAxis.push({splitLine: {show: value}});
        }
        var op = {yAxis: yAxis};
        ec_instance.setOption(op);
        return op;
    };

    ec.ySplitLineColor = function(value) {
        var size = optionEx.option.yAxis.length;
        var yAxis = [];
        for (var i = 0; i < size; i++) {
            yAxis.push({splitLine: {lineStyle: {color: value}}});
        }
        var op = {yAxis: yAxis};
        ec_instance.setOption(op);
        return op;
    };

    ec.yLineColor = function (value) {
        var size = optionEx.option.yAxis.length;
        var yAxis = [];
        for (var i = 0; i < size; i++) {
            yAxis.push({axisLine: {lineStyle: {color: value}}});
        }
        var op = {yAxis: yAxis};
        ec_instance.setOption(op);
        return op;
    };

    /**
     * 将字符串'true' 'false' 转换成boolean
     * @type {{toBoolean: (function(*): boolean)}}
     */
    ec.utils = {
        toBoolean: function (value) {
            return value === "true";
        },
        toInt: function (value) {
            return parseInt(value);
        },
        /**
         *
         * @param hex 16 进制颜色字符串
         * @param transparent 透明度
         */
        toRGB: function (hex, transparent) {
            var reg = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/;
            if (reg.test(hex)) {
                if (hex.length === 4) {
                    var sColorNew = "#";
                    for (var i = 1; i < 4; i += 1) {
                        sColorNew += hex.slice(i, i + 1).concat(hex.slice(i, i + 1));
                    }
                    hex = sColorNew;
                }
                //处理六位的颜色值
                var sColorChange = [];
                for (var i = 1; i < 7; i += 2) {
                    sColorChange.push(parseInt("0x" + hex.slice(i, i + 2)));
                }
                return "rgb(" + sColorChange.join(",") + "," + transparent + ")";
            } else {
                return hex;
            }
        },
        urlVerify: function(url){
            let reg = /^([hH][tT]{2}[pP]:\/\/|[hH][tT]{2}[pP][sS]:\/\/)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\\/])+$/;
            return reg.test(url);
        },
        extend: function (op) {
            ec.option = $.extend(ec.option, op);
        },
        createEvent: function (value, oldValue) {
            return {value:value, oldValue:oldValue};
        },
        showError: function (id, msg) {
            layer.tips(msg, id, {tipsMore:true, time:2000, tips: 3});
        },
        validate: function (id, msg, val, regex) {
            if(regex.test(val)) {
                return true;
            } else {
                this.showError('#' + id, msg);
            }
        }
    };


    function onLinsten_echarts_parameters() {

        //生成动态变化的dom节点ex: 数据列、饼状图的标签
        // $('body').on('click', '#iColorPicker td', function () {
        //     $('.color-choose').text('')
        // });
        //提示框
        $('.litimg tr td.text-right').on('mousemove', function(e){
            $(this).find('.card-tooltip').css({
                left: e.clientX + 10 + 'px',
                top: e.clientY + 10 + 'px',
                display: 'block',
                "z-index": 999999
            })
        }).on('mouseout', function(e){
            $(this).find('.card-tooltip').hide();
        });
        //左侧点击
        $('.left-part-two>p').click(function () {
            var index = $(this).index();
            $(this).addClass('active').siblings().removeClass('active');
            $(this).parent().next().find('.litimg').eq(index).removeClass('none').siblings().addClass('none')
        });
        //下拉菜单
        $('#chart_container').find('.dropdown-toggle').dropdown();
        $('#chart_container').find('.dropdown').on('click', '.dropdown-menu li', function () {
            $(this).parent().siblings('p.dropdown-self').find('span.choose-text').text($(this).find('a').text());
        });


        //颜色选取
        // $('.iColor').iColor();

        $('.js-color-group').on('click', '.color-wrap i', function () {
            $(this).parent().parent().remove();
            var current_index = $(this).parent().prev().attr('index');
            var event = ec.utils.createEvent();
            event.last_index = current_index;
            event.optionColors = true;
            ec.on(null, event);
            //重新设置index
            $('.js-color-group').find('input').each(function (index, e) {
                $(this).attr('index', index);
            })
        });
        $('.js-color-group').find('.iColor').each(function () {
            $(this).iColor(function () {
                var event = ec.utils.createEvent();
                event.last_index = $(this).attr('index');
                event.optionColors = true;
                ec.on(null, event);
            })
        });
        $('.js-add-input').click(function () {
            var last_index = parseInt($('.js-color-group').children('div:last-child').find('input').attr('index')) + 1;
            last_index = isNaN(last_index) ? 0 : last_index;
            var str = '<div class="color-wrap">\
					  			<input index="' + last_index + '" type="text" class="form-control iColor" value="#b1efd7"/>\
					  			<a class="color-red" href="javascript:;"><i class="fa fa-trash"></i></a>\
					  		</div>';
            var $_color = $(str);
            $_color.find('.iColor').iColor(function () {
                var event = ec.utils.createEvent();
                event.last_index = $(this).attr('index');
                event.optionColors = true;
                ec.on(null, event);
            });
            $('.js-color-group').append($_color);
            var event = ec.utils.createEvent();
            event.last_index = last_index;
            event.optionColors = true;
            ec.on(null, event);
        });
        //监听下拉选择
        $('#chart_container').find('.dropdown').on('click', '.dropdown-menu li', function () {
            var $span = $(this).parent().siblings('p.dropdown-self').find('span.choose-text');
            var value = $(this).find('a').attr('v');
            //判断是否是系列、数据标签，位置圆心等，切换显示不同的系列的配置
            if ($span.hasClass('series-chosen')) {
                $('.series_' + value).removeClass('hidden').siblings('.series-child').addClass('hidden');
            } else if ($span.hasClass('pielabel-chosen')) {
                $('.pielabel_' + value).removeClass('hidden').siblings('.pielabel-child').addClass('hidden');
            } else if ($span.hasClass('crs-chosen')) {
                $('.crs_' + value).removeClass('hidden').siblings('.crs-child').addClass('hidden');
            } else if ($span.hasClass('ict-chosen')) {
                $('.ict_' + value).removeClass('hidden').siblings('.ict-child').addClass('hidden');
            } else if ($span.hasClass('gaugecrs-chosen')) {
                $('.gaugecrs_' + value).removeClass('hidden').siblings('.gaugecrs-child').addClass('hidden');
            } else if ($span.hasClass('gaugestyle-chosen')) {
                $('.gaugestyle_' + value).removeClass('hidden').siblings('.gaugestyle-child').addClass('hidden');
            } else if ($span.hasClass('datacategory-chosen')) {
                $('.datacategory_' + value).removeClass('hidden').siblings('.datacategory-child').addClass('hidden');
            } else if ($span.hasClass('linkcategory-chosen')) {
                $('.linkcategory_' + value).removeClass('hidden').siblings('.linkcategory-child').addClass('hidden')
            }
            else {
                var oldValue = $span.attr('v');
                var id = $(this).parent().siblings('p.dropdown-self').find('span.choose-text').text($(this).find('a').text()).attr('v', value).attr('id');
                var event = ec.utils.createEvent(value, oldValue);
                event.s = $span.data("s");
                ec.on(id, event);
                if (id == 'showScatter') {
                    if (ec.utils.toBoolean(value)) {
                        $('#scatterSize').prop('disabled', false);
                    } else {
                        $('#scatterSize').attr('disabled', 'disabled');
                    }
                }
            }
        });
        //input
        $('#body_echarts_parameters input').on('blur', function () {
            if ($(this).hasClass('iColor')) {
                return;
            }
            var value = $(this).val();
            var id = $(this).attr('id');
            var s = $(this).data('s');
            var event = ec.utils.createEvent(value);
            if (s) {
                event.s = s;
            }
            ec.on(id, event);
        });
        //监听字体
        $('.pos-right span').click(function () {
            $(this).toggleClass('active');
            //颜色部分有专门的监听
            if (!$(this).hasClass('iColor')) {
                $(this).attr('v', $(this).hasClass('active'));
                var value = $(this).attr('v');
                var id = $(this).attr('id');
                var event = ec.utils.createEvent(value);
                event.s = $(this).data('s');
                ec.on(id, event);
            }
        });
        $('.pos-right span.iColor').iColor(function () {
            var id = $(this).attr('id');
            var value = $(this).attr('v');
            var event = ec.utils.createEvent(value);
            event.s = $(this).data('s');
            ec.on(id, event);
        });
        //重置按钮
        $('.pos-right .icon-refresh-fill').click(function () {
            var $text = $(this).parent().siblings('p').find('.choose-text');
            $text.text($text.data('dt'));
            $text.attr('v', $text.data('text'));
            var ev = ec.utils.createEvent($text.attr('v'));
            ev.s = $text.data('s');
            ec.on($text.attr('id'), ev);

            $(this).siblings('.color-choose').each(function () {
                $(this).css('background-color', $(this).data('text')).attr('v',$(this).data('text'));
            })
            //字体
            var $italic = $(this).siblings('.icon-italics-fill');
            $italic.removeClass('active').attr('v', $italic.data('text'));

            var $bold = $(this).siblings('.icon-bold-fill');
            $bold.removeClass('active').attr('v', $bold.data('text'));
            //notify ec refresh echarts

            $(this).siblings('span').each(function () {
                var value = $(this).attr('v') || $(this).val();
                var event = ec.utils.createEvent(value);
                event.s = $(this).data('s');
                ec.on($(this).attr('id'), event);
            })

        });
        //图例颜色选择事件监听
        $('.legend-parameters').find('input.iColor').iColor(function () {
            var $this = $(this);
            ec.on($this.attr('id'), ec.utils.createEvent($this.val()));
        });
        $('.tooltip-parameters').find('input.iColor').iColor(function () {
            var $this = $(this);
            ec.on($this.attr('id'), ec.utils.createEvent($this.val()));
        });
        $('.slabel-parameters').find('input.iColor').iColor(function () {
            var $this = $(this);
            ec.on($this.attr('id'), ec.utils.createEvent($this.val()));
        });
        $('.series-parameters').find('input.iColor').iColor(function () {
            var $this = $(this);
            var event = ec.utils.createEvent($this.val());
            event.s = $this.data("s");
            ec.on($this.attr('id'), event);
        });
        $('.mapcommon-parameters').find('input.iColor').iColor(function () {
            var $this = $(this);
            var event = ec.utils.createEvent($this.val());
            ec.on($this.attr('id'), event);
        });
        $('.breadcrumb-parameters').find('input.iColor').iColor(function () {
            var $this = $(this);
            var event = ec.utils.createEvent($this.val());
            ec.on($this.attr('id'), event);
        })
        $('.datacategory-parameters').find('input.iColor').iColor(function(a, color) {
            var $this = $(this);
            var event = ec.utils.createEvent('#'+color);
            event.s = $this.attr('data-s');
            ec.on($this.attr('id'), event);
        })
        $('.linkcategory-parameters').find('input.iColor').iColor(function(a, color) {
            var $this = $(this);
            console.log(color);
            var event = ec.utils.createEvent('#' + color);
            event.s = $this.attr('data-s');
            ec.on($this.attr('id'), event);
        })

    }

    //**************************************Echarts参数配置end**************************************

    $('#screenshot').on('click', function () {
        createScreenshot();
    });

    function createScreenshot() {
        httpInvoker.createScreenshot2(_chart_json.id, $("img.active").attr('type'));
    }

    var httpInvoker = {};

    httpInvoker.createScreenshot2 = function (chartId, chartType) {
        var notMyChart = chartType != '98' && chartType != '99';
        if (notMyChart) {
            var data = null;
            if (ec_instance != null) {
                data = ec_instance.getDataURL();
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
                    _chart_json.thumbnailPath = response.data;
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
    };

    $('#chart_name').on('blur', function () {
       if (optionEx != null) {
           if (optionEx.option.title) {
               optionEx.option.title.text = $(this).val();
           }
           ec.on('showTitle', {value:$('#showTitle').attr('v')})
       }
    });
    //编辑模式设置 选中、数据源类型

    //初始化编辑器
    if (_chart_json.dataSourceType == 3) {
        jsonText = _chart_json.dataSet;
        try {
            createAceEditor(true, JSON.stringify(JSON.parse(jsonText), null, 2));
        } catch (e) {
            createAceEditor(true, jsonText);
        }
    } else if (_chart_json.dataSourceType == 1) {
        sqlText = _chart_json.dataSet;
        createAceEditor(true, sqlText);
    }

    function init() {
        var type = _chart_json.chartType;
        var ot = type;
        type = type == 0 ? 21 : type;
        var $img = $('img[type="' + type + '"]');
        var index = $img.parent('div').attr('index');
        $('p[index="' + index + '"]').click();
        $img.addClass('active');
        if (ot ==91 || ot==93) {
            $('#map').parent().parent().show();
        } else {
            $('#map').parent().parent().hide();
        }
        if (ot == 0) {
            loadDemoData(type);
        } else {
            doCreateOption(false, function (ex) {
                doRender(ex);
                loadParameters(false);
            })
        }
    }

    let rc = $('#city_region').attr('regionCode');
    if (rc && rc.length ==4) {
        loadCity( function () {
            init()
        });
    } else {
        init();
    }

    document.oncontextmenu = function () {
        return false;
    }
});