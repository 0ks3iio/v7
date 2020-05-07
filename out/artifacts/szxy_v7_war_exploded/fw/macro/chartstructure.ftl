<#--
echarts宏应用
-->

<#--
柱状图	OR	折线图
isLine	是否折线图
isSmooth	折线情况下连线是否为曲线
isAreaStyle	折线情况下是否显示区域
isXYexchange	是否xy轴互换
isStack 是否叠加
isDataZoom	是否需要数据区域缩放
loadingDivId 载体div的id，不填就默认，若同一个页面中有多个则必须区分
jsonStringData json格式字符串其中必须包括（‘legendData’图例说明）（‘xAxisData’x轴分类）（‘loadingData’数据）
*后台数据组装例如：json的key必须如下
JSONObject json=new JSONObject();
json.put("text", "柱状图 OR 折线图");//主标题	可不填写
json.put("subtext", "柱状图 OR 折线图副标题");//副标题	可不填写
json.put("legendData", new String[]{"百度","谷歌","必应","其他"});
//json.put("stackData", new String[]{"搜索引擎","搜索引擎","搜索引擎",""});//用于堆叠效果，index对应legendData，名称相同的堆叠在一起，不用堆叠可不填写
//json.put("yMax", 400);//y轴最大值	可不填写
//json.put("yInterval", 40);//分隔断值	可不填写
json.put("xAxisData", new String[]{"周一","周二","周三","周四","周五","周六","周日"});
//loadingData数据顺序和xAxisData相对
json.put("loadingData", new Integer[][]{{620, 732, 701, 734, 1090, 1130, 1120}
,{120, 132, 101, 134, 290, 230, 220},{60, 72, 71, 74, 190, 130, 110},{62, 82, 91, 84, 109, 110, 120}});
//注：Integer类型也可为Double
jsonStringData=json.toString();

