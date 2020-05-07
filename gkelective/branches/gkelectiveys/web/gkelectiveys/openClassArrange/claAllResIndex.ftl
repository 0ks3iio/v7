<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<div class="box-body">
	<div class="nav-tabs-wrap">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
			<li role="presentation" class="active"><a href="#aa" role="tab" id="aa" data-toggle="tab" onclick="itemShowList(1)">组合班结果</a></li> 
			<li role="presentation" ><a href="#ee" role="tab" id="ee" data-toggle="tab" onclick="itemShowList(5)">走班班级统计</a></li>
			<li role="presentation"><a href="#bb" role="tab" id="bb" data-toggle="tab" onclick="itemShowList(2)">走班班级详细</a></li>
			<li role="presentation"><a href="#ff" role="tab" id="ff" data-toggle="tab" onclick="itemShowList(6)">学生走班结果</a></li>
			<li role="presentation"><a href="#cc" role="tab" id="cc" data-toggle="tab" onclick="itemShowList(3)">学生调班</a></li>
			<li role="presentation"><a href="#dd" role="tab" id="dd" data-toggle="tab" onclick="itemShowList(4)">学生选课调整</a></li>
		</ul>
	</div>
	<br>
	<@htmlcomponent.printToolBar container=".print" btnClass="btn btn-blue printBtnClass"/>
	<div class="print" id="itemShowDivId">
	</div>
</div>
<script>
	var chosenSubjectTabType = '';
	$(function(){
		itemShowList(1);
	});
	function itemShowList(tabType){
		var url = '';
		if(tabType)
			chosenSubjectTabType = tabType;
		if(chosenSubjectTabType == 1){
			$(".printBtnClass").show();
	        url = '${request.contextPath}/gkelective/${roundsId!}/openClassArrange/group/class/newClassList/page';
		}else if(chosenSubjectTabType == 5){
			$(".printBtnClass").show();
			url = '${request.contextPath}/gkelective/${roundsId!}/openClassArrange/list/statistics/page';
		}else if(chosenSubjectTabType == 2){
			$(".printBtnClass").show();
			url = '${request.contextPath}/gkelective/${roundsId!}/openClassArrange/group/class/needGoClassIndex/page';
		}else if(chosenSubjectTabType == 6){
			$(".printBtnClass").show();
			url =  '${request.contextPath}/gkelective/${roundsId!}/openClassArrange/list/class/page';
		}else if(chosenSubjectTabType == 3){
			$(".printBtnClass").hide();
			url =  '${request.contextPath}/gkelective/${roundsId!}/openClassArrange/list/classChange/page';
		}else if(chosenSubjectTabType == 4){
			$(".printBtnClass").hide();
			url =  '${request.contextPath}/gkelective/${roundsId!}/openClassArrange/list/stuSubChange/page';
		}
        $("#itemShowDivId").load(url);
	}
</script>
