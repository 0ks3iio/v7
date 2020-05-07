<div class="main-content-inner">
	<ol class="breadcrumb hidden "></ol>
	<div class="page-content">
		<div class="row">
			<div class="col-xs-12">
				<div class="box box-default">
					<div class="box-header">
						<h4 class="box-title">班级成绩查询</h4>
					</div>
					<div class="box-body clearfix">
						<div class="filter filter-f16">
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
									<select name="examId" id="examId" class="form-control" onChange="changeClass()">
										<option value="">---请选择---</option>
									</select>
								</div>
							</div>
							<div class="filter-item">
								<span class="filter-name">班级：</span>
								<div class="filter-content">
									<select name="classId" id="classId" class="form-control" onChange="findQueriesList()">
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
							<div class="filter-item filter-item-right">
								<a href="javascript:;" class="exaRoom-more">更多条件</a>
							</div>	
						</div>
						<div class="filter filter-f16 exaRoom-moreSeach" style="display:none;"> 
							<div class="filter-item">
								<span class="filter-name">显示学生：</span>
								<div class="filter-content">
									<label class="pos-rel">
										<input name="course-checkbox" type="checkbox" class="wp" value="0" onClick="ChangeCheckbox()">
										<span class="lbl">显示作弊生</span>
									</label>
									<label class="pos-rel">
										<input name="course-checkbox" type="checkbox" class="wp" value="1" onClick="ChangeCheckbox()">
										<span class="lbl" style="margin-left:10px;">显示不统分生</span>
									</label>
									<label class="pos-rel">
										<input name="course-checkbox" type="checkbox" class="wp" value="2" onClick="ChangeCheckbox()">
										<span class="lbl" style="margin-left:10px;">显示缺考生</span>
									</label>
								</div>
							</div>
						</div>
						<div id="examClassList">
							
						</div>
					</div>
				</div>
			</div><!-- /.col -->
		</div><!-- /.row -->
	</div><!-- /.page-content -->
</div>
<script>
	$(function(){
		$('.exaRoom-more').click(function(){
			$('.exaRoom-moreSeach').fadeToggle();
		});
		
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
				changeClass();
			}
		});
	}
	
	function changeClass() {
		var gradeId = $("#gradeId").val();
		var classId=$("#classId");
		$.ajax({
			url:"${request.contextPath}/examquery/examClass/classList",
			data:{"gradeId":gradeId},
			dataType: "json",
			success: function(data){
				classId.html("");
				classId.chosen("destroy");
				if(data.length==0){
					classId.append("<option value='' >--请选择--</option>");
				}else{
					for(var i = 0; i < data.length; i ++){
						classId.append("<option value='"+data[i].id+"' >"+data[i].classNameDynamic+"</option>");
					}
				}
				$(classId).chosen({
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
		$('input:checkbox').removeAttr("checked"); 
		var gradeId = $("#gradeId").val(); 
		var examId=$("#examId").val();
		var classId=$("#classId").val();
		var subjectId=$("#subjectId").val();
		var str = "?examId="+examId+"&subjectId="+subjectId+"&gradeId="+gradeId+"&classId="+classId;
		var url='${request.contextPath}/examquery/examClass/queriesList'+str;
		$("#examClassList").load(url); 
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
		$('input:checkbox').removeAttr("checked");
		var gradeId = $("#gradeId").val();
		var examId = $("#examId").val();
		var classId=$("#classId").val();
		var subjectId = $("#subjectId").val();
		var queryType = $("#queryStuType").val();
		var queryName = $("#queryStuContent").val();
		if(queryName==""){
			if (queryType == '0') {
				layerTipMsg(false,"提示","请先输入要查询的姓名！");
			} else if (queryType == '1') {
				layerTipMsg(false,"提示","请先输入要查询的学号！");
			} else {
				layerTipMsg(false,"提示","请先输入要查询的身份证号！");
			}
			return;
		}
		var str = "?examId="+examId+"&subjectId="+subjectId+"&gradeId="+gradeId+"&classId="+classId+"&queryType="+queryType+"&queryName="+queryName;
		var url='${request.contextPath}/examquery/examClass/queriesList'+str;
		url=encodeURI(encodeURI(url));
		$("#examClassList").load(url);
	}
	
	function ChangeCheckbox(){
		var checked = "";
        $('input:checkbox:checked').each(function() {
            checked += $(this).val() + ",";
        });
        checked = checked.substring(0,checked.length-1);
        var gradeId = $("#gradeId").val();
		var examId = $("#examId").val();
		var classId = $("#classId").val();
		var subjectId = $("#subjectId").val();
		var queryType = $("#queryStuType").val();
		var queryName = $("#queryStuContent").val();
		var str = "?examId="+examId+"&subjectId="+subjectId+"&gradeId="+gradeId+"&classId="+classId+"&queryType="+queryType+"&queryName="+queryName+"&checked="+checked;
		var url='${request.contextPath}/examquery/examClass/queriesList'+str;
		url=encodeURI(encodeURI(url));
		$("#examClassList").load(url);
	}
	
	function stuTypeChange() {
		var queryType = $("#queryStuType").val();
		var queryTypeName = $("#queryStuType :selected").text();
		$("#queryStuContent").attr("placeholder","请输入"+queryTypeName);
	}
</script>