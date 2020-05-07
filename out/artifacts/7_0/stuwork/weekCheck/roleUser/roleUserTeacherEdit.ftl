<#import "/fw/macro/popupMacro.ftl" as popup />
<form id="myform">
<div class="layer-syncScore roundsDetail">
	<div class="filter">
	<#if sections?exists && sections?size gt 0>
	<#list sections as sec>
		<div class="filter-item block">
			<span class="filter-name">${mcodeSetting.getMcode("DM-RKXD","${sec?default('0')}")}：</span>
			<div class="filter-content">
			<#assign roleId = ''>
			<#if roles?exists && roles?size gt 0>
				<#list roles as ro>
					<#if ro.section == sec>
					<#assign roleId = ro.userId>
					</#if>
				</#list>
			</#if>
			<#assign roleName = ''>
			<#if users?exists && users?size gt 0>
				<#list users as user>
					<#if user.id == roleId><#assign roleName=user.realName!></#if>
				</#list>
			</#if>
				<input type="hidden" id="roleDto.roles[${sec_index}].section" name="roles[${sec_index}].section" value="${sec!}"/>
				<@popup.selectOneUser clickId="userName${sec_index}" id="userId${sec_index}" name="userName${sec_index}">
					<div class="col-sm-6">
						<div class="input-group">
							<input type="hidden" id="userId${sec_index}" name="roles[${sec_index}].userId" value="${roleId!}">
							<input type="text" id="userName${sec_index}"  class="form-control" value="${roleName!}">
							<a class="input-group-addon" href="javascript:void(0);"></a>
						</div>
					</div>
				</@popup.selectOneUser>
			</div>
		</div>
		</#list>
		</#if>
	</div>
</div>
<input type="hidden" id="dutyDate" name="dutyDate" value="${dutyDate!}"/>
</form>
<#--
<div style="display: none;">
	<@popup.selectOneUser clickId="userName" id="userId" name="userName" handler="change2()">
		<input type="text" id="userName" value=""/>
		<input type="hidden" id="userId" value=""/>
	</@popup.selectOneUser>
</div>-->

<#-- 确定和取消按钮 -->
<div class="layer-footer">
	<button class="btn btn-lightblue" id="rounds-commit">确定</button>
	<button class="btn btn-grey" id="rounds-close">取消</button>
</div>
<!-- /.row -->
<!-- page specific plugin scripts -->
<!--[if lte IE 8]>
  <script src="${request.contextPath}/static/ace/js/excanvas.js"></script>
<![endif]-->

<script type="text/javascript">
<#--
var index = '';
function change2(){
	var roleId = $("#userId").val();
	var roleName = $("#userName").val();
	$('#userName_'+index).attr('value',roleName);
	$('#userId_'+index).attr('value',roleId);
}
function change1(sec){
	index = sec;
	var roleId = $('#userId_'+sec).val();
	var roleName = $('#userName_'+sec).val();
	$("#userName").attr('value',roleName);
	$("#userId").attr('value',roleId);
	$("#userName").click();
}-->
	$(function(){
		//初始化控件
		var viewContent1={
			'allow_single_deselect':'false',//是否可清除，第一个option的text必须为空才能使用
			'select_readonly':'false',//是否只读
			'width' : '220px',//输入框的宽度
			'results_height' : '200px'//下拉选择的高度
		}
		initChosenOne("#planForm","",viewContent1);
	});

// 取消按钮操作功能
$("#rounds-close").on("click", function(){
	doLayerOk("#rounds-commit", {
	redirect:function(){},
	window:function(){layer.closeAll()}
	});
 });
// 确定按钮操作功能
var isSubmit=false;
$("#rounds-commit").on("click", function(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	$(this).addClass("disabled");
	var check = checkValue('.roundsDetail');
	if(!check){
	 	$(this).removeClass("disabled");
	 	isSubmit=false;
	 	return;
	}
	// 提交数据
	var ii = layer.load();
	var options = {
		url : '${request.contextPath}/stuwork/checkweek/roleUserTeacher/save',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.closeAll();
				layerTipMsg(data.success,"成功",data.msg);
				$(".model-div").load("${request.contextPath}/stuwork/checkweek/roleUserTeacher/page?roleType=02");
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			$("#rounds-commit").removeClass("disabled");
	 			isSubmit=false;
			}
			layer.close(ii);
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){} 
	};
	$("#myform").ajaxSubmit(options);
 });	
</script>
