<title>科目管理</title>
<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
			<li class="active" role="presentation"><a href="javascript:void(0)" role="tab" data-toggle="tab" onclick="toAllCourses(0)">课程模块</a></li>
			<li role="presentation"><a href="javascript:void(0)" role="tab" data-toggle="tab" onclick="toAllCourses(1)">必修课</a></li>
			<#if !(isDj?default(true))>
				<li role="presentation"><a href="javascript:void(0)" role="tab" data-toggle="tab" onclick="toAllCourses(2)">选修课</a></li>
			</#if>
		</ul>
		<div class="tab-content" id="aa">
		</div>
	</div>
</div>
					
<script>
$(function(){
	toAllCourses(0);
});

function toAllCourses(type){
	var url="";
	if(type==0){
		url = '${request.contextPath}/basedata/subject/head/page';
	}else{
		url = '${request.contextPath}/basedata/unit/course/head/page?type='+type;
	}
	$("#aa").load(url);
}
</script>