<div class="index data-warehouse">
    <div class="table-type-wrap">
        <div class="color-box-wrap">
            <div class="color-box green">
                <div class="color-box-head">数据总量</div>
                <div class="color-box-content" id="all-total-amount">
                    0
                </div>
            </div>
        </div>
        <div class="color-box-wrap">
            <div class="color-box blue">
                <div class="color-box-head">元数据</div>
                <div class="color-box-content" id="all-meta-amount">
                    0
                </div>
            </div>
        </div>
        <div class="color-box-wrap">
            <div class="color-box purple">
                <div class="color-box-head">数据源</div>
                <div class="color-box-content" id="all-source-amount">
                    0
                </div>
            </div>
        </div>
        <div class="color-box-wrap">
            <div class="color-box black">
                <div class="color-box-head">应用</div>
                <div class="color-box-content" id="all-target-amount">
                    0
                </div>
            </div>
        </div>
        <div class="color-box-wrap">
            <div class="color-box purplrLight">
                <div class="color-box-head">接口数</div>
                <div class="color-box-content" id="all-api-amount">
                    0
                </div>
            </div>
        </div>
    </div>
    <#if topics?exists&&topics?size gt 0>
      	<#list topics as topic>
      		<#assign metas = metaMap[topic.id]>
		    <div class="box-made">
		        <div class="box-made-head">
		            <span class="box-made-name">${topic.name!}</span>
		            <div class="pos-right">
		                共<#if metas?exists>${metas?size}<#else>0</#if>个元数据
		                <#if metas?exists&&metas?size gt 24>
		                <span class="color-00cce3 js-unfold">&nbsp;展开全部</span>
		                </#if>
		            </div>
		        </div>
		        <div class="box-made-body">
		        <#if metas?exists&&metas?size gt 0>
      				<#list metas as meta>
      				<#if meta_index % 12 == 0>
          				<div class="table-style-wrap clearfix">
			      	</#if>
		                <div class="box-table-style" data-meta-id="${meta.id!}">
		                    <div class=""></div>
		                    <p class="ellipsis"  title="${meta.name!}"> ${meta.name!}</p>
		                </div>
	                <#if meta_index % 12 == 11||meta_index==metas?size-1>
		                </div>
		                <div class="box-detail-wrap clearfix hide">
		                </div>
			      	</#if>
		          	</#list>
		        <#else>
					<div class="no-data-common">
						<div class="text-center">
							<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
							<p class="color-999">无数据</p>
						</div>
					</div>
		      	</#if>
                </div>
            </div>
      	</#list>
  	 <#else>
			<div class="no-data-common">
				<div class="text-center">
					<img src="${request.contextPath}/bigdata/v3/static/images/public/no-data-common-100.png"/>
					<p class="color-999">无数据</p>
				</div>
			</div>
  	</#if>
