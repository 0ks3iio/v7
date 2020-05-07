<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>超管-系统激活</title>

    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="/static/components/bootstrap/dist/css/bootstrap.css" />
    <link rel="stylesheet" href="/static/components/font-awesome/css/font-awesome.css" />

    <link rel="stylesheet" href="/static/css/components.css">
    <link rel="stylesheet" href="/static/css/basic-data.css">
    <!--[if lte IE 9]>
    <link rel="stylesheet" href="/static/css/pages-ie.css" />
    <![endif]-->
    <!-- inline styles related to this page -->

    <!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

    <!--[if lte IE 8]>
    <script src="/static/components/html5shiv/dist/html5shiv.min.js"></script>
    <script src="/static/components/respond/dest/respond.min.js"></script>
    <![endif]-->
</head>

<body class="super">
<div class="header">
    <div class="container">
        <a href="#" class="navbar-brand">万朋平台配置助手</a>
    </div>
</div>

<div class="content">
    <div class="container" id="ops-container">

    </div>

</div>

<div class="footer">
    <div class="container">
        <div class="footer-body">
            <p>浙江万朋教育科技股份有限公司版权所有 备案号:浙ICP备05070430号</p>
        </div>
    </div>
</div>

<!--[if !IE]> -->
<script src="/static/components/jquery/dist/jquery.js"></script>

<!-- <![endif]-->

<!--[if IE]>
<script src="/static/components/jquery.1x/dist/jquery.js"></script>
<![endif]-->
<script type="text/javascript">
    if ('ontouchstart' in document.documentElement)
        document.write("<script src='/static/components/_mod/jquery.mobile.custom/jquery.mobile.custom.js'>"
                + "<"+"/script>");
</script>
<script src="/static/components/bootstrap/dist/js/bootstrap.js"></script>
<script src="/static/components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<script src="/static/components/chosen/chosen.jquery.min.js"></script>

<!-- page specific plugin scripts -->
<script src="/static/components/layer/layer.js"></script>
<script src="/static/js/jquery.MultiSelect.js"></script>
<!-- ace scripts -->
<script src="/static/ace/js/ace-extra.js"></script>
<!-- 异步加载js -->
<script src="/static/js/tool.js"></script>

<!-- inline scripts related to this page -->
<script>
    $(document).ready(function () {
    <#if code?default("")=="1">
        $("#ops-container").load("${request.contextPath}/system/ops/sysOption");
    <#else>
        $("#ops-container").load("${request.contextPath}/system/ops/resetPassword");
    </#if>
    })
</script>
</body>
</html>
