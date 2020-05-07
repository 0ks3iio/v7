<div class="filter">
	<#if subjectType == "1">
		<div class="filter-item">
			<span class="filter-name">课程来源：</span>
			<div class="filter-content">
				<select id="searchCourseSource" class="form-control" onchange="getCourseList()">
					<option value="">请选择</option>
					<option value="2">本单位</option>
					<option value="1">系统内置</option>
				</select>
			</div>
		</div>
	<#else>
		<div class="filter-item">
			<span class="filter-name">选修类型：</span>
			<div class="filter-content">
				<select name="xxType" id="xxType" class="form-control" onChange="changeXxType()">
					<option value="">请选择</option>
					<option value="2">选修</option>
					<option value="4">选修Ⅰ-A</option>
					<option value="5">选修Ⅰ-B</option>
					<option value="6">选修Ⅱ</option>
				</select>
			</div>
		</div>
	</#if>
	<div class="filter-item">
		<span class="filter-name">课程类型：</span>
		<div class="filter-content">
			<select id="searchType" name="courseTypeId" class="form-control" onchange="getCourseList()">
				<option value="">请选择</option>
				<#if courseTypeList?exists && (courseTypeList?size>0)>
					<#list courseTypeList as item>
						<option value="${item.id}"> ${item.name} </option>
					</#list>
				</#if>
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">课程名称：</span>
		<div class="filter-content">
			<div class="input-group">
				<input type="text" class="form-control" id="searchName"  name="seachName" value="${seachSubName!}">
				<a href="javascript:;" id="searchCourse" class="input-group-addon"><span><i class="fa fa-search"></i></span></a>
			</div>
		</div>
	</div>
</div>
<div id="courseList" >

</div>
<script>
$(function(){
	getCourseList();
});

function getCourseList(){
	var courseSource = $("#searchCourseSource").val(); 
	var searchType = $("#searchType").val(); 
	if(courseSource == undefined) {
		courseSource="";
	}
	var searchName = $("#searchName").val();
	var xxType = $("#xxType").val();
	if(xxType==undefined){
		xxType="";
	}
	var url = "${request.contextPath}/basedata/courseopen/class/add/detail/page?acadyear=${acadyear!}&semester=${semester!}&gradeId=${gradeId!}&classId=${classId!}&subjectType=${subjectType}&courseSource="
	 + courseSource + "&searchType=" + searchType +"&searchName=" + searchName+ "&xxType=" +xxType;
	url = encodeURI(encodeURI(url));
	$("#courseList").load(url);
}

$('#searchCourse').on('click',function(){
	getCourseList();
});

function changeXxType(){
	var type=$("#xxType").val(); 
	var searchType = $("#searchType");
	$.ajax({
		url:"${request.contextPath}/basedata/course/getCourseTypeList",
		data:{type:type},
		dataType: "json",
		success: function(json){
			searchType.html("");
			var searchTypeHtml='<option value="">请选择</option>';
			if(json && json.length>0){
				for(var i=0;i<json.length;i++){
					searchTypeHtml+='<option value="'+json[i].id+'">';
					searchTypeHtml+=json[i].courseTypename;
					searchTypeHtml+='</option>';
				}
				searchType.append(searchTypeHtml);
				getCourseList();
			}
		}
	});
}

</script>

