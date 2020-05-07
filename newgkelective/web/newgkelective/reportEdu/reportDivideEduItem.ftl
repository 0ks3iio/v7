<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<div class="row">
	<div class="col-sm-6">
		<div class="box-echarts">
			<div class="box-echarts-title">
				<span>学校上报及分班情况</span>
			</div>
			<div class="box-echarts-body">
				<div id="chart1" style="height: 150px;margin-bottom: -86px;"></div>
				<div class="row no-margin">
					<div class="col-sm-6" >
						<div class="text-center color-grey">${reportNum?default(0)+noReportNum?default(0)}所</div>
					</div>
					<div class="col-sm-6" >
						<div class="text-center color-grey">${divideStunumber?default(0)+noDivideStunumber?default(0)}人</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="col-sm-6">
		<div class="box-echarts">
			<div class="box-echarts-title">
				<span>整体学生走班情况</span>
			</div>
			<div class="box-echarts-body">
				<div id="chart2" style="height: 150px;"></div>
			</div>
		</div>
	</div>
</div>
							
<div class="box-echarts">
	<div class="box-echarts-title">
		<span>下属学校分班情况</span>
	</div>
	<div class="box-echarts-body">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th>序号</th>
					<th>学校名称 </th>
					<th>学生人数</th>
					<th>行政班数</th>
					<th>三科固定人数(占比)</th>
					<th>两科固定人数(占比)</th>
					<th>全走班人数(占比)</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<#if schoolNameMap?? && reportList?? && reportList?size gt 0>
				<#list reportList as report>
				<tr>
					<td>${report_index+1}</td>
					<td>${schoolNameMap[report.unitId!]}</td>
					<#assign allStunumber=report.threeStunumber?default(0)+report.twoStunumber?default(0)+report.noStunumber?default(0)>
					<td>${allStunumber}</td>
					<td>${report.xzbNumber?default(0)}</td>
					<td>
						<span <#if report.threeStunumber?default(0) gt report.twoStunumber?default(0) && report.threeStunumber?default(0) gt report.noStunumber?default(0)>class="color-red"</#if>>
						${report.threeStunumber?default(0)}
						(<#if allStunumber==0>)0<#else>${(report.threeStunumber?default(0)/allStunumber)?string('0.00%')}</#if>)
						</span>
					</td>
					<td>
						<span <#if report.twoStunumber?default(0) gt report.threeStunumber?default(0) && report.twoStunumber?default(0) gt report.noStunumber?default(0)>class="color-red"</#if>>
						${report.twoStunumber?default(0)}(<#if allStunumber==0>0<#else>${(report.twoStunumber?default(0)/allStunumber)?string('0.00%')}</#if>)
						</span>
					</td>
					<td>
						<span <#if report.noStunumber?default(0) gt report.twoStunumber?default(0) && report.noStunumber?default(0) gt report.threeStunumber?default(0)>class="color-red"</#if>>
						${report.noStunumber?default(0)}
						(<#if allStunumber==0>)0<#else>${(report.noStunumber?default(0)/allStunumber)?string('0.00%')}</#if>)
						</span>
						</td>
					<td><a href="javascript:;" onClick="loadRightContentByUnitId('${report.unitId!}')">详情</a></td>
				</tr>
				</#list>
				</#if>
			</tbody>
		</table>
		<@htmlcomponent.pageToolBar2 pageInfo=pageInfo function="reload" allNum=allNum/>
	</div>
</div>
<script>

function reload(pageIndex,pageSize){
	if(!pageSize){
		pageSize = ${pageInfo.pageSize!};
	}
	if(!pageIndex){
		pageIndex = $('#pagebar li.active a').text();
	}
	$("#rightContent").load("${request.contextPath}/newgkelective/edu/divideItem/page?gradeYear=${gradeYear!}&unitId=${unit.id!}&pageSize="+pageSize+"&pageIndex="+pageIndex);
}

//学校上报及分班情况
var chart1 = echarts.init(document.getElementById("chart1"));
option1 = {
	color: ['#2b9cfe', '#b1d3ff'],
    tooltip : {
        trigger: 'item',
        formatter: "{b} : {c} ({d}%)"
    },
    calculable : true,
    series : [{
            name:'访问来源',
            type:'pie',
            radius: ['50%', '70%'],
            center: ['25%', '50%'],
            data:[
                {value:'${reportNum!}', name:'已上报'},
                {value:'${noReportNum!}', name:'未上报'},
            ]
        },{
            name:'访问来源',
            type:'pie',
            radius: ['50%', '70%'],
            center: ['75%', '50%'],
            data:[
                {value:'${divideStunumber?default(0)}', name:'已分班'},
                {value:'${noDivideStunumber?default(0)}', name:'未分班'},
            ]
        }
    ]
    <#--
    series : [
        {
            name:'访问来源',
            type:'pie',
            radius : '75%',
            center: ['30%', '55%'],
            data:[
                {value:'${reportNum!}', name:'已上报'},
                {value:'${noReportNum!}', name:'未上报'},
            ],
            itemStyle: {
				normal: {
					label : {
						formatter : '{b}{c}所'
					}
				}
			}
        },
        {
            name:'访问来源',
            type:'pie',
            radius : '75%',
            center: ['70%', '55%'],
            data:[
                {value:'${divideStunumber?default(0)}', name:'已分班'},
                {value:'${noDivideStunumber?default(0)}', name:'未分班'},
            ],
            itemStyle: {
				normal: {
					label : {
						formatter : '{b}{c}人'
					}
				}
			}
        }
    ]
    -->
};
chart1.setOption(option1);
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
		    data:["${threeStunumber!}", "${twoStunumber!}", "${noStunumber!}"]
        }
    ]
};
                    
chart2.setOption(option2);
</script>
	