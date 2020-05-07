<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
	<div class="filter">
		<div class="filter-item"><button class="btn btn-blue" onclick="editStuHonor('','')">颁布学生荣誉</button></div>
	</div>
	<div class="table-container" id="stuHonorListTable">
		<div class="table-container-body">
			<table class="table table-bordered table-striped">
				<thead>
					<tr>
						<th width="5%">序号</th>
						<th width="12%">荣誉名称</th>
						<th width="10%">荣誉照片</th>
						<th width="12%">获奖学生</th>
						<th width="16%">获奖日期</th>
						<th width="25%">班牌展示时间</th>
						<th width="10%">展示状态</th>
						<th width="10%">操作</th>
					</tr>
				</thead>
				<tbody>
				<#if eccHonors?exists&&eccHonors?size gt 0>
					<#list eccHonors as honor>
					<tr>
						<td>${honor_index+1}</td>
						<td>${honor.title!}</td>
						<td><img width="100" height="75" 
						src="${request.contextPath}${honor.pictureUrl!}" 
						alt=""></td>
						<td>${honor.studentName!}</td>
						<td>${(honor.awardTime?string("yyyy-MM-dd"))?if_exists}</td>
						<td>${honor.beginTime!}  -  ${honor.endTime!}</td>
						<td>
						<#if honor.status==1>
						<i class="fa fa-circle color-orange"></i> 未开始
						<#elseif honor.status==2>
						<i class="fa fa-circle color-green"></i> 展示中
						<#else>
						<i class="fa fa-circle color-grey"></i> 已结束
						</#if>
						</td>
						<td>
						<#if honor.canEdit>
							<#if honor.status == 1>
							<a class="" href="javascript:void('0')" onClick="editStuHonor('${honor.id}','${honor.attachmentId!}')">编辑</a>
							</#if>
							<a href="javascript:void(0);" onclick="honorShow('${honor.id!}')">查看</a>
							<a class="js-delPhotos" href="javascript:void('0')" onClick="deleteStuHonor('${honor.id}','${honor.attachmentId!}')">删除</a>
						<#else>
							<a href="javascript:void(0);" onclick="honorShow('${honor.id!}')">查看</a>
						</#if>
						</td>
						</td>
					</tr>
					</#list>
				<#else>
				<tr>
					<td colspan="8" align="center">暂无数据</td>
				</tr>
				</#if>
				</tbody>
			</table>
		</div>
		<#if eccHonors?exists&&eccHonors?size gt 0>
		<@htmlcom.pageToolBar container="#listHonorDiv" class="noprint"/>
		</#if>
	</div>

<script>
	$(function(){
		$("#stuHonorListTable").css({
			height: $(window).height() - $("#stuHonorListTable").offset().top - 60,
			overflowY: 'auto'
		})
	});
	function deleteStuHonor(honorId,attachmentId) {
		layer.confirm('确定要删除吗？', function(index){
			$.ajax({
				url:'${request.contextPath}/eclasscard/standard/honor/delete',
				data: {'honorId':honorId,'attachmentId':attachmentId},
				type:'post',
				success:function(data) {
					layer.close(index);
					var jsonO = JSON.parse(data);
	 				if(jsonO.success){
	 					setTimeout(function(){
    						showHonorDiv('1');
    					},500);
    					layer.msg("删除成功");
	 				}else{
	 					layerTipMsg(jsonO.success,"失败",jsonO.msg);
					}
				},
 				error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
		})
	}
	
	function editStuHonor(honorId,attachmentId) {
		url =  '${request.contextPath}/eclasscard/standard/honor/edit?eccInfoId=${eccInfoId!}&honorId='+honorId+'&attachmentId='+attachmentId;
		$("#myEclasscardDiv").load(url);
	}
	
	function honorShow(honorId) {
		url =  '${request.contextPath}/eclasscard/standard/honor/show?honorId='+honorId;
		$("#myEclasscardDiv").load(url);
	}
</script>