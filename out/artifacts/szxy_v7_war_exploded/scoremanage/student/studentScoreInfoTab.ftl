<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/js/LodopFuncs.js"></script>
<div id="showDiv" class="box box-default">
	<div class="nav-tabs-wrap clearfix">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
			<li role="presentation" class="active"><a href="#aa" role="tab" onclick="itemShowList('1')" data-toggle="tab">必修课成绩查询</a></li>
			<li role="presentation"><a href="#bb" role="tab" onclick="itemShowList('2')" data-toggle="tab">选修课成绩查询</a></li>
			<li role="presentation"><a href="#bb" role="tab" onclick="itemShowList('3')" data-toggle="tab">成绩单</a></li>
		</ul>
	</div>
	<div id="itemShowDivId">
		
	</div>
</div>
<script type="text/javascript">
	$(function(){
		itemShowList('1');
	});
	function itemShowList(tabIndex){
		if(tabIndex == '1'){
			var url =  '${request.contextPath}/scoremanage/student/head/page?tabIndex=1';
		}else if(tabIndex == '2'){
			var url =  '${request.contextPath}/scoremanage/student/head/page?tabIndex=2';
		}else if(tabIndex == '3'){
			var url =  '${request.contextPath}/scoremanage/student/head/page?tabIndex=3';
		}
		$("#itemShowDivId").load(url);
	}
</script>
