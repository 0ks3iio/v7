<form id="myform">
<input type="hidden" name="subjectIds" value="${gkGroupClass.subjectIds!}" />
<input type="hidden" name="stuIdStr" id="stuIdStr" value="${stuIdStr!}" />
<div class="row schedulingDetail filter">	
	<#if rounds.openClassType?default('') == '1'>
		<div class="filter-item block">
			<span class="filter-name">指定行政班：</span>
			<div class="filter-content">
				<select name="classId" id="classId" class="form-control" style="width:120px">
	            <#if clazzList?? && (clazzList?size>0)>
	                <option value="">不指定班级</option>
	                <#list clazzList as clazz>
	                <option value="${clazz.id!}">${clazz.classNameDynamic!clazz.className!}</option>
	                </#list>
	            <#else>
	                <option value="">无可用班级</option>
	            </#if> 
	            </select>
			</div>
		</div>
	</#if>
	<div class="filter-item block">
		<span class="filter-name">班级名称：</span>
		<div class="filter-content">
			   <input type="text" class="form-control" nullable="false"  name="groupName" id="groupName" value="${gkGroupClass.groupName!}" maxlength="20"> 
		</div>
	</div>
</div>
</form>
<#-- 确定和取消按钮 -->
<div class="layer-footer">
	<button class="btn btn-lightblue" id="scheduling-commit">确定</button>
	<button class="btn btn-grey" id="scheduling-close">取消</button>
</div>
<!-- /.row -->
<!-- page specific plugin scripts -->
<!--[if lte IE 8]>
  <script src="${request.contextPath}/static/ace/js/excanvas.js"></script>
<![endif]-->

<script type="text/javascript">

// 取消按钮操作功能
$("#scheduling-close").on("click", function(){
	doLayerOk("#scheduling-commit", {
	redirect:function(){},
	window:function(){layer.closeAll()}
	});
 });
// 确定按钮操作功能
var isSubmit=false;
$("#scheduling-commit").on("click", function(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	$(this).addClass("disabled");
	var check = checkValue('.schedulingDetail');
	if(!check){
	 	$(this).removeClass("disabled");
	 	isSubmit=false;
	 	return;
	}
	// 提交数据
	var options = {
		url : '${request.contextPath}/gkelective/${roundsId}/openClassArrange/perArrange/schedulingEdit/save',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.closeAll();
				layerTipMsg(data.success,"成功",data.msg);
				selectGroup();
				if($("#stuIdStr").val() != ''){
					refHeadSelectDiv();
				}
				//scheduling('${gkGroupClass.subjectIds!}','');
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			$("#scheduling-commit").removeClass("disabled");
	 			isSubmit=false;
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){} 
	};
	$("#myform").ajaxSubmit(options);
 });	

</script>