units	计量单位可以为%
titleFontSize	title字体大小
isDifferentColour	柱状是否不同颜色（单组数据下生效）
barWidth	柱状宽度
barGap	柱间距离，默认为柱形宽度的30%
dataViewReadOnly	数据是否可以临时修改
isShowLegend	是否显示图例
isShowDataLabel	是否显示数据label
isShowToolbox	是否显示工具箱
isNeedAverage	是否需要显示平均值
isNeedMax	是否需要显示最大值
isNeedMin	是否需要显示最小值
color写法"['#b2d8fc','#bcdf69', '#deb968', '#e0796a', '#ee99c3','#a599ef',  '#ffa96e', '#cb92f3','#6791e7', '#89e7b2', '#efd87a']"
brLength x轴显示字符是否换行  0:换行 >0 以brLength个字符换行
rotateNum x轴显示角度 0:整除 负数 逆时针 正数 顺时针
-->
<#macro histogram isLine=false isSmooth=false isStack=false isAreaStyle=false isXYexchange=false isDataZoom=false units="" titleFontSize=18 loadingDivId="loadingDivId" divClass="" divStyle="width: 600px;height: 400px;" jsonStringData="" isDifferentColour=false barWidth=0 barGap="30%" dataViewReadOnly=false isShowLegend=true isShowDataLabel=true isShowToolbox=true isNeedAverage=false isNeedMax=false isNeedMin=false color="" brLength=3 rotateNum=-50>
<div id="${loadingDivId}" class="${divClass}" style="${divStyle}"></div>
<script type="text/javascript">
	<#--该方法可用于异步加载数据-->
	var ${loadingDivId}myChart = echarts.init(document.getElementById('${loadingDivId}'));
	function ${loadingDivId}changeData(jsonStringData){
		var ${loadingDivId}chartObj=jQuery.parseJSON(jsonStringData);
		var ${loadingDivId}itemStyle=
		{
			normal: {
				borderRadius: 5,
				label : {
					<#if isShowDataLabel>
					show : true,
					<#else>
					show : false,
					</#if>
					<#if isXYexchange>
						position:'right',
					<#else>
						position:'top',
					</#if>
					textStyle : {
						fontSize : '12',
						fontFamily : '微软雅黑',
						fontWeight : 'bold'
					},
					formatter : '{c}${units}'
				}
			}
		};
		var ${loadingDivId}markPoint=
		{
			data : [
				<#if isNeedMax>
				{type : 'max', name: '最大值'},
				</#if>
				<#if isNeedMin>
				{type : 'min', name: '最小值'}
				</#if>
			]
		};
		var ${loadingDivId}markLine=
		{
			data : [
				<#if isNeedAverage>
				{type : 'average', name: '平均值'}
				</#if>
			]
		};
		var ${loadingDivId}legendData=${loadingDivId}chartObj.legendData;
		var ${loadingDivId}xAxisData=${loadingDivId}chartObj.xAxisData;
		var ${loadingDivId}loadingData=${loadingDivId}chartObj.loadingData;
		var ${loadingDivId}stackData=${loadingDivId}chartObj.stackData;
		if(${loadingDivId}stackData==null){
			${loadingDivId}stackData=[];
		}else{
			${loadingDivId}itemStyle.normal.label.position='inside';
		}
		var ${loadingDivId}series=[];
		<#if isDifferentColour>
			if(${loadingDivId}legendData.length==1){
				var ${loadingDivId}data=[];
				for(var i=0;i<${loadingDivId}loadingData[0].length;i++){
					var rr=parseInt(Math.random()*256);
					var gg=parseInt(Math.random()*256);
					var bb=parseInt(Math.random()*256);
					${loadingDivId}data[i]={
						value:${loadingDivId}loadingData[0][i],
						itemStyle:{
							normal:{color:'rgb('+rr+','+gg+','+bb+')'}
						}
					};
				}
				${loadingDivId}series[0]={
					name: ${loadingDivId}legendData[0],
					stack:${loadingDivId}stackData[0],
					<#if isLine>
					type: 'line',
					<#if isSmooth>
					smooth:true,
					</#if>
					<#else>
					type: 'bar',
					</#if>
					<#if isAreaStyle>
					areaStyle: {normal: {}},
					</#if>
					barGap:'${barGap}',
					barWidth:${barWidth},
					data: ${loadingDivId}data,
					itemStyle: ${loadingDivId}itemStyle,
					markPoint : ${loadingDivId}markPoint,
					markLine : ${loadingDivId}markLine
				};
			}else{
				for(var i=0;i<${loadingDivId}legendData.length;i++){
					${loadingDivId}series[i]={
						name: ${loadingDivId}legendData[i],
						stack:${loadingDivId}stackData[i],
						<#if isLine>
						type: 'line',
						<#if isSmooth>
						smooth:true,
						</#if>
						<#else>
						type: 'bar',
						</#if>
						<#if isAreaStyle>
						areaStyle: {normal: {}},
						</#if>
						barGap:'${barGap}',
						barWidth:${barWidth},
						data: ${loadingDivId}loadingData[i],
						itemStyle: ${loadingDivId}itemStyle,
						markPoint : ${loadingDivId}markPoint,
						markLine : ${loadingDivId}markLine
					}
				};
			}
		<#else>
			for(var i=0;i<${loadingDivId}legendData.length;i++){
				${loadingDivId}series[i]={
					name: ${loadingDivId}legendData[i],
					stack:${loadingDivId}stackData[i],
					<#if isLine>
					type: 'line',
					<#if isSmooth>
					smooth:true,
					</#if>
					<#else>
					type: 'bar',
					</#if>
					<#if isAreaStyle>
					areaStyle: {normal: {}},
					</#if>
					barGap:'${barGap}',
					barWidth:${barWidth},
					data: ${loadingDivId}loadingData[i],
					itemStyle: ${loadingDivId}itemStyle,
					markPoint : ${loadingDivId}markPoint,
					markLine : ${loadingDivId}markLine
				}
			};
		</#if>
		var ${loadingDivId}option = {
			title: {
				textStyle:{
					fontSize:${titleFontSize}
				},
				x:'center',
				text: ${loadingDivId}chartObj.text,
				subtext: ${loadingDivId}chartObj.subtext
			},
			<#if isStack>
				stack: 'sum',
			</#if>
			animation:true,<#--开启动画效果-->
			toolbox: {
				<#if isShowToolbox>
				show : true,
				<#else>
				show : false,
				</#if>
				itemGap:15,
				orient :'vertical',
				feature : {
					mark : {show: true},
					<#if dataViewReadOnly>
						dataView : {show: true, readOnly:false },
					<#else>
						dataView : {show: true, readOnly:true },
					</#if>
					magicType : {show: true, type: ['line', 'bar']},
					restore : {show: true},
					saveAsImage : {show: true}
				}
			},
			grid:{
				left: '50px',
       			right: '50px'
			},
			tooltip: {
				trigger: 'axis',
				axisPointer : {
					type : 'shadow'
				}
			},
			legend: {
				bottom:'bezier-curve',<#--不写 也是显示在上面 0% 显示在下面-->
				<#if !isShowLegend>
				show:false,
				<#else>
				show:true,
				</#if>
				data: ${loadingDivId}legendData
			},
			<#if isXYexchange>
			xAxis: {
				axisLabel : {
					formatter: '{value}${units}'
				}
			},
			yAxis: {
				type: 'category',
				data: ${loadingDivId}xAxisData
			},
			<#else>
			xAxis: {
				type: 'category',
				data: ${loadingDivId}xAxisData,
				axisLabel:{
					formatter:function(val){
						if(val && val !=''){
							<#if brLength gt 0>
							var brLength=${brLength};
							var arrays = val.split("");
							var str = "";
							for(var i=0;i<arrays.length;i++){
								str += arrays[i];
								if(i%brLength==0 && i>0){
									str += "\n";
								}
							}
							return str;
							<#else>
							 return val;
							</#if>
							
						   
						}else{
							return val;
						}
					},
					interval:0,<#--横轴信息全部显示  -->
                    rotate:${rotateNum} <#-- -50度角倾斜显示-->
				}
			},
			yAxis: {
				axisLabel : {
					formatter: '{value}${units}'
				}
			},
			</#if>
			<#if isDataZoom>
			dataZoom: [{
				type: 'inside',
			}, {
			}],
			</#if>
			series: ${loadingDivId}series,
			<#if color?default('')!=''>
			color: ${color}
			</#if>
		};
		var ${loadingDivId}yMax=${loadingDivId}chartObj.yMax;
		var ${loadingDivId}yInterval=${loadingDivId}chartObj.yInterval;
		<#if isXYexchange>
			if(${loadingDivId}yMax!=null){
				${loadingDivId}option.xAxis.max=${loadingDivId}yMax;
			}
			if(${loadingDivId}yInterval!=null){
				${loadingDivId}option.xAxis.interval=${loadingDivId}yInterval;
			}
		<#else>
			if(${loadingDivId}yMax!=null){
				${loadingDivId}option.yAxis.max=${loadingDivId}yMax;
			}
			if(${loadingDivId}yInterval!=null){
				${loadingDivId}option.yAxis.interval=${loadingDivId}yInterval;
			}
		</#if>
		${loadingDivId}myChart.setOption(${loadingDivId}option);
	}
	$(document).ready(function(){
		<#if jsonStringData?default('')!=''>
			${loadingDivId}changeData('${jsonStringData}');
		</#if>
	});
