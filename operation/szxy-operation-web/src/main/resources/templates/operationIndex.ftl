<!-- 运营平台首页 --->
<#import "macro/layer.ftl" as opLayer />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>运营平台</title>
    <link rel="icon" href="${springMacroRequestContext.contextPath}/static/operation/images/favicon.ico" type="image/x-icon" />
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>

    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet"
          href="${springMacroRequestContext.contextPath}/static/components/bootstrap/dist/css/bootstrap.css"/>
    <link rel="stylesheet"
          href="${springMacroRequestContext.contextPath}/static/components/font-awesome/css/font-awesome.css"/>

    <!-- page specific plugin styles -->


    <link rel="stylesheet" href="${springMacroRequestContext.contextPath}/static/css/iconfont.css">
    <link rel="stylesheet" href="${springMacroRequestContext.contextPath}/static/css/components.css">
    <link rel="stylesheet" href="${springMacroRequestContext.contextPath}/static/css/page-desktop.css">
    <link rel="stylesheet" href="${springMacroRequestContext.contextPath}/static/components/spop/css/spop.css">
    <link rel="stylesheet" href="${springMacroRequestContext.contextPath}/static/css/pages.css">
    <link rel="stylesheet" href="${springMacroRequestContext.contextPath}/static/css/page-desk.css">
    <!--[if lte IE 9]>
    <link rel="stylesheet" href="${springMacroRequestContext.contextPath}/static/css/pages-ie.css"/>
    <![endif]-->
    <!-- inline styles related to this page -->
    <link rel="${springMacroRequestContext.contextPath}/static/components/bootstrap-validator/dist/css/bootstrapValidator.min.css" />


    <!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

    <!--[if lte IE 8]>
    <script src="${springMacroRequestContext.contextPath}/static/components/html5shiv/dist/html5shiv.min.js"></script>
    <script src="${springMacroRequestContext.contextPath}/static/components/respond/dest/respond.min.js"></script>
    <![endif]-->
    <script>
        _contextPath = "${springMacroRequestContext.contextPath}";
    </script>
    <style>
        .menu-icon {
            width: 20px;
            height: 20px;
        }
    </style>
</head>

<body class="no-skin">
<!-- #section:basics/navbar.layout -->
<div id="navbar" class="navbar navbar-default navbar-fixed-top">

    <div class="navbar-container clearfix" id="navbar-container">

        <div class="navbar-header">
            <!-- #section:basics/navbar.layout.brand -->
            <a href="#" class="navbar-brand">
                <#--<img class="logo" src="${springMacroRequestContext.contextPath}/static/images/desktop/logo.png" alt="">-->
                <span>运营平台</span>
            </a>

            <!-- /section:basics/navbar.layout.brand -->
        </div>
        <!-- #section:basics/navbar.dropdown -->

        <ul class="nav navbar-nav navbar-right">
            <!-- #section:basics/navbar.user_menu -->
            <li>
                <a class="" href="#/operation/setting/index">
                    <i class="wpfont icon-cog"></i>
                </a>
            </li>
            <li class="navbar-item-user">
                <a href="javascript:void(0);" class="" data-toggle="dropdown">
                    <#if sex?default(1)==1>
                    <img class="nav-user-photo" src="${springMacroRequestContext.contextPath}/static/images/common/user-male.png"/>
                    <#else >
                    <img class="nav-user-photo" src="${springMacroRequestContext.contextPath}/static/images/common/user-female.png"/>
                    </#if>
                    <span class="user-info">${realName!}</span>
                    <i class="wpfont icon-caret-down"></i>
                </a>
                <div class="dropdown-menu dropdown-menu-user">
                    <ul class="user-list user-list-hover">
                        <li>
                            <#if sex?default(1)==1>
                            <img class="user-avatar" src="${springMacroRequestContext.contextPath}/static/images/common/user-male.png"/>
                            <#else >
                            <img class="user-avatar" src="${springMacroRequestContext.contextPath}/static/images/common/user-female.png"/>
                            </#if>
                            <h5 class="user-name">${realName!}</h5>
                        </li>
                    </ul>
                    <a href="${springMacroRequestContext.contextPath}/operation/logout" class="quit">退出账号</a>
                </div>
            </li>
            <!-- /section:basics/navbar.user_menu -->
        </ul>

        <!-- /section:basics/navbar.dropdown -->
    </div><!-- /.navbar-container -->
</div>

