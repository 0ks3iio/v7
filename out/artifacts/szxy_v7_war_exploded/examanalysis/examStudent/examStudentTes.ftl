<script type="text/javascript" src="${request.contextPath}/static/echarts/echarts.min.js"></script>
<div id="aa" class="tab-pane fade active in">
	<div class="filter"> 
        <div id="mychart01" style="width:100%;height:430px;"></div> 
    </div>
	<table class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th class="text-center">类别</th>
				<th class="text-center">个人得分</th>
				<th class="text-center">年级平均分</th>
				<th class="text-center">班级平均分</th>
				<th class="text-center">班级最高分</th>
				<th class="text-center">班级最低分</th>
				<th class="text-center">年级排名</th>
				<th class="text-center">班级排名</th>
			</tr>
		</thead>
		<tbody>
		<#if examStudentDtosList?exists && (examStudentDtosList?size > 0)>
			<#list examStudentDtosList as dto>
			<tr>
				<td class="text-center">${dto.subjectName!}</td>
				<td class="text-center">${dto.total!}</td>
				<td class="text-center">${dto.gradeAverage!}</td>
				<td class="text-center">${dto.classAverage!}</td>
				<td class="text-center">${dto.classMax!}</td>
				<td class="text-center">${dto.classMin!}</td>
				<td class="text-center">${dto.gradeRanking!}</td>
				<td class="text-center">${dto.classRanking!}</td>
			</tr>
			</#list>
		</#if>
		</tbody>
	</table>
</div>
<script>
	if(document.getElementById("mychart01")) {
		// 基于准备好的dom，初始化echarts实例
		var myChart1 = echarts.init(document.getElementById("mychart01"));
		$(window).on('resize',function(){myChart1.resize();});
		// 指定图表的配置项和数据
		var option1 = {
	    	title: {
				text: '${studnetName!}的成绩分析'
			},
			tooltip : {
				trigger: 'axis',
				axisPointer : {            // 坐标轴指示器，坐标轴触发有效
					type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			legend: {
				data:['年级平均分','班级平均分','个人得分']
			},
			grid: {
				left: '3%',
				right: '4%',
				bottom: '3%',
				containLabel: true
			},
			xAxis : [
				{
					type : 'category',
					data : ${subjectNames!}
				}
			],
			yAxis : [
				{
					type : 'value'
				}
			],
			series : [
				{
					name:'年级平均分',
					type:'bar',
					data:${gradeAverage!}
				},
				{
					name:'班级平均分',
					type:'bar',
					data:${classAverage!}
				},
				{
					name:'个人得分',
					type:'bar',
					data:${ownScores!}
				}
			]
		};
		// 使用刚指定的配置项和数据显示图表
		myChart1.setOption(option1);
	} 
</script>