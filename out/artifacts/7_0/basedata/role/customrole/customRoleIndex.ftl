<!--人员权限设置-->

<title>人员权限设置</title>
<#import "/fw/macro/webmacro.ftl" as w>
<#-- jqGrid报表引入文件 -->
<link rel="stylesheet" href="${request.contextPath}/static/ace/css/ui.jqgrid.css" />
<#-- ztree引入文件 -->
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css" />

<#-- sweetalert引入文件  -->
<link rel="stylesheet" href="${request.contextPath}/static/sweetalert/sweetalert.css" />
<script src="${request.contextPath}/static/sweetalert/sweetalert.min.js"></script>
<script src="${request.contextPath}/static/ace/components/chosen/chosen.jquery.js"></script>

<!-- ajax layout which only needs content area -->
<div class="row">
</div><!-- 条件筛选结束 -->
<div class="row listDiv">
	<div class="col-lg-12 grade-list">
		<table id="simple-table" class="table  table-bordered table-hover">
			<thead>
				<tr>
					<th>角色名称</th>
					<th>相关人员</th>
					<th>
						<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
						操作
					</th>
				</tr>
			</thead>
			<tbody>
				<#if customRoles?exists && customRoles?size gt 0>
				<#list customRoles as customRole>
				<tr>
					<td>
						${customRole.customRole.roleName!}
					</td>
					<td>
						
							<select multiple id="ateschids_${customRole.customRole.id!}" name="ateschids" class="tag-input-style" data-placeholder=" " class="multi-ateschids" style="width:200px;                 ">
								<#assign customRole2=customRole />
								<#if users?exists && users?size gt 0>
									<#list users as user>
										<option value="${user.id!}" <#if customRole2.roleUserMap?? && customRole2.roleUserMap[user.id]??>selected</#if>>${user.realName!}</option>
									</#list>
								</#if>
							</select>
					</td>
					<th>
						<a href="javascript:;" onClick="doSave('ateschids_${customRole.customRole.id!}','${customRole.customRole.id!}')" class="btn btn-primary">保存</a>
					</th>
				</tr>
				</#list>
				<#else>
					<tr>
					<td colspan="8">
						<p class="alert alert-info center" style="padding:10px;margin:0;">暂时还没有数据哦</p>
					</td>
					<tr>
				</#if>
			</tbody>
			</table>
		</div>
</div>

<!-- page specific plugin scripts -->
<script type="text/javascript">

	$(document).ready(function(){
		<#if customRoles?exists && customRoles?size gt 0>
				<#list customRoles as customRole>
				$('#ateschids_${customRole.customRole.id!}').chosen({
					width:'600px;',
					results_height:'120px',
					multi_container_height:'100px',
					no_results_text:"未找到",//无搜索结果时显示的文本
					allow_single_deselect:true,//是否允许取消选择
					search_contains:true,//模糊匹配，false是默认从第一个匹配
					//max_selected_options:1 //当select为多选时，最多选择个数
				});

		</#list>
		</#if>
	});

	function doUpdateRoleUser(){

	}

	var indexDiv = 0;
	var scripts = [null,
		"${request.contextPath}/static/ace/js/jqGrid/jquery.jqGrid.src.js",
		null];
	$('.page-content-area').ace_ajax('loadScripts', scripts, function() {

		// 新增操作
		$("#btn-addCourse").on("click", function(){
			indexDiv = layerDivUrl("${request.contextPath}/basedata/teachclass/addoredit/0/page",{height:370});
		});


	});

	function doSave(id,roleId){
		var ids = new Array();
		$("#"+id).next("div").find("ul:first").find("li").each(function(){
			var index = $(this).find("a").attr("data-option-array-index");
			var op = id+' option:eq('+index+')';
			var nid = $('#'+op).val();
			if(nid != 'undefined'){
				ids.push(nid);
			}
		});

		$.ajax({
		    url:'${request.contextPath}/basedata/${subsystem!}/customrole/save?userIds='+JSON.stringify(ids)+"&customRoleId="+roleId,
		    type:'post',
		    cache:false,
		    contentType: "application/json",
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		 		if(!jsonO.success){
		 			swal({title: "操作失败!",
	    			text: jsonO.msg,type: "error",showConfirmButton: true,confirmButtonText: "确定"}, function(){
	    			});
		 		}
		 		else{
		 			// 显示成功信息
		 			layer.tips(jsonO.msg, "#teachClass-commit", {tips: [4, '#228B22']});
		 			// 调用封装好的函数，此函数内支持移动端和非移动端，也可以自己写，
		 			// 需要区分移动端和非移动端返回处理不一样
		 			swal({title: "操作成功!",
	    				text: jsonO.msg,type: "success",showConfirmButton: true,confirmButtonText: "确定"}, function(){
	    					location.href="#/basedata/${subsystem?default('')}/customrole/customRole.action";
							location.reload(true);
	    			});
    			}
		     }
		});
	}
</script>
