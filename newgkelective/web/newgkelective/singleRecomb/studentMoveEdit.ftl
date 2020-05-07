<div class="filter" style="height:294px">
	<form id="moverStudentForm">
	<div class="filter-item block">
		<input type="hidden" name="subjectType" value="${subjectType!}">
		<input type="hidden" name="divideId" value="${divideId!}">
		<input type="hidden" name="studentId" id="studentId" value="${studentResultDto.studentId!}">
		<span class="filter-name" id="111">学号：</span>
		<div class="filter-content">
			<p>${studentResultDto.studentCode!}</p>
		</div>
	</div>
	<div class="filter-item block">
		<span class="filter-name">姓名：</span>
		<div class="filter-content">
			<p>${studentResultDto.studentName!}</p>
		</div>
	</div>
	<div class="filter-item block">
		<span class="filter-name">学科：</span>
		<div class="filter-content">
			<p>${subjectNames!}</p>
		</div>
	</div>
	<#list bathList as bath>
	<div class="filter-item block">
		<span class="filter-name"><#if subjectType?default("A") == "A">选考<#else>学考</#if>${bath}：</span>
		<div class="filter-content">
			<select name="courseClassId" id="classId_${bath_index}" class="form-control">
				<#list classByBath[bath] as classDto>
	        	<option value="${classDto.subjectIds!}_${classDto.id!}" <#if chooseMap[bath]?exists && chooseMap[bath]==classDto.id>selected</#if>>${classDto.className!}(${classDto.studentCount})</option>
	        	</#list>
	        </select>
		</div>
	</div>
	</#list>
	</form>
</div>

<script type="text/javascript">

//确定按钮操作功能
var isSubmit=false;
function doSaveMoveClass(){
    if(isSubmit){
        return;
    }
    isSubmit=true;
    if(!mycheck()){
    	isSubmit=false;
    	return;
    }
    //验证moverStudentForm
  	 var options = {
		url : "${request.contextPath}/newgkelective/${divideId!}/divideClass/saveMoveStudent",
		dataType : 'json',
		success : function(data){
 			var jsonO = data;
	 		if(!jsonO.success){
	 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
	 			isSubmit=false;
	 			return;
	 		}else{
	 			layer.closeAll();
	 			layer.msg("保存成功", {
						offset: 't',
						time: 2000
					});
	 			 refreshJxbStuList();
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#moverStudentForm").ajaxSubmit(options);
}    

function mycheck(){
	var arrMap={};
	var f=false;
	$("select[name='courseClassId']").each(function(){
		var vv=$(this).val();
		var arr=vv.split("_");
		console.log(arr[0]);
		if(arrMap[arr[0]]){
			f=true;
			layer.tips('班级科目重复',$(this), {
				tipsMore: true,
				tips: 3
			});
			return false;
		}else{
			arrMap[arr[0]]=arr[1];
		}
	})
	if(f){
		return false;
	}
	return true;
}
   

</script>
