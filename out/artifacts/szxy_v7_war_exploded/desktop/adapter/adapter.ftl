<#import  "/fw/macro/webmacro.ftl" as w />

<style>
    .page-sidebar {
        left: 20px !important;
        top: 60px !important;
    }
</style>

<@w.commonWeb title="智慧校园" showFramework=true desktopIndex=true fullLoad=true>

    <div class="main-container" id="main-container">
        <script type="text/javascript">
            try{ace.settings.check('main-container' , 'fixed')}catch(e){}
        </script>
        <div class="main-content">
            <div class="main-content-inner" >
                <div class="breadcrumb show" style="display: none; left: 0; top: 0;box-shadow: none;border: none; padding-left: 20px" id="breadDiv">
                    <div class="float-left _back_div" >
                        <a href="javascript:void(0);" class="back"></a>
                        <span class="title title-name">页面标题</span>
                    </div>
                </div>
                <!-- div load 页面 -->
                <div class="page-content" style="padding: 60px 20px 20px;">
                    <div class="row">
                        <div class="col-xs-12" id="deskTopContainer" >
                            <!-- 7。0子系统实际load的位置 -->
                            <div id="inner-system">

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        $(function () {
           $('#inner-system').load("${targetUrl!}");
           currentModuleName = "${moduleName!}";
           showDefaultBread();
        });
    </script>
</@w.commonWeb>
