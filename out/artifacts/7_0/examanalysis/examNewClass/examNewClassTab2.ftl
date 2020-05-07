<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<div class="box box-default">
    <div class="box-body">
    	<div class="filter box-graybg mb10 no-padding-bottom">
    		<div class="filter-item">
				<label for="" class="filter-name">学年：</label>
				<div class="filter-content">
					<select class="form-control" id="acadyear" onChange="changeExam()">
					<#if acadyearList?exists && acadyearList?size gt 0>
						<#list acadyearList as item>
						<option value="${item!}" <#if item==semester.acadyear?default("")>selected="selected"</#if>>${item!}</option>
						</#list>
					</#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">学期：</label>
				<div class="filter-content">
					<select class="form-control" id="semester" onChange="changeExam()">
						${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
					</select>
				</div>
			</div>		
	        <div class="filter-item">
				<label for="" class="filter-name">年级：</label>
				<div class="filter-content">
					<select class="form-control" id="gradeCode" onChange="changeExam()">
						<#if gradeList?exists && gradeList?size gt 0>
							<#list gradeList as item>
							<option value="${item.gradeCode!}" <#if item.gradeCode==gradeCode?default("")>selected="selected"</#if>>${item.gradeName!}</option>
							</#list>
						</#if>
					</select>
				</div>
			</div>	
			<div class="filter-item chosenExamHeaderClass">
				<label for="" class="filter-name">本次考试：</label>
				<div class="filter-content">
					<select vtype="selectOne" class="form-control" id="examIdType" onChange="showClassList()">
						<option value="">---请选择---</option>
					</select>
				</div>
			</div>		
			<div class="filter-item">
				<span class="filter-name">总分类型：</span>
				<div class="filter-content">
					<select class="form-control" id="subjectId" name="subjectId" onChange="changeReferExamList()">
		                <option value="00000000000000000000000000000000,0">总分</option>
		                <option value="99999999999999999999999999999999,0">非7选3+选考赋分总分</option>
					</select>
				</div>
			</div>
			<div class="filter-item chosenReferExamHeaderClass">
				<label for="" class="filter-name">参照考试：</label>
				<div class="filter-content">
					<select vtype="selectMore" multiple data-placeholder="请选择" id="referExamIds">
						<option value="">暂无数据</option>
					</select>
				</div>
			</div>	
			<div class="filter-item filter-item-right">
				<button class="btn btn-blue" id="exportId" onclick="searchList();">查询</button>
				<button class="btn btn-white" id="exportId" onclick="doExport();">导出</button>
			</div>
		</div>
		<input type="hidden" id="type" value="${type!}">
		<input type="hidden" id="reportType" value="${reportType!}">
		<div id="myTeachListDiv">
		</div>
    </div>
</div>
<script>
	$(function(){
		//初始化单选控件
		var viewContent2={
			'width' : '220px',//输入框的宽度
			'multi_container_height' : '33px',//输入框的高度
			'results_height' : '150px',//下拉选择的高度
		}
		initChosenOne(".chosenExamHeaderClass","",viewContent2);
		var viewContent1={
			'width' : '250px',//输入框的宽度
			'multi_container_height' : '33px',//输入框的高度
			'results_height' : '150px',//下拉选择的高度
			'max_selected_options' : '3'//限制3个
		}
		initChosenMore(".chosenReferExamHeaderClass","",viewContent1);
		changeExam();		
	});
	function changeExam(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var gradeCode=$("#gradeCode").val();
		var examClass=$("#examIdType");
		$.ajax({
			url:"${request.contextPath}/exammanage/common/queryExamList",
			data:{acadyear:acadyear,semester:semester,gradeCode:gradeCode,unitId:'${unitId!}'},
			dataType: "json",
			success: function(data){
				var infolist=data.infolist;
				examClass.html("");
				examClass.chosen("destroy");
				if(infolist.length==0){
					examClass.append("<option value='' >--请选择--</option>");
				}else{
					var examHtml='';
					for(var i = 0; i < infolist.length; i ++){
						examHtml="<option value='"+infolist[i].id+","+infolist[i].type+"' ";
						examHtml+=" >"+infolist[i].name+"</option>";
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
				showClassList();
			}
		});
	}
	
function showClassList(){
	var sumTypeClass = $("#subjectId");
	var examIdType = $('#examIdType').val();
	if(!examIdType || examIdType ==''){
		searchList();
		return false;
	}
	var examId = examIdType.split(',')[0];
	var type = examIdType.split(',')[1];
	sumTypeClass.html("");
	if(type == '0'){
		sumTypeClass.append("<option value='00000000000000000000000000000000,0'>总分</option>");
	}else{
		sumTypeClass.append("<option value='00000000000000000000000000000000,0'>总分</option>");
		sumTypeClass.append("<option value='99999999999999999999999999999999,0'>非7选3+选考赋分总分</option>");
	}
	changeReferExamList();
}
function changeReferExamList(){
	var type=$("#type").val();
	var examIdType=$("#examIdType").val();
	var examId = examIdType.split(',')[0];
	var subjectIdType=$("#subjectId").val();
	var sumId = subjectIdType.split(',')[0];
	var referExamClass=$("#referExamIds");
	$.ajax({
		url:"${request.contextPath}/exammanage/common/queryReferExamList",
		data:{examId:examId,unitId:'${unitId!}',type:type,sumId:sumId},
		dataType: "json",
		success: function(data){
			var infolist=data.infolist;
			if(infolist==null || infolist.length==0){
			}else{
				referExamClass.html("");
				referExamClass.chosen("destroy");
				for(var i = 0; i < infolist.length; i ++){
					var htmlOption="<option ";
	    				htmlOption+=" value='"+infolist[i].id+"'>"+infolist[i].name;
					referExamClass.append(htmlOption);
				}
				$("#referExamIds").chosen({
					'width' : '250px',//输入框的宽度
					'multi_container_height' : '33px',//输入框的高度
					'results_height' : '150px',//下拉选择的高度
					'max_selected_options' : '3'//限制3个
				}); 
			}
		}
	});
	searchList();
}
	function searchList(){
		var examIdType=$("#examIdType").val();
		var examId = examIdType.split(',')[0];
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var subjectId=$("#subjectId").val();
		var gradeCode=$("#gradeCode").val();
		var referExamIds=$("#referExamIds").val();
		var type=$("#type").val();
		var c2='?examId='+examId+'&acadyear='+acadyear+'&semester='+semester+'&subjectId='+subjectId+'&gradeCode='
		+gradeCode+"&referExamIds="+referExamIds+"&type="+type;
		var url='${request.contextPath}/examanalysis/examNewClass/teachList/page'+c2;
		$("#myTeachListDiv").load(url);
	}
</script>