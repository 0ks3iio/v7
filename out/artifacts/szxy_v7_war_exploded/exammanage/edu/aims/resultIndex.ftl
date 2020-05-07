<div class="filter filter-f16">
	<div class="filter-item">
		<span class="filter-name">学校：</span>
		<div class="filter-content">
			<select class="form-control" id="schoolId" name="schoolId">
				<#if schools?exists && schools?size gt 0>
					<#list schools as sch>
						<option value="${sch.id}">${sch.schoolName!}</option>
					</#list>
				</#if>
			</select>
		</div>
	</div>
	<div class="filter-item">
        <span class="filter-name">字段：</span>
        <div class="filter-content">
            <select id="field" class="form-control" name="field" style="width:168px;" >
                <option value="1">考号</option>
                <option value="2">学籍号</option>
                <option value="3">身份证号</option>
                <option value="4">学生姓名</option>
            </select>
        </div>
    </div>
    <div class="filter-item">
        <span class="filter-name">关键字：</span>
        <div class="filter-content">
            <input type="text" id="keyWord" style="width:168px;" class="form-control"  >
        </div>
    </div>
	<div class="table-container-header text-right">
		<a href="javascript:" class="btn btn-blue js-setFilter0"  onclick="queryDiv()">查询</a>
	</div>
</div>
<div class="table-container">
	<div class="table-container-body" id="resultDiv"></div>
</div>
<script>
	function gobackIndex(){
	    var url =  '${request.contextPath}/exammanage/edu/aims/page';
	    $(".model-div").load(url);
	}
	$(function(){
		showBreadBack(gobackIndex,true,"返回");
		$("#schoolId").chosen({
			width:'200px',
			no_results_text:"未找到",//无搜索结果时显示的文本
			allow_single_deselect:true,//是否允许取消选择
			disabch_contains:true,//模糊匹配，false是默认从第一个匹配
			//mle_search:false, //是否有搜索框出现
			searax_selected_options:1 //当select为多选时，最多选择个数
		}); 
		queryDiv();
	});
	
	function queryDiv() {
		var schoolId = $("#schoolId").val();
		var field = $("#field").val();
		var keyWord = $("#keyWord").val();
		var url = '${request.contextPath}/exammanage/edu/aims/result/list?examId=${examId!}&schoolId='+schoolId+'&field='+field+'&keyWord='+keyWord;
		$("#resultDiv").load(url);
	}
</script>