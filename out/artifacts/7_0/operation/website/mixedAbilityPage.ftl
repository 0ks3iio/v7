<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>综合素质</title>
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
                <a class="active" href="${request.contextPath}/operation/website/mixedAbilityPage">综合素质</a>
                <a href="${request.contextPath}/operation/website/careerPlanPage">生涯规划</a>
                <a href="${request.contextPath}/operation/website/classCardPage">电子班牌</a>
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
	            	<p class="title">综合素质</p>
	            	<p class="subtitle">对各个学生的学期成绩、学考成绩、德育成绩、美育成绩进行综合评价，并累加各个维度的数据量化计算出各个学生的综合素质分用于评价学生在校期间的综合表现。</p>
	            	<p class="tc"><img src="images/img/quality.png" /></p>
	            </div>
	        </div>
    	</div>
        <div class="center">
        	<ul class="qualitylist clearfix">
        		<li>
        			<img src="images/img/quality1.png" />
    				<p class="title">学期成绩</p>
    				<p class="tip">每个学期各科成绩按照排名名次段进行转化为相应的综合素质分。</p>
        		</li>
        		<li>
        			<img src="images/img/quality2.png" />
    				<p class="title">德育成绩</p>
    				<p class="tip">记录学生平时的德育考核内容并转化为综合素质分。</p>
        		</li>
        		<li>
        			<img src="images/img/quality3.png" />
    				<p class="title">美育成绩</p>
    				<p class="tip">按照学生在校期间积极参与学校的各个节日活动所获得的个人或团体成绩转化为综合素质分。</p>
        		</li>
        	</ul>
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