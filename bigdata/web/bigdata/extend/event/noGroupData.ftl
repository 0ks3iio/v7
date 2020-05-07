<div class="wrap-1of1 centered no-data-state js-height" >
	<div class="text-center">
		<img src="${request.contextPath}/static/bigdata/images/card-no-data.png"/>
		<p class="no-data-explain color-999">还未添加概览组，你可以点击左上角<a class="bold js-add-overview" href="javascript:void(0)" onclick="editGroup();">"+"</a>添加概览组</p>
	</div>
</div>
<script type="text/javascript">
    	$(document).ready(function(){	
			function height(){
	            $('.js-height').each(function(){
	                $(this).css({
	                    height: $(window).height() - $(this).offset().top - 200,
	                });
	            });
	        }
			height();
		});
</script>