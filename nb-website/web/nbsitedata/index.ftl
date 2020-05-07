<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>首页</title>

    <link type="text/css" rel="stylesheet" href="${request.contextPath}/nbsitedata/css/slick/slick.css">
    <link type="text/css" rel="stylesheet" href="${request.contextPath}/nbsitedata/css/slick/slick-theme.css">
    <link type="text/css" rel="stylesheet" href="${request.contextPath}/nbsitedata/css/public.css">
    <link type="text/css" rel="stylesheet" href="${request.contextPath}/nbsitedata/css/mystyle.css">
    <script type="text/javascript" src="${request.contextPath}/nbsitedata/js/jquery.js"></script>
    <script type="text/javascript" src="${request.contextPath}/nbsitedata/js/myscript.js"></script>
    <script type="text/javascript" src="${request.contextPath}/nbsitedata/js/slick.min.js"></script>

</head>
<body>
<!--[if  IE 6]>
<div id="ie6-warning">
    您正在使用IE6或以IE6为内核的浏览器，可能导致本网站无法正常显示。建议您升级到 <a href="http://www.microsoft.com/china/windows/internet-explorer/"
                                                 target="_blank">Internet Explorer 8</a> 或使用： <a
        href="http://www.mozillaonline.com/">
    Firefox</a> / <a href="http://www.google.com/chrome/?hl=zh-CN">Chrome</a><a id="ie6-close"
                                                                                href="javascript:void(0)">关闭</a>
</div>
<script type="text/javascript">
    $body = $('body');
    $body.addClass('ie6');
    $('#ie6-close').click(function () {
        $body.removeClass('ie6');
        $('#ie6-warning').hide();
    })
</script>
<![endif]-->
<!--不同类型的页面用不同的类区分 wrapper-index（首页） wrapper-news（新闻页）  wrapper-more（更多页）  wrapper-channel（二级页）-->
<!--wrapper-->
<div class="wrapper-index wrapper">
    <div class="header">
        <div class="inner">
            <div class="topBar">你好，欢迎来到宁波市教育局教研室</div>
            <div class="logo"><a href="${request.contextPath}/sitedata/index.html"></a></div>
            <ul class="nav">
                <li class="nav-item"><a href="${request.contextPath}/sitedata/index.html">首页</a></li>
            <#if models?exists && models?size gt 0>
                <#list models as model>
                    <li class="nav-item"><a href="javascript:;" id="model${model.thisId!}"
                                            onclick="go2Model('${model.thisId!}','container');">${model.mcodeContent!}</a>
                    </li>
                </#list>
            </#if>
            <#--
            <li class="nav-item"><a href="">县市区分站</a></li>
            -->
            </ul>
        </div>
    </div>

    <div class="content inner container fn-clearfix">

    </div>

    <div class="footer">
        <!-- 友情链接 -->
        <div class="friendLink">

        </div>
        <!-- 平台信息 -->
        <div class="bah">宁波市教育局教研室 浙ICP备12020745号</div>
        <div class="inner fn-clearfix">
            <div class="footer-content">
                <P>Copyright 2009-2010 www.nbjys.cn All Rights Reserved.</P>
            </div>
            <div class="footer-flags">
                <img src="${request.contextPath}/nbsitedata/images/flag01.png" alt="">
            </div>
        </div>
    </div>
</div>


<script>

    function go2Model(thisId, container) {
        $("." + container).load('${request.contextPath}/sitedata/model.html?thisId=' + thisId);
    }

    $(document).ready(function () {
        //加载首页
        $(".container").load('${request.contextPath}/sitedata/model.html?thisId=-1');
        //加载友情链接
        $(".friendLink").load('${request.contextPath}/sitedata/friendLink.html');
    });
</script>
</body>
</html>