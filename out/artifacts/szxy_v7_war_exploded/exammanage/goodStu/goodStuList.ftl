<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table class="table table-bordered table-striped table-hover no-margin">
	<thead>
		<tr>
			<th style="width:5%">
				<label class="pos-rel"><input type="checkbox" id="studentCheckboxAll" class="wp"  value="" onchange="studentCheckboxAllSelect()"> 
				<span class="lbl" style="font-weight:bold;">全选</span></label></th>
			<th class="text-center" style="width:15%">学校</th>
			<th class="text-center" style="width:10%">学生姓名</th>
			<th class="text-center" style="width:10%">学籍号</th>
			<th class="text-center" style="width:10%">身份证号</th>
			<th class="text-center" style="width:10%">性别</th>
			<th class="text-center" style="width:10%">优秀生</th>
		</tr>
	</thead>
	<tbody>
		<#if enrollStudentList?exists && (enrollStudentList?size > 0)>
			<#list enrollStudentList as dto>
				<tr>
					<#if dto.student?exists>
					<td> <label class="pos-rel">
                        <input   name="stuCheckboxName" type="checkbox" class="wp" value="${dto.id!}">
                        <span class="lbl"></span>
                    </label></td>
					<td class="text-center">${dto.schoolName!}</td>
					<td class="text-center">${dto.student.studentName!}</td>
					<td class="text-center">${dto.student.unitiveCode!}</td>
					<td class="text-center">${dto.student.identityCard!}</td>
					<td class="text-center">${mcodeSetting.getMcode("DM-XB","${dto.student.sex!}")}</td>
					<td class="text-center" id="${dto.id!}_state">
					<#if dto.hasGood?exists&&dto.hasGood == '0'>
						<span class="color-green">否</span>
					<#elseif dto.hasGood?exists&&dto.hasGood == '1'>
						<span class="color-blue">是</span>
					<#else>
						<span class="color-red">未设置</span>
					</#if>
					</td>
					<#else>
						<td colspan="7" class="text-center">该学生已删除</td>
					</#if>
				</tr>
			</#list>
		<#else>
			<tr>
				<td colspan="7" align="center">
					暂无数据
				</td>
			<tr>
		</#if>
	</tbody>
</table>
<#if enrollStudentList?exists&&enrollStudentList?size gt 0>
		<@htmlcom.pageToolBar container="#stuList" class="noprint"/>
</#if>
<script>
	function studentCheckboxAllSelect(){
		if($("#studentCheckboxAll").is(':checked')){
			$('input:checkbox[name=stuCheckboxName]').each(function(i){
				$(this).prop('checked',true);
			});
		}else{
			$('input:checkbox[name=stuCheckboxName]').each(function(i){
				$(this).prop('checked',false);
			});
		}
	}
</script>