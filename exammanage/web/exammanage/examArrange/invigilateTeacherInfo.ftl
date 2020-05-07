<#import "/fw/macro/popupMacro.ftl" as popupMacro>
<form id="invigilateForm">
<div class="layer-body">
	<div class="filter clearfix">
		<input type="hidden" name="id" id="id" value="${examId!}">
		<div class="filter-item block">
			<label for="" class="filter-name">考场总数：</label>
			<span class="filter-name text-left">${placeSize!'0'}间</span>
		</div>
		<div class="filter-item block">
			<label for="" class="filter-name">考试科目：</label>
			<span class="filter-name text-left">${subjectSize!'0'}科</span>
		</div>
		<div class="filter-item block">
			<label for="" class="filter-name">每间考场需老师数：</label>
			<div class="filter-content">
				<input id="teacherSize" name="teacherSize" type="text" class="form-control" value="2" style="width:45px;" onBlur="changeSize()">
				<span class="lbl">位</span>
			</div>
		</div>
		<div class="filter-item block">
			<label for="" class="filter-name">各老师最多可监考：</label>
			<div class="filter-content">
				<input type="text" id="invigilateMax" name="invigilateMax" class="form-control" value="3" style="width:45px;" onBlur="changeSize()">
				<span class="lbl">场</span>
			</div>
		</div>
		<div class="filter-item block">
			<label for="" class="filter-name">最少需老师数：</label>
			<span id="maxTeachers" name="maxTeachers" class="filter-name text-left"></span>
		</div>
		<div class="filter-item block">
			<label for="" class="filter-name">在校老师：</label>
			<@popupMacro.selectMoreTeacher clickId="setTeacherName" id="setTeacherIds" name="setTeacherName" handler="teacherChange();">
				<div class="input-group">
					<input type="hidden" id="setTeacherIds" name="setTeacherIds" value="">
					<input type="text" id="setTeacherName" class="form-control" style="width:400px" value="">
				</div>
			</@popupMacro.selectMoreTeacher>
		</div>
		<div class="filter-item block">
			<label for="" class="filter-name">校外老师：</label>
			<select multiple="multiple" name="setTeacherSelect" id="setTeacherSelect" class="form-control chosen-select chosen-results" data-placeholder="请选择老师"  style="width: 220px;" onChange="teacherChange()">
				<#if teacherOutLists?? && (teacherOutLists?size>0)>
					<#list teacherOutLists as item>
						<option value="${item.id!}" >${item.teacherName?default('')}</option>
					</#list>
				<#else>
					<option value="" disabled="disabled">暂无数据</option>
				</#if>
			</select>  
		</div>
		<p class="no-margin text-center"><em id="selectSize">已选0位</em></p>
	</div>
</div>	
</form>
<div class="layer-footer">
	<a href="javascript:" class="btn btn-lightblue" id="arrange-commit">确定</a>
	<a href="javascript:" class="btn btn-grey" id="arrange-close">取消</a>
</div>
<script>

$('#setTeacherSelect').chosen({
	width:'400px',
	results_height:'200px',
	multi_container_height:'38px',
	no_results_text:"未找到",//无搜索结果时显示的文本
	search_contains:true,//模糊匹配，false是默认从第一个匹配
	//max_selected_options:1 //当select为多选时，最多选择个数
});	

$(function(){
	$('.chosen-results').css({height: '100px',overflow:'auto'});
	changeSize();
});
	
function changeSize() {
	var placeSize = "${placeSize!'0'}";
	var subjectSize = "${subjectSize!'0'}";
	var teacherSize = $('#teacherSize').val();
	var invigilateMax = $('#invigilateMax').val();
	if (teacherSize == "" || invigilateMax == "" || invigilateMax == '0' || isNaN(teacherSize) || isNaN(invigilateMax)) {
		$('#maxTeachers').text(0+'位');
		return;
	}
	placeSize=parseInt(placeSize);
	invigilateMax=parseInt(invigilateMax);
	subjectSize=parseInt(subjectSize);
	teacherSize=parseInt(teacherSize);
	if (invigilateMax > subjectSize) {
		invigilateMax = subjectSize;
	}
	var size = Math.ceil(placeSize*subjectSize*teacherSize/invigilateMax);
	$('#maxTeachers').text(size+'位');
}

