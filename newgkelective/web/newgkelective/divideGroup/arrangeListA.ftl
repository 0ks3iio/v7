<#--<a href="javascript:void(0)" onclick="gobackGroupResult()" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>-->
<div class="box box-default">	
	<div class="box-body">
	<table class="table table-bordered table-striped table-hover ">
		<thead>
			<tr>
				<th>选考科目</th>
				<th>总人数</th>
				<th>剩余人数</th>
				<th>手动排班班级</th>
				<#if isCanEdit><th style="width:20%;">操作</th></#if>
			</tr>
		</thead>
		<tbody>
			<#if gDtoList?exists && (gDtoList?size > 0)>
				<#list gDtoList as dto>
				<tr>
					<td <#if dto.notexists==1>class="color-red"</#if>>${dto.conditionName!}</td>
					<td>${dto.allNumber?default(0)}</td>
					<td>${dto.leftNumber?default(0)}</td>
					<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>
						<td>
							<#list dto.gkGroupClassList as groupDto>
								<#if groupDto_index!=0>
									、
								</#if>
								<#if isCanEdit>
									<a href="javascript:" onclick="arrangeStuA('${dto.subjectIds!}','${groupDto.id}')" <#if dto.notexists==1 || groupDto.notexists==1>class="color-red"</#if>>${groupDto.className!}(${groupDto.studentCount?default(0)})</a>
								<#else>
									<#--<a href="javascript:" onclick="showStuA('${dto.subjectIds!}','${groupDto.id}')" <#if dto.notexists==1 || groupDto.notexists==1>class="color-red"</#if>>${groupDto.className!}(${groupDto.studentCount?default(0)})</a>-->
									<a href="javascript:" onclick="showStuA('${dto.subjectIds!}','${groupDto.id}')">${groupDto.className!}(${groupDto.studentCount?default(0)})</a>
								</#if>
							</#list>
						</td>
					<#else>
						<td>无手动排班</td>
					</#if>
					
					<#if isCanEdit>
					<td>
						<a href="javascript:" onclick="arrangeStuA('${dto.subjectIds!}','')">手动排班</a>
					</td>
					</#if>
					
				</tr>
				</#list>
			</#if>
		</tbody>
	</table>
	<#if haserror>
		<#if isCanEdit>
		<em>说明：红色班级：学生数据存在错误，请进行删除教学班操作。</em>
		<#else>
		<em>说明：红色班级：学生数据存在错误，请进行删除教学班操作。</em>
		</#if>
	<div class="text-center">
		<a class="btn btn-white" href="javascript:" onclick="gobackGroupResult();">上一步</a>
	</div>
	<#else>
	<div class="text-center">
			
		<a class="btn btn-white" href="javascript:" onclick="gobackGroupResult();">上一步</a>
		 <a class="btn btn-blue" href="javascript:"  
	 			<#if isCanEdit> onclick="nextToArrangeClassNumA('${divideId!}','1')" <#else> onclick="nextToArrangeClassNumA('${divideId!}','2')" </#if> >下一步</a>
	</div>
	</#if>
			
	</div>
</div>
<script>
	showBreadBack(toBackDivideIndex,false,"分班安排");
	
	function toBackDivideIndex(){
		var url =  '${request.contextPath}/newgkelective/${gradeId!}/goDivide/index/page';
		$("#showList").load(url);
	}

	function gobackGroupResult(){
		var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/resultClassList';
		$("#showList").load(url);
	}
	
	//调整页面
	function arrangeStuA(subjectId,divideClassId){
		var url =  '${request.contextPath}/newgkelective/${divideId!}/divideGroup/jzbGroupDetail/page?subjectId='+subjectId+"&groupClassId="+divideClassId;
		$("#showList").load(url);
	}
	
	function showStuA(subjectIds,divideClassId){
		var url = "${request.contextPath}/newgkelective/${divideId!}/divideGroup/showStu/page?subjectIds="+subjectIds+"&divideClassId="+divideClassId+"&type=2";
		$("#showList").load(url);
	}
	var isCheck=false;
	function nextToArrangeClassNumA(divideId,type){
		if("2"==type){
			$("#showList").load("${request.contextPath}/newgkelective/BathDivide/"+divideId+"/singleList3/page");
			return;
		}
		if(isCheck){
			return ;
		}
		isCheck=true;
		//验证
		var url =  '${request.contextPath}/newgkelective/BathDivide/'+divideId+'/openClassArrange/checkAllInGroupA';
		$.ajax({
			url:url,
			dataType: "JSON",
			success: function(data){
				if(data.success){
					if("allArrange"==data.msg){
						gobackResultList(divideId);
					}else{
						$("#showList").load("${request.contextPath}/newgkelective/BathDivide/"+divideId+"/singleList3/page");
					}
				}else{
					isCheck=false;
					layerTipMsg(data.success,"失败",data.msg);
				}
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
	}
	
	function removeNot(){
		
	}
</script>