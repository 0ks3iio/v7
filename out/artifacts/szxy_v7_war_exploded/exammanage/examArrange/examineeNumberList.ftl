<form id="examNumberForm">
<input type="hidden" name="examId" value="${examId!}"/>
<table class="table table-bordered table-striped table-hover no-margin">

	<tbody>
		<#if stuDtoList?exists && (stuDtoList?size > 0)>
            <thead>
            <tr>
                <th class="">姓名</th>
                <th class="">性别</th>
                <th class="">学号</th>
                <th class="">班级</th>
                <th class="">考号</th>
            </tr>
            </thead>
			<#list stuDtoList as dto>
				<tr>
					<td class=""><input type="hidden" name="stuDtoList[${dto_index}].studentId" value="${dto.student.id!}">
					<input type="hidden" name="stuDtoList[${dto_index}].studentName" value="${dto.student.studentName!}">
					${dto.student.studentName!}
					
					</td>
					<td class="">${mcodeSetting.getMcode("DM-XB","${dto.student.sex!}")}</td>
					<td class="">${dto.student.studentCode!}</td>
					<td class="">${dto.className!}</td>
					<td class="">
						<input class="table-input color-blue examNumber_class" name="stuDtoList[${dto_index}].examNumber" id="examNumber_${dto_index}" type="text" value="${dto.examNumber!}" maxlength="50" nullable="false"/>
					</td>
				</tr>
			</#list>
		<#else >
            <div class="no-data-container">
                <div class="no-data">
				<span class="no-data-img">
					<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
				</span>
                    <div class="no-data-body">
                        <p class="no-data-txt">暂无相关数据</p>
                    </div>
                </div>
            </div>
		</#if>
	</tbody>
</table>
</form>
<script>
$(function(){
	var canEdit=$("#canEdit").val();
	if(canEdit && canEdit=="false"){
		$(".examNumber_class").attr("disabled", true);
	}
});
// 回车获取焦点
    //$('input:text:first').focus();
    var $inp = $('input:text');
    $inp.on('keydown', function (e) {
        var key = e.which;
        if (key == 13) {
            e.preventDefault();
            var nxtIdx = $inp.index(this) + 1;
            $(":input:text:eq(" + nxtIdx + ")").focus().select();
        }
    }); 
</script>  
