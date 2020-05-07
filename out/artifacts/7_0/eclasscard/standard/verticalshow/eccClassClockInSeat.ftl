<#--panlf seat 竖排 上课签到-->
<#import "${request.contextPath}/eclasscard/standard/verticalshow/macro/eccStuListMacro.ftl" as stuList />
<div id="courseDiv" class="box">
	<div class="box-body no-padding" style="height:327px;">
		<div class="checkin-container">
			<!-- S 课程预告 -->
			<div class="checkin-course">
				<ul class="course-list"></ul>
			</div><!-- E 课程预告 -->
		</div>
	</div>
</div>
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
			<@stuList.tableSeat rowNumbers colNumbers spaceNoArr/>
		</div>
	</div>
</div>

<script>
	//如果没有找到座位表 放到最后一排
	var rowOtherIndex=0;
	var allIndexCol=${colNumbers};
	var colIndex=allIndexCol+1;
	var rowNumber=${rowNumbers};
	$(document).ready(function(){
		$("#footTabClsClock").addClass('active').siblings().removeClass('active');
		$("#clock-tab-type").val(2);
		var	courseUrl = "${request.contextPath}/eccShow/eclasscard/standard/showindex/stuschedule?type=2&status=${teaclzAttence.status?default(0)}&scheduleId=${classAttence.courseScheduleId!}&cardId="+_cardId+"&view=2";
		$("#courseDiv").load(courseUrl);
		// 学生列表高度
		$('.container-parent').css({
			overflow: 'auto',
			height: $(window).height() - $('.container-parent').offset().top - 260
		});

		<#if eccStuclzAttences?exists&&eccStuclzAttences?size gt 0>
			<#list eccStuclzAttences as item>
				var myhtml='';
				<#if item.showPictrueUrl?exists>
				myhtml=myhtml+'<div class="checkin-stu-avatar"><img src="${request.contextPath}${item.showPictrueUrl!}"></div>';
				<#elseif item.sex?exists&&item.sex==2>
				myhtml=myhtml+'<div class="checkin-stu-avatar"><img src="${request.contextPath}/static/eclasscard/show/images/user-female.png"></div>';
				<#else>
				myhtml=myhtml+'<div class="checkin-stu-avatar"><img src="${request.contextPath}/static/eclasscard/show/images/user-male.png"></div>';
				</#if>
				myhtml=myhtml+'<span class="checkin-stu-name">${item.stuRealName?default("（已删除）")}<#if item.status ==3><span class="checkin-flag fa-ads">假</span></#if></span>';
				<#if item.status ==1||item.status ==3>
				myhtml=myhtml+'<span class="label faa-font radius">未签到</span>';
				<#elseif item.status ==2>
				myhtml=myhtml+'<span class="label label-fill-red radius">迟到</span>';
				<#elseif item.status ==4>
				myhtml=myhtml+'<span class="label label-fill-bluePurple radius">已签到</span>';
				</#if>
				<#if item.rowNo gt 0 && item.colNo gt 0 >
				if($('.container-list td[data-row-col="${item.rowNo}_${item.colNo}"]').length>0){
					$('.container-list td[data-row-col="${item.rowNo}_${item.colNo}"]').attr("data-id","${item.studentId!}");
					$('.container-list td[data-row-col="${item.rowNo}_${item.colNo}"]').find("a").append(myhtml);
				}else{
					if(colIndex>allIndexCol){
						rowOtherIndex++;
						addMyRow();
						colIndex=1;
					}
					$('.container-list td[data-row-col="other_'+rowOtherIndex+'_'+colIndex+'"]').attr("data-id","${item.studentId!}");
					$('.container-list td[data-row-col="other_'+rowOtherIndex+'_'+colIndex+'"]').find("a").append(myhtml);
					colIndex++;
				}
				<#else>
				if(colIndex>allIndexCol){
					rowOtherIndex++;
					addMyRow();
					colIndex=1;
				}
				$('.container-list td[data-row-col="other_'+rowOtherIndex+'_'+colIndex+'"]').attr("data-id","${item.studentId!}");
				$('.container-list td[data-row-col="other_'+rowOtherIndex+'_'+colIndex+'"]').find("a").append(myhtml);
				colIndex++;
				</#if>
			</#list>
		</#if>
		if(rowOtherIndex>0){
			//有添加
			initCols(rowNumber);
		}
		// 监听滚轮事件
		$('.container-parent').scrollTop($('.container-parent')[0].scrollHeight);
	})

	function addMyRow(){
		var trHtml=addFirstRow(rowOtherIndex);
		rowNumber++;
		$('.container-list table tbody .space-td').attr('rowspan',rowNumber);
		$('.container-list table tbody .col_umber').attr('rowspan',rowNumber);
		$('.container-list table tbody').prepend(trHtml);
	}

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
				$('td[data-id="'+studentId+'"] a').find('.label').removeClass("faa-font").addClass('label-fill-bluePurple').text('已签到');
			}else if(status==4){
				lateNum++;
				notClockNum--;
				$("#lateNum").text(lateNum);
				$('td[data-id="'+studentId+'"] a').find('.label').removeClass("faa-font").addClass('label-fill-red').text('迟到');
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