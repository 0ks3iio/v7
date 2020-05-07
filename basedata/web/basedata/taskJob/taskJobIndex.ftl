<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="box box-default">
	<div class="box-body" id="taskJobList${businessType}">
	</div>
</div>


<script type="text/javascript">
	$(function(){
		taskJobList${businessType}();
	});
	function taskJobList${businessType}(){
		$("#taskJobList${businessType}").load("${request.contextPath}/basedata/taskJob/list/page?businessType=${businessType}");
	}
</script>
