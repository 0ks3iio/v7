<table class="table table-bordered layout-fixed table-editable">
	<thead>
		<tr>
			<th class="text-center">科目</th>
			<th class="text-center">总人数</th>
			<#if type! == 'A' || type! == 'O'>
				<th class="text-center">分层数</th>
				<th class="text-center">层级</th>
				<th class="text-center">总人数</th>
			</#if>
			<th class="text-center" >班级数</th>
			<#list bathList as bath>
			<th class="text-center" <#if bathList?size lt 5> width="20%" </#if> ><#if type?default("A")=="A">选<#else>学</#if>考${bath}</th>
			</#list>
		</tr>
	</thead>
	<tbody>
	<#if dtoList?exists && dtoList?size gt 0>
	<#list dtoList as dto>
		<#if dto.levelMap?exists && dto.levelMap?size gt 0>
		<#list dto.levelMap?keys as level>
		<tr>
			<#if level_index == 0>
				<td rowspan="${dto.levelNum!}">${dto.courseName!}</td>
				<td rowspan="${dto.levelNum!}">${dto.totalNum!}</td>
				<#if type! == 'A' || type! == 'O'>
					<td rowspan="${dto.levelNum!}">${dto.levelNum!}</td>
				</#if>
			</#if>
			
			<#if type! == 'A' || type! == 'O'>
				<td><#if level?default('')=='V'>/<#else>${level!}</#if></td>
				<td>
					<#assign stuNum = 0>
					<#list dto.levelMap[level] as clazz>
						<#assign stuNum = stuNum + clazz.studentList?size>
					</#list>
					${stuNum}
				</td>
			</#if>
			
			<td>${dto.levelMap[level]?size}</td>
			<#list bathList as i>
			<td class="classroomArrangement">
				<#if dto.levelBatchMap[level][i+""]?exists && dto.levelBatchMap[level][i+""]?size gt 0>
				<#list dto.levelBatchMap[level][i+""] as clazz>
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