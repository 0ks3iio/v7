<div class="index data-warehouse-detail">
    <div class="data-detail-head clearfix">
        <div class="logo">
            <img src="${request.contextPath}/bigdata/v3/static/images/data-warehouse/icon-warehouse-big.png" width="75" height="76">
        </div>
        <div class="data-explain">
            <div class="data-explain-head">
                <span>${meta.name!}</span>
                <div class="pos-right color-999" id="meta-update-time">
                    更新时间：
                </div>
            </div>
            <div class="data-explain-body">
                <p>${meta.remark!}</p>
            </div>
            <div class="pt-15 clearfix">
                <div class="detail-box-float">
                    <div class="" onclick="metaListDetail();" style="cursor: pointer;">
                        <span>数据总量</span>
                        <p id="meta-total-amount">0</p>
                    </div>
                </div>
                <div class="detail-box-float">
                    <div class="">
                        <span>接口数量</span>
                        <p id="meta-api-amount">0</p>
                    </div>
                </div>
                <div class="detail-box-float">
                    <div class="">
                        <span>数据质量</span>
                        <p class="rating-show centered" id="meta-data-quality">无
                            
                        </p>
                    </div>
                </div>
            </div>
        </div>
        <div class="data-handle">
           <div class=""><b>数据源</b></div>
            <div class="data-offer-logo" id="meta-source-urls">
            </div>
            <div class=""><b>应用</b></div>
            <div class="data-offer-logo" id="meta-target-urls">
            </div>
        </div>
    </div>
    <div class="data-detail-body">
        <div class="row no-padding">
            <div class="col-md-6" id="chart-relation-div">
                <div class="box-chart-card my-20">
                    <div class="chart-card-head" data_swich="1">
                        <b>${meta.name!}关系图</b>
                    </div>
                    <div class="chart-wrap-relation" style="height: 400px;"></div>
                </div>
            </div>
            <div class="col-md-6" id="chart-daily-div">
                <div class="box-chart-card my-20">
                    <div class="chart-card-head">
                        <b>最近7天（日增长数据）</b>
                    </div>
                    <div class="chart-wrap-line" style="height: 400px;"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
$(function(){
    getRecentChartData("${meta.id!}",$('.chart-wrap-line')[0]);
    showSouAndTarUrls($("#meta-source-urls"),$("#meta-target-urls"),"","","${meta.id!}",1);
    showMeatInfoData("${meta.id!}");
    getMeatdataMaps();
})
function metaListDetail() {
	router.go({
        path: '/bigdata/property/general/meta/list/head?metaId=${meta.id!}',
        name: '${meta.name!}列表',
        level: 3
    }, function () {
		var url = '${request.contextPath}/bigdata/property/general/meta/list/head?metaId=${meta.id!}';
	    $('.page-content').load(url);
    });
}

function screenChange(type) {
	if(type==1){
		$('.data-detail-head').hide();
		$('#chart-daily-div').hide();
		$('#chart-relation-div').removeClass('col-md-6');
		$('#chart-relation-div .box-chart-card').removeClass('my-20');
		$('.chart-wrap-relation').height($('.page-content').height()-60);
	}else{
		$('.data-detail-head').show();
		$('#chart-daily-div').show();
		$('#chart-relation-div').addClass('col-md-6');
		$('#chart-relation-div .box-chart-card').addClass('my-20');
		$('.chart-wrap-relation').height('400px');
	}
}
function getMeatdataMaps() {
    $.ajax({
		url:'${request.contextPath}/bigdata/property/general/source/target/relation',
		data: {'metaId':"${meta.id!}"},
		type:'post',
		success:function(data) {
			if(data.success){
				var result = data.data;
				showMeatdataMaps(result);
			}
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}
function showMeatdataMaps(result) {
	var myChartMap = echarts.init($('.chart-wrap-relation')[0]);
    var option = {
        tooltip: {
        	formatter: function(params){
	            return params.data.label;
	        },
        },
        animationDurationUpdate: 1500,
        animationEasingUpdate: 'quinticInOut',
        label: {
            normal: {
                show: true,
                textStyle: {
                    fontSize: 12
                },
            }
        },
        color: ["#3ebb99", "#69a4f0", "#af89f3", "#ed5784", "#ed7e46","#9266ff"],
        legend: {show: true, x: "center", x : 'left', y : '200', orient: 'vertical',
            data: [
                {name: '数据表',textStyle:{color:'#666'} },
                {name: '数据采集',textStyle:{color:'#666'}},
                {name: 'ETL调度',textStyle:{color:'#666'}},
                {name: '数据接口',textStyle:{color:'#666'}},
                {name: '数据应用',textStyle:{color:'#666'}}, 
                {name: '数据模型',textStyle:{color:'#666'}}] 
        },
        series: [

            {
                type: 'graph',
                layout: 'force',
                symbolSize: 22,
                focusNodeAdjacency: true,
                roam: true,
                symbol: 'rect',
                symbolSize: [64, 24],
                categories: [
                    {name: '数据表'}, {name: '数据采集'}, {name: 'ETL调度'},{name: '数据接口'},{name: '数据应用'},{name: '数据模型'}
                ],
                label: {
                    normal: {
                        show: true,
                        textStyle: {
                            fontSize: 12
                        },
                        formatter: function(params){
				            return params.data.labelSub;
				        },
                    }
                },
                force: {
                    repulsion: 500
                },
                edgeSymbol: ['rectangle', 'arrow'],
                edgeSymbolSize: [4, 10],
                data: result.data,
                links: result.links,
                lineStyle: {
                    normal: {
                        opacity: 0.9,
                        width: 1,
                        curveness: 0
                    }
                }
            }
        ]
    };
    myChartMap.setOption(option);
    
}
function showMeatInfoData(key) {
    $.ajax({
		url:'${request.contextPath}/bigdata/property/general/stat/info',
		data: {'key':key},
		type:'post',
		success:function(data) {
			if(data.success){
				var result = data.data;
				$("#meta-update-time").text('更新时间：'+returnEmptyStr(result.stat_time));
				$("#meta-total-amount").text(returnEmptyNum(result.total_amount));
				$("#meta-api-amount").text(returnEmptyNum(result.api_amount));
				if(result.data_quality)
				$("#meta-data-quality").html(getStarsHtml(returnEmptyNum(result.data_quality)));
			}
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}


</script>