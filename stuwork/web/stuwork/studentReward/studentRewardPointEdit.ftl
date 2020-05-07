<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
		

				<div class="table-container-body" style="overflow-y: auto; height: 400px;">
					<form id="subForm">
					<table class="table table-bordered">
						<thead>
							<tr>
								<th style="width: 318px;">类型</th>
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
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<#list dyStudentRewardSettingDtoList as dto>
								<#assign project =dto.dyStudentRewardProject/>
								<#assign dyStudentRewardSettings = dto.dyStudentRewardSettings>
								<#if dyStudentRewardSettings?exists && (dyStudentRewardSettings?size>0)>
									<#if classesType?default('1')=='3' && project.rewardPeriod==0>
									<#else>
										<#list dyStudentRewardSettings as setting>
										<tr>
											<td class="combine_classes" dataclasses="${project.rewardClasses!}-第${project.rewardPeriod?default(1)}届">${project.rewardClasses!}</td>
											<#if classesType?default('1')=='2'>
												<td class="combine_remark" dataclasses="${project.rewardClasses!}">${project.projectRemark!}</td>
											</#if>
											<#if classesType?default('1')=='3'> 
												<td class="combine_name" datavalue='${project.id!}' datanames='${project.rewardClasses!}-第${project.rewardPeriod?default(1)}届' dataclasses="${project.rewardClasses!}-第${project.rewardPeriod?default(1)}届${project.projectName!}">${project.projectName!}</td>
											<#else>
												<td class="combine_name" dataclasses="${project.rewardClasses!}">${project.projectName!}</td>
											</#if>
											<#if classesType?default('1')=='1'>
												<td class="combine_grade" dataclasses="${project.projectName!}">${setting.rewardGrade!}</td>
											</#if>
											<#if classesType?default('1')!='2'>
											<td>${setting.rewardLevel!}</td>
											</#if>
											<td>
												${setting.rewardPoint!}
											</td>
											<td>
												<#--a href="javascript:rewardInput('${setting.id!}')">去录入</a-->
												<span class="ui-radio ui-radio-current" data-name="a" checked="checked"><input type="radio" class="radio" name="projectId" value="${project.id!}-${setting.id!}" <#if projectId==project.id && settingId==setting.id>checked="checked"</#if>></span>
											</td>
										</tr>
										</#list>
									</#if>
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
				<div class="layer-footer">
	                  <button class="btn btn-lightblue" id="item-commit">确定</button>
	                  <button class="btn btn-grey" id="item-close">取消</button>
                </div>
<script>
$("#item-close").on("click", function(){
	doLayerOk("#item-commit", {
	redirect:function(){},
	window:function(){layer.closeAll()}
	});
 });


// 确定按钮操作功能
var isSubmit=false;
$("#item-commit").on("click", function(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	$(this).addClass("disabled");
	var projectId=$('input[name="projectId"]:checked').val();//获取选中的单选的值
	// 提交数据
	$.ajax({
		url:"${request.contextPath}/stuwork/studentReward/studentRewardPointUpdate",
		data: { 'projectId':projectId,'pointId':'${pointId!}'},  
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
				if(jsonO.success){
					layer.closeAll();
					layerTipMsg(jsonO.success,"成功",jsonO.msg);
					flashPage();
				}else{
					isSubmit = false;
					layerTipMsg(jsonO.success,"失败",jsonO.msg);
				}
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {
 			
		}
	});
 });		
 
 function flashPage(){
		$(".model-div").load("${request.contextPath}/stuwork/studentReward/studentRewardInputList?settingId=${settingId!}&acadyear="+$("#acadyear").val()+"&semester="+$("#semester").val());
	}
</script>
		
