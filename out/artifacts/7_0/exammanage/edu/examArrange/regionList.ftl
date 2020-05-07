<div class="tab-content">
<div id="a1" class="tab-pane active">
	<div class="filter">
		<div class="filter-item">
			<a href="javascript:" class="btn btn-blue" id="arrange-commit">确定</a>
			<a href="javascript:" class="btn btn-white" id="arrange-back">取消</a>
		</div>
	</div>
	<div class="filter">
		<div class="filter-item">
			<div class="filter-name">考区名称：</div>
			<div class="filter-content">
				<p>${emRegion.regionName!}考区</p>
			</div>
		</div>
		<div class="filter-item">
			<div class="filter-name">考区编号：</div>
			<div class="filter-content">
				<p>${emRegion.examRegionCode!}</p>
			</div>
		</div>
	</div>
	
	<div class="table-container">
		<div class="table-container-body">
			<form id="filterForm">
			<input type="hidden" name="examId" value="${examId!}"/>
			<input type="hidden" name="regionId" value="${regionId!}"/>
			<table class="table table-bordered table-striped table-hover no-margin">
				<thead>
					<tr>
						<th>考点名称</th>
						<th>考点编号</th>
						<th>考点地址</th>
						<th>参考学校</th>
			 			<th class="text-center">操作</th>
					</tr>
				</thead>
				<tbody id="tbody">
					<#if emOptions?exists && (emOptions?size > 0)>
						<#list emOptions as dto>
							<tr id="dto_${dto_index}" class="courseInfoDiv">
								<input type="hidden" value="${dto.id!}" id="id_${dto_index}" class="idClass" name="emOptions[${dto_index}].id">
								<td class="text-center">
									<select multiple="multiple" class="chnageOne" index="${dto_index}" name="emOptions[${dto_index}].optionSchoolId" id="optionSchoolId_${dto_index}" data-placeholder="考点名称选择" style="width: 300px; display: none;">
			                        	<#if unitList?? && (unitList?size>0)>
											<#list unitList as item>
												<option value="${item.id!}" <#if dto.optionSchoolId?? && dto.optionSchoolId==item.id!>selected</#if>>${item.unitName?default('')}考点</option>
											</#list>
										<#else>
											<option value="">暂无数据</option>
										</#if>
			           			 	</select>
								</td>
								<td class="text-center">
									<input type="text" class="table-input optionCode" id="optionCode_${dto_index}" nullable="false" vtype="int" maxlength="10" value="${dto.optionCode!}" name="emOptions[${dto_index}].optionCode">
								</td>
								<td class="text-center">
									<input type="text" class="table-input optionAdd" id="optionAdd_${dto_index}" maxlength="250" value="${dto.optionAdd!}" name="emOptions[${dto_index}].optionAdd">
								</td>
								<td class="text-center">
			            			<select multiple="multiple" class="chnageTwo" name="emOptions[${dto_index}].lkxzSelect" id="lkxzSelect_${dto_index}" data-placeholder="参考学校选择" style="width: 300px; display: none;">
			                        	<#if unitList?? && (unitList?size>0)>
											<#list unitList as item>
												<option value="${item.id!}" <#if dto.lkxzSelectMap?? && dto.lkxzSelectMap[item.id]??>selected</#if>>${item.unitName?default('')}</option>
											</#list>
										<#else>
											<option value="">暂无数据</option>
										</#if>
			           			 	</select>
			           			</td>
								<td class="text-center">
									<#--<a href="javascript:" class="add-exam-close delDiv" id="delDiv_${dto_index}"><i class="fa fa-times-circle"></i></a>-->
									<a href="#aa"  id="delDiv_${dto_index}" data-toggle="tab" aria-expanded="true">删除</a>
								</td>
							</tr>
						</#list>
					</#if>
					<tr id="copyHtml">
						<td class="text-center" colspan="5">
							<a class="table-btn js-addTeacher" href="#aa" onclick="addContent(this)">+新增</a>
						</td>
					</tr>
				</tbody>
			</table>
			</form>

		</div>
	</div>
