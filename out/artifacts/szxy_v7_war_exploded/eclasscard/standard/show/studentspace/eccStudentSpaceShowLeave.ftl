<ul class="leave-paper-type clearfix">
	<li <#if leaveType=='undefined' ||  leaveType == '1'>class="active"</#if>><a href="javascript:void(0);" onclick="selectLeaveType(1)">请假单</a></li>
	<li <#if leaveType == '4'>class="active"</#if>><a href="javascript:void(0);" onclick="selectLeaveType(4)">长期通校申请单</a></li>
</ul>

<#if leaveDtos?exists && leaveDtos?size gt 0>
<#list leaveDtos as dto>
<div class="leave-paper">
	<input type="hidden" id="type" value="${dto.type!}"/>
	<p>申请时间：${dto.applyTime!}</p>
	<#if dto.state == '3'>
	<span class="icon icon-passed">审核通过</span>
	</#if>
	<table class="table table-leave">
		<thead>
			<tr>
				<th class="text-center" colspan="4">${schoolName!}（<#if dto.type == '1'>请假单<#else>长期通校申请单</#if>）</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<th width="16.67%">学生姓名：</th>
				<td width="33.33%">${studentName!}</td>
				<th width="16.67%">班级：</th>
				<td width="33.33%">${className!}</td>
			</tr>
			<tr>
				<th width="16.67%">申请人：</th>
				<td width="33.33%">${studentName!}</td>
				<th width="16.67%">起止时间：</th>
				<td width="33.33%">${dto.leaveTime!}&emsp;共${dto.days?string('0.#')}天</td>
			</tr>
			<#if dto.type == '1'>
			<tr>
				<th class="cell-big" width="16.67%">事由：</th>
				<td width="83.33%" colspan="3">${dto.remark!}</td>
			</tr>
			<#else>
			<tr>
				<th width="16.67%">床位是否保留：</th>
				<td width="33.33%"><#if dto.hasBed?default('0') == '0'>否<#else>是</#if></td>
				<th width="16.67%">通校居住地：</th>
				<td width="33.33%">${dto.address!}</td>
			</tr>
			<tr>
				<th width="16.67%">陪住人姓名：</th>
				<td width="33.33%">${dto.mateName!}</td>
				<th width="16.67%">与本人关系：</th>
				<td width="33.33%">${dto.mateGx!}</td>
			</tr>
			</#if>
			<#if dto.taskName?exists && dto.taskName?size gt 0>
			<#list dto.taskName as task>
				<tr>
					<th class="cell-big" width="16.67%">审核信息<#if dto.taskName?size gt 1>${task_index + 1}</#if>：</th>
					<td width="83.33%" colspan="3">${task!}</td>
				</tr>
			</#list>
			</#if>
		</tbody>
	</table>
</div>
</#list>
<#else>
	<div class="nothing">
		<img src="${request.contextPath}/static/eclasscard/standard/show/images/no-content.png" alt="">
	</div>
</#if>
<script>								
	$(document).ready(function(){
		$('.leave-paper-type a').on('click', function(e){
			e.preventDefault();
			$(this).parent().addClass('active').siblings().removeClass('active');
		})
		
		$('.nothing').height($('.scroll-container').height() - 100 );
		
		<#if leaveType=='undefined'>
			selectLeaveType(1);
		<#else>
			selectLeaveType("${leaveType!}");
		</#if>
	});
	
	function selectLeaveType(selType) {
		$(".leave-paper").each(function(){
			var type = $(this).find("input").val();
			if (type == selType) {
				$(this).removeAttr("style");
			} else {
				$(this).attr("style","display:none");
			}
		})
	}
</script>