<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
        <meta charset="utf-8" />
        <title>超管-应用管理</title>

        <meta name="description" content="" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

        <!-- bootstrap & fontawesome -->
        <link rel="stylesheet" href="${resourceUrl}/components/bootstrap/dist/css/bootstrap.css" />
        <link rel="stylesheet" href="${resourceUrl}/components/font-awesome/css/font-awesome.css" />

        <!-- page specific plugin styles -->
        <link rel="stylesheet" href="${resourceUrl}/components/layer/skin/layer.css">
        <link rel="stylesheet" href="${resourceUrl}/components/chosen/chosen.min.css">
        <link rel="stylesheet" href="${resourceUrl}/components/bootstrap-datepicker/dist/css/bootstrap-datepicker3.css">
        <link rel="stylesheet" href="${resourceUrl}/components/bootstrap-daterangepicker/daterangepicker-bs3.css">
        
        <link rel="stylesheet" href="${resourceUrl}/css/iconfont.css">
        <link rel="stylesheet" href="${resourceUrl}/css/components.css">
        <link rel="stylesheet" href="${resourceUrl}/css/page-desktop.css">
        <link rel="stylesheet" href="${resourceUrl}/css/pages.css">
        <!--[if lte IE 9]>
          <link rel="stylesheet" href="../css/pages-ie.css" />
        <![endif]-->
        <!-- inline styles related to this page -->
        

        <!-- ace settings handler -->
        <script src="${resourceUrl}/assets/js/ace-extra.js"></script>

        <!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

        <!--[if lte IE 8]>
            <script src="../components/html5shiv/dist/html5shiv.min.js"></script>
            <script src="../components/respond/dest/respond.min.js"></script>
        <![endif]-->
    </head>

    <body class="no-skin">
        <!-- #section:basics/navbar.layout -->
        <div id="navbar" class="navbar navbar-default navbar-fixed-top">
            <script type="text/javascript">
                try{ace.settings.check('navbar' , 'fixed')}catch(e){}
            </script>

            <div class="navbar-container clearfix" id="navbar-container">
                
                <div class="navbar-header">
                    <!-- #section:basics/navbar.layout.brand -->
                    <a href="#" class="navbar-brand">
                        <img class="logo" src="../images/logo.png" alt="">
                        <span>浙江万朋教育 · 数字校园</span>
                    </a>

                    <!-- /section:basics/navbar.layout.brand -->
                </div>
                <!-- #section:basics/navbar.dropdown -->
                
                <ul class="nav navbar-nav navbar-right">
                    <li>
                        <a href="#" class="js-dropbox-msg js-dropbox-toggle">
                            <i class="wpfont icon-bell"></i>
                            <span class="badge badge-yellow">5</span>
                        </a>
                        <div class="dropbox dropbox-msg">
                            <div class="drapbox-container">
                                <ul class="nav nav-tabs nav-tabs-1" role="tablist">
                                    <li role="presentation" class="active"><a href="#msg" role="tab" data-toggle="tab">消息</a></li>
                                    <li role="presentation"><a href="#post" role="tab" data-toggle="tab">公告</a></li>
                                    <li role="presentation"><a href="#other" role="tab" data-toggle="tab">其他</a></li>
                                </ul>

                                <!-- Tab panes -->
                                <div class="tab-content">
                                    <div role="tabpanel" class="tab-pane active" id="msg">
                                        <ul class="msg-list">
                                            <li class="msg-item">
                                                <i class="msg-icon fa fa-comment"></i>
                                                <span class="msg-text">这里可以放下十六个字个字个字个字这里可以放下十六个字个字个字个字</span>
                                                <span class="msg-time">12-15</span>
                                            </li>
                                            <li class="msg-item">
                                                <i class="msg-icon wpfont icon-clock-fill"></i>
                                                <span class="msg-text">这里可以放下十六个字个字个字个字</span>
                                                <span class="msg-time">12-15</span>
                                            </li>
                                            <li class="msg-item">
                                                <i class="msg-icon fa fa-refresh"></i>
                                                <span class="msg-text">这里可以放下十六个字个字个字个字</span>
                                                <span class="msg-time">12-15</span>
                                            </li>
                                        </ul>
                                    </div>
                                    <div role="tabpanel" class="tab-pane" id="post">公告</div>
                                    <div role="tabpanel" class="tab-pane" id="other">其他</div>
                                </div>
                            </div>
                        </div>
                    </li>
                    <li>
                        <a class="js-dropbox-mailList js-dropbox-toggle" href="#">
                            <i class="wpfont icon-directory"></i>
                        </a>
                        <div class="dropbox dropbox-mailList">
                            <div class="dropbox-container">
                                <ul class="nav nav-tabs nav-tabs-1" role="tablist">
                                    <li role="presentation" class="active">
                                        <a href="#tabpanel-mail" role="tab" data-toggle="tab">通讯录</a>
                                    </li>
                                </ul>

                                <!-- Tab panes -->
                                <div class="tab-content">
                                    <div role="tabpanel" class="tab-pane active" id="tabpanel-mail">
                                        <div class="mail-search">
                                            <div class="input-group">
                                              <input type="text" class="form-control">
                                              <span class="input-group-addon"><i class="fa fa-search"></i></span>
                                            </div>
                                            <div class="mail-search-result">
                                                <ul class="mail-list">
                                                    <li>
                                                        <span>阮小天</span>
                                                        <div class="mail-list-btns">
                                                            <a href="javascript:void(0);"><i class="fa fa-file-text"></i></a>
                                                            <a href="javascript:void(0);"><i class="fa fa-envelope"></i></a>
                                                        </div>
                                                    </li>
                                                    <li>
                                                        <span>阮小天</span>
                                                        <div class="mail-list-btns">
                                                            <a href="javascript:void(0);"><i class="fa fa-file-text"></i></a>
                                                            <a href="javascript:void(0);"><i class="fa fa-envelope"></i></a>
                                                        </div>
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>
                                        <ul class="nav nav-tabs nav-justified nav-tabs-1 nav-mail-list" role="tablist">
                                            <li role="presentation" class="active">
                                                <a href="#a" role="tab" data-toggle="tab">
                                                    <i class="fa fa-users"></i>
                                                </a>
                                            </li>
                                            <li role="presentation">
                                                <a href="#b" role="tab" data-toggle="tab">
                                                    <i class="fa fa-sitemap"></i>
                                                </a>
                                            </li>
                                        </ul>

                                        <!-- Tab panes -->
                                        <div class="tab-content">
                                            <div role="tabpanel" class="tab-pane active" id="a">
                                                <div class="panel-group" role="tablist" aria-multiselectable="true">
                                                    <div class="panel panel01">
                                                        <div class="panel-heading" role="tab">
                                                            <h4 class="panel-title">
                                                                <a data-toggle="collapse" href="#m01" aria-expanded="true" aria-controls="m01"><i class="fa fa-caret-right"></i>最近联系人</a>
                                                            </h4>
                                                        </div>
                                                        <div id="m01" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
                                                            <div class="panel-body">
                                                                <ul class="mail-list">
                                                                    <li>
                                                                        <span>阮小天</span>
                                                                        <div class="mail-list-btns">
                                                                            <a href="javascript:void(0);"><i class="fa fa-file-text"></i></a>
                                                                            <a href="javascript:void(0);"><i class="fa fa-envelope"></i></a>
                                                                        </div>
                                                                    </li>
                                                                    <li>
                                                                        <span>阮小天</span>
                                                                        <div class="mail-list-btns">
                                                                            <a href="javascript:void(0);"><i class="fa fa-file-text"></i></a>
                                                                            <a href="javascript:void(0);"><i class="fa fa-envelope"></i></a>
                                                                        </div>
                                                                    </li>
                                                                </ul>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="panel panel01">
                                                        <div class="panel-heading" role="tab">
                                                            <h4 class="panel-title">
                                                                <a data-toggle="collapse" href="#m02" aria-expanded="true" aria-controls="m02"><i class="fa fa-caret-right"></i>数学组</a>
                                                            </h4>
                                                        </div>
                                                        <div id="m02" class="panel-collapse collapse" role="tabpanel">
                                                            <div class="panel-body">
                                                                <ul class="mail-list">
                                                                    <li>
                                                                        <span>阮小天</span>
                                                                        <div class="mail-list-btns">
                                                                            <a href="javascript:void(0);"><i class="fa fa-file-text"></i></a>
                                                                            <a href="javascript:void(0);"><i class="fa fa-envelope"></i></a>
                                                                        </div>
                                                                    </li>
                                                                </ul>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                            </div>
                                            <div role="tabpanel" class="tab-pane" id="b">公告</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </li>
                    <li>
                        <a class="js-dropbox-code js-dropbox-toggle" href="#">
                            <i class="wpfont icon-qr-code"></i>
                        </a>
                        <div class="dropbox dropbox-code">
                            <div class="dropbox-container text-center">
                                <p>手机扫码下载移动OA</p>
                                <img src="${resourceUrl}/images/qrcode.png" alt="">
                            </div>
                        </div>
                    </li>
                    <li>
                        <a class="js-dropbox-setting js-dropbox-toggle" href="#">
                            <i class="wpfont icon-cog-fill"></i>
                        </a>
                        <div class="dropbox dropbox-setting">
                            <div class="list-group">
                                <a class="list-group-item" href="#">统计图</a>

                                <a class="list-group-item js-common-module-set" href="#">设置常用操作</a>

                                <a class="list-group-item" href="#">备用功能链接二</a>

                                <a class="list-group-item" href="#">备用功能链接三</a>
                            </div>
                        </div>
                            
                    </li>                   
                    <!-- #section:basics/navbar.user_menu -->
                    <li class="navbar-item-user">
                        <a href="#" class="js-dropbox-user js-dropbox-toggle">
                            <img class="nav-user-photo" src="${resourceUrl}/images/user.png" alt="Jason's Photo" />
                            <span class="user-info">阮小天</span>

                            <i class="wpfont icon-caret-down"></i>
                        </a>
                        <div class="dropbox dropbox-user">
                            <div class="drapbox-container">
                                <div class="user-card">
                                    <a class="user-card-img" href="#">
                                        <img src="${resourceUrl}/images/user-photo.png" alt="...">
                                    </a>
                                    <div class="user-card-body">
                                        <a class="user-sign-in" href="javascript:void(0);">签到</a>
                                        <h4 class="user-card-name">阮小天</h4>
                                        <p class="user-login-lastTime">上次登录时间：2016-02-24 14:35</p>
                                    </div>
                                </div>
                                <div class="user-role-list">
                                    <table class="table table-noborder">
                                        <tbody>
                                            <tr>
                                                <td>阮小天ruanxiaort</td>
                                                <td>教师</td>
                                                <td><a href="">切换</a></td>
                                            </tr>
                                            <tr>
                                                <td>ruanxiaort</td>
                                                <td>学生</td>
                                                <td><a href="">切换</a></td>
                                            </tr>
                                            <tr>
                                                <td>阮小天</td>
                                                <td>家长</td>
                                                <td><a href="">切换</a></td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="btn-group btn-group-justified dropbox-user-btns">
                                    <div class="btn-group">
                                        <a class="btn btn-link js-user-setting">个人设置</a>
                                    </div>
                                    <div class="btn-group">
                                        <a class="btn btn-link">退出</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </li>

                    <!-- /section:basics/navbar.user_menu -->
                </ul>

                <!-- /section:basics/navbar.dropdown -->
            </div><!-- /.navbar-container -->
        </div>

        <!-- /section:basics/navbar.layout -->
        <div class="main-container" id="main-container">
            <script type="text/javascript">
                try{ace.settings.check('main-container' , 'fixed')}catch(e){}
            </script>

            <!-- #section:basics/sidebar -->
            <div id="sidebar" class="sidebar                  responsive sidebar-fixed">
                <script type="text/javascript">
                    try{ace.settings.check('sidebar' , 'fixed')}catch(e){}
                </script>
                <div class="sidebar-shortcuts">
                    <button class="btn btn-block btn-tohome">
                        <i class="wpfont icon-home-fill"></i>
                        <span class="btn-tohome-text">主页</span>
                    </button>
                </div>
                <div class="nav-wrap js-nav-wrap">
                    <ul class="nav nav-list">
                        <li class="active">
                            <a href="javascript:loadPage('${request.contextPath}/system/server/appList');" data-show="a">
                                <img class="menu-icon" src="${resourceUrl}/images/icons/app-icon/icon07-sm.png" alt="">
                                <span class="menu-text">应用管理</span>
                            </a>
                                
                        </li>
                        <li class="active">
                            <a href="javascript:loadPage('/system/developer/manage');" data-show="a">
                                <img class="menu-icon" src="${resourceUrl}/images/icons/app-icon/icon07-sm.png" alt="">
                                <span class="menu-text">开发者管理</span>
                            </a>
                        </li>
                    </ul>
                </div>
                <div class="subNav-modal js-subNav-modal">
                    <div class="subNav-modal-container">
                        
                    </div>
                </div>
                <!-- #section:basics/sidebar.layout.minimize -->
                <div class="sidebar-toggle sidebar-collapse js-siderbar-collapse" id="sidebar-collapse">
                    <i class="wpfont icon-angle-double-left"></i>
                </div>

                <!-- /section:basics/sidebar.layout.minimize -->
                <script type="text/javascript">
                    try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
                </script>
            </div>

            <!-- /section:basics/sidebar -->
            <div class="main-content" id="appListDiv">
            </div><!-- /.main-content -->

        </div><!-- /.main-container -->


        <!-- basic scripts -->

        <!--[if !IE]> -->
        <script src="${resourceUrl}/components/jquery/dist/jquery.js"></script>

        <!-- <![endif]-->

        <!--[if IE]>
        <script src="../components/jquery.1x/dist/jquery.js"></script>
        <![endif]-->
        <script type="text/javascript">
            if('ontouchstart' in document.documentElement) document.write("<script src='${resourceUrl}/components/_mod/jquery.mobile.custom/jquery.mobile.custom.js'>"+"<"+"/script>");
        </script>
        <script src="${resourceUrl}/components/bootstrap/dist/js/bootstrap.js"></script>

        <!-- page specific plugin scripts -->
        <script src="${resourceUrl}/components/layer/layer.js"></script>
        <script src="${resourceUrl}/components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
        <script src="${resourceUrl}/components/chosen/chosen.jquery.min.js"></script>
        <script src="${resourceUrl}/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
        <script src="${resourceUrl}/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
        <script src="${resourceUrl}/components/bootstrap-daterangepicker/moment.min.js"></script>
        <script src="${resourceUrl}/components/bootstrap-daterangepicker/daterangepicker.js"></script>

        <!-- ace scripts -->
        <script src="${resourceUrl}/assets/js/src/ace.ajax-content.js"></script>        <!-- 异步加载js -->

        <!-- inline scripts related to this page -->
        <script src="${resourceUrl}/openapi/js/myscript.js"></script>
        <script src="${resourceUrl}/js/tool.js"></script>
        <script src="${resourceUrl}/js/jquery.form.js"></script>
        <script>
            function loadPage(url){
              $(".main-content").load(url);
            }
            
            function loadPageParam(url,param){
              $(".main-content").load(url,param);
            }
        </script>
    </body>
</html>
