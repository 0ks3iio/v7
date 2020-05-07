				<div class="box box-white">
					<div class="box-header">
						<h3 class="box-title">请假管理</h3>
					</div>
					<div class="box-body">
						<ul class="nav-tab" role="tablist" data-action="tab">
							<li role="presentation"><a href="javascript:;" role="tab" onclick="queryMsgShow(4,1)">请假申请</a></li>
							<li class="active" role="presentation"><a href="javascript:;" role="tab">我的请假</a></li>
						</ul>
						<div class="tab-content">
						<div class="tab-pane active" role="tabpanel">
								<#if leaveDtos?exists && leaveDtos?size gt 0>
						<#list leaveDtos as dto>
							<#if dto.type == '1'>
							<div class="leave-paper">
							<p>申请时间：${dto.applyTime!}</p>
							<#if dto.state == '2'>
							<#--	<span class="icon leave-passed">审核中</span>-->
							<#elseif dto.state == '3'>
							<span class="icon leave-passed">审核通过</span>
							<#elseif dto.state == '4'>
							<#--	<span class="icon leave-passed">审核不通过</span>-->
							</#if>
							<table class="table table-leave">
								<thead>
									<tr>
										<th colspan="4">杭州外国语学校（请假单）</th>
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
										<td width="33.33%">${dto.leaveTime}&emsp;共${dto.days?string('0.#')}天</td>
									</tr>
									<tr>
										<th class="cell-big" width="16.67%">事由：</th>
										<td width="83.33%" colspan="3">${dto.remark!}</td>
									</tr>
									<#if dto.taskName?exists && dto.taskName?size gt 0>
									<#list dto.taskName as task>
									<tr>
										<th class="cell-big" width="16.67%">审核信息<#if dto.taskName?size gt 1>${task_index + 1}</#if>：</th>
										<td width="83.33%" colspan="3">${task!}</td>
									</tr>
									</#list>
									</#if>
									<#if dto.isUndo?exists && dto.isUndo=="1">
									<tr>
										<th  width="16.67%">是否销假：</th>
										<td width="83.33%" colspan="3">是(销假时间:${dto.undoTime})</td>
									</tr>
									</#if>
								</tbody>
							</table>
						</div>
							<#elseif dto.type=='2'>
							<div class="leave-paper">
							<p>申请时间：${dto.applyTime!}</p>
							<#if dto.state == '2'>
						<#--	<span class="icon leave-passed">审核中</span>-->
							<#elseif dto.state == '3'>
							<span class="icon leave-passed">审核通过</span>
							<#elseif dto.state == '4'>
							<#--<span class="icon leave-passed">审核不通过</span>-->
							</#if>
							<table class="table table-leave">
								<thead>
									<tr>
										<th colspan="4">杭州外国语学校（临时出校申请单）</th>
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
										<th width="16.67%">联系电话：</th>
										<td width="33.33%">${dto.linkPhone!}</td>
										<th width="16.67%">起止时间：</th>
										<td width="33.33%">${dto.leaveTime!}</td>
									</tr>
									<tr>
										<th class="cell-big" width="16.67%">事由：</th>
										<td width="83.33%" colspan="3">${dto.remark!}</td>
									</tr>
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
							<#elseif dto.type == '3'>
							<div class="leave-paper">
							<p>申请时间：${dto.applyTime!}</p>
							<#if dto.state == '2'>
							<#elseif dto.state == '3'>
							<span class="icon leave-passed">审核通过</span>
							<#elseif dto.state == '4'>
							</#if>
							<table class="table table-leave">
								<thead>
									<tr>
										<th colspan="4">杭州外国语学校（暂时通校、住校申请单）</th>
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
										<th width="16.67%">通住校选择：</th>
										<td width="33.33%"><#if dto.applyType == '1'>住校<#else>通校</#if></td>
									</tr>
									<tr>
										<th width="16.67%">起止时间：</th>
										<td width="83.33%" colspan="3">${dto.leaveTime!}&emsp;共${dto.days?string('0.#')}天</td>
									</tr>
									<tr>
										<th class="cell-big" width="16.67%">事由：</th>
										<td width="83.33%" colspan="3">${dto.remark!}</td>
									</tr>
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
							<#elseif dto.type == '4'>
							<div class="leave-paper">
							<p>申请时间：${dto.applyTime!}</p>
							<#if dto.state == '2'>
							<#elseif dto.state == '3'>
							<span class="icon leave-passed">审核通过</span>
							<#elseif dto.state == '4'>
							</#if>
							<table class="table table-leave">
								<thead>
									<tr>
										<th colspan="4">杭州外国语学校（通校申请单）</th>
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
									<#if dto.taskName?exists && dto.taskName?size gt 0>
									<#list dto.taskName as task>
										<tr>
											<th class="cell-big" width="16.67%">审核信息<#if dto.taskName?size gt 1>${task_index + 1}</#if>：</th>
											<td width="83.33%" colspan="3">${task!}</td>
										</tr>
									</#list>
									</#if>
									<#if dto.isUndo?exists && dto.isUndo=="1">
									<tr>
										<th  width="16.67%">是否销假：</th>
										<td width="83.33%" colspan="3">是(销假时间:${dto.undoTime})</td>
									</tr>
									</#if>
								</tbody>
							</table>
						</div>
							</#if>
						</#list>
					</#if>
							</div>
						</div>
					</div>
				</div>
