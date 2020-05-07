<div class="box box-default">
    <div class="box-body">
    	<div class="filter box-graybg mb10 no-padding-bottom">
    		<input type="hidden" name="type" id="type" value="${type!}"/>
			<div class="filter-item">
				<span class="filter-name">学年：</span>
				<div class="filter-content">
					<select class="form-control" id="acadyear" name="acadyear" onChange="showExamList()">
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
					<select class="form-control" id="semester" name="semester" onChange="showExamList()">
						<option value="1" <#if semester.semester?default(0)?string=='1'>selected</#if>>第一学期</option>
						<option value="2" <#if semester.semester?default(0)?string=='2'>selected</#if>>第二学期</option>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">年级：</span>
				<div class="filter-content">
					<select class="form-control" id="gradeCode" name="gradeCode" onChange="showExamList()">
					<#if gradeList?? && (gradeList?size>0)>
						<#list gradeList as item>
                            <option value="${item.gradeCode!}">${item.gradeName!}</option>
						</#list>
					</#if>
                    </select>
				</div>
			</div>
			<#if type != '9'>
			<div class="filter-item">
				<span class="filter-name">考试：</span>
				<div class="filter-content">
					<select class="form-control" id="examIdType" name="examIdType" onChange="showClassList()">
						<option value='' >-----请选择-----</option>
					</select>
				</div>
			</div>
			</#if>
			<#if type != '7' && type != '8' && type != '21'>
			<div class="filter-item">
				<span class="filter-name">班级：</span>
				<div class="filter-content">
					<select class="form-control" id="classId" name="classId" onChange="show()">
						<option value='' >-----请选择-----</option>
					</select>
				</div>
			</div>
			</#if>
			<div class="filter-item">
				<span class="filter-name">总分类型：</span>
				<div class="filter-content">
					<select class="form-control" id="sumType" name="sumType" onChange="show()">
		                <option value="0">总分</option>
		                <option value="9">非7选3+选考赋分总分</option>
					</select>
				</div>
			</div>
			<div class="filter-item "  <#if type == '6'>style="display:none;"</#if>>
				<div class="btn-group" role="group">
					<a id="showType0" type="button" class="btn btn-blue selectClass" href="javascript:;" onclick="showData('0')">报表</a>
					<a id="showType1" type="button" class="btn btn-white" href="javascript:;" onclick="showData('1')">图表</a>
				</div>
			</div>
			<div class="filter-item filter-item-right">
				<button class="btn btn-blue" id="exportId" onclick="show();">查询</button>
				<button class="btn btn-white" id="doExport" onclick="doExport()" style="display:none;">导出</button>
				<#--<button class="btn btn-blue" onclick="show()">搜索</button>-->
			</div>
		</div>
		<div id="dataDiv" class="print">
			<div class="no-data-container">
			</div>
		</div>
    </div>
</div>
<script src="${request.contextPath}/static/js/LodopFuncs.js" />
<script>
showExamList();
function show(){
	$('.selectClass').click();
}

function showData(showType){
	if(showType=='0'){
		$('#showType0').attr('Class','btn btn-blue selectClass');
		$('#showType1').attr('Class','btn btn-white');
		$('#doExport').show();
	}else{
		$('#showType1').attr('Class','btn btn-blue selectClass');
		$('#showType0').attr('Class','btn btn-white');
		$('#doExport').hide();
	}
	var type = $('#type').val();
	var sumType = $('#sumType').val();
	var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	if(type == '6'){
		var examIdType = $('#examIdType').val();
		if(!examIdType || examIdType ==''){
			var examId = "";
			var examType = "";
		}else{
			var examId = examIdType.split(',')[0];
			var examType = examIdType.split(',')[1];
		}
		var classId = $('#classId').val();
		var str = "?type="+type+"&showType="+showType+"&examId="+examId+"&examType="+examType+"&classId="+classId+"&sumType="+sumType+"&acadyear="+acadyear+"&semester="+semester;
		$("#dataDiv").load("${request.contextPath}/examanalysis/examNewClass/showData/page"+str);	
	}else if(type == '7' || type=='21'){
		var examIdType = $('#examIdType').val();
		if(!examIdType || examIdType ==''){
			var examId = "";
			var examType = "";
		}else{
			var examId = examIdType.split(',')[0];
			var examType = examIdType.split(',')[1];
		}
		var gradeCode = $('#gradeCode').val();
		var str = "?type="+type+"&showType="+showType+"&examId="+examId+"&examType="+examType+"&sumType="+sumType+"&acadyear="+acadyear+"&semester="+semester+"&gradeCode="+gradeCode;
		$("#dataDiv").load("${request.contextPath}/examanalysis/examNewClass/showData2/page"+str);	
	}else if(type == '8'){
		var examIdType = $('#examIdType').val();
		if(!examIdType || examIdType ==''){
			var examId = "";
			var examType = "";
		}else{
			var examId = examIdType.split(',')[0];
			var examType = examIdType.split(',')[1];
		}
		var gradeCode = $('#gradeCode').val();
		var str = "?type="+type+"&showType="+showType+"&examId="+examId+"&examType="+examType+"&sumType="+sumType+"&acadyear="+acadyear+"&semester="+semester+"&gradeCode="+gradeCode;
		$("#dataDiv").load("${request.contextPath}/examanalysis/examNewClass/showData3/page"+str);	
	}else if(type == '9'){
		var gradeCode = $('#gradeCode').val();
		var classId = $('#classId').val();
		var str = "?type="+type+"&showType="+showType+"&sumType="+sumType+"&acadyear="+acadyear+"&semester="+semester+"&classId="+classId+"&gradeCode="+gradeCode;
		$("#dataDiv").load("${request.contextPath}/examanalysis/examNewClass/showData4/page"+str);	
	}
	
}

