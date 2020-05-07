<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<div class="box-body">
<@htmlcomponent.printToolBar container=".print" btnClass="btn btn-blue printBtnClass"/>
<#if rounds.openClassType?default('') == '1' && rounds.openTwo?default('')=='1'>
<a class="btn btn-blue pull-right" href="javascript:" onclick="oneAutoOpenClass()">一键智能分班</a>
<input type="hidden" id="copyRoundsId" value="">
</#if>
<div class="row print">
	<div class="col-sm-6">
	<h3>三科组合情况</h3>
	<table class="table table-bordered table-striped table-hover ">
		<thead>
			<tr>
				<th>组合</th>
				<th>总人数</th>
				<th>剩余人数</th>
				<th>手动排班班级</th>
				<th class="noprint">操作</th>
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
									<a href="javascript:" onclick="scheduling('${dto.subjectIds!}','${groupDto.id}')" <#if dto.notexists==1 || groupDto.notexists==1>class="color-red"</#if>>${groupDto.groupName!}(${groupDto.number?default(0)})</a>
								<#else>
									<a href="javascript:" onclick="showStu('${dto.subjectIds!}','${groupDto.id}')" <#if dto.notexists==1 || groupDto.notexists==1>class="color-red"</#if>>${groupDto.groupName!}(${groupDto.number?default(0)})</a>
								</#if>
							</#list>
						</td>
					<#else>
						<td>无手动排班</td>
					</#if>
					<td class="noprint">
					<#if isCanEdit>
						<#if dto.notexists==1>
							<a href="javascript:" onclick="moveGroup('${dto.subjectIds!}','','${dto.conditionName!}')">解散组合</a>
						<#else>
							<a href="javascript:" onclick="scheduling('${dto.subjectIds!}','')">手动排班</a>
							<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>
								<a href="javascript:" onclick="moveGroup('${dto.subjectIds!}','','${dto.conditionName!}')">解散组合</a>
							</#if>
						</#if>
					<#else>
						<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>
							<a href="javascript:" onclick="showStu('${dto.subjectIds!}','')">查看</a>
						</#if>
					</#if>
					</td>
				</tr>
				</#list>
			</#if>
		</tbody>
	</table>
	<#if !(rounds.openClassType?default('') == '1' && rounds.openTwo?default('')=='1')>
	<h3>混合组合情况</h3>
	<table class="table table-bordered table-striped table-hover ">
		<thead>
			<tr>
				<th>组合</th>
				<th>总人数</th>
				<th>剩余人数</th>
				<th>手动排班班级</th>
				<th class="noprint">操作</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>${gDto.conditionName!}</td>
				<td>${gDto.allNumber?default(0)}</td>
				<td>${gDto.leftNumber?default(0)}</td>
				<#if (gDto.gkGroupClassList?exists) && (gDto.gkGroupClassList?size > 0)>
					<td>
						<#list gDto.gkGroupClassList as groupDto>
							<#if groupDto_index!=0>
								、
							</#if>
							<#if isCanEdit>
								<a href="javascript:" onclick="scheduling('${gDto.subjectIds!}','${groupDto.id}')" >${groupDto.groupName!}(${groupDto.number?default(0)})</a>
							<#else>
								<a href="javascript:" onclick="showStu('${gDto.subjectIds!}','${groupDto.id}')" >${groupDto.groupName!}(${groupDto.number?default(0)})</a>
							</#if>
						</#list>
					</td>
				<#else>
					<td>无手动排班</td>
				</#if>
				<td class="noprint">
				<#if isCanEdit>
					<a href="javascript:" onclick="scheduling('${gDto.subjectIds!}','')">手动排班</a>
					<#if (gDto.gkGroupClassList?exists) && (gDto.gkGroupClassList?size > 0)>
						<a href="javascript:" onclick="moveGroup('${gDto.subjectIds!}','','${gDto.conditionName!}')">解散组合</a>
					</#if>
				<#else>
					<#if (gDto.gkGroupClassList?exists) && (gDto.gkGroupClassList?size > 0)>
						<a href="javascript:" onclick="showStu('${gDto.subjectIds!}','')">查看</a>
					</#if>
				</#if>
				</td>
			</tr>
		</tbody>
	</table>
	</#if>
	</div>
	<div class="col-sm-6">
	<h3>两科组合情况</h3>
	<table class="table table-bordered table-striped table-hover ">
		<thead>
			<tr>
				<th>组合</th>
				<th>总人数</th>
				<th>剩余人数</th>
				<th>手动排班班级</th>
				<th class="noprint">操作</th>
			</tr>
		</thead>
		<tbody>
			<#if gDtoList2?exists && (gDtoList2?size > 0)>
				<#list gDtoList2 as dto>
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
									<a href="javascript:" onclick="scheduling('${dto.subjectIds!}','${groupDto.id}')" <#if dto.notexists==1 || groupDto.notexists==1>class="color-red"</#if>>${groupDto.groupName!}(${groupDto.number?default(0)})</a>
								<#else>
									<a href="javascript:" onclick="showStu('${dto.subjectIds!}','${groupDto.id}')" <#if dto.notexists==1 || groupDto.notexists==1>class="color-red"</#if>>${groupDto.groupName!}(${groupDto.number?default(0)})</a>
								</#if>
							</#list>
						</td>
					<#else>
						<td>无手动排班</td>
					</#if>
					<td class="noprint">
					<#if isCanEdit>
						<#if dto.notexists==1>
							<a href="javascript:" onclick="moveGroup('${dto.subjectIds!}','','${dto.conditionName!}')">解散组合</a>
						<#else>
							<a href="javascript:" onclick="scheduling('${dto.subjectIds!}','')">手动排班</a>
							<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>
								<a href="javascript:" onclick="moveGroup('${dto.subjectIds!}','','${dto.conditionName!}')">解散组合</a>
							</#if>
						</#if>
					<#else>
						<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>
							<a href="javascript:" onclick="showStu('${dto.subjectIds!}','')">查看</a>
						</#if>
					</#if>
					</td>
				</tr>
				</#list>
			</#if>
		</tbody>
	</table>
	</div>
	<div class="col-xs-12 noprint">
		<em>说明：红色组合：该组合已经不存在，请进行解散操作。红色班级：该班级下存在学生选择组合与该班级科目组合不同。</em>
		<div class="text-right">
		<#if isCanEdit && haserror>
			<a class="btn btn-blue" href="javascript:" onclick="removeNot()">异常清除</a>
		</#if>
		<#if isCanRestart && !isCanEdit && !isArrange>
			<a class="btn btn-blue" href="javascript:" onclick="removeAll()" data-toggle="tooltip" data-placement="top" title="" data-original-title="若被方案引用，则无法重新安排">重新安排</a>
		</#if>
		<#if !(isCanEdit && haserror)>
			<a class="btn btn-blue" href="javascript:" onclick="openGroup()">下一步</a>
		</#if>
		</div>
	</div>
