<script src="${request.contextPath}/static/components/echarts/echarts.min.js"></script>
<div class="box-header" style="padding-left:10px;">
	<div class="row">
        <div class="col-sm-2" style="padding: 10px 0 0 20px;">
             <p class="color-grey" style="font-size:18px;">学生总数</p>
             <p style="font-size:32px;">${dto.allStu}</p>
        </div>
        <div class="col-sm-2" style="padding: 10px 0px 0 20px;border-right:#e5e5e5 solid 1px">
             <p class="color-grey" style="font-size:18px;">未选学生数</p>
             <p style="font-size:32px;">${dto.notChooseStu}</p>
        </div>
        <div class="col-sm-3">
             <div id="chart01" style="height:130px;"></div>
        </div>  
        <div class="col-sm-4" style="margin-top:20px;"> 
              <p class="color-blue">男生：<#if dto.chooseStu!=0>${(dto.maleNum*100/dto.chooseStu)?string('0.00')}<#else>0</#if>%（${dto.maleNum}人）</p>
              <p class="color-red">女生：<#if dto.chooseStu!=0>${(dto.femaleNum*100/dto.chooseStu)?string('0.00')}<#else>0</#if>%（${dto.femaleNum}人）</p>
        </div>
    </div>
</div>
<div class="box-header">
		<h3 class="box-title">&nbsp;&nbsp;&nbsp;&nbsp;</h3>
		<div class="box-header-tools mr10">
			<div class="btn-group js-changeView" data-toggle="buttons">
				<button id="view-rate" class="btn btn-sm btn-white active">比例统计</button>
				<button id="view-num" class="btn btn-sm btn-white">人数统计</button>
			</div>
		</div>
	</div>
<div class="box-body">
     <div class="row" id="rateDiv">
		<div class="col-sm-6"> 
		    <h3>单科选课比例</h3>
			<div id="chart04" style="height:460px;"></div>
		</div>
		<div class="col-sm-6"> 
		    <h3>三科组合选课比例</h3>
			<div id="chart05" style="height:1400px;"></div>
		</div>
    </div>
    <div class="row" id="numDiv">
		<div class="col-sm-6"> 
		    <h3>单科选课人数</h3>
			<div id="chart02" style="height:460px;"></div>
		</div>
		<div class="col-sm-6"> 
		    <h3>三科组合选课人数</h3>
			<div id="chart03" style="height:1400px;"></div>
		</div>
    </div> 
</div>
<script>
//$(function(){

hidenBreadBack();

function toChoiceList(params){
	if('${dto.choiceId!}'==''){
		layer.msg("暂无选课数据", {
							offset: 't',
							time: 2000
						});
		return;
	}
	var sids = params.data.subjectId;
	var url ="${request.contextPath}/newgkelective/${dto.choiceId!}/chosen/tabHead/page?chosenType=1&subjectIds="+sids+"&scourceType=9&unitId=${queryUnitId!}";
	$("#resultDataDiv").load(url);
}

$('#view-rate').click(function(){
	$(this).addClass('active').siblings().removeClass('active');
	$('#rateDiv').show();
	$('#numDiv').hide();
});

