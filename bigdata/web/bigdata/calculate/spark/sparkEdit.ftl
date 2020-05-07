<#import "/fw/macro/webUploaderMacro.ftl" as upload />
<form id="submitForm" enctype='multipart/form-data' method="post">
    <input type="hidden" name="id" value="${spark.id!}">
    <input type="hidden" name="etlType" value="${spark.etlType!}">

	<div class="form-horizontal">
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>名称：</label>
			<div class="col-sm-6">
				<input type="text" name="name" id="name" class="width-1of1 form-control" nullable="false" maxLength="36" value="${spark.name!}">
			</div>
		</div>
		<div class="form-group">
			<@upload.fileUpload businessKey="spark" contextPath="${request.contextPath}" resourceUrl="${resourceUrl!}" extensions="jar" size="1" fileNumLimit="1" handler="loadFile">
	            <label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>jar包：</label>
	            <div class="col-sm-6">
		            <a class="file js-addFiles">上传文件</a>
		            <p class="js-file-content" id="showFileName">${spark.allFileNames!}</p>
					<input type="hidden" id="spark-path" value="">
                    <input type="hidden" name="path" id="path" value="${spark.path!}">
	            </div>
			</@upload.fileUpload>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>类名称：</label>
			<div class="col-sm-6">
			<input type="text" name="fileName" id="fileName" class="form-control" nullable="false" maxLength="36" value="${spark.fileName!}">
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right">文件路径：</label>
			<div class="col-sm-6">
				<label for="businessFile" class="file">添加</label>
                <p class="js-file-content" id="showName">${spark.businessFile!}</p>
                <input type="hidden" name="businessFileName" id="businessFileName" value="${spark.businessFile!}">
                <input style="height: 0px;display: none;" class="businessFile" type="file" id="businessFile" nullable="false" name="businessTempFile">
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>是否流处理：</label>
			<div class="col-sm-6">
				<label class="switch-label">
					<input <#if spark.runType?default(0) ==1>checked="true"</#if> class="wp wp-switch js-runType" type="checkbox">
					<span class="lbl"></span>
				</label>
				    <input type="hidden" id="runType" name="runType" value="${spark.runType?default(0)}">
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>是否定时执行：</label>
			<div class="col-sm-6">
				<label class="switch-label">
					<input <#if spark.isSchedule?default(0) ==1>checked="true"</#if> class="wp wp-switch js-isSchedule" type="checkbox">
					<span class="lbl"></span>
				</label>
				<input type="hidden" id="isSchedule" name="isSchedule" value="${spark.isSchedule?default(0)}">
			</div>
		</div>
		
		<div class="form-group form-shechule-param <#if spark.isSchedule?default(0) ==0>hidden</#if>" >
			<label class="col-sm-2 control-label no-padding-right">定时执行参数：</label>
			<div class="col-sm-6">
				<input type="text" name="scheduleParam" id="scheduleParam" class="form-control" nullable="true" maxLength="100" value="${spark.scheduleParam!}">
			</div>
			<button class="btn btn-blue" type="button" id="paramBtn">参数配置实例</button>
		</div>

		<div class="form-group form-shechule-has-param <#if spark.isSchedule?default(0) ==1>hidden</#if>" >
			<label class="col-sm-2 control-label no-padding-right">是否包含参数：</label>
			<div class="col-sm-6">
				<label class="switch-label">
					<input <#if spark.hasParam?default(0) ==1>checked="true"</#if> class="wp wp-switch js-hasParam" type="checkbox">
					<span class="lbl"></span>
				</label>
				   <input type="hidden" id="hasParam" name="hasParam" value="${spark.hasParam?default(0)}">
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-2 control-label no-padding-right">备注：</label>
			<div class="col-sm-6">
				<input type="text" name="remark" id="remark" class="form-control" nullable="true" maxLength="200" value="${spark.remark!}">
			</div>
		</div>

		<div class="form-group">
	        <label class="col-sm-2 control-label no-padding" for="form-field-1"></label>
	        <div class="col-sm-6" >
	            <button type="button" class="btn btn-long btn-blue js-added" id="sparkSaveBtn">&nbsp;保存&nbsp;</button>
	        </div>
	    </div>
</div>
<div id="paramDiv"></div>
</form>
<script>
    var isSubmit = false;

    $('.js-isSchedule').on('click', function () {
        if ($(this).prop('checked') === true) {
            $('.form-shechule-param').removeClass('hidden');
            $('.form-shechule-has-param').addClass('hidden');
            $("#isSchedule").val("1");
        } else {
            $('.form-shechule-param').addClass('hidden');
            $('.form-shechule-has-param').removeClass('hidden');
            $("#isSchedule").val("0");
        }
    });

    $('.js-runType').on('click', function () {
        if ($(this).prop('checked') === true) {
            $("#runType").val("1");
        } else {
            $("#runType").val("0");
        }
    })

    $(".file-parent").on("change", '.businessFile', function () {
        var urlArr = this.value.split("\\");
        if (this && this.files && this.files[0]) {
            var fileUrl = URL.createObjectURL(this.files[0]);
            var fileName= urlArr[urlArr.length - 1];
            $('#businessFileName').val(fileName);
            $("#showName").empty().text(fileName);
        }
    });

    $('.js-hasParam').on('click', function () {
        if ($(this).prop('checked') === true) {
            $("#hasParam").val("1");
        } else {
            $("#hasParam").val("0");
        }
    })

    $("#sparkSaveBtn").on("click", function () {
        if (isSubmit) {
            return;
        }
        isSubmit = true;
        var check = checkValue('#submitForm');
        if ($('#fileName').val() == "") {
            layer.tips("不能为空", "#fileName", {
                tipsMore: true,
                tips: 3
            });
            isSubmit = false;
            return;
        }
        if (!check) {
            isSubmit = false;
            return;
        }
        if ($('.js-isSchedule').prop('checked') === true) {
            if ($('#scheduleParam').val() == "") {
                layer.tips("不能为空", "#scheduleParam", {
                    tipsMore: true,
                    tips: 3
                });
                isSubmit = false;
                return;
            }
        }

        var options = {
            url: "${request.contextPath}/bigdata/calculate/spark/save",
            dataType: 'json',
            success: function (data) {
                if (!data.success) {
                     showLayerTips4Confirm('error',data.msg);
                    isSubmit = false;
                } else {
                    showLayerTips('success',data.msg,'t');
                    $("#contentDiv").load("${request.contextPath}/bigdata/calculate/realtime/list?calculateType=4");
                }
            },
            clearForm: false,
            resetForm: false,
            type: 'post',
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }//请求出错
        };
        $("input[name='file']").remove();
        $("#submitForm").ajaxSubmit(options);
    });

    $("#paramBtn").on("click", function () {
        var url = '${request.contextPath}/bigdata/etl/paramView';
        $("#paramDiv").load(url, function () {
            layer.open({
                type: 1,
                shade: .5,
                title: ['参数配置实例说明', 'font-size:16px'],
                area: ['900px', '600px'],
                maxmin: false,
                btn: ['确定'],
                content: $('#paramDiv'),
                resize: true,
                yes:function (index) {
	                layer.closeAll();
	                $("#paramDiv").empty();
	            },
	            cancel:function (index) {
	                layer.closeAll();
	                $("#paramDiv").empty();
	            }
            });
            $("#paramDiv").parent().css('overflow', 'auto');
        })
    });

    function loadFile() {
        if (hasUploadSuc) {
            $("#path").val($("#spark-path").val() + "/" + fileName);
            $("#showFileName").empty().text(fileName);
            fileName = "";
        }
    }
</script>