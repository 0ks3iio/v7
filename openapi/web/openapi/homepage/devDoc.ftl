<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
        <meta charset="utf-8" />
        <title>开发者-开发文档</title>
        <meta name="description" content="" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
        <link rel="stylesheet" href="${resourceUrl}/components/bootstrap/dist/css/bootstrap.css" />
        <link rel="stylesheet" href="${resourceUrl}/components/font-awesome/css/font-awesome.css" />
        <link rel="stylesheet" href="${resourceUrl}/components/layer/skin/layer.css">
        <link rel="stylesheet" href="${resourceUrl}/components/chosen/chosen.min.css">
        <link rel="stylesheet" href="${resourceUrl}/components/bootstrap-datepicker/dist/css/bootstrap-datepicker3.css">
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
            <!-- #section:basics/navbar.layout -->
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
                        <li>
                            <a  href="javascript:void(0);" id="manageCenter">控制台</a>
                        </li>
                        <li class="active">
                            <a href="${request.contextPath}/developer/devDoc">开发文档</a>
                        </li>
                        <li>
                            <a href="${request.contextPath}/problem/problemList">常见问题</a>
                        </li>
                    </ul>
                    <#if islogin = 0>
                    <div class="navbar-base-login">
                        <button class="btn btn-sm btn-blue js-login">&nbsp;登录&nbsp;</button>
                        <button class="btn btn-sm btn-ringblue" onclick="window.location.href='${request.contextPath}/developer/regist'">开发者注册</button>
                    </div>
                    <#else>
                      <div class="navbar-base-user">
                          <img class="nav-user-photo" src="/static/images/desktop/user-male.png" />
                          <span>${developer.username!}</span>
                          <a href="${request.contextPath}/developer/logout">退出</a>
                      </div>
                    </#if>
                </div><!-- /.navbar-container -->
            </div>
            <!-- /section:basics/navbar.layout -->
            <!-- #section:basics/toolbar.layout -->
            <div class="toolbar-base">
                <div class="toolbar-base-container clearfix" id="toolbar-base-container">
                    <div class="module-name">开发文档</div>
                </div>
            </div>
            <!-- #section:basics/toolbar.layout -->
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
                          <li>
                            <a href="javascript:void(0);" data-show="c">
                                <img class="menu-icon" src="${resourceUrl}/images/base/icons/app-icon03.png" alt="">
                                <span class="menu-text">概述</span>
                            </a>
                        </li>
                        <!--<li>
                            <a href="#" data-show="c">
                                <img class="menu-icon" src="../images/base/icons/app-icon04.png" alt="">
                                <span class="menu-text">接入指南</span>
                            </a>
                        </li>-->
                        <li>
                            <a href="javascript:void(0);" class="dropdown-toggle" data-show="c">
                                <img class="menu-icon" src="${resourceUrl}/images/base/icons/app-icon05.png" alt="">
                                <span class="menu-text">开放数据</span>
                                <b class="arrow fa fa-angle-down"></b>
                            </a>
                            <ul class="submenu">
                                <li class="">
                                    <a href="javascript:loadPage('${request.contextPath}/data/manage/page');">
                                                                                                             开放读取
                                    </a>
                                </li>
                            </ul>
                        </li>
                        <!--  <li>
                            <a href="#" class="dropdown-toggle" data-show="a">
                                <img class="menu-icon" src="../images/base/icons/app-icon06.png" alt="">
                                <span class="menu-text">单点登录</span>
                                <b class="arrow fa fa-angle-down"></b>
                            </a>
                            <ul class="submenu">
                                <li class="">
                                    <a href="#">
                                                                                                        统一登录
                                    </a>
                                </li>

                                <li class="">
                                    <a href="#">
                                                                                                          授权登录Auth 2.0
                                    </a>
                                </li>
                            </ul>
                        </li>

                        <li>
                            <a href="#" data-show="c">
                                <img class="menu-icon" src="../images/base/icons/app-icon07.png" alt="">
                                <span class="menu-text">资源下载</span>
                            </a>
                        </li>-->
                    </ul>
                </div>
                <script type="text/javascript">
                    try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
                </script>
            </div>

            <!-- /section:basics/sidebar -->
            <div class="main-content">
                
            </div><!-- /.main-content -->

        </div><!-- /.main-container -->

        
        <div class="layer layer-login">
            <form>
                <fieldset>
                    <h3 class="login-title">
                        <img class="logo" src="${logoUrl!}" alt="">
                        <span>${logoName!}</span>
                    </h3>
                    <h4 class="login-title-tips">
                        <p><span>使用用户名或邮箱登录</span></p>
                    </h4>
                    <p  id="error-msg" class="login-tips" style="display:none">用户名错了，再想想！</p>

                    <div class="login-form">
                        <label for="" class="sr-only">用户名</label>
                        <div class="input-group">
                            <span class="input-group-addon">
                                <i class="fa fa-user"></i>
                            </span>
                            <input type="text"  id="username" class="input-lg form-control" placeholder="请输入用户名">
                        </div>
                    </div>

                    <div class="login-form">
                        <label for="" class="sr-only">密码</label>
                        <div class="input-group">
                            <span class="input-group-addon">
                                <span class="fa fa-lock"></span>
                            </span>
                            <input type="password" id="password" class="input-lg form-control" placeholder="请输入密码">
                        </div>
                    </div>
                        
                    <div class="login-form">
                        <button id="login" type="button" class="btn btn-block btn-lg btn-blue">立即登录</button>
                    </div>
                        
                    <div class="login-form clearfix">
                        <span class="pull-right" >还没有账号？<a href="${request.contextPath}/developer/regist">立即注册</a></span>
                    </div>
                </fieldset>
            </form>
        </div>
        

        <!-- basic scripts -->

        <!--[if IE]>
        <script src="../components/jquery.1x/dist/jquery.js"></script>
        <![endif]-->
        <script type="text/javascript">
         if(browser.ie){
              document.write("<script src='${resourceUrl}/components/jquery.1x/dist/jquery.js'>"+"<"+"/script>");
          }else{
              document.write("<script src='${resourceUrl}/components/jquery/dist/jquery.js'>"+"<"+"/script>");
          }
            
            if('ontouchstart' in document.documentElement) document.write("<script src='${resourceUrl}/components/_mod/jquery.mobile.custom/jquery.mobile.custom.js'>"+"<"+"/script>");
        </script>
        <script src="${resourceUrl}/components/bootstrap/dist/js/bootstrap.js"></script>

        <!-- page specific plugin scripts -->
        <script src="${resourceUrl}/components/layer/layer.js"></script>
        <script src="${resourceUrl}/components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
        <script src="${resourceUrl}/components/chosen/chosen.jquery.min.js"></script>
        <script src="${resourceUrl}/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
        <script src="${resourceUrl}/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>

        <!-- ace scripts -->
        <script src="${resourceUrl}/assets/js/src/ace.ajax-content.js"></script>        <!-- 异步加载js -->
        
        <!--代码高亮-->
        <script src="${resourceUrl}/components/highlight/highlight.min.js"></script>
        <script type="text/javascript">
            hljs.initHighlightingOnLoad();
        </script>

        <!-- inline scripts related to this page -->
        <!--  <script src="${resourceUrl}/openapi/js/openapi.desktop.js"></script>-->
        <script src="${resourceUrl}/js/desktop.js"></script>
        <script src="${resourceUrl}/openapi/js/myscript.js"></script>
        <script src="${resourceUrl}/openapi/js/openUtil.js"></script>
        <script>
            $(function(){
              $('.js-login').on('click',showLoginDiv);
              $('#login').on('click',login);
              $('#manageCenter').on('click',manageCenter);
                //初始化
                $('.page-content .tab-pane').each(function(){
                    var paneIndex=$(this).index()+1;
                    var $menu='';
                    var $list='';
                    var itemIndex=0;
                    $(this).find('.base-menu-from').each(function(){
                        itemIndex++;
                        var itemId='tabpanel'+paneIndex+'-item'+itemIndex;
                        $(this).attr('id',itemId)
                        var off_top=parseInt($(this).offset().top);
                        $list=$list+'<li class="base-fixed-menu-tier'+$(this).attr('data-tier')+'" data-scroll="'+off_top+'"><a href="#'+itemId+'"><i class="fa fa-circle"></i>'+$(this).text()+'</a></li>';
                    });
                    $menu='<ul class="base-fixed-menu">'+$list+'</ul>';
                    $(this).append($menu);
                    $(this).find('.base-fixed-menu li:eq(0)').addClass('active');
                });
                //切换的时候重新算高度，隐藏层offset().top不准确
                $('.nav-tabs li').click(function(){
                    setTimeout(function(){
                        var $pane=$('.page-content .tab-pane:eq('+$(this).index()+')');
                        var itemIndex=0;
                        $pane.find('.base-menu-from').each(function(){
                            var off_top=parseInt($(this).offset().top);
                            $pane.find('.base-fixed-menu li:eq('+itemIndex+')').attr('data-scroll',off_top);
                            itemIndex++;
                        });
                    },200);
                });
                //点击左侧定位
                $('.page-content .tab-pane').on('click','.base-fixed-menu li',function(e){
                    e.preventDefault();
                    var sTop=$(this).attr('data-scroll')-136;
                    $(this).addClass('active').siblings('li').removeClass('active');
                    $(window).scrollTop(sTop);
                    //alert(sTop);
                    //alert($(window).scrollTop());
                    //$('html,body').animate({scrollTop:$(this).attr('data-scroll')},500);
                });
                //滚动右侧导航切换
                scrollFixed();
                $(window).scroll(function(){
                    scrollFixed();
                });
                function scrollFixed(){
                    var $menu_li=$('.page-content .tab-pane:visible .base-fixed-menu li');
                    var sTop=$(this).scrollTop()+136;
                    var menu_len=$('.page-content .tab-pane:visible').find('.base-fixed-menu li').length;
                    var i=0;
                    $menu_li.each(function(){
                        var data_scroll=$(this).attr('data-scroll');
                        if (sTop >= data_scroll) {
                            i++;
                        };
                    });
                    if (i == 0) {
                        i==0;
                    } else{
                        i--;
                    };
                    $menu_li.eq(i).addClass('active').siblings('li').removeClass('active');
                    //判断是否滚动到底部
                    var canTop=$(document).height()-$(window).height();
                    if (sTop == canTop) {
                        $menu_li.last().addClass('active').siblings('li').removeClass('active');
                    }
                };
            })
            
            document.onkeydown = function(e) {
                e = e || window.event;
                if(e.keyCode == 13) {
                  $('#login').click();
                    return false;
                }
              }
            
             function showLoginDiv(){
                layer.open({
                    type: 1,
                    shade: .5,
                    area: '500px',
                    title: false,
                    content: $('.layer-login')
                });
              }
            
            function login(){
              var username=$('#username').val();
              if($.trim(username)==''){
                $('#error-msg').text('请输入用户名');
                $('#error-msg').show();
                return;
              };
              var pwd=$('#password').val();
              if($.trim(pwd)==''){
                $('#error-msg').text('请输入密码');
                $('#error-msg').show();
                return;
              };
              // error-msg
              $.post('${request.contextPath}/developer/login',{'username':username,'password':pwd},function(data){
                var obj=JSON.parse(data);
                if(obj.success){
                  $('#login').text(data.msg);
                  location.href='/developer/home';
                }
                $('#error-msg').text(obj.msg);
                $('#error-msg').show();
                return;
              });
            }
            
            function manageCenter(){
              var islogin = ${islogin!};
              if(null == islogin||islogin == 0){
                $('.js-login').click();
                return;
              }
              
              window.location.href="${request.contextPath}/developer/home";
            }
            
            function loadPage(url){
              loadDiv('.main-content',url);
            }
        </script>
    </body>
</html>
