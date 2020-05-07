<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-container-header text-right">
	<a class="btn btn-white" onClick="doImport();">导入Excel</a>
	<a class="btn btn-white" onClick="doExport();">导出Excel</a>
	<a class="btn btn-blue" onClick="savePerformance();">保存</a>
</div>
<form id="subForm" method="post">
<div class="table-container-body" id="myDiv">
	<table class="table table-striped table-hover">
		<thead>
			<tr>
				<th>姓名</th>
				<th>性别</th>
				<th>学号</th>
				<th>班级</th>
				<th>周次</th>
				<th>等第</th>
				<th>备注</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
        <#if studentList?exists && studentList?size gt 0>
					    <#list studentList as stu>
						<tr>
							<td>${stu.studentName!}</td>
							<td>
							    <#if '${stu.sex!}' == '1'>
							                          男
							    <#else>
							                          女 
							    </#if>
							</td>
							<td>${stu.studentCode!}</td>
							<td>${stu.className!}</td>
							<td>${stu.week!}
							<input type="hidden" class="form-control" name="dyStuWeekCheckPerformanceList[${stu_index!}].week" id="dyStuWeekCheckPerformanceList${stu_index!}week" value="${stu.week!}">
							<input type="hidden" class="form-control" name="dyStuWeekCheckPerformanceList[${stu_index!}].id" id="dyStuWeekCheckPerformanceList${stu_index!}id" value="${stu.id!}">
							</td>
							<td>
								<select name="dyStuWeekCheckPerformanceList[${stu_index!}].gradeId" id="dyStuWeekCheckPerformanceList${stu_index!}gradeId" class="form-control">
								        <option value="">--请选择--</option>
								<#if dyBusinessOptionList?exists && dyBusinessOptionList?size gt 0>
								    <#list dyBusinessOptionList as item>
									    <option value="${item.id!}" <#if '${item.id!}' == '${stu.gradeId!}'>selected</#if>>${item.optionName!}</option>
									</#list>
								</#if>
								</select>
							</td>
							<td>
								<input type="text" class="form-control" name="dyStuWeekCheckPerformanceList[${stu_index!}].remark" id="dyStuWeekCheckPerformanceList${stu_index!}remark" value="${stu.remark!}" maxLength="2000">
							</td>
							<td>
								<a href="javascript:doClear('${stu_index!}');" class="table-btn color-red">清除</a>
							</td>
							<input type="hidden" class="form-control" name="dyStuWeekCheckPerformanceList[${stu_index!}].studentId" value="${stu.id!}">
						</tr>
						</#list>
					</#if>	
	    </tbody>
	</table>
	<#if studentList?exists && studentList?size gt 0>
            <@htmlcom.pageToolBar container="#showList" class="noprint"/>
     </#if>
</div>
</form>
<script>
var isSubmit=false;
function savePerformance(){
   <#if !(studentList?exists) || !(studentList?size gt 0)>
       layerTipMsgWarn("提示","没有要保存的数据！");
	   return;
   </#if>
   if(isSubmit){
   		return;
   }
   var acadyear = $('#acadyear').val();
   var semester = $('#semester').val();
   var classId = $('#classId').val();
   var week = $('#week').val();
   var check = checkValue('#myDiv');
    if(!check){
        isSubmit=false;
        return;
    }
    isSubmit=true;
    var acadyear = $('#acadyear').val();
    var semester = $('#semester').val();
    var classId = $('#classId').val();
    var week = $('#week').val();
    var enter = $('#enter').val();
    var gradeId = $('#gradeId').val();
	var options = {
		url : "${request.contextPath}/stuwork/weekCheckPerformance/save?classId="+classId+"&acadyear="+acadyear+"&semester="+semester+"&week="+week+"&enter="+enter+"&gradeId="+gradeId,
		dataType : 'json',
		success : function(data){
	 		var jsonO = data;
		 	if(!jsonO.success){
		 		layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 		isSubmit=false;
		 		return;
		 	}else{
		 		layer.closeAll();
				layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
                var url = "${request.contextPath}/stuwork/weekCheckPerformance/weekCheckPerformanceList?acadyear="+acadyear+"&semester="+semester+"&classId="+classId+"&week="+week+"&enter="+enter+"&gradeId="+gradeId;
                $("#showList").load(url);
    		}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#subForm").ajaxSubmit(options);
}

function doExport(){
    var acadyear = $('#acadyear').val();
    var semester = $('#semester').val();
    var classId = $('#classId').val();
    var week = $('#week').val();
    var enter = $('#enter').val();
    var gradeId = $('#gradeId').val();
   document.location.href = "${request.contextPath}/stuwork/weekCheckPerformance/export?classId="+classId+"&gradeId="+gradeId+"&enter="+enter+"&acadyear="+acadyear+"&semester="+semester+"&week="+week;
}

function doImport(){
   var acadyear = $('#acadyear').val();
   var semester = $('#semester').val();
   var week = $('#week').val();
   if(week == ''){
       layerTipMsgWarn("提示","周次不能为空！");
	   return;
   }
   $("#importDiv").load("${request.contextPath}/stuwork/weekCheckPerformance/import/main?acadyear="+acadyear+"&semester="+semester+"&week="+week);
}

function doClear(index){
    var select1 = document.getElementById("dyStuWeekCheckPerformanceList"+index+"gradeId");     
    for (var i = 0; i < select1.options.length; i++){  
        if (select1.options[i].value == ""){  
            select1.options[i].selected = true;  
            break;  
        }  
    } 
    $("#dyStuWeekCheckPerformanceList"+index+"remark").val("");
}
</script>