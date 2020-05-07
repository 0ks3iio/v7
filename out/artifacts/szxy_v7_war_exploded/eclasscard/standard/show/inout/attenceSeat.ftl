<#import "${request.contextPath}/eclasscard/standard/show/macro/eccStuListMacro.ftl" as stuList />
<div class="grid">
	<div class="grid-cell space-side">
		<div class="box">
			<div class="box-body updown-box" id="inoutfaceBox">
                <div class="updown-sign <#if inoutType?default(0)==2>active</#if>">
                    <div class="">
                        <div class="time-box">${timeStr!}</div>
                        <div class="text-box">${periodGrade!}</div>
                    </div>
                </div>
                <!-- S 考勤数据 -->
                <ul class="checkin-info">
                    <li><span>应到人数：</span><span><em>${sumNum}</em>人</span></li>
                    <li><span>实到人数：</span><span><em id="clockNum">${clockNum}</em>人</span></li>
                    <li><span>未到人数：</span><span><em id="notClockNum">${notClockNum}</em>人</span></li>
                </ul><!-- E 考勤数据 -->
                <#if isActivate>
               		 <button class="btn btn-lg btn-block margin-t-20" onclick='inoutFaceClock()'>刷 脸 签 到</button>
                </#if>
			</div>
		</div>
	</div>
	<input type="hidden" id="inoutPeriodId" value="${periodId!}">
	<!-- S 学生列表 -->
	<div class="grid-cell space-main">
		<div class="box">
			<div class="box-body">
				<div class="checkin-container">
					<@stuList.tableSeat rowNumbers colNumbers spaceNoArr/>
				 </div>
			</div>
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
	$("#footTabInOutAttence").addClass('active').siblings().removeClass('active');
	$("#clock-tab-type").val(7);
	// 学生列表高度
	$('.checkin-stu-list').css({
		overflow: 'auto',
		height: $(window).height() - $('.checkin-stu-list').offset().top - 175
	});
	$('.container-parent').css({
		overflow: 'auto',
		height: $('.checkin-stu-list').height() - 100 
	})
	$('.container-parent .gradient').css({
		top:'5px'
	})
	
	$('.updown-box').css({
    	overflow: 'auto',
    	height: $(window).height() - 270
    });
   
    <#if studentWithInfoList?exists&&studentWithInfoList?size gt 0>
		<#list studentWithInfoList as item>
			var myhtml='';
			<#if item.showPictrueUrl?exists>
			myhtml=myhtml+'<div class="checkin-stu-avatar"><img src="${request.contextPath}${item.showPictrueUrl!}"></div>';
			<#elseif item.student.sex?exists&&item.student.sex==2>
			myhtml=myhtml+'<div class="checkin-stu-avatar"><img src="${request.contextPath}/static/eclasscard/show/images/user-female.png"></div>';
			<#else>
			myhtml=myhtml+'<div class="checkin-stu-avatar"><img src="${request.contextPath}/static/eclasscard/show/images/user-male.png"></div>';
			</#if>
			myhtml=myhtml+'<span class="checkin-stu-name">${item.student.studentName?default("（已删除）")}</span>';
			<#if item.status?default(0) ==4>
			myhtml=myhtml+'<span class="label  label-fill-bluePurple radius">已签到</span>';
			<#else>
			myhtml=myhtml+'<span class="label faa-font radius">未签到</span>';
			</#if>
			<#if item.rowNo gt 0 && item.colNo gt 0 >	
				if($('.container-list td[data-row-col="${item.rowNo}_${item.colNo}"]').length>0){
        			$('.container-list td[data-row-col="${item.rowNo}_${item.colNo}"]').attr("data-id","${item.student.id!}");
        			$('.container-list td[data-row-col="${item.rowNo}_${item.colNo}"]').find("a").append(myhtml);
        		}else{
        			if(colIndex>allIndexCol){
        			  	rowOtherIndex++;
        				addMyRow();
        				colIndex=1;
        			}
        			$('.container-list td[data-row-col="other_'+rowOtherIndex+'_'+colIndex+'"]').attr("data-id","${item.student.id!}");
    				$('.container-list td[data-row-col="other_'+rowOtherIndex+'_'+colIndex+'"]').find("a").append(myhtml);
    				colIndex++;
        		}
        	<#else>
        		if(colIndex>allIndexCol){
    			  	rowOtherIndex++;
    				addMyRow();
    				colIndex=1;
    			}
    			$('.container-list td[data-row-col="other_'+rowOtherIndex+'_'+colIndex+'"]').attr("data-id","${item.student.id!}");
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
	if($('td[data-id="'+studentId+'"]').length==0){
		return;
	}
	var status = result.status;
	if(studentId){
		if(status==1){
			clockNum++;
			notClockNum--;
			$("#clockNum").text(clockNum);
			$('td[data-id="'+studentId+'"]').find('.label').removeClass("faa-font").addClass('label-fill-bluePurple').text('已签到');
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

   
