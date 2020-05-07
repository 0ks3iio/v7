<div class="main-content">
	<div class="main-content-inner">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">学年：</span>
				<div class="filter-content">
					<select name="acadyear" id="acadyear" class="form-control" onchange="doSearch()">
					<#if acadyearList?exists && acadyearList?size gt 0>
					<#list acadyearList as item>
						<option value="${item!}" <#if acadyear?default("")==item>selected="selected"</#if> >${item!}</option>
					</#list>
					</#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">学期：</span>
				<div class="filter-content">
					<select name="semester" id="semester" class="form-control" onchange="doSearch()">
						<option value="1" <#if semester?default("")=="1">selected="selected"</#if> >第一学期</option>
						<option value="2" <#if semester?default("")=="2">selected="selected"</#if> >第二学期</option>
					</select>
				</div>
			</div>
		</div>
		<input type="hidden" id="acadyear" value="${acadyear!}">
		<input type="hidden" id="semester" value="${semester!}">
		<div class="health-checkup">
			<h2>${acadyear!}<#if semester=="1">第一学期<#else>第二学期</#if>体检项目单</h2>
			<table class="table">
				<thead>
					<tr>
						<th width="10%"><label><input type="checkbox" id="checkAll" class="wp"><span class="lbl"></span> 序号</label></th>
						<th width="50%">体检项</th>
						<th>单位</th>
					</tr>
				</thead>
				<tbody>
					<#if itemList?exists && itemList?size gt 0>
					<#list itemList as item>
						<tr>
							<td><label><input type="checkbox" class="wp checked-input" value="${item.id!}" <#if item.haveSelected>checked="true"</#if>><span class="lbl"></span>${item_index+1}</label></td>
							<td>${item.itemName!}</td>
							<td><#if item.itemUnit?default("")=="">/<#else>${item.itemUnit!}</#if></td>
						</tr>
					</#list>
					</#if>
				</tbody>
			</table>
		</div>
		
		<div class="text-right">
			<button class="btn btn-blue" onclick="saveProject()">保存</button>
		</div>
	</div>
</div><!-- /.main-content -->
<script>
$(function(){
	$("#checkAll").click(function(){
		var ischecked = false;
		if($(this).is(':checked')){
			ischecked = true;
		}
	  	$(".checked-input").each(function(){
	  		if(ischecked){
	  			$(this).prop('checked',true);
	  		}else{
	  			$(this).prop('checked',false);
	  		}
		});
	});
})
function doSearch(){
	var acadyear=$("#acadyear").val();
	var semester=$("#semester").val();
	var url ='${request.contextPath}/stuwork/health/project/list?acadyear='+acadyear+"&semester="+semester;
    $("#itemShowDivId").load(url);
}

var isSubmit=false;
function saveProject(){
	if(isSubmit){
		return;
	}
	var acadyear=$("#acadyear").val();
	var semester=$("#semester").val();
	var ids="";
	$(".checked-input").each(function(){
  		if($(this).is(':checked')){
  			if(ids==''){
  				ids = $(this).val();
  			}else{
  				ids+=','+$(this).val();
  			}
  		}
	});
	if(ids==""){
		//layerTipMsg(false,"","请选择要设置的指标");
		//return;
	}
	isSubmit = true;
	$.ajax({
		url:'${request.contextPath}/stuwork/health/project/saveProject',
		data:{'ids':ids,'acadyear':acadyear,'semester':semester},
		type:"post",
		success:function(data){
			var jsonO = JSON.parse(data);
	 		if(jsonO.success){
				layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
	 			doSearch();
	 		}else{
	 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
	 			isSubmit = false;
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	});
	
}
</script>
