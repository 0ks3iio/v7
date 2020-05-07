<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta charset="UTF-8">
	<title>大数据开发者中心</title>
	<meta name="description" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <link rel="stylesheet" type="text/css" href="${springMacroRequestContext.contextPath}/bigdata/v3/static/css/bootstrap.css"/>
    <link rel="stylesheet" href="${springMacroRequestContext.contextPath}/bigdata/v3/static/font-awsome/css/font-awesome.css"/>
    <link rel="stylesheet" type="text/css" href="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/zTree/css/zTreeStyle.css"/>
    <link rel="stylesheet" type="text/css" href="${springMacroRequestContext.contextPath}/bigdata/v3/static/css/iconfont.css"/>
    <link rel="stylesheet" type="text/css" href="${springMacroRequestContext.contextPath}/bigdata/v3/static/fonts/iconfont.css"/>
    <link rel="stylesheet" type="text/css" href="${springMacroRequestContext.contextPath}/bigdata/v3/static/css/all.css"/>
	<link rel="stylesheet" type="text/css" href="${springMacroRequestContext.contextPath}/bigdata/v3/static/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="${springMacroRequestContext.contextPath}/bigdata/v3/static/css/page.css"/>
	<script>
        _contextPath = "${springMacroRequestContext.contextPath}";
    </script>
