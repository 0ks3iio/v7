<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="UTF-8">
    <title>${platformName!}</title>
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <link rel="shortcut icon"  href="${springMacroRequestContext.contextPath}/bigdata/v3/static/images/public/wanshu-icon16.png" >
    <link rel="stylesheet" type="text/css"
          href="${springMacroRequestContext.contextPath}/bigdata/v3/static/css/iconfont.css"/>
    <link rel="stylesheet" type="text/css"
          href="${springMacroRequestContext.contextPath}/bigdata/v3/static/css/all.css"/>
    <link rel="stylesheet" type="text/css"
          href="${springMacroRequestContext.contextPath}/bigdata/v3/static/css/style.css"/>
</head>
<body>
<#import  "/bigdata/v3/templates/bi/macro/biCommonWebMacro.ftl" as commonMacro />
<div class="bi-index">
    <div class="bi-header">
        <div class="bi-header-top">
            <div class="bi-header-title">
                <img src="${logoUrl!}"/>
                <span>${platformName!}</span>
            </div>
            <div class="bi-header-right">
                <!--
                <div class="bi-header-item">公告<span class="bi-header-num">3</span>条</div>
                <div class="bi-header-item">最新数据<span class="bi-header-num">7</span>份</div>
                -->
                <div class="bi-header-item" style="CURSOR: hand" onclick="helpCenter();">帮助中心</div>
                <div class="bi-header-item bi-header-person">
                    <img src="${springMacroRequestContext.contextPath}/bigdata/v3/static/images/bi/touicon.png"
                         class="bi-person-icon">
                    <span>${username!}</span>
                    <img src="${springMacroRequestContext.contextPath}/bigdata/v3/static/images/bi/unfold-icon.png"
                         class="bi-person-more">
                </div>
                <ul class="bi-header-menu">
                    <li onclick="logout();">退出</li>
                </ul>
            </div>
        </div>
        <!---那根神奇的线-->
        <div class="bi-header-down">
            <div class="bi-header-light"></div>
        </div>
    </div>
    <div class="bi-body" id="biBodyDiv"></div>
</div>
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/jquery/jquery.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/slick/slick.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/layer/layer.js" type="text/javascript" charset="utf-8"></script>
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/jquery/jquery.mousewheel.min.js" type="text/javascript" charset="utf-8"></script>
</body>
<script>
    $(function () {
        $("#biBodyDiv").load('${springMacroRequestContext.contextPath}/bigdata/v3/bi/module');
    })

    //头部menu
    $(".bi-header-person").click(function () {
        $(".bi-header-menu").show();
    })

    function logout() {
        window.location.href = '${springMacroRequestContext.contextPath}/fpf/logout/page?call=${v3Index}';
    }

    function helpCenter() {
        window.open('${springMacroRequestContext.contextPath}/bigdata/help/bi/index',"helpCenter");
    }

    $(document).click(function (event) {
        var eo = $(event.target);
        if (eo.attr('class') != 'bi-header-person' && !eo.parents('.bi-header-right').length)
            $(".bi-header-menu").hide();
    });

</script>
</html>