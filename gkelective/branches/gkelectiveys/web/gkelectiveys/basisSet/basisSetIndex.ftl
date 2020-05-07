<script src="${request.contextPath}/gkelectiveys/js/myscriptCommon.js"></script> 
<a href="#" class="page-back-btn gotoIndexClass"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">${gkSubArr.arrangeName!}</h4>
	</div>
	<div class="box-body">
		<div class="nav-tabs-wrap">
			<div class="nav-tabs-btns">
		        <a href="javascript:void(0);" onclick="openClassArrange();" class="btn-sm btn-blue">去选课结果</a>
		    </div>
		    <ul class="nav nav-tabs" role="tablist">
		        <li role="presentation" class="active"><a href="#aa" id="aa" role="tab" data-toggle="tab" onclick="itemShowList(1)">排班成绩依据</a></li>
		        <#--<li role="presentation"><a href="#bb"  role="tab" id="bb" data-toggle="tab" onclick="itemShowList(2)">开班规则设置</a></li>-->
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
	function openClassArrange(){
		var url =  '${request.contextPath}/gkelective/${arrangeId}/chosenSubject/index/page';
		$("#showList").load(url);
	}
	function itemShowList(tabType){
		var url = '';
		if(tabType == 1){
	        url =  '${request.contextPath}/gkelective/${arrangeId}/basisSet/score/index/page';
		}else if(tabType == 2){
			url =  '${request.contextPath}/gkelective/${arrangeId}/basisSet/rule/index/page';
		}
        $("#itemShowDivId").load(url);
	}
</script>
