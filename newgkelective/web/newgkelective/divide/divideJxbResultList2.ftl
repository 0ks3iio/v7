<#-- 普通7选三 模式下的选考 学考分班结果展示 -->

<table class="table table-bordered layout-fixed table-editable" data-label="不可排课">
	<thead>
		<tr>
			<th rowspan="2" width="10%">科目</th>
			<th rowspan="2" width="10%">总人数</th>
			<th rowspan="2" width="10%">总班级数</th>
			<th>跟随行政班上课</th>
			<#list 1..bathNum as ii>
			<th><#if type?default("A")=="A">选<#else>学</#if>考${ii}</th>
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
		<#-- 	<td >${(dto.aXZBStuNum + dto.bXZBStuNum)!0}</td>
			<td >
			<#if dto.asXzbClassList?exists && dto.asXzbClassList?size gt 0>
				<#list dto.asXzbClassList as clazz>
					<#if clazz_index != 0>、</#if>
					<a href="javascript:void(0)" onclick="showDetail('${clazz.id!}','${type}')">${clazz.className!}(${clazz.studentList?size}人)</a>
				</#list>
			<#else>
				/
			</#if>	
			</td>  -->
		<#-- 	<td >${((dto.aStuNum + dto.bStuNum) - (dto.aXZBStuNum + dto.bXZBStuNum))!0}</td>  -->
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
			
			<#list 1..bathNum as ii>
				<td class="classroomArrangement">
					<#if dto.batchClassListMap?exists && dto.batchClassListMap[""+ii]?exists>
						<#assign batchClassList=dto.batchClassListMap[""+ii]>
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