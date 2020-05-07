<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<form id="subForm" method="post">
<div id="myDiv">
			<div class="table-container">
				<div class="table-container-header text-right">
					<a class="btn btn-white" onClick="doImport();">导入Excel</a>
					<a class="btn btn-white" onClick="doExport();">导出Excel</a>
					<a class="btn btn-blue" onClick="saveTraning();">保存</a>
				</div>
			<div class="table-container-body">
				<table class="table table-striped table-hover">
					<thead>
						<tr>
							<th>姓名</th>
							<th>性别</th>
							<th>学号</th>
							<th>班级</th>
							<th>等第</th>
							<th>学年</th>
							<th>学期</th>
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
							<td>
								<select name="dyStuMilitaryTrainingList[${stu_index!}].gradeId" id="dyStuMilitaryTrainingList${stu_index!}gradeId" class="form-control">
								        <option value="">--请选择--</option>
								<#if dyBusinessOptionList?exists && dyBusinessOptionList?size gt 0>
								    <#list dyBusinessOptionList as item>
									    <option value="${item.id!}" <#if '${item.id!}' == '${stu.gradeId!}'>selected</#if>>${item.optionName!}</option>
									</#list>
								</#if>
								</select>
							</td>
							<td>
								<select name="dyStuMilitaryTrainingList[${stu_index!}].acadyear" id="dyStuMilitaryTrainingList${stu_index!}acadyear" class="form-control acadyear">
								        <option value="">--请选择--</option>
						        <#if acadyearList?exists && acadyearList?size gt 0>
						            <#list acadyearList as item>
									    <option value="${item!}" <#if '${stu.acadyear!}' == '${item!}'>selected</#if>>${item!}</option>
									</#list>
							    </#if>
								</select>
							</td>
							<td>
								<select name="dyStuMilitaryTrainingList[${stu_index!}].semester" id="dyStuMilitaryTrainingList${stu_index!}semester" class="form-control">
								        <option value="">--请选择--</option>
									    <option value="1" <#if '${stu.semester!}' == '1'>selected</#if>>第一学期</option>
									    <option value="2" <#if '${stu.semester!}' == '2'>selected</#if>>第二学期</option>
								</select>
							</td>
							<td>
								<input type="text" class="form-control" name="dyStuMilitaryTrainingList[${stu_index!}].remark" id="stuMilitaryTrainingList${stu_index!}remark" value="${stu.remark!}" maxLength="2000">
							</td>
							<td>
								<a href="javascript:doClear('${stu_index!}');" class="table-btn color-red">清除</a>
							</td>
							<input type="hidden" class="form-control" name="dyStuMilitaryTrainingList[${stu_index!}].studentId" value="${stu.id!}">
						</tr>
						</#list>
					</#if>														
					</tbody>
			     </table>
			     <#if studentList?exists && studentList?size gt 0>
  	                    <@htmlcom.pageToolBar container="#showList" class="noprint"/>
                 </#if>
		    </div>
</div>
</form>
<script>
var isSubmit=false;
function saveTraning(){
   <#if !(studentList?exists) || !(studentList?size gt 0)>
       layerTipMsgWarn("提示","没有要保存的数据！");
	   return;
   </#if>
   if(isSubmit){
   		return;
   }
   var check = checkValue('#myDiv');
    if(!check){
        isSubmit=false;
        return;
    }
    
    var flag = false;
    $(".acadyear").each(function(){
    	var $semester = $(this).parent().next().find('select');
    	if($(this).val()=='' && $semester.val()!=''){
    		layer.tips("学年学期需同时填写", $(this), {
				tipsMore: true,
				tips:3		
			});
    		flag = true;
    	}else if($(this).val()!='' && $semester.val()==''){
    		layer.tips("学年学期需同时填写", $semester, {
				tipsMore: true,
				tips:3		
			});
    		flag = true;
    	}
    })
    
    if(flag){
        isSubmit=false;
    	return;
    }
    isSubmit=true;
    var classId = $('#classId').val();
    var gradeId = $('#gradeId').val();
    var enter = $('#enter').val();
	var options = {
		url : "${request.contextPath}/stuwork/militaryTraining/save?classId="+classId+"&gradeId="+gradeId+"&enter="+enter,
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
				///searchList('${classId!}');
				var url = "${request.contextPath}/stuwork/militaryTraining/pageList?classId="+classId+"&gradeId="+gradeId+"&enter="+enter;
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
   var classId = $('#classId').val();
   var gradeId = $('#gradeId').val();
   var enter = $('#enter').val();
   document.location.href = "${request.contextPath}/stuwork/militaryTraining/militaryTrainingExport?classId="+classId+"&gradeId="+gradeId+"&enter="+enter;
}

function doImport(){
	$(".model-div").load("${request.contextPath}/stuwork/militaryTraining/import/main");
}

function doClear(index){
    var select1 = document.getElementById("dyStuMilitaryTrainingList"+index+"gradeId");     
    for (var i = 0; i < select1.options.length; i++){  
        if (select1.options[i].value == ""){  
            select1.options[i].selected = true;  
            break;  
        }  
    } 
    var select2 = document.getElementById("dyStuMilitaryTrainingList"+index+"acadyear");     
    for (var i = 0; i < select2.options.length; i++){  
        if (select2.options[i].value == ""){  
            select2.options[i].selected = true;  
            break;  
        }  
    }
    var select3 = document.getElementById("dyStuMilitaryTrainingList"+index+"semester");     
    for (var i = 0; i < select3.options.length; i++){  
        if (select3.options[i].value == ""){  
            select3.options[i].selected = true;  
            break;  
        }  
    }
    $("#stuMilitaryTrainingList"+index+"remark").val("");
}
</script>