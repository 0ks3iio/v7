<div class="box box-default">
	<div class="box-body">
		<div class="explain">
			<h4>说明：</h4>
			<p>最高可得分：即学生在该模块所有学年学期最高可累计分，0为不限制最高分。</p>
			<p>每届最高可得分：即学生在该届节日中最高可获得的分数。</p>
		</div>
		<div id="showListDiv">
		</div>
	</div>
</div>

<script>
$(function(){
	showListDiv();
});
function showListDiv(){
	var url =  '${request.contextPath}/quality/param/list';
	$("#showListDiv").load(url);
}
</script>