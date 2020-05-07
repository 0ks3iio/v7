<div class="box box-default">
	<div class="box-body clearfix">
        <div class="tab-container">
			<div class="tab-header clearfix">
				<ul class="nav nav-tabs nav-tabs-1">
				 	<li class="active">
				 		<a data-toggle="tab" href="#" onClick="showScoreGeneral();">成绩概况</a>
				 	</li>
				 	<li class="">
				 		<a data-toggle="tab" href="#" onClick="showScoreDetail();">成绩详情</a>
				 	</li>
				</ul>
			</div>			
	       <div class="tab-content" id="scoreTabDiv"></div>
	   </div>
	</div>
</div>

<script>
$(function(){	
    showBreadBack(toBack,true,"成绩统计");
	showScoreGeneral();
});

function showScoreGeneral(){
    var url = "${request.contextPath}/teaexam/scoreCount/showScoreGeneral?examId=${examId!}";
    $('#scoreTabDiv').load(url);
}

function showScoreDetail(){
    var url = "${request.contextPath}/teaexam/scoreCount/showScoreDetail?examId=${examId!}";
    $('#scoreTabDiv').load(url);
}

function toBack(){
    var url = "${request.contextPath}/teaexam/scoreCount/index/page?year=${year!}&type=${type!}";
    $(".model-div").load(url);
}
</script>
