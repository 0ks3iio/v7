<div class="filter">
		<div class="filter-item">
			<span class="filter-name">干部姓名：</span>
			<div class="filter-content">
				<input type="text" class="typeahead scrollable form-control"  autocomplete="off" data-provide="typeahead" id="teacherName" value="${teacherName!}">
			</div>
		</div>
		<div class="filter-item">
			<span class="filter-name">结亲对象村：</span>
			<div class="filter-content">
                <select name="contryName" id="contryName" class="form-control" notnull="false" onChange="searchList();" style="width:168px;">
				${mcodeSetting.getMcodeSelect("DM-XJJQC", '', "1")}
                </select>
				<#--<input type="text" class="typeahead scrollable form-control"  autocomplete="off" data-provide="typeahead" id="contryName" value="${contryName!}">-->
			</div>
		</div>
		<div class="filter-item">
			<span class="filter-name">访亲计划名称：</span>
			<div class="filter-content">
				<select name="" id="activityId" class="form-control" style="width:200px;" onchange="searchList();">
				<#if famDearActivities?exists && famDearActivities?size gt 0>
				    <#list famDearActivities as item>
					    <option value="${item.id!}" <#if '${activityId!}' == '${item.id!}'>selected="selected"</#if>>
					    ${item.title!}
					    </option>
					</#list>
			    <#else>
			        <option value="">--请选择--</option>
				</#if>
				</select>
			</div>
		</div>
		<div class="filter-item">
			 <button type="button" class="btn btn-default" onClick="searchList();">
				 <i class="fa fa-search"></i>
			 </button>
	    </div>
		<div class="filter-item ">
			<div class="text-right">
				<a class="btn btn-blue" onclick="exportList()">导出</a>
			</div>
		</div>
	</div>
<div id="haveAudtionDiv"></div>
<script>
$(function(){
    searchList();
})

function searchList(){
    var activityId = $('#activityId').val();
    var options=$("#contryName option:selected");
    var contryName = "";
    if(options.val()) {
        contryName = options.text();
    }
    // var contryName = $('#contryName').val();
    var teacherName = $('#teacherName').val();
   var url = "${request.contextPath}/familydear/registerAudit/haveAudtingList?activityId="+activityId+"&contryName="+encodeURIComponent(contryName)+"&teacherName="+encodeURIComponent(teacherName);
   $('#haveAudtionDiv').load(url);
}

function exportList() {
    var activityId = $('#activityId').val();
    var options=$("#contryName option:selected");
    var contryName = "";
    if(options.val()) {
        contryName = options.text();
    }
    // var contryName = $('#contryName').val();
    var teacherName = $('#teacherName').val();
    document.location.href="${request.contextPath}/familydear/registerAudit/export?activityId="+activityId+"&contryName="+encodeURIComponent(contryName)+"&teacherName="+encodeURIComponent(teacherName);

}

	
</script>