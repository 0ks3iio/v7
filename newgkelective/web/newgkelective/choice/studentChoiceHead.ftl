<#if chDtos?exists && (chDtos?size>0)>
	<#list chDtos as item>
		<div class="box box-default box-border" 
		<#if item.resultList?? && item.resultList?size gt 0>onClick="goToChooseDetail('${item.choiceId!}')"
		<#else>onclick="goToChooseChoice('${item.choiceId!}',${item.warning?string('true','false')})"</#if>>
			<div class="box-header choose-class-list">
				<h3 class="box-caption">
				<#if item.timeState==2>
					<span class="label label-default position-relative top-2">已结束</span>
				<#elseif item.timeState==0>
					<span class="label label-default position-relative top-2">未开始</span>
				<#else>
					<span class="label label-warning position-relative top-2">进行中</span>
				</#if>
					 ${item.choiceName!}
				<#if item.default?default(false)><span class="badge badge-blue position-relative top-2">已采用</span></#if>
				</h3>
				<div class="box-header-tools">
					<div class="text-right color-999 static-time">选课时间：${item.startTime!} ~ ${item.endTime!}</div>
				</div>
			</div>
			<div class="box-body">
				<#if item.timeState==1>
					<#if courseNameMap?? && item.resultList?? && item.resultList?size gt 0>
					<div class="row">
						<div class="col-md-8 border-right-D9D9D9 clearfix">
							<span class="float-left"><b>已选学科：</b></span>
							<div class="choosed-class float-left clearfix">
								<#list item.resultList as result>
								<div class="float-left">
									<span class="gk-course gk-course-s" data-course="${result.subjectId!}"></span>
									<div class="color-999 text-center">
									(<#if result.subjectType?default('1')=='1'>不可调剂
									<#elseif result.subjectType?default('1')=='2'>可调剂
									<#else>优先调剂
									</#if>)
									</div>
								</div>
								</#list>
							</div>
						</div>
						<div class="col-md-4">
							<div class="class-else">
								<span><b>优先调剂到：</b></span>
								<#if item.wantToSubjectList?? && item.wantToSubjectList?size gt 0>
								<#list item.wantToSubjectList as wantToSubject>
								<span class="class-btn-box">${courseNameMap[wantToSubject!]!}</span>
								</#list>
								<#else>
								无
								</#if>
							</div>
							<div class="class-else">
								<span><b>明确不选：</b></span>
								<#if item.noWantToSubjectList?? && item.noWantToSubjectList?size gt 0>
								<#list item.noWantToSubjectList as noWantToSubject>
								<span class="class-btn-box">${courseNameMap[noWantToSubject!]!}</span>
								</#list>
								<#else>
								无
								</#if>
							</div>
						</div>
					</div>
					<#else>
					<div class="no-data-container">
						<div class="no-data no-margin-b">
							<span class="no-data-img">
								<img src="${request.contextPath}/static/images/public/nodata5.png" alt="">
							</span>
							<div class="no-data-body margin-t-10">
								<p class="no-data-txt no-margin"><button class="btn btn-blue js-start">开始选课</button></p>
							</div>
						</div>
					</div>
					</#if>
				<#elseif item.timeState==0>
					<div class="no-data-container">
						<div class="no-data">
							<span class="no-data-img">
								<img src="${request.contextPath}/static/images/public/nodata3.png" alt="">
							</span>
							<div class="no-data-body">
								<p class="no-data-txt">选课未开始，请稍等...</p>
							</div>
						</div>
					</div>
				<#else>
					<#if courseNameMap?? && item.resultList?? && item.resultList?size gt 0>
					<div class="row">
						<div class="col-md-8 border-right-D9D9D9 clearfix">
							<span class="float-left"><b>已选学科：</b></span>
							<div class="choosed-class float-left clearfix">
								<#list item.resultList as result>
								<div class="float-left">
									<span class="gk-course gk-course-s" data-course="${result.subjectId!}"></span>
									<div class="color-999 text-center">
									(<#if result.subjectType?default('1')=='1'>不可调剂
									<#elseif result.subjectType?default('1')=='2'>可调剂
									<#else>优先调剂
									</#if>)
									</div>
								</div>
								</#list>
							</div>
						</div>
						<div class="col-md-4">
							<div class="class-else">
								<span><b>优先调剂到：</b></span>
								<#if item.wantToSubjectList?? && item.wantToSubjectList?size gt 0>
								<#list item.wantToSubjectList as wantToSubject>
								<span class="class-btn-box">${courseNameMap[wantToSubject!]!}</span>
								</#list>
								<#else>
								无
							</#if>
							</div>
							<div class="class-else">
								<span><b>明确不选：</b></span>
								<#if item.noWantToSubjectList?? && item.noWantToSubjectList?size gt 0>
								<#list item.noWantToSubjectList as noWantToSubject>
								<span class="class-btn-box">${courseNameMap[noWantToSubject!]!}</span>
								</#list>
								<#else>
								无
							</#if>
							</div>
						</div>
					</div>
					<#else>
					<div class="no-data-container">
						<div class="no-data">
							<span class="no-data-img">
								<img src="${request.contextPath}/static/images/public/nodata4.png" alt="">
							</span>
							<div class="no-data-body">
								<p class="no-data-txt">很遗憾，您未参加选课</p>
							</div>
						</div>
					</div>
					</#if>
				</#if>
			</div>
		</div>
	</#list>
<#else>
	<div class="no-data-container">
		<div class="no-data">
			<span class="no-data-img">
				<img src="${request.contextPath}/static/images/classCard/no-tutor-project.png" alt="">
			</span>
			<div class="no-data-body">
				<h3>暂无选课</h3>
				<p class="no-data-txt">如需进行选课，请联系教务管理员新建选课项目</p>
			</div>
		</div>
	</div>
</#if>

<script>

$(function(){
	var courseClass = {
		<#if idNames?? && idNames?size gt 0>
		<#list idNames as ins>
		'${ins[0]!}': 'gk-course-s-${ins[1]!}'<#if ins_has_next>,</#if>
		</#list>
		</#if>
	};
	
	$('.choosed-class .gk-course-s').each(function(){
		var key = $(this).data('course');
		$(this).addClass(courseClass[key]);
	});

})

function goToChooseChoice(choiceId,warn) {
	if(warn){
		layer.msg('您被设置为不参与选课！如需进行选课，请联系教务管理员进行修改',{
			offset: 't'
		})
		return;
	}
	var url = '${request.contextPath}/newgkelective/stuChooseSubject/student/page?choiceId='+choiceId;
	$("#showList").load(url);
}

function goToChooseDetail(choiceId) {
	var url = '${request.contextPath}/newgkelective/stuChooseSubject/detail/page?choiceId='+choiceId;
	$("#showList").load(url);
}

</script>