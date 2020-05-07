<#import "/bigdata/v3/templates/webUploaderMacro.ftl" as upload />
<form id="submitForm">
<input type="hidden" name="id" value="${flink.id!}">
<input type="hidden" name="etlType" value="${flink.etlType!}">
<input type="hidden" name="jobType" value="flink_stream">
<input type="hidden" name="isSchedule" value="0">
<input type="hidden" name="targetType" value="table">
<div class="form-horizontal">
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>名称：</label>
			<div class="col-sm-6">
			<input type="text" name="name" id="name" class="form-control" nullable="false" maxLength="36" value="${flink.name!}">
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>code：</label>
			<div class="col-sm-6">
				<input type="text" name="jobCode" id="jobCode" class="form-control" nullable="false" maxLength="20" value="${flink.jobCode!}">
			</div>
		</div>

        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">数据来源类型：</label>
            <div class="col-sm-6">
                <select id="sourceType" class="form-control" name="sourceType" onchange="changeSourceType()">
                    <option value="table" <#if flink.sourceType?default('table') == 'table'>selected="selected"</#if>>
                        表
                    </option>
                    <option value="app" <#if flink.sourceType?default('table') == 'app'>selected="selected"</#if>>
                        应用
                    </option>
                </select>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">数据来源：</label>
            <div class="col-sm-6">
                <select id="sourceId" class="form-control" name="sourceId">
                </select>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">数据去向：</label>
            <div class="col-sm-6">
                <select id="targetId" class="form-control" name="targetId">
                    <option value="">---请选择---</option>
                        <#list mdList as md>
                            <option value="${md.id!}" <#if md.id! == flink.targetId!>selected="selected"</#if>>${md.name!}</option>
                        </#list>
                </select>
            </div>
        </div>
		
		<div class="form-group">
			<@upload.fileUpload businessKey="flink-stream" contextPath="${request.contextPath}" resourceUrl="${resourceUrl!}" size="1" fileNumLimit="1" handler="loadFile">
	            <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>调度文件：</label>
	            <div class="col-sm-6">
		            <a class="file js-addFiles">上传文件</a>
		            <p class="js-file-content" id="showFileName">${flink.fileName!}</p>
					<input type="hidden" id="flink-stream-path" value="">
					<input type="hidden" name="path" id="path" value="${flink.path!}">
					<input type="hidden" name="fileName" id="fileName" value="${flink.fileName!}">
	            </div>
			</@upload.fileUpload>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right">备注：</label>
			<div class="col-sm-6">
				<input type="text" name="remark" id="remark" class="form-control" nullable="true" maxLength="200" value="${flink.remark!}">
			</div>
		</div>

		<div class="form-group" style="display: none">
			<label class="col-sm-2 control-label no-padding-right">flowChartJson：</label>
			<div class="col-sm-6">
				<textarea id="flowChartJson" name="flowChartJson">${flink.flowChartJson!}</textarea>
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-2 control-label">流程图：</label>
			<div class="col-sm-8">
				<div style="width: 100%; display: flex; justify-content: space-between">
					<div id="myPaletteDiv" style="width: 120px; margin-right: 2px; background-color: whitesmoke; border: solid 1px black"></div>
					<div id="myDiagramDiv" style="flex-grow: 1; height: 380px; border: solid 1px black"></div>
				</div>
			</div>
		</div>

		<div class="form-group">
            <label class="col-sm-2 control-label no-padding" for="form-field-1"></label>
            <div class="col-sm-6" >
                <button type="button" class="btn btn-long btn-blue js-added" id="flinkSaveBtn">&nbsp;保存&nbsp;</button>
                <button type="button" class="btn btn-long btn-blue js-added" id="flinkReturnBtn">&nbsp;返回&nbsp;</button>
            </div>
        </div>
 </div>
<div id="paramDiv"></div>
</form>
<script>
	var isSubmit=false;

	$('.js-isSchedule').on('click',function(){
		if($(this).prop('checked')===true){
			$('.form-shechule-param').removeClass('hidden');
			$('.form-shechule-has-param').addClass('hidden');
			$("#isSchedule").val("1");
		}else{
			$('.form-shechule-param').addClass('hidden');
			$('.form-shechule-has-param').removeClass('hidden');
			$("#isSchedule").val("0");
		}
	})

	$('.js-hasParam').on('click',function(){
		if($(this).prop('checked')===true){
			$("#hasParam").val("1");
		}else{
			$("#hasParam").val("0");
		}
	})

    $("#flinkReturnBtn").on("click",function(){
        var url = "${request.contextPath}/bigdata/calculate/realtime/list?calculateType=8";
        $("#flink-div").load(url);
    })

	$("#flinkSaveBtn").on("click",function(){
		if(isSubmit){
			return;
		}
		isSubmit = true;
		var check = checkValue('#submitForm');
		if(!check){
		 	isSubmit=false;
		 	return;
		}
		if($('.js-isSchedule').prop('checked')===true){
			if($('#scheduleParam').val() ==""){
				layer.tips("不能为空", "#scheduleParam", {
					tipsMore: true,
					tips:3				
				});
				isSubmit=false;
			 	return;
		 	}
		}

        if($('#sourceId').val() !="" && $('#sourceId').val() ==$('#targetId').val()){
            layer.tips("数据来源和数据去向不能相同,请重新维护", "#sourceId", {
                tipsMore: true,
                tips:3
            });
            isSubmit=false;
            return;
        }

		saveFlowChartJson();

		var options = {
				url : "${request.contextPath}/bigdata/calculate/flink/save",
				dataType : 'json',
				success : function(data){
			 		if(!data.success){
                        showLayerTips4Confirm('error', data.msg);
			 			isSubmit = false;
			 		}else{
                        showLayerTips('success', data.msg, 't');
                        var url = "${request.contextPath}/bigdata/calculate/realtime/list?calculateType=8";
                        $("#flink-div").load(url);
	    			}
				},
				clearForm : false,
				resetForm : false,
				type : 'post',
				error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
			};
			$("#submitForm").ajaxSubmit(options);
	});

	function loadFile(){
		if(hasUploadSuc){
			$("#path").val($("#flink-stream-path").val());
			$("#showFileName").text(fileName);
			$("#fileName").val(fileName);
			fileName= "";
		}
	}

    function changeSourceType(){
        var sourceType = $('#sourceType').val();
        if (sourceType == 'app') {
            $("#sourceId").empty();
            //添加子元素
            $("#sourceId").append("<option value=''>---请选择---</option>");
         <#if  appList?exists &&appList?size gt 0>
             <#list appList as app>
                $("#sourceId").append("<option value='${app.id!}'<#if app.id! == flink.sourceId!>selected=\"selected\"</#if>>${app.name!?html}</option>");
             </#list>
         </#if>
        } else {
            $("#sourceId").empty();
            //添加子元素
            $("#sourceId").append("<option value=''>---请选择---</option>");
         <#if  mdList?exists &&mdList?size gt 0>
             <#list mdList as md>
                $("#sourceId").append("<option value='${md.id!}'<#if md.id! == flink.sourceId!>selected=\"selected\"</#if>>${md.name!?html}</option>");
             </#list>
         </#if>
        }
    }

    $(function () {
        changeSourceType();
		$('body').load(init());
		<#if flink.flowChartJson?exists && flink.flowChartJson?length gt 0>
			loadFlowChartJson();
		</#if>
    })

</script>