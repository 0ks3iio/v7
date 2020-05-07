<#if (subjectDtoList?exists && subjectDtoList?size>0)>
	<div class="nav-tabs-wrap">
		<ul id="subTypeName" class="nav nav-tabs nav-tabs-1" role="tablist">
			<!--<li role="presentation" class="active"><a href="#aa" onclick="chooseTab('','')" role="tab" data-toggle="tab">全部</a></li>-->
			<#--<#list courseList as item>
				<#if typeMap?exists && course73Map[item.id]??>
					<#assign subType=typeMap[item.id]?default("")>
					<#if subType?default("")=='0'>
						<li role="presentation" <#if (item_index==0) && subjectId?default("")=="">class="active"</#if> <#if (subjectId!) == (item.id!)>class="active"</#if> ><a href="#bb_${item_index}" role="tab" onclick="chooseTab('${item.id!}','1')" data-toggle="tab">${item.subjectName!}选考</a></li>
						<li role="presentation" ><a href="#bb_${item_index}" role="tab" onclick="chooseTab('${item.id!}','2')" data-toggle="tab">${item.subjectName!}学考</a></li>
					<#elseif subType?default("")=='1'>
						<li role="presentation" <#if (item_index==0) && subjectId?default("")=="">class="active"</#if> <#if (subjectId!) == (item.id!)>class="active"</#if> ><a href="#bb_${item_index}" role="tab" onclick="chooseTab('${item.id!}','1')" data-toggle="tab">${item.subjectName!}选考</a></li>
					<#else>
						<li role="presentation" <#if (item_index==0) && subjectId?default("")=="">class="active"</#if> <#if (subjectId!) == (item.id!)>class="active"</#if> ><a href="#bb_${item_index}" role="tab" onclick="chooseTab('${item.id!}','2')" data-toggle="tab">${item.subjectName!}学考</a></li>
					</#if>
				<#else>
			 		<li role="presentation" <#if (item_index==0) && subjectId?default("")=="">class="active"</#if> <#if (subjectId!) == (item.id!)>class="active"</#if> ><a href="#bb_${item_index}" role="tab" onclick="chooseTab('${item.id!}')" data-toggle="tab">${item.subjectName!}</a></li>
				</#if>
			</#list>-->
			<#assign haveActive=false>
			<#list subjectDtoList as item>
				<li role="presentation" <#if (item_index==0) && subjectId?default("")=="">class="active"</#if> <#if (subjectId!) == (item.subjectId!) && !haveActive><#assign haveActive=true>class="active"</#if> ><a href="#bb_${item_index}" role="tab" onclick="chooseTab('${item.subjectId!}','${item.subType!}')" data-toggle="tab">${item.subjectName!}</a></li>
			</#list>
		</ul>
	</div>
	<input type="hidden" id="subjectId">
	<input type="hidden" id="subType">
	<div class="listDiv tab-content">
	</div>
	<script type="text/javascript">
		
	function chooseTab(subjectId,subType){
		$("#subjectId").val(subjectId);
		var noLimit = $('#noLimit').val();
		var examId=$("#examId").val();
		var classType=$("#classType").val();
		var classId=$("#classId").val();
		if(!subType){
			subType='';
		}
		$("#subType").val(subType);
		if(subjectId==''){
		}else{
			var c2='?examId='+examId+'&classType='+classType+'&classId='+classId+"&subjectId="+subjectId+'&noLimit='+noLimit+"&subType="+subType;
			var url='${request.contextPath}/exammanage/scoreNewInput/onelist/page'+c2;
			$(".listDiv").load(url);
		}
	}
	$(function(){
		//初始化单选控件
		<#if subjectId?default("")=="">
			<#list subjectDtoList as item>
				<#if item_index==0>
					chooseTab('${item.subjectId!}','${item.subType!}');
					<#break>
				</#if>
			</#list>
		<#else>
			<#list subjectDtoList as item>
				<#if subjectId == item.subjectId!>
					chooseTab('${item.subjectId!}','${item.subType!}');
					<#break>
				</#if>
			</#list>
		</#if>
		<#--<#if subjectId?default("")=="">
		<#list courseList as item>
			<#if item_index==0>
				<#if typeMap?exists && course73Map[item.id]?exists>
					<#assign subType=typeMap[item.id]>
					<#if subType?default("")=='0'>
						chooseTab('${item.id!}','1');
					<#elseif subType?default("")=='1'>
						chooseTab('${item.id!}','1');
					<#else>
						chooseTab('${item.id!}','2');
					</#if>
				<#else>
					chooseTab('${item.id!}');
				</#if>
				<#break>
			</#if>
		</#list>
		<#else>
			<#if typeMap?exists && course73Map[subjectId]?exists>
				<#assign subType=typeMap[subjectId]>
				<#if subType?default("")=='0'>
					chooseTab('${subjectId!}','1');
				<#elseif subType?default("")=='1'>
					chooseTab('${subjectId!}','1');
				<#else>
					chooseTab('${subjectId!}','2');
				</#if>
			<#else>
				chooseTab('${subjectId!}');
			</#if>
		</#if>-->
	});	
	</script>
<#else>
 	<div class="no-data-container">
		<div class="no-data">
			<span class="no-data-img">
				<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
			</span>
			<div class="no-data-body">
				<p class="no-data-txt">暂无记录</p>
			</div>
		</div>
	</div>
</#if>




