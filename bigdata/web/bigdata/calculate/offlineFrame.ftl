<div class=" overview-set bg-fff metadata clearfix">
	<input type="hidden" id="calculateType" value=""/>
	<div id="kylin-div" class="filter-item active" onclick="showList('2');">
        <span>Kylin</span>
    </div>
</div>
<div id="contentDiv" class="box box-default clearfix padding-20 js-scroll-height"></div>
<script type="text/javascript">
	function showList(calculateType){
		var titleName='Kylin';
		router.go({
	        path: '/bigdata/calculate/offline/list?calculateType='+calculateType,
	        name:titleName,
	        level: 2
	    }, function () {
			var index=layer.msg('数据加载中......', {
		      icon: 16,
		      time:0,
		      shade: 0.01
		    });
			if(!calculateType)
				calculateType=$("#calculateType").val();
		    if(calculateType =="2"){
		    	$('#kylin-div').addClass('active').siblings().removeClass('active');			
		    }
			$("#calculateType").val(calculateType);
			var url = "${request.contextPath}/bigdata/calculate/offline/list?calculateType="+calculateType;
			$("#contentDiv").load(url,function() {
	  			layer.close(index);
			});
	    });
		
	}

	$(document).ready(function(){
		showList('2'); 
	});
</script>