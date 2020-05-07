<div class="filter filter-f16">
    <div class="filter-item">
        <span class="filter-name">导入科目：</span>
        <div class="filter-content" id="subjectIdTypeDiv">
        	<select name="subjectIdType" id="subjectIdType" class="form-control" onChange="findByCondition()">
				<#if (courseList?exists && courseList?size>0)>
					<#list courseList as item>
						<#if typeMap?exists && course73Map[item.id]??>
						<#assign subType=typeMap[item.id]?default("")>
							<#if subType?default("")=='0'>
								<option  value = "${item.id!}_1" <#if (item.subjectName!)+"选考" == (subTypeName!) || subjectId?default("")==item.id>selected="selected"</#if> >${item.subjectName!}选考</option>
								<option  value = "${item.id!}_2" <#if (item.subjectName!)+"学考" == (subTypeName!) || subjectId?default("")==item.id>selected="selected"</#if> >${item.subjectName!}学考</option>
							<#elseif subType?default("")=='1'>
								<option  value = "${item.id!}_1" <#if (item.subjectName!)+"选考" == (subTypeName!) || subjectId?default("")==item.id>selected="selected"</#if> >${item.subjectName!}选考</option>
							<#else>
								<option  value = "${item.id!}_2" <#if (item.subjectName!)+"学考" == (subTypeName!) || subjectId?default("")==item.id>selected="selected"</#if> >${item.subjectName!}学考</option>
							</#if>
					    <#else>
					     	<option  value = "${item.id!}" <#if (item.subjectName!) == (subTypeName!) || subjectId?default("")==item.id>selected="selected"</#if> >${item.subjectName!}</option>
					    </#if>
			     	</#list>
			     <#else>
			     	<option  value = "" >--请选择--</option>
			     </#if>
			</select>
        </div>
    </div>
    <div class="filter-item">
        <span class="filter-name">识别字段：</span>
        <div class="filter-content">
        	<select name="stuCard" id="stuCard" class="form-control" onChange="findByCondition()">
		     	<option  value = "1" >学号</option>
		     	<option  value = "2" >考号</option>
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
	$("#type").val(1);
	$('#findDiv').attr("style","display:block;");
	var subjectIdType = $("#subjectIdType").val();
	var subjectId='';
	if(subjectIdType){
		subjectId=subjectIdType.split("_")[0];
	}
	var classId=$("#classId").val();
	if(classId){
		$("#classId").attr("onChange","searchList();");
		searchList();
	}else{
		$('#importBtnDiv').attr("style","display:block;");
		$("#examId").attr("onChange","changeSubjectList();");
		searchList();
	}
}
function findByCondition(){
	var subjectIdType = $("#subjectIdType").val();
	var subjectId='';
	var subType='';
	if(subjectIdType){
		var ss=subjectIdType.split("_");
		subjectId=ss[0];
		if(ss.length>1){
			subType=ss[1];
		}
	}
    if(subjectId==""){
		layer.tips('请选择科目', $("#subjectIdTypeDiv"), {
			tipsMore: true,
			tips: 3
		});
		return;
    }
    
    //查询录入权限，如果成绩已经提交或者 没有录入权限，则不能导入
    var examId=$("#examId").val();
	var classType=$("#classType").val();
	var classId=$("#classId").val();
	var noLimit = $('#noLimit').val();
	if(!classId){
		classId="";
	}
	if(!classType){
		classType="";
	}
    var url = '${request.contextPath}/exammanage/scoreNewInput/checkInputPower';
    var params ='examId='+examId+'&subjectId='+subjectId+'&noLimit='+noLimit+"&subType="+subType+'&classType='+classType+'&classId='+classId;
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
    		doImport(subjectId,subType);
    	}
    });
    
    
}
function doImport(subjectId,subType){
	var examId=$("#examId").val();
	var stuCard = $("#stuCard").val();
	var classType = $("#classType").val();
	var classId = $("#classId").val();
	if(!classId){
		classId="";
	}
	if(!classType){
		classType="";
	}
	var str='?examId='+examId+'&subjectId='+subjectId+"&subType="+subType+"&stuCard="+stuCard+'&classType='+classType+'&classId='+classId;
    var url =  '${request.contextPath}/exammanage/scoreImport/main'+str;
	$("#importScoreDiv").load(url);
}
</script>