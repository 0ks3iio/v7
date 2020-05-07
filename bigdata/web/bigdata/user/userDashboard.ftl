<div class="index">
	<div class="row no-padding user-page">
		<div class="col-md-9">
			<div class="box box-default lay-made no-padding-bottom" id="reportStatDiv"></div>
			<div class="box box-default lay-made no-padding-bottom" id="reportMarkDiv"></div>
			<div class="box box-default lay-made no-padding-bottom" id="operationMarkDiv"></div>
			<div class="box box-default lay-made no-padding-bottom" id="sharedDiv"></div>
			<div class="box box-default lay-made no-padding-bottom" id="favoriteDiv"></div>
		</div>
		<div class="col-md-3">
			<div class="box box-default lay-made" id="noticeDiv"></div>
			<div class="box box-default lay-made" id="helpDiv"></div>
			<div class="box box-default lay-made no-padding-bottom" id="beSharedDiv"></div>
		</div>
	</div>
</div>
<script>
	function showReportFromDesktop(id,type,name) {
		var url = '${request.contextPath}/bigdata/user/report/preview?businessType='+type+'&businessId='+ id+"&businessName="+name;
	 	window.open(url,id)
    }
   
	function loadReportStat(){
		var reportStatIndex=layer.msg('数据加载中......', {
		      icon: 16,
		      time:0,
		      shade: 0.01
	    });
		var url =  "${request.contextPath}/bigdata/user/dashboard/reportStat";
		$("#reportStatDiv").load(url,function() {
			layer.close(reportStatIndex);
		});
	}

	function loadReportMark(){
		var reportMarkIndex=layer.msg('数据加载中......', {
		      icon: 16,
		      time:0,
		      shade: 0.01
	    });
		var url =  "${request.contextPath}/bigdata/user/dashboard/reportMark";
		$("#reportMarkDiv").load(url,function() {
			layer.close(reportMarkIndex);
		});
	}

	function loadOperationMark(){
		var operationMarkIndex=layer.msg('数据加载中......', {
		      icon: 16,
		      time:0,
		      shade: 0.01
	    });
		var url =  "${request.contextPath}/bigdata/user/dashboard/operationMark";
		$("#operationMarkDiv").load(url,function() {
			layer.close(operationMarkIndex);
		});
	}
	
	function loadSharedDiv(){
		var sharedIndex=layer.msg('数据加载中......', {
		      icon: 16,
		      time:0,
		      shade: 0.01
	    });
		var url =  "${request.contextPath}/bigdata/share/component?type=1";
		$("#sharedDiv").load(url,function() {
			layer.close(sharedIndex);
		});
	}

	function loadBeSharedDiv(){
		var beSharedIndex=layer.msg('数据加载中......', {
		      icon: 16,
		      time:0,
		      shade: 0.01
	    });
		var url =  "${request.contextPath}/bigdata/share/component?type=2";
		$("#beSharedDiv").load(url,function() {
			layer.close(beSharedIndex);
		});
	}

	function loadFavoriteDiv(){
		var favoriteIndex=layer.msg('数据加载中......', {
		      icon: 16,
		      time:0,
		      shade: 0.01
	    });
		var url =  "${request.contextPath}/bigdata/favorite/component";
		$("#favoriteDiv").load(url,function() {
			layer.close(favoriteIndex);
		});
	}

	function loadNoticeDiv(){
		var noticeIndex=layer.msg('数据加载中......', {
		      icon: 16,
		      time:0,
		      shade: 0.01
	    });
		var url =  "${request.contextPath}/bigdata/notice/component";
		$("#noticeDiv").load(url,function() {
			layer.close(noticeIndex);
		});
	}
	
	function loadHelpDiv(){
		var helpIndex=layer.msg('数据加载中......', {
		      icon: 16,
		      time:0,
		      shade: 0.01
	    });
		var url =  "${request.contextPath}/bigdata/help/component";
		$("#helpDiv").load(url,function() {
			layer.close(helpIndex);
		});
	}
	
	function loadMessageDiv(){
		var messageIndex=layer.msg('数据加载中......', {
		      icon: 16,
		      time:0,
		      shade: 0.01
	    });
		var url =  "${request.contextPath}/bigdata/help/component";
		$("#messageDiv").load(url,function() {
			layer.close(messageIndex);
		});
	}
	
	$(document).ready(function(){
		var statIndex=layer.msg('数据加载中......', {
		      icon: 16,
		      time:0,
		      shade: 0.01
	    });
		loadReportStat();
		loadReportMark();
		loadOperationMark();
		loadSharedDiv();
		loadBeSharedDiv();
		loadFavoriteDiv();
		loadNoticeDiv();
		loadHelpDiv();
		layer.close(statIndex);
	});
</script>