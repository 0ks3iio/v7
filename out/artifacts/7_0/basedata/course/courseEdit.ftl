<#assign courseSection=course.section?default('')>
<#assign chooseSection=''>
<#if sectionDtos?exists && sectionDtos?size gt 0>
	<#list sectionDtos as sectionDto>
		<#if course.id?default('')=='' && sectionDto_index==0>
			<#assign chooseSection='${sectionDto.sectionValue}'>
		<#elseif course.id?default('')!=''>
			<#if isHw?default(false)>
			<#assign chooseSection='${course.section!}'>
			<#elseif courseSection==sectionDto.sectionValue>
			<#assign chooseSection='${sectionDto.sectionValue}'>
			</#if>
		</#if>
 	</#list>
</#if>
<div class="layer-content" id="myDiv">
	<form id="courseForm">
		<input type="hidden" name="id" value="${course.id!}"/>
		<input type="hidden" name="subjectType" value="${course.subjectType!}"/>
		<table width="100%">
			<tr>
				<td width="90" style="text-align:right;"><p class="mb10"><em>*</em>学段：</p></td>
				<td colspan="3">
					<div class="filter-content" id="sectionId">
						<#if sectionDtos?exists && sectionDtos?size gt 0>
							<#if isHw?default(false)>
								<input type="hidden" name="sections" id="sections">
							</#if>
							<#list sectionDtos as sectionDto>
							<label class="pos-rel">
								<#if isHw?default(false)>
									<#if chooseSection?contains(sectionDto.sectionValue)>
									<input type="checkbox" class="wp" name="section" value="${sectionDto.sectionValue}" checked <#if chooseSection!='' && course.id?default('')!=''>disabled</#if> >
									<#else>
										<input type="checkbox" class="wp" name="section" value="${sectionDto.sectionValue}" <#if chooseSection!='' && course.id?default('')!=''>disabled</#if> >
									</#if>
			            			<span class="lbl">${sectionDto.sectionName!}</span>
								<#else>
									<#if chooseSection==sectionDto.sectionValue>
									<input type="radio" class="wp" name="section" value="${sectionDto.sectionValue}" checked>
									<#else>
										<input type="radio" class="wp" name="section" value="${sectionDto.sectionValue}" <#if chooseSection!='' && course.id?default('')!=''>disabled</#if> >
									</#if>
			            			<span class="lbl">${sectionDto.sectionName!}</span>
		            			</#if>
		     			 	</label>
		     			 	</#list>
			      		</#if>
					</div>
				</td>
			</tr>
			<tr>
				<td style="text-align:right;"><p class="mb10"><em>*</em>课程码：</p></td>
				<td>
					<div class="filter-content" >
						<input type="text" class="form-control mb10" name="subjectCode" id="subjectCode" maxLength="20" nullable="false" value="${course.subjectCode!}" <#if (EDU?default(false) && chooseSection!='' && course.id?default('')!='') || (!(EDU?default(false)) && course.id?default('')!='')>readOnly</#if>>
					</div>
				</td>
				<td style="text-align:right;"><p class="mb10"><em>*</em>课程名称：</p></td>
				<td>
					<div class="filter-content" >
						<input type="text" class="form-control mb10" name="subjectName" id="subjectName" maxLength="100" nullable="false" value="${course.subjectName!}" <#if chooseSection!='' && course.id?default('')!=''>readOnly</#if>>
					</div>
				</td>
			</tr>
			<#if type?default('1')=='1'>
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
						<input type="hidden" name="type" value="1">
						<select name="courseTypeId" id="courseType" class="form-control mb10" nullable="false">
							<option value="">请选择</option>
							<#if courseTypeDtos?exists && courseTypeDtos?size gt 0>
							<#list courseTypeDtos as typeDto>
								<option value="${typeDto.id!}" <#if course.courseTypeId?default('')==typeDto.id>selected</#if>>${typeDto.courseTypename!}</option>
							</#list>
							</#if>
						</select>
					</div>
				</td>
			</tr>
			<#else>
			<tr>
				<td style="text-align:right;"><p class="mb10">简称：</p></td>
				<td>
					<div class="filter-content" >
						<input type="text" class="form-control mb10" name="shortName" id="shortName" maxLength="150" value="${course.shortName!}">
					</div>
				</td>
			</tr>
			<tr>
				<td style="text-align:right;"><p class="mb10"><em>*</em>选修类型：</p></td>
				<td>
					<div class="filter-content" >
						<select name="type" id="type" class="form-control mb10" nullable="false" onChange="changeType()" <#if chooseSection!='' && course.id?default('')!=''>disabled</#if>>
							<option value="2" <#if course.type?default('2')=='2'>selected</#if>>选修</option>
							<option value="4" <#if course.type?default('2')=='4'>selected</#if>>选修Ⅰ-A</option>
							<option value="5" <#if course.type?default('2')=='5'>selected</#if>>选修Ⅰ-B</option>
							<option value="6" <#if course.type?default('2')=='6'>selected</#if>>选修Ⅱ</option>
						</select>
					</div>
				</td>
				<td style="text-align:right;"><p class="mb10"><em>*</em>课程类型：</p></td>
				<td>
					<div class="filter-content" >
						<select name="courseTypeId" id="courseType" class="form-control mb10" nullable="false">
							<option value="">请选择</option>
							<#if courseTypeDtos?exists && courseTypeDtos?size gt 0>
							<#list courseTypeDtos as typeDto>
								<option value="${typeDto.id!}" <#if course.courseTypeId?default('')==typeDto.id>selected</#if>>${typeDto.courseTypename!}</option>
							</#list>
							</#if>
						</select>
					</div>
				</td>
			</tr>
			</#if>
			<tr>
				<td style="text-align:right;"><p class="mb10"><#if type != '1'><em>*</em></#if>学分：</p></td>
				<td>
					<div class="filter-content" >
						<input type="text" class="form-control mb10" name="initCredit" id="initCredit" vtype="int" <#if type != '1'>nullable="false"</#if>  maxLength="3" value="${course.initCredit!}">
					</div>
				</td>
				<td style="text-align:right;"><p class="mb10">满分：</p></td>
				<td>
					<div class="filter-content" >
						<input type="text" class="form-control mb10" name="fullMark" id="fullMark" vtype="int"  maxLength="3" value="${course.fullMark!}">
					</div>
				</td>
			</tr>
			<tr>
				<td style="text-align:right;"><p class="mb10">及格分：</p></td>
				<td>
					<div class="filter-content" >
						<input type="text" class="form-control mb10" name="initPassMark" id="initPassMark" vtype="int"  maxLength="3"  value="${course.initPassMark!}">
					</div>
				</td>
				<td style="text-align:right;"><p class="mb10"><em>*</em>是否在用：</p></td>
				<td>
					<label><input type="checkbox" class="wp wp-switch" name="isUsing" value="1" <#if course.isUsing?default(1) == 1>checked</#if>><span class="lbl"></span></label>
				</td>
			</tr>
			<tr>
				<td style="text-align:right;"><p class="mb10">排序号：</p></td>
				<td>
					<div class="filter-content" >
						<input type="text" class="form-control mb10" name="orderId" id="orderId" vtype="int"  maxLength="9" value="${course.orderId!}">
					</div>
				</td>
				<#if EDU?default(false) && type=='1'>
				<td style="text-align:right;"><p class="mb10">颜色：</p></td>
				<td>
					<input class="input_cxcolor" id="color"  name="bgColor" type="text" value="${course.bgColor?default('')}" readonly>
				</td>
				<#else>
					<td style="text-align:right;"><p class="mb10">总课时：</p></td>
					<td>
						<div class="filter-content" >
							<input type="text" class="form-control mb10" name="totalHours" id="totalHours" vtype="int"  maxLength="4" value="${course.totalHours!}">
						</div>
					</td>
				</#if>
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
		<#if isHw?default(false)>
			var sections = [];
			$('[name="section"]:checked').each(function(){
				sections.push($(this).val());
			});
			$("#sections").val(sections.join(","));
		</#if>
		var options = {
			url : '${request.contextPath}/basedata/course/save',
			dataType : 'json',
			success : function(data){
		 		if(data.success){
		 			layer.closeAll();
	 			 	layer.msg(data.msg, {offset: 't',time: 3000});
	 			 	refreshPage();
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
		$("#courseForm").ajaxSubmit(options);
	});
	
});
<#if type=='1'>
(function(){
	var color=$("#color");
	
	$("#color").cxColor();
})();
</#if>
	
