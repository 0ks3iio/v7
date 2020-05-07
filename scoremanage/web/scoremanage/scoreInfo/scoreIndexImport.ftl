<div class="filter filter-f16">
    <div class="filter-item">
        <span class="filter-name">科目：</span>
        <div class="filter-content">
        	<select name="subjectId" id="subjectId" class="form-control" onChange="findByCondition()">
				<#if (infoList?exists && infoList?size>0)>
					<#list infoList as item>
			     	<option  value = "${item.subjectId!}_${item.id!}" <#if (item.courseName!) == (subType!) >selected="selected"</#if> >${item.courseName!}</option>
			     	</#list>
			     <#else>
			     	<option  value = "" >--请选择--</option>
			     </#if>
			</select>
        </div>
    </div>
     <div class="filter-item filter-item-right">
	   <a href="javascript:" class="btn btn-blue"  onclick="toScoreBack();">返回</a>
	</div>
</div>
<div id="importScoreDiv">
</div>
<script>
$(function(){
	findByCondition();
})
function toScoreBack(){
	$('#findDiv').attr("style","display:block;");
	$("#type").val(1);
	$("#classIdSearch").attr("onChange","searchList();");
	var subjectId = $("#subjectId").val().split('_')[0];
	searchList(subjectId);
}
function findByCondition(){
	var array = $("#subjectId").val().split('_');
	var courseId = array[0];
	var subjectInfoId = array[1];
	
    if(courseId==""){
    	layer.tips("科目不能为空", $("#subjectId"), {
				tipsMore: true,
				tips:3		
			});
		return;
    }
    
    //查询录入权限，如果成绩已经提交或者 没有录入权限，则不能导入
    var examId=$("#examId").val();
	var classType=$("#classType").val();
	var classIdSearch=$("#classIdSearch").val();
	var noLimit = $('#noLimit').val();
	
    var url = '${request.contextPath}/scoremanage/scoreInfo/checkInputPower';
    var params = 'classType='+classType+'&classIdSearch='+classIdSearch+'&examId='+examId
    			+'&subjectId='+courseId+'&subjectInfoId='+subjectInfoId+'&noLimit='+noLimit;
    $.ajax({url:url,
    	type:"POST",
    	data:params,
    	dataType:"JSON",
    	success:function(data){
    		if(!data.success){
    			layerTipMsg(false,"失败",data.msg);
    			$("#importScoreDiv").html(data.msg);
    			return;
    		}
    		
    		doImport();
    	}
    });
    
    
}
function doImport(){
	var examId=$("#examId").val();
	var classType=$("#classType").val();
	var classIdSearch=$("#classIdSearch").val();
	var subjectId = $("#subjectId").val().split('_')[0];
	var gradeCode=$("#gradeCode").val();
	var str='?examId='+examId+'&classIdSearch='+classIdSearch+'&classType='+classType+'&subjectId='+subjectId+'&gradeCode='+gradeCode;
    var url =  '${request.contextPath}/scoremanage/scoreImport/main'+str;
	$("#importScoreDiv").load(url);
}
</script>