</script>
</#macro>

<#--
饼状图
loadingDivId 载体div的id，不填就默认，若同一个页面中有多个则必须区分
jsonStringData json格式字符串其中必须包括（‘legendData’图例说明）（‘loadingData’数据）
*后台数据组装例如：json的key必须如下
JSONObject json=new JSONObject();
json.put("text", "饼图");//主标题	可不填写
json.put("subtext", "饼图副标题");//副标题	可不填写
json.put("legendData", new String[]{"直接访问","邮件营销","联盟广告"});
JSONArray jsonArr=new JSONArray();
JSONObject json2=new JSONObject();
json2.put("value", 335);
json2.put("name", "直接访问");
jsonArr.add(json2);
json2=new JSONObject();
json2.put("value", 310);
json2.put("name", "邮件营销");
jsonArr.add(json2);
json2=new JSONObject();
json2.put("value", 234);
json2.put("name", "联盟广告");
jsonArr.add(json2);
json.put("loadingData", jsonArr);
jsonStringData=json.toString();

units	计量单位可以为%
radius	 图大小
center	图圆心所在位置"['x%', 'y%']"
titleFontSize	title字体大小
dataViewReadOnly	数据是否可以临时修改
isShowLegend	是否显示图例
isShowToolbox	是否显示工具箱
isShowDataLabel	是否显示数据label
dataDisplay	数据label显示格式 例如："{b}：{c}\\n（{d}%）" 显示为："直接访问:335\n(n%)" 其中\n为换行

