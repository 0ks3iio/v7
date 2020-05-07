<script src="${request.contextPath}/static/js/jquery.form.js"></script>
<form id="editForm" name="editForm" method="post" >
<div class="box box-default">
	<input type="hidden" name="sectionId" value="${newGkSection.id!}">
	<div class="box-header header_filter">
		<div class="filter">
			<div class="filter-item">
				<label for="" class="filter-name">总人数：</label>
				<div class="filter-content">
				<span id="allstuNum"></span>
				 <a href="javascript:" class="btn btn-blue"  onclick="calallStuNum()">统计</a>
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">开设行政班数量：</label>
				<div class="filter-content">
				<input type="text" name="openClassNum" <#if isNow>disabled </#if> class="form-control inline-block number" nullable="false" min="1" vtype="int" id="openClassNum"  maxLength=2 value="${newGkSection.openClassNum!0}">
				</div>
			</div>
			
			<div class="filter-item">
				<label for="" class="filter-name">班级人数：</label>
				<div class="filter-content">
				<input type="text" name="meanSize" id="meanSize" <#if isNow>disabled </#if> class="form-control inline-block number" min="1" nullable="false" vtype="int"  maxLength=2 value="${newGkSection.meanSize!0}">
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">浮动人数：</label>
				<div class="filter-content">
				<input type="text" name="marginSize"  id="marginSize"  <#if isNow>disabled </#if> class="form-control inline-block number" nullable="false" vtype="int"  maxLength=2 value="${newGkSection.marginSize!0}">
				</div>
			</div>
		</div>
		<div class="explain">
		<p>选择不参与分班的3科组合，以及不想组的2科组合。</p>
		</div>
	</div>
	
	<div class="box-body" id="section2xDiv">
	<#if (dtoList3?exists && dtoList3?size gt 0) || (dtoList2?exists && dtoList2?size gt 0)>
	<div class="row">
	<div class="col-sm-6">
	<table class="table table-bordered subject3table" style="margin-top:10px">
		<thead>
			<tr>
				<th>
					<label class="pos-rel">
						<input name="course-checkbox" type="checkbox" class="wp checkboxAllClass">
					<span class="lbl"></span>
					</label>
				</th>
				<th>3科组合</th>
				<th>总人数</th>
				<th>安排人数</th>
				<th>剩余人数</th>
			</tr>
		</thead>
		<tbody>
			<#if dtoList3?exists && dtoList3?size gt 0>
			<#list dtoList3 as ex>
			<tr>
				<td>
					<label class="pos-rel">
					<input name="sectionBeginId" value="${ex.id!}" type="checkbox" <#if isNow>disabled </#if> class="wp checkBoxItemClass" <#if ex.noJoin?default(1)==0>checked</#if>>
					<span class="lbl"></span>
					</label>
				</td>
				<td>${ex.groupName!}</td>
				<td>${ex.studentNum?default(0)}</td>
				<td>${ex.arrangeNum?default(0)}</td>
				<td>${(ex.studentNum?default(0)-ex.arrangeNum?default(0))}</td>
			</tr>
			</#list>
			<#else>
			</#if>
		</tbody>
	</table>
	</div>
	<div class="col-sm-6">
	<table class="table table-bordered" style="margin-top:10px">
			<thead>
			<tr>
				<th>
					<label class="pos-rel">
						<input name="sectionBeginId" type="checkbox" class="wp checkboxAllClass">
					<span class="lbl"></span>
					</label>
				</th>
				<th>2科组合</th>
				<th>总人数</th>
				<th>安排人数</th>
				<th>剩余人数</th>
			</tr>
			</thead>
			<tbody>
			<#if dtoList2?exists && dtoList2?size gt 0>
			<#list dtoList2 as ex>
			<tr>
				<td>
					<label class="pos-rel">
					<input name="sectionBeginId" value="${ex.id!}" type="checkbox" <#if isNow>disabled </#if> class="wp checkBoxItemClass" <#if ex.noJoin?default(1)==0>checked</#if>>
					<span class="lbl"></span>
					</label>
				</td>
				<td>${ex.groupName!}</td>
				<td>${ex.studentNum?default(0)}</td>
				<td>${ex.arrangeNum?default(0)}</td>
				<td>${(ex.studentNum?default(0)-ex.arrangeNum?default(0))}</td>
			</tr>
			</#list>
			<#else>
			</#if>
		</tbody>
	</table>
	</div>
	</div>
	<#else>
	<div class="no-data-container">
		<div class="no-data">
			<span class="no-data-img">
				<img src="${request.contextPath}/gkelective/images/noOpenClass.png" alt="">
			</span>
		</div>
	</div>
	</#if>
