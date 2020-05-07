<#if (sublist?exists && sublist?size>0)>
	<div class="nav-tabs-wrap">
		<ul id="subTypeName" class="nav nav-tabs nav-tabs-1" role="tablist">
			<#list sublist as item>
				<li role="presentation" <#if (subjectId!) == (item.id!)>class="active"</#if> >
				<a href="#bb_${item_index}" role="tab" onclick="chooseTab('${item.id!}')" data-toggle="tab">${item.subjectName!}</a></li>
			</#list>
		</ul>
	</div>
	<input type="hidden" id="subjectId" value="${subjectId!}">
	<div class="listDiv tab-content">
	</div>
	<script type="text/javascript">
	function chooseTab(subjectId){
		$("#subjectId").val(subjectId);
		var examId=$("#examId").val();
		var classId=$("#classId").val();
		if(subjectId==''){
		}else{
			var c2='?examId='+examId+'&classId='+classId+"&subjectId="+subjectId;
			var url='${request.contextPath}/exammanage/edu/score/editlist'+c2;
			$(".listDiv").load(url);
		}
	}
	$(function(){
		//初始化单选控件
		chooseTab('${subjectId!}');
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