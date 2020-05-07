<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>大数据</title>
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
                <a class="active" href="${request.contextPath}/operation/website/bigdataPage">大数据分析</a>
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
	            	<p class="title">大数据分析</p>
	            	<p class="subtitle">通过将各区基础教育数据进行提取、分类、汇总，在管理、科研、教学等层面进行宏观挖掘、分析，在考试成绩等微观方面分析单个师生的服务需求。</p>
	            	<p class="gifContainer"><img src="images/img/data.gif"></p>
	            </div>
	        </div>
    	</div>
        <div class="center">
        	<p class="f30 tc c-000 mt80">教与学质量的好与坏，用数据说话</p>
        	<ul class="datalist clearfix">
        		<li>
        			<img src="images/img/data1.png" />
    				<p class="title">管理层面</p>
    				<p class="tip">综合分析，助力教育局与学校科学决策</p>
        		</li>
        		<li>
        			<img src="images/img/data2.png" />
    				<p class="title">教学层面</p>
    				<p class="tip">教学分析，帮助教师针对性教学</p>
        		</li>
        		<li>
        			<img src="images/img/data3.png" />
    				<p class="title">学习层面</p>
    				<p class="tip">学情分析，给学生个性化成长档案</p>
        		</li>
        	</ul>
        </div>
    	<div style="border-top: 1px solid #e8e9f3;"></div>
    	<div class="animate-part data-part1 mt80">
        	<div class="center">
        		<div class="info fl">
                    <h3 class="hd">选课情况分析</h3>
                    <p class="p1">提供学校、教育局多层面的选课分析，协助教学管理人员指导学生落实更优化的选课策略，帮助更高效的调配教学资源以应对学生实际需求，提高课程建设能力。</p>
                </div> 
                <div class="animate fr"></div>
        	</div>    
        </div>
        <div class="animate-part data-part2">
        	<div class="center">
        		<div class="info fr">
                    <h3 class="hd">学校资源配置</h3>
                    <p class="p1">根据学校实际学科师资、教学场地、学生选课需求情况动态分析资源配比，帮助学校选择适合自身的走班教学模式及指导学生选课，协助教育局把握整体选课进度及情况，促进选课有效落实。</p>
                </div> 
                <div class="animate fl"></div>
        	</div>    
        </div>
        <div class="animate-part data-part3">
        	<div class="center">
        		<div class="info fl">
                    <h3 class="hd">学生学业测评</h3>
                    <p class="p1">提供多指标、多维度的成绩分析，支持图表、报表、大屏等多种可视化形式呈现，支持勾勒用户画像，更为立体的评价群体及个人学业成绩，实现个性化教学管理。</p>
                </div> 
                <div class="animate fr"></div>
        	</div>    
        </div>
        <div class="animate-part data-part4">
        	<div class="center">
        		<div class="info fr">
                    <h3 class="hd">教学质量分析</h3>
                    <p class="p1">通过对学生考试成绩进行量化分析、深度挖掘，横向比较、纵向跟踪，协助任课教师从多角度、多方位对班级进行综合教学质量分析，促进教学优化；帮助教学管理人员监测教师教学质量，及时发现问题。</p>
                </div> 
                <div class="animate fl"></div>
        	</div>    
        </div>
        <div class="animate-part data-part5">
        	<div class="center">
        		<div class="info fl">
                    <h3 class="hd">学习成本分析</h3>
                    <p class="p1">通过分析学生投入学习的时间、家庭投入学习的费用等元素，综合分析成绩相关的投入成本，帮助家长和教师更客观的评价学生个体，采取更为个性化的教育措施，减少学生低效的学习任务，提升学生综合素质能力。</p>
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
		    	$(".data-part1 .animate").addClass('animate-pop-up'); 
		  	}
		  	if($(document).scrollTop()>=613){
		    	$(".data-part2 .animate").addClass('animate-pop-up'); 
		  	}
		  	if($(document).scrollTop()>=1026){
		    	$(".data-part3 .animate").addClass('animate-pop-up'); 
		  	}
		  	if($(document).scrollTop()>=1439){
		    	$(".data-part4 .animate").addClass('animate-pop-up'); 
		  	}
		  	if($(document).scrollTop()>=1852){
		    	$(".data-part5 .animate").addClass('animate-pop-up'); 
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