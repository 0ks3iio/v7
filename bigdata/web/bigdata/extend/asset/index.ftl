<div class="box box-default no-margin clearfix height-full">
            <div class="col-md-9 height-1of1 position-relative step-content">
                <#if assetMap['zl']?exists>
                <div class="step-all step-one pos-middle api_tag">
                    <ul class="right-list">
                        <li class="color-46c6a3">
                            <span class="line bg-46c6a3"></span>
                            <span class="pic-bg bg-46c6a3"><img src="${request.contextPath}/static/bigdata/images/icon-data.png" alt="" /></span>
                            <span>累积数据达：</span>
                            <span class="active-font">100PB+</span>
                            <span class="big-ball bg-46c6a3 font-new">100PB+</span>
                        </li>
                        <li class="color-2b9bfd">
                            <span class="line bg-2b9bfd"></span>
                            <span class="pic-bg bg-2b9bfd"><img src="${request.contextPath}/static/bigdata/images/icon-data.png" alt="" /></span>
                            <span>日增数据：</span>
                            <span class="active-font">300G</span>
                            <span class="big-ball big-ball-two bg-2b9bfd font-new">300G</span>
                        </li>
                        <li class="color-c7d52a">
                            <span class="line bg-c7d52a"></span>
                            <span class="pic-bg bg-c7d52a"><img src="${request.contextPath}/static/bigdata/images/icon-data.png" alt="" /></span>
                            <span>日增日志：</span>
                            <span class="active-font">3亿条</span>
                            <span class="big-ball big-ball-three bg-c7d52a"><span class="font-36 font-new">3</span>亿</span>
                        </li>
                        <li class="color-f8bd49">
                            <span class="line bg-f8bd49"></span>
                            <span class="pic-bg bg-f8bd49"><img src="${request.contextPath}/static/bigdata/images/icon-data.png" alt="" /></span>
                            <span>日统计数据：</span>
                            <span class="active-font">10T</span>
                            <span class="big-ball big-ball-four bg-f8bd49 font-new">10TB</span>
                        </li>
                    </ul>
                </div>
                </#if>

                <#if assetMap['jcsj']?exists>
                <div class="step-all pos-middle none clearfix api_tag">
                    <div class="col-md-3 text-center">
                        <img src="${request.contextPath}/static/bigdata/images/icon-schools.png"/>
                        <h3 class="font-new color-46c6a3 font-36">3000</h3>
                        <span class="font-18 color-grey"><b>学校数量</b></span>
                    </div>
                    <div class="col-md-3 text-center">
                        <img src="${request.contextPath}/static/bigdata/images/icon-students.png"/>
                        <h3 class="color-2b9bfd"><span class="font-new font-36">500</span>万</h3>
                        <span class="font-18 color-grey"><b>学生数量</b></span>
                    </div>
                    <div class="col-md-3 text-center">
                        <img src="${request.contextPath}/static/bigdata/images/icon-teachers.png"/>
                        <h3 class="color-c7d52a"><span class="font-new font-36">30</span>万</h3>
                        <span class="font-18 color-grey"><b>教师数量</b></span>
                    </div>
                    <div class="col-md-3 text-center">
                        <img src="${request.contextPath}/static/bigdata/images/icon-parents.png"/>
                        <h3 class="color-f8bd49"><span class="font-new font-36">1000</span>万</h3>
                        <span class="font-18 color-grey"><b>家长数量</b></span>
                    </div>
                </div>
                </#if>

                <#if assetMap['zc']?exists>
                <div class="pos-middle-center asset none api_tag"  id="zc" apiUrl='${assetMap['zc'].apiUrl!}'>
                    <ul class="clearfix">
                        <li class="bg-2b9bfd">房屋与建筑物
                            <div class="vertical-line"></div>
                            <div class="circle-110">
                                <span class="font-36 font-new">3</span>平方米
                            </div>
                        </li>
                        <li class="bg-46c6a3">专用设备
                            <div class="vertical-line vertical-line-longer"></div>
                            <div class="circle-110 circle-110-higher">
                                <span class="font-36 font-new">3</span>平方米
                            </div>
                        </li>
                        <li class="bg-f8bd49">一般设备
                            <div class="vertical-line"></div>
                            <div class="circle-110">
                                <span class="font-36 font-new">3</span>平方米
                            </div>
                        </li>
                        <li class="bg-bea1f5">文物与陈列品
                            <div class="vertical-line vertical-line-longer"></div>
                            <div class="circle-110 circle-110-higher">
                                <span class="font-36 font-new">3</span>平方米
                            </div>
                        </li>
                        <li class="bg-f78bad">图书
                            <div class="vertical-line"></div>
                            <div class="circle-110">
                                <span class="font-36 font-new">3</span>平方米
                            </div>
                        </li>
                        <li class="bg-f8a174">其他固定资产
                            <div class="vertical-line vertical-line-longer"></div>
                            <div class="circle-110 circle-110-higher">
                                <span class="font-36 font-new">3</span>平方米
                            </div>
                        </li>
                    </ul>
                </div>
                </#if>

                <#if assetMap['kj']?exists>
                <div class="pos-middle-center space-wrap none api_tag" id="kj" apiUrl='${assetMap['kj'].apiUrl!}'>
                    <div class="interspace text-center color-fff position-relative">
                        <div class="circles circle-140 bg-2b9bfd font-30">
                            <span><b>空间</b></span>
                        </div>
                        <div class="circles circle-120 bg-46c6a3">
                            <span class="font-new font-30" id="classSpace"></span><br />
                            <span>个班级空间</span>
                        </div>
                        <div class="circles circle-100 bg-f8a174">
                            <span class="font-new font-30" id="share"></span><br />
                            <span>条分享</span>
                        </div>
                        <div class="circles circle-120 bg-bea1f5">
                            <span class="font-new font-30" id="teacherStudio"></span><br />
                            <span>个名师工作室</span>
                        </div>
                        <div class="circles circle-100 bg-f78bad">
                            <span class="font-new font-30" id="praise"></span><br />
                            <span>个赞</span>
                        </div>
                        <div class="circles circle-120 bg-f8bd49">
                            <span class="font-new font-30" id="activity"></span><br />
                            <span>次调研活动</span>
                        </div>
                        <div class="line-bias bg-46c6a3"></div>
                        <div class="line-bias bg-f8a174"></div>
                        <div class="line-bias bg-bea1f5"></div>
                        <div class="line-bias bg-f78bad"></div>
                        <div class="line-bias bg-f8a174"></div>
                    </div>
                </div>
                </#if>

                <#if assetMap['zyk']?exists>
                <div class="pos-middle-center resource-group none api_tag" id="zyk" apiUrl='${assetMap['zyk'].apiUrl!}'>
                    <ul class=" clearfix">
                        <li class="clearfix float-left">
                            <div class="bg-bluedark float-left">
                                <img src="${request.contextPath}/static/bigdata/images/icon-resource.png"/>
                            </div>
                            <div class="bg-2b9bfd float-left">
                                <p class="font-new font-30" id="sizeSum"></p>
                                <span >条资源</span>
                            </div>
                        </li>
                        <li class="clearfix float-left">
                            <div class="bg-greendark float-left">
                                <img src="${request.contextPath}/static/bigdata/images/icon-load.png"/>
                            </div>
                            <div class="bg-46c6a3 float-left">
                                <p class="font-new font-30" id="downloadTimesSum"></p>
                                <span>次下载</span>
                            </div>
                        </li>
                        <li class="clearfix float-left">
                            <div class="bg-greenyellow float-left">
                                <img src="${request.contextPath}/static/bigdata/images/icon-eyes.png"/>
                            </div>
                            <div class="bg-f8bd49 float-left">
                                <p class="font-new font-30" id="readTimesSum"></p>
                                <span>次浏览</span>
                            </div>
                        </li>
                    </ul>
                </div>
                </#if>

                <#if assetMap['msyk']?exists>
                <div class="pos-middle-center prety-teacher none api_tag" id="msyk" apiUrl='${assetMap['msyk'].apiUrl!}'>
                    <div class="text-center pic-logo"><img src="${request.contextPath}/static/bigdata/images/meishi.png"/></div>
                    <div class="prety clearfix">
                        <div class="single-box bg-46c6a3">
                            <span>备课次数</span>
                            <h3 class="font-new font-30 text-right" id="courseWareSum"></h3>
                        </div>
                        <div class="single-box bg-bea1f5">
                            <span>作业次数</span>
                            <h3 class="font-new font-30 text-right" id="homeworkSum"></h3>
                        </div>
                        <div class="single-box bg-f78bad">
                            <span>题目数</span>
                            <h3 class="font-new font-30 text-right" id="questionSum"></h3>
                        </div>
                        <div class="single-box bg-f8a174">
                            <span>视频数</span>
                            <h3 class="font-new font-30 text-right" id="videoSum"></h3>
                        </div>
                        <div class="single-box bg-2b9bfd">
                            <span>图片数</span>
                            <h3 class="font-new font-30 text-right" id="imageSum"></h3>
                        </div>
                    </div>
                </div>
                </#if>

                <#if assetMap['khw']?exists>
                <div class="pos-middle-center prety-teacher none api_tag" id="khw" apiUrl='${assetMap['khw'].apiUrl!}'>
                    <div class="text-center pic-logo"><img src="${request.contextPath}/static/bigdata/images/kehuo.png"/></div>
                    <div class="prety clearfix">
                        <div class="single-box bg-46c6a3">
                            <span>直播课</span>
                            <h3 class="font-new font-30 text-right" id="course_num"></h3>
                        </div>
                        <div class="single-box bg-bea1f5">
                            <span>上课人次</span>
                            <h3 class="font-new font-30 text-right" id="online_num"></h3>
                        </div>
                        <div class="single-box bg-f78bad">
                            <span >作业人次</span>
                            <h3 class="font-new font-30 text-right" id="exer_num"></h3>
                        </div>
                    </div>
                </div>
                </#if>

                <#if assetMap['wkzst']?exists>
                <div class="pos-middle-center prety-teacher none api_tag" id="wkzst" apiUrl='${assetMap['wkzst'].apiUrl!}'>
                    <div class="text-center pic-logo"><img src="${request.contextPath}/static/bigdata/images/weike.png"/></div>
                    <div class="prety clearfix" id="wkzstDiv">
                        <div class="single-box bg-46c6a3">
                            <span>班级圈信息</span>
                            <h3 class="font-new font-30 text-right">512</h3>
                        </div>
                        <div class="single-box bg-bea1f5">
                            <span>班级圈点赞</span>
                            <h3 class="font-new font-30 text-right">52132</h3>
                        </div>
                        <div class="single-box bg-f78bad">
                            <span>班级通知</span>
                            <h3 class="font-new font-30 text-right">52132</h3>
                        </div>
                        <div class="single-box bg-f8a174">
                            <span>作业人次</span>
                            <h3 class="font-new font-30 text-right">22512</h3>
                        </div>
                    </div>
                </div>
                </#if>
            </div>

            <div class="col-md-3 height-1of1">
                <ul class="left-list">
                    <#if assetMap['zl']?exists>
                        <li class="active">
                            <span><b>总览</b></span>
                            <span class="circle"></span>
                            <div class="line-hide-top"></div>
                        </li>
                    </#if>
                    <#if assetMap['jcsj']?exists>
                        <li>
                            <span><b>基础数据</b></span>
                            <span class="circle"></span>
                        </li>
                    </#if>
                    <#if assetMap['zc']?exists>
                        <li>
                            <span><b>资产</b></span>
                            <span class="circle"></span>
                        </li>
                    </#if>
                    <#if assetMap['kj']?exists>
                        <li>
                            <span><b>空间</b></span>
                            <span class="circle"></span>
                        </li>
                    </#if>
                    <#if assetMap['zyk']?exists>
                        <li>
                            <span><b>资源</b></span>
                            <span class="circle"></span>
                        </li>
                    </#if>
                    <#if assetMap['msyk']?exists>
                        <li>
                            <span><b>美师优课</b></span>
                            <span class="circle"></span>
                        </li>
                    </#if>
                    <#if assetMap['khw']?exists>
                        <li>
                            <span><b>课后网</b></span>
                            <span class="circle"></span>
                        </li>
                    </#if>
                    <#if assetMap['wkzst']?exists>
                        <li>
                            <span><b>微课掌上通</b></span>
                            <span class="circle"></span>
                            <div class="line-hide-last"></div>
                        </li>
                    </#if>
                </ul>
            </div>

        </div>
