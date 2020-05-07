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
			<select class="form-control" id="examId" name="examId" onChange="changeSchool()">
				<option value="">--请选择--</option>
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">学校：</span>
		<div class="filter-content">
			<select class="form-control" id="schoolId" name="schoolId" onChange="showStuList()">
				<option value="">--请选择--</option>
			</select>
		</div>
	</div>
	
</div>

<div class="table-container">
	<div class="filter-item">
		<span class="filter-name">搜索条件：</span>
		<div class="filter-content">
			<select class="form-control" id="seachType" name="seachType">
				<option value="1">考号</option>
				<option value="2">学籍号</option>
				<option value="3">身份证号</option>
				<option value="4">学生姓名</option>
			</select>
		</div>
	</div>
	<div class="filter-item">
        <span class="filter-name">关键字：</span>
        <div class="filter-content">
            <input type="text" id="keyWord" style="width:168px;" class="form-control"  >
        </div>
    </div>
	<div class="filter-item">
        <div class="filter-content text-right">
        	<a href="javascript:" class="btn btn-blue js-setFilter0"  onclick="showStuList()">查询</a>
			<a href="javascript:" class="btn btn-blue js-setFilter0"  onclick="save()">保存</a>
		</div>
	</div>
	<div class="table-container-body" id="stuList">
		
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
				changeSchool();
			}
		});
	}
	
	function changeSchool() {
		var examId = $("#examId").val();
		var schoolId = $("#schoolId");
		$.ajax({
			url:"${request.contextPath}/exammanage/edu/goodStu/schoolnamelist",
			data:{"examId":examId},
			dataType: "json",
			success: function(data){
				schoolId.html("");
				schoolId.chosen("destroy");
				if(data.length==0){
					schoolId.append("<option value='' >--请选择--</option>");
				}else{
					schoolId.append("<option value='all' >全部</option>");
					for(var i = 0; i < data.length; i ++){
						schoolId.append("<option value='"+data[i].id+"' >"+data[i].schoolName+"</option>");
					}
				}
				$(schoolId).chosen({
					width:'200px',
					no_results_text:"未找到",//无搜索结果时显示的文本
					allow_single_deselect:true,//是否允许取消选择
					disabch_contains:true,//模糊匹配，false是默认从第一个匹配
					//mle_search:false, //是否有搜索框出现
					searax_selected_options:1 //当select为多选时，最多选择个数
				}); 
				showStuList();
			}
		});
	}
	
	function showStuList() {
		var examId = $("#examId").val();
		var schoolId = $("#schoolId").val();
		var seachType = $("#seachType").val();
		var title = $("#keyWord").val();
		var url = '${request.contextPath}/exammanage/edu/sports/list?examId='+examId+'&schoolId='+schoolId+'&seachType='+seachType+'&title='+title;
		$("#stuList").load(url);
	}
	
</script>