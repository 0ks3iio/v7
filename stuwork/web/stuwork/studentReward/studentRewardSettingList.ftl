
<div class="box box-default">
	<div class="box-body">
		
		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
			<li <#if classesType?default('1')=='1'>class="active"</#if> role="presentation" onclick="changeSetting('1')"><a href="#" role="tab" data-toggle="tab">学科竞赛</a></li>
			<li <#if classesType?default('1')=='2'>class="active"</#if> role="presentation" onclick="changeSetting('2')"><a href="#" role="tab" data-toggle="tab">校内奖励</a></li>
			<li <#if classesType?default('1')=='3'>class="active"</#if> role="presentation" onclick="changeSetting('3')"><a href="#" role="tab" data-toggle="tab">节日活动奖励</a></li>
		</ul>
		<div class="tab-content">
			<div id="aa" class="tab-pane active" role="tabpanel">
	
				<div class="explain">
					<p>说明：奖励分值，输入为空则在录入时输入。有值,录入时无需输入</p>
				</div>
			</div>
			
			<div class="table-container" id="showList">
				<div class="table-container-header text-right">
					<#if classesType?default('1')=='1'>
						<a  href="#" onclick="onProjectImport()"  class="btn btn-blue">导入项目</a>
						<a  href="#" onclick="onEditProject()"  class="btn btn-blue">新增项目</a>
					</#if>
					<button type="button" class="btn btn-blue" onclick="saveSetting()">保存</button>
				
				</div>
				<div class="table-container-body" >
					<form id="subForm">
					<table class="table table-bordered">
						<thead>
							<tr>
								<th>类型</th>
								<#if classesType?default('1')=='2'>
								<th>分类</th>
								</#if>
								<th>项目名称</th>
								<#if classesType?default('1')=='1'>
								<th>级别</th>
								</#if>
								<#if classesType?default('1')!='2'>
								<th>奖级</th>
								</#if>
								<th>分值</th>
								<th>备注</th>
								<#if classesType?default('1')=='1'>
								<th>操作</th>
								</#if>
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
									<td class="combine_classes" dataclasses="${project.rewardClasses!}">${project.rewardClasses!}
									<#if project.rewardClasses?default("")=="学生干部">（每学年就高）</#if>
									</td>
									<#if classesType?default('1')=='2'>
										<td class="combine_remark" style="width: 88px;" dataclasses="${project.rewardClasses!}">${project.projectRemark!}
											<#if project.projectRemark?default("")=="评优" || project.projectRemark?default("")=="先进">（每学年就高）</#if>
										</td>
									</#if>
									<td class="combine_name" dataclasses="${project.rewardClasses!}">${project.projectName!}</td>
									<#if classesType?default('1')=='1'>
										<td class="combine_grade" dataclasses="${project.projectName!}">${setting.rewardGrade!}</td>
									</#if>
									<#if classesType?default('1')!='2'>
									<td>${setting.rewardLevel!}</td>
									</#if>
									<td>
										<input name="dyStudentRewardSettings[${countIndex}].id" id="" type="hidden" value="${setting.id!}" type="text"/>
										<input name="dyStudentRewardSettings[${countIndex}].unitId" id="" type="hidden" value="${setting.unitId!}" type="text"/>
										<input name="dyStudentRewardSettings[${countIndex}].rewardLevelOrder" id="" type="hidden" value="${setting.rewardLevelOrder!}" type="text"/>
										<input name="dyStudentRewardSettings[${countIndex}].rewardGrade" id="" type="hidden" value="${setting.rewardGrade!}" type="text"/>
										<input name="dyStudentRewardSettings[${countIndex}].rewardLevel" id="" type="hidden" value="${setting.rewardLevel!}" type="text"/>
										<input name="dyStudentRewardSettings[${countIndex}].rewardPeriod" id="" type="hidden" value="${setting.rewardPeriod!}" type="text"/>
										<input name="dyStudentRewardSettings[${countIndex}].projectId" id="" type="hidden" value="${setting.projectId!}" type="text"/>
										<input name="dyStudentRewardSettings[${countIndex}].rewardPoint"  class="point_input"  id=""  value="${setting.rewardPoint!}" type="text"/>
									</td>
									<td>
										<input name="dyStudentRewardSettings[${countIndex}].remark" maxlength="50" class="remark_input" id=""  value="${setting.remark!}" type="text"/>												
									</td>
									<#if classesType?default('1')=='1'>
									<td>
										<a href="javascript:deleteSetting('${setting.id!}','${setting.projectId!}')">删除</a>
										<a href="javascript:updateSetting('${setting.id!}','${setting.projectId!}')">修改</a>
									</td>
									</#if>
								</tr>
								<#assign countIndex=countIndex+1/>
								</#list>
								<#else>
									<tr>
										<td class="combine_classes">${dto.rewardClasses!}</td>
										<#if classesType?default('1')=='2'>
											<td>${dto.projectRemark!}</td>
										</#if>
										<td class="combine_name">${dto.projectName!}</td>
										<#if classesType?default('1')=='1'>
											<td class="combine_grade"></td>
										</#if>
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
			</div>	
		</div>
	</div>	
