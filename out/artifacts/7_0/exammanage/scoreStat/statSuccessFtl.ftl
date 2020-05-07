<div class="box box-default">
	<div class="box-body">
		<div class="successed-analysis-container">
			<div class="successed-analysis-data">
				<span class="successed-analysis-img">
					<img src="../../static/images/icons/icon-successed-big.png" alt="">
				</span>
				<div class="successed-analysis-body">
					<p class="successed-analysis-tit">成绩分析成功！</p>
					<#--<p class="successed-analysis-text">请前往考试分析模块查看</p>
					<p><a class="btn btn-blue" href="#" onclick="showResult('${examId!}')">去查看</a></p>-->
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
$(function(){
	showBreadBack(goBack,true,"成绩统计");
});
function goBack(){
	var url =  '${request.contextPath}/exammanage/scoreStat/head/page';
	$("#scoreStatDiv").load(url);
}
function showResult(examId){
	
}
</script>