color写法"['#b2d8fc','#bcdf69', '#deb968', '#e0796a', '#ee99c3','#a599ef',  '#ffa96e', '#cb92f3','#6791e7', '#89e7b2', '#efd87a']"
-->
<#macro pieChart loadingDivId="loadingDivId" divClass="" divStyle="width: 600px;height: 400px;" jsonStringData="" units="" radius="67%" center="['50%', '50%']" titleFontSize=18 dataViewReadOnly=false isShowLegend=true isShowToolbox=true isShowDataLabel=true dataDisplay="{b}：{c}\\n（{d}%）" color="">
<div id="${loadingDivId}" class="${divClass}" style="${divStyle}"></div>
<script type="text/javascript">
	<#--该方法可用于异步加载数据-->
	var ${loadingDivId}myChart = echarts.init(document.getElementById('${loadingDivId}'));
	function ${loadingDivId}changeData(jsonStringData){
		var ${loadingDivId}chartObj=jQuery.parseJSON(jsonStringData);
		var ${loadingDivId}itemStyle=
		{
			normal: {
				borderRadius: 5,
				label : {
					<#if isShowDataLabel>
					show : true,
					<#else>
					show : false,
					</#if>
					position:'outer',
					textStyle : {
						fontSize : '12',
						fontFamily : '微软雅黑',
						fontWeight : 'bold'
					},
					formatter : '${dataDisplay}'
				}
			}
		};
		var ${loadingDivId}legendData=${loadingDivId}chartObj.legendData;
		var ${loadingDivId}loadingData=${loadingDivId}chartObj.loadingData;
		var ${loadingDivId}series=
		[
			{
				name: "数据",
				type:'pie',
				radius : '${radius}',
				center: ${center},
				data: ${loadingDivId}loadingData,
				itemStyle: ${loadingDivId}itemStyle
			}
		];
		var ${loadingDivId}option = {
			title: {
				textStyle:{
					fontSize:${titleFontSize}
				},
				x:'center',
				text: ${loadingDivId}chartObj.text,
				subtext: ${loadingDivId}chartObj.subtext
			},
			tooltip: {
				position: ['10%', '50%'],
				trigger: 'item',
				formatter: '{b}：{c}${units}（{d}%）'
			},
			animation:true,<#--开启动画效果-->
			toolbox: {
				<#if isShowToolbox>
				show : true,
				<#else>
				show : false,
				</#if>
				itemGap:15,
				orient :'vertical',
				feature : {
					mark : {show: true},
					<#if dataViewReadOnly>
						dataView : {show: true, readOnly:false },
					<#else>
						dataView : {show: true, readOnly:true },
					</#if>
					restore : {show: true},
					saveAsImage : {show: true}
				}
			},
			legend: {
				bottom:'0%',
				orient:'vertical',
				right: 'right',
				top:'0%',
				<#if !isShowLegend>
				show:false,
				<#else>
				show:true,
				</#if>
			    formatter: function (name) {
				    if(name && name.length > 4){
				    	return name.substring(0,4) + '...';
				    }else{
			        	return name;
			        }
			    },
			    tooltip: {
			        show: true
			    },
				data: ${loadingDivId}legendData
			},
			series: ${loadingDivId}series,
			<#if color?default('')!=''>
			color: ${color}
			</#if>
		};
		${loadingDivId}myChart.setOption(${loadingDivId}option);
	}
	$(document).ready(function(){
		<#if jsonStringData?default('')!=''>
			${loadingDivId}changeData('${jsonStringData}');
		</#if>
	});
</script>
</#macro>

<#--
散点图
isDataZoom	是否需要数据区域缩放
loadingDivId 载体div的id，不填就默认，若同一个页面中有多个则必须区分
jsonStringData json格式字符串其中必须包括（‘xName’x轴名称）（‘yName’y轴名称）（‘legendData’图例说明）（‘loadingData’数据）
*后台数据组装例如：json的key必须如下
JSONObject json=new JSONObject();
json.put("text", "散点图");//可不填写
json.put("subtext", "散点图副标题");//可不填写
json.put("xName", "身高");//x轴名称
json.put("yName", "体重");//y轴名称
json.put("legendData", new String[]{"女性","男性"});
JSONArray jsonArr=new JSONArray();
JSONArray jsonArr2=new JSONArray();
jsonArr2.add(new String[][]{{"161.2", "51.6","A1"}, {"167.5", "59.0","A2"}, {"159.5", "49.2","A3"}, {"157.0", "63.0","A4"}, {"155.8", "53.6","A5"},
{"169.5", "67.3","A6"}, {"160.0", "75.5","A7"}, {"172.7", "68.2","A8"}, {"162.6", "61.4","A9"}, {"157.5", "76.8","A10"}});
JSONArray jsonArr3=new JSONArray();
jsonArr3.add(new String[][]{{"174.0", "65.6","B1"}, {"175.3", "71.8","B2"}, {"193.5", "80.7","B3"}, {"186.5", "72.6","B4"}, {"187.2", "78.8","B5"},
{"180.3", "73.2","B6"}, {"167.6", "76.3","B7"}, {"183.0", "65.9","B8"}, {"183.0", "90.9","B9"}, {"179.1", "89.1","B10"}});
jsonArr.add(jsonArr2);
jsonArr.add(jsonArr3);
json.put("loadingData", jsonArr);
//数据标线-适用于单图例及只有一组数据，若不需要则不用写下面内容
JSONObject json2=new JSONObject();
json2.put("name", "重点线");
json2.put("value", 90);
json.put("horizontalLine", json2);//水平线，则需要设置x轴最小值和最大值
json2=new JSONObject();
json2.put("name", "基线");
json2.put("value", 180);
json.put("verticalLine", json2);//垂直线，则需要设置y轴最小值和最大值
json.put("xMin", 100);//x轴最小值
json.put("xMax", 200);//x轴最大值
json.put("yMin", 10);//y轴最小值
json.put("yMax", 100);//y轴最大值
jsonStringData=json.toString();

