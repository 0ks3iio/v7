<div class="box">
	<div class="box-body no-padding">
		<div class="checkin-container updown-box">
		     <div class="updown-sign <#if inoutType?default(0)==2>active</#if>">
                <div class="">
                    <div class="time-box">${timeStr!}</div>
                    <div class="text-box">${periodGrade!}</div>
                </div>
            </div>
		</div>
	</div>
</div>
<input type="hidden" id="inoutPeriodId" value="${periodId!}">
<div class="box">
	<div class="box-body">
		<div class="checkin-container">
			<!-- S 考勤数据 -->
			<ul class="checkin-info">
				<li>应到人数：<em>${sumNum}</em>人</li>
            	<li>实到人数：<em id="clockNum">${clockNum}</em>人</li>
            	<li>未到人数：<em id="notClockNum">${notClockNum}</em>人</li>
            	<#if isActivate><li> <a class='btn margin-t-20' onClick='inoutFaceClock()'>刷 脸 签 到</a></li></#if>
            	 
			</ul>
			
			<!-- S 学生列表 -->
			<ul class="checkin-stu-list width-son-140">
				<#if studentWithInfoList?exists&&studentWithInfoList?size gt 0>
      				<#list studentWithInfoList as item>
					<li id="${item.student.id!}" class="checkin-animate">
						<a class="js-openCheckin" href="javascript:void(0);">
							<#if item.showPictrueUrl?exists>
							<div class="checkin-stu-avatar"><img src="${request.contextPath}${item.showPictrueUrl!}"></div>
							<#elseif item.student.sex?exists&&item.student.sex==2>
							<div class="checkin-stu-avatar"><img src="${request.contextPath}/static/eclasscard/show/images/user-female.png"></div>
							<#else>
							<div class="checkin-stu-avatar"><img src="${request.contextPath}/static/eclasscard/show/images/user-male.png"></div>
							</#if>
							<span class="checkin-stu-name">${item.student.studentName?default("（已删除）")}</span>
							<#if item.status?default(0) ==4>
							<span class="label label-fill label-fill-bluePurple">已签到</span>
							<#else>
							<span class="label label-fill">未签到</span>
							</#if>
						</a>
					</li>
					</#list>
    			</#if>
			</ul><!-- E 学生列表 -->
		</div>
	</div>
</div>

	
<script>

$(document).ready(function(){
	$("#footTabInOutAttence").addClass('active').siblings().removeClass('active');
	$("#clock-tab-type").val(7);
	// 学生列表高度
	$('.checkin-stu-list').css({
		overflow: 'auto',
		height: $(window).height() - $('.checkin-stu-list').offset().top - 170
	});
})
function inoutFaceClock(){
	//不验证时间 因为进到这个页面前提就是当前是打卡时间
	openFaceView();
}


var clockNum = $("#clockNum").text();
var notClockNum = $("#notClockNum").text();

//打卡触发
function inoutAttenceBy(cardNumber){
	if(faceSubmit){
		return;
	}
    $.ajax({
        url:'${request.contextPath}/eccShow/eclasscard/clockIn',
        data:{"cardNumber":cardNumber,"cardId":_cardId,"objectId":"${periodId!}", "clockType":0,"type":1},
        type:'post',
        success:function(data){
            var result = JSON.parse(data);
            if(result.haveStu){
            	showStuClockMsg(result);
            	if(result.status!=2){
            		showInoutStuSuccess(result);
            		if(result.status==3){
						showPrompt("signFail");
            		}else{
						showPrompt("signSuc");
            		}
            	} else {
					showPrompt("signFail");
					if(result.msg=="超过考勤时间"){
						//回到首页
						setTimeout(function(){
    						var freshUrl="${request.contextPath}/eccShow/eclasscard/showIndex?cardId="+_cardId+"&view="+_view;
							location.href = freshUrl;
    					},2000);
					}
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

function showInoutStuSuccess(result){
	var studentId = result.studentId;
	if($("#"+studentId).length==0){
		return;
	}
	var status = result.status;
	if(studentId){
		if(status==1){
			clockNum++;
			notClockNum--;
			$("#clockNum").text(clockNum);
			$("#"+studentId).find('.label-fill').addClass('label-fill-bluePurple').text('已签到');
		}
		$("#notClockNum").text(notClockNum);
	}
}
function showOtherCardInoutAttance(data){
	$.each(data, function(index, json) {
		showInoutStuSuccess(json);
	});
}
</script>