function teacherChange() {
    var teacherinSize = 0;
    if ($("#setTeacherIds").val().split(",") != "") {
    	teacherinSize = $("#setTeacherIds").val().split(",").length;
    }
	var teacherSize = $('#setTeacherSelect :selected').length;
	var allSize = teacherinSize + teacherSize;
	$('#selectSize').text('已选'+allSize+'位');
}

var isSubmit=false;
$("#arrange-commit").on("click", function(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	var placeSize = "${placeSize!'0'}";
	var subjectSize = "${subjectSize!'0'}";
	var teacherSize = $('#teacherSize').val();
	var invigilateMax = $('#invigilateMax').val();
	if (teacherSize == "" || isNaN(teacherSize)) {
		layerTipMsg(false,"提示","请正确填写每间考场需老师数！");
		isSubmit=false;
		return;
	}
	if (invigilateMax == "" || invigilateMax == '0' || isNaN(invigilateMax)) {
		layerTipMsg(false,"提示","请正确填写各老师最多可监考场次！");
		isSubmit=false;
		return;
	}
	placeSize=parseInt(placeSize);
	invigilateMax=parseInt(invigilateMax);
	subjectSize=parseInt(subjectSize);
	teacherSize=parseInt(teacherSize);
	if (invigilateMax > subjectSize) {
		invigilateMax = subjectSize;
	}
	var maxTeachers = Math.ceil(placeSize*subjectSize*teacherSize/invigilateMax);
	if (placeSize == 0) {
		layerTipMsg(false,"提示","请先维护考场！");
		isSubmit=false;
		return;
	}
	if (subjectSize == 0) {
		layerTipMsg(false,"提示","请先维护考试科目！");
		isSubmit=false;
		return;
	}
	if (teacherSize<1) {
		layerTipMsg(false,"提示","每间考场需老师数量不得少于1位！");
		isSubmit=false;
		return;
	}
	if (invigilateMax<1) {
		layerTipMsg(false,"提示","各老师最多可监考场次不得少于1场！");
		isSubmit=false;
		return;
	}
    if($("#setTeacherSelect").val() == null && $('#setTeacherIds').val() == ""){
		layerTipMsg(false,"提示","监考老师不能为空！");
		isSubmit=false;
		return;
	}
	var teacherinSize = 0;
    if ($("#setTeacherIds").val().split(",") != "") {
    	teacherinSize = $("#setTeacherIds").val().split(",").length;
    }
	var teacherSize = $('#setTeacherSelect :selected').length;
	var allSize = teacherinSize + teacherSize;
	if (allSize<maxTeachers) {
		layerTipMsg(false,"提示","已选老师数量不足！");
		isSubmit=false;
		return;
	}
	$("#arrange-commit").addClass("disabled");
	var teacherInIds = $("#setTeacherIds").val();
	var teacherOutIds = $("#setTeacherSelect").val();
	if(teacherOutIds == null){
		teacherOutIds="";
	}
    var str = "?examId=${examId!}&teacherInIds="+teacherInIds+"&teacherOutIds="+teacherOutIds;
	// 提交数据
	var options = {
		url : '${request.contextPath}/exammanage/examArrange/invigilateBatchSave'+str,
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.closeAll();
				layerTipMsg(data.success,"成功",data.msg);
				var url =  '${request.contextPath}/exammanage/examArrange/invigilateTeacherIndex/page?examId=${examId!}&status=2&subjectId=all';
				$("#showTabDiv").load(url);
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			$("#arrange-commit").removeClass("disabled");
	 			isSubmit=false;
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#invigilateForm").ajaxSubmit(options);
});


// 取消按钮操作功能
$("#arrange-close").on("click", function(){
    doLayerOk("#arrange-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
 });
</script>