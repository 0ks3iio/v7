<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist" id="tablist">
			<li class="active" role="presentation"><a onclick="getRes('1')" href="javascript:void(0)" role="tab" data-toggle="tab">选课资源</a></li>
			<li  role="presentation"><a onclick="getRes('2')" href="javascript:void(0)" role="tab" data-toggle="tab">分班资源</a></li>
			<li  role="presentation"><a onclick="getRes('3')" href="javascript:void(0)" role="tab" data-toggle="tab">排课资源</a></li>
		</ul>
		<div class="tab-content">
			<div id="aa" class="tab-pane active" role="tabpanel">
				<#-- 加载内容 -->
				
			</div>
		</div>
	</div>
</div>
<script>

function getRes(type){
	var url = "";
	if(type == '1'){
		url = "${request.contextPath}/newgkelective/report/choice/index/page?gradeId=${gradeId!}";
	}else if(type == '2'){
		url = "${request.contextPath}/newgkelective/report/divide/index/page?gradeId=${gradeId!}";
	}else if(type == '3'){
		url = "${request.contextPath}/newgkelective/edu/baseItem/upload/page?gradeId=${gradeId!}";
	}else{
		alert("type:"+type+"不存在");
		return;
	}
	
	if(!url) return;
	$("#aa").load(url);
}

function goBack() {
	var url =  '${request.contextPath}/newgkelective/index/list/page';
	$("#showList").load(url);
}
showBreadBack(goBack,false,"资源上报");

$(function(){
	getRes('1');
});
</script>