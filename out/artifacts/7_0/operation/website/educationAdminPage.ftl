<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>教务管理</title>
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
                <a class="active" href="${request.contextPath}/operation/website/educationAdminPage">教务管理</a>
                <a href="${request.contextPath}/operation/website/bigdataPage">大数据分析</a>
                <a href="${request.contextPath}/operation/website/mixedAbilityPage">综合素质</a>
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
	            	<p class="title">新高考教务管理</p>
	            	<p class="subtitle" style="width: auto;">简单3步，体验智能走班选课高效管理模式，拥有技术领先的算法和便捷排课设置，一键攻克教务难题</p>
	            	<p class="tc"><img src="images/img/edu.png" /></p>
	            </div>
	        </div>
    	</div>
        <div class="edu-infor">
	        <div class="center">
	        	<p>新高考教务管理系统，围绕学校的教学、考务等相关日常教学工作，提供走班选课、智能分班、智能排课到成绩管理等全流程业务，力求满足新高考改革形势下教育管理部门和学校对教务教学的新需求，充分整合和挖掘学校现有的教育资源，提高教育管理能力与服务水平，实现教育管理现代化、决策科学化。</p>
	        </div>
        </div>
    	<div class="animate-part edu-part1 mt80">
        	<div class="center">
        		<div class="info fl">
                    <h3 class="hd">走班选课</h3>
                    <p class="p1">支持多轮选课，使学生选课方式更清晰，学校实时掌握和引导学生选课，帮助学校科学培训师资、场地等教学资源。在新高考模式下，实现选课过程智能化。</p>
                    <ul class="icon clearfix">
                    	<li>
                    		<img src="images/img/edu1.png">
                    		<p>发布选课</p>
                    	</li>
                    	<li>
                    		<img src="images/img/edu2.png">
                    		<p>学生选课</p>
                    	</li>
                    	<li>
                    		<img src="images/img/edu3.png">
                    		<p>选课结果</p>
                    	</li>
                    </ul>
                </div> 
                <div class="animate fr"></div>
        	</div>    
        </div>
        <div class="animate-part edu-part2">
        	<div class="center">
        		<div class="info fr">
                    <h3 class="hd">智能分班</h3>
                    <p class="p1">在综合考虑学校男女生比例、成绩等各方面因素的前提下，根据成绩开设创新班或平行班，自动+手动结合，具有科学性、智能化、人性化的特点，提高分班效率，最优化配置教学资源。</p>
                    <ul class="icon clearfix">
                    	<li>
                    		<img src="images/img/edu4.png">
                    		<p>手工分班</p>
                    	</li>
                    	<li>
                    		<img src="images/img/edu5.png">
                    		<p>组合分班</p>
                    	</li>
                    	<li>
                    		<img src="images/img/edu6.png">
                    		<p>单科分班</p>
                    	</li>
                    </ul>
                </div> 
                <div class="animate fl"></div>
        	</div>    
        </div>
        <div class="animate-part edu-part3">
        	<div class="center">
        		<div class="info fl">
                    <h3 class="hd">智能排课</h3>
                    <p class="p1">支持设置多样化排课条件，优化算法以解决“课程、班级、教室、教师、时间”五维关系的编排冲突问题，使学校在有限资源的条件下，得出最佳排课结果，提升日常教务工作效率，全面适应新高考课程改革。</p>
                    <ul class="icon clearfix">
                    	<li>
                    		<img src="images/img/edu7.png">
                    		<p>多种排课条件设置</p>
                    	</li>
                    	<li>
                    		<img src="images/img/edu8.png">
                    		<p>一键智能编排</p>
                    	</li>
                    	<li>
                    		<img src="images/img/edu9.png">
                    		<p>日常课表管理</p>
                    	</li>
                    </ul>
                </div> 
                <div class="animate fr"></div>
        	</div>    
        </div>
        <div style="border-top: 1px solid #e8e9f3;"></div>
        <div class="animate-part mt80">
        	<div class="center">
        		<div class="info fl">
                    <h3 class="hd">考务管理</h3>
                    <p class="p1">可选择普通的传统中小学的考务编排模式和目前的新高考选考模式分别进行编排考生考场。</p>
                    <ul class="icon clearfix">
                    	<li>
                    		<img src="images/img/edu10.png">
                    		<p>考场编排</p>
                    	</li>
                    	<li>
                    		<img src="images/img/edu11.png">
                    		<p>监巡考设置</p>
                    	</li>
                    	<li>
                    		<img src="images/img/edu12.png">
                    		<p>考务报表</p>
                    	</li>
                    </ul>
                </div>
                <div class="info fr">
                    <h3 class="hd">成绩管理</h3>
                    <p class="p1">以各种图标形式清晰呈现本班的各科总体表现和变化趋势，教师可查看每位学生的趋势动态和详细分析，为个性化教学提供科学依据。</p>
                    <ul class="icon clearfix">
                    	<li>
                    		<img src="images/img/edu13.png">
                    		<p>学习水平分析</p>
                    	</li>
                    	<li>
                    		<img src="images/img/edu14.png">
                    		<p>教学质量分析</p>
                    	</li>
                    	<li>
                    		<img src="images/img/edu15.png">
                    		<p>学习轨迹记录</p>
                    	</li>
                    	<li>
                    		<img src="images/img/edu16.png">
                    		<p>动态趋势预测</p>
                    	</li>
                    </ul>
                </div> 
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
		  	if($(document).scrollTop()>=100){
		    	$(".edu-part1 .animate").addClass('animate-pop-up'); 
		  	}
		  	if($(document).scrollTop()>=413){
		    	$(".edu-part2 .animate").addClass('animate-pop-up'); 
		  	}
		  	if($(document).scrollTop()>=880){
		    	$(".edu-part3 .animate").addClass('animate-pop-up'); 
		  	}
		});
    </script>
</div>
</body>
<script>
	$(function() {
		//页面停留的功能模块位置
		var pos=${pos};
		if(pos>=0 && pos <5)$(".hd")[pos].scrollIntoView();
		
		if($("#realNameView").html()=="未登录"){
            $("#realNameView").hide();
			$("#linkto").hide();
		}else{
			$("#login").hide();
		}
		
	})
</script>
</html>