function checkBlank(){
	var sections = $('[name="section"]:checked');
	if(sections.length==0){
		layer.tips('学段不能为空', $("#sectionId"), {
				tipsMore: true,
				tips:3				
			});
		return true;
	}
	var subjectCode = $('[name="subjectCode"]').val();
	var sucjecdCodeFormat = /^[0-9a-zA-Z]+$/g;
	if(subjectCode!=undefined && !sucjecdCodeFormat.test(subjectCode)){
		layer.tips('课程码只支持数字、字母', $("#subjectCode"), {
				tipsMore: true,
				tips:3				
			});
		return true;
	}
	<#if EDU?default(false) && !isHw?default(false)>
		<#if chooseSection!='' && course.id?default('')!=''>
		<#else>
		var section = sections.eq(0).val();
		if(subjectCode.substring(0,1)!=section){
			var sectionName = sections.eq(0).siblings('span').text();
			layer.tips(sectionName+'学段课程码必须以'+section+'开头', $("#subjectCode"), {
				tipsMore: true,
				tips:3				
			});
			return true;
		}
		</#if>
	</#if>
	//排序号去空
	var orderIdEle = $('[name="orderId"]');
	orderIdEle.val($.trim(orderIdEle.val()));
	var fullMarkEle = $('[name="fullMark"]');
	fullMarkEle.val($.trim(fullMarkEle.val()));
	var initPassMarkEle = $('[name="initPassMark"]');
	initPassMarkEle.val($.trim(initPassMarkEle.val()));
	
	var orderId = $('[name="orderId"]').val();
	var fullMark = $('[name="fullMark"]').val();
	var initPassMark = $('[name="initPassMark"]').val();
	if(fullMark!="" && initPassMark!=""){
		if((parseInt(fullMark)-parseInt(initPassMark))<0){
			layer.tips('及格分不得大于满分', $("#initPassMark"), {
				tipsMore: true,
				tips:3				
			});
			return true;
		}
	}
	
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

function changeType(){
	var type=$("#type").val(); 
	var courseType = $("#courseType");
	$.ajax({
		url:"${request.contextPath}/basedata/course/getCourseTypeList",
		data:{type:type},
		dataType: "json",
		success: function(json){
			courseType.html("");
			var courseTypeHtml="";
			if(json && json.length>0){
				for(var i=0;i<json.length;i++){
					courseTypeHtml+='<option value="'+json[i].id+'">';
					courseTypeHtml+=json[i].courseTypename;
					courseTypeHtml+='</option>';
				}
				courseType.append(courseTypeHtml);
			}
		}
	});
}

</script>