<script typea="text/javascript">
    $(function(){
        $('.page-content').css('padding-bottom',0);
        function height(){
            $('.height-full').each(function(){
                $(this).css({
                    height: $(window).height() - 46 - 52 -20
                })
            })
        };height();

        var index=0,timer;
        timer=setInterval(slide,5000);
        function slide(){
            $('.left-list li').eq(index).addClass('active').siblings().removeClass('active');
            $('.step-content>div').eq(index).removeClass('none').siblings().addClass('none');
            index++;
            if(index==8){
                index=0
            }
        };
        $('.left-list li span:first-child').mouseenter(function(){
            clearInterval(timer);
            $(this).parent().addClass('active').siblings().removeClass('active');
            $('.step-content>div').eq($(this).parent().index()).removeClass('none').siblings().addClass('none');
        }).mouseleave(function(){
            index=$(this).parent().index();
            timer=setInterval(slide,5000);
        });

        $('.api_tag').each(function (val, index) {
            var apiUrl = $(this).attr('apiUrl');
            queryResult(apiUrl, $(this).attr('id'));
        });

    });
    var color = ['bg-46c6a3','bg-bea1f5','bg-f78bad','bg-f8a174','bg-2b9bfd','bg-greendark','bg-bluedark','bg-f8a174','bg-c7d52a'];
    function queryResult(apiUrl, assetCode) {
        if (apiUrl == null || apiUrl == "") {
            return;
        }
        $.ajax({
            url: '${request.contextPath}/bigdata/asset/queryData',
            data: {apiUrl : apiUrl,assertCode:assetCode},
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    // layer.msg(response.message, {icon: 2});
                }
                else {
                    if (assetCode == "kj"){
                        var kjObj = JSON.parse(response.data);
                        $('#teacherStudio').html(kjObj.teacherStudio);
                        $('#activity').html(kjObj.activity);
                        $('#classSpace').html(kjObj.classSpace);
                        $('#share').html(kjObj.share);
                        $('#praise').html(kjObj.praise);
                    } else if (assetCode == "msyk") {
                        var msykObj = JSON.parse(response.data);
                        $('#homeworkSum').html(msykObj.infos.homeworkSum);
                        $('#videoSum').html(msykObj.infos.videoSum);
                        $('#courseWareSum').html(msykObj.infos.courseWareSum);
                        $('#imageSum').html(msykObj.infos.imageSum);
                        $('#questionSum').html(msykObj.infos.questionSum);
                    } else if (assetCode == "zyk") {
                        var zykObj = JSON.parse(response.data)[0];
                        $('#sizeSum').html(zykObj.sizeSum);
                        $('#downloadTimesSum').html(zykObj.downloadTimesSum);
                        $('#readTimesSum').html(zykObj.readTimesSum);
                    } else if (assetCode == "khw") {
                        var khwObj = JSON.parse(response.data);
                        $('#course_num').html(khwObj.course_num);
                        $('#exer_num').html(khwObj.exer_num);
                        $('#online_num').html(khwObj.online_num);
                    } else if (assetCode == "wkzst") {
                        $('#wkzstDiv').empty();
                        var wkzstObj = JSON.parse(response.data);
                        $.each(wkzstObj.list, function (i, v){
                            var obj = "<div class=\"single-box " + color[i%9] + "\">\n" +
                                    "                            <span>"+ v.name +"</span>\n" +
                                    "                            <h3 class=\"font-new font-30 text-right\">"+v.count+"</h3>\n" +
                                    "                        </div>";
                            $('#wkzstDiv').append(obj);
                        });
                    }
                }
            }
        });
    }
</script>