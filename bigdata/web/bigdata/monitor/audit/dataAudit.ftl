<div class=" overview-set bg-fff metadata clearfix">
	<input type="hidden" id="type" value=""/>
	<div id="business-log-div" class="filter-item active" onclick="showList('business');">
        <span>业务追溯</span>
    </div>
    <!--
    <div id="system-log-div" class="filter-item" onclick="showList('system');">
        <span>系统日志</span>
    </div>
    -->
</div>
<div id="contentDiv" class="box box-default no-margin">
</div>	
<script type="text/javascript">
	function showList(type){
		if(!type)
			type=$("#type").val();
		$("#type").val(type);
	    if(type =="business"){
	    	$('#business-log-div').addClass('active').siblings().removeClass('active');  
	    }else if (type == "system"){
	    	$('#system-log-div').addClass('active').siblings().removeClass('active');
	    }
		var url =  "${request.contextPath}/bigdata/monitor/log/query/list?type="+type;
		$("#contentDiv").load(url);	
		
	}

	$(document).ready(function(){
		showList('business');
	});
</script>