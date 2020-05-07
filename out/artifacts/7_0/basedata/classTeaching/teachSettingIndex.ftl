<div class="filter">
	<div class="filter-item">
		<span class="filter-name">学年：</span>
		<div class="filter-content">
			<select name="acadyear" id="acadyear" class="form-control" onchange="allChange()">
				<#if acadyearList?exists && (acadyearList?size>0)>
                    <#list acadyearList as item>
	                     <option value="${item!}" <#if acadyear?default('')==item>selected</#if>>${item!}</option>
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
			<select name="semester" id="semester" class="form-control" onchange="allChange()">
				${mcodeSetting.getMcodeSelect('DM-XQ',(semester?default(0))?string,'0')}
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">年级：</span>
		<div class="filter-content">
			<select name="gradeId" id="gradeId" class="form-control" onchange="allChange()">
				<#if gradeList?exists && (gradeList?size>0)>
                    <#list gradeList as item>
	                     <option value="${item.id!}" <#if gradeId?default('a')==item.id?default('b')>selected</#if>>${item.gradeName!}</option>
                    </#list>
                <#else>
                	 <option value=''>暂无年级</option>
                </#if>
			</select>
		</div>
	</div>
	
	<div class="filter-item">
		<span class="filter-name">科目：</span>
		<div class="filter-content">
			<select name="subjectId" id="subjectId" class="form-control" onchange="teachSetting()">
				
			</select>
		</div>
	</div>
	
	<div class="filter-item filter-item-right">
		<button class="btn btn-blue" onclick="courseScheduleImport()">任课老师导入</button>
	</div>
</div>

<div class="row" id="teachSetting">
	
</div>

<script>
$(function() {
	gradeChange();
});

function allChange() {
	gradeChange();
}

function teachSetting(useMaster) {
	var acadyear = $("#acadyear").val(); 
	var semester = $("#semester").val(); 
	var gradeId = $("#gradeId").val(); 
	var subjectId = $("#subjectId").val(); 
	var url = "${request.contextPath}/basedata/courseopen/teach/setting/page?acadyear=" + acadyear + "&semester=" 
		+ semester + "&gradeId=" + gradeId + "&subjectId=" + subjectId+"&useMaster="+useMaster;
	$("#teachSetting").load(url);
}

function gradeChange(){
	var acadyear = $("#acadyear").val(); 
	var semester = $("#semester").val(); 
	var gradeId = $("#gradeId").val(); 
	$.ajax({
		url:"${request.contextPath}/basedata/courseopen/getSubject",
		data:{"acadyear":acadyear, "semester":semester, "gradeId":gradeId},
		dataType: "json",
		success: function(data){
			$("#subjectId").html("");
			var content = "";
			var jsonArr = data.jsonArr;
			if(jsonArr.length>0){
				for(var i=0;i<jsonArr.length;i++){
					var arrData=jsonArr[i];
					tempContent = '<option value='+ arrData.courseId +'>' + arrData.courseName +'</option>';
					content += tempContent;
				}
			}else{
				tempContent = "<option value=''>暂无开设科目</option>";
				content += tempContent;
			}
			$("#subjectId").html(content);
			teachSetting();
		}
	});
}

function courseScheduleImport(){
	var acadyear = $("#acadyear").val(); 
	var semester = $("#semester").val(); 
	var gradeId = $("#gradeId").val();
	var url = "${request.contextPath}/basedata/import/courseScheduleImport/head/page?tabIndex=3&acadyear="+acadyear+"&semester="+semester+"&gradeId="+gradeId;
	$("#showList").load(url);
}
</script>