<link rel="stylesheet" href="${request.contextPath}/static/css/pages.css"/>
<!-- chosen -->
<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<!-- chartsScript -->
<script src="${request.contextPath}/static/js/chartsScript.js"></script>
<div class="box box-default">
    <div class="box-body">
        <ul class="nav nav-tabs nav-tabs-1" role="tablist">
            <li role="presentation" class="active">
                <a role="tab" data-toggle="tab" href="#aaa" onclick="doChangeTab('1')">在校生</a>
            </li>
            <li role="presentation">
                <a role="tab" data-toggle="tab" href="#bbb" onclick="doChangeTab('2')">毕业生(包括离校生)</a>
            </li>
        </ul>
        <br>
        <div class="box-transparent" id="plan_set_contain">


            <div class="filter">
                <div class="filter-item block" id="in_school">
                    <div class="row">
                        <div class="col-xs-3">
                            <label for="" class="filter-name">年级：</label>
                            <div class="filter-content">
                                <select tyle="display:none" class="form-control" id="grade_yes" name="searchType"
                                        onChange="doChangeDate('grade')" style="width: 150px">
                                </select>
                            </div>
                        </div>

                        <div class="col-xs-6">
                            <label for="" class="filter-name">学年学期：</label>
                            <div class="filter-content">
                                <select class="form-control" id="acadyear-select" onChange="doChangeDate('acadyear')">
                                </select>
                                <select class="form-control" id="semester-select" name="searchType"
                                        onChange="doChangeDate('semester')">
                                </select>
                            </div>

                        </div>
                        <button class="btn btn-blue" onclick="savePlanSet()">保存</button>
                    </div>
                </div>
            </div>


        </div>
        <div id="showDiv">
            <div class="box box-transparent listDiv">
                <div class="row">
                    <div class="col-sm-3">
                        <div class="list-group" id="plan-list" style="max-height:650px;overflow:auto;">
                        </div>
                    </div>

                    <div class="col-sm-7">
                        <div class="box box-default">
                            <div class="table-wrapper" id="showList">
                                <table class="table table-bordered table-striped table-hover">
                                    <thead>
                                    <tr>
                                        <th>科目</th>
                                        <th>成绩类型</th>
                                    </tr>
                                    </thead>
                                    <tbody id="sub-list"></tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>

