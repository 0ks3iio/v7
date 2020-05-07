<#import "/fw/macro/treemacro.ftl" as treemacro>
<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.all.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<div class="filter">
	<div class="filter-item">
		<span class="filter-name">学年：</span>
		<div class="filter-content">
			<select name="searchAcadyear" id="searchAcadyear" class="form-control" onChange="showList('${type!}','')">
					<#if acadyearList?exists && (acadyearList?size>0)>
	                    <#list acadyearList as item>
		                     <option value="${item!}" <#if semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
	                    </#list>
                    </#if>
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">学期：</span>
		<div class="filter-content">
			<select id="searchSemester" name="searchSemester" class="form-control" onChange="showList('${type!}','')">
					${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
			</select>
		</div>
	</div>
	<#if type=='1'>
		<div class="filter-item">
			<span class="filter-name">记录类型：</span>
			<div class="filter-content">
				<select name="recordType" id="recordType" class="form-control" onChange="showList('${type!}','');">
					  <#if recordTypes?exists && (recordTypes?size>0)>
					        <option value="">全部类型</option>
		                    <#list recordTypes as recordType>
			                     <option value="${recordType.thisId!}" <#if tutorRecord?exists &&tutorRecord.recordType==recordType.thisId> selected </#if>>${recordType.mcodeContent!}</option>
		                    </#list>
	                  </#if>
				</select>
			</div>
		</div>
		<#if isShowTN?default(false)>
	        <div class="filter-item">
				<span class="filter-name">姓名：</span>
				<div class="filter-content">
					<input type="text" class="form-control" id="tName" onchange="showList('${type!}','');" >
				</div>
			</div>
	    </#if>
	<#elseif type=='2'>
		<div class="filter-item">
			<span class="filter-name">轮次名称：</span>
			<div class="filter-content">
				<select name="" id="tutorName" class="form-control" onChange="showList('${type!}','');" style="width:180px">
					<option value="">---请选择---</option>
					<#if tutorList?exists && tutorList?size gt 0>
	                  	<#list tutorList as tutor>
						 <option value="${tutor.id!}">${tutor.roundName!}</option>
	              	    </#list>
	                </#if>
				</select>
			</div>
	    </div>
	    <div class="filter-item filter-item-right">
			<a href="javascript:void(0);" onclick="doTeaExport()" class="btn btn-blue">导出Excel</a>
	    </div>
	</#if>
</div>
<input type="hidden" id="studentId" value="">
<input type="hidden" id="deptId" value="">
<#if type=='1'>
  <div id="classTutor" class="table-container">
			
  </div>
<#elseif type=='2'>
    <#--  
	<div class="clearfix">
		<div class="tree-wrap">
		<h4>部门选择</h4>
			<@treemacro.deptForUnitInsetTree height="550" click="onTreeClick"/>
		</div>
	</div>
	-->
	<form  class="tutorRecordAll">
		<div id="showList" class="table-container">
			
		</div>
	</form>
</#if>
<script>
$(function(){
<#if type=='1'>
	showList('1','');
<#else>
	showList('2','');
</#if>
});
$("#tName").keypress(function () {
			displayResult();
});
function onTreeClick(event, treeId, treeNode, clickFlag){
	if(treeNode.type == "dept"){
		var id = treeNode.id;
		$("#deptId").val(id);
		showList('2',id);
	}
}
function showList(type,id){
	var acadyear = $("#searchAcadyear").val();
	var semester =  $("#searchSemester").val();
	if(type=='1'){
	var teacherName = $("#tName").val();
	teacherName = teacherName.replaceAll(" ", "");
	var recordType = $("#recordType").val();
	var isAll = "true";
	var url =  '${request.contextPath}/tutor/record/manage/doRecordList?acadyear='+acadyear+'&semester='+semester+'&recordType='+recordType+'&isAll='+isAll+'&teacherName='+teacherName;
	$("#classTutor").load(url);
	}else if(type=='2'){
	    <#--  
		if(id==undefined ||id==""){
			id=$("#deptId").val();
		}
		-->
		var tutorId = $("#tutorName").val();
		var url =  '${request.contextPath}/tutor/allrecord/teacher/recordsum/list?tutorId='+tutorId+'&acadyear='+acadyear+'&semester='+semester;
		$("#showList").load(url);
	}
}

//导出
function doTeaExport(){
	var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
	if (LODOP==undefined || LODOP==null) {
		return;
	}
	//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
	LODOP.ADD_PRINT_TABLE("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($(".tutorRecordAll")));
	LODOP.SAVE_TO_FILE(getConditions()+"导师记录汇总"+".xls");	
}
function getConditions(){
	var acadyear = $("#searchAcadyear").val();
	var semester =  $("#searchSemester").val();
	<#--  
	var tutorName = $("#tutorName").text();
	if(tutorName==undefined ||tutorName=="---请选择---"){
			tutorName = "所有";
	}
	-->
	return acadyear+"学年第"+semester+"学期";
}
function displayResult(){	
		var x;
        if(window.event){
        	 // IE8 以及更早版本
        	x=event.keyCode;
        }else if(event.which){
        	// IE9/Firefox/Chrome/Opera/Safari
            x=event.which;
        }
        if(13==x){
            showList('1','');
        }
    }
</script>