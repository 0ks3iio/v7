<#import "/fw/macro/popupMacro.ftl" as popupMacro>
<form id="planForm">
	<div class="tab-content">
		<table class="table table-bordered">
		    <thead>
		        <tr>
		            <th style="width:20%;">科目</th>
		            <th style="width:20%;">至少需要教师数</th>
		            <th>任课教师</th>
		        </tr>
		    </thead>
		    <tbody>
		    	
		    		<#if dtoList?exists && (dtoList?size>0)>
		            	<#list dtoList as item>
		            		<tr>
		            		<td>
		            		<input type="hidden" name="dtoList[${item_index}].teacherPlanId" value="${item.teacherPlanId!}">
		            		${item.subjectName!}
		            		</td>
		            		<td>${item.minNum?default(0)}</td>
		            		<td>
		            		<input type="hidden" id="indexId${item.teacherPlanId!}" value="${item_index}">
		            		<input type="hidden" id="teacherPlanId${item_index}" value="${item.teacherPlanId!}">
		            		<div class="publish-course-g" id="recommend-list${item_index!}">
		            			<input type="hidden" name="dtoList[${item_index}].teacherIds" value="${item.teacherIds!}">
		            			<#if item.teacherNameList?exists && item.teacherNameList?size gt 0>
		            			<#list item.teacherNameList as tName>
		            			<span>${tName!}</span>
		            			</#list>
		            			</#if>
		            			<a href="javascript:void(0)" style="font-size:14px" onclick="alertList('${item.teacherPlanId!}','${item.teacherIds!}')">修改</a></div>
		            		</td>
		            		</tr>
		            	</#list>
		            </#if>
		    	
			</tbody>
		</table>
	</div>

    <div class="text-center">
        <a class="btn btn-white" onclick="goback();" href="#">上一步</a>
        <a  onclick="saveJxbTeaCla();" class="btn btn-blue "> 保存</a>
    </div>
</form>


<script>
	$(function(){
		
	});
var isSubmit=false;
function saveJxbTeaCla(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	var checkVal = checkValue('#planForm');
	if(!checkVal){
	 	isSubmit=false;
	 	return;
	}
	// 提交数据
	var ii = layer.load();
	var options = {
		url : '${request.contextPath}/newgkelective/${divideId!}/subjectTeacherArrange/saveClassJxb?arrayItemId=${arrayItemId!}',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.msg(data.msg, {offset: 't',time: 2000});
	 			var url = "${request.contextPath}/newgkelective/${divideId!}/teacherClass/choosedJxb?gradeId=${gradeId!}&arrayId=${arrayId!}&arrayItemId=${arrayItemId!}";
	 			$("#contentId").load(url);
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
			}
			layer.close(ii);
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#planForm").ajaxSubmit(options);
}

function alertList(teacherPlanId,teacherIds){
 	 var url = "${request.contextPath}/newgkelective/${divideId}/getTeacherList/page?arrayItemId=${arrayItemId!}&teacherPlanId="+teacherPlanId+"&teacherIds="+teacherIds;
	 indexDiv = layerDivUrl(url,{title: '选择教师',width:850,height:300});
}
	
</script>