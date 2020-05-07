<#import "/fw/macro/treemacro.ftl" as treemacro>
<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<link rel="stylesheet" href="${request.contextPath}/static/css/pages.css"/>  
<!-- ztree -->
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css" />
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<#if blackTable! == '1'>
<a href="javascript:" class="page-back-btn gotoRoleUserIndex" onclick="blackTable()"><i class="fa fa-arrow-left"></i> 返回</a>
<#elseif blackAdmin! == '1'>
<a href="javascript:" class="page-back-btn gotoRoleUserIndex" onclick="blackAdmin()"><i class="fa fa-arrow-left"></i> 返回</a>
</#if>
<form id="myform">
		<div class="itemDetail">
			<div class="box box-default">
				<div class="box-header">
					<h4 class="box-title">${acadyear!}学年第${semester!}学期 第${week}周</h4>
				</div>
				<div class="box-body">
					<div class="filter-container">
						<div class="filter">
						<#if roleType == '02'>
							<div class="filter-item">
								<span class="filter-name">日期：</span>
								<div class="filter-content">
									<input type = "hidden" value="${dutyDate!}" name="dutyDate" id="dutyDate"/>
									<p>${dutyDate!}</p>
								</div>
							</div>
						<#else>
							<div class="filter-item">
								<span class="filter-name">日期：</span>
								<div class="filter-content">
									<div class="input-group">
										<input class="form-control date-picker startTime-date date-picker-time" vtype="data" style="width: 146px" type="text" nullable="false" name="dutyDate" id="dutyDate" placeholder="考核时间" value="${dutyDate!}" onchange="onDate()">
										<span class="input-group-addon">
											<i class="fa fa-calendar bigger-110"></i>
										</span>
									</div>
								</div>
							</div>
						</#if>
						<#if hasSubmit == '1'>
							<div class="filter-item filter-item-right"  id="submitAll">
								<button class="btn btn-blue" type="button" onclick="submitAll();">全部提交</button>
							</div>
							</#if>
						</div>
					</div>
					<p>规则：如有班级扣分，请录入个别扣分项，未扣分的不用填写</p>
					<p>提交：当天所有班级的扣分情况录入完毕后，点击右上角“全部提交”按钮提交扣分数据</p>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-3">
					<div class="box box-default">
							<div class="box-header">
								<h3 class="box-title">班级菜单</h3>
							</div>
							 <!-- 树菜单开始 -->
		                    <div class="box-body">
		                        <@treemacro.gradeClassForSchoolInsetTree height="500" click="onTreeClick" parameter="sections=${sections!}&gradeCodes=${grades!}"/>
		                    </div><!-- 树菜单结束 -->
					</div>
				</div>
				<div class="col-sm-9" id="checkInfo">
				</div>
			</div>
		</div>
</form>
<script type="text/javascript">

function blackAdmin(){
	$(".model-div").load("${request.contextPath}/stuwork/checkweek/checkEdit/page?blackAdmin=${blackAdmin!}&blackTable=${blackTable!}");
}
function blackTable(){
	$(".model-div").load("${request.contextPath}/stuwork/checkweek/checkTable/page?blackAdmin=${blackAdmin!}&blackTable=${blackTable!}&roleType=${roleType!}");
}
$(function(){
	//初始化日期控件
	var viewContent={
		'format' : 'yyyy-mm-dd',
		'minView' : '2',
		'startDate' : '${beginDate?string('yyyy-MM-dd')}',
		<#if userEnd>
		'endDate' : '${endDate?string('yyyy-MM-dd')}',
		<#else>
		'endDate' : '${.now?string('yyyy-MM-dd')}',
		</#if>
	};
	initCalendarData("#myform",".date-picker",viewContent);
});
function onDate(){
	var dutyDate = $('#dutyDate').val();
	if(dutyDate && dutyDate != ''){
		$(".model-div").load("${request.contextPath}/stuwork/checkweek/checkList/page?sections=${sections!}&gradeCodes=${grades!}&blackAdmin=${blackAdmin!}&blackTable=${blackTable!}&roleType=${roleType!}&beginDate=${beginDate?string('yyyy-MM-dd')!}&endDate=${endDate?string('yyyy-MM-dd')}&dutyDate="+dutyDate);
	}
}
var clsId = '';
function onTreeClick(event, treeId, treeNode, clickFlag){
  var id = treeNode.id;
  clsId = id;
  var dutyDate = $('#dutyDate').val();
  if(treeNode.type == "class"){
  	$("#checkInfo").load("${request.contextPath}/stuwork/checkweek/checkInfo/page?roleType=${roleType!}&dutyDate="+dutyDate+"&classId="+id);
  }
}

var isSubmit = false;
function submitAll(){
	if(isSubmit){
		return;
	}
	$.ajax({
		url:"${request.contextPath}/stuwork/checkweek/itemListEdit/saveSubmit",
		data: {'roleType':'${roleType!}','dutyDate':'${dutyDate}','acadyear':'${acadyear}','semester':'${semester}'},  
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
				if(jsonO.success){
					layerTipMsg(jsonO.success,"成功",jsonO.msg);
					<#if roleType != "01">
					$('#submitAll').hide();
					</#if>
				}else{
					layerTipMsg(jsonO.success,"失败",jsonO.msg);
				}
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {
 			
		}
	});
}

var isSave=false;
function save(){
	if(isSave){
		return;
	}
	isSave=true;
	$("#saveFrom").addClass("disabled");
	var check = checkValue('.itemDetail');
	if(!check){
	 	$("#saveFrom").removeClass("disabled");
	 	isSave=false;
	 	return;
	}
	// 提交数据
	var options = {
		url : '${request.contextPath}/stuwork/checkweek/itemListEdit/saveResult',
		dataType : 'json',
		success : function(data){
			//var jsonO = JSON.parse(data);
				//alert(data);
				if(data.success){
					layer.closeAll();
					layerTipMsg(data.success,"成功",data.msg);
				  var dutyDate = $('#dutyDate').val();
				  if(clsId != ""){
				  	$("#checkInfo").load("${request.contextPath}/stuwork/checkweek/checkInfo/page?roleType=${roleType!}&dutyDate="+dutyDate+"&classId="+clsId);
				  }
				}else{
					layerTipMsg(data.success,"失败",data.msg);
				}
	          isSave=false;
	          $("#saveFrom").removeClass("disabled");
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){} 
	};
	$("#myform").ajaxSubmit(options);
}
</script>