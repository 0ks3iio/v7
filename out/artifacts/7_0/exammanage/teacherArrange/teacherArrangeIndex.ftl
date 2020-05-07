<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.all.min.js"> </script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/components/layer/layer.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/components/moment/min/moment-with-locales.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/components/bootstrap-datetimepicker-4.17/js/bootstrap-datetimepicker.js"></script>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/default/css/ueditor.css"/>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/iframe.css"/>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/components/bootstrap-datetimepicker-4.17/css/bootstrap-datetimepicker.min.css">

<style>
    .datetimepicker {
        margin-top:auto;
    }
</style>
<div class="box box-default">
    <div class="box-body clearfix">
		<div class="tab-container">
			<div class="tab-header clearfix">
				<ul class="nav nav-tabs nav-tabs-1">
					<li <#if type?default("1")=="1">class="active" </#if>>
						<a data-toggle="tab" href="#aa" aria-expanded="true" onclick="itemShowArrange(1)">巡考老师统计</a>
					</li>
					<li <#if type?default("1")=="2">class="active" </#if>>
						<a data-toggle="tab" href="#bb" aria-expanded="false" onclick="itemShowArrange(2)">监考老师统计</a>
					</li>
				 </ul>
			</div>
			<!-- tab切换开始 -->
			<div class="tab-content" id="showTeacherArrangeTabDiv">

			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
$(function(){
	itemShowArrange('1');
});
function itemShowArrange(type){
	if("1"==type){
		var url =  '${request.contextPath}/exammanage/teacherStat/inspectorsArrangeIndex/page';
		$("#showTeacherArrangeTabDiv").load(url);
	}else {
		var url =  '${request.contextPath}/exammanage/teacherStat/invigilateArrangeIndex/page';
		$("#showTeacherArrangeTabDiv").load(url);
	}
}
</script>