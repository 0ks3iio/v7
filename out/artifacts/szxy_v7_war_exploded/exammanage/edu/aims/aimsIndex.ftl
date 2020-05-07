<div class="filter filter-f16">
	<div class="filter-item">
		<span class="filter-name">学年：</span>
		<div class="filter-content">
			<select class="form-control" id="acadyear" name="acadyear" onChange="changeExam()">
				<#if acadyearList?exists && (acadyearList?size>0)>
		        	<#list acadyearList as item>
			        	<option value="${item!}" <#if semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
		            </#list>
	            <#else>
		            <option value="">未设置</option>
	            </#if>
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">学期：</span>
		<div class="filter-content">
			<select class="form-control" id="semester" name="semester" onChange="changeExam()">
				${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
			</select>
		</div>
	</div>
	
</div>

<div class="table-container">
	<div class="table-container-body" id="examList">
	</div>
</div>
<script>
	$(function(){
		changeExam();
	});
	
	function changeExam() {
		var acadyear = $("#acadyear").val();
		var semester = $("#semester").val();
		var url = '${request.contextPath}/exammanage/edu/aims/list?acadyear='+acadyear+'&semester='+semester;
		$("#examList").load(url);
	}
</script>