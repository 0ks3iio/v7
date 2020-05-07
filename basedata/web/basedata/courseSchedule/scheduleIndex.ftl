<div class="box box-default">
<div class="row">
	<div class="col-xs-12">
	      <div class="box-body">
	         <div class="filter clearfix head-div">
	        	<div class="filter-item">
					<label for="" class="filter-name">学年：</label>
					<div class="filter-content">
						<select class="form-control" id="acadyear" onChange="searchWeek();" style="width:168px;">
						<#if (acadyears?size>0)>
							<#list acadyears as item>
							<option value="${item!}" <#if acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
							</#list>
						</#if>
						</select>
					</div>
				</div>
				<div class="filter-item">
					<label for="" class="filter-name">学期：</label>
					<div class="filter-content">
						<select class="form-control" id="semester" onChange="searchWeek();" style="width:168px;">
						 ${mcodeSetting.getMcodeSelect('DM-XQ',(semester?default(0))?string,'0')}
						</select>
					</div>
				</div>
				 <#if type == "2">
					 <div class="filter-item">
						 <label for="" class="filter-name">年级：</label>
						 <div class="filter-content">
							 <select vtype="selectOne" class="form-control" id="grade" onChange="searchSchedule();" style="width:168px;">
								 <option value="">所有年级</option>
								 <#if gradeList?exists && gradeList?size gt 0>
									 <#list gradeList as item>
										 <option value="${item.id!}">${item.gradeName!}</option>
									 </#list>
								 </#if>
							 </select>
						 </div>
					 </div>
				 </#if>
				<div class="filter-item">
				   	 <label for="" class="filter-name">周次：</label>
				     <div class="filter-content">
					     <select vtype="selectOne" class="form-control" id="week" onChange="searchSchedule();" style="width:168px;">
						     <#if weeks?exists && (weeks?size>0)>
					              <#list weeks as item>
						               <option value="${item!}" <#if weekSearch?default(1)==item?default(1)>selected</#if>>第${item!}周</option>
					              </#list>
				             <#else>
					                   <option value="">---请选择---</option>
				             </#if>						           
					     </select>
				    </div>
			    </div>
			    
			    <div class="filter-item filter-item-right">
						<form id="downform" name="downform" method="post">
						<iframe id="downIframe" name="downIframe" style="display:none"></iframe>
							<a href="javascript:" class="btn btn-blue" id="downBtn" data-toggle="tooltip" data-original-title="导出页面中查询出的结果">导出Excel</a>
						</form>
				</div>
			</div>
			<div class="table-wrapper" id="showList">
			</div>
	    </div>
	</div>
</div>
</div>
<script>
$(function(){
	searchSchedule();
	
	$('#downBtn').on('click',function(){
		var week=$('#week').val();
		if(week==''){
			layer.msg("请选择周次！", {
							offset: 't',
							time: 2000
						});
			return;			
		}
		var showTeacher;
		var showClass;
		var showPlace;
		var gradeId;
		if ($("#showTeacher").length > 0) {
			showTeacher = $("#showTeacher").val();
		} else {
			showTeacher = "";
		}
		if ($("#showClass").length > 0) {
			showClass = $("#showClass").val();
		} else {
			showClass = "";
		}
		if ($("#showPlace").length > 0) {
			showPlace = $("#showPlace").val();
		} else {
			showPlace = "";
		}
		if ($("#grade").length > 0) {
			gradeId = $("#grade").val();
		} else {
			gradeId = "";
		}
		var acadyear=$('#acadyear').val();
		var semester=$('#semester').val();
		var url = '${request.contextPath}/basedata/mastercourseschedule/download?acadyear='+acadyear+'&semester='+semester+'&weekOfWorktime='+week+'&showTeacher='+showTeacher+'&showClass='+showClass+'&showPlace='+showPlace+'&gradeId='+gradeId+'&type=${type!"1"}';
		var downform=document.getElementById('downform');
		if(downform){
			downform.action=url;
			downform.target="downIframe";
			downform.submit();
		}
	});
})

function searchWeek(){
	var acadyear=$('#acadyear').val();
	var semester=$('#semester').val();
	var url = '${request.contextPath}/basedata/mastercourseschedule/index/page?type=${type!"1"}&acadyear='+acadyear+'&semester='+semester;
	$('#pageDiv').load(url);
}

function searchSchedule(){
	var week=$('#week').val();
	if(week==''){
		layer.msg("请选择周次！", {
						offset: 't',
						time: 2000
					});
		return;			
	}
	var acadyear=$('#acadyear').val();
	var semester=$('#semester').val();
	if ($("#grade").length > 0) {
		var gradeId = $("#grade").val();
	} else {
		var gradeId="";
	}
	var url = '${request.contextPath}/basedata/mastercourseschedule/list/page?acadyear='+acadyear+'&semester='+semester+'&gradeId='+gradeId+'&weekOfWorktime='+week+'&type=${type!"1"}';
	$('#showList').load(url);
}
</script>