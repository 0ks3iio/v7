<div class="box-body">
	<div class="filter">
		<div class="filter-item">
			<span class="filter-name">年级：</span>
			<div class="filter-content">
				<select name="" id="gradeId" class="form-control" onChange="changeGrade()">
					<option value="">---请选择---</option>
					<#if grades?exists&&grades?size gt 0>
	                  	<#list grades as item>
						<option value="${item.id!}">${item.gradeName!}</option>
	              	    </#list>
	                </#if>
				</select>
			</div>
		</div>
		<div class="filter-item">
			<span class="filter-name">班级：</span>
			<div class="filter-content">
				<select name="" id="classId" class="form-control" onChange="showListDiv()">
					<option value="">---请选择---</option>
				</select>
			</div>
		</div>
		<div class="filter-item">
			<a href="javascript:void(0);" onclick="doExportClass()" class="btn btn-blue">批量导出</a>
		</div>
		<div class="filter-item filter-item-right">
			<span id="statisticsTime"><#if qualityScore?exists>上次统计时间：${qualityScore.statisticalTime?string('yyyy-MM-dd HH:mm')}<#else>请统计总分</#if>&nbsp;&nbsp;</span>
			<a href="javascript:void(0);" onclick="statisticsScore()" class="btn btn-blue">统计</a>
		</div>
	</div>
	<div id="showListDiv">
	</div>
</div>
<script src="${request.contextPath}/static/js/login/jquery.cookies.js"></script>
<script type="text/javascript">
$(function(){
	showListDiv();
});
function showListDiv(){
	var gradeId = $("#gradeId").val();
	var classId = $("#classId").val();
	var url =  '${request.contextPath}/quality/sum/list?classId='+classId+'&gradeId='+gradeId;
	$("#showListDiv").load(url);
}
function changeGrade(){
	var gradeId = $("#gradeId").val();
	if(gradeId==""){
		var htmlStr = '<option value="">---请选择---</option>';
		$("#classId").html(htmlStr);
		$("#classId").val('');
		showListDiv();
	}else{
		$.ajax({
			url: "${request.contextPath}/quality/sum/get/class/page",
			data: {'gradeId': gradeId},
			dataType: 'json',
			async: true,
			type: 'POST',
			success: function (data) {
				var array = data;
				var htmlStr = '<option value="">全部</option>';
				if (array.length > 0) {
					$.each(array, function (index, json) {
						htmlStr += '<option value="' + json.id + '">' + json.name + '</option>';
					});
				}
				$("#classId").html(htmlStr);
				$("#classId").val('');
				showListDiv();
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
			}
		})
    }

}
var isSubmit = false;
function  statisticsScore(){
	var ii = layer.load();
	isSubmit = true;
	  $.ajax({
	        url:"${request.contextPath}/quality/sum/statistics",
	        data:{},
	        dataType:'json',
	        contentType:'application/json',
	        type:'POST',
	        success:function (data) {
	        	layer.close(ii);
	            if(data.success){
	            	showListDiv();
	            	$("#statisticsTime").html("上次统计时间："+new Date().format('yyyy-MM-dd hh:mm'));
	                layerTipMsg(data.success,data.msg,"");
	            }else{
	                layerTipMsg(data.success,data.msg,"");
	            }
	            isSubmit = false;
	        }
	    });
}

function doExportClass(){
	var classId = $("#classId").val();
	if(classId==''){
		layer.alert('请先选择班级！',{icon:7});
		return;
	}
	var ii = layer.load();
	var downId=new Date().getTime();//以时间戳来区分每次下载
	document.location.href = "${request.contextPath}/quality/sum/class/pdfExport?classId="+classId+"&downId="+downId;
	var interval=setInterval(function(){
		var down=$.cookies.get("D"+downId);
		if(down==downId){
			layer.close(ii);
			$.cookies.del("D"+downId);
		}
	},1000);
}
</script>