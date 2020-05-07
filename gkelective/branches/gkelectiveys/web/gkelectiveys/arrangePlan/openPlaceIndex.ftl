<div class="box-body">
<div class="row">
	<form id="planForm">
		<div class="col-xs-12">
			<div class="box-header">
				<h4 class="box-title">3科组合班教室安排</h4>
			</div>
			<table class="table table-striped table-bordered table-hover no-margin">
			    <thead>
			        <tr>
			            <th style="width:20%;">序号</th>
			            <th style="width:20%;">科目</th>
			            <th style="width:20%;">至少需要数量</th>
			            <th>任课老师</th>
			        </tr>
			    </thead>
			    <tbody>
			    	
			    		<#if subjectDtoList?exists && (subjectDtoList?size>0)>
			            	<#list subjectDtoList  as item>
			            		<tr>
			            		<td>
			            		 <input type="hidden" name="gkSubjectList[${item_index}].id" value="${item.gkSubjectId!}">
			            		 <input type="hidden" name="gkSubjectList[${item_index}].subjectId" value="${item.subjectId!}">
			            		 
			            		${item_index+1}
			            		</td>
			            		<td>${item.subjectName!}</td>
			            		<td>${item.teacherNum}</td>
			            		<td>
			            		<select  multiple vtype="selectMore" <#if (plan.step>0)>disabled="disabled" </#if> name="gkSubjectList[${item_index}].teacherIds" id="teacherIds_${item_index}" class="teacherIdsClass" data-placeholder="选择教师" >
									<#if teacherList?exists && (teacherList?size>0)>
										<option value=""></option>
										<#list teacherList as teacher>
											<#assign teasel=false />
											<#if item.teacherIds?exists>
											<#list item.teacherIds as teaid>
												<#if teaid==teacher.id>
									                <#assign teasel=true />
									                <#break>
									            </#if>
											</#list>
											</#if>
											<option value="${teacher.id!}" <#if teasel>selected="selected"</#if>>${teacher.teacherName!}</option>
										</#list>
									<#else>
										<option value="">暂无数据</option>
									</#if>
								</select>
			            		</td>
			            		</tr>
			            	</#list>
			            </#if>
			    	
				</tbody>
			</table>
		</div>
		<div class="col-xs-12">
			<div class="box-header">
				<h4 class="box-title">教室安排</h4>
			</div>
			<table class="table table-striped table-bordered table-hover no-margin">
			    <thead>
			        <tr>
			            <th style="width:20%;">类别</th>
			            <th style="width:20%;">至少需要数量</th>
			            <th>教室</th>
			        </tr>
			    </thead>
			    <tbody>
			    	<tr>
			    		<td>教室</td>
			    		<td>
			    		 <input type="hidden" name="planId" value="${planId!}">
			    		${placeMax}
			    		</td>
			    		<td>
			    			<select  multiple vtype="selectMore" <#if (plan.step>0)>disabled="disabled" </#if> name="placeIds" id="placeIdIds" class="placeIdsClass" data-placeholder="选择教室" >
								<#if placeMap?exists && (placeMap?size>0)>
									<option value=""></option>
									<#list placeMap?keys as key>
										<#assign teasel=false />
										<#if placeIdSet?exists>
										<#list placeIdSet as teaid>
											<#if teaid==key>
								                <#assign teasel=true />
								                <#break>
								            </#if>
										</#list>
										</#if>
										<option value="${key!}" <#if teasel>selected="selected"</#if>>${placeMap[key]?default("")}</option>
									</#list>
								<#else>
									<option value="">暂无数据</option>
								</#if>
							</select>
			    		</td>
			    	</tr>
				</tbody>
			</table>
		</div>
		<div class="col-xs-12">
			<div class="text-right">
			<#if plan.step==0>
				<a  href="javascript:" class="btn btn-blue  plan-save" ><#if isAdd>保存<#else>修改</#if></a>
				<a  href="javascript:" class="btn btn-blue  plan-nextStep-1" >自动分配教师场地</a>
			<#else>
				<a  href="javascript:" class="btn btn-blue  plan-nextStep-2" >下一步</a>
			</#if>
			</div>
		</div>
	</form>
</div>
</div>


<script>
	$(function(){
		//初始化多选控件
		var viewContent1={
			'width' : '100%',//输入框的宽度
			'multi_container_height' : '28px',//输入框的高度
			'results_height':'200px'
		}
		initChosenMore("#planForm","",viewContent1);
		
		<#if plan.step==0>
		$('.plan-save').on('click',function(){
			savePlan();
		});
		
		$('.plan-nextStep-1').on('click',function(){
			autoPlan();
		});
		</#if>
		$('.plan-nextStep-2').on('click',function(){
			nextStep();
		});
		$('.gotoLcIndex').on('click',function(){
			var url =  contextPath+'/gkelective/${arrangeId!}/arrangePlan/index/page';
			$("#showList").load(url);
		});
	});
	<#if plan.step==0>
	function savePlan(){
		save(1);
	}
	
	function autoPlan(){
		save(2);
	}
	var isSubmit=false;
	function save(type){
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
			url : '${request.contextPath}/gkelective/${arrangeId!}/arrangePlan/saveMakePlan?type='+type,
			dataType : 'json',
			success : function(data){
		 		if(data.success){
		 			layer.closeAll();
		 			isSubmit=false;
		 			if(type="1"){
		 				toMakePlan();
		 			}else{
		 				//查看后面的
		 				dealClickType(['.teacher-result-step','.place-result-step','.all-result-step'],true);
		 				toTeacher();
		 			}
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
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#planForm").ajaxSubmit(options);
	}

	</#if>
	function nextStep(){
		toTeacher();
	}
</script>
