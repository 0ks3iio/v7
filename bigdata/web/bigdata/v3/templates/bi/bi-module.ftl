<div class="bi-body-slice">
    <!---轮播图--->
    <div class="slick">
            <#if rootList?? && rootList?size gt 0>
                <#list rootList as rootModule>
                <div class="slick-item">
                    <div class="bi-slick-item">
                        <div class="bi-slick-title">
                            <span>${rootModule.name!}</span>/${rootModule.description!}
                        </div>
                        <div class="bi-slick-body">
                            <#if rootModule.type == 'dir'>
                                <#if rootModule.children?? && rootModule.children?size gt 0>
                                    <#list rootModule.children as child>
                                        <div class="bi-slick-li" onclick="loadConetnet('${child.id!}','${child.url!}',${child.openType!})">
                                            <div class="bi-slick-li-box">
                                                <div class="bi-slick-li-body">
                                                    <div class="bi-slickli-tite">${child.name!}</div>
                                                    <img src="${springMacroRequestContext.contextPath}/bigdata/v3/static/images/bi/${child.icon!}">
                                                </div>
                                            </div>
                                        </div>
                                    </#list>
                                </#if>
                            </#if>
                        </div>
                    </div>
                </div>
                </#list>
            </#if>
    </div>
    <!---轮播图--->
    <!---滚动鼠标--->
    <div class="bi-mouse">
        <img src="${springMacroRequestContext.contextPath}/bigdata/v3/static/images/bi/mouse-icon.png">
        <span>滚动鼠标</span>
        <img src="${springMacroRequestContext.contextPath}/bigdata/v3/static/images/bi/guide-icon.png"
             class="bi-gun-img">
    </div>
    <!---滚动鼠标--->
</div>
<!--切换-->
<div class="bi-down-function" onclick="loadNoticeList()">
    <!--
    <div class="bi-fun-item">
        <img src="${springMacroRequestContext.contextPath}/bigdata/v3/static/images/bi/gonggao-icon.png">
        <span>公告</span>
    </div>-->
    <div class="bi-fun-item" onclick="loadFavoriteList()">
        <img src="${springMacroRequestContext.contextPath}/bigdata/v3/static/images/bi/shoucang-icon.png">
        <span>收藏</span>
    </div>
    <div class="bi-fun-item" onclick="loadShareList()">
        <img src="${springMacroRequestContext.contextPath}/bigdata/v3/static/images/bi/fenxiang-icon.png">
        <span>分享</span>
    </div>
</div>
<script>
    $(function () {
        //进场动画
        $("html").css("overflow","hidden");
        $(".bi-slick-body").eq(0).find(".bi-slick-li").eq(0).css("left","100px");
        $(".bi-slick-body").eq(0).find(".bi-slick-li").eq(1).css("left","200px");
        $(".bi-slick-body").eq(0).find(".bi-slick-li").eq(2).css("left","200px");

        setTimeout(function () {
            $(".bi-header-title").css({ "left": "0", "opacity": "1" })
            $(".bi-header-right").css({ "right": "0", "opacity": "1" })
            $(".bi-header-down").css({ "right": "0", "opacity": "1" })
            $(".bi-body-slice").css({ "right": "0", "opacity": "1" })
            $(".bi-down-function").css({ "bottom": "21px", "opacity": "1" })
        }, 100)

        setTimeout(function () {
            $(".bi-slick-body").eq(1).find(".bi-slick-li").eq(0).css("left","0");
        }, 500)
        setTimeout(function () {
            $(".bi-slick-body").eq(1).find(".bi-slick-li").eq(1).css("left","0");
        }, 600)
        setTimeout(function () {
            $(".bi-slick-body").eq(1).find(".bi-slick-li").eq(2).css("left","0");
        }, 700)

        setTimeout(function () {
            $("html").css("overflow","auto");
        }, 900)

        setTimeout(function () {
            $(".bi-header-light").css({ "left": "0", "opacity": "1" });
        }, 100)
        setTimeout(function () {
            $(".bi-header-light").css({ "left": "340px", "opacity": "0" });
        }, 1500)
        setTimeout(function () {
            $(".bi-header-light").css({ "left": "-340px" });
        }, 3000)

        //头部跑马
        setInterval(function () {
            $(".bi-header-light").css({ "left": "0", "opacity": "1" });
            setTimeout(function () {
                $(".bi-header-light").css({ "left": "340px", "opacity": "0" });
            }, 1500)
            setTimeout(function () {
                $(".bi-header-light").css({ "left": "-340px" });
            }, 3000)
        }, 4500);

        //鼠标跑马
        setInterval(function () {
            $(".bi-gun-img").css({"right": "-20px"});
            setTimeout(function () {
                $(".bi-gun-img").css({"right": "0"});
            }, 200)
        }, 400);
        //幻灯片、跑马灯
        $('.slick').slick({
            /*
             * 特别说明：
             * 1、plugs/slick内部的.css和.scss为原版；
             * 2、assets/scss/_slick.scss为定制版；
             * 3、增加fullScreen参数，默认false，配置为ture全屏模式，屏幕大于1200px，上下切换按钮位置不同；
             * 4、其他参数参照http://www.jq22.com/jquery-info406
             */
            fullScreen: true,//全屏模式
            infinite: true,	//循环播放
            dots: true,		//下面切换圆点
            arrows: false,	//左右切换按钮
            speed: 400,		//滚动速度
            slidesToShow: 1	//显示个数
        });

        //F鼠标滚轮事件
        window.addEventListener("mousewheel",
                function (event, delta) {
                    var dir = event.wheelDelta > 0 ? 'Up' : 'Down';
                    if (dir == 'Up') {
                        $('.slick').slickPrev();
                    } else {
                        $('.slick').slickNext();
                    }
                    return false;

                }, { passive: false });
    })

    function loadFavoriteList() {
        $("#biBodyDiv").load('${springMacroRequestContext.contextPath}/bigdata/v3/bi/list?group=2&businessType=91');
    }

    function loadShareList() {
        $("#biBodyDiv").load('${springMacroRequestContext.contextPath}/bigdata/v3/bi/list?group=2&businessType=92');
    }

    function loadNoticeList() {
        //window.open('${springMacroRequestContext.contextPath}/bigdata/share/common/bi/index',"bi-notice");
    }

    function loadConetnet(id, url, openType) {
        if (openType == 1)
            $("#biBodyDiv").load('${springMacroRequestContext.contextPath}'+url);
        else
            window.open(url, id);
    }
</script>