</div>
	
<script>
	$(function(){
		dealTable('.combine_classes');
		dealTable('.combine_grade');
		dealTable('.combine_remark');
		dealTable('.combine_name');
	});
	
	
	function dealTable(name){
		var tempHtml='';
		var tdArray={};
		$(name).each(function (){
			if($(this).html()!=''){
				
				if(tempHtml == ($(this).html()+$(this).attr("dataclasses"))){
					var tdGet = tdArray[tempHtml]; 	
					tdGet[tdGet.length] = $(this);
				}else{
					tempHtml= $(this).html()+$(this).attr("dataclasses");
					var tdGet = []; 	
					tdGet[0] = $(this);
				}
				tdArray[tempHtml] = tdGet;
			}
		});
		for(var name in tdArray){
			for(var i=0;i<tdArray[name].length;i++){
				if(i==0){
					tdArray[name][i].attr("rowspan",tdArray[name].length);	
				}else{
					tdArray[name][i].remove();
				}
			}
		}
	}
	
	function changeSetting(classesType){
		if(classesType && classesType=='3'){
			$("#showList").load("${request.contextPath}/stuwork/studentReward/studentRewardByOne?classesType="+classesType);
		}else{
			$(".model-div").load("${request.contextPath}/stuwork/studentReward/studentRewardSettingPage?classesType="+classesType);
		}
	}
	
	var isSubmit=false;
	function saveSetting(){
	
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
		//alert(check);
		if(!check){
			return;
		}
		isSubmit=true;
		var options = {
			url : "${request.contextPath}/stuwork/studentReward/settingSave?classesType=${classesType!}",
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
					changeSetting('${classesType!}');
				}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#subForm").ajaxSubmit(options);
	}
	
	
	function onEditProject(){
		var url = "${request.contextPath}/stuwork/studentReward/studentRewardProjectPage";
		indexDiv = layerDivUrl(url,{title: "新增项目",width:610,height:330});
	}
	
	function deleteSetting(settingId,projectId){
		layer.confirm('确定删除吗？', {
			btn: ['确定', '取消'],
			yes: function(index){
				$.ajax({
					url:"${request.contextPath}/stuwork/studentReward/studentRewardSettingDelete",
					data: { 'settingId': settingId,'projectId':projectId},  
					type:'post',
					success:function(data) {
						var jsonO = JSON.parse(data);
							if(jsonO.success){
								layerTipMsg(jsonO.success,"成功",jsonO.msg);
								changeSetting('${classesType!}');
								layer.closeAll();
							}else{
								layerTipMsg(jsonO.success,"失败",jsonO.msg);
								layer.close(index);
							}
					},
			 		error : function(XMLHttpRequest, textStatus, errorThrown) {
			 			
					}
				});
			}
		})
	}
	function updateSetting(settingId,projectId){
		var url = "${request.contextPath}/stuwork/studentReward/studentRewardProjectPage?projectId="+projectId+"&settingId="+settingId;
		indexDiv = layerDivUrl(url,{title: "修改项目",width:610,height:330});
	}
	
	
	function onProjectImport(){
			$(".model-div").load("${request.contextPath}/stuwork/studentReward/studentRewardImport/main");
	}
</script>