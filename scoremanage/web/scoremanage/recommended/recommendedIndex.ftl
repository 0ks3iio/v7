<#-- panlf -->
<link rel="stylesheet" href="${request.contextPath}/static/css/pages.css"/>
<div id="itemShowDivId">
    <div class="box box-default">
        <div class="box-body">

            <div class="filter">
                <div class="filter-item">
                    <span class="filter-name">年级：</span>
                    <div class="filter-content">
                        <select name="" id="gradeList" class="form-control" onchange="doChangeGrade('');">
                            <#if gradeList?? && gradeList?size gt 0>
                                <#list gradeList as grade>
                                    <option value="${grade.gradeId!}">${grade.name!}</option>
                                </#list>
                            <#else >
                                <option value="">暂时数据</option>
                            </#if>
                        </select>
                    </div>
                </div>

                <div class="filter-item filter-item-right">
                    <button class="btn btn-white" onclick="doStatistic()">统计</button>
                    <button class="btn btn-blue" onclick="doPrintResult()">打印</button>
                    <button class="btn btn-lightgreen" onclick="doExport();">导出</button>
                </div>

            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-3" id="ztree-box">
            <#--ztree -->

        </div>
        <div class="box-default" id="submitForm">

        </div>
    </div>

    <script type="text/javascript" src="${request.contextPath}/static/js/LodopFuncs.js"></script>

    <script>
        var type = '';  ////classExport   or  stuExport
        var exportId = '';
        var staticFlaging = false;
        $(function () {

            jQuery.ajaxSettings.async = false;
            //在校生年级列表
            doChangeGrade();
            jQuery.ajaxSettings.async = true;
        })


        //ztree回调
        function treeCallback(treeId, treeNode) {
            if (treeNode.isParent) {
                type = "classExport";
                exportId = treeNode['id'];
            } else {
                exportId = treeNode['id'];
                type = "stuExport";
                $('#submitForm').load('${request.contextPath}/scoremanage/recommended/stuReport?studentId=' + exportId)
            }
        }

        //年级切换
        function doChangeGrade() {
            type = '';
            exportId = '';
            $('#submitForm').html('')
            var gradeId = $('#gradeList').val();
            if (gradeId != '') {
                var url = '${request.contextPath}/scoremanage/recommended/stuTree?callback=treeCallback&gradeId=' + gradeId;
                $('#ztree-box').load(url);
            }
        }


        //统计
        function doStatistic() {
            if (staticFlaging) return;
            staticFlaging = true;
            var gradeId = $('#gradeList').val();
            if (gradeId != null && gradeId != '') {
                var url = '${request.contextPath}/scoremanage/recommended/statistic?gradeId=' + gradeId;
                layer.load();
                $.ajax({
                    url: url,
                    type: "get",
                    contentType: "application/json",
                    dataType: "json",
                    success: function (data) {
                        //data=JSON.parse(data);
                        layer.closeAll();
                        if (data['code'] == '00') {
                            layer.msg(data.msg, {
                                offset: 't',
                                time: 2000
                            });
                            //layer.msg(data['msg'])
                        } else {
                            layer.msg(data.msg, {
                                offset: 't',
                                time: 2000
                            });
                            //layer.msg(data['msg'])
                        }
                        staticFlaging = false;
                    },
                    error: function () {
                        staticFlaging = false;
                        layer.closeAll();
                    }
                })
            } else {
                layer.msg('请先选择一个年级');
            }
        }

        function checkPrintTarget() {
            if (type == '' || exportId == '') {
                layer.msg('请选择一个班级,或者一个学生');
                return false;
            }
            return true;
        }

        //导出
        function doExport() {
            if (!checkPrintTarget()) {
                return;
            }
            var url = '${request.contextPath}/scoremanage/excelExport/02/' + type + '/' + exportId;
            var dataCheckUrl = '${request.contextPath}/scoremanage/excelExport/02/checkDataCount?';
            var tagerPerson;
            if(type=='stuExport'){
                dataCheckUrl+="studentId="+exportId;
                tagerPerson='学生';
            }else{
                dataCheckUrl+="classId="+exportId;
                tagerPerson='班级';
            }
            $.get(dataCheckUrl, function (data) {
                if (data == "0") {
                    layer.msg("该"+tagerPerson+"暂无数据");
                }else{
                    document.location.href = url;
                }
            })
        }

        //打印
        function doPrintResult() {
            if (type != 'stuExport' || exportId == '') {
                layer.msg('请选择一个学生');
                return false;
            }
            layerTime();
            var LODOP = getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
            if (LODOP == undefined || LODOP == null) {
                layerClose();
                return;
            }
            LODOP.ADD_PRINT_HTM("20mm", "15mm", "RightMargin:15mm", "BottomMargin:15mm", getPrintContent($("#print")));
            LODOP.SET_PRINT_PAGESIZE(1, 0, 0, "");//横向打印
            LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED", 1);//横向时的正向显示
            LODOP.PREVIEW();//打印预览
            layerClose();
        }

        function layerTime() {
            oneTime = layer.msg('加载中', {
                icon: 16,
                shade: 0.01,
                time: 60 * 1000
            });
        }

        function layerClose() {
            layer.close(oneTime);
        }

    </script>
</div>
