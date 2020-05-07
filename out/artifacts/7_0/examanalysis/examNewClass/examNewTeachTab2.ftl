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
					<select class="form-control" id="gradeCode" onChange="changeSubjectList()">
						<#if gradeList?exists && gradeList?size gt 0>
							<#list gradeList as item>
							<option value="${item.gradeCode!}" <#if item.gradeCode==gradeCode?default("")>selected="selected"</#if>>${item.gradeName!}</option>
							</#list>
						</#if>
					</select>
				</div>
			</div>	
			<div class="filter-item">
				<label for="" class="filter-name">科目：</label>
				<div class="filter-content">
					<select class="form-control" id="subjectId" onChange="changeClassList()">
						<option value="">---请选择---</option>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">班级：</label>
				<div class="filter-content">
					<select class="form-control" id="classId" onChange="changeSubType()">
						<option value="">---请选择---</option>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">科目考试类型：</label>
				<div class="filter-content">
					<select class="form-control" id="subType" onChange="searchList()">
						<option value="">---请选择---</option>
					</select>
				</div>
			</div>
			<div class="filter-item ">
				<div class="btn-group" role="group">
					<a type="button"  class="btn btn-blue" href="javascript:;" id="tableId" onclick="changeType('1')">报表</a>
					<a type="button" class="btn btn-white" href="javascript:;" id="echartsId" onclick="changeType('2')">图表</a>
				</div>
			</div>
			<div class="filter-item filter-item-right">
				<button class="btn btn-blue" id="exportId" onclick="searchList();">查询</button>
				<button class="btn btn-white" id="exportId" onclick="doExport();">导出</button>
			</div>
		</div>
		<input type="hidden" id="reportType" value="${reportType!}">
		<input type="hidden" id="type" value="${type!}">
		<div id="myTeachListDiv">
		</div>
    </div>
</div>
<script>
	$(function(){
		//初始化单选控件
		initChosenOne(".header_filter");
		changeSubjectList();
	});
	function changeSubjectList(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var gradeCode=$("#gradeCode").val();
		var subjectIdClass=$("#subjectId");
		$.ajax({
			url:"${request.contextPath}/exammanage/common/queryExamAllSubList",
			data:{acadyear:acadyear,semester:semester,gradeCode:gradeCode,unitId:'${unitId!}'},
			dataType: "json",
			success: function(data){
				var infolist=data.infolist;
				subjectIdClass.html("");
				if(infolist==null || infolist.length==0){
					subjectIdClass.append("<option value='' >---请选择---</option>");
				}else{
					var subjectHtml='';
					for(var i = 0; i < infolist.length; i ++){
						subjectHtml="<option value='"+infolist[i].id+"' ";
						subjectHtml+=" >"+infolist[i].name+"</option>";
						subjectIdClass.append(subjectHtml);
					}
				}
				changeClassList();
			}
		});
	}
	function changeClassList(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var gradeCode=$("#gradeCode").val();
		var subjectId=$("#subjectId").val();
		var classIdClass=$("#classId");
		$.ajax({
			url:"${request.contextPath}/exammanage/common/queryExamClsList",
			data:{acadyear:acadyear,semester:semester,gradeCode:gradeCode,unitId:'${unitId!}',subjectId:subjectId},
			dataType: "json",
			success: function(data){
				var infolist=data.infolist;
				classIdClass.html("");
				if(infolist==null || infolist.length==0){
					classIdClass.append("<option value='' >---请选择---</option>");
				}else{
					var classHtml='';
					for(var i = 0; i < infolist.length; i ++){
						classHtml="<option value='"+infolist[i].id+"' ";
						classHtml+=" >"+infolist[i].name+"</option>";
						classIdClass.append(classHtml);
					}
				}
				changeSubType();
			}
		});
	}
	function changeSubType(){
		var subjectId=$("#subjectId").val();
		var subTypeClass=$("#subType");
		$.ajax({
			url:"${request.contextPath}/exammanage/common/queryExamSubTypeList",
			data:{subjectId:subjectId,unitId:'${unitId!}'},
			dataType: "json",
			success: function(data){
				var infolist=data.infolist;
				subTypeClass.html("");
				if(infolist==null || infolist.length==0){
					subTypeClass.append("<option value='' >---请选择---</option>");
				}else{
					var subTypeHtml='';
					for(var i = 0; i < infolist.length; i ++){
						subTypeHtml="<option value='"+infolist[i].id+"' ";
						subTypeHtml+=" >"+infolist[i].name+"</option>";
						subTypeClass.append(subTypeHtml);
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
		var gradeCode=$("#gradeCode").val();
		var subjectId=$("#subjectId").val();
		var subType=$("#subType").val();
		var classId=$("#classId").val();
		var type=$("#type").val();
		var reportType=$("#reportType").val();
		if(!reportType){
			reportType="1";
		}
		var c2='?examId='+examId+'&acadyear='+acadyear+'&semester='+semester+'&subjectId='+subjectId+'&gradeCode='
		+gradeCode+"&classId="+classId+"&reportType="+reportType+"&subType="+subType+"&type="+type;
		var url='${request.contextPath}/examanalysis/examNewClass/teachList/page'+c2;
		$("#myTeachListDiv").load(url);
	}
	function changeType(reportType){
		$('#reportType').attr('value',reportType);
		if(reportType && reportType=='1'){
			if(!$("#tableId").hasClass("btn-blue")){
				$("#tableId").addClass("btn-blue").removeClass("btn-white");
				$("#echartsId").removeClass("btn-blue").addClass("btn-white");
				$("#exportId").show();
			}
		}else if(reportType && reportType=='2'){
			if(!$("#echartsId").hasClass("btn-blue")){
				$("#echartsId").addClass("btn-blue").removeClass("btn-white");
				$("#tableId").addClass("btn-white").removeClass("btn-blue");
				$("#exportId").hide();
			}
		}
		searchList();
	}
</script>