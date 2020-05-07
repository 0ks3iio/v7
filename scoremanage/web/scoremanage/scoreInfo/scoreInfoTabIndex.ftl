<#if (infoList?exists && infoList?size>0)>
	<div class="nav-tabs-wrap">
		<ul id="subType" class="nav nav-tabs nav-tabs-1" role="tablist">
			<!--<li role="presentation" class="active"><a href="#aa" onclick="chooseTab('','')" role="tab" data-toggle="tab">全部</a></li>-->
			<#list infoList as info>
			 	<li role="presentation" id="${info.subjectId!}_li" <#if ((info_index==0) && ((subjectId!) == 'undefined'||subjectId?default('')=='') || (subjectId!) == (info.subjectId!))>class="active"</#if> ><a href="#bb_${info_index}" role="tab" onclick="chooseTab('${info.subjectId!}','${info.id!}')" data-toggle="tab">${info.courseName!}</a></li>
			</#list>
		</ul>
	</div>
	<div class="listDiv tab-content">
	</div>
	<script type="text/javascript">
		
	function chooseTab(courseId,subjectInfoId){
		var noLimit = $('#noLimit').val();
		var examId=$("#examId").val();
		var classType=$("#classType").val();
		var classIdSearch=$("#classIdSearch").val();
		if(courseId==''){
		}else{
			var c2='?examId='+examId+'&classType='+classType+'&classIdSearch='+classIdSearch+"&subjectInfoId="
				+subjectInfoId+"&courseId="+courseId+'&noLimit='+noLimit;
			var url='${request.contextPath}/scoremanage/scoreInfo/onelist/page'+c2;
			$(".listDiv").load(url);
		}
	}
	$(function(){
		//初始化单选控件
		<#if (subjectId!) == 'undefined' || subjectId?default('')==''>
		<#list infoList as info>
			<#if info_index==0>
				chooseTab('${info.subjectId!}','${info.id!}');
				<#break>
			</#if>
		</#list>
		<#else>
			chooseTab('${subjectId!}','${subInfoId!}');
		</#if>
	});	
	</script>
<#else>
	暂无数据
</#if>




