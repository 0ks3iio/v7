<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>应用管理平台</title>

		<meta name="description" content="" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

		<!-- bootstrap & fontawesome -->
		<link rel="stylesheet" href="${resourceUrl}/components/bootstrap/dist/css/bootstrap.css" />
		<link rel="stylesheet" href="${resourceUrl}/components/font-awesome/css/font-awesome.css" />

		<!-- page specific plugin styles -->
		<link rel="stylesheet" href="${resourceUrl}/components/layer/skin/layer.css">
		<link rel="stylesheet" href="${resourceUrl}/components/chosen/chosen.min.css">
		<link rel="stylesheet" href="${resourceUrl}/components/bootstrap-datepicker/dist/css/bootstrap-datepicker3.css">

		
		<link rel="stylesheet" href="${resourceUrl}/css/iconfont.css">
		<link rel="stylesheet" href="${resourceUrl}/css/components.css">
		<link rel="stylesheet" href="${resourceUrl}/css/page-desktop.css">
       <link rel="stylesheet" href="${resourceUrl}/css/pages.css">
		

		<!-- ace settings handler -->
		<script src="/static/assets/js/ace-extra.js"></script>
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
		<!-- #section:basics/navbar.layout -->
		<div id="navbar" class="navbar navbar-default navbar-fixed-top">
			<script type="text/javascript">
				try{ace.settings.check('navbar' , 'fixed')}catch(e){}
			</script>

			<div class="navbar-container clearfix" id="navbar-container">
				
				<div class="navbar-header">
					<!-- #section:basics/navbar.layout.brand -->
					<a href="#" class="navbar-brand">
						<!-- <img class="logo" src="/static/images/desktop/logo.png" alt=""> -->
						<span>应用管理配置助手</span>
					</a>

					<!-- /section:basics/navbar.layout.brand -->
				</div>
				<!-- #section:basics/navbar.dropdown -->
				
				<ul class="nav navbar-nav navbar-right">
					<!-- #section:basics/navbar.user_menu -->
					<li class="navbar-item-user">
						<a href="#" class="" data-toggle="dropdown">
							<img class="nav-user-photo" src="${resourceUrl}/images/desktop/user-default.png" alt="超管" />
							<span class="user-info">超级管理员</span>

							<i class="wpfont icon-caret-down"></i>
						</a>
                        <div class="dropdown-menu dropdown-menu-user">
              <ul class="list-group">
                <li class="list-group-item text-center" href="#"><a href="${request.contextPath}/superAdmin/logout">退出</a></a>
              </ul>
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
				<div class="nav-wrap js-nav-wrap">
					<div class="common-module">
						<ul class="nav nav-list">
							<li>
								<a id="developerManage" href="javascript:void(0);" onclick="load('${request.contextPath}/system/developer/index');">
									<img class="menu-icon" src="${resourceUrl}/images/icons/app-icon/icon01-sm.png" alt="">
									<span class="menu-text">开发者管理</span>
								</a>
							</li>
							<li>
								<a id="appManage" href="javascript:void(0);" onclick="load('${request.contextPath}/system/server/index');">
									<img class="menu-icon" src="${resourceUrl}/images/icons/app-icon/icon02-sm.png" alt="">
									<span class="menu-text">应用管理</span>
								</a>
							</li>
                           <li>
                                <a id="serverAuthorize" href="javascript:void(0);" onclick="load('${request.contextPath}/system/serverAuthorize/index');">
                                    <img class="menu-icon" src="${resourceUrl}/images/icons/app-icon/icon03-sm.png" alt="">
                                    <span class="menu-text">应用授权</span>
                                </a>
                            </li>
							<li>
                                <a id="problem" href="javascript:void(0);" onclick="load('${request.contextPath}/system/problem/index');">
                                    <img class="menu-icon" src="${resourceUrl}/images/icons/app-icon/icon06-sm.png" alt="">
                                    <span class="menu-text">常见问题</span>
                                </a>
                            </li>

						</ul>
					</div>
				</div>
				<script type="text/javascript">
					try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
				</script>
			</div>

			<!-- /section:basics/sidebar -->
			<div class="main-content">
				<div class="main-content-inner">
				</div>
			</div><!-- /.main-content -->

			<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
				<i class="fa fa-angle-double-up icon-only bigger-110"></i>
			</a>
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

		<!-- page specific plugin scripts -->
		<script src="${resourceUrl}/components/layer/layer.js"></script>
		<script src="${resourceUrl}/components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
		<script src="${resourceUrl}/components/chosen/chosen.jquery.min.js"></script>
		<script src="${resourceUrl}/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
		<script src="${resourceUrl}/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>

		<!-- ace scripts -->
		<script src="${resourceUrl}/assets/js/src/ace.ajax-content.js"></script>  		<!-- 异步加载js -->

		<!-- inline scripts related to this page -->
		<script src="${resourceUrl}/js/desktop.js"></script>
        <script src="${resourceUrl}/js/tool.js"></script>
        <script src="${resourceUrl}/js/jquery.form.js"></script>
		<script>
            $(function(){
              $('#developerManage').click();
            });
	  
			function load(url){
		  	  $('.main-content-inner').empty();
		  	  $('.main-content-inner').load(url);
			}
		</script>
	</body>
</html>
