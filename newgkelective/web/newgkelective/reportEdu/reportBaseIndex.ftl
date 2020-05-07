<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist" id="tablist">
			<li class="active" role="presentation"><a onclick="getRes('1')" href="javascript:void(0)" role="tab" data-toggle="tab">场地资源</a></li>
			<li  role="presentation"><a onclick="getRes('2')" href="javascript:void(0)" role="tab" data-toggle="tab">教师资源</a></li>
		</ul>
		<div class="tab-content">
			<div id="aa" class="tab-pane active" role="tabpanel">
				<#-- 加载内容 -->
				
			</div>
		</div>
	</div>
</div>
<script>

function getRes(type,subjectId){
	var url = "";
	//debugger;
	if(type == '1'){
		url = "${request.contextPath}/newgkelective/edu/baseItem/placeRes?unitId=${unitId!}&gradeYear=${gradeYear!}";
	}else if(type == '2'){
		if(!subjectId)
			subjectId = '';
		url = "${request.contextPath}/newgkelective/edu/baseItem/teacherRes?unitId=${unitId!}&gradeYear=${gradeYear!}&subjectId="+subjectId;
	}else{
		alert("type:"+type+"不存在");
		return;
	}
	$("#aa").load(url);
}
$(function(){
	getRes('1');
});
</script>