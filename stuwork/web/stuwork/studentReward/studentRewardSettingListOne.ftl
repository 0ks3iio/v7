<div class="table-container-header text-right">
	<button type="button" class="btn btn-blue" onclick="saveSetting3()">保存</button>
</div>
<div class="table-container-body" >
	<form id="subForm">
	<table class="table table-bordered festival">
		<thead>
			<tr>
				<th>类型</th>
				<th>项目名称</th>
				<th>奖级</th>
				<th>分值</th>
				<th>备注</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<#assign countIndex =0/>
			<#list dyStudentRewardSettingDtoList as dto>
				<#assign project =dto.dyStudentRewardProject/>
				<#assign dyStudentRewardSettings = dto.dyStudentRewardSettings>
				<#if dyStudentRewardSettings?exists && (dyStudentRewardSettings?size>0)>
				<#list dyStudentRewardSettings as setting>
					<tr>
						<td class="combine_classes" dataclasses="${project.rewardClasses!}" data-type="${project.rewardClasses!}">${project.rewardClasses!}</td>
						<td class="combine_name" dataclasses="${project.rewardClasses!}" data-name="${project.rewardClasses!}-${dto_index}">${project.projectName!}</td>
						<td><input type="text" type="text" id='settings${countIndex}rewardLevel' nullable="false" maxlength="10" name="dyStudentRewardSettings[${countIndex}].rewardLevel" value='${setting.rewardLevel!}'></td>
						<td>
							<input name="dyStudentRewardSettings[${countIndex}].id" id="" type="hidden" value="${setting.id!}" type="text"/>
							<input name="dyStudentRewardSettings[${countIndex}].unitId" id="" type="hidden" value="${setting.unitId!}" type="text"/>
							<input name="dyStudentRewardSettings[${countIndex}].rewardLevelOrder" id="" type="hidden" value="${setting.rewardLevelOrder!}" type="text"/>
							<input name="dyStudentRewardSettings[${countIndex}].rewardGrade" id="" type="hidden" value="${setting.rewardGrade!}" type="text"/>
							<#--<input name="dyStudentRewardSettings[${countIndex}].rewardLevel" id="" type="hidden" value="${setting.rewardLevel!}" type="text"/>-->
							<input name="dyStudentRewardSettings[${countIndex}].rewardPeriod" id="" type="hidden" value="${setting.rewardPeriod!}" type="text"/>
							<input name="dyStudentRewardSettings[${countIndex}].projectId" id="" type="hidden" value="${setting.projectId!}" type="text"/>
							<input name="dyStudentRewardSettings[${countIndex}].rewardPoint"  class="point_input"  id=""  value="${setting.rewardPoint!}" type="text"/>
						</td>
						<td>
							<input name="dyStudentRewardSettings[${countIndex}].remark" maxlength="50" class="remark_input" id=""  value="${setting.remark!}" type="text"/>												
						</td>
						<td><a <#if setting_index==0>class="table-btn color-blue" style="opacity: 0.2"<#else>class="table-btn color-blue js-del"</#if> data-del="${project.rewardClasses!}-${dto_index}" href="javascirpt:;">删除</a></td>
					</tr>
					<#if setting_index==(dyStudentRewardSettings?size-1)>
						<tr>
							<td class="combine_classes" dataclasses="${project.rewardClasses!}" data-type="${project.rewardClasses!}">${project.rewardClasses!}</td>
							<td class="combine_name" dataclasses="${project.rewardClasses!}" data-name="${project.rewardClasses!}-${dto_index}">${project.projectName!}</td>
							<td data-add="${project.rewardClasses!}-${dto_index}" colspan="4" data-value="${project.id!}" class="text-center"><a class="table-btn color-blue js-add" href="javascript:;">新增</a></td>
						</tr
					</#if>
				<#assign countIndex=countIndex+1/>
				</#list>
				<#else>
					<tr>
						<td class="combine_classes">${dto.rewardClasses!}</td>
						<td class="combine_name">${dto.projectName!}</td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
				</#if>
			</#list>
		</tbody>
	</table>
	</form>
</div>
<script>
	var isSubmit=false;
	function saveSetting3(){
		if(isSubmit){
			return;
		}
		var x=0;
		$(".point_input").each(function(){
			var val = $(this).val();
			if(val!=''){
				$(this).attr("id","create"+x).attr("decimalLength","2").attr("vtype","number").attr("max","999").attr("min","0.01");
				x++;
			}else{
				$(this).attr("id","");
			}
		});
		var check = checkValue('.table-container-body');
		if(!check){
			return;
		}
		isSubmit=true;
		var options = {
			url : "${request.contextPath}/stuwork/studentReward/settingSave?classesType=3",
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
					changeSetting('3');
				}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#subForm").ajaxSubmit(options);
	}
	var countIndex='${countIndex!}';
	$(function(){
		dealTable('.combine_classes');
		dealTable('.combine_name');
		$(".js-add").click(function(){
			countIndex++;
			var add = $(this).parent().data("add");
			var projectId = $(this).parent().data("value");
			var type = add.split("-")[0];
			var tr = '<tr>'+
						'<td><input type="text" id="settings'+countIndex+'rewardLevel" nullable="false" maxlength="10"  name="dyStudentRewardSettings['+countIndex+'].rewardLevel"></td><input name="dyStudentRewardSettings['+countIndex+'].projectId" type="hidden" value="'+projectId+'"/>'+
						'<td><input name="dyStudentRewardSettings['+countIndex+'].rewardPoint"  class="point_input"  id="" type="text"/></td>'+
						'<td><input name="dyStudentRewardSettings['+countIndex+'].remark" maxlength="50" class="remark_input" id="" type="text"/></td>'+
						'<td><a class="table-btn color-blue js-del" data-del="'+add+'" href="javascript:;">删除</a></td>'+
					  +'</tr>';
			$(".festival td").each(function(){
				if($(this).data("type") === type || $(this).data("name") === add){
					var rowspan = parseInt($(this).attr("rowspan")) + 1;
					$(this).attr("rowspan",rowspan);
				}
			})
			$(this).parents("tr").before(tr);
		})
		$(".festival").on('click','.js-del',function(){
			var del = $(this).data("del");
			var type = del.split("-")[0];
			$(".festival td").each(function(){
				if($(this).data("type") === type || $(this).data("name") === del){
					var rowspan = parseInt($(this).attr("rowspan")) - 1;
					$(this).attr("rowspan",rowspan);
				}
			})
			$(this).parents("tr").remove();
		})
	});
</script>