<div class="tab-pane">
    <div class="row-step">
        <div class="box-name">
            <p class="no-margin radius-top-5">考试成绩</p>
        </div>
        <div class="box-step box-step-h radius-bottom-5 clearfix">
            <div class="col-md-8">
                <div class="choose-chart">
                    <div class="filter">
                        <div class="filter-item block">
                            <span class="filter-name">考试名称&nbsp;&nbsp;</span>
                            <select name="" onchange="changeExam()" id="examList" class="form-control chosen-select chosen-width"
                                    data-placeholder="未选择">
                            </select>
                        </div>
                    </div>
                </div>
                <div class="grade-raking clearfix">
                    <div class="col-md-4 no-padding">
                        <div class="box">
                            <div class="pos-left">
                                <img src="${request.contextPath}/static/bigdata/images/all-score.png"/>
                            </div>
                            <div class="r">
                                <span>总分</span>
                                <p class="no-margin font-20"><b id="totalScore"></b></p>
                                <span class="color-blue" id=""></span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4 no-padding clearfix">
                        <div class="box m-auto">
                            <div class="pos-left">
                                <img src="${request.contextPath}/static/bigdata/images/ranking-green.png"/>
                            </div>
                            <div class="r">
                                <span>班级排名</span>
                                <p class="no-margin font-20"><b id="classRank"></b></p>
                                <span class="" id="classRankUp"></span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4 no-padding clearfix">
                        <div class="box float-right">
                            <div class="pos-left">
                                <img src="${request.contextPath}/static/bigdata/images/ranking-blue.png"/>
                            </div>
                            <div class="r">
                                <span>年级排名</span>
                                <p class="no-margin font-20"><b id="gradeRank"></b></p>
                                <span class="" id="gradeUpRank"></span>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="chart-w">
                    <div class="chart" id="grade">
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <p class="score-title">各科目考试成绩</p>
                <div class="chart-score">
                    <div class="chart" id="score">
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row-step">
        <div class="box-name">
            <p class="no-margin radius-top-5">学期汇总</p>
        </div>
        <div class="box-step box-step-c radius-bottom-5 clearfix">
            <div class="col-md-12">
                <div class="choose-chart">
                    <div class="filter">
                        <div class="filter-item block" style="float: left">
                            <span class="filter-name">学年&nbsp;&nbsp;</span>
                            <select name="" onchange="changeAcadyear()" id="acadyearList" class="form-control chosen-select chosen-width"
                                    data-placeholder="未选择">
                            </select>
                        </div>
                        <div class="filter-item block" style="float: left">
                            <span class="filter-name">学期&nbsp;&nbsp;</span>
                            <select name="" onchange="changeAcadyear()" id="semesterList" class="form-control chosen-select chosen-width"
                                    data-placeholder="未选择">
                                <option value="1" <#if currentSemester == '1'>selected="selected"</#if> >第一学期</option>
                                <option value="2" <#if currentSemester == '2'>selected="selected"</#if> >第二学期</option>
                            </select>
                        </div>
                    </div>
                </div>
                <p class="score-title no-margin">历次考试排名趋势</p>

                <div class="chart-bar" id="chart-bar">

                </div>

                <div class="chart-line">
                    <div class="chart" id="chart-line">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function () {
        $('.page-content').css('padding-bottom', 0);
        var $L = $('.js-long').find('.every-step').length;
        $L % 2 == 0 ? $('.grow-step').width($L * 240 + 180 + 30) : $('.grow-step').width($L * 240 + 180 + 90 + 30);
        $('.chart').each(function () {
            $(this).css({
                width: $(this).parent().width(),
                height: $(this).parent().height()
            });
        });
        $('.chart').width('100%');
        initExam();
        initAcadyearList();
        initSubjectScoreInfo();
        initSubjectScore();
        initSubject();
        initHistoryExamInfo();
        initHistoryExamRank();
    });
    
    function changeExam() {
        initSubjectScoreInfo();
        initSubjectScore();
        initSubject();
    }

    function changeAcadyear() {
        initHistoryExamInfo();
        initHistoryExamRank();
    }

    function initExam() {
        var data = '${examList!}';
        $('#examList').empty();
        var datas = JSON.parse(data);
        $.each(datas.infolist,function(index,value){
            var name = value.name;
            $('#examList').append("<option value='"+value.id+"'>"+name+"</option>");
        });
    }

    function initSubjectScoreInfo() {
        $.ajax({
            url: '${request.contextPath}/bigdata/personAnalysis/getScoreInfo',
            data: {userId: $('#userId').val(), unitId:$('#unitId').val(), examId:$('#examList').val()},
            type: 'get',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    // layer.msg(response.message, {icon: 2});
                } else {
                    var data = JSON.parse(response.data);
                    $('#totalScore').html(data.score);
                    $('#classRank').html(data.classRank);
                    var c = data.classUpRank + "";
                    if (c == "undefined") {
                        c = "0";
                    }
                    if (c.search('-') != 0) {
                        $('#classRankUp').html("同比提升"+ c +"名");
                        $('#classRankUp').addClass('color-blue');
                    } else {
                        $('#classRankUp').html("同比下降"+ c.replace('-', '') +"名");
                        $('#classRankUp').addClass('color-red');
                    }

                    $('#gradeRank').html(data.gradeRank);
                    var g = data.gradeUpRank + "";
                    if (g == "undefined") {
                        g = "0";
                    }
                    if (g.search('-') != 0) {
                        $('#gradeUpRank').html("同比提升"+ g +"名");
                        $('#gradeUpRank').addClass('color-blue');
                    } else {
                        $('#gradeUpRank').html("同比下降"+ g.replace('-', '') +"名");
                        $('#gradeUpRank').addClass('color-red');
                    }
                }
            }
        });
    }


    function initSubjectScore() {
        $.ajax({
            url: '${request.contextPath}/bigdata/personAnalysis/getSubjectScore',
            data: {userId: $('#userId').val(), unitId:$('#unitId').val(), examId:$('#examList').val()},
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    // layer.msg(response.message, {icon: 2});
                }
                else {
                    var echart_div = echarts.init(document.getElementById('grade'));
                    var data = JSON.parse(response.data);
                    echart_div.setOption(data);
                }
            }
        });
    }


    function initSubject() {
        $.ajax({
            url: '${request.contextPath}/bigdata/personAnalysis/getSubjectRendar',
            data: {userId: $('#userId').val(), unitId:$('#unitId').val(), examId:$('#examList').val()},
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    // layer.msg(response.message, {icon: 2});
                }
                else {
                    var echart_div = echarts.init(document.getElementById('score'));
                    var data = JSON.parse(response.data);
                    echart_div.setOption(data);
                }
            }
        });
    }

    function initHistoryExamInfo() {
        var currentAcadyear = '${currentAcadyear!}';
        var acadyear = $('#acadyearList').val();
        if (acadyear == null || acadyear == '') {
            acadyear = currentAcadyear;
        }
        $.ajax({
            url: '${request.contextPath}/bigdata/personAnalysis/getHistoryExamInfo',
            data: {userId: $('#userId').val(), acadyear:acadyear, semester:$('#semesterList').val(), unitId:$('#unitId').val()},
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    $('#chart-bar').hide();
                }
                else {
                    $('#chart-bar').show();
                    var echart_div = echarts.init(document.getElementById('chart-bar'));
                    var data = JSON.parse(response.data);
                    echart_div.setOption(data);
                }
            }
        });
    }

    function initHistoryExamRank() {
        var currentAcadyear = '${currentAcadyear!}';
        var acadyear = $('#acadyearList').val();
        if (acadyear == null || acadyear == '') {
            acadyear = currentAcadyear;
        }
        $.ajax({
            url: '${request.contextPath}/bigdata/personAnalysis/getHistoryExamRank',
            data: {userId: $('#userId').val(), acadyear:acadyear, semester:$('#semesterList').val(), unitId:$('#unitId').val()},
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    $('#chart-line').hide();
                }
                else {
                    $('#chart-line').show();
                    var echart_div = echarts.init(document.getElementById('chart-line'));
                    var data = JSON.parse(response.data);
                    echart_div.setOption(data, true);
                }
            }
        });
    }
    
    function initAcadyearList() {
        var currentAcadyear = '${currentAcadyear!}';
        $('#acadyearList').empty();
        $.ajax({
            url: '${request.contextPath}/remote/common/basedata/remoteAcadyearList',
            data: {},
            type: 'get',
            dataType: 'json',
            success: function (response) {
                $.each(response.infolist,function(index,value){
                    var name = value.name;
                    if (name == currentAcadyear + "学年") {
                        $('#acadyearList').append("<option value='"+value.id+"' selected=selected>"+name+"</option>");
                    } else {
                        $('#acadyearList').append("<option value='"+value.id+"'>"+name+"</option>");
                    }
                });
            }
        });
    }
</script>