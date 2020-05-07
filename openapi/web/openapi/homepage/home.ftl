<!DOCTYPE html>
<html lang="en">
        <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
        <meta charset="utf-8" />
        <title>开放平台</title>
        <meta name="description" content="" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
        <link rel="stylesheet" href="${resourceUrl}/components/bootstrap/dist/css/bootstrap.css" />
        <link rel="stylesheet" href="${resourceUrl}/components/font-awesome/css/font-awesome.css" />
        <link rel="stylesheet" href="${resourceUrl}/components/layer/skin/layer.css">
        <link rel="stylesheet" href="${resourceUrl}/components/chosen/chosen.min.css">
        <link rel="stylesheet" href="${resourceUrl}/components/bootstrap-datepicker/dist/css/bootstrap-datepicker3.css">
        <link rel="stylesheet" href="${resourceUrl}/components/bootstrap-daterangepicker/daterangepicker-bs3.css">
        <link rel="stylesheet" href="${resourceUrl}/components/highlight/styles/monokai-sublime-custom.css">
        <link rel="stylesheet" href="${resourceUrl}/css/iconfont.css">
        <link rel="stylesheet" href="${resourceUrl}/css/components.css">
        <link rel="stylesheet" href="${resourceUrl}/css/page-desktop.css">
        <link rel="stylesheet" href="${resourceUrl}/css/pages.css">
        <script src="${resourceUrl}/assets/js/ace-extra.js"></script>
        <script type="text/javascript">
          var browser = {};//定义浏览器json数据对象
          var ua = navigator.userAgent.toLowerCase();
          var s;
          (s = ua.match(/msie ([\d.]+)/)) ? browser.ie = s[1] :
          (s = ua.match(/firefox\/([\d.]+)/)) ? browser.firefox =   s[1] :
          (s = ua.match(/chrome\/([\d.]+)/)) ? browser.chrome =   s[1] :
          (s = ua.match(/opera.([\d.]+)/)) ? browser.opera = s[1]   :
          (s = ua.match(/version\/([\d.]+).*safari/)) ?   browser.safari = s[1] : 0;
          if (browser.ie){
            document.write('<link rel="stylesheet" href="${resourceUrl}/css/pages-ie.css">');
            if(browser.ie=='8.0'){
              document.write("<script src='${resourceUrl}/components/html5shiv/dist/html5shiv.min.js'>"+"<"+"/script>");
              document.write("<script src='${resourceUrl}/components/respond/dest/respond.min.js'>"+"<"+"/script>");
            }
         }
        </script>
    </head>
    <body class="no-skin">
        <div class="base-top-fixed">
            <div id="navbar" class="navbar navbar-base">
                <script type="text/javascript">
                    try{ace.settings.check('navbar' , 'fixed')}catch(e){}
                </script>
                <div class="navbar-container clearfix" id="navbar-container">
                    <div class="navbar-header">
                        <a href="${request.contextPath}/developer/index" class="navbar-brand">
                            <img class="logo" src="${logoUrl!}" alt="">
                            <span>${logoName!}</span>
                        </a>
                    </div>
                    
                    <ul class="navbar-base-nav navbar-left">
                        <li>
                            <a href="${request.contextPath}/developer/index">首页</a>
                        </li>
                        <li class="active">
                            <a href="${request.contextPath}/developer/home">控制台</a>
                        </li>
                        <li>
                            <a href="${request.contextPath}/developer/devDoc">开发文档</a>
                        </li>
                        <li>
                            <a href="${request.contextPath}/problem/problemList">常见问题</a>
                        </li>
                    </ul>
                    
                    <div class="navbar-base-user">
                        <img class="nav-user-photo" src="/static/images/desktop/user-male.png"  />
                        <span>${developer.username!}</span>
                        <a href="${request.contextPath}/developer/logout">退出</a>
                    </div>
                </div>
            </div>
            
            <div class="toolbar-base">
                <div class="toolbar-base-container clearfix" id="toolbar-base-container">
                    <div class="module-name">控制台</div>
                </div>
            </div>
        </div>
        
        <div class="main-container" id="main-container">
            <script type="text/javascript">
                try{ace.settings.check('main-container' , 'fixed')}catch(e){}
            </script>

            <!-- #section:basics/sidebar -->
            <div id="sidebar" class="sidebar                  responsive sidebar-fixed">
                <script type="text/javascript">
                    try{ace.settings.check('sidebar' , 'fixed')}catch(e){}
                </script>
                <div class="nav-wrap js-nav-wrap">
                    <ul class="nav nav-list">
                        <li class="active">
                            <a href="javascript:loadPage('${request.contextPath}/appManage/appList');"data-show="a">
                                <img class="menu-icon" src="${resourceUrl}/images/base/icons/app-icon01.png" alt="">
                                <span class="menu-text">我的应用</span>
                            </a>
                        </li>
                        <!--  <li>
                            <a href="javascript:loadPage('/data/manage/page');" data-show="b">
                                <img class="menu-icon" src="${resourceUrl}/images/icons/app-icon/icon08-sm.png" alt="">
                                <span class="menu-text">开放接口</span>
                            </a>
                        </li>
                        <li>
                            <a href="javascript:loadPage('/developer/sso/passportDocument')" data-show="c">
                                <img class="menu-icon" src="${resourceUrl}/images/icons/app-icon/icon02-sm.png" alt="">
                                <span class="menu-text">单点登录</span>
                            </a>
                        </li>-->
                        <li>
                            <a href="javascript:loadPage('${request.contextPath}/developer/userInfo');" data-show="d">
                                <img class="menu-icon" src="${resourceUrl}/images/base/icons/app-icon02.png" alt="">
                                <span class="menu-text">我的资料</span>
                            </a>
                        </li>
                    </ul>
                </div>
                <div class="subNav-modal js-subNav-modal">
                    <div class="subNav-modal-container">
                        
                    </div>
                </div>
                <!-- #section:basics/sidebar.layout.minimize -->
               <!--   <div class="sidebar-toggle sidebar-collapse js-siderbar-collapse" id="sidebar-collapse">
                    <i class="wpfont icon-angle-double-left"></i>
                </div>-->

                <!-- /section:basics/sidebar.layout.minimize -->
                <script type="text/javascript">
                    try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
                </script>
            </div>

            <!-- /section:basics/sidebar -->
            <div class="main-content">
                
            </div><!-- /.main-content -->

        </div><!-- /.main-container -->
        
        
        
        <script type="text/javascript">
            if(browser.ie){
                document.write("<script src='${resourceUrl}/components/jquery.1x/dist/jquery.js'>"+"<"+"/script>");
            }else{
                document.write("<script src='${resourceUrl}/components/jquery/dist/jquery.js'>"+"<"+"/script>");
            }
            if('ontouchstart' in document.documentElement) document.write("<script src='${resourceUrl}/components/_mod/jquery.mobile.custom/jquery.mobile.custom.js'>"+"<"+"/script>");
        </script>
        <script src="${resourceUrl}/components/bootstrap/dist/js/bootstrap.js"></script>
        <script src="${resourceUrl}/components/layer/layer.js"></script>
        <script src="${resourceUrl}/components/echarts/echarts.min.js"></script>
        <script src="${resourceUrl}/components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
        <script src="${resourceUrl}/components/chosen/chosen.jquery.min.js"></script>
        <script src="${resourceUrl}/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
        <script src="${resourceUrl}/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
        <script src="${resourceUrl}/components/bootstrap-daterangepicker/moment.min.js"></script>
        <script src="${resourceUrl}/components/bootstrap-daterangepicker/daterangepicker.js"></script>
        <script src="${resourceUrl}/assets/js/src/ace.ajax-content.js"></script>
        <script src="${resourceUrl}/js/jquery.form.js"></script>
        <script src="${resourceUrl}/js/tool.js"></script>
        <script src="${resourceUrl}/openapi/js/openapi.desktop.js"></script>
        <script src="${resourceUrl}/openapi/js/myscript.js"></script>
        <script src="${resourceUrl}/openapi/js/openUtil.js"></script>
        <script>
            $(function(){
              loadPage('${request.contextPath}/appManage/appList');
            });
            
            function loadPage(url){
              loadDiv('.main-content',url);
            }
        </script>
    </body>
</html>