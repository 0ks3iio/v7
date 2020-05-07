<div class="box box-structure report-view">
    <div class="box-header clearfix scrollBar4">
        <div class="filter-made no-radius" id="termMain">
        </div>
    </div>
    <div class="box-body mb-20">
        <div class="pa-20 bg-fff" id="reportMain" style="height:700px; overflow: auto;">

        </div>
    </div>
</div>
    <input type="hidden" id="chart_id" value="${chart.id!}">
    <input type="hidden" id="chart_unitId" value="${chart.unitId!}">
    <input type="hidden" id="chart_name" value="${chart.name!}">
    <input type="hidden" id="datasource_type" value="${chart.dataSourceType!}">
    <input type="hidden" id="datasource_id" value="${datasourceId!}">
    <input type="hidden" id="data_set" value="${chart.dataSet!}">
    <input type="hidden" id="template_id" value="${template.id!}">
<script type="text/javascript">
    (function ($) {
        var opt;

        $.fn.jqprint = function (options) {
            opt = $.extend({}, $.fn.jqprint.defaults, options);

            var $element = (this instanceof jQuery) ? this : $(this);

            if (opt.operaSupport && $.browser.opera) {
                var tab = window.open("", "jqPrint-preview");
                tab.document.open();

                var doc = tab.document;
            }
            else {
                var $iframe = $("<iframe  />");

                if (!opt.debug) {
                    $iframe.css({position: "absolute", width: "0px", height: "0px", left: "-600px", top: "-600px"});
                }

                $iframe.appendTo("body");
                var doc = $iframe[0].contentWindow.document;
            }

            if (opt.importCSS) {
                if ($("link[media=print]").length > 0) {
                    $("link[media=print]").each(function () {
                        doc.write("<link type='text/css' rel='stylesheet' href='" + $(this).attr("href") + "' media='print' />");
                    });
                }
                else {
                    $("link").each(function () {
                        doc.write("<link type='text/css' rel='stylesheet' href='" + $(this).attr("href") + "' />");
                    });
                }
            }

            if (opt.printContainer) {
                doc.write($element.outer());
            }
            else {
                $element.each(function () {
                    doc.write($(this).html());
                });
            }

            doc.close();

            (opt.operaSupport && $.browser.opera ? tab : $iframe[0].contentWindow).focus();
            setTimeout(function () {
                (opt.operaSupport && $.browser.opera ? tab : $iframe[0].contentWindow).print();
                if (tab) {
                    tab.close();
                }
            }, 1000);
        }

        $.fn.jqprint.defaults = {
            debug: false,
            importCSS: true,
            printContainer: true,
            operaSupport: true
        };

        // Thanks to 9__, found at http://users.livejournal.com/9__/380664.html
        jQuery.fn.outer = function () {
            return $($('<div></div>').html(this.clone())).html();
        }
    })(jQuery);
