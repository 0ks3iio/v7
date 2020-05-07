<#import  "/fw/macro/webmacro.ftl" as w />
<@w.commonWeb title="智慧校园" showFramework=true desktopIndex=true fullLoad=true>
<div id="navbar" class="navbar navbar-default navbar-fixed-top">

</div>

<div class="main-container" id="main-container">
    <script type="text/javascript">
        try{ace.settings.check('main-container' , 'fixed')}catch(e){}
    </script>
    <div id="sidebar" class="sidebar responsive sidebar-fixed">
    </div>
    <div class="main-content">
        <div class="page-tab-wrap clearfix ">
            <div class="btn-tab-scroll left js-pageTabLeft" href="#">
                <a href="#">
                    <i class="wpfont icon-arrow-left"></i>
                </a>
            </div>
            <div class="page-tabs js-pageTabs">
                <div class="page-tabs-container js-pageTabsContainer">
                </div>
            </div>
            <div class="btn-tab-scroll right js-pageTabRight" href="#">
                <a href="#">
                    <i class="wpfont icon-arrow-right"></i>
                </a>
            </div>
        </div>
        <div class="main-content-inner" >
            <div class="breadcrumb show" style="display: none;" id="breadDiv">
                <div class="float-left _back_div" style="display: none;">
                    <a href="javascript:void(0);" class="back"></a>
                    <span class="title title-name">页面标题</span>
                </div>
                <ol class="float-right">
                    <li><a href="javascript:void(0);" id="bread-home" onclick="goHome();">桌面</a></li>
                    <li><a href="javascript:void(0);" id="bread-system"></a></li>
                    <li id="bread-parent"><a href="javascript:void(0);"></a></li>
                    <li class="active" id="bread-child"><a href="javascript:void(0);"></a></li>
                </ol>
            </div>
            <!-- div load 页面 -->
            <div class="page-content" >
                <div class="row">
                    <div class="col-xs-12" id="deskTopContainer" >
                        <div id="index-home" class="row"></div>
                    </div>
                </div>
            </div>
            <!-- S 面包屑导航 -->
            <div class="breadcrumb show" style="display: none;" id="breadIFrame">
                <div class="float-left _back_iframe" style="display: none;">
                    <a href="javascript:void(0);" class="back"></a>
                    <span class="title title-name"></span>
                </div>
                <ol class="float-right">
                    <li><a href="javascript:void(0);" id="bread-home" onclick="goHome();">桌面</a></li>
                    <li><a href="javascript:void(0);" id="bread-system"></a></li>
                    <li id="bread-parent"><a href="javascript:void(0);"></a></li>
                    <li class="active" id="bread-child"><a href="javascript:void(0);"></a></li>
                </ol>
            </div>
            <!-- E iframe插入页面 -->
            <div class="embed-responsive iframe-page js-iframe-page">

            </div>
        </div>
    </div>
</div>

<div class="user-setting"></div>
<!-- 功能区设置弹出框 -->
<div class="layer layer-boxSetting"></div>
<!-- S 添加模块弹出框 -->
<div class="layer layer-addModule"></div>

<script>
    $(document).ready(function(){
        <#--$("#navbar").load("${request.contextPath}/desktop/index/header?appId=${appId!}",headerInit);-->
        $("#sidebar").load("${request.contextPath}/desktop/index/nav?appId=${appId!}&module=${module!}",function () {
            sideBarInit();newNavListInit();
        });
        pageTabInit();
        //打开特定的子系统首页
    });



</script>
</@w.commonWeb>
