<#if statList?exists && statList?size gt 0>
	<div class="explain">
		<p>${summary!}</p>
	</div>
	<div class="table-container no-margin">
		<div class="table-container-body js-scroll" >
			<form class="print">
			<table class="table table-bordered table-striped table-hover">
				<thead>
					<tr>
						<th class="text-center" <#if is73Sub?default("")=='true'>colspan="12"<#else>colspan="11"</#if>>${title!}</th>
					</tr>
					<tr>
						<th>序号</th>
						<th>学号</th>
						<th>行政班</th>
						<th>学生</th>
						<th>考试分</th>
						<th>标准分T（年级）</th>
						<th>百分等级分数（年级）</th>
						<#if is73Sub?default("")=='true'><th>赋分</th></#if>
						<th>班级排名</th>
						<th>年级排名</th>
						<th>非7选3+赋分总分</th>
						<th>非7选3+赋分总分排名</th>
					</tr>
				</thead>
				<tbody>
					<#list statList as item>
		 				<tr>
						    <td>${item_index+1}</td>
						    <td><span class="ellipsis">${item.studentCode!}</span></td>
						    <td><span class="ellipsis">${item.className!}</span></td>
						    <td><span class="ellipsis">${item.studentName!}</span></td>
						    <td>${item.score?default(0.0)?string("0.#")}</td>
						    <td>${item.scoreT?default(0.0)?string("0.#")}</td>
						    <td>${item.scoreLevel?default(0.0)?string("0.#")}</td>
						    <#if is73Sub?default("")=='true'><td>${item.conScore?default(0.0)?string("0.#")}</td></#if>
						    <td>${item.classRank!}</td>
						    <td>${item.gradeRank!}</td>
						    <td>${item.allScore?default(0.0)?string("0.#")}</td>
						    <td>${item.allRank!}</td>
						</tr>
					</#list>
				</tbody>
			</table>
			</form>
		</div>
	</div>
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
<script>
$(function(){
	layer.closeAll('dialog');
	if($('.js-scroll').offset()){
		$('.js-scroll').css({
			overflow: 'auto',
			height: $(window).innerHeight() - $('.js-scroll').offset().top - 45
		})
	}
})
</script>