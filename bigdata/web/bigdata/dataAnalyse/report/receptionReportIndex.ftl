<script src="${request.contextPath}/bigdata/v3/static/plugs/chosen/chosen.jquery.min.js"></script>
<div class="box box-structure">
    <div class="box-header clearfix">
        <div class="form-group search">
            <div class="input-group">
                <input type="text" maxlength="100" id="reportName" class="form-control"
                       placeholder="输入名称查询">
                <a href="javascript:void(0);" onclick="searchReport()" class="input-group-addon" hidefocus="true"><i class="wpfont icon-search"></i></a>
            </div>
        </div>
        <div class="form-group clearfix">
            <button class="btn btn-lightblue js-add-kanban" onclick="addMultiReport('${type!}')">新建<#if type!  ==6>看板<#else>报告</#if></button>
        </div>
    </div>
    <div class="box-body" id="reportDiv"></div>
    <!--弹窗-->
    <div class="layer add-kanban" id="addReportDiv"></div>
</div>
<div class="layer kanban-set-div">

</div>
<script>
    var reportSubmit = false;

    function searchReport() {
        $('#reportDiv').load('${request.contextPath}/bigdata/data/analyse/list?type=${type}&reportName='
            + $('#reportName').val());
    }

    //新增基本图表
    function addMultiReport(type) {
        $.ajax({
            url: '${request.contextPath}/bigdata/data/analyse/add',
            type: 'POST',
            data: {
                type: type
            },
            dataType: 'html',
            success: function (response) {
                $('#addReportDiv').empty().append(response);
            }
        });
        layer.open({
            type: 1,
            title: '新建<#if type!  ==6>看板<#else>报告</#if>',
            area: ['520px', '175px'],
            btn: ['确定', '取消'],
            content: $('.add-kanban'),
            yes: function (index, layero) {
                if (reportSubmit) {
                    return;
                }
                reportSubmit = true;
                
                var check = checkValue('#reportSubmitForm');
				if(!check){
				 	reportSubmit=false;
				 	return;
				}

                var options = {
                    url: "${request.contextPath}/bigdata/data/analyse/save",
                    dataType: 'json',
                    success: function (data) {
                        layer.close(index);
                        if (!data.success) {
                            showLayerTips4Confirm('error', data.message);
                        } else {
                            showLayerTips('success', '新增<#if type!  ==6>看板<#else>报告</#if>成功', 't');
                            $('#reportDiv').load('${request.contextPath}/bigdata/data/analyse/list?type=${type!}');
                        }
                        reportSubmit = false;
                    },
                    clearForm: false,
                    resetForm: false,
                    type: 'post',
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        reportSubmit = false;
                    }//请求出错
                };
                $("#reportSubmitForm").ajaxSubmit(options);
            }
        });
    }

    function deleteMultiReport(id, name) {
        showConfirmTips('prompt', "提示", "确认删除" + name + "吗？", function () {
            $.ajax({
                url: '${request.contextPath}/bigdata/data/analyse/delete',
                data: {reportId: id},
                dataType: 'json',
                success: function (val) {
                    if (val.success) {
                        showLayerTips('success', '删除<#if type!  ==6>看板<#else>报告</#if>成功', 't');
                        $('#reportDiv').load('${request.contextPath}/bigdata/data/analyse/list?type=${type}');
                    } else {
                        showLayerTips4Confirm('error', val.message);
                    }
                }
            });
        });
    }

    function editMultiReport(id, type) {
        $('.kanban-set-div').html("<div class='pos-middle-center'><h4><img src='${request.contextPath}/static/ace/img/loading.gif' />玩命的加载中......</h4></div>");
        $('.kanban-set-div').load('${request.contextPath}/bigdata/data/analyse/edit?reportId=' + id + '&type=' + type);
        layer.open({
            type: 1,
            title: '<#if type!  ==6>看板<#else>报告</#if>设置',
            area: ['750px', '600px'],
            btn: ['确定', '取消'],
            content: $('.kanban-set-div'),
            yes: function (index, layero) {

                if (reportSubmit) {
                    return;
                }
                reportSubmit = true;

                _chart_json.name = $('#rName').val();
                if (_chart_json.name.length == 0) {
                    tips('名称不能为空', '#rName');
                    reportSubmit = false;
                    return;
                }

                if (_chart_json.name.replace(/[\u0391-\uFFE5]/g, "aa").length > 50) {
                    tips('名称长度不能超过50', '#rName');
                    reportSubmit = false;
                    return;
                }

                _chart_json.folderId = $('#folderId').val();
                if (_chart_json.folderId == null) {
                    $('.basicSet').click();
                    tips('请选择文件夹', '#folderId');
                    reportSubmit = false;
                    return;
                }

                _chart_json.orderId = $('#rOrderId').val();
                if (_chart_json.orderId.length == 0) {
                    tips('排序号不能为空!', '#orderId');
                    reportSubmit = false;
                    return;
                }
                _chart_json.remark = $('#remark').val();

                var data = _chart_json ? _chart_json : {};

                //ajax调用保存
                $.ajax({
                    url: '${request.contextPath}/bigdata/data/analyse/saveMultiReport',
                    data: data,
                    type: 'POST',
                    dataType: 'json',
                    success: function (val) {
                        if (!val.success) {
                            layer.msg(val.message, {icon: 2, time: 2000});
                        }
                        else {
                            layer.close(index);
                            showLayerTips('success', '保存成功', 't');
                            searchReport();
                            $('.kanban-set-div').empty();
                        }
                        reportSubmit = false;
                    },
                    error: function () {
                        $('#tagsList').load('${request.contextPath}/bigdata/chart/layer/tags?chartId=' + $('#multiReportId').val());
                    }
                });

            },
        });
    }

    $(document).ready(function () {
        $('#reportDiv').load('${request.contextPath}/bigdata/data/analyse/list?type=${type}');
        initSelection();

        $('#select').on('change', function () {
            searchReport();
        });
    });

    function tips(msg, key) {
        layer.tips(msg, key, {
            tipsMore: true,
            tips: 3,
            time: 2000
        });
    }

    //初始化标签查询下拉框
    function initSelection() {
        $('#select').chosen({
            allow_single_deselect: true,
            disable_search_threshold: 10,
            no_results_text: '没有找到标签'
        }).change(searchReport);

        $(window).off('resize.chosen')
                .on('resize.chosen', function () {
                    $('.chosen-select').each(function () {
                        $(this).next().css({'width': '265px'});
                    })
                }).trigger('resize.chosen');
        $('.chosen-choices').css('height', '32px');
    }
</script>