isShowDataLabel	是否显示数据label
dataDisplay	数据显示格式，isShowDataLabel为true时有效，例如"{c}：身高{a}，体重{b}"则{"174.0", "65.6","B1"}这条数据显示为"B1：身高174.0，体重65.6"，注暂只支持abc
isShowLegend	是否显示图例
isShowToolbox	是否显示工具箱
dataViewReadOnly	数据是否可以临时修改
-->
<#macro scatter isDataZoom=false loadingDivId="loadingDivId" divClass="" divStyle="width: 600px;height: 400px;" jsonStringData="" isShowDataLabel=false dataDisplay="{a},{b}" isShowLegend=true isShowToolbox=true dataViewReadOnly=false>
<div id="${loadingDivId}" class="${divClass}" style="${divStyle}"></div>
<script type="text/javascript">
	<#--该方法可用于异步加载数据-->
	var ${loadingDivId}myChart = echarts.init(document.getElementById('${loadingDivId}'));
	function ${loadingDivId}changeData(jsonStringData){
		var ${loadingDivId}chartObj=jQuery.parseJSON(jsonStringData);
		var ${loadingDivId}itemStyle=
		{
			normal: {
				borderRadius: 5,
				label : {
					<#if isShowDataLabel>
					show : true,
					<#else>
					show : false,
					</#if>
					position:'top',
					<#if dataDisplay?default('')!=''>
					formatter:function (param) {
						var dataDisplay='${dataDisplay}';
						for(var i=0;i<param.data.length;i++){
							switch(i){
								case 0:
									dataDisplay=dataDisplay.replace(new RegExp("{a}","gm"),param.data[0]);
									break;
								case 1:
									dataDisplay=dataDisplay.replace(new RegExp("{b}","gm"),param.data[1]);
									break;
								case 2:
									dataDisplay=dataDisplay.replace(new RegExp("{c}","gm"),param.data[2]);
									break;
								default:
									break; 
							}
						}
						return dataDisplay;
					},
					</#if>
					textStyle : {
						fontSize : '12',
						fontFamily : '微软雅黑',
						fontWeight : 'bold'
					}
				}
			}
		};
		
		var ${loadingDivId}markPoint=
		{
			data : [
				<#--if isNeedMax>
				{type : 'max', name: '最大值'},
				</#if>
				<#if isNeedMin>
				{type : 'min', name: '最小值'}
				</#if-->
			]
		};
		var ${loadingDivId}markLine=
		{
			data : [
				[{},{}],
				[{},{}],
				<#--if isNeedAverage>
				{type : 'average', name: '平均值'}
				</#if-->
			]
		};
		var ${loadingDivId}xAxis=[
		{
			name:${loadingDivId}chartObj.xName,
			type : 'value',
			scale:true
		}
		];
		var ${loadingDivId}yAxis=[
		{
			name:${loadingDivId}chartObj.yName,
			type : 'value',
			scale:true,
		}
		];
		var ${loadingDivId}horizontalLine=${loadingDivId}chartObj.horizontalLine;
		var ${loadingDivId}verticalLine=${loadingDivId}chartObj.verticalLine;
		var ${loadingDivId}xMin=${loadingDivId}chartObj.xMin;
		var ${loadingDivId}xMax=${loadingDivId}chartObj.xMax;
		if(${loadingDivId}xMin!=null)
		${loadingDivId}xAxis[0].min=${loadingDivId}xMin;
		if(${loadingDivId}xMax!=null)
		${loadingDivId}xAxis[0].max=${loadingDivId}xMax;
		if(${loadingDivId}horizontalLine!=null){
			${loadingDivId}markLine.data[0]={
				name:${loadingDivId}horizontalLine.name,
				yAxis:${loadingDivId}horizontalLine.value
			};
		}
		var ${loadingDivId}yMin=${loadingDivId}chartObj.yMin;
		var ${loadingDivId}yMax=${loadingDivId}chartObj.yMax;
		if(${loadingDivId}yMin!=null)
		${loadingDivId}yAxis[0].min=${loadingDivId}yMin;
		if(${loadingDivId}yMax!=null)
		${loadingDivId}yAxis[0].max=${loadingDivId}yMax;
		if(${loadingDivId}verticalLine!=null){
			${loadingDivId}markLine.data[1]={
				name:${loadingDivId}verticalLine.name,
				xAxis:${loadingDivId}verticalLine.value,
			};
		}
		var ${loadingDivId}legendData=${loadingDivId}chartObj.legendData;
		var ${loadingDivId}loadingData=${loadingDivId}chartObj.loadingData;
		var ${loadingDivId}series=[];
		for(var i=0;i<${loadingDivId}legendData.length;i++){
			${loadingDivId}series[i]={
				name: ${loadingDivId}legendData[i],
				type: 'scatter',
				data: ${loadingDivId}loadingData[i][0],
				itemStyle: ${loadingDivId}itemStyle,
				markPoint : ${loadingDivId}markPoint,
				markLine : ${loadingDivId}markLine
			}
		};
		
		var ${loadingDivId}option = {
			title: {
				x:'center',
				text: ${loadingDivId}chartObj.text,
				subtext: ${loadingDivId}chartObj.subtext
			},
			animation:true,<#--开启动画效果-->
			toolbox: {
				<#if isShowToolbox>
				show : true,
				<#else>
				show : false,
				</#if>
				itemGap:15,
				right:'20',
				orient :'vertical',
				feature : {
					mark : {show: true},
					dataZoom : {show: true},
					<#if dataViewReadOnly>
						dataView : {show: true,readOnly:false },
					<#else>
						dataView : {show: true,readOnly:true },
					</#if>
					restore : {show: true},
					saveAsImage : {show: true}
				}
			},
			tooltip: {
				trigger: 'axis',
				axisPointer : {
					type : 'cross',
					lineStyle: {
						type : 'dashed',
						width : 1
					}
				},
				formatter : function (params) {
					if (params.componentType == "series") {
						return ${loadingDivId}chartObj.xName+'：'+params.value[0]+'，'+${loadingDivId}chartObj.yName+'：'+params.value[1];
					}else{
						return params.name+"："+params.value;
					}
				},
			},
			legend: {
				bottom:'0%',
				<#if !isShowLegend>
				show:false,
				<#else>
				show:true,
				</#if>
				data: ${loadingDivId}legendData
			},
			grid:{
				y:'80'
			},
			xAxis : ${loadingDivId}xAxis,
			yAxis : ${loadingDivId}yAxis,
			<#if isDataZoom>
			dataZoom: [{
				type: 'inside',
			}, {
			}],
			</#if>
			series: ${loadingDivId}series
		};
	
		${loadingDivId}myChart.setOption(${loadingDivId}option);
	}
	$(document).ready(function(){
		<#if jsonStringData?default('')!=''>
			${loadingDivId}changeData('${jsonStringData}');
		</#if>
	});
