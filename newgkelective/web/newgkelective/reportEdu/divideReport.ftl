<div class="form-horizontal" role="form">
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right">分班结果：</label>
		<div class="col-sm-3">
			<div class="filter-content header_filter" style="width:300px;" >
            <select vtype="selectOne" class="form-control" id="divideId" onChange="reportShow()" style="width: 500px">
                <#if allList?exists && allList?size gt 0>
                    <#list allList as e>
		                <option value="${e.id!}" <#if divideId! == e.id!>selected</#if>>${e.divideName!}</option>
                    </#list>
                <#else>
                    <option value="">---未进行任何分班---</option>
                </#if>
            </select>
            
			</div>
		</div>
		<div class="col-sm-4 control-tips"></div>
    </div>
    <div id="reportDetail">

    </div>
</div>

<script>
    $(function () {
    	<#if allList?exists && allList?size gt 0>
    	initChosenOne(".header_filter");  
        reportShow();
        </#if>
    });

    function goBack() {
        var url =  '${request.contextPath}/newgkelective/${gradeId!}/goDivide/index/page';
        $("#showList").load(url);
    }

    function reportShow() {
        var url = '${request.contextPath}/newgkelective/report/divide/detail/page?gradeId=${gradeId!}&divideId=' + $("#divideId option:selected").val();
        $("#reportDetail").load(url);
    }
</script>