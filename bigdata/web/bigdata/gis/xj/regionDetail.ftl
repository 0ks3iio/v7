<div class="map-right-part">
	<div class="right-part-head">
		<b>${currentRegion.regionName!}</b>
	</div>
	<div class="table-wrap-data">
		<table class="table table-striped table-hover">
			<thead>
				<tr>
					<th>教育阶段</th>
					<th>校数</th>
					<th>班数</th>
					<th>在校生数</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>小学教育</td>
					<td><#if regionStat.xx_xs?default(0) gt 0>${regionStat.xx_xs?default("/")}<#else>/</#if></td>
					<td><#if regionStat.xx_bs?default(0) gt 0>${regionStat.xx_bs?default("/")}<#else>/</#if></td>
					<td><#if regionStat.xx_zxss?default(0) gt 0>${regionStat.xx_zxss?default("/")}<#else>/</#if></td>
				</tr>
				<tr>
					<td>初中教育</td>
					<td><#if regionStat.cz_xs?default(0) gt 0>${regionStat.cz_xs?default("/")}<#else>/</#if></td>
                    <td><#if regionStat.cz_bs?default(0) gt 0>${regionStat.cz_bs?default("/")}<#else>/</#if></td>
                    <td><#if regionStat.cz_zxss?default(0) gt 0>${regionStat.cz_zxss?default("/")}<#else>/</#if></td>
				</tr>
				<tr>
					<td>高中教育</td>
					<td><#if regionStat.gz_xs?default(0) gt 0>${regionStat.gz_xs?default("/")}<#else>/</#if></td>
                    <td><#if regionStat.gz_bs?default(0) gt 0>${regionStat.gz_bs?default("/")}<#else>/</#if></td>
                    <td><#if regionStat.gz_zxss?default(0) gt 0>${regionStat.gz_zxss?default("/")}<#else>/</#if></td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<div class="right-part-data">
		<div class="right-part-title">
			<b>学生民汉概览<b>
		</div>
		<div class="clearfix">
			<div class="fl-half">
				<div class="echart-part echart-part-one"></div>
			</div>
			<div class="fl-half">
				<div class="flex-center">
					<div class="progress-item-wrap">
        				<div class="progress-wrap progress-red-one clearfix">
        					<div class="progress-name float-left">汉族</div>
        					<div class="progress float-left">
							  	<div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: ${regionStat.studnationHz?default(0)}%;"></div>
							</div>
							<div class="progress-num float-left">${regionStat.studnationHz?default(0)}%</div>
        				</div>
						<div class="progress-wrap progress-yellow-one clearfix">
        					<div class="progress-name float-left">维吾尔族</div>
        					<div class="progress float-left">
							  	<div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: ${regionStat.studnationWwez?default(0)}%;"></div>
							</div>
							<div class="progress-num float-left">${regionStat.studnationWwez?default(0)}%</div>
        				</div>
        				<div class="progress-wrap progress-blue-one clearfix">
        					<div class="progress-name float-left">其他</div>
        					<div class="progress float-left">
							  	<div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: ${regionStat.studnationOther?default(0)}%;"></div>
							</div>
							<div class="progress-num float-left">${regionStat.studnationOther?default(0)}%</div>
        				</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="right-part-data">
		<div class="right-part-title">
			<b>教师民汉概览</b>
		</div>
		<div class="clearfix">
			<div class="fl-half">
				<div class="echart-part echart-part-two"></div>
			</div>
			<div class="fl-half">
				<div class="flex-center">
					<div class="progress-item-wrap">
        				<div class="progress-wrap progress-red-one clearfix">
        					<div class="progress-name float-left">汉族</div>
        					<div class="progress float-left">
							  	<div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: ${regionStat.teacnationHz?default(0)}%;"></div>
							</div>
							<div class="progress-num float-left">${regionStat.teacnationHz?default(0)}%</div>
        				</div>
						<div class="progress-wrap progress-yellow-one clearfix">
        					<div class="progress-name float-left">维吾尔族</div>
        					<div class="progress float-left">
							  	<div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: ${regionStat.teacnationWwez?default(0)}%;"></div>
							</div>
							<div class="progress-num float-left">${regionStat.teacnationWwez?default(0)}%</div>
        				</div>
        				<div class="progress-wrap progress-blue-one clearfix">
        					<div class="progress-name float-left">其他</div>
        					<div class="progress float-left">
							  	<div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: ${regionStat.teacnationOther?default(0)}%;"></div>
							</div>
							<div class="progress-num float-left">${regionStat.teacnationOther?default(0)}%</div>
        				</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
 $(function(){
	var option1 = {
					color: [ '#d042a4','#597ef7'],
					title : {
				        text: '学生民汉比例',
				        x:'center',
				        textStyle:{
					        //文字颜色
					        color:'#FamDearPlanService',
					        //字体风格,'normal','italic','oblique'
					        fontStyle:'normal',
					        //字体粗细 'normal','bold','bolder','lighter',100 | 200 | 300 | 400...
					        fontWeight:'100',
					        //字体大小
					　　fontSize:12
					    }
				    },
					legend: {
						textStyle: {
							color: '#FamDearPlanService'
						},
						orient: 'horizontal', // 'vertical'
				        x: 'center', // 'center' | 'left' | {number},
				        y: 'bottom', // 'center' | 'bottom' | {number}
						data: ['汉族', '少数民族']
					},
				    tooltip : {
				        trigger: 'item',
				        formatter: "{b}: {c} ({d}%)"
				    },
				    grid: {
				    	top: '30',
				        left: '3%',
				        right: '4%',
				        bottom: '20',
				        containLabel: true
				    },
				    series : [
				        {
				        	name: '学生人数',
				            type:'pie',
				            radius: ['50%', '70%'],
				            avoidLabelOverlap: false,
				            labelLine: {
					            normal: {
					                show: false
					            }
					        },
					        label: { //标签的位置
					            normal: {
					                show: false
					            },
					            emphasis: {
					                show: false,
					                textStyle: {
					                    fontWeight: 'bold'
					                }
					            }
					        },
				            data: [
				            	{name: '汉族', value: ${regionStat.studnationHzmz?default(0)}},
				            	{name: '少数民族', value: ${regionStat.studnationSsmz?default(0)}}
				            ]
				        }
				    ]
				};
	var option2 = {
					color: [ '#ee913a','#3bb7f0'],
					title : {
				        text: '教师民汉比例',
				        x:'center',
				        textStyle:{
					        //文字颜色
					        color:'#FamDearPlanService',
					        //字体风格,'normal','italic','oblique'
					        fontStyle:'normal',
					        //字体粗细 'normal','bold','bolder','lighter',100 | 200 | 300 | 400...
					        fontWeight:'100',
					        //字体大小
					　　fontSize:12
					    }
				    },
					legend: {
						textStyle: {
							color: '#FamDearPlanService'
						},
						orient: 'horizontal', // 'vertical'
				        x: 'center', // 'center' | 'left' | {number},
				        y: 'bottom', // 'center' | 'bottom' | {number}
						data: ['汉族', '少数民族']
					},
				    tooltip : {
				        trigger: 'item',
				        formatter: "{b}: {c} ({d}%)"
				    },
				    grid: {
				    	top: '30',
				        left: '3%',
				        right: '4%',
				        bottom: '20',
				        containLabel: true
				    },
				    series : [
				        {
				        	name: '教师人数',
				            type:'pie',
				            radius: ['50%', '70%'],
				            avoidLabelOverlap: false,
				              labelLine: {
					            normal: {
					                show: false
					            }
					        },
					        label: { //标签的位置
					            normal: {
					                show: false
					            },
					            emphasis: {
					                show: false,
					                textStyle: {
					                    fontWeight: 'bold'
					                }
					            }
					        },
				            data: [
				            	{name: '汉族', value: ${regionStat.teacnationHzmz?default(0)}},
				            	{name: '少数民族', value: ${regionStat.teacnationSsmz?default(0)}}
				            ]
				        }
				    ]
				};
	var echart1 = echarts.init($('.echart-part-one')[0]);
	var echart2 = echarts.init($('.echart-part-two')[0]);
	echart1.setOption(option1);
	echart2.setOption(option2);
 })
</script>