</div>
<script type="text/javascript">
$(function(){
	$('.table-style-wrap').on('click','.box-table-style',function(){
	    $(this).closest('.box-made-body').addClass('active');
	    $(this).closest('.box-made-body').find('.box-table-style').removeClass('active');
	    $(this).addClass('active');
	    $(this).closest('.box-made-body').find('.box-detail-wrap').addClass('hide');
	    $(this).closest('.table-style-wrap').next('.box-detail-wrap').removeClass('hide');
	    var metaId = $(this).attr("data-meta-id");
	    getMetaInfo($(this).closest('.table-style-wrap').next('.box-detail-wrap'),metaId);
	});
	$('.js-unfold').on('click',function(){
		if($(this).hasClass('show-topic')){
			$(this).text(" 展开全部");
			$(this).removeClass('show-topic');
		}else{
			$(this).text(" 收起");
			$(this).addClass('show-topic');
		}
	    $(this).closest('.box-made-head').siblings('.box-made-body').toggleClass('auto')
	});
    showAllInfoData();
})
function getMetaInfo(obj,metaId) {
    $.ajax({
		url:'${request.contextPath}/bigdata/property/general/stat/info',
		data: {'key':metaId,'metaId':metaId},
		type:'post',
		success:function(data) {
			var result = data.data;
			showMetaInfo(obj,result,metaId);
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}

function showMetaInfo(obj,result,metaId) {
	var html = '<div class="detail-left"><div class="detail-head"><span class="detail-name">'+result.meta_name+'</span>';
	html+='<div class="pos-right color-999">  更新时间：'+returnEmptyStr(result.stat_time)+'</div></div>';
	html+='<div class="detail-body"><div>'+result.meta_remark+'<a href="javascript:void(0);" onclick="showMetadataDetail(\''+metaId+'\')">查看详情> </a></div></div></div>';
	html+='<div class="detail-left second clearfix"><div class="detail-box-float"><div class=""><span>数据总量</span><p>'+returnEmptyNum(result.total_amount)+'</p></div></div>';
	html+='<div class="detail-box-float"><div class=""><span>接口数</span><p>'+returnEmptyNum(result.api_amount)+'</p></div></div>';
	html+='<div class="detail-box-float"><div class=""><span>数据源</span><p>'+returnEmptyNum(result.source_amount)+'</p></div></div>';
	html+='<div class="detail-box-float"><div class=""><span>应用</span><p>'+returnEmptyNum(result.target_amount)+'</p></div></div></div></div>';
	obj.html(html);
}

function showTopicDetail(levelId,topicId) {
    router.go({
        path: '/bigdata/property/general/topic/detail?levelId='+levelId+'&topicId='+topicId,
        name: '资产主题',
        level: 2
    }, function () {
		var url = '${request.contextPath}/bigdata/property/general/topic/detail?levelId='+levelId+'&topicId='+topicId;
	    $('.page-content').load(url);
    });
}  

function returnEmptyStr(str) {
	if(str){
		return str;
	}else{
		return '';
	}
}

function returnEmptyNum(str) {
	if(str){
		return str;
	}else{
		return '0';
	}
}

function getStarsHtml(num) {
	var html = '<span class="rating-list">';
	for (i = 0; i < 5; i++) { 
		if(num >= 20){
			html+='<a href="javascript:void(0);" hidefocus="true" class="active"></a>';
		}else if(num >= 10){
			html+='<a href="javascript:void(0);" hidefocus="true" class="active half"></a>';
		}else{
			html+='<a href="javascript:void(0);" hidefocus="true"></a>';
		}
		num=num - 20;
	}
	html+='</span>';
	return html;
}

function getRecentChartData(key,obj) {
    $.ajax({
		url:'${request.contextPath}/bigdata/property/general/recent/daily/acount',
		data: {'key':key},
		type:'post',
		success:function(data) {
			if(data.success){
				var result = data.data;
				showRecentDataChart(obj,result);
			}
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}

function showRecentDataChart(obj,result) {
	var option = {
        tooltip : {
            trigger: 'axis'
        },
        legend: {
            data:['日增长量'],
            x: 'center',
            y: 'bottom'
        },
        calculable : true,
        grid: {
            top: 20,
            left: 0,
            right: 30,
            bottom: 40,
            containLabel: true
        },
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                data : result[0]
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : [
            {
                name:'日增长量',
                type:'line',
                data:result[1]
            },
        ]
    };
    var myChart = echarts.init(obj);
    myChart.setOption(option);
    $(window).resize(function(){
        myChart.resize()
    })
}
function showAllInfoData() {
    $.ajax({
		url:'${request.contextPath}/bigdata/property/general/stat/info',
		data: {'key':'00000000000000000000000000000000'},
		type:'post',
		success:function(data) {
			if(data.success){
				var result = data.data;
				$("#all-total-amount").text(returnEmptyNum(result.total_amount));
				$("#all-meta-amount").text(returnEmptyNum(result.md_amount));
				$("#all-source-amount").text(returnEmptyNum(result.source_amount));
				$("#all-target-amount").text(returnEmptyNum(result.target_amount));
				$("#all-api-amount").text(returnEmptyNum(result.api_amount));
			}
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}

function showSouAndTarUrls(sobj,tobj,levelId,topicId,metaId,type) {
    $.ajax({
		url:'${request.contextPath}/bigdata/property/general/source/target/url',
		data: {'levelId':levelId,'topicId':topicId,'metaId':metaId,'type':type},
		type:'post',
		success:function(data) {
			if(data.success){
				var result = data.data;
				surls = result[0];
				turls = result[1];
				shtml = '';
				thtml = '';
				for(i=0;i<surls.length;i++){
					shtml += '<img style="width: 100%; height: 100%; max-height: 40px; max-width: 40px" src="'+surls[i]+'" >';
				}
				for(i=0;i<turls.length;i++){
					thtml += '<img style="width: 100%; height: 100%; max-height: 40px; max-width: 40px" src="'+turls[i]+'" >';
				}
				if(shtml=='')shtml='无数据源';
				if(thtml=='')thtml='无应用';
				sobj.html(shtml);
				tobj.html(thtml);
			}
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}
function showMetadataDetail(metaId) {
    router.go({
        path: '/bigdata/property/general/metadata/info?metaId='+metaId,
        name: '元数据',
        level: 2
    }, function () {
		var url = '${request.contextPath}/bigdata/property/general/metadata/info?metaId='+metaId;
	    $('.page-content').load(url);
    });
} 
</script>