function showExamList(){
	var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	var gradeCode = $('#gradeCode').val();
	<#if type != '9'>
	var examClass=$("#examIdType");
	$.ajax({
		url:"${request.contextPath}/exammanage/common/queryExamList",
		data:{acadyear:acadyear,semester:semester,unitId:'${unitId!}',gradeCode:gradeCode},
		dataType: "json",
		success: function(json){
			examClass.html("");
			var data = json.infolist;
			if(data.length==0){
				examClass.append("<option value='' >没有考试信息</option>");
			}else{
				for(var i = 0; i < data.length; i ++){
					if(i==0){
						examClass.append("<option value='"+data[i].id+","+data[i].type+"' selected='selected'>"+data[i].name+"</option>");
					}else{
						examClass.append("<option value='"+data[i].id+","+data[i].type+"' >"+data[i].name+"</option>");
					}
				}
			}
			showClassList();
		}
	});
	<#else>
	var subjectClass = $("#classId");
	$.ajax({
		url:"${request.contextPath}/remote/common/basedata/remoteClazzList",
		data:{acadyear:acadyear,unitId:'${unitId!}',gradeCode:gradeCode},
		dataType: "json",
		success: function(json){
			subjectClass.html("");
			var data = json.infolist;
			if(data.length==0){
				subjectClass.append("<option value='' >没有班级信息</option>");
			}else{
				for(var i = 0; i < data.length; i ++){
					if(i==0){
						subjectClass.append("<option value='"+data[i].id+"' selected='selected'>"+data[i].className+"</option>");
					}else{
						subjectClass.append("<option value='"+data[i].id+"' >"+data[i].className+"</option>");
					}
				}
			}
			show();
		}
	});
	</#if>
}

function showClassList(){
	var subjectClass = $("#classId");
	var sumTypeClass = $("#sumType");
	var examIdType = $('#examIdType').val();
	if(!examIdType || examIdType ==''){
		show();
		return false;
	}
	var examId = examIdType.split(',')[0];
	var type = examIdType.split(',')[1];
	sumTypeClass.html("");
	if(type == '0'){
		sumTypeClass.append("<option value='0'>总分</option>");
	}else{
		sumTypeClass.append("<option value='0'>总分</option>");
		sumTypeClass.append("<option value='9'>非7选3+选考赋分总分</option>");
	}
	<#if type != '6'>
	show();
	return false;
	</#if>
	$.ajax({
		url:"${request.contextPath}/exammanage/common/queryExamClassList",
		data:{examId:examId,unitId:'${unitId!}',classType:'1'},
		dataType: "json",
		success: function(json){
			subjectClass.html("");
			var data = json.infolist;
			if(data.length==0){
				subjectClass.append("<option value='' >没有考试科目信息</option>");
			}else{
				for(var i = 0; i < data.length; i ++){
					if(i==0){
						subjectClass.append("<option value='"+data[i].id+"' selected='selected'>"+data[i].className+"</option>");
					}else{
						subjectClass.append("<option value='"+data[i].id+"' >"+data[i].className+"</option>");
					}
				}
			}
			show();
		}
	});
}
</script>