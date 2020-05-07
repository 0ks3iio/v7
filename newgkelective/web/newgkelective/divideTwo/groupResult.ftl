<#--<a href="javascript:" class="page-back-btn gotoDivideIndex"><i class="fa fa-arrow-left"></i> 返回</a>-->
<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1 headUl" role="tablist">
			<#if dtoList?exists && dtoList?size gt 0>
				<#list dtoList as dto>
					<li <#if dto.key?default('')==groupType>class="active"</#if> role="presentation" data-value="${dto.key!}"><a href="#aa_${dto_index}" data-value="tabContent_${dto_index}" role="tab" data-toggle="tab">${dto.tabName!}</a></li>
				</#list>
			</#if>
		</ul>
		<#if dtoList?exists && dtoList?size gt 0>
			<#list dtoList as dto>
				<div class="tab-content" id="tabContent_${dto_index}" <#if dto.key?default('')!=groupType>class="display:none"</#if>>
				<#assign showList=dto.showList>
				<#assign classList=dto.classList>
				<#if showList?exists && showList?size gt 0>
					<#list showList as showCourse>
						<div class="box box-primary">
							<div class="box-header">
								<h3 class="box-title">${showCourse.subjectName!}</h3>
							</div>
							<#assign clazzList=classList[showCourse.id]>
							<div class="box-body">
								<#if clazzList?exists && clazzList?size gt 0>
								<div class="class-container clearfix">
									<ul class="class-list">
										<#list clazzList as clazz>
										<li class="top"><a href="javascript:toClassDetail('${clazz.id!}');"><em>${clazz.studentCount?default(0)}</em><span>${clazz.className!}</span><i class="fa fa-times-circle class-del"></i></a></li>
										</#list>
									</ul>
								</div>
								<#else>
								<p class="tip tip-big tip-grey">暂无班级</p>
								</#if>
							</div>
							
						</div>
					</#list>
				</#if>
				</div>
			</#list>
		</#if>
		
	</div>
</div>
<script>
	$(function(){
		//返回
		showBreadBack(toMyBackIndex,false,"分班结果");
		<#--
			$(".gotoDivideIndex").on("click",function(){
				toMyBackIndex();
			});
		-->
		$(".headUl li").each(function(){
			if($(this).hasClass("active")){
				var contentId=$(this).find("a").attr("data-value");
				$(".tab-content").hide();
				$("#"+contentId).show();
				return false;
			}
		});
		$(".headUl li").on("click",function(){
			var contentId=$(this).find("a").attr("data-value");
			$(".tab-content").hide();
			$("#"+contentId).show();
		});
		
	})
	function toMyBackIndex(){
		var url =  '${request.contextPath}/newgkelective/${newDivide.gradeId!}/goDivide/index/page';
		<#if fromArray?default('') == '1'>
            <#if fromSolve?default('0') == '1'>
                url = '${request.contextPath}/newgkelective/${arrayId!}/arraySet/pageIndex';
			<#elseif arrayId?default('')==''>
			    url = '${request.contextPath}/newgkelective/${newDivide.gradeId!}/goArrange/addArray/page?divideId=${newDivide.id!}&lessArrayId=${lessArrayId!}&plArrayId=${plArrayId!}';
		    <#else>
			    url = '${request.contextPath}/newgkelective/${newDivide.gradeId!}/goArrange/editArray/page?arrayId=${arrayId!}';
		    </#if>
		</#if>
		$("#showList").load(url);
	}
	function toClassDetail(divideClassId){
		var groupType="";
		$(".headUl li").each(function(){
			if($(this).hasClass("active")){
				groupType=$(this).attr("data-value");
				return false;
			}
		});
		var url = '${request.contextPath}/newgkelective/${newDivide.id!}/divideClass/resultClassDeatil?divideClassId='+divideClassId+'&groupType='+groupType;
		$("#showList").load(url);
	}
</script>