</script>
</#macro>

<#--
雷达图
loadingDivId 载体div的id，不填就默认，若同一个页面中有多个则必须区分
jsonStringData json格式字符串其中必须包括（‘legendData’图例说明）（‘loadingData’数据）
*后台数据组装例如：json的key必须如下
JSONObject json = new JSONObject();
json.put("text", "雷达图");//主标题	可不填写
json.put("subtext", "雷达图副标题");//副标题	可不填写
json.put("legendData", new String[]{"预算分配","分配额度"});
JSONArray jsonArr = new JSONArray();
JSONObject json2 = new JSONObject();
json2.put("text", "语文");
json2.put("max", 100);
jsonArr.add(json2);
json2 = new JSONObject();
json2.put("text", "英语");
json2.put("max", 100);
jsonArr.add(json2);
json2 = new JSONObject();
json2.put("text", "数学");
json2.put("max", 100);
jsonArr.add(json2);
json2 = new JSONObject();
json2.put("text", "政治");
json2.put("max", 100);
jsonArr.add(json2);
json2 = new JSONObject();
json2.put("text", "历史");
json2.put("max", 100);
jsonArr.add(json2);
json.put("polarIndicator", jsonArr);
jsonArr=new JSONArray();
json2=new JSONObject();
json2.put("name", "预算分配");//该值与legendData匹配
json2.put("value", new Double[]{43.2, 53.5, 60.0, 70.0, 82.0});
jsonArr.add(json2);
json2=new JSONObject();
json2.put("name", "分配额度");//该值与legendData匹配
json2.put("value", new Double[]{50.2, 60.5, 25.0, 78.0, 40.0});
jsonArr.add(json2);
json.put("loadingData", jsonArr);
jsonStringData=json.toString();

