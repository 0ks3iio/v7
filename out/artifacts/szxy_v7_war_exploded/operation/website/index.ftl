<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
    <title>新高考智慧校园-智能化教务平台</title>
    <link rel="Shortcut icon" href="images/img/favicon.ico">
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
                <a class="logo" href="http://www.wanpeng.com/" target="_blank">万朋教育</a>
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
                <a class="active" href="${request.contextPath}/operation/website/index">首页</a>
                <a href="${request.contextPath}/operation/website/educationAdminPage">教务管理</a>
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
        <div class="banner"></div>
        <div class="center">
            <div class="publicModel">
            	<p class="title">互联网+大数据+教育改革，开启智能教学时代</p>
            	<p class="subtitle">解决各级教育局、各类学校管理需求，打造"互联网+"教育新生态</p>
            	<img class="box-shadow" src="images/img/index1.jpg" />
            </div>
        </div>
        <div class="bg1">
            <div class="center">
	            <div class="publicModel">
	            	<p class="title">简单3步 体验智能走班选课高效管理模式</p>
	            	<p class="subtitle">技术领先的优化算法，便捷排课设置，一键攻克教务难题</p>
	            	<ul class="step3 clearfix">
	            		<li class="select">
	            			<a href="${request.contextPath}/operation/website/educationAdminPage?pos=0">
	            				<span class="title">选课</span>
		            			<span class="tip">支持多轮选课，流程清晰，选课过程智能化，实时生成“一师一课表，一生一课表”。</span>
	            			</a>
	            		</li>
	            		<li class="separate">
	            			<a href="${request.contextPath}/operation/website/educationAdminPage?pos=1">
	            				<span class="title">分班</span>
		            			<span class="tip">贴合学校实际分班模式预设条件，满足全走班、大走班及小走班等多种教学需求。</span>
	            			</a>
	            		</li>
	            		<li class="line">
	            			<a href="${request.contextPath}/operation/website/educationAdminPage?pos=2">
	            				<span class="title">排课</span>
		            			<span class="tip">智能对接选课、分班结果，呈现最佳排课结果，最大化利用教学资源，提高教务工作效率。</span>
	            			</a>
	            		</li>
	            	</ul>
	            	<p class="tc mt30"><a class="btn btn01" href="${request.contextPath}/operation/website/educationAdminPage">查看详情</a></p>
	            </div>
	        </div>
        </div>
        <div class="center">
            <div class="publicModel">
            	<p class="title">选择电子化考务 考试安排更简便</p>
            	<p class="subtitle">考前设置考试信息，考后完善考试数据，信息安全有保障</p>
            	<ul class="internet clearfix">
            		<li>
            			<a href="${request.contextPath}/operation/website/educationAdminPage?pos=3">
            				<img src="images/img/internet1.jpg" />
            				<span class="title">考试信息快捷设置</span>
	            			<span class="tip">相关教务人员再考前设置好考试对象、时间、类型、名称等信息。</span>
            			</a>
            		</li>
            		<li>
            			<a href="${request.contextPath}/operation/website/educationAdminPage?pos=3">
            				<img src="images/img/internet2.jpg" />
            				<span class="title">录分权限科学分配</span>
	            			<span class="tip">考试信息、成绩录入需有权限的教师操作，管理员灵活修改权限。</span>
            			</a>
            		</li>
            		<li>
            			<a href="${request.contextPath}/operation/website/educationAdminPage?pos=3">
            				<img src="images/img/internet3.jpg" />
            				<span class="title">成绩录入易筛易查</span>
	            			<span class="tip">考试成绩可通过学期、年级、名称、班级等条件查询，快速方便。</span>
            			</a>
            		</li>
            		<li>
            			<a href="${request.contextPath}/operation/website/educationAdminPage?pos=3">
            				<img src="images/img/internet4.jpg" />
            				<span class="title">考试信息安全锁定</span>
	            			<span class="tip">学生考试信息的锁定与解锁，只允许有权限的教师操作，保证信息安全。</span>
            			</a>
            		</li>
            	</ul>
            </div>
        </div>
        <div class="dataContainer">
            <div class="center">
	            <div class="publicModel">
	            	<p class="title">教与学质量的好与坏，用数据说话</p>
	            	<p class="subtitle">大数据动态监测平台，数据精准分析，为教育部门的宏观调控和决策提供动态可观数据依据</p>
	            	<ul class="data clearfix">
	            		<li class="manage">
	            			<a href="${request.contextPath}/operation/website/bigdataPage">
	            				<span class="title">管理层面</span>
		            			<span class="tip">综合分析，助力教育局与学校科学决策</span>
		            			<span class="detail">自定义模型，多维度分析，直观的数据图标反映区域综合校情；从个体到整体，从微观到宏观，动态预测助力教学质量评价与高效决策。</span>
	            			</a>
	            		</li>
	            		<li class="teach">
	            			<a href="${request.contextPath}/operation/website/bigdataPage">
	            				<span class="title">教学层面</span>
		            			<span class="tip">教学分析，帮助教师针对性教学</span>
		            			<span class="detail">多样分析图表，聚焦共性薄弱知识点，掌握班级学情和变化趋势；个体学生成绩动态趋势和详细分析，让培优补差更有针对性。</span>
	            			</a>
	            		</li>
	            		<li class="study">
	            			<a href="${request.contextPath}/operation/website/bigdataPage">
	            				<span class="title">学习层面</span>
		            			<span class="tip">学情分析，给学生个性化成长档案</span>
		            			<span class="detail">学情诊断分析，直观感受自己学习进步的轨迹，明确学习重难点；基于往届数据分析未来的发展预期，科学制定属于学习计划。</span>
	            			</a>
	            		</li>
	            	</ul>
	            	<p class="tc mt30"><a class="btn btn01" href="${request.contextPath}/operation/website/bigdataPage">查看详情</a></p>
	            </div>
	        </div>
        </div>
        <div class="center">
            <div class="publicModel">
            	<p class="title">为近百所学校提供服务</p>
            	<p class="subtitle">收到教育局、学校老师、学生及家长的一致好评</p>
            	<ul class="school clearfix">
            		<li>
        				<img src="images/img/school1.jpg" />
        				<span class="title">杭州外国语学校</span>
            			<span class="tip">学校通过智慧校园平台对12个初中版、20个普通高中班进行分层教学，学生信息录入系统，由系统自动生成“一生一课表”一目了然。该系统帮助学校实现分层次、小班化的走班教学，成功应对新高考。</span>
            		</li>
            		<li>
        				<img src="images/img/school2.jpg" />
        				<span class="title">黑龙江大庆实验中学</span>
            			<span class="tip">黑龙江省首批示范性高中，该校全面使用智慧校园平台，对120个高中班进行智能个性化走班选课和分班排课，高效利用学校有限资源，让学校、教师和学生从容应对新高考。</span>
            		</li>
            		<li>
        				<img src="images/img/school3.jpg" />
        				<span class="title">杭州学军中学</span>
            			<span class="tip">浙江省政府批准的首批重点中学，历来是学霸必争之地。该校启用万朋教育智慧校园走班选课系统、优化智能开班系统以及灵活的排课系统，对提升学校教育管理能力上卓有成效。</span>
            		</li>
            		<li>
        				<img src="images/img/school4.jpg" />
        				<span class="title">浙江省镇海中学</span>
            			<span class="tip">是浙江省唯一一所进入全国排名前十的高中。秋季新高一将启用万朋教育智慧校园平台，依据学校个性化需求，从选课分班排课全流程智能化，呈现最贴合学校实际应用的选课分班排课结果。</span>
            		</li>
            	</ul>
            	<div class="schoollist">
            		<img src="images/img/sc1.jpg" />
            		<img src="images/img/sc2.jpg" />
            		<img src="images/img/sc3.jpg" />
            		<img src="images/img/sc4.jpg" />
            		<img src="images/img/sc5.jpg" />
            		<img src="images/img/sc6.jpg" />
            		<img src="images/img/sc7.jpg" />
            		<img src="images/img/sc8.jpg" />
            		<img src="images/img/sc9.jpg" />
            		<img src="images/img/sc10.jpg" />
            		<img src="images/img/sc11.jpg" />
            		<img src="images/img/sc12.jpg" />
            	</div>
            </div>
        </div>
        <div class="try">
            <div class="center">
            	<p>现在申请，立即体验</p>
            	<a class="btn btn01 mt30" href="${request.contextPath}/operation/website/trialUserRegister">试用申请</a>
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