</div>
</div>
<script>
	contextPath = '${request.contextPath}';
	roundsId = '${roundsId!}';
	$(function(){
		$('[data-toggle="tooltip"]').tooltip({
			container: 'body',
			trigger: 'hover'
		});
	});
	function scheduling(subjectIds,groupClassId){
		var url =  contextPath+'/gkelective/'+roundsId+'/openClassArrange/perArrange/scheduling/page?subjectIds='+subjectIds+"&groupClassId="+groupClassId;
		$("#groupList").load(url);
	}
	
	function showStu(subjectIds,groupClassId){
		var url =  contextPath+'/gkelective/'+roundsId+'/openClassArrange/perArrange/showStu/page?subjectIds='+subjectIds+"&groupClassId="+groupClassId;
		$("#groupList").load(url);
	}
	
	function removeNot(){
		$.ajax({
			url:'${request.contextPath}/gkelective/'+roundsId+'/openClassArrange/clearNotPerArrange',
			dataType : 'json',
			type:'post',
			success:function(data) {
				var jsonO = data;
		 		if(jsonO.success){
		 			layer.closeAll();
				  	toPerArrange();
		 		}
		 		else{
		 			layer.closeAll();
		 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
	
	function openGroup(){
		toGroupResult();
	}
	
	function moveGroup(subjectIds,groupId,conditionName){
		var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
			showConfirm("是否确定解散<span style='color:red'>"+conditionName+"</span>组合下所有班级",options,function(){
			$.ajax({
				url:'${request.contextPath}/gkelective/'+roundsId+'/openClassArrange/moveGroup',
				data:{"subjectIds":subjectIds,"groupId":groupId},
				dataType : 'json',
				type:'post',
				success:function(data) {
					var jsonO = data;
			 		if(jsonO.success){
			 			layer.closeAll();
					  	toPerArrange();
			 		}
			 		else{
			 			layer.closeAll();
			 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
					}
				},
		 		error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
				
			},function(){});
	}
	<#if isCanRestart>
	function removeAll(){
		var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
		showConfirm("重新安排将清除全部开班数据（但不包括手动排班结果数据），确定要重新安排开班吗？",options,function(){
			var ii = layer.load();
			$.ajax({
				url:'${request.contextPath}/gkelective/${roundsId!}/openClassArrange/moveAllArrange',
				data:{},
				dataType : 'json',
				type:'post',
				success:function(data) {
					var jsonO = data;
			 		if(jsonO.success){
			 			layer.closeAll();
						layerTipMsg(jsonO.success,"成功",jsonO.msg);
					  	dealClickType(['.group-result-step','.un-arrange-step','.single-step','.all-result-step'],false);
					  	toPerArrange();
			 		}
			 		else{
			 			layer.closeAll();
			 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
					}
					layer.close(ii);
				},
		 		error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
		},function(){});
		
	}
	</#if>
	function oneAutoOpenClass(){
		$.ajax({
			url:'${request.contextPath}/gkelective/'+roundsId+'/openClassArrange/oneAutoOpenClass',
			data:{'copyRoundsId':$("#copyRoundsId").val()},
			dataType : 'json',
			type:'post',
			success:function(data) {
				var jsonO = data;
		 		if(jsonO.success){
		 			layer.closeAll();
		 			layerTipMsg(jsonO.success,"成功",jsonO.msg);
				  	toPerArrange();
		 		}
		 		else{
		 			layer.closeAll();
		 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
</script>
