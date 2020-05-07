<!-- 签到状态弹框 -->
<div class="layer-checkin">
	<div class="layer-checkin-header">
		<img id="tipHeaderImg" src="${request.contextPath}/static/eclasscard/show/images/avatar-stu.png" alt="" class="stu-avatar">
		<h4 id="tipStuRealName"></h4>
		<a href="" class="layer-checkin-close">&times;</a>
	</div>
	<table class="table">
		<tbody>
			<tr>
				<td id="tipRowOneName"></td>
				<td id="tipRowOneValue"></td>
			</tr>
			<tr>
				<td id="tipRowTwoName"></td>
				<td id="tipRowTwoValue"></td>
			</tr>
			<tr>
				<td id="tipRowThreeName"></td>
				<td id="tipRowThreeValue"></td>
			</tr>
		</tbody>
	</table>
	<div id="tipShowMsgDiv"  >
	
	</div>
	<!-- 
	 -->
</div>
<script>
function showStuClockMsg(result){
	$("#tipHeaderImg").attr('src',"${request.contextPath}"+result.showPictrueUrl);
    $("#tipStuRealName").html(result.stuRealName);
    $("#tipRowOneName").html(result.rowOneName);
    $("#tipRowOneValue").html(result.rowOneValue);
    $("#tipRowTwoName").html(result.rowTwoName);
    $("#tipRowTwoValue").html(result.rowTwoValue);
    $("#tipRowThreeName").html(result.rowThreeName);
    $("#tipRowThreeValue").html(result.rowThreeValue);
    if(result.status==1){
    	$("#tipShowMsgDiv").html('<div class="layer-checkin-state successed"> <i class="icon icon-successed"></i>签到成功</div>')
    }else if(result.status==2){
    	$("#tipShowMsgDiv").html('<div class="layer-checkin-state failed"><i class="icon icon-failed"></i>签到失败，'+result.msg+'</div>')
    }else if(result.status==3){
    	$("#tipShowMsgDiv").html('<div class="layer-checkin-state warning"><i class="icon icon-warning"></i>'+result.msg+'</div>')
    }else{
    	$("#tipShowMsgDiv").html('<div class="layer-checkin-state warning"><i class="icon icon-warning"></i> 签到成功，'+result.msg+'</div>')
    }
    openMsgLayer(result.type);
}
var timeoutID = '';
var indexArr = new Array();
function openMsgLayer(type){
	if(timeoutID!=''){
		clearTimeout(timeoutID);
	}
	var index= layer.open({
		type: 1,
		title: false,
		shade: 0,
		closeBtn: 0,
		shadeClose: true,
		area: '600px',
		content: $('.layer-checkin')
	});
	indexArr.push(index);
	layer.style(index, {
		'border-radius': '15px'
	})
	$('.layer-checkin-close').on('click', function(e){
		e.preventDefault();
		layer.close(index);
	})
	if (type == '41' || type == '42') {
		timeoutID=setTimeout(function(){
			for(j = 0; j < indexArr.length; j++) {
				layer.close(indexArr[j]);
			}
		},5000);
	} else {
		timeoutID=setTimeout(function(){
			for(j = 0; j < indexArr.length; j++) {
				layer.close(indexArr[j]);
			}
		},3000);
	}
}
</script>
