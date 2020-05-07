<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
        <meta charset="utf-8" />
        <title>开放平台</title>
        <meta name="description" content="" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
        <!-- bootstrap & fontawesome -->
        <link rel="stylesheet" href="${resourceUrl}/components/bootstrap/dist/css/bootstrap.css" />
        <link rel="stylesheet" href="${resourceUrl}/components/font-awesome/css/font-awesome.css" />

        <link rel="stylesheet" href="${resourceUrl}/css/components.css">
        <link rel="stylesheet" href="${resourceUrl}/css/basic-data.css">
        <!--[if lte IE 9]>
          <link rel="stylesheet" href="${resourceUrl}/css/pages-ie.css" />
        <![endif]-->
        <!-- inline styles related to this page -->

        <!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

        <!--[if lte IE 8]>
            <script src="${resourceUrl}/components/html5shiv/dist/html5shiv.min.js"></script>
            <script src="${resourceUrl}/components/respond/dest/respond.min.js"></script>
        <![endif]-->
    </head>
<script>

</script>
    <body>
        <div class="header clearfix">
            <div class="navbar-header">
                <a href="${request.contextPath}/developer/index" class="navbar-brand">
                    <img class="logo" src="${logoUrl!}" alt="">
                    <span>${logoName!}</span>
                </a>
            </div>
            <ul class="navbar-nav">
                <li>
                    <a href="${request.contextPath}/developer/index">首页</a>
                </li>
                <li>
                    <a href="javascript:void(0);" id="manageCenter">控制台</a>
                </li>
                <li>
                    <a href="${request.contextPath}/developer/devDoc">开发文档</a>
                </li>
                <li class="active">
                    <a href="${request.contextPath}/problem/problemList">常见问题</a>
                </li>
                <!-- /section:basics/navbar.user_menu -->
            </ul>
            <#if islogin = 0>
            <div class="header-login">
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
        </div>
        <div class="content">
            <div class="faq-header">
                <div class="faq-header-inner">
                    <div class="module-name">常见问题</div>
                </div>
            </div>
            <div class="faq-container clearfix">
                <div class="faq-sidebar">
                    <#if problemTypeList??>
                    <#list problemTypeList as type>
                    <div class="faq-sidebar-item"><!-- faq-sidebar-item-open-->
                        <div class="faq-sidebar-item-header"><i class="fa fa-angle-down"></i>${type.name}</div>
                        <#if typeProblemsMap??&&typeProblemsMap[type.id]??>
                        <ul class="faq-sidebar-item-body">
                            <#list typeProblemsMap[type.id] as problem>
                                <li class="questionclick"><a href="javascript:void(0);" data-i="${problem.id!}" class="${problem.id!}">• ${problem.question!}</a></li>
                            </#list>
                        </ul>
                        </#if>
                    </div>
                    </#list>
                    </#if>
                </div>
                <div class="faq-content">
                    <div class="faq-search">
                        <div class="faq-search-wrap clearfix">
                            <input type="text" class="faq-search-txt" placeholder="请输入关键词" />
                            <button class="faq-search-btn">搜索</button>
                        </div>
                    </div>
                    <div id="rightContent">
                        
                    </div> <!--rightContent-->
                </div><!--faq-content-->
            </div>
        </div>
        
        <!--登录-->
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
        <!--.layer-login-->
        
        <#include "/openapi/homepage/foot.ftl">
        <!--<div class="footer">
            <div class="container">
                <div class="footer-body">
                    <p><a href="#">微课掌上通</a>|<a href="#">课后网</a>|<a href="#">三通两平台</a></p>
                    <p>浙江万朋教育科技股份有限公司版权所有 备案号:浙ICP备05070430号</p>
                    <p>客服热线：400-863-2003（教育云平台）400-617-1997（课后网）400-667-1997（家校互联）4000-150-150（微课掌上通）</p>
                </div>
            </div>
        </div>-->
        <!-- basic scripts -->
        
        
        <!--[if !IE]> -->
        <script src="${resourceUrl}/components/jquery/dist/jquery.js"></script>

        <!-- <![endif]-->

        <!--[if IE]>
        <script src="${resourceUrl}/components/jquery.1x/dist/jquery.js"></script>
        <![endif]-->
        <script type="text/javascript">
            if('ontouchstart' in document.documentElement) document.write("<script src='${resourceUrl}/components/_mod/jquery.mobile.custom/jquery.mobile.custom.js'>"+"<"+"/script>");
        </script>
        <script src="${resourceUrl}/components/bootstrap/dist/js/bootstrap.js"></script>

        <!-- page specific plugin scripts -->
        <script src="${resourceUrl}/components/layer/layer.js"></script>

        <!-- inline scripts related to this page -->
        <script>
            $(function(){
                $('.faq-sidebar-item-header').click(function(){
                    $(this).parent('.faq-sidebar-item').toggleClass('faq-sidebar-item-open');
                });
                $('.faq-sidebar-item-body li').click(function(){
                    $('.faq-sidebar-item-body li').removeClass('active');
                    $(this).addClass('active');
                });
                
                window.rightContentLoad = function(url,data){
                  if(data){
                    $("#rightContent").load(url,data);
                    return;
                  }
                  $("#rightContent").load(url);
                };
                rightContentLoad("${request.contextPath}/problem/lastProblemList");
                $(".questionclick").click(function(){
                    $(".faq-search-txt").val("");
                    rightContentLoad("${request.contextPath}/problem/problemDetail?id="+$(this).find('a').data("i"));
                });
                $(".faq-search-btn").click(function(){
                    var question = $.trim($(".faq-search-txt").val());
                    if(question==""){
                        layer.alert("请输入关键字");
                        return;
                    }
                    rightContentLoad("${request.contextPath}/problem/serchProblem",{"question":question});
                });
                //登录
                $('#manageCenter').on('click',manageCenter);
                $('.js-login').on('click',showLoginDiv);
                $('#login').on('click',login);
                
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
                      location.href='${request.contextPath}/developer/home';
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
                
            })
        </script>
    </body>
</html>
