<!-- chosen -->
<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<div class="box box-default">
    <div class="box-header header_filter">
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
					 ${mcodeSetting.getMcodeSelect("DM-XQ", searchDto.semester?default("0"), "0")}
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
			<div class="filter-item" >
				<label for="" class="filter-name">考试名称：</label>
				<div class="filter-content" id="examIdDiv">
					<select vtype="selectOne" class="form-control" id="examId" onChange="changeSubjectList()">
						<option value="">---请选择---</option>
					</select>
				</div>
			</div>		
			<div class="filter-item" id="findDiv">
				<label for="" class="filter-name">科目：</label>
				<div class="filter-content" id="subjectIdDiv">
					<select class="form-control" id="subjectId" onChange="searchList()">
						<option value="">---请选择---</option>
					</select>
				</div>
			</div>
			<#if noLimit?default("")=="1">
				<div class="filter-item filter-item-right" id="importBtnDiv">
					<a href="javascript:" class="btn btn-blue" onclick="clearScores()">清除</a>
					<a href="javascript:" class="btn btn-blue" onclick="doAllImport()">批量导入</a>
				</div>
			</#if>		
		</div>
    </div>
	<div class="box-body tabDiv">	
	</div>
</div>
<script>
	$(function(){
		showBreadBack(gobackIndex,true,"录入");
		//初始化单选控件
		initChosenOne(".header_filter");
		changeExam();
	});
	function clearScores(){
		var examId=$("#examId").val();
		if(examId==''){
			layer.tips('请选择考试', $("#examIdDiv"), {
				tipsMore: true,
				tips: 3
			});
			return;
		}
		var subjectId=$("#subjectId").val();
		if(subjectId==''){
			layer.tips('请选择科目', $("#subjectIdDiv"), {
				tipsMore: true,
				tips: 3
			});
			return;
		}
		showConfirmMsg('确认清除已录入的成绩？','提示',function(){
			$.ajax({
				url:"${request.contextPath}/exammanage/scoreNewInput/clearScores",
				data:{examId:examId,subjectId:subjectId},
				dataType: "json",
				success: function(data){
					if(data.success){
						layer.msg(data.msg, {
							offset: 't',
							time: 2000
						});
						searchList();
					}else{
						layerTipMsg(data.success,"失败",data.msg);
					}
				}
			});
		});
	}
	function doAllImport(){
		var examId=$("#examId").val();
		if(examId==''){
			layer.tips('请选择考试', $("#examIdDiv"), {
				tipsMore: true,
				tips: 3
			});
			return;
		}
		var subjectId=$("#subjectId").val();
		if(subjectId==''){
			layer.tips('请选择科目', $("#subjectIdDiv"), {
				tipsMore: true,
				tips: 3
			});
			return;
		}
		$("#type").val(2);
		var noLimit=$("#noLimit").val();
		$('#findDiv').attr("style","display:none;");
		$('#importBtnDiv').attr("style","display:none;");
		$("#examId").attr("onChange","doAllImport();");
		var str = '?examId='+examId+"&noLimit="+noLimit+"&subjectId="+subjectId;
		var url='${request.contextPath}/exammanage/scoreImport/head'+str;
		url=encodeURI(encodeURI(url));
		$(".tabDiv").load(url);
	}
	function changeExam(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var gradeCode=$("#gradeCode").val();
		var type=$("#type").val();
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
				if(type==2){
					doAllImport();
				}else{
					changeSubjectList();
				}
			}
		});
	}
	function changeSubjectList(){
		var examId=$("#examId").val();
		var subjectIdClass=$("#subjectId");
		var subjectId='${searchDto.subjectId!}';
		$.ajax({
			url:"${request.contextPath}/exammanage/common/subjectList",
			data:{examId:examId,unitId:'${unitId!}',noLimit:'${noLimit!}',fromType:'1'},
			dataType: "json",
			success: function(data){
				subjectIdClass.html("");
				if(data==null || data.length==0){
					subjectIdClass.append("<option value='' >---请选择---</option>");
				}else{
					var subjectHtml='';
					for(var i = 0; i < data.length; i ++){
						subjectHtml="<option value='"+data[i].id+"' ";
						if(subjectId==data[i].id){
							subjectHtml+='selected="selected" ';
						}
						subjectHtml+=" >"+data[i].subjectName+"</option>";
						subjectIdClass.append(subjectHtml);
					}
				}
				searchList();
			}
		});
	}
	
	function searchList(){
		var examId=$("#examId").val();
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var subjectId=$("#subjectId").val();
		var gradeCode=$("#gradeCode").val();
		var c2='?examId='+examId+'&acadyear='+acadyear+'&semester='+semester+'&subjectId='+subjectId+'&gradeCode='+gradeCode+'&noLimit=${noLimit!}';
		var url='${request.contextPath}/exammanage/scoreNewInput/manageList/page'+c2;
		$(".tabDiv").load(url);
	}
	function gobackIndex(){
		var url='${request.contextPath}/exammanage/scoreNewInput/index/page';
		$(".model-div").load(url);
	}
	
</script>