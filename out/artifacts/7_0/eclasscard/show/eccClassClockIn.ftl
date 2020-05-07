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
		_cardId = "${cardId!}";
		_contextPath = "${request.contextPath}";
	</script>
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/show/plugins/slick/slick.css">
	<link rel="stylesheet" href="${request.contextPath}/static/eclasscard/show/css/style.css">
</head>
<#include "/eclasscard/show/clockStateTip.ftl">
<#include "/eclasscard/show/showMsgtip.ftl">
<body>
	<header class="header">
		<div class="back">
			<a class="arrow" href="javascript:void(0);" onclick="backIndex()"></a>
			<span>${classAttence.teacherRealName!}-${classAttence.subjectName!}</span>
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
		<div class="checkin-container">
			<div class="checkin-right">
				<div class="checkin-teacher">
					<img src="${request.contextPath}/zdsoft/crop/doPortrait?type=big&userName=${classAttence.teacherName!}">
					<p class="checkin-teacher-name">${classAttence.teacherRealName!}老师</p>
					<p class="checkin-teacher-course">本次授课：${classAttence.subjectName!}</p>
					<#if teaclzAttence.status ==1>
					<div id="teacherClockState" class="checkin-state">未签到</div>
					<#elseif teaclzAttence.status ==2>
					<div class="checkin-state checkin-state-late">迟到</div>
					<#elseif teaclzAttence.status ==4>
					<div class="checkin-state checkin-state-checked">已签到</div>
					</#if>
				</div>
				<div class="checkin-info">
					<h4>到班情况汇总 </h4>
					<ul>
						<li>应到人数：<b>${sumNum}</b>人</li>
						<li>实到人数：<b id="clockNum">${clockNum}</b>人</li>
						<li>请假人数：<b>${leaveNum}</b>人</li>
						<li>未到人数：<b id="notClockNum">${notClockNum}</b>人</li>
						<li>迟到人数：<b id="lateNum">${lateNum}</b>人</li>
					</ul>
				</div>
			</div>
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
						<div class='checkin-state-div'>
							<#if item.status ==1||item.status ==3>
							<div class="checkin-state">未签到</div>
							<#elseif item.status ==2>
							<div class="checkin-state checkin-state-late">迟到</div>
							<#elseif item.status ==4>
							<div class="checkin-state checkin-state-checked">已签到</div>
							</#if>
						<div>
					</a>
				</li>
			 </#list>
        	</#if>
			</ul>
			
		</div>
	</div>

<script src="${request.contextPath}/static/eclasscard/show/plugins/jquery/jquery.min.js"></script>
<script src="${request.contextPath}/static/eclasscard/show/plugins/slick/slick.js"></script>
<script src="${request.contextPath}/static/eclasscard/show/plugins/layer/layer.js"></script>
<script src="${request.contextPath}/static/eclasscard/show/js/myscript.js"></script>
<script type="text/javascript" src="http://cdn.sockjs.org/sockjs-0.3.min.js"></script>
<script src="${request.contextPath}/static/eclasscard/show/js/eccPublic.js"></script>
<script>
$(document).ready(function(){
	var index;
	$('.js-openCheckin').on('click', function(e){
		e.preventDefault();
		index = layer.open({
			type: 1,
			title: false,
			shade: .5,
			closeBtn: 0,
			shadeClose: true,
			area: '600px',
			content: $('.layer-checkin')
		});
		layer.style(index, {
			'border-radius': '15px'
		})
	})

	$('.layer-checkin-close').on('click', function(e){
		e.preventDefault();
		layer.close(index);
	})
})

function backIndex(){
	location.href = "${request.contextPath}/eccShow/eclasscard/showIndex?cardId="+_cardId+"&view="+_view;
}
//打卡触发
function eccClockIn(cardNumber){
	if(!cardNumber||cardNumber==''||cardNumber=='0'){
		showMsgTip("无效卡")
		return;
	}
    $.ajax({
        url:'${request.contextPath}/eccShow/eclasscard/clockIn',
        data:{"cardNumber":cardNumber,"cardId":_cardId,"objectId":"${classAttence.id!}","clockType":0},
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
		$("#teacherClockState").addClass('checkin-state-checked').text('已签到');
	}else if(status==4){
		$("#teacherClockState").addClass('checkin-state-late').text('迟到');
	}
}
function showStuSuccess(result){
	var studentId = result.studentId;
	var status = result.status;
	if(studentId){
		$("#"+studentId).addClass('checkin-animate');
		var clockNum = $("#clockNum").text();
		var notClockNum = $("#notClockNum").text();
		var lateNum = $("#lateNum").text();
		if(status==1){
			clockNum++;
			notClockNum--;
			$("#clockNum").text(clockNum);
			$("#"+studentId).find('.checkin-state').addClass('checkin-state-checked').text('已签到');
		}else if(status==4){
			lateNum++;
			notClockNum--;
			$("#lateNum").text(lateNum);
			$("#"+studentId).find('.checkin-state').addClass('checkin-state-late').text('迟到');
		}
		$("#notClockNum").text(notClockNum)
	}
}

function showStudentRingAttance(data){
	$.each(data, function(index, json) {
		showStuSuccess(json);
	});
}
</script>
</body>
</html>