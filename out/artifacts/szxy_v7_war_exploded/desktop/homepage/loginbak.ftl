<---><!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>数字校园-登录页</title>

    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="${request.contextPath}/desktop/components/bootstrap/dist/css/bootstrap.css" />
    <link rel="stylesheet" href="${request.contextPath}/desktop/components/font-awesome/css/font-awesome.css" />
    <link rel="stylesheet" href="${request.contextPath}/desktop/css/iconfont.css">

    <!-- page specific plugin styles -->
    <link rel="stylesheet" href="${request.contextPath}/desktop/components/slick/slick.css">


    <link rel="stylesheet" href="${request.contextPath}/desktop/css/page-desktop.css">
    <!--[if lte IE 9]>
    <link rel="stylesheet" href="${request.contextPath}/desktop/css/pages-ie.css" />
    <![endif]-->
    <!-- inline styles related to this page -->


    <!-- ace settings handler -->
    <script src="${request.contextPath}/desktop/assets/js/ace-extra.js"></script>

    <!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

    <!--[if lte IE 8]>
    <script src="${request.contextPath}/desktop/components/html5shiv/dist/html5shiv.min.js"></script>
    <script src="${request.contextPath}/desktop/components/respond/dest/respond.min.js"></script>
    <![endif]-->
</head>

<body class="no-skin">
<!-- #section:basics/navbar.layout -->
<div id="navbar" class="navbar">
    <script type="text/javascript">
        try{ace.settings.check('navbar' , 'fixed')}catch(e){}
    </script>

    <div class="navbar-container clearfix" id="navbar-container">
        <div class="container">
            <div class="navbar-header">
                <!-- #section:basics/navbar.layout.brand -->
                <a href="#" class="navbar-brand">
                    <img class="logo" src="${request.contextPath}/desktop/images/logo-login.png" alt="">
                    <span>浙江万朋教育 · 数字校园</span>
                </a>

                <!-- /section:basics/navbar.layout.brand -->
            </div>
        </div>
    </div><!-- /.navbar-container -->
</div>
<div>
    <div class="container pos-rel">
        <div class="login-box">
                <fieldset>
                    <h3 class="login-title">用户登录</h3>
                    <p class="login-tips"></p>

                    <div class="login-form">
                        <label for="" class="sr-only">用户名</label>
                        <div class="input-group">
									<span class="input-group-addon">
										<i class="fa fa-user"></i>
									</span>
                            <input type="text" id="userName" class="input-lg form-control" placeholder="请输入用户名">
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

                    <div class="login-form clearfix">
                        <label for="" class="sr-only">验证码</label>
                        <input type="text" id="verifyCode" class="login-verCode input-lg form-control" placeholder="请输入验证码">
                        <a href=""><img src="${request.contextPath}/desktop/images/ver-code.png" alt=""></a>
                    </div>

                    <div class="login-form clearfix">
                        <label class="inline">
                            <input type="checkbox" class="wp" />
                            <span class="lbl"> 记住密码</span>
                        </label>
                        <a class="pull-right" href="">忘记密码</a>
                    </div>

                    <button class="btn btn-block btn-lg btn-blue" id="loginBtn">登&emsp;录</button>
                </fieldset>
        </div>
    </div>
    <div class="slide login-slide">
        <a href=""><img src="${request.contextPath}/desktop/images/banner01.png" alt=""></a>
    </div>
