<div class="box box-default">
	<div class="box box-default padding-20">
	  	<div class="text-center margin-t-30 margin-b-30">
		<#if newGkChoice.isDefault?default(0)==1>
	  	<img src="${request.contextPath}/static/images/7choose3/icon-smile.png"/></div>
	  	<p class="font-20 text-center margin-b-30">已采用本轮选课</p>
	  	<#else>
	  	<img src="${request.contextPath}/static/images/7choose3/icon-success.png"/></div>
	  	<p class="font-20 text-center margin-b-30">恭喜,选课成功</p>
	  	</#if>
	  	<div class="bg-f2faff padding-30 clearfix class-result">
	  		<div class="text-center">
	  			<div class="choosed-class clearfix margin-b-30">
					<div class="float-left no-margin width-123">
						<p class="text-right">已选学科：</p>
					</div>
					<#if courseNameMap?? && resultList?? && resultList?size gt 0>
					<#list resultList as result>
					<div class="float-left <#if result_index==0>margin-l-20</#if>">
						<span class="gk-course gk-course-s" data-course="${result.subjectId!}"></span>
						<div class="color-999 text-center">
						(<#if result.subjectType?default('1')=='1'>不可调剂
						<#elseif result.subjectType?default('1')=='2'>可调剂
						<#else>优先调剂
						</#if>)
						</div>
					</div>
					</#list>
					</#if>
				</div>
				<div class="choosed-class clearfix margin-b-30">
					<div class="float-left no-margin width-123">
						<p class="text-right">优先调剂到：</p>
					</div>
					<div class="float-left margin-l-20">
						<#if courseNameMap?? && wantToSubjectList?? && wantToSubjectList?size gt 0>
						<#list wantToSubjectList as wantToSubject>
						<span class="class-btn-box <#if wantToSubject_index==0>margin-r-10</#if>">${courseNameMap[wantToSubject!]!}</span>
						</#list>
						<#else>
						无
						</#if>
					</div>
				</div>
				<div class="choosed-class clearfix">
					<div class="float-left no-margin width-123">
						<p class="text-right">明确不选：</p>
					</div>
					<div class="float-left margin-l-20">
						<#if courseNameMap?? && noWantToSubjectList?? && noWantToSubjectList?size gt 0>
						<#list noWantToSubjectList as noWantToSubject>
						<span class="class-btn-box <#if noWantToSubject_index==0>margin-r-10</#if>">${courseNameMap[noWantToSubject!]!}</span>
						</#list>
						<#else>
						无
						</#if>
					</div>
				</div>
	  		</div>
		</div>
		<p class="color-red margin-t-10" id="tipsId"></p>
		<div class="text-center margin-t-30 margin-b-30">
			<span class="color-red">结束时间：${newGkChoice.endTime!}</span>
			<button class="btn btn-blue margin-lr-10" <#if isReelect?default(false)>onClick="goToChooseChoice('${newGkChoice.id!}')"<#else>disabled</#if>>重新选课</button>
			<a href="javascript:;" class="js-alert"><span>查看选课说明</span></a>
		</div>
	</div>
</div>
<div class="layer layer-addInputerset layui-layer-wrap" style="display: none;">
    <div class="layer-body" style="height: 350px;overflow-y: scroll">
        ${newGkChoice.notice!}
    </div>
</div>
<script>
$(function(){
	showBreadBack(goBack,true,"${newGkChoice.choiceName!}");
	function goBack(){
		var url =  '${request.contextPath}/newgkelective/stuChooseSubject/head/page';
		$("#showList").load(url);
	}
	
	$('.js-alert').on('click',function(){
		layer.open({
			type: 1,
			shade: .5,
			title: ['选课说明','font-size:16px;'],
			area: ['800px', '500px'],
			btn: ['知道了'],
			content: $('.layer-addInputerset')
		})
	});
	
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

		
	<#if isTips?default(false)>
		var resultCountMap = {};
		var resultNameMap = {};
		var resultIds = '${resultIds!}';
		var showNum = '${showNum!}';
		var hintContent='${hintContent!}';
		
		var jsonStringData=jQuery.parseJSON('${jsonStringData!}');
		var nameJson = jsonStringData.legendData;
	    var countJson=jsonStringData.loadingData;
	    
	    for(i=0;i<countJson.length;i++){
	   		resultNameMap[countJson[i].subjectId]=nameJson[i];
	    	resultCountMap[countJson[i].subjectId]=countJson[i].value;
		}
		
		if(resultIds!=''){
			if(resultCountMap[resultIds]!=null&&resultCountMap[resultIds]<showNum){
				$("#tipsId").text(hintContent);
			}
	    }
	</#if>	    
})
</script>