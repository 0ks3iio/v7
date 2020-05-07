<#assign courseSection=course.section?default('')>
<#assign chooseSection=''>
<#if sectionDtos?exists && sectionDtos?size gt 0>
	<#list sectionDtos as sectionDto>
		<#if course.id?default('')=='' && sectionDto_index==0>
			<#assign chooseSection='${sectionDto.sectionValue}'>
		<#elseif course.id?default('')!='' && courseSection==sectionDto.sectionValue>
			<#assign chooseSection='${sectionDto.sectionValue}'>
		</#if>
 	</#list>
</#if>
<div class="layer-content" id="myDiv">
	<form id="courseForm">
		<input type="hidden" name="id" value="${course.id!}"/>
		<input type="hidden" name="subjectType" value="${course.subjectType!}"/>
		<input type="hidden" name="type" value="${type!}"/>
		<input type="hidden" name="sections" value="${course.section!}"/>
		<input type="hidden" name="isUsing" value="1"/>
		<table width="100%">
			<tr>
				<td style="text-align:right;"><p class="mb10"><em>*</em>课程码：</p></td>
				<td>
					<div class="filter-content" >
						<input type="text" class="form-control mb10" name="subjectCode" id="subjectCode" maxLength="20" nullable="false" value="${course.subjectCode!}" <#if (EDU?default(false) && chooseSection!='' && course.id?default('')!='') || (!(EDU?default(false)) && course.id?default('')!='')>readOnly</#if>>
					</div>
				</td>
				<td style="text-align:right;"><p class="mb10"><em>*</em>虚拟课程名称：</p></td>
				<td>
					<div class="filter-content" >
						<input type="text" class="form-control mb10" name="subjectName" id="subjectName" maxLength="100" nullable="false" value="${course.subjectName!}" <#if chooseSection!='' && course.id?default('')!=''>readOnly</#if>>
					</div>
				</td>
			</tr>
			<tr>
				<td style="text-align:right;"><p class="mb10">简称：</p></td>
				<td>
					<div class="filter-content" >
						<input type="text" class="form-control mb10" name="shortName" id="shortName" maxLength="150" value="${course.shortName!}">
					</div>
				</td>
				<td style="text-align:right;"><p class="mb10"><em>*</em>课程类型：</p></td>
				<td>
					<div class="filter-content" >
						<select name="courseTypeId" id="courseType" class="form-control mb10" nullable="false" disabled="disabled" readonly="">
							<#if courseTypeDtos?exists && courseTypeDtos?size gt 0>
							<#list courseTypeDtos as typeDto>
								<option value="${typeDto.id!}" <#if course.courseTypeId?default('')==typeDto.id>selected</#if>>${typeDto.courseTypename!}</option>
							</#list>
							</#if>
						</select>
					</div>
				</td>
			</tr>
			<tr>
				<td style="text-align:right;"><p class="mb10">排序号：</p></td>
				<td>
					<div class="filter-content" >
						<input type="text" class="form-control mb10" name="orderId" id="orderId" vtype="int"  maxLength="9" value="${course.orderId!}" <#if type=='3'>readonly unselectable="on"</#if>>
					</div>
				</td>
				<td></td>
				<td></td>
			</tr>
		</table>
	</form>
</div>	
<div class="layer-footer" style="vertical-align: middle">
	<button class="btn btn-lightblue" id="course-commit">确定</button>
	<button class="btn btn-grey" id="course-close">取消</button>
</div>
	
<script>
$(function(){
    // 虚拟课程序号自增
	<#if !course.id?exists>
	indexChange($("#sectionId input[checked]").next().text());

	function indexChange() {
		var index = 0;
		$(".wp").each(function () {
			var tmp = parseInt($(this).closest("tr").find(".orderId").text());
			if (index < tmp) {
				index = tmp;
			}
		});
		$("#orderId").val(index + 1);
	}
	</#if>

	// 取消按钮操作功能
	$("#course-close").on("click", function(){
	    doLayerOk("#course-commit", {
	    redirect:function(){},
	    window:function(){layer.closeAll()}
	    });     
	 });
	 var isSubmit=false;
	 
	 // 确定按钮操作功能
	$("#course-commit").on("click", function(){	
		if(isSubmit){
			return;
		}
		isSubmit=true;
		var check = checkValue('#myDiv');
	    if(!check){
	        isSubmit=false;
	        return;
	    }
		if(checkBlank()){
			isSubmit=false;
	        return;
		}
		var options = {
			url : '${request.contextPath}/basedata/course/save',
			dataType : 'json',
			success : function(data){
		 		if(data.success){
		 			layer.closeAll();
					refreshPage();
					layer.msg(data.msg, {offset: 't',time: 1000});
		 		}
		 		else{
		 			layerTipMsg(data.success,"失败",data.msg);
					isSubmit=false;
				}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#courseType").removeAttr("disabled");
		$("#courseForm").ajaxSubmit(options);
	});
	
});
	
function checkBlank(){
	
	var subjectCode = $('[name="subjectCode"]').val();
	var sucjecdCodeFormat = /^[0-9a-zA-Z]+$/g;
	if(subjectCode!=undefined && !sucjecdCodeFormat.test(subjectCode)){
		layer.tips('课程码只支持数字、字母', $("#subjectCode"), {
				tipsMore: true,
				tips:3				
			});
		return true;
	}
	// 排序号去空
	var orderIdEle = $('[name="orderId"]');
	orderIdEle.val($.trim(orderIdEle.val()));
	
	var orderId = $('[name="orderId"]').val();
	
	//排序号格式检测
	if(isNaN(orderId) || orderId.indexOf(".")>-1){
		layerTipMsg(false,"失败",'此处排序号必须为整数');
		return true;
	}
	if(orderId>2147483647 || orderId<-2147483648){
		layer.tips('排序号范围是-2147483648 ~ 2147483648', $("#orderId"), {
			tipsMore: true,
			tips:3				
		});
		return true;
	}
}

</script>