radius	 图大小
center	图圆心所在位置"['x%', 'y%']"
titleFontSize	title字体大小
isShowLegend	是否显示图例
isShowToolbox	是否显示工具箱
-->
<#macro radar loadingDivId="loadingDivId" divClass="" divStyle="width: 600px;height: 400px;" jsonStringData="" radius="62%" center="['50%', '56%']" titleFontSize=18 isShowLegend=true isShowToolbox=true >
<div id="${loadingDivId}" class="${divClass}" style="${divStyle}"></div>
<script type="text/javascript">
	<#--该方法可用于异步加载数据-->
	var ${loadingDivId}myChart = echarts.init(document.getElementById('${loadingDivId}'));
	function ${loadingDivId}changeData(jsonStringData){
		var ${loadingDivId}chartObj=jQuery.parseJSON(jsonStringData);
		var ${loadingDivId}itemStyle=
		{
			normal: {
				borderRadius: 5,
				label : {
					show : true,
					position:'top',
					textStyle : {
						fontSize : '12',
						fontFamily : '微软雅黑',
						fontWeight : 'bold'
					},
					formatter : function (param) {
						return param.value;
					}
				}
			}
		};
		var ${loadingDivId}legendData=${loadingDivId}chartObj.legendData;
		var ${loadingDivId}xAxisData=${loadingDivId}chartObj.xAxisData;
		var ${loadingDivId}loadingData=${loadingDivId}chartObj.loadingData;
		var ${loadingDivId}polarIndicator=${loadingDivId}chartObj.polarIndicator;
		var ${loadingDivId}polar=[];
		var ${loadingDivId}indicator=[];
		for(var i=0;i<${loadingDivId}polarIndicator.length;i++){
			${loadingDivId}indicator[i]={
				text:${loadingDivId}polarIndicator[i].text,
				max:${loadingDivId}polarIndicator[i].max
			};
		}
		${loadingDivId}polar[0]={
			indicator :${loadingDivId}indicator,
			radius : '${radius}',
			center: ${center},
		};
		var ${loadingDivId}data=[];
		for(var i=0;i<${loadingDivId}legendData.length;i++){
			${loadingDivId}data[i]={
				value:${loadingDivId}chartObj.loadingData[i].value,
				name:${loadingDivId}chartObj.loadingData[i].name
			};
		};
		var ${loadingDivId}series=[];
		${loadingDivId}series[0]={
			type: 'radar',
			data: ${loadingDivId}data,
			itemStyle: ${loadingDivId}itemStyle
		};
		var ${loadingDivId}tooltip={
		};
		var ${loadingDivId}option = {
			title: {
				textStyle:{
					fontSize:${titleFontSize}
				},
				x:'center',
				text: ${loadingDivId}chartObj.text,
				subtext: ${loadingDivId}chartObj.subtext
			},
			animation:true,<#--开启动画效果-->
			toolbox: {
				<#if isShowToolbox>
				show : true,
				<#else>
				show : false,
				</#if>
				itemGap:15,
				orient :'vertical',
				feature : {
					saveAsImage : {show: true}
				}
			},
			tooltip: ${loadingDivId}tooltip,
			legend: {
				bottom:'0%',
				<#if !isShowLegend>
				show:false,
				<#else>
				show:true,
				</#if>
				data: ${loadingDivId}legendData
			},
			polar : ${loadingDivId}polar,
			series: ${loadingDivId}series
		};
	
		${loadingDivId}myChart.setOption(${loadingDivId}option);
	}
	$(document).ready(function(){
		<#if jsonStringData?default('')!=''>
			${loadingDivId}changeData('${jsonStringData}');
		</#if>
	});
</script>
</#macro>

<#--
仪表盘-单一
loadingDivId 载体div的id，不填就默认，若同一个页面中有多个则必须区分
jsonStringData json格式字符串其中必须包括（‘legendData’图例说明）（‘loadingData’数据）
*后台数据组装例如：json的key必须如下
JSONObject json = new JSONObject();
json.put("text", "仪表盘-单一");//主标题	可不填写
json.put("subtext", "仪表盘-单一副标题");//副标题	可不填写
JSONObject json2=new JSONObject();
json2.put("min", 20);//可不写默认为0
json2.put("max", 50);//可不写默认为100
json2.put("name", "数量");
json2.put("value", "30");
json.put("loadingData", json2);
jsonStringData=json.toString();

