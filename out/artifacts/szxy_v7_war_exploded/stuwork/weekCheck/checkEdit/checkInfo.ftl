<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title">${className!}
		<input type = "hidden" value="${classId!}" name="classId" id="classId"/>
		</h3>
	</div>
	<div class="box-body">
		<div class="table-container">
			<div class="table-container-header text-right">
				<button id="saveFrom" type="button" class="btn btn-blue" onclick="save()">保存</button>
			</div>
			<div class="table-container-body">
				<table class="table table-bordered layout-fixed">
					<thead>
						<tr>
							<th width="15%" class="text-center">考核项</th>
							<th width="15%" class="text-center">类别</th>
							<th width="15%">值周人员</th>
							<th width="15%">扣分情况</th>
							<th width="40%">备注</th>
						</tr>
					</thead>
					<tbody>
					<#assign editIndex = 0>
					<#if resultDtos?exists && resultDtos?size gt 0>
					<#list resultDtos as dto>
					<tr>
						<td class="text-center" rowspan="${dto.result?size}">
							${dto.item.itemName!}(<#if dto.item.hasTotalScore == 1>${dto.item.totalScore}<#else>无总分</#if>)
						</td>
						<td class="text-center" rowspan="${dto.result?size}">
							<#if dto.item.type == 1>
							卫生
							<#elseif dto.item.type == 2>
							纪律
							</#if>
						</td>
						<#list dto.result as re>
						<#if re_index gt 0>
						<tr>
						</#if>
						<td for="result[${editIndex}].score">${re.roleName!}</td>
							<#if roleType == '01' || re.roleType == roleType>
							<td>
								<input type = "hidden" name="result[${editIndex}].id" value="${re.id!}" />
								<input type = "hidden" name="result[${editIndex}].schoolId" value="${re.schoolId!}" />
								<input type = "hidden" name="result[${editIndex}].acadyear" value="${acadyear!}" />
								<input type = "hidden" name="result[${editIndex}].semester" value="${semester!}" />
								<input type = "hidden" name="result[${editIndex}].roleType" value="${re.roleType!}" />
								<input type = "hidden" name="result[${editIndex}].week" value="${week!}" />
								<input type = "hidden" name="result[${editIndex}].day" value="${day!}" />
								<input type = "hidden" name="result[${editIndex}].totalScore" value="${dto.item.totalScore?string('0.#')}" />
								<input type = "hidden" name="result[${editIndex}].type" value="${dto.item.type!}" />
								<input type = "hidden" name="result[${editIndex}].itemId" value="${dto.item.id!}" />
								<input type = "hidden" name="result[${editIndex}].itemName" value="${dto.item.itemName!}" />
								<input type = "hidden" name="result[${editIndex}].classId" value="${classId!}" />
								<input type = "hidden" name="result[${editIndex}].checkDate" value="${dutyDate!}">
								<input type="text" class="form-control" nullable="false" decimalLength="1" vtype="number" max="9999" min="0" id="score_${editIndex}" name="result[${editIndex}].score" value="${re.score?string('0.#')}">
							</td>
							<td>
								<input type="text" class="form-control" name="result[${editIndex}].remark" id="remark_${editIndex}" value="${re.remark!}" maxLength="100"/>
								<#assign editIndex = editIndex + 1>
							</td>
							<#else>
							<td>
								${re.score?string('0.#')}
							</td>
							<td>
								${re.remark!}
							</td>
							</#if>
							</tr>
						</#list>
					</#list>
					</#if>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>