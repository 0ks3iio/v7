<div class="header clearfix">
    <div class="navbar-header">
        <a href="${request.contextPath}/developer/index" class="navbar-brand">
            <img class="logo" src="${logoUrl!}" alt="">
            <span>${logoName!}</span>
        </a>
    </div>
    <ul class="navbar-nav">
        <li class="active">
            <a href="${request.contextPath}/developer/index">首页</a>
        </li>
        <li>
            <a href="javascript:void(0);" id="manageCenter">控制台</a>
        </li>
        <li>
            <a href="${request.contextPath}/developer/devDoc">开发文档</a>
        </li>
        <li>
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
          <img class="nav-user-photo" src="/static/images/desktop/user-male.png"  />
          <span>${name! }</span>
          <a href="${request.contextPath}/developer/logout">退出</a>
      </div>
    </#if>
</div>

 <div class="login-box layer layer-login">
    <form>
        <fieldset>
            <h3 class="login-title">
                <img class="logo" src="${logoUrl!}" alt="">
                <span>${logoName!}</span>
            </h3>
            <h4 class="login-title-tips">
                <p><span>使用用户名登录</span></p>
            </h4>
            <p id="error-msg" class="login-tips" style="display:none">用户名错了，再想想！</p>

            <div class="login-form">
                <label for="" class="sr-only">用户名</label>
                <div class="input-group">
                    <span class="input-group-addon">
                        <i class="fa fa-user"></i>
                    </span>
                    <input type="text" id="username" class="input-lg form-control" placeholder="请输入用户名">
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
                <input type="button" id="login" class="btn btn-block btn-lg btn-blue" value="立即登录">
            </div>
                
            <div class="login-form clearfix">
                <span class="pull-right">还没有账号？<a href="${request.contextPath}/developer/regist">立即注册</a></span>
            </div>
        </fieldset>
    </form>
</div>

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

<script>
    $(function(){
      $('.js-login').on('click',showLoginDiv);
      $('#login').on('click',login);
      $('#manageCenter').on('click',manageCenter);
    });
    
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
    
    function manageCenter(isNewOpen){
      var islogin = ${islogin!};
      if(null == islogin||islogin == 0){
        $('.js-login').click();
        return;
      }
      if(isNewOpen&&isNewOpen == 1){
        window.open('${request.contextPath}/developer/home','_blank');
        return;
      }
      window.location.href="${request.contextPath}/developer/home";
    }
    
</script>
