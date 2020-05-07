<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<div class="box box-default">
	<div class="box-body">
		<div class="nav-tabs-wrap">
			<ul class="nav nav-tabs nav-tabs-1" role="tablist">
				<li class="active" role="presentation"><a href="#aa" role="tab" data-toggle="tab" onclick="itemShowList(1)">学科成绩</a></li>
				<li role="presentation"><a href="#bb" role="tab" data-toggle="tab" onclick="itemShowList(2)">英语</a></li>
				<li role="presentation"><a href="#bb" role="tab" data-toggle="tab" onclick="itemShowList(4)">学考等第</a></li>
				<#--<li role="presentation"><a href="#bb" role="tab" data-toggle="tab" onclick="itemShowList(3)">综合素质计算</a></li>-->
			</ul>
		</div>
		<div class="tab-content" id="itemShowDivId">
		</div>
	</div>
</div>
<script>
$(function(){
	itemShowList(1);

});

	
function itemShowList(tabType){
	var url = '';
	if(tabType == 1){
	       url =  '${request.contextPath}/comprehensive/quality/score/listOther/page';
	}else if(tabType == 2){
	       url =  '${request.contextPath}/comprehensive/quality/score/listEnglish/page';
	}else if(tabType == 3){
		url =  '${request.contextPath}/comprehensive/qualityScoreStat/index/page';
	}else if(tabType == 4){
		url =  '${request.contextPath}/comprehensive/quality/score/listXk/page';
	}
    $("#itemShowDivId").load(url);
}

</script>