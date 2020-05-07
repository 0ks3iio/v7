<#import "/bigdata/v3/templates/webUploaderMacro.ftl" as upload />
<form id="submitForm">
<input type="hidden" name="id" value="${kettle.id!}">
<input type="hidden" name="etlType" value="${kettle.etlType!}">
<input type="hidden" name="targetType" value="table">
    <div class="form-horizontal">
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>名称：</label>
			<div class="col-sm-6">
				<input type="text" name="name" id="name" class="width-1of1 form-control" nullable="false" maxLength="36" value="${kettle.name!}">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>类型：</label>
			<div class="col-sm-6">
				<select name="jobType" id="jobType" class="form-control">
					${mcodeSetting.getMcodeSelect("DM-KETTLE-JOB","${kettle.jobType!}","0")}
				</select>
			</div>
		</div>

        <div class="form-group">
            <label class="col-sm-2 control-label no-padding-right">数据来源类型：</label>
            <div class="col-sm-6">
                <select id="sourceType" class="form-control" name="sourceType" onchange="changeSourceType()">
                    <option value="table" <#if kettle.sourceType?default('table') == 'table'>selected="selected"</#if>>
                        表
                    </option>
                    <option value="app" <#if kettle.sourceType?default('table') == 'app'>selected="selected"</#if>>
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
                <select id="targetId" class="form-control" nullable="true" name="targetId">
                    <option value="">---请选择---</option>
                    <#list mdList as md>
                        <option value="${md.id!}" <#if md.id! == kettle.targetId!>selected="selected"</#if>>${md.name!}</option>
                    </#list>
                </select>
            </div>
        </div>
		
		<div class="form-group">
			<@upload.fileUpload businessKey="kettle" contextPath="${request.contextPath}" resourceUrl="${resourceUrl!}" size="10" fileNumLimit="10" handler="loadFile">
	            <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>调度文件：</label>
	            <div class="col-sm-6">
		            <a class="file js-addFiles">上传文件</a>
		            <p class="js-file-content" id="showFileName">${kettle.allFileNames!}</p>
					<input type="hidden" id="kettle-path" value="">
					<input type="hidden" name="path" id="path" value="${kettle.path!}">
					<input type="hidden" name="allFileNames" id="allFileNames" value="${kettle.allFileNames!}">
	            </div>
			</@upload.fileUpload>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>主任务名称：</label>
			<div class="col-sm-6">
			<input type="text" name="fileName" id="fileName" class="form-control" nullable="false" maxLength="100" value="${kettle.fileName!}">
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>是否定时执行：</label>
			<div class="col-sm-6">
				<label class="switch-label">
					<input <#if kettle.isSchedule?default(0) ==1>checked="true"</#if> class="wp wp-switch js-isSchedule" type="checkbox">
					<span class="lbl"></span>
				</label>
				<input type="hidden" id="isSchedule" name="isSchedule" value="${kettle.isSchedule?default(0)}">
			</div>
		</div>
		
		<div class="form-group form-shechule-param <#if kettle.isSchedule?default(0) ==0>hidden</#if>" >
			<label class="col-sm-2 control-label no-padding-right">定时执行参数：</label>
			<div class="col-sm-6">
				<input type="text" name="scheduleParam" id="scheduleParam" class="form-control" nullable="true" maxLength="100" value="${kettle.scheduleParam!}">
			</div>
			<button class="btn btn-blue" type="button" id="paramBtn">参数配置实例</button>
		</div>
		
		<div class="form-group form-shechule-has-param <#if kettle.isSchedule?default(0) ==1>hidden</#if>" >
			<label class="col-sm-2 control-label no-padding-right">是否包含参数：</label>
			<div class="col-sm-6">
				<label class="switch-label">
					<input <#if kettle.hasParam?default(0) ==1>checked="true"</#if> class="wp wp-switch js-hasParam" type="checkbox">
					<span class="lbl"></span>
				</label>
				<input type="hidden" id="hasParam" name="hasParam" value="${kettle.hasParam?default(0)}">
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right">备注：</label>
			<div class="col-sm-6">
				<input type="text" name="remark" id="remark" class="form-control" nullable="true" maxLength="200" value="${kettle.remark!}">
			</div>
		</div>

		<div class="form-group" style="display: none">
			<label class="col-sm-2 control-label no-padding-right">flowChartJson：</label>
			<div class="col-sm-6">
				<textarea id="flowChartJson" name="flowChartJson">${kettle.flowChartJson!}</textarea>
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-2 control-label">流程图：</label>
			<div class="col-sm-8">
				<div style="width: 100%; display: flex; justify-content: space-between">
					<div id="myPaletteDiv" style="width: 105px; margin-right: 2px; background-color: whitesmoke; border: solid 1px black"></div>
					<div id="myDiagramDiv" style="flex-grow: 1; height: 500px; border: solid 1px black"></div>
				</div>
			</div>
		</div>

		<div class="form-group">
	        <label class="col-sm-2 control-label no-padding" for="form-field-1"></label>
	        <div class="col-sm-6" >
	            <button type="button" class="btn btn-long btn-blue js-added" id="kettleSaveBtn">&nbsp;保存&nbsp;</button>
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

$("#paramBtn").on("click",function(){
	var url =  '${request.contextPath}/bigdata/etl/paramView';
	 $("#paramDiv").load(url,function(){
        layer.open({
            type: 1,
            shade: .5,
            title: ['参数配置实例说明','font-size:16px'],
            area: ['900px','600px'],
            maxmin: false,
            btn:['确定'],
            content: $('#paramDiv'),
            resize:true,
            yes:function (index) {
                layer.closeAll();
                $("#paramDiv").empty();
            },
            cancel:function (index) {
                layer.closeAll();
                $("#paramDiv").empty();
            }
        });
        $("#paramDiv").parent().css('overflow','auto');
    })
});

function loadFile(){
	if(hasUploadSuc){
		$("#path").val($("#kettle-path").val());
		$("#showFileName").text(fileName);
		$("#fileName").val(fileName);
		$("#allFileNames").val(fileName);
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
                $("#sourceId").append("<option value='${app.id!}'<#if app.id! == kettle.sourceId!>selected=\"selected\"</#if>>${app.name!}</option>");
             </#list>
         </#if>
    } else {
        $("#sourceId").empty();
        //添加子元素
        $("#sourceId").append("<option value=''>---请选择---</option>");
         <#if  mdList?exists &&mdList?size gt 0>
             <#list mdList as md>
                $("#sourceId").append("<option value='${md.id!}'<#if md.id! == kettle.sourceId!>selected=\"selected\"</#if>>${md.name!}</option>");
             </#list>
         </#if>
    }
}

$(function () {
    changeSourceType();
	$('body').load(init());
	<#if kettle.flowChartJson?exists && kettle.flowChartJson?length gt 0>
		loadFlowChartJson();
	</#if>
})

$("#kettleSaveBtn").on("click",function(){
	if(isSubmit){
		return;
	}
	isSubmit = true;
	var check = checkValue('#submitForm');
	if(!check){
		isSubmit=false;
		return;
	}

	if ($("#showFileName").html() == "") {
		layer.tips("不能为空", "#showFileName", {
			tipsMore: true,
			tips:3
		});
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
		url : "${request.contextPath}/bigdata/etl/kettle/save",
		dataType : 'json',
		success : function(data){
			if(!data.success){
				showLayerTips4Confirm('error', data.msg);
				isSubmit = false;
			}else{
				showLayerTips('success', data.msg, 't');
				showList('1')
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
	};
	$("#submitForm").ajaxSubmit(options);
});
</script>