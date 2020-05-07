<#import "/fw/macro/mobilecommon.ftl" as common>
<@common.moduleDiv titleName="成长手册">
<link href="${request.contextPath}/studevelop/mobile/css/style.css" rel="stylesheet"/>
<div class="mui-content">
	<div class="mui-noRecord">
		<i></i><br />暂无成长记录
	</div>
</div>

<script type="text/javascript" charset="utf-8">
  	mui.init();
</script>
<script type="text/javascript" src="http://cdnjs.gtimg.com/cdnjs/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript">
$(function(){
	var window_h = $(window).height();
	var header_h = $('header').height();
	var content_h = window_h - header_h;
	$('.mui-noRecord').height(content_h);
});
</script>
</@common.moduleDiv>