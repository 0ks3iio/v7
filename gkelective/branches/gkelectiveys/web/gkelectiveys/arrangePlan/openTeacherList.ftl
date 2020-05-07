<div class="tab-content" id="resultListDiv">
<form id="teacherEditForm">
<table class="table table-striped table-bordered table-hover no-margin mainTable">
    <thead>
		<tr>
			<th><#if viewtype="1">科目<#else>${PCKC!}</#if></th>
			<th>班级</th>
			<th>选考类型</th>
			<th><#if viewtype="1">${PCKC!}<#else>科目</#if></th>
			<th>总人数</th>
			<th>男生</th>
			<th>女生</th>
			<th>班级平均分</th>
			<th><#if viewtype="1">老师安排<#else>场地安排</#if></th>
			<th class="noprint">查看学生</th>
        </tr>
    </thead>
    <tbody>
    <#assign index=-1>
	<#if gkBatchMap?? && (gkBatchMap?size>0)>
		<#list gkBatchMap?keys as key>
			<#list gkBatchMap[key] as item>
				<tr>
					<#if item_index == 0>
						<td rowSpan='${gkBatchMap[key]?size}'>${key}</td>
					</#if>
					<td>
					
					<#assign index=index+1>
					<input type="hidden" id="batchIds_${index}" name="dtolist[${index}].batchIds" value="${item.id!}-${item.batch}">
					<input type="hidden" id ="classId_${index}" name="dtolist[${index}].classId" value="${item.teachClassId!}"/>
					<input type="hidden" id ="batch_${index}" name="dtolist[${index}].batch" value="${item.batch}"/>	
					${item.className!}</td>
					<td><#if item.classType?default('') == 'A'>选考<#else>学考</#if></td>
					
					<td><#if viewtype="1">${PCKC!}${item.batch!}<#else>${item.subjectName!}</#if></td>
					
					<td>${item.number!}</td>
					<td>${item.manNumber!}</td>
					<td>${item.womanNumber!}</td>
					<td>${item.averageScore!}</td>
					<td>
					<#if viewtype="1">
						<input type="hidden" id ="oldId_${index}" name="dtolist[${index}].oldId" value="${item.teacherId!}"/>
						<select  name="dtolist[${index}].teacherId" id="teacherId_${index}" onchange="checkTeacherId('${index}')">
							<#if teacherList?exists && (teacherList?size>0)>
								<option value="">请选择</option>
								<#list teacherList as teacher>
									<option value="${teacher.id!}" <#if item.teacherId?default('')==teacher.id>selected="selected"</#if>>${teacher.teacherName!}</option>
								</#list>
							<#else>
								<option value="">请选择</option>
							</#if>
						</select>
					<#else>	
						<input type="hidden" id ="oldId_${index}" name="dtolist[${index}].oldId" value="${item.placeId!}"/>
						<select  name="dtolist[${index}].place" id="placeId_${index}" onchange="checkPlace('${index}')">
							<#if placeMap?exists && (placeMap?size>0)>
								<option value="">请选择</option>
								<#list placeMap?keys as key>
									<option value="${key!}" <#if item.placeId?default('')==key>selected="selected"</#if>>${placeMap[key]?default("")}</option>
								</#list>
							<#else>
								<option value="">请选择</option>
							</#if>
						</select>
						
					</#if>
					</td>
					<td>
					<a href="#" onclick="showSingleStu('${item.id!}','${item.teachClassId!}')">查看</a>
					</td>
				</tr>
			</#list>
		</#list>
	</#if>
	</tbody>
</table>
</form>
	<div class="text-right">
		<a href="javascript:" class="btn btn-blue  plan-nextStep" >下一步</a>
	</div>	
</div>
<script>
	$(function(){
		$('.plan-nextStep').on('click',function(){
			<#if viewtype="1">
			toTeacherPlace('2');
			<#else>
			toAllResult();
			</#if>
		});
	})
	function showSingleStu(batchId,teachClassId){
		var url = "${request.contextPath}/gkelective/${arrangeId!}/arrangePlan/single/detail/page?planId=${planId!}&viewtype=${viewtype!}&batchId="+batchId+"&teachClassId="+teachClassId;
		$("#resultListDiv").load(url);
	}
	<#if viewtype="2">
	function checkPlace(index){
		var oldId=$("#oldId_"+index).val();
		var placeId=$("#placeId_"+index).val();
		var batchIds=$("#batchIds_"+index).val();
		var url = "${request.contextPath}/gkelective/${arrangeId!}/arrangePlan/place/checkSave?planId=${planId!}";
		$.ajax({
			url:url,
			data: {'batchIds':batchIds,'placeId':placeId},
			type:'post',
			success:function(data) {
			    var jsonO = JSON.parse(data);
			    if(jsonO.success){
			    	 $("#oldId_"+index).val(placeId);
			    	 layer.msg("操作成功", {
						offset: 't',
						time: 2000
					});
			    }else{
			        layerTipMsg(jsonO.success,"失败",jsonO.msg);
			        $("#placeId_"+index).val(oldId);
			    }
		    },
		     error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
	<#else>
	function checkTeacherId(index){
		var oldId=$("#oldId_"+index).val();
		var teacherId=$("#teacherId_"+index).val();
		var teacherName=$("#teacherId_"+index).find("option:selected").text();
		var teaClsId=$("#classId_"+index).val();
		var batch=parseInt($("#batch_"+index).val());
		var url = "${request.contextPath}/gkelective/${arrangeId!}/arrangePlan/teacher/checkSave?planId=${planId!}";
		$.ajax({
	        url:url,
	        data: {'teaClsId':teaClsId,'batch':batch,'teacherId':teacherId},
	        type:'post',
	        success:function(data) {
	            var jsonO = JSON.parse(data);
	            if(jsonO.success){
	            	$("#oldId_"+index).val(teacherId);
	            	layer.msg("操作成功", {
						offset: 't',
						time: 2000
					});
	            }else{
	                layerTipMsg(jsonO.success,"失败",jsonO.msg);
	     			if(oldId=='' || oldId=="00000000000000000000000000000000"){
	     				 $("#teacherId_"+index).val('');
	     			}else{
	                	$("#teacherId_"+index).val(oldId);
	                }
	            }
	            
	        },
	         error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
	}
	
	</#if>
	<#--
		var isSubmit=false;
		function saveTeacherOrPlace(){
			if(isSubmit){
				return;
			}
			isSubmit=true;
			// 提交数据
			var ii = layer.load();
			<#if viewtype="1">
				var url='${request.contextPath}/gkelective/${arrangeId}/arrangePlan/saveTeacher?planId=${planId}';
			<#else>
				var url='${request.contextPath}/gkelective/${arrangeId}/arrangePlan/savePlace?planId=${planId}';
			</#if>
			var options = {
				url : url,
				dataType : 'json',
				success : function(data){
			 		if(data.success){
			 			layer.closeAll();
					  	findByCondition();
			 		}
			 		else{
			 			layerTipMsg(data.success,"失败",data.msg);
			 			isSubmit=false;
					}
					layer.close(ii);
				},
				clearForm : false,
				resetForm : false,
				type : 'post',
				error:function(XMLHttpRequest, textStatus, errorThrown){} 
			};
			$("#teacherEditForm").ajaxSubmit(options);
		}
	-->
</script>
