<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="box box-default">
	<div class="box-body">
		
		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
			<li <#if classesType?default('1')=='1'>class="active"</#if> role="presentation" onclick="changeInput('1')"><a href="#" role="tab" data-toggle="tab">学科竞赛</a></li>
			<li <#if classesType?default('1')=='2'>class="active"</#if> role="presentation" onclick="changeInput('2')"><a href="#" role="tab" data-toggle="tab">校内奖励</a></li>
			<li <#if classesType?default('1')=='3'>class="active"</#if> role="presentation" onclick="changeInput('3')"><a href="#" role="tab" data-toggle="tab">节日活动奖励</a></li>
			<li <#if classesType?default('1')=='4'>class="active"</#if> role="presentation" onclick="changeInput('4')"><a href="#" role="tab" data-toggle="tab">其他奖励</a></li>
		</ul>
		<div class="tab-content">
			
			<div class="table-container">
				<div class="table-container-header text-right">
					<button type="button" class="btn btn-blue " onclick="doImport()">导入</button>
					<#if classesType?default('1')=='3' >
						<button type="button" class="btn btn-blue js-addActivityAward" >新建</button>
					</#if>
				</div>
				<div class="table-container-body">
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
											<td class="combine_classes" dataclasses="${project.rewardClasses!}-第${project.rewardPeriod?default(1)}届">${project.rewardClasses!}
											<#if classesType?default('1')=='3'>-第${project.rewardPeriod?default(1)}年
												&nbsp;&nbsp;
												<a href="javascript:upActivityAward('${project.rewardClasses!}','${project.rewardPeriod!}','${project.acadyear!}','${project.semester!}')">修改</a>
												<a style="color:red;" href="javascript:delActivityAward('${project.rewardClasses!}'+'-第'+'${project.rewardPeriod?default(1)}届')">删除</a>
											</#if></td>
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
												<a href="javascript:rewardInput('${setting.id!}','${project.id!}')">去录入</a>
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
					<#if classesType?default('1')=='3'>
						<#if dyStudentRewardSettingDtoList?exists && dyStudentRewardSettingDtoList?size gt 0>
						  	<@htmlcom.pageToolBar container=".model-div" class="noprint"/>
						</#if>
					</#if>
				</div>
			</div>	
		</div>
	</div>	
</div>

<#if classesType?default('1')=='3'>
<!-- S 新建活动奖励 -->
<div class="layer layer-addActivityAward">
	<form id="periodForm">
	<div class="layer-content">
		<div class="form-horizontal">
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">学年：</label>
				<div class="col-sm-9">
					<div class="filter-content">
					<select name="acadyear" id="acadyear" class="form-control" >
						<#list acadyearList as ac>
							<option value="${ac}" <#if ac == acadyear>selected</#if>>${ac!}</option>
						</#list>
					</select>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">学期：</label>
				<div class="col-sm-9">
					<select name="semester" id="semester" class="form-control" >
						<option value="1" <#if semester == 1>selected</#if>>第一学期</option>
						<option value="2" <#if semester == 2>selected</#if>>第二学期</option>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">年份：</label>
				<div class="col-sm-9">
					第
					<input id="rewardPeriod" type="text" class="form-control inline-block number" maxlength="4" vtype="int" max="9999" min="1" nullable="false"/>
					年
				</div>
			</div>
			<div class="form-group" id="rewardRadioDivId">
				<label class="col-sm-2 control-label no-padding-right">类型：</label>
				<div class="col-sm-9 checkDiv">
					<#if dyStudentRewardSettingDefaultDtoList?exists && (dyStudentRewardSettingDefaultDtoList?size>0)>
					<#list dyStudentRewardSettingDefaultDtoList as dto>
						<#assign project =dto.dyStudentRewardProject/>
						<#if project.rewardPeriod == 0 && project.projectName=='个人'>
							<label><input  type="radio" name="rewardRadio" class="wp" value="${project.rewardClasses!}" ><span class="lbl">${project.rewardClasses!}</span></label>
						</#if>
					</#list>
					<#else>
						必须先设置节假日综合素质分
					</#if>
				</div>
			</div>
		</div>
	</div>
	</form>
