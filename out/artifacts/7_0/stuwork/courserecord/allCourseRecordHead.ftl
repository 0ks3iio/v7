<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
<div class="tab-content">
   <div id="bb" class="tab-pane active" role="tabpanel">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">日期：</span>
				<div class="filter-content">
					<div class="input-group">
						<input type="text" style="width:120px" id="queryDate" onChange="searchList();" class="form-control datepicker" value="${(nowDate?string('yyyy-MM-dd'))!}">
						<span class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</span>
					</div>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">班级：</span>
				<div class="filter-content">
					<select name="" id="classId" class="form-control" onChange="searchList();">
					<#if clsList?exists && clsList?size gt 0>
					   <#list clsList as item>
					        <option value="${item.id!}">${item.classNameDynamic!}</option>
					   </#list>
					<#else>
					<option value="">---请选择---</option>
					</#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">节次：</span>
				<div class="filter-content">
					<select name="" id="period" class="form-control" onChange="searchList();">
					<#if periodList?exists && periodList?size gt 0>
					   <option value="">---请选择---</option>
					   <#list periodList as item>
					        <option value="${item!}">${item!}</option>
					   </#list>
					<#else>
					   <option value="">---请选择---</option>
					</#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">老师：</span>
				<div class="filter-content">
					<select name="" id="teacherId" class="form-control" onChange="searchList();">
					<#if teacherList?exists && teacherList?size gt 0>
					   <option value="">---请选择---</option>
					   <#list teacherList as item>
					        <option value="${item.id!}">${item.teacherName!}</option>
					   </#list>
					<#else>
					   <option value="">---请选择---</option>
					</#if>
					</select>
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
	
	//初始化单选控件
	var classIdSearch = $('#classId');
	$(classIdSearch).chosen({
	width:'130px',
	no_results_text:"未找到",//无搜索结果时显示的文本
	allow_single_deselect:true,//是否允许取消选择
	disable_search:false, //是否有搜索框出现
	search_contains:true,//模糊匹配，false是默认从第一个匹配
	//max_selected_options:1 //当select为多选时，最多选择个数
	});
	
	var teacherIdSearch = $('#teacherId');
	$(teacherIdSearch).chosen({
	width:'130px',
	no_results_text:"未找到",//无搜索结果时显示的文本
	allow_single_deselect:true,//是否允许取消选择
	disable_search:false, //是否有搜索框出现
	search_contains:true,//模糊匹配，false是默认从第一个匹配
	//max_selected_options:1 //当select为多选时，最多选择个数
	});
});

function searchList(){
    var queryDate = $('#queryDate').val();
    var classId = $('#classId').val();
    var teacherId = $('#teacherId').val();
    var period = $('#period').val();
    var str = "?acadyear=${acadyear!}&semester=${semester!}&teacherId="+teacherId+"&queryDate="+queryDate+"&period="+period+"&classId="+classId;
    var url = "${request.contextPath}/stuwork/courserecord/allCourseList"+str;
    $("#showList").load(url);
}
</script>