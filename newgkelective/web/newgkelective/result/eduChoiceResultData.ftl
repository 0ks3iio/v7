<script src="${request.contextPath}/static/components/echarts/echarts.min.js"></script>
<div class="box-header" style="padding-left:10px;">
	<div class="row">
        <div class="col-sm-2" style="padding: 10px 0 0 20px;">
             <p class="color-grey" style="font-size:18px;">学校总数</p>
             <p style="font-size:32px;">${dto.schNum}</p>
        </div>
        <div class="col-sm-2" style="padding: 10px 0 0 10px;">
             <p class="color-grey" style="font-size:18px;">学校总人数</p>
             <p style="font-size:32px;">${dto.allStu}</p>
        </div>
        <div class="col-sm-2" style="padding: 10px 0 0 10px;border-right:#e5e5e5 solid 1px">
             <p class="color-grey" style="font-size:18px;">已选总人数</p>
             <p style="font-size:32px;">${dto.chooseStu}</p>
        </div>
        <div class="col-sm-3">
             <div id="chart01" style="height:130px;"></div>
        </div>  
        <div class="col-sm-3" style="margin-top:20px;"> 
              <p class="color-blue">男生：<#if dto.chooseStu!=0>${(dto.maleNum*100/dto.chooseStu)?string('0.00')}<#else>0</#if>%（${dto.maleNum}人）</p>
              <p class="color-red">女生：<#if dto.chooseStu!=0>${(dto.femaleNum*100/dto.chooseStu)?string('0.00')}<#else>0</#if>%（${dto.femaleNum}人）</p>
        </div>
     </div>
</div>
<div class="box-body">
     <div class="row">
		<div class="col-sm-6">
			<h3>单科组合选课情况</h3>
			<div id="chart02" style="height:300px;"></div>
		</div>
		<div class="col-sm-6">
			<h3>三科组合选课情况</h3>
			<div id="chart03" style="height:1000px;"></div>
		</div>
	</div>
</div>
<script>
//$(function(){

hidenBreadBack();
	// 批次统计图
	 var chart01 = document.getElementById('chart01');
	   echarts.init( chart01 ).setOption({
	    tooltip : {
	        trigger: 'item',
	        formatter: "{a} <br/>{b} : {d}% ({c}人)"
	    },
	    series : [
	        {
	            name: '',
	            type: 'pie',
	            radius : '55%',
	            center: ['50%', '35%'],
	            data:[
	                {value:${dto.maleNum}, name:'男生',itemStyle:{normal:{color:'#317eeb'}}},
	                {value:${dto.femaleNum}, name:'女生',itemStyle:{normal:{color:'#ff6666'}}}
	            ]
	        }
	    ]
	 });

    var chart02 = document.getElementById('chart02');
    var str1 = '${dto.jsonStringDataStr1!}';
    var legendData1=[];
    var loadingData1=["0"];
    if(str1!=''){
    	var jsonStringData1=jQuery.parseJSON(str1);
    	legendData1=jsonStringData1.legendData;
    	loadingData1=jsonStringData1.loadingData;
    }
	 echarts.init( chart02 ).setOption({
	    color: ['#255e91'],
	    tooltip: {
	        trigger: 'axis',
	        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
	            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	        },
	        formatter: '{b} {c}人'
	    },
	    grid: {
	    	top: '10',
	        left: '2%',
	        bottom: '10%',
	        containLabel: true
	    },
	    xAxis: {
	        show : false
	    },
	    yAxis:{
            type: 'category',
            data: legendData1,
            axisTick: {
                alignWithLabel: true
            }
        },
	    series: [
	        {
	            name: '选课人数',
	            type: 'bar',
	            barCategoryGap: '30%',
	            data: loadingData1,
	            itemStyle: {   
	                normal:{  
	                    color: function (params){
	                        var colorList = ['#5b9bd5','#ed7d31','#a5a5a5','#ffc000','#4472c4','#70ad47','#255e91'];
	                        return colorList[params.dataIndex];
	                    }
	                }
	            },
	            label: {
	                normal: {
	                    show: true,
	                    color: '#333',
	                    position: 'right'
	                }
	            }
	        }
	   ]
    });

    var chart03 = document.getElementById('chart03');
    var str3 = '${dto.jsonStringDataStr3!}';
    var legendData3=[];
    var loadingData3=["0"];
    if(str3!=''){
    	var jsonStringData3=jQuery.parseJSON(str3);
    	legendData3=jsonStringData3.legendData;
    	loadingData3=jsonStringData3.loadingData;
    }
	echarts.init( chart03 ).setOption({
	    color: ['#255e91'],
	    tooltip: {
	        trigger: 'axis',
	        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
	            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	        },
	        formatter: '{b} {c}人'
	    },
	    grid: {
	    	top: '10',
	        left: '2%',
	        bottom: '10%',
	        containLabel: true
	    },
	    xAxis: {
	        show : false,
	    },
	    yAxis:{
            type: 'category',
            data: legendData3,
            axisTick: {
                alignWithLabel: true
            }
        },
	    series: [
	        {
	            name: '选课人数',
	            type: 'bar',
	            barCategoryGap: '40%',
	            data: loadingData3,
	            itemStyle: {   
	                normal:{  
	                    color: function (params){
	                        var colorList = ['#4472c4','#70ad47','#d26012','#848484','#cc9a00','#335aa1','#5a8a39','#9dc3e6','#f4b183','#c9c9c9','#ffd966','#8faadc','#a9d18e','#1f4e79','#843c0b','#535353','#7f6000','#203864','#327dc2','#d26012','#848484','#cc9a00','#335aa1','#5a8a39','#9dc3e6','#f4b183','#c9c9c9','#ffd966','#8faadc','#a9d18e','#1f4e79','#843c0b','#535353','#7f6000','#203864'];
	                        return colorList[params.dataIndex];
	                    }
	                }
	            },
	            label: {
	                normal: {
	                    show: true,
	                    color: '#333',
	                    position: 'right'
	                }
	            }
	        }
	   ]
    });
//});

</script>
	
