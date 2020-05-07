<#import "/studevelop/common/studevelopTreemacro.ftl" as studevelopTreemacro>
<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.all.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<title>社团活动登记</title>
<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<div class="row" id="importDiv">
	<div class="col-xs-12">
	   <div class="box box-default">
           <div class="row">
               <div class="col-sm-2" id="treeDivId">
               	   <#if isAdmin?default(false)>
	                   <div class="box box-default" id="id1">
	                       <div class="box-header">
	                           <h3 class="box-title">班级菜单</h3>
	                       </div>
					   <@studevelopTreemacro.gradeClassStudentForSchoolInsetTree height="550" click="onTreeClick"/>
	                   </div>
                   </#if>
               </div>
		<div class="col-sm-10" >
		      <div class="box-body">
		         <div class="filter clearfix">
		        	<div class="filter-item">
						<label for="" class="filter-name">学年：</label>
						<div class="filter-content">
							<select class="form-control" id="queryAcadyear" onChange="searchList()" style="width:168px;">
							<#if (acadyearList?size>0)>
								<#list acadyearList as item>
								<option value="${item!}" <#if semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
								</#list>
							</#if>
							</select>
						</div>
					</div>
					<div class="filter-item">
						<label for="" class="filter-name">学期：</label>
						<div class="filter-content">
							<select class="form-control" id="querySemester" onChange="searchList()" style="width:168px;">
							 ${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
							</select>
						</div>
					</div>
					
		
					<#if tabType == '1'>
					<div class="filter-item">
						<label for="" class="filter-name">奖励级别：</label>
						<div class="filter-content">
							<select name="" id="rewardslevel" class="form-control" notnull="false" onChange="searchList();" style="width:168px;">
							 	${mcodeSetting.getMcodeSelect("DM-JLJB", '', "1")}
							</select>
						</div>
					</div>	
					<div class="filter-item">
						<button class="btn btn-blue js-addTerm" onclick="addRewards();">+新增</button>
						<a href="javascript:doRewardImport();" class="btn btn-blue btn-seach">导入</a>
					</div>
					<#else>
					<div class="filter-item">
						<label for="" class="filter-name">惩处类型：</label>
						<div class="filter-content">
							<select name="" id="punishtype" class="form-control" notnull="false" style="width:168px;" onChange="searchList();">
							 	${mcodeSetting.getMcodeSelect("DM-CFMC", '', "1")}
							</select>
						</div>
					</div>
					<div class="filter-item">
						<button class="btn btn-blue js-addTerm" onclick="addPunish();">+新增</button>
						<a href="javascript:doPunishImport();" class="btn btn-blue btn-seach">导入</a>
					</div>
					</#if>	
		        <input type="hidden" id="studentId">
	            <input type="hidden" id="classId">
				<div class="table-wrapper" id="showList">
				</div>
		    </div>
		</div>
	</div>
</div>
<script>
$(function(){
	<#if isAdmin?default(false)>
		//初始化单选控件
		searchList();
	<#else>
		getMyTree();
	</#if>
});
function getMyTree(){
	var acadyear=$("#queryAcadyear").val();
    var semester=$("#querySemester").val();
    var str='?acadyear='+acadyear+"&semester="+semester+"&noTeaching=true";
    var url='${request.contextPath}/stuDevelop/proItemResult/toClassTree/page'+str;
    $('#treeDivId').load(url);
}
function　searchList(){
   var queryAcadyear = $('#queryAcadyear').val();
   var querySemester = $('#querySemester').val();
   var classId = $('#classId').val();
   var studentId = $('#studentId').val();
   <#if tabType == '1'>
   var rewardslevel = $('#rewardslevel').val();
   var str = "?queryAcadyear="+queryAcadyear+"&querySemester="+querySemester+"&classId="+classId+"&rewardslevel="+rewardslevel+"&studentId="+studentId+"&tabType=1";
   var url = "${request.contextPath}/studevelop/rewardsRecord/rewardList"+str;
   <#else>
   var punishtype = $('#punishtype').val();
   var str = "?queryAcadyear="+queryAcadyear+"&querySemester="+querySemester+"&classId="+classId+"&punishtype="+punishtype+"&studentId="+studentId+"&tabType=2";
   var url = "${request.contextPath}/studevelop/rewardsRecord/punishList"+str;
   </#if>
   $("#showList").load(url);
}

function onTreeClick(event, treeId, treeNode, clickFlag){
    if(treeNode.type == "student"){
        var id = treeNode.id;
        $("#studentId").val(id);
        searchList();
    }else if(treeNode.type == "class"){
        var id = treeNode.id;
        $("#classId").val(id);
        $("#studentId").val("");
        searchList();
    }
}

function addRewards(){
    var queryAcadyear = $('#queryAcadyear').val();
    var querySemester = $('#querySemester').val();
	var studentId = $('#studentId').val();
	if(studentId == ''){
	    layerTipMsg(false,"请选择一个学生!","");
		return;
	}
	var str = "?acadyear="+queryAcadyear+"&semester="+querySemester+"&studentId="+studentId;
	var url = "${request.contextPath}/studevelop/rewardsRecord/rewardsAdd"+str;
	indexDiv = layerDivUrl(url,{title: "奖励信息",width:750,height:490});
}

function addPunish(){
    var queryAcadyear = $('#queryAcadyear').val();
    var querySemester = $('#querySemester').val();
	var studentId = $('#studentId').val();
	if(studentId == ''){
	   layerTipMsg(false,"请选择一个学生!","");
		return;
	}
	var str = "?acadyear="+queryAcadyear+"&semester="+querySemester+"&studentId="+studentId;
	var url = "${request.contextPath}/studevelop/rewardsRecord/punishAdd"+str;
	indexDiv = layerDivUrl(url,{title: "惩处信息",width:750,height:630});
}
function doRewardImport(){
   var classId = $('#classId').val();
   var acadyear =  $('#queryAcadyear').val();
   var semester = $('#querySemester').val();
   if(classId == '' || classId == undefined){
       layerTipMsg(false,"请选择一个班级!","");
	   return;
   }
   $("#showDiv").load("${request.contextPath}/studevelop/stuDevelopRewards/import/main?acadyear="+acadyear+"&semester="+semester+"&classId="+classId);
}
function doPunishImport(){
   var acadyear =  $('#queryAcadyear').val();
   var semester = $('#querySemester').val();
   var classId = $('#classId').val();
   if(classId == '' || classId == undefined){
       layerTipMsg(false,"请选择一个班级!","");
	   return;
   }
   $("#showDiv").load("${request.contextPath}/studevelop/stuDevelopPunish/import/main?acadyear="+acadyear+"&semester="+semester+"&classId="+classId);
}

</script>
       