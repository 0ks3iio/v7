<div class="box-header">
	<ul class="tabs">
		<li class="active"><a href="javascript:void(0);" data-action="tab">健康数据</a></li>
	</ul>
</div>
<div class="box-body scroll-container">
<div class="space-content">
<div class="tab-content">
<div class="tab-pane active" role="tabpanel">
<div class="health-toolbar clearfix">
	<div class="btn-group pull-left margin-left-100">
		<button class="btn btn-100" onclick="healthTime('-1')">&lt;</button>
	</div>
	<input type='hidden' id='numberOfTime' value=''/>
	<span id='healthDate' class="health-date"></span>
	<div class="btn-group pull-right">
		<button id="data1" class="btn" onclick="findStuHealthData('1')">日视图</button>
		<button id="data2" class="btn active" onclick="findStuHealthData('2')">周视图</button>
		<button id="data3" class="btn" onclick="findStuHealthData('3')">月视图</button>
	</div>
	<div class="btn-group pull-right margin-right-100">
		<button class="btn btn-100" onclick="healthTime('1')">&gt;</button>
	</div>
</div>
<div id='stuHealthDataDiv'>
					
</div>
</div>
</div>
</div>
</div>
<script>
	$(document).ready(function(){
		$('.scroll-container').each(function(){
			$(this).css({
				overflow: 'auto',
				height: $(window).height() - $(this).offset().top - 160
			});
		});
		
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
			url:'${request.contextPath}/eccShow/eclasscard/standard/dateOfWeek',
			data: {'dateType':dateType,'numberOfTime':numberOfTime,'num':num},
			type:'post',
			success:function(data) {
				$("#numberOfTime").val(data[0]);
				$("#healthDate").text(data[1]);
				var userId = "${userId!}";
				$("#stuHealthDataDiv").load("${request.contextPath}/eccShow/eclasscard/standard/stuHealthData/page?userId="+userId+"&dateType="+dateType+"&startTime="+data[0]+"&endTime="+data[2]+"&view="+_view);
			},
 			error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
</script>