</head>
<body>
    <!--头部 S-->
    <div class="center-head ">
        <span class="page-name">大数据开发者中心</span>
        <#if isLogin?default(false)>
            <span class="register-word">${developer.username!}
                 <a href="${request.contextPath}/bigdata/api/logout/page">退出</a>
            </span>
        <#else>
            <span class="register-word"><a href="javascript:void(0)" onclick="openWin('${request.contextPath}/bigdata/api/regist/page')">开发者注册</a></span>
        </#if>
    </div><!--头部 E-->
    
    <!--主体 S-->
    <div class="developer-center">
        
        <div class="foreword-content-wrap">
            <div class="foreword-content">
                <div class="foreword-word">
                    <div class="">
                        <h3>互联互通 服务教育</h3>
                        <p>为各业务系统提供标准的基础数据，兼容各类第三方数据库 打破数据孤岛，实现数据互通</p>
                        <#if !isLogin?default(false)>
                             <div class="btn-made"><a href="javascript:void(0)" onclick="openWin('${request.contextPath}/bigdata/api/regist/page')">注册加入</a></div>
                        </#if>
                    </div>
                </div>
                <#if !isLogin?default(false)>
	                <div class="register-form">
	                    <h3 class="register-form-name">开发者登录</h3>
	                    <div class="error-box" style="display:none"><i class="wpfont icon-close-round-fill"></i> <i id ="error-msg"> </i> <i class="wpfont icon-close"></i></div>
	                    <input class="form-control" type="text" name="text" id="uid" placeholder="请输入用户名"/>
	                    <input class="form-control" type="password" name="password" id="pwd" placeholder="请输入密码"/>
	                    <button type="button" class="btn btn-blue btn-lg btn-block" id = "btn-login" >登录</button>
	                </div>
	             </#if>
            </div>
        </div>
        
        <div class="vice-module-wrap-block">
            <div class="vice-module-wrap clearfix">
                <div class="vice-module">
                    <img src="${springMacroRequestContext.contextPath}/bigdata/v3/static/images/base/icon-fast.png" >
                    <div class="wrap-full">
                        <div class="">
                            <b>快速接入</b>
                            <p class="mt-5">可视化调试数据对接及单点登录对接</p>
                        </div>
                    </div>
                </div>
                <div class="vice-module">
                    <img src="${springMacroRequestContext.contextPath}/bigdata/v3/static/images/base/icon-save.png" >
                    <div class="wrap-full">
                        <div class="">
                            <b>安全共享</b>
                            <p class="mt-5">监控数据读取，保障数据安全</p>
                        </div>
                    </div>
                </div>
                <div class="vice-module">
                    <img src="${springMacroRequestContext.contextPath}/bigdata/v3/static/images/base/icon-integration.png" >
                    <div class="wrap-full">
                        <div class="">
                            <b>易于集成</b>
                            <p class="mt-5">支持各块角色，覆盖各种订阅授权方式</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="developer-center-content-wrap">
            <h1 class="text-center">开发接入流程</h1>
            <div class="developer-center-content clearfix">
                <div class="developer-content-box">
                    <h3>01 开发者注册</h3>
                    <div class="developer-box-body">
                        <h4 class="mb-10">开发者注册</h4>
                        <p>申请成为开发者，维护企业信息完善联系方式，同意平台服务协议</p>
                        <div class="btn-made"><a href="javascript:void(0);" onclick="showDoc
                        ('${request.contextPath}/bigdata/api/developer/page','1');">了解详情</a></div>
                    </div>
                </div>
                <div class="developer-content-box">
                    <h3>04 对接数据</h3>
                    <div class="developer-box-body">
                        <h4 class="mb-10">对接数据</h4>
                        <p>申请基础数据接口，调试读取基本数据</p>
                        <div class="btn-made"><a href="javascript:void(0);" onclick="showDoc
                        ('${request.contextPath}/bigdata/api/apply/index','2','jiekouapi');">了解详情</a></div>
                    </div>
                </div>
                <div class="developer-content-box">
                    <h3>02 了解开发指南</h3>
                    <div class="developer-box-body">
                        <h4 class="mb-10">开发指南</h4>
                        <p>前往查看开发文档，了解平台接入开发细节内容</p>
                        <div class="btn-made"><a href="javascript:void(0);">了解详情</a></div>
                    </div>
                </div>
                <div class="developer-content-box">
                    <h3>05 应用上线</h3>
                    <div class="developer-box-body">
                        <h4 class="mb-10">应用上线</h4>
                        <p>调试测试完毕后联系应用上线</p>
                        <div class="btn-made"><a href="javascript:void(0);" id ="appOnLine" onclick="showDoc
                        ('${request.contextPath}/bigdata/v3#','1');">了解详情</a></div>
                    </div>
                </div>
                <div class="developer-content-box">
                    <h3>03 创建应用</h3>
                    <div class="developer-box-body">
                        <h4 class="mb-10">创建应用</h4>
                        <p>维护应用信息，提交审核</p>
                        <div class="btn-made"><a href="javascript:void(0);"onclick="showDoc
                        ('${request.contextPath}/bigdata/api/apply/index','2','yingyongguanli');">了解详情</a></div>
                    </div>
                </div>
            </div>
            
        </div>
        
	</div><!--主体 E-->
    
    <!--尾部 S-->
    <div class="center-foot">
        <span>Copyright ©2013-2017 &nbsp; 浙江万朋教育科技股份有限公司版权所有 &nbsp; 备案号:浙ICP备05070430号 </span>
    </div><!--尾部 E-->
	
	<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/jquery/jquery.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/bootstrap/bootstrap.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/fonts/iconfont.js" async="async" defer="defer" type="text/javascript" charset="utf-8"></script>
	<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/plugs/chosen/chosen.jquery.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/layer/layer.js" async="async" defer="defer" type="text/javascript" charset="utf-8"></script>
	<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/zTree/js/jquery.ztree.all.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/jquery/jquery-toast.js" type="text/javascript" charset="utf-8"></script>
	<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/laydate/laydate.min.js" defer="defer" type="text/javascript" charset="utf-8"></script>
    <script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/plugs/webuploader/webuploader.custom.js" type="text/javascript" charset="utf-8"></script>
    <script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/jquery/jquery-tabs.js" type="text/javascript" charset="utf-8"></script>
	<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/assets/js/myscript.js" type="text/javascript" charset="utf-8"></script>
	<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/myscript.js" type="text/javascript" charset="utf-8"></script>
	<script src="${springMacroRequestContext.contextPath}/bigdata/v3/static/js/tool.js" type="text/javascript" charset="utf-8"></script>

	<script type="text/javascript">
		$(function(){
            $('body').on('click','.icon-close',function(){
                $('.error-box').hide();
            })    
            $('#btn-login').on('click',login);
		})
		function showDoc (url, param, type){
			if(param == '2'){
				<#if !isLogin?default(false)>
				    showConfirmTips('prompt',"提示","请先进行登陆！！！")
				    return;
				</#if>
			}
			url = url + "?type=" + type;
	        openWin(url);
		}
		function login(){
			$('.error-box').hide();
            var username=$('#uid').val();
            if($.trim(username)==''){
              $('#error-msg').text('请输入用户名');
              $('.error-box').show();
              return;
            };
            var pwd=$('#pwd').val();
            if($.trim(pwd)==''){
              $('#error-msg').text('请输入密码');
              $('.error-box').show();
              return;
            };
            // error-msg
            $.post('${request.contextPath}/bigdata/api/login/page',{'uid':username,'pwd':pwd},function(data){
              if(data.success){
                $('#btn-login').text(data.msg);
                location.href='${request.contextPath}/bigdata/api/index';
              }else{
                 $('#error-msg').text(data.message);
                 $('.error-box').show();
              }
              return;
            });
          }
		
		function openWin(url) {
	      $('body').append($('<a href="'+url+'" target="_blank" id="openWin"></a>'))
	      document.getElementById("openWin").click();//点击事件
	      $('#openWin').remove();
		}
		
	</script>	
</body>
</html>