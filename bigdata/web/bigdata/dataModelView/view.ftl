<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/chosen/chosen.min.css">
<script defer src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<div class="box box-default">
    <div class="box-body">
        <div class="report-view">
            <#if showTitle?string('yes', 'no') == 'yes'>
                <h1 align="center">${favorite.favoriteName!}</h1>
                <hr>
            </#if>


            <#if dataModelParams?exists && dataModelParams?size gt 0>
                <div class="pos-rel mb-30 condition-div">
                    <div class="filter-made no-radius">
                        <#list dataModelParams as param>
                            <div class="filter-item">
                                <div class="form-group">
                                    <select multiple=""
                                            class="form-control chosen-select condition_${favorite.id!}"
                                            param_id="${param.id!}" data-placeholder="选择${param.name!}筛选"
                                            style="width:300px;">
                                    </select>
                                </div>
                            </div>
                        </#list>
                    </div>
                </div>
            </#if>


            <#if favorite.isShowChart?default(1) == 1>
                <div class="">
                    <div class="score-title no-margin pos-rel">
                        <span>&nbsp;</span>
                        <span class="pos-right-c chart_type" favorite_id="${favorite.id!}">
                                <span class="${favorite.id!} fake-btn-img fake-btn-default no-radius-right no-border-right <#if favorite.chartType?default('bar') == 'line'>active</#if>"
                                      type="line" id=""><img
                                            src="${request.contextPath}/static/bigdata/images/icon-line.png"/></span>
                                <span class="${favorite.id!} fake-btn-img fake-btn-default no-radius <#if favorite.chartType?default('bar') == 'bar'>active</#if>"
                                      type="bar"><img src="${request.contextPath}/static/bigdata/images/icon-bar.png"/></span>
                                <span class="${favorite.id!} fake-btn-img fake-btn-default no-radius-left no-border-left <#if favorite.chartType?default('bar') == 'cookie'>active</#if>"
                                      type="cookie"><img
                                            src="${request.contextPath}/static/bigdata/images/icon-pie.png"/></span>
                            </span>
                    </div>
                    <div class="chart-bar common-chart-view" id="chartDiv_${favorite.id!}" style="height: 500px;"></div>
                </div>
            </#if>
            <div class="table-container">
                <div class="table-container-body" id="main_${favorite.id!}" style="overflow: auto;">
                    <table id="multiDimensionTable_${favorite.id!}"
                           class="table table-bordered table-striped table-hover text-center display"
                           style="text-align: center" width="100%">
                        <thead id="head_${favorite.id!}"></thead>
                        <tbody id="data_${favorite.id!}"></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <input type="hidden" id="favoriteId" value="${favorite.id!}">
    <#if filterMap?exists && filterMap?size gt 0>
        <#list filterMap?keys as key>
            <input type="hidden" id="${favorite.id!}_paramValue_${key}"
                   value="<#list filterMap[key] as item>${item!},</#list>">
        </#list>
    </#if>
