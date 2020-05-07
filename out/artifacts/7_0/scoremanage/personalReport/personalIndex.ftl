<#-- copy from D:\ideaWorkplace\v7\scoremanage\web\scoremanage\scoreReport\scoreReportHead.ftl-->
<#-- panlf -->

<link rel="stylesheet" href="${request.contextPath}/static/css/pages.css"/>
<div id="contain-box">
    <div class="box box-default">
        <ul class="nav nav-tabs nav-tabs-1" role="tablist">
            <li role="presentation" class="active">
                <a role="tab" data-toggle="tab" href="#aaa" onclick="doChangeTab(0)">在校生</a>
            </li>
            <li role="presentation">
                <a role="tab" data-toggle="tab" href="#bbb" onclick="doChangeTab(1)">毕业生(包括离校生)</a>
            </li>
        </ul>
        <br>
        <div class="box-body" style="padding: 8px">
            <div class="filter">
                <div class="filter-item">
                    <span class="filter-name" id="gradeType">年级：</span>
                    <div class="filter-content">
                        <select name="" id="gradeList" class="form-control" onchange="doChangeGrade('');"
                                style="width: 150px">
                            <option value="">暂无数据</option>
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

        </div>
        <#--<div class="col-sm-1" style="width:30px"></div>-->
        <div id="submitForm" class="box-default">

        </div>
    </div>
</div>
<script type="text/javascript" src="${request.contextPath}/static/js/LodopFuncs.js"></script>

<script>


    var type = '';  //classExport   or  stuExport
    var exportId = '';

    $(function () {
        jQuery.ajaxSettings.async = false;
        //在校生年级列表
        getGradeList(0);
        //学生数刷新
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
            $('#submitForm').load('${request.contextPath}/scoremanage/personal/stuReport?studentId=' + exportId);
        }
    }

    //年级切换
    function doChangeGrade() {
        //个人信息展示区清空
        $('#print').html('');
        var gradeId = $('#gradeList').val();
        if (gradeId != '') {
            var url = '${request.contextPath}/scoremanage/recommended/stuTree?callback=treeCallback&gradeId=' + gradeId;
            $('#ztree-box').load(url);
        }

    }

    function getGradeList(type) {
        var gradeUrl = '${request.contextPath}/scoremanage/recommended/getGradeList?type=' + type;
        $.get(gradeUrl, function (data) {
            data = JSON.parse(data);
            if (data['code'] == '00') {
                var grades = data['gradeList'];
                $('#gradeList').html('');
                for (var i = 0; i < grades.length; i++) {
                    var element = '<option value="' + grades[i]['gradeId'] + '">' + grades[i]['name'] + '</option>'
                    $('#gradeList').append(element);
                }
            } else {
                $('#gradeList').html('<option value="">暂无数据</option>');
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
        LODOP.ADD_PRINT_HTM("20mm", "15mm", "RightMargin:20mm", "BottomMargin:15mm", getPrintContent($("#print")));
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

    function checkPrintTarget() {
        if (type == '' || exportId == '') {
            layer.msg('请选择一个班级,或者一个学生');
            return false;
        }
        return true;
    }

    //excel导出
    function doExport() {
        if (!checkPrintTarget()) {
            return;
        }
        var url = '${request.contextPath}/scoremanage/excelExport/03/' + type + '/' + exportId;

        var dataCheckUrl = '${request.contextPath}/scoremanage/excelExport/03/checkDataCount?';
        var tagerPerson;
        if (type == 'stuExport') {
            dataCheckUrl += "studentId=" + exportId;
            tagerPerson = '学生';
        } else {
            dataCheckUrl += "classId=" + exportId;
            tagerPerson = '班级';
        }
        $.get(dataCheckUrl, function (data) {
            if (data == "0") {
                layer.msg("该" + tagerPerson + "暂无数据");
            } else {
                document.location.href = url;
            }
        })
    }

    //统计
    var staticFlaging = false;

    function doStatistic() {
        if (staticFlaging) return;
        staticFlaging = true;
        var gradeId = $('#gradeList').val();
        if (gradeId != null && gradeId != '' && gradeId != undefined) {
            var vi = layer.load();
            var url = '${request.contextPath}/scoremanage/personal/statistic?gradeId=' + gradeId;
            $.ajax({
                url: url,
                type: "get",
                contentType: "application/json",
                dataType: "json",
                success: function (data) {
                    layer.closeAll();
                    if (data['code'] == '00') {
                        layer.msg(data['msg'])
                    } else {
                        layer.msg(data['msg'])
                    }
                    staticFlaging = false;
                },
                error: function () {
                    layer.closeAll();
                    staticFlaging = false;
                }
            })
        }
    }

    function doChangeTab(type) {
        exportId = '';

        var gradeList = ['年级:&nbsp;&nbsp;', '毕业年份:&nbsp;&nbsp;']
        $('#gradeType').html(gradeList[type]);
        //加载年级列表
        getGradeList(type);
        //清空展示区
        $('#ztree-box').html('')
        //清空班级树
        $('#submitForm').html('')
    }
</script>
