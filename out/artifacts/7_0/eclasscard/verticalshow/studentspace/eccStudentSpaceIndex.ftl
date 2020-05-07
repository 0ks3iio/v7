<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta charset="UTF-8">
	<title>学生空间</title>
	<script>
	_deviceNumber = "${deviceNumber!}";
	_view = "${view!}";
	</script>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/verticalshow/plugins/slick/slick.css">
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/verticalshow/css/style.css">
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/verticalshow/plugins/mobiscroll/mobiscroll.full.min.css">
</head>
<body>
	<header class="header">
		<div class="logo"><img src="${request.contextPath}/static/eclasscard/verticalshow/images/logo.png" alt=""></div>
		<div class="date">
			<span class="time"></span>
			<div class="right">
				<span class="day"></span>
				<span class="week"></span>
			</div>
		</div>
	</header>
	<#include "/eclasscard/verticalshow/showMsgtip.ftl">
	<div class="main-container">
		<div class="sign-out-tip">温馨提示：若您在1分钟内无任何操作，为保护您的信息安全系统将自动退出账号。</div>
		<div class="box">
			<div class="space space-stu">
				<div class="space-header">
					<button class="btn space-stu-signOut" onclick="goback()">退出</button>
					<div class="role">
						<#if showPicUrl?exists>
						<span class="role-img"><img src="${request.contextPath}${showPicUrl!}" alt=""></span>
						<#else>
						<span class="role-img"><img src="${request.contextPath}/static/eclasscard/verticalshow/images/nothing.png" alt=""></span>
						</#if>
						<h4 class="role-name">${studentName!}</h4>
					</div>
					<input type="hidden" id="userId" value="${userId!}">
					<div class="btn-group btn-group-lg">
						<a class="btn" href="javascript:void(0);" id="homepage" onclick="queryMsgShow(2)" data-action="tab">个人主页</a>
						<a class="btn active" href="javascript:void(0);" id="schedule" onclick="queryMsgShow(1)" data-action="tab">课程表</a>
						<a class="btn" href="javascript:void(0);" id="leaveQuery" onclick="queryMsgShow(3)" data-action="tab">请假查询</a>
					</div>
				</div>
				<div class="space-content">
					<div class="tab-content" id="showDiv">
						
					</div>
				</div>
			</div>
		</div>
			
	</div>
<footer id="footer" class="footer">
		
</footer>

	<script src="${request.contextPath}/static/eclasscard/verticalshow/plugins/jquery/jquery.min.js"></script>
	<script src="${request.contextPath}/static/eclasscard/verticalshow/plugins/echarts/echarts.min.js"></script>
	<script src="${request.contextPath}/static/eclasscard/verticalshow/plugins/layer/layer.js"></script>
	<script src="${request.contextPath}/static/eclasscard/verticalshow/js/myscript.js"></script>
	<script src="${request.contextPath}/static/eclasscard/verticalshow/plugins/mobiscroll/mobiscroll.full.min.js"></script>
	<script>
		var load_time = null;  
		var down_time = null; 
	
		$(document).ready(function(){
			var footUrl = "${request.contextPath}/eccShow/eclasscard/showIndex/showFooter?type=4&name="+_deviceNumber+"&view="+_view;
			$("#footer").load(footUrl);
			queryMsgShow(1);
	    	//打开页面60秒不操作就跳转  
	   		load_time = setTimeout(function(){  
	        	goback();  
	    	},1000*60);  
		});
		
		function queryMsgShow(type,leaveType){
			var userId=$("#userId").val();
			if(type==1){
				$("#schedule").addClass('active');
				$("#homepage").removeClass('active');
				$("#leaveQuery").removeClass('active');
				$("#showDiv").load("${request.contextPath}/eccShow/eclasscard/stuSchedule/page?userId="+userId+"&view="+_view);
			}else if(type==2){
				$("#homepage").addClass('active');
				$("#schedule").removeClass('active');
				$("#leaveQuery").removeClass('active');
				$("#showDiv").load("${request.contextPath}/eccShow/eclasscard/homepage?userId="+userId+"&view="+_view);
			}else if(type==3){
				$("#homepage").removeClass('active');
				$("#schedule").removeClass('active');
				$("#leaveQuery").addClass('active');
				$("#showDiv").load("${request.contextPath}/eccShow/eclasscard/studentLeave/page?userId="+userId+"&view="+_view);
			}else if(type==4){
				$("#homepage").removeClass('active');
				$("#schedule").removeClass('active');
				$("#leaveQuery").addClass('active');
				$("#showDiv").load("${request.contextPath}/eccShow/eclasscard/studentLeave/applyPage?userId="+userId+"&leaveType="+leaveType+"&view="+_view);
			}
		}
		
		
		function goback(){
			location.href = "${request.contextPath}/eccShow/eclasscard/showIndex?deviceNumber="+_deviceNumber+"&view="+_view;
		}
		
		function stopLoadTime(){ 
   			clearInterval(load_time);  
   			if(null != down_time){  
       		clearInterval(down_time);  
   			}  
   			down_time = setTimeout(function(){  
       			goback();  
   			},1000*60);  
		}
		document.addEventListener('touchstart', function(event) {
     		// 如果这个元素的位置内只有一个手指的话
    		if (event.targetTouches.length == 1) {
　　　	 		//event.preventDefault();
         		stopLoadTime();
    		}
		}, false);
		
		function showStudentSpace(){
			location.href = "${request.contextPath}/eccShow/eclasscard/studentSpace/login?deviceNumber="+_deviceNumber+"&view="+_view;
		}
		
		function showClassSpace(){
			location.href = "${request.contextPath}/eccShow/eclasscard/classSpace/index?deviceNumber="+_deviceNumber+"&view="+_view;
		}
	</script>
</body>
</html>