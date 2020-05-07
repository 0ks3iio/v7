<form id="myItem">
				<div class="form-horizontal itemDetail">
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right" for="orderId">序号:</label>
						<div class="col-sm-9">
							<input type="hidden" name="id" value="${item.id!}">
							<input type="hidden" name="schoolId" value="${item.schoolId!}">
							<input type="text" vtype ="int" min="1" max="99" class="form-control" nullable="false" name="orderId"  id="orderId" value="${item.orderId!}"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right" for="itemName">考核项名称:</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" nullable="false" maxLength="30" id="itemName" name="itemName" value="${item.itemName!}"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">类别:</label>
						<div class="col-sm-9">
							<label class="pos-rel">
								<input type="radio" name="type" class="wp form-control form-radio" value="1" <#if item.type == 1>checked</#if>/>
								<span class="lbl">卫生</span>
							</label>
							<label class="pos-rel">
								<input type="radio" name="type" class="wp form-control form-radio" value="2"  <#if item.type != 1>checked</#if>/>
								<span class="lbl">纪律</span>
							</label>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right ">是否有总分值:</label>
						<div class="col-sm-9">
							<label class="pos-rel">
								<input type="radio" name="hasTotalScore" class="wp form-control form-radio" value="1" <#if item.hasTotalScore == 1>checked</#if> onclick="hasScore('1')"/>
								<span class="lbl hasTotalScore">是</span>
							</label>
							<label class="pos-rel">
								<input type="radio" name="hasTotalScore" class="wp form-control form-radio" value="0" <#if item.hasTotalScore == 0>checked</#if> onclick="hasScore('0')"/>
								<span class="lbl hasTotalScore">否</span>
							</label>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right" for="totalScore">总分值:</label>
						<div class="col-sm-9">
							<input type="text" vtype="int" nullable="false" max="999" min="0" class="form-control" name="totalScore" id="totalScore" <#if item.hasTotalScore == 0> disabled="disabled" </#if>value="${item.totalScore}">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">可录入角色:</label>
						<div class="col-sm-9">
						<#list rolelist as r>
							<label><input name="roleIds"  value="${r.roleType!}" <#if r.hasSelect == '1'>checked</#if> type="checkbox" class="wp"><span class="lbl"> ${r.roleName!}</span></label>
						</#list>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label no-padding-right">考核时间:</label>
						<div class="col-sm-9">
							<#list daylist as d>
							<label><input name="dayIds" value="${d.day}" <#if d.hasSelect == '1'>checked</#if> type="checkbox" class="wp"><span class="lbl"> ${d.dayName!}</span></label>
							</#list>
						</div>
					</div>
				</div>
</form>
<#-- 确定和取消按钮 -->
<div class="layer-footer">
	<button class="btn btn-lightblue" id="item-commit">确定</button>
	<button class="btn btn-grey" id="item-close">取消</button>
</div>
<!-- /.row -->
<!-- page specific plugin scripts -->
<!--[if lte IE 8]>
  <script src="${request.contextPath}/static/ace/js/excanvas.js"></script>
<![endif]-->

<script type="text/javascript">
// 取消按钮操作功能
$("#item-close").on("click", function(){
	doLayerOk("#item-commit", {
	redirect:function(){},
	window:function(){layer.closeAll()}
	});
 });
 
 function hasScore(hasTotal){
 	
 	if(hasTotal == 0){
 		$('#totalScore').attr("disabled","disabled");
 		$('#totalScore').attr("value",'0');
 	}else{
 		$('#totalScore').removeAttr("disabled");
 	}
 }
 
// 确定按钮操作功能
var isSubmit=false;
$("#item-commit").on("click", function(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	$(this).addClass("disabled");
	var check = checkValue('.itemDetail');
	if(!check){
	 	$(this).removeClass("disabled");
	 	isSubmit=false;
	 	return;
	}
	// 提交数据
	var ii = layer.load();
	var options = {
		url : '${request.contextPath}/stuwork/checkweek/itemEdit/save',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.closeAll();
				layerTipMsg(data.success,"成功",data.msg);
				$(".model-div").load("${request.contextPath}/stuwork/checkweek/itemEdit/page");
	 		}else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			$("#item-commit").removeClass("disabled");
	 			isSubmit=false;
			}
			layer.close(ii);
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){} 
	};
	$("#myItem").ajaxSubmit(options);
 });	
</script>