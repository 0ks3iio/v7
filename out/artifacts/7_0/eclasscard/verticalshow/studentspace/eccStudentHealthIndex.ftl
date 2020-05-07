<h2 class="space-stu-title">健康数据</h2>

<div class="health-toolbar clearfix">
	<div class="btn-group pull-left">
		<button class="btn" onclick="healthTime('-1')">&lt;</button>
		<button class="btn" style="margin-left:20px;" onclick="healthTime('1')">&gt;</button>
	</div>
	<input type='hidden' id='numberOfTime' value=''/>
	<span id='healthDate' class="health-date">4/28-5/05</span>
	<div class="btn-group pull-right">
		<button id="data1" class="btn" onclick="findStuHealthData('1')">日视图</button>
		<button id="data2" class="btn active" onclick="findStuHealthData('2')">周视图</button>
		<button id="data3" class="btn" onclick="findStuHealthData('3')">月视图</button>
	</div>
</div>
<div id='stuHealthDataDiv'>
					
</div>
<script>
	$(document).ready(function(){
		healthTime('0');
	})
	
	function findStuHealthData(dateType) {
		$("button[class='btn active']").attr("class","btn");
		$("#data"+dateType).attr("class","btn active");
		$("#numberOfTime").val("");
		healthTime('0');
	}
	
	function healthTime(num){
		var dateType = $("button[class='btn active']").attr("id").substring(4);
		var numberOfTime = $("#numberOfTime").val();
		$.ajax({
			url:'${request.contextPath}/eccShow/eclasscard/dateOfWeek',
			data: {'dateType':dateType,'numberOfTime':numberOfTime,'num':num},
			type:'post',
			success:function(data) {
				$("#numberOfTime").val(data[0]);
				$("#healthDate").text(data[1]);
				var userId = "${userId!}";
				var view = "${view!}";
				$("#stuHealthDataDiv").load("${request.contextPath}/eccShow/eclasscard/stuHealthData/page?userId="+userId+"&dateType="+dateType+"&startTime="+data[0]+"&endTime="+data[2]+"&view="+view);
			},
 			error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
</script>