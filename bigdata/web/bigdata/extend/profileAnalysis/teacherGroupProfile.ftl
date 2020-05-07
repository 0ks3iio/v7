<div class="row">
    <div class="col-xs-12">
        <div class="box">
            <div class="clearfix margin-b-20">
                <div class="col-md-4 no-padding-left">
                    <div class="box-default no-padding-left margin-b-20 radius-5 js-standard">
                        <div class="box-name">
                            <p class="no-margin radius-top-5">性别人数比例</p>
                        </div>
                        <div class="box-step">
                            <div class="text-center">
                                <h3 class="color-blue font-20 bold" id="totalCount">&nbsp;</h3>
                                <span>教师总数</span>
                            </div>
                            <div class="app-percent">
                                <p class="position-relative">
                                    <span>男</span>
                                    <span class="pos-right"><b id="manCount"></b></span>
                                </p>
                                <div class="progress no-margin">
                                    <div id="manBar" class="progress-bar progress-bar-primary" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 40%;">
                                    </div>
                                </div>
                            </div>
                            <div class="app-percent">
                                <p class="position-relative">
                                    <span>女</span>
                                    <span class="pos-right"><b id="womanCount"></b></span>
                                </p>
                                <div class="progress no-margin">
                                    <div id="womanBar" class="progress-bar progress-bar-warning" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 32%;">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="box-default no-padding-left radius-5 js-height-standard">
                        <div class="box-name">
                            <p class="no-margin radius-top-5">职称</p>
                        </div>
                        <div class="box-step radius-bottom-5">
                            <div class="chart" id="schoolType"></div>
                        </div>
                    </div>
                </div>


                <div class="col-md-8 no-padding-right radius-5">
                    <div class="box-default no-padding-right radius-5 js-height-two">
                        <div class="box-name">
                            <p class="no-margin radius-top-5">区域展示</p>
                        </div>
                        <div class="box-step radius-bottom-5">
                            <div class="chart" id="areaShow"></div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="clearfix margin-b-20">
                <div class="col-md-4 no-padding-left">
                    <div class="box-default no-padding-left radius-5 js-height-standard">
                        <div class="box-name">
                            <p class="no-margin radius-top-5">学历</p>
                        </div>
                        <div class="box-step radius-bottom-5">
                            <div class="chart" id="hobbies"></div>
                        </div>
                    </div>
                </div>


                <div class="col-md-8 no-padding-right radius-5">
                    <div class="box-default no-padding-right radius-5 js-height-standard">
                        <div class="box-name">
                            <p class="no-margin radius-top-5">年龄段</p>
                        </div>
                        <div class="box-step radius-bottom-5">
                            <div class="chart" id="profession"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<input type="hidden" id="tagArray" value='${tagArray!}'>
