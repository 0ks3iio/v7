<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta charset="UTF-8">
	<title>打卡</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
	<script>
		_webSocketUrl = "${webSocketUrl!}";
		_eccIndexUrl = "${eccIndexUrl!}";
		_view = "${view!}";
		_sockJSUrl = "${sockJSUrl!}";
		_deviceNumber = "${deviceNumber!}";
		_contextPath = "${request.contextPath}";
	</script>
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/verticalshow/plugins/slick/slick.css">
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/verticalshow/css/style.css">
</head>
<#include "/eclasscard/verticalshow/clockStateTip.ftl">
<#include "/eclasscard/verticalshow/showMsgtip.ftl">
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
	<div class="main-container">
	<div class="box">
			<div class="box-header">
				<h3 class="box-title">上课签到</h3>
			</div>
			<div class="box-body">
				<div class="checkin-container">
					<div class="checkin-course">
						<ul class="course-list">
							<li class="course-item current">
								<div class="course-card">
									<div class="course-teacher">
										<div class="role">
											<#if classAttence.teacherName?exists>
												<span class="role-img"><img src="${request.contextPath}/zdsoft/crop/doPortrait?type=big&userName=${classAttence.teacherName!}" alt=""></span>
											<#else>
												<span class="role-img"><img src="${request.contextPath}/static/eclasscard/verticalshow/images/avatar.png" alt=""></span>
											</#if>
											<#if teaclzAttence.status ==1>
											<span id="teacherClockState" class="label label-fill">未签到</span>
											<#elseif teaclzAttence.status ==2>
											<span class="label label-fill label-fill-red">迟到</span>
											<#elseif teaclzAttence.status ==4>
											<span class="label label-fill label-fill-yellow">已签到</span>
											</#if>
											<h4 class="role-name">${classAttence.teacherRealName!}</h4>
										</div>
									</div>
									<ul class="course-info">
										<li><span>课程：${classAttence.subjectName!}</span></li>
										<li><span>时间：${classAttence.beginTime!}~${classAttence.endTime!}</span></li>
										<li class="course-info-teacher">教师：${classAttence.teacherRealName!}</li>
									</ul>
								</div>
							</li>
						</ul>
					</div>
					<!-- S 考勤数据 -->
					<ul class="checkin-info">
						<li>应到人数：<em>${sumNum}</em>人</li>
						<li>实到人数：<em id="clockNum">${clockNum}</em>人</li>
						<li>请假人数：<em>${leaveNum}</em>人</li>
						<li>未到人数：<em id="notClockNum">${notClockNum}</em>人</li>
						<li>迟到人数：<em id="lateNum">${lateNum}</em>人</li>
					</ul><!-- E 考勤数据 -->
					<ul class="checkin-stu-list">
					<#if eccStuclzAttences?exists&&eccStuclzAttences?size gt 0>
		          	<#list eccStuclzAttences as item>
						<li id="${item.studentId!}" class="">
							<a class="" href="javascript:void(0);">
								<#if item.showPictrueUrl?exists>
								<div class="checkin-stu-avatar"><img src="${request.contextPath}${item.showPictrueUrl!}"></div>
								<#elseif item.sex?exists&&item.sex==2>
								<div class="checkin-stu-avatar"><img src="${request.contextPath}/static/eclasscard/show/images/user-female.png"></div>
								<#else>
								<div class="checkin-stu-avatar"><img src="${request.contextPath}/static/eclasscard/show/images/user-male.png"></div>
								</#if>
								<span class="checkin-stu-name">${item.stuRealName?default("（已删除）")}<#if item.status ==3><span class="checkin-flag">假</span></#if></span>
								<#if item.status ==1||item.status ==3>
								<span class="label label-fill">未签到</span>
								<#elseif item.status ==2>
								<span class="label label-fill label-fill-red">迟到</span>
								<#elseif item.status ==4>
								<span class="label label-fill label-fill-yellow">已签到</span>
								</#if>
							</a>
						</li>
					 </#list>
		        	</#if>
					</ul>
				</div>
			</div>
		</div>
	</div>
<footer id="footer" class="footer">
		
