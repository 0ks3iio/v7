<div class="clearfix">
	<div class="col-md-6">
		<p class="margin-b-20"><b>Kafka主题配置</b></p>
	</div>
	<div class="col-md-6">
		<p class="margin-b-20"><b>标题</b></p>
	</div>
</div>
<div class="clearfix scroll-height-area">
	<div class="col-md-6 height-1of1">
		<div class="text height-1of1">
			<pre id="editorKafka" class="height-1of1">${kafkaInfo!}</pre>
		</div>
	</div>
	<div class="col-md-6 height-1of1">
		<div class="right-part-textarea height-1of1">
			<div class="textarea">
				<div>{</div>
				<div>
					<p>"topic": "event1",   <span>（主题）</span></p>
				</div>
				<div>
					<p>"replicationFactor": 1,   <span>（副主题）</span></p>
				</div>
				<div>
					<p>"partitions": 1   <span>（分区数）</span></p>
				</div>
				<div>}</div>
			</div>
		</div>
	</div>
</div>
<div class="clearfix">
	<div class="col-md-6">
		<div class="text-right run-data run-data-two">
			<a href="javascript:void(0);" onclick="submitKafkaParam();"><button class="btn btn-blue">保存Kafka配置</button></a>
			<a href="javascript:void(0);" onclick="step('step2','druid');"><button class="btn btn-blue">下一步：Druid配置</button></a>
		</div>
	</div>
</div>
<script>
	function submitKafkaParam(){
		let kafkaInfo = ace.edit("editorKafka").session.getValue();
		if ($.trim(kafkaInfo) == '') {
			showLayerTips4Confirm('error','Kafka参数不能为空');
			return;
		}
		$.ajax({
	            url:'${request.contextPath}/bigdata/eventMaintain/configure/kafka/submit',
	            data:{
	            	 'eventId':'${eventId!}',
	              	'kafkaInfo':kafkaInfo
	            },
	            type:"post",
	            dataType: "json",
	            success:function(data){
			 		if(!data.success){
						showLayerTips4Confirm('error',data.message);
			 		}else{
			 			showLayerTips('success','Kafka配置成功','t');
			 			step('step2','druid')
	    			}
	          }
	    });
	}

	$(document).ready(function(){
		heightSet();
        var editorKafka = ace.edit("editorKafka");
        editorKafka.setTheme("ace/theme/twilight");
        editorKafka.session.setMode("ace/mode/json");
        $('.ace_print-margin').addClass('hide');
		
	});
</script>