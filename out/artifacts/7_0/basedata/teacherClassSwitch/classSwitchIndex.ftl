<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1 nav-tabs-table" role="tablist">
			<li class="active li_class" id="li01" role="presentation"><a href="javascript:void(0)" role="tab" data-toggle="tab" >调课申请</a></li>
            <#if isAdmin>
            <li class="li_class" id="li02" role="presentation"><a href="javascript:void(0)" role="tab" data-toggle="tab" >调课管理</a></li>
            </#if>
		</ul>
		<div class="tab-content" id="gradeTableList">
		</div>
	</div>
</div>

<script>
    $(function () {
        var url = "${request.contextPath}/basedata/classswitch/switchhead/page?schoolId=${schoolId}&teacherId=${teacherId}&from=01";
        $("#gradeTableList").load(url);
    });

    $("#li01").on("click", function () {
        var url = "${request.contextPath}/basedata/classswitch/switchhead/page?schoolId=${schoolId}&teacherId=${teacherId}&from=01";
        $("#gradeTableList").load(url);
    });

    <#if isAdmin>
    $("#li02").on("click", function () {
        var url = "${request.contextPath}/basedata/classswitch/switchhead/page?schoolId=${schoolId}&teacherId=${teacherId}&from=02";
        $("#gradeTableList").load(url);
    });
    </#if>
</script>