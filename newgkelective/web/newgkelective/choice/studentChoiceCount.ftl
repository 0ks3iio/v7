<div class="curr-statist-title">统计结果</div>
<div id="chart-result-bar" style="height:440px;"></div>
<script src="${request.contextPath}/static/components/echarts/echarts.min.js"></script>
<script>
	$(function(){
		if(document.getElementById("chart-result-bar")) {
		    // 基于准备好的dom，初始化echarts实例
			var myChart1 = echarts.init(document.getElementById("chart-result-bar"));
			window.addEventListener('resize',function(){myChart1.resize();},false);
			var jsonStringData=jQuery.parseJSON('${jsonStringData!}');
		    var legendData=jsonStringData.legendData;
		    var loadingData=jsonStringData.loadingData;
		    // 指定图表的配置项和数据
			var option1 = {
				tooltip: {
	              trigger: "item",
	              axisPointer: {
	                type: "shadow"
	              }
	            },
			    grid: {
			        left: '2%',
			        right: '2%',
			        bottom: '6%',
			        top: '4%',
			        containLabel: true,
			        borderColor: '#e8e8e8'
			    },
			    xAxis: [
			        {
			            type: 'category',
			            data: legendData,
			            axisTick: {
			                alignWithLabel: true
			            },
			            axisLabel: {
		                  textStyle: {
		                    color: "#666666"
		                  },
		                  rotate: 45
		                },
		                axisLine: {
		                  lineStyle: {
		                    color: "#E8E8E8"
		                  }
		                }
			        }
			    ],
			    yAxis: [
	              {
	                type: "value",
	                axisLabel: {
	                  textStyle: {
	                    color: "#666666",
						fontSize: 14
	                  }
	                },
	                axisLine: {
	                  lineStyle: {
	                    color: "#E8E8E8"
	                  }
	                },
	                splitLine: {
	                  show: false
	                }
	              }
	            ],
			    series: [
	              {
	                type: "bar",
	                barWidth: "60%",
	                tooltip: {
	                  formatter: "{b} {c}人"
	                },
	                itemStyle: {
	                  normal: {
	                    color: {
	                      type: "linear",
	                      x: 0,
	                      y: 0,
	                      x2: 0,
	                      y2: 1,
	                      colorStops: [
	                        {
	                          offset: 0,
	                          color: "#56E4FC" // 0% 处的颜色
	                        },
	                        {
	                          offset: 1,
	                          color: "#29A8FA" // 100% 处的颜色
	                        }
	                      ],
	                      global: false // 缺省为 false
	                    }
	                  }
	                },
	                data:loadingData,
	                label: {
	                  normal: {
	                    show: true,
	                    textStyle: {
	                      color: "#333",
												fontSize: 14
	                    },
	                    position: "top"
	                  }
	                }
	              }
	            ]
			};
		    // 使用刚指定的配置项和数据显示图表
			myChart1.setOption(option1);
		}
	
	})
</script>
