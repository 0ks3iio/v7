<script src="${request.contextPath}/static/bigdata/editor/ace.js"/>
<div class="row height-1of1 no-padding">
	<div class="clearfix">
			<div class="box-header step-head no-border col-md-6">
				<ol class="steps-made">
					<li class="first active">
						<a href="javascript:void(0);" id="step1" onclick="step('step1','kafka');">
							<span class="step-made"></span>
							<span class="step-made step-big look" id="step1look">1</span>
							<p>Kafka配置</p>
						</a>
					</li>
					<li class="last active">
						<a href="javascript:void(0);" id="step2" onclick="step('step2','druid');">
							<span class="step-made"></span>
							<span class="step-made step-big" id="step2look">2</span>
							<p>Druid配置</p>
						</a>
					</li>
				</ol>
			</div>
	</div>	
	<div id="contentDiv"></div>
</div>							
<script type="text/javascript">
	function go2page(type){
		var index=layer.msg('数据加载中......', {
	      icon: 16,
	      time:0,
	      shade: 0.01
	    });
	    var url;
		if(type =="kafka")
			 url =  "${request.contextPath}/bigdata/eventMaintain/configure/kafka?eventId=${eventId!}";
		if(type =="druid")
		 	url =  "${request.contextPath}/bigdata/eventMaintain/configure/druid?eventId=${eventId!}";
		$("#contentDiv").load(url,function() {
  			layer.close(index);
		});
	}
	
	//步骤选择
	function step(id,type){
		var obj=$("#"+id+"look");
		obj.addClass('look');
		obj.parents('li').siblings().find('.step-big').removeClass('look');
		go2page(type);
	}


	function heightSet(){
		$('.scroll-height-area').each(function(){
			$(this).css({
				height: $(window).height() - $(this).offset().top - 56 - 40,
				overflow: 'auto'
			})
		});
	};
	
	$(document).ready(function(){
		go2page('kafka'); 
	});
</script>