<div class=" overview-set bg-fff metadata clearfix">
	<input type="hidden" id="type" value=""/>
	<input type="hidden" id="dimCode" value=""/>
	<div  id="quality-dim-div" class="filter-item active"  onclick="showList('dim','');">
        <span>质量维度</span>
    </div>
    <div id="quality-rule-div" class="filter-item" onclick="showList('rule','');">
        <span>质量规则</span>
    </div>
    <div id="quality-stat-div" class="filter-item" onclick="showList('stat','');">
        <span>质量统计</span>
    </div>
</div>
<div class="box box-default clearfix" id="contentDiv"></div>

<script type="text/javascript">
	function showList(type,dimCode){
		if(!type)
			type=$("#type").val();
		if(!dimCode)
			dimCode=$("#dimCode").val();
	    if(type =="dim"){
	    	$('#quality-dim-div').addClass('active').siblings().removeClass('active');			
	    }else if (type == "rule"){
	    	$('#quality-rule-div').addClass('active').siblings().removeClass('active');
	    }else if (type == "stat"){
	    	$('#quality-stat-div').addClass('active').siblings().removeClass('active');
	    }
		$("#type").val(type);
		$("#dimCode").val(dimCode);
		if(type == "stat"){
			var url =  "${request.contextPath}/bigdata/setting/quality/stat";
			$("#contentDiv").load(url);
		}else{
			var url =  "${request.contextPath}/bigdata/setting/quality/list?type="+type+"&dimCode="+dimCode;
			$("#contentDiv").load(url);
		}
	}

	$(document).ready(function(){
		showList('dim','');
	});
</script>