<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="explain">
	<p>说明：该荣誉为班级荣誉，由校级管理员颁布，此页面显示的由学校颁布给班级的荣誉。</p>
</div>
<div class="table-container" id="classHonorListTable">
	<div class="table-container-body">
		<table class="table table-bordered table-striped">
			<thead>
				<tr>
					<th width="5%">序号</th>
					<th width="20%">荣誉名称</th>
					<th width="10%">荣誉样式</th>
					<th width="20%">获奖日期</th>
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
					<#if honor.style == 1>
					src="${request.contextPath}/static/eclasscard/standard/show/images/honor-flag.png" 
					<#elseif honor.style == 2>
					src="${request.contextPath}/static/eclasscard/standard/show/images/honor-cup.png"
					<#else>
					src="${request.contextPath}/static/eclasscard/standard/show/images/honor-medal.png"
					</#if>
					alt=""></td>
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
						<a href="javascript:void(0);" onclick="honorShow('${honor.id!}')">查看</a>
					</td>
					</td>
				</tr>
				</#list>
			<#else>
			<tr>
				<td colspan="7" align="center">暂无数据</td>
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
		$("#classHonorListTable").css({
			height: $(window).height() - $("#classHonorListTable").offset().top - 60,
			overflowY: 'auto'
		})
	});
	function honorShow(honorId) {
		url =  '${request.contextPath}/eclasscard/standard/honor/show?honorId='+honorId;
		$("#myEclasscardDiv").load(url);
	}
	
</script>