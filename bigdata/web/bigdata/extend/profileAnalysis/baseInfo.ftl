<div class="tab-pane active man-life clearfix">
    <div class="clearfix row-step">
        <div class="col-md-8 no-padding-left radius-5">
            <div class="box-name">
                <p class="no-margin radius-top-5">成长历程</p>
            </div>
            <div class="box-step box-steps radius-bottom-5 over-x scrollbar-made">
                <#if growthEvents?exists && growthEvents?size gt 0>
                    <div class="grow-step">
                        <div class="man-step clearfix js-long">
                            <div class="every-step every-step-sb">
                                <div class="first-dot">
                                </div>
                            </div>
                                    <#assign color=["border-2ebcb2", "border-98bc5c", "border-fea81d", "border-ec534b", "border-e7548b", "border-765991"] />
                                        <#list growthEvents as item>
                                            <#if item_index%2 == 1 >
                                                <div class="every-step">
                                                    <p class="">${item.eventName!}</p>
                                                    <span><b>${item.eventDate?string('yyyy-MM-dd')}</b></span>
                                                    <div class="b-circle b-circle-t ${color[item_index%6]}">
                                                        <div class="s-circle ${color[item_index%6]}"></div>
                                                    </div>
                                                    <div class="step-line step-line-t ${color[item_index%6]}">
                                                    </div>
                                                    <div class="step-dot step-dot-t ${color[item_index%6]}">
                                                    </div>
                                                    <div class="row-line row-line-t ${color[item_index%6]}">
                                                    </div>
                                                </div>
                                            </#if>
                                        </#list>
                        </div>

                        <div class="man-step man-step-d clearfix">
                            <div class="every-step every-step-s">

                            </div>
                                    <#list growthEvents as item>
                                        <#if item_index%2 == 0 >
                                                <div class="every-step">
                                                    <p class="">${item.eventName!}</p>
                                                    <span><b>${item.eventDate?string('yyyy-MM-dd')}</b></span>
                                                    <div class="b-circle b-circle-d ${color[item_index%6]}">
                                                        <div class="s-circle ${color[item_index%6]}"></div>
                                                    </div>
                                                    <div class="step-line step-line-d ${color[item_index%6]}">
                                                    </div>
                                                    <div class="step-dot step-dot-d ${color[item_index%6]}">
                                                    </div>
                                                </div>
                                        </#if>
                                    </#list>
                        </div>
                    </div>
                <#else>
                    <div class="wrap-1of1 centered no-data-state">
                        <div class="text-center">
                            <img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png"/>
                            <p>暂无数据</p>
                        </div>
                    </div>
                </#if>
            </div>
        </div>
        <div class="col-md-4 no-padding-right radius-5">
            <div class="box-name">
                <p class="no-margin radius-top-5">应用偏好</p>
            </div>
            <div class="box-step box-steps scrollbar-made">
                        <#if appPreferences?exists && appPreferences?size gt 0>
                            <#assign bar=["#337ab7", "#5cb85c", "#f0ad4e", "#1abc9c", "#4A577D", "#ffca6a"] />
                                <#list appPreferences as item>
                                <div class="app-percent">
                                    <p class="position-relative">
                                        <span>${item.appName!}</span>
                                        <span class="pos-right"><b>${item.appUsage!}%</b></span>
                                    </p>
                                    <div class="progress no-margin">
                                        <div class="progress-bar" role="progressbar" aria-valuenow="60"
                                             aria-valuemin="0" aria-valuemax="100"
                                             style="width: ${item.appUsage!}%;background-color: ${bar[item_index%6]};">
                                        </div>
                                    </div>
                                </div>
                                </#list>
                        <#else>
                            <div class="wrap-1of1 centered no-data-state">
                                <div class="text-center">
                                    <img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png"/>
                                    <p>暂无数据</p>
                                </div>
                            </div>
                        </#if>
            </div>
        </div>
    </div>

    <div class="clearfix row-step">
        <div class="col-md-8 no-padding-left radius-5">
            <div class="box-name">
                <p class="no-margin radius-top-5">移动端使用时长</p>
            </div>
            <div class="box-step box-steps radius-bottom-5">
                <div class="chart" id="tLength">

                </div>
            </div>
        </div>
        <div class="col-md-4 no-padding-right radius-5">
            <div class="box-name">
                <p class="no-margin radius-top-5">平台使用频率</p>
            </div>
            <div class="box-step box-steps">
                <div class="chart" id="platform"></div>
            </div>
        </div>
    </div>

    <div class="clearfix row-step" style="">
                        <#list statStuEvaluations as item>
                            <div class="man-emotion text-center">
                                <div class="box-emotion">
                                    <p>${item.itemResult!}</p>
                                    <div>${item.itemName!}</div>
                                </div>
                            </div>
                        </#list>
    </div>

    <div class="clearfix row-step">
        <div class="col-md-8 no-padding-left radius-5">
            <div class="box-name">
                <p class="no-margin radius-top-5">在线社交圈</p>
            </div>
            <div class="box-step box-step-big radius-bottom-5">
                <div class="text-center">
                    <p><br/><img src="${request.contextPath}/static/bigdata/images/weik.png"/></p>
                    <p><b>微课掌上通</b></p>
                    <span class="color-ccc">最近3个月统计数据</span>
                </div>
                <div class="clearfix small-class">
                    <div class="text-center">
                        <p><br/><img src="${request.contextPath}/static/bigdata/images/fans.png"/></p>
                        <p><b>${socialCircles.fsAmount!}</b></p>
                        <span class="color-999">粉丝数</span>
                    </div>
                    <div class="text-center">
                        <p><br/><img src="${request.contextPath}/static/bigdata/images/attention.png"/></p>
                        <p><b>${socialCircles.gzAmount!}</b></p>
                        <span class="color-999">关注度</span>
                    </div>
                    <div class="text-center">
                        <p><br/><img src="${request.contextPath}/static/bigdata/images/friend.png"/></p>
                        <p><b>${socialCircles.hyAmount!}</b></p>
                        <span class="color-999">好友数</span>
                    </div>
                    <div class="text-center">
                        <p><br/><img src="${request.contextPath}/static/bigdata/images/zan.png"/></p>
                        <p><b>${socialCircles.dzAmount!}</b></p>
                        <span class="color-999">点赞数</span>
                    </div>
                    <div class="text-center">
                        <p><br/><img src="${request.contextPath}/static/bigdata/images/share.png"/></p>
                        <p><b>${socialCircles.fxAmount!}</b></p>
                        <span class="color-999">分享数</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-4 no-padding-right radius-5">
            <div class="box-name">
                <p class="no-margin radius-top-5">个人标签</p>
            </div>
            <div class="box-step box-step-big">
                <div class="chart" id="sLabel"></div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function () {
        $('.page-content').css('padding-bottom', 0);
        var $L = $('.every-step').length;
        if ($L < 8) {
            $('.growth-box').css('overflow-x', 'hidden');
        }
        $('.grow-step').css({
            width: 120*$L -20
        });

        $('.chart').each(function () {
            $(this).css({
                width: $(this).parent().width(),
                height: $(this).parent().height()
            });
        });
        $('.chart').width('100%');
        initAppUsage();
        initPlatformUsage();
        initPersonTag();
    });

    function initAppUsage() {
        $.ajax({
            url: '${request.contextPath}/bigdata/personAnalysis/getAppUsageOption',
            data: {userId: $('#userId').val()},
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    // layer.msg(response.message, {icon: 2});
                    $('#tLength').empty().html("<div class=\"wrap-1of1 centered no-data-state\">\n" +
                            "                        <div class=\"text-center\">\n" +
                            "                            <img src=\"${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png\"/>\n" +
                            "                            <p>暂无数据</p>\n" +
                            "                        </div>\n" +
                            "                    </div>");
                }
                else {
                    var echart_div = echarts.init(document.getElementById('tLength'));
                    var data = JSON.parse(response.data);
                    echart_div.setOption(data);
                }
            }
        });
    }

    function initPlatformUsage() {
        $.ajax({
            url: '${request.contextPath}/bigdata/personAnalysis/getPlatformUsageOption',
            data: {userId: $('#userId').val()},
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    $('#platform').empty().html("<div class=\"wrap-1of1 centered no-data-state\">\n" +
                            "                        <div class=\"text-center\">\n" +
                            "                            <img src=\"${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png\"/>\n" +
                            "                            <p>暂无数据</p>\n" +
                            "                        </div>\n" +
                            "                    </div>");
                }
                else {
                    var echart_div = echarts.init(document.getElementById('platform'));
                    var data = JSON.parse(response.data);
                    echart_div.setOption(data);
                }
            }
        });
    }

    function initPersonTag() {
        $.ajax({
            url: '${request.contextPath}/bigdata/personAnalysis/getPersonTag',
            data: {userId: $('#userId').val()},
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (!response.success) {
                    $('#sLabel').empty().html("<div class=\"wrap-1of1 centered no-data-state\">\n" +
                            "                        <div class=\"text-center\">\n" +
                            "                            <img src=\"${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png\"/>\n" +
                            "                            <p>暂无数据</p>\n" +
                            "                        </div>\n" +
                            "                    </div>");
                }
                else {
                    var echart_div = echarts.init(document.getElementById('sLabel'));
                    var data = JSON.parse(response.data);
                    echart_div.setOption(data);
                }
            }
        });
    }

</script>