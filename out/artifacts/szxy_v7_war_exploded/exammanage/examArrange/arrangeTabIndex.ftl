
<div class="box box-default">
	<div class="box-body clearfix">
		<div class="tab-container">
			<div class="tab-header clearfix">
				<ul class="nav nav-tabs">
					<li <#if type?default("1")=="1">class="active" </#if>>
						<a href="#aa"  id="aa" data-toggle="tab" aria-expanded="true" onclick="itemShowList(1)">不排考学生设置</a>
					</li>
					<li  <#if type?default("1")=="7">class="active" </#if>>
						<a href="#ff"  id="ff" data-toggle="tab" aria-expanded="true" onclick="itemShowList(7)">考号设置</a>
					</li>
					<#if isgk?default("0") == "1">
					<li  <#if type?default("1")=="2">class="active" </#if>>
						<a href="#bb"  id="bb" data-toggle="tab" aria-expanded="true" onclick="itemShowList(2)">考试场地设置</a>
					</li>
					<li  <#if type?default("1")=="8">class="active" </#if>>
						<a href="#hh"  id="hh" data-toggle="tab" aria-expanded="true" onclick="itemShowList(8)">考场分配设置</a>
					</li>
					<#else>
					<li  <#if type?default("1")=="2">class="active" </#if>>
						<a href="#bb"  id="bb" data-toggle="tab" aria-expanded="true" onclick="itemShowList(2)">考试场地设置</a>
					</li>
					</#if>
					
					<li  <#if type?default("1")=="3">class="active" </#if>>
						<a href="#cc"  id="cc" data-toggle="tab" aria-expanded="true" onclick="itemShowList(3)">考场考生设置</a>
					</li>
				</ul>
			</div>
			<div class="tab-content" id="showTabDiv">
			</div>
		</div>
	</div>
</div>
										

<!-- page specific plugin scripts -->
<script type="text/javascript">
function gobackIndex(){
    var url =  '${request.contextPath}/exammanage/examArrange/head/page';

    $("#examArrangeDiv").load(url);
	//$(".model-div").load(url);
};
$(function(){
	itemShowList('${type?default("1")}');
	<#--$('.gotoExamArrangeClass').on('click',function(){-->
		<#--var url =  '${request.contextPath}/exammanage/examArrange/head/page';-->
		<#--$("#examArrangeDiv").load(url);-->
	<#--});-->

    showBreadBack(gobackIndex,true,"编排设置");
});
function itemShowList(type){
	if("1"==type){
		var url =  '${request.contextPath}/exammanage/examArrange/filterIndex/page?examId=${examId!}';
		$("#showTabDiv").load(url);
	}else if("2"==type){
		var url =  '${request.contextPath}/exammanage/examArrange/placeIndex/page?examId=${examId!}';
		$("#showTabDiv").load(url);
	}else if("3"==type){
		var url =  '${request.contextPath}/exammanage/examArrange/placeStudentIndex/page?examId=${examId!}';
		$("#showTabDiv").load(url);
	}else if("4"==type){
		var url =  '${request.contextPath}/exammanage/examArrange/inspectorsTeacher/page?examId=${examId!}';
		$("#showTabDiv").load(url);
	}else if("5"==type){
		var str = "&status=2&subjectId=all";
		var url =  '${request.contextPath}/exammanage/examArrange/invigilateTeacherIndex/page?examId=${examId!}'+str;
		$("#showTabDiv").load(url);
	}else if("6"==type){
		var url =  '${request.contextPath}/exammanage/examArrange/outTeacherIndex/page?examId=${examId!}';
		$("#showTabDiv").load(url);
	}else if("7"==type){
		var url =  '${request.contextPath}/exammanage/examArrange/examineeNumberIndex/page?examId=${examId!}';
		$("#showTabDiv").load(url);
	}else if("8" == type){
		var url =  '${request.contextPath}/exammanage/examArrange/placeArrange/page?examId=${examId!}';
		$("#showTabDiv").load(url);
	}
}


</script>

