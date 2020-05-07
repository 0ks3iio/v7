<div class="index data-warehouse-detail">
    <div class="data-detail-head clearfix">
        <div class="logo">
            <img src="${request.contextPath}/bigdata/v3/static/images/data-warehouse/icon-warehouse-big.png" width="75" height="76">
        </div>
        <div class="data-explain">
            <div class="data-explain-head">
                <span>${name!}</span>
                <div class="pos-right color-999" id="topic-update-time">
                    更新时间：
                </div>
            </div>
            <div class="data-explain-body">
                <p class="ellipsis" title="${remark!}">${remark!}</p>
            </div>
            <div class="pt-15 clearfix">
                <div class="detail-box-float">
                    <div class="">
                        <span>数据总量</span>
                        <p id="topic-total-amount">0</p>
                    </div>
                </div>
                <div class="detail-box-float">
                    <div class="">
                        <span>元数据</span>
                        <p id="topic-md-amount">0</p>
                    </div>
                </div>
                <div class="detail-box-float">
                    <div class="">
                        <span>数据源</span>
                        <p id="topic-source-amount">0</p>
                    </div>
                </div>
                <div class="detail-box-float">
                    <div class="">
                        <span>应用</span>
                        <p id="topic-target-amount">0</p>
                    </div>
                </div>
            </div>
        </div>
        <div class="data-handle">
            <div class="data-offer">
                <div class=""><b>数据源</b></div>
                <div class="data-offer-logo" id="topic-source-urls">
                    <img src="" >
                </div>
                <div class=""><b>应用</b></div>
                <div class="data-offer-logo" id="topic-target-urls">
                    <img src="" >
                </div>
            </div>
        </div>
    </div>
    <div class="data-detail-body table-scroll-made">
        <div class="row no-padding">
            <div id="meatadata-list-div" class="col-md-6">
                
            </div>
            <div class="col-md-6">
                <div class="box-chart-card">
                    <div class="chart-card-head">
                        <b>最近7天（日增长数据）</b>
                    </div>
                    <div class="chart-wrap" style="height: calc(100% - 32px);"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
$(function(){
    getRecentChartData("${levelId!}${topicId!}",$('.chart-wrap')[0]);
    showSouAndTarUrls($("#topic-source-urls"),$("#topic-target-urls"),"${levelId!}","${topicId!}","",2);
    showMeatdataList() ;
    showTopicInfoData("${levelId!}${topicId!}");
})
    
function showMeatdataList() {
	var url = '${request.contextPath}/bigdata/property/general/topic/metadatas?levelId=${levelId!}&topicId=${topicId!}';
	$("#meatadata-list-div").load(url);
}

function showTopicInfoData(key) {
    $.ajax({
		url:'${request.contextPath}/bigdata/property/general/stat/info',
		data: {'key':key},
		type:'post',
		success:function(data) {
			if(data.success){
				var result = data.data;
				$("#topic-update-time").text('更新时间：'+returnEmptyStr(result.stat_time));
				$("#topic-total-amount").text(returnEmptyNum(result.total_amount));
				$("#topic-md-amount").text(returnEmptyNum(result.md_amount));
				$("#topic-source-amount").text(returnEmptyNum(result.source_amount));
				$("#topic-target-amount").text(returnEmptyNum(result.target_amount));
			}
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}

function showMetadataDetail(metaId) {
    router.go({
        path: '/bigdata/property/general/metadata/info?metaId='+metaId,
        name: '元数据',
        level: 3
    }, function () {
		var url = '${request.contextPath}/bigdata/property/general/metadata/info?metaId='+metaId;
	    $('.page-content').load(url);
    });
}  
</script>