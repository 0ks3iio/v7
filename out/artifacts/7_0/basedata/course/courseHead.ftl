<div class="tab-pane active">
	<div class="filter">
		<#if type == '1' && !isEdu?default(false)>
			<div class="filter-item">
				<span class="filter-name">课程来源：</span>
				<div class="filter-content">
					<select id="searchCourseSource" class="form-control" onchange="refreshPage()">
						<option value="">请选择</option>
						<option value="2">本单位</option>
						<option value="1">系统内置</option>
					</select>
				</div>
			</div>
		</#if>
		<#if type=='2'>
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
		<#if type!='3'>
		<div class="filter-item">
			<span class="filter-name">课程类型：</span>

			<div class="filter-content">
				<select id="searchType" name="courseTypeId" class="form-control" onchange="refreshPage()">
					<option value="">请选择</option>
					<#if courseTypeList?exists && courseTypeList?size gt 0>
						<#list courseTypeList as item>
							<option value="${item.id}">${item.name}</option>
						</#list>
					</#if>
				</select>
			</div>
		</div>
		</#if>
		<div class="filter-item">
			<span class="filter-name">课程名称：</span>
			<div class="filter-content">
				<div class="input-group">
					<input type="text" class="form-control" id="searchName" name="searchName">
					<a href="javascript:;" id="search" class="input-group-addon"><span><i class="fa fa-search"></i></span></a>
				</div>
			</div>
		</div>
		<div class="filter-item filter-item-right">
			<#if type!='3'>
			<a class="btn btn-white" id="importExcel">导入</a>
			</#if>  
			<a class="btn btn-danger">删除</a>
			<a href="javascript:;" class="btn btn-blue js-addCourse">新建<#if type=='3'>虚拟</#if>课程</a>
		</div>
	</div>
	<div id="showListDiv"></div>
</div>
<script>
$(function(){
	refreshPage();
	//导入Excel
	$('#importExcel').on('click',function(){
		var url = '${request.contextPath}/basedata/import/course/index?type=${type!}';
		$('#aa').load(url);
	});
	//查询
	$('#search').on('click',function(){
		refreshPage();
	});
})

function refreshPage(){
	var searchCourseSource = $('#searchCourseSource').val();
	if(!searchCourseSource){
		searchCourseSource="";
	}
	var searchType = $('#searchType').val();
	if(!searchType){
		searchType="";
	}
	var searchName = $('#searchName').val();
	if(!searchName){
		searchName="";
	}
	var xxType = $('#xxType').val();
	if(!xxType){
		xxType="";
	}
	var url = '${request.contextPath}/basedata/unit/course/list/page?type=${type!}&searchType='+searchType+'&courseSource='+searchCourseSource+'&searchName='+searchName+'&xxType='+xxType;
	$("#showListDiv").load(url);
}

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
				refreshPage();
			}
		}
	});
}
</script>