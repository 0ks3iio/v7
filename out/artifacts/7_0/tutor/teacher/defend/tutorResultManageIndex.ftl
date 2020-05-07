<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
			<li class="active" role="presentation"><a href="#tabList" onclick="showTab('1')" role="tab" data-toggle="tab">教师管理</a></li>
			<li role="presentation"><a href="#tabList" onclick="showTab('2')" role="tab" data-toggle="tab">学生管理</a></li>
		</ul>
		<div class="tab-content">
			<div id="tabList" class="tab-pane active" role="tabpanel">

			</div>
		</div>
	</div>
</div>
<script>
$(function(){
	showTab('1');
});
function showTab(type){
	if(type=='1'){
		var url =  '${request.contextPath}/tutor/result/teacher/head';
		$("#tabList").load(url);
	}else{
		var url =  '${request.contextPath}/tutor/result/student/head';
		$("#tabList").load(url);
	}

}
</script>