<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta charset="UTF-8">
	<title>学生空间</title>
	<script>
	_cardId = "${cardId!}";
	_view = "${view!}";
	</script>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
	<script src="${request.contextPath}/static/eclasscard/show/plugins/jquery/jquery.min.js"></script>
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/show/plugins/slick/slick.css">
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/show/css/style.css">
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/show/plugins/mobiscroll/mobiscroll.full.min.css">
	<script src="${request.contextPath}/static/eclasscard/show/plugins/layer/layer.js"></script>
</head>
<body>
	<header class="header">
		<div class="back">
			<a href="javascript:void(0);" class="arrow" onclick="goback()"></a>
			<span>学生空间</span>
		</div>
		<div class="date">
			<span class="time"></span>
			<div class="right">
				<span class="day"></span>
				<span class="week"></span>
			</div>
		</div>
	</header>
	<div class="main-container">
		<div class="space">
			<div class="space-left">
				<div class="stu-info">
					<div class="stu-info-bg">
						<img src="${request.contextPath}/static/eclasscard/show/images/student-space.png" alt="">
					</div>
					<input type="hidden" id="userId" value="${userId!}">
					<div class="stu-info-body">
						<img src="${request.contextPath}${showPicUrl!}" alt="" class="stu-avatar">
						<ul class="stu-info-list">
							<li><span>姓名：</span><span>${studentName!}</span></li>
							<li><span>学号：</span><span>${studentCode!}</span></li>
							<li><span>班级：</span><span>${className!}</span></li>
							<li><span>班主任：</span><span>${teacherName!}</span></li>
						</ul>
						<button class="btn btn-block" onclick="goback()">退&emsp;出</button>
					</div>
				</div>
				<div class="nav-block" data-action="tab">
					<a href="javascript:void(0);" id="schedule" onclick="queryMsgShow(1)" class="nav-block-item active" >
						<span class="icon icon-schedule"></span>
						<p>课表查询</p>
					</a>
					<#if healthData>
					<a href="javascript:void(0);" id="healthQuery" onclick="queryMsgShow(2)" class="nav-block-item">
						<span class="icon icon-health"></span>
						<p>健康数据</p>
					</a>
					<#else>
					<a href="javascript:void(0);" id="scoreQuery" onclick="queryMsgShow(5)" class="nav-block-item">
						<span class="icon icon-search"></span>
						<p>成绩查询</p>
					</a>
					</#if>
					<a href="javascript:void(0);" id="leaveQuery" onclick="queryMsgShow(4,1)" class="nav-block-item ">
						<span class="icon icon-search"></span>
						<p>请假查询</p>
					</a>
				</div>
			</div>
				<div class="space-right" id="showDiv">
					
						
				</div>
			</div>
		</div>
	</div>


	<script src="${request.contextPath}/static/eclasscard/show/plugins/jquery/jquery.min.js"></script>
	<script src="${request.contextPath}/static/eclasscard/show/plugins/slick/slick.js"></script>
	<script src="${request.contextPath}/static/eclasscard/show/plugins/echarts/echarts.min.js"></script>
	<script src="${request.contextPath}/static/eclasscard/show/js/myscript.js"></script>
	<script src="${request.contextPath}/static/js/jquery.form.js"></script>
	<script src="${request.contextPath}/static/eclasscard/show/plugins/mobiscroll/mobiscroll.full.min.js"></script>
<script>
var load_time = null;  
var down_time = null;  
$(document).ready(function(){
		queryMsgShow(1);
	    //打开页面60秒不操作就跳转  
	   load_time = setTimeout(function(){  
	        goback();  
	    },1000*60);  
	})
function goback(){
	location.href = "${request.contextPath}/eccShow/eclasscard/showIndex?cardId="+_cardId+"&view="+_view;
}
function queryMsgShow(type,leaveType){
	var userId=$("#userId").val();
	if(type==1){
		$("#schedule").addClass('active');
		$("#scoreQuery").removeClass('active');
		$("#leaveQuery").removeClass('active');
		$("#healthQuery").removeClass('active');
		$("#showDiv").load("${request.contextPath}/eccShow/eclasscard/stuSchedule/page?userId="+userId+"&view="+_view);
	}else if(type==2){
		$("#healthQuery").addClass('active');
		$("#schedule").removeClass('active');
		$("#scoreQuery").removeClass('active');
		$("#leaveQuery").removeClass('active');
		$("#showDiv").load("${request.contextPath}/eccShow/eclasscard/stuHealthIndex/page?userId="+userId+"&view="+_view);
	}else if(type==3){
		$("#scoreQuery").removeClass('active');
		$("#healthQuery").removeClass('active');
		$("#schedule").removeClass('active');
		$("#leaveQuery").addClass('active');
		$("#showDiv").load("${request.contextPath}/eccShow/eclasscard/studentLeave/page?userId="+userId+"&view="+_view);
	}else if(type==4){
		$("#scoreQuery").removeClass('active');
		$("#healthQuery").removeClass('active');
		$("#schedule").removeClass('active');
		$("#leaveQuery").addClass('active');
		$("#showDiv").load("${request.contextPath}/eccShow/eclasscard/studentLeave/applyPage?userId="+userId+"&leaveType="+leaveType+"&view="+_view);
	}else if(type==5){
		$("#scoreQuery").addClass('active');
		$("#healthQuery").removeClass('active');
		$("#schedule").removeClass('active');
		$("#leaveQuery").removeClass('active');
	}
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
</script>
</body>
</html>