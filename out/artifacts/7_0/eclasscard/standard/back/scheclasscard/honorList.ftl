<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div id="cc" class="tab-pane" role="tabpanel">
	<div class="filter">
		<div class="filter-item"><button class="btn btn-blue" onclick="honorEdit('1','')">颁布班级荣誉</button></div>
	</div>
	<div class="table-container">
		<div class="table-container-body">
			<table class="table table-bordered table-striped">
				<thead>
					<tr>
						<th width="5%">序号</th>
						<th width="10%">荣誉名称</th>
						<th width="10%">荣誉样式</th>
						<th width="25%">获奖班级</th>
						<th width="10%">获奖日期</th>
						<th width="20%">班牌展示时间</th>
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
						<#if honor.style == 1>
						src="${request.contextPath}/static/eclasscard/standard/show/images/honor-flag.png" 
						<#elseif honor.style == 2>
						src="${request.contextPath}/static/eclasscard/standard/show/images/honor-cup.png"
						<#else>
						src="${request.contextPath}/static/eclasscard/standard/show/images/honor-medal.png"
						</#if>
						alt=""></td>
						<td>${honor.className!}</td>
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
								<a href="javascript:void(0);" onclick="honorEdit('1','${honor.id!}')">编辑</a>
							</#if>
						</#if>
							<a href="javascript:void(0);" onclick="honorEdit('0','${honor.id!}')">查看</a>
						<#if honor.canEdit>
							<a href="javascript:void(0);" onclick="honorDelete('${honor.id!}')">删除</a>
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
		<@htmlcom.pageToolBar container="#tabContent" class="noprint"/>
		</#if>
	</div>
</div>
<script>
	$(function(){
		$('.table-container').each(function(){
			$(this).css({
				height: $(window).height() - $(this).offset().top - 60,
				overflowY: 'auto'
			})
		});
	});
	function honorEdit(canEdit,honorId) {
		url =  '${request.contextPath}/eclasscard/standard/honor/classedit?honorId='+honorId+'&canEdit='+canEdit;
		$("#schEclasscardDiv").load(url);
	}
	
	function honorDelete(honorId) {
		layer.confirm('确定要删除吗？', function(index){
			$.ajax({
				url:'${request.contextPath}/eclasscard/standard/honor/delete',
				data: {'honorId':honorId},
				type:'post',
				success:function(data) {
					var jsonO = JSON.parse(data);
		 			if(jsonO.success){
		 				showContent('2');
		 				layer.msg("删除成功");
		 			}else{
		 				layerTipMsg(jsonO.success,"失败",jsonO.msg);
					}
				},
	 			error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
		});
	}
</script>