$('#view-num').click(function(){
	$(this).addClass('active').siblings().removeClass('active');
	$('#numDiv').show();
	$('#rateDiv').hide();
});

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
	            center: ['50%', '50%'],
	            data:[
	                {value:${dto.maleNum}, name:'男生',itemStyle:{normal:{color:'#317eeb'}}},
	                {value:${dto.femaleNum}, name:'女生',itemStyle:{normal:{color:'#ff6666'}}}
	            ]
	        }
	    ]
	 });

     var chart04 = document.getElementById('chart04');
     var str1 = '${dto.jsonStringDataStr1!}';
     var legendData1=[];
    var loadingData1=["0"];
    var loadingNumData1=["0"];
    if(str1!=''){
    	var jsonStringData1=jQuery.parseJSON(str1);
    	legendData1=jsonStringData1.legendData;
    	loadingData1=jsonStringData1.loadingPerData;
    	loadingNumData1=jsonStringData1.loadingData;
    }
    var estr1 = '${dto.eduJsonStringDataStr1!}';
     var eduLoadingData1=[];
     if(estr1 != ''){
     	var ejsonStringData1=jQuery.parseJSON(estr1);
     	eduLoadingData1 = ejsonStringData1.loadingPerData;
     } else if(legendData1.length > 0){
     	eduLoadingData1 = getDefaultData(legendData1.length);
     }
     var c04 = echarts.init( chart04 );
     c04.setOption({
	    tooltip: {
	        trigger: 'axis',
	        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
	            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	        },
	        formatter: '{b}:\n{c}%'  
	    },
	    legend: {
	        data: ['本校', '所属上级教育局']
	    },
	    grid: {
	    	top: '10%',
	        left: '2%',
	        bottom: '10%',
	        containLabel: true
	    },
	    xAxis: {
	        type: 'value',
	        position: 'top',
	        axisLabel: {  
                  show: true,  
                  interval: 'auto',  
                  formatter: '{value} %'  
                }
	    },
	    yAxis:{
            type: 'category',
            data: legendData1,
        },
	    series: [
	        {
	            name: '本校',
	            type: 'bar',
	            barCategoryGap: '30%',
	            data: loadingData1,
	            itemStyle: {   
	                normal:{  
	                    color: '#007cf9'
	                }
	            },
	            label: {
	                normal: {
	                    show: true,
	                    color: '#333',
	                    position: 'right'
	                }
	            }
	        },
	        {
	            name: '所属上级教育局',
	            type: 'bar',
	            data: eduLoadingData1,
	            itemStyle: {   
	                normal:{  
	                    color: '#ad3a96'
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
	c04.on('click', function(params){
	    toChoiceList(params);
	});
    
    var chart05 = document.getElementById('chart05');
    var str3 = '${dto.jsonStringDataStr3!}';
     var legendData3=[];
    var loadingData3=["0"];
    var loadingNumData3=["0"];
    if(str3!=''){
    	var jsonStringData3=jQuery.parseJSON(str3);
    	legendData3=jsonStringData3.legendData;
    	loadingData3=jsonStringData3.loadingPerData;
    	loadingNumData3=jsonStringData3.loadingData;
    }
    var estr3 = '${dto.eduJsonStringDataStr3!}';
     var eduLoadingData3=[];
     if(estr3 != ''){
     	var ejsonStringData3=jQuery.parseJSON(estr3);
     	eduLoadingData3 = ejsonStringData3.loadingPerData;
     } else if(legendData3.length > 0){
     	eduLoadingData3 = getDefaultData(legendData3.length);
     }
     var c05 = echarts.init( chart05 );
     c05.setOption({
	    tooltip: {
	        trigger: 'axis',
	        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
	            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	        },
	        formatter: '{b}:\n{c}%'  
	    },
	    legend: {
	        data: ['本校', '所属上级教育局']
	    },
	    grid: {
	    	top: '3%',
	        left: '2%',
	        bottom: '10%',
	        containLabel: true
	    },
	    xAxis: {
	        type: 'value',
	        position: 'top',
	        axisLabel: {  
                  show: true,  
                  interval: 'auto',  
                  formatter: '{value} %'  
                }
	    },
	    yAxis:{
            type: 'category',
            data: legendData3,
        },
	    series: [
	        {
	            name: '本校',
	            type: 'bar',
	            barCategoryGap: '30%',
	            data: loadingData3,
	             itemStyle: {   
	                normal:{  
	                    color: '#007cf9'
	                }
	            },
	            label: {
	                normal: {
	                    show: true,
	                    color: '#333',
	                    position: 'right'
	                }
	            }
	        },
	        {
	            name: '所属上级教育局',
	            type: 'bar',
	            data: eduLoadingData3,
	            itemStyle: {   
	                normal:{  
	                    color: '#ad3a96',
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
	c05.on('click', function(params){
	    toChoiceList(params);
	});
	
	var chart02 = document.getElementById('chart02');
	var c02 = echarts.init( chart02 );
	c02.setOption({
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
	            data: loadingNumData1,
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
    c02.on('click', function(params){
	    toChoiceList(params);
	});

    var chart03 = document.getElementById('chart03');
    var c03 = echarts.init( chart03 );
    c03.setOption({
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
	            data: loadingNumData3,
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
    c03.on('click', function(params){
	    toChoiceList(params);
	});
    
    $('#numDiv').hide();
//});
</script>