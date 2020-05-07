<div id="dataTaskListDiv" >
</div>
<div id="dataTaskDiv" style="display:none">
</div>
<script>
	$(function(){
		showTaskHead();
	});
	
	var _ownerType = "${ownerType!}";
	
	function showTaskHead() {
		var url = "${request.contextPath}/datareport/taskresult/tasklisthead";
		$("#dataTaskListDiv").load(url);
	}
	
	var loadTime;

	function layerTime() {
		var width = ($(window).width() - 210)/2+120;
		var height = $(window).height()/2-32;
		loadTime = layer.msg('加载中', {
  			icon: 16,
  			shade: 0.01,
  			time: 60*1000,
  			offset: [height+'px', width+'px']
		});
	}

	function layerMsg(title) {
		var width = ($(window).width() - 210)/2+120;
		var height = $(window).height()/2-32;
		layer.msg(title,{offset: [height+'px', width+'px']});
	}

	function layerClose() {
		layer.close(loadTime);
	}
	
	<#-- 字数限制  -->
    function limitWords(objthis){
		var max = $(objthis).attr('maxlength');
		if(objthis.value.length+1 > max){
			layer.tips('数字长度不能大于'+max+'位哦', objthis, {
				tips: [1, '#000'],
				time: 2000
			})
		}
	}
</script>