</div>
<script>

    $(function () {
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

        // 加载条件选择
        if ($('.condition_' + '${favorite.id!}').length > 0) {
            $('.condition_' + '${favorite.id!}').each(function (i, v) {
                var $this = $(v);
                var paramId = $(v).attr('param_id');
                // 查询过滤数据
                $.ajax({
                    url: '${request.contextPath}/bigdata/model/getFilterData',
                    data: {
                        code: '${code!}',
                        dimensionId: paramId,
                        modelDatasetId: '${modelDatasetId!}'
                    },
                    type: 'POST',
                    dataType: 'json',
                    async: false,
                    success: function (response) {
                        if (!response.success) {
                            layer.msg(response.message, {icon: 2});
                        } else {
                            var data = response.data;
                            var paramValue = $('#${favorite.id!}' + '_paramValue_' + paramId).val();
                            $.each(data, function (i, v) {
                                if (paramValue != null && paramValue.indexOf(v) != -1) {
                                    $this.append('<option selected=selected>' + v + '</option>');
                                } else {
                                    $this.append('<option>' + v + '</option>');
                                }

                            });
                            $this.chosen({
                                allow_single_deselect: true,
                                disable_search_threshold: 10,
                                no_results_text: '没有找到组'
                            });
                        }
                    }
                });
            });
            initSelection();
        }

        $('.chart_type').on('click', 'span.fake-btn-img', function () {
            $(this).addClass('active').siblings('span').removeClass('active');
            var type = $(this).attr('type');
            var favoriteId = $(this).parent().attr('favorite_id');
            showChart(favoriteId);
        });

        $('.condition_' + '${favorite.id!}').on('change', function () {
            if ('${favorite.isShowChart?default(1)}' == '1') {

                showChart('${favorite.id!}');
            }
            showReport();
        });

        if ('${favorite.isShowChart?default(1)}' == '1') {
            showChart('${favorite.id!}');
        }
        showReport();

        //初始化标签查询下拉框
        function initSelection() {
            if ($('.chosen-select').length > 0) {
                $(window).off('resize.chosen')
                    .on('resize.chosen', function () {
                        $('.chosen-select').each(function () {
                            var $this = $(this);
                            $this.next().css({'width': $this.width()});
                        })
                    }).trigger('resize.chosen');

            }
        }

        function showReport() {
            var filterDataMap = [];
            if ($('.condition_' + '${favorite.id!}').length > 0) {
                filterDataMap = {};
                $('.condition_' + '${favorite.id!}').each(function (i, v) {
                    var paramId = $(v).attr('param_id');
                    if ($(v).val() != null) {
                        filterDataMap[paramId] = $(v).val();
                    }
                });
            }

            $.ajax({
                url: '${request.contextPath}/bigdata/model/view/showReport',
                data: {
                    favoriteId: '${favorite.id!}',
                    filterDataMap: JSON.stringify(filterDataMap)
                },
                type: 'POST',
                dataType: 'json',
                success: function (response) {
                    if (!response.success) {
                        return;
                    } else {
                        var dataList = [];
                        var columnList = [];
                        var columnDefs = [];
                        if (table) {
                            table.destroy(); //还原初始化了的dataTable
                            $("#head_" + "${favorite.id!}").empty();
                            $("#data_" + "${favorite.id!}").empty();
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

                        $("#head_" + "${favorite.id!}").html(result.head);

                        for (var i = 0; i < dimRowSize - 1; i++) {
                            columnDefs.push(originalColumnDefs[i])
                        }
                        createTable(dataList, columnList, columnDefs);
                    }
                }
            });
        }

        function showChart(favoriteId) {
            var echart_div = echarts.init(document.getElementById('chartDiv_' + favoriteId));
            echart_div.showLoading({
                text: '数据正在努力加载...'
            });
            var filterDataMap = [];
            if ($('.condition_' + '${favorite.id!}').length > 0) {
                filterDataMap = {};
                $('.condition_' + '${favorite.id!}').each(function (i, v) {
                    var paramId = $(v).attr('param_id');
                    if ($(v).val() != null) {
                        filterDataMap[paramId] = $(v).val();
                    }
                });
            }

            $.ajax({
                url: '${request.contextPath}/bigdata/model/view/showChart',
                data: {
                    favoriteId: favoriteId,
                    filterDataMap: JSON.stringify(filterDataMap),
                    type: $('span.fake-btn-img.active.' + favoriteId).attr('type')
                },
                type: 'POST',
                dataType: 'json',
                success: function (response) {
                    if (!response.success) {
                        return;
                    } else {
                        document.getElementById('chartDiv_' + favoriteId).setAttribute('_echarts_instance_', '');
                        echart_div = echarts.init(document.getElementById('chartDiv_' + favoriteId));
                        var data = JSON.parse(response.data);
                        echart_div.setOption(data);
                        echart_div.hideLoading();
                        echart_div.resize();
                        // echart_div.on('click', function (params) {
                        //    alert('你点击的是: ' + params.dataIndex);
                        // });
                    }
                }
            });
        }

        function createTable(dataList, columnList, columnDefs) {
            table = $("#multiDimensionTable_" + "${favorite.id!}").DataTable({
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
                columnDefs: columnDefs,
                createdRow: function (row, data, dataIndex) {
                    //$('td:eq(0)', row).html( '<h2>A</h2>' );
                    //$(row).addClass('important');
                    //$('td', row).eq(0).css('backgroundColor','#00E400');
                }
            });


            // $('#multiDimensionTable tbody').on('click','td', function (e) {
            //     var cellindex = $(this).parent().context._DT_CellIndex.column; //列号
            //     var rowindex = $(this).parent().context._DT_CellIndex.row; //行号
            //     var data =table.rows(rowindex).data()[0];
            //
            //     var nRow = $(this).parent();//得到这一行
            //     var aData = $('#multiDimensionTable').dataTable().fnGetData(nRow);
            //     console.log(aData);
            //     //	var name = $(this).text();
            //     //alert(name)
            //     // alert(data[0]+"--"+data[1]+"--"+data[2])
            //     //	alert(cellindex)
            //     //	alert(rowindex)
            // } );

            table.draw(true);
        }

        $('.table-bordered tbody').on('mouseover', 'td', function () {
            var column = table.cell(this).index().column;
            var row = table.cell(this).index().row;
            //alert(column+"--"+row)
            $(this).css('backgroundColor', 'red');
        });
        $('.table-bordered tbody').on('mouseleave', 'td', function () {
            $(this).css('backgroundColor', '#ffffff');
        });
    });

    function toExcel() {
        $('#multiDimensionTable_' + '${favorite.id!}').table2excel({
            name: "Excel Document Name",
            filename: "多维报表分析.xls",
            exclude_img: true,
            exclude_links: true,
            exclude_inputs: true
        });
    }

    function toPdf() {
        var targetDom = $("#multiDimensionTable_" + "${favorite.id!}");
        var copyDom = targetDom.clone();
        copyDom.width(targetDom.width() + "px");
        copyDom.height(targetDom.height() + "px");
        copyDom.css('background', '#fff');
        copyDom.css({
            "position": "absolute",
            "top": "0px",
            "z-index": "-1"
        });
        $('#multiDimensionTable_' + "${favorite.id!}").append(copyDom);
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
</script>