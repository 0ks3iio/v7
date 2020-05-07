<div class="row">
	<div class="col-sm-6">
		<div class="box-echarts">
			<div class="box-echarts-title">
				<span>基本信息</span>
			</div>
			<div class="box-echarts-body">
				<ul class="statistics-list clearfix">
					<li class="statistics-item">
						<div class="statistics-media group"><i class="group"></i></div>
						<div class="statistics-num">${report.threeStunumber?default(0)+report.twoStunumber?default(0)+report.noStunumber?default(0)}</div>
						<div class="statistics-txt">学生数(人)</div>
					</li>
					<li class="statistics-item">
						<div class="statistics-media"><i class="xing"></i></div>
						<div class="statistics-num">${report.xzbNumber!}</div>
						<div class="statistics-txt">行政班(班)</div>
					</li>
				</ul>
			</div>
		</div>
	</div>
	<div class="col-sm-6">
		<div class="box-echarts">
			<div class="box-echarts-title">
				<span>学生走班情况</span>
			</div>
			<div class="box-echarts-body">
				<div id="chart2" style="height: 150px;"></div>
			</div>
		</div>
	</div>
</div>

<div class="row">
	<div class="col-sm-6">
		<div class="box-echarts">
			<div class="box-echarts-title">
				<span>选考开班情况</span>
			</div>
			<div class="box-echarts-body">
				<table class="table table-bordered table-striped table-hover">
					<thead>
						<tr>
							<th>选考</th>
							<th>总人数</th>
							<th>行政班</th>
							<th>教学班</th>
							<th>合计</th>
						</tr>
					</thead>
					<tbody>
						<#if aList?exists && aList?size gt 0>
						<#list aList as item>
						<tr>
							<td>${courseNameMap[item.subjectId!]}</td>
							<td>${item.studentNumber!}</td>
							<td>${item.xzbNumber!}</td>
							<td>${item.jxbNumber!}</td>
							<td>${item.xzbNumber?default(0)+item.jxbNumber?default(0)}</td>
						</tr>
						</#list>
						</#if>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div class="col-sm-6">
		<div class="box-echarts">
			<div class="box-echarts-title">
				<span>学考开班情况</span>
			</div>
			<div class="box-echarts-body">
				<table class="table table-bordered table-striped table-hover">
					<thead>
						<tr>
							<th>选考</th>
							<th>总人数</th>
							<th>行政班</th>
							<th>教学班</th>
							<th>合计</th>
						</tr>
					</thead>
					<tbody>
						<#if bList?exists && bList?size gt 0>
						<#list bList as item>
						<tr>
							<td>${courseNameMap[item.subjectId!]}</td>
							<td>${item.studentNumber!}</td>
							<td>${item.xzbNumber!}</td>
							<td>${item.jxbNumber!}</td>
							<td>${item.xzbNumber?default(0)+item.jxbNumber?default(0)}</td>
						</tr>
						</#list>
						</#if>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>

<script>
//整体学生走班情况
var chart2 = echarts.init(document.getElementById("chart2"));
option2 = {
    tooltip : {
        trigger: 'axis',
		axisPointer: { // 坐标轴指示器，坐标轴触发有效
			type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
		}
    },
    grid: {
		x: 60,
		x2: 40,
		y: 10,
		y2: 20
	},
    calculable : true,
    xAxis : [
        {
           type : 'value' 
        }
    ],
    yAxis : [
        {
           type : 'category',
           data : ['三科固定','两科固定','全走班']
        }
    ],
    series : [
        {
            name:'学生人数',
            type:'bar',
		    itemStyle: {
	            normal: {
	                color: function(params) { 
	                    var colorList = ['#2b9cfe','#47c6a4','#f8bd48' ]; 
	                    return colorList[params.dataIndex] 
	                },
	                label: {
				    	show: true,
				    	position: 'right',
				    	formatter: '{c}'
				    }
	            }
	        },
		    data:["${report.threeStunumber!}", "${report.twoStunumber!}", "${report.noStunumber!}"]
        }
    ]
};
                    
chart2.setOption(option2);
</script>				