<script type="text/javascript">
    $(function(){
        $('.page-content').css('padding-bottom',0);
        var $height=$('.js-standard').height();
        $('.js-height-standard').each(function(){
            $(this).css({
                height: $height
            })
        });
        $('.js-height-two').each(function(){
            $(this).css({
                height: $height*2 + 20
            })
        });
        $('.chart').each(function(){
            $(this).css({
                width: '100%',
                height: $(this).parent().parent().height() - 60,
            });
        });

        var areaShow=echarts.init($('#areaShow')[0]);
        var profession=echarts.init($('#profession')[0]);
        var schoolType=echarts.init($('#schoolType')[0]);
        // 性别比例
        initSexProportion();
        // 职称
        initTitle();
        // 学历
        initEducation();
        // 年龄段
        initAge();
        // 地图
        initMap();
        $(window).resize(function(){
            schoolType.resize();areaShow.resize();profession.resize()
        });
    });

    function initSexProportion() {
        $.ajax({
            url: '${request.contextPath}/bigdata/groupAnalysis/getSexProportion',
            data: {profileCode : $('#profileCode').val(), tagArray : $('#tagArray').val()},
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    showLayerTips4Confirm('error', response.message, 't', null);
                }
                else {
                    $('#totalCount').html(response.data.totalCount);
                    $('#manCount').html(response.data.manCount);
                    $('#womanCount').html(response.data.womanCount);
                    $('#manBar').css('width', response.data.manProportion + "%");
                    $('#womanBar').css('width', response.data.womanProportion + "%");
                }
            }
        });

    }

    function initTitle() {
        $.ajax({
            url: '${request.contextPath}/bigdata/groupAnalysis/teacher/getTitleOption',
            data: {profileCode : $('#profileCode').val(), tagArray : $('#tagArray').val()},
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    $('#schoolType').empty().html("<div class=\"wrap-1of1 centered no-data-state\">\n" +
                            "                        <div class=\"text-center\">\n" +
                            "                            <img src=\"${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png\"/>\n" +
                            "                            <p>暂无数据</p>\n" +
                            "                        </div>\n" +
                            "                    </div>");
                }
                else {
                    var echart_div = echarts.init(document.getElementById('schoolType'));
                    var option = {
                        color: ['#46c6a3','#f58a54','#af8af4','#317deb'],
                        tooltip : {
                            trigger: 'item',
                            formatter: "{a} <br/>{b} : {c} ({d}%)"
                        },
                        series : [
                            {
                                name: '职称',
                                type: 'pie',
                                radius : '75%',
                                center: ['50%', '50%'],
                                itemStyle: {
                                    emphasis: {
                                        shadowBlur: 10,
                                        shadowOffsetX: 0,
                                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                                    }
                                }
                            }
                        ]
                    };
                    var data = [];
                    $.each(response.data, function (i, v) {
                         var d = {value : v.value, name : v.key};
                        data.push(d);
                    });
                    option.series[0].data = data;
                    echart_div.setOption(option);
                }
            }
        });
    }

    function initEducation() {
        $.ajax({
            url: '${request.contextPath}/bigdata/groupAnalysis/teacher/getEducation',
            data: {profileCode : $('#profileCode').val(), tagArray : $('#tagArray').val()},
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    $('#hobbies').empty().html("<div class=\"wrap-1of1 centered no-data-state\">\n" +
                            "                        <div class=\"text-center\">\n" +
                            "                            <img src=\"${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png\"/>\n" +
                            "                            <p>暂无数据</p>\n" +
                            "                        </div>\n" +
                            "                    </div>");
                }
                else {
                    var echart_div = echarts.init(document.getElementById('hobbies'));
                    var option = {
                        color: ['#37a2da','#34c4e9','#96bfff','#317deb','#fedb5b','#96beb8','#ff9f7f','#46c6a3'],
                        tooltip : {
                            trigger: 'item',
                            formatter: "{a} <br/>{b} : {c} ({d}%)"
                        },
                        calculable : true,
                        series : [
                            {
                                name:'学历',
                                type:'pie',
                                radius : [30, 60],
                                center : ['50%', '50%'],
                                roseType : 'area',
                            }
                        ]
                    };
                    var data = [];
                    $.each(response.data, function (i, v) {
                        var d = {value : v.value, name : v.key};
                        data.push(d);
                    });
                    option.series[0].data = data;
                    echart_div.setOption(option);
                    echart_div.resize();
                }
            }
        });
    }

    function initAge() {
        $.ajax({
            url: '${request.contextPath}/bigdata/groupAnalysis/teacher/getAgeOption',
            data: {profileCode : $('#profileCode').val(), tagArray : $('#tagArray').val()},
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    $('#profession').empty().html("<div class=\"wrap-1of1 centered no-data-state\">\n" +
                            "                        <div class=\"text-center\">\n" +
                            "                            <img src=\"${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png\"/>\n" +
                            "                            <p>暂无数据</p>\n" +
                            "                        </div>\n" +
                            "                    </div>");
                }
                else {
                    var echart_div = echarts.init(document.getElementById('profession'));
                    var data = JSON.parse(response.data);
                    echart_div.setOption(data);
                    echart_div.resize();
                }
            }
        });
    }

    function initMap() {
        if (echarts.getMap("00") == null) {
            $.get(_contextPath + '/static/bigdata/js/echarts/map/json/province/00.json', function (geoJson) {
                echarts.registerMap("china", geoJson);
                $.ajax({
                    url: '${request.contextPath}/bigdata/groupAnalysis/getMapOption',
                    data: {profileCode : $('#profileCode').val(), tagArray : $('#tagArray').val()},
                    type: 'POST',
                    dataType: 'json',
                    success: function (response) {
                        if (!response.success) {
                            $('#areaShow').empty().html("<div class=\"wrap-1of1 centered no-data-state\">\n" +
                                    "                        <div class=\"text-center\">\n" +
                                    "                            <img src=\"${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png\"/>\n" +
                                    "                            <p>暂无数据</p>\n" +
                                    "                        </div>\n" +
                                    "                    </div>");
                        }
                        else {
                            var echart_div = echarts.init(document.getElementById('areaShow'));
                            var data = JSON.parse(response.data);
                            echart_div.setOption(data);
                            echart_div.resize();
                        }
                    }
                });
                if (typeof call === 'function') {
                    call();
                }
            });
        }
    }
</script>