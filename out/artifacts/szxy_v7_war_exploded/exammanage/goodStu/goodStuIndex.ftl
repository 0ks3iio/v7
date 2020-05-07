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
	<div class="filter-item">
		<span class="filter-name">是否优秀生：</span>
		<div class="filter-content">
			<select class="form-control" id="hasGood" name="hasGood" onChange="showStuList()">
				<option value="all">全部</option>
				<option value="0">否</option>
				<option value="1">是</option>
			</select>
		</div>
	</div>
</div>

<div class="table-container">
	<div class="table-container-header text-right">
		<a href="javascript:" class="btn btn-blue js-setFilter0"  onclick="setApproval('1');">设为优秀生</a>
		<a href="javascript:" class="btn btn-blue js-setFilter0"  onclick="setApproval('0');">设为非优秀生</a>
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
		var hasGood = $("#hasGood").val();
		var url = '${request.contextPath}/exammanage/edu/goodStu/list?examId='+examId+'&schoolId='+schoolId+'&hasGood='+hasGood;
		$("#stuList").load(url);
	}
	
	var isSubmit = false;
	function setApproval(hasGood) {
		if (isSubmit) {
			return;
		}
		var examId = $("#examId").val();
		var ids="";
		var objElem=$('input:checkbox[name=stuCheckboxName]');
		
		
		var num = 0;
		if (hasGood != "allpass") {
			if(objElem.length>0){
				$('input:checkbox[name=stuCheckboxName]').each(function(i){
					if($(this).is(':checked')){
						ids=ids+","+$(this).val();
						num = num + 1;
					}
				});
			}else{
				layerTipMsg(false,"提示","请先选择学生！");
				return;
			}
			if(ids==""){
				layerTipMsg(false,"提示","请先选择学生！");
				return;
			}
			if (num > 200) {
				layerTipMsg(false,"提示","一次性处理不能超过200个学生！");
				return;
			}
			ids=ids.substring(1);
		}
		
		isSubmit = true;
		$.ajax({
			url:"${request.contextPath}/exammanage/edu/goodStu/save",
			data:{"ids":ids,"hasGood":hasGood},
			dataType: "json",
			success: function(data){
				if(data.success){
					layer.msg("保存成功");
					if (hasGood == "allpass") {
						showStuList();
					} else {
						var objs = ids.split(",");
						var mag = "";
						if (hasGood == "1") {
							mag = "<span class='color-blue'>是</span>";
						} else {
							mag = "<span class='color-red'>否</span>";
						}
						for (var i=0;i<objs.length;i++) {
							$("#"+objs[i] + "_state").html(mag);
						}
					}
	 			} else {
	 				layerTipMsg(data.success,"保存失败",data.msg);
				}
	 			isSubmit=false;
			}
		});
	}
	
</script>