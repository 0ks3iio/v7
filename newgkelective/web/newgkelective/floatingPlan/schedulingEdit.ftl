<form id="myform">
    <input type="hidden" name="batch" id="batch" value="${batch!}"/>
    <input type="hidden" name="subjectType" id="subjectType" value="${subjectType!}"/>
    <input type="hidden" name="subjectIds" value="${subjectIds!}"/>
    <input type="hidden" name="stuIdStr" id="stuIdStr" value="${stuIdStr!}"/>
    <input type="hidden" name="classType" id="classType" value="2"/>
    <div class="row schedulingDetail filter" style="height:113px">
        <div class="filter-item block" style="margin-top: 28px; margin-left: 35px">
            <span class="filter-name">班级名称：</span>
            <div class="filter-content">
                <input type="text" class="form-control" nullable="false" name="className" id="className"
                       value="${className!}" maxlength="20">
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
		var check = checkValue('.schedulingDetail');
		if (!check) {
			$(this).removeClass("disabled");
			isSubmit = false;
			return;
		}
		stuIdStr = "";
		var appendUrl = "";
        if ($("#combinationId").hasClass("active") && $("#leftTableId").find('input:checkbox[name=studentIdName]:checked').size() > 0) {
            $("#leftTableId").find('input:checkbox[name=studentIdName]:checked').each(function (i) {
                stuIdStr = stuIdStr + "," + $(this).val();
            });
            stuIdStr = stuIdStr.substring(1);
            appendUrl = '?stuIdStr=' + stuIdStr + '&relateId=' + $(combinationObj).attr("combinationClassId");
            // console.log(appendUrl);
        }
		var options = {
			url: '${request.contextPath}/newgkelective/${divideId!}/floatingPlan/saveClass' + appendUrl,
			dataType: 'json',
			success: function (data) {
				if (data.success) {
                    if ($("#independentId").hasClass("active")) {
                        layer.closeAll();
                        var first = false;
                        var htmlContentTmp = '<option value="' + data.id + '">' + data.className + '(0)</option>';
                        if ($("#teachClassId").val() == "emptyTeachClass") {
                            $("#teachClassId").empty();
                            if ($("#leftTableId").find('input:checkbox[name=studentIdName]:checked').size() > 0) {
                                leftToRight();
                                first = true;
                            }
                        } else {
                            layer.msg("独立教学班开设完成！", {
                                offset: 't',
                                time: 2000
                            });
                        }
                        $("#teachClassId").append(htmlContentTmp);
                        classIdToStudentIdsArr[data.id] = [];
                        if (first) {
                            saveTeachClassStu();
                        }
                    } else {
                        layer.closeAll();
                        layer.msg("合班教学班开设完成，所选学生已纳入该班级！", {
                            offset: 't',
                            time: 2000
                        });

                        refeshTeachClassStudentSet();
                    }
				} else {
                    layer.msg(data.msg, {
                        offset: 't',
                        time: 2000
                    });
				}
                isSubmit = false;
			},
			clearForm: false,
			resetForm: false,
			type: 'post',
			error: function (XMLHttpRequest, textStatus, errorThrown) {}
		};
		$("#myform").ajaxSubmit(options);
	});
</script>

