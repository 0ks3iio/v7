<div class="form-horizontal">
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right">科目：</label>
		<div class="col-sm-4 mt7 totalCount" total="${count!}">${course.subjectName!} (${count!}人)</div>
		<label class="col-sm-4 mt7 same-level"><input type="checkbox" class="wp" name="sameLevel" checked><span class="lbl"> 同分同层</span></label>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label no-padding-right">层数：</label>
		<div class="col-sm-10">
			<label class="stratify-num"><input type="radio" class="wp" name="stratifyCount" value="1"><span class="lbl"> 1层</span></label>
			<label class="stratify-num"><input type="radio" class="wp" name="stratifyCount" value="2" checked=""><span
						class="lbl"> 2层</span></label>
			<label class="stratify-num"><input type="radio" class="wp" name="stratifyCount" value="3"><span class="lbl"> 3层</span></label>
			<label class="stratify-num"><input type="radio" class="wp" name="stratifyCount" value="4"><span class="lbl"> 4层</span></label>
		</div>
	</div>
	<p class="color-yellow">
		<i class="fa fa-exclamation-circle"></i> 默认按照语数英与${course.subjectName!}成绩之和排名
	</p>
	<form id="myForm">
		<input id="sameLevelId" type="hidden" class="wp" name="sameLevel" value="1">
		<input id="range" name="range" value="2" type="hidden">
		<input id="subjectId" name="subjectId" value="${course.id!}" type="hidden">
		<table class="table table-striped table-bordered table-hover no-margin">
			<tbody id="stratify-layer">
			<tr>
				<td class="stratify-tr stratify_0">
					<input type="hidden" name="exList[0].range" value="A">
					A层：<span class="inline-block w40 stratify-number-start">1</span> - <input type="text" id="aLevel"
																							  name="exList[0].stuNum"
																							  nullable="false"
																							  vtype="int" min="0"
																							  class="form-control inline-block stratify-number number">
					名，共 <strong class="color-blue total">0</strong> 人
				</td>
			</tr>
			<tr>
				<td class="stratify-tr stratify_1 final">
					<input type="hidden" name="exList[1].range" value="B">
					B层：<span class="inline-block w40 stratify-number-start">1</span> - <input type="text" id="bLevel"
																							  name="exList[1].stuNum"
																							  nullable="false"
																							  vtype="int" min="0"
																							  class="form-control inline-block stratify-number number" value="${count!}">
					名，共 <strong class="color-blue total">${count!}</strong> 人
				</td>
			</tr>
			<tr>
				<td class="stratify-tr stratify_2" style="display: none">
					<input type="hidden" name="exList[2].range" value="C" disabled="">
					C层：<span class="inline-block w40 stratify-number-start">1</span> - <input type="text" id="cLevel"
																							  name="exList[2].stuNum"
																							  nullable="false"
																							  vtype="int" min="0"
																							  class="form-control inline-block stratify-number number"
																							  disabled="">
					名，共 <strong class="color-blue total">0</strong> 人
				</td>
			</tr>
			<tr>
				<td class="stratify-tr stratify_3" style="display: none">
					<input type="hidden" name="exList[3].range" value="D" disabled="">
					D层：<span class="inline-block w40 stratify-number-start">1</span> - <input type="text" id="dLevel"
																							  name="exList[3].stuNum"
																							  nullable="false"
																							  vtype="int" min="0"
																							  class="form-control inline-block stratify-number number"
																							  disabled="">
					名，共 <strong class="color-blue total">0</strong>人
				</td>
			</tr>
			</tbody>
		</table>
	</form>
</div>

<div class="layer-footer" style="vertical-align: middle;border-top: 1px solid #eee;">
	<button class="btn btn-lightblue" id="quick3-commit">确定</button>
	<button class="btn btn-grey" id="quick3-close">取消</button>
</div>