</div>
</#if>
<script>
	var isSubmit = false;
	function upActivityAward(rewardClasses,rewardPeriodOld,acadyearOld,semesterOld){
		var type=rewardClasses+"-第"+rewardPeriodOld+"届";
		var pIds='';
		$(".combine_name").each(function (){
			if($(this).attr("datanames")==type){
				if(!pIds || pIds==''){
					pIds=$(this).attr("datavalue");
				}else{
					pIds+=','+$(this).attr("datavalue");
				}
			}
		});
		debugger;
		$("#rewardPeriod").val(rewardPeriodOld);
		$("#rewardRadioDivId").hide();
		var acadyear_options = document.getElementById("acadyear").options;
		for (i=0; i<acadyear_options.length; i++){
			if (acadyear_options[i].value == acadyearOld)  
			{
				acadyear_options[i].selected = true;
			}
		}
		var semester_options = document.getElementById("semester").options;
		for (i=0; i<semester_options.length; i++){
			if (semester_options[i].value == semesterOld)  
			{
				semester_options[i].selected = true;
			}
		}
		layer.open({
			type: 1,
			shade: 0.5,
			title: '修改',
			area: '420px',
			btn: ['确定', '取消'],
			yes: function(index){
				var check = checkValue('#periodForm');
				if(!check){
					return;
				}
				var rewardPeriod = $("#rewardPeriod").val();
				var acadyear = $("#acadyear").val();
				var semester = $("#semester").val();
				if(isSubmit){
					return;
				}
				$.ajax({
					url:"${request.contextPath}/stuwork/studentReward/studentRewardFestivalAdd",
					data: { 'rewardPeriod': rewardPeriod,'rewardClasses':rewardClasses,'acadyear':acadyear,'semester':semester,'pIds':pIds},  
					type:'post',
					success:function(data) {
						var jsonO = JSON.parse(data);
							if(jsonO.success){
								layer.closeAll();
								layerTipMsg(jsonO.success,"成功",jsonO.msg);
								changeInput('3');
							}else{
								isSubmit = false;
								layerTipMsg(jsonO.success,"失败",jsonO.msg);
							}
					},
			 		error : function(XMLHttpRequest, textStatus, errorThrown) {
			 			
					}
				});
				
			},
			content: $('.layer-addActivityAward')
		})
	}
	function delActivityAward(type){
		layer.confirm('删除该年节日活动后，将一并删除该年活动的所有奖励信息', {
			btn: ['确定', '取消'],
			yes: function(index){
				var pIds='';
				$(".combine_name").each(function (){
					if($(this).attr("datanames")==type){
						if(!pIds || pIds==''){
							pIds=$(this).attr("datavalue");
						}else{
							pIds+=','+$(this).attr("datavalue");
						}
					}
				});
				$.ajax({
					url:"${request.contextPath}/stuwork/studentReward/studentRewardDel",
					data: { 'pIds': pIds},  
					type:'post',
					success:function(data) {
						var jsonO = JSON.parse(data);
						debugger;
						if(jsonO.success){
							layer.closeAll();
							layerTipMsg(jsonO.success,"成功",jsonO.msg);
							changeInput('3');
						}else{
							layerTipMsg(jsonO.success,"失败",jsonO.msg);
							layer.close(index);
						}
					},
			 		error : function(XMLHttpRequest, textStatus, errorThrown) {
			 			
					}
				});
			}
		});
	}
	$(function(){
			
		<#if classesType?default('1')=='3'>
			dealInputTable('.combine_classes');
			dealInputTable('.combine_name');
		</#if>
		$('.js-addActivityAward').on('click', function(e){
			e.preventDefault();
			$("#rewardRadioDivId").show();
			$("#rewardPeriod").val('');
			$("input[name='rewardRadio']").each(function(){
				$(this).attr("checked",false);
			});
			
			layer.open({
				type: 1,
				shade: 0.5,
				title: '新增',
				area: '420px',
				btn: ['确定', '取消'],
				yes: function(index){
					var check = checkValue('#periodForm');
					if(!check){
						return;
					}
					var rewardPeriod = $("#rewardPeriod").val();
					var rewardClasses = $("input[name='rewardRadio']:checked").val();
					var acadyear = $("#acadyear").val();
					var semester = $("#semester").val();
					if(!rewardClasses){
						layer.tips("必须选择节假日类型", ".checkDiv", {
							tipsMore: true,
							tips:3		
						});
						return;
					}
					if(isSubmit){
						return;
					}
					$.ajax({
						url:"${request.contextPath}/stuwork/studentReward/studentRewardFestivalAdd",
						data: { 'rewardPeriod': rewardPeriod,'rewardClasses':rewardClasses,'acadyear':acadyear,'semester':semester},  
						type:'post',
						success:function(data) {
							var jsonO = JSON.parse(data);
								if(jsonO.success){
									layer.closeAll();
									layerTipMsg(jsonO.success,"成功",jsonO.msg);
									changeInput('3');
								}else{
									isSubmit = false;
									layerTipMsg(jsonO.success,"失败",jsonO.msg);
								}
						},
				 		error : function(XMLHttpRequest, textStatus, errorThrown) {
				 			
						}
					});
					
				},
				content: $('.layer-addActivityAward')
			})
		});

		$('.js-del').on('click', function(e){
			e.preventDefault();
			var that = $(this);

			layer.confirm('确定删除吗？', {
				btn: ['确定', '取消'],
				yes: function(index){
					that.closest('tr').remove();
					layer.close(index);
				}
			})
		});
	
	});
	
	
	function dealInputTable(name){
		var tempHtml='';
		var tdArray={};
		$(name).each(function (){
			if($(this).html()!=''){
				
				if(tempHtml == $(this).attr("dataclasses")){
					var tdGet = tdArray[tempHtml]; 	
					tdGet[tdGet.length] = $(this);
				}else{
					tempHtml= $(this).attr("dataclasses");
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
	
	
	
	function rewardInput(id,projectId){
		
		$(".model-div").load("${request.contextPath}/stuwork/studentReward/studentRewardInputList?settingId="+id+"&projectId="+projectId+"&classesType=${classesType!}"<#if classesType?default('1')=='3'>+"&_pageSize=${_pageSize!}&_pageIndex=${_pageIndex!}"</#if>);
	}
	
	function changeInput(classesType){
		$(".model-div").load("${request.contextPath}/stuwork/studentReward/studentRewardInputPage?classesType="+classesType);
	}
	
	
	function doImport(){
		$(".model-div").load("${request.contextPath}/stuwork/studentReward/studentRewardPointImport/main?classesType=${classesType!}");
	}

	
</script>
		