</div>

</div>
</form>
<div class="navbar-fixed-bottom opt-bottom">
	<#if isNow>正在分班中 
    
    <#else>
   	 <a href="javascript:" class="btn btn-blue" onclick="saveSection()">下一步</a>
    </#if>
</div>
<script type="text/javascript">
$(function(){
	showBreadBack(toBack,false,"2+x");

	$('#section2xDiv').find('table').each(function(){
		if($(this).find('input:checkbox[name=sectionBeginId]:checked').length>0){
			$(this).find(".checkboxAllClass").prop('checked',true);
		}else{
			$(this).find(".checkboxAllClass").prop('checked',false);
		}
	})
	
	$('#section2xDiv').on('change','.checkboxAllClass',function(){
		if($(this).is(':checked')){
			$(this).parents("table").find("input:checkbox[name='sectionBeginId']").each(function(i){
				$(this).prop('checked',true);
			});
		}else{
			$(this).parents("table").find("input:checkbox[name='sectionBeginId']").each(function(i){
				$(this).prop('checked',false);
			});
		}
	});
	
	$('#section2xDiv').on('change','.checkBoxItemClass',function(){
		if($(this).parents("table").find('input:checkbox[name=sectionBeginId]:checked').length>0){
			$(this).parents("table").find(".checkboxAllClass").prop('checked',true);
		}else{
			$(this).parents("table").find(".checkboxAllClass").prop('checked',false);
		}
	});
})
function calallStuNum(){
	var allNum=0;
	$(".subject3table").find("tbody").find("tr").each(function(){
		if($(this).find("input:checkbox[name='sectionBeginId']:checked").length>0){
			
		}else{
			var nn=$(this).find("td:eq(4)").html();
			allNum=allNum+parseInt(nn);
		}
	})
	$("#allstuNum").html(allNum);
}
function toBack(){
	$("#showList").load("${request.contextPath}/newgkelective/${divideId!}/section2x/index/page");
}
var isSubmit=false;
function saveSection(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	if(!checkValue("#editForm")){
		isSubmit=false;
		return;
	}
	var ii = layer.load();
	var options = {
		url : "${request.contextPath}/newgkelective/${divideId!}/section2x/saveSectionBegin",
		dataType : 'json',
		success : function(data){
			layer.closeAll();
 			var jsonO = data;
	 		if(!jsonO.success){
	 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
	 			isSubmit=false;
	 			return;
	 		}else{
	 			//进入智能分班结果
	 			if("2"==jsonO.msg){
	 				$("#showList").load("${request.contextPath}/newgkelective/${divideId!}/section2x/result/page?sectionId=${newGkSection.id!}");
	 			}else if("1"==jsonO.msg){
	 				$("#showList").load("${request.contextPath}/newgkelective/${divideId!}/section2x/edit/page?sectionId=${newGkSection.id!}");
	 			}else{
	 				//回到首页
	 				$("#showList").load("${request.contextPath}/newgkelective/${divideId!}/section2x/index/page");
	 			}
	 			
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#editForm").ajaxSubmit(options);
}



</script>