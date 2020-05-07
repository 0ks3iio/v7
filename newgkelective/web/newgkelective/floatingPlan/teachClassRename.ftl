<form id="myform">
    <input type="hidden" name="subjectType" id="subjectType" value="${subjectType!}"/>
    <input type="hidden" name="teachClassId" id="teachClassId" value="${teachClassId!}"/>
    <input type="hidden" name="originClassName" id="originClassName" value="${teachClassName!}"/>
    <div class="row schedulingDetail filter">
        <div class="filter-item block" style="margin-left: 35px;">
            <span class="filter-name">班级名称：</span>
            <div class="filter-content">
                <input type="text" class="form-control" nullable="false" name="teachClassName" id="teachClassName"
                       value="${teachClassName!}" maxlength="20">
            </div>
        </div>
    </div>
</form>
<div class="layui-layer-btn">
    <a class="layui-layer-btn0" id="scheduling-commit">确定</a>
    <a class="layui-layer-btn1" id="scheduling-close">取消</a>
</div>

<script src="${request.contextPath}/static/ace/js/excanvas.js"></script>
<script type="text/javascript">
	$(function () {
		/*if (chooseStuIdsToAdd && chooseStuIdsToAdd != "") {
			$("#stuIdStr").val(chooseStuIdsToAdd);
		}
		chooseStuIdsToAdd = "";*/
	});

	// 取消按钮操作功能
	$("#scheduling-close").on("click", function () {
		doLayerOk("#scheduling-commit", {
			redirect: function () {
			},
			window: function () {
				layer.closeAll()
			}
		});
	});

	// 确定按钮操作功能
	var isSubmit = false;
	$("#scheduling-commit").on("click", function () {
		if (isSubmit) {
			return;
		}
        isSubmit = true;
		$(this).addClass("disabled");
		var options = {
			url: '${request.contextPath}/newgkelective/${divideId!}/floatingPlan/renameTeachClassSave',
			dataType: 'json',
			success: function (data) {
                if (data.success) {
                    refreshTeachClassSet();
                    layer.msg("教学班名称已修改", {
                        offset: 't',
                        time: 2000
                    });
                } else {
					layerTipMsg(data.success, "失败", data.msg);
					$("#scheduling-commit").removeClass("disabled");
					isSubmit = false;
				}
			},
			clearForm: false,
			resetForm: false,
			type: 'post',
			error: function (XMLHttpRequest, textStatus, errorThrown) {}
		};
		$("#myform").ajaxSubmit(options);
	});
</script>

