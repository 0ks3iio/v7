<div class="filter filter-f16">
	<div class="filter-item">
		<span class="filter-name">学年：</span>
		<div class="filter-content">
			<select class="form-control" id="acadyear" name="acadyear" onChange="changeExam()">
				<#if acadyearList?exists && (acadyearList?size>0)>
		        	<#list acadyearList as item>
			        	<option value="${item!}" <#if semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
		            </#list>
	            <#else>
		            <option value="">未设置</option>
	            </#if>
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">学期：</span>
		<div class="filter-content">
			<select class="form-control" id="searchSemester" name="searchSemester" onChange="changeExam()">
				${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">考试名称：</span>
		<div class="filter-content">
			<select class="form-control" id="examId" name="examId" onChange="showStatistical()">
				<option value="">--请选择--</option>
			</select>
		</div>
	</div>
</div>

<div class="table-container">
	<div class="table-container-body" id="showStatisticalDiv">
		
	</div>
</div>

<script>
	$(function(){
		changeExam();
	});
	
	function changeExam() {
		var acadyear = $("#acadyear").val();
		var searchSemester = $("#searchSemester").val();
		var examId = $("#examId");
		$.ajax({
			url:"${request.contextPath}/exammanage/edu/examStu/enrollstu/examnamelist",
			data:{"acadyear":acadyear,"searchSemester":searchSemester},
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
				showStatistical();
			}
		});
	}
	
	function showStatistical() {
		var examId = $("#examId").val();
		var url = '${request.contextPath}/exammanage/edu/goodStuStatistics/list?examId='+examId;
		$("#showStatisticalDiv").load(url);
	}
</script>