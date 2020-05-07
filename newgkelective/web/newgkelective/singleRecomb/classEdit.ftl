<div class="layer-content">
	<p><span class="mr30">选科组合：${groupName!}</span></p>
	<form id="editForm">
	<div style="height:350px;overflow-y:auto;">
		<table class="table table-striped table-bordered table-hover no-margin">
			<thead>
				<tr>
					<th width="120">班级名称</th>
					<th width="80">人数</th>
					<th>组合-人数</th>
					<th width="100">操作</th>
				</tr>
			</thead>
			<tbody>
				<#if dtoList?exists && dtoList?size gt 0>
				<#list dtoList as item>
				<tr>
					<td><input type="hidden" class="classId" name="saveDto[${item_index}].classId" value="${item.classId!}">
					<input type="text" class="form-control" nullable="false"  name="saveDto[${item_index}].className" id="className_${item_index}" value="${item.className!}" maxlength="20"> 
					</td>
					<td class="stuNum">${item.stuNum}</td>
					<td>
					<#if item.stuNum gt 0>
						<#list item.subjectIds as sub>
							<span class="member inline-block groups" data-num="${item.subStuNum[sub_index]!}">
                             	<input type="hidden" class="subjectIds" name="saveDto[${item_index}].subjectIds[${sub_index}]" value="${sub!}"> ${item.subGroupName[sub_index]!}-${item.subStuNum[sub_index]!}
	                            <a href="javascript:void(0)" onclick="removeClassGroup(this)">
	                                <i class="fa fa-times-circle"></i>
	                            </a>
	                        </span>
						</#list>
					</#if>
					</td>
					<td>
						<a class="table-btn color-blue" href="javascript:void(0)" onclick="removeClass(this)">删除</a>
					</td>
				</tr>
				</#list>
				<#else>
					<tr colspan="3" class="text-center">暂无开设班级</tr>
				</#if>
			</tbody>
		</table>
		</div>
	</form>
</div>
<div class="layer-footer" style="vertical-align: middle;border-top: 1px solid #eee;">
	<#if dtoList?exists && dtoList?size gt 0>
	<button class="btn btn-lightblue" id="editClass-commit">确定</button>
	</#if>
	<button class="btn btn-grey" id="editClass-close">取消</button>
</div>
<script>
	var isSubmit = false;
	var delClassId = "";
	$(function() {
		$("#editClass-close").on("click", function () {
			layer.closeAll()
		});
		<#if dtoList?exists && dtoList?size gt 0>
		$("#editClass-commit").off('click').on("click", function () {
			if (isSubmit) {
				return false;
			}
			isSubmit = true;
			$(this).addClass("disabled");
			var check = checkValue('#editForm');
			if (!check) {
				$(this).removeClass("disabled");
				isSubmit = false;
				return;
			}
			//页面判断不能重复

			// 提交数据
			var options = {
				url: '${request.contextPath}/newgkelective/${divideId!}/divideClass/singleRecomb/saveClassOrDel',
				data: {"delClassId": delClassId},
				dataType: 'json',
				success: function (data) {
					if (data.success) {
						layer.closeAll();
						layer.msg("保存成功！", {
							offset: 't',
							time: 2000
						});
						refreshThis();
					} else {
						layerTipMsg(data.success, "失败", data.msg);
						$("#editClass-commit").removeClass("disabled");
						isSubmit = false;
					}
				},
				clearForm: false,
				resetForm: false,
				type: 'post',
				error: function (XMLHttpRequest, textStatus, errorThrown) {
				}
			};
			$("#editForm").ajaxSubmit(options);
		});
		</#if>
	});
	<#if dtoList?exists && dtoList?size gt 0>
	// 外围删除 还是整行删除
	function removeClass(obj) {
		var classId = $(obj).parents("tr").find(".classId").val();
		if (delClassId == "") {
			delClassId = classId;
		} else {
			delClassId = delClassId + "," + classId;
		}
		$(obj).parents("tr").remove();
	}

	function removeClassGroup(obj) {
		var $tr = $(obj).parents("tr");
		var stuNum = $.trim($($tr).find(".stuNum").html());
		var stuNumInt = parseInt(stuNum);
		var num1 = $.trim($(obj).parent().attr("data-num"));
		$(obj).parent().remove();
		var s = stuNumInt - num1;
		var len = $($tr).find(".groups").length;
		if (len == 0) {
			// 全部为空的时候 删除行
			var classId = $($tr).find(".classId").val();
			if (delClassId == "") {
				delClassId = classId;
			} else {
				delClassId = delClassId + "," + classId;
			}
			$($tr).remove();
		} else {
			$($tr).find(".stuNum").html(s);
		}
	}
	</#if>
</script>
