<div class="filter">
	<div class="filter-item">
		<span class="filter-name">学年：</span>
		<div class="filter-content">
			<select name="acadyear" id="acadyear" class="form-control" onchange="change()">
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
			<select name="semester" id="semester" class="form-control" onchange="change()">
				${mcodeSetting.getMcodeSelect('DM-XQ',(semester?default(0))?string,'0')}
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">年级：</span>
		<div class="filter-content">
			<select name="gradeId" id="gradeId" class="form-control" onchange="change()">
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
	<div class="filter-item filter-item-right">
		<button class="btn btn-blue" onclick="courseScheduleCopy()">复制或导入课表</button>
		<button class="btn btn-blue" onclick="copyToOtherGrade()">复制课程到其他年级</button>
		<button class="btn btn-blue js-addCourse" onclick="gradeAdd()">添加开设课程</button>
		<button class="btn btn-blue" onclick="gradeImport()">导入</button>
	</div>
</div>

<div class="row" id="gradeDetail">
	
</div>
<div id="addCourseDiv" style="display:none">
</div>
<script>
$(function(){
	change();
});

function gradeImport(){
	var acadyear = $("#acadyear").val(); 
	var semester = $("#semester").val(); 
	var gradeId = $("#gradeId").val(); 
	var url = "${request.contextPath}/basedata/courseopen/gradeImport/head/page?tabIndex=1&acadyear="+acadyear+"&semester="+semester+"&gradeId="+gradeId;;
	$("#showList").load(url);
}

function gradeAdd(){
	var acadyear = $("#acadyear").val(); 
	var semester = $("#semester").val(); 
	var gradeId = $("#gradeId").val(); 
	if(gradeId != '') {
		var url = "${request.contextPath}/basedata/courseopen/class/add/index/page?acadyear=" + acadyear + "&semester=" + semester + "&gradeId=" + gradeId;
		$("#addCourseDiv").load(url);
	}else {
		layer.alert('请选择年级',{icon:7});
	}
	layer.open({
			type: 1,
			shadow: 0.5,
			title: '添加开设课程',
			area: ['1000px','660px'],
			scrollbar:false,
			btn: ['确定', '取消'],
			content: $('#addCourseDiv'),
			yes:function(index,layero){
				saveCourseList();
			}
	})
}

function change(useMaster){
	var acadyear = $("#acadyear").val(); 
	var semester = $("#semester").val(); 
	var gradeId = $("#gradeId").val(); 
	var url = "${request.contextPath}/basedata/courseopen/detail/index/page?acadyear=" + acadyear + "&semester=" + semester + "&gradeId=" + gradeId+"&useMaster"+useMaster;
	$("#gradeDetail").load(url);
}

function copyToOtherGrade(){
	var acadyear = $("#acadyear").val(); 
	var semester = $("#semester").val(); 
	var gradeId = $("#gradeId").val(); 
	if(gradeId != '') {
		$.ajax({
			url:"${request.contextPath}/basedata/courseopen/copyToOtherGrade",
			data:{"acadyear":acadyear, "semester":semester, "gradeId":gradeId},
			dataType: "json",
			success: function(data){
				if(data.success){
					layer.msg(data.msg, {offset: 't',time: 2000});
		 		}else{
		 			layerTipMsg(data.success,"操作失败",data.msg);
				}
			}
		});
	}else {
		layer.alert('请选择年级',{icon:7});
	}
}

function courseScheduleCopy(){
	var acadyear = $("#acadyear").val(); 
	var semester = $("#semester").val(); 
	var gradeId = $("#gradeId").val();
	var url = "${request.contextPath}/basedata/courseScheduleCopy/index/page?tabIndex=1&acadyear="+acadyear+"&semester="+semester+"&gradeId="+gradeId;
	$("#showList").load(url);
}

</script>
