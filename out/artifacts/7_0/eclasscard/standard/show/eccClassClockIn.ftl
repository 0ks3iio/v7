<div class="grid">
	<div class="grid-cell space-side">
		<div id="courseDiv" class="box">
		</div>
	</div>
	<input type="hidden" id="classAttenceId" value="${classAttence.id!}">
	<div class="grid-cell space-main">
		<div class="box">
			<div class="box-body">
				<div class="checkin-container">
					<!-- S 考勤数据 -->
					<ul class="checkin-info">
						<li>应到人数：<em>${sumNum}</em>人</li>
						<li>实到人数：<em id="clockNum">${clockNum}</em>人</li>
						<li>请假人数：<em>${leaveNum}</em>人</li>
						<li>未到人数：<em id="notClockNum">${notClockNum}</em>人</li>
						<li>迟到人数：<em id="lateNum">${lateNum}</em>人</li>
					</ul><!-- E 考勤数据 -->
					
					<!-- S 学生列表 -->
					<ul class="checkin-stu-list">
					<#if eccStuclzAttences?exists&&eccStuclzAttences?size gt 0>
          				<#list eccStuclzAttences as item>
						<li id="${item.studentId!}" class="checkin-animate">
							<a class="js-openCheckin" href="javascript:void(0);">
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
								<span class="label label-fill label-fill-bluePurple">已签到</span>
								</#if>
							</a>
						</li>
						</#list>
        			</#if>
					</ul><!-- E 学生列表 -->
				</div>
			</div>
		</div>
	</div>
</div>
				
<script>
$(document).ready(function(){
	$("#footTabClsClock").addClass('active').siblings().removeClass('active');
	$("#clock-tab-type").val(2);
	var	courseUrl = "${request.contextPath}/eccShow/eclasscard/standard/showindex/stuschedule?type=2&status=${teaclzAttence.status?default(0)}&scheduleId=${classAttence.courseScheduleId!}&cardId="+_cardId+"&view="+_view;
	$("#courseDiv").load(courseUrl);
	// 学生列表高度
	$('.checkin-stu-list').css({
		overflow: 'auto',
		height: $(window).height() - $('.checkin-stu-list').offset().top - 175
	});
})
var clockNum = $("#clockNum").text();
var notClockNum = $("#notClockNum").text();
var lateNum = $("#lateNum").text();

//打卡触发
function classAttenceBy(cardNumber){
	if(faceSubmit){
		return;
	}
    $.ajax({
        url:'${request.contextPath}/eccShow/eclasscard/clockIn',
        data:{"cardNumber":cardNumber,"cardId":_cardId,"objectId":"${classAttence.id!}", "clockType":0},
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
            		if(result.status==3){
						showPrompt("signFail");
            		}else{
						showPrompt("signSuc");
            		}
            	} else {
					showPrompt("signFail");
				}
            }else{
            	showMsgTip(result.msg);
				showPrompt("signFail");
            }
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
			showPrompt("signFail");
        }
    });
}

function showTeaSuccess(result){
	var status = result.status;
	if(status==1){
		$("#teacherClockState").addClass('label-fill-bluePurple').text('已签到');
	}else if(status==4){
		$("#teacherClockState").addClass('label-fill-red').text('迟到');
	}
}
function showStuSuccess(result){
	var studentId = result.studentId;
	var status = result.status;
	if(studentId){
		if(status==1){
			clockNum++;
			notClockNum--;
			$("#clockNum").text(clockNum);
			$("#"+studentId).find('.label-fill').addClass('label-fill-bluePurple').text('已签到');
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
</script>