</div>
<div class="content">
    <div class="container">
        <ul class="download-list clearfix">
            <li>
                <a class="download-item" href="">
							<span class="download-img">
								<img src="${request.contextPath}/desktop/images/icons/icon-video.png" alt="">
							</span>
                    <h5 class="download-name">公文管理操作视频</h5>
                </a>
            </li>
            <li>
                <a class="download-item" href="">
							<span class="download-img">
								<img src="${request.contextPath}/desktop/images/icons/icon-office.png" alt="">
							</span>
                    <h5 class="download-name">office2007下载</h5>
                </a>
            </li>
            <li>
                <a class="download-item" href="">
							<span class="download-img">
								<img src="${request.contextPath}/desktop/images/icons/icon-wps.png" alt="">
							</span>
                    <h5 class="download-name">WPS下载</h5>
                </a>
            </li>
            <li>
                <a class="download-item" href="">
							<span class="download-img">
								<img src="${request.contextPath}/desktop/images/icons/icon-pdf.png" alt="">
							</span>
                    <h5 class="download-name">pdf阅读器下载</h5>
                </a>
            </li>
            <li>
                <a class="download-item" href="">
							<span class="download-img">
								<img src="${request.contextPath}/desktop/images/icons/icon-wps.png" alt="">
							</span>
                    <h5 class="download-name">WPS下载</h5>
                </a>
            </li>
        </ul>
        <div class="row">
            <div class="col-sm-7">
                <div class="info">
                    <h2 class="info-title">学校介绍</h2>
                    <div class="info-body">
                        <p class="text-indent2">万朋，浙江省著名商标，成立于2003年6月25日，中国互联网教育行业领军企业，中国智慧教育领域杰出品牌，依托领先的技术优势和持续的创新精神，提供三通两平台、课后网校、美师优课、微课掌上通、云课堂等教育云平台解决方案，引领中国智慧教育的发展。万朋，让孩子卓越闯天下，让老师桃李满天下！</p>
                    </div>
                </div>
            </div>
            <div class="col-sm-4 col-sm-offset-1">
                <div class="info">
                    <h2 class="info-title">二维码关注下载</h2>
                    <div class="info-body">
                        <div class="media">
                            <a class="media-left" href="#">
                                <img src="${request.contextPath}/desktop/images/qrcode.png" alt="${request.contextPath}/desktop">
                            </a>
                            <div class="media-body">
                                <h4 class="media-heading">扫一扫OA下载</h4>
                                <p>全新移动端OA系统欢迎您的使用</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="footer">
    <div class="container">
        <div class="footer-body">
            <p>浙江万朋教育科技股份有限公司 提供技术支持 Copyright 2013-2015,万朋版权所有</p>
        </div>
    </div>
</div>
<!-- basic scripts -->

<!--[if !IE]> -->
<script src="${request.contextPath}/desktop/components/jquery/dist/jquery.js"></script>

<!-- <![endif]-->

<!--[if IE]>
<script src="${request.contextPath}/desktop/components/jquery.1x/dist/jquery.js"></script>
<![endif]-->
<script type="text/javascript">
    if('ontouchstart' in document.documentElement) document.write("<script src='${request.contextPath}/desktop/components/_mod/jquery.mobile.custom/jquery.mobile.custom.js'>"+"<"+"/script>");
</script>
<script src="${request.contextPath}/desktop/components/bootstrap/dist/js/bootstrap.js"></script>

<!-- page specific plugin scripts -->
<script src="${request.contextPath}/desktop/components/slick/slick.min.js"></script>

<!-- ace scripts -->
<script src="${request.contextPath}/desktop/assets/js/src/ace.ajax-content.js"></script>  		<!-- 异步加载js -->

<!-- inline scripts related to this page -->
<script>
    $(document).ready(function(){
        var $userName = $("#userName");
        var $password = $("#password");
        var $verifyCode = $("#verifyCode");
        $('.slide').slick({
            dots: true,
            slidesToShow: 1,
            slidesToScroll: 1,
            prevArrow:false,
            nextArrow:false,
            mobileFirst:true,
            autoplay:true
        });

//        $("#userName").addEventListener("oninput",function(){
//           clearMsg();
//        });
//        $("#userName").on("onpropertychange",function(){
//            clearMsg();
//        });


        $("#loginBtn").unbind().on("click",function(){
            var userName = $userName.val();
            var password = $("#password").val();
            var verifyCode = $("#verifyCode").val();
            if(isBlank(userName)){
                showError("请输入用户名");
                $("#userName").focus();
                return ;
            }
            if(isBlank(password)){
                showError("请输入密码");
                $("#password").focus();
                return ;
            }
            if(isBlank(verifyCode)){
                showError("请输入验证码！");
                $("#verifyCode").focus();
                return ;
            }
            var params = new Object();
            params.username = userName;
            params.password = password;
            params.verifyCode = verifyCode;
            $.ajax({
                url:'${request.contextPath}/homepage/loginUser/page',
                data:$.param(params),
                type:'post',
                success:function(data){
                    var jsonO = JSON.parse(data);
                    if(jsonO.success){
                        location.href = "${request.contextPath}/desktop/index/page";
                    }else{
                        showError(data.msg);
                    }
                },
                error : function(XMLHttpRequest, textStatus, errorThrown) {
                    $("#loginCommit").attr("disabled", "false");
                    var text = syncText(XMLHttpRequest);
                    swal({title: "操作失败!",text: text, type:"error",showConfirmButton: true});
                }
            });
        })

        function showError(msg) {
            $(".login-tips").html(msg);
        }
        function clearMsg() {
            $(".login-tips").html("");
        }
        function isBlank(str){
            return !str || str.replace(/(^s*)|(s*$)/g, "").length ==0;
        }
    });


</script>
</body>
</html>
