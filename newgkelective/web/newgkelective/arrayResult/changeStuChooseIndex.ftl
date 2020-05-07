<div class="filter">
	<div class="filter-item">
		<span class="filter-name">学生：</span>
		<div class="filter-content chosenClassHeaderClass">
			<select vtype="selectOne" id="stuId" data-placeholder="选择学生" onchange="doChangeStu()">
				<#if studentList?? && (studentList?size>0)>
					<option value=""></option>
					<#list studentList as item>
						<option value="${item.id}" >${item.studentCode?default('无学号')}-${item.studentName?default('')}</option>
					</#list>
				<#else>
					<option value="">暂无数据</option>
				</#if>
			</select>
		</div>
	</div>
	<div class="filter-item chosenSubjectHeaderClass">
		<span class="filter-name">选课调整：</span>
		<div class="filter-content">
			<select multiple vtype="selectMore" id="searchSubject" data-placeholder=" " onchange="findByCondition()">
				<#if courseList?? && (courseList?size>0)>
					<#list courseList as item>
						<option value="${item.id}" >${item.subjectName}</option>
					</#list>
				<#else>
					<option value="">暂无数据</option>
				</#if>
			</select>
		</div>
	</div>
	<div class="detail-div" id="stuChangeShowListDivId">
	
	</div>
</div>
<script type="text/javascript">

$(function(){
	//初始化多选控件
	var viewContent1={
		'width' : '250px',//输入框的宽度
		'multi_container_height' : '33px',//输入框的高度
		'results_height' : '150px',//下拉选择的高度
		'max_selected_options' : '3'//限制3个
	}
	initChosenMore(".chosenSubjectHeaderClass","",viewContent1);
	var viewContent2={
		'width' : '220px',//输入框的宽度
		'multi_container_height' : '33px',//输入框的高度
		'results_height' : '150px',//下拉选择的高度
	}
	initChosenOne(".chosenClassHeaderClass","",viewContent2);
});
function doChangeStu(){
	$("#stuChangeShowListDivId").html("");
	$("#searchSubject option").each(function(){
		$(this).removeAttr("selected");
	});
	$("#searchSubject").trigger("chosen:updated");
	$("#stuChosenSubName").val("");
}
function findByCondition(){
	$("#stuChangeShowListDivId").html("");
	if($("#searchSubject").val() == null || $("#searchSubject").val().length < 3){
		return;
	}
	var studentId = $("#stuId").val();
	if(studentId == ""){
		return;
	}
	var searchSubject = $("#searchSubject").val();
	var searchSubjectIds = "";
	for(var i=0;i<searchSubject.length;i++){
		if(searchSubjectIds == ""){
			searchSubjectIds+=searchSubject[i];
		}else{
			searchSubjectIds+=","+searchSubject[i];
		}
	}

	$("#stuChangeShowListDivId").load("${request.contextPath}/newgkelective/${arrayId!}/arrayResult/toChangeStuChooseList?studentId="+studentId+"&subjectIds="+searchSubjectIds);
	
}
</script>