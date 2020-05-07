<div class="main-content-inner">
	<ol class="breadcrumb hidden "></ol>
	<div class="page-content">
		<div class="row">
			<div class="col-xs-12">
				<div class="box box-default">
					<div class="box-header">
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
									<select name="examId" id="examId" class="form-control" onChange="findStudentsScores()">
										<option value='' >--请选择--</option>
									</select>
								</div>
							</div>
							<div class="filter-item">
								<span class="filter-name">班级：</span>
								<div class="filter-content">
									<select name="classId" id="classId" class="form-control" onChange="findStudentsScores()">
										<option value='' >--请选择--</option>
									</select>
								</div>
							</div>
						</div>
					</div>
					<div class="box-body clearfix" id="examStudentList" style="overflow:auto;">
									    
					</div>
				</div>
			</div><!-- /.col -->
		</div><!-- /.row -->
	</div><!-- /.page-content -->
</div>
<script>
	$(function(){
		changeExam();
	});
	
	function changeExam() {
		var gradeId = $("#gradeId").val();
		var examId=$("#examId");
		$.ajax({
			url:"${request.contextPath}/examanalysis/findExamIdByGradeId",
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
				changeClazz();
			}
		});
	}
	
	function changeClazz(){
		var gradeId = $("#gradeId").val();
		var classId=$("#classId");
		$.ajax({
			url:"${request.contextPath}/examanalysis/findClassIdByGradeId",
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
				findStudentsScores();
			}
		});
	}
	
	function findStudentsScores() {
		var gradeId = $("#gradeId").val();
		var examId = $("#examId").val();
		var classId = $("#classId").val();
		var str = "?examId="+examId+"&gradeId="+gradeId+"&classId="+classId;
		var url='${request.contextPath}/examanalysis/examStudent/List/page'+str;
		$("#examStudentList").load(url);
	}
</script>