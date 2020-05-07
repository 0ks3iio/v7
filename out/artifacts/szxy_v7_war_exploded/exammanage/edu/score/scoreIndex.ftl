<!-- chosen -->
<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<div class="box box-default">
    <div class="box-body tabDiv">
		<div class="filter filter-f16">
			<input type='hidden' id='noLimit' value=''/>
        	<input type='hidden' id='type' value='${type!}'/>
        	<div class="filter-item">
				<label for="" class="filter-name">学年：</label>
				<div class="filter-content">
					<select class="form-control" id="acadyear" name="acadyear" onChange="changeExam()">
					<#if acadyearList?exists && acadyearList?size gt 0>
						<#list acadyearList as item>
						<option value="${item!}" <#if semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
						</#list>
					</#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">学期：</label>
				<div class="filter-content">
					<select class="form-control" id="semester" name="semester" onChange="changeExam()">
					 ${mcodeSetting.getMcodeSelect("DM-XQ", searchDto.semester, "0")}
					</select>
				</div>
			</div>		
			<div class="filter-item">
				<label for="" class="filter-name">考试名称：</label>
				<div class="filter-content" id="examIdDiv">
					<select vtype="selectOne" class="form-control" id="examId" onChange="querySchoolList()">
						<option value="">---请选择---</option>
					</select>
				</div>
			</div>	
			<div class="filter-item">
				<label for="" class="filter-name">学校：</label>
				<div class="filter-content" >
					<select class="form-control" id="schoolId" onChange="changeSchool()">
						<option value="">---请选择---</option>
					</select>
				</div>
			</div>	
			<div class="filter-item">
				<label for="" class="filter-name">班级：</label>
				<div class="filter-content">
					<select vtype="selectOne" class="form-control" id="classId" onChange="searchList()">
						<option value="">---请选择---</option>
					</select>
				</div>
			</div>	
			<div class="filter-item filter-item-right"  id='findDiv'>	
				<a href="javascript:" class="btn btn-blue" onclick="searchList()">查找</a>
			</div>
		</div>
		<div id="showDivId">
		</div>
	</div>
</div>	
<script>
	$(function(){
		//初始化单选控件
		initChosenOne(".header_filter");
		changeExam();
	});
	function changeExam(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var examClass=$("#examId");
		var examId='${searchDto.examId!}';
		$.ajax({
			url:"${request.contextPath}/exammanage/common/edu/examList",
			data:{acadyear:acadyear,semester:semester,unitId:'${unitId!}'},
			dataType: "json",
			success: function(data){
				examClass.html("");
				examClass.chosen("destroy");
				if(data.length==0){
					examClass.append("<option value='' >--请选择--</option>");
				}else{
					var examHtml='';
					for(var i = 0; i < data.length; i ++){
						examHtml="<option value='"+data[i].id+"' ";
						if(examId==data[i].id){
							examHtml+='selected="selected" ';
						}
						examHtml+=" >"+data[i].examName+"</option>";
						examClass.append(examHtml);
					}
				}
				$(examClass).chosen({
					width:'200px',
					no_results_text:"未找到",//无搜索结果时显示的文本
					allow_single_deselect:true,//是否允许取消选择
					disable_search:false, //是否有搜索框出现
					search_contains:true,//模糊匹配，false是默认从第一个匹配
					//max_selected_options:1 //当select为多选时，最多选择个数
				}); 
				querySchoolList();
			}
		});
	}
	
	function changeSchool(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var examId=$("#examId").val();
		var schoolId=$("#schoolId").val();
		var selClass=$("#classId");
		$.ajax({
			url:"${request.contextPath}/exammanage/edu/score/clsList",
			data:{examId:examId,acadyear:acadyear,semester:semester,schoolId:schoolId},
			dataType: "json",
			success: function(data){
				selClass.html("");
				if(data==null){
					selClass.append("<option value='' >---请选择---</option>");
				}else{
					if(data.length==0){
						selClass.append("<option value='' >--请选择--</option>");
					}else{
						var selIdHtml='';
						for(var i = 0; i < data.length; i ++){
							selIdHtml="<option value='"+data[i].id+"' ";
							if(i==0){
								selIdHtml+='selected="selected" ';
							}
							selIdHtml+=" >"+data[i].classNameDynamic+"</option>";
							selClass.append(selIdHtml);
						}
					}
				}
				searchList();
			}
		});
	}
	
	function querySchoolList(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var examId=$("#examId").val();
		var schClass=$("#schoolId");
		$.ajax({
			url:"${request.contextPath}/exammanage/common/edu/schoolList",
			data:{examId:examId},
			dataType: "json",
			success: function(data){
				schClass.html("");
				if(data==null){
					schClass.append("<option value='' >---请选择---</option>");
				}else{
					if(data.length==0){
						schClass.append("<option value='' >--请选择--</option>");
					}else{
						var schIdHtml='';
						for(var i = 0; i < data.length; i ++){
							schIdHtml="<option value='"+data[i].id+"' ";
							if(i==0){
								schIdHtml+='selected="selected" ';
							}
							schIdHtml+=" >"+data[i].schoolName+"</option>";
							schClass.append(schIdHtml);
						}
					}
				}
				changeSchool();
			}
		});
	}
	
	function searchList(){
		var examId=$("#examId").val();
		var classId=$("#classId").val();
		var c2='?examId='+examId+'&classId='+classId;
		var url='${request.contextPath}/exammanage/edu/score/tablist'+c2;
		$("#showDivId").load(url);
	}
	
</script>