<script>
	var isSubmit = false;
	var rangeEdgeObj = null;

	$(function () {
		$(".same-level").on("change", function () {
			if ($(this).find("input").prop("checked")) {
				$("#sameLevelId").val("1");
			} else {
				$("#sameLevelId").val("0");
			}
			$(".stratify-number-start").text("1");
			$(".stratify-number").val("");
			$(".total").text("0");
			$(".final .stratify-number").val("${count!}");
			$(".final .total").text("${count!}");

		});

		$(".stratify-number").on("focus", function () {
			if (rangeEdgeObj) {
				$(this).blur();
				layer.msg("正在统计同分同层", {
					offset: 't',
					time: 1000
				});
			}
		});

		$(".stratify-number").on("blur", function () {
			if ($(this).parents(".stratify-tr").hasClass("final")) {
				$(this).val("${count!}");
				return;
			}
			if (parseInt($(this).val()) && parseInt($(this).val()) < ${count!} && parseInt($(this).val()) >= parseInt($(this).attr("min"))) {
				$(this).parents("tr").find(".total").text(parseInt($(this).val()) - parseInt($(this).prev().text()) + 1);
				$(this).parents("tr").next().find(".stratify-number-start").text(parseInt($(this).val()) + 1);
				$(this).parents("tr").next().find(".stratify-number").attr("min", parseInt($(this).val()) + 1);
				if (parseInt($(this).parents("tr").next().find(".stratify-number").val())) {
					$(this).parents("tr").next().find(".total").text(parseInt($(this).parents("tr").next().find(".stratify-number").val()) - parseInt($(this).val()))
				}
				if ($("#sameLevelId").val() == "1") {
					rangeEdgeObj = $(this).parents("tr").find(".total");
                    $.ajax({
                        url: "${request.contextPath}/newgkelective/${divideId!}/divideClass/singleRecomb/quickStratifyRangeEdge",
                        dataType: "json",
                        data: {
                        	"subjectId": "${course.id}",
                            "edge": $(this).val()
                        },
                        success: function(data){
                            if(data.success){
                            	var head = parseInt(rangeEdgeObj.prev().prev().text());
                            	var tail = parseInt(data.businessValue);
								rangeEdgeObj.text(tail - head + 1);
								rangeEdgeObj.parents("tr").next().find(".stratify-number-start").text(tail + 1);
								rangeEdgeObj.parents("tr").next().find(".stratify-number").attr("min", tail + 1);
								if (parseInt(rangeEdgeObj.parents("tr").next().find(".stratify-number").val())) {
									rangeEdgeObj.parents("tr").next().find(".total").text(parseInt(rangeEdgeObj.parents("tr").next().find(".stratify-number").val()) - tail)
								}
                            }
                            rangeEdgeObj = null;
                        },
						error: function () {
							rangeEdgeObj = null;
						}
                    });
                }
			} else {
				$(this).val("");
			}
		});

		$(".stratify-num").on("click", function () {
			var tmp = $(this).find("input").val();
			$("#range").val(tmp);
			var tmpInt = parseInt(tmp);
			for (var i = 0; i < 4; i++) {
				$(".stratify_" + i).removeClass("final");
				if (i < tmpInt) {
					$(".stratify_" + i).show();
					$(".stratify_" + i).find('input').removeAttr("disabled");
					$(".stratify_" + i).find(".stratify-number").val("");
					$(".stratify_" + i).find(".stratify-number-head").text("1");
					$(".stratify_" + i).find(".total").text("0");
					if (i == tmpInt - 1) {
						$(".stratify_" + i).addClass("final");
						$(".stratify_" + i).find(".stratify-number").val($(".totalCount").attr("total"));
						$(".stratify_" + i).find(".total").text('${count!}');
					}
				} else {
					$(".stratify_" + i).hide();
					$(".stratify_" + i).find('input').attr("disabled", "");
				}
			}
		});

		$("#quick3-close").on("click", function () {
			doLayerOk("#quick3-commit", {
				redirect: function () {
				},
				window: function () {
					layer.closeAll();
				}
			});
		});

		$("#quick3-commit").off('click').on("click", function () {
			if(isSubmit){
				return false;
			}
			isSubmit=true;
			$(this).addClass("disabled");
			var check = checkValue('#myForm');
			if (!check) {
				isSubmit = false;
				$(this).removeClass("disabled");
				return;
			}
			// 提交数据
			var options = {
				url : '${request.contextPath}/newgkelective/${divideId!}/divideClass/singleRecomb/saveQuickStratify',
				dataType : 'json',
				success : function(data){
					if(data.success){
						// 休眠1秒
						var start = (new Date()).getTime();
						while ((new Date()).getTime() - start < 1000) {
							continue;
						}
						layer.closeAll();
						layer.msg("保存成功！", {
							offset: 't',
							time: 2000
						});
						refreshThis();
					}
					else{
						layerTipMsg(data.success,"失败",data.msg);
						$("#quick3-commit").removeClass("disabled");
						isSubmit=false;
					}
				},
				clearForm : false,
				resetForm : false,
				type : 'get',
				error:function(XMLHttpRequest, textStatus, errorThrown){}
			};
			$("#myForm").ajaxSubmit(options);
		});
	});
</script>
			