<script>
    var gradeMap = {};
    var semesterSet = [];

    function loadSubList(planId) {
        $('#sub-list').find('tr').hide();
        $('#sub-list').find('.class-' + planId).show();
    }


    //plan 列表
    //学年学期,年级id 查找数据
    function doChangeDate(type) {
        if (type=="grade"){
            changeGrade($('#grade_yes').val().trim())
        } else if(type=="acadyear"){
            changeAcadyear();
        }
        $('#plan-list').html('');
        $('#sub-list').html('');
        var gradeId = $('#grade_yes').val().trim();
        var acadyear = $('#acadyear-select').val().trim();
        var semester = $('#semester-select').val().trim();
        var url = '${request.contextPath}/scoremanage/planSet/getPlanList?gradeId=' + gradeId + "&acadyear=" + acadyear + "&semester=" + semester;
        $.get(url, function (data) {
            data = JSON.parse(data);
            if (data.code == '00') {
                var result = data['result'];
                for (var i = 0; i < result.length; i++) {
                    var check = "";
                    if (result[i]['isUsing'] == 1) {
                        check = 'checked'
                    }
                    var active = '';
                    //默认选中第一个
                    if (i == 0) active = 'active';
                    //拼接 左侧方案列表
                    var e = '<a href="javascript:;" class="list-group-item exam-list ' + active + '" data="' + result[i]['planId'] + '" data-examId="' + result[i]['examId'] + '">' +
                        '                               <label class="pos-rel">\n' +
                        '                                   <input type="checkbox" ' + check + ' class="wp  plan-check" >\n' +
                        '                                   <span class="lbl"></span>\n' +
                        '                               </label>' + result[i]['examName'] +
                        '                           </a>';
                    $('#plan-list').append(e);
                    var subList = result[i]['subList'];
                    if (subList.length > 0) {
                        for (var j = 0; j < subList.length; j++) {
                            var inputType = "";
                            if (subList[j]['inputType'] == 'S') {
                                inputType = '   <label class="pos-rel sum">' +
                                    '     <input type="radio" class="wp " name="radio-' + subList[j]['planId'] + subList[j]['subId'] + '" value="2">' +
                                    '         <span class="lbl">总评成绩</span>' +
                                    '   </label>';
                            }
                            var checked = "";
                            if (subList[j]['isUsing']) {
                                checked = 'checked';
                            }
                            //拼接右侧 单选项
                            var e = '<tr class="class-' + subList[j]['planId'] + '"data=' + subList[j]['planId'] + '>' +
                                '  <td  data="' + subList[j]['subId'] + '">' +
                                '   <label class="pos-rel">' +
                                '     <input type="checkbox" ' + checked + ' class="wp checkbox-sub" >' +
                                '         <span class="lbl">' + subList[j]['subName'] + '</span>' +
                                '   </label>' +
                                '  </td>' +
                                '  <td>' +
                                '   <label class="pos-rel exam">' +
                                '     <input type="radio" class="wp " name="radio-' + subList[j]['planId'] + subList[j]['subId'] + '" value="1">' +
                                '         <span class="lbl">考试成绩</span>' +
                                '   </label>' +
                                inputType
                            '  </td>' +
                            '</tr>';
                            $('#sub-list').append(e);

                            //1 考试   2 总评
                            if (subList[j]['type'] == '1') {
                                $("input[name='radio-" + subList[j]['planId'] + subList[j]['subId'] + "']").get(0).checked = true;
                            } else {
                                $("input[name='radio-" + subList[j]['planId'] + subList[j]['subId'] + "']").get(1).checked = true;
                            }
                        }
                    }

                }
                loadSubList(result[0]['planId']);
            } else {
                //清空盒子
                $('#plan-list').html('');
            }
        })
    }

    $(function () {
        // 0 在校生  1: 离校和毕业
        iniGradeData(0)
    })

    function changeAcadyear() {
        var acad = $('#acadyear-select').val().trim();
        var semesterTemp=[];
        if(acad!=''){
            semesterTemp=gradeMap[$('#grade_yes').val().trim()][acad];
        }else{
            semesterTemp=semesterSet;
        }
        semester = $('#semester-select');
        semester.html('<option value="">请选择</option>')
        semesterTemp.sort();
        for (var i = 0; i < semesterTemp.length; i++) {
            var seme = "";
            if (semesterTemp[i] == '1') {
                seme = '第一学期'
            }
            if (semesterTemp[i] == '2') {
                seme = '第二学期'
            }
            var e = '<option value="' + semesterTemp[i] + '">' + seme + '</option>'
            semester.append(e);
        }
    }

    function changeGrade(gradeId) {
        var acadMap = gradeMap[gradeId];
        var acadList = [];
        if (acadMap==null){
            return;
        }
        for(var val in acadMap){
            acadList.push(val);
            var semesterArr = acadMap[val];
            for (var i = 0; i < semesterArr.length; i++) {
                if (!semesterSet.includes(semesterArr[i])) {
                    semesterSet.push(semesterArr[i]);
                }
            }
        }


        acadList.sort();
        acadyear = $('#acadyear-select');
        acadyear.html('<option value="">请选择</option>')
        for (var i = 0; i < acadList.length; i++) {
            var e = '<option value="' + acadList[i] + '">' + acadList[i] + '学年</option>'
            acadyear.append(e);
        }
        changeAcadyear();
    }

    function iniGradeData(type) {
        var url = '${request.contextPath}/scoremanage/planSet/getAllGrade?type=' + type;
        $('#semester-select').html('');
        $('#acadyear-select').html('');
        $('#plan-list').html('');
        $('#sub-list').html('');
        $.get(url, function (result) {
            var data = JSON.parse(result);
            if (data.code == '00') {
                gradeMap = data['map'];
                var gradeList = data['gradeList'];
                var gradeSelect = $('#grade_yes');
                gradeSelect.html('');
                for (var i = 0; i < gradeList.length; i++) {
                    var e = '<option value="' + gradeList[i]['gradeId'] + '">' + gradeList[i]['name'] + '</option>'
                    gradeSelect.append(e);
                }
                var defaultGradeId = gradeList[0]['gradeId'];
                changeGrade(defaultGradeId);
                doChangeDate('grade');
            } else {
                $('#grade_yes').html('<option value="">暂无数据</option>')
                $('#semester-select').html('<option value="">暂无数据</option>')
                $('#acadyear-select').html('<option value="">暂无数据</option>')
                $('#plan-list').html('<label class="pos-rel"><span class="lbl"></span></label>');
            }
        })

    }

    function doChangeTab(tabIndex) {
        if (tabIndex == '1') {
            iniGradeData(0)   //在校
        } else if (tabIndex == '2') {
            iniGradeData(1)  //离校 毕业
        }
    }

    function planCheck() {
        //如果选了一门科目,则 把这个plan 勾上
        var planId = $('#plan-list').find('.active').attr('data');
        if ($('.class-' + planId).find('input[type=checkbox]:checked').length > 0) {
            $('#plan-list').find('.active').find('input').prop('checked', true);
        } else {
            $('#plan-list').find('.active').find('input').prop('checked', false);
        }

    }

    function changePlan() {
        var planId = $(this).attr('data');

        if (!$(this).hasClass('active')) {
            $(this).siblings().removeClass('active');
            $(this).addClass('active')
        }
        loadSubList(planId);
    }

    function planCheckChange() {
        var planId = $(this).parents('a').attr('data');
        if ($(this).prop('checked')) {
            //勾选
            $('#sub-list').find('.class-' + planId).find('input[type="checkbox"]').prop('checked', true);
        } else {
            //取消勾选
            $('#sub-list').find('.class-' + planId).find('input[type="checkbox"]').prop('checked', false);
        }

    }

    $('#plan-list').on('click', '.exam-list', changePlan)
        .on('change', '.plan-check', planCheckChange)

    $('#sub-list').on('click', '.checkbox-sub', planCheck)

    //保存按钮  保存单个 plan
    function savePlanSet() {
        if ($('#sub-list').children().length == 0) {
            return;
        }

        var subList = [];
        $('#sub-list').find('input[type="checkbox"]').each(function () {
            if ($(this).prop('checked')) {
                var subId = $(this).parents('td').attr('data');
                var planId = $(this).parents('tr').attr('data');
                var scoreType = $("input[name='radio-" +planId+ subId + "']:checked").val();
                var sub = {
                    hwPlanId: planId,
                    objKey: subId,
                    scoreType: scoreType
                }
                subList.push(sub);
            }
        })
        //删除页面上显示的,但是没有被选中的plan
        var delPlanIds = [];
        $('#plan-list').find('input[type="checkbox"]:not(:checked)').each(function () {
            delPlanIds.push($(this).parents('a').attr('data'));
        })

        var url = '${request.contextPath}/scoremanage/planSet/saveSetting';
        var params = {
            subList: subList,
            delPlanIds: delPlanIds
        }
        $.ajax({
            url: url,
            type: "post",
            data: JSON.stringify(params),
            contentType: "application/json",
            dataType: "json",
            success: function (data) {
                if (data['code'] == '00') {
                    layer.msg('保存成功');
                } else {
                    layer
                    console.log(data['msg']);
                }
            }
        })

    }

</script>
