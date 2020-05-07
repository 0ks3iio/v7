<!-- chosen -->
<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<div class="box box-default">
    <div class="box-body tabDiv">
		<div class="filter filter-f16">
			<input type='hidden' id='noLimit' value='${noLimit!}'/>
        	<input type='hidden' id='type' value='${type!}'/>
        	<div class="filter-item">
				<label for="" class="filter-name">学年：</label>
				<div class="filter-content">
					<select class="form-control" id="acadyear" onChange="changeExam()">
					<#if acadyearList?exists && acadyearList?size gt 0>
						<#list acadyearList as item>
						<option value="${item!}" <#if item==searchDto.acadyear?default("")>selected="selected"</#if>>${item!}</option>
						</#list>
					</#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">学期：</label>
				<div class="filter-content">
					<select class="form-control" id="semester" onChange="changeExam()">
					 ${mcodeSetting.getMcodeSelect("DM-XQ", searchDto.semester, "0")}
					</select>
				</div>
			</div>		
	        <div class="filter-item">
				<label for="" class="filter-name">年级：</label>
				<div class="filter-content">
					<select class="form-control" id="gradeCode" onChange="changeExam()">
						<#if gradeList?exists && gradeList?size gt 0>
							<#list gradeList as item>
							<option value="${item.gradeCode!}" <#if item.gradeCode==searchDto.gradeCode?default("")>selected="selected"</#if>>${item.gradeName!}</option>
							</#list>
						</#if>
					</select>
				</div>
			</div>	
			<div class="filter-item">
				<label for="" class="filter-name">考试名称：</label>
				<div class="filter-content" id="examIdDiv">
					<select vtype="selectOne" class="form-control" id="examId" onChange="changeClassType()">
						<option value="">---请选择---</option>
					</select>
				</div>
			</div>	
			<div class="filter-item">
				<label for="" class="filter-name">班级类型：</label>
				<div class="filter-content" >
					<select class="form-control" id="classType" onChange="changeClassType()">
						<option value="1" <#if searchDto.classType?default('1')=='1'>selected="selected"</#if>>行政班</option>
						<option value="2" <#if searchDto.classType?default('1')=='2'>selected="selected"</#if>>教学班</option>
					</select>
				</div>
			</div>	
			<div class="filter-item">
				<label for="" class="filter-name">班级：</label>
				<div class="filter-content" id="classIdDiv">
					<select vtype="selectOne" class="form-control" id="classId" onChange="searchList()">
						<option value="">---请选择---</option>
					</select>
				</div>
			</div>	
			<div class="filter-item filter-item-right"  id='findDiv'>	
				<a href="javascript:" class="btn btn-blue" onclick="searchList()">查找</a>
				<a href="javascript:" class="btn btn-blue" onclick="searchImport()">导入</a>
			</div>
		</div>
		<div id="showDivId">
			
		</div>
	</div>
</div>	
<script>
	$(function(){
		showBreadBack(goBack,true,"录入");
		//初始化单选控件
		initChosenOne(".header_filter");
		changeExam();
	});
	function changeExam(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var gradeCode=$("#gradeCode").val();
		var examClass=$("#examId");
		var examId='${searchDto.examId!}';
		$.ajax({
			url:"${request.contextPath}/exammanage/common/examList",
			data:{acadyear:acadyear,semester:semester,gradeCode:gradeCode,unitId:'${unitId!}',noLimit:'${noLimit!}',fromType:'1'},
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
				changeClassType();
			}
		});
	}
	function changeClassType(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var examId=$("#examId").val();
		var classType=$("#classType").val();
		var gradeCode=$("#gradeCode").val();
		var type = $("#type").val();
		var selClass=$("#classId");
		var classId='${searchDto.classId}';
		$.ajax({
			url:"${request.contextPath}/exammanage/scoreNewInput/classList",
			data:{examId:examId,classType:classType,gradeCode:gradeCode,acadyear:acadyear,semester:semester,noLimit:'${noLimit!}'},
			dataType: "json",
			success: function(data){
				selClass.html("");
				selClass.chosen("destroy");
				if(data==null){
					selClass.append("<option value='' >---请选择---</option>");
				}else{
					if(data.length==0){
						selClass.append("<option value='' >--请选择--</option>");
					}else{
						var classIdHtml='';
						for(var i = 0; i < data.length; i ++){
							classIdHtml="<option value='"+data[i].classId+"' ";
							if(classId==data[i].classId){
								classIdHtml+='selected="selected" ';
							}
							classIdHtml+=" >"+data[i].className+"</option>";
							selClass.append(classIdHtml);
						}
					}
				}
				$(selClass).chosen({
					no_results_text:"未找到",//无搜索结果时显示的文本
					allow_single_deselect:true,//是否允许取消选择
					disable_search:false, //是否有搜索框出现
					search_contains:true,//模糊匹配，false是默认从第一个匹配
					//max_selected_options:1 //当select为多选时，最多选择个数
				}); 
				if (type == 1) {
					searchList();
				} else {
					searchImport();
				}
			}
		});
	}
	
	function searchList(){
		var examId=$("#examId").val();
		var classType=$("#classType").val();
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var classId=$("#classId").val();
		var gradeCode=$("#gradeCode").val();
		var c2='?examId='+examId+'&classType='+classType+'&classId='+classId+'&gradeCode='+gradeCode+'&acadyear='+acadyear+'&semester='+semester+'&subjectId=${searchDto.subjectId!}&noLimit=${noLimit!}';
		var url='${request.contextPath}/exammanage/scoreNewInput/tablist/page'+c2;
		$("#showDivId").load(url);
	}
	
	function searchImport() {
		var examId=$("#examId").val();
		if(examId==''){
			layer.tips('请选择考试', $("#examIdDiv"), {
				tipsMore: true,
				tips: 3
			});
			return;
		}
		var classId = $('#classId').val();
		if(classId==''){
			layer.tips('请选择班级', $("#classIdDiv"), {
				tipsMore: true,
				tips: 3
			});
			return;
		}
		$('#findDiv').attr("style","display:none;");
		$("#type").val(2);
		$("#classId").attr("onChange","searchImport();");
		var classId=$("#classId").val();
		var classType=$("#classType").val();
		var subTypeName = $("#subTypeName li[class='active']").children().text();
		var noLimit=$("#noLimit").val();
		var str = '?examId='+examId+'&subTypeName='+subTypeName+"&noLimit="+noLimit+"&classId="+classId+"&classType="+classType;
		var url='${request.contextPath}/exammanage/scoreImport/head'+str;
		url=encodeURI(encodeURI(url));
		$("#showDivId").load(url);
	}
	function goBack(){
		var noLimit=$("#noLimit").val();
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var gradeCode=$("#gradeCode").val();
		var subjectId=$("#subjectId").val();
		if(!subjectId){
			var subjectIdType=$("#subjectIdType").val();
			if(subjectIdType){
				subjectId=subjectIdType.split("_")[0];
			}
		}
		var examId=$("#examId").val();
		var c2='?examId='+examId+'&acadyear='+acadyear+'&semester='+semester+'&gradeCode='+gradeCode+'&subjectId='+subjectId+'&noLimit='+noLimit;
		var url='${request.contextPath}/exammanage/scoreNewInput/index/page'+c2;
		$(".model-div").load(url);
	}
</script>