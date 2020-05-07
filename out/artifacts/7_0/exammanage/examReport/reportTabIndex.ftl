<div class="box box-default">
    <div class="box-body clearfix">
	<div class="tab-container">
		<div class="tab-header clearfix">
			<ul class="nav nav-tabs nav-tabs-1">
				<#if isXhzx?default(false)><li <#if type?default("1")=="1">class="active" </#if>>
					<a data-toggle="tab" href="#aa" aria-expanded="true" onclick="itemShowReport(1)">考场安排</a>
				</li></#if>
				<li <#if type?default("1")=="2">class="active" </#if>>
					<a data-toggle="tab" href="#bb" aria-expanded="false" onclick="itemShowReport(2)">考场门贴</a>
				</li>
				<li <#if type?default("1")=="3">class="active" </#if>>
					<a data-toggle="tab" href="#cc" aria-expanded="false" onclick="itemShowReport(3)">考生桌帖</a>
				</li>
				<li <#if type?default("1")=="4">class="active" </#if>>
					<a data-toggle="tab" href="#dd" aria-expanded="false" onclick="itemShowReport(4)">班级清单</a>
				</li>
				<li <#if type?default("1")=="5">class="active" </#if>>
					<a data-toggle="tab" href="#ee" aria-expanded="false" onclick="itemShowReport(5)">监考老师安排</a>
				</li>
				<li <#if type?default("1")=="6">class="active" </#if>>
					<a data-toggle="tab" href="#ff" aria-expanded="false" onclick="itemShowReport(6)">考试安排总表</a>
				</li>
			 </ul>
		</div>
		<!-- tab切换开始 -->
		<div class="tab-content" id="showTabDiv">

		</div>
	</div>
	</div>
</div>


										

<!-- page specific plugin scripts -->
<script type="text/javascript">
    function gobackIndex(){
        var url =  '${request.contextPath}/exammanage/examReport/head/page';
        $("#examReportDiv").load(url);
        //$(".model-div").load(url);
    };
    $(function(){
        itemShowReport('${type?default("1")}');
	<#--$('.gotoExamArrangeClass').on('click',function(){-->
	<#--var url =  '${request.contextPath}/exammanage/examArrange/head/page';-->
	<#--$("#examArrangeDiv").load(url);-->
	<#--});-->

        showBreadBack(gobackIndex,false,"返回");
    });
function itemShowReport(type){
	if("1"==type){
		var url =  '${request.contextPath}/exammanage/examReport/examPlaceList/page?examId=${examId!}';
		$("#showTabDiv").load(url);
	}else if("2"==type){
		var url =  '${request.contextPath}/exammanage/examReport/doorIndex/page?examId=${examId!}';
		$("#showTabDiv").load(url);
	}else if("3"==type){
		var url =  '${request.contextPath}/exammanage/examReport/tableIndex/page?examId=${examId!}';
		$("#showTabDiv").load(url);
	}else if("4"==type){
		var url =  '${request.contextPath}/exammanage/examReport/classIndex/page?examId=${examId!}';
		$("#showTabDiv").load(url);
	}else if("5"==type){
		var url =  '${request.contextPath}/exammanage/examReport/invigilatorList/page?examId=${examId!}';
		$("#showTabDiv").load(url);
	}else if("6"==type){
		var url =  '${request.contextPath}/exammanage/examReport/examArrangeList/page?examId=${examId!}';
		$("#showTabDiv").load(url);
	}else{
	
	}
}


</script>

