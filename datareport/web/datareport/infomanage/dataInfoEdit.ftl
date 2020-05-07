<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.all.min.js"> </script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/default/css/ueditor.css"/>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/iframe.css"/>
<script>
//实例化编辑器
//建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
var ue = UE.getEditor('header',{
    //focus时自动清空初始化时的内容
    autoClearinitialContent:false,
    //关闭字数统计
    wordCount:false,
    //关闭elementPath
    elementPathEnabled:false,
    //关闭右键菜单
    enableContextMenu:false,
    //默认的编辑区域高度
    toolbars:[[
         'fullscreen', 'source', '|', 'undo', 'redo', '|', 'bold', 'fontsize', 
         'justifyleft', 'justifycenter', 'justifyright'
     	]],
    initialFrameHeight:100
    //更多其他参数，请参考ueditor.config.js中的配置项
});
</script>
<#import "/fw/macro/treemacro.ftl" as treemacro>
<#import "/fw/macro/popupMacro.ftl" as popupMacro>
	<form id="dataInfoForm">
	<div class="box box-default" id="infoDetailDiv">
		<div class="box-body">
			<div class="form-horizontal" role="form">
				<input type="hidden" id="infoId" name="dataReportInfo.id" value="${dataReportInfo.id!}" >
				<input type="hidden" id="unitId" name="dataReportInfo.unitId" value="${unitId!}" >
				<input type="hidden" id="ownerId" name="dataReportInfo.ownerId" value="${ownerId!}">
				<input type="hidden" id="needCheck" name="dataReportInfo.needCheck" value="0">
				<input type="hidden" id="isTimeSend" name="dataReportInfo.isTimeSend" value="0">
				<input type="hidden" id="state" name="dataReportInfo.state" value="1">
				<input type="hidden" id="structType" name="dataReportInfo.structType" value="1">
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding-right">问卷名称：</label>
					<div class="col-sm-8">
						<input type="text" id="infoTitle" name="dataReportInfo.title" maxlength="50" autocomplete="off" class="form-control" placeholder="问卷名称" value="${dataReportInfo.title!}" onkeyup="limitWords(this)">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding-right">问卷收集日期：</label>
					<div class="col-sm-2">
						<div class="input-group">
							<input class="form-control form datetimepicker" autocomplete="off" id="startTime" name="dataReportInfo.startTime" type="text" placeholder="开始时间" value="${dataReportInfo.startTime!}">
							<span class="input-group-addon">
								<i class="fa fa-calendar"></i>
							</span>
						</div>
					</div>
					<span class="float-left mt6">至</span>
					<div class="col-sm-2">
						<div class="input-group">
							<input class="form-control form datetimepicker" autocomplete="off" id="endTime" name="dataReportInfo.endTime" type="text" placeholder="结束时间" value="${dataReportInfo.endTime!}">
							<span class="input-group-addon">
								<i class="fa fa-calendar"></i>
							</span>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding-right">是否允许上传附件：</label>
					<div class="col-sm-8">
						<label>
							<input type="checkbox" class="wp wp-switch" <#if dataReportInfo.isAttachment?exists && dataReportInfo.isAttachment == 1>checked</#if> onClick="isnotEmpty(this)">
							<span class="lbl"></span>
							<input type="hidden" id="isAttachment" name="dataReportInfo.isAttachment" value="${dataReportInfo.isAttachment!(0)}">
						</label>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding-right">接收对象：</label>
					<#if unitClass == 1>
					<div class="col-sm-4">
						<div class="authorization-title clearfix">
							<b>所有单位</b>&nbsp;&nbsp;&nbsp;&nbsp;<b class="color-red">(可勾选也可点击单位名称)</b>
						</div>
						<div class="authorization-body">
							<@treemacro.unitForSubInsetTree height="400" notRelate=true checkEnable=true onCheck="chooseSch" click="chooseEdu"/>
						</div>
					</div>
					<div class="col-sm-4">
						<div class="authorization-title clearfix"><b>已选</b></div>
						<div class="authorization-body">
							<div style="height:400px; overflow:auto;" id="chooseUnitDiv">
								
							</div>
						</div>
					</div>
					<input type="hidden" id="objectType" name="objectType" value="1" >
					<input type="hidden" id="objectIds" name="objectIds">    
					<#else>
					<@popupMacro.selectMoreTeacher clickId="objectNames" id="objectIds" name="objectNames" handler="">
							<div class="col-sm-6">
								<div class="input-group">
									<input type="hidden" id="objectType" name="objectType" value="2">
									<input type="hidden" id="objectIds" name="objectIds" value="${objectIds!}">
									<input type="text" autocomplete="off" id="objectNames" class="form-control" value="${objectNames!}">
									<a class="input-group-addon" href="javascript:void(0);"></a>
								</div>
							</div>
						</@popupMacro.selectMoreTeacher>  
					</#if>
				</div>
			</div>
		</div>
	</div>
	<div class="box box-default" id="infoTableDiv" style="display:none">
		<div class="box-body">
			<div class="form-horizontal" role="form">
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding-right">表头：</label>
					<div class="col-sm-8">
						<textarea id="header" name="header" type="text/plain">${title!}</textarea>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding-right">表类型：</label>
					<div class="col-sm-6">
						<div class="select-img">
							<span class="item <#if !dataReportInfo.tableType?exists || dataReportInfo.tableType == 1>active</#if>" id="tableType1"><span class="img-outter"><img src="${request.contextPath}/static/images/data-provid/row.jpg"></span><br>列</span>
							<span class="item <#if dataReportInfo.tableType?exists && dataReportInfo.tableType == 2>active</#if>" id="tableType2"><span class="img-outter"><img src="${request.contextPath}/static/images/data-provid/col.jpg"></span><br>行</span>
							<span class="item <#if dataReportInfo.tableType?exists && dataReportInfo.tableType == 3>active</#if>" id="tableType3"><span class="img-outter"><img src="${request.contextPath}/static/images/data-provid/row-col.jpg"></span><br>行列表</span>
						</div>
					</div>
					<input type="hidden" id="tableType" name="dataReportInfo.tableType" value="${dataReportInfo.tableType!(1)}">
				</div>
				<div class="form-group" id="rowDiv" style="<#if dataReportInfo.tableType?exists && dataReportInfo.tableType == 2>display:none;<#else>display:block;</#if>">
					<label class="col-sm-2 control-label no-padding-right">列：</label>
					<div class="col-sm-8">
						<table class="table table-bordered table-striped table-hover no-margin">
							<thead>
								<tr>
									<th width="33%">列名称</th>
									<th width="33%">列属性</th>
									<th width="33%">数据操作</th>
									<th width="90">是否必填</th>
									<th width="50">操作</th>
								</tr>
							</thead>
							<tbody id="rowTbodyDiv">
								<#if rowColumns?exists&&rowColumns?size gt 0>
								<#list rowColumns as row>
								<tr>
									<td><input class="form-control rowColumn" type="text" id="rowColumns[${row_index}].columnName" name="rowColumns[${row_index}].columnName" maxlength="10" autocomplete="off" value="${row.columnName!}" onkeyup="limitWords(this)"></td>
								    <td>
								    	<select id="rowColumns[${row_index}].columnType" name="rowColumns[${row_index}].columnType" class="form-control" onChange="changeColumnType(this)">
											<option value="1" <#if row.columnType==1>selected</#if>>文本</option>
											<option value="2" <#if row.columnType==2>selected</#if>>数值</option>
											<option value="3" <#if row.columnType==3>selected</#if>>自增序列</option>
										</select>
								    </td>
								    <td>
								    	<select id="rowColumns[${row_index}].methodType" name="rowColumns[${row_index}].methodType" <#if row.columnType != 2>disabled="disabled"</#if> class="form-control disabled methodType">
											<option value="">--请选择--</option>
											<#if row.columnType==2>
												<option value="1" <#if row.methodType?exists&&row.methodType==1>selected</#if>>求平均</option>
												<option value="2" <#if row.methodType?exists&&row.methodType==2>selected</#if>>求总和</option>
											</#if>
										</select>
								    </td>
								    <td>
								    	<label>
											<input type="checkbox" class="wp wp-switch" <#if row.isNotnull == 1>checked</#if> onClick="isnotEmpty(this)">
											<span class="lbl"></span>
											<input class="isNotnull" type="hidden" id="rowColumns[${row_index}].isNotnull" name="rowColumns[${row_index}].isNotnull" value="${row.isNotnull!}" >
										</label>
								    </td>
								    <td>
								    	<a class="color-blue" href="#" onClick="deleteTr(this)">删除</a>
								    </td>
								</tr>
								</#list>
								</#if>
								<tr id="addRowTr">
								    <td colspan="5" class="text-center"><a class="color-blue" href="javascript:void(0)" onClick="addNewTr('row')">+ 新增</a></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<div class="form-group" id="rankDiv" style="<#if !dataReportInfo.tableType?exists || dataReportInfo.tableType?exists && dataReportInfo.tableType == 1>display:none;<#else>display:block;</#if>">
					<label class="col-sm-2 control-label no-padding-right">行：</label>
					<div class="col-sm-8">
						<table class="table table-bordered table-striped table-hover no-margin">
							<thead>
								<tr>
									<th width="33%">行名称</th>
									<th width="33%">行属性</th>
									<th width="33%">数据操作</th>
									<th width="90">是否必填</th>
									<th width="50">操作</th>
								</tr>
							</thead>
							<tbody id="rankTbodyDiv">
								<#if rankColumns?exists&&rankColumns?size gt 0>
								<#list rankColumns as rank>
								<tr>
									<td><input class="form-control rankColumn" type="text" id="rankColumns[${rank_index}].columnName" name="rankColumns[${rank_index}].columnName" maxlength="10" autocomplete="off" value="${rank.columnName!}" onkeyup="limitWords(this)"></td>
								    <#if dataReportInfo.tableType?exists && dataReportInfo.tableType == 2>
								    <td>
								    	<select id="rankColumns[${rank_index}].columnType" name="rankColumns[${rank_index}].columnType" class="form-control" onChange="changeColumnType(this)">
											<option value="1" <#if rank.columnType==1>selected</#if>>文本</option>
											<option value="2" <#if rank.columnType==2>selected</#if>>数值</option>
											<option value="3" <#if rank.columnType==3>selected</#if>>自增序列</option>
										</select>
								    </td>
								    <td>
								    	<select id="rankColumns[${rank_index}].methodType" name="rankColumns[${rank_index}].methodType" <#if rank.columnType != 2>disabled="disabled"</#if> class="form-control disabled methodType">
											<option value="">--请选择--</option>
											<#if rank.columnType==2>
												<option value="1" <#if rank.methodType?exists&&rank.methodType==1>selected</#if>>求平均</option>
												<option value="2" <#if rank.methodType?exists&&rank.methodType==2>selected</#if>>求总和</option>
											</#if>
										</select>
								    </td>
								    <td>
								    	<label>
											<input type="checkbox" class="wp wp-switch" <#if rank.isNotnull == 1>checked</#if> onClick="isnotEmpty(this)">
											<span class="lbl"></span>
											<input class="isNotnull" type="hidden" id="rankColumns[${rank_index}].isNotnull" name="rankColumns[${rank_index}].isNotnull" value="${rank.isNotnull!}" >
										</label>
								    </td>
								    <#else>
								    <td></td><td></td><td></td>
								    </#if>
								    <td>
								    	<a class="color-blue" href="#" onClick="deleteTr(this)">删除</a>
								    </td>
								</tr>
								</#list>
								</#if>
								<tr id="addRankTr">
								    <td colspan="5" class="text-center"><a class="color-blue" href="javascript:void(0)" onClick="addNewTr('rank')">+ 新增</a></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding-right">备注：</label>
					<div class="col-sm-8">
						<textarea id="remark" name="remark" placeholder="请填写备注（非必填）：" type="text/plain" style="width:100%;height:90px;">${remark!}</textarea>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label no-padding-right">预览：</label>
					<div class="col-sm-8" id="previewDiv">
						
					</div>
				</div>
			</div>
		</div>
	</div>
	</form>
	<div class="navbar-fixed-bottom opt-bottom">
        <span id="beforeDiv">
        <a class="btn btn-blue" href="#" onClick="nextBottom()">下一步</a>
        </span>
        <span id="nextDiv" style="display:none">
        <a class="btn btn-white" href="###" onClick="beforeBottom()">上一步</a>
        <a class="btn btn-white" href="###" onClick="previewExcel()">预览</a>
        <a class="btn btn-white" href="###" onClick="saveDataInfo('false')">保存</a>
        <a class="btn btn-blue" href="###" onClick="saveDataInfo('true')">发布</a>
        </span>
    </div>
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>
<script>
	
	var rowMaxIndex =${rowColumns?size}; 
	var rankMaxIndex = ${rankColumns?size};
	
	$(function(){
		if ($("#infoId").val().length>0) {
	    	showBreadBack(showInfoListHead,true,"编辑问卷");
		} else {
	    	showBreadBack(showInfoListHead,true,"新增问卷");
		}
		
		if(date_timepicker &&date_timepicker!=null){
			date_timepicker.remove();
		}
		setTimeout(function(){
			date_timepicker = $('.datetimepicker').datetimepicker({
				language: 'zh-CN',
				minView: 'month',
    			format: 'yyyy-mm-dd',
    			autoclose: true
	    	})
		},100);
	    
	    <#if unitClass == 1>
	    	var objIds = "${objectIds!}".split(",");
	    	var objNames = "${objectNames!}".split(",");
	    	if (objIds != "") {
	    		var zTree11=$.fn.zTree.getZTreeObj("unitForSubInsetTree");
				if(zTree11 == null){
	    			setTimeout(function(){
	       				zTree11=$.fn.zTree.getZTreeObj("unitForSubInsetTree");
	       				for (var i = 0; i < objIds.length; i++ ) {
                 			var treeNode2 = zTree11.getNodeByParam("id",objIds[i]);
                 			treeNode2.checked = true;
                 			zTree11.updateNode(treeNode2);
	       				}
	    			}, 1500);
				}
				var divHtml = '';
   				for (var i = 0; i < objIds.length; i++) {
       				divHtml = divHtml + '<div class="clearfix" id='+objIds[i]+'><span class="pull-left">'+objNames[i]+'</span><input type="hidden" class="objValue" value='+objIds[i]+'><a class="pull-right" href="#" onclick="delChoose(\''+objIds[i]+'\');"><i class="fa fa-times-circle color-ccc font-16"></i></a></div>';
   				}
				$('#chooseUnitDiv').append(divHtml);
	    	}
	    </#if>
	});
	
	<#-- 学校选择 -->
	function chooseSch(event, treeId, treeNode) {
		$('#chooseUnitDiv').html('');
	  	var html = '';
		var zTree11=$.fn.zTree.getZTreeObj("unitForSubInsetTree");
   		var nodes=zTree11.getCheckedNodes(true);
   		for (var i = 0; i < nodes.length; i++) {
       		html = html	+ '<div class="clearfix" id='+nodes[i].id+'><span class="pull-left">'+nodes[i].name+'</span><input type="hidden" class="objValue" value='+nodes[i].id+'><a class="pull-right" href="#" onclick="delChoose(\''+nodes[i].id+'\');"><i class="fa fa-times-circle color-ccc font-16"></i></a></div>';
   		}
   		$('#chooseUnitDiv').append(html);
	}
	
	<#-- 教育局选择 -->
	function chooseEdu(event, treeId, treeNode, clickFlag){
		var zTree11=$.fn.zTree.getZTreeObj("unitForSubInsetTree");
		var treeNode2 = zTree11.getNodeByParam("id",treeNode.id);
		if (treeNode2.checked == false) {
			treeNode2.checked = true;
			zTree11.updateNode(treeNode2);
			var html = '<div class="clearfix" id='+treeNode.id+'><span class="pull-left">'+treeNode.name+'</span><input type="hidden" class="objValue" value='+treeNode.id+'><a class="pull-right" href="#" onclick="delChoose(\''+treeNode.id+'\');"><i class="fa fa-times-circle color-ccc font-16"></i></a></div>';
			$('#chooseUnitDiv').append(html);
		} else {
			treeNode2.checked = false;
			zTree11.updateNode(treeNode2);
			$('#'+treeNode.id).remove();
		}
	}
	
	<#-- 删除已选的单位 -->
	function delChoose(id) {
		var zTree11=$.fn.zTree.getZTreeObj("unitForSubInsetTree");
		var treeNode2 = zTree11.getNodeByParam("id",id);
		treeNode2.checked = false;
		zTree11.updateNode(treeNode2);
		$('#'+id).remove();
	}
	
	<#-- 表类型选择 -->
	$(".select-img .item").click(function(){
		if ($(this).hasClass('active')) {
			return;
		}
		$(this).addClass('active').siblings().removeClass('active');
		rowMaxIndex = 0;
		rankMaxIndex = 0;
		$("#addRowTr").siblings('tr').remove();
		$("#addRankTr").siblings('tr').remove();
		if ($(this).attr('id') == "tableType1") {
			$("#tableType").val(1);
			$("#rowDiv").attr("style","display:block;");
			$("#rankDiv").attr("style","display:none;");
		} else if ($(this).attr('id') == "tableType2") {
			$("#tableType").val(2);
			$("#rowDiv").attr("style","display:none;");
			$("#rankDiv").attr("style","display:block;");
		} else {
			$("#tableType").val(3);
			$("#rowDiv").attr("style","display:block;");
			$("#rankDiv").attr("style","display:block;");
		}
		$("#previewDiv").html('');
	});
	
	<#-- 新增行或列  -->
	function addNewTr(type) {
		var tableType = $("#tableType").val();
		if (type == "row") {
			var $newrow = '<tr>\
						<td><input type="text" id="rowColumns['+rowMaxIndex+'].columnName" name="rowColumns['+rowMaxIndex+'].columnName" maxlength="10" autocomplete="off" class="form-control rowColumn" onkeyup="limitWords(this)"></td>\
					    <td><select id="rowColumns['+rowMaxIndex+'].columnType" name="rowColumns['+rowMaxIndex+'].columnType" class="form-control" onChange="changeColumnType(this)"><option value="1">文本</option><option value="2">数值</option><option value="3">自增序列</option></select></td>\
					    <td><select id="rowColumns['+rowMaxIndex+'].methodType" name="rowColumns['+rowMaxIndex+'].methodType" disabled="disabled" class="form-control methodType"><option value="">--请选择--</option></select></td>\
					    <td><label><input type="checkbox" class="wp wp-switch" checked onClick="isnotEmpty(this)"><span class="lbl"></span><input class="isNotnull" type="hidden" id="rowColumns['+rowMaxIndex+'].isNotnull" name="rowColumns['+rowMaxIndex+'].isNotnull" value="1"></label></td>\
					    <td><a class="color-blue" href="#" onClick="deleteTr(this)">删除</a></td>\
						</tr>';
			$('#addRowTr').before($newrow);
			rowMaxIndex++;
		} else {
			var $newrank = '<tr><td><input type="text" id="rankColumns['+rankMaxIndex+'].columnName" name="rankColumns['+rankMaxIndex+'].columnName" maxlength="10" autocomplete="off" class="form-control rankColumn" onkeyup="limitWords(this)"></td>';
			if (tableType == 2) {
				$newrank += '<td><select id="rankColumns['+rankMaxIndex+'].columnType" name="rankColumns['+rankMaxIndex+'].columnType" class="form-control" onChange="changeColumnType(this)"><option value="1">文本</option><option value="2">数值</option><option value="3">自增序列</option></select></td>\
					    	<td><select id="rankColumns['+rankMaxIndex+'].methodType" name="rankColumns['+rankMaxIndex+'].methodType" disabled="disabled" class="form-control methodType"><option value="">--请选择--</option></select></td>\
					    	<td><label><input type="checkbox" class="wp wp-switch" checked onClick="isnotEmpty(this)"><span class="lbl"></span><input class="isNotnull" type="hidden" id="rankColumns['+rankMaxIndex+'].isNotnull" name="rankColumns['+rankMaxIndex+'].isNotnull" value="1"></label></td>';
			} else {
				$newrank += '<td></td><td></td><td></td>';
			}
				$newrank +=	'<td><a class="color-blue" href="#" onClick="deleteTr(this)">删除</a></td></tr>';
			$('#addRankTr').before($newrank);
			rankMaxIndex++;
		}
	}
	
	<#-- 删除行或列  -->
	function deleteTr(objThis) {
		$(objThis).parent().parent().remove();
	}
	
	<#-- 改变行或列类型  -->
	function changeColumnType(objthis) {
		var type = $(objthis).val();
		var option = '';
		if (type == 2) {
			option = '<option value="">--请选择--</option><option value="1">求平均</option><option value="2">求总和</option>';
			$(objthis).parent().next().children("select").removeAttr("disabled","disabled").empty().append(option);
		} else {
			option = '<option value="">--请选择--</option>';
			$(objthis).parent().next().children("select").attr("disabled","disabled").empty().append(option);
		}
		
		if (type == 3) {
			$(objthis).parent().next().next().children("label").attr("style","display:none;");
			if ($(objthis).parent().next().next().children("label").children(".isNotnull").val() == 1) {
				$(objthis).parent().next().next().children("label").children(".wp").click();
			}
		} else {
			$(objthis).parent().next().next().children("label").removeAttr("style");
			if ($(objthis).parent().next().next().children("label").children(".isNotnull").val() == 0) {
				$(objthis).parent().next().next().children("label").children(".wp").click();
			}
		}
	}
	
	<#-- 修改是否必填 -->
	function isnotEmpty(objthis){
		var notNull = $(objthis).next().next().val();
		if (notNull == 1) {
			$(objthis).next().next().val(0);
		} else {
			$(objthis).next().next().val(1);
		}
	}
	
	<#-- 下一步 -->
	function nextBottom() {
		var infoTitle = $("#infoTitle").val();
		var infoId = $("#infoId").val();
		var startTime = $("#startTime").val();
		var endTime = $("#endTime").val();
		if (infoTitle == '') {
			layer.msg("问卷名称不能为空！");
			return;
		}
		if (startTime == '' || endTime == '') {
			layer.msg("开始时间或结束时间不能为空！");
			return;
		}
		if (startTime>endTime) {
			layer.msg("开始时间不能大于结束时间！");
			return;
		}
		var nowTime = "${((.now)?string('yyyy-MM-dd'))?if_exists}";
		if (endTime<=nowTime) {
			layer.msg("结束时间不能小于等于当前时间！");
			return;
		}
		var objectIds = "";
		<#if unitClass == 1>
    	$(".objValue").each(function(){
			objectIds = objectIds + "," + $(this).val();
    	})
    	if (objectIds == "") {
    		layer.msg("接收对象不能为空！");
			return;
    	} else {
    		objectIds = objectIds.substring(1);
    		$("#objectIds").val(objectIds);
    	}
    	<#else>
    		objectIds = $("#objectIds").val();
    		if (objectIds == "") {
    			layer.msg("接收对象不能为空！");
				return;
    		}
    	</#if> 
    	$.ajax({
			url:"${request.contextPath}/datareport/infomanage/sametitle",
			data:{"infoId":infoId,"infoTitle":infoTitle},
			type:'post',
			dataType : 'json',
			success:function(data) {
				if(data.success){
					$("#beforeDiv").attr("style","display:none");
					$("#nextDiv").attr("style","display:block");
					$("#infoDetailDiv").attr("style","display:none");
					$("#infoTableDiv").attr("style","display:block");
				} else {
		 			layer.msg("问卷名称重复！");
				}
			},
 			error : function(XMLHttpRequest, textStatus, errorThrown) {  
 				layer.msg(XMLHttpRequest.status);
			}
		});                                     
	}  
		
	<#-- 上一步 -->
	function beforeBottom() {
		$("#beforeDiv").attr("style","display:block");
		$("#nextDiv").attr("style","display:none");
		$("#infoDetailDiv").attr("style","display:block");
		$("#infoTableDiv").attr("style","display:none");
	}
	
	<#-- 预览 -->
	function previewExcel() {
		var tableType = $("#tableType").val();
		var header = UE.getEditor('header').getContent();
		var previewHtml = '<h3>';
			previewHtml += header;				
			previewHtml += '</h3><div class="table-container no-margin" style="overflow-x:auto;"><div class="table-container-body">\
						  	  <table class="table table-bordered table-striped table-hover">';
		var rowTrSize = $("#rowTbodyDiv tr").size();
		var rankTrSize = $("#rankTbodyDiv tr").size();
		if (tableType == 1) {
			previewHtml += '<thead><tr>';
			$("#rowTbodyDiv tr").each(function(index){
				if (index + 1 == rowTrSize) {
					return true;
				}
				previewHtml += '<th style="min-width: 180px">';
				if ($(this).find('.isNotnull').val()==1) {
					previewHtml += '<span class="color-red">*</span>';
				}
				previewHtml += $(this).find('.rowColumn').val() +'</th>';
			});
			previewHtml += '</tr></thead><tbody><tr>';
			$("#rowTbodyDiv tr").each(function(index){
				if (index + 1 == rowTrSize) {
					return true;
				}
				if ($(this).find('.methodType').val()==1) {
					previewHtml += '<td>求平均</td>';
				} else if ($(this).find('.methodType').val()==2) {
					previewHtml += '<td>求总和</td>';
				} else {
					previewHtml += '<td></td>';
				}
			});
			previewHtml += '</tr></tbody></table></div></div>';
		} else if (tableType == 2) {
			previewHtml += '<tbody>';
			$("#rankTbodyDiv tr").each(function(index){
				if (index + 1 == rankTrSize) {
					return true;
				}
				previewHtml += '<tr><th width="180px">';
				if ($(this).find('.isNotnull').val()==1) {
					previewHtml += '<span class="color-red">*</span>';
				}
				previewHtml += $(this).find('.rankColumn').val() +'</th>';
				if ($(this).find('.methodType').val()==1) {
					previewHtml += '<td>求平均</td>';
				} else if ($(this).find('.methodType').val()==2) {
					previewHtml += '<td>求总和</td>';
				} else {
					previewHtml += '<td></td>';
				}
				previewHtml += '<tr>';
			});
			previewHtml += '<tbody></table></div></div>';
		} else { 
			previewHtml += '<thead><tr><th width="180px" style="min-width: 180px"></th>';
			$("#rowTbodyDiv tr").each(function(index){
				if (index + 1 == rowTrSize) {
					return true;
				}
				previewHtml += '<th style="min-width: 180px">';
				if ($(this).find('.isNotnull').val()==1) {
					previewHtml += '<span class="color-red">*</span>';
				}
				previewHtml += $(this).find('.rowColumn').val() +'</th>';
			});
			previewHtml += '</tr></thead><tbody>';
			$("#rankTbodyDiv tr").each(function(index){
				if (index + 1 == rankTrSize) {
					return true;
				}
				previewHtml += '<tr><th>'+ $(this).find('.rankColumn').val() +'</th>';
				for (var i=0;i<rowTrSize-1;i++) {
					previewHtml += '<td></td>';
				}
				previewHtml += '</tr>';
			});
			previewHtml += '<tr><th></th>';
			$("#rowTbodyDiv tr").each(function(index){
				if (index + 1 == rowTrSize) {
					return true;
				}
				if ($(this).find('.methodType').val()==1) {
					previewHtml += '<td>求平均</td>';
				} else if ($(this).find('.methodType').val()==2) {
					previewHtml += '<td>求总和</td>';
				} else {
					previewHtml += '<td></td>';
				}
			});
			previewHtml += '</tr></tbody></table></div></div>';
		}
		$("#previewDiv").html(previewHtml);
	}
	
	<#-- 发布或保存 -->
	var isSubmit=false;
	function saveDataInfo(isSub) {
		if(isSubmit){
       		return;
    	}
    	
    	$(".has-error").each(function(){
    		$(this).removeAttr("class");
    	})
    	
    	var header = UE.getEditor('header').getContent();
    	if (!header || header == "") {
    		layer.msg("表头不能为空！");
			return;
    	}													
    	
    	var rowTbody = $("#rowTbodyDiv tr").size();
    	var rankTbody = $("#rankTbodyDiv tr").size();
    	var tableType = $("#tableType").val();
    	if (tableType == 1 || tableType == 3) {
    		if (rowTbody == 1) {
    			layer.msg("行信息不能为空！");
				return;
    		}
    	}
    	if (tableType == 2 || tableType == 3) {
    		if (rankTbody == 1) {
    			layer.msg("列信息不能为空！");
				return;
    		}
    	}
    	
    	var haveError = false;
    	if (tableType == 1 || tableType == 3) {
    		$("#rowTbodyDiv input[type='text']").each(function(index){
    			if ($(this).val() == "" || $(this).val() == null) {
    				$(this).parent().attr("class","has-error");
    				haveError = true;
    			}
    		})
    	} 
    	if (tableType == 2 || tableType == 3) {
    		$("#rankTbodyDiv input[type='text']").each(function(index){
    			if ($(this).val() == "" || $(this).val() == null) {
    				$(this).parent().attr("class","has-error");
    				haveError = true;
    			}
    		})
    	}
    	if (haveError) {
    		layer.msg("行或列的名称不能为空！");
    		return;
    	}
    	var notice = ""; 
    	if (isSub == "true") {
    		notice = "确定要发布吗？";
    	} else {
    		notice = "确定要保存吗？";
    	}
    	var rellayer = layer.confirm(notice, function(index){
			layer.close(rellayer);
			isSubmit = true;
			if (isSub == "true") {
				layerTime("发布中");
    		} else {
    			layerTime("保存中");
    		}
			var options = {
				url : "${request.contextPath}/datareport/infomanage/saveinfo?isSub="+isSub,
				dataType : 'json',
				success : function(data){
 					if(!data.success){
 						layerClose();
 						layerTipMsg(data.success,"失败",data.msg);
 						isSubmit = false;
 					}else{
 						if (isSub == "true") {
 							createExcelTable(data.msg,tableType);
    					} else {
    						layerClose();
	 						layer.msg("保存成功");
							showInfoListHead();
    					}
					}
				},
				clearForm : false,
				resetForm : false,
				type : 'post',
				error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
			};
			$("#dataInfoForm").ajaxSubmit(options);
		});
	}
	
	<#-- 生成模板Excel -->
	function createExcelTable(infoId,tableType) {
		$.ajax({
			url:"${request.contextPath}/datareport/table/createexcel",
			data:{"infoId":infoId,"tableType":tableType},
			type:'post',
			dataType : 'json',
			success:function(data) {
				layerClose();
				if(!data.success){
		 			layerTipMsg(data.success,"Excel模板生成失败",data.msg);
					isSubmit = false;
				} else {
					layer.msg("发布成功");
					showInfoListHead();
				}
			},
 			error : function(XMLHttpRequest, textStatus, errorThrown) {  
 				layer.msg(XMLHttpRequest.status);
			}
		});
	}
</script>