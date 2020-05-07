<div class="clearfix">
	<div class="col-md-6">
		<p class="margin-b-20"><b>Druid数据配置</b></p>
	</div>
	<div class="col-md-6">
		<p class="margin-b-20"><b>标题</b></p>
	</div>
</div>
<div class="clearfix scroll-height-area">
	<div class="col-md-6 height-1of1">
		<div class="text height-1of1">
			<pre id="editorDruid" class="height-1of1">${metadata!}</pre>
		</div>
	</div>
	<div class="col-md-6 height-1of1">
		<div class="right-part-textarea height-1of1 ">
			<div class="textarea scrollBar4">
				<div>{</div>
					<div><p>"type": "kafka",</p></div>
					<div><p>"dataSchema": {</p></div>
					<div><p>  "dataSource": "event_test",   <span>（表名）</span></p></div>
					<div><p> "parser": {</p></div>
					<div><p>  "type": "string",</p></div>
					<div><p>  "parseSpec": {</p></div>
					<div><p>   "format": "json",</p></div>
					<div><p>   "timestampSpec": {   <span>（时间序列字段）</span></p></div>
					<div><p>   "column": "timestamp",</p></div>
					<div><p>   "format": "auto"</p></div>
					<div><p>    },</p></div>
					<div><p>   "dimensionsSpec": {</p></div>
					<div><p>   "dimensions": ["student_name","dept_name","unit_name","teacher_name","score_type"], <span>（维度字段）</span></p></div>
					<div><p>   "dimensionExclusions": ["timestamp","score"] <span>（非维度字段）</span></p></div>
					<div><p>  }</p></div>
					<div><p> }</p></div>
					<div><p>},</p></div>
					<div><p>"metricsSpec": [ <span>（聚合值）</span></p></div>
					<div><p>{</p></div>
					<div><p> "name": "count",</p></div>
					<div><p>"type": "count"</p></div>
					<div><p>},</p></div>
					<div><p>{</p></div>
					<div><p>"type": "longSum",</p></div>
					<div><p>"name": "score",</p></div>
					<div><p>"fieldName": "score"</p></div>
					<div><p>}</p></div>
					<div><p>],</p></div>
					<div><p>"granularitySpec": { <span>（数据颗粒度）</span></p></div>
					<div><p>"type": "uniform",</p></div>
					<div><p>"segmentGranularity": "DAY",</p></div>
					<div><p>"queryGranularity": "NONE"</p></div>
					<div><p>}</p></div>
					<div><p>},</p></div>
					<div><p>"tuningConfig": {</p></div>
					<div><p>"type": "kafka",</p></div>
					<div><p>"maxRowsPerSegment": 5000000</p></div>
					<div><p>},</p></div>
					<div><p>"ioConfig": { <span>（kafka配置）</span></p></div>
					<div><p>"topic": "test123456",</p></div>
					<div><p>"consumerProperties": {</p></div>
					<div><p>"bootstrap.servers": "192.168.20.53:9092"</p></div>
					<div><p>},</p></div>
					<div><p>"taskCount": 1,</p></div>
					<div><p>"replicas": 1,</p></div>
					<div><p>"taskDuration": "PT5M" <span>（采集频率）</span></p></div>
					<div><p> }</p></div>
					<div>}</div>
			</div>
		</div>
	</div>
</div>
<div class="clearfix">
	<div class="col-md-6">
		<div class="text-right run-data run-data-two">
			<a href="javascript:void(0);" onclick="step('step1','kafka');"><button class="btn btn-blue">上一步：Kafka配置</button></a>
			<a href="javascript:void(0);" onclick="submitDruidParam();"><button class="btn btn-blue">保存Druid配置</button></a>
		</div>
	</div>
</div>
<script>
	function submitDruidParam(){
		let metadata = ace.edit("editorDruid").session.getValue();
		if ($.trim(metadata) == '') {
			showLayerTips4Confirm('error','druid参数不能为空');
			return;
		}
		$.ajax({
	            url:'${request.contextPath}/bigdata/eventMaintain/configure/druid/submit',
	            data:{
	              'eventId':'${eventId!}',
	              'metadata':metadata
	            },
	            type:"post",
	            dataType: "json",
	            success:function(data){
			 		if(!data.success){
						showLayerTips4Confirm('error','druid配置失败');
			 		}else{
			 			showLayerTips('success','druid配置成功','t');
	    			}
	          }
	    });
	}

	$(document).ready(function(){
		heightSet();
        var editorDruid = ace.edit("editorDruid");
        editorDruid.setTheme("ace/theme/twilight");
        editorDruid.session.setMode("ace/mode/json");
        $('.ace_print-margin').addClass('hide');
	
	});
</script>