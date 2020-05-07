<div class="filter filter-f16" id="invigilateDiv">
	<div class="filter-item">
		<span class="filter-name">起始时间：</span>
		<div class="filter-content">
			<div class="input-group float-left">
				<input class="form-control datetimepicker" vtype="data" type="text" style="width: 150px" name="startTime" id="startTime" value="${(startTime?string('yyyy-MM-dd'))?if_exists}" changeDate="showInvigilateList()">
				<span class="input-group-addon">
					<i class="fa fa-calendar bigger-110"></i>
				</span>
			</div>
			<span class="float-left mr10 ml10"> 至 </span>
			<div class="input-group float-left">
				<input class="form-control datetimepicker" vtype="data" type="text" style="width: 150px" name="endTime" id="endTime" value="${(endTime?string('yyyy-MM-dd'))?if_exists}" changeDate="showInvigilateList()">
				<span class="input-group-addon">
					<i class="fa fa-calendar bigger-110"></i>
				</span>
			</div>
		</div>
	</div>
    <div class="filter-item ">
        <div class="filter-content">
            <div class="input-group input-group-search">
                <span class="filter-name">教师姓名：</span>
                <div class="pos-rel pull-left">
                    <input type="text" name="queryTeacherName" id="queryTeacherName" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="请输入老师姓名" onkeydown="dispResInvigilate()">
                </div>
                <div class="input-group-btn">
                    <button type="button" class="btn btn-default" onclick="findByInvigilateTeacherName()">
                        <i class="fa fa-search"></i>
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="invigilateList">
</div>
<script>
$(function(){
    $('.datetimepicker').datetimepicker4({
        format: 'YYYY-MM-DD',
        sideBySide: true,
        locale: moment.locale('zh-cn'),
        dayViewHeaderFormat: 'YYYY MMMM',
        useCurrent: false,
    }).next().on('click', function(){
        $(this).prev().focus();
        showInvigilateList();
    })
	//初始化多选控件
	initChosenMore("#invigilateDiv");
	showInvigilateList();
});
function dispResInvigilate(){
	var x;
    if(window.event) // IE8 以及更早版本
    {	x=event.keyCode;
    }else if(event.which) // IE9/Firefox/Chrome/Opera/Safari
    {
        x=event.which;
    }
    if(13==x){
        findByInvigilateTeacherName();
    }
}
function showInvigilateList(){
	var startTime = $("#startTime").val();
    var endTime = $("#endTime").val();
	var queryTeacherName=$("#queryTeacherName").val();
	var str = "?queryTeacherName=" + queryTeacherName + "&startTime=" + startTime + "&endTime=" + endTime;
	var url =  '${request.contextPath}/exammanage/teacherStat/invigilateArrangeList/page'+str;
	$("#invigilateList").load(url);
}


function findByInvigilateTeacherName(){
	var startTime = $("#startTime").val();
    var endTime = $("#endTime").val();
	var queryTeacherName=$("#queryTeacherName").val();
	var str = "?queryTeacherName=" + queryTeacherName + "&startTime=" + startTime + "&endTime=" + endTime;
//	var ss="请先输入要查询的老师姓名";
//	if(queryTeacherName==""){
//		layerTipMsg(false,"提示",ss+"！");
//		return;
//	}
	var url =  '${request.contextPath}/exammanage/teacherStat/invigilateArrangeList/page'+str;
	url=encodeURI(encodeURI(url));
	$("#invigilateList").load(url);
}
</script>