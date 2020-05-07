<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="UTF-8">
    <title>${headInfo.title!}</title>
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <link rel="shortcut icon"  href="${request.contextPath}/bigdata/v3/static/images/public/wanshu-icon16.png" >
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/css/bootstrap.css"/>
    <link rel="stylesheet" href="${request.contextPath}/bigdata/v3/static/font-awsome/css/font-awesome.css"/>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/fonts/iconfont.css"/>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/css/all.css"/>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/css/page.css"/>
</head>
<body>
<!--头部 S-->
<div class="bi-ana-header">
    <div class="bi-ana-header-title">
        <img src="${headInfo.logo!}">
        <span>${headInfo.platformName!}</span>
    </div>
    <div class="bi-ana-ul">
        <#if bizModuleList?exists && bizModuleList?size gt 0>
            <#list bizModuleList as module>
                <div id="module-${module.code!}" class="bi-ana-li active" onclick="loadModuleDetail('${module.code!}','${module.url!}')">${module.name!}</div>
            </#list>
        </#if>
    </div>
    <div></div>
</div>
<!--头部 E-->
<!--主体 S-->
<div class="main-container">
    <!--侧边栏 E-->
    <div class="main-content" >
        <div class="page-content" id="moduleDetailDiv"></div>
    </div><!-- /.main-content -->
    <!--内容 S-->
</div>
<!--主体 E-->

<script src="${request.contextPath}/bigdata/v3/static/js/jquery/jquery.min.js" type="text/javascript"
        charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/jquery/jquery.form.js" type="text/javascript"
        charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/bootstrap/bootstrap.min.js" type="text/javascript"
        charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/fonts/iconfont.js" async="async" defer="defer"
        type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/plugs/chosen/chosen.jquery.min.js" type="text/javascript"
        charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/layer/layer.js" async="async" defer="defer"
        type="text/javascript" charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/zTree/js/jquery.ztree.all.min.js" type="text/javascript"
        charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/jquery/jquery-toast.js" type="text/javascript"
        charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/laydate/laydate.min.js" defer="defer" type="text/javascript"
        charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/echarts/echarts.4.1.0.rc2.min.js" type="text/javascript"
        charset="utf-8"></script>
<script src="https://cdn.bootcss.com/ace/1.3.3/ace.js"></script>
<script src="https://cdn.bootcss.com/ace/1.3.3/ext-language_tools.js"></script>
<script src="https://cdn.bootcss.com/ace/1.3.3/theme-monokai.js"></script>
<script src="${request.contextPath}/bigdata/v3/static/assets/js/myscript.js" type="text/javascript"
        charset="utf-8"></script>
<script src="${request.contextPath}/bigdata/v3/static/js/myscript.js" type="text/javascript" charset="utf-8"></script>
<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/tool.js" type="text/javascript"
        charset="utf-8"></script>

<script type="text/javascript">
    function loadModuleDetail(code,url){
        $("#module-" + code).addClass('active').siblings('div').removeClass('active');
        $(".page-content").load('${request.contextPath}'+url);
    }

    $(document).ready(function () {
        <#if bizModuleList?exists && bizModuleList?size gt 0>
            $("#module-${currentModuleCode!}").trigger('click');
        <#else>
            $(".page-content").load('${request.contextPath}${currentModuleUrl!}');
        </#if>
    });
</script>
</body>
</html>
