<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta charset="utf-8" />
<title>运维-登录</title>

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
  <div class="login-box login-box-super box-default">
    <form id="loginForm">
      <fieldset>
        <h3 class="login-title login-title-super">
          <span>万朋平台配置助手</span>
        </h3>
        <h4 class="login-title-tips">
          <p>
            <span>用户登录</span>
          </p>
        </h4>
        <p class="login-tips"></p>

        <div class="login-form">
          <label for="" class="sr-only">用户名</label>
          <div class="input-group">
            <span class="input-group-addon"> <i class="fa fa-user"></i>
            </span> <input id="username" name="username" type="text" class="input-lg form-control" placeholder="请输入用户名">
          </div>
        </div>

        <div class="login-form">
          <label for="" class="sr-only">密码</label>
          <div class="input-group">
            <span class="input-group-addon"> <span class="fa fa-lock"></span>
            </span> <input id="password" name="password" type="password" class="input-lg form-control" placeholder="请输入密码">
          </div>
        </div>

        <div class="login-form">
          <button id="loginBtn" type="button" class="btn btn-block btn-lg btn-blue">登录</button>
        </div>
      </fieldset>
    </form>
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

  <!-- page specific plugin scripts -->
  <script src="/static/components/layer/layer.js"></script>

  <!-- inline scripts related to this page -->
  <script>
      $(function() {
        function winLoad() {
          var window_h = $(window).height();
          var login_h = $('.login-box-super').outerHeight();
          var login_top = parseInt((window_h - login_h) / 2);
          $('.login-box-super').css('margin-top', login_top);
        }
        ;
        winLoad();
        $(window).resize(function() {
          winLoad();
        });
      })

      $('input').keyup(function(event) {
        console.log(event.keyCode);
        if (event.keyCode == 13) {
          $('#loginBtn').trigger("click");
        }
      });

      $('#loginBtn').click(function() {
        $('.login-tips').empty();
        if ($('#username').val() == '') {
          $('.login-tips').text('请输入超管账号');
          $('#username').focus();
          return;
        } else if ($('#password').val() == '') {
          $('.login-tips').text('请输入密码');
          $('#password').focus();
          return;
        }
        $.post({
          url : '/system/ops/verify',
          data : $('#loginForm').serialize(),
          success : function(msg) {
            var jsonO = JSON.parse(msg);
            if (jsonO.success) {
              //跳转
                window.location.href = '${request.contextPath}/system/ops/index?code='+jsonO.code;
                return ;
              if (jsonO.code == '1') {
                window.location.href = '${request.contextPath}/system/ops/sysOption';
              } else {
                window.location.href = '${request.contextPath}/system/ops/resetPassword';
              }
            } else {
              $('.login-tips').text(jsonO.msg);
            }
          }
        });
      });
    </script>
</body>
</html>
