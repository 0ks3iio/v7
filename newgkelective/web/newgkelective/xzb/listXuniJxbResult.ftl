<#-- 行政班 排课 虚拟课程 教学班结果展示 -->
<table class="table table-bordered layout-fixed table-editable" data-label="不可排课">
    <thead>
		<tr>
			<th rowspan="2" width="10%">科目</th>
			<th rowspan="2" width="10%">总人数</th>
			<th rowspan="2" width="10%">总班级数</th>
            <th>跟随行政班上课</th>
			<#list virList as virId>
			<th>${coureNameMap[virId]!}</th>
			</#list>
		</tr>
	</thead>
    <tbody>
	<#if dtoList?exists && dtoList?size gt 0>
	<#list dtoList as dto>
        <tr>
            <td >${dto.courseName!}</td>
            <td >${(dto.aStuNum + dto.bStuNum)!}</td>
            <td >${dto.totalNum!}</td>
            <td class="classroomArrangement">
                <#if dto.asXzbClassList?exists && dto.asXzbClassList?size gt 0>
                    <#list dto.asXzbClassList as clazz>
                        <a class="btn btn-sm btn-white my2 item" href="javascript:void(0)" <#if clazz.studentList?size gt 0>onclick="showDetail('${clazz.id!}','${type}')"</#if>>
                            <#if clazz.studentList?size == 0>
                                <span class="del" onclick="deleteBlankTeachClass('${clazz.id!}', this)" style="display: none">×</span>
                            </#if>
                            <span class="color-blue">${clazz.className!}</span>
                            <span class="color-999 font-12">（${clazz.studentList?size}人）</span>
                        </a>
                    </#list>
                <#else>
                    /
                </#if>
            </td>
			<#list virList as virId>
				<td class="classroomArrangement">
					<#if dto.batchClassListMap?exists && dto.batchClassListMap[virId]?exists>
						<#assign batchClassList=dto.batchClassListMap[virId]>
						<#list batchClassList as clazz>
							<a class="btn btn-sm btn-white my2 item" href="javascript:void(0)" <#if clazz.studentList?size gt 0>onclick="showDetail('${clazz.id!}','${type}')"</#if>>
		                        <#if clazz.studentList?size == 0>
		                            <span class="del" onclick="deleteBlankTeachClass('${clazz.id!}', this)" style="display: none">×</span>
		                        </#if>
								<span class="color-blue">${clazz.className!}</span>
								<span class="color-999 font-12">（${clazz.studentList?size}人）</span>
							</a>
						</#list>

					<#else>
						/
					</#if>

				</td>
			</#list>
		</tr>
	</#list>
	</#if>
	</tbody>
</table>

<script>
    $(function () {
        $(".classroomArrangement .item").on("mouseover", function () {
            $(this).children(".del").show();
        });
        $(".classroomArrangement .item").on("mouseout", function () {
            $(this).children(".del").hide();
        });
    });

    function deleteBlankTeachClass(id, obj) {
        layer.confirm('确定删除吗？', function(index) {
            $.ajax({
                url: "${request.contextPath}/newgkelective/${divideId!}/showDivideResult/deleteEmptyTeachClass",
                data: {"classId": id},
                success: function (result) {
                    var jsonResult = JSON.parse(result);
                    if (jsonResult.success) {
                        $(obj).parent().remove();
                        layer.msg(jsonResult.msg, {offset: 't', time: 2000});
                    } else {
                        layer.msg(jsonResult.msg, {offset: 't', time: 2000});
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
        },function () {
        });
    }
</script>