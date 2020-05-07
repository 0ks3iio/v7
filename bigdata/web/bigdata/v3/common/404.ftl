<div class="no-data-404">
	<img id="404ImageId"  src=""/>
	<div class="ml-30">
		<div class="word-404">
			出错了！您访问的页面不存在
		</div>
		<button class="btn btn-lightblue" onclick="go2Homepage()">返回首页</button>
	</div>
</div>
<script>
	function go2Homepage(){
		router.go({
	         path: '/home'
	    });
	}
	
	$(document).ready(function(){
		var imageUrl=_contextPath+"/bigdata/v3/static/images/public/404.png";
		$("#404ImageId").attr("src",imageUrl);
	});
</script>