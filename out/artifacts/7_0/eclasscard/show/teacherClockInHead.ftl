<#import "/fw/macro/popupMacro.ftl" as popup />
<div class="tab-content">
	<div id="aa" class="tab-pane active" role="tabpanel">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">日期：</span>
				<div class="filter-content">
					<div class="input-group" style="width:260px;">
						<input type="text" class="form-control datepicker" id="startTime" onchange="searchList()">
						<span class="input-group-addon">
							<i class="fa fa-minus"></i>
						</span>
						<input type="text" class="form-control datepicker" id="endTime" onchange="searchList()">
					</div>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">班牌用途：</span>
				<div class="filter-content">
					<select name="" id="type" class="form-control" onchange="searchList()">
						<option value="">---请选择---</option>
						<#if usedForDtos?exists&&usedForDtos?size gt 0>
		                  	<#list usedForDtos as item>
							<option value="${item.thisId!}">${item.content!}</option>
		              	    </#list>
                    	</#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">教师：</span>
			    <@popup.selectOneTeacher clickId="teacherName" columnName="教师(单选)" dataUrl="${request.contextPath}/common/div/teacher/popupData" id="teacherId" name="teacherName" dataLevel="2" type="danxuan" recentDataUrl="${request.contextPath}/common/div/teacher/recentData" resourceUrl="${resourceUrl}" handler="searchList();">
                    <input type="text" id="teacherName"  class="form-control"/>
                    <input type="hidden" id="teacherId" name="teacherId" />
                </@popup.selectOneTeacher>
           </div>
		</div>
		<div class="table table-striped" id="showList">
		</div>
	</div>
</div>

<script>
$(function(){
	$('.datepicker').datepicker({
		language: 'zh-CN',
		format: 'yyyy-mm-dd',
		autoclose: true
	}).next().on('click', function(){
		$(this).prev().focus();
	});	
	searchList();		
});

function searchList(){
    var startTime = $('#startTime').val();
    var endTime = $('#endTime').val();
    var type = $('#type').val();
    var teacherId = $('#teacherId').val();
    if(startTime != '' && endTime != ''){
        if(startTime>endTime){
           layerTipMsgWarn("提示","开始时间不能大于结束时间");
	       return;
        }
    }
    var str = "?startTime="+startTime+"&endTime="+endTime+"&type="+type+"&teacherId="+teacherId;
    var url = "${request.contextPath}/eccShow/eclasscard/teacherClockInList"+str;
    $("#showList").load(url);
}
</script>									