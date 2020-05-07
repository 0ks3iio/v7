<#if schoolId =="">
<div class="wrap-1of1 centered no-data-state">
	<div class="text-center">
		<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-50.png"/>
		<p>暂无数据</p>
	</div>
</div>
<#else>
<div class="map-right-part">
	<div class="right-part-head">
		<b>${schoolName!}</b>
	</div>
<div class="right-part-data">
		<div class="right-part-title">
			<b>学校概况</b>
		</div>
		<div class="clearfix">
			<p>办学类型：${schoolStat.bxlx?default("/")}</p>
			<div class="fl-half">
				<p>在校学生：${schoolStat.zxxs?default("/")}</p>
				<p>毕业生数：${schoolStat.byss?default("/")}</p>
				<p>教职工数：${schoolStat.jzgs?default("/")}</p>
				<p>班数：${schoolStat.bs?default("/")}</p>
				<p>联系人：${schoolStat.linkman?default("/")}</p>
				<p>联系方式：${schoolStat.telephone?default("/")}</p>
				<p>地址：${schoolStat.address?default("/")}</p>
			</div>
			<div class="fl-half">
				<p>招生数：${schoolStat.zss?default("/")}</p>
				<p>寄宿生数：${schoolStat.jsss?default("/")}</p>
				<p>专任教师数：${schoolStat.zrjss?default("/")}</p>
				<p>教室数：${schoolStat.jss?default("/")}</p>
			</div>
		</div>
	</div>
	
	<div class="right-part-data">
		<div class="right-part-title">
			<b>学生、教师民汉概览</b>
		</div>
		<div class="clearfix">
			<div class="fl-half">
				<div class="echart-part echart-part-one"></div>
			</div>
			<div class="fl-half">
				<div class="echart-part echart-part-two"></div>
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
	            type:'pie',
	            radius: ['50%', '70%'],
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
	            	{name: '汉族', value: ${schoolStat.studnationHz?default(0)}},
	            	{name: '少数民族', value:${schoolStat.studnationSsmz?default(0)}}
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
	            type:'pie',
	            radius: ['50%', '70%'],
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
	            	{name: '汉族', value: ${schoolStat.teacnationHz?default(0)}},
	            	{name: '少数民族', value: ${schoolStat.teacnationSsmz?default(0)}}
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
</#if>