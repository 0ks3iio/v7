<div class="box box-default">
	<div class="box-body clearfix">
		<div class="tab-container">
			<div class="tab-header clearfix">
				<ul class="nav nav-tabs">
					<li  <#if type?default("4")=="4">class="active" </#if>>
						<a href="#dd" id="dd" data-toggle="tab" aria-expanded="true" onclick="itemShowList(4)">巡考老师设置</a>
					</li>
					<li  <#if type?default("4")=="5">class="active" </#if>>
						<a href="#ee"  id="ee" data-toggle="tab" aria-expanded="true" onclick="itemShowList(5)">监考老师设置</a>
					</li>
					<li  <#if type?default("4")=="6">class="active" </#if>>
						<a href="#gg"  id="gg" data-toggle="tab" aria-expanded="true" onclick="itemShowList(6)">校外老师设置</a>
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
        var url =  '${request.contextPath}/exammanage/examTeacher/head/page';
        $("#examTeacherDiv").load(url);
        //$(".model-div").load(url);
    };
    $(function(){
        itemShowList('${type?default("4")}');
        showBreadBack(gobackIndex,true,"监巡考设置");
    });
function itemShowList(type){
	if("4"==type){
		var url =  '${request.contextPath}/exammanage/examTeacher/inspectorsTeacher/page?examId=${examId!}';
		$("#showTabDiv").load(url);
	}else if("5"==type){
		var str = "&status=2&subjectId=all";
		var url =  '${request.contextPath}/exammanage/examTeacher/invigilateTeacherIndex/page?examId=${examId!}'+str;
		$("#showTabDiv").load(url);
	}else if("6"==type){
		var url =  '${request.contextPath}/exammanage/examTeacher/outTeacherIndex/page?examId=${examId!}';
		$("#showTabDiv").load(url);
	}
}
</script>

