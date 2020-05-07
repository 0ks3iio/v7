<title>虚拟课程管理</title>
<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
			<li class="active" role="presentation"><a href="javascript:void(0)" role="tab" data-toggle="tab" onclick="loadvirtual(0)">虚拟课程设置</a></li>
			<li role="presentation"><a href="javascript:void(0)" role="tab" data-toggle="tab" onclick="loadvirtual(1)">走班时间点调整</a></li>
		</ul>
		<div class="tab-content" id="virtual">
		</div>
	</div>
</div>
					
<script>

function loadvirtual(type){
	var url="";
	if(type==0){
		url = '${request.contextPath}/basedata/unit/course/head/page?type=3';
	}else{
		url = '${request.contextPath}/basedata/classhour/index/page';
	}
	$("#virtual").load(url);
}

$(function(){
	loadvirtual(0);
});
</script>