<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>电子班牌</title>
    <link rel="stylesheet" href="css/base.css">
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="css/page.css">
    <script src="js/jquery.js"></script>
</head>
<body>
<div class="wrap">
    <div class="topWrap">
        <div class="top">
            <div class="nav fl">
                <a class="logo" href="http://www.wanpeng.com/" target="_blank"><i class="ico"></i>万朋教育</a>
                <a href="http://xk.msyk.cn/operation/website/index" style="color: #3a7deb;">新高考</a>
                <a href="http://www.msyk.cn/" target="_blank">美师优课</a>
                <a href="https://www.kehou.com/index.htm" target="_blank">课后网</a>
                <a href="http://edu.wanpeng.com/" target="_blank">三通两平台</a>
                <a href="http://wk.wanpeng.com/" target="_blank">微课掌上通</a>
                <a href="http://www.wanpeng.com/cpzx-tbkt.html" target="_blank">同步课堂</a>
            </div>
        </div>
    </div>
    <div class="headerWrap">
        <div class="header">
            <a class="logo" href="${request.contextPath}/operation/website/index"><img src="images/img/logo.png"></a>
            <div class="nav">
                <a href="${request.contextPath}/operation/website/index">首页</a>
                <a href="${request.contextPath}/operation/website/educationAdminPage">教务管理</a>
                <a href="${request.contextPath}/operation/website/bigdataPage">大数据分析</a>
                <a href="${request.contextPath}/operation/website/mixedAbilityPage">综合素质</a>
                <a href="${request.contextPath}/operation/website/careerPlanPage">生涯规划</a>
                <a class="active" href="${request.contextPath}/operation/website/classCardPage">电子班牌</a>
            </div>
            <div class="lR">
                <a class="btn btn03" href="${request.contextPath}/operation/website/trialUserRegister">申请试用</a>
                <a class="btn btn02" id="login" href="${request.contextPath}/operation/website/loginForOperation">登录</a>
            </div>
            <div class="admin">
                <a class="name" href="#" id="realNameView">${realName}</a>
                <div id="linkto">
                	<ul>
                		<li>
                			<a href="${request.contextPath}/desktop/index/page" target="_blank">智慧校园</a>
                		</li>
	                    <li>
                			<a href="http://www.msyk.cn/" target="_blank">智慧课堂</a>
                		</li>
                	</ul>
                	<a class="btn btn04" href="${request.contextPath}/homepage/logout/page?call=${call!}">退出</a>
                </div>

            </div>
        </div>
    </div>
    <div class="centerWrap">
    	<div class="bg1">
    		<div class="center">
	            <div class="subpublicModel">
	            	<p class="title">电子班牌</p>
	            	<p class="subtitle">班牌可应用在多种不同的场景用途下，功能根据场景的不同而不同，可分：教室、校门、寝室等。</p>
	            	<p class="tc"><img src="images/img/electronics.png" /></p>
	            </div>
	        </div>
    	</div>
        <div class="center">
        	<p class="f30 tc c-000 mt80">使用场景</p>
        	<ul class="electronicslist clearfix">
        		<li>
        			<img src="images/img/electronics1.jpg" />
    				<p class="title">校园门口</p>
        		</li>
        		<li>
        			<img src="images/img/electronics2.jpg" />
    				<p class="title">教室门口</p>
        		</li>
        		<li>
        			<img src="images/img/electronics3.jpg" />
    				<p class="title">寝室门口</p>
        		</li>
        	</ul>
        </div>
    	<div style="border-top: 1px solid #e8e9f3;"></div>
    	<div class="animate-part electronics-part1 mt80">
        	<div class="center">
        		<div class="info fl">
                    <h3 class="hd">上课考勤</h3>
                    <p class="p1">按照走班课表进行在不同的教室中上课考勤，任课教师可直接查看学生签到名单，支持发送消息通知班主任，轻松掌握学生上课情况。</p>
                </div> 
                <div class="animate fr"></div>
        	</div>    
        </div>
        <div class="animate-part electronics-part2">
        	<div class="center">
        		<div class="info fr">
                    <h3 class="hd">通知公告</h3>
                    <p class="p1">可在任何一块班牌上进行发布通知公告；可支持多种形式展示：全屏锁定展示、顶部滚动展示、首页通知列表展示。</p>
                </div> 
                <div class="animate fl"></div>
        	</div>    
        </div>
        <div class="animate-part electronics-part3">
        	<div class="center">
        		<div class="info fl">
                    <h3 class="hd">班牌全屏信息展示</h3>
                    <p class="p1">班牌可以进行发布全屏信息，包括公告、多媒体内容（相册、视频、PPT），在相应时间段内全屏展示播放，可支持锁定界面防止学生操作。</p>
                </div> 
                <div class="animate fr"></div>
        	</div>    
        </div>
        <div class="animate-part electronics-part4">
        	<div class="center">
        		<div class="info fr">
                    <h3 class="hd">个人中心</h3>
                    <p class="p1">学生刷卡登录后可查询个人的课表、个人获得的荣誉信息，以及可以申请请假并查看请假审核的状态。</p>
                </div> 
                <div class="animate fl"></div>
        	</div>    
        </div>
        <div class="animate-part electronics-part5">
        	<div class="center">
        		<div class="info fl">
                    <h3 class="hd">请假</h3>
                    <p class="p1">学生可刷卡登录班牌个人空间进行申请请假，老师审批通过后可刷校门班牌出校。</p>
                </div> 
                <div class="animate fr"></div>
        	</div>    
        </div>
    </div>
    <div class="footerWrap">
		<div class="footer">
			<div class="footer-inner clearfix">
				<div class="footer-info-logo fl">
					<img src="images/img/footer-logo2018.png" alt="">
				</div>
				<div class="footer-info fl">
					<h3 class="title">旗下网站</h3>
					<div class="infos-main">
						<a href="https://www.kehou.com/index.htm">课后网</a>
						<a href="http://pad.msyk.cn/">美师优课</a>
						<a href="http://edu.wanpeng.com/index.html">三通两平台</a>
						<a href="http://wk.wanpeng.com/">微课掌上通</a>
						<a href="http://xgk.wanpeng.com/cpzx-zhykt.html">智慧云课堂</a>
					</div>
				</div>
				<div class="footer-info footer-certified fl">
					<h3 class="title">公司认证</h3>
					<div class="infos-main">
						<p>通过CMMI L5国际软件认证</p>
						<p>通过ISO 9001国际质量认证</p>
						<p>通过ISO27001信息安全认证</p>
					</div>
				</div>
				<div class="footer-info footer-contact fl">
					<h3 class="title">联系电话</h3>
                    <div class="infos-main">
                        <p>400-863-2003（教育云平台和其他产品）</p>
                        <p>400-617-1997（课后网）</p>
                    </div>
				</div>
				<div class="aboutus fr">
					<h3 class="title">关于我们</h3>
					<div class="aboutus-main">
						<img src="images/img/code04.jpg" alt="">
						<p>官方微信</p>
					</div>
				</div>
			</div>
			<div class="footer-content">
				<p>Copyright ©2013-2018   浙江万朋教育科技股份有限公司版权所有   备案号:浙B2-20100206-17</p>
			</div>
		</div>
	</div>
	<script src="js/jquery.js" type="text/javascript"></script>
    <script>
     	$(window).scroll(function(){
		  	if($(document).scrollTop()>=200){
		    	$(".electronics-part1 .animate").addClass('animate-pop-up'); 
		  	}
		  	if($(document).scrollTop()>=613){
		    	$(".electronics-part2 .animate").addClass('animate-pop-up'); 
		  	}
		  	if($(document).scrollTop()>=1080){
		    	$(".electronics-part3 .animate").addClass('animate-pop-up'); 
		  	}
		  	if($(document).scrollTop()>=1500){
		    	$(".electronics-part4 .animate").addClass('animate-pop-up'); 
		  	}
		  	if($(document).scrollTop()>=1900){
		    	$(".electronics-part5 .animate").addClass('animate-pop-up'); 
		  	}
		});
    </script>
</div>
</body>
<script>
	$(function() {
		if($("#realNameView").html()=="未登录"){
            $("#realNameView").hide();
			$("#linkto").hide();
		}else{
			$("#login").hide();
		}
		
	})
</script>
</html>