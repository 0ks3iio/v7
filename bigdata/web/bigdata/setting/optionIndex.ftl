<div class=" overview-set bg-fff metadata clearfix">
	<input type="hidden" id="type" value=""/>
	<div  id="system-option-div" class="filter-item active"  onclick="showList('system');">
        <span>系统参数设置</span>
    </div>
    <div id="frame-option-div" class="filter-item" onclick="showList('frame');">
        <span>框架参数设置</span>
    </div>
    <div id="desktop-option-div" class="filter-item" onclick="showList('desktop');">
        <span>桌面参数设置</span>
    </div>
</div>
<div class="box box-default clearfix no-margin js-height" id="contentDiv"></div>

<script type="text/javascript">
	function showList(type){
		if(!type)
			type=$("#type").val();

	    if(type =="system"){
	    	$('#system-option-div').addClass('active').siblings().removeClass('active');			
	    }else if (type == "frame"){
	    	$('#frame-option-div').addClass('active').siblings().removeClass('active');
	    }else if (type == "desktop"){
	    	$('#desktop-option-div').addClass('active').siblings().removeClass('active');
	    }
		$("#type").val(type);
		var url =  "${request.contextPath}/bigdata/setting/option/list?type="+type;
		$("#contentDiv").load(url);
	}

	$(document).ready(function(){
		showList('system');
	});
</script>