</div>
<script>
var index=-1;
<#if emOptions?exists && (emOptions?size>0)>
	index=${emOptions?size-1};
</#if>
var optionHtml = '';
 <#if unitList?? && (unitList?size>0)>
	<#list unitList as item>
		optionHtml = optionHtml+'<option value="${item.id!}">${item.unitName!}考点</option>'
	</#list>
</#if>

var optionHtmlNew = '';
 <#if unitList?? && (unitList?size>0)>
	<#list unitList as item>
		optionHtmlNew = optionHtmlNew+'<option value="${item.id!}">${item.unitName!}</option>'
	</#list>
</#if>
//alert(optionHtml);

$(function(){
	<#if emOptions?exists && (emOptions?size>0)>
	  <#list emOptions as item>
	  	init(${item_index},'${item.id!}');
	  </#list>
	</#if>
	
});

function init(ii,subjectInfoId){
	if(subjectInfoId!=''){
		$('#delDiv_'+ii).on('click',function(){
			var obj=$(this);
			showConfirmMsg('确认删除已保存的记录？','提示',function(){
				doDeleteById(subjectInfoId,obj);
			});
		});
	}else{
		$('#delDiv_'+ii).on('click',function(){
			$(this).parent().parent().remove();
		});
	}
}

$('.chnageOne').bind('chosen:hiding_dropdown', function() {
  	var unitId=$(this).val();
  	var ste=$(this).parent().find(".chnageOne").attr("index");
  	$.ajax({
		url:'${request.contextPath}/exammanage/edu/examArrange/optionName?unitId='+unitId,
		type:'get',
		success:function(data) {
			$("#optionAdd_"+ste).val(data);
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {}
	});
});

function doDeleteById(id,obj){
	var examId='${examId!}';
	layer.load();
	$.ajax({
		url:'${request.contextPath}/exammanage/edu/examArrange/optionDelete',
		data: {'optionId':id,'examId':examId},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
 			layer.closeAll();
	 		if(jsonO.success){
				obj.parent().parent().remove();
	 		}
	 		else{
	 			layer.closeAll();
	 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
			}
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {}
	});
}

function copyHtml(hasIndex){
	var contentHtml = '';
	contentHtml=contentHtml+"<tr class='courseInfoDiv'>"
	contentHtml=contentHtml+"<td class='text-center'><select class='chnageOne' multiple='multiple' nullable='false' id='id_"+hasIndex+"' name='emOptions["+hasIndex+"].optionSchoolId' style='width: 300px; display: none;' data-placeholder='考点名称选择'>";
	contentHtml=contentHtml+optionHtml;
	contentHtml=contentHtml+"</select></td>";
	contentHtml=contentHtml+"<td class='text-center'><input type='text' class='table-input optionCode'  nullable='false' vtype='int' maxlength='10' id='optionCode_"+hasIndex+"' name='emOptions["+hasIndex+"].optionCode' value=''></td>";
	contentHtml=contentHtml+"<td class='text-center'><input type='text' class='table-input optionAdd' nullable='false' maxlength='250' id='optionAdd_"+hasIndex+"' name='emOptions["+hasIndex+"].optionAdd' value=''></td>";
	contentHtml=contentHtml+"<td class='text-center'><select class='chnageTwo' multiple='multiple' nullable='false' id='lkxzSelect_"+hasIndex+"' name='emOptions["+hasIndex+"].lkxzSelect' style='width: 300px; display: none;' data-placeholder='参考学校选择'>";
	contentHtml=contentHtml+optionHtmlNew;
	contentHtml=contentHtml+"</select></td>";
	contentHtml=contentHtml+"<td class='text-center'> <a  id='delDiv_"+hasIndex+"' data-toggle='tab' aria-expanded='true'>删除</a></td></tr>";
	return contentHtml;
}

var isSubmit = false;
function addContent(obj){
	if(isSubmit){
		return;
	}
	isSubmit = true;
	if(index > -1){
		var hasIndex = -1;
		for(var i=index;i>-1;i--){
			if($("#dto_"+i).html()){
				hasIndex = i;
				break;
			}
		}
		if(hasIndex>-1){
			index++;
			var exam_item=copyHtml(index);
			$("#copyHtml").before(exam_item);
			$('#delDiv_'+index).on('click',function(){
				index=index-1;
				if(index==-1){
					index=-1;
				}
				$(this).parent().parent().remove();
			});
			isSubmit = false;
			
			$('.chnageOne').chosen({
		width:'500px',
		results_height:'100px',
		multi_container_height:'100px',
		no_results_text:"未找到",//无搜索结果时显示的文本
		search_contains:true,//模糊匹配，false是默认从第一个匹配
		max_selected_options:1, //当select为多选时，最多选择个数
		allow_single_deselect:true,
		disable_search_threshold: 10
	}); 
					//resize the chosen on window resize

	$(window).off('resize.chosen')
					.on('resize.chosen', function() {
						$('.chnageOne').each(function() {
							var $this = $(this);
							$this.next().css({'width': $this.width()});
						})
	}).trigger('resize.chosen');
	$('.chnageTwo').chosen({
		width:'500px',
		results_height:'100px',
		multi_container_height:'100px',
		no_results_text:"未找到",//无搜索结果时显示的文本
		search_contains:true,//模糊匹配，false是默认从第一个匹配
		//max_selected_options:1, //当select为多选时，最多选择个数
		allow_single_deselect:true,
		disable_search_threshold: 10
	}); 
					//resize the chosen on window resize

					$(window)
					.off('resize.chosen')
					.on('resize.chosen', function() {
						$('.chnageTwo').each(function() {
							var $this = $(this);
							$this.next().css({'width': $this.width()});
						})
					}).trigger('resize.chosen');
			
			return;
		}
	}
	index++;
	var exam_item=copyHtml(index);
	$("#copyHtml").before(exam_item);
	
	$('#delDiv_'+index).on('click',function(){
		index=index-1;
		if(index==-1){
			index=-1;
		}
		$(this).parent().parent().remove();
	});
	isSubmit = false;
	$('.chnageOne').chosen({
		width:'500px',
		results_height:'100px',
		multi_container_height:'100px',
		no_results_text:"未找到",//无搜索结果时显示的文本
		search_contains:true,//模糊匹配，false是默认从第一个匹配
		max_selected_options:1, //当select为多选时，最多选择个数
		allow_single_deselect:true,
		disable_search_threshold: 10
	}); 
					//resize the chosen on window resize

	$(window).off('resize.chosen')
					.on('resize.chosen', function() {
						$('.chnageOne').each(function() {
							var $this = $(this);
							$this.next().css({'width': $this.width()});
						})
	}).trigger('resize.chosen');
	$('.chnageTwo').chosen({
		width:'500px',
		results_height:'100px',
		multi_container_height:'100px',
		no_results_text:"未找到",//无搜索结果时显示的文本
		search_contains:true,//模糊匹配，false是默认从第一个匹配
		//max_selected_options:1, //当select为多选时，最多选择个数				
		allow_single_deselect:true,
		disable_search_threshold: 10
					}); 
					//resize the chosen on window resize

					$(window)
					.off('resize.chosen')
					.on('resize.chosen', function() {
						$('.chnageTwo').each(function() {
							var $this = $(this);
							$this.next().css({'width': $this.width()});
						})
					}).trigger('resize.chosen');
	
	$('.chnageOne').bind('chosen:hiding_dropdown', function() {
  		var unitId=$(this).val();
  		var ste=$(this).parent().find(".chnageOne").attr("index");
  		$.ajax({
			url:'${request.contextPath}/exammanage/edu/examArrange/optionName?unitId='+unitId,
			type:'get',
			success:function(data) {
				$("#optionAdd_"+ste).val(data);
			},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {}
		});
	});
	
}

function doExamItem(){
	var url =  '${request.contextPath}/exammanage/edu/examArrange/examItemIndex/page?examId='+'${examId!}';
	$("#examArrangeDiv").load(url);	
}

$('.chnageOne').chosen({
	width:'180px',
	results_height:'100px',
	multi_container_height:'100px',
	no_results_text:"未找到",//无搜索结果时显示的文本
	search_contains:true,//模糊匹配，false是默认从第一个匹配
	max_selected_options:1 //当select为多选时，最多选择个数
});

$('.chnageTwo').chosen({
	width:'300px',
	results_height:'100px',
	multi_container_height:'100px',
	no_results_text:"未找到",//无搜索结果时显示的文本
	search_contains:true,//模糊匹配，false是默认从第一个匹配
	//max_selected_options:1 //当select为多选时，最多选择个数
});

$("#arrange-back").on("click",function(){
	var url =  '${request.contextPath}/exammanage/edu/examArrange/examItemIndex/page?examId='+'${examId!}'+"&type=1";
	$("#examArrangeDiv").load(url);
});

var isSave =false;
$("#arrange-commit").on("click", function(){
	if(isSave){
		return;
	}
	
	var check = checkValue('#a1');
    if(!check){
        return;
    }
	
	var b=false;
	var couSelMap={};
	var numMap={};
	var c=true;
	
    $(".courseInfoDiv").each(function(){
		//各种校验
		c=false;
		var subjectId = $(this).find(".chnageOne").val();
		var num=$(this).find(".chnageOne").parent().next().find("input").val();
		if(subjectId==''||subjectId==null){
			layer.tips('不能为空!', $(this).find(".chnageOne").next(), {
				tipsMore: true,
				tips: 3
			});
			if(!b){
				$(this).find(".chnageOne").focus();
				b=true;
			}
		}
		
		if(couSelMap[subjectId]){
			layer.tips('考点名称不可重复!', $(this).find(".chnageOne").next(), {
				tipsMore: true,
				tips: 3
			});
			if(!b){
				$(this).find(".chnageOne").focus();
				b=true;
			}
		}else{
			couSelMap[subjectId]=subjectId;
		}
		
		var optionId = $(this).find(".chnageTwo").val();
		if(optionId==''||optionId==null){
			layer.tips('不能为空!', $(this).find(".chnageTwo").next(), {
				tipsMore: true,
				tips: 3
			});
			if(!b){
				$(this).find(".chnageTwo").focus();
				b=true;
			}
		}
		
		var optionCode = $(this).find(".optionCode").val();
		if(optionCode==''){
			layer.tips('不能为空!', $(this).find(".optionCode"), {
				tipsMore: true,
				tips: 3
			});
			if(!b){
				$(this).find(".optionCode").focus();
				b=true;
			}
		}
		
		if(numMap[num]){
			layer.tips('考点编号不可重复!', $(this).find(".optionCode"), {
				tipsMore: true,
				tips: 3
			});
			if(!b){
				$(this).find(".optionCode").focus();
				b=true;
			}
		}else{
			numMap[num]=num;
		}
		
		
		var optionAdd = $(this).find(".optionAdd").val();
		if(optionAdd==''){
			layer.tips('不能为空!', $(this).find(".optionAdd"), {
				tipsMore: true,
				tips: 3
			});
			if(!b){
				$(this).find(".optionAdd").focus();
				b=true;
			}
		}
		
	});
    
    if(b||c){
		isSave = false;
		return;
	}
    
	var options = {
		url : "${request.contextPath}/exammanage/edu/examArrange/optionsSave",
		dataType : 'json',
		success : function(data){
 			var jsonO = data;
	 		if(!jsonO.success){
	 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
	 			$("#arrange-commit").removeClass("disabled");
	 			return;
	 		}else{
	 			layer.closeAll();
				layer.msg(jsonO.msg, {
					offset: 't',
					time: 2000
				});
			  	setTimeout(showList(),500);
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#filterForm").ajaxSubmit(options);
});

function showList(){
	var url =  '${request.contextPath}/exammanage/edu/examArrange/regionList/page?examId=${examId!}'+"&regionId="+'${regionId!}';
		$("#showTabDiv").load(url);
}

</script>