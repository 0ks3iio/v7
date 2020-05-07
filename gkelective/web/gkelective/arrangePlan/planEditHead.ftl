<#if rounds?exists>
<div class="nav-tabs-wrap">
	<em>所选轮次特点：重组行政班（<#if rounds.openClassType=="1"><i class="ace-icon glyphicon glyphicon-ok"></i><#else><i class="ace-icon glyphicon glyphicon-remove"></i></#if>）、开设学考班（<#if rounds.openClass=="1"><i class="ace-icon glyphicon glyphicon-ok"></i><#else><i class="ace-icon glyphicon glyphicon-remove"></i></#if>）。</em>
	<ul class="nav nav-tabs nav-tabs-1" role="tablist">
	<#if rounds.openClassType=="1">
		<li role="presentation" class="active"><a href="#bb" role="tab" id="bb" data-toggle="tab" onclick="itemShowList(0)">组合班结果</a></li>
		<li role="presentation" ><a href="#aa" role="tab" id="aa" data-toggle="tab" onclick="itemShowList(1)">走班班级详情</a></li>
	<#else>
		<li role="presentation" class="active"><a href="#aa" role="tab" id="aa" data-toggle="tab" onclick="itemShowList(1)">走班班级详情</a></li>
	</#if>	
		<li role="presentation"><a href="#ee" role="tab" id="ee" data-toggle="tab" onclick="itemShowList(2)">学生走班结果</a></li>
	</ul>
</div>
<div id="itemShowDivId">
		
</div>
<script type="text/javascript">
	var contextPath = '${request.contextPath}';
	var arrangeId = '${arrangeId!}';
	$(function(){
		<#if rounds.openClassType=="1">
			itemShowList(0);
		<#else>
			itemShowList(1);
		</#if>
	})
	function itemShowList(type){
		var roundsId=$("#roundsId").val();
		if(roundsId==""){
			return;
		}
		var url;
		if(type==0){
			url='${request.contextPath}/gkelective/'+roundsId+'/openClassArrange/group/class/newClassList/page';
		}else if(type==1){
			url='${request.contextPath}/gkelective/'+roundsId+'/openClassArrange/group/class/needGoClassIndex/page';
		}else if(type==2){
			url='${request.contextPath}/gkelective/'+roundsId+'/openClassArrange/list/class/page';
		}
		 $("#itemShowDivId").load(url);
	}

</script>
</#if>