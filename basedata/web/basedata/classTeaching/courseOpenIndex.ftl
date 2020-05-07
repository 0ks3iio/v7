<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
			<li <#if tabIndex?default('1')=='1'>class="active"</#if> role="presentation"><a href="javascript:void(0)" role="tab" data-toggle="tab" onclick="itemShowList(1,true)">年级课程开设</a></li>
			<li role="presentation" <#if tabIndex?default('1')=='2'>class="active"</#if>><a href="javascript:void(0)" role="tab" data-toggle="tab" onclick="itemShowList(2,true)">班级课程开设</a></li>
			<li role="presentation" <#if tabIndex?default('1')=='3'>class="active"</#if>><a href="javascript:void(0)" role="tab" data-toggle="tab" onclick="itemShowList(3,true)">行政班任课老师设置</a></li>
		
		</ul>
		<div class="tab-content" id="itemShowDiv">
		
		</div>
	</div>
</div>


<script>
$(function(){
	itemShowList(${tabIndex?default(1)});
});
	
function itemShowList(tabType,isClick){
	var url = '';
	var param = '';
	if(!isClick){//如果是从其他页面跳转过来而不是点击切换tab，则添加参数
		param = '?acadyear=${acadyear!}&semester=${semester!}&gradeId=${gradeId!}';
	}
	if(tabType == 1){
		url =  '${request.contextPath}/basedata/courseopen/grade/index/page'+param;
	}else if(tabType == 2){
		url =  '${request.contextPath}/basedata/courseopen/class/index/page'+param;
	}else if(tabType == 3){
		url =  '${request.contextPath}/basedata/courseopen/teach/setting/index/page'+param;
	}
    $("#itemShowDiv").load(url);
}
</script>