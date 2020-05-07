<div class="layer-content">
	<div class="form-horizontal">
		<form id="myForm">
			<input type="hidden" name="subjectIds" value="${subjectIds!}">
			<input type="hidden" name="subjectIdsB" value="${subjectIdsB!}">
			<input type="hidden" name="subjectType" value="${subjectType!}">
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">2科组合：</label>
				<div class="col-sm-3 mt7">${groupName!}</div>
				<label class="col-sm-3 control-label no-padding-right">总人数：</label>
				<div class="col-sm-3 mt7">${allSize?default(0)}</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">未安排人数：</label>
				<div class="col-sm-3 mt7" id="noArrange">${arrangeSize?default(0)}</div>
				<label class="col-sm-3 control-label no-padding-right">安排人数：</label>
				<div class="col-sm-3">
					<div class="input-group form-num" data-step="1" >
	                    <input class="form-control" type="text" name="arrangeStudentNum"  id="arrangeStudentNum"  maxlength="4" value="${arrangeSize?default(0)}" onblur="checkInt('arrangeStudentNum')">
	                    <span class="input-group-btn">
	                        <button class="btn btn-default form-number-add" type="button">
	                        	<i class="fa fa-angle-up"></i>
	                        </button>
	                        <button class="btn btn-default form-number-sub" type="button">
	                        	<i class="fa fa-angle-down"></i>
	                        </button>
	                    </span>
	                </div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">每班人数：</label>
				<div class="col-sm-3 mt7" id="classStudentNum">${avg?default(50)}</div>
				<label class="col-sm-3 control-label no-padding-right">开班数：</label>
				<div class="col-sm-3">
					<div class="input-group form-num" data-step="1" >
	                    <input class="form-control" type="text" name="classNum" id="classNum" maxlength="3" value="${defaultClassNum?default(0)}" onblur="checkInt('classNum')">
	                    <span class="input-group-btn">
	                        <button class="btn btn-default form-number-add" type="button">
	                        	<i class="fa fa-angle-up"></i>
	                        </button>
	                        <button class="btn btn-default form-number-sub" type="button">
	                        	<i class="fa fa-angle-down"></i>
	                        </button>
	                    </span>
	                </div>
				</div>
				
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">学生分发依据：</label>
				<div class="col-sm-9">
					<select class="form-control" name="openBasis" id="openBasis" onChange="changeOpenBasis()">
						<option value="2">按该选科与语数英成绩之和排名</option>
						<option value="1">按该选科成绩排名</option>
						<option value="0">无依据</option>
					</select>
				</div>
			</form>
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">学生分发方式：</label>
				<div class="col-sm-9">
					<label><input type="radio" class="wp" name="basisType"  value="1"><span class="lbl"> 按顺序分发</span></label>
					<label><input type="radio" class="wp" name="basisType" value="2"><span class="lbl"> 按顺序交叉分发</span></label>
					<label><input type="radio" class="wp" name="basisType" value="0"><span class="lbl"> 随机</span></label>
				</div>
			</div>
		</form>
	</div>
</div>
<div class="layer-footer" style="vertical-align: middle;border-top: 1px solid #eee;">
	<button class="btn btn-lightblue" id="quick3-commit">确定</button>
	<button class="btn btn-grey" id="quick3-close">取消</button>
</div>
<script>
	var isSubmit=false;
	$(function(){
		changeOpenBasis();
		
		$("#quick3-close").on("click", function(){
			doLayerOk("#quick3-commit", {
				redirect:function(){},
				window:function(){layer.closeAll()}
			});
		 });
		$("#quick3-commit").off('click').on("click",function(){
			if(isSubmit){
				return false;
			}
			isSubmit=true;
			$(this).addClass("disabled");
			if(!checkSubmit()){
				$(this).removeClass("disabled");
		 		isSubmit=false;
		 		return false;
			}
			// 提交数据
			var options = {
				url : '${request.contextPath}/newgkelective/${divideId!}/divideClass/singleRecomb/saveQuickOpen',
				dataType : 'json',
				success : function(data){
			 		if(data.success){
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
				type : 'post',
				error:function(XMLHttpRequest, textStatus, errorThrown){} 
			};
			$("#myForm").ajaxSubmit(options);
		});
		
		//数字增加以及减少
		$("#myForm").on('click','.form-num >span > button',function(e){
			e.preventDefault();
			var $num = $(this).parent().siblings('.form-control');
			var val = $num.val();
			if (!val ) val = 0;
			if (!/^\d+$/.test(val)){
				val=0;
			}
			var num = parseInt(val);
			var step = $num.parent('.form-num').attr('data-step');
			if (step === undefined) {
				step = 1;
			} else{
				step = parseInt(step);
			}
			if ($(this).hasClass('form-number-add')) {
				num += step;
			} else{
				num -= step;
				if (num <= 0) num = 1;
			}
			$num.val(num);
			changeAvg();
		});
		
	})
	function changeOpenBasis(){
		var openBasis=$("#openBasis").val();
		if(openBasis=="0"){
			$("input:radio[value='0']").prop('checked',true);
			$("input:radio[value='0']").attr("disabled",false);
			$("input:radio[value='1']").attr("disabled",true);
			$("input:radio[value='2']").attr("disabled",true);
		}else{
			if(!$("input:radio[value='1']").is(':checked')){
				$("input:radio[value='2']").prop('checked',true);
			}else{
				$("input:radio[value='1']").prop('checked',true);
			}
			$("input:radio[value='0']").attr("disabled",true);
			$("input:radio[value='2']").attr("disabled",false);
			$("input:radio[value='1']").attr("disabled",false);
		}
	}
	function checkSubmit(){
		if(!checkInt("arrangeStudentNum")){
		 	return false;
		}
		if(!checkInt("classNum")){
			return false;
		}
		var allNum=$.trim($("#noArrange").html());
		var allNumInt=parseInt(allNum);
		var studentNum=parseInt($("#arrangeStudentNum").val());
		if(studentNum>allNumInt){
			layer.tips('安排人数不能超过未安排人数', $("#arrangeStudentNum"), {
				tipsMore: true,
				tips:3				
			});
		 	return false;
		}
		changeAvg();
		return true;
	}
	
	function checkInt(objKey){
		var item=$.trim($("#"+objKey).val());
		if (!/^\d+$/.test(item)) {
			layer.tips('请输入正整数', $("#"+objKey), {
				tipsMore: true,
				tips:3				
			});
			return false;
		}
		if(parseInt(item)>0){
			$("#"+objKey).val(item);
			changeAvg();
			return true;
		}else{
			layer.tips('请输入正整数', $("#"+objKey), {
				tipsMore: true,
				tips:3				
			});
			return false;
		}
	}
	
	function changeAvg(){
		var classNum= $("#classNum").val();
		var arrangeNum=$("#arrangeStudentNum").val();
		var avg=parseInt((parseInt(arrangeNum)-1)/parseInt(classNum)+1);
		$("#classStudentNum").html(avg);
	}
	
	
</script>