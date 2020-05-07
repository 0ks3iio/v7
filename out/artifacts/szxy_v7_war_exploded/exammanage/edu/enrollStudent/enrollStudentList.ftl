<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table class="table table-bordered table-striped table-hover no-margin">
	<thead>
		<tr>
			<th style="width:5%">
				<label class="pos-rel"><input type="checkbox" id="studentCheckboxAll" class="wp"  value="" onchange="studentCheckboxAllSelect()"> 
				<span class="lbl" style="font-weight:bold;">全选</span></label></th>
			<th class="text-center" style="width:10%">照片</th>
			<th class="text-center" style="width:15%">学校</th>
			<th class="text-center" style="width:10%">姓名</th>
			<th class="text-center" style="width:10%">学籍号</th>
			<th class="text-center" style="width:10%">身份证号</th>
			<th class="text-center" style="width:10%">性别</th>
			<th class="text-center" style="width:10%">民族</th>
 			<th class="text-center" style="width:10%">班级</th>
 			<th class="text-center" style="width:10%">状态</th>
		</tr>
	</thead>
	<tbody>
		<#if enrollStudentList?exists && (enrollStudentList?size > 0)>
			<#list enrollStudentList as dto>
				<tr>
					<#if dto.student?exists>
					<td> <label class="pos-rel">
                        <input   name="stuCheckboxName" type="checkbox" class="wp" value="${dto.studentId!}">
                        <span class="lbl"></span>
                    </label></td>
					<td class="text-center"><img style="width:50px;heigth:50px;" src="${request.contextPath}${dto.showPictrueUrl!}"></td>
					<td class="text-center">${dto.schoolName!}</td>
					<td class="text-center">${dto.student.studentName!}</td>
					<td class="text-center">${dto.student.unitiveCode!}</td>
					<td class="text-center">${dto.student.identityCard!}</td>
					<td class="text-center">${mcodeSetting.getMcode("DM-XB","${dto.student.sex!}")}</td>
					<td class="text-center">${mcodeSetting.getMcode("DM-MZ","${dto.student.nation!}")}</td>
					<td class="text-center">${dto.clazz.classNameDynamic!}</td>
					<td class="text-center" id="${dto.studentId!}_state">
					<#if dto.hasPass == '0'>
						<span class="color-green">待审核</span>
					<#elseif dto.hasPass == '1'>
						<span class="color-blue">已通过</span>
					<#else>
						<span class="color-red">未通过</span>
					</#if>
					</td>
					<#else>
						<td colspan="10" class="text-center">该学生已删除</td>
					</#if>
				</tr>
			</#list>
		<#else>
			<tr>
				<td colspan="10" align="center">
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