<div class="list-nav-wrap left-coming">
    <div class="list-nav">
        <dl>
            <dt><b>MENU</b></dt>
            <#if group! ==1>
                <dd id="businessType-2" <#if businessType! ==2>class="active"</#if> onclick="loadBusinessContent(2)">
                    <span>数据大屏</span>
                    <img src="${springMacroRequestContext.contextPath}/bigdata/v3/static/images/bi/daping2-icon.png">
                    <div id="businessType-2-num">${statData.user_cockpit_num!}</div>
                </dd>
                <dd id="businessType-1" <#if businessType! ==1>class="active"</#if> onclick="loadBusinessContent(1)">
                    <span>图表</span>
                    <img src="${springMacroRequestContext.contextPath}/bigdata/v3/static/images/bi/tubiao2-icon.png">
                    <div id="businessType-1-num">${statData.user_chart_num!}</div>
                </dd>
                <dd id="businessType-6" <#if businessType! ==6>class="active"</#if> onclick="loadBusinessContent(6)">
                    <span>看板</span>
                    <img src="${springMacroRequestContext.contextPath}/bigdata/v3/static/images/bi/kanban2-icon.png">
                    <div id="businessType-6-num">${statData.user_data_board_num!}</div>
                </dd>
                <dd id="businessType-7" <#if businessType! ==7>class="active"</#if> onclick="loadBusinessContent(7)">
                    <span>报告</span>
                    <img src="${springMacroRequestContext.contextPath}/bigdata/v3/static/images/bi/baogao2-icon.png">
                    <div id="businessType-7-num">${statData.user_data_report_num!}</div>
                </dd>
            <#elseif group! ==2>
                <dd id="businessType-91" <#if businessType! ==91>class="active"</#if> onclick="loadBusinessContent(91)">
                    <span>收藏</span>
                    <img src="${springMacroRequestContext.contextPath}/bigdata/v3/static/images/bi/choucang2-icon.png">
                    <div id="businessType-91-num">${statData.user_favorite_num!}</div>
                </dd>
                <dd id="businessType-92" <#if businessType! ==92>class="active"</#if> onclick="loadBusinessContent(92)">
                    <span>分享</span>
                    <img src="${springMacroRequestContext.contextPath}/bigdata/v3/static/images/bi/fenxiang2-icon.png">
                    <div id="businessType-92-num">${statData.user_share_num!}</div>
                </dd>
            <#elseif group! ==3>
                <dd id="businessType-99" onclick="loadBusinessContent(99)">
                    <span>经典大屏</span>
                    <img src="${springMacroRequestContext.contextPath}/bigdata/v3/static/images/bi/daping2-icon.png">
                    <div id="businessType-99-num"></div>
                </dd>
            </#if>
        </dl>
    </div>

    <!-- S 返回按钮 -->
    <div class="return-back" onclick="goBack2MainIndex()">
        <div class="clearfix">
            <img src="${springMacroRequestContext.contextPath}/bigdata/v3/static/images/bi/return-icon.png" alt=""><b>BACK</b>
        </div>
    </div><!-- E 返回按钮 -->
</div><!-- E 列表导航 -->

<!-- S 主内容 -->
<div class="main-content right-coming" id="businessContentDiv"></div>
<script type="text/javascript">
    function goBack2MainIndex() {
        $("#biBodyDiv").load('${springMacroRequestContext.contextPath}/bigdata/v3/bi/module');
    }

    function loadBusinessContent(businessType) {
        $("#businessType-" + businessType).addClass('active').siblings('dd').removeClass('active');

        if (businessType == 1) {
            $("#businessContentDiv").load('${springMacroRequestContext.contextPath}/bigdata/chartQuery/chartList');
        } else if (businessType == 2) {
            $("#businessContentDiv").load('${springMacroRequestContext.contextPath}/bigdata/cockpitQuery/index');
        } else if (businessType == 6) {
            $("#businessContentDiv").load('${springMacroRequestContext.contextPath}/bigdata/multireport/bi/list?type=6');
        } else if (businessType == 7) {
            $("#businessContentDiv").load('${springMacroRequestContext.contextPath}/bigdata/multireport/bi/list?type=7');
        } else if (businessType == 91) {
            $("#businessContentDiv").load('${springMacroRequestContext.contextPath}/bigdata/favorite/common/bi/index');
        } else if (businessType == 92) {
            $("#businessContentDiv").load('${springMacroRequestContext.contextPath}/bigdata/share/common/bi/index?type=1');
        } else if (businessType == 99) {
            $("#businessContentDiv").load('${springMacroRequestContext.contextPath}/bigdata/demo/bi/screen');
        }
    }

    $(function () {
        $("#businessType-${businessType!}").trigger('click');
        //进场动画
        $("html").css("overflow", "hidden");
        setTimeout(function () {
            $(".bi-header-title").css({"left": "0", "opacity": "1"})
            $(".bi-header-right").css({"right": "0", "opacity": "1"})
            $(".bi-header-down").css({"right": "0", "opacity": "1"})
            $(".left-coming").css({"left": "0", "opacity": "1"})
            $(".right-coming").css({"right": "0", "opacity": "1"})
        }, 100)

        setTimeout(function () {
            $("html").css("overflow", "auto");
        }, 900)

        setTimeout(function () {
            $(".bi-header-light").css({"left": "0", "opacity": "1"});
        }, 100)
        setTimeout(function () {
            $(".bi-header-light").css({"left": "340px", "opacity": "0"});
        }, 1500)
        setTimeout(function () {
            $(".bi-header-light").css({"left": "-340px"});
        }, 3000)

        //卡片高度
        function cardH() {
            var h = parseInt($('.box-img').first().width() * 9 / 16);
            $('.box-img').each(function (index, ele) {
                $(this).height(h)
            });
        }

        cardH();
        $(window).resize(cardH);
    })
</script>