<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">

    <!-- #section:basics/sidebar -->
    <div id="sidebar" class="sidebar                  responsive sidebar-fixed">
        <div class="sidebar-shortcuts">
            <button class="btn btn-tohome js-siderbar-collapse">
                <i class="wpfont icon-dedent"></i>
            </button>
        </div>
        <div class="nav-wrap js-nav-wrap">
            <ul class="nav nav-list">
                <#if modules?? && modules?size gt 0>
                <#list modules as module>
                    <li>
                        <a href="#${module.url}" data-show="a">
                            <img class="menu-icon"
                                 src="${springMacroRequestContext.contextPath}/static/operation/images/icons/unitmanage.png"
                                 alt="">
                            <span class="menu-text">${module.name}</span>
                        </a>
                    </li>
                </#list>
                </#if>
            </ul>
        </div>
    </div>

    <!-- /section:basics/sidebar -->
    <div class="main-content">

        <div class="main-content-inner">
            <!-- 主内容区 -->
            <div class="page-content">
            </div>
        </div>
    </div><!-- /.main-content -->
</div><!-- /.main-container -->


<!-- basic scripts -->

<!--[if !IE]> -->
<script src="${springMacroRequestContext.contextPath}/static/components/jquery/dist/jquery.js"></script>

<!-- <![endif]-->

<!--[if IE]>
<script src="${springMacroRequestContext.contextPath}/static/components/jquery.1x/dist/jquery.js"></script>
<![endif]-->
<script src="${springMacroRequestContext.contextPath}/static/operation/js/op-layer.js"></script>
<script src="${springMacroRequestContext.contextPath}/static/components/spop/js/spop.js"></script>
<script src="${springMacroRequestContext.contextPath}/static/operation/js/jquery-ajax-security.js"></script>

<@opLayer.op_layer />
<script type="text/javascript">
    if ('ontouchstart' in document.documentElement) document.write("<script src='${springMacroRequestContext.contextPath}/static/components/_mod/jquery.mobile.custom/jquery.mobile.custom.js'>" + "<" + "/script>");
</script>
<script src="${springMacroRequestContext.contextPath}/static/components/bootstrap/dist/js/bootstrap.js"></script>

<!-- page specific plugin scripts -->
<script src="${springMacroRequestContext.contextPath}/static/components/layer/layer.js"></script>
<script src="${springMacroRequestContext.contextPath}/static/components/jquery-slimscroll/jquery.slimscroll.min.js"></script>

<!-- inline scripts related to this page -->
<script src="${springMacroRequestContext.contextPath}/static/js/desktop.js"></script>
<script src="${springMacroRequestContext.contextPath}/static/components/szxyTree/js/szxyTree.js"></script>
<script src="${springMacroRequestContext.contextPath}/static/js/router.js"></script>
<script src="${springMacroRequestContext.contextPath}/static/operation/js/operationIndex.js"></script>
<script src="${springMacroRequestContext.contextPath}/static/operation/js/loading.js"></script>
<script src="${springMacroRequestContext.contextPath}/static/operation/js/message.js"></script>
<script src="${springMacroRequestContext.contextPath}/static/components/bootstrap-validator/dist/js/bootstrapValidator.js" ></script>
<script src="${springMacroRequestContext.contextPath}/static/components/bootstrap-validator/dist/js/language/zh_CN.js" ></script>
<script src="${springMacroRequestContext.contextPath}/static/components/bootstrap-validator/dist/js/bootstrapValidator-chineseLength.js" ></script>
<script src="${springMacroRequestContext.contextPath}/static/components/bootstrap-validator/dist/js/bootstrapValidator-decimal.js" ></script>
<script src="${springMacroRequestContext.contextPath}/static/components/bootstrap-validator/dist/js/bootstrapValidator-chineseMobile.js" ></script>

<script>
    $(function () {
       <#if modules?? && modules?size gt 0>
           <#list modules as module>
                routeUtils.add('${module.url}', function () {
                    $('.page-content').loading("${springMacroRequestContext.contextPath}${module.url}")
                });
           </#list>
       </#if>
        //
        $(document).click(function(event){
            var eo=$(event.target);
            if($('.multi-select-layer').is(':visible') && !eo.parents('.multi-select').length)
                $('.multi-select-layer').addClass('hide')
        });

        routeUtils.add('/operation/setting/index', function () {
            $('.page-content').loading(_contextPath + '/operation/setting/index');
        })
    });
</script>
</body>
</html>