</footer>
<script src="${request.contextPath}/static/eclasscard/verticalshow/plugins/jquery/jquery.min.js"></script>
<script src="${request.contextPath}/static/eclasscard/verticalshow/plugins/slick/slick.js"></script>
<script src="${request.contextPath}/static/eclasscard/verticalshow/plugins/layer/layer.js"></script>
<script src="${request.contextPath}/static/eclasscard/verticalshow/js/myscript.js"></script>
<script type="text/javascript" src="http://cdn.sockjs.org/sockjs-0.3.min.js"></script>
<script src="${request.contextPath}/static/eclasscard/verticalshow/js/eccPublic.js"></script>
<script>
$(document).ready(function(){
	var footUrl = "${request.contextPath}/eccShow/eclasscard/showIndex/showFooter?type=2&name="+_deviceNumber+"&view="+_view;
	$("#footer").load(footUrl);
	// 学生列表高度
	$('.checkin-stu-list').css({
		overflow: 'auto',
		height: $(window).height() - $('.checkin-stu-list').offset().top - 180
	});
	centerCurrent();
})

function backIndex(){
	location.href = "${request.contextPath}/eccShow/eclasscard/showIndex?deviceNumber="+_deviceNumber+"&view="+_view;
}
//打卡触发
function eccClockIn(cardNumber){
	if(!cardNumber||cardNumber==''||cardNumber=='0'){
		showMsgTip("无效卡")
		return;
	}
    $.ajax({
        url:'${request.contextPath}/eccShow/eclasscard/clockIn',
        data:{"cardNumber":cardNumber,"deviceNumber":_deviceNumber,"objectId":"${classAttence.id!}","clockType":0},
        type:'post',
        success:function(data){
            var result = JSON.parse(data);
            if(result.haveStu){
            	showStuClockMsg(result);
            	if(result.status!=2){
            		if(result.ownerType=='2'){
	            		showTeaSuccess(result);
            		}else{
	            		showStuSuccess(result);
            		}
            	}
            }else{
            	showMsgTip(result.msg);
            }
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function showTeaSuccess(result){
	var status = result.status;
	if(status==1){
		$("#teacherClockState").addClass('label-fill-yellow').text('已签到');
	}else if(status==4){
		$("#teacherClockState").addClass('label-fill-red').text('迟到');
	}
}
function showStuSuccess(result){
	var studentId = result.studentId;
	var status = result.status;
	if(studentId){
		var clockNum = $("#clockNum").text();
		var notClockNum = $("#notClockNum").text();
		var lateNum = $("#lateNum").text();
		if(status==1){
			clockNum++;
			notClockNum--;
			$("#clockNum").text(clockNum);
			$("#"+studentId).find('.label-fill').addClass('label-fill-yellow').text('已签到');
		}else if(status==4){
			lateNum++;
			notClockNum--;
			$("#lateNum").text(lateNum);
			$("#"+studentId).find('.label-fill').addClass('label-fill-red').text('迟到');
		}
		$("#notClockNum").text(notClockNum);
	}
}

function showStudentRingAttance(data){
	$.each(data, function(index, json) {
		showStuSuccess(json);
	});
}
// 居中当前课程
function centerCurrent(){
	var cur = $('.course-item.current');
	var container = $('.checkin-course');
	var list = $('.course-list');
	var sum_width = 0;
	var center_left = $(window).innerWidth() / 2 - $('.course-item').outerWidth() / 2 - container.offset().left;
	var cur_left = 0;

	$('.course-item').each(function(){
		sum_width = sum_width + $(this).outerWidth();
	});
	var pass = $('.course-item.pass');
	var p_length = pass.length;
	if(cur.length <= 0){
		p_length = p_length - 1;
	}
	for(var i=0; i<p_length; i++){
		cur_left = cur_left + pass.eq(i).outerWidth();
	}
	
	list.outerWidth(sum_width);

	var s_max = sum_width - container.outerWidth();

	if(cur_left > center_left && cur_left - center_left > s_max){
		container.css({
			'padding-right': cur_left - center_left - s_max +'px'
		})
		container.scrollLeft(cur_left - center_left);
	}else if(cur_left < center_left){
		container.css({
			'padding-left': center_left - cur_left +'px'
		})
		container.scrollLeft(0);
	}else{
		container.scrollLeft(cur_left - center_left);
	}
}
</script>
</body>
</html>