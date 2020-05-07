<link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap-datepicker/dist/css/bootstrap-datepicker.min.css">
<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs" role="tablist">
			<li class="active" role="presentation"><a href="#aa" role="tab" data-toggle="tab" onclick="showMyclassAttList('1')">我的课次签到</a></li>
			<li role="presentation"><a href="#aa" role="tab" data-toggle="tab" onclick="showMyclassAttList('2')">全校课次签到</a></li>
		</ul>
		<div class="tab-content">
			<input type="hidden" id="selectPageType" value='1'>
			<div id="aa" class="tab-pane active" role="tabpanel">

				<div class="filter">
					<div class="filter-item">
						<span class="filter-name">日期：</span>
						<div class="filter-content">
							<div class="input-group">
								<input type="text" id="selectDate" class="form-control datepicker" readonly="readonly" onchange="changeSelect()" value="${(nowDate?default(.now))?string('yyyy-MM-dd')}">
								<span class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</span>
							</div>
						</div>
					</div>
					<div class="filter-item" id="periodDiv" style="display:none">
						<span class="filter-name">节次：</span>
						<div class="filter-content">
							<select name="" id="selectSection" class="form-control" onchange="changeSelect()">
							<#if periodMap?exists&&periodMap?size gt 0>
								<#list periodMap?keys as key> 
									<option value="${key}">${periodMap[key]!}</option>
								</#list>
							<#else>
								<option value="0">---请选择---</option>
			                </#if>
							</select>
						</div>
					</div>
					<div class="filter-item" id="gradeDiv" style="display:none">
						<span class="filter-name">年级：</span>
						<div class="filter-content">
							<select name="" id="gradeId" class="form-control" onchange="changeGrade()">
								<option value="">---请选择---</option>
								<#if grades?exists&&grades?size gt 0>
				                  	<#list grades as item>
									<option value="${item.id!}">${item.gradeName!}</option>
				              	    </#list>
				                </#if>
							</select>
						</div>
					</div>
					<div class="filter-item" id="classIdDiv" style="display:none">
						<span class="filter-name">班级：</span>
						<div class="filter-content">
							<select name="" id="classId" class="form-control" onChange="changeSelect()">
								<option value="">---请选择---</option>
							</select>
						</div>
					</div>
					<div id="showListDiv">
					</div>
					
				</div>
			</div>
		</div>
	</div>
</div>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<script src="${request.contextPath}/static/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>

<script type="text/javascript">
var isShowMyList = false;
$(function(){
	var maxDate = "${(nowDate?default(.now))?string('yyyy-MM-dd')}";
	$('.datepicker').datepicker({
		endDate:maxDate,
		language: 'zh-CN',
    	format: 'yyyy-mm-dd',
    	autoclose: true
    }).next().on('click', function(){
		$(this).prev().focus();
	});
});

function changeSelect(){
	var type = $("#selectPageType").val();
	showMyclassAttList(type)
}
function showMyclassAttList(type){
	if(isShowMyList){
		return;
	}
	var section = 0;
	var classId = "";
	if(type=='1'){
		$("#selectPageType").val('1');
		$("#periodDiv").hide();
		$("#gradeDiv").hide();
		$("#classIdDiv").hide();
	}else{
		$("#selectPageType").val('2');
		section = $("#selectSection").val();
		classId = $("#classId").val();
		$("#periodDiv").show();
		$("#gradeDiv").show();
		$("#classIdDiv").show();
	}
	var date = $("#selectDate").val();
	var url =  '${request.contextPath}/eclasscard/mycalss/signin/list?type='+type+'&section='+section+'&date='+date+'&classId='+classId;
	isShowMyList = true;
	$("#showListDiv").load(url,function(responseTxt,statusTxt,xhr){
		isShowMyList =false;
	});
}

function changeGrade(){
	var gradeId = $("#gradeId").val();
	$.ajax({
        url:"${request.contextPath}/eclasscard/get/class/page",
        data:{'gradeId':gradeId},
        dataType:'json',
        async: true,
        type:'POST',
        success: function(data) {
			var array = data;
			var htmlStr = '<option value="">---请选择---</option>';
			if(array.length > 0){
    			$.each(array, function(index, json){
    				htmlStr += '<option value="'+json.id+'">'+json.name+'</option>';
    			});
    		}
			$("#classId").html(htmlStr);
			$("#classId").val('');
			//showListDiv();
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
		}
    })

}


</script>