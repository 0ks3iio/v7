<a href="javascript:void(0)" onclick="goback('${divide.gradeId!}')" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title">${divide.divideName!}</h3>
	</div>
	<div class="box-body">

		<div class="explain">
			<ul class="gk-student-stat">
				<li><span>选课学生：<em>${chosenStudentNum!}</em>人</span></li>
				<li><span>已安排：<em>${fixStudentNum!}</em>人</span></li>
				<li><span>未安排：<em>${noFixStudentNum!}</em>人</span></li>
			</ul>
		</div>
		<div class="explain">
			<p>蓝色、A代表选考班，绿色、B代表学考班</p>
		</div>
		<div class="box box-primary">
			<div class="box-header">
				<h3 class="box-title">行政班</h3>
			</div>
			<div class="box-body">
				<#if divideClassList?exists && divideClassList?size gt 0>
				
				<div class="class-container clearfix">
					<ul class="class-list">
						<#list divideClassList as item>
						<li><a href="javascript:toClassDetail('${item.id!}');"><em>${(item.studentList)?size}</em><span>${item.className!}</span><i  class="fa fa-times-circle class-del" id="${item.id!}"></i></a></li>
						</#list>
					</ul>
				</div>
				<#else>
					<p class="tip tip-big tip-grey">暂无班级</p>				
				</#if>
				
			</div>
		</div>
		<div class="box box-primary">
			<div class="box-header">
				<h3 class="box-title">3科或者2科组合相同的班级</h3>
			</div>
			<div class="box-body">
				<#if divideThreeSubList?exists && divideThreeSubList?size gt 0>
				<div class="class-container clearfix">
					<ul class="class-list">
						<#list divideThreeSubList as item>
						<li><a  href="javascript:toClassDetail('${item.id!}');"><em>${(item.studentList)?size}</em><span>${item.className!}</span><i style="display:none" class="fa fa-times-circle class-del" id="${item.id!}"></i></a></li>
						</#list>
					</ul>
				</div>
				<#else>
					<p class="tip tip-big tip-grey">暂无班级</p>
				</#if>
			</div>
		</div>
		<#if oneSubMap?exists && oneSubMap?size gt 0>
			<#list oneSubMap?keys as key>
				<div class="box box-primary">
					<div class="box-header">
						<h3 class="box-title">${key!}</h3>
					</div>
					<div class="box-body">
					<#if oneSubMap[key]?exists && oneSubMap[key]?size gt 0>
						<div class="class-container clearfix">
							<ul class="class-list">
								<#list oneSubMap[key] as item>
								<li <#if item.subjectType?default("")=="A">class="top"</#if>><a  href="javascript:toClassDetail('${item.id!}');"><em>${(item.studentList)?size}</em><span>${item.className!}</span><i style="display:none" class="fa fa-times-circle class-del" id="${item.id!}"></i></a></li>
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
		<input type="hidden" id="divideId" value="${divide.id!}">
	</div>
</div>
<script>
	function toClassDetail(divideClassId){
		var divideId=$("#divideId").val();
		var url = '${request.contextPath}/newgkelective/'+divideId+'/divideClass/resultClassDeatil?divideClassId='+divideClassId;
		$("#showList").load(url);
	}
	function goback(gradeId){
		var url =  '${request.contextPath}/newgkelective/'+gradeId+'/goDivide/index/page';
		$("#showList").load(url);
	}
	
</script>
