<div class="page-content">
    <div class="box box-default">
    	<div class="box-body">
    		<div class="filter">
    			<div class="filter-item">
    				<span class="filter-name">年级：</span>
    				<div class="filter-content">
						<select name="gradeId" id="gradeId" class="form-control" onChange="changeExam()">
							<#if gradeList?exists && (gradeList?size>0)>
								<#list gradeList as grade>
									<option value="${grade.id!}">${grade.gradeName!}</option>
								</#list>
							<#else>
								<option value="">---请选择---</option>
							</#if>
						</select>
					</div>
    			</div>
    			<div class="filter-item">
    				<span class="filter-name">考试名称：</span>
    				<div class="filter-content">
    					<select name="examId" id="examId" class="form-control" onChange="changeSubject()">
							<option value="">---请选择---</option>
						</select>
    				</div>
    			</div>
    			<div class="filter-item">
    				<span class="filter-name">科目：</span>
    				<div class="filter-content">
    					<select name="subjectId" id="subjectId" class="form-control" onChange="findQueriesList()">
							<option value="">---请选择---</option>
						</select>
    				</div>
    			</div>
    			<div class="filter-item">
					<div class="filter-content">
						<div class="input-group input-group-search">
							<select name="queryStuType" id="queryStuType" class="form-control" onChange='stuTypeChange()'>
								<option value="0">姓名</option>
								<option value="1">学号</option>
								<option value="2">身份证号</option>
							</select>
							<div class="pos-rel pull-left">
								<input type="text" id="queryStuContent" name="queryStuContent" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="请输入姓名" onkeydown="dispResStudent()">
							</div>
							<div class="input-group-btn">
								<button type="button" class="btn btn-default" onclick="findStuResult()">
									<i class="fa fa-search"></i>
								</button>
							</div>
						</div><!-- /input-group -->
					</div>
				</div>
    		</div>
    		<div id="examGradeList">
							
			</div>	
    	</div>
    </div>
</div>
<script>
	$(function(){
		changeExam();
	});

	function changeExam() {
		var gradeId = $("#gradeId").val();
		var examId=$("#examId");
		$.ajax({
			url:"${request.contextPath}/examquery/examGrade/examList",
			data:{"gradeId":gradeId},
			dataType: "json",
			success: function(data){
				examId.html("");
				examId.chosen("destroy");
				if(data.length==0){
					examId.append("<option value='' >--请选择--</option>");
				}else{
					for(var i = 0; i < data.length; i ++){
						examId.append("<option value='"+data[i].id+"' >"+data[i].examName+"</option>");
					}
				}
				$(examId).chosen({
					width:'200px',
					no_results_text:"未找到",//无搜索结果时显示的文本
					allow_single_deselect:true,//是否允许取消选择
					disable_search:false, //是否有搜索框出现
					search_contains:true,//模糊匹配，false是默认从第一个匹配
					//max_selected_options:1 //当select为多选时，最多选择个数
				}); 
				changeSubject();
			}
		});
	}
	
	function changeSubject() {
		var examId=$("#examId").val();
		var subjectId = $("#subjectId");
		$.ajax({
			url:"${request.contextPath}/examquery/examGrade/subjectList",
			data:{"examId":examId},
			dataType: "json",
			success: function(data){
				subjectId.html("");
				subjectId.chosen("destroy");
				if(data.length==0){
					subjectId.append("<option value='' >--请选择--</option>");
				}else{
					subjectId.append("<option value='all' >全部</option>");
					for(var i = 0; i < data.length; i ++){
						subjectId.append("<option value='"+data[i].subjectId+"' >"+data[i].courseName+"</option>");
					}
				}
				$(subjectId).chosen({
					width:'130px',
					no_results_text:"未找到",//无搜索结果时显示的文本
					allow_single_deselect:true,//是否允许取消选择
					disable_search:false, //是否有搜索框出现
					search_contains:true,//模糊匹配，false是默认从第一个匹配
					//max_selected_options:1 //当select为多选时，最多选择个数
				}); 
				findQueriesList();
			}
		});
	}

	function findQueriesList() {
		$("#queryStuContent").val("");
		var gradeId = $("#gradeId").val();
		var examId = $("#examId").val();
		var subjectId = $("#subjectId").val();
		var str = "?examId="+examId+"&subjectId="+subjectId+"&gradeId="+gradeId;
		var url='${request.contextPath}/examquery/examGrade/queriesList'+str;
		$("#examGradeList").load(url);
	}

	function dispResStudent() {
		var x;
    	if(window.event) // IE8 以及更早版本
    	{	x=event.keyCode;
    	}else if(event.which) // IE9/Firefox/Chrome/Opera/Safari
    	{
       		x=event.which;
    	}
    	if(13==x){
        	findStuResult();
    	}
	}
	
	function findStuResult() {
		var gradeId = $("#gradeId").val();
		var examId = $("#examId").val();
		var subjectId = $("#subjectId").val();
		var queryType = $("#queryStuType").val();
		var queryName = $("#queryStuContent").val();
		var str = "?examId="+examId+"&subjectId="+subjectId+"&gradeId="+gradeId+"&queryType="+queryType+"&queryName="+queryName;
		var url='${request.contextPath}/examquery/examGrade/queriesList'+str;
		url=encodeURI(encodeURI(url));
		$("#examGradeList").load(url);
	}
	
	function stuTypeChange() {
		var queryType = $("#queryStuType").val();
		var queryTypeName = $("#queryStuType :selected").text();
		$("#queryStuContent").attr("placeholder","请输入"+queryTypeName);
	}
</script>