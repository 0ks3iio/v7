<div class="filter filter-f16">
	<div class="filter-item">
		<span class="filter-name">状态：</span>
		<div class="filter-content">
			<select class="form-control" id="status" name="status" onChange="showFilterList()">
				<option value="2" <#if (status!) == '2'>selected</#if>>全部</option>
				<option value="1" <#if (status!) == '1'>selected</#if>>已安排</option>
				<option value="0" <#if (status!) == '0'>selected</#if>>未安排</option>
			</select>
		</div>
	</div>
<div class="filter-item">
	<span class="filter-name">科目：</span>
	<div class="filter-content">
		<select class="form-control" id="subjectId" name="subjectId" onChange="showFilterList()">
			<#if courseList?exists && (courseList?size>0)>
            	<option value="all">全部</option>
                <#list courseList as item>
	            	<option value="${item.id!}" <#if (subjectId!) == (item.id!)>selected</#if>>${item.subjectName!}</option>
                </#list>
            <#else>
            	<option value="">未设置</option>
            </#if>
		</select>
	</div>
</div>
<div class="filter-item">
	<div class="filter-content">
		<div class="input-group input-group-search">
			<span class="form-control">姓名</span>
			<div class="pos-rel pull-left">
				<input type="text" name="teacherName" id="teacherName" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="请输入老师姓名" onkeydown="dispRes()">
			</div>
			<div class="input-group-btn">
				<button type="button" class="btn btn-default" onclick="findByTeacherName()">
					<i class="fa fa-search"></i>
				</button>
			</div>
		</div>
	</div>
</div>
<#if havedata == 'true'>
<div class="filter-item filter-item-right">
	<a href="javascript:" class="btn btn-blue" id="invigilateImport">导入</a>
	<a href="javascript:" class="btn btn-blue" onclick="setTeacher()">设置监考老师</a>
</div>
<#else>
<div class="filter-item filter-item-right">
	<span class="tip tip-grey ">(请先维护考试科目和考场！)</span>
</div>
</#if>
</div>

<div id="filterList">
</div>
<script>
$(function(){
	showFilterList();
});
function dispRes(){
	var x;
    if(window.event) // IE8 以及更早版本
    {	x=event.keyCode;
    }else if(event.which) // IE9/Firefox/Chrome/Opera/Safari
    {
        x=event.which;
    }
    if(13==x){
        findByTeacherName();
    }
}
function showFilterList(){
	$("#teacherName").val("");
	var status=$("#status").val();
	var subjectId=$("#subjectId").val();
	var str='&status='+status+'&subjectId='+subjectId;
	var url =  '${request.contextPath}/exammanage/examArrange/invigilateTeacherList/page?examId=${examId!}'+str;
	$("#filterList").load(url);
}

function setTeacher() {
	var url = "${request.contextPath}/exammanage/examArrange/invigilateTeacherInfo/page?examId=${examId!}&courseSize=${courseList?size}";
	indexDiv = layerDivUrl(url,{title: "设置监考老师",width:540,height:600});
}

function findByTeacherName(){
	$("#status").val("1");
	$("#subjectId").val("all");
	var teacherName=$("#teacherName").val();
	var ss="请先输入要查询的老师姓名";
	if(teacherName==""){
		layerTipMsg(false,"提示",ss+"！");
		return;
	}
	var status=$("#status").val();
	var subjectId=$("#subjectId").val();
	var str = "&teacherName=" + teacherName + "&status=" + status + "&subjectId=" + subjectId;
	var url =  '${request.contextPath}/exammanage/examArrange/invigilateTeacherList/page?examId=${examId!}'+str;
	url=encodeURI(encodeURI(url));
	$("#filterList").load(url);
}

$("#invigilateImport").on("click",function(){
	var url="${request.contextPath}/exammanage/invigilateImport/main?examId=${examId!}"
	$("#showTabDiv").load(url);
});
</script>