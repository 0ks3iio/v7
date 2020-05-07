<#--
    this page is full can be used to iframe
    ex:
        public String ex(){
            return "/desktop/error.ftl";
        }
 -->
<#import  "/fw/macro/webmacro.ftl" as w />
<@w.commonWeb title="智慧校园" showFramework=true desktopIndex=true fullLoad=true>
<div class="main-container" id="main-container">

    <div class="main-content">
        <div class="main-content-inner">
            <div class="page-content">
                <div class="page-error">
                    <h1 class="page-error-state">404</h1>
                    <p class="page-error-tips">出错了！${errorMess?default('您访问的页面不存在')}</p>
                    <button class="btn btn-blue btn-long" onclick="function () {top.location.href='${homeUrl!}';}">返回首页</button>
                </div>
            </div><!-- /.page-content -->
        </div>
    </div><!-- /.main-content -->

</div>
</@w.commonWeb>