radius	 图大小
center	图圆心所在位置"['x%', 'y%']"
titleFontSize	title字体大小
isShowToolbox	是否显示工具箱
-->
<#macro gauge loadingDivId="loadingDivId" divClass="" divStyle="width: 600px;height: 400px;" jsonStringData="" radius="90%" center="['50%', '65%']" titleFontSize=18 isShowToolbox=true>
<div id="${loadingDivId}" class="${divClass}" style="${divStyle}"></div>
<script type="text/javascript">
	<#--该方法可用于异步加载数据-->
	var ${loadingDivId}myChart = echarts.init(document.getElementById('${loadingDivId}'));
	function ${loadingDivId}changeData(jsonStringData){
		var ${loadingDivId}chartObj=jQuery.parseJSON(jsonStringData);
		var ${loadingDivId}loadingData=${loadingDivId}chartObj.loadingData;
		var ${loadingDivId}series = [];
		${loadingDivId}series[0]={
			name:'数据',
			radius : '${radius}',
			center: ${center},
			type:'gauge',
			detail : {formatter:'{value}'},
			data:[{name:${loadingDivId}loadingData.name,value:${loadingDivId}loadingData.value}]
		};
		if(${loadingDivId}loadingData.min!=null){
			${loadingDivId}series[0].min=${loadingDivId}loadingData.min;
		}
		if(${loadingDivId}loadingData.max!=null){
			${loadingDivId}series[0].max=${loadingDivId}loadingData.max;
		}
		
		var ${loadingDivId}option = {
			title: {
				textStyle:{
					fontSize:${titleFontSize}
				},
				text: ${loadingDivId}chartObj.text,
				subtext: ${loadingDivId}chartObj.subtext,
				x:'center'
			},
			toolbox: {
				<#if isShowToolbox>
				show : true,
				<#else>
				show : false,
				</#if>
				itemGap:15,
				orient :'vertical',
				feature : {
					saveAsImage : {show: true}
				}
			},
			series : ${loadingDivId}series
		};
		${loadingDivId}myChart.setOption(${loadingDivId}option);
	}
	$(document).ready(function(){
		<#if jsonStringData?default('')!=''>
			${loadingDivId}changeData('${jsonStringData}');
		</#if>
	});
</script>
</#macro>



<#macro map loadingDivId="loadingDivId" city="" divClass="" jsonStringData="" divStyle="width: 600px;height: 400px;">
<div id="${loadingDivId}" class="${divClass}" style="${divStyle}"></div>
<script type="text/javascript">
	var ${loadingDivId}chartObj=jQuery.parseJSON('${jsonStringData!}');
	
	function ${loadingDivId}changeData(jsonStringData){
		var ${loadingDivId}myChart = echarts.init(document.getElementById('${loadingDivId}'));
		${loadingDivId}myChart.showLoading();
		$.get('${request.contextPath}/static/region-json/${city!}.json', function (geoJson) {
	    ${loadingDivId}myChart.hideLoading();
	    echarts.registerMap('${city!}', geoJson);
	    if(!${loadingDivId}chartObj.rangeMin){
	    	${loadingDivId}chartObj.rangeMin = 100;
	    }
	    if(!${loadingDivId}chartObj.rangeMax){
	    	${loadingDivId}chartObj.rangeMax = 5000;
	    }
	    ${loadingDivId}myChart.setOption(option = {
	        title: {
	            text: ${loadingDivId}chartObj.title,
	        },
	        tooltip: {
	            trigger: 'item',
	            formatter: '{b}<br/>{c}(人)'
	        },
	        toolbox: {
	            show: true,
	            orient: 'vertical',
	            left: 'right',
	            top: 'center'
	        },
	        visualMap: {
	            min: ${loadingDivId}chartObj.rangeMin,
	            max: ${loadingDivId}chartObj.rangeMax,
	            text:['最大','最小'],
	            realtime: false,
	            calculable: true,
	            inRange: {
	                color: ['lightskyblue','yellow', 'red']
	            }
	        },
	        series: [
	            {
	                name: ${loadingDivId}chartObj.title,
	                type: 'map',
	                mapType: '${city!}', // 自定义扩展图表类型
	                itemStyle:{
	                    normal:{label:{show:${loadingDivId}chartObj.showLabelNromal}},
	                    emphasis:{label:{show:true}}
	                },
	                data:${loadingDivId}chartObj.data,
	                nameMap:${loadingDivId}chartObj.nameMap
	            }
	        ]
	    });
	});
	}

	$(document).ready(function(){
		<#if jsonStringData?default('')!=''>
			${loadingDivId}changeData('${jsonStringData}');
		</#if>
	});
</script>
</#macro>