</script>
<script type="text/javascript">
    // 保存条件临时map
    var termMap = {};
    $(function () {
        initData();
        // 遍历条件
        $("#termMain").html('');
        var i = 0;
        $.map(termMap, function (value, key) {
            var str = $("<div class=\"filter-item\"><div class='filter-content'><select style='width: 180px' name=\"termSelect\" id=\"" + value['termKey'] + "\" " +
                    "termName=\"" + value['termName'] + "\"" +
                    "termKey=\"" + value['termKey'] + "\"" +
                    "termColumn=\"" + value['termColumn'] + "\"" +
                    "dataset=\"" + value['dataSet'] + "\"" +
                    "cascadeTermId=\"" + value['cascadeTermId'] + "\"" +
                    "isFinalTerm=\"" + value['idFinalTerm'] + "\"" +
                    "termDataSourceType=\"" + value['termDataSourceType'] + "\"" +
                    "termDataSourceId=\"" + value['termDataSourceId'] + "\"" +
                    "parentNode=\"" + value['parentNode'] + "\"" +
                    "isQuery=\"" + value['isQuery'] + "\" class=\"form-control\" onchange=\"changeSelectData(this);\"" +
                    "</select></div></div>");
            $("#termMain").append(str);
        });
        loadSelect();
        var length = Object.keys(termMap).length;
        if (length < 1) {
            execQuery();
        } else {
            $("#termMain").append('<div class="filter-item"><button class="btn btn-lightblue" type="button" onclick="showReport()">查询</button><div>');
        }
        $("#termMain").append('<div class="filter-item"><button class="btn btn-lightblue js-add-kanban" onclick="exportToFile(\'0\')">导出pdf</button><div>');
        $("#termMain").append('<div class="filter-item"><button class="btn btn-lightblue js-add-kanban" onclick="exportToFile(\'1\')">导出excel</button><div>');
        $("#termMain").append('<div class="filter-item"><button class="btn btn-lightblue js-add-kanban" onclick="printReport()">打印</button><div>');
        $("#termMain").css('width', 180 * length + 500 + "px");
    });

    function initData() {
        var terms = '${reportTerms!}';
        if (terms != null && terms != '') {
            var idMap = {};
            var jsonObject = JSON.parse(terms);
            var j = 0;
            for (var i = 0; i < jsonObject.length; i++) {
                idMap[jsonObject[i].id] = jsonObject[i].termName;
                var map = {};
                map['termName'] = jsonObject[i].termName;
                map['termKey'] = jsonObject[i].termKey;
                map['termColumn'] = jsonObject[i].termColumn;
                map['dataSet'] = jsonObject[i].dataSet.replaceAll('@', '[').replaceAll('#', ']');
                map['isFinalTerm'] = jsonObject[i].isFinalTerm;
                map['isQuery'] = jsonObject[i].isQuery;
                map['termDataSourceType'] = jsonObject[i].dataSourceType;
                if (jsonObject[i].dataSourceType == '1') {
                    map['termDataSourceId'] = jsonObject[i].termDatabaseId;
                } else {
                    map['termDataSourceId'] = jsonObject[i].termApiId;
                }

                var parentNode = idMap[jsonObject[i].cascadeTermId];
                if (parentNode != null) {
                    map['parentNode'] = parentNode;
                }
                termMap[jsonObject[i].termName] = map;
            }
        }
    }


    function execQuery() {
        var data = {
            'id': $('#chart_id').val(),
            'unitId': $('#chart_unitId').val(),
            'name': $('#chart_name').val(),
            'dataSourceType': $('#datasource_type').val(),
            'dataSourceId': $('#datasource_id').val(),
            'dataSet': $('#data_set').val(),
            'templateId': $('#template_id').val()
        };
        var selectMap = {};
        // 放入选择的数据
        $.map(termMap, function (value, key) {
            var isFinalTerm = value['isFinalTerm'];
            if (isFinalTerm != undefined && isFinalTerm == '1') {
                selectMap[value['termKey']] = $("#" + value['termKey']).val();
                data['selectMap'] = JSON.stringify(selectMap);
            }
        });
        $.ajax({
            url: '${request.contextPath}/bigdata/report/template/queryReport',
            data: data,
            type: 'POST',
            dataType: 'html',
            beforeSend: function(){
                $('#reportMain').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
            },
            success: function (val) {
                //生成报表
                if (val.match("^\{(.+:.+,*){1,}\}$")) {
                    var rdata = jQuery.parseJSON(val);
                    val = rdata.msg;
                    layer.msg(val, {icon: 2});
                }

                $('#reportMain').html(val);
                var w = $('.jrPage tr:first td');

                w.each(function () {
                    if ($('.jrPage')[0].clientWidth > 1000 && $(this).width() > 570) {
                        if ($('.jrPage tr:first').height() == 0) {
                            $('.jrPage tr:eq(0) td:eq(0)').parent().remove();
                            return;
                        }
                    }
                    if ($('.jrPage')[0].clientWidth < 1000) {
                        if ($('.jrPage tr:first').height() == 0) {
                            $('.jrPage tr:eq(0) td:eq(0)').parent().remove();
                            return;
                        }
                    }
                });

                if ($('.jrPage')[0].clientHeight < 700) {
                    $('#reportMain').css('height', '700px')
                } else {
                    $('#reportMain').css('height', $('.jrPage')[0].clientHeight + 20)
                }

                $('#chart_type_list').attr('chart-show', 'true');
            },
            error: function () {
            }
        });
    }

    function loadSelectData(e) {
        $(e).empty();
        $(e).append("<option value=''>请选择" + $(e).attr('termName') + "</option>");
        var dataSet = $(e).attr('dataSet');
        var termColumn = $(e).attr('termColumn');
        var parentNode = $(e).attr('parentNode');

        var data = {
            dataSourceType: $(e).attr('termDataSourceType'),
            termDatabaseId: $(e).attr('termDataSourceId'),
            dataSet: dataSet
        };
        if (parentNode != null && parentNode != 'undefined') {
            var map = termMap[parentNode];
            // 获取级联select选中值
            var val = $("#" + map['termKey']).val();
            if (val == null || val == 'null' || val == '') {
                $(e).empty();
                $("select[name='termSelect']").each(function () {
                    var parentNode = $(this).attr('parentNode');
                    if (parentNode != null && parentNode == $(e).attr('termName')) {
                        loadSelectData($(this));
                    }
                });
                return;
            }
            if ($(e).attr('termDataSourceType') == '1') {
                data['dataSet'] = dataSet + " where " + termColumn + "='" + val + "'";
            } else {
                var selectMap = {};
                selectMap[map['termKey']] = val;
                // 放入选择的数据
                $.map(termMap, function (value, key) {
                    var isFinalTerm = value['isFinalTerm'];
                    if (isFinalTerm != undefined && isFinalTerm == '1') {
                        selectMap[value['termKey']] = $("#" + value['termKey']).val();
                    }
                });
                data['selectMap'] = JSON.stringify(selectMap);
            }
        }
        $.ajax({
                    type: "post",
                    dataType: "json",
                    data: data,
                    url: "${request.contextPath}/bigdata/report/queryCondition",
                    success: function (result) {
                        if (result.success) {
                            var datas = JSON.parse(result.data);
                            $.each(datas, function (index, value) {
                                var name = value.name;
                                if (name == null || name == "") {
                                    for (i in value) {
                                        if (i.search("Name") != -1) {
                                            name = value[i];
                                        }
                                    }
                                }
                                $(e).append("<option value='" + value.id + "'>" + name + "</option>");
                            });
                            $("select[name='termSelect']").each(function () {
                                var parentNode = $(this).attr('parentNode');
                                if (parentNode != null && parentNode == $(e).attr('termName')) {
                                    loadSelectData($(this));
                                }
                            });
                        } else {
                            layer.msg($(e).attr('termName') + '条件sql不正确:' + result.message, {icon: 2});
                        }

                    }
                }
        );

    }

    function loadSelect() {
        // 加载所有一级下拉框
        var sl = $("select[name='termSelect']");
        sl.each(function () {
            var parentNode = $(this).attr('parentNode');
            if (parentNode == null || parentNode == 'undefined') {
                loadSelectData($(this));
            }
        });
    }

    function changeSelectData(e) {
        // 加载子类下拉框数据
        $("select[name='termSelect']").each(function () {
            var parentNode = $(this).attr('parentNode');
            var value = $(e).val();
            if (parentNode != null && parentNode == $(e).attr('termName')) {
                loadSelectData($(this));
            }
        });
        var isQuery = $(e).attr('isQuery');
        if (isQuery == '1') {
            showReport();
        }
    }


    function showReport() {
        var isSelect = true;
        $.each(termMap, function (key, value) {
            var id = value['termKey'];
            var sel = $('#' + id).val();
            if (sel == null || sel == '') {
                layer.msg('请先选择查询条件', {icon: 2});
                isSelect = false;
                return;
            }
        });
        if (isSelect) {
            execQuery();
        }
    }

    function exportToFile(type) {
        var isSelect = true;
        $.each(termMap, function (key, value) {
            var id = value['termKey'];
            var sel = $('#' + id).val();
            if (sel == null || sel == '') {
                layer.msg('请先选择查询条件', {icon: 2});
                isSelect = false;
                return;
            }
        });
        if (isSelect) {
            var id = $('#chart_id').val();
            var array = [];
            // 放入选择的数据
            $.map(termMap, function (value, key) {
                var isFinalTerm = value['isFinalTerm'];
                if (isFinalTerm != undefined && isFinalTerm == '1') {
                    array.push(value['termKey'] + '~' + $("#" + value['termKey']).val());
                }
            });
            var url = '${request.contextPath}/bigdata/report/exportToFile?id=' + id + '&type=' + type + '&selectMap=' + array.join("$");
            window.location.href = url;
        }
    }

    function printReport() {

        $('#reportMain').jqprint();

    }
</script>
<script src="http://www.jq22.com/jquery/jquery-migrate-1.2.1.min.js"></script>